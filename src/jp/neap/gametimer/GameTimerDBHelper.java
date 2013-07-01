/**
 * データベースヘルパー
 */
package jp.neap.gametimer;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.LinkedList;
import java.util.List;


public class GameTimerDBHelper extends SQLiteOpenHelper {

	public static final String DB_FILENAME = "snsgametimer.db";

	public static final int DB_VERSION = 2;

	public static final String TABLE_NAME_SETTINGS = "gametimer_settings";

	public static final String TABLE_NAME_PREFERENCE = "gametimer_preference";

	private Context context;

	public GameTimerDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context; 
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(
				"CREATE TABLE " + TABLE_NAME_SETTINGS + " " +
				"(id INTEGER NOT NULL" +
				",notify_text TEXT NOT NULL" +
				",notify_datetime INTEGER NOT NULL" +
				",later_minute INTEGER INT NOT NULL" +
				",later_hourminute_hour INTEGER INT NOT NULL" +
				",later_hourminute_minute INTEGER INT NOT NULL" +
				",at_time_day INTEGER INT NOT NULL" +
				",at_time_hour INTEGER INT NOT NULL" +
				",at_time_minute INTEGER INT NOT NULL" +
				",now_point INTEGER INT NOT NULL" +
				",full_point INTEGER INT NOT NULL" +
				",restoration_interval_time INTEGER INT NOT NULL" +
				",restoration_point INTEGER INT NOT NULL" +
				",sns_type INTEGER INT NOT NULL" +
				",sort_order INTEGER INT NOT NULL" +
				",private_audio_file INTEGER NOT NULL" +
				",audio_file_long_path TEXT NOT NULL" +
				",audio_file_short_path TEXT NOT NULL" +
				",sns_url TEXT" + // 2013-02-26追加
				")"
		);
		db.execSQL("DELETE FROM " + TABLE_NAME_SETTINGS);

		final String text1 = context.getString(R.string.game_1);
		final String text2 = context.getString(R.string.game_2);
		final String text3 = context.getString(R.string.game_3);
		final String noodle = context.getString(R.string.noodle);
		
		// 初期値
		final String[][] initialValues = {
			// id, notify_text, notify_datetime, later_minute, later_hourminute_hour, later_hourminute_minute, at_time_day, at_time_hour, at_time_minute, now_point, full_point, restoration_interval_time, restoration_point, sns_type,                                             sort_order, private_audio_file, audio_file_long_path, audio_file_short_path
			{"1",  text1,        "0",            "60",          "0",                   "0",                     "0",         "0",          "0",            "0",      "0",        "0",                       "0",               String.valueOf(GameTimerSettingsBean.SNSTYPE_OTHER),   "0",       "0",                 "",                   ""},
			{"2",  text2,        "0",            "0",           "1",                   "30",                    "0",         "0",          "0",            "0",      "0",        "0",                       "0",               String.valueOf(GameTimerSettingsBean.SNSTYPE_OTHER),   "1",       "0",                 "",                   ""},
			{"3",  text3,        "0",            "0",           "0",                   "0",                     "0",         "13",         "0",           "0",      "0",        "0",                       "0",               String.valueOf(GameTimerSettingsBean.SNSTYPE_OTHER),   "2",        "0",                "",                   ""},
			{"4",  noodle,       "0",            "3",           "0",                   "0",                     "0",         "0",          "0",            "0",      "0",        "0",                       "0",               String.valueOf(GameTimerSettingsBean.SNSTYPE_OTHER),   "3",       "0",                 "",                   ""},
			{"5",  "",           "0",            "0",           "0",                   "0",                     "0",         "0",          "0",            "0",      "0",        "0",                       "0",               String.valueOf(GameTimerSettingsBean.SNSTYPE_OTHER),   "4",        "0",                 "",                   ""},
			{"6",  "",           "0",            "0",           "0",                   "0",                     "0",         "0",          "0",            "0",      "0",        "0",                       "0",               String.valueOf(GameTimerSettingsBean.SNSTYPE_OTHER),   "5",        "0",                "",                   ""},
			{"7",  "",           "0",            "0",           "0",                   "0",                     "0",         "0",          "0",            "0",      "0",        "0",                       "0",               String.valueOf(GameTimerSettingsBean.SNSTYPE_OTHER),   "6",        "0",                "",                   ""},
			{"8",  "",           "0",            "0",           "0",                   "0",                     "0",         "0",          "0",            "0",      "0",        "0",                       "0",               String.valueOf(GameTimerSettingsBean.SNSTYPE_OTHER),   "7",        "0",                "",                   ""},
			{"9",  "",           "0",            "0",           "0",                   "0",                     "0",         "0",          "0",            "0",      "0",        "0",                       "0",               String.valueOf(GameTimerSettingsBean.SNSTYPE_OTHER),   "8",        "0",                "",                   ""},
			{"10", "",           "0",            "0",           "0",                   "0",                     "0",         "0",          "0",            "0",      "0",        "0",                       "0",               String.valueOf(GameTimerSettingsBean.SNSTYPE_OTHER),   "9",        "0",                "",                   ""},
		};

		for (int paramIndex = 0 ; paramIndex < initialValues.length ; paramIndex++) {
			db.execSQL("INSERT INTO " + TABLE_NAME_SETTINGS + " " +
					"(id" +
					",notify_text" +
					",notify_datetime" +
					",later_minute" +
					",later_hourminute_hour" +
					",later_hourminute_minute" +
					",at_time_day" +
					",at_time_hour" +
					",at_time_minute" +
					",now_point" +
					",full_point" +
					",restoration_interval_time" +
					",restoration_point" +
					",sns_type" +
					",sort_order" +
					",private_audio_file" +
					",audio_file_long_path" +
					",audio_file_short_path" +
					",sns_url" +
					") VALUES (" +
						initialValues[paramIndex][0] +				// id
						",'" + initialValues[paramIndex][1] + "'" +	// notify_text
						"," + initialValues[paramIndex][2] +		// notify_datetime
						"," + initialValues[paramIndex][3] +		// later_minute
						"," + initialValues[paramIndex][4] +		// later_hourminute_hour
						"," + initialValues[paramIndex][5] +		// later_hourminute_minute
						"," + initialValues[paramIndex][6] +		// at_time_day
						"," + initialValues[paramIndex][7] +		// at_time_hour
						"," + initialValues[paramIndex][8] +		// at_time_minute
						"," + initialValues[paramIndex][9] +		// now_point
						"," + initialValues[paramIndex][10] +		// full_point
						"," + initialValues[paramIndex][11] +		// restoration_interval_time
						"," + initialValues[paramIndex][12] +		// restoration_point
						"," + initialValues[paramIndex][13] +		// sns_type
						"," + initialValues[paramIndex][14] +		// sort_order
						"," + initialValues[paramIndex][15] +		// private_audio_file
						",'" + initialValues[paramIndex][16] + "'" +// audio_file_long_path
						",'" + initialValues[paramIndex][17] + "'" +// audio_file_short_path
						",''" +										// sns_type
						")");
		}

		db.execSQL(
				"CREATE TABLE " + TABLE_NAME_PREFERENCE + " " +
				"(property_name TEXT NOT NULL" +
				",property_value TEXT NOT NULL)"
		);
		db.execSQL("DELETE FROM " + TABLE_NAME_PREFERENCE);
		db.execSQL("INSERT INTO " + TABLE_NAME_PREFERENCE + " " +
				"(property_name" +
				",property_value) " +
				"VALUES (" +
					"'audioFileFullPath'" +
					",''" +
					")");
		db.execSQL("INSERT INTO " + TABLE_NAME_PREFERENCE + " " +
				"(property_name" +
				",property_value) " +
				"VALUES (" +
					"'audioFileShortPath'" +
					",''" +
					")");
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1) {
			if (newVersion == 2) {
				db.execSQL(
						"ALTER TABLE " + TABLE_NAME_SETTINGS + " " +
						" ADD COLUMN sns_url TEXT");
				db.execSQL("UPDATE " + TABLE_NAME_SETTINGS + " SET sns_url=''");
			}
		}
	}

	public List<GameTimerSettingsBean> listAllSorted(SQLiteDatabase db) {
		final List<GameTimerSettingsBean> beanList = new LinkedList<GameTimerSettingsBean>();
		Cursor c = db.rawQuery(
						"SELECT" +
						" id" +
						",notify_text" +
						",notify_datetime" +
						",later_minute" +
						",later_hourminute_hour" +
						",later_hourminute_minute" +
						",at_time_day" +
						",at_time_hour" +
						",at_time_minute" +
						",sns_type" +
						",sort_order" +
						",sns_url" +
						" FROM " + TABLE_NAME_SETTINGS +
						" ORDER BY sort_order ASC",
						new String[0]);
		try {
			if (c.moveToFirst()) {
				do {
					beanList.add(new GameTimerSettingsBean(
							c.getInt(0),
							c.getString(1),
							c.getLong(2),
							c.getInt(3),
							c.getInt(4),
							c.getInt(5),
							c.getInt(6),
							c.getInt(7),
							c.getInt(8),
							c.getInt(9),
							c.getInt(10),
							c.getString(11),
							context.getString(R.string.days_later),
							context.getString(R.string.minutes_later),
							context.getString(R.string.hours)));
				} while (c.moveToNext());
			}
			return beanList;
		}
		finally {
			c.close();
		}
	}

	public GameTimerSettingsBean getRecordById(SQLiteDatabase db, int id) {
		final String[] params = new String[1];
		params[0] = String.valueOf(id);
		Cursor c = db.rawQuery(
						"SELECT" +
						" id" +
						",notify_text" +
						",notify_datetime" +
						",later_minute" +
						",later_hourminute_hour" +
						",later_hourminute_minute" +
						",at_time_day" +
						",at_time_hour" +
						",at_time_minute" +
						",sns_type" +
						",sort_order" +
						",sns_url" +
						" FROM " + TABLE_NAME_SETTINGS +
						" WHERE id=?",
						params);
		try {
			if (c.moveToFirst()) {
				GameTimerSettingsBean bean = new GameTimerSettingsBean(
							c.getInt(0),
							c.getString(1),
							c.getLong(2),
							c.getInt(3),
							c.getInt(4),
							c.getInt(5),
							c.getInt(6),
							c.getInt(7),
							c.getInt(8),
							c.getInt(9),
							c.getInt(10),
							c.getString(11),
							context.getString(R.string.days_later),
							context.getString(R.string.minutes_later),
							context.getString(R.string.hours));
				return bean;
			}
			return null;
		}
		finally {
			c.close();
		}
	}

	public GameTimerSettingsBean getRecordBySortOrder(SQLiteDatabase db, int sortOrder) {
		final String[] params = new String[1];
		params[0] = String.valueOf(sortOrder);
		Cursor c = db.rawQuery(
						"SELECT" +
						" id" +
						",notify_text" +
						",notify_datetime" +
						",later_minute" +
						",later_hourminute_hour" +
						",later_hourminute_minute" +
						",at_time_day" +
						",at_time_hour" +
						",at_time_minute" +
						",sns_type" +
						",sort_order" +
						",sns_url" +
						" FROM " + TABLE_NAME_SETTINGS +
						" WHERE sort_order=?",
						params);
		try {
			if (c.moveToFirst()) {
				GameTimerSettingsBean bean = new GameTimerSettingsBean(
							c.getInt(0),
							c.getString(1),
							c.getLong(2),
							c.getInt(3),
							c.getInt(4),
							c.getInt(5),
							c.getInt(6),
							c.getInt(7),
							c.getInt(8),
							c.getInt(9),
							c.getInt(10),
							c.getString(11),
							context.getString(R.string.days_later),
							context.getString(R.string.minutes_later),
							context.getString(R.string.hours));
				return bean;
			}
			return null;
		}
		finally {
			c.close();
		}
	}

	public void updateRecord(SQLiteDatabase db, GameTimerSettingsBean bean) {
		final ContentValues assoc = new ContentValues();

		assoc.put("notify_text", bean.notifyText());
		assoc.put("notify_datetime", new Long(bean.notifyDateTime()));
		assoc.put("later_minute", new Integer(bean.laterMinute()));
		assoc.put("later_hourminute_hour", new Integer(bean.laterHourMinuteHour()));
		assoc.put("later_hourminute_minute", new Integer(bean.laterHourMinuteMinute()));
		assoc.put("at_time_day", new Integer(bean.atTimeDay()));
		assoc.put("at_time_hour", new Integer(bean.atTimeHour()));
		assoc.put("at_time_minute", new Integer(bean.atTimeMinute()));
		assoc.put("sns_type", new Integer(bean.snsType()));
		assoc.put("sort_order", new Integer(bean.sortOrder()));
		assoc.put("sns_url", bean.snsUrl());

		db.update(TABLE_NAME_SETTINGS, assoc, "id=?", new String[]{new Integer(bean.id()).toString()});
	}

	public void updateSortOrder(SQLiteDatabase db, int id, int sortOrder) {
		final ContentValues assoc = new ContentValues();

		assoc.put("sort_order", new Integer(sortOrder));

		db.update(TABLE_NAME_SETTINGS, assoc, "id=?", new String[]{new Integer(id).toString()});
	}

	public String getProperty(SQLiteDatabase db, String propertyName, String defaultValue) {
		final String[] params = new String[1];
		params[0] = propertyName;
		Cursor c = db.rawQuery(
						"SELECT" +
						" property_value" +
						" FROM " + TABLE_NAME_PREFERENCE +
						" WHERE property_name=?",
						params);
		try {
			if (c.moveToFirst()) {
				return c.getString(0);
			}
			return defaultValue;
		}
		finally {
			c.close();
		}
	}

	public void setProperty(SQLiteDatabase db, String propertyName, String propertyValue) {
		if (hasProperty(db, propertyName)) {
			final ContentValues assoc = new ContentValues();
			assoc.put("property_value", propertyValue);
			db.update(TABLE_NAME_PREFERENCE, assoc, "property_name=?", new String[]{propertyName});
		}
		else {
			db.execSQL("INSERT INTO " + TABLE_NAME_PREFERENCE + " " +
					"(property_name" +
					",property_value) " +
					"VALUES (" +
						"'" + propertyName + "'" +
						",'" + propertyValue + "'" +
						")");
		}
	}

	public boolean hasProperty(SQLiteDatabase db, String propertyName) {
		final String[] params = new String[1];
		params[0] = propertyName;
		Cursor c = db.rawQuery(
						"SELECT" +
						" property_value" +
						" FROM " + TABLE_NAME_PREFERENCE +
						" WHERE property_name=?",
						params);
		try {
			return c.moveToFirst();
		}
		finally {
			c.close();
		}
	}
}
