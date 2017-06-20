package com.tpyzq.mobile.pangu.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.DbUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;

/**
 * Created by 刘泽鹏 on 2016/7/20.
 * HistoryStock
 * 历史自选股
 */
public class Db_PUB_SEARCHHISTORYSTOCK {
    private static final String TAG = "SEARCHHISTORYSTOCK";


    /**
     * 查询历史自选股条数
     * @return
     */
    public static int getHistoryStockListCount() {
        StringBuilder sql = new StringBuilder("select * from PUB_SEARCHHISTORYSTOCK");
        SQLiteDatabase db = null;
        Cursor c = null ;
        int count = 0;
        try {
            db = DbUtil.getInstance().getDB();
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return 0;
            }
            count = c.getCount();
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if(c != null){
                c.close();
            }
            if (db != null) {
               db.close();
            }
        }

        return count;
    }


    public static void delteOneStockByStockCode(String stockCode) {
        String sql = "DELETE FROM PUB_SEARCHHISTORYSTOCK where STOCKCODE = " + Helper.getSafeString(stockCode);
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
        } finally {
//            if (db != null) {
//                db.close();
//            }
        }
    }



    /**
     * 增加一条
     * @param entitiy
     */
    public static void addOneData(StockInfoEntity entitiy){


        int count = getHistoryStockListCount();
        if (count > 50) {
            ArrayList<StockInfoEntity> entities = queryAllDatasByDesc();

            String stockNumber = entities.get(entities.size() -1).getStockNumber() ;

            delteOneStockByStockCode(stockNumber);

        }

        StringBuilder sql = new StringBuilder("insert into PUB_SEARCHHISTORYSTOCK (STOCKCODE, NAME, HOT, HOLD,DATE ) VALUES (");

        sql.append(Helper.getSafeString(entitiy.getStockNumber())).append(",")
                .append(Helper.getSafeString(entitiy.getStockName())).append(",")
                .append(Helper.getSafeString(entitiy.getHot())).append(",")
                .append(Helper.getSafeString(entitiy.getHold())).append(",")
                .append(Helper.getSafeString(System.currentTimeMillis()+"")).append(")");

        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    /**
     * 倒序搜索全部
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatasByDesc() {
        String sql = "SELECT * FROM PUB_SEARCHHISTORYSTOCK order by DATE desc";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();

        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
                entitiy.setHot(c.getString(c.getColumnIndexOrThrow("HOT")));
                entitiy.setHold(c.getString(c.getColumnIndexOrThrow("HOLD")));
                entitiy.setSeeDate(c.getString(c.getColumnIndexOrThrow("DATE")));
                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            if(db != null){
                db.close();
            }
        }
        return entitiys;
    }

    /**
     * 搜索全部
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatas() {
        String sql = "SELECT * FROM PUB_SEARCHHISTORYSTOCK LEFT JOIN HOLD_SEQ ON PUB_SEARCHHISTORYSTOCK.STOCKCODE = HOLD_SEQ.CODE order by DATE desc";//

        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
                entitiy.setHot(c.getString(c.getColumnIndexOrThrow("HOT")));
                entitiy.setHold(c.getString(c.getColumnIndexOrThrow("HOLD")));
                entitiy.setSeeDate(c.getString(c.getColumnIndexOrThrow("DATE")));
                entitiy.setStockholdon(c.getString(c.getColumnIndexOrThrow("STOCKHOLDON")));
                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return entitiys;
    }

    /**
     * 根据主键搜索
     * @return
     */
    public static StockInfoEntity queryFromID(String stockCode) {
        String sql = "SELECT * FROM PUB_SEARCHHISTORYSTOCK WHERE STOCKCODE = " + Helper.getSafeString(stockCode);

        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        StockInfoEntity entitiy = new StockInfoEntity();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
            entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
            entitiy.setHot(c.getString(c.getColumnIndexOrThrow("HOT")));
            entitiy.setHold(c.getString(c.getColumnIndexOrThrow("HOLD")));
            entitiy.setSeeDate(c.getString(c.getColumnIndexOrThrow("DATE")));

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return entitiy;
    }


    /**
     * 删除所有数据
     */
    public static void deleteAllDatas() {
        String sql = "DELETE FROM PUB_SEARCHHISTORYSTOCK";
        SQLiteDatabase db = null ;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 根据主键删除数据
     */
    public static void deleteFromID(String stockCode) {
        String sql = "DELETE FROM PUB_SEARCHHISTORYSTOCK WHERE STOCKCODE = " + Helper.getSafeString(stockCode);
        SQLiteDatabase db = null ;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }

}
