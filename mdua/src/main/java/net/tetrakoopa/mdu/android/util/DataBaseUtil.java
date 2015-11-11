package net.tetrakoopa.mdu.android.util;

import android.database.Cursor;

public class DataBaseUtil {
	
	public static int getInt(Cursor cursor, String columnName) {
		return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
	}
	public static long getLong(Cursor cursor, String columnName) {
		return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
	}
	public static String getString(Cursor cursor, String columnName) {
		return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
	}

}
