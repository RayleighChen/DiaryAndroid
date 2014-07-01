package com.sebox.diary.db;


import com.sebox.diary.constant.Constant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{
	private static final int VERSION=1;
	public static final String DBNAME = "diary.db";
	public DataBaseHelper (Context context){
		super(context,DBNAME,null,VERSION);
	}
	
	public DataBaseHelper (Context context,int version){
		super(context,DBNAME,null,version);
	}
	/**
	 * 该函数是子啊第一次创建数据库的时候执行，实际上是第一次
	 * 得到SQLiteDatabase对象的时候才会被调用
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(Constant.TABLE_INFO);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
