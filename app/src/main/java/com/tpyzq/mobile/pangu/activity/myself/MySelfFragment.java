package com.tpyzq.mobile.pangu.activity.myself;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.account.MyNewsActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.SettingActivity;
import com.tpyzq.mobile.pangu.activity.myself.view.AccountPager;
import com.tpyzq.mobile.pangu.activity.myself.view.BaseMySelfPager;
import com.tpyzq.mobile.pangu.activity.myself.view.BursePager;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dragindicator.DragIndicatorView;
import com.tpyzq.mobile.pangu.view.radiobutton.MyRadioButton;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/9/19.
 * 我的Fragment
 */
public class MySelfFragment extends BaseFragment
        implements View.OnClickListener, CustomApplication.GetMessageListenr
{
    private MyRadioButton rb_burse, rb_account;
    private RadioGroup rg_myself;
    private Context context;
    private FrameLayout fl_basepager_content;
    private ImageView iv_setting, bianji;
    private DragIndicatorView div_num;
    private ArrayList<BaseMySelfPager> baseMySelfPager;


    //推送消息 内容,时间
    private int count_int;
    private String count_count;

    @Override
    public void initView(View view) {
        fl_basepager_content = (FrameLayout) view.findViewById(R.id.fl_basepager_content);
        rg_myself = (RadioGroup) view.findViewById(R.id.rg_myself);
        div_num = (DragIndicatorView) view.findViewById(R.id.div_num);
        rb_account = (MyRadioButton) view.findViewById(R.id.rb_account);
        rb_burse = (MyRadioButton) view.findViewById(R.id.rb_burse);
        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);
        bianji = (ImageView) view.findViewById(R.id.bianji);
        initData();
    }

    private void initData() {
        context = getActivity();
        baseMySelfPager = new ArrayList<>();
        baseMySelfPager.add(new AccountPager(context));
        baseMySelfPager.add(new BursePager(context));
        setInfoNum();
        CustomApplication.setGetMessageListenr(this);
        rb_account.setChecked(true);
        fl_basepager_content.addView(new AccountPager(context).rootView);
        rg_myself.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fl_basepager_content.removeAllViews();
                switch (checkedId) {
                    case R.id.rb_burse:
                        BRutil.menuSelect("M001");
                        SpUtils.putBoolean(context, "burse", true);
                        SpUtils.putBoolean(context, "account", false);
                        AccountPager accountPager = (AccountPager) baseMySelfPager.get(0);
                        fl_basepager_content.addView(accountPager.rootView);
                        accountPager.refreshView();
                        break;
                    case R.id.rb_account:
                        BRutil.menuSelect("K001");
                        SpUtils.putBoolean(context, "burse", false);
                        SpUtils.putBoolean(context, "account", true);
                        BursePager bursePager = (BursePager) baseMySelfPager.get(1);
                        fl_basepager_content.addView(bursePager.rootView);
                        bursePager.refreshView();
                        break;
                }
            }
        });
        iv_setting.setOnClickListener(this);
        bianji.setOnClickListener(this);

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


        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_HQ_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e("", e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    //总未读条数
                    count_count = jsonObject.optString("count_count");

                    if (!count_count.equals("") && !count_count.equals("0")) {
                        div_num.setText(count_count);
                        div_num.setVisibility(View.VISIBLE);
                        div_num.setOnDismissAction(new DragIndicatorView.OnIndicatorDismiss() {
                            @Override
                            public void OnDismiss(DragIndicatorView view) {

                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private boolean flag = false;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        flag = hidden;
        if (!hidden){
            fl_basepager_content.removeAllViews();
            if (rb_burse.isChecked()) {
                AccountPager accountPager = (AccountPager) baseMySelfPager.get(0);
                fl_basepager_content.addView(accountPager.rootView);
                accountPager.refreshView();
            } else {
                BursePager bursePager = (BursePager) baseMySelfPager.get(1);
                fl_basepager_content.addView(bursePager.rootView);
                bursePager.refreshView();
            }
            boolean account = SpUtils.getBoolean(context, "account", false);
            boolean burse = SpUtils.getBoolean(context, "burse", false);
            if (account) {
                rb_account.setChecked(true);
            }
            if (burse) {
                rb_burse.setChecked(true);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!flag){
            fl_basepager_content.removeAllViews();
            if (rb_burse.isChecked()) {
                AccountPager accountPager = (AccountPager) baseMySelfPager.get(0);
                fl_basepager_content.addView(accountPager.rootView);
                accountPager.refreshView();
            } else {
                BursePager bursePager = (BursePager) baseMySelfPager.get(1);
                fl_basepager_content.addView(bursePager.rootView);
                bursePager.refreshView();
            }
            boolean account = SpUtils.getBoolean(context, "account", false);
            boolean burse = SpUtils.getBoolean(context, "burse", false);
            if (account) {
                rb_account.setChecked(true);
            }
            if (burse) {
                rb_burse.setChecked(true);
            }
        }
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_myself;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_setting:
                intent.setClass(context, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.bianji:
                intent.setClass(context, MyNewsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void getMessage(int message) {
        if (!TextUtils.isEmpty(count_count)) {
            count_int = Integer.valueOf(count_count) + message;
            div_num.setText(count_int + "");
            if (message != 0) {
                div_num.setVisibility(View.VISIBLE);
            } else {
                div_num.setVisibility(View.GONE);
            }
            div_num.setOnDismissAction(new DragIndicatorView.OnIndicatorDismiss() {
                @Override
                public void OnDismiss(DragIndicatorView view) {

                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        String a = SpUtils.getString(context, "mDivnum", null);
        if ("true".equals(a)) {
            count_int = 0;
            div_num.setVisibility(View.GONE);
        }
    }
}
