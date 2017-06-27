package com.tpyzq.mobile.pangu.base;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.data.NetworkVotingEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
                                bean.setEntrust_amount(obj.getString("BUSINESS_AMOUNT"));
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
    public void queryHistoryNetworkVoting(String session,String begin_date,String end_date,final String TAG,final InterfaceCallback callback){
        Map map1 = new HashMap<>();
        map1.put("funcid","300804");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
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
}
