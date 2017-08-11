package com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock.remain;

import android.app.Dialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.SubSelfChoiceEntity;
import com.tpyzq.mobile.pangu.http.doConnect.self.DeleteRemainStockPriceConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.QueryRemainStockPriceConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToDeleteRemainStockPriceConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToQueryRemainStockPriceConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.util.List;

/**
 * Created by zhangwenbo on 2016/11/7.
 * 我的提醒
 */
public class MySelfRemainFragment extends BaseFragment implements ICallbackResult,
        SwipeRefreshLayout.OnRefreshListener, MySelfRemainAdapter.DeleteRemainListener{

    private SimpleRemoteControl mSimpleRemoteControl;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MySelfRemainAdapter mAdapter;
    private List<SubSelfChoiceEntity> mDatas;
    private ImageView mKongIv;
    private int mDeletePosition;
    private Dialog mLoadDialog;
    private static final String TAG = "MySelfRemainFragment";

    @Override
    public void initView(View view) {

        mSimpleRemoteControl = new SimpleRemoteControl(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.remainRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mKongIv = (ImageView) view.findViewById(R.id.iv_remain_kong);
        ListView listView = (ListView) view.findViewById(R.id.remainSelfListView);
        mAdapter = new MySelfRemainAdapter(this);
        listView.setAdapter(mAdapter);

        if (mLoadDialog == null) {
            mLoadDialog = LoadingDialog.initDialog(getActivity(), "正在加载");
        }

        mLoadDialog.show();
        if (mSimpleRemoteControl != null) {
            mSimpleRemoteControl.setCommand(new ToQueryRemainStockPriceConnect(new QueryRemainStockPriceConnect(TAG, "", UserUtil.userId)));
            mSimpleRemoteControl.startConnect();
        }
    }

    @Override
    public void onRefresh() {
        if (mSimpleRemoteControl != null) {
            mKongIv.setVisibility(View.GONE);
            mSimpleRemoteControl.setCommand(new ToQueryRemainStockPriceConnect(new QueryRemainStockPriceConnect(TAG, "", UserUtil.userId)));
            mSimpleRemoteControl.startConnect();
        }

    }
    @Override
    public void getResult(Object result, String tag) {

        mLoadDialog.dismiss();
        mSwipeRefreshLayout.setRefreshing(false);
        mKongIv.setVisibility(View.GONE);
        if ("QueryRemainStockPriceConnect".equals(tag)) {

            if (result instanceof String) {
                mKongIv.setVisibility(View.VISIBLE);
                CentreToast.showText(getActivity(), ConstantUtil.NETWORK_ERROR);
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }

            mDatas = (List<SubSelfChoiceEntity>) result;

            if (mDatas == null || mDatas.size() <= 0) {
                mKongIv.setVisibility(View.VISIBLE);
                return ;
            }

            mAdapter.setDatas(mDatas);

        } else if ("DeleteRemainStockPriceConnect".equals(tag)) {

            String strResult = (String) result;

            if (!TextUtils.isEmpty(strResult) && strResult.contains("成功")) {
                CentreToast.showText(getActivity(),"删除成功",true);
                mDatas.remove(mDeletePosition);

                if (mDatas == null || mDatas.size() <= 0) {
                    mKongIv.setVisibility(View.VISIBLE);
                }

                mAdapter.setDatas(mDatas);
            } else {
                CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog("" + strResult,CustomCenterDialog.SHOWCENTER);
                customCenterDialog.show(getActivity().getFragmentManager(),MySelfRemainFragment.class.toString());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void deleteRemain(int position) {
        mDeletePosition = position;
        if (mSimpleRemoteControl != null) {
            mSimpleRemoteControl.setCommand(new ToDeleteRemainStockPriceConnect(new DeleteRemainStockPriceConnect(TAG, "", UserUtil.userId, mDatas.get(position).getCODE(), mDatas.get(position).getRemainType())));
            mSimpleRemoteControl.startConnect();
        }


    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            LogUtil.e(TAG, "MySelfRemainFragment   ----   onHiddenChanged()");
            if (mSimpleRemoteControl != null) {
                mSimpleRemoteControl.setCommand(new ToQueryRemainStockPriceConnect(new QueryRemainStockPriceConnect(TAG, "", UserUtil.userId)));
                mSimpleRemoteControl.startConnect();
            }
        }
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.remain_myself_tab;
    }
}
