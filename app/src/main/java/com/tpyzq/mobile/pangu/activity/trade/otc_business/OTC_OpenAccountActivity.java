package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_OpenAccountEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * OTC 开户界面
 * 刘泽鹏
 */
public class OTC_OpenAccountActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "OTC_OpenAccount";
    public static final int REQUSET = 1;
    private final int MAXNUM = 6;           //产品公司代码输入框如果达到这个值，开始请求网络
    private int point = -1;                 //标记值，用于标记选中的是哪一个个公司
    private TextView ProductCompany;
    private Button bnOTC_OpenAccountQD;
    private String mSession;
    private String porductNo;
    private boolean isCheck = false;
    private ArrayList<OTC_OpenAccountEntity> list;

    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");                              //获取 session值\
        list = new ArrayList<OTC_OpenAccountEntity>();
        getProductList();                                                                  //初始化获取产品公司列表信息
        this.findViewById(R.id.ivOTC_OpenAccount_back).setOnClickListener(this);        //返回按钮
        ProductCompany = (TextView) this.findViewById(R.id.ProductCompany);          //产品公司
        ProductCompany.setOnClickListener(this);
        bnOTC_OpenAccountQD = (Button) this.findViewById(R.id.bnOTC_OpenAccountQD);   //确定


        bnOTC_OpenAccountQD.setBackgroundResource(R.drawable.lonin4);            //背景灰

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OTC_SubscriptionActivity.REQUSET && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            point = data.getIntExtra("position", -1);
            ProductCompany.setText(list.get(position).getProductName());
            porductNo = list.get(position).getProductNo();

            bnOTC_OpenAccountQD.setOnClickListener(this);
            bnOTC_OpenAccountQD.setEnabled(true);                                   //如果选中 确定按钮可以点击
            bnOTC_OpenAccountQD.setBackgroundResource(R.drawable.lonin);
            /*if(isCheck == true && point != -1){
                bnOTC_OpenAccountQD.setEnabled(true);                                   //如果选中 确定按钮可以点击
                bnOTC_OpenAccountQD.setBackgroundResource(R.drawable.lonin);            //背景亮
            }else {
                bnOTC_OpenAccountQD.setEnabled(false);                                   //确定按钮不可以点击
                bnOTC_OpenAccountQD.setBackgroundResource(R.drawable.lonin4);            //背景灰
            }*/


        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__open_account;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ivOTC_OpenAccount_back:       //返回
                finish();
                break;
            case R.id.ProductCompany:              //选择产品公司
                intent.setClass(this, OTC_OpenAccountProductActivity.class);
                intent.putExtra("list", list);
                intent.putExtra("point", point);
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.bnOTC_OpenAccountQD:          //确定
                openAccountCommit();                //开户
                break;
        }
    }

    /**
     * 开户的网络请求
     */
    private void openAccountCommit() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("PRODTA_NO", porductNo);
        map1.put("funcid", "300507");
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
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("-6".equals(code)){
                        Intent intent = new Intent(OTC_OpenAccountActivity.this, TransactionLoginActivity.class);
                        OTC_OpenAccountActivity.this.startActivity(intent);
                        OTC_OpenAccountActivity.this.finish();
                    }else if("0".equals(code)){
                        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(res.optString("msg"),CustomCenterDialog.SHOWCENTER);
                        customCenterDialog.show(getFragmentManager(),OTC_OpenAccountActivity.class.toString());
                    }else {
                        CentreToast.showText(OTC_OpenAccountActivity.this,res.optString("msg"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                /**
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_OpenAccountCommit>() {
                }.getType();
                OTC_OpenAccountCommit bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OTC_OpenAccountActivity.this, TransactionLoginActivity.class);
                    OTC_OpenAccountActivity.this.startActivity(intent);
                    OTC_OpenAccountActivity.this.finish();
                } else if (code.equals("0")) {
//                    ResultDialog.getInstance().show("委托已提交", R.mipmap.duigou);
                    MistakeDialog.showDialog(msg, OTC_OpenAccountActivity.this);


//                    Toast toast = Toast.makeText(OTC_OpenAccountActivity.this, "委托已提交", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    LinearLayout toastView = (LinearLayout) toast.getView();
//                    toastView.setBackgroundResource(R.drawable.toasts);
//                    ImageView imageCodeProject = new ImageView(OTC_OpenAccountActivity.this);
//                    imageCodeProject.setImageResource(R.mipmap.xinxiqueren);
//                    toastView.addView(imageCodeProject, 0);
//                    toast.show();
                } else {
                    MistakeDialog.showDialog(msg, OTC_OpenAccountActivity.this);
//                    Toast toast = Toast.makeText(OTC_OpenAccountActivity.this, msg, Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    LinearLayout toastView = (LinearLayout) toast.getView();
//                    ImageView imageCodeProject = new ImageView(OTC_OpenAccountActivity.this);
////                    imageCodeProject.setImageResource(R.drawable.qiresou);
//                    toastView.addView(imageCodeProject, 0);
//                    toast.show();
                }
                 */
            }
        });
    }

    /**
     * 获取产品公司列表信息
     */
    private void getProductList() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map1.put("funcid", "300509");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(OTC_OpenAccountActivity.this,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("-6".equals(code)){
                        Intent intent = new Intent(OTC_OpenAccountActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                        OTC_OpenAccountActivity.this.finish();
                    }else if("0".equals(code)){
                        JSONArray jsonArray = res.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            for(int i = 0;i < jsonArray.length();i++){
                                OTC_OpenAccountEntity intentBean = new OTC_OpenAccountEntity();
                                JSONObject json = jsonArray.getJSONObject(i);
                                intentBean.setProductName(json.optString("PRODTA_NAME"));
                                intentBean.setProductNo(json.optString("PRODTA_NO"));
                                intentBean.setFlag(false);
                                list.add(intentBean);
                            }
                        }
                    }else {
                        CentreToast.showText(OTC_OpenAccountActivity.this,res.optString("msg"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                /**
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_OpenAccountProductListBean>() {
                }.getType();
                OTC_OpenAccountProductListBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<OTC_OpenAccountProductListBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {              //如果 code 的值为-6的话  证明sesson 值失效了  需要跳转登陆页面从新获取
                    Intent intent = new Intent(OTC_OpenAccountActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                    OTC_OpenAccountActivity.this.finish();
                } else if (code.equals("0") && data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        OTC_OpenAccountEntity intentBean = new OTC_OpenAccountEntity();     //实例化传递数据用的实体类
                        OTC_OpenAccountProductListBean.DataBean dataBean = data.get(i);
                        String prodta_name = dataBean.getPRODTA_NAME();     //产品公司名称
                        String prodta_no = dataBean.getPRODTA_NO();         //TA 编号
                        intentBean.setProductName(prodta_name);
                        intentBean.setProductNo(prodta_no);
                        intentBean.setFlag(false);
                        list.add(intentBean);
                    }
                } else {
                    ResultDialog.getInstance().showText("网络异常");
                }
                */
            }
        });
    }
}
