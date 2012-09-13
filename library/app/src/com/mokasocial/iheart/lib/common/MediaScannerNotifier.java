package com.mokasocial.iheart.lib.common;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

/**
 * Notify the media scanner to add the given image to its database.
 * 
 */
public class MediaScannerNotifier implements MediaScannerConnectionClient {
	private Context mContext;
	private MediaScannerConnection mConnection;
	private String mPath;
	private String mMimeType;

	public MediaScannerNotifier(Context context, String path, String mimeType) {
		mContext = context;
		mPath = path;
		mMimeType = mimeType;
		mConnection = new MediaScannerConnection(context, this);
		mConnection.connect();
	}

	public void onMediaScannerConnected() {
		mConnection.scanFile(mPath, mMimeType);
	}

	public void onScanCompleted(String path, Uri uri) {
		// OPTIONAL: scan is complete, this will cause the viewer to render it
		try {
			if (uri != null) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(uri);
				mContext.startActivity(intent);
			}
		} finally {
			mConnection.disconnect();
			mContext = null;
		}
	}
}
