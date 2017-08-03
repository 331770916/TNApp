package com.tpyzq.mobile.pangu.http.doConnect.detail;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
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
 * Created by zhangwenbo on 2016/11/30.
 * 搜索股票网络连接
 */

public class GetSearchStockConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetSearchStockConnect";
    private String mHttpTAG;
    private String mHoldStockCodes;
    private String mStockNumber;
    private String mStartNumber;
    public GetSearchStockConnect(String httpTag, String stockNumber, String startNumber) {
        mHttpTAG = httpTag;
        mHoldStockCodes = HOLD_SEQ.getHoldCodes();
        mStockNumber = stockNumber;
        mStartNumber = startNumber;
    }

    public void doConnect(ICallbackResult callbackResult){
        mCallbackResult = callbackResult;
        request(mStockNumber, mStartNumber);
    }

    public void request(String stockInfo, String startNumber) {
        Map map = new HashMap();

        try {
//            SearchStockParams searchStockParams = new SearchStockParams();
//
//            searchStockParams.setNum("30");
//
//            searchStockParams.setArg(stockInfo);
//
//            searchStockParams.setStart(startNumber);
//
//            ArrayList<SearchStockParams> object = new ArrayList<SearchStockParams>(1);
//            object.add(searchStockParams);
            Map map1 = new HashMap();
            map1.put("num","30");
            map1.put("start",startNumber);
            map1.put("arg",stockInfo);
            Object[] obj = new Object[1];
            obj[0] = map1;
            Gson gson = new Gson();
            String strJson = gson.toJson(obj);
            map.put("PARAMS", strJson);
            map.put("FUNCTIONCODE", "HQING006");

        } catch (Exception e) {
            e.printStackTrace();
        }

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), map, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                mCallbackResult.getResult("-1", TAG);
            }

            @Override
            public void onResponse(String response, int id) {
//                response = "[{\"bytes\":28,\"totalCount\":\"30\",\"data\":[[\"21000002\",\"万  科Ａ\",\"27.57\",\"25.06\"],[\"21000006\",\"深振业Ａ\",\"8.72\",\"8.55\"],[\"21000007\",\"全新好\",\"19.15\",\"18.38\"],[\"21000010\",\"美丽生态\",\"7.75\",\"7.64\"],[\"21000018\",\"神州长城\",\"11.06\",\"10.88\"],[\"21000029\",\"深深房Ａ\",\"11.94\",\"11.56\"],[\"21000034\",\"神州数码\",\"29.62\",\"29.13\"],[\"21000035\",\"中国天楹\",\"6.82\",\"6.75\"],[\"21000049\",\"德赛电池\",\"38.1\",\"37.93\"],[\"21000060\",\"中金岭南\",\"11.33\",\"11.15\"],[\"21000063\",\"中兴通讯\",\"15.16\",\"15.18\"],[\"21000066\",\"长城电脑\",\"12.19\",\"12.17\"],[\"21000069\",\"华侨城Ａ\",\"7.3\",\"7.51\"],[\"21000099\",\"中信海直\",\"13.4\",\"13.27\"],[\"21000159\",\"国际实业\",\"7.43\",\"7.37\"],[\"21000404\",\"华意压缩\",\"10.37\",\"10.31\"],[\"21000420\",\"吉林化纤\",\"7.03\",\"7.0\"],[\"21000426\",\"兴业矿业\",\"10.95\",\"10.56\"],[\"21000488\",\"晨鸣纸业\",\"9.31\",\"9.22\"],[\"21000506\",\"中润资源\",\"8.12\",\"8.07\"],[\"21000509\",\"华塑控股\",\"6.24\",\"5.98\"],[\"21000510\",\"金路集团\",\"8.19\",\"8.08\"],[\"21000532\",\"力合股份\",\"16.64\",\"16.62\"],[\"21000536\",\"华映科技\",\"15.04\",\"15.01\"],[\"21000548\",\"湖南投资\",\"8.19\",\"8.13\"],[\"21000559\",\"万向钱潮\",\"15.41\",\"15.38\"],[\"21000572\",\"海马汽车\",\"5.21\",\"5.2\"],[\"21000576\",\"广东甘化\",\"14.01\",\"14.0\"],[\"21000592\",\"平潭发展\",\"8.57\",\"8.5\"],[\"21000605\",\"渤海股份\",\"21.02\",\"21.02\"]],\"code\":\"0\"}]";
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
                try{
                    JSONArray res = new JSONArray(response);
                    JSONObject jsonObject = res.getJSONObject(0);
                    ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                    if("0".equals(jsonObject.optString("code"))){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            for(int i = 0;i < jsonArray.length();i++){
                                JSONArray array = jsonArray.getJSONArray(i);
                                StockInfoEntity _bean = new StockInfoEntity();
                                _bean.setTotalCount(jsonObject.optString("totalCount"));
                                _bean.setStockNumber(array.getString(0));
                                _bean.setStockName(array.getString(1));
                                if ("-".equals(array.getString(2))) {
                                    _bean.setNewPrice("0");
                                } else {
                                    _bean.setNewPrice(array.getString(2));
                                }
                                if ("-".equals(array.getString(3))) {
                                    _bean.setClose("0");
                                } else {
                                    _bean.setClose(array.getString(3));
                                }
                                if ("-".equals(array.getString(4))) {
                                    _bean.setHot("0");
                                } else {
                                    _bean.setHot(array.getString(4));
                                }
                                //处理涨跌幅和涨跌值
                                double price = Double.parseDouble(_bean.getNewPrice());
                                double close = Double.parseDouble(_bean.getClose());

                                double zdz = price - close;
                                double zdf = zdz / close;

                                _bean.setUpAndDownValue(zdz);
                                _bean.setPriceChangeRatio(zdf);

                                if (!TextUtils.isEmpty(mHoldStockCodes) && mHoldStockCodes.contains(_bean.getStockNumber())) {
                                    _bean.setStockholdon("true");
                                }
                                //判断是不是自选股
                                StockInfoEntity tempBean = Db_PUB_STOCKLIST.queryStockFromID(_bean.getStockNumber());
                                if (tempBean != null) {
                                    _bean.setSelfChoicStock(true);

                                    if ((tempBean.getStock_flag()&2) == 2) {
                                        _bean.setIsHoldStock("true");
                                        _bean.setApperHoldStock("true");
                                    }

                                } else {
                                    _bean.setSelfChoicStock(false);
                                }
                                beans.add(_bean);
                            }
                        }
                        mCallbackResult.getResult(beans, TAG);
                    }else{
                        mCallbackResult.getResult("无数据", TAG);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    mCallbackResult.getResult("-1", TAG);
                }
//                try {
//                    ObjectMapper objectMapper = JacksonMapper.getInstance();
//                    ArrayList<Map<String, Object>> responseValues = objectMapper.readValue(response, new ArrayList<Map<String, Object>>().getClass());
//                    ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
//                    for (int i = 0; i < responseValues.size(); i++) {
//                        Map<String, Object> maps = responseValues.get(i);
//                        String code = (String) maps.get("code");
//                        String totalCount = (String) maps.get("totalCount");
//                        if ("-5".equals(code)) {
//                            //无数据
//                            mCallbackResult.getResult("无数据", TAG);
//                        } else if ("0".equals(code)) {
//                            List<List<String>> data = (List<List<String>>) maps.get("data");
//                            for (List<String> strs : data) {
//                                StockInfoEntity _bean = new StockInfoEntity();
//                                _bean.setTotalCount(totalCount);
//                                for (int j = 0; j < strs.size(); j++) {
//
//                                    if (!TextUtils.isEmpty(strs.get(0))) {
//                                        _bean.setStockNumber(strs.get(0));
//
//                                        if (!TextUtils.isEmpty(mHoldStockCodes) && mHoldStockCodes.contains(_bean.getStockNumber())) {
//                                            _bean.setStockholdon("true");
//                                        }
//
//                                        //判断是不是自选股
//                                        StockInfoEntity tempBean = Db_PUB_STOCKLIST.queryStockFromID(_bean.getStockNumber());
//                                        if (tempBean != null) {
//                                            _bean.setSelfChoicStock(true);
//
//                                            if (!TextUtils.isEmpty(tempBean.getIsHoldStock())) {
//                                                _bean.setIsHoldStock("true");
//                                            }
//
//                                            if (!TextUtils.isEmpty(tempBean.getApperHoldStock())) {
//                                                _bean.setApperHoldStock(tempBean.getApperHoldStock());
//                                            }
//
//                                        } else {
//                                            _bean.setSelfChoicStock(false);
//                                        }
//
//                                    }
//
//                                    if (!TextUtils.isEmpty(strs.get(1))) {
//                                        _bean.setStockName(strs.get(1));
//                                    }
//
//                                    if (!TextUtils.isEmpty(strs.get(2))) {
//
//                                        if ("-".equals(strs.get(2))) {
//                                            _bean.setNewPrice("0");
//                                        } else {
//                                            _bean.setNewPrice(strs.get(2));
//                                        }
//
//                                    }
//
//                                    if (!TextUtils.isEmpty(strs.get(3))) {
//
//                                        if ("-".equals(strs.get(3))) {
//                                            _bean.setClose("0");
//                                        } else {
//                                            _bean.setClose(strs.get(3));
//                                        }
//                                    }
//
//                                    if (!TextUtils.isEmpty(strs.get(4))) {
//
//                                        if ("-".equals(strs.get(4))) {
//                                            _bean.setHot("0");
//                                        } else {
//                                            _bean.setHot(strs.get(4));
//                                        }
//
//                                    }
//
//                                    if (!TextUtils.isEmpty(_bean.getNewPrice()) && !TextUtils.isEmpty(_bean.getClose())) {
//                                        double price = Double.parseDouble(_bean.getNewPrice());
//                                        double close = Double.parseDouble(_bean.getClose());
//
//                                        double zdz = price - close;
//                                        double zdf = zdz / close;
//
//                                        _bean.setUpAndDownValue("" + zdz);
//                                        _bean.setPriceChangeRatio("" + zdf);
//                                    } else {
//                                        _bean.setUpAndDownValue("0");
//                                        _bean.setPriceChangeRatio("0");
//                                    }
//
//                                }
//                                beans.add(_bean);
//                            }
//
//                            mCallbackResult.getResult(beans, TAG);
//
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    mCallbackResult.getResult("-1", TAG);
//                }
            }
        });
    }
}
