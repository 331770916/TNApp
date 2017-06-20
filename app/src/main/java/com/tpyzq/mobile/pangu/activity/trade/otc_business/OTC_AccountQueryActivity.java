package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_AccountQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * OTC 账户查询 页面
 */
public class OTC_AccountQueryActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "OTC_AccountQuery";
    private ListView mListView=null;
    private OTC_AccountQueryAdapter adapter;
    private ArrayList<HashMap<String,String>> list;
    private ImageView ivOTC_AccountQueryKong;       //空图片

    @Override
    public void initView() {
        ivOTC_AccountQueryKong = (ImageView) this.findViewById(R.id.ivOTC_AccountQueryKong);
        this.findViewById(R.id.ivOTC_AccountQuery_back).setOnClickListener(this);
        mListView = (ListView) this.findViewById(R.id.lvOTC_AccountQuery);
        adapter = new OTC_AccountQueryAdapter(this);
        list = new ArrayList<HashMap<String,String>>();
        getAccountlist();           //获取账户数据
        mListView.setAdapter(adapter);
    }

    /**
     * 获取账户数据
     */
    private void getAccountlist() {
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("FLAG","true");
        map2.put("SEC_ID","tpyzq");
        map1.put("funcid","300506");
        map1.put("token",mSession);
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ResultDialog.getInstance().showText("网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if (code.equals("-6")) {
                        Intent intent = new Intent(OTC_AccountQueryActivity.this, TransactionLoginActivity.class);
                        OTC_AccountQueryActivity.this.startActivity(intent);
                        OTC_AccountQueryActivity.this.finish();
                    }else if("0".equals(code)){
                        JSONArray jsonArray = res.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            for(int i = 0;i < jsonArray.length();i++){
                                HashMap<String,String> map = new HashMap<String, String>();
                                JSONObject json = jsonArray.getJSONObject(i);
                                map.put("open_date",json.optString("OPEN_DATE"));
                                map.put("secum_account",json.optString("SECUM_ACCOUNT"));
                                map.put("prodta_no",json.optString("PRODTA_NO"));
                                map.put("prodholder_status",json.optString("STATUS_NAME"));
                                list.add(map);
                            }
                        }else {
                            ivOTC_AccountQueryKong.setVisibility(View.VISIBLE);
                        }
                    }else {
                        ResultDialog.getInstance().showText(res.optString("msg"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(list.size()>0){
                    ivOTC_AccountQueryKong.setVisibility(View.GONE);
                }
                adapter.setList(list);
                /**
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_AccountQueryListBean>() {}.getType();
                OTC_AccountQueryListBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<OTC_AccountQueryListBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OTC_AccountQueryActivity.this, TransactionLoginActivity.class);
                    OTC_AccountQueryActivity.this.startActivity(intent);
                    OTC_AccountQueryActivity.this.finish();
                }else
                if(data.size() == 0){
                    ivOTC_AccountQueryKong.setVisibility(View.VISIBLE);
                }else
                if(code.equals("0") && data != null){
                    for(int i=0;i<data.size();i++){
                        HashMap<String,String> map = new HashMap<String, String>();
                        OTC_AccountQueryListBean.DataBean dataBean = data.get(i);
                        String open_date = dataBean.getOPEN_DATE();                     //开户时间
                        String secum_account = dataBean.getSECUM_ACCOUNT();             //账户
                        String prodta_no = dataBean.getPRODTA_NO();                     //公司
//                        String prodholder_status = dataBean.getPRODHOLDER_STATUS();     //状态
                        String status_name = dataBean.getSTATUS_NAME();                 //状态
                        map.put("open_date",open_date);
                        map.put("secum_account",secum_account);
                        map.put("prodta_no",prodta_no);
                        map.put("prodholder_status",status_name);
                        list.add(map);
                    }
                }
                if(list.size()>0){
                    ivOTC_AccountQueryKong.setVisibility(View.GONE);
                }
                adapter.setList(list);
                */
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__account_query;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ivOTC_AccountQuery_back){
            finish();
        }
    }
}
