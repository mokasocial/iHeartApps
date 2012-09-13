package com.mokasocial.iheart.lib.adapters;

import java.util.ArrayList;

import com.mokasocial.iheart.lib.LibApp;
import com.mokasocial.iheart.lib.Main;
import com.mokasocial.iheart.lib.ActivityVideos.Video;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VideoAdapter extends ArrayAdapter<Video> {

	private final ArrayList<Video> mItems;
	private final Context mContext;
	private final LayoutInflater mInflater;
	private LibApp mLibApp;

	public VideoAdapter(Context context, Activity activity, int textViewResourceId, ArrayList<Video> objects) {
		super(context, textViewResourceId, objects);
		
		mLibApp = ((LibApp) activity.getApplication());

		mItems = objects;
		mContext = context;

		// Cache the LayoutInflate to avoid asking for a new one each time.
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public synchronized int getCount() {
		return mItems.size();
	}

	@Override
	public synchronized Video getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public synchronized int getPosition(Video item) {
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
			convertView = mInflater.inflate(mLibApp.getResourceByKey(Main.LAYOUT_VIDEO_ROW), null);

			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(mLibApp.getResourceByKey(Main.ID_VIDEO_TITLE));
			holder.description = (TextView) convertView.findViewById(mLibApp.getResourceByKey(Main.ID_VIDEO_DESCRIPTION));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Video video = mItems.get(position);

		holder.title.setText(video.title);
		holder.description.setText(video.description);

		return convertView;
	}

	private static class ViewHolder {
		TextView title;
		TextView description;
	}
}