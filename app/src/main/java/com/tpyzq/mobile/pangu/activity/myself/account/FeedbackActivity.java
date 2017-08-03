package com.tpyzq.mobile.pangu.activity.myself.account;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 反馈界面
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv_back;
    EditText et_valuable_idea;
    EditText et_contact_way;
    Button bt_true;
    String TAG = "feedbackactivity";

    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_valuable_idea = (EditText) findViewById(R.id.et_valuable_idea);
        et_contact_way = (EditText) findViewById(R.id.et_contact_way);
        bt_true = (Button) findViewById(R.id.bt_true);
        initData();
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        bt_true.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_true:
                if (!Helper.isMobileNO(et_contact_way.getText().toString())) {
                    ToastUtils.centreshow(this, "请输入正确的手机号");
                } else {
                    getData();
                }
                break;
        }
    }

    private void getData() {
        HashMap map900105 = new HashMap();
        map900105.put("funcid", "900105");
        map900105.put("token", "");
        HashMap map900105_1 = new HashMap();
        map900105_1.put("classid", "000100010005");
        map900105_1.put("title", "0");
        map900105_1.put("poster", et_contact_way.getText().toString());
        map900105_1.put("posterID", et_contact_way.getText().toString());
        map900105_1.put("content", et_valuable_idea.getText().toString());
        map900105_1.put("contenttype", "0");
        map900105.put("parms", map900105_1);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_HQ_HS(), map900105, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        MistakeDialog.showDialog("反馈成功", FeedbackActivity.this,mistakeDialgoListener);
                    } else {
                        MistakeDialog.showDialog("反馈失败", FeedbackActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    MistakeDialog.MistakeDialgoListener mistakeDialgoListener = new MistakeDialog.MistakeDialgoListener() {
        @Override
        public void doPositive() {
            finish();
        }
    };
}
