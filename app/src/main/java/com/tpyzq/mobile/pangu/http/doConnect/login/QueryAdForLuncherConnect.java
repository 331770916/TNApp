package com.tpyzq.mobile.pangu.http.doConnect.login;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bonree.k.M;
import okhttp3.Call;

import static com.tpyzq.mobile.pangu.util.panguutil.UserUtil.userId;

/**
 * Created by zhangwenbo on 2016/8/23.
 * 获取广告信息
 */
public class QueryAdForLuncherConnect {

    private String mToken;
    private String mPageIndex;
    private String mPageSize;
    private String mPostion;
    private ICallbackResult mCallbackResult;
    private String mHttpTAG;

    public QueryAdForLuncherConnect(String httpTag, String TOKEN, String position, String pageIndex, String pageSize) {
        mHttpTAG = httpTag;
        mToken = TOKEN;
        mPostion = position;
        mPageIndex = pageIndex;
        mPageSize = pageSize;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request(mToken, mPostion, mPageIndex, mPageSize);
    }

    /**
     * 获取广告信息
     * @param TOKEN 查询id
     * @param position 广告位置（数据字典id）
     * @param pageIndex 页码
     * @param pageSize 每页条数
     */
    private void request(String TOKEN, String position, String pageIndex, String pageSize) {
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("position", position);
        map2.put("pageIndex", pageIndex);
        map.put("PARAMS", map2);
        map.put("FUNCTIONCODE" , "HQLNG101");
        map.put("TOKEN", TOKEN);
        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_HQ_WA(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(mHttpTAG, e.toString());
//                CentreToast.showText(CustomApplication.getContext(),ConstantUtil.NETWORK_ERROR);
                String msg = "" + e.toString();
                mCallbackResult.getResult(msg, mHttpTAG);
//                Map<String,String> resultMap = new HashMap<String, String>();
//                resultMap.put("adUrl","http://img4.duitang.com/uploads/item/201406/12/20140612222256_3sMcF.thumb.700_0.jpeg");
//                mCallbackResult.getResult(resultMap, mHttpTAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    if("0".equals(code)){
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONArray array = jsonArray.optJSONObject(i).optJSONArray("advert_data");
                            for (int j = 0; j < array.length(); j++) {
                                Map<String,String> resultMap = new HashMap<>();
                                resultMap.put("jump_type",array.optJSONObject(j).optString("jump_type"));
                                resultMap.put("jump_url",array.optJSONObject(j).optString("jump_url"));
                                resultMap.put("jump_position",array.optJSONObject(j).optString("jump_position"));
                                resultMap.put("show_url",array.optJSONObject(j).optString("show_url"));
                                mCallbackResult.getResult(resultMap, mHttpTAG);
                            }
                        }

                    }else{
                        String msg = jsonObject.optString("msg");
                        mCallbackResult.getResult(msg, mHttpTAG);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mCallbackResult.getResult(e.toString(), mHttpTAG);
                }
            }
        });
    }
}
