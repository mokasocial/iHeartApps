package com.mokasocial.iheart.lib.boss;

import java.util.List;

import com.google.api.client.util.Key;

public class SearchResponse {
	@Key
	public List<Photo> resultset_images;
	@Key
	public int responsecode;
	@Key
	public String nextpage;
	@Key
	public int totalhits;
	@Key
	public int deephits;
	@Key
	public int count;
	@Key
	public int start;
}
