package com.mokasocial.iheart.lib;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mokasocial.iheart.lib.adapters.SearchAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * 
 * @author Michael Hradek
 *
 */
public class ActivitySearch extends ListActivity {
	
	private static final String TAG = "ActivitySearch";
	
	private PerformSearchTask mPerformSearchTask;
	private ArrayList<TwitterSearchResultItem> mSearchResults;
	private SearchAdapter mSearchAdapter;
	
	private Context mContext;
	private ListActivity mActivity;
	private LibApp mLibApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mLibApp = ((LibApp) getApplication());
		
		setContentView(R.layout.search_query);
		
		mContext = this;
		mActivity = this;
		mSearchResults = new ArrayList<TwitterSearchResultItem>();
		
		LibApp.appSanityCheck(mContext, mActivity, mLibApp);
		
		final Button searchButton = (Button) findViewById(R.id.search_btn);
		searchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mPerformSearchTask = new PerformSearchTask(getSearchTerms());
				mPerformSearchTask.execute();
			}
		});
		
		mSearchAdapter = new SearchAdapter(mContext, mActivity, mLibApp.getResourceByKey(Main.LAYOUT_FAVORITES_ROW), mSearchResults);
		setListAdapter(mSearchAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		/**
		 * Open this item in news view activity
		 * 
		 * TODO Article link needs work.
		 */

		Intent viewSummary = new Intent(mContext, ViewNewsActivity.class);
		final TwitterSearchResultItem message = mSearchResults.get(position);

		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_URL, message.getProfileImageUrl().toExternalForm());
		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_DATE, message.getCreatedAt());
		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_DESCRIPTION, message.getText());
		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_TITLE, message.getText());
		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_ID, message.getId());
		viewSummary.putExtra(ViewNewsActivity.INTENT_EXTRA_ENCODING, "Unicode");

		startActivity(viewSummary);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if(mPerformSearchTask != null) {
			mPerformSearchTask.cancel(true);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		if(mPerformSearchTask != null) {
			mPerformSearchTask.cancel(true);
		}
	}
	
	private String getSearchTerms() {
		final EditText searchTextBox = (EditText) findViewById(R.id.search_terms_box);
		Editable temp = searchTextBox.getEditableText();
		
		if(temp != null) {
			return temp.toString();
		}
		
		return null;
	}
	
	private class PerformSearchTask extends AsyncTask<Void, Void, ArrayList<TwitterSearchResultItem>>  {

		private String aSearchTerms;
		
		PerformSearchTask (String searchTerms) {
			aSearchTerms = searchTerms;
		}
		
		@Override
		protected ArrayList<TwitterSearchResultItem> doInBackground(Void... params) {
			
			// No null or empty searches
			if(aSearchTerms == null || aSearchTerms.length() < 1) {
				return null;
			}
			
			ArrayList<TwitterSearchResultItem> theData = new ArrayList<TwitterSearchResultItem>();

			try {
				final URL url = new URL("http://search.twitter.com/search.json?q=" + 
						URLEncoder.encode(aSearchTerms) + "&result_type=mixed");
				InputStream inputStream = url.openStream();
				DataInputStream dataInputStream =  new DataInputStream(new BufferedInputStream(inputStream, 4096));

				String json = "";
				String s;
				
				while ((s = dataInputStream.readLine()) != null) {
					json = json + s;
				}

				// Create the tokener from the string
				JSONTokener tokener = new JSONTokener(json);
				// Grab the next object from the tokener
				JSONObject jsonObj = (JSONObject) tokener.nextValue();
				// Dive in and grab the results array
				JSONArray jsonArray = (JSONArray) jsonObj.get("results");
				// Build the data array!
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject tweet = (JSONObject) jsonArray.get(i);
					theData.add(TwitterSearchResultItem.createFromTwitterJSON(tweet));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return theData;
		}
		
		@Override
		protected void onPostExecute(ArrayList<TwitterSearchResultItem> result) {
			if(result == null) {
				return;
			}
			
			mSearchResults = result;
			Collections.sort(mSearchResults);
			mSearchAdapter.notifyDataSetChanged();
			Log.d(TAG, "Found: " + mSearchResults.size());
			
			// Set up our view!
			final ListView temp = getListView();
			temp.setVisibility(ListView.VISIBLE);
			registerForContextMenu(temp);
		}
	}
}
