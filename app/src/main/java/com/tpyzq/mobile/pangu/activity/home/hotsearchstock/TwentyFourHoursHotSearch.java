package com.tpyzq.mobile.pangu.activity.home.hotsearchstock;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.adapter.home.HotSearchAdapter;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToTwentyFourHoursHotSearch;
import com.tpyzq.mobile.pangu.http.doConnect.home.TwentyFourHoursHotSearchConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.util.ArrayList;

/**
 * Created by wangqi on 2016/12/28.
 * 24小时热搜
 */

public class TwentyFourHoursHotSearch extends BaseFragment implements AdapterView.OnItemClickListener, ICallbackResult {

    public static final String TAG = "TwentyFourHoursHotSearchConnect";

    private JumpPageListener mJumpPageListener;
    private PullToRefreshListView rs_listView;
    private ImageView mEmpty;
    private HotSearchAdapter mAdapter;
    private ArrayList<StockInfoEntity> mBeans;
//    private ArrayList<StockInfoEntity> mEntities;
    private boolean mIs;
    private Dialog mLoadDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(TwentyFourHoursHotSearch.this);
                    simpleRemoteControl.setCommand(new ToTwentyFourHoursHotSearch(new TwentyFourHoursHotSearchConnect(getContext(), TAG, "1")));
                    simpleRemoteControl.startConnect();
                    break;
            }
        }
    };


    @Override
    public void initView(View view) {
        mBeans = new ArrayList<>();
        rs_listView = (PullToRefreshListView) view.findViewById(R.id.RS_ListView);
        mEmpty = (ImageView) view.findViewById(R.id.iv_isEmpty);
        rs_listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        rs_listView.setOnItemClickListener(this);
        mAdapter = new HotSearchAdapter(getContext());
        rs_listView.setAdapter(mAdapter);
        rs_listView.setEmptyView(mEmpty);
        Renovate();
        toConnect();
    }

    private void Renovate() {
        rs_listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置尾布局样式文字
                rs_listView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                rs_listView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                rs_listView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1500);
//                            mIs = true;
                            handler.sendEmptyMessage(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        //将下拉视图收起
                        rs_listView.onRefreshComplete();
                    }
                }.execute();
            }
        });
    }


    /**
     * 网络链接
     * MOST_TYPE: 1.热门浏览 2.热门浏览 3.自选股热门入选
     */
    private void toConnect() {
        mLoadDialog = LoadingDialog.initDialog(getActivity(), "正在加载...");
        if (!getActivity().isFinishing()) {
            mLoadDialog.show();
        }
        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
        simpleRemoteControl.setCommand(new ToTwentyFourHoursHotSearch(new TwentyFourHoursHotSearchConnect(getContext(), TAG, "1")));
        simpleRemoteControl.startConnect();

    }


    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_resou;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), StockDetailActivity.class);

        StockDetailEntity data = new StockDetailEntity();
        data.setStockName(mBeans.get(position - 1).getStockName());
        data.setStockCode(mBeans.get(position - 1).getStockNumber());
        intent.putExtra("stockIntent", data);
        startActivity(intent);
    }


    @Override
    public void getResult(Object result, String tag) {
        if (mLoadDialog != null ) {
            mLoadDialog.dismiss();
        }
        if(result instanceof String){
            return;
        } else if (TwentyFourHoursHotSearch.TAG.equals(tag)) {
            mBeans = (ArrayList<StockInfoEntity>) result;
//            if (mIs) {
//                mBeans.clear();
//                mIs = true;
//            }
//            mBeans.addAll(mEntities);
            mAdapter.setData(mBeans);

        }
    }

    public void setJumPageListener(JumpPageListener listener) {
        mJumpPageListener = listener;
    }


    public interface JumpPageListener {
        void onCheckedChanged(int position);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            toConnect();
        }


    }

    @Override
    public void onDestroy() {
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
        super.onDestroy();

    }
}
