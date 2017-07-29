package com.tpyzq.mobile.pangu.activity.detail.newsTab;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialogTwo;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;

/**
 * 详情页  公告 tab  点击加载更多  跳转的页面
 */
public class AnnouncementStydyDetailActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "AnnouncementStydyDetail";
    private LinearLayout rlStudyDetail;           //背景
    private LinearLayout llStudyDetailXinJiaZai;    //重新加载
    private WebView mWebView;                         //展示内容的 VebView
    private TextView mTitle,tvDetailtitle,tvDetailTime,tvDetailFenGe;         //tab标题 , 内容标题,时间,分割线
    private Dialog dialog;                           //菊花
    private String msgId;                           //信息 ID
    private int type;                               //判断 公告  或者  研报的
    private ScrollView mScrollView;                               //判断 公告  或者  研报的


    @Override
    public void initView() {
        Intent intent = getIntent();
        msgId = intent.getStringExtra("msgId");
        type = intent.getIntExtra("type", -1);

        mTitle = (TextView) this.findViewById(R.id.tvGongGaoYanBaoTitle);
        tvDetailtitle = (TextView) this.findViewById(R.id.tvDetailtitle);           //内容标题
        tvDetailTime = (TextView) this.findViewById(R.id.tvDetailTime);             //内容时间
        tvDetailFenGe = (TextView) this.findViewById(R.id.tvDetailFenGe);           //分割线
        rlStudyDetail = (LinearLayout) this.findViewById(R.id.rlStudyDetail);     //背景
        mScrollView = (ScrollView) this.findViewById(R.id.mScrollView);     //背景

        llStudyDetailXinJiaZai = (LinearLayout) this.findViewById(R.id.llStudyDetailXinJiaZai);     //重新加载
        mWebView = (WebView) this.findViewById(R.id.wvStudyDetail);

        switch (type){
            case 1:
                mTitle.setText("公告");
                break;
            case 2:
                mTitle.setText("研报");
                break;
        }

        initData();
    }

    private void initData() {
        dialog = LoadingDialogTwo.initDialog(AnnouncementStydyDetailActivity.this);
        dialog.show();          //初始化显示菊花
        tvDetailtitle.setVisibility(View.GONE);     //隐藏标题
        tvDetailTime.setVisibility(View.GONE);     //隐藏时间
        tvDetailFenGe.setVisibility(View.GONE);     //隐藏分割线
        mWebView.setVisibility(View.GONE);          //隐藏 webView
        this.findViewById(R.id.ivStudyDetail_back).setOnClickListener(this);
        getData(msgId);
    }

    private void getData(String ids) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("Id",ids);
        map1.put("funcid","900104");
        map1.put("token","");
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_NEW(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i(TAG,e.toString());
                dialog.dismiss();     //隐藏菊花
                rlStudyDetail.setBackgroundColor(ContextCompat.getColor(AnnouncementStydyDetailActivity.this,R.color.white));    //背景 为 白色
                mScrollView.setVisibility(View.GONE);
                tvDetailtitle.setVisibility(View.GONE);     //隐藏标题
                tvDetailTime.setVisibility(View.GONE);     //隐藏时间
                tvDetailFenGe.setVisibility(View.GONE);     //隐藏分割线
                mWebView.setVisibility(View.GONE);          //隐藏 webView
                llStudyDetailXinJiaZai.setVisibility(View.VISIBLE);    //显示 重新加载
                llStudyDetailXinJiaZai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llStudyDetailXinJiaZai.setVisibility(View.GONE);  //隐藏重新加载图片
                        dialog.show();      //显示菊花
                        rlStudyDetail.setVisibility(View.VISIBLE);//显示背景
                        rlStudyDetail.setBackgroundColor(ContextCompat.getColor(AnnouncementStydyDetailActivity.this,R.color.dividerColor)); //设置为灰色

                        //模拟加载数据线程休息1秒
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                super.onPostExecute(result);
                                getData(msgId);
                            }
                        }.execute();
                    }
                });

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if("0".equals(code)){
                        JSONArray data = jsonObject.getJSONArray("data");
                        String title = data.getString(1);
                        String time = data.getString(19);
                        String content = data.getString(23);


                        dialog.dismiss();                               //隐藏 菊花
                        mScrollView.setVisibility(View.VISIBLE);
                        rlStudyDetail.setBackgroundColor(ContextCompat.getColor(AnnouncementStydyDetailActivity.this,R.color.white));
                        tvDetailtitle.setVisibility(View.VISIBLE);     //显示 标题
                        tvDetailTime.setVisibility(View.VISIBLE);     //显示 时间
                        tvDetailFenGe.setVisibility(View.VISIBLE);     //显示 分割线
                        mWebView.setVisibility(View.VISIBLE);          //显示 webView

                        tvDetailtitle.setText(title);
                        try {
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                            Date parse = sdf1.parse(time);
                            String format = sdf2.format(parse);
                            tvDetailTime.setText(format);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String newConten = content.replaceAll("\n", "<br>");
                        StringBuffer sb=new StringBuffer();
                        sb.append("<!DOCTYPE html><html><head><meta charset=\\\"UTF-8\\\"<title></title></head><body>")

                                .append(newConten)

                                .append("</body><script>window.onload = function replace(){var str=document.body." +
                                        "innerHTML;var str1=str.replace(/&nbsp;/ig,\\\"\\\");document." +
                                        "write('<div style=\\\"width: 96%;margin-left: 2%;text-align:justify;" +
                                        "letter-spacing: 2px;text-justify:inter-ideograph;" +
                                        "line-height: 40px;font-size: 20px;\\\">'+str1+'</div>');}</script></html>");


                        mWebView.setVerticalScrollBarEnabled(false);        // 设置webview不允许竖直滚动
                        mWebView.getSettings().setDefaultTextEncodingName("utf-8") ;
                        mWebView.getSettings().setJavaScriptEnabled(true);
                        mWebView.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_stydy_detail;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivStudyDetail_back){
            dialog.dismiss();
            finish();
        }
    }
}
