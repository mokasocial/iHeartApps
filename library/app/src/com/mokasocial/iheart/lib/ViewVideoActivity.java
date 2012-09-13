package com.mokasocial.iheart.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class ViewVideoActivity extends Activity {
	public static final String INTENT_EXTRA_VIEW_URL_ID = null;

	private Context mContext;
	private WebView mWeb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * @todo this should be more like a Flicka ActivityPhoto (zoomable,
		 *       sharable, wallpaperable)
		 */
		
		mContext = this;
	
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String url = extras.getString(ViewVideoActivity.INTENT_EXTRA_VIEW_URL_ID);
		mWeb = new WebView(mContext);
		/**
		 * @todo load from cache
		 */
		mWeb.loadUrl(url);
		setContentView(mWeb);
	}
}