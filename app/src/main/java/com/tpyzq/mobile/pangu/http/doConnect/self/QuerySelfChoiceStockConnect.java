package com.tpyzq.mobile.pangu.http.doConnect.self;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/8/23.
 * 查询自选股
 */
public class QuerySelfChoiceStockConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "QuerySelfChoiceStockConnect";
    private String mHttpTAG;
    private String mToken;
    private String mCapitalAccount;
    private String mUserId;

    public QuerySelfChoiceStockConnect(String httpTag, String token, String capitalAccount, String userId) {
        mHttpTAG = httpTag;
        mToken = token;
        mCapitalAccount = capitalAccount;
        mUserId = userId;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request(mToken, mCapitalAccount, mUserId);
    }

    private void request(String token, String capitalAccount, String userId) {
        Map map = new HashMap();
        map.put("funcid", "800121");
        map.put("token", token);
        Map map2 = new HashMap();
        map2.put("CapitalAccount", capitalAccount);
        map2.put("USERID", userId);
        map2.put("STATUS", "1");
//        map2.put("SOURCE", "A002");
        map2.put("TYPE","");

        map.put("parms", map2);

//        String tempUrl = "http://192.168.0.205:8080/HTTPServer/servlet";
        //FileUtil.URL_NEW
        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_NEW(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(CustomApplication.getContext(), "网络异常");
                String msg = "" + e.toString();
                mCallbackResult.getResult(msg, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
//                LogUtil.e(TAG,"QuerySelfChoiceStockConnect:"+response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("0".equals(code)){
                        String totalcount = res.optString("totalcount");
                        JSONArray jsonArray = res.optJSONArray("data");
                        ArrayList<StockInfoEntity> stockInfoEntities = new ArrayList<StockInfoEntity>();
                        if(null != jsonArray && jsonArray.length() >0){
                            for(int i = 0; i < jsonArray.length(); i++){
                                StockInfoEntity entity = new StockInfoEntity();
                                JSONObject json = jsonArray.optJSONObject(i);
                                entity.setCode(code);
                                entity.setTotalCount(totalcount);
                                entity.setNewPrice(json.optString("PRICE"));
                                entity.setSECU_NAME(json.optString("NAME"));
                                entity.setStockName(json.optString("NAME"));
                                entity.setIsHotStock(json.optString("ISMOST"));
                                entity.setStockNumber(json.optString("CODE"));
                                entity.setSECU_CODE(json.optString("CODE"));
                                stockInfoEntities.add(entity);
                            }
                        }
                        mCallbackResult.getResult(stockInfoEntities, TAG);
                    }else{
                        String msg = res.optString("msg");
                        mCallbackResult.getResult(msg, TAG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mCallbackResult.getResult(ConstantUtil.JSON_ERROR, TAG);
                }

//
//                //{"totalcount":1,"data":[],"code":0,"msg":"查询成功"}
//
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//
//                try {
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String,Object>().getClass());
//
//                    String totalcount = "";
//                    if (null != responseValues.get("totalcount")) {
//                        totalcount = String.valueOf(responseValues.get("totalcount"));
//                    }
//
//                    String code = "";
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
//                    }
//
//                    if (!"0".equals(code)) {
//                        mCallbackResult.getResult("系统异常", TAG);
//                        return;
//                    }
//
//                    String msg = "";
//                    if (null != responseValues.get("msg")) {
//                        msg = String.valueOf(responseValues.get("msg"));
//                    }
//
//                    List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//                    ArrayList<StockInfoEntity> stockInfoEntities = new ArrayList<StockInfoEntity>();
//
//                    if (null != responseValues.get("data") && responseValues.get("data") instanceof List) {
//                        data =  (List<Map<String, Object>>) responseValues.get("data");
//
//                        if (data != null && data.size() > 0) {
//                            for (Map<String, Object> item : data) {
//                                StockInfoEntity  entity = new StockInfoEntity();
//
//                                entity.setCode(code);
//                                entity.setTotalCount(totalcount);
//
//                                String RESERVEDCHAR3 = "";
//                                if (null != item.get("RESERVEDCHAR3")) {
//                                    RESERVEDCHAR3 = String.valueOf(item.get("RESERVEDCHAR3"));
//                                }
//
//                                String BINDING = "";
//                                if (null != item.get("BINDING")) {
//                                    BINDING = String.valueOf(item.get("BINDING"));
//                                }
//
//                                String PRICE = "";
//                                if (null != item.get("PRICE")) {
//                                    PRICE = String.valueOf(item.get("PRICE"));
//                                }
//
//                                entity.setNewPrice(PRICE);
//
//                                String RESERVEDCHAR2 = "";
//                                if (null != item.get("RESERVEDCHAR2")) {
//                                    RESERVEDCHAR2 = String.valueOf(item.get("RESERVEDCHAR2"));
//                                }
//
//                                String NAME = "";
//                                if (null != item.get("NAME")) {
//                                    NAME = String.valueOf(item.get("NAME"));
//                                }
//
//                                entity.setSECU_NAME(NAME);
//                                entity.setStockName(NAME);
//
//                                String RESERVEDCHAR1 = "";
//                                if (null != item.get("RESERVEDCHAR1")) {
//                                    RESERVEDCHAR1 = String.valueOf(item.get("RESERVEDCHAR1"));
//                                }
//
//                                String SORTRULE = "";
//                                if (null != item.get("SORTRULE")) {
//                                    SORTRULE = String.valueOf(item.get("SORTRULE"));
//                                }
//
//                                String ISMOST = "";
//
//                                if (null != item.get("ISMOST")) {
//                                    ISMOST = String.valueOf(item.get("ISMOST"));
//                                }
//
//                                entity.setIsHotStock(ISMOST);
//
//                                String CREATETIME = "";
//                                if (null != item.get("CREATETIME")) {
//                                    CREATETIME = String.valueOf(item.get("CREATETIME"));
//                                }
//
//                                String STATUS ="";
//                                if (null != item.get("STATUS")) {
//                                    STATUS = String.valueOf(item.get("STATUS"));
//                                }
//
//                                String RESERVEDNUMBER2 = "";
//                                if (null != item.get("RESERVEDNUMBER2")) {
//                                    RESERVEDNUMBER2 = String.valueOf(item.get("RESERVEDNUMBER2"));
//                                }
//
//                                String RESERVEDNUMBER1 = "";
//                                if (null != item.get("RESERVEDNUMBER1")) {
//                                    RESERVEDNUMBER1 = String.valueOf(item.get("RESERVEDNUMBER1"));
//                                }
//
//                                String SOURCE = "";
//                                if (null != item.get("SOURCE")) {
//                                    SOURCE = String.valueOf(item.get("SOURCE"));
//                                }
//
//                                String RESERVEDNUMBER3 = "";
//                                if (null != item.get("RESERVEDNUMBER3")) {
//                                    RESERVEDNUMBER3 = String.valueOf(item.get("RESERVEDNUMBER3"));
//                                }
//
//                                String CAPITALACCOUNT = "";
//                                if (null != item.get("CAPITALACCOUNT")) {
//                                    CAPITALACCOUNT = String.valueOf(item.get("CAPITALACCOUNT"));
//                                }
//
//                                String ID = "";
//                                if (null != item.get("ID")) {
//                                    ID = String.valueOf(item.get("ID"));
//                                }
//
//                                String UPDATETIME = "";
//                                if (null != item.get("UPDATETIME")) {
//                                    UPDATETIME = String.valueOf(item.get("UPDATETIME"));
//                                }
//
//                                String TYPE = "";
//                                if (null != item.get("TYPE")) {
//                                    TYPE = String.valueOf(item.get("TYPE"));
//                                }
//
//                                String CODE = "";
//                                if (null != item.get("CODE")) {
//                                    CODE = String.valueOf(item.get("CODE"));
//                                }
//                                entity.setStockNumber(CODE);
//                                entity.setSECU_CODE(CODE);
//
//                                String USERID = "";
//                                if (null != item.get("USERID")) {
//                                    USERID = String.valueOf(item.get("USERID"));
//                                }
//
//                                stockInfoEntities.add(entity);
//
//                            }
//                        }
//                        mCallbackResult.getResult(stockInfoEntities, TAG);
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    String msg = "" + e.toString();
//                    mCallbackResult.getResult(msg, TAG);
//                }

            }
        });
    }
}
