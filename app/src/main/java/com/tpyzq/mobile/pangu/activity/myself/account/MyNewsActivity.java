package com.tpyzq.mobile.pangu.activity.myself.account;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dragindicator.DragIndicatorView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by wangqi on 2016/10/11.
 * 我的消息页面
 */
public class MyNewsActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "MyInformation";
    private TextView MyI_item_tv1, MyI_item_tv2, MyI_item_tv3;          //内容ID
    private TextView MyI_date1, MyI_date2, MyI_date3;                   //时间ID
    private DragIndicatorView MyI_div_num1, MyI_div_num2, MyI_div_num3;   //圆圈数量

    @Override
    public void initView() {
        SpUtils.putString(this, "mDivnum", "true");
        findViewById(R.id.MyI_back).setOnClickListener(this);
        findViewById(R.id.MyI_RL1).setOnClickListener(this);
        findViewById(R.id.MyI_RL2).setOnClickListener(this);
        findViewById(R.id.MyI_RL3).setOnClickListener(this);


        MyI_item_tv1 = (TextView) findViewById(R.id.MyI_item_tv1);
        MyI_item_tv2 = (TextView) findViewById(R.id.MyI_item_tv2);
        MyI_item_tv3 = (TextView) findViewById(R.id.MyI_item_tv3);

        MyI_date1 = (TextView) findViewById(R.id.MyI_date1);
        MyI_date2 = (TextView) findViewById(R.id.MyI_date2);
        MyI_date3 = (TextView) findViewById(R.id.MyI_date3);

        MyI_div_num1 = (DragIndicatorView) findViewById(R.id.MyI_div_num1);
        MyI_div_num2 = (DragIndicatorView) findViewById(R.id.MyI_div_num2);
        MyI_div_num3 = (DragIndicatorView) findViewById(R.id.MyI_div_num3);

        setInfoNum();
    }


    /**
     * 查询推送消息
     */
    private void setInfoNum() {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map.put("funcid", "800123");
        map.put("token", "");
        map.put("parms", map1);
        map1.put("ACCOUNT", UserUtil.Mobile);
        map1.put("OBJECTIVE", map2);
        String warning_push_time = SpUtils.getString(context, "warning_push_time", "");
        String newshare_push_time = SpUtils.getString(context, "newshare_push_time", "");
        String inform_push_time = SpUtils.getString(context, "inform_push_time", "");
        map2.put("1", warning_push_time);
        map2.put("2", newshare_push_time);
        map2.put("3", inform_push_time);
        String a = new Gson().toJson(map);
        LogUtil.e("----------req:"+map);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_ZX_GS, map, new StringCallback() {

            private int count_five_int;
            private int count_four_int;
            private int count_one_int;

            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e("", e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                LogUtil.e("--------nesss:"+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    //总未读条数
                    String count_count = jsonObject.optString("count_count");
                    //到价提醒未读条数
                    String count_one = jsonObject.optString("count_one");
                    //新股申购未读条数
                    String count_two = jsonObject.optString("count_two");
                    //社区提醒未读条数
                    String count_three = jsonObject.optString("count_three");
                    String count_four = jsonObject.optString("count_four");
                    String count_five = jsonObject.optString("count_five");

                    if (!"0".equals(count_three) && count_three != null) {
                        MyI_div_num1.setVisibility(View.VISIBLE);
                    }
                    if (!"0".equals(count_two) && count_two != null) {
                        MyI_div_num2.setVisibility(View.VISIBLE);
                    }
                    if (!"0".equals(count_one) && count_one != null) {
                        MyI_div_num3.setVisibility(View.VISIBLE);
                    }
                    MyI_div_num1.setText(count_three);
                    MyI_div_num2.setText(count_two);

                    if (!TextUtils.isEmpty(count_one)){
                        count_one_int = Integer.parseInt(count_one);
                    }
                    if (!TextUtils.isEmpty(count_four)){
                        count_four_int = Integer.parseInt(count_four);
                    }
                    if (!TextUtils.isEmpty(count_five)){
                        count_five_int = Integer.parseInt(count_five);
                    }

                    MyI_div_num3.setText(String.valueOf(count_one_int + count_four_int + count_five_int));

                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data != null && data.length()>0) {
                        JSONArray list_Warning = data.getJSONArray(0);
                        JSONArray list_NewShare = data.getJSONArray(1);
                        JSONArray list_Inform = data.getJSONArray(2);

                        //通知公告
                        if (list_Inform != null && list_Inform.length() > 0) {
                            for (int j = 0; j < list_Inform.length(); j++) {
                                String token_Inform = list_Inform.getJSONObject(0).optString("TITLE");
                                String push_time_Inform = list_Inform.getJSONObject(0).optString("PUSH_TIME");

                                if (token_Inform != null) {
                                    MyI_item_tv1.setText(token_Inform);
                                } else {
                                    MyI_item_tv1.setText(getString(R.string.MyInformationText4));
                                }
                                SpUtils.putString(MyNewsActivity.this,"inform_push_time",push_time_Inform);

                                MyI_date1.setText(push_time_Inform);
                            }
                        }
                        //新股申购通知
                        if (list_NewShare != null && list_NewShare.length() > 0) {
                            for (int x = 0; x < list_NewShare.length(); x++) {
                                String token_Newshare = list_NewShare.getJSONObject(0).optString("TITLE");
                                String push_time_Newshare = list_NewShare.getJSONObject(0).optString("PUSH_TIME");

                                if (token_Newshare != null) {
                                    MyI_item_tv2.setText(token_Newshare);
                                } else {
                                    MyI_item_tv2.setText(getString(R.string.MyInformationText4));
                                }
                                SpUtils.putString(MyNewsActivity.this,"newshare_push_time",push_time_Newshare);
                                MyI_date2.setText(push_time_Newshare);
                            }
                        }
                        //行情预警通知
                        if (list_Warning != null && list_Warning.length() > 0) {
                            for (int v = 0; v < list_Warning.length(); v++) {
                                String token_Warning = list_Warning.getJSONObject(0).optString("TITLE");
                                String push_time_Warning = list_Warning.getJSONObject(0).optString("PUSH_TIME");

                                if (token_Warning != null) {
                                    MyI_item_tv3.setText(token_Warning);
                                } else {
                                    MyI_item_tv3.setText(getString(R.string.MyInformationText4));
                                }

                                SpUtils.putString(MyNewsActivity.this,"warning_push_time",push_time_Warning);
                                MyI_date3.setText(push_time_Warning);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_myinformation;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.MyI_back:
                finish();
                break;
            case R.id.MyI_RL1:
                intent.setClass(this, InformActivity.class);
                intent.putExtra("token_Inform", MyI_div_num1.getText().toString());
                startActivity(intent);
                break;
            case R.id.MyI_RL2:
                intent.setClass(this, NewStockTipsActivity.class);
                intent.putExtra("token_Newshare", MyI_div_num2.getText().toString());
                startActivity(intent);
                break;
            case R.id.MyI_RL3:
                intent.setClass(this, PricesPromptActivity.class);
                intent.putExtra("token_Warning", MyI_div_num3.getText().toString());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String mDivnum1 = SpUtils.getString(this, "mDivnum_InformActivity", null);
        String mDivnum2 = SpUtils.getString(this, "mDivnum_NewSharesTips", null);
        String mDivnum_PricesPrompt = SpUtils.getString(this, "mDivnum_PricesPrompt", null);

        if (mDivnum1.equals("true")) {
            MyI_div_num1.setText("");
            MyI_div_num1.setVisibility(View.GONE);
        }
        if (mDivnum2.equals("true")) {
            MyI_div_num2.setText("");
            MyI_div_num2.setVisibility(View.GONE);
        }
        if (mDivnum_PricesPrompt.equals("true")) {
            MyI_div_num3.setText("");
            MyI_div_num3.setVisibility(View.GONE);
        }


    }
}
