package com.tpyzq.mobile.pangu.activity.home.information;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.HotAnalysisDetailAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.InformationBean;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.OneTimiceAddSelfChoiceListener;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.ScreenShot;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.SimulateListView;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;
import com.tpyzq.mobile.pangu.view.progress.RoundProgressBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 热点公告解析   详情 页面
 * 刘泽鹏
 * 修改报文解析方式及处理无股票价格引起程序崩溃问题
 */
public class HotAnalysisDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "HotAnalysisDetail";
    private RoundProgressBar mRoundProgressBar;             //圆形进度条
    private SimulateListView mListView;      //列表
    private View headView;           //头布局
    private TextView tvType, tvBaiFenBi, tvAttentionDate;        //百分比  天数  可关注日期
    private WebView mWebView;        //webView
    private ArrayList<StockInfoEntity> list;
    private HotAnalysisDetailAdapter adapter;
    private InformationBean informationBean, informationBeanPrice;
    private TextView tvHotAnalysisZiXuan;
    private Dialog loadingDialog;
    private int progress = 0;                                //进度
    private LinearLayout llShuiBoWen;                        //水波纹
    private  ShareDialog shareDialog;
    @Override
    public void initView() {

        headView = LayoutInflater.from(this).inflate(R.layout.hot_analysis_detail_head, null);
        mRoundProgressBar = (RoundProgressBar) headView.findViewById(R.id.ZxroundProgressBar);
        llShuiBoWen = (LinearLayout) headView.findViewById(R.id.llShuiBoWen);
        tvType = (TextView) headView.findViewById(R.id.tvType);
        tvBaiFenBi = (TextView) headView.findViewById(R.id.tvBaiFenBi);
        tvAttentionDate = (TextView) headView.findViewById(R.id.tvAttentionDate);
        mWebView = (WebView) headView.findViewById(R.id.wvHotDetail);
        tvHotAnalysisZiXuan = (TextView) this.findViewById(R.id.tvHotAnalysisZiXuan);
        mListView = (SimulateListView) this.findViewById(R.id.lvHotAnalysisDetail);         //列表

        initData();
    }

    private void initData() {
        list = new ArrayList<StockInfoEntity>();
        shareDialog = new ShareDialog(this);
        final Intent intent = getIntent();
        informationBean = (InformationBean) intent.getSerializableExtra("informationBean");
        informationBeanPrice = (InformationBean) intent.getSerializableExtra("informationBeanPrice");
        String tname = informationBean.getTname();
        final String prob = informationBean.getProb();
        String days = informationBean.getDays();
        String state = informationBean.getState();
        String mDate = informationBean.getPublishTime();

        this.findViewById(R.id.ivHotDetailFenXiang).setOnClickListener(this);       //分享
        this.findViewById(R.id.llRDJXYiJianZiXuan).setOnClickListener(this);        //一键自选
        this.findViewById(R.id.iv_HotAnalysisDetailBack).setOnClickListener(this);  //返回


        if (!TextUtils.isEmpty(mDate)) {
            String mNewDate = Helper.getProTime2(mDate);
//            mNewDate.substring(0, 3);
//            mNewDate.substring(3, 4);

            tvAttentionDate.setText("(" + mNewDate + "有效)");
        }

        String newProb = prob.replace("%", "");
        int Percentage = Integer.parseInt(newProb);

        mRoundProgressBar.setProgress(Percentage);


        tvType.setText(tname);
        if (Percentage >= 0) {
            llShuiBoWen.setBackgroundResource(R.mipmap.zx_shuibowen_zhang);
            tvType.setTextColor(ContextCompat.getColor(this, R.color.red));
            tvBaiFenBi.setTextColor(ContextCompat.getColor(this, R.color.red));
            mRoundProgressBar.setCricleColor(ContextCompat.getColor(this, R.color.textDate));
            mRoundProgressBar.setCricleProgressColor(ContextCompat.getColor(this, R.color.red));
        } else if (Percentage < 0) {
            llShuiBoWen.setBackgroundResource(R.mipmap.zx_shuibowen_die);
            tvType.setTextColor(ContextCompat.getColor(this, R.color.green));
            tvBaiFenBi.setTextColor(ContextCompat.getColor(this, R.color.green));
            mRoundProgressBar.setCricleColor(ContextCompat.getColor(this, R.color.qianGreen));
            mRoundProgressBar.setCricleProgressColor(ContextCompat.getColor(this, R.color.green));
        }
        tvBaiFenBi.setText(informationBean.getProb());     //百分比

        StringBuffer sb = new StringBuffer();
        sb.append("<html>\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"UTF-8\">\n" +
                "\t\t<title>word</title>\n" +
                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1, user-scalable=no\">\n" +
                "\t\t\n" +
                "\t</head>\n" +
                "\t<body>\n");

        sb.append(state);

        sb.append("</body>\n" +
                "\t<script>\n" +
                "\t\twindow.onload = function replace(){\n" +
                "\t\t\t\t\tvar str=document.body.innerHTML;\t\t\n" +
                "\t\t\t\t\tdocument.write('<div style=\"width: 94%;margin-left: 3%;text-align:justify;letter-spacing: \t\t\t\t2px;text-justify:inter-ideograph;line-height: 25px;font-size: 14px;color:#4c4c4c;\">'+'<span style=\"color: #e84242; font-size: 14px;font-weight: bold;letter-spacing: 1px;\">[事件说明]</span>'+str+'</div>');\n" +
                "\t\t\t\t}\n" +
                "\t</script>\n" +
                "</html>\n");


        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);

        adapter = new HotAnalysisDetailAdapter(this);
        mListView.addHeaderView(headView);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new SimulateListView.OnItemClickListener() {
            @Override
            public void onItemClick(SimulateListView parent, View view, int position, long id) {
                if(position == 0){
                    return;
                }
                String StockCode=list.get(position-1).getStockNumber();
                String StockName=list.get(position-1).getStockName();
                Intent intent1 = new Intent();
                intent1.setClass(HotAnalysisDetailActivity.this, StockDetailActivity.class);
                StockDetailEntity entity = new StockDetailEntity();
                entity.setStockName(StockName);
                entity.setStockCode(StockCode);
                intent1.putExtra("stockIntent", entity);
                startActivity(intent1);
            }
        });
        getListView();
        mWebView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent ev) {

                ((WebView) v).requestDisallowInterceptTouchEvent(true);


                return false;
            }
        });

    }


    private void getListView() {
        StringBuilder sb = new StringBuilder();
        if (informationBean != null && informationBeanPrice != null) {
            String stockCode = informationBean.getJsonArray();
            sb.append(stockCode);
            if (sb != null && sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        Object[] obj = new Object[1];
        map1.put("market", "0");
        map1.put("code", sb.toString());
        map1.put("type", "4");
        map1.put("order", "0");
        obj[0] = map1;
        Gson gson = new Gson();
        String strJson = gson.toJson(obj);
        map.put("FUNCTIONCODE", "HQING005");
        map.put("PARAMS", strJson);
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.URL, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONArray array = new JSONArray(response);
                    list = new ArrayList<StockInfoEntity>();
                    JSONObject res = array.getJSONObject(0);
                    if("0".equals(res.optString("code"))){
                        String totalCount = res.optString("totalCount");
                        JSONArray jsonArray = res.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            //如果只有1条记录，报文格式不为array数组，故进行特殊处理  2017-03-14
                            if("1".equals(totalCount) || jsonArray.length() == 1){
                                StockInfoEntity _bean = new StockInfoEntity();
                                _bean.setStockNumber(jsonArray.getString(0));
                                _bean.setStockName(jsonArray.getString(1));
                                _bean.setTime(jsonArray.getString(2));
                                _bean.setNewPrice(jsonArray.getString(3));
                                _bean.setClose(jsonArray.getString(4));
                                list.add(_bean);
                            }else{
                                for(int i = 0; i <jsonArray.length();i++){
                                    JSONArray json = jsonArray.getJSONArray(i);
                                    StockInfoEntity _bean = new StockInfoEntity();
                                    _bean.setTotalCount(totalCount);
                                    if(null != json && json.length() > 0){
                                        _bean.setStockNumber(json.getString(0));
                                        _bean.setStockName(json.getString(1));
                                        _bean.setTime(json.getString(2));
                                        _bean.setNewPrice(json.getString(3));
                                        _bean.setClose(json.getString(4));
                                    }
                                    list.add(_bean);
                                }
                            }
                        }
                    }
                    adapter.setList(list);
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                try {
//                    ArrayList<Map<String, Object>> responseValues = objectMapper.readValue(response, new ArrayList<Map<String, Object>>().getClass());
//                    for (int i = 0; i < responseValues.size(); i++) {
//                        if ("0".equals(responseValues.get(i).get("code"))) {
//                            if ("1".equals(responseValues.get(i).get("totalCount"))) {
//                                List<String> data = (List<String>) responseValues.get(i).get("data");
//                                HotAnalysisDetailBean bean = new HotAnalysisDetailBean();
//                                bean.setPrice(data.get(3));
//                                bean.setCloseValue(data.get(4));
////                                bean.setStockName(data.get(1));
//                                bean.setStockName(informationBean.getName().get(0));
//                                bean.setStockCode(data.get(0).substring(2));
//                                bean.setNewStockCode(data.get(0));
//
//                                list.add(bean);
//                            } else {
//                                List<List<String>> data = (List<List<String>>) responseValues.get(i).get("data");
//                                for (int j = 0; j < data.size(); j++) {
//                                    HotAnalysisDetailBean bean = new HotAnalysisDetailBean();
//                                    for (int k = 0; k < data.get(j).size(); k++) {
//                                        bean.setPrice(data.get(j).get(3));
//                                        bean.setCloseValue(data.get(j).get(4));
////                                        bean.setStockName(data.get(j).get(1));
//                                        bean.setStockName(informationBean.getName().get(j));
//
//                                        bean.setStockCode(data.get(j).get(0).substring(2));
//                                        bean.setNewStockCode(data.get(j).get(0));
//                                    }
//                                    list.add(bean);
//                                }
//                            }
//                        }
//                        adapter.setList(list);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_hot_analysis_detail;
    }

    @Override
    public void onClick(View v) {
        String maker = null;
        String subCode = null;
        switch (v.getId()) {
            case R.id.llRDJXYiJianZiXuan:       //一键添加自选
//                ArrayList<StockInfoEntity> datas = new ArrayList<>();
//                for (int i = 0; i < list.size(); i++) {
//                    StockInfoEntity entity = new StockInfoEntity();
//                    entity.setStockName(list.get(i).getStockName());
//                    entity.setStockNumber(list.get(i).getStockNumber());
//                    entity.setNewPrice(list.get(i).getPrice());
//                    entity.setClose(list.get(i).getCloseValue());
//                    datas.add(entity);
//                }
                SelfStockHelper.oneTimiceAddSelfChoice(TAG, "", list, new OneTimiceAddSelfChoiceListener() {
                    @Override
                    public void getResult(String result) {
                        SelfStockHelper.explanOneTimiceAddSelfChoiceResult(HotAnalysisDetailActivity.this, result);

                    }
                });
                break;
            case R.id.iv_HotAnalysisDetailBack:         //返回
                this.finish();
                break;
            case R.id.ivHotDetailFenXiang:               //分享
                loadingDialog = LoadingDialog.initDialog(this, "加载中…");
                loadingDialog.show();       //显示菊花
                getShare();
                break;
        }
    }

    /**
     * 分享
     */
    private void getShare() {
        String capitalAccount = UserUtil.capitalAccount;                        //注册账户
        String base64 = ScreenShot.shoot((Activity) this);         //截屏 拿到图片的 base64
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("base64", base64);
        map2.put("account", capitalAccount);
        map2.put("type", "2");
        map2.put("phone_type", "2");
        map1.put("FUNCTIONCODE", "HQFNG001");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString("MarketFragment", ConstantUtil.URL_FX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG, e.toString());
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response, int id) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        String url = jsonObject.getString("msg");
                        loadingDialog.dismiss();
                        shareDialog.setUrl(url);
                        shareDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }
}
