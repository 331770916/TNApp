package com.tpyzq.mobile.pangu.activity.trade.margin_trading;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;

/**
 * Created by ltyhome on 14/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:融资融券 撤单
 */

public class RevokeOrderActivity extends BaseActivity implements  AdapterView.OnItemClickListener,View.OnClickListener{
    private PullToRefreshListView mRKListView = null;
    private ImageView iv_isEmpty;
    @Override
    public void initView() {
        findViewById(R.id.RKpublish_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.revokTitle1)).setText("名称/代码");
        ((TextView)findViewById(R.id.revokTitle2)).setText("买卖/状态");
        ((TextView)findViewById(R.id.revokTitle3)).setText("委托量/成交量");
        ((TextView)findViewById(R.id.revokTitle4)).setText("委托价");
        mRKListView = (PullToRefreshListView) findViewById(R.id.RKListView);
        mRKListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        iv_isEmpty = (ImageView) findViewById(R.id.iv_isEmpty);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.RKpublish_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_revok;
    }
}
