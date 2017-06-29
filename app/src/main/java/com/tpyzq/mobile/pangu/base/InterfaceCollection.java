package com.tpyzq.mobile.pangu.base;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.data.AssessConfirmEntity;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.OTC_AffirmBean;
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionCommitBean;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
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
 * 0 success -1 net error -2 json error -3 no data -6 jump
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
                info.setCode("-1");
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
        Map map1 = new HashMap<>();
        map1.put("funcid","300701");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map2.put("STOCK_CODE",stock_code);
        map1.put("parms",map2);
       String A=new Gson().toJson(map1);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("-1");
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
                info.setCode("-1");
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
                info.setCode("-1");
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
                info.setCode("-1");
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
                info.setCode("-1");
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
//                                bean.setStoken_name(obj.getString("STOCK_NAME"));
//                                bean.setStocken_code(obj.getString("STOCK_CODE"));
//                                bean.setBusiness_name(obj.getString("BUSINESS_NAME"));
//                                bean.setReport_time(obj.getString("REPORT_TIME"));
//                                bean.setEntrust_amount(obj.getString("ENTRUST_AMOUNT"));
//                                bean.setStock_account(obj.getString("STOCK_ACCOUNT"));
//                                bean.setPosition_str(obj.getString("POSITION_STR"));
//                                bean.setCurr_date(obj.getString("CURR_DATE"));
//                                bean.setEntrust_no(obj.getString("ENTRUST_NO"));
//                                bean.setEntrust_status(obj.getString("ENTRUST_STATUS"));
//                                bean.setEntrust_balance(obj.getString("ENTRUST_BALANCE"));
//                                bean.setEntrust_amount(obj.getString("BUSINESS_AMOUNT"));
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
                info.setCode("-1");
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
                info.setCode("-1");
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
                info.setCode("-1");
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
                info.setCode("-1");
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
                info.setCode("-1");
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
                                bean.setInit_date(obj.getString("INIT_DATE"));
                                bean.setVote_motion(obj.getString("VOTE_MOTION"));
                                bean.setVote_info(obj.getString("VOTE_INFO"));
                                bean.setVote_type(obj.getString("VOTE_TYPE"));
                                bean.setVote_numcontrol(obj.getString("VOTE_NUMCONTROL"));
                                bean.setMeeting_seq(obj.getString("MEETING_SEQ"));
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
     * 300803
     * 提交投票
     * @param session token
     * @param stock_code  股票代码
     * @param entrust_amount  数量
     * @param entrust_price 投票议案（序号）
     * @param meeting_seq 股东大会编码
     * @param TAG tag
     * @param callback callback
     */
    public void submitVoting(String session,String stock_code,String entrust_amount,String entrust_price,String meeting_seq,final String TAG,final InterfaceCallback callback){
        Map map1 = new HashMap<>();
        map1.put("funcid","300803");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("STOCK_CODE",stock_code);
        map2.put("entrust_amount",entrust_amount);
        map2.put("entrust_price",entrust_price);
        map2.put("meeting_seq",meeting_seq);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("-1");
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
     * @param session token
     * @param begin_date  起始日期
     * @param end_date 到期日期
     * @param TAG tag
     * @param callback callback
     */
    public void queryHistoryNetworkVoting(String session,String his_type ,String position_str,String request_num,String begin_date,String end_date,final String TAG,final InterfaceCallback callback){
        Map map1 = new HashMap<>();
        map1.put("funcid","300804");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("HIS_TYPE",his_type);
        map2.put("POSITION_STR",position_str);
        map2.put("REQUEST_NUM",request_num);
        map2.put("BEGIN_DATE",begin_date);
        map2.put("END_DATE",end_date);
        map1.put("parms",map2);
        net.okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("-1");
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
                                bean.setInit_date(obj.getString("INIT_DATE"));
                                bean.setMeeting_seq(obj.getString("MEETING_SEQ"));
                                bean.setStock_code(obj.getString("STOCK_CODE"));
                                bean.setVote_motion(obj.getString("VOTE_MOTION"));
                                bean.setBusiness_amount(obj.getString("BUSINESS_AMOUNT"));
                                bean.setStock_name(obj.getString("STOCK_NAME"));
                                bean.setStatus(obj.getString("STATUS"));
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
                info.setCode("-1");
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
                    info.setCode("-1");
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
                info.setCode("-1");
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
                    info.setCode("-1");
                    info.setMsg(e.getMessage());
                }
                callback.callResult(info);
            }
        });
    }

    /**
     * OTC认购获取判断是否跳转 确认书界面的 值
     */
    public void getAffirm(final String stockCode, final String prodta_no, String session, final String SubscriptionMoney, final InterfaceCallback callback) {
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
                info.setCode("-1");
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
                        assessConfirmBean.type = "3";
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
    public void getProductMsg(String session, String stockCode, String prodta_no, String SubscriptionMoney, final InterfaceCallback callback){
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("PROD_CODE",stockCode);
        map2.put("PRODTA_NO",prodta_no);
        map2.put("ENTRUST_BALANCE",SubscriptionMoney);
        map2.put("FLAG","true");
        map1.put("funcid","730201");
        map1.put("token",session);
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString("730201", ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultInfo info = new ResultInfo();
                info.setCode("-1");
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


    public void getData(int size,final InterfaceCallback callback){
        ResultInfo info = new ResultInfo();
        info.setCode("0");
        List<NetworkVotingEntity> ses = new ArrayList<>();
        NetworkVotingEntity entity;
        for (int i = 0; i < size; i++) {
            entity = new NetworkVotingEntity();
            entity.setMeeting_seq("证券分级");
            entity.setStock_code("000888");
            entity.setStatus("分级基金合并");
            entity.setBusiness_amount("已报");
            entity.setStock_name("分级基金合并");
            entity.setInit_date("2017-09-18");
            entity.setBusiness_amount("10000");
            ses.add(entity);
        }
        info.setData(ses);
        callback.callResult(info);
    }
}
