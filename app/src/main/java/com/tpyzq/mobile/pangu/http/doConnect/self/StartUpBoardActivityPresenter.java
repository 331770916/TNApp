package com.tpyzq.mobile.pangu.http.doConnect.self;

import android.content.Intent;
import android.text.TextUtils;

import com.tpyzq.mobile.pangu.activity.myself.handhall.ChangeDepositBankActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.StartyUpBoardActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.data.PartnerInfoEntity;
import com.tpyzq.mobile.pangu.data.SecondContactsEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * anthor:Created by tianchen on 2017/3/20.
 * email:963181974@qq.com
 */

public class StartUpBoardActivityPresenter {

    private StartyUpBoardActivity activity;

    public StartUpBoardActivityPresenter(StartyUpBoardActivity activity) {
        this.activity = activity;
    }

    public StartyUpBoardActivity getActivity() {
        return activity;
    }

    public void transact() {
        String funcid = "300605";
        String token = SpUtils.getString(activity, "mSession", null);
        Map<String, Object> parms = new HashMap<>();
        Map map = new HashMap();
        map.put("SEC_ID", "tpyzq");
        map.put("FLAG", "true");
        parms.put("funcid", funcid);
        parms.put("token", token);
        parms.put("parms", map);
        activity.setLoading(true);
        OkHttpUtil.okHttpForPostStringTime(StartyUpBoardActivity.TAG, ConstantUtil.getURL_JY_HS(), parms, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                activity.setHint(ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                activity.setLoading(false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String data = jsonObject.getString("data");
                    String msg = jsonObject.getString("msg");

                    if ("-6".equals(code)) {
                        Intent intent = new Intent();
                        if (!Db_PUB_USERS.isRegister()) {
                            intent = new Intent(activity, ShouJiZhuCeActivity.class);
                            activity.startActivity(intent);
                        } else if (!Db_PUB_USERS.islogin()) {
                            intent = new Intent();
                            intent.setClass(activity, TransactionLoginActivity.class);
                            activity.startActivity(intent);
                        } else {
                            intent = new Intent();
                            intent.setClass(activity, TransactionLoginActivity.class);
                            activity.startActivity(intent);
                        }
                        activity.finish();
                        return;
                    }

                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        PartnerInfoEntity partnerInfo = new PartnerInfoEntity();
                        partnerInfo.STATUS = jsonArray.getJSONObject(0).getString("STATUS");
                        partnerInfo.STOCK_ACCOUNT = jsonArray.getJSONObject(0).getString("STOCK_ACCOUNT");
                        partnerInfo.RIGHT_OPEN_DATE = jsonArray.getJSONObject(0).getString("RIGHT_OPEN_DATE");

                        activity.setPartnerInfo(partnerInfo);
                        if (TextUtils.isEmpty(partnerInfo.STATUS)) {
                            return;
                        }
                        switch (partnerInfo.STATUS) {
                            case "0":
                                activity.startFragment(4);
                                break;
                            case "1":
                                activity.startFragment(4);
                                setEndflag();
                                break;
                            case "2":
                                activity.startFragment(1);
                                break;
                        }
                    } else {
                        activity.setHint(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    activity.setHint(ConstantUtil.JSON_ERROR);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    activity.setHint(ConstantUtil.JSON_ERROR);
                }
            }
        },30000);
    }

    public void setEndflag() {
        activity.setEndflag(false);
    }

    public void getSecondContacts(final StartyUpBoardActivity.SecondInfo secondInfo) {
        activity.setLoading(true);
        String funcid = "300609";
        String token = SpUtils.getString(activity, "mSession", null);
        Map<String, Object> parms = new HashMap<>();
        Map map = new HashMap();
        map.put("SEC_ID", "tpyzq");
        map.put("FLAG", "true");
        parms.put("funcid", funcid);
        parms.put("token", token);
        parms.put("parms", map);
        OkHttpUtil.okHttpForPostString(StartyUpBoardActivity.TAG, ConstantUtil.getURL_JY_HS(), parms, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                activity.setHint(ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                activity.setLoading(false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String data = jsonObject.getString("data");
                    String msg = jsonObject.getString("msg");

                    if ("-6".equals(code)) {
                        Intent intent = new Intent();
                        if (!Db_PUB_USERS.isRegister()) {
                            intent = new Intent(activity, ShouJiZhuCeActivity.class);
                            activity.startActivity(intent);
                        } else if (!Db_PUB_USERS.islogin()) {
                            intent = new Intent();
                            intent.setClass(activity, TransactionLoginActivity.class);
                            activity.startActivity(intent);
                        } else {
                            intent = new Intent();
                            intent.setClass(activity, TransactionLoginActivity.class);
                            activity.startActivity(intent);
                        }
                        activity.finish();
                        return;
                    }

                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        JSONObject jo_data = jsonArray.getJSONObject(0);
                        SecondContactsEntity secondContacts = new SecondContactsEntity();
                        secondContacts.RELATIONSHIP = jo_data.getString("RELATIONSHIP");
                        secondContacts.SECOND_NAME = jo_data.getString("SECOND_NAME");
                        secondContacts.SECOND_MOBILE = jo_data.getString("SECOND_MOBILE");
                        activity.setSecondContacts(secondContacts, secondInfo);

                    } else if ("-6".equals(code)) {
                        activity.startActivity(new Intent(activity, TransactionLoginActivity.class));
                    } else {
                        activity.setHint(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    activity.setHint(ConstantUtil.JSON_ERROR);
                }
            }
        });
    }

    public void setSecondContacts(String name, String phone, int relationship) {
        activity.setLoading(true);
        String funcid = "300606";
        String token = SpUtils.getString(activity, "mSession", null);
        Map<String, Object> parms = new HashMap<>();
        Map map = new HashMap();
        map.put("SEC_ID", "tpyzq");
        map.put("FLAG", "true");
        map.put("SEC_RELATION_NAME", name);
        map.put("SEC_REPATION_PHONE", phone);
        map.put("SOCIALRAL_TYPE", relationship);
        parms.put("funcid", funcid);
        parms.put("token", token);
        parms.put("parms", map);
        OkHttpUtil.okHttpForPostString(StartyUpBoardActivity.TAG, ConstantUtil.getURL_JY_HS(), parms, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                activity.setHint(ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                activity.setLoading(false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String data = jsonObject.getString("data");
                    String msg = jsonObject.getString("msg");

                    if ("-6".equals(code)) {
                        Intent intent = new Intent();
                        if (!Db_PUB_USERS.isRegister()) {
                            intent = new Intent(activity, ShouJiZhuCeActivity.class);
                            activity.startActivity(intent);
                        } else if (!Db_PUB_USERS.islogin()) {
                            intent = new Intent();
                            intent.setClass(activity, TransactionLoginActivity.class);
                            activity.startActivity(intent);
                        } else {
                            intent = new Intent();
                            intent.setClass(activity, TransactionLoginActivity.class);
                            activity.startActivity(intent);
                        }
                        activity.finish();
                        return;
                    }

                    if ("0".equals(code)) {
                        activity.startFragment(3);
                    } else if ("-6".equals(code)) {
                        activity.startActivity(new Intent(activity, TransactionLoginActivity.class));
                    } else {
                        activity.setHint(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    activity.setHint(ConstantUtil.JSON_ERROR);
                }
            }
        });
    }

    public void setGEMRegister() {
        activity.setLoading(true);
        String funcid = "300607";
        String token = SpUtils.getString(activity, "mSession", null);
        Map<String, Object> parms = new HashMap<>();
        Map map = new HashMap();
        map.put("SEC_ID", "tpyzq");
        map.put("FLAG", "true");
        parms.put("funcid", funcid);
        parms.put("token", token);
        parms.put("parms", map);
        OkHttpUtil.okHttpForPostString(StartyUpBoardActivity.TAG, ConstantUtil.getURL_JY_HS(), parms, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                activity.setHint(ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                activity.setLoading(false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String data = jsonObject.getString("data");
                    String msg = jsonObject.getString("msg");

                    if ("-6".equals(code)) {
                        Intent intent = new Intent();
                        if (!Db_PUB_USERS.isRegister()) {
                            intent = new Intent(activity, ShouJiZhuCeActivity.class);
                            activity.startActivity(intent);
                        } else if (!Db_PUB_USERS.islogin()) {
                            intent = new Intent();
                            intent.setClass(activity, TransactionLoginActivity.class);
                            activity.startActivity(intent);
                        } else {
                            intent = new Intent();
                            intent.setClass(activity, TransactionLoginActivity.class);
                            activity.startActivity(intent);
                        }
                        activity.finish();
                        return;
                    }

                    if ("0".equals(code)) {
                        PartnerInfoEntity partnerInfo = activity.getPartnerInfo();
                        partnerInfo.STATUS = "0";
                        activity.setPartnerInfo(partnerInfo);
                        setEndflag();
                        activity.startFragment(4);
                    } else if ("-6".equals(code)) {
                        activity.startActivity(new Intent(activity, TransactionLoginActivity.class));
                    } else {
                        activity.setHint(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    activity.setHint(ConstantUtil.JSON_ERROR);
                }
            }
        });
    }
}
