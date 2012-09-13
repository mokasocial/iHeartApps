package com.mokasocial.iheart.lib.boss;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.api.client.util.Key;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class Photo implements Comparable<Photo> {
	
	private static final String TAG = "Photo";
	
	private static SimpleDateFormat mDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
	
	@Key
	public String clickurl;

	// this breaks hard. why'd they call a field by a keyword? damnit, yahoo
	// @Key
	// public String abstract;

	@Key
	public String date;
	@Key
	public String filename;
	@Key
	public String format;
	@Key
	public int height;
	@Key
	public String mimetype;
	@Key
	public String refererurl;
	@Key
	public int size;
	@Key
	public String thumbnail_height;
	@Key
	public String thumbnail_url;
	@Key
	public String thumbnail_width;
	@Key
	public String url;
	@Key
	public String title;
	@Key
	public int width;

	public Date getDate() {
		mDateFormat.setLenient(true);
		try {
			return mDateFormat.parse(date);
		} catch (ParseException e) {
			Log.e(TAG, "Unable to parse date", e);
			return null;
		}
	}

	public CharSequence getTitle() {
		return title;
	}

	public boolean isFavorite() {
		// TODO Auto-generated method stub
		return false;
	}

	public URL getThumbnailUrl() {
		try {
			return new URL(thumbnail_url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public URL getLargeUrl() {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Drawable getThumbnailDrawable() {
		// TODO Auto-generated method stub
		return null;
	}

	public int compareTo(Photo another) {
		return another.getDate().compareTo(getDate());
	}
}
