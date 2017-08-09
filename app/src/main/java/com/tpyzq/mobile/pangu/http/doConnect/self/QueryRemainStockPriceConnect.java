package com.tpyzq.mobile.pangu.http.doConnect.self;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.SelfChoiceEntity;
import com.tpyzq.mobile.pangu.data.SubSelfChoiceEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/9/12.
 * 查询到价提醒列表
 */
public class QueryRemainStockPriceConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "QueryRemainStockPriceConnect";
    private String mHttpTAG;
    private String mToken;
    private String mUserId;

    public QueryRemainStockPriceConnect(String httpTag, String token, String userId) {
        mHttpTAG = httpTag;
        mToken = token;
        mUserId = userId;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request();
    }

    private void request() {
        Map map1 = new HashMap();
        map1.put("funcid", "800117");
        map1.put("token", mToken);

        Map map2 = new HashMap();
        map2.put("USERID", mUserId);
        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_HQ_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                mCallbackResult.getResult("timeout", TAG);
            }

            @Override
            public void onResponse(String response, int id) {

//                response = "{\"msg\":\"查询成功\",\"totalcount\":3,\"code\":0,\"data\":[{\"MINIMUM\":\"-0\",\"CODE\":\"21000009\",\"RISESIZQCONFINE\":\"0\",\"MAXIMUM\":\"1\",\"FALLSIZECONFINE\":\"0\",\"NAME\":\"中国宝安\"},{\"MINIMUM\":\"-0\",\"CODE\":\"21000009\",\"RISESIZQCONFINE\":\"0\",\"MAXIMUM\":\"1\",\"FALLSIZECONFINE\":\"0\",\"NAME\":\"中国宝安\"},{\"MINIMUM\":\"-0\",\"CODE\":\"21000009\",\"RISESIZQCONFINE\":\"0\",\"MAXIMUM\":\"1\",\"FALLSIZECONFINE\":\"0\",\"NAME\":\"中国宝安\"}]}";

                if (TextUtils.isEmpty(response)) {
                    return;
                }

                Gson gson = new Gson();

                java.lang.reflect.Type type = new TypeToken<SelfChoiceEntity>() {
                }.getType();
                SelfChoiceEntity bean = gson.fromJson(response, type);
                String code = "";
                if (bean != null) {
                    code = bean.getCode();
                }
                if (!"0".equals(code)) {
                    Helper.getInstance().showToast(CustomApplication.getContext(), "" + bean.getMsg());
                    return ;
                }

                List<SubSelfChoiceEntity> beans = new ArrayList<SubSelfChoiceEntity>();

                if (null != bean.getData() && bean.getData().size() > 0) {

                    for (SubSelfChoiceEntity _bean : bean.getData()) {

                        if (!TextUtils.isEmpty(_bean.getMAXIMUM()) && !"-0".equals(_bean.getMAXIMUM()) && !"0".equals(_bean.getMAXIMUM()) && !"+0".equals(_bean.getMAXIMUM())) {
                            SubSelfChoiceEntity _bean1 = new SubSelfChoiceEntity();
                            _bean1.setNAME(_bean.getNAME());
                            _bean1.setCODE(_bean.getCODE());
                            _bean1.setRemainType("股价涨到" + _bean.getMAXIMUM() +"元提醒");
                            beans.add(_bean1);
                        }

                        if (!TextUtils.isEmpty(_bean.getMINIMUM())&& !"-0".equals(_bean.getMINIMUM()) && !"0".equals(_bean.getMINIMUM()) && !"+0".equals(_bean.getMINIMUM())) {
                            SubSelfChoiceEntity _bean2 = new SubSelfChoiceEntity();
                            _bean2.setNAME(_bean.getNAME());
                            _bean2.setCODE(_bean.getCODE());
                            _bean2.setRemainType("股价跌到" + _bean.getMINIMUM() +"元提醒");
                            beans.add(_bean2);
                        }

                        if (!TextUtils.isEmpty(_bean.getRISESIZQCONFINE())&& !"-0".equals(_bean.getRISESIZQCONFINE()) && !"0".equals(_bean.getRISESIZQCONFINE()) && !"+0".equals(_bean.getRISESIZQCONFINE())) {
                            SubSelfChoiceEntity _bean3 = new SubSelfChoiceEntity();
                            _bean3.setNAME(_bean.getNAME());
                            _bean3.setCODE(_bean.getCODE());
                            _bean3.setRemainType("日涨幅到" + _bean.getRISESIZQCONFINE() +"%提醒");
                            beans.add(_bean3);
                        }

                        if (!TextUtils.isEmpty(_bean.getFALLSIZECONFINE())&& !"-0".equals(_bean.getFALLSIZECONFINE()) && !"0".equals(_bean.getFALLSIZECONFINE()) && !"+0".equals(_bean.getFALLSIZECONFINE())) {
                            SubSelfChoiceEntity _bean4 = new SubSelfChoiceEntity();
                            _bean4.setNAME(_bean.getNAME());
                            _bean4.setCODE(_bean.getCODE());
                            _bean4.setRemainType("日跌幅到" + _bean.getFALLSIZECONFINE() +"%提醒");
                            beans.add(_bean4);
                        }

                    }
                }
                mCallbackResult.getResult(beans, TAG);
            }
        });

    }
}
