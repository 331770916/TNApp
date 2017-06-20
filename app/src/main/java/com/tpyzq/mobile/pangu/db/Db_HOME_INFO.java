package com.tpyzq.mobile.pangu.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.tpyzq.mobile.pangu.data.NewsInofEntity;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.DbUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/11/9.
 * 首页资讯数据
 */
public class Db_HOME_INFO {


    private static final String TAG = "Db_HOME_INFO";
    /**
     * 新增一条首页资讯数据
     * @param json
     */
    public static void addOneHomeInfo(String json) {
        StringBuilder sql = new StringBuilder("insert into Db_HomeInformation (USERID) VALUES (");
        sql.append(Helper.getSafeString(json)).append(")");
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
     * 获取所有首页资讯数据
     * @return
     */
    public static ArrayList<String> getHomeInfos() {
        String sql = "SELECT * FROM Db_HomeInformation";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;

        ArrayList<String> infos = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                String inof = c.getString(c.getColumnIndexOrThrow("USERID"));
                infos.add(inof);
            } while (c.moveToNext());
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
        return infos;
    }
    /**
     * 删除所有首页资讯数据
     */
    public static void deleteAll() {
        String sql = "DELETE FROM Db_HomeInformation";
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
     * 增加 一条自选股新闻
     * @param entitiy
     */
    public static void addOneSelfNewsData(NewsInofEntity entitiy){

        StringBuilder sql = new StringBuilder("insert into Db_OPTIONAL_SHARE (NEWSID, AUTHOR, TIME, CONTENT,TITLE,COMP,TICK, EXCOLUMN00) VALUES (");
        sql.append(Helper.getSafeString(entitiy.getId())).append(",")
                .append(Helper.getSafeString(entitiy.getAuth())).append(",")
                .append(Helper.getSafeString(String.valueOf(entitiy.getDt()))).append(",")
                .append(Helper.getSafeString(entitiy.getSum())).append(",")
                .append(Helper.getSafeString(entitiy.getTitle())).append(",")
                .append(Helper.getSafeString(entitiy.getComp())).append(",")
                .append(Helper.getSafeString(entitiy.getTick())).append(",")
                .append(Helper.getSafeString(entitiy.getStockCode())).append(")");
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }

    }


    /**
     * 批量添加自选股
     * @param entities
     * @return
     */
    public static boolean addStockListDatas(List<NewsInofEntity> entities) {


        boolean isSuccelssFul = false;
        SQLiteDatabase db = null;
        if (null != entities && entities.size() <= 0) {
            return isSuccelssFul;
        }

        try {
            StringBuilder sql = new StringBuilder("insert into Db_OPTIONAL_SHARE (NEWSID, AUTHOR, TIME, CONTENT, TITLE, COMP, TICK, EXCOLUMN00, EXCOLUMN01) VALUES (" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?)");

            db = DbUtil.getInstance().getDB();
            SQLiteStatement stat = db.compileStatement(sql.toString());
            db.beginTransaction();

            for (NewsInofEntity entitiy : entities) {


                NewsInofEntity temEntity = queryOneSelfNews(entitiy.getId());

                if (temEntity != null) {
                    deleteOneSelfNewsDataById(entitiy.getId());
                }

                stat.bindString(1, entitiy.getId());
                stat.bindString(2, entitiy.getAuth());
                stat.bindString(3, String.valueOf(entitiy.getDt()));
                stat.bindString(4, entitiy.getSum());
                stat.bindString(5, entitiy.getTitle());
                stat.bindString(6, entitiy.getComp());
                stat.bindString(7, entitiy.getTick());
                stat.bindString(8, entitiy.getStockCode());
                stat.bindString(9, Helper.getTimeByTimeC(String.valueOf(entitiy.getDt())));

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

    public static NewsInofEntity queryOneSelfNews(String newsId) {
        String sql = "SELECT * FROM Db_OPTIONAL_SHARE   WHERE Db_OPTIONAL_SHARE.NEWSID = " + Helper.getSafeString(newsId);
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        NewsInofEntity newsInofEntity = new NewsInofEntity();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            newsInofEntity.setId(c.getString(c.getColumnIndexOrThrow("NEWSID")));
            newsInofEntity.setAuth(c.getString(c.getColumnIndexOrThrow("AUTHOR")));
            newsInofEntity.setDt(Long.valueOf(c.getString(c.getColumnIndexOrThrow("TIME"))));
            newsInofEntity.setSum(c.getString(c.getColumnIndexOrThrow("CONTENT")));
            newsInofEntity.setTitle(c.getString(c.getColumnIndexOrThrow("TITLE")));
            newsInofEntity.setComp(c.getString(c.getColumnIndexOrThrow("COMP")));
            newsInofEntity.setTick(c.getString(c.getColumnIndexOrThrow("TICK")));
            newsInofEntity.setStockCode(c.getString(c.getColumnIndexOrThrow("EXCOLUMN00")));
            newsInofEntity.setDate(c.getString(c.getColumnIndexOrThrow("EXCOLUMN01")));

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
//            if (db != null) {
//                db.close();
//            }
        }
        return newsInofEntity;
    }

    /**
     * 搜索全部自选股新闻
     * @return
     */
    public static ArrayList<NewsInofEntity> queryAllSelfNews() {

        String sql = "SELECT * FROM Db_OPTIONAL_SHARE  order by date(Db_OPTIONAL_SHARE.EXCOLUMN01) DESC,  time(Db_OPTIONAL_SHARE.EXCOLUMN01) DESC  limit 0,10";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<NewsInofEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return entitiys;
            }
            do {
                NewsInofEntity  newsInofEntity = new NewsInofEntity();
                newsInofEntity.setId(c.getString(c.getColumnIndexOrThrow("NEWSID")));
                newsInofEntity.setAuth(c.getString(c.getColumnIndexOrThrow("AUTHOR")));
                newsInofEntity.setDt(Long.valueOf(c.getString(c.getColumnIndexOrThrow("TIME"))));
                newsInofEntity.setSum(c.getString(c.getColumnIndexOrThrow("CONTENT")));
                newsInofEntity.setTitle(c.getString(c.getColumnIndexOrThrow("TITLE")));
                newsInofEntity.setComp(c.getString(c.getColumnIndexOrThrow("COMP")));
                newsInofEntity.setTick(c.getString(c.getColumnIndexOrThrow("TICK")));
                newsInofEntity.setStockCode(c.getString(c.getColumnIndexOrThrow("EXCOLUMN00")));
                newsInofEntity.setDate(c.getString(c.getColumnIndexOrThrow("EXCOLUMN01")));
                entitiys.add(newsInofEntity);
            } while (c.moveToNext());

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
        return entitiys;
    }

    /**
     * 删除所有数据
     */
    public static void deleteAllSelfNewsDatas() {
        String sql = "DELETE FROM Db_OPTIONAL_SHARE";

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
     * 删除一条自选股新闻
     * @param stockCode
     */
    public static void deleteOneSelfNewsData(String stockCode) {
        String sql = "DELETE FROM Db_OPTIONAL_SHARE WHERE Db_OPTIONAL_SHARE.EXCOLUMN00 = " + Helper.getSafeString(stockCode);
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
     * 删除一条自选股新闻
     * @param newsId
     */
    public static void deleteOneSelfNewsDataById(String newsId) {
        String sql = "DELETE FROM Db_OPTIONAL_SHARE WHERE Db_OPTIONAL_SHARE.NEWSID = " + Helper.getSafeString(newsId);
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

