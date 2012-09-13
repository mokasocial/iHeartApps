package com.mokasocial.iheart.lib.parsers;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.mokasocial.iheart.lib.Message;

import android.util.Log;

public class GoogleNewsParser extends BaseFeedParser {
	
	// for a feed like http://www.usmagazine.com/hotpics/rss

	private final static String TAG = "GoogleNewsParser";

	public GoogleNewsParser(String[] feedUrl) {
		super(feedUrl);
	}

	public ArrayList<Message> parse() {
		ArrayList<Message> messages = new ArrayList<Message>();
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			
			for(InputStream is : this.getInputStreams()) {
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(is, "utf-8");
				// auto-detect the encoding from the stream
				int eventType = parser.getEventType();
				Message currentMessage = null;
				boolean done = false;
				while (eventType != XmlPullParser.END_DOCUMENT && !done){
					String name = null;
					switch (eventType){
						case XmlPullParser.START_DOCUMENT:
							Log.i(TAG, "Start parsing document");
							break;
						case XmlPullParser.START_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(ITEM)){
								// start of a new node
								currentMessage = new Message();
							} else if (currentMessage != null){
								// add a property to a current node
								if (name.equalsIgnoreCase(TITLE)){
									currentMessage.setTitle(parser.nextText());
								} else if (name.equalsIgnoreCase(DESCRIPTION)) {
									currentMessage.setDescription(parser.nextText());
								} else if (name.equalsIgnoreCase(PUB_DATE)) {
									currentMessage.setPubDate(parser.nextText());
								} else if (name.equalsIgnoreCase(LINK)) {
									currentMessage.setLink(parser.nextText());
								} else if (name.equalsIgnoreCase(GUID)) {
									currentMessage.setGuid(parser.nextText());
								} else {
									Log.w(TAG, "Unused name while parsing: " + name);
								}
							}
							break;
						case XmlPullParser.END_TAG:
							name = parser.getName();
							if (name.equalsIgnoreCase(ITEM) && currentMessage != null){
								messages.add(currentMessage);
							} else if (name.equalsIgnoreCase(CHANNEL)){
								done = true;
							}
							break;
					}
					
					eventType = parser.next();
				}
			}
			
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			throw new RuntimeException(e);
		}
		
		return messages;
	}
}
