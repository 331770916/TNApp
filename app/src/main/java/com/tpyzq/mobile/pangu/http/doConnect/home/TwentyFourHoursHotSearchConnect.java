package com.tpyzq.mobile.pangu.http.doConnect.home;

import android.content.Context;
import android.text.TextUtils;

import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/12/30.
 * 热搜网络链接
 */
public class TwentyFourHoursHotSearchConnect {
    private ICallbackResult mCallbackResult;
    private Context mContext;
    private String mHttpTag;
    private String mMostType;
    private String mHoldStockCodes;
    public static final String TAG = "TwentyFourHoursHotSearchConnect";


    public TwentyFourHoursHotSearchConnect(Context context, String httpTag, String mostType) {
        mContext = context;
        mHttpTag = httpTag;
        mMostType = mostType;
        mHoldStockCodes = HOLD_SEQ.getHoldCodes();
    }


    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request();
    }

    private void request() {
        HashMap hashMap = new HashMap();
        hashMap.put("FUNCTIONCODE", "HQTNG101");
        HashMap hashMap_1 = new HashMap();
        hashMap.put("PARAMS", hashMap_1);
        hashMap_1.put("MOST_TYPE", mMostType);
        hashMap.put("TOKEN", "");

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTag, ConstantUtil.URL_RS, hashMap, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Helper.getInstance().showToast(mContext, "网络异常");
                LogUtil.e(mHttpTag, e.toString());
                mCallbackResult.getResult("网络异常", TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    JSONArray message = jsonObject.getJSONArray("message");
                    ArrayList<StockInfoEntity> data = new ArrayList<StockInfoEntity>();
                    if ("200".equals(code)) {
                        if (message != null && message.length() > 0) {
                            for (int i = 0; i < message.length(); i++) {
                                StockInfoEntity _bean = new StockInfoEntity();
                                _bean.setMessage(message.getJSONObject(i).getString("message"));
                                _bean.setTimestamp(message.getJSONObject(i).getString("timestamp"));
                                _bean.setStockNumber(message.getJSONObject(i).getString("symbol"));
                                _bean.setStockName(message.getJSONObject(i).getString("name"));
                                _bean.setCurprice(message.getJSONObject(i).getString("curprice"));
                                _bean.setRead(message.getJSONObject(i).getString("read"));
                                _bean.setViewcount(message.getJSONObject(i).getString("viewcount"));

                                if (!TextUtils.isEmpty(mHoldStockCodes) && mHoldStockCodes.contains(_bean.getStockNumber())) {
                                    _bean.setStockholdon("true");
                                }

                                //判断是不是自选股
                                StockInfoEntity tempBean = Db_PUB_STOCKLIST.queryStockFromID(_bean.getStockNumber());
                                if (tempBean != null) {
                                    _bean.setSelfChoicStock(true);

                                    if (!TextUtils.isEmpty(tempBean.getIsHoldStock())) {
                                        _bean.setIsHoldStock("true");
                                    }

                                    if (!TextUtils.isEmpty(tempBean.getApperHoldStock())) {
                                        _bean.setApperHoldStock(tempBean.getApperHoldStock());
                                    }

                                } else {
                                    _bean.setSelfChoicStock(false);
                                }
                                data.add(_bean);
                            }
                        }
                        mCallbackResult.getResult(data, TAG);
                    }else{
                        mCallbackResult.getResult("获取热搜股票失败", TAG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mCallbackResult.getResult("报文解析失败", TAG);
                }


            }
        });
    }


}
