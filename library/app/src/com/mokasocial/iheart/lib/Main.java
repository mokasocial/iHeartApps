package com.mokasocial.iheart.lib;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 
 * @author Michael Hradek
 *
 */
public class Main extends Activity {

	@SuppressWarnings("unused")
	private final static String TAG = "Main";

	public final static String PLACEHOLDER_VERSION = "{VERSION}";
	
	/**
	 * DEFINITIONS:
	 * 
	 */
	public final static String LAYOUT_SPLASH 		= "LAYOUT_SPLASH";
	public final static String LAYOUT_DIALOG_ABOUT 	= "LAYOUT_DIALOG_ABOUT";
	public final static String LAYOUT_NEWS_LIST		= "LAYOUT_NEWS_LIST";
	public final static String LAYOUT_FAVORITES_LIST = "LAYOUT_FAVORITES_LIST";	
	public final static String LAYOUT_PHOTO_STREAM	= "LAYOUT_PHOTO_STREAM";
	public final static String LAYOUT_VIDEOS_LIST	= "LAYOUT_VIDEOS_LIST";
	public final static String LAYOUT_PHOTO_DETAIL	= "LAYOUT_PHOTO_DETAIL";
	public final static String LAYOUT_NEWS_ITEM_DETAIL = "LAYOUT_NEWS_ITEM_DETAIL";
	public final static String LAYOUT_FAVORITES_ROW	= "LAYOUT_FAVORITES_ROW";
	public final static String LAYOUT_NEWS_ROW		= "LAYOUT_NEWS_ROW";
	public final static String LAYOUT_VIDEO_ROW		= "LAYOUT_VIDEO_ROW";
	public final static String LAYOUT_SEARCH_QUERY 	= "LAYOUT_SEARCH_QUERY";

	public final static String STRING_ABOUT_TITLE 	= "STRING_ABOUT_TITLE";
	public final static String STRING_PHOTO_CONTEXT_TITLE = "STRING_PHOTO_CONTEXT_TITLE";
	public final static String STRING_PHOTO_SHARE	= "STRING_PHOTO_SHARE";
	public final static String STRING_PHOTO_WALLPAPER_SUCCESS = "STRING_PHOTO_WALLPAPER_SUCCESS";
	public final static String STRING_PHOTO_WALLPAPER_FAILURE = "STRING_PHOTO_WALLPAPER_FAILURE";
	public final static String STRING_YOUTUBE_BASE_FEED_URL = "STRING_YOUTUBE_BASE_FEED_URL";
	public final static String STRING_YAHOO_BASE_QUERY_URL = "STRING_YAHOO_BASE_QUERY_URL";
	public final static String STRING_YAHOO_APP_ID	= "STRING_YAHOO_APP_ID";
	public final static String STRING_ADDED_FAVE 	= "STRING_ADDED_FAVE";
	public final static String STRING_ADD_FAILED		= "STRING_ADD_FAILED";
	public final static String STRING_UNABLE_CONN_INET = "STRING_UNABLE_CONN_INET";
	public final static String STRING_REMOVED_FAVE	= "STRING_REMOVED_FAVE";
	public final static String STRING_REMOVE_FAVE_FAILED = "STRING_REMOVE_FAVE_FAILED";
	public final static String STRING_NEWS_SHARE_SUBJECT = "STRING_NEWS_SHARE_SUBJECT";
	public final static String STRING_PHOTO_SHARE_SUBJECT = "STRING_PHOTO_SHARE_SUBJECT";

	public final static String ID_IHEART_LOGO 		= "ID_IHEART_LOGO";
	public final static String ID_BTN_NAV_VIDEOS     = "ID_BTN_NAV_VIDEOS";
	public final static String ID_BTN_NAV_FAVORITES	= "ID_BTN_NAV_FAVORITES";
	public final static String ID_BTN_NAV_PHOTOS		= "ID_BTN_NAV_PHOTOS";
	public final static String ID_BTN_NAV_NEWS		= "ID_BTN_NAV_NEWS";
	public final static String ID_BTN_NAV_SEARCH	= "ID_BTN_NAV_SEARCH";
	public final static String ID_PHOTO_VIEW			= "ID_PHOTO_VIEW";
	public final static String ID_AD_VIEW			= "ID_AD_VIEW";
	public final static String ID_SUMMARY			= "ID_SUMMARY";
	public final static String ID_LOADING			= "ID_LOADING";
	public final static String ID_NOTHING_LOADED		= "ID_NOTHING_LOADED";
	public final static String ID_NEWS_TITLE			= "ID_NEWS_TITLE";
	public final static String ID_NEWS_DATE			= "ID_NEWS_DATE";
	public final static String ID_NEWS_DESCRIPTION	= "ID_NEWS_DESCRIPTION";
	public final static String ID_NEWS_VIA			= "ID_NEWS_VIA";
	public final static String ID_FAVORITED			= "ID_FAVORITED";
	public final static String ID_VIDEO_TITLE		= "ID_VIDEO_TITLE";
	public final static String ID_VIDEO_DESCRIPTION	= "ID_VIDEO_DESCRIPTION";
	public final static String ID_ANDROID_PHOTOGRID	= "ID_ANDROID_PHOTOGRID";
	public final static String ID_ABOUT		= "ID_ABOUT";
	public final static String ID_PHOTO_SAVE			= "ID_PHOTO_SAVE";
	public final static String ID_PHOTO_SET_WALLPAPER = "ID_PHOTO_SET_WALLPAPER";
	public final static String ID_PHOTO_SHARE		= "ID_PHOTO_SHARE";
	public final static String ID_NEWS_ADD_TO_FAVES = "ID_NEWS_ADD_TO_FAVES";
	public final static String ID_NEWS_REMOVE_FROM_FAVES = "ID_NEWS_REMOVE_FROM_FAVES";
	public final static String ID_TWEETER				= "ID_TWEETER";
	public final static String ID_POST_ICON				= "ID_POST_ICON";
	public final static String ID_LOADING_IMAGE			= "ID_LOADING_IMAGE";
	public final static String ID_NEWS_SHARE			= "ID_NEWS_SHARE";
	public final static String ID_LOADING_MESSAGE		= "ID_LOADING_MESSAGE";

	public final static String MENU_CONTEXT_PHOTO	= "MENU_CONTEXT_PHOTO";
	public final static String MENU_CONTEXT_NEWS		= "MENU_CONTEXT_NEWS";
	public final static String MENU_CONTEXT_FAVES	= "MENU_CONTEXT_FAVES";
	public final static String MENU_MAIN				= "MENU_MAIN";

	public final static String ANIM_FADE_IN 			= "ANIM_FADE_IN";
	public final static String ANIM_ROTATE_INDEFINITELY = "ANIM_ROTATE_INDEFINITELY";

	public final static String ARRAY_TEST_DEVICES 	= "ARRAY_TEST_DEVICES";
	public final static String ARRAY_VIDEO_AUTHORS	= "ARRAY_VIDEO_AUTHORS";
	public final static String ARRAY_PHOTO_QUERIES	= "ARRAY_PHOTO_QUERIES";
	public final static String ARRAY_NEWS_URLS		= "ARRAY_NEWS_URLS";
	public final static String ARRAY_TWITTER_URLS	= "ARRAY_TWITTER_URLS";
	public final static String ARRAY_NAV_ITEM_PADDING = "ARRAY_NAV_ITEM_PADDING";

	public final static String DRAWABLE_NAV_ITEM_DIVIDER_SELECTED = "DRAWABLE_NAV_ITEM_DIVIDER_SELECTED";
	public final static String DRAWABLE_APP_LAUNCHER = "DRAWABLE_APP_LAUNCHER";
	public final static String DRAWABLE_EXAMINE_IMAGE_ICON = "DRAWABLE_EXAMINE_IMAGE_ICON";	
	public final static String DRAWABLE_OVERLAY_PHOTO = "DRAWABLE_OVERLAY_PHOTO";
	
	private final int mSplashTime = 2000;
	private Context mContext;
	private Runnable mRunnable;
	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LibApp libApp = (LibApp) getApplicationContext();
		
		// Protect ourselves from later mistakes
		if(libApp.isProperSetup() == false) {
			// @todo Uncomment this before building!
			// @todo Figure out a better way to do this.
			// throw new RuntimeException("You must define the database name and create the resource map to use this library.");
		}
		
		libApp.setResourceMap(createResourceMap());

		setContentView(libApp.getResourceByKey(LAYOUT_SPLASH));

		mContext = this;

		final ImageView splashImageView = (ImageView) findViewById(libApp.getResourceByKey(ID_IHEART_LOGO));
		final Animation myFadeInAnimation = AnimationUtils.loadAnimation(mContext, libApp.getResourceByKey(ANIM_FADE_IN));
		splashImageView.startAnimation(myFadeInAnimation);

		mHandler = new Handler();
		mRunnable = new Runnable() {
			public void run() {
				proceedFromSplash();
			}
		};

		mHandler.postDelayed(mRunnable, mSplashTime);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// Remove the exitRunnable callback from the handler queue
			mHandler.removeCallbacks(mRunnable);
			// Run the exit code manually
			proceedFromSplash();
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_HOME) {
			// Prevent proceedFromSplash if listed keys are pressed
			mHandler.removeCallbacks(mRunnable);
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 */
	private void proceedFromSplash() {
		finish();
		startActivity(new Intent(mContext, ActivityNews.class));
	}
	
	/**
	 * For testing only.
	 * 
	 * @return
	 */
	private HashMap<String, Integer> createResourceMap() {
		
		HashMap<String, Integer> resourceMap = new HashMap<String, Integer>();

		// The layouts
		resourceMap.put(Main.LAYOUT_SPLASH, R.layout.splash);
		resourceMap.put(Main.LAYOUT_DIALOG_ABOUT, R.layout.dialog_about);
		resourceMap.put(Main.LAYOUT_NEWS_LIST, R.layout.news_list);
		resourceMap.put(Main.LAYOUT_FAVORITES_LIST, R.layout.favorites_list);
		resourceMap.put(Main.LAYOUT_PHOTO_STREAM, R.layout.photo_stream);
		resourceMap.put(Main.LAYOUT_VIDEOS_LIST, R.layout.video_list);
		resourceMap.put(Main.LAYOUT_PHOTO_DETAIL, R.layout.photo_detail);
		resourceMap.put(Main.LAYOUT_NEWS_ITEM_DETAIL, R.layout.news_item_detail);
		resourceMap.put(Main.LAYOUT_FAVORITES_ROW, R.layout.favorites_row);
		resourceMap.put(Main.LAYOUT_NEWS_ROW, R.layout.news_row);
		resourceMap.put(Main.LAYOUT_VIDEO_ROW, R.layout.video_row);
		resourceMap.put(Main.LAYOUT_SEARCH_QUERY, R.layout.search_query);

		// The strings
		resourceMap.put(Main.STRING_ABOUT_TITLE, R.string.about_title);
		resourceMap.put(Main.STRING_PHOTO_CONTEXT_TITLE, R.string.photo_context_title);
		resourceMap.put(Main.STRING_PHOTO_SHARE, R.string.photo_share);
		resourceMap.put(Main.STRING_PHOTO_WALLPAPER_SUCCESS, R.string.photo_wallpaper_success);
		resourceMap.put(Main.STRING_PHOTO_WALLPAPER_FAILURE, R.string.photo_wallpaper_failure);
		resourceMap.put(Main.STRING_YOUTUBE_BASE_FEED_URL, R.string.youtube_base_feed_url);
		resourceMap.put(Main.STRING_YAHOO_BASE_QUERY_URL, R.string.yahoo_base_query_url);
		resourceMap.put(Main.STRING_YAHOO_APP_ID, R.string.yahoo_app_id);
		resourceMap.put(Main.STRING_ADDED_FAVE, R.string.added_fave);
		resourceMap.put(Main.STRING_ADD_FAILED, R.string.add_fave_failed);
		resourceMap.put(Main.STRING_UNABLE_CONN_INET, R.string.unable_conn_inet);
		resourceMap.put(Main.STRING_REMOVED_FAVE, R.string.removed_fave);
		resourceMap.put(Main.STRING_REMOVE_FAVE_FAILED, R.string.remove_fave_failed);
		resourceMap.put(Main.STRING_NEWS_SHARE_SUBJECT, R.string.news_share_subject);
		resourceMap.put(Main.STRING_PHOTO_SHARE_SUBJECT, R.string.photo_share_subject);

		// The views
		resourceMap.put(Main.ID_IHEART_LOGO, R.id.iheart_logo);
		resourceMap.put(Main.ID_BTN_NAV_VIDEOS, R.id.btn_nav_search);
		resourceMap.put(Main.ID_BTN_NAV_FAVORITES, R.id.btn_nav_favorites);
		resourceMap.put(Main.ID_BTN_NAV_PHOTOS, R.id.btn_nav_media);
		resourceMap.put(Main.ID_BTN_NAV_NEWS, R.id.btn_nav_news);
		resourceMap.put(Main.ID_BTN_NAV_SEARCH, R.id.btn_nav_query);
		resourceMap.put(Main.ID_PHOTO_VIEW, R.id.photo);
		resourceMap.put(Main.ID_AD_VIEW, R.id.ad);
		resourceMap.put(Main.ID_SUMMARY, R.id.summary);
		resourceMap.put(Main.ID_LOADING, R.id.loading);
		resourceMap.put(Main.ID_NOTHING_LOADED, R.id.nothing_loaded);
		resourceMap.put(Main.ID_NEWS_TITLE, R.id.news_title);
		resourceMap.put(Main.ID_NEWS_DATE, R.id.news_date);
		resourceMap.put(Main.ID_NEWS_DESCRIPTION, R.id.news_description);
		resourceMap.put(Main.ID_FAVORITED, R.id.favorited);
		resourceMap.put(Main.ID_VIDEO_TITLE, R.id.video_title);
		resourceMap.put(Main.ID_VIDEO_DESCRIPTION, R.id.video_description);
		resourceMap.put(Main.ID_ANDROID_PHOTOGRID, R.id.android_photogrid);
		resourceMap.put(Main.ID_ABOUT, R.id.about);
		resourceMap.put(Main.ID_PHOTO_SAVE, R.id.photo_save);
		resourceMap.put(Main.ID_PHOTO_SET_WALLPAPER, R.id.android_photogrid);
		resourceMap.put(Main.ID_PHOTO_SHARE, R.id.photo_share);
		resourceMap.put(Main.ID_NEWS_ADD_TO_FAVES, R.id.news_add_to_faves);
		resourceMap.put(Main.ID_NEWS_REMOVE_FROM_FAVES, R.id.news_remove_from_faves);
		resourceMap.put(Main.ID_TWEETER, R.id.tweeter);
		resourceMap.put(Main.ID_POST_ICON, R.id.post_icon);
		resourceMap.put(Main.ID_LOADING_IMAGE, R.id.loading_image);
		resourceMap.put(Main.ID_NEWS_SHARE, R.id.news_share);
		resourceMap.put(Main.ID_LOADING_MESSAGE, R.id.loading_message);		

		// The menus
		resourceMap.put(Main.MENU_CONTEXT_PHOTO, R.menu.context_photo);
		resourceMap.put(Main.MENU_CONTEXT_NEWS, R.menu.context_news);
		resourceMap.put(Main.MENU_CONTEXT_FAVES, R.menu.context_faves);
		resourceMap.put(Main.MENU_MAIN, R.menu.main);

		// The animations
		resourceMap.put(Main.ANIM_FADE_IN, R.anim.fade_in);
		resourceMap.put(Main.ANIM_ROTATE_INDEFINITELY, R.anim.rotate_indefinitely);

		// The string arrays
		resourceMap.put(Main.ARRAY_TEST_DEVICES, R.array.test_devices);
		resourceMap.put(Main.ARRAY_VIDEO_AUTHORS, R.array.video_authors);
		resourceMap.put(Main.ARRAY_PHOTO_QUERIES, R.array.photo_query_strings);
		resourceMap.put(Main.ARRAY_NEWS_URLS, R.array.news_urls);
		resourceMap.put(Main.ARRAY_TWITTER_URLS, R.array.twitter_urls);
		resourceMap.put(Main.ARRAY_NAV_ITEM_PADDING, R.array.nav_item_padding);

		// The drawables
		resourceMap.put(Main.DRAWABLE_NAV_ITEM_DIVIDER_SELECTED, R.drawable.nav_item_divider_selected);
		resourceMap.put(Main.DRAWABLE_APP_LAUNCHER, R.drawable.app_launcher);
		resourceMap.put(Main.DRAWABLE_EXAMINE_IMAGE_ICON, R.drawable.examine_image_icon);
		resourceMap.put(Main.DRAWABLE_OVERLAY_PHOTO, R.drawable.overlay_photo);
        
        return resourceMap;
	}
}
