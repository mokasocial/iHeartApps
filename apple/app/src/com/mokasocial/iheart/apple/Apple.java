package com.mokasocial.iheart.apple;

import java.util.HashMap;

import com.mokasocial.iheart.lib.Main;
import com.mokasocial.iheart.lib.LibApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Apple extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LibApp libApp = ((LibApp) getApplication());
		libApp.setResourceMap(createResourceMap());
		libApp.setDatabaseName(getString(R.string.app_name));
		
		// This call requires that LibApp be set up properly and will throw if it isn't.
		startActivity(new Intent(this, Main.class));
	}
	
	/**
	 * This needs to be here unfortunately. The integer resource ID
	 * seems to be related to the file size or other file specific
	 * point so we simply need to override ALL the values in the default 
	 * library. It's kind of lame so perhaps we can figure out a way
	 * to pass into a static function inside the lib or something. For
	 * now we'll have to copy and paste this format into each new 
	 * iHeart.
	 * 
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