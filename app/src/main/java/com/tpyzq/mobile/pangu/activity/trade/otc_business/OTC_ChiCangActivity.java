package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_RedeemChiCangAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_RedeemEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * OTC 我的持仓界面
 * 刘泽鹏
 */
public class OTC_ChiCangActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "OTC_ChiCangActivity";
    private ListView lvOTC_RedeemProduct=null;
    private OTC_RedeemChiCangAdapter adapter;
    private ImageView iv_empty;
    private ArrayList<OTC_RedeemEntity> list;

    @Override
    public void initView() {
        list = new ArrayList<OTC_RedeemEntity>();
        int point = getIntent().getIntExtra("point",-1);
        lvOTC_RedeemProduct= (ListView) this.findViewById(R.id.lvOTC_RedeemProduct);       //展示数据的listView
        iv_empty= (ImageView) this.findViewById(R.id.iv_empty);       //展示数据的listView
        adapter = new OTC_RedeemChiCangAdapter(this);                                           //初始化适配器
        adapter.setList(list);                                                                     //添加数据
        adapter.setPoint(point);
        this.lvOTC_RedeemProduct.setAdapter(adapter);                                           //适配
        lvOTC_RedeemProduct.setEmptyView(iv_empty);
        this.findViewById(R.id.ivOTC_Redeem_back).setOnClickListener(this);                  //给返回按钮添加点击监听
        //item 点击监听
        this.lvOTC_RedeemProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OTC_RedeemEntity intentBean = list.get(position);
                intentBean.setFlag(true);
                adapter.setList(list);
                Intent intent = new Intent();
                intent.putExtra("position",position);
                intent.putExtra("stockcode",intentBean.getStockCode());
                setResult(RESULT_OK,intent);
                OTC_ChiCangActivity.this.finish();
            }
        });

        getProductList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__chi_cang;
    }


    /**
     * 网络获取OTC持仓列表信息
     */
    private void getProductList() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("FLAG", "true");
        map2.put("SEC_ID", "tpyzq");
        map1.put("funcid", "300501");
        map1.put("token", SpUtils.getString(this, "mSession", ""));
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
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("-6".equals(code)) {
                        Intent intent = new Intent(OTC_ChiCangActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                        OTC_ChiCangActivity.this.finish();
                    } else if ("0".equals(code)) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            String otc_market_value = jsonObject1.getString("OTC_MARKET_VALUE"); //市值
                            JSONArray otc_list = jsonObject1.getJSONArray("OTC_LIST");
                            for (int j = 0; j < otc_list.length(); j++) {
                                JSONObject item = otc_list.getJSONObject(j);
                                String prod_name = item.getString("PROD_NAME");
                                String prod_code = item.getString("PROD_CODE");
                                OTC_RedeemEntity intentBean = new OTC_RedeemEntity();
                                intentBean.setStockName(prod_name);
                                intentBean.setStockCode(prod_code);
                                intentBean.setFlag(false);
                                list.add(intentBean);
                            }
                        }

                    } else {
                        showDialog(jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),OTC_ChiCangActivity.class.toString());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ivOTC_Redeem_back){
            this.finish();
        }
    }
}
