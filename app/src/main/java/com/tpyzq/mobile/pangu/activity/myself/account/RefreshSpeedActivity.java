package com.tpyzq.mobile.pangu.activity.myself.account;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.util.ColorUtils;


/**
 *  刷新速度设置界面
 */

public class RefreshSpeedActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_two,tv_three;
    private ImageView iv_time1, iv_time2, iv_time3;
    private RelativeLayout rl_time1, rl_time2, rl_time3;
    private ImageView iv_back;
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_two = (TextView) findViewById(R.id.tv_two);
        tv_three = (TextView) findViewById(R.id.tv_three);
        iv_time1 = (ImageView) findViewById(R.id.iv_time1);
        iv_time2 = (ImageView) findViewById(R.id.iv_time2);
        iv_time3 = (ImageView) findViewById(R.id.iv_time3);
        rl_time1 = (RelativeLayout) findViewById(R.id.rl_time1);
        rl_time2 = (RelativeLayout) findViewById(R.id.rl_time2);
        rl_time3 = (RelativeLayout) findViewById(R.id.rl_time3);
        initData();
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        rl_time1.setOnClickListener(this);
        rl_time2.setOnClickListener(this);
        rl_time3.setOnClickListener(this);
        if (TextUtils.isEmpty(Db_PUB_USERS.searchRefreshTime())) {
            Db_PUB_USERS.updateRefreshTime("2");
            iv_time2.setVisibility(View.VISIBLE);
        } else {
            switch (Db_PUB_USERS.searchRefreshTime()) {
                case "1":
                    iv_time1.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    iv_time2.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    iv_time3.setVisibility(View.VISIBLE);
                    break;
            }
        }
        String time = "2秒(默认)";
        SpannableString sp = new SpannableString(time);
        sp.setSpan(new ForegroundColorSpan(ColorUtils.BLACK), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(ColorUtils.TEXT), 2, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_two.setText(sp);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_refresh_speed;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_time1:
                iv_time1.setVisibility(View.VISIBLE);
                iv_time2.setVisibility(View.INVISIBLE);
                iv_time3.setVisibility(View.INVISIBLE);
                Db_PUB_USERS.updateRefreshTime("1");
                break;
            case R.id.rl_time2:
                iv_time1.setVisibility(View.INVISIBLE);
                iv_time2.setVisibility(View.VISIBLE);
                iv_time3.setVisibility(View.INVISIBLE);
                Db_PUB_USERS.updateRefreshTime("2");
                break;
            case R.id.rl_time3:
                iv_time1.setVisibility(View.INVISIBLE);
                iv_time2.setVisibility(View.INVISIBLE);
                iv_time3.setVisibility(View.VISIBLE);
                Db_PUB_USERS.updateRefreshTime("3");
                break;
        }
    }
}
