package com.tpyzq.mobile.pangu.http.doConnect.self;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.panguutil.AddPosition;
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
 * Created by zhangwenbo on 2016/9/19.
 * 持仓网络连接
 */
public class HoldCloudConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "HoldCloudConnect";
    private String mHttpTAG;
    private String mToken;

    public HoldCloudConnect(String httpTag, String token) {
        mHttpTAG = httpTag;
        mToken = token;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request();
    }

    private void request() {
        Map map1 = new HashMap();
        map1.put("funcid", "300201");
        map1.put("token", mToken);

        Map map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                mCallbackResult.getResult("网络异常", TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    if("0".equals(res.optString("code"))){
                        ArrayList<StockInfoEntity> stockInfoEntities = new ArrayList<StockInfoEntity>();
                        JSONArray jsonArray = res.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            for(int i = 0 ; i < jsonArray.length();i++){
                                JSONObject json = jsonArray.getJSONObject(i);
//                                String MKT_VAL = json.optString("MKT_VAL");
//                                String DRAW_AMT = json.optString("DRAW_AMT");
//                                String TOTAL_INCOME_BAL =json.optString("TOTAL_INCOME_BAL");
//                                String AVAILABLE = json.optString("AVAILABLE");
//                                String ASSERT_VAL = json.optString("ASSERT_VAL");
                                JSONArray array =  json.getJSONArray("INCOME_LIST");
                                if(null != array && array.length() > 0){
                                    AddPosition.getInstance().setData(TAG,array);
                                    for(int j = 0;j <array.length();j++){
                                        JSONObject jsonObject = array.getJSONObject(j);
                                        StockInfoEntity stockInfoEntity = new StockInfoEntity();
                                        stockInfoEntity.setMKT_VAL(jsonObject.optString("MKT_VAL")); //市值
                                        stockInfoEntity.setSECU_ACC(jsonObject.optString("SECU_ACC"));//股东代码
                                        stockInfoEntity.setSHARE_BLN(jsonObject.optString("SECU_ACC"));//股份余额
                                        stockInfoEntity.setSHARE_AVL(jsonObject.optString("SHARE_AVL")); //股份可卖数量(可用数量)
                                        stockInfoEntity.setSHARE_QTY(jsonObject.optString("SHARE_QTY"));//股份总数量(持有数量)
                                        stockInfoEntity.setPROFIT_RATIO(jsonObject.optString("PROFIT_RATIO"));//盈亏比
                                        String market = jsonObject.optString("MARKET");
                                        stockInfoEntity.setMARKET(market);//交易市场
                                        stockInfoEntity.setBRANCH(jsonObject.optString("BRANCH")); //分支机构
                                        stockInfoEntity.setMKT_PRICE(jsonObject.optString("MKT_PRICE")); //现价
                                        stockInfoEntity.setINCOME_AMT(jsonObject.optString("INCOME_AMT")); //盈亏金额
                                        stockInfoEntity.setSECU_NAME(jsonObject.optString("SECU_NAME"));//证券名字
                                        stockInfoEntity.setStockName(jsonObject.optString("SECU_NAME"));
                                        stockInfoEntity.setNewPrice("0.0");
                                        stockInfoEntity.setCURRENT_COST(jsonObject.optString("CURRENT_COST"));  //当前成本价
                                        stockInfoEntity.setApperHoldStock("true");
                                        stockInfoEntity.setIsHoldStock("true");
                                        stockInfoEntity.setApperHoldStock("true");
                                        if ("1".equals(market)) {
                                            String stockNumber = Helper.getStockCode(jsonObject.optString("SECU_CODE"), "83");
                                            stockInfoEntity.setSECU_CODE(stockNumber);//证券代码
                                            stockInfoEntity.setStockNumber(stockNumber);
                                        } else if ("2".equals(market)) {
                                            String stockNumber = Helper.getStockCode(jsonObject.optString("SECU_CODE"), "90");
                                            stockInfoEntity.setSECU_CODE(stockNumber);//证券代码
                                            stockInfoEntity.setStockNumber(stockNumber);
                                        }
                                        stockInfoEntities.add(stockInfoEntity);
                                    }
                                }else{
                                    mCallbackResult.getResult("导入持仓暂无数据", TAG);
                                }
                            }
                        }
                        mCallbackResult.getResult(stockInfoEntities, TAG);
                    }else {
                        mCallbackResult.getResult(res.optString("msg"), TAG);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    mCallbackResult.getResult("报文解析异常", TAG);
                }
//                response = "{\"code\":\"0\",\"msg\":\"(持仓查询成功)\",\"data\":[{\"INCOME_LIST\":[{\"MKT_VAL\":\"32699.00\",\"SECU_ACC\":\"A413454911\",\"SHARE_BLN\":\"1900.00\",\"SHARE_AVL\":\"0\",\"SHARE_QTY\":\"1900.00\",\"PROFIT_RATIO\":\"2.93\",\"MARKET\":\"1\",\"BRANCH\":\"\",\"MKT_PRICE\":\"17.2100\",\"SECU_CODE\":\"600030\",\"INCOME_AMT\":\"864.78\",\"SECU_NAME\":\"中信证券\",\"CURRENT_COST\":\"16.720\"},{\"MKT_VAL\":\"28429632.00\",\"SECU_ACC\":\"0031025865\",\"SHARE_BLN\":\"1025600.00\",\"SHARE_AVL\":\"0\",\"SHARE_QTY\":\"1025600.00\",\"PROFIT_RATIO\":\"6.49\",\"MARKET\":\"2\",\"BRANCH\":\"\",\"MKT_PRICE\":\"27.7200\",\"SECU_CODE\":\"000002\",\"INCOME_AMT\":\"1674929.86\",\"SECU_NAME\":\"万 科Ａ\",\"CURRENT_COST\":\"26.031\"},{\"MKT_VAL\":\"3968.80\",\"SECU_ACC\":\"0031025865\",\"SHARE_BLN\":\"4100.00\",\"SHARE_AVL\":\"0\",\"SHARE_QTY\":\"4100.00\",\"PROFIT_RATIO\":\"-3.39\",\"MARKET\":\"2\",\"BRANCH\":\"\",\"MKT_PRICE\":\"0.9680\",\"SECU_CODE\":\"165521\",\"INCOME_AMT\":\"-146.50\",\"SECU_NAME\":\"信诚金融\",\"CURRENT_COST\":\"1.002\"}],\"MKT_VAL\":\"28466299.80\",\"DRAW_AMT\":\"804638017.04\",\"TOTAL_INCOME_BAL\":\"1675648.14\",\"AVAILABLE\":\"804638017.04\",\"ASSERT_VAL\":\"1001755591.43\"}]}";

//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                try {
//
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//                    String code = "";
//
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
//                    }
//
//
//                    String msg = "";
//                    if (null != responseValues.get("msg")) {
//                        msg = String.valueOf(responseValues.get("msg"));
//                    }
//
//                    if (!TextUtils.isEmpty(code) && !"0".equals(code)) {
//                        mCallbackResult.getResult(msg, TAG);
//                        return ;
//                    }
//
//                    if (!(responseValues.get("data") instanceof List)) {
//                        Helper.getInstance().showToast(CustomApplication.getContext(), "导入持仓网络接口json类型服务器更改");
//                        return;
//                    }
//
//                    List<Object> data = (List<Object>) responseValues.get("data");
//                    ArrayList<StockInfoEntity> stockInfoEntities = new ArrayList<StockInfoEntity>();
//                    if (data == null || data.size() <= 0) {
//                        mCallbackResult.getResult(stockInfoEntities, TAG);
//                        return;
//                    }
//
//                    for (Object responseObj : data) {
//
//                        if (!(responseObj instanceof Map)) {
//                            Helper.getInstance().showToast(CustomApplication.getContext(), "导入持仓网络连接数据类型错误");
//                            return;
//                        }
//
//                        Map<String, Object> subResponseObj = (Map<String, Object>) responseObj;
//
//                        String MKT_VAL = "";
//                        if (null != subResponseObj.get("MKT_VAL")) {
//                            MKT_VAL = String.valueOf(subResponseObj.get("MKT_VAL"));
//                        }
//
//                        String DRAW_AMT = "";
//                        if (null != subResponseObj.get("DRAW_AMT")) {
//                            DRAW_AMT = String.valueOf(subResponseObj.get("DRAW_AMT"));
//                        }
//
//                        String TOTAL_INCOME_BAL = "";
//                        if (null != subResponseObj.get("TOTAL_INCOME_BAL")) {
//                            TOTAL_INCOME_BAL = String.valueOf(subResponseObj.get("TOTAL_INCOME_BAL"));
//                        }
//
//                        String AVAILABLE = "";
//                        if (null != subResponseObj.get("AVAILABLE")) {
//                            AVAILABLE = String.valueOf(subResponseObj.get("AVAILABLE"));
//                        }
//
//                        String ASSERT_VAL = "";
//                        if (null != subResponseObj.get("ASSERT_VAL")) {
//                            ASSERT_VAL = String.valueOf(subResponseObj.get("ASSERT_VAL"));
//                        }
//
//                        List<Map<String, Object>> INCOME_LIST = null;
//                        if (null != subResponseObj.get("INCOME_LIST") && subResponseObj.get("INCOME_LIST") instanceof List) {
//                            INCOME_LIST = (List<Map<String,Object>>) subResponseObj.get("INCOME_LIST");
//
//                            AddPosition.getInstance().setData(TAG,INCOME_LIST);
//                        }
//
//                        if (null == INCOME_LIST || INCOME_LIST.size() <= 0) {
//                            mCallbackResult.getResult("导入持仓暂无数据", TAG);
//                            return;
//                        }
//
//                        for (Map<String, Object> map : INCOME_LIST) {
//
//                            StockInfoEntity stockInfoEntity = new StockInfoEntity();
//
//                            //市值
//                            String sub_MKT_VAL = "";
//                            if (null != map.get("MKT_VAL")) {
//                                sub_MKT_VAL = String.valueOf(map.get("MKT_VAL"));
//                            }
//
//                            //股东代码
//                            String SECU_ACC = "";
//                            if (null != map.get("SECU_ACC")) {
//                                SECU_ACC = String.valueOf(map.get("SECU_ACC"));
//                            }
//
//                            //股份余额
//                            String SHARE_BLN = "";
//                            if (null != map.get("SHARE_BLN")) {
//                                SHARE_BLN = String.valueOf(map.get("SECU_ACC"));
//                            }
//
//                            //股份可卖数量(可用数量)
//                            String SHARE_AVL = "";
//                            if (null != map.get("SHARE_AVL")) {
//                                SHARE_AVL = String.valueOf(map.get("SHARE_AVL"));
//                            }
//
//                            //股份总数量(持有数量)
//                            String SHARE_QTY = "";
//                            if (null != map.get("SHARE_QTY")) {
//                                SHARE_QTY = String.valueOf(map.get("SHARE_QTY"));
//                            }
//
//                            //盈亏比
//                            String PROFIT_RATIO = "";
//                            if (null != map.get("PROFIT_RATIO")) {
//                                PROFIT_RATIO = String.valueOf(map.get("PROFIT_RATIO"));
//                            }
//
//                            //交易市场
//                            String MARKET = "";
//                            if (null != map.get("MARKET")) {
//                                MARKET = String.valueOf(map.get("MARKET"));
//                            }
//
//                            //分支机构
//                            String BRANCH = "";
//                            if (null != map.get("BRANCH")) {
//                                BRANCH = String.valueOf(map.get("BRANCH"));
//                            }
//
//                            //现价
//                            String MKT_PRICE = "";
//                            if (null != map.get("MKT_PRICE")) {
//                                MKT_PRICE = String.valueOf(map.get("MKT_PRICE"));
//                            }
//
//                            //证券代码
//                            String SECU_CODE = "";
//                            if (null != map.get("SECU_CODE")) {
//                                SECU_CODE = String.valueOf(map.get("SECU_CODE"));
//                            }
//
//                            //盈亏金额
//                            String INCOME_AMT = "";
//                            if (null != map.get("INCOME_AMT")) {
//                                INCOME_AMT = String.valueOf(map.get("INCOME_AMT"));
//                            }
//
//                            //证券名字
//                            String SECU_NAME = "";
//                            if (null != map.get("SECU_NAME")) {
//                                SECU_NAME = String.valueOf(map.get("SECU_NAME"));
//                            }
//
//                            //当前成本价
//                            String CURRENT_COST = "";
//                            if (null != map.get("CURRENT_COST")) {
//                                CURRENT_COST = String.valueOf(map.get("CURRENT_COST"));
//                            }
//
//                            stockInfoEntity.setMKT_VAL(sub_MKT_VAL); //市值
//                            stockInfoEntity.setSECU_ACC(SECU_ACC);//股东代码
//                            stockInfoEntity.setSHARE_BLN(SHARE_BLN);//股份余额
//                            stockInfoEntity.setSHARE_AVL(SHARE_AVL); //股份可卖数量(可用数量)
//                            stockInfoEntity.setSHARE_QTY(SHARE_QTY);//股份总数量(持有数量)
//                            stockInfoEntity.setPROFIT_RATIO(PROFIT_RATIO);//盈亏比
//                            stockInfoEntity.setMARKET(MARKET);//交易市场
//                            stockInfoEntity.setBRANCH(BRANCH); //分支机构
//                            stockInfoEntity.setMKT_PRICE(MKT_PRICE); //现价
//                            stockInfoEntity.setINCOME_AMT(INCOME_AMT); //盈亏金额
//                            stockInfoEntity.setSECU_NAME(SECU_NAME);//证券名字
//                            stockInfoEntity.setStockName(SECU_NAME);
//                            stockInfoEntity.setNewPrice("0.0");
//                            stockInfoEntity.setCURRENT_COST(CURRENT_COST);  //当前成本价
//                            stockInfoEntity.setApperHoldStock("true");
//                            stockInfoEntity.setIsHoldStock("true");
//                            stockInfoEntity.setApperHoldStock("true");
//
//                            if ("1".equals(MARKET)) {
//
//                                String stockNumber = Helper.getStockCode(SECU_CODE, "83");
//                                stockInfoEntity.setSECU_CODE(stockNumber);//证券代码
//                                stockInfoEntity.setStockNumber(stockNumber);
//
//                            } else if ("2".equals(MARKET)) {
//                                String stockNumber = Helper.getStockCode(SECU_CODE, "90");
//                                stockInfoEntity.setSECU_CODE(stockNumber);//证券代码
//                                stockInfoEntity.setStockNumber(stockNumber);
//                            }
//
//                            stockInfoEntities.add(stockInfoEntity);
//                        }
//
//                        mCallbackResult.getResult(stockInfoEntities, TAG);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    mCallbackResult.getResult("json解析错误", TAG);
//                }
//
//                //{"code":"0","msg":"(持仓查询成功)","data":[{"INCOME_LIST":[{"MKT_VAL":"1117.50","SECU_ACC":"A768059900","SHARE_BLN":"500.00","SHARE_AVL":"0","SHARE_QTY":"500.00","PROFIT_RATIO":"-2.27","MARKET":"1","BRANCH":"","MKT_PRICE":"2.2350","SECU_CODE":"510050","INCOME_AMT":"-31.20","SECU_NAME":"50ETF","CURRENT_COST":"2.287"},{"MKT_VAL":"138175124.00","SECU_ACC":"A768059900","SHARE_BLN":"9998200.00","SHARE_AVL":"9975100.00","SHARE_QTY":"9998200.00","PROFIT_RATIO":"1284.77","MARKET":"1","BRANCH":"","MKT_PRICE":"13.8200","SECU_CODE":"600004","INCOME_AMT":"128025397.90","SECU_NAME":"白云机场","CURRENT_COST":"0.998"},{"MKT_VAL":"1420.00","SECU_ACC":"A768059900","SHARE_BLN":"100.00","SHARE_AVL":"0","SHARE_QTY":"100.00","PROFIT_RATIO":"2.52","MARKET":"1","BRANCH":"","MKT_PRICE":"14.2000","SECU_CODE":"600216","INCOME_AMT":"28.32","SECU_NAME":"浙江医药","CURRENT_COST":"13.851"},{"MKT_VAL":"89300000.00","SECU_ACC":"A768059900","SHARE_BLN":"10000000.00","SHARE_AVL":"10000000.00","SHARE_QTY":"10000000.00","PROFIT_RATIO":"793.00","MARKET":"1","BRANCH":"","MKT_PRICE":"8.9300","SECU_CODE":"601002","INCOME_AMT":"79186588.90","SECU_NAME":"晋亿实业","CURRENT_COST":"1.000"},{"MKT_VAL":"3413520.00","SECU_ACC":"A768059900","SHARE_BLN":"862000.00","SHARE_AVL":"0","SHARE_QTY":"862000.00","PROFIT_RATIO":"-4.83","MARKET":"1","BRANCH":"","MKT_PRICE":"3.9600","SECU_CODE":"601518","INCOME_AMT":"-177703.57","SECU_NAME":"吉林高速","CURRENT_COST":"4.161"},{"MKT_VAL":"213600000.00","SECU_ACC":"A768059900","SHARE_BLN":"10000000.00","SHARE_AVL":"10000000.00","SHARE_QTY":"10000000.00","PROFIT_RATIO":"2036.00","MARKET":"1","BRANCH":"","MKT_PRICE":"21.3600","SECU_CODE":"603003","INCOME_AMT":"203328727.90","SECU_NAME":"龙宇燃油","CURRENT_COST":"1.000"},{"MKT_VAL":"90700907.00","SECU_ACC":"0163106142","SHARE_BLN":"10000100.00","SHARE_AVL":"10000000.00","SHARE_QTY":"10000100.00","PROFIT_RATIO":"807.00","MARKET":"2","BRANCH":"","MKT_PRICE":"9.0700","SECU_CODE":"000001","INCOME_AMT":"80586578.66","SECU_NAME":"平安银行","CURRENT_COST":"1.000"},{"MKT_VAL":"2617.00","SECU_ACC":"0163106142","SHARE_BLN":"100.00","SHARE_AVL":"0","SHARE_QTY":"100.00","PROFIT_RATIO":"10.51","MARKET":"2","BRANCH":"","MKT_PRICE":"26.1700","SECU_CODE":"000002","INCOME_AMT":"241.18","SECU_NAME":"万  科Ａ","CURRENT_COST":"23.681"},{"MKT_VAL":"478.00","SECU_ACC":"0163106142","SHARE_BLN":"100.00","SHARE_AVL":"0","SHARE_QTY":"100.00","PROFIT_RATIO":"-8.09","MARKET":"2","BRANCH":"","MKT_PRICE":"4.7800","SECU_CODE":"000016","INCOME_AMT":"-47.68","SECU_NAME":"深康佳Ａ","CURRENT_COST":"5.201"},{"MKT_VAL":"1923.00","SECU_ACC":"0163106142","SHARE_BLN":"100.00","SHARE_AVL":"0","SHARE_QTY":"100.00","PROFIT_RATIO":"-1.39","MARKET":"2","BRANCH":"","MKT_PRICE":"19.2300","SECU_CODE":"000501","INCOME_AMT":"-34.12","SECU_NAME":"鄂武商Ａ","CURRENT_COST":"19.501"},{"MKT_VAL":"3224.00","SECU_ACC":"0163106142","SHARE_BLN":"400.00","SHARE_AVL":"0","SHARE_QTY":"400.00","PROFIT_RATIO":"-13.82","MARKET":"2","BRANCH":"","MKT_PRICE":"8.0600","SECU_CODE":"000758","INCOME_AMT":"-525.42","SECU_NAME":"中色股份","CURRENT_COST":"9.353"},{"MKT_VAL":"156500000.00","SECU_ACC":"0163106142","SHARE_BLN":"10000000.00","SHARE_AVL":"10000000.00","SHARE_QTY":"10000000.00","PROFIT_RATIO":"1465.00","MARKET":"2","BRANCH":"","MKT_PRICE":"15.6500","SECU_CODE":"002142","INCOME_AMT":"146304374.90","SECU_NAME":"宁波银行","CURRENT_COST":"1.000"},{"MKT_VAL":"2924.00","SECU_ACC":"0163106142","SHARE_BLN":"400.00","SHARE_AVL":"0","SHARE_QTY":"400.00","PROFIT_RATIO":"-7.97","MARKET":"2","BRANCH":"","MKT_PRICE":"7.3100","SECU_CODE":"002385","INCOME_AMT":"-261.12","SECU_NAME":"大北农","CURRENT_COST":"7.943"},{"MKT_VAL":"3702.00","SECU_ACC":"0163106142","SHARE_BLN":"200.00","SHARE_AVL":"0","SHARE_QTY":"200.00","PROFIT_RATIO":"-2.35","MARKET":"2","BRANCH":"","MKT_PRICE":"18.5100","SECU_CODE":"300003","INCOME_AMT":"-97.90","SECU_NAME":"乐普医疗","CURRENT_COST":"18.956"},{"MKT_VAL":"442500000.00","SECU_ACC":"0163106142","SHARE_BLN":"10000000.00","SHARE_AVL":"10000000.00","SHARE_QTY":"10000000.00","PROFIT_RATIO":"4325.00","MARKET":"2","BRANCH":"","MKT_PRICE":"44.2500","SECU_CODE":"300104","INCOME_AMT":"431946874.90","SECU_NAME":"乐视网","CURRENT_COST":"1.000"}],"MKT_VAL":"1134206956.5","DRAW_AMT":"9945612.63","TOTAL_INCOME_BAL":"1069200111.64","AVAILABLE":"9945612.63","ASSERT_VAL":"1227150173.38"}]}
//                //{"code":"-6","msg":"session失效","data":[]}
//
            }
        });

    }

    private class HoldEntitiy {
        private String code;
        private String msg;
        private List<HoldData> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<HoldData> getData() {
            return data;
        }

        public void setData(List<HoldData> data) {
            this.data = data;
        }
    }

    private class HoldData {
        private List<HoldList> INCOME_LIST;

        public List<HoldList> getINCOME_LIST() {
            return INCOME_LIST;
        }

        public void setINCOME_LIST(List<HoldList> INCOME_LIST) {
            this.INCOME_LIST = INCOME_LIST;
        }
    }

    private class HoldList {
        private String MKT_VAL;             //市值
        private String SECU_ACC;            //股东代码
        private String SHARE_BLN;           //股份余额
        private String SHARE_AVL;           //股份可卖数量(可用数量)
        private String SHARE_QTY;           //股份总数量(持有数量)
        private String PROFIT_RATIO;        //盈亏比
        private String MARKET;              //交易市场
        private String BRANCH;              //分支机构
        private String MKT_PRICE;           //现价
        private String SECU_CODE;           //证券代码
        private String INCOME_AMT;          //盈亏金额
        private String SECU_NAME;           //证券代码
        private String CURRENT_COST;        //当前成本价

        public String getMKT_VAL() {
            return MKT_VAL;
        }

        public void setMKT_VAL(String MKT_VAL) {
            this.MKT_VAL = MKT_VAL;
        }

        public String getSECU_ACC() {
            return SECU_ACC;
        }

        public void setSECU_ACC(String SECU_ACC) {
            this.SECU_ACC = SECU_ACC;
        }

        public String getSHARE_BLN() {
            return SHARE_BLN;
        }

        public void setSHARE_BLN(String SHARE_BLN) {
            this.SHARE_BLN = SHARE_BLN;
        }

        public String getSHARE_AVL() {
            return SHARE_AVL;
        }

        public void setSHARE_AVL(String SHARE_AVL) {
            this.SHARE_AVL = SHARE_AVL;
        }

        public String getSHARE_QTY() {
            return SHARE_QTY;
        }

        public void setSHARE_QTY(String SHARE_QTY) {
            this.SHARE_QTY = SHARE_QTY;
        }

        public String getPROFIT_RATIO() {
            return PROFIT_RATIO;
        }

        public void setPROFIT_RATIO(String PROFIT_RATIO) {
            this.PROFIT_RATIO = PROFIT_RATIO;
        }

        public String getMARKET() {
            return MARKET;
        }

        public void setMARKET(String MARKET) {
            this.MARKET = MARKET;
        }

        public String getBRANCH() {
            return BRANCH;
        }

        public void setBRANCH(String BRANCH) {
            this.BRANCH = BRANCH;
        }

        public String getMKT_PRICE() {
            return MKT_PRICE;
        }

        public void setMKT_PRICE(String MKT_PRICE) {
            this.MKT_PRICE = MKT_PRICE;
        }

        public String getSECU_CODE() {
            return SECU_CODE;
        }

        public void setSECU_CODE(String SECU_CODE) {
            this.SECU_CODE = SECU_CODE;
        }

        public String getINCOME_AMT() {
            return INCOME_AMT;
        }

        public void setINCOME_AMT(String INCOME_AMT) {
            this.INCOME_AMT = INCOME_AMT;
        }

        public String getSECU_NAME() {
            return SECU_NAME;
        }

        public void setSECU_NAME(String SECU_NAME) {
            this.SECU_NAME = SECU_NAME;
        }

        public String getCURRENT_COST() {
            return CURRENT_COST;
        }

        public void setCURRENT_COST(String CURRENT_COST) {
            this.CURRENT_COST = CURRENT_COST;
        }
    }

}
