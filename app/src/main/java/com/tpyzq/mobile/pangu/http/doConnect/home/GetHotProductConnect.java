package com.tpyzq.mobile.pangu.http.doConnect.home;

import android.text.TextUtils;

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
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/10/10.
 * 热销产品网络连接
 */
public class GetHotProductConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetHotProductConnect";
    private String mHttpTAG;
    private String mToken;


    public GetHotProductConnect(String httpTag, String token) {
        mHttpTAG = httpTag;
        mToken = token;
    }

    public void doConnect(ICallbackResult callbackResult){
        mCallbackResult = callbackResult;
        request();
    }

    public void request() {

        Map params = new HashMap();
        params.put("funcid", "100235");
        params.put("token", mToken);

        Map[] array = new Map[1];
        Map map = new HashMap();
        map.put("", "");
        array[0] = map;

        params.put("parms", map);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.ULR_MANAGEMONEY, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                mCallbackResult.getResult(e.toString(), TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                //{"totalcount":12,"data":[{"RISKLEVEL":"0","BUY_LOW_AMOUNT":"1","INCOMETYPE":"-1","PRODCODE":"C124","PRODRATIO":"1","INVESTDAYS":"1","TYPE":"3","PRODNAME":"测试2"},{"RISKLEVEL":"0","BUY_LOW_AMOUNT":"1","INCOMETYPE":"-1","PRODCODE":"1","PRODRATIO":"1","INVESTDAYS":"1","TYPE":"3","PRODNAME":"1"},{"RISKLEVEL":"0","BUY_LOW_AMOUNT":"2","INCOMETYPE":"-1","PRODCODE":"2","PRODRATIO":"2","INVESTDAYS":"2","TYPE":"3","PRODNAME":"2"},{"RISKLEVEL":"0","BUY_LOW_AMOUNT":"34567","INCOMETYPE":"-1","PRODCODE":"JADETYPE3","PRODRATIO":"12","INVESTDAYS":"14","TYPE":"3","PRODNAME":"JADETYPE3"},{"RISKLEVEL":"1","BUY_LOW_AMOUNT":"12345","INCOMETYPE":"1","PRODCODE":"JADETYPE1","PRODRATIO":"0.08","INVESTDAYS":"100","TYPE":"2","PRODNAME":"太平洋证券JADE1"},{"RISKLEVEL":"0","BUY_LOW_AMOUNT":"1","INCOMETYPE":"1","PRODCODE":"0.0.011","PRODRATIO":"1%","INVESTDAYS":"1","TYPE":"2","PRODNAME":"1"},{"RISKLEVEL":"0","BUY_LOW_AMOUNT":"1","INCOMETYPE":"1","PRODCODE":"1W1","PRODRATIO":"21%","INVESTDAYS":"1","TYPE":"2","PRODNAME":"1"},{"RISKLEVEL":"0","BUY_LOW_AMOUNT":"1","INCOMETYPE":"1","PRODCODE":"3331","PRODRATIO":"1","INVESTDAYS":"1","TYPE":"2","PRODNAME":"你好"},{"RISKLEVEL":"0","BUY_LOW_AMOUNT":"1","INCOMETYPE":"3","PRODCODE":"121222","PRODRATIO":"1","INVESTDAYS":"12345","TYPE":"2","PRODNAME":"123"},{"RISKLEVEL":"0","BUY_LOW_AMOUNT":"2","INCOMETYPE":"2","PRODCODE":"111","PRODRATIO":"22.12%","INVESTDAYS":"11111","TYPE":"2","PRODNAME":"eee"},{"RISKLEVEL":"0","BUY_LOW_AMOUNT":"23456","INCOMETYPE":"1","PRODCODE":"JADETYPE2","PRODRATIO":"0.22","INVESTDAYS":"200","TYPE":"2","PRODNAME":"JADETYPE2"},{"RISKLEVEL":"3","BUY_LOW_AMOUNT":"34567","INCOMETYPE":"1","PRODCODE":"JADETYPE3","PRODRATIO":"0.33","INVESTDAYS":"14","TYPE":"2","PRODNAME":"JADETYPE3"}],"code":"0","msg":"查询成功"}
                try{
                    JSONObject res = new JSONObject(response);
                    ArrayList<CleverManamgerMoneyEntity> entities = new ArrayList<CleverManamgerMoneyEntity>();
                    if("0".equals(res.optString("code"))){
                        JSONArray data = res.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0;i < data.length();i++) {
                                JSONObject json = data.optJSONObject(i);
                                CleverManamgerMoneyEntity entity = new CleverManamgerMoneyEntity();
                                entity.setPRODNAME(json.optString("PRODNAME"));
                                entity.setPRODCODE(json.optString("PRODCODE"));
                                entity.setRISKLEVEL(json.optString("RISKLEVEL"));
                                entity.setPRODRATIO(json.optString("PRODRATIO"));
                                entity.setINVESTDAYS(json.optString("INVESTDAYS"));
                                entity.setBUY_LOW_AMOUNT(json.optString("BUY_LOW_AMOUNT"));
                                entity.setINCOMETYPE(json.optString("INCOMETYPE"));
                                entity.setPRODTYPE(json.optString("TYPE"));
                                entities.add(entity);
                            }
                        }
                        mCallbackResult.getResult(entities, TAG);
                    }else {
                        mCallbackResult.getResult(res.opt("msg"),TAG);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    mCallbackResult.getResult(e.toString(), TAG);
                }
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                try {
//
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//                    String code = "";
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
//                    }
//
//                    String msg = "";
//                    if (null != responseValues.get("msg")) {
//                        msg = String.valueOf(responseValues.get("msg"));
//                    }
//
//                    String totalcount = "";
//
//                    if (null != responseValues.get("totalcount")) {
//                        totalcount = String.valueOf(responseValues.get("totalcount"));
//                    }
//
//
//                    List<Object> data = new ArrayList<Object>();
//
//                    if (null != responseValues.get("data")) {
//                        data = (List<Object>) responseValues.get("data");
//                    }
//
//                    ArrayList<CleverManamgerMoneyEntity> entities = new ArrayList<CleverManamgerMoneyEntity>();
//                    if (data != null && data.size() > 0) {
//                        for (Object object : data) {
//                            CleverManamgerMoneyEntity entity = new CleverManamgerMoneyEntity();
//                            Map<String, Object> items = (Map<String, Object>) object;
//
//                            String RISKLEVEL = "";
//
//                            if (null != items.get("RISKLEVEL")) {
//                                RISKLEVEL = String.valueOf(items.get("RISKLEVEL"));
//                            }
//
//                            String BUY_LOW_AMOUNT = "";
//
//                            if (null != items.get("BUY_LOW_AMOUNT")) {
//                                BUY_LOW_AMOUNT = String.valueOf(items.get("BUY_LOW_AMOUNT"));
//                            }
//
//                            String INCOMETYPE = "";
//
//                            if (null != items.get("INCOMETYPE")) {
//                                INCOMETYPE = String.valueOf(items.get("INCOMETYPE"));
//                            }
//
//                            String PRODCODE = "";
//                            if (null != items.get("PRODCODE")) {
//                                PRODCODE = String.valueOf(items.get("PRODCODE"));
//                            }
//
//                            String PRODRATIO = "";
//                            if (null != items.get("PRODRATIO")) {
//                                PRODRATIO = String.valueOf(items.get("PRODRATIO"));
//                            }
//
//                            String INVESTDAYS ="";
//                            if (null != items.get("INVESTDAYS")) {
//                                INVESTDAYS = String.valueOf(items.get("INVESTDAYS"));
//                            }
//
//                            String TYPE = "";
//                            if (null != items.get("TYPE")) {
//                                TYPE = String.valueOf(items.get("TYPE"));
//                            }
//
//                            String PRODNAME = "";
//                            if (null != items.get("PRODNAME")) {
//                                PRODNAME = String.valueOf(items.get("PRODNAME"));
//                            }
//
//                            entity.setPRODNAME(PRODNAME);
//                            entity.setPRODCODE(PRODCODE);
//                            entity.setRISKLEVEL(RISKLEVEL);
//                            entity.setPRODRATIO(PRODRATIO);
//                            entity.setINVESTDAYS(INVESTDAYS);
//                            entity.setBUY_LOW_AMOUNT(BUY_LOW_AMOUNT);
//                            entity.setINCOMETYPE(INCOMETYPE);
//                            entity.setPRODTYPE(TYPE);
//                            entities.add(entity);
//                        }
//                    }
//                    mCallbackResult.getResult(entities, TAG);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    mCallbackResult.getResult(e.toString(), TAG);
//                }
            }
        });
    }

    private class HotProductEntity {
        private String totalcount;
        private String msg;
        private String code;
        private List<SubHotProductBean> data;

        public String getTotalcount() {
            return totalcount;
        }

        public void setTotalcount(String totalcount) {
            this.totalcount = totalcount;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<SubHotProductBean> getData() {
            return data;
        }

        public void setData(List<SubHotProductBean> data) {
            this.data = data;
        }
    }

    private class SubHotProductBean {
        private String RISKLEVEL;       //风险等级（整型数字）0:默认等级 1:保本等级 2:低风险等级 3:中风险等级 4:高风险等级
        private String BUY_LOW_AMOUNT;  //起购金额
        private String INCOMETYPE;      //收益类型（如果是OTC产品，此值为1固定2浮动3固定+浮动，其他产品为-1）
        private String PRODCODE;        //产品代码
        private String PRODRATIO;       //收益率（如果是14天理财类产品，为其“业绩比较基准”字段值）
        private String INVESTDAYS;      //投资期限（整数，天）
        private String TYPE;            //产品大类， 1-开放式基金2-OTC 3-14天现金增益
        private String PRODNAME;        //产品名称

        public String getRISKLEVEL() {
            return RISKLEVEL;
        }

        public void setRISKLEVEL(String RISKLEVEL) {
            this.RISKLEVEL = RISKLEVEL;
        }

        public String getBUY_LOW_AMOUNT() {
            return BUY_LOW_AMOUNT;
        }

        public void setBUY_LOW_AMOUNT(String BUY_LOW_AMOUNT) {
            this.BUY_LOW_AMOUNT = BUY_LOW_AMOUNT;
        }

        public String getINCOMETYPE() {
            return INCOMETYPE;
        }

        public void setINCOMETYPE(String INCOMETYPE) {
            this.INCOMETYPE = INCOMETYPE;
        }

        public String getPRODCODE() {
            return PRODCODE;
        }

        public void setPRODCODE(String PRODCODE) {
            this.PRODCODE = PRODCODE;
        }

        public String getPRODRATIO() {
            return PRODRATIO;
        }

        public void setPRODRATIO(String PRODRATIO) {
            this.PRODRATIO = PRODRATIO;
        }

        public String getINVESTDAYS() {
            return INVESTDAYS;
        }

        public void setINVESTDAYS(String INVESTDAYS) {
            this.INVESTDAYS = INVESTDAYS;
        }

        public String getTYPE() {
            return TYPE;
        }

        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
        }

        public String getPRODNAME() {
            return PRODNAME;
        }

        public void setPRODNAME(String PRODNAME) {
            this.PRODNAME = PRODNAME;
        }
    }

}
