package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.FJWithdrawOrderAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.dialog.StructuredFundDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * 分级基金撤单
 */

public class FJWithdrawOrderActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, StructuredFundDialog.Expression {
    public static final String TAG = "FJWithdrawOrder";
    private ListView mListView;
    private List<StructuredFundEntity> mList;
    private FJWithdrawOrderAdapter mFjwithdrawOrderAdapter;


    @Override
    public void initView() {
        mList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.mListView);
        ImageView image = (ImageView) findViewById(R.id.detail_back);
        image.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        mFjwithdrawOrderAdapter = new FJWithdrawOrderAdapter(this, mList);
        mListView.setAdapter(mFjwithdrawOrderAdapter);
        requestData();
        //  假数据
        for (int i = 0; i < 10; i++) {
            StructuredFundEntity structruedFundEntity = new StructuredFundEntity();
            structruedFundEntity.setExchange_type("ll00089" + i);
            structruedFundEntity.setFund_status("ll0009808" + i);
            structruedFundEntity.setMerge_amount("" + i);
            structruedFundEntity.setSplit_amount("ll00078" + i);
            structruedFundEntity.setStoken_name("ll0008" + i);
            structruedFundEntity.setStock_account("ll0008" + i);
            mList.add(structruedFundEntity);
        }
        mFjwithdrawOrderAdapter.notifyDataSetChanged();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
//        map2.put("SEC_ID", "tpyzq");
//        map2.put("FLAG", "true");
//        map2.put("SECU_CODE", "");
//        map2.put("OPEN_TYPE", "1");
//        map1.put("funcid", "300198");
//        map1.put("token", "");
//        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPost(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fj_withdraw_order;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StructuredFundEntity entity = mList.get(position);
        StructuredFundDialog dialog = new StructuredFundDialog(this, TAG, this, entity, null, null);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_back:
                finish();
                break;
        }
    }

    @Override
    public void State() {
        Helper.getInstance().showToast(this, "撤销此委托成功");
    }
}
