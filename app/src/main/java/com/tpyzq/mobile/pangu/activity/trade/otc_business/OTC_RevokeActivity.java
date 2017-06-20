package com.tpyzq.mobile.pangu.activity.trade.otc_business;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.view.OTC_RevokePopupWindow;
import com.tpyzq.mobile.pangu.adapter.trade.OTC_RevokeAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
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
 * 、
 * OTC 撤单界面
 * 刘泽鹏
 */
public class OTC_RevokeActivity extends BaseActivity implements View.OnClickListener, OTC_RevokePopupWindow.PositionListener {

    private String TAG = "OTC_RevokeActivity";
    private PullToRefreshListView lvRevoke = null;
    private ArrayList<HashMap<String, String>> list;
    private OTC_RevokeAdapter adapter;
    private String mSession;
    private ImageView ivOtcCheDanKong;

    @Override
    public void initView() {
        mSession = SpUtils.getString(this, "mSession", "");                     //获取session
        list = new ArrayList<HashMap<String, String>>();
        ivOtcCheDanKong = (ImageView) this.findViewById(R.id.ivOtcCheDanKong);      //空 图片
        lvRevoke = (PullToRefreshListView) this.findViewById(R.id.lvRevoke);          //展示数据的listView
        this.findViewById(R.id.ivOTC_RevokeBack).setOnClickListener(this);          //给返回按钮添加监听
        getRevokeList();                                                      //获取OTC撤单列表信息
        adapter = new OTC_RevokeAdapter(this);                                      //实例化适配器
        lvRevoke.setEmptyView(ivOtcCheDanKong);
        lvRevoke.setAdapter(adapter);                                              //适配
        lvRevoke.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置尾布局样式文字
                lvRevoke.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                lvRevoke.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                lvRevoke.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1500);
                            list.clear();
                            getRevokeList();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        //将下拉视图收起
                        lvRevoke.onRefreshComplete();
                    }
                }.execute();
            }
        });

        lvRevoke.setOnItemClickListener(new AdapterView.OnItemClickListener() {     //添加监听
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = list.get(position - 1);
                OTC_RevokePopupWindow popupWindow = new OTC_RevokePopupWindow(OTC_RevokeActivity.this,
                        OTC_RevokeActivity.this, map, position - 1, OTC_RevokeActivity.this);

                popupWindow.setFocusable(true);     //获取焦点
                ColorDrawable dw = new ColorDrawable(0xf0000000);     //0x60000000
                popupWindow.setBackgroundDrawable(dw);      //设置背景颜色
                popupWindow.setOutsideTouchable(true);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.7f;
                getWindow().setAttributes(lp);
                //消失的时候设置窗体背景变亮
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().setAttributes(lp);
                    }
                });

                //显示窗口
                popupWindow.showAtLocation(OTC_RevokeActivity.this.findViewById(R.id.OTC_RevokeActivity),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
    }

    /**
     * 获取OTC撤单列表信息
     */
    private void getRevokeList() {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("WITHDRAW_FLAG", "1");
        map1.put("funcid", "730204");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                lvRevoke.setVisibility(View.GONE);               //隐藏 listView
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
                        Intent intent = new Intent(OTC_RevokeActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                        OTC_RevokeActivity.this.finish();
                    }else if("0".equals(code)){
                        JSONArray jsonArray = res.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            for(int i = 0;i < jsonArray.length();i++){
                                JSONObject json = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                String prod_name = json.optString("PROD_NAME");                         //产品名称
                                String prod_code = json.optString("PROD_CODE");                         //产品代码
                                String entrust_date = json.optString("ENTRUST_DATE");                   //日期
                                String entrust_time = json.optString("ENTRUST_TIME");                   //时间
                                String entrust_amount = json.optString("ENTRUST_AMOUNT");               //份额
                                String entrust_status_name = json.optString("ENTRUST_STATUS_NAME");     //状态
                                String prodta_no = json.optString("PRODTA_NO");                         //产品TA码
                                String allot_no = json.optString("ALLOT_NO");                           //申请编码
                                String business_flag = json.optString("BUSINESS_FLAG");                 //类型
                                map.put("prod_name", prod_name);
                                map.put("prod_code", prod_code);
                                map.put("business_flag", business_flag);
                                map.put("entrust_date", Helper.formateDate1(entrust_date));
                                map.put("entrust_time", Helper.formateDate(entrust_time));
                                map.put("entrust_amount", entrust_amount);
                                map.put("entrust_status_name", entrust_status_name);
                                map.put("prodta_no", prodta_no);
                                map.put("allot_no", allot_no);
                                list.add(map);
                            }
                        }
                    }else{
                        ResultDialog.getInstance().showText(res.optString("msg"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    ResultDialog.getInstance().showText("网络异常");
                }
                adapter.setList(list);
               /*
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<OTC_RevokeListBean>() {
                }.getType();
                OTC_RevokeListBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<OTC_RevokeListBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OTC_RevokeActivity.this, TransactionLoginActivity.class);
                    startActivity(intent);
                    OTC_RevokeActivity.this.finish();
                } else if (code.equals("0")) {
                    if (data != null && data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            OTC_RevokeListBean.DataBean dataBean = data.get(i);
                            String prod_name = dataBean.getPROD_NAME();                         //产品名称
                            String prod_code = dataBean.getPROD_CODE();                         //产品代码
                            String entrust_date = dataBean.getENTRUST_DATE();                   //日期
                            String entrust_time = dataBean.getENTRUST_TIME();                   //时间
                            String entrust_amount = dataBean.getENTRUST_AMOUNT();               //份额
                            String entrust_status_name = dataBean.getENTRUST_STATUS_NAME();     //状态
                            String prodta_no = dataBean.getPRODTA_NO();                         //产品TA码
                            String allot_no = dataBean.getALLOT_NO();                           //申请编码
                            String business_flag = dataBean.getBUSINESS_FLAG();                 //类型
                            map.put("prod_name", prod_name);
                            map.put("prod_code", prod_code);
                            map.put("business_flag", business_flag);

                            try {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                                // SimpleDateFormat的parse(String time)方法将String转换为Date
                                java.util.Date date = simpleDateFormat.parse(entrust_date);
                                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                // SimpleDateFormat的format(Date date)方法将Date转换为String
                                String entrustDate = simpleDateFormat.format(date);
                                map.put("entrust_date", entrustDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            try {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss");
                                // SimpleDateFormat的parse(String time)方法将String转换为Date
                                if (entrust_time.length() == 5) {
                                    entrust_time = "0" + entrust_time;
                                }
                                java.util.Date date = simpleDateFormat.parse(entrust_time);
                                simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                                // SimpleDateFormat的format(Date date)方法将Date转换为String
                                String entrustTime = simpleDateFormat.format(date);
                                map.put("entrust_time", entrustTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            map.put("entrust_amount", entrust_amount);
                            map.put("entrust_status_name", entrust_status_name);
                            map.put("prodta_no", prodta_no);
                            map.put("allot_no", allot_no);
                            list.add(map);
                        }
                    }
                    adapter.setList(list);                                    //添加数据源
                } else {
                    adapter.setList(list);
                    ResultDialog.getInstance().showText("网络异常");
                }
               */
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_otc__revoke;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivOTC_RevokeBack) {
            this.finish();
        }
    }

    @Override
    public void callBack(int position) {
        list.clear();
        getRevokeList();
//        list.remove(position);
//        adapter.setList(list);
    }
}
