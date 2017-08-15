package com.tpyzq.mobile.pangu.activity.market.selfChoice.childNews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.NewsInofEntity;
import com.tpyzq.mobile.pangu.db.Db_HOME_INFO;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.http.doConnect.self.SelfChoiceStockNews;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToSelfChoiceStockNews;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.pulllayou.PullLayout;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/8/22.
 * 自选股新闻
 */
public class SelfChoiceNewsTab extends BaseTabPager implements
        AdapterView.OnItemClickListener, ICallbackResult, View.OnClickListener, PullLayout.OnPullCallBackListener {

    private SimpleRemoteControl mSimpleRemoteControl;
    private static final String TAG = "SelfChoiceNewsTab";
    private SelfNewsAdapter mSelfNewsAdapter;
    private Activity mActivity;
    private Dialog mLoadDialog;
    private ArrayList<NewsInofEntity> mEntities;
    private PullLayout mPullLayout;
    private int mPagerNum = 0;
    private FrameLayout mKongLayout;
    private boolean doReFrushing;

    public SelfChoiceNewsTab(Activity activity, ArrayList<BaseTabPager> tabs) {
        super(activity, tabs);
        this.mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity) {
        mLoadDialog = LoadingDialog.initDialog(activity, "请稍等...");

        mActivity = activity;
        mEntities = new ArrayList<>();
        mSimpleRemoteControl = new SimpleRemoteControl(this);
        mPullLayout = (PullLayout) view.findViewById(R.id.selfchoiceNewPullDownScroll);
        mKongLayout = (FrameLayout) view.findViewById(R.id.stockNewsLayout);
        mKongLayout.setOnClickListener(this);
        ListView listView = (ListView) view.findViewById(R.id.selfNewsList);
        mSelfNewsAdapter = new SelfNewsAdapter();
        listView.setAdapter(mSelfNewsAdapter);
        listView.setOnItemClickListener(this);
        mPullLayout.setOnPullListener(this);
        mKongLayout.setVisibility(View.VISIBLE);
        requestSelfChoiceNews(String.valueOf(mPagerNum));
//        initSelfChoiceNewsList(); //从数据库中取值
    }

    @Override
    public void onRefresh() {
        mPagerNum = 0;
        doReFrushing = true;
        requestSelfChoiceNews(String.valueOf(mPagerNum));
    }

    @Override
    public void onLoad() {
        mPagerNum = mPagerNum + 10;
        requestSelfChoiceNews(String.valueOf(mPagerNum));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mActivity, NewsDetailActivity.class);
        intent.putExtra("requestId", mEntities.get(position).getId());
        mActivity.startActivity(intent);
    }

    private void requestSelfChoiceNews(String pagerNum) {
        int count = Db_PUB_STOCKLIST.getStockListCount();
        if (mSimpleRemoteControl != null && count > 0) {
            mSimpleRemoteControl.setCommand(new ToSelfChoiceStockNews(new SelfChoiceStockNews(TAG, pagerNum, null)));
            mSimpleRemoteControl.startConnect();
        } else {
            mEntities.clear();
            mSelfNewsAdapter.setDatas(mEntities);
            if (null!=mPullLayout) {
                mPullLayout.finishPull();
            }
            mKongLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void myTabonResume() {
        LogUtil.e("2222222222222");
        initSelfChoiceNewsList();
    }

    @Override
    public void toRunConnect() {
        LogUtil.e("1111111111111111111");
        requestSelfChoiceNews(String.valueOf(mPagerNum));
    }

    private void initSelfChoiceNewsList() {

        mEntities = Db_HOME_INFO.queryAllSelfNews();

        if (mEntities != null && mEntities.size() > 0)  {
            mKongLayout.setVisibility(View.GONE);
        } else {
            mKongLayout.setVisibility(View.VISIBLE);
        }
        mSelfNewsAdapter.setDatas(mEntities);
    }

    @Override
    public void onHiddenChanged(boolean hided) {
        super.onHiddenChanged(hided);
        LogHelper.e(TAG,"onHiddenChanged hided:"+hided);
    }

    @Override
    public void getResult(Object result, String tag) {
        if ("SelfChoiceStockNews".equals(tag)) {
            mKongLayout.setVisibility(View.GONE);
            if (mPullLayout != null) {
                mPullLayout.finishPull();
            }
            if (mLoadDialog != null) {
                mLoadDialog.dismiss();
            }
            if (null == result ||result instanceof String) {
                if (mEntities == null || mEntities.size() <= 0 ) {
                    mKongLayout.setVisibility(View.VISIBLE);
                }
//                CentreToast.showText(CustomApplication.getContext(), (String)result);
                return;
            }
            ArrayList<NewsInofEntity> entities = (ArrayList<NewsInofEntity>) result;
            if(entities != null && entities.size() > 0){
                if(mPagerNum == 0){
                    Db_HOME_INFO.deleteAllSelfNewsDatas();
                    Db_HOME_INFO.addStockListDatas(entities);
                    mEntities.clear();
                }
                mEntities.addAll(entities);
            }
            mSelfNewsAdapter.setDatas(mEntities);
        }
    }

    @Override
    public void toStopConnect() {
//        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_choice_news;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stockNewsLayout:
                requestSelfChoiceNews("0");
                break;
        }
    }
}
