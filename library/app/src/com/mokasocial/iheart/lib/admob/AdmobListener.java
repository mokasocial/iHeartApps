package com.mokasocial.iheart.lib.admob;

import android.util.Log;

import com.admob.android.ads.AdView;
import com.admob.android.ads.SimpleAdListener;

public class AdmobListener extends SimpleAdListener {
	private static final String TAG = "AdmobListener";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.admob.android.ads.AdView.SimpleAdListener#onFailedToReceiveAd(com
	 * .admob.android.ads.AdView)
	 */
	@Override
	public void onFailedToReceiveAd(AdView adView) {
		super.onFailedToReceiveAd(adView);
		Log.d(TAG, "onFailedToReceiveAd!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.admob.android.ads.AdView.SimpleAdListener#onFailedToReceiveRefreshedAd
	 * (com.admob.android.ads.AdView)
	 */
	@Override
	public void onFailedToReceiveRefreshedAd(AdView adView) {
		super.onFailedToReceiveRefreshedAd(adView);
		Log.d(TAG, "onFailedToReceiveRefreshedAd!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.admob.android.ads.AdView.SimpleAdListener#onReceiveAd(com.admob.android
	 * .ads.AdView)
	 */
	@Override
	public void onReceiveAd(AdView adView) {
		super.onReceiveAd(adView);
		Log.d(TAG, "onReceiveAd!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.admob.android.ads.AdView.SimpleAdListener#onReceiveRefreshedAd(com
	 * .admob.android.ads.AdView)
	 */
	@Override
	public void onReceiveRefreshedAd(AdView adView) {
		super.onReceiveRefreshedAd(adView);
		Log.d(TAG, "onReceiveRefreshedAd!");
	}
}
