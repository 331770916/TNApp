package com.tpyzq.mobile.pangu.activity.home.managerMoney;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.OptionalFinancingAdapter;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FixSucessEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.ScreenShot;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 稳赢
 * author：cxy
 */
public class OptionalFinancingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "OptionalFinancingActivity";
    private ListView lv_product;
    private ImageView iv_back;
    private ImageView iv_share;
    private ImageView iv_null;
    private OptionalFinancingAdapter optionalFinancingAdapter;
    private FixSucessEntity fixSucessBean;
    private Dialog mLoadingDialog;
    private    ShareDialog shareDialog;
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_null = (ImageView) findViewById(R.id.iv_null);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        lv_product = (ListView) findViewById(R.id.lv_product);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        shareDialog = new ShareDialog(this);
        mLoadingDialog = LoadingDialog.initDialog(this, "加载中……");
        mLoadingDialog.show();
        optionalFinancingAdapter = new OptionalFinancingAdapter(this);
        iv_back.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        lv_product.setEmptyView(iv_null);
        lv_product.setAdapter(optionalFinancingAdapter);
        lv_product.setOnItemClickListener(new OptionalItemClickListen());
        getServer();
    }

    private void getServer() {
        Map params = new HashMap();
        params.put("FUNCTIONCODE", "HQTNG102");
        Map map = new HashMap();
        map.put("SCHEMAID", "13");
        params.put("PARAMS", map);
        params.put("TOKEN", "");
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_RS, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mLoadingDialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                mLoadingDialog.dismiss();
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    String type = jsonObject.getString("type");
                    if ("200".equals(code)) {
                        fixSucessBean = new Gson().fromJson(message, FixSucessEntity.class);
                        optionalFinancingAdapter.setData(fixSucessBean.prod,fixSucessBean.schema_id);
                        BRutil.menuNewSelect("Z1-4-4", fixSucessBean.schema_id, "-1", "1", new Date(), "-1", "-1");
                    } else {
                        ToastUtils.showShort(OptionalFinancingActivity.this, type);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoadingDialog.dismiss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_optional_financing;
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                getShare("5");
                break;
        }
    }

    /**
     * 截屏 获取图片
     *
     * @param type 1自选股 4行情 2资讯 5稳赢
     */
    private void getShare(String type) {
        String capitalAccount = UserUtil.capitalAccount;                        //注册账户
        String base64 = ScreenShot.shoot(this);         //截屏 拿到图片的 base64
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("base64", base64);
        map2.put("account", capitalAccount);
        map2.put("type", type);
        map2.put("phone_type", "2");
        map1.put("FUNCTIONCODE", "HQFNG001");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString("MarketFragment", ConstantUtil.URL_FX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    if ("0".equals(code)) {
                        String url = jsonObject.getString("msg");
                        mLoadingDialog.dismiss();
                        shareDialog.setUrl(url);
                        if (!OptionalFinancingActivity.this.isFinishing()){
                            shareDialog.show();
                        }
                    } else {
                        ToastUtils.showShort(OptionalFinancingActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * ListView条目点击事件
     */
    class OptionalItemClickListen implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            String TYPE;
            boolean register;
            String prod_status;
            switch (position) {
                case 0:
                case 2:
                    break;
                case 1:
                    prod_status = fixSucessBean.prod.get(0).prod_status;
                    if (TextUtils.isEmpty(prod_status)) {
                        return;
                    }
                    intent.putExtra("productCode", fixSucessBean.prod.get(0).prod_code);
                    TYPE = fixSucessBean.prod.get(0).TYPE;
                    intent.putExtra("TYPE", TYPE);
                    intent.putExtra("prod_type", fixSucessBean.prod.get(0).prod_type);
                    intent.putExtra("prod_nhsy", fixSucessBean.prod.get(0).prod_nhsy);
                    intent.putExtra("prod_qgje", fixSucessBean.prod.get(0).prod_qgje);
                    intent.putExtra("schema_id", fixSucessBean.schema_id);
                    intent.putExtra("prod_code", fixSucessBean.prod.get(0).prod_code);
                    intent.putExtra("ofund_risklevel_name", fixSucessBean.prod.get(0).ofund_risklevel_name);
                    register = Db_PUB_USERS.isRegister();
                    BRutil.menuNewSelect("Z1-4-4", fixSucessBean.schema_id, fixSucessBean.prod.get(0).prod_code, "2", new Date(), "-1", "-1");
                    if ("3".equals(TYPE)) {
                        intent.putExtra("prod_status", prod_status);
                        if (register) {
                            intent.setClass(OptionalFinancingActivity.this, ManagerMoenyDetailActivity.class);
                            startActivity(intent);
                        } else {
                            intent.putExtra("Identify", "2");
                            intent.setClass(OptionalFinancingActivity.this, ShouJiZhuCeActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        intent.setClass(OptionalFinancingActivity.this, ManagerMoenyDetailActivity.class);
                        startActivity(intent);
                    }

                    break;
                default:
                    prod_status = fixSucessBean.prod.get(position - 2).prod_status;
                    intent.putExtra("productCode", fixSucessBean.prod.get(position - 2).prod_code);
                    TYPE = fixSucessBean.prod.get(position - 2).TYPE;
                    intent.putExtra("TYPE", TYPE);
                    intent.putExtra("prod_type", fixSucessBean.prod.get(position - 2).prod_type);
                    intent.putExtra("prod_nhsy", fixSucessBean.prod.get(position - 2).prod_nhsy);
                    intent.putExtra("prod_qgje", fixSucessBean.prod.get(position - 2).prod_qgje);
                    intent.putExtra("schema_id", fixSucessBean.schema_id);
                    intent.putExtra("prod_code", fixSucessBean.prod.get(position - 2).prod_code);
                    intent.putExtra("ofund_risklevel_name", fixSucessBean.prod.get(position - 2).ofund_risklevel_name);
                    register = Db_PUB_USERS.isRegister();
                    BRutil.menuNewSelect("Z1-4-4", fixSucessBean.schema_id, fixSucessBean.prod.get(position - 2).prod_code, "2", new Date(), "-1", "-1");
                    if ("3".equals(TYPE)) {
                        intent.putExtra("prod_status", prod_status);
                        if (register) {
                            intent.setClass(OptionalFinancingActivity.this, ManagerMoenyDetailActivity.class);
                            startActivity(intent);
                        } else {
                            intent.putExtra("Identify", "2");
                            intent.setClass(OptionalFinancingActivity.this, ShouJiZhuCeActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        intent.setClass(OptionalFinancingActivity.this, ManagerMoenyDetailActivity.class);
                        startActivity(intent);
                    }

                    break;
            }

        }
    }
}
