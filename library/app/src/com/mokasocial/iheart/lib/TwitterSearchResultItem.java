package com.mokasocial.iheart.lib;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

/**
 * 
 * @author Michael Hradek
 * 
 * @see http://search.twitter.com/search.json?q=twitter
 * <pre>
{
"from_user_id_str":"160953403",
"profile_image_url":"http://a2.twimg.com/profile_images/1201256078/aldsfuflsaflkds_normal.jpg",
"created_at":"Wed, 29 Dec 2010 01:54:23 +0000",
"from_user":"itshannahxo",
"id_str":"19934220839288832",
"metadata":{"result_type":"recent"},
"to_user_id":null,
"text":"It sorta looks like I'm bald on the sides of my head on my Twitter picture. WTF. lmfaooo.",
"id":19934220839288832,
"from_user_id":160953403,
"geo":null,
"iso_language_code":"en",
"to_user_id_str":null,
"source":"&lt;a href=&quot;http://twitter.com/&quot;&gt;web&lt;/a&gt;"
},</pre>
 *
 */
public class TwitterSearchResultItem implements Comparable<TwitterSearchResultItem>, Cloneable {

	private String fromUserIdStr;
	private URL profileImageUrl;
	private Date createdAt;
	private String fromUser;
	private String idStr;
	private String resultType;
	private String toUserId;
	private String text;
	private String id;
	private String fromUserId;
	private String geo;
	private String isoLanguageCode;
	private String toUserIdStr;
	private String source;

	// Example: Wed, 29 Dec 2010 02:20:24 +0000
	private static SimpleDateFormat mDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

	/**
	 * Implementation of Comparable interface
	 */
	public int compareTo(TwitterSearchResultItem another) {
		if (another == null) {
			return 1;
		}
		return another.createdAt.compareTo(createdAt);
	}

	/**
	 * Implementation of Cloneable interface
	 */
	@Override
	public TwitterSearchResultItem clone() {
		try {
			return (TwitterSearchResultItem) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public static TwitterSearchResultItem createFromTwitterJSON(JSONObject jsonObj) {
		TwitterSearchResultItem msg = new TwitterSearchResultItem();
		mDateFormat.setLenient(true);
			
		try {
			msg.setCreatedAt(mDateFormat.parse(jsonObj.getString("created_at")));
			msg.setFromUser(jsonObj.getString("from_user"));
			msg.setFromUserId(jsonObj.getString("from_user_id"));
			msg.setFromUserIdStr(jsonObj.getString("from_user_id_str"));
			msg.setGeo(jsonObj.getString("geo"));
			msg.setId(jsonObj.getString("id"));
			msg.setIdStr(jsonObj.getString("id_str"));
			// Apparently not always set
			// msg.setIsoLanguageCode(jsonObj.getString("iso_language_code"));
			msg.setProfileImageUrl(new URL(jsonObj.getString("profile_image_url")));
			
			// For now just the basic search item result meta. There can be more.
			JSONObject metaObj = jsonObj.getJSONObject("metadata");
			msg.setResultType(metaObj.getString("result_type"));
			
			msg.setSource(jsonObj.getString("source"));
			msg.setText(jsonObj.getString("text"));
			msg.setToUserId(jsonObj.getString("to_user_id"));
			msg.setToUserIdStr(jsonObj.getString("to_user_id_str"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return msg;
	}
	
	public String getFromUserIdStr() {
		return fromUserIdStr;
	}

	public void setFromUserIdStr(String fromUserIdStr) {
		this.fromUserIdStr = fromUserIdStr;
	}

	public URL getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(URL profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getGeo() {
		return geo;
	}

	public void setGeo(String geo) {
		this.geo = geo;
	}

	public String getIsoLanguageCode() {
		return isoLanguageCode;
	}

	public void setIsoLanguageCode(String isoLanguageCode) {
		this.isoLanguageCode = isoLanguageCode;
	}

	public String getToUserIdStr() {
		return toUserIdStr;
	}

	public void setToUserIdStr(String toUserIdStr) {
		this.toUserIdStr = toUserIdStr;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
