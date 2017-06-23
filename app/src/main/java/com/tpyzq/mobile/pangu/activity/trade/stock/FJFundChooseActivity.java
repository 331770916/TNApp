package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.FJFundChooseAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.StructuredFundEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * wangq
 * 选择基金
 */

public class FJFundChooseActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static String TAG = "FJFundChooseActivity";
    private ListView mListView;
    private List<StructuredFundEntity> list;
    private String mSession;

    @Override
    public void initView() {
        findViewById(R.id.ivCurrencyFundRedeem_back).setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.listView);
        initData();
    }

    private void initData() {
        mSession = SpUtils.getString(this, "mSession", "");
        list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            StructuredFundEntity bean = new StructuredFundEntity();
            bean.setStocken_code("012345" + i);
            bean.setStoken_name("ces" + i);
            bean.setMarket("市场" + i);
            list.add(bean);
        }


        int mPoint = getIntent().getIntExtra("point", -1);
        mListView.setOnItemClickListener(this);
        FJFundChooseAdapter adapter = new FJFundChooseAdapter(this);
        adapter.setData(list);
        adapter.setPoint(mPoint);
        mListView.setAdapter(adapter);
        requestData();
    }

    private void requestData() {
        HashMap map = new HashMap();
        HashMap hashMap = new HashMap();
        map.put("funcid", "300200");
        map.put("token", mSession);
        hashMap.put("FLAG", "true");
        hashMap.put("SEC_ID", "tpyzq");
        map.put("parms", hashMap);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
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
        return R.layout.activity_choose_fund;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCurrencyFundRedeem_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("Name", list.get(position).getStoken_name());
        intent.putExtra("Code", list.get(position).getStocken_code());
        intent.putExtra("Market", list.get(position).getMarket());
        intent.putExtra("point", position);
        setResult(RESULT_OK, intent);
        finish();
    }


}
