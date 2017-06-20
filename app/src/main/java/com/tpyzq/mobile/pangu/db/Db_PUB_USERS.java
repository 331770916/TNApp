package com.tpyzq.mobile.pangu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;


import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.DbUtil;
import com.tpyzq.mobile.pangu.util.Helper;

import java.util.ArrayList;

/**
 * Created by 刘泽鹏 on 2016/7/20.
 * <p/>
 * 操作用户表
 * EXCOLUMN01 注册类型
 */
public class Db_PUB_USERS {
    private static final String TAG = "USERS";

    /**
     * 添加一条数据
     *
     * @param entity
     */

    public static void addOneData(UserEntity entity) {
        StringBuilder sql = new StringBuilder("insert into PUB_USERS(TRADESCNO,USERNAME,REALNAME,USERTYPE,TOKEN," +
                "ISARRANGE,ISREGISTER,SCNO,PASSWORD,MOBILE,MOBILE_TYPE,IDCARD,IDENTITYID,MOUNT,REGTIME," +
                "REGCHANNEL,USERSTATUS,ISLOGIN,REALAUTH,PICKNAME,PINYIN,SIGNATURE,SEX,PHOTO,ZOOMPHOTO," +
                "BGURL,INTERESTS,BIRTHDATE,HOMETOWN,RELIGIOUSVIEW,PROVINCEID) VALUES (");
        sql.append(Helper.getSafeString(entity.getTradescno())).append(",");
        sql.append(Helper.getSafeString(entity.getUsername())).append(",");
        sql.append(Helper.getSafeString(entity.getRealname())).append(",");
        sql.append(Helper.getSafeString(entity.getUsertype())).append(",");
        sql.append(Helper.getSafeString(entity.getToken())).append(",");
        sql.append(Helper.getSafeString(entity.getIsarrange())).append(",");
        sql.append(Helper.getSafeString(entity.getIsregister())).append(",");
        sql.append(Helper.getSafeString(entity.getScno())).append(",");
        sql.append(Helper.getSafeString(entity.getPassword())).append(",");
        sql.append(Helper.getSafeString(entity.getMobile())).append(",");
        sql.append(Helper.getSafeString(entity.getMobile_TYPE())).append(",");
        sql.append(Helper.getSafeString(entity.getIdcard())).append(",");
        sql.append(Helper.getSafeString(entity.getIdentityid())).append(",");
        sql.append(Helper.getSafeString(entity.getMount())).append(",");
        sql.append(Helper.getSafeString(entity.getRegtime())).append(",");
        sql.append(Helper.getSafeString(entity.getRegchannel())).append(",");
        sql.append(Helper.getSafeString(entity.getUserstatus())).append(",");
        sql.append(Helper.getSafeString(entity.getIslogin())).append(",");
        sql.append(Helper.getSafeString(entity.getRealauth())).append(",");
        sql.append(Helper.getSafeString(entity.getPinyin())).append(",");
        sql.append(Helper.getSafeString(entity.getSignature())).append(",");
        sql.append(Helper.getSafeString(entity.getSEX())).append(",");
        sql.append(Helper.getSafeString(entity.getPhoto())).append(",");
        sql.append(Helper.getSafeString(entity.getZoomphoto())).append(",");
        sql.append(Helper.getSafeString(entity.getBgurl())).append(",");
        sql.append(Helper.getSafeString(entity.getInterests())).append(",");
        sql.append(Helper.getSafeString(entity.getBirthdate())).append(",");
        sql.append(Helper.getSafeString(entity.getHometown())).append(",");
        sql.append(Helper.getSafeString(entity.getReligiousview())).append(",");
        sql.append(Helper.getSafeString(entity.getProvinceid())).append(")");
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
     * 查询资金账号
     *
     * @return
     */
    public static ArrayList<UserEntity> queryingTradescno() {
        String sql = "SELECT * FROM PUB_USERS";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<UserEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                UserEntity entitiy = new UserEntity();
                entitiy.setTradescno(c.getString(c.getColumnIndexOrThrow("TRADESCNO")));
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
     * 判断当前是否有注册用户
     *
     * @return
     */
    public static boolean isRegister() {

        ArrayList<UserEntity> entitiys = queryingIsregister();

        if (entitiys != null && entitiys.size() > 0) {
            for (UserEntity userEntity : entitiys) {
                String falg = userEntity.getIsregister();

                if (TextUtils.isEmpty(falg)) {
                    return false;
                }

                return "0".equals(falg);
            }
        }

        return false;
    }

    /**
     * 查询是否注册用户 0是注册用户，1是未注册用户
     *
     * @return
     */
    public static ArrayList<UserEntity> queryingIsregister() {
        String sql = "SELECT ISREGISTER FROM PUB_USERS where USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<UserEntity> entitiys = new ArrayList<>();
        try {
             c = db.rawQuery(sql.toString(), null);
             if (c == null || !c.moveToFirst()) {
                return null;
             }
            do {
                UserEntity entitiy = new UserEntity();
                entitiy.setIsregister(c.getString(c.getColumnIndexOrThrow("ISREGISTER")));
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
     * 查詢账号类型（0:qq 1:微信 2:微博 3:手机）
     * EXCOLUMN01 注册类型
     *
     * @return
     */
    public static String queryingTypescno() {
        String sql = "SELECT * FROM PUB_USERS where USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String phone = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                phone = c.getString(c.getColumnIndexOrThrow("EXCOLUMN01"));
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
        return phone;
    }

    /**
     * 查詢是否在手机端安装并登录过?
     *
     * @return
     */
    public static ArrayList<UserEntity> queryingMount() {
        String sql = "SELECT * FROM PUB_USERS where USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<UserEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                UserEntity entitiy = new UserEntity();
                entitiy.setTradescno(c.getString(c.getColumnIndexOrThrow("MOUNT")));
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
     * 查詢手机号
     *
     * @return
     */
    public static String queryingMobile() {
        String sql = "SELECT * FROM PUB_USERS where USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String phone = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                phone = c.getString(c.getColumnIndexOrThrow("MOBILE"));
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
        return phone;
    }


    /**
     * 判断 是否登录
     */
    public static boolean islogin() {
        String phone = queryingIslogin();
        if (phone != null && phone != "") {
            return "true".equals(phone);
        }
        return false;
    }


    /**
     * 查询 是否登录
     */
    public static String queryingIslogin() {
        String sql = "SELECT * FROM PUB_USERS WHERE USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String phone = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                phone = c.getString(c.getColumnIndexOrThrow("ISLOGIN"));
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
        return phone;
    }

    /**
     * 查询 是否双向认证成功
     */
    public static String queryingCertification() {
        String sql = "SELECT * FROM PUB_USERS WHERE USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String phone = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                phone = c.getString(c.getColumnIndexOrThrow("CERTIFICATION"));
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
        return phone;
    }


    /**
     * 查询 是否下载插件
     */
    public static String queryingPlugins() {
        String sql = "SELECT * FROM PUB_USERS WHERE USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String phone = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                phone = c.getString(c.getColumnIndexOrThrow("PIUGINS"));
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
        return phone;
    }

    /**
     * 查询 是否合法性
     */
    public static String queryingLegitimacy() {
        String sql = "SELECT * FROM PUB_USERS WHERE USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String phone = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                phone = c.getString(c.getColumnIndexOrThrow("LEGITIMACY"));
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
        return phone;
    }


    /**
     * 查询 键盘 明文 暗文
     */
    public static String queryingKeyboard() {
        String sql = "SELECT * FROM PUB_USERS WHERE USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String phone = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                phone = c.getString(c.getColumnIndexOrThrow("KEYBOARD"));
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
        return phone;
    }

    /**
     * 查询  注册账号
     */
    public  static String queryingScno() {
        String sql = "SELECT * FROM PUB_USERS WHERE USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String phone = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                phone = c.getString(c.getColumnIndexOrThrow("SCNO"));
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
        return phone;
    }

    /**
     * 查询 注册标识ID
     */
    public static String queryingRegister() {
        String sql = "SELECT * FROM PUB_USERS WHERE USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String phone = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                phone = c.getString(c.getColumnIndexOrThrow("REGISTERID"));
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
        return phone;
    }


    /**
     * 资金账号修改
     */
    public static void UpdateTradescno(UserEntity userEntity) {
//        StringBuilder sql = new StringBuilder("UPDATE PUB_USERS SET ");
//        sql.append("TRADESCNO = "+userEntity.getTradescno());
//        sql.append("WHERE USERID = '1'");
        ContentValues contentValues = new ContentValues();
        contentValues.put("TRADESCNO", userEntity.getTradescno());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    /**
     * 手机号修改
     */
    public static void UpdateMobile(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("MOBILE", userEntity.getMobile());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }

        }
    }


    /**
     * 修改  注册账号
     */
    public static void UpdateScno(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("SCNO", userEntity.getScno());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            db.close();
        }
    }


    /**
     * 修改  是否注册用户 0是注册用户，1是未注册用户
     */
    public static void UpdateIsregister(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ISREGISTER", userEntity.getIsregister());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    /**
     * 修改 是否登录
     */
    public static void UpdateIslogin(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ISLOGIN", userEntity.getIslogin());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 修改 是否双向认证
     */
    public static void UpdateCertification(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CERTIFICATION", userEntity.getCertification());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    /**
     * 修改 是否下载插件
     */
    public static void UpdatePlugins(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PIUGINS", userEntity.getPlugins());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }

        }
    }


    /**
     * 修改 验证合法性
     */
    public static void UpdateLegitimacy(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("LEGITIMACY", userEntity.getLegitimacy());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    /**
     * 修改 键盘 明文  暗文
     */
    public static void UpdateKeyboard(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("KEYBOARD", userEntity.getKeyboard());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 修改   账号类型（0:qq 1:微信 2:微博 3:手机）
     */
    public static void UpdateTypescno(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("EXCOLUMN01", userEntity.getTypescno());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 修改  注册标识ID
     */
    public static void UpdateRegister(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("REGISTERID", userEntity.getRegisterID());
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 修改  行情刷新时间  1: 1000毫秒 2: 2000毫秒 3：3000毫秒
     */
    public static void updateRefreshTime(String logo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("EXCOLUMN00", logo);
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 设置默认  行情刷新时间  为3
     */
    public static void clearRefreshTime() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("EXCOLUMN00", 2);
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 查询  行情刷新时间  1: 1000毫秒 2: 2000毫秒 3：3000毫秒
     */
    public static String searchRefreshTime() {
        String sql = "SELECT * FROM PUB_USERS WHERE USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String time = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                time = c.getString(c.getColumnIndexOrThrow("EXCOLUMN00"));
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
        return time;
    }

    /**
     * 修改  Session 登陆唯一标识
     */
    public static void updateSession(String session) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("EXCOLUMN01", session);
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    /**
     * 查询  当前session
     */
    public static String searchSession() {
        String sql = "SELECT * FROM PUB_USERS WHERE USERID = 1";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        String time = "";
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                time = c.getString(c.getColumnIndexOrThrow("EXCOLUMN02"));
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
        return time;
    }

    /**
     * 清空 Session值
     */
    public static void clearSession() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("EXCOLUMN02", "");
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.update("PUB_USERS", contentValues, "USERID = 1", null);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}