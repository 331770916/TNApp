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
import com.tpyzq.mobile.pangu.adapter.trade.OTC_SubscribeProductAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_SubscribeEntity;
import com.tpyzq.mobile.pangu.data.OTC_SubscribeListBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * OTC 申购选择OTC 产品界面
 * 刘泽鹏
 */
public class OTC_SubscribeProductActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG="OTC_SubscribeProductActivity";
    private ListView lvOTC_SGProduct = null;
    private OTC_SubscribeProductAdapter adapter;
    private ImageView mNull;
    private  int point = -1;
    private String mSession;
    private ArrayList<OTC_SubscribeEntity> list;
    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");
        list = new ArrayList<OTC_SubscribeEntity>();
        getProductList(mSession);

        this.lvOTC_SGProduct = (ListView) this.findViewById(R.id.lvOTC_SGProduct);       //展示数据的listView
        mNull = (ImageView) findViewById(R.id.Null);
        adapter = new OTC_SubscribeProductAdapter(this);                                  //初始化适配器
        adapter.setList(list);                                                             //添加数据
        adapter.setPoint(point);
        lvOTC_SGProduct.setEmptyView(mNull);
        this.lvOTC_SGProduct.setAdapter(adapter);                                        //适配
        this.findViewById(R.id.ivOTC_SGProduct_back).setOnClickListener(this);           //给返回按钮添加点击监听
        //item 点击监听
        this.lvOTC_SGProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OTC_SubscribeEntity intentBean = list.get(position);
                intentBean.setFlag(true);
                adapter.setList(list);
                Intent intent = new Intent();
                intent.putExtra("getStockCode", intentBean.getStockCode());
                setResult(RESULT_OK, intent);
                OTC_SubscribeProductActivity.this.finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__subscribe_product;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivOTC_SGProduct_back) {
            this.finish();
        }
    }

    /**
     * 网络获取OTC产品列表信息
     */
    private void getProductList(String mSession) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("OPR_TYPE", "1");
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map1.put("funcid", "300502");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                Gson gson = new Gson();
                Type type = new TypeToken<OTC_SubscribeListBean>() {
                }.getType();
                OTC_SubscribeListBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<OTC_SubscribeListBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OTC_SubscribeProductActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                    OTC_SubscribeProductActivity.this.finish();
                } else if (code.equals("0") && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        OTC_SubscribeListBean.DataBean dataBean = data.get(i);
                        String prod_name = dataBean.getPROD_NAME();         //产品名称
                        String prod_code = dataBean.getPROD_CODE();         //产品代码
                        OTC_SubscribeEntity intentBean = new OTC_SubscribeEntity();
                        intentBean.setStcokName(prod_name);
                        intentBean.setStockCode(prod_code);
                        intentBean.setFlag(false);
                        list.add(intentBean);
                        adapter.setList(list);
                    }
                } else {
                    CentreToast.showText(OTC_SubscribeProductActivity.this,"网络异常");
                }
            }
        });
    }


}
