package com.tpyzq.mobile.pangu.activity.myself.account;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.myself.SpeedTestAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.SpeedTestEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;


/**
 * 站点切换
 */
public class SpeedTestActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "SpeedTestActivity";
    private ImageView iv_back;
    private ListView lv_speed;
    private List<SpeedTestEntity> speedTestBeen;
    private SpeedTestAdapter speedTestAdapter;
    private Button bt_speedtest;
    private Button bt_sure;
    private String sitename;
    private Dialog loadingDialog;
    private int count;

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_speed = (ListView) findViewById(R.id.lv_speed);
        bt_speedtest = (Button) findViewById(R.id.bt_speedtest);
        bt_sure = (Button) findViewById(R.id.bt_sure);

        bt_speedtest.setClickable(false);
        bt_speedtest.setFocusable(false);
        bt_speedtest.setBackgroundResource(R.color.texts);

        bt_sure.setClickable(false);
        bt_sure.setFocusable(false);
        bt_sure.setBackgroundResource(R.color.texts);
        initData();
    }

    private void initData() {
        String ip = SpUtils.getString(this, "market_ip", null);
        if (TextUtils.isEmpty(ip)) {
            SpUtils.putString(this, "market_ip", ConstantUtil.HQ_IP);
        }
        loadingDialog = LoadingDialog.initDialog(this, "请稍后...");
        speedTestBeen = new ArrayList<SpeedTestEntity>();
        speedTestAdapter = new SpeedTestAdapter(this, speedTestBeen, speedCallBack);
        iv_back.setOnClickListener(this);
        bt_sure.setOnClickListener(this);
        bt_speedtest.setOnClickListener(this);
        lv_speed.setAdapter(speedTestAdapter);
        getData();
    }

    private void getData() {
        try{
            JSONObject jsonObj = new JSONObject(ConstantUtil.SITE_JSON);
            jsonObj = jsonObj.optJSONObject("message");
            HashMap hashMap = InterfaceCollection.getInstance().parseSites(jsonObj);
            String[] tradeArr=(String[])hashMap.get("hq");
            SpeedTestEntity speedTestEntity;
            String ip ;
            if (null!=speedTestBeen)
                speedTestBeen.clear();
            for (int i=0;i<tradeArr.length;i++) {
                String[] arr = tradeArr[i].split("\\|");
                speedTestEntity = new SpeedTestEntity();
                speedTestEntity.version_name = arr[0];
                speedTestEntity.version_ip = arr[1];
                if (ConstantUtil.JY_IP.equals(speedTestEntity.version_ip)) {
                    speedTestEntity.isChecked = true;
                } else {
                    speedTestEntity.isChecked = false;
                }
                speedTestBeen.add(speedTestEntity);
            }
            setSite();
        }catch (Exception e){

        }
    }

/*
    private void getData() {
        HashMap map800125 = new HashMap();
        map800125.put("FUNCTIONCODE", "HQLNG107");
        map800125.put("TOKEN", "");
        HashMap map800125_1 = new HashMap();
        map800125.put("PARAMS", map800125_1);
        String url = ConstantUtil.getURL_HQ_WA();
        NetWorkUtil.getInstence().okHttpForPostString(TAG, url, map800125, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if(loadingDialog != null){
                    loadingDialog.dismiss();
                }
                MistakeDialog.showDialog(ConstantUtil.NETWORK_ERROR, SpeedTestActivity.this, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
                            finish();
                        }
                    }).show();
            }

            @Override
            public void onResponse(String response, int id) {
                if(loadingDialog != null){
                    loadingDialog.dismiss();
                }
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String type = jsonObject.getString("type");

                    if (!"200".equals(code)) {
                        MistakeDialog.showDialog(type,SpeedTestActivity.this, new MistakeDialog.MistakeDialgoListener() {
                            @Override
                            public void doPositive() {
                                finish();
                            }
                        }).show();
                        return;
                    }
                    String msg = jsonObject.getString("message");
                    JSONObject jsonObject2 = new JSONObject(msg);
                    String data = jsonObject2.getString("data");
                    String code2 = jsonObject2.getString("code");

                    if ("0".equals(code2)) {
                        JSONArray jsonArray = new JSONArray(data);
                        speedTestBeen.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsBean = jsonArray.getJSONObject(i);
                            SpeedTestEntity speedTestBean = new SpeedTestEntity();
                            speedTestBean.version_name = jsBean.getString("version_name");
                            speedTestBean.version_id = jsBean.getString("version_id");
                            speedTestBean.version_ip = jsBean.getString("version_num");
                            speedTestBeen.add(speedTestBean);
                        }

                        setSite();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
*/

    private void setSite() {
        if (null!=loadingDialog && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
        count = 0;//需要调用的请求次数
        for (int i = 0; i < speedTestBeen.size(); i++) {
            final long starttime = System.currentTimeMillis();
            final int j = i;
            String url = speedTestBeen.get(i).version_ip;
            Map<String, String> map005 = new HashMap();
            Object[] object = new Object[1];
            map005.put("FUNCTIONCODE", "HQING005");
            Map map005_1 = new HashMap();
            map005_1.put("market", "0");
            map005_1.put("code", "10000001&20399001");
            map005_1.put("type", "4");
            map005_1.put("order", "0");
            object[0] = map005_1;
            map005.put("PARAMS", Arrays.toString(object));
            NetWorkUtil.getInstence().okHttpForGet("", url, map005, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    count++;
                    speedTestBeen.get(j).version_time = 10000;
                    speedTestBeen.get(j).version_status = "良";
                    setList();
                }

                @Override
                public void onResponse(String response, int id) {
                    long endtime = System.currentTimeMillis();
                    speedTestBeen.get(j).version_time = endtime - starttime;
                    if (endtime - starttime < 1000) {
                        speedTestBeen.get(j).version_status = "优";
                    } else {
                        speedTestBeen.get(j).version_status = "良";
                    }
                    count++;
                    setList();
//                    setClear();
                }
            });
        }
    }

    /**
     * 测试完成，进行展示
     */
    private void setList() {
        if (count == speedTestBeen.size()) {
            speedTestBeen.get(getSort()).version_status = "优";
            lv_speed.setAdapter(new SpeedTestAdapter(SpeedTestActivity.this, speedTestBeen, speedCallBack));

            bt_speedtest.setClickable(true);
            bt_speedtest.setFocusable(true);
            bt_speedtest.setBackgroundResource(R.color.blue);

            bt_sure.setClickable(true);
            bt_sure.setFocusable(true);
            bt_sure.setBackgroundResource(R.color.blue);
            if (null!=loadingDialog && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        }
    }

    /**
     * 给List进行排序
     */
    private int getSort() {
        Map<Integer, Long> map = new HashMap<>();
        for (int i = 0; i < speedTestBeen.size(); i++) {
            map.put(i, speedTestBeen.get(i).version_time);
        }
        List<Long> longs = new ArrayList<>();
        for (int i = 0; i < speedTestBeen.size(); i++) {
            longs.add(speedTestBeen.get(i).version_time);
        }
        Collections.sort(longs);
        long min = longs.get(0);
        int back = 0;
        Set<Integer> kset = map.keySet();
        for (Integer ks : kset) {
            if (min == map.get(ks)) {
                back = ks;
            }
        }

        return back;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_speed_test;
    }

    private String appIP = ConstantUtil.HQ_IP;

    SpeedTestAdapter.SpeedCallBack speedCallBack = new SpeedTestAdapter.SpeedCallBack() {
        @Override
        public void setUrl(String name, String url) {
            sitename = name;
            appIP = url;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_speedtest:
//                setClear();
//                getData();
                setSite();
                break;
            case R.id.bt_sure:
                SpUtils.putString(SpeedTestActivity.this, "market_ip", appIP);
//                CentreToast.showText(this, "站点已切换到: " + sitename);
                String jy_ip = SpUtils.getString(SpeedTestActivity.this, "jy_ip", null);
                ConstantUtil.setUrl(appIP,jy_ip);
                finish();
                break;
        }
    }

/*
    private void setClear() {
        for (SpeedTestEntity speedTestBean : speedTestBeen) {
            final String ip = speedTestBean.version_ip + ":" + speedTestBean.version_port;
            if (ConstantUtil.HQ_IP.equals(ip)) {
                sitename = ConstantUtil.status;
                appIP = ConstantUtil.HQ_IP;
                return;
            } else {
                sitename = "暂无选中";
                appIP = ConstantUtil.HQ_IP;
            }
        }
    }
*/
}
