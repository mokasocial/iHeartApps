package com.mokasocial.iheart.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class ViewFavoritesActivity extends Activity {

	private static final String TAG = "ViewFavoritesActivity";

	static final String INTENT_EXTRA_TITLE = "message_title";
	static final String INTENT_EXTRA_URL = "message_url";
	static final String INTENT_EXTRA_DESCRIPTION = "message_description";
	static final String INTENT_EXTRA_DATE = "message_date";
	static final String INTENT_EXTRA_ID = "message_id";
	static final String INTENT_EXTRA_ENCODING = "message_encoding";

	private Context mContext;
	private Activity mActivity;
	private WebView mWeb;
	private LibApp mLibApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mLibApp = ((LibApp) getApplication());
		
		mContext = this;
		mActivity = this;
		
		LibApp.appSanityCheck(mContext, mActivity, mLibApp);

		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(mLibApp.getResourceByKey(Main.LAYOUT_NEWS_ITEM_DETAIL));

		Intent intent = getIntent();

		Bundle extras = intent.getExtras();
		String url = extras.getString(ViewNewsActivity.INTENT_EXTRA_URL);
		Log.d(TAG, "Starting activity with URL: " + url);
		String description = extras.getString(ViewNewsActivity.INTENT_EXTRA_DESCRIPTION);
		// String date = extras.getString(ViewNewsActivity.INTENT_EXTRA_DATE);
		String title = extras.getString(ViewNewsActivity.INTENT_EXTRA_TITLE);
		// String id = extras.getString(ViewNewsActivity.INTENT_EXTRA_ID);
		// String encoding =
		// extras.getString(ViewNewsActivity.INTENT_EXTRA_ENCODING);

		// LinearLayout controlTab = (LinearLayout)
		// findViewById(R.id.control_tab);

		TextView titleView = new TextView(mContext);
		titleView.setText(title);
		// controlTab.addView(titleView);

		mWeb = (WebView) findViewById(mLibApp.getResourceByKey(Main.ID_SUMMARY));

		// to load just description
		mWeb.loadDataWithBaseURL(url, "<html><body>" + description + "</html></body>", "text/html", "utf-8", url);
		// load the page!
		// mWeb.loadUrl(url);

		/*
		 * ImageButton thumbsup = (ImageButton) findViewById(R.id.thumbs_up);
		 * ImageButton thumbsdown = (ImageButton)
		 * findViewById(R.id.thumbs_down);
		 * 
		 * thumbsup.setClickable(true); thumbsup.setOnClickListener(new
		 * ImageButton.OnClickListener() { public void onClick(View v) { Toast
		 * toast = Toast.makeText(mContext, R.string.confirm_thumbs_up,
		 * Toast.LENGTH_SHORT); toast.show(); } });
		 * 
		 * thumbsdown.setClickable(true); thumbsdown.setOnClickListener(new
		 * ImageButton.OnClickListener() { public void onClick(View v) { Toast
		 * toast = Toast.makeText(mContext, R.string.confirm_thumbs_down,
		 * Toast.LENGTH_SHORT); toast.show(); } });
		 * 
		 * thumbsdown.setClickable(true);
		 */

		HelperUtils.initAdMob(mActivity, mLibApp.getResourceByKey(Main.ID_AD_VIEW));
	}
}
