package com.tpyzq.mobile.pangu.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.util.CursorUtils;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;

/**
 * Created by ZHANGWENBO on 2016/10/29.
 *
 *  重构 - huwwds@gmail.com on 2017/06/23
 *
 */
public class HOLD_SEQ extends StockTable {

    private static final String TAG = "HOLD_SEQ";

    public static String getHoldCodes() {

        String sql = "select * from STOCK where stock_flag&" + STOCK_HOLD + "=" + STOCK_HOLD;
        SQLiteDatabase db = getDatabase();
        Cursor c = null;
        String codes = "";
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            do {
                String strCode = c.getString(c.getColumnIndexOrThrow("STOCK_CODE"));
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
        }

        return codes;
    }

    public static void deleteAll() {
        try {
            String delSql="delete from STOCK where STOCK_FLAG="+STOCK_HOLD;
            getDatabase().execSQL(delSql);
            String udtSql="update STOCK set STOCK_FLAG=STOCK_FLAG-"+STOCK_HOLD+" where STOCK_FLAG&"+STOCK_HOLD+"="+STOCK_HOLD;
            getDatabase().execSQL(udtSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量添加持仓股票
     *
     * @param entities
     */
    public static boolean addHoldDatas(ArrayList<StockInfoEntity> entities) {
        SQLiteDatabase db = getDatabase();
        if (null == entities || entities.size() <= 0) {
            return false;
        }

        try {
            db.beginTransaction();
            for (StockInfoEntity entity : entities) {
                Cursor cursor = getDatabase().rawQuery("select STOCK_FLAG from STOCK where STOCK_CODE=?", new String[]{entity.getSECU_CODE()});
                if (cursor!=null&&cursor.moveToNext()){
                    Integer stock_flag = CursorUtils.getInt(cursor, "STOCK_FLAG");
                    if ((stock_flag&STOCK_HOLD)==STOCK_HOLD){
                        continue;
                    }
                    String udtSql="update STOCK set STOCK_FLAG=STOCK_FLAG|"+STOCK_HOLD+" where STOCK_CODE="+entity.getSECU_CODE();
                    db.execSQL(udtSql);
                }else{
                    String istSql="insert into STOCK(STOCK_FLAG,STOCK_CODE, STOCK_NAME) values('"+STOCK_HOLD+"','"+entity.getSECU_CODE()+ "','" +entity.getSECU_NAME() +"')";
                    db.execSQL(istSql);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != db) {
                    db.endTransaction();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean getCode(String code) {

        boolean flag = false;

        String sql = "select * from STOCK where STOCK_CODE = " + Helper.getSafeString(code)+" and STOCK_FLAG&"+STOCK_HOLD+">0";
        SQLiteDatabase db =getDatabase();
        Cursor c = null;
        String strCode ;
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return false;
            }
            strCode = c.getString(c.getColumnIndexOrThrow("STOCK_CODE"));

            if (!TextUtils.isEmpty(strCode)) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }

        }

        return flag;

    }

}
