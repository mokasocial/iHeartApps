package com.mokasocial.iheart.lib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class ViewNewsActivity extends Activity {

	private static final String TAG = "ViewNewsActivity";

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

		// This is a tweet so we need to parse out the actual URL
		if (url.contains("http://twitter.com/")) {
			Log.d(TAG, "This is a twitter message");
			Pattern tweetUrlPattern = Pattern.compile("([A-Za-z]+:\\/\\/[A-Za-z0-9-_]+\\.[A-Za-z0-9-_:%&amp;\\?\\/.=]+)");
			Matcher m = tweetUrlPattern.matcher(title);
			if (m.find()) {
				url = m.group();
				mWeb.loadUrl(url);
				/*
				 * Remove this activity from the stack since we're not using it.
				 * This prevents the back button from loading an empty activity
				 * on return from the browser activity.
				 */
				finish();
			} else {
				mWeb.loadUrl(url);
				finish();
			}
		} else {
			// to load just url
			mWeb.loadUrl(url);
			finish();
		}

		HelperUtils.initAdMob(mActivity, mLibApp.getResourceByKey(Main.ID_AD_VIEW));
	}
}
