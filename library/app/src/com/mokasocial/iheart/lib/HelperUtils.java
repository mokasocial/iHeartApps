package com.mokasocial.iheart.lib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;
import com.mokasocial.iheart.lib.admob.AdmobListener;
import com.mokasocial.iheart.lib.common.Utilities;

/**
 * Specific to this application!
 * 
 */
public class HelperUtils {

	@SuppressWarnings("unused")
	private final static String TAG = "HelperUtils";

	/**
	 * 
	 * 
	 * @param activity
	 * @param adViewId
	 */
	public static void initAdMob(final Activity activity, final int adViewId) {
		LibApp libApp = ((LibApp) activity.getApplication());
		
		String[] testDevices = Utilities.addAll(activity.getResources().getStringArray(libApp.getResourceByKey(Main.ARRAY_TEST_DEVICES)), 
			new String[] { AdManager.TEST_EMULATOR });

		AdManager.setTestDevices(testDevices);
		AdManager.setAllowUseOfLocation(true);

		AdmobListener listener = new AdmobListener();

		AdView ad = (AdView) activity.findViewById(adViewId);
		ad.setAdListener(listener);
	}

	/**
	 *
	 * 
	 * @param activity
	 * @param context
	 * @param mainViewId
	 */
	public static void initViewAndButtons(final Activity activity, final Context context, final int mainViewId) {
		LibApp libApp = ((LibApp) activity.getApplication());
		// Set up main view
		activity.setContentView(mainViewId);

		// Start the loading animation
		final ImageView loadingImage = (ImageView) activity.findViewById(libApp.getResourceByKey(Main.ID_LOADING_IMAGE)); 
		loadingImage.startAnimation(AnimationUtils.loadAnimation(context, libApp.getResourceByKey(Main.ANIM_ROTATE_INDEFINITELY))); 

		// Highlight appropriate button
		if (libApp.getResourceByKey(Main.LAYOUT_NEWS_LIST) == mainViewId) {
			final ImageButton selectedButton = (ImageButton) activity.findViewById(libApp.getResourceByKey(Main.ID_BTN_NAV_NEWS));
			selectedButton.setBackgroundResource(libApp.getResourceByKey(Main.DRAWABLE_NAV_ITEM_DIVIDER_SELECTED));
			/**
			 * If these don't work, check http://www.anddev.org/xml_integer_array_resource_references_getintarray-t9268.html
			 */
			int[] padding = context.getResources().getIntArray(libApp.getResourceByKey(Main.ARRAY_NAV_ITEM_PADDING));
			selectedButton.setPadding(padding[0], padding[1], padding[2], padding[3]);
		} else if (libApp.getResourceByKey(Main.LAYOUT_PHOTO_STREAM) == mainViewId) {
			final ImageButton selectedButton = (ImageButton) activity.findViewById(libApp.getResourceByKey(Main.ID_BTN_NAV_PHOTOS));
			selectedButton.setBackgroundResource(libApp.getResourceByKey(Main.DRAWABLE_NAV_ITEM_DIVIDER_SELECTED));
			int[] padding = context.getResources().getIntArray(libApp.getResourceByKey(Main.ARRAY_NAV_ITEM_PADDING));
			selectedButton.setPadding(padding[0], padding[1], padding[2], padding[3]);
		} else if (libApp.getResourceByKey(Main.LAYOUT_FAVORITES_LIST) == mainViewId) {
			final ImageButton selectedButton = (ImageButton) activity.findViewById(libApp.getResourceByKey(Main.ID_BTN_NAV_FAVORITES));
			selectedButton.setBackgroundResource(libApp.getResourceByKey(Main.DRAWABLE_NAV_ITEM_DIVIDER_SELECTED));
			int[] padding = context.getResources().getIntArray(libApp.getResourceByKey(Main.ARRAY_NAV_ITEM_PADDING));
			selectedButton.setPadding(padding[0], padding[1], padding[2], padding[3]);
		} else if (libApp.getResourceByKey(Main.LAYOUT_VIDEOS_LIST) == mainViewId) {
			final ImageButton selectedButton = (ImageButton) activity.findViewById(libApp.getResourceByKey(Main.ID_BTN_NAV_VIDEOS));
			selectedButton.setBackgroundResource(libApp.getResourceByKey(Main.DRAWABLE_NAV_ITEM_DIVIDER_SELECTED));
			int[] padding = context.getResources().getIntArray(libApp.getResourceByKey(Main.ARRAY_NAV_ITEM_PADDING));
			selectedButton.setPadding(padding[0], padding[1], padding[2], padding[3]);
		} else if (libApp.getResourceByKey(Main.LAYOUT_SEARCH_QUERY) == mainViewId) {
			final ImageButton selectedButton = (ImageButton) activity.findViewById(libApp.getResourceByKey(Main.ID_BTN_NAV_SEARCH));
			selectedButton.setBackgroundResource(libApp.getResourceByKey(Main.DRAWABLE_NAV_ITEM_DIVIDER_SELECTED));
			int[] padding = context.getResources().getIntArray(libApp.getResourceByKey(Main.ARRAY_NAV_ITEM_PADDING));
			selectedButton.setPadding(padding[0], padding[1], padding[2], padding[3]);
		}

		// Init all the buttons for this view
		final ImageButton newsBtn = (ImageButton) activity.findViewById(libApp.getResourceByKey(Main.ID_BTN_NAV_NEWS));
		newsBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				final Intent intent = new Intent(context, ActivityNews.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				activity.startActivity(intent);
			}
		});

		final ImageButton photosBtn = (ImageButton) activity.findViewById(libApp.getResourceByKey(Main.ID_BTN_NAV_PHOTOS));
		photosBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				final Intent intent = new Intent(context, ActivityPhotos.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				activity.startActivity(intent);
			}
		});

		final ImageButton favesBtn = (ImageButton) activity.findViewById(libApp.getResourceByKey(Main.ID_BTN_NAV_FAVORITES));
		favesBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				final Intent intent = new Intent(context, ActivityFavorites.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
			}
		});

		final ImageButton videosBtn = (ImageButton) activity.findViewById(libApp.getResourceByKey(Main.ID_BTN_NAV_VIDEOS));
		videosBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				final Intent intent = new Intent(context, ActivityVideos.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				activity.startActivity(intent);
			}
		});
		
		final ImageButton searchBtn = (ImageButton) activity.findViewById(libApp.getResourceByKey(Main.ID_BTN_NAV_SEARCH));
		searchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				final Intent intent = new Intent(context, ActivitySearch.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				activity.startActivity(intent);
			}
		});
	}

	/**
	 * Show the about dialog box.
	 * 
	 * @return void
	 */
	public static void showAboutDialog(Context context, Activity activity) {
		LibApp libApp = ((LibApp) activity.getApplication());
		
		Dialog dialog = new Dialog(context);
		dialog.setContentView(libApp.getResourceByKey(Main.LAYOUT_DIALOG_ABOUT));
		String version = Utilities.getSoftwareVersion(context).versionName;
		String title = context.getString(libApp.getResourceByKey(Main.STRING_ABOUT_TITLE)).replace(Main.PLACEHOLDER_VERSION, version);
		dialog.setTitle(title);
		dialog.show();
	}
}
