package com.mokasocial.iheart.lib;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class Message {

	private String title;
	private String link;
	private String guid;
	private String pubDate;
	private String description;
	
	private URL imageUrl;
		
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public URL findImageUrl() {
		if (imageUrl == null){
			try {
				Pattern imageUrlPattern = Pattern.compile("[^\"]*jpg");
				Matcher urlMatcher = imageUrlPattern.matcher(description);
				if (urlMatcher.find()) {
					String foundUrl = urlMatcher.group();
					Log.d("Message", foundUrl);
					imageUrl = new URL(foundUrl);
				}				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		return imageUrl;
	}
	
}