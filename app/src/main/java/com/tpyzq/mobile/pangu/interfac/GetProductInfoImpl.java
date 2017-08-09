package com.tpyzq.mobile.pangu.interfac;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/4/14.
 */

public class GetProductInfoImpl implements IGetProductInfo {

    private static final String TAG = GetProductInfoImpl.class.getSimpleName();

    @Override
    public void getProductInfo(String token, String productCode, final IGetProductInfoResult iGetProductInfoResult) {
        Map params = new HashMap();
        params.put("FUNCTIONCODE", "HQTNG105");
        params.put("TOKEN", token);

        final Map map = new HashMap();
        map.put("prod_code", productCode);

        params.put("PARAMS", map);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_HQ_WB(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                iGetProductInfoResult.getProductInfoResultError("网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
//{"message":[{"BUY_LOW_AMOUNT":"5万","RETAINCHAR1":"","RETAINCHAR2":"","INVEST_TYPE":"固定收益类","RETAINCHAR5":"","INTERESTDAY":"2017-05-09","RETAINCHAR6":"","RETAINCHAR3":"","RETAINCHAR4":"","PRODTYPE":"货币型基金","COMPREF":"18.00","IPO_END_DATE":"2017-05-29","SUBNAME":"太牛犇预约测试0001","PRODRATIO":"","INVESTDAYS":"21","TYPE":"3","RISKLEVEL":"默认等级","TIP":"测试","PAYDAY":"","DESCRIPTION":"","INCOME_TYPE":"保本固定收益","PRODCODE":"SU0508","PUBCOMPANY":"1","REVENUERULE":"","CREAT_TIME":"2017-06-09","PRODNAME":"太牛犇预约测试0001","PROD_STATUS":"预约认购期","INCOMETYPE":"保本固定收益","OTCNOTICEDATE":[{"filename":"太牛犇2号 产品说明书.pdf","fileurl":"http://tnhq.tpyzq.com:80/prodfile/1496369151943.pdf"},{"filename":"太牛犇2号 风险揭示书.pdf","fileurl":"http://tnhq.tpyzq.com:80/prodfile/1496369158826.pdf"},{"filename":"太牛犇2号 客户认购协议书.pdf","fileurl":"http://tnhq.tpyzq.com:80/prodfile/1496369165605.pdf"}],"IPO_BEGIN_DATE":"2017-05-08","ENDDAY":"2017-05-30","ORDER_PROD_STATUS":"3"}],"code":"200","type":"success"}
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                LogUtil.e("---------sss:"+response);
                try{
                    JSONObject res = new JSONObject(response);
                    if("200".equals(res.optString("code"))){
                        JSONArray array = res.getJSONArray("message");
                        String order_prod_status = "";
                        if(null != array && array.length() > 0){
                            JSONObject json = array.getJSONObject(0);
                            order_prod_status  = json.optString("ORDER_PROD_STATUS");
                        }
                        iGetProductInfoResult.getProductInfoResultSuccess(order_prod_status);
                    } else {
                        iGetProductInfoResult.getProductInfoResultError(res.optString("type"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    iGetProductInfoResult.getProductInfoResultError("报文解析异常");
                }
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//
//                try {
//
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//                    String code = "";
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
//                    }
//
//                    String type = "";
//                    if (null != responseValues.get("type")) {
//                        type = String.valueOf(responseValues.get("type"));
//                    }
//
//                    if (!"200".equals(code)) {
//                        iGetProductInfoResult.getProductInfoResultError(type);
//                        return;
//                    }
//                    String ORDER_PROD_STATUS = "";
//
//                    Object object = responseValues.get("message");
//
//                    if (null != object && object instanceof ArrayList) {
//                        List<Object> datas = (List<Object>) object;
//
//                        if (datas != null && datas.size() > 0) {
//                            for (Object subObj : datas) {
//                                Map<String, Object> map1 = (Map<String, Object>) subObj;
//
//                                if (null != map1.get("ORDER_PROD_STATUS")) {
//                                    ORDER_PROD_STATUS = String.valueOf(map1.get("ORDER_PROD_STATUS"));
//                                }
//
//                            }
//                        }
//                    }
//
//                    iGetProductInfoResult.getProductInfoResultSuccess(ORDER_PROD_STATUS);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    iGetProductInfoResult.getProductInfoResultError(e.toString());
//                }
            }
        });
    }
}
