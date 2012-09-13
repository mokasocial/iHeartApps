package com.mokasocial.iheart.lib;

import java.util.ArrayList;

import com.mokasocial.iheart.lib.adapters.NewsAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class ActivityFavorites extends ListActivity {

	private final static String TAG = "ActivityFavorites";

	private Context mContext;
	private Activity mActivity;
	private ArrayList<Tweet> mMessages;
	private NewsAdapter mNewsAdapter;
	private LibApp mLibApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mLibApp = ((LibApp) getApplication());

		mContext = this;
		mActivity = this;
		
		LibApp.appSanityCheck(mContext, mActivity, mLibApp);

		// Views
		HelperUtils.initViewAndButtons(mActivity, mContext, mLibApp.getResourceByKey(Main.LAYOUT_FAVORITES_LIST));

		// Start loading
		loadFeed();

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
		Tweet message = mMessages.get(position);
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
		inflater.inflate(mLibApp.getResourceByKey(Main.MENU_CONTEXT_FAVES), menu);
		menu.setHeaderIcon(mLibApp.getResourceByKey(Main.DRAWABLE_APP_LAUNCHER));
		menu.setHeaderTitle(getString(mLibApp.getResourceByKey(Main.STRING_PHOTO_CONTEXT_TITLE)));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == mLibApp.getResourceByKey(Main.ID_NEWS_REMOVE_FROM_FAVES)) {
			final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			Tweet message = mMessages.get(info.position);
			try {
				Database.removeTweet(mContext, mActivity, message);
				mMessages.remove(info.position);
				mNewsAdapter.notifyDataSetChanged();
				if (mMessages.size() < 1) {
					final LinearLayout nothingLoaded = (LinearLayout) findViewById(mLibApp.getResourceByKey(Main.ID_NOTHING_LOADED));
					getListView().setVisibility(LinearLayout.GONE);
					nothingLoaded.setVisibility(LinearLayout.VISIBLE);
				}
				faveSuccessToast(true);
			} catch (Exception e) {
				Log.e(TAG, "Unable to save favorite", e);
				faveSuccessToast(false);
			}
			return true;
		}
		
		return super.onContextItemSelected(item);
	}

	/**
	 * Display the appropriate toast for a successful removal from faves.
	 * 
	 * @param result
	 */
	private void faveSuccessToast(boolean result) {
		CharSequence text = getString(mLibApp.getResourceByKey(Main.STRING_REMOVED_FAVE));
		if (result == false) {
			text = getString(mLibApp.getResourceByKey(Main.STRING_REMOVE_FAVE_FAILED));
		}
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(mContext, text, duration);
		toast.show();
	}

	private void loadFeed() {
		try {
			new FetchnParse().execute();
		} catch (Throwable t) {
			Log.e(TAG, t.getMessage(), t);
		}
	}

	/**
	 * We do all the work in here.
	 * 
	 */
	private class FetchnParse extends AsyncTask<Void, Void, ArrayList<Tweet>> {

		@Override
		protected ArrayList<Tweet> doInBackground(Void... params) {
			return Database.loadFavedTweets(mContext, mActivity);
		}

		@Override
		protected void onPostExecute(ArrayList<Tweet> result) {
			mMessages = result;

			final LinearLayout loading = (LinearLayout) findViewById(mLibApp.getResourceByKey(Main.ID_LOADING));
			if (result.size() < 1) {
				final LinearLayout nothingLoaded = (LinearLayout) findViewById(mLibApp.getResourceByKey(Main.ID_NOTHING_LOADED));
				loading.setVisibility(LinearLayout.GONE);
				nothingLoaded.setVisibility(LinearLayout.VISIBLE);
				return;
			}

			mNewsAdapter = new NewsAdapter(mContext, mActivity, mLibApp.getResourceByKey(Main.LAYOUT_FAVORITES_ROW), mMessages);
			setListAdapter(mNewsAdapter);
			registerForContextMenu(getListView());
			loading.setVisibility(LinearLayout.GONE);
			getListView().setVisibility(ListView.VISIBLE);
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