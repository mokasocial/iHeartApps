package com.mokasocial.iheart.lib;

import java.net.HttpURLConnection;
import java.net.URL;

import com.mokasocial.iheart.lib.common.ImageMgmt;
import com.mokasocial.iheart.lib.common.MediaScannerNotifier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ViewPhotoActivity extends Activity {

	private static final String TAG = "ViewPhotoActivity";

	public static final String INTENT_EXTRA_VIEW_PHOTO_URL = null;

	private ImageMgmt mImageMgmt;
	private URL mImageUrl;
	private Context mContext;
	private Activity mActivity;
	private ImageView mImageView;
	private LibApp mLibApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		/**
		 * @todo this should be more like a Flicka ActivityPhoto (zoomable,
		 *       sharable, wallpaperable)
		 */
		super.onCreate(savedInstanceState);
		
		mLibApp = ((LibApp) getApplication());
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		final String photoUrl = extras.getString(ViewPhotoActivity.INTENT_EXTRA_VIEW_PHOTO_URL);

		mContext = this;
		mActivity = this;
		
		LibApp.appSanityCheck(mContext, mActivity, mLibApp);
		mImageMgmt = ImageMgmt.getInstance(mLibApp.getImageCachePath());
		
		// this should go directly to link
		Intent viewUrlIntent = new Intent();
		viewUrlIntent.setAction(Intent.ACTION_VIEW);
		viewUrlIntent.setData(Uri.parse(photoUrl));
		startActivity(viewUrlIntent);
		finish();
	}

	/**
	 * Ping the image to see if it is available.
	 * 
	 * @param imageUrl
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean isImageAvailable(URL imageUrl) {
		final int timeout = 2000;
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);

			int httpResponse = conn.getResponseCode();
			if (httpResponse != HttpURLConnection.HTTP_OK) {
				Log.d(TAG, "Response from '" + imageUrl.toString() + "' invalid");
				conn.disconnect();
				return false;
			}

			conn.disconnect();
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Unable to test to verify image is available", e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(mLibApp.getResourceByKey(Main.MENU_CONTEXT_PHOTO), menu);
		menu.setHeaderIcon(mLibApp.getResourceByKey(Main.DRAWABLE_APP_LAUNCHER));
		menu.setHeaderTitle(getString(mLibApp.getResourceByKey(Main.STRING_PHOTO_CONTEXT_TITLE)));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == mLibApp.getResourceByKey(Main.ID_PHOTO_SAVE)) {
			mImageMgmt.saveImage(mImageUrl, mImageView.getDrawable());
			new MediaScannerNotifier(mContext, mImageMgmt.getSavedImagePath(mImageUrl), "image/jpeg");
			return true;
		} else if(item.getItemId() == mLibApp.getResourceByKey(Main.ID_PHOTO_SET_WALLPAPER)) {
			new SetWallpaperTask().execute();
			return true;
		} else if(item.getItemId() == mLibApp.getResourceByKey(Main.ID_PHOTO_SHARE)) {
			final Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("text/plain");
			share.putExtra(Intent.EXTRA_TEXT, mImageUrl.toString());
			share.putExtra(Intent.EXTRA_SUBJECT, getString(mLibApp.getResourceByKey(Main.STRING_PHOTO_SHARE_SUBJECT)));
			startActivity(Intent.createChooser(share, getString(mLibApp.getResourceByKey(Main.STRING_PHOTO_SHARE))));
			return true;
		}
		
		return super.onContextItemSelected(item);
	}

	/**
	 * This could take a while so let's not lock the phone and start a thread.
	 * 
	 */
	private class SetWallpaperTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				ImageMgmt.setWallpaper(mActivity, mContext, ((BitmapDrawable) mImageView.getDrawable()).getBitmap());
			} catch (Exception e) {
				Log.e(TAG, "Unable to setWallpaper", e);
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CharSequence text = getString(mLibApp.getResourceByKey(Main.STRING_PHOTO_WALLPAPER_SUCCESS));
			if (!result) {
				text = getString(mLibApp.getResourceByKey(Main.STRING_PHOTO_WALLPAPER_FAILURE));
			}

			final Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
