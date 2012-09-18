package com.mokasocial.iheart.lib.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

/**
 * <p>
 * All our image management stuff, memory (& file) caching, etc. All the
 * threading and background work is handled within this class. Simply execute
 * and move on. Eventually we can add more to make it more flexible like adding
 * different file extension support, sub-directory support, etc.
 * </p>
 * 
 * <p>
 * The caching works in this order:
 * </p>
 * <ol>
 * <li>Memory cache</li>
 * <li>File system cache</li>
 * <li>Fetch from url</li>
 * </ol>
 * 
 * <h2>Usage</h2>
 * <p>
 * Then instantiate it using the path you want to use to save your images to. At
 * this time the files use {@link #getSHA1HashOfString(String)} to determine
 * what the file name will be. Also, we currently default to .jpg file
 * extensions. This is pretty much fire and forget.
 * </p>
 * 
 * <pre>
 * ImageMgmt mImageMgmt = ImageMgmt.getInstance(ActivityTabs.IMAGE_CACHE_PATH);
 * mImageMgmt.fetchCachedImage(String, ImageView);
 * </pre>
 * 
 * 
 * @author mokasocial
 * 
 */
public class ImageMgmt {

	private static final String TAG = "ImageMgmt";

	private static final int WALLPAPER_SCREEN_SIZE = 3;
	private static final String FILE_EXTENSION = ".jpg";
	private static final String STREAM_NAME = "getImage";
	private static Map<String, SoftReference<Drawable>> mDrawableMap;
	private static ImageMgmt mInstance;

	/**
	 * Manage concurrent tasks. When an async request is made, add it to this
	 * queue. It will then collect tasks and then execute them as room becomes
	 * available. AsyncTask has its own execution pool (5) - this is a work
	 * around for that limitation.
	 */
	private LinkedBlockingQueue<LoadRemoteImage> mTaskQueue = new LinkedBlockingQueue<LoadRemoteImage>();

	private String mBaseCachePath;
	private boolean mCcacheFiles = false;

	private ImageMgmt(String baseCachePath) {
		mDrawableMap = new HashMap<String, SoftReference<Drawable>>();
		mBaseCachePath = baseCachePath;
		Log.i(TAG, TAG + " new instance. Base dir: " + baseCachePath);
	}

	/**
	 * The entry point to our singleton.
	 * 
	 * @return
	 */
	public static ImageMgmt getInstance(String baseCachePath) {
		synchronized (ImageMgmt.class) {
			if (mInstance == null) {
				mInstance = new ImageMgmt(baseCachePath);
			}
		}

		return mInstance;
	}

	/**
	 * Get the size of the execution queue.
	 * 
	 * @return
	 */
	public int queueSize() {
		return mTaskQueue.size();
	}

	/**
	 * Add a task to the queue. The first will immediately execute. Subsequent
	 * additions will execute up until a RegectedExecutionException is thrown.
	 * 
	 * @param task
	 */
	private void addTask(LoadRemoteImage task) {
		Log.i(TAG, "Image queue size: " + queueSize());
		try {
			task.execute();
			while (!mTaskQueue.isEmpty()) {
				task = mTaskQueue.remove();
				if (task.getStatus() == AsyncTask.Status.PENDING) {
					task.execute();
					continue;
				}

				// If it is already running, remove it
				Log.w(TAG, "Removing finished or running task");
				mTaskQueue.remove();
			}
		} catch (RejectedExecutionException r) {
			Log.i(TAG, "RejectedExecutionException Exception - Adding task to queue");
			if (!mTaskQueue.contains(task)) {
				mTaskQueue.add(task);
			}
		} catch (Exception e) {
			Log.e(TAG, "Something else went wrong. Clearing queue", e);
			mTaskQueue.clear();
		}
	}

	/**
	 * Check the memory hash table first, if not present use the async task to
	 * fetch the image and place it into the cache.
	 * 
	 * @param url
	 * @param imageView
	 */
	public void fetchCachedImage(URL url, ImageView imageView) {
		final String urlString = url.toString();

		Log.i(TAG, "Request image url: " + urlString);

		if (mDrawableMap.containsKey(urlString)) {
			SoftReference<Drawable> softReference = mDrawableMap.get(urlString);

			// Watch out for recycled objects
			Drawable temp = softReference.get();
			if (temp != null) {
				imageView.setImageDrawable(temp);
				Log.i(TAG, "Using image from map");
				return;
			}
		}

		if (mCcacheFiles == true && loadImage(url, imageView) == true) {
			Log.i(TAG, "Using image from local disc");
			return;
		}

		Log.i(TAG, "Using image from async fetch");
		fetchAsyncRemoteImage(url, imageView);
	}

	/**
	 * Set an image into the local memory hash table.
	 * 
	 * @param urlString
	 * @param image
	 */
	private void setImage(URL url, Drawable image) {
		mDrawableMap.put(url.toString(), new SoftReference<Drawable>(image));
	}

	/**
	 * 
	 * 
	 * @param url
	 * @param imageView
	 */
	public void fetchAsyncRemoteImage(URL url, ImageView imageView) {
		addTask(new LoadRemoteImage(url, imageView));
	}

	/**
	 * 
	 * 
	 * @param url
	 * @param streamName
	 * @return
	 */
	private Drawable getImageFromUrl(URL url, String streamName) {

		InputStream iostream;
		Drawable imageDrawable;
		try {
			iostream = new BufferedInputStream((java.io.InputStream) url.getContent(), 4096);
			imageDrawable = Drawable.createFromStream(iostream, streamName);
			iostream.close();
		} catch (Exception e) {
			Log.e(TAG, "Unable to getImageFromUrl", e);
			return null;
		}

		return imageDrawable;
	}

	/**
	 * Get a SHA1 hash of the given string. This simplifies the image caching
	 * file structure.
	 * 
	 * @todo Verify collision rate
	 * 
	 * @param input
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String getSHA1HashOfString(String input) throws NoSuchAlgorithmException {
		MessageDigest sha1 = MessageDigest.getInstance("sha1");
		byte[] hashValue = sha1.digest(input.getBytes());

		String result = new String();
		for (int i = 0; i < hashValue.length; i++) {
			String hex = Integer.toHexString(hashValue[i]);
			if (hex.length() == 1)
				hex = "0" + hex;
			hex = hex.substring(hex.length() - 2);
			result = result.concat(hex);
		}

		return result;
	}

	/**
	 * Save the drawable as a file on the local disc.
	 * 
	 * @see mBaseCachePath
	 * @param url
	 * @param image
	 */
	public void saveImage(URL url, Drawable image) {
		try {
			String fileName = getFileName(url);
			Log.d(TAG, "Saving image as: " + fileName);
			File directory = new File(mBaseCachePath);

			// Check to see if the directory exists
			directory.mkdirs();

			// Check to see if the file exists
			File imageFile = new File(directory.getPath(), fileName);
			if (!imageFile.exists()) {
				imageFile.createNewFile();
			}

			// Write file contents to the 'handle'.
			FileOutputStream out = new FileOutputStream(imageFile);
			Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			Log.e(TAG, "Unable to save image locally", e);
		}
	}

	/**
	 * Get the save file path based on the file path provided in construction.
	 * 
	 * @param url
	 * @return
	 */
	public String getSavedImagePath(URL url) {
		return new File(mBaseCachePath).getPath() + File.separator + getFileName(url);
	}

	/**
	 * Load an image from the local file system.
	 * 
	 * @see mBaseCachePath
	 * @param url
	 * @param imageView
	 * @return
	 */
	private boolean loadImage(URL url, ImageView imageView) {
		try {
			String fileName = getFileName(url);
			Log.d(TAG, "Loading image as: " + fileName);
			Drawable image = Drawable.createFromPath(mBaseCachePath + File.separator + fileName);

			if (image == null) {
				return false;
			}

			imageView.setImageDrawable(image);
			setImage(url, image);
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Unable to load local image", e);
		}

		return false;
	}

	/**
	 * Visible file loading helper
	 * 
	 * @param url
	 * @return
	 */
	public Drawable loadImage(URL url) {
		String fileName = getFileName(url);
		Log.d(TAG, "Loading image as: " + fileName);
		return Drawable.createFromPath(mBaseCachePath + File.separator + fileName);
	}

	/**
	 * Wrapper for creating file names from URLs
	 * 
	 * @param url
	 * @return
	 */
	private String getFileName(URL url) {
		try {
			String preSaveString = "";
			if (isCacheFiles()) {
				preSaveString = ".";
			}
			return preSaveString + getSHA1HashOfString(url.toString()) + FILE_EXTENSION;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * This small class subclasses Async which handles all the background
	 * processing. The "thread" is inside doInBackground and it calls
	 * onPostExecute with the appropriate params.
	 * 
	 */
	private class LoadRemoteImage extends AsyncTask<String, Void, Drawable> {

		private final ImageView lImageView;
		private final URL lUrl;

		public LoadRemoteImage(URL url, ImageView imageView) {
			lImageView = imageView;
			lUrl = url;
		}

		@Override
		protected Drawable doInBackground(String... params) {
			return getImageFromUrl(lUrl, STREAM_NAME);
		}

		@Override
		protected void onPostExecute(Drawable result) {
			lImageView.setImageDrawable(result);
			setImage(lUrl, result);
			if (mCcacheFiles == true) {
				saveImage(lUrl, result);
			}
		}
	}

	/**
	 * Set whether or not to cache files to a directory
	 * 
	 * @see mBaseCachePath
	 * @param cacheFiles
	 */
	public void setCacheFiles(boolean cacheFiles) {
		mCcacheFiles = cacheFiles;
	}

	/**
	 * Should we cache files?
	 * 
	 * @return
	 */
	public boolean isCacheFiles() {
		return mCcacheFiles;
	}

	/**
	 * Combine two images together into one Bitmap. Optionally add a third param
	 * to return image location, if wanting to save the Bitmap.
	 * 
	 * @param Bitmap
	 *            top
	 * @param Bitmap
	 *            bottom
	 * @return
	 */
	public static Bitmap combineImages(Bitmap c, Bitmap s) {
		Bitmap cs = null;

		int width, height = 0;

		if (c.getWidth() > s.getWidth()) {
			width = c.getWidth();
			height = c.getHeight() + s.getHeight();
		} else {
			width = s.getWidth();
			height = c.getHeight() + s.getHeight();
		}

		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Canvas comboImage = new Canvas(cs);

		comboImage.drawBitmap(c, 0f, 0f, null);
		comboImage.drawBitmap(s, 0f, c.getHeight(), null);

		/*
		 * Un-comment everything below if you want to save the new image and
		 * return the location String tmpImg =
		 * String.valueOf(System.currentTimeMillis()) + ".png";
		 * 
		 * OutputStream os = null;
		 * 
		 * try { os = new FileOutputStream(loc + tmpImg);
		 * cs.compress(CompressFormat.PNG, 100, os); } catch(IOException e) {
		 * Log.e("combineImages", "problem combining images", e); }
		 */

		return cs;
	}

	/**
	 * Resize the image to the given dimensions, scaled.
	 * 
	 * @param bitmap
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	public static Bitmap resizeBitmap(final Bitmap bitmap, final int targetWidth, final int targetHeight) {

		final int width = bitmap.getWidth();
		final int height = bitmap.getHeight();

		int newWidth = 0, newHeight = 0;

		final double scaledByWidthRatio = ((float) targetWidth) / width;
		final double scaledByHeightRatio = ((float) targetHeight) / height;

		if (height * scaledByWidthRatio <= targetHeight) {
			newWidth = targetWidth;
			newHeight = (int) (height * scaledByWidthRatio);
		} else {
			newWidth = (int) (width * scaledByHeightRatio);
			newHeight = targetHeight;
		}

		Log.d(TAG, "Resizing to WxH: " + newWidth + "x" + newHeight);

		return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
	}

	/**
	 * Save the currently display image to the device's wallpaper.
	 * 
	 * @throws IOException
	 */
	public static void setWallpaper(Activity activity, Context context, Bitmap bitmap) throws IOException {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		final int targetWidth = metrics.widthPixels * WALLPAPER_SCREEN_SIZE;
		final int targetHeight = metrics.heightPixels;

		Bitmap resizedBitmap = ImageMgmt.resizeBitmap(bitmap, targetWidth, targetHeight);
		context.setWallpaper(resizedBitmap);
		ImageMgmt.clearBitmap(resizedBitmap);
	}

	/**
	 * Free up a bitmap and ask the garbage collector to grab resources.
	 * 
	 * @param bitmap
	 */
	public static void clearBitmap(Bitmap bitmap) {
		bitmap.recycle();
		bitmap = null;
		System.gc();
	}
}
