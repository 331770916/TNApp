package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.myself.StockHolderInfoAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.StockHolderInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/9/2.
 * 股东资料
 */
public class StockHolderInfoActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "Information";
    private StockHolderInfoEntity entrust;
    private PullToRefreshListView mInformationListView;
    private StockHolderInfoAdapter mAdapter;
    private ImageView iv_isEmpty;
    private List<StockHolderInfoEntity> beans;

    @Override
    public void initView() {
        findViewById(R.id.ivCurrencyFundRedeem_back).setOnClickListener(this);
        mInformationListView = (PullToRefreshListView) findViewById(R.id.InformationListView);
        iv_isEmpty = (ImageView) findViewById(R.id.iv_isEmpty);
        mAdapter = new StockHolderInfoAdapter(this);
        toConnect();

        mInformationListView.setAdapter(mAdapter);
        mInformationListView.setEmptyView(iv_isEmpty);

        mInformationListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置尾布局样式文字
                mInformationListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mInformationListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                mInformationListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1500);
                            beans.clear();
                            toConnect();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        //将下拉视图收起
                        mInformationListView.onRefreshComplete();
                    }
                }.execute();
            }
        });
    }

    private void toConnect() {
        String mSession = SpUtils.getString(this, "mSession", "");
        final HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "700070");
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(StockHolderInfoActivity.this,"网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("code"))) {
                        beans = new ArrayList<StockHolderInfoEntity>();
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                StockHolderInfoEntity _bean = new StockHolderInfoEntity();
                                _bean.setCustomerCode(data.getJSONObject(i).getString("FUND_ACCOUNT"));    // 资金账号
                                _bean.setAccountType(data.getJSONObject(i).getString("MARKET"));        //市场
                                _bean.setShareholderSName(data.getJSONObject(i).getString("SECU_NAME"));//股票名称
                                _bean.setShareholderSCode(data.getJSONObject(i).getString("SECU_CODE"));//股票代码
                                beans.add(_bean);
                            }
                            mAdapter.setData(beans);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        startActivity(new Intent(StockHolderInfoActivity.this, TransactionLoginActivity.class));
                    } else {
                        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(jsonObject.getString("msg"),CustomCenterDialog.SHOWCENTER);
                        customCenterDialog.show(getFragmentManager(),StockHolderInfoActivity.class.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Helper.getInstance().showToast(StockHolderInfoActivity.this,"网络异常");
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_information;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCurrencyFundRedeem_back:
                finish();
                break;
        }
    }
}
