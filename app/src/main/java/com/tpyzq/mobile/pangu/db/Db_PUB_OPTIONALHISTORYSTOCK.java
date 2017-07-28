package com.tpyzq.mobile.pangu.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.util.CloseUtils;
import com.tpyzq.mobile.pangu.db.util.CursorUtils;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;

/**
 * Created by 刘泽鹏 on 2016/7/21.
 *
 * 重构 - huwwds@gmail.com on 2017/06/23
 *
 * 最近浏览过的股票数据库表
 */
public class Db_PUB_OPTIONALHISTORYSTOCK extends StockTable {
    private static final String TAG = "OPTIONALHISTORYSTOCK";


    /**
     * 查询历史自选股条数
     * @return
     */
    public static int getLookStockListCount() {
        StringBuilder sql = new StringBuilder("select * from STOCK where STOCK_FLAG&"+ STOCK_BROWSE_NEARBY +"="+ STOCK_BROWSE_NEARBY);
        int count = 0;

        SQLiteDatabase db =getDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return 0;
            }
            count = c.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(c);
        }

        return count;
    }

    /**
     * 增加一条
     */
    public static void addOneData(String stockName, String stockCode){
        StockInfoEntity sie = queryFromID(stockCode);
        SQLiteDatabase db = getDatabase();
        try{
            String sql;
            if(sie==null){
                int count = getLookStockListCount();
                if (count >= 30) {
                    ArrayList<StockInfoEntity> entities = queryAllDatasByDesc();
                    if (entities!=null&&entities.size()>0){
                        String stockNumber = entities.get(entities.size() -1).getStockNumber() ;
                        deleteFromID(stockNumber);
                    }
                }
                sql="insert into STOCK(STOCK_CODE,STOCK_NAME,CREATE_TIME,STOCK_FLAG) values("+ Helper.getSafeString(stockCode)+","+ Helper.getSafeString(stockName)+","+System.currentTimeMillis()+","+ STOCK_BROWSE_NEARBY +")";
            }else{//已经存在
                sql="update STOCK set STOCK_NAME="+ Helper.getSafeString(stockName)+",STOCK_CODE="+ Helper.getSafeString(stockCode)+",CREATE_TIME="+System.currentTimeMillis()+",STOCK_FLAG=STOCK_FLAG|"+ STOCK_BROWSE_NEARBY+" where STOCK_CODE="+ Helper.getSafeString(stockCode);
            }
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void updateStockNameByCode(String stockName, String stockCode){
        SQLiteDatabase db = null;
        try{
            String sql = "update STOCK SET STOCK_NAME="+ Helper.getSafeString(stockName)+
                        " where STOCK_CODE="+ Helper.getSafeString(stockCode);
            db = getDatabase();
            db.execSQL(sql);
            sql = "update STOCK set "
                    + "STOCK_NAME = " +  Helper.getSafeString(stockName)
                    +" where STOCK_CODE  = " + Helper.getSafeString(stockCode);
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 搜索全部
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatasByDesc() {
        String sql = "SELECT * FROM STOCK where STOCK_FLAG&"+ STOCK_BROWSE_NEARBY +"="+ STOCK_BROWSE_NEARBY +" order by CREATE_TIME desc";

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
                entitiy.setSeeDate(c.getString(c.getColumnIndexOrThrow("CREATE_TIME")));
                entitiy.setHold(((CursorUtils.getInt(c,"STOCK_FLAG")& STOCK_HOLD)== STOCK_HOLD)+"");
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
        String sql = "SELECT * FROM STOCK WHERE STOCK_CODE =" + Helper.getSafeString(stockCode);

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
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL("DELETE FROM STOCK WHERE STOCK_FLAG="+ STOCK_BROWSE_NEARBY);
            db.execSQL("update STOCK set STOCK_FLAG=STOCK_FLAG-"+ STOCK_BROWSE_NEARBY +" where STOCK_FLAG&"+ STOCK_BROWSE_NEARBY +"="+ STOCK_BROWSE_NEARBY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据主键删除数据
     */
    public static void deleteFromID(String stockCode) {
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL("DELETE FROM STOCK WHERE STOCK_FLAG="+ STOCK_BROWSE_NEARBY +" and STOCK_CODE = "+ Helper.getSafeString(stockCode));
            db.execSQL("update STOCK set STOCK_FLAG=STOCK_FLAG-"+ STOCK_BROWSE_NEARBY +" where STOCK_FLAG&"+ STOCK_BROWSE_NEARBY +"="+ STOCK_BROWSE_NEARBY +" and STOCK_CODE="+ Helper.getSafeString(stockCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
