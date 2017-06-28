package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.myself.RiskTestDetailAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/6/27.
 * 风险承受能力测评详情查看界面
 */

public class RiskTestDetailActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnCancelListener{

    private final String TAG =  RiskTestDetailActivity.class.getSimpleName();
    private RiskTestDetailAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private List<Map<String, Object>> mDatas;

    @Override
    public void initView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("查看问卷详情");
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        ListView listView = (ListView)findViewById(R.id.lv_riskTestDetile);
        mAdapter = new RiskTestDetailAdapter(RiskTestDetailActivity.this);
        listView.setAdapter(mAdapter);
        mDatas = new ArrayList<>();

        getTestCheckDetailResult();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        OkHttpUtil.cancelSingleRequestByTag(TAG);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_risktest_detail;
    }


    private void getTestCheckDetailResult() {
        String session = SpUtils.getString(this, "mSession", "");
        initLoadDialog();
        HashMap map = new HashMap();
        map.put("funcid", "731013");
        map.put("token", session);
        HashMap map1 = new HashMap();
        map1.put("PAPER_TYPE", "1");
        map1.put("PRODTA_NO", "");
        map1.put("POSITION_STR", "");
        map1.put("REQUEST_NUM", "");
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        map.put("parms", map1);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                showMistackDialog(ConstantUtil.NETWORK_ERROR, null);

                if (mProgressDialog!= null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
            }

            @Override
            public void onResponse(String response, int id) {

                if (mProgressDialog!= null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }

                if (TextUtils.isEmpty(response)) {
                    showMistackDialog(ConstantUtil.SERVICE_NO_DATA, null);
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");

                    if ("-6".equals(code)) {

                        Intent intent = new Intent(RiskTestDetailActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                        return;
                    }

                    if (!"0".equals(code)) {
                        showMistackDialog(msg, null);
                        return ;
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (jsonArray == null || jsonArray.length() <= 0) {
                        return;
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Map<String, Object> data = new HashMap<>();

                        JSONObject subObj = jsonArray.getJSONObject(i);
                        String question = subObj.optString("QUESTION_CONTENT");//试题内容
                        String quesiontNo = subObj.optString("ORDER_NO");//试题顺序号
                        String answer_history =  subObj.optString("ANSWER_HISTORY");//历史选择答案
                        String question_kind = subObj.optString("QUESTION_KIND");//试题类型0：单选 1：多选 2：可编辑题

                        data.put("question", quesiontNo +"."+ question);
                        data.put("answer_history", answer_history);
                        data.put("question_kind", question_kind);

                        JSONArray answers = subObj.getJSONArray("OPTION_ANSWER");

                        if (answers == null || answers.length() <= 0) {
                            continue;
                        }

                        ArrayList<Map<String, String>> subData = new ArrayList<Map<String, String>>();
                        for (int j = 0; j < answers.length(); j++) {
                            Map<String, String> map = new HashMap<>();

                            JSONObject answer = answers.getJSONObject(j);
                            String answer_content = answer.optString("ANSWER_CONTENT");
                            String answer_no = answer.optString("ANSWER_NO");

                            map.put("answer_content", answer_no + "." + answer_content);
                            subData.add(map);
                        }

                        data.put("subData", subData);
                        mDatas.add(data);
                        mAdapter.setDatas(mDatas);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog(ConstantUtil.JSON_ERROR, null);
                }

            }
        });
    }

    private void initLoadDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void showMistackDialog(String errorMsg,  DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(RiskTestDetailActivity.this).create();
        alertDialog.setMessage(errorMsg);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", onClickListener);
        alertDialog.show();
    }
}
