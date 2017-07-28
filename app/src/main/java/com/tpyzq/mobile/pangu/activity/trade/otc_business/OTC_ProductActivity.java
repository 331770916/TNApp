package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_ProductAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionListEntity;
import com.tpyzq.mobile.pangu.data.OTC_SubscriptionListMasBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * 刘泽鹏
 * OTC产品Activity
 */
public class OTC_ProductActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvOTC_Product = null;        //OTC产品列表
    private OTC_ProductAdapter adapter;
    private ImageView mNUll;
    private static final String TAG = "OTC_ProductActivity";
    private ArrayList<OTC_SubscriptionListEntity> list;
    private String mSession;
    private int point = -1;

    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");
        list = new ArrayList<>();

        this.findViewById(R.id.ivOTC_Product_back).setOnClickListener(this);    //返回按钮
        mNUll = (ImageView) findViewById(R.id.Null);
        lvOTC_Product = (ListView) this.findViewById(R.id.lvOTC_Product);        //展示数据的listView
        adapter = new OTC_ProductAdapter(this);                                    //实例化adapter
        adapter.setList(list);                                                   //添加数据源
        adapter.setPoint(point);
        lvOTC_Product.setEmptyView(mNUll);
        lvOTC_Product.setAdapter(adapter);                                      //适配

        //点击监听
        lvOTC_Product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OTC_SubscriptionListEntity listIntent = list.get(position);
                listIntent.setFlag(true);
                adapter.setList(list);
                Intent intent = new Intent();
                intent.putExtra("getStockCode", listIntent.getStockCode());
                setResult(RESULT_OK, intent);
                OTC_ProductActivity.this.finish();
            }
        });
        getListMsg(mSession);
    }


    /**
     * 获取产品列表信息
     */
    private void getListMsg(String mSession) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("OPR_TYPE", "2");
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map1.put("funcid", "300502");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_SubscriptionListMasBean>() {
                }.getType();
                OTC_SubscriptionListMasBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<OTC_SubscriptionListMasBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OTC_ProductActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                    OTC_ProductActivity.this.finish();
                } else if (code.equals("0") && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        OTC_SubscriptionListMasBean.DataBean dataBean = data.get(i);
                        String prod_name = dataBean.getPROD_NAME();     //产品名称
                        String prod_code = dataBean.getPROD_CODE();     //产品代码
                        OTC_SubscriptionListEntity listIntent = new OTC_SubscriptionListEntity();
                        listIntent.setStockName(prod_name);
                        listIntent.setStockCode(prod_code);
                        listIntent.setFlag(false);
                        list.add(listIntent);
                        adapter.setList(list);
                    }
                } else {
                    ResultDialog.getInstance().showText("网络异常");
                }
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__product;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivOTC_Product_back) {
            this.finish();
        }
    }

}
