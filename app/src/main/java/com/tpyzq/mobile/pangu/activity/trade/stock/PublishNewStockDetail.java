package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.market.PublishNewStockDetailAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.tpyzq.mobile.pangu.view.pullDownGroup.PullDownElasticImp;
import com.tpyzq.mobile.pangu.view.pullDownGroup.PullDownScrollView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/7/3.
 * 新股详情
 */
public class PublishNewStockDetail extends BaseActivity implements View.OnClickListener, PullDownScrollView.RefreshListener {

    private String TAG = "PublishNewStockDetail";

    private PullDownScrollView mPullDownScrollView;
    private PublishNewStockDetailAdapter mDetailAdapter;
    private DecimalFormat mFormat1;
    private DecimalFormat mFormat2;
    private String mNumber;


    @Override
    public void initView() {

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        mNumber = intent.getStringExtra("number");

        mFormat1 = new DecimalFormat("#0.00");
        mFormat2 = new DecimalFormat("#0.00%");

        findViewById(R.id.publish_detail_back).setOnClickListener(this);
        TextView titleTv = (TextView) findViewById(R.id.publish_detail_title);

        if (!TextUtils.isEmpty(name)) {
            titleTv.setText(name);
        }

        ListView listView = (ListView) findViewById(R.id.newStockDetailList);

        mDetailAdapter = new PublishNewStockDetailAdapter();

        listView.setAdapter(mDetailAdapter);

        mPullDownScrollView = (PullDownScrollView) findViewById(R.id.new_pullDownId);
        mPullDownScrollView.setRefreshListener(this);
        mPullDownScrollView.setPullDownElastic(new PullDownElasticImp(PublishNewStockDetail.this));

        if (!TextUtils.isEmpty(mNumber)) {
            toConnect(mNumber);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish_detail_back:
                finish();
                break;
        }
    }


    @Override
    public void onRefresh(PullDownScrollView view) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String bDate = format1.format(new Date());
                toConnect(mNumber);
                mPullDownScrollView.finishRefresh("上次刷新时间:" + bDate);
            }
        }, 2000);
    }


    @Override
    public int getLayoutId() {
        return R.layout.publishnewstockdetail;
    }

    private void toConnect(String stockNumber) {
        Map map = new HashMap();
        final Map map2 = new HashMap();
        map.put("funcid", "100210");
        map.put("token", "");
        map2.put("secucode", stockNumber);
        map.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_NEW(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
//{"totalcount":"1","data":[{"ISSUEVOL":"111500000","ISSUEPRICE":"6.8300","LOTRATEONLINE":"0.000670578000000","SECUCODE":"11603323","ONLINESTARTDATE":"2016-11-16","BUSINESSMAJOR":"    吸收公众存款;发放短期、中期和长期贷款;办理国内外结算业务;办理票据承兑与贴现;代理收付款项及代理保险业务;代理发行、代理兑付、承销政府债券;买卖政府债券和金融债券;从事同业拆借;提供保管箱服务;外汇存款;外汇贷款;外汇汇款;外币兑换;结汇、售汇;资信调查、咨询、见证业务;经银行业监督管理机构和有关部门批准的其他业务。\r\n","SECUABBR":"吴江银行","APPLYMAXONLINE":"33000","ISSUERESULTPUBLDATE":"2016-11-18","LISTDATE":"","OLBEFPUTBACK":"100350000","DILUTEDPERATIO":"12.62000000","APPLYCODEONLINE":"732323","BRIEFINTROTEXT":"    2004年8月25日,本行在江苏省工商行政管理局注册登记,注册号3200001105926,注册名称为江苏吴江农村商业银行股份有限公司,注册资本为30,000万元,住所为吴江市流虹路408号,法定代表人为黄兴龙。\r\n","WEIGHTEDPERATIO":""}],"code":"0","msg":"查询成功"}
               try{
                   JSONObject res = new JSONObject(response);
                   ArrayList<Map<String, String>> mDatas = new ArrayList<>();
                   if("0".equals(res.optString("code"))){
                       JSONArray jsonArray = res.getJSONArray("data");
                       if (null != jsonArray && jsonArray.length() > 0){
                           for(int i = 0;i < jsonArray.length();i++){
                               Map<String, String> data1 = new HashMap<>();
                               Map<String, String> data2 = new HashMap<>();
                               JSONObject json = jsonArray.getJSONObject(i);
                               data1.put("typeTitle", "申购进程");
                               data2.put("typeTitle", "发行资料");

                               data1.put("title1", "股票名称");
                               data2.put("title1", "发行价格");

                               data1.put("title2", "股票代码");
                               data2.put("title2", "发行市盈率");

                               data1.put("title3", "申购代码");
                               data2.put("title3", "发行总数");

                               data1.put("title4", "申购日期");
                               data2.put("title4", "网上发行");

                               data1.put("title5", "中签公布日");
                               data2.put("title5", "申购上限");

                               data1.put("title6", "中签率");
                               data2.put("title6", "公司简介");

                               data1.put("title7", "上市日期");
                               data2.put("title7", "经营范围");

                               data1.put("value1", json.optString("SECUABBR"));
                               data2.put("value1", mFormat1.format(TransitionUtils.string2double(json.optString("ISSUEPRICE"))));

                               data1.put("value2", Helper.getStockNumber(json.optString("SECUCODE")));
                               data2.put("value2", mFormat1.format(TransitionUtils.string2double(json.optString("DILUTEDPERATIO"))));

                               data1.put("value3", json.optString("APPLYCODEONLINE"));
                               data2.put("value3", TransitionUtils.string2double(json.optString("ISSUEVOL")) / 10000 + "万股");

                               data1.put("value4", json.optString("ONLINESTARTDATE"));
                               data2.put("value4",  TransitionUtils.string2double(json.optString("OLBEFPUTBACK"))/10000  + "万股");

                               data1.put("value5", json.optString("ISSUERESULTPUBLDATE"));
                               data2.put("value5", TransitionUtils.string2double(json.optString("APPLYMAXONLINE"))/10000 + "万股");

                               data1.put("value6", mFormat2.format(TransitionUtils.string2double(json.optString("LOTRATEONLINE"))));
                               data2.put("value6", json.optString("BRIEFINTROTEXT"));

                               data1.put("value7", json.optString("LISTDATE"));
                               data2.put("value7", json.optString("BUSINESSMAJOR"));

                               mDatas.add(data1);
                               mDatas.add(data2);
                               mDetailAdapter.setDatas(mDatas);
                           }
                       }
                   }else{
                       ResultDialog.getInstance().showText(res.optString("msg"));
                   }
               }catch (JSONException e){
                   e.printStackTrace();
                   ResultDialog.getInstance().showText(e.toString());
               }
               /**
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<PublishNewStockDetailEntity>() {
                }.getType();
                PublishNewStockDetailEntity bean = gson.fromJson(response, type);

                ArrayList<Map<String, String>> mDatas = new ArrayList<>();

                if (bean.getData() != null && bean.getData().size() > 0) {
                    for (PublishNewStockDetailEntity.DataBean _bean : bean.getData()) {
                        Map<String, String> data1 = new HashMap<>();
                        Map<String, String> data2 = new HashMap<>();

                        data1.put("typeTitle", "申购进程");
                        data2.put("typeTitle", "发行资料");

                        data1.put("title1", "股票名称");
                        data2.put("title1", "发行价格");

                        data1.put("title2", "股票代码");
                        data2.put("title2", "发行市盈率");

                        data1.put("title3", "申购代码");
                        data2.put("title3", "发行总数");

                        data1.put("title4", "申购日期");
                        data2.put("title4", "网上发行");

                        data1.put("title5", "中签公布日");
                        data2.put("title5", "申购上限");

                        data1.put("title6", "中签率");
//                        data2.put("title6", "经营范围");
                        data2.put("title6", "公司简介");

                        data1.put("title7", "上市日期");
//                        data2.put("title7", "公司简介");
                        data2.put("title7", "经营范围");

                        if (!TextUtils.isEmpty(_bean.getSECUABBR())) {
                            data1.put("value1", _bean.getSECUABBR());
                        } else {
                            data1.put("value1", "");
                        }

                        if (!TextUtils.isEmpty(_bean.getISSUEPRICE())) {
                            data2.put("value1", mFormat1.format(TransitionUtils.string2double(_bean.getISSUEPRICE())));
                        } else {
                            data2.put("value1", "");
                        }

                        if (!TextUtils.isEmpty(_bean.getSECUCODE())) {
                            data1.put("value2", Helper.getStockNumber(_bean.getSECUCODE()));
                        } else {
                            data1.put("value2", "");
                        }

                        if (!TextUtils.isEmpty(_bean.getDILUTEDPERATIO())) {
                            data2.put("value2", mFormat1.format(TransitionUtils.string2double(_bean.getDILUTEDPERATIO())));
                        } else {
                            data2.put("value2", "");
                        }

                        if (!TextUtils.isEmpty(_bean.getAPPLYCODEONLINE())) {
                            data1.put("value3", _bean.getAPPLYCODEONLINE());
                        } else {
                            data1.put("value3", "");
                        }

                        if (!TextUtils.isEmpty(_bean.getISSUEVOL())) {
                            String issuevol = _bean.getISSUEVOL();
                            data2.put("value3", TransitionUtils.string2double(issuevol) / 10000 + "万股");
                        } else {
                            data2.put("value3", "");
                        }


                        if (!TextUtils.isEmpty(_bean.getONLINESTARTDATE())) {
                            data1.put("value4", _bean.getONLINESTARTDATE());
                        } else {
                            data1.put("value4", "");
                        }

                        if (!TextUtils.isEmpty(_bean.getOLBEFPUTBACK())) {
                            data2.put("value4",  TransitionUtils.string2double(_bean.getOLBEFPUTBACK())/10000  + "万股");
                        } else {
                            data2.put("value4", "");
                        }

                        if (!TextUtils.isEmpty(_bean.getISSUERESULTPUBLDATE())) {
                            data1.put("value5", _bean.getISSUERESULTPUBLDATE());
                        } else {
                            data1.put("value5", "");
                        }

                        if (!TextUtils.isEmpty(_bean.getAPPLYMAXONLINE())) {
                            data2.put("value5", TransitionUtils.string2double(_bean.getAPPLYMAXONLINE())/10000 + "万股");

                        } else {
                            data2.put("value5", "");
                        }

                        if (!TextUtils.isEmpty(_bean.getLOTRATEONLINE())) {
                            data1.put("value6", mFormat2.format(TransitionUtils.string2double(_bean.getLOTRATEONLINE())));
                        } else {
                            data1.put("value6", "");
                        }

//                        if (!TextUtils.isEmpty(_bean.getBUSINESSMAJOR())) {
//                            data2.put("value6", _bean.getBUSINESSMAJOR());
//                        } else {
//                            data2.put("value6", "");
//                        }

                        if (!TextUtils.isEmpty(_bean.getBRIEFINTROTEXT())) {
                            data2.put("value6", _bean.getBRIEFINTROTEXT());
                        } else {
                            data2.put("value6", "");
                        }



                        if (!TextUtils.isEmpty(_bean.getLISTDATE())) {
                            data1.put("value7", _bean.getLISTDATE());
                        } else {
                            data1.put("value7", "");
                        }

//                        if (!TextUtils.isEmpty(_bean.getBRIEFINTROTEXT())) {
//                            data2.put("value7", _bean.getBRIEFINTROTEXT());
//                        } else {
//                            data2.put("value7", "");
//                        }

                        if (!TextUtils.isEmpty(_bean.getBUSINESSMAJOR())) {
                            data2.put("value7", _bean.getBUSINESSMAJOR());
                        } else {
                            data2.put("value7", "");
                        }

                        mDatas.add(data1);
                        mDatas.add(data2);

                        mDetailAdapter.setDatas(mDatas);
                    }
                }
                */

            }
        });

    }
}
