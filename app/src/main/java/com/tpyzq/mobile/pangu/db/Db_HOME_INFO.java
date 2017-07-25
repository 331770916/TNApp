package com.tpyzq.mobile.pangu.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tpyzq.mobile.pangu.data.NewsInofEntity;
import com.tpyzq.mobile.pangu.db.util.CursorUtils;
import com.tpyzq.mobile.pangu.db.util.JsonUtils;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/11/9.
 * <p>
 * 重构 - huwwds@gmail.com on 2017/06/23
 * <p>
 * 首页资讯数据
 */
public class Db_HOME_INFO extends BaseTable {
    private static final int NEWS_INFO_HOME = 1;
    private static final int NEWS_INFO_OPTIONAL_STOCK = 2;

    private static final String TAG = "Db_HOME_INFO";

    /**
     * 新增一条首页资讯数据
     *
     * @param json
     */
    public static void addOneHomeInfo(String json) {
        StringBuilder sql = new StringBuilder("insert into NEWS_INFO (CONTENT,NEWS_FLAG) VALUES (" + Helper.getSafeString(json) + "," + NEWS_INFO_HOME + ")");
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL(sql.toString());
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    /**
     * 获取所有首页资讯数据
     *
     * @return
     */
    public static ArrayList<String> getHomeInfos() {
        String sql = "SELECT * FROM NEWS_INFO where NEWS_FLAG=" + NEWS_INFO_HOME;
        SQLiteDatabase db = getDatabase();
        Cursor c = null;

        ArrayList<String> infos = new ArrayList<>();
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                String inof = c.getString(c.getColumnIndexOrThrow("CONTENT"));
                infos.add(inof);
            } while (c.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return infos;
    }

    /**
     * 删除所有首页资讯数据
     */
    public static void deleteAll() {
        String sql = "DELETE FROM NEWS_INFO where NEWS_FLAG=" + NEWS_INFO_HOME;
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
        }
    }


    /**
     * 批量添加自选股新闻
     *
     * @param entities
     * @return
     */
    public static boolean addStockListDatas(List<NewsInofEntity> entities) {
        if (entities == null || entities.size() <= 0) return false;


        boolean isSuccelssFul = false;
        SQLiteDatabase db = getDatabase();
        try {
            for (NewsInofEntity entitiy : entities) {
                NewsInofEntity temEntity = queryOneSelfNews(entitiy.getId());
                if (temEntity != null) {
                    deleteOneSelfNewsDataById(entitiy.getId());
                }
                try {
                    String istSql = "insert into NEWS_INFO(CONTENT,NEWS_FLAG,NEWS_ID,STOCK_CODE) values('" + JsonUtils.toJson(entitiy) + "'," + NEWS_INFO_OPTIONAL_STOCK + "," + entitiy.getId() + ",'" + entitiy.getStockCode() + "')";
                    db.execSQL(istSql);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccelssFul = false;
        }
        return isSuccelssFul;
    }

    public static NewsInofEntity queryOneSelfNews(String newsId) {
        String sql = "SELECT * FROM NEWS_INFO   WHERE NEWS_ID = " + Helper.getSafeString(newsId);
        SQLiteDatabase db = getDatabase();
        Cursor c = null;
        NewsInofEntity newsInofEntity = new NewsInofEntity();
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            String content = CursorUtils.getString(c, "CONTENT");
            newsInofEntity = JsonUtils.object(content, NewsInofEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return newsInofEntity;
    }

    /**
     * 搜索全部自选股新闻
     *
     * @return
     */
    public static ArrayList<NewsInofEntity> queryAllSelfNews() {

        String sql = "SELECT ni.CONTENT,s.STOCK_FLAG FROM STOCK s LEFT JOIN NEWS_INFO ni  on s.STOCK_CODE=ni.STOCK_CODE where ni.NEWS_FLAG=" + NEWS_INFO_OPTIONAL_STOCK + " limit 0,10";
        SQLiteDatabase db = getDatabase();
        Cursor c = null;
        ArrayList<NewsInofEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql, null);
            if (c == null || !c.moveToFirst()) {
                return entitiys;
            }
            do {
                try {
                    String content = CursorUtils.getString(c, "ni.CONTENT");
                    NewsInofEntity object = JsonUtils.object(content, NewsInofEntity.class);
                    if (object!=null)
                        object.setFlag(CursorUtils.getInt(c,"s.STOCK_FLAG"));
                    entitiys.add(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
     * 删除所有数据
     */
    public static void deleteAllSelfNewsDatas() {
        String sql = "DELETE FROM NEWS_INFO where NEWS_FLAG=" + NEWS_INFO_OPTIONAL_STOCK;

        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
        }
    }

    /**
     * 删除一条自选股新闻
     *
     * @param stockCode
     */
    public static void deleteOneSelfNewsData(String stockCode) {
        String sql = "DELETE FROM NEWS_INFO WHERE  NEWS_FLAG=" + NEWS_INFO_OPTIONAL_STOCK + " and STOCK_CODE=" + Helper.getSafeString(stockCode);
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除一条自选股新闻
     *
     * @param newsId
     */
    public static void deleteOneSelfNewsDataById(String newsId) {
        String sql = "DELETE FROM NEWS_INFO WHERE NEWSID = " + Helper.getSafeString(newsId);
        SQLiteDatabase db = getDatabase();
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
        }
    }

}

