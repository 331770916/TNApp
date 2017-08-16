package com.tpyzq.mobile.pangu.base;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.AddOrModFixFundActivity;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.FixFundEntity;
import com.tpyzq.mobile.pangu.data.FundDataEntity;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.NewStockEnitiy;
import com.tpyzq.mobile.pangu.data.OTC_AffirmBean;
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionCommitBean;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StockHolderInfoEntity;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.DeviceUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bonree.k.M;
import okhttp3.Call;

/**
 * Created by ltyhome on 23/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: Interface Manager
 * 0 success 400 net error -2 json error -3 no data -6 jump
 */
public class InterfaceCollection {
    //网络请求类
    private NetWorkUtil net;

    /**
     * 构造方法
     */
    private InterfaceCollection() {
        net = NetWorkUtil.getInstence();
    }

    /**
     * 单例实现
     */
    private static class InterfaceHolder {
        private static final InterfaceCollection INSTANCE = new InterfaceCollection();
    }

    public static final InterfaceCollection getInstance() {
        return InterfaceHolder.INSTANCE;
    }

    /**
     * 回调接口
     */
    public interface InterfaceCallback {
        void callResult(ResultInfo info);
    }

    /**
     * 300200
     * 分级基金 选择基金
     *
     * @param session  token
     * @param TAG      tag
     * @param callback callback
     */
    public void Fundchoice(String session, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        HashMap map = new HashMap();
        map.put("funcid", "300200");
        map.put("token", session);
        HashMap hashMap = new HashMap();
        hashMap.put("FLAG", "true");
        hashMap.put("SEC_ID", "tpyzq");
        map.put("parms", hashMap);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        if ("0".equals(code)) {
                            List<StructuredFundEntity> list = new ArrayList<StructuredFundEntity>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                bean.setStocken_code(data.optJSONObject(i).optString("SECU_CODE"));
                                bean.setMarket(data.optJSONObject(i).optString("MARKET"));
                                bean.setStoken_name(data.optJSONObject(i).optString("SECU_NAME"));
                                list.add(bean);
                            }

                            info.setData(list);
                        }

                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 300701
     * 分级基金信息查询
     *
     * @param session    token
     * @param stock_code 证券代码
     * @param TAG        tag
     * @param callback   callback
     */
    public void queryStructuredFund(String session, String stock_code, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300701");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map2.put("STOCK_CODE", stock_code);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setStoken_name(obj.optString("STOCK_NAME"));
                                bean.setMerge_amount(obj.optString("MERGE_AMOUNT"));
                                bean.setSplit_amount(obj.optString("SPLIT_AMOUNT"));
                                bean.setExchange_type(obj.optString("EXCHANGE_TYPE"));
                                bean.setFund_status(obj.optString("FUND_STATUS"));
                                bean.setStock_account(obj.optString("STOCK_ACCOUNT"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 300702
     * 分级基金的合并
     *
     * @param exchange_type  交易类别
     * @param stock_account  当前市场的主证券账户
     * @param stock_code     证券代码
     * @param entrust_amount 委托数量
     * @param session        token
     * @param TAG            tag
     * @param callback       callback
     */
    public void mergerStructuredFund(String exchange_type, String stock_account, String stock_code, String entrust_amount, String session, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300702");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("EXCHANGE_TYPE", exchange_type);
        map2.put("STOCK_ACCOUNT", stock_account);
        map2.put("STOCK_CODE", stock_code);
        map2.put("ENTRUST_AMOUNT", entrust_amount);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            JSONArray data = jsonObject.optJSONArray("data");
                            StructuredFundEntity bean = new StructuredFundEntity();
                            JSONObject obj = data.optJSONObject(0);
                            bean.setInit_date(obj.optString("INIT_DATE"));
                            bean.setMerge_amount(obj.optString("ENTRUST_NO"));
                            info.setData(bean);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 300703
     * 分级基金的拆分
     *
     * @param exchange_type  交易类别
     * @param stock_account  当前市场的主证券账户
     * @param stock_code     证券代码
     * @param entrust_amount 委托数量
     * @param session        token
     * @param TAG            tag
     */
    public void splitStructuredFund(String exchange_type, String stock_account, String stock_code, String entrust_amount, String session, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300703");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("EXCHANGE_TYPE", exchange_type);
        map2.put("STOCK_ACCOUNT", stock_account);
        map2.put("STOCK_CODE", stock_code);
        map2.put("ENTRUST_AMOUNT", entrust_amount);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            JSONArray data = jsonObject.optJSONArray("data");
                            StructuredFundEntity bean = new StructuredFundEntity();
                            JSONObject obj = data.optJSONObject(0);
                            bean.setInit_date(obj.optString("INIT_DATE"));
                            bean.setMerge_amount(obj.optString("ENTRUST_NO"));
                            info.setData(bean);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 300704
     * 分级基金当日委托查询
     *
     * @param session   token
     * @param page      查第一页不用传
     * @param num       查询行数
     * @param action_in 查询可撤  0查所有  1查可撤
     * @param TAG       tag
     */
    public void queryTodayEntrust(String session, String page, String num, String action_in, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300704");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("POSITION_STR", page);
        map2.put("REQUEST_NUM", num);
        map2.put("ACTION_IN", action_in);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setStoken_name(obj.optString("STOCK_NAME"));
                                bean.setStocken_code(obj.optString("STOCK_CODE"));
                                bean.setBusiness_name(obj.optString("BUSINESS_NAME"));
                                bean.setReport_time(obj.optString("CURR_TIME"));
                                bean.setEntrust_amount(obj.optString("ENTRUST_AMOUNT"));
                                bean.setStock_account(obj.optString("STOCK_ACCOUNT"));
                                bean.setPosition_str(obj.optString("POSITION_STR"));
                                bean.setCurr_date(obj.optString("CURR_DATE"));
                                bean.setEntrust_no(obj.optString("ENTRUST_NO"));
                                bean.setEntrust_status(obj.optString("ENTRUST_STATUS"));
                                bean.setEntrust_balance(obj.optString("ENTRUST_BALANCE"));
                                bean.setBusiness_amount(obj.optString("BUSINESS_AMOUNT"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300705
     * 分级基金分级基金撤单
     *
     * @param session    token
     * @param entrust_no 委托编号
     * @param TAG        tag
     */
    public void fundWithdrawOrder(String session, String entrust_no, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300705");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("ENTRUST_NO", entrust_no);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setInit_date(obj.optString("INIT_DATE"));
                                bean.setEntrust_no(obj.optString("ENTRUST_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300706
     * 分级基金历史委托查询
     *
     * @param session    token
     * @param TAG        tag
     * @param callback   callback
     * @param page       page
     * @param num        num
     * @param his_type   历史类型 0:自定义  1：一周内  2：一个月内  3：三个月内
     * @param start_date 开始时间  HIS_TYPE不为0时可传空
     * @param end_date   结束时间   HIS_TYPE不为0时可传空
     */
    public void queryHistoryEntrust(String session, String page, String num, String his_type, String start_date, String end_date, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300706");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("POSITION_STR", page);
        map2.put("REQUEST_NUM", num);
        map2.put("HIS_TYPE", his_type);
        map2.put("START_DATE", start_date);
        map2.put("END_DATE", end_date);
        map2.put("FLAG", "true");
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setStoken_name(obj.optString("STOCK_NAME"));
                                bean.setStocken_code(obj.optString("STOCK_CODE"));
                                bean.setBusiness_name(obj.optString("BUSINESS_NAME"));
                                bean.setReport_time(obj.optString("REPORT_TIME"));
                                bean.setEntrust_amount(obj.optString("ENTRUST_AMOUNT"));
                                bean.setStock_account(obj.optString("STOCK_ACCOUNT"));
                                bean.setPosition_str(obj.optString("POSITION_STR"));
                                bean.setCurr_date(obj.optString("CURR_DATE"));
                                bean.setEntrust_no(obj.optString("ENTRUST_NO"));
                                bean.setEntrust_status(obj.optString("ENTRUST_STATUS"));
                                bean.setEntrust_balance(obj.optString("ENTRUST_BALANCE"));
                                bean.setBusiness_amount(obj.optString("BUSINESS_AMOUNT"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300707
     * 分级基金当日成交查询
     *
     * @param session  token
     * @param page     page
     * @param num      num
     * @param TAG      tag
     * @param callback callback
     */
    public void queryTodayDeal(String session, String page, String num, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300707");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("POSITION_STR", page);
        map2.put("REQUEST_NUM", num);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setStoken_name(obj.optString("STOCK_NAME"));
                                bean.setStocken_code(obj.optString("STOCK_CODE"));
                                bean.setBusiness_name(obj.optString("BUSINESS_NAME"));
                                bean.setReport_time(obj.optString("RETURN_TIME"));
                                bean.setInit_date(obj.optString("INIT_DATE"));
                                bean.setPosition_str(obj.optString("POSITION_STR"));
                                bean.setEntrust_amount(obj.optString("ENTRUST_AMOUNT"));
                                bean.setEntrust_balance(obj.optString("ENTRUST_BALANCE"));
                                bean.setSerial_no(obj.optString("SERIAL_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300708
     * 分级基金历史成交查询
     *
     * @param session    token
     * @param page       page
     * @param num        num
     * @param his_type   0:自定义  1：一周内  2：一个月内   3：三个月内
     * @param start_date 开始时间  HIS_TYPE不为0时可传空
     * @param end_date   结束时间  HIS_TYPE不为0时可传空
     * @param TAG        tag
     * @param callback   callback
     */
    public void queryHistoryDeal(String session, String page, String num, String his_type, String start_date, String end_date, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300708");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("POSITION_STR", page);
        map2.put("REQUEST_NUM", num);
        map2.put("HIS_TYPE", his_type);
        map2.put("START_DATE", start_date);
        map2.put("END_DATE", end_date);
        map2.put("FLAG", "true");
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setStoken_name(obj.optString("SECU_NAME"));
                                bean.setStocken_code(obj.optString("SECU_CODE"));
                                bean.setBusiness_name(obj.optString("BUSINESS_NAME"));
                                bean.setEntrust_status(obj.optString("BUSINESS_STATUS"));
                                bean.setReport_time(obj.optString("MATCHED_TIME"));
                                bean.setInit_date(obj.optString("INIT_DATE"));
                                bean.setPosition_str(obj.optString("KEY_STR"));
                                bean.setEntrust_amount(obj.optString("ENTRUST_AMOUNT"));//委托量等同分拆份额
                                bean.setEntrust_balance(obj.optString("ENTRUST_BALANCE"));
                                bean.setBusiness_amount(obj.optString("MATCHED_QTY"));
                                bean.setMatched_price(obj.optString("MATCHED_PRICE"));
                                bean.setMatched_amt(obj.optString("MATCHED_AMT"));
                                bean.setSerial_no(obj.optString("SERIAL_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 300801
     * 查询网络投票列表
     *
     * @param session       token
     * @param exchange_type 市场
     * @param page          page
     * @param num           num
     * @param TAG           tag
     * @param callback      callback
     */
    public void queryNetworkVoting(String session, String exchange_type, String page, String num, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300801");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("EXCHANGE_TYPE", exchange_type);
        map2.put("POSITION_STR", page);
        map2.put("REQUEST_NUM", num);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<NetworkVotingEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                NetworkVotingEntity bean = new NetworkVotingEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setStock_account(obj.optString("STOCK_ACCOUNT"));
                                bean.setExchange_type(obj.optString("EXCHANGE_TYPE"));
                                bean.setMeeting_name(obj.optString("MEETING_NAME"));
                                bean.setMeeting_seq(obj.optString("MEETING_SEQ"));
                                bean.setCompany_name(obj.optString("COMPANY_NAME"));
                                bean.setCompany_code(obj.optString("COMPANY_CODE"));
                                bean.setBegin_date(obj.optString("BEGIN_DATE"));
                                bean.setEnd_date(obj.optString("END_DATE"));
                                bean.setInit_date(obj.optString("INIT_DATE"));
                                bean.setPosition_str(obj.optString("POSITION_STR"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300802
     * 议案信息获取
     *
     * @param session     token
     * @param meeting_seq 股东大会编码
     * @param TAG         tag
     * @param callback    callback
     */
    public void queryProposal(String session, String meeting_seq, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300802");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("MEETING_SEQ", meeting_seq);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            Map<String, List<NetworkVotingEntity>> map = new HashMap<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            map.put("0", parseJSONArray(data.optJSONArray(0)));
                            map.put("1", parseJSONArray(data.optJSONArray(1)));
                            info.setData(map);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 解析json数据
     *
     * @param array json数组
     * @return List<NetworkVotingEntity>
     * @throws JSONException
     */
    private List<NetworkVotingEntity> parseJSONArray(JSONArray array) throws JSONException {
        List<NetworkVotingEntity> ses = new ArrayList<>();
        NetworkVotingEntity bean;
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.optJSONObject(i);
            String vt = obj.optString("VOTE_TYPE");
            bean = new NetworkVotingEntity();
            bean.setVote_info(obj.optString("VOTE_INFO"));
            bean.setEntrust_amount(obj.optString("VOTE_NUMCONTROL"));
            bean.setStatus(obj.optString("EN_REFCODE"));//stock_code入参
            bean.setInit_date(obj.optString("INIT_DATE"));
            bean.setVote_motion(obj.optString("VOTE_MOTION"));
            bean.setMeeting_seq(obj.optString("MEETING_SEQ"));
            bean.setVote_type(vt);
            String list = obj.optString("LIST");
            if (vt.equals("1") && !TextUtils.isEmpty(list)) {
                JSONArray arr = new JSONArray(list);
                bean.setList(parseJSONArray(arr));
            }
            ses.add(bean);
        }
        return ses;
    }


    /**
     * 300803
     * 提交投票
     *
     * @param session  token
     * @param list     投票实体列表
     * @param TAG      tag
     * @param callback callback
     */
    public void submitVoting(String session, String stock_code, String exchage_type, String meeting_seq, List<NetworkVotingEntity> list, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300803");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("STOCK_CODE", stock_code);
        map2.put("EXCHANGE_TYPE", exchage_type);
        map2.put("MEETING_SEQ", stock_code);
        List data = new ArrayList();
        for (NetworkVotingEntity entity : list) {
            Map map = new HashMap<>();
            map.put("ENTRUST_AMOUNT", entity.getEntrust_no() == null ? "1" : entity.getEntrust_no());
            map.put("ENTRUST_PRICE", entity.getVote_motion());
            map.put("MEETING_SEQ", entity.getMeeting_seq());
            data.add(map);
        }
        map2.put("LIST", data);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300804
     * 历史网络投票结果查询
     *
     * @param session    token
     * @param begin_date 起始日期
     * @param end_date   到期日期
     * @param TAG        tag
     * @param callback   callback
     */
    public void queryHistoryNetworkVoting(String session, String his_type, String exchange_type, String position_str, String request_num, String begin_date, String end_date, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300804");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("HIS_TYPE", his_type);
        map2.put("POSITION_STR", position_str);
        map2.put("REQUEST_NUM", request_num);
        map2.put("EXCHANGE_TYPE", exchange_type);
        map2.put("BEGIN_DATE", begin_date);
        map2.put("END_DATE", end_date);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<NetworkVotingEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                NetworkVotingEntity bean = new NetworkVotingEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setStock_code(obj.optString("STOCK_CODE"));
                                bean.setBusiness_amount(obj.optString("BUSINESS_AMOUNT"));
                                bean.setStatus(obj.optString("ENTRUST_STATUS"));
                                bean.setEntrust_status_name(obj.optString("ENTRUST_STATUS_NAME"));
                                bean.setStock_name(obj.optString("STOCK_NAME"));
                                bean.setMeeting_seq(obj.optString("MEETING_SEQ"));
                                bean.setInit_date(obj.optString("INIT_DATE"));
                                bean.setEntrust_no(obj.optString("ENTRUST_NO"));
                                bean.setVote_motion(obj.optString("VOTE_MOTION"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300805
     * 当日网络投票结果信息查询
     *
     * @param session       token
     * @param exchange_type 市场 1 上海 2深圳
     * @param page          页码
     * @param num           数量
     * @param TAG           tag
     * @param callback      callback
     */
    public void queryTodayVoting(String session, String exchange_type, String page, String num, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300805");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("POSITION_STR", page);
        map2.put("REQUEST_NUM", num);
        map2.put("EXCHANGE_TYPE", exchange_type);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<NetworkVotingEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                NetworkVotingEntity bean = new NetworkVotingEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setMeeting_seq(obj.optString("MEETING_SEQ"));
                                bean.setStock_code(obj.optString("STOCK_CODE"));
                                bean.setVote_motion(obj.optString("ENTRUST_PRICE"));
                                bean.setStatus(obj.optString("ENTRUST_STATUS"));
                                bean.setEntrust_status_name(obj.optString("ENTRUST_STATUS_NAME"));
                                bean.setInit_date(obj.optString("ENTRUST_DATE"));
                                bean.setStock_name(obj.optString("STOCK_NAME"));
                                bean.setBusiness_amount(obj.optString("ENTRUST_AMOUNT"));
                                bean.setPosition_str(obj.optString("POSITION_STR"));
                                bean.setEntrust_no(obj.optString("ENTRUST_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 700070
     * 查询股东资料
     *
     * @param session  session
     * @param TAG      tag
     * @param callback callback
     */
    public void queryStockInfo(String session, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "700070");
        map.put("token", session);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<StockHolderInfoEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StockHolderInfoEntity _bean = new StockHolderInfoEntity();
                                JSONObject obj = data.optJSONObject(i);
                                _bean.setCustomerCode(obj.optString("FUND_ACCOUNT"));    // 资金账号
                                _bean.setAccountType(obj.optString("MARKET"));        //市场
                                _bean.setShareholderSName(obj.optString("SECU_NAME"));//股票名称
                                _bean.setShareholderSCode(obj.optString("SECU_CODE"));//股票代码
                                ses.add(_bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 331261
     * 产品适当性交易匹配查询
     *
     * @param prodta_no    产品TA编号
     * @param prod_code    产品代码
     * @param fund_company 基金公司
     * @param fund_code    基金代码
     * @param session      token
     * @param TAG          tag
     */
    public void queryProductSuitability(String session, String prodta_no, String prod_code, String fund_company, String fund_code, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "331261");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("PRODTA_NO", prodta_no);
        map2.put("PROD_CODE", prod_code);
//        fund_code = "000326";
//        fund_company = "01";
        map2.put("FUND_COMPANY", fund_company);
        map2.put("FUND_CODE", fund_code);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    info.setCode(code);
                    info.setMsg(msg);
                    if ("0".equals(code)) {
                        JSONArray data = jsonObject.optJSONArray("data");
                        if (null != data && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject subJsonObj = data.optJSONObject(i);
                                HashMap<String, String> resultMap = new HashMap<String, String>();
                                resultMap.put("PRODRISK_LEVEL", subJsonObj.optString("PRODRISK_LEVEL"));//产品风险等级描述
                                resultMap.put("CORP_RISK_LEVEL", subJsonObj.optString("CORP_RISK_LEVEL"));//客户风险等级
                                resultMap.put("CORP_RISK_LEVEL_INFO", subJsonObj.optString("CORP_RISK_LEVEL_INFO"));//客户风险等级描述
                                resultMap.put("ELIG_RISK_FLAG", subJsonObj.optString("ELIG_RISK_FLAG"));//风险匹配标志 此接口标志位均为1 匹配 0不匹配
                                resultMap.put("ELIG_RISK_FLAG_INFO", subJsonObj.optString("ELIG_RISK_FLAG_INFO"));//风险匹配标志描述
                                resultMap.put("ELIG_INVESTKIND_FLAG", subJsonObj.optString("ELIG_INVESTKIND_FLAG"));//投资品种标志(1 匹配，0 不匹配)
                                resultMap.put("ELIG_INVESTKIND_FLAG_INFO", subJsonObj.optString("ELIG_INVESTKIND_FLAG_INFO"));//投资品种标志描述
                                resultMap.put("ELIG_TERM_FLAG", subJsonObj.optString("ELIG_TERM_FLAG"));//投资周期匹配标志(1 匹配，0 不匹配)
                                resultMap.put("ELIG_TERM_FLAG_INFO", subJsonObj.optString("ELIG_TERM_FLAG_INFO"));//投资周期匹配标志描述
                                resultMap.put("ELIG_DEFICITRATE_FLAG", subJsonObj.optString("ELIG_DEFICITRATE_FLAG"));//亏损率匹配标志(1 匹配，0 不匹配)
                                resultMap.put("ENABLE_FLAG", subJsonObj.optString("ENABLE_FLAG"));//可操作标志(1 可以委托，0不可委托)
                                resultMap.put("NEED_VIDEO_FLAG", subJsonObj.optString("NEED_VIDEO_FLAG"));//是否需要视频录制(0 否，1是)
                                resultMap.put("URL_ID", subJsonObj.optString("URL_ID"));//跳转对象编号(双录地址)
                                resultMap.put("INSTR_BATCH_NO", subJsonObj.optString("INSTR_BATCH_NO"));//指令批号(适当性校验批次号)
                                info.setData(resultMap);
                            }
                        }
                    }
                } catch (JSONException e) {
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                    info.setTag(TAG);
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 331279
     * 产品适当性记录
     *
     * @param instr_batch_no 记录批次号
     * @param oper_info      周边操作信息
     * @param session        token
     * @param TAG            tag
     */
    public void productSuitabilityRecord(String session, String instr_batch_no, String oper_info, String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "331279");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("INSTR_BATCH_NO", instr_batch_no);
        map2.put("OPER_INFO", oper_info);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    info.setCode(code);
                    info.setMsg(msg);
                } catch (JSONException e) {
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * OTC认购获取判断是否跳转 确认书界面的 值
     */
    /**
     * OTC认购或申购获取判断是否跳转 确认书界面的 值
     *
     * @param requestType       3 申购 4认购
     * @param stockCode
     * @param prodta_no
     * @param session
     * @param SubscriptionMoney
     * @param callback
     */
    public void getAffirm(final String requestType, final String stockCode, final String prodta_no, String session, final String SubscriptionMoney, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", stockCode);
        map2.put("PRODTA_NO", prodta_no);
        map2.put("FLAG", "true");
        map1.put("funcid", "300512");
        map1.put("token", session);
        map1.put("parms", map2);
        net.okHttpForPostString("300512", ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    onError(null, new Exception("返回值为空"), 0);
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_AffirmBean>() {
                }.getType();
                OTC_AffirmBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                List<OTC_AffirmBean.DataBean> data = bean.getData();
                ResultInfo info = new ResultInfo(response);
                info.setCode(code);
                info.setMsg("");
                if (("0").equalsIgnoreCase(code) && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        OTC_AffirmBean.DataBean dataBean = data.get(i);
                        String is_ok = dataBean.getIS_OK();
                        String is_agreement = dataBean.getIS_AGREEMENT();
                        String is_open = dataBean.getIS_OPEN();
                        String is_outofdate = dataBean.getIS_OUTOFDATE();
                        String ofrisk_flag = dataBean.getOFRISK_FLAG();
                        String prodrisk_level_name = dataBean.getPRODRISK_LEVEL_NAME();
                        String corp_risk_level = dataBean.getCORP_RISK_LEVEL();
                        String risk_level_name = dataBean.getRISK_LEVEL_NAME();
                        String prodrisk_level = dataBean.getPRODRISK_LEVEL();
                        AssessConfirmEntity assessConfirmBean = new AssessConfirmEntity();
                        assessConfirmBean.productcode = stockCode;
                        assessConfirmBean.productcompany = prodta_no;
                        assessConfirmBean.productprice = SubscriptionMoney;
                        assessConfirmBean.type = requestType;
                        assessConfirmBean.IS_ABLE = is_ok;
                        assessConfirmBean.IS_AGREEMENT = is_agreement;
                        assessConfirmBean.IS_OPEN = is_open;
                        assessConfirmBean.IS_VALIB_RISK_LEVEL = is_outofdate;
                        assessConfirmBean.OFRISK_FLAG = ofrisk_flag;
                        assessConfirmBean.OFUND_RISKLEVEL_NAME = prodrisk_level_name;
                        assessConfirmBean.RISK_LEVEL = corp_risk_level;
                        assessConfirmBean.RISK_LEVEL_NAME = risk_level_name;
                        assessConfirmBean.RISK_RATING = prodrisk_level;

                            /*intent.putExtra("assessConfirm",assessConfirmBean);
                            intent.putExtra("transaction", "true");
                            intent.setClass(context,AssessConfirmActivity.class);
                            mActivity.startActivityForResult(intent,100);
                            dismiss();*/
                        info.setData(assessConfirmBean);

                    }

                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 认购
     */
    public void getProductMsg(String funcid, String session, String stockCode, String prodta_no, String SubscriptionMoney, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("PROD_CODE", stockCode);
        map2.put("PRODTA_NO", prodta_no);
        map2.put("ENTRUST_BALANCE", SubscriptionMoney);
        map2.put("FLAG", "true");
//        map1.put("funcid","730201");
        map1.put("funcid", funcid);
        map1.put("token", session);
        map1.put("parms", map2);
        net.okHttpForPostString("730201", ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_SubscriptionCommitBean>() {
                }.getType();
                OTC_SubscriptionCommitBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                ResultInfo info = new ResultInfo(response);
                info.setCode(code);
                info.setMsg(msg);
                callback.callResult(info);
            }
        });
    }

    /**
     * ETF认购申购赎回信息查询
     * 300720
     *
     * @param session    token
     * @param stock_code 证券代码
     * @param TAG        tag
     * @param callback   callBack
     */
    public void queryApplyfor(String session, String stock_code, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300720");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("STOCK_CODE", stock_code);
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setTag(TAG);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setExchange_type(obj.optString("EXCHANGE_TYPE"));
                                bean.setStock_code(obj.optString("STOCK_CODE"));
                                bean.setStock_name(obj.optString("STOCK_NAME"));
                                bean.setStock_account(obj.optString("STOCK_ACCOUNT"));
                                bean.setEnable_balance(obj.optString("ENABLE_BALANCE"));
                                bean.setStock_max(obj.optString("STOCK_MAX"));
                                bean.setCash_max(obj.optString("CASH_MAX"));
                                bean.setAllot_max(obj.optString("ALLOT_MAX"));
                                bean.setRedeem_max(obj.optString("REDEEM_MAX"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300734   300736    共用一个接口
     * ETF申购	确认     ETF赎回
     *
     * @param session        session
     * @param exchange_type  交易类别
     * @param stock_code     证券代码
     * @param entrust_amount 委托数量
     * @param TAG            tag
     * @param callback       callback
     */
    public void applyforDetermine(String funcid, String session, String exchange_type, String stock_code,
                                  String entrust_amount, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", funcid);
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("EXCHANGE_TYPE", exchange_type);
        map2.put("STOCK_CODE", stock_code);
        map2.put("ENTRUST_AMOUNT", entrust_amount);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setInit_date(obj.optString("INIT_DATE"));
                                bean.setEntrust_no(obj.optString("ENTRUST_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }

                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300738
     * ETF申赎委托查询  查询可撤委托   和全部
     *
     * @param session      session
     * @param query_kind   查询控制值   0-查询全部委托；1-查询可撤委托;   0  今日查询传入0
     * @param position_str 定位串   可不传值
     * @param request_num  请求行数
     * @param TAG          tag
     * @param callback     callback
     */
    public void queryEntrust(String session, String query_kind, String position_str,
                             String request_num, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300738");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("QUERY_KIND", query_kind);
        map2.put("POSITION_STR", position_str);
        map2.put("REQUEST_NUM", request_num);
        map1.put("parms", map2);

        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.optJSONObject(i);
                                String init_date = Helper.formateDate1(obj.optString("INIT_DATE"));
                                bean.setInit_date(init_date);
                                String currn_time = Helper.getMyDateHMS(obj.optString("CURR_TIME"));
                                bean.setReport_time(currn_time);
                                bean.setStock_code(obj.optString("STOCK_CODE"));
                                bean.setStock_name(obj.optString("STOCK_NAME"));
                                bean.setStock_account(obj.optString("STOCK_ACCOUNT"));
                                bean.setPrev_balance(obj.optString("PREV_BALANCE"));
                                bean.setEntrust_bs(obj.optString("ENTRUST_BS"));
                                bean.setEntrust_no(obj.optString("ENTRUST_NO"));
                                bean.setEnable_balance(obj.optString("ENTRUST_BALANCE"));
                                bean.setEntrust_amount(obj.optString("ENTRUST_AMOUNT"));
                                bean.setEntrust_prop(obj.optString("ENTRUST_PROP"));
                                bean.setEntrust_status(obj.optString("ENTRUST_STATUS"));
                                bean.setPosition_str(obj.optString("POSITION_STR"));
                                bean.setEntrust_status_name(obj.optString("ENTRUST_STATUS_NAME"));
                                bean.setExchange_type_name(obj.optString("EXCHANGE_TYPE_NAME"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);


            }
        });
    }

    /**
     * 300740
     * ETF申赎撤单
     *
     * @param session       session
     * @param exchange_type 交易类别
     * @param entrust_no    委托编号
     * @param stock_code    证券代码
     * @param TAG           tag
     * @param callback      callback
     */
    public void revokeOrder(String session, String exchange_type, String entrust_no,
                            String stock_code, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300740");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("EXCHANGE_TYPE", exchange_type);
        map2.put("ENTRUST_NO", entrust_no);
        map2.put("STOCK_CODE", stock_code);
        map1.put("parms", map2);

        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setInit_date(obj.optString("INIT_DATE"));
                                bean.setEntrust_no(obj.optString("ENTRUST_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }

                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300748
     * ETF申赎历史查询
     *
     * @param session      session
     * @param begin_date   起始日期 N
     * @param end_date     到期日期 N
     * @param his_type     历史类型 0:自定义  1：一周内   2：一个月内 3：三个月内   Y
     * @param position_str 定位串 N
     * @param request_num  请求行数 Y
     * @param TAG          tag
     * @param callback     callback
     */
    public void queryHistory(String session, String begin_date, String end_date, String his_type,
                             String position_str, String request_num, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300748");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("BEGIN_DATE", begin_date);
        map2.put("END_DATE", end_date);
        map2.put("HIS_TYPE", his_type);
        map2.put("POSITION_STR", position_str);
        map2.put("REQUEST_NUM", request_num);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.optJSONObject(i);
                                String init_date = Helper.formateDate1(obj.optString("INIT_DATE"));
                                bean.setInit_date(init_date);
                                bean.setEntrust_no(obj.optString("ENTRUST_NO"));
                                bean.setStock_name(obj.optString("STOCK_NAME"));
                                bean.setStock_code(obj.optString("STOCK_CODE"));
                                bean.setEntrust_amount(obj.optString("ENTRUST_AMOUNT"));
                                bean.setEntrust_status(obj.optString("ENTRUST_STATUS"));
                                String report = Helper.getMyDateHMS(obj.optString("REPORT_TIME"));
                                bean.setReport_time(report);
                                bean.setEntrust_status_name(obj.optString("ENTRUST_STATUS_NAME"));
                                bean.setPrev_balance(obj.optString("PREV_BALANCE"));
                                bean.setEntrust_bs(obj.optString("ENTRUST_BS"));
                                bean.setEntrust_price(obj.optString("ENTRUST_PRICE"));
                                bean.setStock_account(obj.optString("STOCK_ACCOUNT"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }

                    } catch (JSONException e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300746
     * ETF申赎成交查询
     *
     * @param session      session
     * @param position_str 定位串   N
     * @param request_num  请求行数  Y
     * @param TAG          tag
     * @param callback     callback
     */
    public void queryDeal(String session, final String position_str, String request_num, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300746");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("POSITION_STR", position_str);
        map2.put("REQUEST_NUM", request_num);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setStock_code(obj.optString("STOCK_CODE"));//证券代码
                                bean.setStock_name(obj.optString("STOCK_NAME"));//证券名称
                                bean.setEntrust_bs(obj.optString("ENTRUST_BS"));//买卖方向
                                bean.setReal_status(obj.optString("REAL_STATUS"));//状态
                                bean.setReal_status_name(obj.optString("REAL_STATUS_NAME"));//状态名称
                                bean.setBusiness_amount(obj.optString("BUSINESS_AMOUNT"));//成交数量
                                bean.setBusiness_balance(obj.optString("BUSINESS_BALANCE"));//成交金额
                                bean.setInit_date(Helper.formateDate1(obj.optString("INIT_DATE")));//交易时间
                                bean.setCurr_time(obj.optString("CURR_TIME"));//委托时间
                                bean.setEntrust_no(obj.optString("ENTRUST_NO"));//委托编号
                                bean.setStock_account(obj.optString("STOCK_ACCOUNT"));//证券编号<股东代码>
                                bean.setTrade_plat(obj.optString("TRADE_PLAT"));//交易平台<交易所名称>
                                bean.setPosition_str(obj.optString("POSITION_STR_LONG"));//定位穿
                                bean.setCbp_business_id(obj.optString("CBP_BUSINESS_ID"));//定位穿
                                bean.setShowRule(false);
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (Exception e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 300744
     * ETF成分股
     *
     * @param session    token 手机识别码
     * @param stock_code 股票代码
     * @param page       定位串
     * @param num        请求行数
     * @param TAG
     * @param callback
     */
    public void constituentStock(String session, String stock_code, String page, String num, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300744");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("STOCK_CODE", stock_code);
        map2.put("POSITION_STR", page);
        map2.put("REQUEST_NUM", num);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setStock_code(obj.optString("STOCK_CODE"));//证券代码
                                bean.setComponent_code(obj.optString("COMPONENT_CODE"));//成份股代码
                                bean.setStock_name(obj.optString("COMPONENT_NAME"));//成份股名称
                                bean.setEnable_balance(obj.optString("REPLACE_BALANCE"));//替代金额
                                bean.setEntrust_amount(obj.optString("COMPONENT_AMOUNT"));//成份股数量<单位数量>
                                bean.setStock_max(obj.optString("REPLACE_RATIO"));//溢价比率
                                bean.setCash_max(obj.optString("REPLACE_FLAG"));//替代标志
                                bean.setAllot_max(obj.optString("REPLACE_FLAG_NAME"));//替代标志名称
                                bean.setPosition_str(obj.optString("POSITION_STR"));//定位穿
                                bean.setShowRule(false);
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (Exception e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 300742
     * ETF列表查询
     *
     * @param session      token
     * @param position_str 定位串
     * @param request_num  num
     * @param TAG          tag
     * @param callback     callback
     */
    public void constituentStockList(String session, String position_str, String request_num, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(), "mSession", "");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300742");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("POSITION_STR", position_str);
        map2.put("REQUEST_NUM", request_num);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setStock_code(obj.optString("STOCK_CODE_0"));
                                bean.setComponent_code(obj.optString("STOCK_CODE_3"));
                                bean.setStock_name(obj.optString("STOCK_NAME"));
                                bean.setPosition_str(obj.optString("POSITION_STR"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (Exception e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                    callback.callResult(info);
                }
            }
        });

    }

    /**
     * 1.7.1 要闻
     *
     * @param limit 条数
     * @param page  页码
     * @param level 级别
     */
    public void queryImportant(String limit, String page, String level,String TAG, InterfaceCallback callback) {
        Map map1 = new HashMap<>();
        map1.put("limit", limit);
        map1.put("page", page);
        map1.put("level", level);
        parseInformation(ConstantUtil.getURL_IMPORTANT(), map1, TAG, callback, "");
    }

    /**
     * 1.7.2 直播
     *
     * @param daysno 返回历史几天的数据
     * @param option 1=包含历史数据 2=不包含历史数据
     * @param limit  页数
     * @param page   页码
     */
    public void queryStreaming(String daysno, String option, String limit, String page,String TAG,InterfaceCallback callback) {
        Map map1 = new HashMap<>();
        map1.put("daysno", daysno);
        map1.put("option", option);
        map1.put("limit", limit);
        map1.put("page", page);
        parseInformation(ConstantUtil.getURL_STREAMING(), map1, TAG, callback, "queryStreaming");
    }

    /**
     * 1.7.3 查询栏目信息列表
     *
     * @param classno 栏目id
     * @param limit
     * @param page
     */
    public void queryHkstocks(String classno, String limit, String page,String TAG,InterfaceCallback callback) {
        Map map1 = new HashMap<>();
        map1.put("classno", classno);
        map1.put("limit", limit);
        map1.put("page", page);
        parseInformation(ConstantUtil.getURL_HKSTOCKS(), map1, TAG, callback, "");
    }

    /**
     * 1.7.4 信息详情
     *
     * @param newsno 信息id
     */
    public void queryDetail(String newsno, String TAG, InterfaceCallback callback) {
        Map map1 = new HashMap<>();
        map1.put("newsno", newsno);
        parseInformation(ConstantUtil.getURL_DETAIL(), map1, TAG, callback, "queryDetail");
    }

    /**
     * 1.7.5 栏目list
     */
    public void queryClasslist(String TAG, InterfaceCallback callback) {
        parseInformation(ConstantUtil.getURL_CLASSLIST(), new HashMap(), TAG, callback, "");
    }

    /**
     * 1.7.6 股票相关新闻
     */
    public void queryStockNews(String stockNo, String limit, String page,String TAG,InterfaceCallback callback){
        Map map1 = new HashMap<>();
        map1.put("stockNo", stockNo);
        map1.put("limit", limit);
        map1.put("page", page);
        parseInformation(ConstantUtil.getURL_STOCKNEWS(),map1, TAG, callback,"");
    }

    /**
     * 解析资讯实体
     *
     * @param TAG
     * @param callback
     * @return
     */
    private void parseInformation(String url, Map map, final String TAG, final InterfaceCallback callback, final String type) {
        net.okHttpForGet(TAG, url, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("message");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("200".equals(code))
                            info.setData(parseInformationArray(jsonObject.optJSONArray("data"), type));
                    } catch (Exception e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                    callback.callResult(info);
                }
            }
        });
    }

    /**
     * 解析json数据
     *
     * @param array json数组
     * @return List<NetworkVotingEntity>
     * @throws JSONException
     */
    private List<InformationEntity> parseInformationArray(JSONArray array, String type) throws JSONException {
        List<InformationEntity> ses = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            if (type.equals("queryStreaming")) {
                JSONArray a1 = array.getJSONArray(i);
                for (int j = 0; j < a1.length(); j++)
                    parseJSONObject(ses, a1.optJSONObject(j), type);
            } else
                parseJSONObject(ses, array.optJSONObject(i), type);
        }
        return ses;
    }

    public void parseJSONObject(List<InformationEntity> ses, JSONObject obj, String type) throws JSONException {
        InformationEntity bean = new InformationEntity();
        bean.setNewsno(obj.optString("newsno"));
        bean.setTitle(obj.optString("title"));
        bean.setDigest(obj.optString("digest"));
        String time = obj.optString("time");
        if(!TextUtils.isEmpty(time)&&time.length()==19)
            time = time.substring(5,time.length()-3);
        bean.setTime(time);
        bean.setImage_url(obj.optString("image"));
        bean.setDate(obj.optString("date"));
        bean.setContent(obj.optString("content"));
        bean.setSource(obj.optString("source"));
        bean.setStatement(obj.optString("statement"));
        bean.setLabelno(obj.optString("labelno"));
        bean.setLabelname(obj.optString("labelname"));
        bean.setClassno(obj.optString("classno"));
        bean.setClassname(obj.optString("classname"));
        bean.setSecuCode(obj.optString("secuCode"));
        bean.setSecuAbbr(obj.optString("secuAbbr"));
        JSONArray label = obj.optJSONArray("label");
        if (label != null && label.length() > 0)
            bean.setList(parseInformationArray(label, type));
        ses.add(bean);
    }

    //基金定投开始

    /**
     * 334008
     * 定投新增
     *
     * @param FUND_COMPANY 基金公司  可空，默认为基金代码对应基金公司
     * @param FUND_CODE    基金代码
     * @param RATION_TYPE  定投期满类型 默认‘0’， 0设定结束日期   1累计金额上限   2累计成功次数
     * @param BALANCE      发生金额
     * @param START_DATE   开始日期
     * @param END_DATE     到期日期
     * @param EN_FUND_DATE 扣款允许日
     */
    public void addFixFund(final String FUND_COMPANY, final String FUND_CODE, String RATION_TYPE, final String BALANCE,
                           final String START_DATE, final String END_DATE, final String EN_FUND_DATE,
                           final String DO_OPEN, final String DO_CONTRACT, final String DO_PRE_CONDITION,
                           final String TAG, final InterfaceCallback callback) {
        Map map1 = new HashMap<>();
        map1.put("funcid", "334008");
        map1.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("FUND_COMPANY", FUND_COMPANY);
        map2.put("FUND_CODE", FUND_CODE);
        map2.put("RATION_TYPE", RATION_TYPE);
        map2.put("BALANCE", BALANCE);
        map2.put("START_DATE", START_DATE);
        map2.put("END_DATE", END_DATE);
        map2.put("EN_FUND_DATE", EN_FUND_DATE);
        map2.put("DO_OPEN", DO_OPEN);   //是否需要开户
        map2.put("DO_CONTRACT", DO_CONTRACT);   //是否需要签署协议 如果DO_PRE_CONDITION传1的话以上两个字段不传
        map2.put("DO_PRE_CONDITION", DO_PRE_CONDITION);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            info.setMsg("基金定投登记成功");
                            JSONArray jsonArr = jsonObject.optJSONArray("data");
                            AssessConfirmEntity assessConfirmBean = new AssessConfirmEntity();
                            if (null!=jsonArr && jsonArr.length()>0) {
                                jsonObject = jsonArr.getJSONObject(0);
                                assessConfirmBean.productcode = FUND_CODE;
                                assessConfirmBean.productcompany = FUND_COMPANY;
                                assessConfirmBean.productprice = BALANCE;//复用
                                assessConfirmBean.type = "0";//基金定投
                                assessConfirmBean.IS_ABLE = jsonObject.optString("IS_ABLE");
                                assessConfirmBean.IS_AGREEMENT = jsonObject.optString("IS_AGREEMENT");
                                assessConfirmBean.IS_OPEN = jsonObject.optString("IS_OPEN");
                                assessConfirmBean.IS_VALIB_RISK_LEVEL = jsonObject.optString("IS_VALIB_RISK_LEVEL");
                                assessConfirmBean.OFRISK_FLAG = jsonObject.optString("OFRISK_FLAG");
                                assessConfirmBean.OFUND_RISKLEVEL_NAME = jsonObject.optString("OFUND_RISKLEVEL_NAME");
                                assessConfirmBean.RISK_RATING = START_DATE;
                                assessConfirmBean.RISK_LEVEL = END_DATE;
                                assessConfirmBean.RISK_LEVEL_NAME = EN_FUND_DATE;
                            }
                            info.setData(assessConfirmBean);
                        }
                    } catch (Exception e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                    callback.callResult(info);
                }
            }
        });
    }

    /**
     * 334103
     * 基金定投列表
     *
     * @param ALLOTNO 申请编号 传空为查询全部
     */
    public void getFixFundList(String ALLOTNO, final String TAG, final InterfaceCallback callback) {
        Map map1 = new HashMap<>();
        map1.put("funcid", "734103");
        map1.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("ALLOTNO", ALLOTNO);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<FixFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.optJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                FixFundEntity bean = new FixFundEntity();
                                JSONObject obj = data.optJSONObject(i);
                                bean.setFUND_CODE(obj.optString("FUND_CODE"));
                                bean.setFUND_NAME(obj.optString("FUND_NAME"));
                                bean.setBALANCE(obj.optString("BALANCE"));
                                bean.setSEND_BALANCE(obj.optString("SEND_BALANCE"));
                                bean.setEN_FUND_DATE(obj.optString("EN_FUND_DATE"));
                                String END_DATE = obj.optString("END_DATE");
                                END_DATE = Helper.formateDate1(END_DATE);
                                bean.setEND_DATE(END_DATE);
                                bean.setDEAL_DATE(obj.optString("DEAL_DATE"));
                                bean.setALLOTNO(obj.optString("ALLOTNO"));
                                bean.setDEAL_FLAG(obj.optString("DEAL_FLAG"));
                                bean.setDEAL_FLAG_NAME(obj.optString("DEAL_FLAG_NAME"));
                                String start_date = obj.optString("START_DATE");
                                start_date = Helper.formateDate1(start_date);
                                bean.setSTART_DATE(start_date);
                                bean.setCURR_RATION_TIMES(obj.optString("CURR_RATION_TIMES"));
                                bean.setPOSITION_STR(obj.optString("POSITION_STR"));
                                bean.setFUND_VAL(obj.optString("FUND_VAL"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (Exception e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                    callback.callResult(info);
                }
            }
        });
    }

    /**
     * * 334009
     * 基金定投修改
     *
     * @param FUND_COMPANY 基金公司  N	可空，默认为基金代码对应基金公司
     * @param FUND_CODE    基金代码
     * @param BALANCE      发生金额
     * @param START_DATE   开始日期
     * @param END_DATE     结束日期
     * @param EN_FUND_DATE 扣款日
     * @param ALLOTNO      申请编码
     * @param TAG
     * @param callback
     */
    public void modifyFixFund(String FUND_COMPANY, String FUND_CODE, String BALANCE, String START_DATE,
                              String END_DATE, String EN_FUND_DATE, String ALLOTNO, final String TAG, final InterfaceCallback callback) {
        Map map1 = new HashMap<>();
        map1.put("funcid", "334009");
        map1.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("FUND_COMPANY", FUND_COMPANY);
        map2.put("FUND_CODE", FUND_CODE);
        map2.put("BALANCE", BALANCE);
        map2.put("START_DATE", START_DATE);
        map2.put("END_DATE", END_DATE);
        map2.put("EN_FUND_DATE", EN_FUND_DATE);
        map2.put("ALLOTNO", ALLOTNO);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);

                        if ("0".equals(code)) {
                            info.setMsg("基金定投登记成功");
                        }
                    } catch (Exception e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                    callback.callResult(info);
                }
            }
        });
    }

    /**
     * * 334010
     * 基金定投修改
     *
     * @param FUND_CODE 基金代码
     * @param ALLOTNO   申请编码
     * @param TAG
     * @param callback
     */
    public void revokeFixFund(String FUND_CODE, String ALLOTNO, final String TAG, final InterfaceCallback callback) {
        Map map1 = new HashMap<>();
        map1.put("funcid", "334010");
        map1.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("FUND_CODE", FUND_CODE);
        map2.put("ALLOTNO", ALLOTNO);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);

                        if ("0".equals(code)) {
                            info.setMsg("基金定投撤销成功");
                        }
                    } catch (Exception e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                    callback.callResult(info);
                }
            }
        });
    }


    public void getFundData(String fundcode, String fundcompany, final String TAG, final InterfaceCallback callback) {
        HashMap map300431 = new HashMap();
        map300431.put("funcid", "300431");
        map300431.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        HashMap map300431_1 = new HashMap();
        map300431_1.put("SEC_ID", "tpyzq");
        map300431_1.put("FLAG", "true");
        map300431_1.put("FUND_CODE", fundcode);
        map300431_1.put("FUND_COMPANY", fundcompany);
        map300431_1.put("OPER_TYPE", "0");
        map300431.put("parms", map300431_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300431, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                try {
                    if (TextUtils.isEmpty(response)) {
                        info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                        info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                        info.setTag(TAG);
                    } else {
                        JSONObject object = new JSONObject(response);
                        String code = object.getString("code");
                        String msg = object.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if (code.equals("0")) {
                            FundDataEntity fundDataBean = new Gson().fromJson(response, FundDataEntity.class);
                            info.setData(fundDataBean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                } finally {
                    callback.callResult(info);
                }
            }
        });
    }


    /**
     * 手机注册 图片验证码
     *
     * @param TAG
     * @param callback
     */
    public void getImageVerification(final String TAG, final InterfaceCallback callback) {
        HashMap map = new HashMap();
        map.put("equipment", DeviceUtil.getDeviceId(CustomApplication.getContext()));
        NetWorkUtil.getInstence().okHttpForGet("", ConstantUtil.getURL_HANDSE_PICTURE(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String type = jsonObject.getString("type");
                    info.setCode(code);
                    info.setTag(TAG);
                    if ("0".equals(code)) {
                        info.setData(jsonObject.getString("message"));

                    } else {
                        info.setMsg(type);
                    }
                } catch (Exception e) {
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                    info.setTag(TAG);
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 手机注册 短信验证码
     *
     * @param TAG
     * @param number   手机号
     * @param image    图片验证码
     * @param callback
     */
    public void getVerificationCode(final String TAG, String number, final String image, final InterfaceCallback callback) {
        HashMap map = new HashMap();
        map.put("equipment", DeviceUtil.getDeviceId(CustomApplication.getContext()));
        map.put("phone", number);
        map.put("auth", image);

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HANDSE_SMS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    info.setCode(code);
                    info.setTag(TAG);
                    if ("0".equals(code)) {
//                        CentreToast.showText(ShouJiZhuCe.this,"发送短信成功");
                    } else {
                        info.setMsg(jsonObject.opt("message").toString());
                    }
                } catch (JSONException e) {
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                    info.setTag(TAG);
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 手机注册语音验证码
     *
     * @param TAG
     * @param number   手机号
     * @param image    图片验证码
     * @param callback
     */
    public void getVoiceVerificationCode(final String TAG, String number, final String image, final InterfaceCallback callback) {
        HashMap map = new HashMap();
        map.put("equipment", DeviceUtil.getDeviceId(CustomApplication.getContext()));
        map.put("phone", number);
        map.put("auth", image);

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HANDSE_SPEECH(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    info.setTag(TAG);
                    info.setCode(code);
                    if ("0".equals(code)) {
                    } else {
                        info.setMsg(jsonObject.getString("message"));
                    }

                } catch (JSONException e) {
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                    info.setTag(TAG);
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 手机注册
     *
     * @param TAG
     * @param number   手机号
     * @param captcha  短信验证码
     * @param callback
     */
    public void InscriptionRegistry(final String TAG, String number, String captcha, final InterfaceCallback callback) {
        HashMap map = new HashMap();
        map.put("equipment", DeviceUtil.getDeviceId(CustomApplication.getContext()));
        map.put("phone", number);
        map.put("auth", captcha);
        map.put("phone_type", "1");  //手机类型 1.安卓 2.苹果 3.其他
        map.put("token", SpUtils.getString(CustomApplication.getContext(), "RegId", ""));
        map.put("user_account", number);
        map.put("user_type", "1");//账户类型，1：手机，2：qq，3：微信

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HANDSE_REGISTER(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                response = response.replace("Served at: /Bigdata", "");
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("message");
                    info.setCode(code);
                    info.setTag(TAG);
                    info.setMsg(msg);
                } catch (Exception e) {
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                    info.setTag(TAG);
                }
                callback.callResult(info);
            }
        });

    }

    /**
     * HQING014
     * 新股查询接口
     */
    public void queryNewStock(final String TAG,final InterfaceCallback callback){
        HashMap map=new HashMap();
        map.put("funcid", "100213");
        map.put("token", "");
        map.put("parms", new HashMap());
        net.okHttpForPostString(TAG, ConstantUtil.getURL_HQ_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("msg");
                    info.setCode(code);
                    info.setTag(TAG);
                    info.setMsg(msg);
                    if("0".equals(code)){
                        JSONArray data = object.optJSONArray("data");
                        NewStockEnitiy bean = new NewStockEnitiy();
                        if (data != null && data.length() > 0) {
                            List<NewStockEnitiy.DataBeanToday> dataBeanTodays = new ArrayList();
                            int publishNum = 0;
                            for (int i = 0;i<data.length();i++) {
                                JSONObject item = data.optJSONObject(i);
                                NewStockEnitiy.DataBeanToday dataBeanToday = new NewStockEnitiy.DataBeanToday();
                                dataBeanToday.setSECUCODE(item.optString("SECUCODE"));
                                dataBeanToday.setONLINESTARTDATE(item.optString("ONLINESTARTDATE"));
                                dataBeanToday.setISSUENAMEABBR_ONLINE(item.optString("ISSUENAMEABBR_ONLINE"));
                                dataBeanToday.setAPPLYMAXONLINE(item.optString("APPLYMAXONLINE"));
                                String isToday =item.optString("ISTODAY");
                                if (null != isToday) {//W N
                                    dataBeanToday.setISTODAY(isToday);
                                    if ("N".equals(isToday))
                                        publishNum++;
                                }
                                dataBeanToday.setDILUTEDPERATIO(item.optString("DILUTEDPERATIO"));
                                dataBeanToday.setAPPLYCODEONLINE(item.optString("APPLYCODEONLINE"));
                                dataBeanToday.setPREPAREDLISTEXCHANGE(item.optString("PREPAREDLISTEXCHANGE"));
                                dataBeanToday.setISSUEPRICE(item.optString("ISSUEPRICE"));
                                dataBeanToday.setWEIGHTEDPERATIO(item.optString("WEIGHTEDPERATIO"));
                                dataBeanToday.setAPPLYMAXONLINEMONEY(item.optString("APPLYMAXONLINEMONEY"));
                                dataBeanTodays.add(dataBeanToday);
                            }
                            bean.setNewStockSize(publishNum);
                            bean.setData(dataBeanTodays);
                        }
                        info.setData(bean);
                    }
                } catch (Exception e) {
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                    info.setTag(TAG);
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * 修改资金密码
     * @param TAG
     * @param currentpassword 旧密码
     * @param newpassword     新密码
     * @param unikey          UNIKEY插件ID
     * @param callback
     */
    public void ModifyFundsPassword(final String TAG, String currentpassword, String newpassword,String unikey, final InterfaceCallback callback){
        HashMap map=new HashMap();
        map.put("funcid","700061");
        map.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        HashMap hashMap=new HashMap();
        hashMap.put("SEC_ID", "tpyzq");
        hashMap.put("FLAG", "true");
        hashMap.put("NEW_PWD", newpassword);
        hashMap.put("OLD_PWD", currentpassword);


        hashMap.put("PWD_TYPE", UserUtil.Keyboard); //密码类型 0：明文 1：密文
        hashMap.put("MOBILE", DeviceUtil.getDeviceId(CustomApplication.getContext()));                       //绑定UNIKEYID的手机号
        hashMap.put("UNIKEYID", unikey);                       //UNIKEY插件ID
        hashMap.put("APP_TYPE", "1");                       //手机类型 0：ios        1：android

        map.put("parms",hashMap);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)){
                    return;
                }
                try {
                    JSONObject jsonObject =new JSONObject(response);
                    String code=jsonObject.getString("code");
                    String msg= jsonObject.getString("msg");
                    JSONArray data=jsonObject.getJSONArray("data");
                    info.setCode(code);
                    info.setTag(TAG);
                    info.setMsg(msg);
                    if("0".equals(code)){
//                        {"code":"0","msg":"331101成功","data":[{"ERROR_NO":"0","ERROR_INFO":""}]}
                        info.setData(data);
                    }
                 } catch (JSONException e) {
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                    info.setTag(TAG);
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 绑定手机号
     * @param TAG
     * @param number  手机号
     * @param captcha 短信验证码
     * @param callback
     */
    public void BindingMobil(final String TAG , String number, String captcha, final InterfaceCallback callback){
        HashMap map = new HashMap();
        map.put("phone", number);
        map.put("user_account", UserUtil.userId);
        map.put("user_type", KeyEncryptionUtils.getInstance().Typescno());
        map.put("equipment", DeviceUtil.getDeviceId(CustomApplication.getContext()));
        map.put("auth", captcha);

        NetWorkUtil.getInstence().okHttpForGet("", ConstantUtil.getURL_HANDSE_BINDING(), map, new StringCallback()
        {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("message");
                    info.setCode(code);
                    info.setTag(TAG);
                    info.setMsg(msg);
                } catch (Exception e) {
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                    info.setTag(TAG);
                }
                callback.callResult(info);
            }
        });

    }


    /**
     * 轮播图
     * @param TAG tag
     * @param position    广告位置（数据字典id）
     * @param pageIndex  pageIndex 页码
     * @param session    session
     * @param callback   callback
     */
    public void requestCarouselImg(final String TAG, String position, String pageIndex, String session, final InterfaceCallback callback){
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("position", position);
        map2.put("pageIndex", pageIndex);
        map.put("PARAMS", map2);
        map.put("FUNCTIONCODE" , "HQLNG101");
        map.put("TOKEN", session);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_HQ_WA(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);

            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                if (TextUtils.isEmpty(response)) {
                    info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
//                        List<Map<String,String>> list = new ArrayList<>();
                        List<List<Map<String,String>>> lists = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("message");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)){
                            JSONArray jsonArray = jsonObject.optJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                List<Map<String,String>> list = new ArrayList<>();
                                if ("M1N2".equals(jsonArray.optJSONObject(i).optString("marshalling"))){
                                    JSONArray array = jsonArray.optJSONObject(i).optJSONArray("advert_data");
                                    for (int j = 0; j < array.length(); j++) {
                                        Map<String,String> map = new HashMap<>();
                                        String jump_type = array.optJSONObject(j).optString("jump_type");
                                        String jump_url = array.optJSONObject(j).optString("jump_url");
                                        String jump_position = array.optJSONObject(j).optString("jump_position");
                                        String show_url = array.optJSONObject(j).optString("show_url");
                                        map.put("show_url",show_url);
                                        map.put("jump_position",jump_position);
                                        map.put("jump_url",jump_url);
                                        map.put("jump_type",jump_type);
                                        list.add(map);
                                    }
                                    lists.add(0,list);
                                }else if ("M1N1".equals(jsonArray.optJSONObject(i).optString("marshalling"))){
                                    JSONArray array = jsonArray.optJSONObject(i).optJSONArray("advert_data");
                                    for (int j = 0; j < array.length(); j++) {
                                        Map<String,String> map = new HashMap<>();
                                        String jump_type = array.optJSONObject(j).optString("jump_type");
                                        String jump_url = array.optJSONObject(j).optString("jump_url");
                                        String jump_position = array.optJSONObject(j).optString("jump_position");
                                        String show_url = array.optJSONObject(j).optString("show_url");
                                        map.put("show_url",show_url);
                                        map.put("jump_position",jump_position);
                                        map.put("jump_url",jump_url);
                                        map.put("jump_type",jump_type);
                                        list.add(map);
                                    }
                                    lists.add(list);
                                }
                            }
                            info.setData(lists);
                        }

                    } catch (Exception e) {
                        info.setCode(ConstantUtil.JSON_ERROR_CODE);
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                    callback.callResult(info);
                }
            }


        });
    }

    /**
     * 请求站点
     * @param url 请求地址 启动页两路都要进行请求，ConstantUtil.IP+ConstantUtil.GET_SITES
     * @param TAG tag值
     * @param callback 返回类
     */
    public void getSites(String url, final String TAG, final InterfaceCallback callback) {
        Map<String, String> map001 = new HashMap<>();
        NetWorkUtil.getInstence().okHttpForGet("", url, map001, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode(ConstantUtil.NETWORK_ERROR_CODE);
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo(response);
                JSONObject jsonObj;
                String code, type;
                try {
                    if (TextUtils.isEmpty(response)) {
                        info.setCode(ConstantUtil.SERVICE_NO_DATA_CODE);
                        info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                        info.setTag(TAG);
                    } else {
                        jsonObj = new JSONObject(response);
                        code = jsonObj.optString("code");
                        type = jsonObj.optString("type");
                        info.setCode(code);
                        info.setMsg(type);
                        info.setTag(TAG);
                        if ("0".equalsIgnoreCase(code)) {
                            jsonObj = jsonObj.optJSONObject("message");
                            SpUtils.putString(CustomApplication.getContext(),"site_json",response);
                            ConstantUtil.SITE_JSON = response;
                            ConstantUtil.registerServerUrl = jsonObj.optString("register-httpServer");
                            ConstantUtil.registerNoteUrl = jsonObj.optString("register-note");
                            HashMap resultMap = parseSites(jsonObj);
                            info.setData(resultMap);
                        }
                    }
                } catch (Exception e) {
                    info.setCode(ConstantUtil.JSON_ERROR_CODE);
                    info.setMsg(ConstantUtil.JSON_ERROR);
                    info.setTag(TAG);
                }
                callback.callResult(info);

            }
        });
    }

    /**
     * 站点接口解析
     * @param jsonObj message的jsonobject
     * @return
     * @throws JSONException
     */
    public static HashMap parseSites(JSONObject jsonObj) throws JSONException{
        HashMap resultMap = new HashMap();
        String code, type;
        JSONObject subJsonObj;
        JSONArray jsonArr, subJsonArr;
        String[] strs;
        try {
            //map第二个值为注册URL
            resultMap.put("register",jsonObj.optString("register"));

            //map第三个值为交易站点集合 key为trade value为数组 数组内为字符串 格式 站点名称+^^^+站点地址
            jsonArr = jsonObj.optJSONArray("trade");
            strs = new String[jsonArr.length()];
            for (int i=0;i<jsonArr.length();i++){
                subJsonObj = jsonArr.optJSONObject(i);
                strs[i] = subJsonObj.optString("name")+"|"+subJsonObj.optString("url");
            }
            resultMap.put("trade",strs);

            //map第四个值为行情站点集合 key为hq value为数组 数组内为字符串 格式 站点名称+^^^+站点地址
            jsonArr = jsonObj.optJSONArray("hq");
            strs = new String[jsonArr.length()];
            for (int i=0;i<jsonArr.length();i++){
                subJsonObj = jsonArr.optJSONObject(i);
                strs[i] = subJsonObj.optString("name")+"|"+subJsonObj.optString("url");
            }
            resultMap.put("hq",strs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return resultMap;
        }

    }

}
