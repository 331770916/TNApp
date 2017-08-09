package com.tpyzq.mobile.pangu.http.doConnect.home;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/3/1.
 */

public class GetProductInfoOtcConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetProductInfoOtcConnect";
    private String mHttpTAG;
    private String mToken;
    private String mProCode;
    private String mProType;


    public GetProductInfoOtcConnect(String httpTag, String token, String proCode, String proType) {
        mHttpTAG = httpTag;
        mToken = token;
        mProCode = proCode;
        mProType = proType;
    }

    public void doConnect(ICallbackResult callbackResult){
        mCallbackResult = callbackResult;
        request();
    }

    private void request() {
        Map params = new HashMap();
        params.put("FUNCTIONCODE", "HQTNG105");
        params.put("TOKEN", mToken);

        final Map map = new HashMap();
        map.put("prod_code", mProCode);

        params.put("PARAMS", map);
        Gson gson = new Gson();
        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_HQ_WB(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                ArrayList<CleverManamgerMoneyEntity> entities = new ArrayList<CleverManamgerMoneyEntity>();
                CleverManamgerMoneyEntity entity = new CleverManamgerMoneyEntity();
                entity.setMistackMsg("" + e.toString());
                mCallbackResult.getResult(entities, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                //{"message":[{"RISKLEVEL":"2","TIP":"","BUY_LOW_AMOUNT":"","PAYDAY":"","INVEST_TYPE":"","DESCRIPTION":"","INCOME_TYPE":"","INTERESTDAY":"","PRODCODE":"P1211","PUBCOMPANY":"","OTCPROAOCOLDATE":[{"fileurl":"","filename":""},{"fileurl":"","filename":""}],"PRODTYPE":"","REVENUERULE":"","PRODNAME":"mike测试文件1","IPO_END_DATE":"","COMPREF":"0.00","SUBNAME":"mike测试文件1","PROD_STATUS":"","INCOMETYPE":"","OTCNOTICEDATE":[{"fileurl":"","filename":""}],"IPO_BEGIN_DATE":"","PRODRATIO":"","ENDDAY":"","INVESTDAYS":"","TYPE":"3"}],"code":"200","type":"success"}
               try{
                   JSONObject res = new JSONObject(response);
                   ArrayList<CleverManamgerMoneyEntity> cleverManamgerMoneyEntitys = new ArrayList<CleverManamgerMoneyEntity>();
                   if("200".equals(res.optString("code"))){
                       JSONArray data = res.getJSONArray("message");
                       if (data != null && data.length() > 0) {
                           for (int i = 0; i < data.length(); i++) {
                               JSONObject json = data.optJSONObject(i);
                               CleverManamgerMoneyEntity cleverManamgerMoneyEntity = new CleverManamgerMoneyEntity();
                               cleverManamgerMoneyEntity.setRISKLEVEL(json.optString("RISKLEVEL"));
                               cleverManamgerMoneyEntity.setTIP(json.optString("TIP"));
                               cleverManamgerMoneyEntity.setBUY_LOW_AMOUNT(json.optString("BUY_LOW_AMOUNT"));
                               cleverManamgerMoneyEntity.setPAYDAY(json.optString("PAYDAY"));
                               cleverManamgerMoneyEntity.setINVEST_TYPE(json.optString("INVEST_TYPE"));
                               cleverManamgerMoneyEntity.setDESCRIPTION(json.optString("DESCRIPTION"));
                               cleverManamgerMoneyEntity.setINCOMETYPE(json.optString("INCOME_TYPE"));
                               cleverManamgerMoneyEntity.setINTERESTDAY(json.optString("INTERESTDAY"));
                               cleverManamgerMoneyEntity.setPRODCODE(json.optString("PRODCODE"));
                               cleverManamgerMoneyEntity.setPUBCOMPANY(json.optString("PUBCOMPANY"));
                               cleverManamgerMoneyEntity.setPRODTYPE(json.optString("PRODTYPE"));
                               cleverManamgerMoneyEntity.setREVENUERULE(json.optString("REVENUERULE"));
                               cleverManamgerMoneyEntity.setPRODNAME(json.optString("PRODNAME"));
                               cleverManamgerMoneyEntity.setIPO_END_DATE(json.optString("IPO_END_DATE"));
                               cleverManamgerMoneyEntity.setCOMPREF(json.optString("COMPREF"));
                               cleverManamgerMoneyEntity.setSUBNAME(json.optString("SUBNAME"));
                               cleverManamgerMoneyEntity.setPROD_STATUS(json.optString("PROD_STATUS"));
                               cleverManamgerMoneyEntity.setIPO_BEGIN_DATE(json.optString("IPO_BEGIN_DATE"));
                               cleverManamgerMoneyEntity.setCREAT_TIME(json.optString("CREAT_TIME"));
                               cleverManamgerMoneyEntity.setPRODRATIO(json.optString("PRODRATIO"));
                               cleverManamgerMoneyEntity.setENDDAY(json.optString("ENDDAY"));
                               cleverManamgerMoneyEntity.setINVESTDAYS(json.optString("INVESTDAYS"));
                               cleverManamgerMoneyEntity.setTYPE(json.optString("TYPE"));
                               //产品风险说明附件(Array)
                               if(json.has("OTCPROAOCOLDATE")){
                                   JSONArray otcProaoclDate = json.getJSONArray("OTCPROAOCOLDATE");
                                   if(null != otcProaoclDate && otcProaoclDate.length() > 0){
                                       ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
                                       for(int j = 0;j < otcProaoclDate.length();j++){
                                           Map<String,String> map = new HashMap<String, String>();
                                           map.put("name", otcProaoclDate.getJSONObject(j).optString("filename"));
                                           map.put("url",otcProaoclDate.getJSONObject(j).optString("fileurl"));
                                           maps.add(map);
                                       }
                                       cleverManamgerMoneyEntity.setOtcProaoclDate(maps);
                                   }
                               }

                               //产品开户附件(Array)
                               if(json.has("OTCNOTICEDATE")){
                                   JSONArray otcNoticeDate = json.getJSONArray("OTCNOTICEDATE");
                                   if(null != otcNoticeDate && otcNoticeDate.length() > 0){
                                       ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
                                       for(int k = 0;k < otcNoticeDate.length();k++){
                                           Map<String,String> map = new HashMap<String, String>();
                                           map.put("name", otcNoticeDate.getJSONObject(k).optString("filename"));
                                           map.put("url",otcNoticeDate.getJSONObject(k).optString("fileurl"));
                                           maps.add(map);
                                       }
                                       cleverManamgerMoneyEntity.setOtcNoticeDate(maps);
                                   }
                               }
                              cleverManamgerMoneyEntitys.add(cleverManamgerMoneyEntity);
                           }
                       }
                       mCallbackResult.getResult(cleverManamgerMoneyEntitys, TAG);
                   }else{
                       mCallbackResult.getResult(res.opt("msg"),TAG);
                   }
               }catch (JSONException e){
                   e.printStackTrace();
                   mCallbackResult.getResult("报文解析异常", TAG);
               }
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                ArrayList<CleverManamgerMoneyEntity> cleverManamgerMoneyEntitys = new ArrayList<CleverManamgerMoneyEntity>();
//                try {
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//                    String type = "";
//                    if (null != responseValues.get("type")) {
//                        type = String.valueOf(responseValues.get("type"));
//                    }
//
//                    if (!"success".equals(type)) {
//                        ArrayList<CleverManamgerMoneyEntity> entities = new ArrayList<CleverManamgerMoneyEntity>();
//                        mCallbackResult.getResult(entities, TAG);
//                        return;
//                    }
//
//                    Object object = responseValues.get("message");
//
//                    if (null != object && object instanceof ArrayList) {
//
//                        List<Object> datas = (List<Object>) object;
//
//                        if (datas != null && datas.size() > 0) {
//                            for (Object subObj : datas) {
//                                Map<String, Object> map1 = (Map<String, Object>) subObj;
//                                CleverManamgerMoneyEntity cleverManamgerMoneyEntity = new CleverManamgerMoneyEntity();
//
//                                String RISKLEVEL = "";          //风险等级(String)
//                                if (null != map1.get("RISKLEVEL")) {
//                                    RISKLEVEL = String.valueOf(map1.get("RISKLEVEL"));
//                                }
//
//                                cleverManamgerMoneyEntity.setRISKLEVEL(RISKLEVEL);
//
//                                String TIP = "";                //挂钩标的(String)
//                                if (null != map1.get("TIP")) {
//                                    TIP = String.valueOf(map1.get("TIP"));
//                                }
//
//                                cleverManamgerMoneyEntity.setTIP(TIP);
//
//                                String BUY_LOW_AMOUNT = "";     //个人最低申购金额(String)
//                                if (null != map1.get("BUY_LOW_AMOUNT")) {
//                                    BUY_LOW_AMOUNT = String.valueOf(map1.get("BUY_LOW_AMOUNT"));
//                                }
//
//                                cleverManamgerMoneyEntity.setBUY_LOW_AMOUNT(BUY_LOW_AMOUNT);
//
//                                String PAYDAY = "";             //到账日(String)
//                                if (null != map1.get("PAYDAY")) {
//                                    PAYDAY = String.valueOf(map1.get("PAYDAY"));
//                                    PAYDAY = "20181212";
//                                }
//
//                                cleverManamgerMoneyEntity.setPAYDAY(PAYDAY);
//
//                                String INVEST_TYPE = "";        //投资类别(String)
//                                if (null != map1.get("INVEST_TYPE")) {
//                                    INVEST_TYPE = String.valueOf(map1.get("INVEST_TYPE"));
//                                }
//
//                                cleverManamgerMoneyEntity.setINVEST_TYPE(INVEST_TYPE);
//
//                                String DESCRIPTION = "";        //相关说明(String)
//                                if (null != map1.get("DESCRIPTION")) {
//                                    DESCRIPTION = String.valueOf(map1.get("DESCRIPTION"));
//                                }
//
//                                cleverManamgerMoneyEntity.setDESCRIPTION(DESCRIPTION);
//
//                                String INCOME_TYPE = "";        //收益类型(String)
//                                if (null != map1.get("INCOME_TYPE")) {
//                                    INCOME_TYPE = String.valueOf(map1.get("INCOME_TYPE"));
//                                }
//
//                                cleverManamgerMoneyEntity.setINCOMETYPE(INCOME_TYPE);
//
//                                String INTERESTDAY = "";        //起息日（产品成立日）(String)
//                                if (null != map1.get("INTERESTDAY") ) {
//                                    INTERESTDAY = String.valueOf(map1.get("INTERESTDAY"));
//                                }
//                                cleverManamgerMoneyEntity.setINTERESTDAY(INTERESTDAY);
//
//                                String PRODCODE = "";           //产品代码(String)
//                                if (null != map1.get("PRODCODE") ) {
//                                    PRODCODE = String.valueOf(map1.get("PRODCODE"));
//                                }
//
//                                cleverManamgerMoneyEntity.setPRODCODE(PRODCODE);
//
//                                String PUBCOMPANY = "";         //产品发行公司(String)
//                                if (null != map1.get("PUBCOMPANY") ) {
//                                    PUBCOMPANY = String.valueOf(map1.get("PUBCOMPANY"));
//                                }
//
//                                cleverManamgerMoneyEntity.setPUBCOMPANY(PUBCOMPANY);
//
//                                String PRODTYPE = "";           //产品类型（产品代码类型）(String)
//                                if (null != map1.get("PRODTYPE") ) {
//                                    PRODTYPE = String.valueOf(map1.get("PRODTYPE"));
//                                }
//
//                                cleverManamgerMoneyEntity.setPRODTYPE(PRODTYPE);
//
//                                String REVENUERULE = "";        //收益规则(String)
//                                if (null != map1.get("REVENUERULE") ) {
//                                    REVENUERULE = String.valueOf(map1.get("REVENUERULE"));
//                                }
//
//                                cleverManamgerMoneyEntity.setREVENUERULE(REVENUERULE);
//
//                                String PRODNAME = "";           //产品全称(String)
//                                if (null != map1.get("PRODNAME") ) {
//                                    PRODNAME = String.valueOf(map1.get("PRODNAME"));
//                                }
//
//                                cleverManamgerMoneyEntity.setPRODNAME(PRODNAME);
//
//                                String IPO_END_DATE = "";       //产品募集结束如期(String)
//                                if (null != map1.get("IPO_END_DATE") ) {
//                                    IPO_END_DATE = String.valueOf(map1.get("IPO_END_DATE"));
//                                }
//
//                                cleverManamgerMoneyEntity.setIPO_END_DATE(IPO_END_DATE);
//
//                                String COMPREF = "";            //
//                                if (null != map1.get("COMPREF") ) {
//                                    COMPREF = String.valueOf(map1.get("COMPREF"));
//                                }
//
//                                cleverManamgerMoneyEntity.setCOMPREF(COMPREF);
//
//                                String SUBNAME = "";            //产品简称(String)
//                                if (null != map1.get("SUBNAME") ) {
//                                    SUBNAME = String.valueOf(map1.get("SUBNAME"));
//                                }
//
//                                cleverManamgerMoneyEntity.setSUBNAME(SUBNAME);
//
//                                String PROD_STATUS = "";        //产品状态（产品交易状态）(String)
//                                if (null != map1.get("PROD_STATUS") ) {
//                                    PROD_STATUS = String.valueOf(map1.get("PROD_STATUS"));
//                                }
//
//                                cleverManamgerMoneyEntity.setPROD_STATUS(PROD_STATUS);
//
//                                String INCOMETYPE = "";         //收益类型(String)
//                                if (null != map1.get("INCOMETYPE") ) {
//                                    INCOMETYPE = String.valueOf(map1.get("INCOMETYPE"));
//                                }
//
////                                cleverManamgerMoneyEntity.setINCOMETYPE(INCOMETYPE);
//
//                                String IPO_BEGIN_DATE = "";     //产品募集开始日期(String)
//                                if (null != map1.get("IPO_BEGIN_DATE") ) {
//                                    IPO_BEGIN_DATE = String.valueOf(map1.get("IPO_BEGIN_DATE"));
//                                }
//
//                                cleverManamgerMoneyEntity.setIPO_BEGIN_DATE(IPO_BEGIN_DATE);
//
//                                String PRODRATIO = "";          //预期年化收益率(String)
//                                if (null != map1.get("PRODRATIO") ) {
//                                    PRODRATIO = String.valueOf(map1.get("PRODRATIO"));
//                                }
//
//                                cleverManamgerMoneyEntity.setPRODRATIO(PRODRATIO);
//
//                                String ENDDAY = "";             //到期日（产品结束日）(String)
//                                if (null != map1.get("ENDDAY") ) {
//                                    ENDDAY = String.valueOf(map1.get("ENDDAY"));
//                                }
//
//                                cleverManamgerMoneyEntity.setENDDAY(ENDDAY);
//
//                                String INVESTDAYS = "";         //产品投资期限(String)
//                                if (null != map1.get("INVESTDAYS") ) {
//                                    INVESTDAYS = String.valueOf(map1.get("INVESTDAYS"));
//                                }
//
//                                cleverManamgerMoneyEntity.setINVESTDAYS(INVESTDAYS);
//
//                                String TYPE = "";               //产品类型(String)
//                                if (null != map1.get("TYPE") ) {
//                                    TYPE = String.valueOf(map1.get("TYPE"));
//                                }
//
//                                cleverManamgerMoneyEntity.setTYPE(TYPE);
//
//                                Object pdf1 = map1.get("OTCPROAOCOLDATE");//产品风险/说明附件(Array)
//
//                                if (pdf1 != null) {
//                                    List<Object> otcProaoclDate = (List<Object>) pdf1;
//                                    if (otcProaoclDate != null && otcProaoclDate.size() > 0) {
//                                        ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
//
//                                        for (Object _bean : otcProaoclDate) {
//
//                                            if (null != _bean) {
//                                                Map<String, Object> subData = (Map<String, Object>) _bean;
//
//                                                String name = "";
//                                                if (null != subData.get("filename")) {
//                                                    name = String.valueOf(subData.get("filename"));
//                                                }
//
//                                                String url = "";
//                                                if (null != subData.get("fileurl")) {
//                                                    url = String.valueOf(subData.get("fileurl"));
//                                                }
//
//
//                                                map.put("name", name);
//                                                map.put("url",url);
//                                                maps.add(map);
//
//                                            }
//                                        }
//                                        cleverManamgerMoneyEntity.setOtcProaoclDate(maps);
//                                    }
//                                }
//
//                                Object pdf2 = map1.get("OTCNOTICEDATE");//产品开户附件(Array)
//
//                                if (pdf2 != null) {
//
//                                    List<Object> subData = (List<Object>) pdf2;
//
//                                    if (subData != null && subData.size() > 0) {
//                                        ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
//
//                                        for (Object _bean : subData) {
//
//                                            Map<String, Object> data = ( Map<String, Object>) _bean;
//
//                                            String name = "";
//                                            if (null != data.get("filename")) {
//                                                name = String.valueOf(data.get("filename"));
//                                            }
//
//                                            String url = "";
//                                            if (null != data.get("fileurl")) {
//                                                url = String.valueOf(data.get("fileurl"));
//                                            }
//
//                                            Map<String, String> map = new HashMap<String, String>();
//                                            map.put("name", name);
//                                            map.put("url", url);
//                                            maps.add(map);
//                                        }
//                                        cleverManamgerMoneyEntity.setOtcNoticeDate(maps);
//                                    }
//                                }
//
//                                cleverManamgerMoneyEntitys.add(cleverManamgerMoneyEntity);
//
//                            }
//                        }
//
//                    }
//
//                    mCallbackResult.getResult(cleverManamgerMoneyEntitys, TAG);
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        });
    }
}
