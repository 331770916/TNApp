package com.tpyzq.mobile.pangu.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.DbUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;

/**
 * Created by ZHANGWENBO on 2016/10/29.
 */
public class HOLD_SEQ {

    private static final String TAG = "HOLD_SEQ";

    public static String getHoldCodes () {

        String sql = "select * from HOLD_SEQ";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String codes = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            do {
               String strCode = c.getString(c.getColumnIndexOrThrow("CODE"));
                sb.append(strCode).append(",");
            } while (c.moveToNext());

            codes = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return codes;
    }

    public static void deleteAll() {
        String sql = "delete from HOLD_SEQ";
        SQLiteDatabase db = null ;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 批量添加持仓股票
     * @param entities
     */
    public static boolean addHoldDatas(ArrayList<StockInfoEntity> entities) {

        boolean isSuccelssFul = false;
        SQLiteDatabase db = null;
        if (null != entities && entities.size() <= 0) {
            return isSuccelssFul;
        }

        try {
            StringBuilder sql = new StringBuilder("insert into HOLD_SEQ (CODE, STOCKHOLDON) VALUES ( ?,");
            sql.append(Helper.getSafeString("true")).append(")");
            db = DbUtil.getInstance().getDB();
            SQLiteStatement stat = db.compileStatement(sql.toString());
            db.beginTransaction();

            for (StockInfoEntity entity : entities) {
                stat.bindString(1, entity.getSECU_CODE());
                long result = stat.executeInsert();
                if (result < 0) {
                    return isSuccelssFul;
                }
            }
            db.setTransactionSuccessful();
            isSuccelssFul = true;
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.e(TAG, e.toString());
            isSuccelssFul = false;
        } finally {
            try {
                if (null != db) {
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return isSuccelssFul;
    }

    /**
     * 增加一条
     * @param entitiy
     */
    public static void addOneHoldData(StockInfoEntity entitiy){

        if (null != entitiy && !TextUtils.isEmpty(entitiy.getSECU_CODE())) {

            if (!getCode(entitiy.getSECU_CODE())) {
                StringBuilder sql = new StringBuilder("insert into HOLD_SEQ (CODE, STOCKHOLDON) VALUES (");
                sql.append(Helper.getSafeString(entitiy.getSECU_CODE())).append(",")
                        .append(Helper.getSafeString("true")).append(")");
                SQLiteDatabase db = null ;
                try {
                    db = DbUtil.getInstance().getDB();
                    db.execSQL(sql.toString());
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }finally {
                    if (db != null) {
                        db.close();
                    }
                }
            }
        }
    }

    public static boolean getCode(String code) {

        boolean falg = false;

        String sql = "select * from HOLD_SEQ where CODE = " + Helper.getSafeString(code);
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String strCode = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return falg;
            }
            strCode = c.getString(c.getColumnIndexOrThrow("CODE"));

            if (!TextUtils.isEmpty(strCode)) {
                falg = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
//            if (db != null) {
//                db.close();
//            }
        }

        return falg;

    }

}
