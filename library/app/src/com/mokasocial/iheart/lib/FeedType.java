package com.mokasocial.iheart.lib;

import java.io.Serializable;

public class FeedType implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int NEWS_TYPE = 0;
	private static final int PHOTOS_TYPE = 1;
	private static final int VIDEOS_TYPE = 2;

	public static final FeedType NEWS = new FeedType(NEWS_TYPE);
	public static final FeedType PHOTOS = new FeedType(PHOTOS_TYPE);
	public static final FeedType VIDEOS = new FeedType(VIDEOS_TYPE);

	private int feedType;

	private FeedType(int type) {
		this.setFeedType(type);
	}

	/**
	 * @param feedType
	 *            the feedType to set
	 */
	private void setFeedType(int feedType) {
		this.feedType = feedType;
	}

	/**
	 * @return the feedType
	 */
	public int getFeedType() {
		return feedType;
	}

	/**
	 * Get the type based on the int type.
	 * 
	 * @param feedType
	 * @return
	 */
	public static FeedType fromFeedType(int feedType) {
		switch (feedType) {
		case NEWS_TYPE:
			return NEWS;
		case PHOTOS_TYPE:
			return PHOTOS;
		case VIDEOS_TYPE:
			return VIDEOS;
		default:
			throw new IllegalArgumentException("Unsupported feed type: " + feedType);
		}
	}

	/**
	 * Get the type based on the string type, then converted to int.
	 * 
	 * @see fromFeedType(int)
	 * @param feedType
	 * @return
	 */
	public static FeedType fromFeedType(String feedType) {
		return fromFeedType(Integer.parseInt(feedType));
	}

	/**
	 * Return a human readable string representing this instantiated object's
	 * type.
	 * 
	 * 
	 */
	@Override
	public String toString() {
		switch (feedType) {
		case NEWS_TYPE:
			return "news";
		case PHOTOS_TYPE:
			return "photos";
		case VIDEOS_TYPE:
			return "videos";
		}

		return super.toString();
	}
}
