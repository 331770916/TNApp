package com.tpyzq.mobile.pangu.util.keyboard;


import android.text.TextUtils;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.util.DeviceUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;
import com.yzd.unikeysdk.UnikeyUrls;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangqi on 2017/2/22.
 */

public class KeyEncryptionUtils {
    public static final String JY = "1";      //交易账号
    public static final String SJ = "3";      //手机号
    public static final String SJZY = "6";    //手机验证
    private static KeyEncryptionUtils addPosition;
    private static String diviceId;
    private static UnikeyUrls unikeyUrls;

    private KeyEncryptionUtils() {
    }

    public static synchronized KeyEncryptionUtils getInstance() {
        if (addPosition == null) {
            addPosition = new KeyEncryptionUtils();
            getUnikey();
        }

        return addPosition;
    }


    private static void getUnikey() {
        try {
            diviceId = DeviceUtil.getDeviceId(CustomApplication.getContext());
            unikeyUrls = new UnikeyUrls(Constants.APPLY_PLUGIN, Constants.GET_CHALLEAGE, Constants.GET_BIT_AUTH_DATA, Constants.CHECK_BIT_AUTH_DATA, Constants.VERIFY_APP, Constants.CONFIRM_APPLY_PLUGIN_FORMAT);
            UniKey.getInstance().init(CustomApplication.getContext(), unikeyUrls, diviceId);
        } catch (UnikeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 手机号  加密
     *
     * @param data
     */
    public static void localEncryptMobile(String data) {
//        try {
//            if (!TextUtils.isEmpty(data)) {
        UserEntity userEntity = new UserEntity();
//                String Newdata = UniKey.getInstance().localEncrypt(data);
//                if (!TextUtils.isEmpty(Newdata)) {
        userEntity.setMobile(data);
        Db_PUB_USERS.UpdateMobile(userEntity);
//                } else {
////                    Helper.getInstance().showToast(CustomApplication.getContext(), "加密之后的数据为空，可能导致后面的操作异常");
//                }
//            } else {
////                Helper.getInstance().showToast(CustomApplication.getContext(), "进行加密的数据为空，可能导致后面的操作异常");
//            }
//        } catch (UnikeyException e) {
//            e.printStackTrace();
//
//        }

    }


    /**
     * 手机号  解密
     *
     * @return
     */
    public static String localDecryptMobile() {
        return localDecryptMobile(Db_PUB_USERS.queryingMobile());
    }

    public static String localDecryptMobile(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return "";
        }
        try {
            if (Helper.isMobileNO(mobile) && Helper.isNumbers(mobile)) {
                return mobile;
            }
            mobile = UniKey.getInstance().localDecrypt(mobile);
            return TextUtils.isEmpty(mobile)?"":mobile;
        } catch (UnikeyException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 资金账号  加密
     *
     * @param data
     */
    public static void localEncryptTradescno(String data) {
//        try {
//            if (!TextUtils.isEmpty(data)) {
        UserEntity userEntity = new UserEntity();
//                String Newdata = UniKey.getInstance().localEncrypt(data);
//                if (!TextUtils.isEmpty(Newdata)) {
        userEntity.setTradescno(data);
        Db_PUB_USERS.UpdateTradescno(userEntity);
//                } else {
//                    Helper.getInstance().showToast(CustomApplication.getContext(), "加密之后的数据为空，可能导致后面的操作异常");
//    }
//            } else {
//                Helper.getInstance().showToast(CustomApplication.getContext(), "进行加密的数据为空，可能导致后面的操作异常");
//            }
//        } catch (UnikeyException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 资金账号 解密
     *
     * @return
     */
    public static List<UserEntity> localDecryptTradescno() {
        return localDecryptTradescno(Db_PUB_USERS.queryingTradescno());
    }

    public static List<UserEntity> localDecryptTradescno(List<UserEntity> list) {
        List<UserEntity> newlist =  new ArrayList<>();
        try {
            UserEntity userEntity = new UserEntity();
            if (list != null && list.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {

                    String Newdata = list.get(i).getTradescno();

                    if (!TextUtils.isEmpty(Newdata)) {
                        if (Newdata.contains(",")) {

                            String[] accounts = Newdata.split(",");
                            newlist.add(userEntity);
                            for (int j = 0; j < accounts.length; j++) {

                                if (accounts[j].length() <= 9 && Helper.isNumbers(accounts[j])) {

                                    sb.append(accounts[j]).append(",").toString();

                                }
                            }
                            userEntity.setTradescno(sb.toString());

                        } else {
                            String newdata = UniKey.getInstance().localDecrypt(Newdata);
                            if (!TextUtils.isEmpty(newdata)) {
                                userEntity.setTradescno(newdata);
                                newlist.add(userEntity);
                            }
                        }
                    } else {
                        newlist = list;
                    }
                }
            }
        } catch (UnikeyException e) {
            e.printStackTrace();
        }
        return newlist;
    }


    /**
     * 查询 注册类型
     *
     * @return
     */
    public static String Typescno() {
        String Mobile = Db_PUB_USERS.queryingMobile();
        if (!TextUtils.isEmpty(Mobile)) {
            if (!Helper.isMobileNO(Mobile)) {
                String Typescno = Db_PUB_USERS.queryingTypescno();
                return Typescno;
            } else {
                String Typescno = Db_PUB_USERS.queryingTypescno();
                if (TextUtils.isEmpty(Typescno) || Typescno.equals("3")) {
                    UserEntity userUtil = new UserEntity();
                    userUtil.setTypescno("1");
                    Db_PUB_USERS.UpdateTypescno(userUtil);
                    String NewTypescno = Db_PUB_USERS.queryingTypescno();
                    return NewTypescno;
                }
            }
        }
        return Db_PUB_USERS.queryingTypescno();
    }


    /**
     * 双向认证过程中会生成sessionkey
     * <p>
     * 加密调用
     *
     * @param sessionkey
     */
    public static String encryptBySessionKey(String sessionkey) {
        String mSessionkey = "";
        try {
            if ("0".equals(UserUtil.Keyboard)) {
                mSessionkey = sessionkey;
            } else {
                mSessionkey = UniKey.getInstance().encryptBySessionKey(sessionkey);
            }
        } catch (UnikeyException e) {
            e.printStackTrace();
        }
        return mSessionkey;
    }

    /**
     * 解密调用：
     *
     * @param sessionkey
     * @return
     */
    public String decryptBySessionKey(String sessionkey) {
        String mSessionkey = "";
        try {
            mSessionkey = UniKey.getInstance().decryptBySessionKey(sessionkey);
        } catch (UnikeyException e) {
            e.printStackTrace();
        }
        return mSessionkey;
    }
}
