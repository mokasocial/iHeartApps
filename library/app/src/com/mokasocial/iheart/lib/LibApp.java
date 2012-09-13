package com.mokasocial.iheart.lib;

import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class LibApp extends Application {

	private String mDatabaseName = "iHeartLib";

	private String mImageCachePath = Environment.getExternalStorageDirectory().getPath() + 
		File.separator + mDatabaseName;

	private HashMap<String, Integer> mResourceMap;
	
	private boolean mProperDatabaseSetup = false;
	private boolean mProperResourceSetup = false;
	
	public void setResourceMap(HashMap<String, Integer> resourceMap) {
		mProperResourceSetup = true;
		mResourceMap = resourceMap;
	}

	public int getResourceByKey(String key) {
		return mResourceMap.get(key);
	}
	
	public void setDatabaseName(String newName) {
		mProperDatabaseSetup = true;
		mDatabaseName = newName;
	}
	
	public String getDatabaseName() {
		return mDatabaseName;
	}
	
	public String getImageCachePath() {
		return mImageCachePath;
	}

	public boolean isProperSetup() {
		return mProperDatabaseSetup && mProperResourceSetup;
	}
	
	public static void appSanityCheck(Context context, Activity activity, LibApp libApp) {
		if(libApp == null || libApp.isProperSetup() == false) {
			Intent intent = new Intent(context, Main.class);
			context.startActivity(intent);
			activity.finish();
		}
	}
}
