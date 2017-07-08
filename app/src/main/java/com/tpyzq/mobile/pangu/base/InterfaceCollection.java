package com.tpyzq.mobile.pangu.base;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.EtfDataEntity;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.OTC_AffirmBean;
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionCommitBean;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StockHolderInfoEntity;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private InterfaceCollection (){
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
     * @param session  token
     * @param TAG      tag
     * @param callback callback
     */
    public void Fundchoice(String session, final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        HashMap map = new HashMap();
        map.put("funcid", "300200");
        map.put("token", session);
        HashMap hashMap = new HashMap();
        hashMap.put("FLAG", "true");
        hashMap.put("SEC_ID", "tpyzq");
        map.put("parms", hashMap);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        if ("0".equals(code)) {
                            List<StructuredFundEntity> list = new ArrayList<StructuredFundEntity>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                bean.setStocken_code(data.getJSONObject(i).getString("SECU_CODE"));
                                bean.setMarket(data.getJSONObject(i).getString("MARKET"));
                                bean.setStoken_name(data.getJSONObject(i).getString("SECU_NAME"));
                                list.add(bean);
                            }

                            info.setData(list);
                        }

                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param session token
     * @param stock_code 证券代码
     * @param TAG tag
     * @param callback callback
     */
    public void queryStructuredFund(String session, String stock_code, final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300701");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map2.put("STOCK_CODE",stock_code);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setStoken_name(obj.getString("STOCK_NAME"));
                                bean.setMerge_amount(obj.getString("MERGE_AMOUNT"));
                                bean.setSplit_amount(obj.getString("SPLIT_AMOUNT"));
                                bean.setExchange_type(obj.getString("EXCHANGE_TYPE"));
                                bean.setFund_status(obj.getString("FUND_STATUS"));
                                bean.setStock_account(obj.getString("STOCK_ACCOUNT"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param exchange_type 交易类别
     * @param stock_account  当前市场的主证券账户
     * @param stock_code 证券代码
     * @param entrust_amount 委托数量
     * @param session token
     * @param TAG tag
     * @param callback callback
     */
    public void mergerStructuredFund(String exchange_type,String stock_account,String stock_code,String  entrust_amount,String session,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300702");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("EXCHANGE_TYPE", exchange_type);
        map2.put("STOCK_ACCOUNT",stock_account);
        map2.put("STOCK_CODE",stock_code);
        map2.put("ENTRUST_AMOUNT",entrust_amount);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            JSONArray data = jsonObject.getJSONArray("data");
                            StructuredFundEntity bean = new StructuredFundEntity();
                            JSONObject obj = data.getJSONObject(0);
                            bean.setInit_date(obj.getString("INIT_DATE"));
                            bean.setMerge_amount(obj.getString("ENTRUST_NO"));
                            info.setData(bean);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param exchange_type 交易类别
     * @param stock_account 当前市场的主证券账户
     * @param stock_code 证券代码
     * @param entrust_amount 委托数量
     * @param session token
     * @param TAG tag
     */
    public void splitStructuredFund(String exchange_type,String stock_account,String stock_code,String  entrust_amount,String session,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300703");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("EXCHANGE_TYPE", exchange_type);
        map2.put("STOCK_ACCOUNT",stock_account);
        map2.put("STOCK_CODE",stock_code);
        map2.put("ENTRUST_AMOUNT",entrust_amount);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            JSONArray data = jsonObject.getJSONArray("data");
                            StructuredFundEntity bean = new StructuredFundEntity();
                            JSONObject obj = data.getJSONObject(0);
                            bean.setInit_date(obj.getString("INIT_DATE"));
                            bean.setMerge_amount(obj.getString("ENTRUST_NO"));
                            info.setData(bean);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param session token
     * @param page  查第一页不用传
     * @param num  查询行数
     * @param action_in 查询可撤  0查所有  1查可撤
     * @param TAG tag
     */
    public void queryTodayEntrust(String session,String page,String num,String action_in,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300704");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("POSITION_STR",page);
        map2.put("REQUEST_NUM",num);
        map2.put("ACTION_IN",action_in);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setStoken_name(obj.getString("STOCK_NAME"));
                                bean.setStocken_code(obj.getString("STOCK_CODE"));
                                bean.setBusiness_name(obj.getString("BUSINESS_NAME"));
                                bean.setReport_time(obj.getString("CURR_TIME"));
                                bean.setEntrust_amount(obj.getString("ENTRUST_AMOUNT"));
                                bean.setStock_account(obj.getString("STOCK_ACCOUNT"));
                                bean.setPosition_str(obj.getString("POSITION_STR"));
                                bean.setCurr_date(obj.getString("CURR_DATE"));
                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));
                                bean.setEntrust_status(obj.getString("ENTRUST_STATUS"));
                                bean.setEntrust_balance(obj.getString("ENTRUST_BALANCE"));
                                bean.setBusiness_amount(obj.getString("BUSINESS_AMOUNT"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param session token
     * @param entrust_no 委托编号
     * @param TAG tag
     */
    public void fundWithdrawOrder(String session,String entrust_no,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300705");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("ENTRUST_NO",entrust_no);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setInit_date(obj.getString("INIT_DATE"));
                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param session token
     * @param TAG tag
     * @param callback callback
     * @param page page
     * @param num num
     * @param his_type 历史类型 0:自定义  1：一周内  2：一个月内  3：三个月内
     * @param start_date 开始时间  HIS_TYPE不为0时可传空
     * @param end_date  结束时间   HIS_TYPE不为0时可传空
     */
    public void queryHistoryEntrust(String session,String page,String num,String his_type,String start_date,String end_date,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300706");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("POSITION_STR",page);
        map2.put("REQUEST_NUM",num);
        map2.put("HIS_TYPE",his_type);
        map2.put("START_DATE",start_date);
        map2.put("END_DATE",end_date);
        map2.put("FLAG", "true");
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setStoken_name(obj.getString("STOCK_NAME"));
                                bean.setStocken_code(obj.getString("STOCK_CODE"));
                                bean.setBusiness_name(obj.getString("BUSINESS_NAME"));
                                bean.setReport_time(obj.getString("REPORT_TIME"));
                                bean.setEntrust_amount(obj.getString("ENTRUST_AMOUNT"));
                                bean.setStock_account(obj.getString("STOCK_ACCOUNT"));
                                bean.setPosition_str(obj.getString("POSITION_STR"));
                                bean.setCurr_date(obj.getString("CURR_DATE"));
                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));
                                bean.setEntrust_status(obj.getString("ENTRUST_STATUS"));
                                bean.setEntrust_balance(obj.getString("ENTRUST_BALANCE"));
                                bean.setBusiness_amount(obj.getString("BUSINESS_AMOUNT"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param session token
     * @param page page
     * @param num num
     * @param TAG tag
     * @param callback callback
     */
    public void queryTodayDeal(String session,String page,String num,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300707");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("POSITION_STR",page);
        map2.put("REQUEST_NUM",num);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setStoken_name(obj.getString("STOCK_NAME"));
                                bean.setStocken_code(obj.getString("STOCK_CODE"));
                                bean.setBusiness_name(obj.getString("BUSINESS_NAME"));
                                bean.setReport_time(obj.getString("RETURN_TIME"));
                                bean.setInit_date(obj.getString("INIT_DATE"));
                                bean.setPosition_str(obj.getString("POSITION_STR"));
                                bean.setEntrust_amount(obj.getString("ENTRUST_AMOUNT"));
                                bean.setEntrust_balance(obj.getString("ENTRUST_BALANCE"));
                                bean.setSerial_no(obj.getString("SERIAL_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param session token
     * @param page page
     * @param num num
     * @param his_type  0:自定义  1：一周内  2：一个月内   3：三个月内
     * @param start_date  开始时间  HIS_TYPE不为0时可传空
     * @param end_date  结束时间  HIS_TYPE不为0时可传空
     * @param TAG tag
     * @param callback  callback
     */
    public void queryHistoryDeal(String session,String page,String num,String his_type,String start_date,String end_date,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300708");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("POSITION_STR",page);
        map2.put("REQUEST_NUM",num);
        map2.put("HIS_TYPE",his_type);
        map2.put("START_DATE",start_date);
        map2.put("END_DATE",end_date);
        map2.put("FLAG", "true");
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            List<StructuredFundEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StructuredFundEntity bean = new StructuredFundEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setStoken_name(obj.getString("SECU_NAME"));
                                bean.setStocken_code(obj.getString("SECU_CODE"));
                                bean.setBusiness_name(obj.getString("BUSINESS_NAME"));
                                bean.setEntrust_status(obj.getString("BUSINESS_STATUS"));
                                bean.setReport_time(obj.getString("MATCHED_TIME"));
                                bean.setInit_date(obj.getString("INIT_DATE"));
                                bean.setPosition_str(obj.getString("KEY_STR"));
                                bean.setEntrust_amount(obj.getString("ENTRUST_AMOUNT"));//委托量等同分拆份额
                                bean.setEntrust_balance(obj.getString("ENTRUST_BALANCE"));
                                bean.setBusiness_amount(obj.getString("MATCHED_QTY"));
                                bean.setMatched_price(obj.getString("MATCHED_PRICE"));
                                bean.setMatched_amt(obj.getString("MATCHED_AMT"));
                                bean.setSerial_no(obj.getString("SERIAL_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param session token
     * @param exchange_type 市场
     * @param page page
     * @param num num
     * @param TAG tag
     * @param callback callback
     */
    public void queryNetworkVoting(String session,String exchange_type,String page,String num,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300801");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("EXCHANGE_TYPE",exchange_type);
        map2.put("POSITION_STR",page);
        map2.put("REQUEST_NUM",num);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            List<NetworkVotingEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                NetworkVotingEntity bean = new NetworkVotingEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setStock_account(obj.getString("STOCK_ACCOUNT"));
                                bean.setExchange_type(obj.getString("EXCHANGE_TYPE"));
                                bean.setMeeting_name(obj.getString("MEETING_NAME"));
                                bean.setMeeting_seq(obj.getString("MEETING_SEQ"));
                                bean.setCompany_name(obj.getString("COMPANY_NAME"));
                                bean.setCompany_code(obj.getString("COMPANY_CODE"));
                                bean.setBegin_date(obj.getString("BEGIN_DATE"));
                                bean.setEnd_date(obj.getString("END_DATE"));
                                bean.setInit_date(obj.getString("INIT_DATE"));
                                bean.setPosition_str(obj.getString("POSITION_STR"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param session token
     * @param meeting_seq 股东大会编码
     * @param TAG tag
     * @param callback callback
     */
    public void queryProposal(String session,String meeting_seq,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300802");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("MEETING_SEQ",meeting_seq);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            Map<String,List<NetworkVotingEntity>> map = new HashMap<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            map.put("0",parseJSONArray(data.getJSONArray(0)));
                            map.put("1",parseJSONArray(data.getJSONArray(1)));
                            info.setData(map);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param array json数组
     * @return List<NetworkVotingEntity>
     * @throws JSONException
     */
    private List<NetworkVotingEntity> parseJSONArray(JSONArray array) throws JSONException{
        List<NetworkVotingEntity> ses = new ArrayList<>();
        NetworkVotingEntity bean;
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String vt = obj.getString("VOTE_TYPE");
            bean = new NetworkVotingEntity();
            bean.setVote_info(obj.getString("VOTE_INFO"));
            bean.setEntrust_amount(obj.getString("VOTE_NUMCONTROL"));
            bean.setStatus(obj.getString("EN_REFCODE"));//stock_code入参
            bean.setInit_date(obj.getString("INIT_DATE"));
            bean.setVote_motion(obj.getString("VOTE_MOTION"));
            bean.setMeeting_seq(obj.getString("MEETING_SEQ"));
            bean.setVote_type(vt);
            String list = obj.optString("LIST");
            if(vt.equals("1")&&!TextUtils.isEmpty(list)){
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
     * @param session token
     * @param list  投票实体列表
     * @param TAG tag
     * @param callback callback
     */
    public void submitVoting(String session,String stock_code,String exchage_type,String meeting_seq,List<NetworkVotingEntity> list,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300803");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("STOCK_CODE",stock_code);
        map2.put("EXCHANGE_TYPE",exchage_type);
        map2.put("MEETING_SEQ",stock_code);
        List data = new ArrayList();
        for (NetworkVotingEntity entity:list) {
            Map map = new HashMap<>();
            map.put("ENTRUST_AMOUNT",entity.getEntrust_amount());
            map.put("ENTRUST_PRICE",entity.getVote_motion());
            map.put("MEETING_SEQ",entity.getMeeting_seq());
            data.add(map);
        }
        map2.put("LIST",data);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                    } catch (JSONException e) {
                        info.setCode("-2");
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
    public void queryHistoryNetworkVoting(String session, String his_type, String exchange_type,String position_str, String request_num, String begin_date, String end_date, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300804");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("HIS_TYPE", his_type);
        map2.put("POSITION_STR", position_str);
        map2.put("REQUEST_NUM", request_num);
        map2.put("EXCHANGE_TYPE",exchange_type);
        map2.put("BEGIN_DATE", begin_date);
        map2.put("END_DATE", end_date);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<NetworkVotingEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                NetworkVotingEntity bean = new NetworkVotingEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setStock_code(obj.getString("STOCK_CODE"));
                                bean.setBusiness_amount(obj.getString("BUSINESS_AMOUNT"));
                                bean.setStatus(obj.getString("ENTRUST_STATUS"));
                                bean.setStock_name(obj.getString("STOCK_NAME"));
                                bean.setMeeting_seq(obj.getString("MEETING_SEQ"));
                                bean.setInit_date(obj.getString("INIT_DATE"));
                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));
                                bean.setVote_motion(obj.getString("VOTE_MOTION"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param session token
     * @param exchange_type 市场 1 上海 2深圳
     * @param page 页码
     * @param num 数量
     * @param TAG tag
     * @param callback callback
     */
    public void queryTodayVoting(String session,String exchange_type,String page,String num,final String TAG, final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","300805");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("POSITION_STR",page);
        map2.put("REQUEST_NUM",num);
        map2.put("EXCHANGE_TYPE",exchange_type);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            List<NetworkVotingEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                NetworkVotingEntity bean = new NetworkVotingEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setMeeting_seq(obj.getString("MEETING_SEQ"));
                                bean.setStock_code(obj.getString("STOCK_CODE"));
                                bean.setVote_motion(obj.getString("ENTRUST_PRICE"));
                                bean.setStatus(obj.getString("ENTRUST_STATUS"));
                                bean.setInit_date(obj.getString("ENTRUST_DATE"));
                                bean.setStock_name(obj.getString("STOCK_NAME"));
                                bean.setBusiness_amount(obj.getString("ENTRUST_AMOUNT"));
                                bean.setPosition_str(obj.getString("POSITION_STR"));
                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param mSession token
     * @param TAG tag
     * @param callback callback
     */
    public void queryStockInfo(String session,final String TAG, final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "700070");
        map.put("token", session);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if(TextUtils.isEmpty(response)){
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if("0".equals(code)){
                            List<StockHolderInfoEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                StockHolderInfoEntity _bean = new StockHolderInfoEntity();
                                JSONObject obj = data.getJSONObject(i);
                                _bean.setCustomerCode(obj.getString("FUND_ACCOUNT"));    // 资金账号
                                _bean.setAccountType(obj.getString("MARKET"));        //市场
                                _bean.setShareholderSName(obj.getString("SECU_NAME"));//股票名称
                                _bean.setShareholderSCode(obj.getString("SECU_CODE"));//股票代码
                                ses.add(_bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param prodta_no  产品TA编号
     * @param prod_code 产品代码
     * @param fund_company 基金公司
     * @param fund_code 基金代码
     * @param session token
     * @param TAG tag
     */
    public void queryProductSuitability(String session, String prodta_no, String prod_code, String fund_company, String fund_code, String TAG, final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","331261");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("PRODTA_NO", prodta_no);
        map2.put("PROD_CODE", prod_code);
//        fund_code = "000326";
//        fund_company = "01";
        map2.put("FUND_COMPANY", fund_company);
        map2.put("FUND_CODE",fund_code);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(e.getMessage());
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    info.setCode(code);
                    info.setMsg(msg);
                    if("0".equals(code)){
                        JSONArray data = jsonObject.optJSONArray("data");
                        if (null!=data&&data.length()>0) {
                            for (int i=0;i<data.length();i++){
                                JSONObject subJsonObj = data.optJSONObject(i);
                                HashMap<String,String> resultMap = new HashMap<String, String>();
                                resultMap.put("PRODRISK_LEVEL",subJsonObj.optString("PRODRISK_LEVEL"));//产品风险等级描述
                                resultMap.put("CORP_RISK_LEVEL",subJsonObj.optString("CORP_RISK_LEVEL"));//客户风险等级
                                resultMap.put("CORP_RISK_LEVEL_INFO",subJsonObj.optString("CORP_RISK_LEVEL_INFO"));//客户风险等级描述
                                resultMap.put("ELIG_RISK_FLAG",subJsonObj.optString("ELIG_RISK_FLAG"));//风险匹配标志 此接口标志位均为1 匹配 0不匹配
                                resultMap.put("ELIG_RISK_FLAG_INFO",subJsonObj.optString("ELIG_RISK_FLAG_INFO"));//风险匹配标志描述
                                resultMap.put("ELIG_INVESTKIND_FLAG",subJsonObj.optString("ELIG_INVESTKIND_FLAG"));//投资品种标志(1 匹配，0 不匹配)
                                resultMap.put("ELIG_INVESTKIND_FLAG_INFO",subJsonObj.optString("ELIG_INVESTKIND_FLAG_INFO"));//投资品种标志描述
                                resultMap.put("ELIG_TERM_FLAG",subJsonObj.optString("ELIG_TERM_FLAG"));//投资周期匹配标志(1 匹配，0 不匹配)
                                resultMap.put("ELIG_TERM_FLAG_INFO",subJsonObj.optString("ELIG_TERM_FLAG_INFO"));//投资周期匹配标志描述
                                resultMap.put("ELIG_DEFICITRATE_FLAG",subJsonObj.optString("ELIG_DEFICITRATE_FLAG"));//亏损率匹配标志(1 匹配，0 不匹配)
                                resultMap.put("ENABLE_FLAG",subJsonObj.optString("ENABLE_FLAG"));//可操作标志(1 可以委托，0不可委托)
                                resultMap.put("NEED_VIDEO_FLAG",subJsonObj.optString("NEED_VIDEO_FLAG"));//是否需要视频录制(0 否，1是)
                                resultMap.put("URL_ID",subJsonObj.optString("URL_ID"));//跳转对象编号(双录地址)
                                resultMap.put("INSTR_BATCH_NO",subJsonObj.optString("INSTR_BATCH_NO"));//指令批号(适当性校验批次号)
                                info.setData(resultMap);
                            }
                        }
                    }
                } catch (JSONException e) {
                    info.setCode("400");
                    info.setMsg(e.getMessage());
                }
                callback.callResult(info);
            }
        });
    }


    /**
     * 331279
     * 产品适当性记录
     * @param instr_batch_no  记录批次号
     * @param oper_info 周边操作信息
     * @param session token
     * @param TAG tag
     */
    public void productSuitabilityRecord(String session, String instr_batch_no, String oper_info, String TAG, final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid","331279");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("INSTR_BATCH_NO", instr_batch_no);
        map2.put("OPER_INFO", oper_info);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(e.getMessage());
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    info.setCode(code);
                    info.setMsg(msg);
                } catch (JSONException e) {
                    info.setCode("400");
                    info.setMsg(e.getMessage());
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
     * @param requestType 3 申购 4认购
     * @param stockCode
     * @param prodta_no
     * @param session
     * @param SubscriptionMoney
     * @param callback
     */
    public void getAffirm(final String requestType, final String stockCode, final String prodta_no, String session, final String SubscriptionMoney, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("PROD_CODE",stockCode);
        map2.put("PRODTA_NO",prodta_no);
        map2.put("FLAG","true");
        map1.put("funcid","300512");
        map1.put("token",session);
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString("300512", ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(e.getMessage());
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    onError(null,new Exception("返回值为空"),0);
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_AffirmBean>() {}.getType();
                OTC_AffirmBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                List<OTC_AffirmBean.DataBean> data = bean.getData();
                ResultInfo info = new ResultInfo();
                info.setCode(code);
                info.setMsg("");
                if(("0").equalsIgnoreCase(code) && data != null){
                    for(int i=0;i<data.size();i++){
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
    public void getProductMsg(String funcid,String session, String stockCode, String prodta_no, String SubscriptionMoney, final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("PROD_CODE",stockCode);
        map2.put("PRODTA_NO",prodta_no);
        map2.put("ENTRUST_BALANCE",SubscriptionMoney);
        map2.put("FLAG","true");
//        map1.put("funcid","730201");
        map1.put("funcid",funcid);
        map1.put("token",session);
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString("730201", ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(e.getMessage());
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_SubscriptionCommitBean>() {}.getType();
                OTC_SubscriptionCommitBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                ResultInfo info = new ResultInfo();
                info.setCode(code);
                info.setMsg(msg);
                callback.callResult(info);
                /*if (code.equals("-6")) {
                    Intent intent = new Intent(context, TransactionLoginActivity.class);
                    context.startActivity(intent);
                    dismiss();
                    ((Activity)context).finish();
                } else
                if (code.equals("0")) {
                    ResultDialog.getInstance().show("委托已提交", R.mipmap.duigou);
                    dismiss();
                    isOk.callBack(true);
                }else {
                    MistakeDialog.showDialog(msg, mActivity);
                    dismiss();
                    isOk.callBack(false);
                }*/
            }
        });
    }

    /**
     * ETF认购申购赎回信息查询
     * 300720
     *
     * @param session      token
     * @param stock_code 证券代码
     * @param TAG        tag
     * @param callback   callBack
     */
    public void queryApplyfor(String session, String stock_code, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300720");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("STOCK_CODE", stock_code);
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(e.getMessage());
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setExchange_type(obj.getString("EXCHANGE_TYPE"));
                                bean.setStock_code(obj.getString("STOCK_CODE"));
                                bean.setStock_name(obj.getString("STOCK_NAME"));
                                bean.setStock_account(obj.getString("STOCK_ACCOUNT"));
                                bean.setEnable_balance(obj.getString("ENABLE_BALANCE"));
                                bean.setStock_max(obj.getString("STOCK_MAX"));
                                bean.setCash_max(obj.getString("CASH_MAX"));
                                bean.setAllot_max(obj.getString("ALLOT_MAX"));
                                bean.setRedeem_max(obj.getString("REDEEM_MAX"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param token          token
     * @param exchange_type  交易类别
     * @param stock_code     证券代码
     * @param entrust_amount 委托数量
     * @param TAG            tag
     * @param callback       callback
     */
    public void applyforDetermine(String funcid ,String session, String exchange_type, String stock_code,
                                  String entrust_amount, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
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
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setInit_date(obj.getString("INIT_DATE"));
                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }

                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param token        token
     * @param query_kind   查询控制值   0-查询全部委托；1-查询可撤委托;   0  今日查询传入0
     * @param position_str 定位串   可不传值
     * @param request_num  请求行数
     * @param TAG          tag
     * @param callback     callback
     */
    public void queryEntrust(String session, String query_kind, String position_str,
                             String request_num, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
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

        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.getJSONObject(i);
                                String init_date = Helper.formateDate1(obj.getString("INIT_DATE"));
                                bean.setInit_date(init_date);
                                String currn_time = Helper.getMyDateHMS(obj.getString("CURR_TIME"));
                                bean.setReport_time(currn_time);
                                bean.setStock_code(obj.getString("STOCK_CODE"));
                                bean.setStock_name(obj.getString("STOCK_NAME"));
                                bean.setStock_account(obj.getString("STOCK_ACCOUNT"));
                                bean.setPrev_balance(obj.getString("PREV_BALANCE"));
                                bean.setEntrust_bs(obj.getString("ENTRUST_BS"));
                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));
                                bean.setEnable_balance(obj.getString("ENTRUST_BALANCE"));
                                bean.setEntrust_amount(obj.getString("ENTRUST_AMOUNT"));
                                bean.setEntrust_prop(obj.getString("ENTRUST_PROP"));
                                bean.setEntrust_status(obj.getString("ENTRUST_STATUS"));
                                bean.setPosition_str(obj.getString("POSITION_STR"));
                                bean.setEntrust_status_name(obj.getString("ENTRUST_STATUS_NAME"));
                                bean.setExchange_type_name(obj.getString("EXCHANGE_TYPE_NAME"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param token         token
     * @param exchange_type 交易类别
     * @param entrust_no    委托编号
     * @param stock_code    证券代码
     * @param TAG           tag
     * @param callback      callback
     */
    public void revokeOrder(String session, String exchange_type, String entrust_no,
                            String stock_code, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
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

        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setInit_date(obj.getString("INIT_DATE"));
                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }

                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param token        token
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
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
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
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.getJSONObject(i);
                                String init_date = Helper.formateDate1(obj.getString("INIT_DATE"));
                                bean.setInit_date(init_date);
                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));
                                bean.setStock_name(obj.getString("STOCK_NAME"));
                                bean.setStock_code(obj.getString("STOCK_CODE"));
                                bean.setEntrust_amount(obj.getString("ENTRUST_AMOUNT"));
                                bean.setEntrust_status(obj.getString("ENTRUST_STATUS"));
                                String report = Helper.getMyDateHMS(obj.getString("REPORT_TIME"));
                                bean.setReport_time(report);
                                bean.setEntrust_status_name(obj.getString("ENTRUST_STATUS_NAME"));
                                bean.setPrev_balance(obj.getString("PREV_BALANCE"));
                                bean.setEntrust_bs(obj.getString("ENTRUST_BS"));
                                bean.setEntrust_price(obj.getString("ENTRUST_PRICE"));
                                bean.setStock_account(obj.getString("STOCK_ACCOUNT"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }

                    } catch (JSONException e) {
                        info.setCode("-2");
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
     * @param token        token
     * @param position_str 定位串   N
     * @param request_num  请求行数  Y
     * @param TAG          tag
     * @param callback     callback
     */
    public void queryDeal(String session, final String position_str, String request_num, final String TAG, final InterfaceCallback callback) {
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300746");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("POSITION_STR", position_str);
        map2.put("REQUEST_NUM", request_num);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setStock_code(obj.getString("STOCK_CODE"));//证券代码
                                bean.setStock_name(obj.getString("STOCK_NAME"));//证券名称
                                bean.setEntrust_bs(obj.optString("ENTRUST_BS"));//买卖方向
                                bean.setReal_status(obj.getString("REAL_STATUS"));//状态
                                bean.setReal_status_name(obj.getString("REAL_STATUS_NAME"));//状态名称
                                bean.setBusiness_amount(obj.getString("BUSINESS_AMOUNT"));//成交数量
                                bean.setBusiness_balance(obj.getString("BUSINESS_BALANCE"));//成交金额
                                bean.setInit_date(obj.getString("INIT_DATE"));//交易时间
                                bean.setCurr_time(obj.getString("CURR_TIME"));//委托时间
                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));//委托编号
                                bean.setStock_account(obj.getString("STOCK_ACCOUNT"));//证券编号<股东代码>
                                bean.setTrade_plat(obj.getString("TRADE_PLAT"));//交易平台<交易所名称>
                                bean.setPosition_str(obj.getString("POSITION_STR_LONG"));//定位穿
                                bean.setCbp_business_id(obj.getString("CBP_BUSINESS_ID"));//定位穿
                                bean.setShowRule(false);
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (Exception e) {
                        info.setCode("-2");
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
     * @param session token 手机识别码
     * @param stock_code 股票代码
     * @param page 定位串
     * @param num 请求行数
     * @param TAG
     * @param callback
     */
    public void constituentStock(String session,String stock_code,String page,String num,final String TAG, final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300744");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("STOCK_CODE",stock_code);
        map2.put("POSITION_STR", page);
        map2.put("REQUEST_NUM", num);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setStock_code(obj.getString("STOCK_CODE"));//证券代码
                                bean.setComponent_code(obj.getString("COMPONENT_CODE"));//成份股代码
                                bean.setStock_name(obj.getString("COMPONENT_NAME"));//成份股名称
                                bean.setEnable_balance(obj.getString("REPLACE_BALANCE"));//替代金额
                                bean.setEntrust_amount(obj.optString("COMPONENT_AMOUNT"));//成份股数量<单位数量>
                                bean.setStock_max(obj.getString("REPLACE_RATIO"));//溢价比率
                                bean.setCash_max(obj.getString("REPLACE_FLAG"));//替代标志
                                bean.setAllot_max(obj.getString("REPLACE_FLAG_NAME"));//替代标志名称
                                bean.setPosition_str(obj.getString("POSITION_STR"));//定位穿
                                bean.setShowRule(false);
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (Exception e) {
                        info.setCode("-2");
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
     * @param session  token
     * @param position_str  定位串
     * @param request_num   num
     * @param TAG tag
     * @param callback callback
     */
    public void  constituentStockList(String session , String position_str,String request_num,final String TAG,final InterfaceCallback callback){
        session = SpUtils.getString(CustomApplication.getContext(),"mSession","");
        Map map1 = new HashMap<>();
        map1.put("funcid", "300742");
        map1.put("token", session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("POSITION_STR", position_str);
        map2.put("REQUEST_NUM", request_num);
        map1.put("parms", map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("400");
                info.setMsg(ConstantUtil.NETWORK_ERROR);
                info.setTag(TAG);
                callback.callResult(info);
            }

            @Override
            public void onResponse(String response, int id) {
                ResultInfo info = new ResultInfo();
                if (TextUtils.isEmpty(response)) {
                    info.setCode("-3");
                    info.setMsg(ConstantUtil.SERVICE_NO_DATA);
                    info.setTag(TAG);
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        info.setCode(code);
                        info.setMsg(msg);
                        info.setTag(TAG);
                        if ("0".equals(code)) {
                            List<EtfDataEntity> ses = new ArrayList<>();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                EtfDataEntity bean = new EtfDataEntity();
                                JSONObject obj = data.getJSONObject(i);
                                bean.setStock_code(obj.getString("STOCK_CODE_0"));
                                bean.setComponent_code(obj.getString("STOCK_CODE_3"));
                                bean.setStock_name(obj.getString("STOCK_NAME"));
                                bean.setPosition_str(obj.getString("POSITION_STR"));
                                ses.add(bean);
                            }
                            info.setData(ses);
                        }
                    } catch (Exception e) {
                        info.setCode("-2");
                        info.setMsg(ConstantUtil.JSON_ERROR);
                        info.setTag(TAG);
                    }
                    callback.callResult(info);

                }

            }
        });


    }


}
