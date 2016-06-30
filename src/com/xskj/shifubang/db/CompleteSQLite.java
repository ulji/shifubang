package com.xskj.shifubang.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CompleteSQLite extends SQLiteOpenHelper {

	private final static String SQL_NAME = "complete_Data.db";
	private final static String TABLE_NAME = "complete_data";
	private final static int VERSION = 2;

	public CompleteSQLite(Context context) {
		super(context, SQL_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table "
				+ TABLE_NAME
				+ "(id Integer primary key autoincrement,sendAddress text,orderCreateTime text,column1 text,column3 text,orderId text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion < newVersion) {
			db.execSQL("drop table " + TABLE_NAME);
			onCreate(db);
		}
	}

	// 增删改查方法

	public void insert(String sendAddress, String orderCreateTime, String column1,String column3,String orderId) {

		SQLiteDatabase sd = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("sendAddress", sendAddress);
		cv.put("orderCreateTime", orderCreateTime);
		cv.put("column1", column1);
		cv.put("column3", column3);
		cv.put("orderId", orderId);
		sd.insert(TABLE_NAME, null, cv);
	}

	public void delete(String id) {
		SQLiteDatabase sd = getWritableDatabase();
		String whereClause = "id = ?";
		String[] whereArgs = { id };

		sd.delete(TABLE_NAME, whereClause, whereArgs);
	}

	public void update(String description, String destination, String id) {
		SQLiteDatabase sd = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("description", description);
		cv.put("destination", destination);
	
		String whereClause = "id = ?";
		String[] whereArgs = { id };
		sd.update(TABLE_NAME, cv, whereClause, whereArgs);
	}
	public void updateStatus(String status,String orderId) {
		SQLiteDatabase sd = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("status", status);
		String whereClause = "orderId = ?";
		String[] whereArgs = { orderId };
		sd.update(TABLE_NAME, cv, whereClause, whereArgs);
	}

	public Cursor query() {
		SQLiteDatabase sd = getReadableDatabase();
		return sd.query(TABLE_NAME, null, null, null, null, null, null);
	}

	public void deleteAll() {
		SQLiteDatabase sd = getWritableDatabase();

		String whereClause = "";
		String[] whereArgs = {};

		sd.delete(TABLE_NAME, whereClause, whereArgs);
	}
}