package com.tpyzq.mobile.pangu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.util.CursorUtils;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;

/**
 * Created by 刘泽鹏 on 2016/7/19.
 * <p>
 * 重构 - huwwds@gmail.com on 2017/06/23
 * <p>
 * 自选股股票列表
 */
public class Db_PUB_STOCKLIST extends StockTable {
    private static final String TAG = "Db_PUB_STOCKLIST";

    public static void initUnregisterData() {
        if("1".equals(Db_PUB_USERS.entity.getIsInitUnregisterData()))return;
        for (int i = 0; i < StockEnum.values().length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("STOCK_FLAG", STOCK_OPTIONAL);
            contentValues.put("STOCK_CODE", StockEnum.values()[i].getCode());
            contentValues.put("STOCK_NAME", StockEnum.values()[i].getName());
            contentValues.put("CREATE_TIME", System.currentTimeMillis());
            getDatabase().insert("STOCK", null, contentValues);
        }
        Db_PUB_USERS.updateInitUnregisterData("1");
    }

    enum StockEnum {

        CYBZ("创业板指", 20399006),
        SZZS("上证指数", 10000001),
        SZCZ("深证成指", 20399001),
        TPY("太平洋", 11601099);
        String name;
        int code;

        StockEnum(String name, int code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public int getCode() {
            return code;
        }
    }


    /**
     * 获取数据库数据总数
     *
     * @return
     */
    public static int getStockListCount() {
        // TODO: 2017/6/22 0022 huwwds
        StringBuilder sql = new StringBuilder("select * from STOCK where STOCK_FLAG&" + STOCK_OPTIONAL + "=" + STOCK_OPTIONAL);
        int count = 0;
        SQLiteDatabase db = getDatabase();
        Cursor c = null;
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return 0;
            }
            count = c.getCount();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return count;
    }


    /**
     * 增加一条
     *
     * @param entitiy
     */
    public static boolean addOneStockListData(StockInfoEntity entitiy) {

        boolean tag = false;

//        StockInfoEntity temEntity = queryStockFromID(entitiy.getStockNumber());
        String querySql = "select count(*) from STOCK where STOCK_CODE=" + entitiy.getStockNumber();
        Cursor cursor = getDatabase().rawQuery(querySql, null);
        String sql;
        if (cursor != null && cursor.moveToNext() && cursor.getInt(0) > 0) {
            sql = "update STOCK set STOCK_FLAG=STOCK_FLAG|" + STOCK_OPTIONAL + " where STOCK_CODE=" + entitiy.getStockNumber();
        } else {
            sql = "insert into STOCK (ID, STOCK_FLAG, STOCK_CODE,STOCK_NAME,CREATE_TIME) VALUES (null," + STOCK_OPTIONAL + ",'" + entitiy.getStockNumber() + "','" + entitiy.getStockName() + "'," + entitiy.getTime() + ")";
        }

        int count = getStockListCount();
        if (count > 50) {
            return false;
        }

        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL(sql);
            tag = true;
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tag;
    }


    /**
     * 搜索全部
     *
     * @return
     */
    public static ArrayList<StockInfoEntity> queryStockListDatas() {
        // TODO: 2017/6/22 0022 huwwds
        String sql = "SELECT * FROM STOCK where STOCK_FLAG&" + STOCK_OPTIONAL + "=" + STOCK_OPTIONAL + " ORDER BY ID DESC";
        SQLiteDatabase db = getDatabase();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entity = new StockInfoEntity();
                entity.setStock_flag(CursorUtils.getInt(c, "STOCK_FLAG"));
                entity.setStockNumber(CursorUtils.getString(c, "STOCK_CODE"));
                entity.setStockName(CursorUtils.getString(c, "STOCK_NAME"));
                entity.setTime(CursorUtils.getString(c, "CREATE_TIME"));
                entitiys.add(entity);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }

        }
        return entitiys;
    }


    /**
     * 根据主键搜索
     *
     * @return
     */
    public static StockInfoEntity queryStockFromID(String stockCode) {
        // TODO: 2017/6/22 0022 huwwds
        //查持仓股
        String sql = "SELECT * FROM STOCK where STOCK_CODE=" + Helper.getSafeString(stockCode) + " and STOCK_FLAG&" + STOCK_OPTIONAL + "=" + STOCK_OPTIONAL + " ORDER BY ID DESC";
        SQLiteDatabase db = getDatabase();
        Cursor c = null;
        StockInfoEntity entitiy = new StockInfoEntity();
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            entitiy.setTime(CursorUtils.getString(c, "CREATE_TIME"));
            entitiy.setStockName(CursorUtils.getString(c, "STOCK_NAME"));
            entitiy.setStockNumber(CursorUtils.getString(c, "STOCK_CODE"));
            entitiy.setStock_flag(CursorUtils.getInt(c, "STOCK_FLAG"));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
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
    public static void deleteAllStocListkDatas() {
        String sql = "DELETE FROM STOCK where STOCK_FLAG=" + STOCK_OPTIONAL;
        SQLiteDatabase db = null;
        try {
            db = getDatabase();
            db.execSQL(sql);
            String udtSql = "update STOCK set STOCK_FLAG=STOCK_FLAG-" + STOCK_OPTIONAL + " where STOCK_FLAG&" + STOCK_OPTIONAL + "=" + STOCK_OPTIONAL;
            db.execSQL(udtSql);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
        }
    }

    /**
     * 根据主键删除数据
     */
    public static boolean deleteStockFromID(String stockCode) {
        String sql = "DELETE FROM STOCK WHERE  STOCK_CODE = " + Helper.getSafeString(stockCode) + " and STOCK_FLAG=" + STOCK_OPTIONAL;
        boolean tag;
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL(sql);
            tag = true;
            String udtSql = "update STOCK set STOCK_FLAG=STOCK_FLAG-" + STOCK_OPTIONAL + " where STOCK_CODE=" + Helper.getSafeString(stockCode) + " and STOCK_FLAG&" + STOCK_OPTIONAL + "=" + STOCK_OPTIONAL;
            db.execSQL(udtSql);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
            tag = false;
        }
        return tag;
    }
}
