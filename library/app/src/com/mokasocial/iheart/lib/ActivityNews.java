package com.mokasocial.iheart.lib;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mokasocial.iheart.lib.adapters.NewsAdapter;
import com.mokasocial.iheart.lib.common.Utilities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityNews extends ListActivity {

	private final static String TAG = "ActivityNews";

	private Context mContext;
	private ListActivity mActivity;
	private ArrayList<Tweet> mTweets;
	private NewsAdapter mNewsAdapter;
	private LibApp mLibApp;

	private LinkedBlockingQueue<FetchnParse> mTaskQueue = new LinkedBlockingQueue<FetchnParse>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mLibApp = ((LibApp) getApplication());

		mContext = this;
		mActivity = this;
		mTweets = new ArrayList<Tweet>();
		
		LibApp.appSanityCheck(mContext, mActivity, mLibApp);

		mNewsAdapter = new NewsAdapter(mContext, mActivity, mLibApp.getResourceByKey(Main.LAYOUT_FAVORITES_ROW), mTweets);
		setListAdapter(mNewsAdapter);

		// Views
		HelperUtils.initViewAndButtons(mActivity, mContext, mLibApp.getResourceByKey(Main.LAYOUT_NEWS_LIST));

		// Start loading
		Utilities.isNetworkAvailable(mActivity, networkTestHandler);

		// Ads
		HelperUtils.initAdMob(mActivity, mLibApp.getResourceByKey(Main.ID_AD_VIEW));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		/**
		 * Open this item in news view activity
		 */

		Intent viewSummary = new Intent(mContext, ViewNewsActivity.class);
		final Tweet message = mTweets.get(position);

		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_URL, message.getLink().toExternalForm());
		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_DATE, message.getCreatedAt());
		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_DESCRIPTION, message.getText());
		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_TITLE, message.getText());
		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_ID, message.getId());
		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_ENCODING, "Unicode");

		startActivity(viewSummary);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(mLibApp.getResourceByKey(Main.MENU_CONTEXT_NEWS), menu);
		menu.setHeaderIcon(mLibApp.getResourceByKey(Main.DRAWABLE_APP_LAUNCHER));
		menu.setHeaderTitle(getString(mLibApp.getResourceByKey(Main.STRING_PHOTO_CONTEXT_TITLE)));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = 
			(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Tweet message = mTweets.get(info.position);
		
		if(item.getItemId() == mLibApp.getResourceByKey(Main.ID_NEWS_ADD_TO_FAVES)) {
			message.setFavorite(true);
			try {
				Database.saveTweet(mContext, mActivity, message);
				message.setFavorite(true);
				mNewsAdapter.notifyDataSetChanged();
				faveSuccessToast(true);
			} catch (IOException e) {
				Log.e(TAG, "Unable to save favorite", e);
				faveSuccessToast(false);
			}
			return true;
		} else if (item.getItemId() ==  mLibApp.getResourceByKey(Main.ID_NEWS_SHARE)) {
			final Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("text/plain");
			share.putExtra(Intent.EXTRA_TEXT, message.getText());
			share.putExtra(Intent.EXTRA_SUBJECT, getString(mLibApp.getResourceByKey(Main.STRING_NEWS_SHARE_SUBJECT)));
			startActivity(Intent.createChooser(share, getString(mLibApp.getResourceByKey(Main.STRING_PHOTO_SHARE))));
			return true;
		}
		
		return super.onContextItemSelected(item);
	}

	/**
	 * Display the appropriate toast for a successful add to faves.
	 * 
	 * @param result
	 */
	private void faveSuccessToast(boolean result) {
		CharSequence text = getString(mLibApp.getResourceByKey(Main.STRING_ADDED_FAVE));
		if(result == false) {
			text = getString(mLibApp.getResourceByKey(Main.STRING_ADD_FAILED));
		}
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(mContext, text, duration);
		toast.show();
	}

	public Handler networkTestHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Utilities.NETWORK_TEST_SUCCESS:
				String[] urls = Utilities.addAll(
					getResources().getStringArray(mLibApp.getResourceByKey(Main.ARRAY_NEWS_URLS)),
					getResources().getStringArray(mLibApp.getResourceByKey(Main.ARRAY_TWITTER_URLS))
				);
				loadFeeds(urls);
				break;
			case Utilities.NETWORK_TEST_FAILED:
				failedNetworkTest();
				break;
			}

			super.handleMessage(msg);
		}
	};

	private void failedNetworkTest() {
		final CharSequence text = getString(mLibApp.getResourceByKey(Main.STRING_UNABLE_CONN_INET));
		final int duration = Toast.LENGTH_SHORT;

		final Toast toast = Toast.makeText(mContext, text, duration);
		toast.show();
	}

	private void loadFeeds(String[] twitterScreenNames){
		for(final String screenName : twitterScreenNames) {
			addTask(new FetchnParse(screenName));
		}
	}

	/**
	 * Get the queue size. Could be helpful for onStop and other
	 * Activity functionality to clear our queues.
	 * 
	 * @return
	 */
	public int queueSize() {
		return mTaskQueue.size();
	}

	/**
	 *
	 * @see ImageManagement 
	 * @param task
	 */
	private void addTask(FetchnParse task) {
		Log.i(TAG, "Feed queue size: " + queueSize());
		try{
			task.execute();
			while(!mTaskQueue.isEmpty()){
				task = mTaskQueue.remove();
				if(task.getStatus() == AsyncTask.Status.PENDING) {
					task.execute();
					continue;
				}

				// If it is already running, remove it
				Log.w(TAG, "Removing finished or running task");
				mTaskQueue.remove();
			}
		} catch (RejectedExecutionException r){
			Log.i(TAG, "RejectedExecutionException Exception - Adding task to queue");
			if(!mTaskQueue.contains(task)) {
				mTaskQueue.add(task);
			}
		} catch (Exception e) {
			Log.e(TAG, "Something else went wrong. Clearing queue", e);
			mTaskQueue.clear();
		}
	}

	/**
	 * We do all the work in here. This tasks handles ONE url or fetch at a time.
	 * Use the {@link #addTask} function to start processing if dealing with more
	 * than a single twitter name.
	 *
	 * @see loadFeeds()
	 */
	private class FetchnParse extends AsyncTask<Void, Void, ArrayList<Tweet>> {

		private String lTwitterScreenName;

		FetchnParse(String twitterScreenName) {
			lTwitterScreenName = twitterScreenName;
		}

		@Override
		protected ArrayList<Tweet> doInBackground(Void... params) {
			ArrayList<Tweet> theData = new ArrayList<Tweet>();

			try {
				final URL url = new URL("http://api.twitter.com/1/statuses/user_timeline.json?screen_name=" + 
						lTwitterScreenName + "&count=5&exclude_replies=true");
				InputStream inputStream = url.openStream();
				DataInputStream dataInputStream =  new DataInputStream(new BufferedInputStream(inputStream, 4096));

				String json = "";
				String s;

				while ((s = dataInputStream.readLine()) != null) {
					json = json + s;
				}

				JSONTokener tokener = new JSONTokener(json);
				JSONArray array = (JSONArray) tokener.nextValue();
				for (int i = 0; i < array.length(); i++) {
					JSONObject tweet = (JSONObject) array.get(i);
					theData.add( Tweet.createFromTwitterJSON(tweet));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return theData;
		}

		@Override
		protected void onPostExecute(ArrayList<Tweet> result) {
			// Add everything we got.
			mTweets.addAll(result);
			Collections.sort(mTweets);
			mNewsAdapter.notifyDataSetChanged();

			final LinearLayout loading = (LinearLayout) findViewById(mLibApp.getResourceByKey(Main.ID_LOADING));
			if(mTweets.size() < 1) {
				final LinearLayout nothingLoaded = (LinearLayout) findViewById(mLibApp.getResourceByKey(Main.ID_NOTHING_LOADED));
				loading.setVisibility(LinearLayout.GONE);
				nothingLoaded.setVisibility(LinearLayout.VISIBLE);
				return;
			}

			// hide loading initial screen
			loading.setVisibility(LinearLayout.GONE);

			// also hide loading update screen
			final View loadingMessage = findViewById(mLibApp.getResourceByKey(Main.ID_LOADING_MESSAGE));	
			loadingMessage.setVisibility(View.GONE);

			final ListView temp = getListView();
			temp.setVisibility(ListView.VISIBLE);
			registerForContextMenu(temp);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(mLibApp.getResourceByKey(Main.MENU_MAIN), menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		if(item.getItemId() == mLibApp.getResourceByKey(Main.ID_ABOUT)) {
			HelperUtils.showAboutDialog(mContext, mActivity);
			return true;
		}

		return false;
	}
}
