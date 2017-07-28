package com.tpyzq.mobile.pangu.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.util.CloseUtils;
import com.tpyzq.mobile.pangu.db.util.CursorUtils;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;

/**
 * Created by 刘泽鹏 on 2016/7/20.
 *
 * 重构 - huwwds@gmail.com on 2017/06/23
 *
 * HistoryStock
 * 历史自选股
 */
public class Db_PUB_SEARCHHISTORYSTOCK extends StockTable {
    private static final String TAG = "SEARCHHISTORYSTOCK";


    /**
     * 查询历史自选股条数
     * @return
     */
    public static int getHistoryStockListCount() {
        StringBuilder sql = new StringBuilder("select * from STOCK where STOCK_FLAG&"+ STOCK_HISTORY_OPTIONAL +"="+ STOCK_HISTORY_OPTIONAL);
        SQLiteDatabase db = getDatabase();
        Cursor c = null ;
        int count = 0;
        try {
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
        }

        return count;
    }


    public static void delteOneStockByStockCode(String stockCode) {
        String sql = "DELETE FROM STOCK where STOCK_CODE = " + Helper.getSafeString(stockCode)+" and STOCK_FLAG&"+ STOCK_HISTORY_OPTIONAL +"="+ STOCK_HISTORY_OPTIONAL;
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL(sql);
            String udtSql="update STOCK set STOCK_FLAG=STOCK_FLAG-"+ STOCK_HISTORY_OPTIONAL +" where STOCK_CODE="+stockCode+" and STOCK_FLAG&"+ STOCK_HISTORY_OPTIONAL +"="+ STOCK_HISTORY_OPTIONAL;
            db.execSQL(udtSql);
        } catch (Exception e) {
            Log.v(TAG, e.toString());
        }
    }



    /**
     * 增加一条
     * @param entitiy
     */
    public static void addOneData(StockInfoEntity entitiy){
        SQLiteDatabase db = getDatabase();
        int count = getHistoryStockListCount();
        if (count > 50) {
            ArrayList<StockInfoEntity> entities = queryAllDatasByDesc();
            if (entities!=null&&entities.size()>0){
                String stockNumber = entities.get(entities.size() -1).getStockNumber();
                delteOneStockByStockCode(stockNumber);
            }
        }
        String qrySql="select count(*) from STOCK where STOCK_CODE='"+entitiy.getStockNumber()+"'";
        Cursor cursor = db.rawQuery(qrySql, null);
        String execSql;
        if (cursor!=null&&cursor.moveToNext()&&cursor.getInt(0)>0){
            execSql="update STOCK set STOCK_FLAG=STOCK_FLAG|"+ STOCK_HISTORY_OPTIONAL +" , STOCK_NAME="+ Helper.getSafeString(entitiy.getStockName())+", STOCK_CODE="+ Helper.getSafeString(entitiy.getStockNumber())+",CREATE_TIME="+System.currentTimeMillis()+" where STOCK_CODE="+ Helper.getSafeString(entitiy.getStockNumber());
        }else{
            execSql ="insert into STOCK (STOCK_FLAG,STOCK_NAME, STOCK_CODE, CREATE_TIME ) VALUES ('"+ STOCK_HISTORY_OPTIONAL +"',"+ Helper.getSafeString(entitiy.getStockName())+","+ Helper.getSafeString(entitiy.getStockNumber())+","+System.currentTimeMillis()+")";
        }
        try {
            db.execSQL(execSql);
            CloseUtils.close(cursor);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }


    /**
     * 倒序搜索全部
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatasByDesc() {
        String sql = "SELECT * FROM STOCK where STOCK_FLAG&"+ STOCK_HISTORY_OPTIONAL +"="+ STOCK_HISTORY_OPTIONAL +" order by CREATE_TIME desc";
        SQLiteDatabase db =getDatabase();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCK_CODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("STOCK_NAME")));
                entitiy.setHold(((CursorUtils.getInt(c,"STOCK_FLAG")& STOCK_HOLD)== STOCK_HOLD)+"");
                entitiy.setSeeDate(c.getString(c.getColumnIndexOrThrow("CREATE_TIME")));
                entitiys.add(entitiy);
            } while (c.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(c);
        }
        return entitiys;
    }

    /**
     * 搜索全部
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatas() {
        String sql = "SELECT * FROM STOCK where STOCK_FLAG&"+ STOCK_HISTORY_OPTIONAL +"="+ STOCK_HISTORY_OPTIONAL +" order by CREATE_TIME desc";

        SQLiteDatabase db = getDatabase();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCK_CODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("STOCK_NAME")));
                entitiy.setStock_flag(c.getInt(c.getColumnIndexOrThrow("STOCK_FLAG")));
                entitiy.setHold(((CursorUtils.getInt(c,"STOCK_FLAG")& STOCK_HOLD)== STOCK_HOLD)+"");
                entitiy.setSeeDate(c.getString(c.getColumnIndexOrThrow("CREATE_TIME")));
                entitiy.setStockholdon(((CursorUtils.getInt(c,"STOCK_FLAG")& STOCK_HOLD)== STOCK_HOLD)+"");
                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return entitiys;
    }

    public static ArrayList<StockInfoEntity> queryAllHistoryOptionalDatas(){
        String sql = "SELECT * FROM STOCK where STOCK_FLAG&"+ STOCK_HISTORY_OPTIONAL +"="+ STOCK_HISTORY_OPTIONAL +" order by CREATE_TIME desc";

        SQLiteDatabase db = getDatabase();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCK_CODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("STOCK_NAME")));
                entitiy.setStock_flag(c.getInt(c.getColumnIndexOrThrow("STOCK_FLAG")));
                entitiy.setHold(((CursorUtils.getInt(c,"STOCK_FLAG")& STOCK_HOLD)== STOCK_HOLD)+"");
                entitiy.setSeeDate(c.getString(c.getColumnIndexOrThrow("CREATE_TIME")));
                entitiy.setStockholdon(((CursorUtils.getInt(c,"STOCK_FLAG")& STOCK_HOLD)== STOCK_HOLD)+"");
                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return entitiys;
    }


    /**
     * 根据主键搜索
     * @return
     */
    public static StockInfoEntity queryFromID(String stockCode) {
        String sql = "SELECT * FROM STOCK WHERE STOCK_CODE = " + Helper.getSafeString(stockCode)+" and STOCK_FLAG&"+ STOCK_HISTORY_OPTIONAL +"="+ STOCK_HISTORY_OPTIONAL;

        SQLiteDatabase db = getDatabase();
        Cursor c = null;
        StockInfoEntity entitiy = new StockInfoEntity();
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCK_CODE")));
            entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("STOCK_NAME")));
            entitiy.setHold(((CursorUtils.getInt(c,"STOCK_FLAG")& STOCK_HOLD)== STOCK_HOLD)+"");
            entitiy.setSeeDate(c.getString(c.getColumnIndexOrThrow("CREATE_TIME")));

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return entitiy;
    }


    /**
     * 删除所有数据
     */
    public static void deleteAllDatas() {
        String sql = "DELETE FROM STOCK where STOCK_FLAG="+ STOCK_HISTORY_OPTIONAL;
        SQLiteDatabase db = getDatabase() ;
        try {
            db.execSQL(sql);
            String udtSql="update STOCK set STOCK_FLAG=STOCK_FLAG-"+ STOCK_HISTORY_OPTIONAL +" where STOCK_FLAG&"+ STOCK_HISTORY_OPTIONAL +"="+ STOCK_HISTORY_OPTIONAL;
            db.execSQL(udtSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据主键删除数据
     */
    public static void deleteFromID(String stockCode) {
        String sql = "DELETE FROM STOCK WHERE STOCK_CODE =" + Helper.getSafeString(stockCode)+" and STOCK_FLAG="+ STOCK_HISTORY_OPTIONAL;
        SQLiteDatabase db = getDatabase() ;
        try {
            db.execSQL(sql);
            String udtSql="update STOCK set STOCK_FLAG=STOCK_FLAG-"+ STOCK_HISTORY_OPTIONAL +" where STOCK_FLAG&"+ STOCK_HISTORY_OPTIONAL +"="+ STOCK_HISTORY_OPTIONAL +" and STOCK_CODE="+ Helper.getSafeString(stockCode);
            db.execSQL(udtSql);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
        }
    }

}
