package com.tpyzq.mobile.pangu.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.DbUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;

/**
 * Created by 刘泽鹏 on 2016/7/21.
 * 最近浏览过的股票数据库表
 */
public class Db_PUB_OPTIONALHISTORYSTOCK {
    private static final String TAG = "OPTIONALHISTORYSTOCK";


    /**
     * 查询历史自选股条数
     * @return
     */
    public static int getLookStockListCount() {
        StringBuilder sql = new StringBuilder("select * from PUB_OPTIONALHISTORYSTOCK");
        int count = 0;

        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = DbUtil.getInstance().getDB();
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return 0;
            }
            count = c.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return count;
    }

    /**
     * 增加一条
     */
    public static void addOneData(String stockName, String stockCode){
        StockInfoEntity sie = queryFromID(stockCode);
        SQLiteDatabase db = null;
        try{
            StringBuilder sql = new StringBuilder();
            if(sie==null){
                int count = getLookStockListCount();
                if (count >= 30) {
                    ArrayList<StockInfoEntity> entities = queryAllDatasByDesc();
                    String stockNumber = entities.get(entities.size() -1).getStockNumber() ;
                    deleteFromID(stockNumber);
                }

                sql.append("insert into PUB_OPTIONALHISTORYSTOCK (STOCKCODE ,NAME,DATE) VALUES (")
                        .append(Helper.getSafeString(stockCode)).append(",")
                        .append(Helper.getSafeString(stockName)).append(",")
                        .append(System.currentTimeMillis()+"").append(")");
            }else{
                sql.append("update PUB_OPTIONALHISTORYSTOCK SET NAME=")
                        .append(Helper.getSafeString(stockName)).append(",DATE='")
                        .append(System.currentTimeMillis())
                        .append("' where STOCKCODE=").append(Helper.getSafeString(stockCode));
//                String sql = "update PUB_OPTIONALHISTORYSTOCK SET NAME="+Helper.getSafeString(stockName)+",DATE='"+System.currentTimeMillis()+"'" +
//                        " where STOCKCODE="+Helper.getSafeString(stockCode);
            }
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
        }catch (Exception e){
            LogUtil.e("--------------");
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }
    public static void updateStockNameByCode(String stockName, String stockCode){
        SQLiteDatabase db = null;
        try{
            String sql = "update PUB_OPTIONALHISTORYSTOCK SET NAME="+Helper.getSafeString(stockName)+
                        " where STOCKCODE="+Helper.getSafeString(stockCode);
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql);
            sql = "update PUB_STOCKLIST set "
                    + "NAME = " +  Helper.getSafeString(stockName)
                    +" where STOCKCODE  = " + Helper.getSafeString(stockCode);
            db.execSQL(sql);
        }catch (Exception e){
            LogUtil.e("--------------");
            e.printStackTrace();
        }finally {
            if (db != null) {
                db.close();
            }
        }
    }
    /**
     * 搜索全部
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatasByDesc() {
        String sql = "SELECT * FROM PUB_OPTIONALHISTORYSTOCK order by DATE desc";

        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();

        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                LogUtil.e("eeeeeeeeeeeeeeeeeee");
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
//                LogHelper.e("DB historyViewStock:",entitiy.getStockNumber()+","+entitiy.getStockName()+","+entitiy.getSeeDate());
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
        String sql = "SELECT * FROM PUB_OPTIONALHISTORYSTOCK WHERE STOCKCODE =" + Helper.getSafeString(stockCode);

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
        String sql = "DELETE FROM PUB_OPTIONALHISTORYSTOCK";
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 根据主键删除数据
     */
    public static void deleteFromID(String stockCode) {
        String sql = "DELETE FROM PUB_OPTIONALHISTORYSTOCK WHERE STOCKCODE = "+ Helper.getSafeString(stockCode);

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
}
