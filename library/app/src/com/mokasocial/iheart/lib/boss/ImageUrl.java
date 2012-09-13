package com.mokasocial.iheart.lib.boss;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class ImageUrl extends GenericUrl {

	/**
	 * yadda yadda
	 */
	@Key
	final String alt = "jsonc";
	@Key
	String author;
	@Key("max-results")
	Integer maxResults;

	public ImageUrl(String url) {
		super(url);
	}
}
