package com.mokasocial.iheart.lib;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.mokasocial.iheart.lib.boss.Photo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * The database driver for iHeart. Here we create, upgrade, and access
 * (save/retrieve) data from the database. We access this statically when
 * possible.
 * 
 * @author mhradek
 * 
 */
public class Database {

	private static final String TAG = "Database";

	/** GLOBAL DATABASE DEFINITIONS */
	private static final int DATABASE_VERSION = 2;

	/** TABLE DEFINITIONS */
	private static final String TABLE_MESSAGES = "messages";
	private static final String TABLE_TWEETS = "tweets";

	/** COLUMN DEFINITIONS */

	// Messages
	private static final String CL_ID = "id";
	private static final String CL_TITLE = "title";
	private static final String CL_LINK = "link";
	private static final String CL_LANGUAGE = "language";
	private static final String CL_COPYRIGHT = "copyright";
	private static final String CL_GUID = "guid";
	private static final String CL_DESCRIPTION = "description";
	private static final String CL_DATE = "date";
	private static final String CL_MEDIACONTAINER = "mediaContainer";
	private static final String CL_IMAGETITLE = "imageTitle";
	private static final String CL_IMAGEURL = "imageUrl";
	private static final String CL_FEED = "feed";
	private static final String CL_ISFAVORITE = "isFavorite";
	private static final String CL_INSERTTIME = "insertTime";

	// Tweets
	private static final String CL_COORDINATES = "coordinates";
	private static final String CL_INREPLYTOSCREENNAME = "inReplyToScreenName";
	private static final String CL_PLACE = "place";
	private static final String CL_TRUNCATED = "truncated";
	private static final String CL_INREPLYTOSTATUSID = "inReplyToStatusId";
	private static final String CL_INREPLYTOUSERID = "inReplyToUserId";
	private static final String CL_SOURCE = "source";
	private static final String CL_TWEETTEXT = "tweetText";
	private static final String CL_RETWEETED = "reTweeted";
	private static final String CL_IDSTRING = "idString";
	private static final String CL_INREPLYTOUSERIDSTRING = "inReplyToUserIdString";
	private static final String CL_INREPLYTOSTATUSIDSTRING = "inReplyToStatusIdString";
	private static final String CL_CONTRIBUTORS = "contributors";
	private static final String CL_RETWEETCOUNT = "retweetCount";
	private static final String CL_GEO = "geo";
	private static final String CL_TWEETID = "tweetId";
	private static final String CL_TWITTERUSER = "TwitterUser";
	private static final String CL_CREATEDAT = "createdAt";

	/** CONTEXT DEFINITION */
	private final Context mContext;
	private final DatabaseHelper mDatabaseHelper;
	private SQLiteDatabase mSQLiteDatabase;
	private LibApp mLibApp;
	
	private String mDatabaseName = "iHeartLib.db";

	public Database(Context context, Activity activity) {
		mLibApp = ((LibApp) activity.getApplication());
		mDatabaseName = mLibApp.getDatabaseName() + ".db";
		mContext = context;
		mDatabaseHelper = new DatabaseHelper(mContext);
	}

	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, mDatabaseName, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			createTableMessages(db);
			createTableTweets(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (newVersion > oldVersion) {
				db.beginTransaction();
				boolean success = true;

				for (int i = oldVersion; i < newVersion; ++i) {
					int nextVersion = i + 1;
					switch (nextVersion) {
					case 2:
						createTableTweets(db);
						break;
					default:
						success = false;
						break;
					}

					if (!success) {
						break;
					}
				}

				if (success) {
					db.setTransactionSuccessful();
				}

				db.endTransaction();
			} else {
				onCreate(db);
			}
		}

		private void createTableMessages(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGES + " (" + CL_ID + " INTEGER PRIMARY KEY, " + CL_TITLE + " TEXT, " + CL_LINK + " TEXT, " + CL_LANGUAGE + " INTEGER, " + CL_COPYRIGHT + " TEXT, " + CL_GUID + " TEXT UNIQUE, " + CL_DESCRIPTION + " TEXT, " + CL_DATE + " TEXT, " + CL_MEDIACONTAINER + " TEXT, " + CL_IMAGETITLE + " TEXT, " + CL_IMAGEURL + " TEXT, " + CL_FEED + " INTEGER, " + CL_ISFAVORITE + " BOOLEAN, " + CL_INSERTTIME + " INTEGER);");
		}

		private void createTableTweets(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TWEETS + " (" + CL_ID + " INTEGER PRIMARY KEY, " + CL_TWEETID + " INTEGER KEY, " + CL_COORDINATES + " TEXT, " + CL_INREPLYTOSCREENNAME + " TEXT, " + CL_PLACE + " TEXT, " + CL_TRUNCATED + " BOOLEAN, " + CL_INREPLYTOSTATUSID + " TEXT, " + CL_INREPLYTOUSERID + " TEXT, " + CL_SOURCE + " TEXT, " + CL_TWEETTEXT + " TEXT, " + CL_RETWEETED + " BOOLEAN, " + CL_IDSTRING + " TEXT, " + CL_INREPLYTOUSERIDSTRING + " TEXT, " + CL_INREPLYTOSTATUSIDSTRING + " TEXT, " + CL_CONTRIBUTORS + " TEXT, " + CL_RETWEETCOUNT + " INTEGER, " + CL_GEO + " TEXT, " + CL_TWITTERUSER + " TEXT, " + CL_CREATEDAT + " INTEGER, " + CL_ISFAVORITE + " BOOLEAN KEY, " + CL_INSERTTIME + " INTEGER);");
		}
	}

	/**
	 * Open a connection to the database as defined in the Database class.
	 * 
	 * @return Database handle
	 */
	private Database open() throws SQLException {
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close the open database handle.
	 */
	private void close() {
		mDatabaseHelper.close();
	}

	/**
	 * Get the last insert time of the feed.
	 * 
	 * @param context
	 * @param feedType
	 * @return long
	 */
	public static long getLastInsertTimeByFeed(Context context, Activity activity, FeedType feedType) {
		Database dbObj = new Database(context, activity);
		dbObj.open();

		Cursor result = dbObj.mSQLiteDatabase.query(TABLE_MESSAGES, new String[] { CL_INSERTTIME }, TABLE_MESSAGES + "." + CL_FEED + " = '" + feedType.toString() + "'", null, null, null, TABLE_MESSAGES + "." + CL_INSERTTIME + " DESC");

		if (result.getCount() < 1) {
			result.close();
			dbObj.close();
			return -1;
		}

		result.moveToFirst();
		long temp = result.getLong(result.getColumnIndexOrThrow(CL_INSERTTIME));

		result.close();
		dbObj.close();
		return temp;
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param photo
	 * @return
	 */
	public static boolean isPhotoFaved(Context context, Photo photo) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param tweet
	 * @return
	 * @throws IOException
	 */
	public static int saveTweet(Context context, Activity activity, Tweet tweet) throws IOException {
		Database dbObj = new Database(context, activity);
		dbObj.open();

		final ContentValues values = new ContentValues();
		values.put(CL_CONTRIBUTORS, tweet.getContributors());
		values.put(CL_COORDINATES, tweet.getCoordinates());
		values.put(CL_CREATEDAT, tweet.getCreatedAt().toString());
		values.put(CL_TWEETID, tweet.getId());
		values.put(CL_IDSTRING, tweet.getIdString());
		values.put(CL_INREPLYTOSCREENNAME, tweet.getInReplyToScreenName());
		values.put(CL_INREPLYTOSTATUSID, tweet.getInReplyToStatusId());
		values.put(CL_INREPLYTOSTATUSIDSTRING, tweet.getInReplyToStatusIdString());
		values.put(CL_INREPLYTOUSERID, tweet.getInReplyToUserId());
		values.put(CL_INREPLYTOUSERIDSTRING, tweet.getInReplyToUserIdString());
		values.put(CL_PLACE, tweet.getPlace());
		values.put(CL_RETWEETCOUNT, tweet.getRetweetCount());
		values.put(CL_RETWEETED, tweet.getReTweeted());
		values.put(CL_SOURCE, tweet.getSource());
		values.put(CL_TWEETTEXT, tweet.getText());
		values.put(CL_TRUNCATED, tweet.isTruncated());

		TwitterUser userObj = tweet.getUser();
		values.put(CL_TWITTERUSER, userObj.getJsonUserObject().toString());
		values.put(CL_ISFAVORITE, tweet.isFavorite());

		values.put(CL_INSERTTIME, System.currentTimeMillis());

		int rowId = (int) dbObj.mSQLiteDatabase.replace(TABLE_TWEETS, null, values);
		dbObj.close();

		return rowId;
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param tweet
	 * @return
	 */
	public static boolean isTweetFaved(Context context, Activity activity, Tweet tweet) {
		Database dbObj = new Database(context, activity);
		dbObj.open();

		Cursor result = dbObj.mSQLiteDatabase.query(TABLE_TWEETS, new String[] { CL_ID }, TABLE_TWEETS + "." + CL_IDSTRING + " = " + DatabaseUtils.sqlEscapeString(tweet.getIdString()) + " AND " + TABLE_TWEETS + "." + CL_ISFAVORITE + " = '1'", null, null, null, null);

		if (result.getCount() < 1) {
			result.close();
			dbObj.close();
			return false;
		}

		result.close();
		dbObj.close();
		return true;
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param tweet
	 */
	public static void removeTweet(Context context, Activity activity, Tweet tweet) {
		Database dbObj = new Database(context, activity);
		dbObj.open();

		dbObj.mSQLiteDatabase.delete(TABLE_TWEETS, TABLE_TWEETS + "." + CL_IDSTRING + " = " + DatabaseUtils.sqlEscapeString(tweet.getIdString()) + " AND " + TABLE_TWEETS + "." + CL_ISFAVORITE + " = '1'", null);

		dbObj.close();
	}

	/**
	 * 
	 * 
	 * @param context
	 * @return ArrayList<Tweet>
	 */
	public static ArrayList<Tweet> loadFavedTweets(Context context, Activity activity) {
		Database dbObj = new Database(context, activity);
		dbObj.open();

		Cursor result = dbObj.mSQLiteDatabase.query(TABLE_TWEETS, null, TABLE_TWEETS + "." + CL_ISFAVORITE + " = '1'", null, null, null, null);

		ArrayList<Tweet> tweets = new ArrayList<Tweet>();

		if (result.getCount() < 1) {
			result.close();
			dbObj.close();
			return tweets;
		}

		result.moveToFirst();
		while (result.isAfterLast() == false) {
			Tweet tweet = new Tweet();

			tweet.setContributors(result.getString(result.getColumnIndexOrThrow(CL_CONTRIBUTORS)));
			tweet.setCoordinates(result.getString(result.getColumnIndexOrThrow(CL_COORDINATES)));
			String createdAt = result.getString(result.getColumnIndexOrThrow(CL_CREATEDAT));

			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
				dateFormat.setLenient(false);
				tweet.setCreatedAt(dateFormat.parse(createdAt));
			} catch (ParseException e) {
				Log.e(TAG, "Unable to parse date [" + createdAt + "]", e);
			}

			tweet.setGeo(result.getString(result.getColumnIndexOrThrow(CL_GEO)));
			tweet.setId(result.getInt(result.getColumnIndexOrThrow(CL_TWEETID)));
			tweet.setIdString(result.getString(result.getColumnIndexOrThrow(CL_IDSTRING)));
			tweet.setInReplyToScreenName(result.getString(result.getColumnIndexOrThrow(CL_INREPLYTOSCREENNAME)));
			tweet.setInReplyToStatusId(result.getString(result.getColumnIndexOrThrow(CL_INREPLYTOSTATUSID)));
			tweet.setInReplyToStatusIdString(result.getString(result.getColumnIndexOrThrow(CL_INREPLYTOSTATUSIDSTRING)));
			tweet.setInReplyToUserId(result.getString(result.getColumnIndexOrThrow(CL_INREPLYTOUSERID)));
			tweet.setInReplyToUserIdString(result.getString(result.getColumnIndexOrThrow(CL_INREPLYTOUSERIDSTRING)));
			tweet.setPlace(result.getString(result.getColumnIndexOrThrow(CL_PLACE)));
			tweet.setRetweetCount(result.getInt(result.getColumnIndexOrThrow(CL_RETWEETCOUNT)));
			tweet.setReTweeted("1".equals(result.getInt(result.getColumnIndexOrThrow(CL_RETWEETED))));
			tweet.setSource(result.getString(result.getColumnIndexOrThrow(CL_SOURCE)));
			tweet.setText(result.getString(result.getColumnIndexOrThrow(CL_TWEETTEXT)));
			tweet.setTruncated("1".equals(result.getInt(result.getColumnIndexOrThrow(CL_TRUNCATED))));

			try {
				String userString = result.getString(result.getColumnIndexOrThrow(CL_TWITTERUSER));
				JSONObject userObj = new JSONObject(userString);
				tweet.setUser(TwitterUser.createFromJSON(userObj));
			} catch (JSONException e) {
				throw new RuntimeException();
			}

			tweet.setFavorite(true);
			tweets.add(tweet);

			result.moveToNext();
		}

		result.close();
		dbObj.close();
		return tweets;
	}
}
