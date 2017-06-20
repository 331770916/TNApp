package com.tpyzq.mobile.pangu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tpyzq.mobile.pangu.util.ConstantUtil;

/**
 *
 */

public class MyDbHelper extends SQLiteOpenHelper {

    private static int version = 1;
    private static final String TAG = "MyDbHelper";

    public MyDbHelper(Context context) {
        super(context, ConstantUtil.DB_FILE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(newVersion){
            case 1:
                Log.e(TAG, "update DB start !!!!");
//                db.execSQL("v1版本时数据库升级操作"); //旧版本的数据库神级操作的case语句后都没有break语句，
                //这样便可以确保每一次升级当前版本到最新版本的更新操作都可以被执行到了
            case 2:
                Log.e(TAG, "update DB start !!!!");
//                db.execSQL("v1——v2版本数据库升级操作");
            case 3:
                Log.e(TAG, "update DB start !!!!");
//                db.execSQL("v2——v3版本数据库升级操作");
                //v3版本为当前最新数据库版本
                break;
        }
    }
}
