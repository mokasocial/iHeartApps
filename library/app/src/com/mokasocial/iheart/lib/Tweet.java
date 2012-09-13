package com.mokasocial.iheart.lib;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class Tweet implements Comparable<Tweet>, Cloneable {

	private boolean favorite; // faved on iHeart

	private String coordinates;
	private String inReplyToScreenName;
	private String place;
	private boolean truncated;
	private String inReplyToStatusId;
	private String inReplyToUserId;
	private String source;
	private String text;
	private boolean reTweeted;
	private String idString;
	private String inReplyToUserIdString;
	private String inReplyToStatusIdString;
	private String contributors;
	private int retweetCount;
	private String geo;
	private int id;
	private TwitterUser user;
	private Date createdAt;
	
	private static SimpleDateFormat mDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

	public Tweet() {
		favorite = false;
	}

	/**
	 * Implementation of Comparable interface
	 */
	public int compareTo(Tweet another) {
		if (another == null) {
			return 1;
		}
		return another.createdAt.compareTo(createdAt);
	}

	/**
	 * Implementation of Cloneable interface
	 */
	@Override
	public Tweet clone() {
		try {
			return (Tweet) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public static Tweet createFromTwitterJSON(JSONObject jsonTweet) {
		Tweet msg = new Tweet();
		mDateFormat.setLenient(true);

		try {
			msg.setCoordinates(jsonTweet.getString("coordinates"));
			msg.setInReplyToUserId(jsonTweet.getString("in_reply_to_user_id"));
			msg.setInReplyToStatusId(jsonTweet.getString("in_reply_to_status_id"));
			msg.setTruncated(jsonTweet.getBoolean("truncated"));
			msg.setCreatedAt(mDateFormat.parse(jsonTweet.getString("created_at")));
			msg.setPlace(jsonTweet.getString("place"));
			msg.setInReplyToScreenName(jsonTweet.getString("in_reply_to_screen_name"));
			msg.setSource(jsonTweet.getString("source"));
			msg.setInReplyToStatusIdString(jsonTweet.getString("in_reply_to_status_id_str"));
			msg.setContributors(jsonTweet.getString("contributors"));
			String retweetCount = jsonTweet.getString("retweet_count");
			msg.setRetweetCount(Integer.parseInt((retweetCount == "null") ? "0" : retweetCount));
			msg.setInReplyToUserIdString(jsonTweet.getString("in_reply_to_user_id_str"));
			msg.setIdString(jsonTweet.getString("id_str"));
			msg.setReTweeted(jsonTweet.getBoolean("retweeted"));
			msg.setGeo(jsonTweet.getString("geo"));
			msg.setId(jsonTweet.getInt("id"));
			msg.setText(jsonTweet.getString("text"));
			msg.setUser(TwitterUser.createFromJSON(jsonTweet.getJSONObject("user")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}

	public void setUser(TwitterUser user) {
		this.user = user;
	}

	public TwitterUser getUser() {
		return user;
	}

	public void setCreatedAt(Date date) {
		this.createdAt = date;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public URL getLink() {
		try {
			return new URL("http://twitter.com/" + user.getScreenName() + "/statuses/" + idString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean b) {
		this.favorite = true;
	}

	public URL getIconUrl() {
		return user.getProfileImageUrl();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPlace() {
		return place;
	}

	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	public boolean isTruncated() {
		return truncated;
	}

	public void setInReplyToStatusId(String inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public String getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public void setInReplyToUserId(String inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	public String getInReplyToUserId() {
		return inReplyToUserId;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setReTweeted(boolean reTweeted) {
		this.reTweeted = reTweeted;
	}

	public boolean getReTweeted() {
		return reTweeted;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setInReplyToUserIdString(String inReplyToUserIdString) {
		this.inReplyToUserIdString = inReplyToUserIdString;
	}

	public String getInReplyToUserIdString() {
		return inReplyToUserIdString;
	}

	public void setInReplyToStatusIdString(String inReplyToStatusIdString) {
		this.inReplyToStatusIdString = inReplyToStatusIdString;
	}

	public String getInReplyToStatusIdString() {
		return inReplyToStatusIdString;
	}

	public void setContributors(String contributors) {
		this.contributors = contributors;
	}

	public String getContributors() {
		return contributors;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setGeo(String geo) {
		this.geo = geo;
	}

	public String getGeo() {
		return geo;
	}

	public void setIdString(String id) {
		this.idString = id;
	}

	public String getIdString() {
		return idString;
	}
}
