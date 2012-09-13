package com.mokasocial.iheart.lib.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csam.jentities.Entities;
import com.csam.jentities.HTML4Entities;
import com.mokasocial.iheart.lib.LibApp;
import com.mokasocial.iheart.lib.Main;
import com.mokasocial.iheart.lib.TwitterSearchResultItem;
import com.mokasocial.iheart.lib.common.ImageMgmt;
import com.mokasocial.iheart.lib.common.Utilities;

/**
 * 
 * @author Michael Hradek
 *
 */
public class SearchAdapter extends ArrayAdapter<TwitterSearchResultItem> {

	private final static String TAG = "SearchAdapter";

	private final ArrayList<TwitterSearchResultItem> mItems;
	private final Context mContext;
	private final LayoutInflater mInflater;
	private final TimeZone mTimeZone;
	private final long mSysTime;

	private final Time mCurrentTime = new Time();
	private final Time mArticleTime = new Time();
	private Entities mEntities;

	private ImageMgmt mImageMgmt;
	private LibApp mLibApp;
	
	private Activity mActivity;

	public SearchAdapter(Context context, Activity activity, int textViewResourceId, ArrayList<TwitterSearchResultItem> objects) {
		super(context, textViewResourceId, objects);
		
		mItems = objects;
		mContext = context;
		mActivity = activity;
		
		mLibApp = ((LibApp) mActivity.getApplication());

		// Cache the LayoutInflate to avoid asking for a new one each time.
		mInflater = LayoutInflater.from(mContext);

		final Calendar cal = Calendar.getInstance();
		mTimeZone = cal.getTimeZone();

		mSysTime = System.currentTimeMillis();

		try {
			mEntities = new HTML4Entities();
		} catch (IOException e) {
			Log.e(TAG, "Unable to start Entities parser. Will continue without it", e);
		}

		mImageMgmt = ImageMgmt.getInstance(mLibApp.getImageCachePath());
	}

	@Override
	public synchronized int getCount() {
		return mItems.size();
	}

	@Override
	public synchronized TwitterSearchResultItem getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public synchronized int getPosition(TwitterSearchResultItem item) {
		return mItems.indexOf(item);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public synchronized View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(mLibApp.getResourceByKey(Main.LAYOUT_NEWS_ROW), null);

			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(mLibApp.getResourceByKey(Main.ID_NEWS_TITLE));
			holder.dateInfo = (TextView) convertView.findViewById(mLibApp.getResourceByKey(Main.ID_NEWS_DATE));
			holder.tweeter = (TextView) convertView.findViewById(mLibApp.getResourceByKey(Main.ID_TWEETER));
			holder.postIcon = (ImageView) convertView.findViewById(mLibApp.getResourceByKey(Main.ID_POST_ICON));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Grab all shit from message upfront
		TwitterSearchResultItem message = getItem(position);
		String tweeter = message.getFromUser();
		Date postDate = message.getCreatedAt();

		// Has a request for this image already been made?
		if (holder.postIcon.getTag() != message.getProfileImageUrl()) {
			holder.postIcon.setImageResource(mLibApp.getResourceByKey(Main.DRAWABLE_EXAMINE_IMAGE_ICON));
			holder.postIcon.setTag(message.getProfileImageUrl());
		}

		mImageMgmt.fetchCachedImage(message.getProfileImageUrl(), holder.postIcon);

		// Post title
		final String postTitle = message.getText();
		try {
			holder.title.setText(mEntities.parseText(postTitle).replaceAll("\\<.*?\\>", ""));
		} catch (NullPointerException e) {
			Log.e(TAG, "mEntities was null");
			holder.title.setText(postTitle.replaceAll("\\<.*?\\>", ""));
		}

		mCurrentTime.set(mSysTime);
		mArticleTime.set(postDate.getTime());

		// switch to local timezone for happy comparison
		mArticleTime.switchTimezone(mTimeZone.getID());

		long diff = mCurrentTime.toMillis(true) - mArticleTime.toMillis(true);

		String timeString = Utilities.getTimeString(diff);
		String dateAndPubText;

		dateAndPubText = timeString;
		holder.tweeter.setVisibility(View.VISIBLE);
		holder.dateInfo.setText(dateAndPubText);
		holder.dateInfo.setVisibility(TextView.VISIBLE);
		holder.tweeter.setText(tweeter);

		return convertView;
	}

	private static class ViewHolder {
		TextView title;
		TextView dateInfo;
		TextView tweeter;
		ImageView postIcon;
	}
}
