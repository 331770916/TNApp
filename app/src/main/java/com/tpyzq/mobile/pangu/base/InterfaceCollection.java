package com.tpyzq.mobile.pangu.base;

import android.text.TextUtils;

import com.google.gson.Gson;
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
 * 0 success -1 net error -2 json error -3 no data
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
     */
    public void queryStructuredFund(String session, String stock_code, final String TAG,final InterfaceCallback callback){
        Map map1 = new HashMap<>();
        map1.put("funcid","300701");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("FLAG", true);
        map2.put("SEC_ID", "tpyzq");
        map2.put("STOCK_CODE",stock_code);
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
                                bean.setStoken_name(obj.getString("STOKEN_NAME"));
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
     * @param sec_id 券商代码
     * @param exchange_type 交易类别
     * @param stock_account  当前市场的主证券账户
     * @param stock_code 证券代码
     * @param entrust_amount 委托数量
     * @param session token
     * @param TAG tag
     */
    public void mergerStructuredFund(String sec_id,String exchange_type,String stock_account,String stock_code,String  entrust_amount,String session,final String TAG,final InterfaceCallback callback){
        Map map1 = new HashMap<>();
        map1.put("funcid","300702");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", sec_id);
        map2.put("FLAG", true);
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
     * @param sec_id  券商代码
     * @param exchange_type 交易类别
     * @param stock_account 当前市场的主证券账户
     * @param stock_code 证券代码
     * @param entrust_amount 委托数量
     * @param session token
     * @param TAG tag
     */
    public void splitStructuredFund(String sec_id,String exchange_type,String stock_account,String stock_code,String  entrust_amount,String session,final String TAG,final InterfaceCallback callback){
        Map map1 = new HashMap<>();
        map1.put("funcid","300703");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", sec_id);
        map2.put("FLAG", true);
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
     * @param sec_id  券商代码
     * @param session token
     * @param page  查第一页不用传
     * @param num  查询行数
     * @param action_in 查询可撤  0查所有  1查可撤
     * @param TAG tag
     */
    public void queryTodayEntrust(String sec_id,String session,String page,String num,String action_in,final String TAG,final InterfaceCallback callback){
        Map map1 = new HashMap<>();
        map1.put("funcid","300704");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", sec_id);
        map2.put("FLAG", true);
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
     * 300705
     * 分级基金分级基金撤单
     * @param sec_id 券商代码
     * @param session token
     * @param entrust_no 委托编号
     * @param TAG tag
     */
    public void fundWithdrawOrder(String sec_id,String session,String entrust_no,final String TAG,final InterfaceCallback callback){
        Map map1 = new HashMap<>();
        map1.put("funcid","300705");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", sec_id);
        map2.put("FLAG", true);
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
                        String code = jsonObject.getString("MSG_CODE");
                        String msg = jsonObject.getString("MSG_TEXT");
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


    public void getData(int size,final InterfaceCallback callback){
        ResultInfo info = new ResultInfo();
        info.setCode("0");
        List<StructuredFundEntity> ses = new ArrayList<>();
         StructuredFundEntity entity;
             for (int i = 0; i < size; i++) {
                 entity = new StructuredFundEntity();
                 entity.setStoken_name("证券分级");
                 entity.setStocken_code("000888");
                 entity.setBusiness_name("分级基金合并");
                 entity.setEntrust_status("已报");
                 entity.setReport_time("10:09:08");
                 entity.setCurr_date("2017-08-08");
                 entity.setEntrust_amount("10000");
                 entity.setEntrust_balance("10000");
                 entity.setInit_date("2017-09-18");
                 entity.setBusiness_amount("10000");
                 entity.setEntrust_no("23456887387");
                 entity.setSerial_no("10000");
                 ses.add(entity);
             }
        info.setData(ses);
        callback.callResult(info);
    }

}
