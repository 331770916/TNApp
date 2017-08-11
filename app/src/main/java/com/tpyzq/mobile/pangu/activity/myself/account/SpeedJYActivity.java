package com.tpyzq.mobile.pangu.activity.myself.account;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.myself.SpeedTestAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.SpeedTestEntity;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/5/11.
 */

public class SpeedJYActivity extends BaseActivity implements View.OnClickListener{

    private SpeedTestAdapter mAdapter;
    private ArrayList<SpeedTestEntity> mDatas;
    private Dialog mLoadingDialog;
    private ListView mListView;
    private String sitename;
    private String jyIp = ConstantUtil.JY_IP;
    private TextView mSureBtn;
    private static final String TAG = SpeedJYActivity.class.getSimpleName();

    @Override
    public void initView() {
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        mSureBtn = (TextView) findViewById(R.id.tv_confirm_jy);
        mSureBtn.setClickable(false);
        mSureBtn.setFocusable(false);
        mSureBtn.setBackgroundResource(R.color.texts);


        mSureBtn.setOnClickListener(this);
        TextView titleTextView = (TextView) findViewById(R.id.toolbar_title);
        titleTextView.setText("交易站点切换");

        mListView = (ListView) findViewById(R.id.listview_jy_speed);
        mDatas = new ArrayList<>();
        mAdapter = new SpeedTestAdapter(this, mDatas, speedCallBack);
        mListView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        /*mLoadingDialog = LoadingDialog.initDialog(this, "正在加载...");
        mLoadingDialog.show();*/

        String ip = SpUtils.getString(this, "jy_ip", null);
        if (TextUtils.isEmpty(ip)) {
            SpUtils.putString(this, "jy_ip", ConstantUtil.JY_IP);
        }

        getData();
    }

    SpeedTestAdapter.SpeedCallBack speedCallBack = new SpeedTestAdapter.SpeedCallBack() {
        @Override
        public void setUrl(String name, String url) {
            sitename = name;
            jyIp = url;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
            case R.id.tv_confirm_jy:


                UserEntity userEntity = new UserEntity();

                userEntity.setIslogin("false");
//                userEntity.setCertification("");
//                Db_PUB_USERS.UpdateCertification(userEntity);
                Db_PUB_USERS.UpdateIslogin(userEntity);

//                HOLE_SEQ.deleteAll();
                SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);

                SpUtils.putString(SpeedJYActivity.this, "jy_ip", jyIp);
//                CentreToast.showText(this, "站点已切换到: " + sitename);
                String ip = SpUtils.getString(SpeedJYActivity.this, "market_ip", null);
                ConstantUtil.setUrl(ip, jyIp);
                finish();
                break;
        }
    }
    private void getData() {
        try{
            JSONObject jsonObj = new JSONObject(ConstantUtil.SITE_JSON);
            jsonObj = jsonObj.optJSONObject("message");
            HashMap hashMap = InterfaceCollection.getInstance().parseSites(jsonObj);
            String[] hqArr=(String[])hashMap.get("trade");
            SpeedTestEntity speedTestEntity;
            for (int i=0;i<hqArr.length;i++) {
                String[] arr = hqArr[i].split("\\|");
                speedTestEntity = new SpeedTestEntity();
                speedTestEntity.version_name = arr[0];
                speedTestEntity.version_ip = arr[1];
                if (ConstantUtil.JY_IP.equals(speedTestEntity.version_ip)) {
                    speedTestEntity.isChecked = true;
                } else {
                    speedTestEntity.isChecked = false;
                }
                mDatas.add(speedTestEntity);
            }
            mListView.setAdapter(new SpeedTestAdapter(SpeedJYActivity.this, mDatas, speedCallBack));

            mSureBtn.setClickable(true);
            mSureBtn.setFocusable(true);
            mSureBtn.setBackgroundResource(R.color.blue);
        }catch (Exception e){

        }
    }
  /*  private void getData() {
        HashMap map800125 = new HashMap();
        map800125.put("FUNCTIONCODE", "HQLNG107");
        map800125.put("TOKEN", "");
        HashMap map800125_1 = new HashMap();
        map800125.put("PARAMS", map800125_1);
        String url = ConstantUtil.getURL_HQ_WA();

        NetWorkUtil.getInstence().okHttpForPostString(TAG, url, map800125, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if(mLoadingDialog != null){
                    mLoadingDialog.dismiss();
                }
                MistakeDialog.showDialog(ConstantUtil.NETWORK_ERROR,SpeedJYActivity.this, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {
                        finish();
                    }
                });
            }

            @Override
            public void onResponse(String response, int id) {
                if(mLoadingDialog != null){
                    mLoadingDialog.dismiss();
                }
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                //{"message":{"data":[{"version_name":"开发环境","version_ip":"","version_num":"http://106.120.112.246:8082","version_register_url":"1","version_port":"","version_transaction_url":"http://106.120.112.246:8081","version_id":"39"},{"version_name":"测试环境","version_ip":"","version_num":"http://tnhq.tpyzq.com","version_register_url":"1","version_port":"","version_transaction_url":"https://tn.tpyzq.com","version_id":"40"}],"code":"0"},"code":"200","type":"success"}

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String type = jsonObject.getString("type");

                    if (!"200".equals(code)) {
                        MistakeDialog.showDialog(type, SpeedJYActivity.this, new MistakeDialog.MistakeDialgoListener() {
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
                        mDatas.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsBean = jsonArray.getJSONObject(i);
                            SpeedTestEntity speedTestBean = new SpeedTestEntity();
                            speedTestBean.version_name = jsBean.getString("version_name");
                            speedTestBean.version_id = jsBean.getString("version_id");
                            speedTestBean.version_ip = jsBean.getString("version_transaction_url");
                            mDatas.add(speedTestBean);
                        }
                        mListView.setAdapter(new SpeedTestAdapter(SpeedJYActivity.this, mDatas, speedCallBack));

                        mSureBtn.setClickable(true);
                        mSureBtn.setFocusable(true);
                        mSureBtn.setBackgroundResource(R.color.blue);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtil.cancelSingleRequestByTag(TAG);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_speed_jy;
    }
}
