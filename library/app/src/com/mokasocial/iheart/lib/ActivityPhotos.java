package com.mokasocial.iheart.lib;

import java.util.ArrayList;
import java.util.Collections;

import com.mokasocial.iheart.lib.adapters.PhotosAdapter;
import com.mokasocial.iheart.lib.boss.Photo;
import com.mokasocial.iheart.lib.parsers.GoogleNewsParser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

public class ActivityPhotos extends Activity {

	private final static String TAG = "ActivityPhotos";

	private Context mContext;
	private Activity mActivity;
	private ArrayList<Photo> mPhotos;
	private PhotosAdapter mAdapter;
	private LibApp mLibApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mLibApp = ((LibApp) getApplication());

		mContext = this;
		mActivity = this;
		
		LibApp.appSanityCheck(mContext, mActivity, mLibApp);

		// Views
		HelperUtils.initViewAndButtons(mActivity, mContext, mLibApp.getResourceByKey(Main.LAYOUT_PHOTO_STREAM));

		processPhotos();

		// Ads
		HelperUtils.initAdMob(mActivity, mLibApp.getResourceByKey(Main.ID_AD_VIEW));
	}

	private void processPhotos() {
		new FetchnParse().execute();
	}

	private void initViews() {
		GridView gridView = (GridView) findViewById(mLibApp.getResourceByKey(Main.ID_ANDROID_PHOTOGRID));
		mAdapter = new PhotosAdapter(mContext, mActivity, mPhotos);
		gridView.setAdapter(mAdapter);

		// omfg so bad
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				final Intent viewPhoto = new Intent(mContext, ViewPhotoActivity.class);
				viewPhoto.putExtra(ViewPhotoActivity.INTENT_EXTRA_VIEW_PHOTO_URL, mPhotos.get(position).clickurl);
				startActivity(viewPhoto);
			}
		});

		gridView.setVisibility(GridView.VISIBLE);
	}

	private ArrayList<Photo> loadPhotos() {
		// references to our images
		ArrayList<Photo> photos = new ArrayList<Photo>();
		String[] feedUrls = getResources().getStringArray(mLibApp.getResourceByKey(Main.ARRAY_PHOTO_QUERIES));
		GoogleNewsParser parser = new GoogleNewsParser(feedUrls);
		ArrayList<Message> messages = parser.parse();
		
		// for each rss feed url
		for (Message message : messages) {	
						
			try {
				Photo photo = new Photo();
				photo.clickurl = message.getLink();
				photo.date = message.getPubDate();
				if (message.findImageUrl() != null){
					photo.thumbnail_url = message.findImageUrl().toExternalForm();
				} else {
					continue;
				}
				if (photos.indexOf(photo) == -1){
					photos.add(photo);
				}
			} catch (Throwable t) {
				// hmm, no image. that's okay, just keep going.
				Log.e(TAG, t.getMessage(), t);
			}
		}

		// Sort by dates, descending
		Collections.reverse(photos);
		return photos;
	}

	private class FetchnParse extends AsyncTask<Void, Void, ArrayList<Photo>> {

		@Override
		protected ArrayList<Photo> doInBackground(Void... params) {
			return loadPhotos();
		}

		@Override
		protected void onPostExecute(ArrayList<Photo> result) {
			mPhotos = result;

			final LinearLayout loading = (LinearLayout) findViewById(mLibApp.getResourceByKey(Main.ID_LOADING));
			if (result.size() < 1) {
				final LinearLayout nothingLoaded = (LinearLayout) findViewById(mLibApp.getResourceByKey(Main.ID_NOTHING_LOADED));
				loading.setVisibility(LinearLayout.GONE);
				nothingLoaded.setVisibility(LinearLayout.VISIBLE);
				return;
			}

			loading.setVisibility(LinearLayout.GONE);
			initViews();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(mLibApp.getResourceByKey(Main.MENU_MAIN), menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == mLibApp.getResourceByKey(Main.ID_ABOUT)) {
			HelperUtils.showAboutDialog(mContext, mActivity);
			return true;	
		}

		return false;
	}
}
