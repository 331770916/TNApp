package com.tpyzq.mobile.pangu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.util.CloseUtils;
import com.tpyzq.mobile.pangu.db.util.CursorUtils;
import com.tpyzq.mobile.pangu.db.util.StringUtils;
import com.tpyzq.mobile.pangu.log.LogUtil;

import java.util.ArrayList;

/**
 * Created by 刘泽鹏 on 2016/7/20.
 *
 * 重构 - huwwds@gmail.com on 2017/06/23
 *
 *
 * 操作用户表
 * EXCOLUMN01 注册类型
 */
public class Db_PUB_USERS extends BaseTable {
    private static final String TAG = "USERS";
    public static UserEntity entity=new UserEntity();

    static {
        loadUserEntity();
    }

    private static void loadUserEntity(){
        Cursor c = null;
        try {
            c = getDatabase().rawQuery("select * from USER", null);
            if (c == null || !c.moveToNext()) return;
            String id = CursorUtils.getString(c, "ID");
            String username = CursorUtils.getString(c, "USERNAME");
            String nickname = CursorUtils.getString(c, "NICKNAME");
            String mobile_num = CursorUtils.getString(c, "MOBILE_NUM");
            String is_encrypt = CursorUtils.getString(c, "IS_ENCRYPT");
            String user_type = CursorUtils.getString(c, "USER_TYPE");
            String register_account = CursorUtils.getString(c, "REGISTER_ACCOUNT");
            String trade_account = CursorUtils.getString(c, "TRADE_ACCOUNT");
            String register_time = CursorUtils.getString(c, "REGISTER_TIME");
            String profile_pic = CursorUtils.getString(c, "PROFILE_PIC");
            String is_login = CursorUtils.getString(c, "IS_LOGIN");
            String certification = CursorUtils.getString(c, "CERTIFICATION");
            String plugins = CursorUtils.getString(c, "PLUGINS");
            String refresh_time = CursorUtils.getString(c, "REFRESH_TIME");
            String is_init_unregister_data = CursorUtils.getString(c, "IS_INIT_UNREGISTER_DATA");

            entity.setIsregister(StringUtils.isAllEmpty(register_account,mobile_num)?"1":"0");
            entity.setUserId(id);
            entity.setUsername(username);
            entity.setPickname(nickname);
            entity.setMobile(mobile_num);
            entity.setKeyboard(is_encrypt);
            entity.setUsertype(user_type);
            entity.setScno(register_account);
            entity.setTradescno(trade_account);
            entity.setRegtime(register_time);
            entity.setIslogin(is_login);
            entity.setCertification(certification);
            entity.setPlugins(plugins);
            entity.setRefreshTime(refresh_time);
            entity.setIsInitUnregisterData(is_init_unregister_data);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            CloseUtils.close(c);
        }
    }


    /**
     * 查询资金账号
     *
     * @return
     */
    public static ArrayList<UserEntity> queryingTradescno() {
        ArrayList<UserEntity> entities = new ArrayList<>();
        entities.add(entity);
        return entities;
    }

    /**
     * 判断当前是否有注册用户
     *
     * @return
     */
    public static boolean isRegister() {
        return "0".equals(entity.getIsregister());
    }


    /**
     * 查詢账号类型（0:qq 1:微信 2:微博 3:手机）
     * EXCOLUMN01 注册类型
     *
     * @return
     */
    public static String queryingTypescno() {
        return entity.getUsertype();
    }

    /**
     * 查詢手机号
     *
     *
     * @return
     */
    public static String queryingMobile() {
        return entity.getMobile();
    }


    /**
     * 判断 是否登录
     */
    public static boolean islogin() {
        return "true".equals(entity.getIslogin());
    }


    /**
     * 查询 是否登录
     */
    public static String queryingIslogin() {
       return entity.getIslogin();
    }

    /**
     * 查询 是否双向认证成功
     */
    public static String queryingCertification() {
        return entity.getCertification();
    }


    /**
     * 查询 键盘 明文 暗文
     */
    public static String queryingKeyboard() {
        return entity.getKeyboard();
    }

    public static String queryingScno() {
      return entity.getScno();
    }

    /**
     * 查询 注册标识ID
     */
    public static String queryingRegister() {
        return entity.getScno();
    }


    /**
     * 资金账号修改
     */
    public static void UpdateTradescno(UserEntity userEntity) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("TRADE_ACCOUNT", userEntity.getTradescno());
        SQLiteDatabase db = getDatabase();
        try {
            if (db.update("USER", contentValues, "ID = 1", null)>0){
                entity.setTradescno(userEntity.getTradescno());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    /**
     * 资金账号修改
     */
    public static void delTradeId(String tradeId) {
        ArrayList<UserEntity> userEntities = queryingTradescno();
        if (userEntities==null||userEntities.size()<=0)return;
        String tradescno = userEntities.get(0).getTradescno();

        if (!tradescno.contains(tradeId)) {
            return;
        }

        String[] split = tradescno.split(",");
        int index=-1;
        for (int i = 0; i < split.length; i++) {
            if (split[i].trim().equals(tradeId.trim())) {
                if (i!=0)
                    index=i;
                break;
            }
        }

        if (index==-1) {
            return;
        }

        split[index]="";
        tradescno="";
        for (String aSplit : split) {
            if (!TextUtils.isEmpty(aSplit))
                tradescno += aSplit + ",";
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setTradescno(tradescno);
        UpdateTradescno(userEntity);
    }

    /**
     * 手机号修改
     */
    public static void UpdateMobile(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("MOBILE_NUM", userEntity.getMobile());
        try {
            if(getDatabase().update("USER", contentValues, "ID = 1", null)>0){
                entity.setMobile(userEntity.getMobile());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }


    /**
     * 修改  注册账号
     */
    public static void UpdateScno(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("REGISTER_ACCOUNT", userEntity.getScno());
        try {
            if(getDatabase().update("USER", contentValues, "ID = 1", null)>0){
                entity.setScno(userEntity.getScno());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改  是否注册用户 0是注册用户，1是未注册用户
     */
    public static void UpdateIsregister(UserEntity userEntity) {
        entity.setIsregister(StringUtils.isAllEmpty(userEntity.getMobile(),userEntity.getScno())?"1":"0");
    }


    /**
     * 修改 是否登录
     */
    public static void UpdateIslogin(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("IS_LOGIN", userEntity.getIslogin());
        try {
            if(getDatabase().update("USER", contentValues, "ID = 1", null)>0){
                entity.setIslogin(userEntity.getIslogin());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    /**
     * 修改 是否双向认证
     */
    public static void UpdateCertification(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CERTIFICATION", userEntity.getCertification());
        try {
            if(getDatabase().update("USER", contentValues, "ID = 1", null)>0){
              entity.setCertification(userEntity.getCertification());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }


    /**
     * 修改 是否下载插件
     */
    public static void UpdatePlugins(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PLUGINS", userEntity.getPlugins());
        try {
            if(getDatabase().update("USER", contentValues, "ID = 1", null)>0){
                entity.setPlugins(userEntity.getPlugins());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    /**
     * 修改 键盘 明文  暗文
     */
    public static void UpdateKeyboard(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("IS_ENCRYPT", userEntity.getKeyboard());
        try {
            if(getDatabase().update("USER", contentValues, "ID = 1", null)>0){
                entity.setKeyboard(userEntity.getKeyboard());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    /**
     * 修改   账号类型（0:qq 1:微信 2:微博 3:手机）
     */
    public static void UpdateTypescno(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_TYPE", userEntity.getTypescno());
        try {
            if(getDatabase().update("USER", contentValues, "ID = 1", null)>0){
                entity.setUsertype(userEntity.getUsertype());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    /**
     * 修改  注册标识ID
     */
    public static void UpdateRegister(UserEntity userEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("REGISTER_ACCOUNT", userEntity.getRegisterID());
        try {
            if(getDatabase().update("USER", contentValues, "ID = 1", null)>0){
                entity.setScno(userEntity.getScno());
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    /**
     * 修改  行情刷新时间  1: 1000毫秒 2: 2000毫秒 3：3000毫秒
     */
    public static void updateRefreshTime(String refreshTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("REFRESH_TIME", refreshTime);
        try {
            if(getDatabase().update("USER", contentValues, "ID = 1", null)>0){
                entity.setRefreshTime(refreshTime);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    /**
     * 设置默认  行情刷新时间  为2
     */
    public static void clearRefreshTime() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("REFRESH_TIME", "2");
        try {
            if (getDatabase().update("USER", contentValues, "ID = 1", null)>0){
                entity.setRefreshTime("2");
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    /**
     * 查询  行情刷新时间  1: 1000毫秒 2: 2000毫秒 3：3000毫秒
     */
    public static String searchRefreshTime() {
        return entity.getRefreshTime();
    }

    public static void updateUserById(String userId) {
        ContentValues contentValues=new ContentValues();
        contentValues.put("ID",entity.getUserId());
        contentValues.put("USERNAME",entity.getUsername());
        contentValues.put("NICKNAME",entity.getPickname());
        contentValues.put("MOBILE_NUM",entity.getMobile());
        contentValues.put("IS_ENCRYPT",entity.getKeyboard());
        contentValues.put("USER_TYPE",entity.getUsertype());
        contentValues.put("REGISTER_ACCOUNT",entity.getScno());
        contentValues.put("TRADE_ACCOUNT",entity.getTradescno());
        contentValues.put("REGISTER_TIME",entity.getRegtime());
        contentValues.put("IS_LOGIN",entity.getIslogin());
        contentValues.put("CERTIFICATION",entity.getCertification());
        contentValues.put("PLUGINS",entity.getPlugins());
        contentValues.put("REFRESH_TIME",entity.getRefreshTime());

        getDatabase().update("USER",contentValues,"ID=?",new String[]{userId});
    }

    public static void updateInitUnregisterData(String s) {
        ContentValues contentValues=new ContentValues();
        contentValues.put("IS_INIT_UNREGISTER_DATA",s);
        getDatabase().update("USER",contentValues,"ID=1",null);
    }
}