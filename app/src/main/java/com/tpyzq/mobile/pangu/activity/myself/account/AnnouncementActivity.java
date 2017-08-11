package com.tpyzq.mobile.pangu.activity.myself.account;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/10/13.
 * 通知公告
 */
public class AnnouncementActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Announcement";
    private TextView title_text, push_time_text, contene_text;
    private String marked;

    @Override
    public void initView() {
        title_text = (TextView) findViewById(R.id.title_text);
        push_time_text = (TextView) findViewById(R.id.push_time_text);
        contene_text = (TextView) findViewById(R.id.contene_text);
        findViewById(R.id.ASpublish_back).setOnClickListener(this);

        initData();
    }

    private void initData() {
        String BIZID = getIntent().getStringExtra("BIZID");
        marked = getIntent().getStringExtra("Marked");

        toConnect(BIZID);

    }

    private void toConnect(String bizid) {
        final HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "800124");
        map.put("token", "");
        map.put("parms", map1);
        map1.put("BIZID", bizid);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_NEW(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                Helper.getInstance().showToast(AnnouncementActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        title_text.setText(data.getJSONObject(i).getString("TITLE"));
                        push_time_text.setText(data.getJSONObject(i).getString("PUSH_TIME"));
                        contene_text.setText(data.getJSONObject(i).getString("CONTENE"));
                        String BIZID = data.getJSONObject(i).getString("BIZID");
                        if (!"true".equals(marked)) {
                            setConnect(BIZID);
                        }
                    }

                } catch (JSONException e) {
                    CentreToast.showText(AnnouncementActivity.this, ConstantUtil.JSON_ERROR);
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_announcement;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ASpublish_back:
                finish();
                break;
        }
    }


    /**
     * 修改消息 为已读
     *
     * @param connect
     */
    public void setConnect(String connect) {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "800122");
        map.put("token", "");
        map.put("parms", map1);
        map1.put("BIZID", connect);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_NEW(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                CentreToast.showText(AnnouncementActivity.this, ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("code"))) {
                        LogUtil.e(TAG, jsonObject.getString("msg"));
                    } else {
                        LogUtil.e(TAG, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
