package com.tpyzq.mobile.pangu.base;

import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by ltyhome on 23/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: Interface Manager
 */
public class InterfaceCollection {
    //网络请求类
    private NetWorkUtil net;
    //接口回调
    private InterfaceCallback callback;

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
     * 设置回调接口
     * @param callback
     */
    public void setInterfaceCallback(InterfaceCallback callback){
        this.callback = callback;
    }

    /**
     * 300701
     * 分级基金信息查询
     * @param session token
     * @param stock_code 证券代码
     * @param TAG tag
     */
    public void queryStructuredFund(String session, String stock_code, String TAG){
        Map map1 = new HashMap<>();
        map1.put("funcid","300701");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("FLAG", "true");
        map2.put("STOCK_CODE",stock_code);
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
                    info.setCode("-1");
                    info.setMsg(e.getMessage());
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
    public void mergerStructuredFund(String sec_id,String exchange_type,String stock_account,String stock_code,int entrust_amount,String session,String TAG){
        Map map1 = new HashMap<>();
        map1.put("funcid","300702");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "sec_id");
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
                        JSONArray data = jsonObject.getJSONArray("data");
                        StructuredFundEntity bean = new StructuredFundEntity();
                        JSONObject obj = data.getJSONObject(0);
                        bean.setInit_date(obj.getInt("INIT_DATE"));
                        bean.setMerge_amount(obj.getString("ENTRUST_NO"));
                        info.setData(bean);
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
    public void splitStructuredFund(String sec_id,String exchange_type,String stock_account,String stock_code,int entrust_amount,String session,String TAG){
        Map map1 = new HashMap<>();
        map1.put("funcid","300703");
        map1.put("token",session);
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "sec_id");
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
                        JSONArray data = jsonObject.getJSONArray("data");
                        StructuredFundEntity bean = new StructuredFundEntity();
                        JSONObject obj = data.getJSONObject(0);
                        bean.setInit_date(obj.getInt("INIT_DATE"));
                        bean.setMerge_amount(obj.getString("ENTRUST_NO"));
                        info.setData(bean);
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
     * 给属性设置值
     * @return object
     */
    public Object reflection(Object bean) throws IllegalAccessException{
        Class clazz =  bean.getClass();
        Field[] fs = clazz.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true);
            f.set(bean,"111") ;
        }
        return bean;
    }

    public void getData(int size){
        ResultInfo info = new ResultInfo();
        info.setCode("0");
        try {
             List<StructuredFundEntity> ses = new ArrayList<>();
             StructuredFundEntity entity;
             for (int i = 0; i < size; i++) {
                 entity = (StructuredFundEntity) reflection(new StructuredFundEntity());
                 ses.add(entity);
             }
            info.setData(ses);
        }catch (Exception e){
            info.setCode("-1");
            info.setMsg(e.getMessage());
        }
        callback.callResult(info);
    }

}
