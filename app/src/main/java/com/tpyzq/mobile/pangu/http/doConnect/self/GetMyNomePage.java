package com.tpyzq.mobile.pangu.http.doConnect.self;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.data.MyHomePageEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * Created by wangqi on 2016/10/24.
 * 我的消息数据
 */
public class GetMyNomePage {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetMyHomePage";
    private String mHttpTAG;

    public GetMyNomePage(String httpTag) {
        mHttpTAG = httpTag;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request();
    }

    private void request() {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        HashMap map2=new HashMap();
        map.put("funcid", "800123");
        map.put("token", "");
        map.put("parms", map1);
        map1.put("ACCOUNT", UserUtil.Mobile);


        map1.put("ACCOUNT", UserUtil.Mobile);
        map1.put("OBJECTIVE", map2);

        String warning_push_time = SpUtils.getString(context, "warning_push_time", "");
        String newshare_push_time = SpUtils.getString(context, "newshare_push_time", "");
        String inform_push_time = SpUtils.getString(context, "inform_push_time", "");
        map2.put("1", warning_push_time);
        map2.put("2", newshare_push_time);
        map2.put("3", inform_push_time);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_NEW(), map, new StringCallback() {


            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                mCallbackResult.getResult(null, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                //{"totalcount":"21","count_three":"10","data":[[],[],[{"OBJECTIVE":"3","START":"1","BIZID":"e396ae889dad45c5a737d3fe719e6db2","PUSH_TIME":"2016-11-14 22:38:34","READ_TYPE":"0","REMARKS":"","CONTENE":"测试2","ACCOUNT":"","PUSH_RECORD_ID":"f7f6b93d7934493aaa72ac230a6d2ba4","TOKEN":"","TITLE":"测试"},{"OBJECTIVE":"3","START":"1","BIZID":"8c82e586adc3447eb8d0ff7b136021e1","PUSH_TIME":"2016-11-14 22:37:45","READ_TYPE":"0","REMARKS":"","CONTENE":"测试1","ACCOUNT":"","PUSH_RECORD_ID":"f889e32f3466495a97ddc7c1b88b18d6","TOKEN":"","TITLE":"测试"},{"OBJECTIVE":"3","START":"1","BIZID":"d3a6b44497884f4eba66ffaf002fca7b","PUSH_TIME":"2016-11-14 13:12:36","READ_TYPE":"0","REMARKS":"","CONTENE":"测试1","ACCOUNT":"","PUSH_RECORD_ID":"353783ea4c1f46178288ba8eb86b33a6","TOKEN":"","TITLE":"测试"},{"OBJECTIVE":"3","START":"1","BIZID":"0fcec8de75f04c6fa445ddd0111866bd","PUSH_TIME":"2016-11-14 13:10:33","READ_TYPE":"0","REMARKS":"","CONTENE":"测试1","ACCOUNT":"","PUSH_RECORD_ID":"e2528299df204a7bbc004d3c41a276b3","TOKEN":"","TITLE":"测试"},{"OBJECTIVE":"3","START":"1","BIZID":"2a73e2fbf4b14ee2aee099b689f66e56","PUSH_TIME":"2016-11-14 13:10:06","READ_TYPE":"0","REMARKS":"","CONTENE":"测试测试+++","ACCOUNT":"","PUSH_RECORD_ID":"ee1cd85bfa814f919a2b730fbd341fd5","TOKEN":"","TITLE":"测试"},{"OBJECTIVE":"3","START":"1","BIZID":"615a9d421497459aa030b5153fedf337","PUSH_TIME":"2016-11-14 13:08:55","READ_TYPE":"0","REMARKS":"","CONTENE":"测试测试+++","ACCOUNT":"","PUSH_RECORD_ID":"039821e48835453481ec288f443eb666","TOKEN":"","TITLE":"测试"},{"OBJECTIVE":"3","START":"1","BIZID":"b090e4e34a31495d816b7f94f81d3757","PUSH_TIME":"2016-11-14 13:04:17","READ_TYPE":"0","REMARKS":"","CONTENE":"测试测试+++","ACCOUNT":"","PUSH_RECORD_ID":"d0071fcf66b34baeb9d2af83685dc03b","TOKEN":"","TITLE":"测试"},{"OBJECTIVE":"3","START":"1","BIZID":"1576aa67b0b04e0c977bd060490a1874","PUSH_TIME":"2016-11-14 11:36:22","READ_TYPE":"0","REMARKS":"","CONTENE":"测试","ACCOUNT":"","PUSH_RECORD_ID":"3b2d0f865cdc4125ba0fc3d06eed9f1d","TOKEN":"","TITLE":"测试"},{"OBJECTIVE":"3","START":"1","BIZID":"497c36c51d66442ba971a65d19c86653","PUSH_TIME":"2016-11-14 11:34:35","READ_TYPE":"0","REMARKS":"","CONTENE":"测试++++","ACCOUNT":"","PUSH_RECORD_ID":"5a1857ef20c04cd3a3614784b1216632","TOKEN":"","TITLE":"测试"},{"OBJECTIVE":"3","START":"1","BIZID":"a3998b686e5c42adb04afc90934a0b09","PUSH_TIME":"2016-11-14 11:34:12","READ_TYPE":"0","REMARKS":"","CONTENE":"fasdfasf","ACCOUNT":"","PUSH_RECORD_ID":"895630ab979b4e9b851f73983b0a504a","TOKEN":"","TITLE":"fsdffasdf"}]],"count_count":"10","code":0,"msg":"操作成功","count_one":"0","count_two":"0"}

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ArrayList<MyHomePageEntity> banderDatas = new ArrayList<MyHomePageEntity>();
                    if ("0".equals(jsonObject.getString("code"))) {
                        MyHomePageEntity beans = new MyHomePageEntity();
                        beans.setCount_count(jsonObject.optString("count_count"));             //总未读条数
                        beans.setCount_one(jsonObject.optString("count_one"));                  //到价提醒未读条数
                        beans.setCount_two(jsonObject.optString("count_two"));                  //新股申购未读条数
                        beans.setCount_three(jsonObject.optString("count_three"));               //社区提醒未读条数
                        banderDatas.add(beans);
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {

                                JSONArray list_Warning = data.getJSONArray(0);
                                JSONArray list_NewShare = data.getJSONArray(1);
                                JSONArray list_Inform = data.getJSONArray(2);

                                //通知公告
                                if (list_Inform != null && list_Inform.length() > 0) {
                                    for (int j = 0; j < list_Inform.length(); j++) {
                                        MyHomePageEntity _beans1 = new MyHomePageEntity();
                                        _beans1.setToken_Inform(list_Inform.getJSONObject(0).optString("TITLE"));
                                        _beans1.setPush_time_inform(list_Inform.getJSONObject(0).optString("PUSH_TIME"));
                                        banderDatas.add(_beans1);
                                    }
                                }

                                //新股申购通知
                                if (list_NewShare != null && list_NewShare.length() > 0) {
                                    for (int x = 0; x < list_NewShare.length(); x++) {
                                        MyHomePageEntity _beans2 = new MyHomePageEntity();
                                        _beans2.setToken_newshare(list_NewShare.getJSONObject(0).optString("TITLE"));
                                        _beans2.setPush_time_newshare(list_NewShare.getJSONObject(0).optString("PUSH_TIME"));
                                        banderDatas.add(_beans2);
                                    }
                                }

                                //行情预警通知
                                if (list_Warning != null && list_Warning.length() > 0) {
                                    for (int c = 0; c < list_Warning.length(); c++) {
                                        MyHomePageEntity _beans3 = new MyHomePageEntity();
                                        _beans3.setToken_warning(list_Warning.getJSONObject(0).optString("TITLE"));
                                        _beans3.setPush_time_warning(list_Warning.getJSONObject(0).optString("PUSH_TIME"));
                                        banderDatas.add(_beans3);
                                    }
                                }
                            }
                        }

                        mCallbackResult.getResult(banderDatas, TAG);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    mCallbackResult.getResult(null, TAG);
                }

            }
        });
    }
}
