package com.tpyzq.mobile.pangu.activity.trade.otc_business;

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
import com.tpyzq.mobile.pangu.adapter.trade.OTC_ElectronicContractAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OTC_ElectronicContractEntity;
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
 * OTC 电子合同界面
 * 刘泽鹏
 */
public class OTC_ElectronicContractActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "OTC_ElectronicContract";
    private String mSession;
    private PullToRefreshListView mListView=null;
    private ArrayList<OTC_ElectronicContractEntity>  list;
    private OTC_ElectronicContractAdapter adapter;
    private ImageView ivOtcElectronicKong;

    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");
        list = new ArrayList<OTC_ElectronicContractEntity>();
        getContractList();      //获取合同列表数据
        ivOtcElectronicKong = (ImageView) this.findViewById(R.id.ivOtcElectronicKong);      //空  图片
        this.findViewById(R.id.ivOTC_ElectronicContract_back).setOnClickListener(this);     //返回按钮
        mListView = (PullToRefreshListView) this.findViewById(R.id.lvElectronicContract);
        adapter = new OTC_ElectronicContractAdapter(this);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OTC_ElectronicContractEntity intentBean = list.get(position-1);
                ArrayList<OTC_ElectronicContractEntity> listBean = new ArrayList<OTC_ElectronicContractEntity>();
                listBean.add(intentBean);
                String is_sign = intentBean.getIs_sign();
                String prod_name = intentBean.getProd_name();
                String prod_code = intentBean.getProd_code();
                Intent intent = new Intent();
                if(is_sign.equals("1")){
                    intent.setClass(OTC_ElectronicContractActivity.this,OTC_ContractSignActivity.class);
                    intent.putExtra("list",listBean);
                    startActivity(intent);
                }else if(is_sign.equals("0")){
                    intent.setClass(OTC_ElectronicContractActivity.this,OTC_ContractFlowWaterActivity.class);
                    intent.putExtra("list",listBean);
                    startActivity(intent);
                }
            }
        });

        //下拉刷新
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置尾布局样式文字
                mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1500);
                            getContractList();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        //将下拉视图收起
                        mListView.onRefreshComplete();
                    }
                }.execute();
            }
        });
    }

    /**
     * 获取合同列表信息
     */
    private void getContractList() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("FLAG","true");
        map2.put("SEC_ID","tpyzq");
        map1.put("funcid","300511");
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
                        Intent intent = new Intent(OTC_ElectronicContractActivity.this, TransactionLoginActivity.class);
                        OTC_ElectronicContractActivity.this.startActivity(intent);
                        OTC_ElectronicContractActivity.this.finish();
                    }else if("0".equals(code)){
                        JSONArray jsonArray = res.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            if (list!=null&&list.size()>0){
                                list.clear();
                            }
                            for(int i = 0;i < jsonArray.length();i++){
                                JSONObject json = jsonArray.getJSONObject(i);
                                OTC_ElectronicContractEntity intentBean = new OTC_ElectronicContractEntity();
                                intentBean.setProd_name(json.optString("PROD_NAME"));
                                intentBean.setProd_code(json.optString("PROD_CODE"));
                                intentBean.setIs_sign(json.optString("IS_SIGN"));
                                intentBean.setProdta_no(json.optString("PRODTA_NO"));
                                intentBean.setInit_date(json.optString("INIT_DATE"));
                                intentBean.setEcontract_id(json.optString("ECONTRACT_ID"));
                                intentBean.setProdta_no_name(json.optString("PRODTA_NO_NAME"));
                                intentBean.setStatus(json.optString("ECONTRACT_STATUS"));
                                list.add(intentBean);
                            }
                            adapter.setList(list);
                        }else {
                            ivOtcElectronicKong.setVisibility(View.VISIBLE);
                        }
                    }else {
                        ResultDialog.getInstance().showText(res.optString("msg"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
               /**
                Gson gson = new Gson();
                Type type = new TypeToken<OTC_ContractListBean>() {}.getType();
                OTC_ContractListBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<OTC_ContractListBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OTC_ElectronicContractActivity.this, TransactionLoginActivity.class);
                    OTC_ElectronicContractActivity.this.startActivity(intent);
                    OTC_ElectronicContractActivity.this.finish();
                }else
                if(data.size() == 0){
                    ivOtcElectronicKong.setVisibility(View.VISIBLE);    //空 图片
                }else
                if(code.equals("0") && data != null){
                    if (list!=null&&list.size()>0){
                        list.clear();
                    }
                    for(int i=0 ; i<data.size() ; i++){
                        OTC_ElectronicContractEntity intentBean = new OTC_ElectronicContractEntity();
                        HashMap<String,String> map = new HashMap<String,String>();
                        OTC_ContractListBean.DataBean dataBean = data.get(i);
                        String prod_name = dataBean.getPROD_NAME();             //产品名称
                        String prod_code = dataBean.getPROD_CODE();             //产品代码
                        String is_sign = dataBean.getIS_SIGN();                 //是否签署电子合同
                        String prodta_no = dataBean.getPRODTA_NO();             //产品TA
                        String init_date = dataBean.getINIT_DATE();             //签署时间、
                        String econtract_id = dataBean.getECONTRACT_ID();       //委托编号
                        String prodta_no_name = dataBean.getPRODTA_NO_NAME();   //TA公司 名称
                        String STATUS=dataBean.getECONTRACT_STATUS_NAME();      //状态
                        intentBean.setProd_name(prod_name);
                        intentBean.setProd_code(prod_code);
                        intentBean.setIs_sign(is_sign);
                        intentBean.setProdta_no(prodta_no);
                        intentBean.setInit_date(init_date);
                        intentBean.setEcontract_id(econtract_id);
                        intentBean.setProdta_no_name(prodta_no_name);
                        intentBean.setStatus(STATUS);
                        list.add(intentBean);
                    }
                }
                if(list.size()>0){
                    ivOtcElectronicKong.setVisibility(View.GONE);       //如果有数据  隐藏空图片
                }
                adapter.setList(list);
                */

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__electronic_contract;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivOTC_ElectronicContract_back){
            this.finish();                                          //点击返回按钮销毁当前界面
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getContractList();      //获取合同列表数据
    }
}
