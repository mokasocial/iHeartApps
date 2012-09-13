package com.mokasocial.iheart.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Key;
import com.mokasocial.iheart.lib.adapters.VideoAdapter;

public class ActivityVideos extends ListActivity {

	private final static String TAG = "ActivityVideos";

	private Context mContext;
	private Activity mActivity;
	private ArrayList<Video> mVideos;
	private VideoAdapter mVideoAdapter;
	private LibApp mLibApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mLibApp = ((LibApp) getApplication());

		mContext = this;
		mActivity = this;
		
		LibApp.appSanityCheck(mContext, mActivity, mLibApp);

		// Views
		HelperUtils.initViewAndButtons(mActivity, mContext, mLibApp.getResourceByKey(Main.LAYOUT_VIDEOS_LIST));

		loadFeed(getResources().getStringArray(mLibApp.getResourceByKey(Main.ARRAY_VIDEO_AUTHORS)));

		// Ads
		HelperUtils.initAdMob(mActivity, mLibApp.getResourceByKey(Main.ID_AD_VIEW));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		/**
		 * @todo show item in tab
		 */
		// To go directly to browser:
		Intent viewMessage = new Intent(Intent.ACTION_VIEW, Uri.parse(mVideos.get(position).player.defaultUrl));
		this.startActivity(viewMessage);
	}

	private void loadFeed(String[] urls) {
		try {
			new FetchnParse().execute(urls);
		} catch (Throwable t) {
			Log.e(TAG, t.getMessage(), t);
		}
	}

	/**
	 * We do all the work in here.
	 * 
	 */
	private class FetchnParse extends AsyncTask<String, Void, ArrayList<Video>> {

		@Override
		protected ArrayList<Video> doInBackground(String... authors) {
			ArrayList<Video> theData = new ArrayList<Video>();

			try {
				// setup up the HTTP transport
				HttpTransport transport = new HttpTransport();
				transport.defaultHeaders.put("GData-Version", "2");
				transport.addParser(new JsonCParser());

				// build the HTTP GET request and URL
				HttpRequest request = transport.buildGetRequest();

				for (String youtubeAuthor : authors) {
					try {
						// build feed request for this youtube author
						YouTubeUrl youTubeUrl = new YouTubeUrl(getResources().getString(mLibApp.getResourceByKey(Main.STRING_YOUTUBE_BASE_FEED_URL)));
						youTubeUrl.author = youtubeAuthor;
						youTubeUrl.maxResults = 10;
						request.url = youTubeUrl;

						// execute the request and the parse video feed
						VideoFeed feed;
						feed = request.execute().parseAs(VideoFeed.class);
						for (Video video : feed.items) {
							theData.add(video);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return theData;
		}

		@Override
		protected void onPostExecute(ArrayList<Video> result) {
			mVideos = result;

			final LinearLayout loading = (LinearLayout) findViewById(mLibApp.getResourceByKey(Main.ID_LOADING));
			if (result.size() < 1) {
				final LinearLayout nothingLoaded = (LinearLayout) findViewById(mLibApp.getResourceByKey(Main.ID_NOTHING_LOADED));
				loading.setVisibility(LinearLayout.GONE);
				nothingLoaded.setVisibility(LinearLayout.VISIBLE);
				return;
			}

			mVideoAdapter = new VideoAdapter(mContext, mActivity, mLibApp.getResourceByKey(Main.LAYOUT_FAVORITES_ROW), mVideos);
			setListAdapter(mVideoAdapter);
			loading.setVisibility(LinearLayout.GONE);
			getListView().setVisibility(ListView.VISIBLE);
		}
	}

	public static class YouTubeUrl extends GenericUrl {
		@Key
		final String alt = "jsonc";
		@Key
		String author;
		@Key("max-results")
		Integer maxResults;

		YouTubeUrl(String url) {
			super(url);
		}
	}

	public static class VideoFeed {
		@Key
		public List<Video> items;
	}

	public static class Video {
		@Key
		public String title;
		@Key
		public String description;
		@Key
		public Player player;
	}

	public static class Player {
		@Key("default")
		public String defaultUrl;
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
