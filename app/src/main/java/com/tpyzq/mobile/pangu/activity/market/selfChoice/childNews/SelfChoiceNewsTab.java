package com.tpyzq.mobile.pangu.activity.market.selfChoice.childNews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.newsTab.DetailNewsListActivity;
import com.tpyzq.mobile.pangu.activity.home.information.NewsDetailActivity;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.NewsInofEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
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
import java.util.List;


/**
 * Created by zhangwenbo on 2016/8/22.
 * 自选股新闻
 */
public class SelfChoiceNewsTab extends BaseTabPager implements
        AdapterView.OnItemClickListener, InterfaceCollection.InterfaceCallback, View.OnClickListener, PullLayout.OnPullCallBackListener {

    private static final String TAG = "SelfChoiceNewsTab";
    private SelfNewsAdapter mSelfNewsAdapter;
    private Activity mActivity;
    private Dialog mLoadDialog;
    private ArrayList<NewsInofEntity> mEntities;
    private PullLayout mPullLayout;
    private int mPagerNum = 1;
    private FrameLayout mKongLayout;

    public SelfChoiceNewsTab(Activity activity, ArrayList<BaseTabPager> tabs) {
        super(activity, tabs);
        this.mActivity = activity;
    }

    @Override
    public void initView(View view, Activity activity) {
        mLoadDialog = LoadingDialog.initDialog(activity, "请稍等...");

        mActivity = activity;
        mEntities = new ArrayList<>();
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

    private String getStockCodeList() {
        List<StockInfoEntity> datas = Db_PUB_STOCKLIST.queryStockListDatas();
        if (datas != null && datas.size() > 0) {
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < datas.size(); i++) {
                if (i < 10) {
                    String stockNumber = datas.get(i).getStockNumber();
                    if (!TextUtils.isEmpty(stockNumber) && !stockNumber.startsWith("10") && !stockNumber.startsWith("20")) {
                        String _stockNumber = stockNumber;
                        if (i == 9) {
                            sb1.append(_stockNumber).append("");
                        } else {
                            sb1.append(_stockNumber).append(",");
                        }
                    }
                }
            }

            String result = sb1.toString();

            if (TextUtils.isEmpty(result) || result.length() <= 0) {
                return "";
            }

            String reglex  = result.substring(result.length() - 1, result.length());
            if (",".equals(reglex)) {
                result = result.substring(0, result.length() - 1);
            }

            return result;
        }

        return "";
    }

    @Override
    public void onRefresh() {
        mPagerNum = 1;
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
        String codes = getStockCodeList();
        if (count > 0) {
            mInterface.queryStockNews(codes,"30", pagerNum,TAG,this);
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
    public void callResult(ResultInfo info) {
        mKongLayout.setVisibility(View.GONE);
        if (mPullLayout != null) {
            mPullLayout.finishPull();
        }
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
        if (null == info.getData()) {
            if (mEntities == null || mEntities.size() <= 0 ) {
                mKongLayout.setVisibility(View.VISIBLE);
            }
//                CentreToast.showText(CustomApplication.getContext(), (String)result);
            return;
        }


        List<InformationEntity> data = (List<InformationEntity>)info.getData();

        if (data == null || data.size() <= 0) {
            return;
        }

        ArrayList<NewsInofEntity> entities = new ArrayList<>();
        for (InformationEntity informationEntity : data) {
            NewsInofEntity newsInofEntity = new NewsInofEntity();
            newsInofEntity.setId(informationEntity.getNewsno());
            newsInofEntity.setComp(informationEntity.getSecuAbbr());
            newsInofEntity.setTick(informationEntity.getSecuCode());
            newsInofEntity.setDate(informationEntity.getTime());
            newsInofEntity.setAuth(informationEntity.getSource());
            newsInofEntity.setTitle(informationEntity.getTitle());
            newsInofEntity.setSum(informationEntity.getDigest());
            newsInofEntity.setStockCode(informationEntity.getSecuCode());
            entities.add(newsInofEntity);
        }

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
