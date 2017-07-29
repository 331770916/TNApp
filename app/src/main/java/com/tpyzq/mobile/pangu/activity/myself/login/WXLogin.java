package com.tpyzq.mobile.pangu.activity.myself.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_HOME_INFO;
import com.tpyzq.mobile.pangu.db.Db_PUB_OPTIONALHISTORYSTOCK;
import com.tpyzq.mobile.pangu.db.Db_PUB_SEARCHHISTORYSTOCK;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.doConnect.self.AddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.QuerySelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToAddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToQuerySelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;



/**
 * Created by wangqi on 2017/2/15.
 * 微信登录
 */


public class WXLogin implements ICallbackResult {
    private static String TAG = "WXLogin";
    private Context mContext;
    private UMShareAPI mShareAPI;
    private Dialog mLoadingDialog;
    private Marked mMarked;

    public WXLogin(Context context, Marked marked, int Imarker) {
        mContext = context;
        mMarked = marked;
        if (Imarker==0){
            inintView();
        }

    }


    private void inintView() {
        mShareAPI = UMShareAPI.get(mContext);
        mShareAPI.isInstall((Activity) mContext, SHARE_MEDIA.WEIXIN);
        //授权成功
        mShareAPI.doOauthVerify((Activity) mContext, SHARE_MEDIA.WEIXIN, wwumAuthListener);
    }

    private UMAuthListener wwumAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (mContext instanceof Activity&&((Activity)mContext).isFinishing())return;
            //获取用户信息
            mLoadingDialog = LoadingDialog.initDialog((Activity) mContext, "正在提交...");
            //显示Dialog
            mLoadingDialog.show();
            mShareAPI.getPlatformInfo((Activity) mContext, SHARE_MEDIA.WEIXIN, umAuthListener);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Helper.getInstance().showToast(mContext, "授权失败");
            mMarked.MarkedLogic(false);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Helper.getInstance().showToast(mContext, "授权失败");
            mMarked.MarkedLogic(false);
        }

//        openid:openid
//        unionid:（6.2以前用unionid）用户id
//        accesstoken: accessToken （6.2以前用access_token）
//        refreshtoken: refreshtoken: （6.2以前用refresh_token）
//        过期时间：expiration （6.2以前用expires_in）
//        name：name（6.2以前用screen_name）
//        城市：city
//        省份：prvinice
//        国家：country
//        性别：gender
//        头像：iconurl（6.2以前用profile_image_url）

        private UMAuthListener umAuthListener = new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if (map == null || map.size() < 1) {
                    return;
                }

                Set<String> set = null;
                try {
                    set = map.keySet();
                    String openid = map.get("openid");
                    String screen_name = map.get("screen_name");
                    InscriptionRegistry(openid, screen_name);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }


            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                throwable.printStackTrace();
                Helper.getInstance().showToast(mContext, "授权失败");
                mMarked.MarkedLogic(false);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Helper.getInstance().showToast(mContext, "授权失败");
                mMarked.MarkedLogic(false);

            }
        };
    };


    /**
     * 注册登录
     */
    private void InscriptionRegistry(final String openid, final String screen_name) {
        String mRegId = SpUtils.getString(mContext, "RegId", "");
        Map map = new HashMap<>();
        Map map1 = new HashMap<>();
        map.put("funcid", "800100");
        map.put("token", "");
        map.put("parms", map1);
        map1.put("user_type", "3");
        map1.put("user_account", openid);
        map1.put("phone_type", "1");
        map1.put("nickname", screen_name);
        map1.put("token", mRegId);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_NEW(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                Helper.getInstance().showToast(mContext, "网络异常");
                mLoadingDialog.dismiss();
                mMarked.MarkedLogic(false);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                response = response.replace("Served at: /Bigdata", "");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    if ("0".equals(code)) {
                        WipeData();
                        UserEntity userEntity = new UserEntity();
                        userEntity.setScno(screen_name);                                 //注册账号
                        userEntity.setIsregister("0");                                   //0是注册用户，1是未注册用户
                        userEntity.setTypescno("3");                                     //账号类型（4:微博 1:手机 2:QQ 3:微信）
                        userEntity.setRegisterID(openid);                                //注册账号标识ID

                        Db_PUB_USERS.UpdateScno(userEntity);                             //修改 注册账号
                        Db_PUB_USERS.UpdateTypescno(userEntity);                         //修改 账号类型（4:微博 1:手机 2:QQ 3:微信）
                        Db_PUB_USERS.UpdateRegister(userEntity);                         //修改 注册标识ID

                        HOLD_SEQ.deleteAll();
                        SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);
                        updateNewUserSelfChoice();

                        Db_PUB_USERS.UpdateIsregister(userEntity);                       //修改  是否注册用户
                        mLoadingDialog.dismiss();
                        SpUtils.putString(mContext, "First", "");
                        mMarked.MarkedLogic(true);

                    } else if (code.equals("1")) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setScno(screen_name);                                 //注册账号
                        userEntity.setIsregister("0");                                   //0是注册用户，1是未注册用户
                        userEntity.setTypescno("3");                                     //账号类型（4:微博 1:手机 2:QQ 3:微信）
                        userEntity.setRegisterID(openid);                                //注册账号标识ID

                        //修改 数据储存
                        Db_PUB_USERS.UpdateScno(userEntity);                             //修改 注册账号
                        Db_PUB_USERS.UpdateTypescno(userEntity);                         //修改 账号类型（0:qq 1:微信 2:微博 3:手机）
                        Db_PUB_USERS.UpdateRegister(userEntity);                         //修改 注册标识ID
                        Db_PUB_USERS.UpdateIsregister(userEntity);                       //修改  是否注册用户
                        UserUtil.refrushUserInfo();


                        //删除数据股所有自选股
                        HOLD_SEQ.deleteAll();
                        SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);
                        Db_PUB_STOCKLIST.deleteAllStocListkDatas();                            //从云端下载数据库自选股
                        Db_HOME_INFO.deleteAllSelfNewsDatas();//删除自选股新闻

                        SimpleRemoteControl mSimpleRemoteControl = new SimpleRemoteControl((ICallbackResult) mContext);
                        mSimpleRemoteControl.setCommand(new ToQuerySelfChoiceStockConnect(new QuerySelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, UserUtil.userId)));
                        mSimpleRemoteControl.startConnect();

                        mLoadingDialog.dismiss();
                        SpUtils.putString(mContext, "First", "");
                        mMarked.MarkedLogic(true);

                    } else {
                        mLoadingDialog.dismiss();
                        MistakeDialog.showDialog(msg, (Activity) mContext);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //切换手机把之前的数据清空
    private void WipeData() {
        UserEntity user = new UserEntity();
        user.setMobile("");
        user.setScno("");
        user.setRegisterID("");
        user.setTypescno("");
        user.setTradescno("");

        Db_PUB_USERS.UpdateMobile(user);
        Db_PUB_USERS.UpdateScno(user);
        Db_PUB_USERS.UpdateRegister(user);
        Db_PUB_USERS.UpdateTypescno(user);
        Db_PUB_USERS.UpdateTradescno(user);
    }

    /**
     * 当新用户更新自选股
     */
    private void updateNewUserSelfChoice() {
        List<StockInfoEntity> mChartTimeDatas = Db_PUB_STOCKLIST.queryStockListDatas();
        if (mChartTimeDatas != null && mChartTimeDatas.size() > 0) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < mChartTimeDatas.size(); i++) {
                String stockNumber = mChartTimeDatas.get(i).getStockNumber();
                String stockName = mChartTimeDatas.get(i).getStockName();
                String NewPrice = mChartTimeDatas.get(i).getNewPrice();

                sb.append(stockNumber).append(",");
                sb1.append(stockName).append(",");
                sb2.append(NewPrice).append(",");

            }
            String stockNumbers = sb.toString();
            String stockNames = sb1.toString();
            String NewPrices = sb2.toString();

            NewPrices.substring(0, NewPrices.length() - 1);
            UserUtil.refrushUserInfo();

            if (Db_PUB_USERS.isRegister()) {
                Db_PUB_STOCKLIST.deleteAllStocListkDatas();
                Db_HOME_INFO.deleteAllSelfNewsDatas();
            } else {
                SimpleRemoteControl mSimpleRemoteControl = new SimpleRemoteControl((ICallbackResult) mContext);
                mSimpleRemoteControl.setCommand(new ToAddSelfChoiceStockConnect(new AddSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stockNumbers, UserUtil.userId, stockNames, NewPrices)));
                mSimpleRemoteControl.startConnect();
            }
        }
    }

    @Override
    public void getResult(Object result, String tag) {
        if ("AddSelfChoiceStockConnect".equals(tag)) {
            String msg = (String) result;
            SelfStockHelper.explanOneTimiceAddSelfChoiceResult((Activity) mContext, msg);
        } else if ("QuerySelfChoiceStockConnect".equals(tag)) {
            //老用户
            if (result instanceof String) {
                MistakeDialog.showDialog("导入自选股异常" + result.toString(), (Activity) mContext, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {

                    }
                });

            } else {
                //把查询到的股票添加到数据库
                ArrayList<StockInfoEntity> stockInfoEntities = (ArrayList<StockInfoEntity>) result;

                if (stockInfoEntities != null && stockInfoEntities.size() > 0) {
                    for (StockInfoEntity stockInfoEntity : stockInfoEntities) {
                        Db_PUB_STOCKLIST.addOneStockListData(stockInfoEntity);
                        SelfStockHelper.sendUpdateSelfChoiceBrodcast((Activity) mContext, stockInfoEntity.getStockNumber());
                    }
                }
            }
        }
    }


    //清空数据库数据
    private void setData() {
        UserEntity userEntity = new UserEntity();
        userEntity.setIslogin("false");
        userEntity.setTradescno("");
        userEntity.setMobile("");

        SpUtils.putString(CustomApplication.getContext(), "mSession", "");
        Db_PUB_USERS.UpdateMobile(userEntity);
        Db_PUB_USERS.UpdateIslogin(userEntity);
        Db_PUB_USERS.UpdateTradescno(userEntity);
        Db_PUB_SEARCHHISTORYSTOCK.deleteAllDatas();
        Db_PUB_OPTIONALHISTORYSTOCK.deleteAllDatas();
        Db_PUB_USERS.clearRefreshTime();

    }

    public interface Marked {
        void MarkedLogic(boolean isMarked);
    }
}