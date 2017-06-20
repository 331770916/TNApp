package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.presenter.ReverseRepoGuideActivityPresenter;
import com.tpyzq.mobile.pangu.base.BaseActivity;

import java.util.List;



/**
 * 逆回购
 */
public class ReverseRepoGuideActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_using_money;
    private ImageButton ib_search;
    private ImageButton ib_start;
    private ImageButton ib_backbill;
    private ImageView iv_back;

    ReverseRepoGuideActivityPresenter presenter;

    @Override
    public void initView() {
        tv_using_money = (TextView) findViewById(R.id.tv_using_money);
        ib_search = (ImageButton) findViewById(R.id.ib_search);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ib_start = (ImageButton) findViewById(R.id.ib_start);
        ib_backbill = (ImageButton) findViewById(R.id.ib_backbill);
        initData();
    }

    private void initData() {
        ib_search.setOnClickListener(this);
        ib_start.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        ib_backbill.setOnClickListener(this);
        presenter = new ReverseRepoGuideActivityPresenter(this);
        presenter.getUseMoney();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_reverse_repo_guide;
    }

    public void setMoney(String s) {
        tv_using_money.setText(s);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ib_search:
                intent.setClass(this, ReferActivity.class);
                break;
            case R.id.ib_start:
                intent.setClass(this, TraChooseBreedActivity.class);
                break;
            case R.id.ib_backbill:
                intent.setClass(this, RevokeActivity.class);
                break;
        }
        if (isIntentAvailable(this, intent)) {
            startActivity(intent);
        }
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
