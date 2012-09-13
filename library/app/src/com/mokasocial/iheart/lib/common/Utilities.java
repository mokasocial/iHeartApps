package com.mokasocial.iheart.lib.common;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

public class Utilities {

	private final static String TAG = "Utilities";

	private final static String NETWORK_TEST_URL = "http://www.google.com";
	private final static int NETWORK_TEST_TIMEOUT = 5000;

	public final static int NETWORK_TEST_SUCCESS = 1;
	public final static int NETWORK_TEST_FAILED = 0;

	/**
	 * Gets the software version and version name for this application
	 * 
	 * @param context
	 * @return
	 */
	public static PackageInfo getSoftwareVersion(Context context) {
		PackageInfo info = new PackageInfo();
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (Exception e) {
			Log.e(TAG, "Unable to get package info", e);
		}

		return info;
	}

	/**
	 * Convert an integer to a boolean value. Yes, unbelievable but we have a
	 * use for this.
	 * 
	 * @param value
	 * @return
	 */
	public static boolean intToBool(int value) {
		return (value != 0);
	}

	/**
	 * Convert a boolean to an integer value.
	 * 
	 * @param activity
	 * @param handler
	 */
	public static int boolToInt(boolean value) {
		return (value) ? 1 : 0;
	}

	/**
	 * Start a thread which executes the network test. This function actually
	 * does not return anything. You should instead use a Handler to listen for
	 * the result once the thread is complete.
	 * 
	 * @param activity
	 * @param handler
	 */
	public static void isNetworkAvailable(final Activity activity, final Handler handler) {
		new Thread() {
			@Override
			public void run() {
				boolean result = networkAvailabilityTest(activity);
				handler.sendEmptyMessage(boolToInt(result));
			}
		}.start();
	}

	/**
	 * Remember to set permissions for network access.
	 * 
	 * @param activity
	 * @return
	 */
	private static boolean networkAvailabilityTest(Activity activity) {
		HttpURLConnection conn = null;

		try {
			ConnectivityManager conMan = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = conMan.getActiveNetworkInfo();

			// What did we get from the object. If it is null then we failed
			// somewhere.
			if (networkInfo != null && networkInfo.isConnected()) {
				Log.i(TAG, "Android network state: " + networkInfo.getState().toString());
			} else {
				Log.i(TAG, "Android network state: null");
				return false;
			}

			// Only do this part of the test if the device thinks it has
			// network.
			URL url = new URL(NETWORK_TEST_URL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(NETWORK_TEST_TIMEOUT);
			conn.setReadTimeout(NETWORK_TEST_TIMEOUT);
			conn.connect();
			int httpResponse = conn.getResponseCode();
			if (httpResponse != HttpURLConnection.HTTP_OK) {
				Log.d(TAG, "Response from '" + NETWORK_TEST_URL + "' invalid");
				conn.disconnect();
				return false;
			}

			Log.i(TAG, "Network test succeeded");
			conn.disconnect();
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Network test failed", e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return false;
	}

	/**
	 * This is essentially a copy of the addAll function inside ArrayUtils since
	 * that isn't available within the SDK by default.
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static String[] addAll(String[] first, String[] second) {
		String[] combined = new String[first.length + second.length];
		System.arraycopy(first, 0, combined, 0, first.length);
		System.arraycopy(second, 0, combined, first.length, second.length);

		return combined;
	}

	/**
	 * 
	 * 
	 * @param pattern
	 * @param toMatch
	 * @return
	 */
	public static String regExMatch(String pattern, String toMatch) {
		Pattern tweetUrlPattern = Pattern.compile(pattern);
		Matcher m = tweetUrlPattern.matcher(toMatch);
		if (m.find()) {
			return m.group();
		}

		return null;
	}

	/**
	 * 
	 * 
	 * @param diff
	 * @return
	 */
	public static String getTimeString(long diff) {

		// these are millis
		long second = 1000;
		long minute = second * 60;
		long hour = minute * 60;
		long day = hour * 24;
		long month = day * 30;

		String timeString;

		// Log.d(TAG, "Date diff is: " + diff);
		if (diff < 0) {
			timeString = "just now";
		} else if (diff < minute) {
			timeString = "< 1 min";
		} else if (diff < hour) {
			timeString = Math.round(diff / minute) + " min" + (Math.round(diff / minute) > 1 ? "s" : "");
		} else if (diff < day) {
			timeString = Math.round(diff / hour) + " hr" + (Math.round(diff / hour) > 1 ? "s" : "");
		} else if (diff < month) {
			timeString = Math.round(diff / day) + " day" + (Math.round(diff / day) > 1 ? "s" : "");
		} else if (diff < day * 365) {
			timeString = Math.round(diff / month) + " mon" + (Math.round(diff / month) > 1 ? "s" : "");
		} else {
			timeString = "a long time";
		}

		return timeString;
	}
}
