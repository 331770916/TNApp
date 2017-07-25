package com.tpyzq.mobile.pangu.db;

import android.database.sqlite.SQLiteDatabase;

import com.tpyzq.mobile.pangu.util.DbUtil;

/**
 * **************************************************************
 * <p>
 * **************************************************************
 * Authors:huweidong on 2017/6/22 0022 11:00
 * Email：huwwds@gmail.com
 */
public class BaseTable {

    static SQLiteDatabase getDatabase(){
        return DbUtil.getDB();
    }
}
