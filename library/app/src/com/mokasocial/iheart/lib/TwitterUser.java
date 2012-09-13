package com.mokasocial.iheart.lib;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class TwitterUser {

	private boolean verified;
	private URL profileBackgroundImageUrl;
	private int favoritesCount;
	private Date createdAt;
	private URL profileImageUrl;
	private String description;
	private String profileSidebarFillColor;
	private boolean showAllInlineMedia;
	private int listedCount;
	private boolean geoEnabled;
	private boolean profileBackgroundTile;
	private String screenName;
	private String profileSidebarBorderColor;
	private boolean contributorsEnabled;
	private String location;
	private String idStr;
	private String timeZone;
	private String profileBackgroundColor;
	private String lang;
	private int statusesCount;
	private boolean isProtected;
	private URL url;
	private String profileTextColor;
	private String name;
	private String followRequestSent;
	private boolean profileUseBackgroundImage;
	private int followersCount;
	private String id;
	private String following;
	private String notifications;
	private String utcOffset;
	private int friendsCount;
	private String profileLinkColor;

	private JSONObject jsonUserObject;
	private static SimpleDateFormat mDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

	public static TwitterUser createFromJSON(JSONObject jsonUserObject) {
		TwitterUser user = new TwitterUser();
		mDateFormat.setLenient(true);
		
		try {
			user.verified = jsonUserObject.getBoolean("verified");
			user.profileBackgroundImageUrl = new URL(jsonUserObject.getString("profile_background_image_url"));
			user.favoritesCount = jsonUserObject.getInt("favourites_count");
			user.createdAt = mDateFormat.parse(jsonUserObject.getString("created_at"));
			user.profileImageUrl = new URL(jsonUserObject.getString("profile_image_url"));
			user.description = jsonUserObject.getString("description");
			user.profileSidebarFillColor = jsonUserObject.getString("profile_sidebar_fill_color");
			user.showAllInlineMedia = jsonUserObject.getBoolean("show_all_inline_media");
			user.listedCount = jsonUserObject.getInt("listed_count");
			user.geoEnabled = jsonUserObject.getBoolean("geo_enabled");
			user.profileBackgroundTile = jsonUserObject.getBoolean("profile_background_tile");
			user.screenName = jsonUserObject.getString("screen_name");
			user.profileSidebarBorderColor = jsonUserObject.getString("profile_sidebar_border_color");
			user.contributorsEnabled = jsonUserObject.getBoolean("contributors_enabled");
			user.location = jsonUserObject.getString("location");
			user.idStr = jsonUserObject.getString("id_str");
			user.timeZone = jsonUserObject.getString("time_zone");
			user.profileBackgroundColor = jsonUserObject.getString("profile_background_color");
			user.lang = jsonUserObject.getString("lang");
			user.statusesCount = jsonUserObject.getInt("statuses_count");
			user.isProtected = jsonUserObject.getBoolean("protected");
			user.url = new URL(jsonUserObject.getString("url"));
			user.profileTextColor = jsonUserObject.getString("profile_text_color");
			user.name = jsonUserObject.getString("name");
			user.followRequestSent = jsonUserObject.getString("follow_request_sent");
			user.profileUseBackgroundImage = jsonUserObject.getBoolean("profile_use_background_image");
			user.followersCount = jsonUserObject.getInt("followers_count");
			user.id = jsonUserObject.getString("id");
			user.following = jsonUserObject.getString("following");
			user.notifications = jsonUserObject.getString("notifications");
			user.utcOffset = jsonUserObject.getString("utc_offset");
			user.friendsCount = jsonUserObject.getInt("friends_count");
			user.profileLinkColor = jsonUserObject.getString("profile_link_color");

			// Store this so we can serialize into DB. Lazy approach.
			user.setJsonUserObject(jsonUserObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public boolean isVerified() {
		return verified;
	}

	public URL getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}

	public int getFavoritesCount() {
		return favoritesCount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public URL getProfileImageUrl() {
		return profileImageUrl;
	}

	public String getDescription() {
		return description;
	}

	public String getProfileSidebarFillColor() {
		return profileSidebarFillColor;
	}

	public boolean isShowAllInlineMedia() {
		return showAllInlineMedia;
	}

	public int getListedCount() {
		return listedCount;
	}

	public boolean isGeoEnabled() {
		return geoEnabled;
	}

	public boolean isProfileBackgroundTile() {
		return profileBackgroundTile;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileSidebarBorderColor() {
		return profileSidebarBorderColor;
	}

	public boolean isContributorsEnabled() {
		return contributorsEnabled;
	}

	public String getLocation() {
		return location;
	}

	public String getIdStr() {
		return idStr;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getProfileBackgroundColor() {
		return profileBackgroundColor;
	}

	public String getLang() {
		return lang;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	public boolean isProtected() {
		return isProtected;
	}

	public URL getUrl() {
		return url;
	}

	public String getProfileTextColor() {
		return profileTextColor;
	}

	public String getName() {
		return name;
	}

	public String getFollowRequestSent() {
		return followRequestSent;
	}

	public boolean isProfileUseBackgroundImage() {
		return profileUseBackgroundImage;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public String getId() {
		return id;
	}

	public String getFollowing() {
		return following;
	}

	public String getNotifications() {
		return notifications;
	}

	public String getUtcOffset() {
		return utcOffset;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public String getProfileLinkColor() {
		return profileLinkColor;
	}

	public void setJsonUserObject(JSONObject jsonUserObject) {
		this.jsonUserObject = jsonUserObject;
	}

	public JSONObject getJsonUserObject() {
		return jsonUserObject;
	}
}
