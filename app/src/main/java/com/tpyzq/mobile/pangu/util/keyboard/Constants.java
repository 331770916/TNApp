package com.tpyzq.mobile.pangu.util.keyboard;


import com.tpyzq.mobile.pangu.util.ConstantUtil;

/**
 * Created by xusheng on 2016/1/18.
 */
public class Constants {

    //获取插件
    public static String APPLY_PLUGIN = String.format("%s/unikeyAuth/servlet?funid=200001&appid=%s", ConstantUtil.JY_IP,  ConstantUtil.APP_ID);
    //激活认证
    public static String CONFIRM_APPLY_PLUGIN_FORMAT = String.format("%s/unikeyAuth/servlet?funid=200012&appid=%s", ConstantUtil.JY_IP,  ConstantUtil.APP_ID);
    //请求挑战码
    public static String GET_CHALLEAGE = String.format("%s/unikeyAuth/servlet?funid=200002&appid=%s", ConstantUtil.JY_IP,  ConstantUtil.APP_ID);
    //验证认证码
    public static String VERIFY_CODE = String.format("%s/unikeyAuth/servlet?funid=200003&appid=%s", ConstantUtil.JY_IP,  ConstantUtil.APP_ID);
    // 校验app信息
    public static String VERIFY_APP = String.format("%s/unikeyAuth/servlet?funid=200004&appid=%s", ConstantUtil.JY_IP,  ConstantUtil.APP_ID);
    //双向认证第一部
    public static String GET_BIT_AUTH_DATA = String.format("%s/unikeyAuth/servlet?funid=200006&appid=%s", ConstantUtil.JY_IP,  ConstantUtil.APP_ID);
    //双向认证第二步
    public static String CHECK_BIT_AUTH_DATA = String.format("%s/unikeyAuth/servlet?funid=200007&appid=%s", ConstantUtil.JY_IP,  ConstantUtil.APP_ID);
    //加密
    public static String ENCRYPT = String.format("%s/unikeyAuth/servlet?funid=200008&appid=%s", ConstantUtil.JY_IP,  ConstantUtil.APP_ID);
    //解密
    public static String DECRPT = String.format("%s/unikeyAuth/servlet?funid=200009&appid=%s", ConstantUtil.JY_IP,  ConstantUtil.APP_ID);

    //访问网络请求的标识
    public static final int APPLY_PLUGIN_REQ = 1;
    public static final int GET_CODE_REQ = 6;
    public static final int VERIFY_CODE_REQ = 7;
    public static final int GET_SERVER_CRYPTSTR = 8;
    public static final int Bi_AUTH_FIRST_REQ = 10;
    public static final int GET_KEYBOARD_INPUT_DECRYPTED_REQ = 12;
    public static final int VERIFY_APP_REQ = 13;

}
