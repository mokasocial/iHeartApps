package com.mokasocial.iheart.lib.adapters;

import java.net.URL;
import java.util.ArrayList;

import com.mokasocial.iheart.lib.LibApp;
import com.mokasocial.iheart.lib.Main;
import com.mokasocial.iheart.lib.boss.Photo;
import com.mokasocial.iheart.lib.common.ImageMgmt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotosAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = "PhotosAdapter";

	private final Context mContext;
	private ArrayList<Photo> mItems;
	private ImageMgmt mImageMgmt;
	private LibApp mLibApp;

	public PhotosAdapter(Context context, Activity activity, ArrayList<Photo> photos) {
		
		mLibApp = ((LibApp) activity.getApplication());

		mContext = context;
		mItems = photos;
		mImageMgmt = ImageMgmt.getInstance(mLibApp.getImageCachePath());
	}

	public int getCount() {
		return mItems.size();
	}

	public Photo getItem(int position) {
		return mItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		iHeartGridImageView imageView;

		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new iHeartGridImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(96, 96));
			imageView.setScaleType(ImageView.ScaleType.CENTER);
			// imageView.setPadding(1,1,1,1);
		} else {
			imageView = (iHeartGridImageView) convertView;
		}

		final URL imageUrl = getItem(position).getThumbnailUrl();

		// Has a request for this image already been made?
		if (imageView.getTag() != imageUrl) {
			imageView.setImageResource(mLibApp.getResourceByKey(Main.DRAWABLE_EXAMINE_IMAGE_ICON));
			imageView.setTag(imageUrl);
		}

		mImageMgmt.fetchCachedImage(imageUrl, imageView);

		return imageView;
	}

	private class iHeartGridImageView extends ImageView {

		private Bitmap mOverlayBitmap;

		public iHeartGridImageView(Context context) {
			super(context);
			mOverlayBitmap = BitmapFactory.decodeResource(getResources(), mLibApp.getResourceByKey(Main.DRAWABLE_OVERLAY_PHOTO));
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawBitmap(mOverlayBitmap, 0, 0, null);
		}
	}
}
