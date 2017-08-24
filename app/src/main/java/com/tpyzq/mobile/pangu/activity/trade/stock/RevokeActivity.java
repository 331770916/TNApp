package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.RevokeAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.RevokeEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.RevokeDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * Created by wangqi on 2016/9/7.
 * 股票撤单
 */
public class RevokeActivity extends BaseActivity implements AdapterView.OnItemClickListener, RevokeDialog.Expression, View.OnClickListener {
    private static String TAG = "Revoke";
    private String mSession;
    private RevokeAdapter adapter;
    private List<RevokeEntity> beans;
    private PullToRefreshListView mRKListView = null;
    private ImageView iv_isEmpty;

    @Override
    public void initView() {
        beans = new ArrayList<>();
        findViewById(R.id.RKpublish_back).setOnClickListener(this);
        mRKListView = (PullToRefreshListView) findViewById(R.id.RKListView);
        mRKListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        iv_isEmpty = (ImageView) findViewById(R.id.iv_isEmpty);

        adapter = new RevokeAdapter(this);
        toConnect();
        mRKListView.setAdapter(adapter);
//        mRKListView.setEmptyView(MyRelativeLayout);
        mRKListView.setEmptyView(iv_isEmpty);
        mRKListView.setOnItemClickListener(this);

        mRKListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置尾布局样式文字
                mRKListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mRKListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                mRKListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

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
                        mRKListView.onRefreshComplete();
                    }
                }.execute();
            }
        });

    }


    /**
     * 网络请求
     */
    private void toConnect() {
        String mSession1 = SpUtils.getString(this, "mSession", "");
        if (mSession1.equals("") && mSession1 == null && mSession1 == "") {
            mSession = "";
        } else {
            mSession = mSession1;
        }

        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "300160");//150
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        map1.put("FUND_ACCOUNT", "true");
        map1.put("ACTION_IN", "1");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                CentreToast.showText(RevokeActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("code"))) {


                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                RevokeEntity _bean = new RevokeEntity();
                                _bean.setCode(data.optJSONObject(i).optString("SECU_CODE"));
                                _bean.setName(data.getJSONObject(i).getString("SECU_NAME"));
                                _bean.setTitm(data.getJSONObject(i).getString("ORDER_TIME"));
                                _bean.setPrice(data.getJSONObject(i).getString("PRICE"));
                                _bean.setMatchedPrice(data.getJSONObject(i).getString("MATCHED_PRICE"));
                                String  QTY=data.getJSONObject(i).getString("QTY");
                                if (!"0".equals(QTY)) {
                                    int qty_idx = QTY.lastIndexOf(".");
                                    String QTY_New = QTY.substring(0, qty_idx);
                                    _bean.setWithdrawnQty(QTY_New);
                                } else {
                                    _bean.setWithdrawnQty(QTY);
                                }
                                _bean.setMatchedQty(data.getJSONObject(i).getString("MATCHED_QTY"));
                                _bean.setEntrustStatus(data.getJSONObject(i).getString("ENTRUST_STATUS"));
                                _bean.setEntrustBs(data.getJSONObject(i).getString("ENTRUST_BS"));
                                _bean.setEntrusNo(data.getJSONObject(i).getString("ENTRUST_NO"));
                                beans.add(_bean);
                            }
                            adapter.setData(beans);
                        }
                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        startActivity(new Intent(RevokeActivity.this, TransactionLoginActivity.class));
                    } else {
                        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(jsonObject.getString("msg"),CustomCenterDialog.SHOWCENTER);
                        customCenterDialog.show(getFragmentManager(),RevokeActivity.class.toString());
                    }
                } catch (JSONException e) {
                    CentreToast.showText(RevokeActivity.this,ConstantUtil.JSON_ERROR);
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_revok;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String Name = beans.get(position - 1).getName();
        String Titm = beans.get(position - 1).getTitm();

        String Price = beans.get(position - 1).getPrice();
        String WithdrawnQty = beans.get(position - 1).getWithdrawnQty();

        String EntrustBs = beans.get(position - 1).getEntrustBs();

        String EntrusNo = beans.get(position - 1).getEntrusNo();

        RevokeDialog dialog = new RevokeDialog(this, Name, Titm, Price, WithdrawnQty, EntrustBs, EntrusNo, this);

        dialog.show();
    }

    @Override
    public void State() {
//        beans.remove(state);
        beans.clear();
        CentreToast.showText(this,"委托已提交",true);
        toConnect();
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RKpublish_back:
                finish();
                break;
//            case R.id.MyRelativeLayout:
//                dialog = LoadingDialogTwo.getInstance().initDialog(Revoke.this);  //菊花
//                dialog.show();
//                toConnect();
//                break;

        }
    }

}
