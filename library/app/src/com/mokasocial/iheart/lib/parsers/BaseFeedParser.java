package com.mokasocial.iheart.lib.parsers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public abstract class BaseFeedParser implements FeedParser {

	private static final String TAG = "BaseFeedParser";

	// names of the XML tags
	public static final String CHANNEL = "channel";
	public static final String PUB_DATE = "pubDate";
	public static final String DESCRIPTION = "description";
	public static final String LINK = "link";
	public static final String TITLE = "title";
	public static final String ITEM = "item";
	public static final String CONTENT = "content:encoded";
	public static final String ENCODED = "encoded";

	public static final String LANGUAGE = "language";
	public static final String COPYRIGHT = "copyright";
	public static final String GUID = "guid";

	public static final String IMAGE = "image";
	public static final String URL = "url";

	private final URL[] feedUrls;

	/**
	 * @todo Need to be able to handle multiple URLs
	 * 
	 * @param url
	 */
	protected BaseFeedParser(String[] urls) {
		feedUrls = new URL[urls.length];
		try {
			int i = 0;
			for (String url : urls) {
				this.feedUrls[i] = new URL(url);
				i++;
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * We get an array of URLs and return an array of InputStreams. If a stream
	 * fails (throws) then we skip it.
	 * 
	 * @todo verify using BufferedInputStream performance improvement
	 * @return
	 */
	protected InputStream[] getInputStreams() {
		InputStream[] inputStreams = new InputStream[feedUrls.length];
		int i = 0;
		for (URL url : feedUrls) {
			try {
				inputStreams[i] = new BufferedInputStream(url.openConnection().getInputStream(), 8192);
			} catch (IOException e) {
				Log.e(TAG, "Unable to open stream to " + url.toString(), e);
				continue;
			}

			i++;
		}
		return inputStreams;

	}
}
