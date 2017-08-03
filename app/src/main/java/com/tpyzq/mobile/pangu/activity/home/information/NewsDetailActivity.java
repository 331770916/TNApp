package com.tpyzq.mobile.pangu.activity.home.information;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.EventDetailListAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.OneTimiceAddSelfChoiceListener;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.ScreenShot;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.FlowLayout;
import com.tpyzq.mobile.pangu.view.SimulateListView;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialogTwo;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * 新闻详情页
 * 刘泽鹏
 */

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener ,InterfaceCollection.InterfaceCallback {

    private static final String TAG = "NewsDetail";
    private PullToRefreshScrollView mPullRefreshScrollView;
    private TextView tvNewsTitle, tvLaiYuanName, tvLaiYuanDate, tv_tags,tvStatement,
            tvShangYou, tvZhongJian, tvXiaYou, tvOneKeyXuan; //标题 ,来源,来源时间,标签,上游，中间，下游,一键自选
    private WebView myWebView;                                             //webView
    private FlowLayout myFlowLayout;                                      //标签
    private FrameLayout llRelevantIndustry;
    private LinearLayout llDetailYiJianZiXuan, llNewDetailJiaZai;      //相关行业 , 一键自选,重新加载的图片
    private SimulateListView tvDetailListView;                                 //listView
    private FlowLayout.LayoutParams layoutParams;                        //flowLayout  的属性设置
    private String requestId;
    //存储 网络请求回来数据的实体类
    private InformationEntity entity;
    private EventDetailListAdapter adapter;
    private ArrayList<StockInfoEntity> beans;
    private List<InformationEntity> tags;
    private Dialog dialog;
    private Dialog loadingDialog;
    private LinearLayout llXiangGuanTitle, llListViewTitle;      //相关行业上面的  title , listView 上面的title
    private TextView tvListViewFenGe, tvChaKanQuanBu;           // listView 上面的分割线 , 查看全部
    private ShareDialog shareDialog ;
    private ImageView ivDetailA;
    private int textSize = 14;
    private String textData,newSum;

    @Override
    public void initView() {
        requestId = getIntent().getStringExtra("requestId");
        if (requestId == null) {
            return;
        }
        this.findViewById(R.id.ivNewsDetail_back).setOnClickListener(this);         //详情页返回按钮
        this.findViewById(R.id.ivDetailFenXiang).setOnClickListener(this);         //详情页分享按钮
        ivDetailA = (ImageView) this.findViewById(R.id.ivDetailA);//字体变大变小
        ivDetailA.setOnClickListener(this);
        mPullRefreshScrollView = (PullToRefreshScrollView) this.findViewById(R.id.svNewsDetail);
        tvLaiYuanName = (TextView)this.findViewById(R.id.tvLaiYuanName);
        tvStatement = (TextView)this.findViewById(R.id.tvStatement);
        tvNewsTitle = (TextView) this.findViewById(R.id.tvNewsTitle);              //标题
//        tvChaKanQuanBu = (TextView) this.findViewById(R.id.tvChaKanQuanBu);         //查看全部
        tvLaiYuanDate = (TextView) this.findViewById(R.id.tvLaiYuanDate);         //发布时间
        tvShangYou = (TextView) this.findViewById(R.id.tvShangYou);                 //上游
        tvZhongJian = (TextView) this.findViewById(R.id.tvZhongJian);               //中间
        tvXiaYou = (TextView) this.findViewById(R.id.tvXiaYou);                      //下游
        tvOneKeyXuan = (TextView) this.findViewById(R.id.tvOneKeyXuan);             //一键自选
        tvListViewFenGe = (TextView) this.findViewById(R.id.tvListViewFenGe);             //listView 上面的分割线
        myWebView = (WebView) this.findViewById(R.id.wvNewDetal);                   // 展示内容的 webView
        myFlowLayout = (FlowLayout) this.findViewById(R.id.FlowLayout);             // 展示标签的流失布局
        llRelevantIndustry = (FrameLayout) this.findViewById(R.id.llRelevantIndustry);     //存放 行业标签的 线性布局
        llDetailYiJianZiXuan = (LinearLayout) this.findViewById(R.id.llDetailYiJianZiXuan); //一键自选的 线性布局
        llXiangGuanTitle = (LinearLayout) this.findViewById(R.id.llXiangGuanTitle); //相关行业上面的  title;
        llListViewTitle = (LinearLayout) this.findViewById(R.id.llListViewTitle); //listView 上面的title
        tvDetailListView = (SimulateListView) this.findViewById(R.id.tvDetailListView);            //展示相关股票的  listView
        mPullRefreshScrollView.scrollTo(0, 0);
//        tvChaKanQuanBu.setOnClickListener(this);
        llDetailYiJianZiXuan.setOnClickListener(this);          //一键自选点击事件
        llNewDetailJiaZai = (LinearLayout) this.findViewById(R.id.llNewDetailJiaZai);   //重新加载图片
        llNewDetailJiaZai.setVisibility(View.GONE);     //初始化  隐藏
        shareDialog  = new ShareDialog(this);
        dialog = LoadingDialogTwo.initDialog(this);       //加载数据的   菊花
        dialog.show();      //显示菊花
        mPullRefreshScrollView.setVisibility(View.GONE);    //隐藏 scrollView
        adapter = new EventDetailListAdapter(this);
        tvDetailListView.setAdapter(adapter);
        layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, TransitionUtils.dp2px(40, this));
        //上拉、下拉设定  Mode.PULL_FROM_START
        mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //上拉监听函数
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (refreshView.isShownHeader()) {
                    //判断头布局是否可见，如果可见执行下拉刷新
                    //设置尾布局样式文字
                    mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    //模拟加载数据线程休息3秒
                    myFlowLayout.removeAllViews();   //清空  子View
                    mInterface.queryDetail(requestId,TAG,NewsDetailActivity.this);
                }
            }
        });
        mInterface.queryDetail(requestId,TAG,NewsDetailActivity.this);
    }

    @Override
    public void callResult(ResultInfo info) {
        dialog.dismiss();
        if(info.getCode().equals("200")){
            mPullRefreshScrollView.setVisibility(View.VISIBLE);
            operation(info.getData());        //给   UI赋值
        }else{
            mPullRefreshScrollView.setVisibility(View.GONE);
        }
    }



    /**
     * 拿到数据后的赋值操作
     */
    private void operation(Object data) {
        if(data!=null&&data instanceof List){
            ArrayList<InformationEntity> mData = (ArrayList<InformationEntity>)data;
            if(mData.size()>0){
                entity = mData.get(0);
                //标题赋值
                tvNewsTitle.setText(entity.getTitle());
                //来源时间赋值
                tvLaiYuanDate.setText(entity.getTime());
                tvLaiYuanName.setText("来源："+entity.getSource());
                tvStatement.setText(entity.getStatement());
                /**
                 * webView 的数据
                 */
                newSum = entity.getContent().replaceAll("\n", "<br>");
                myWebView.getSettings().setDefaultTextEncodingName("utf-8");
                myWebView.getSettings().setJavaScriptEnabled(true);
                myWebView.removeJavascriptInterface("searchBoxJavaBridge_");
                myWebView.removeJavascriptInterface("accessibility");
                myWebView.removeJavascriptInterface("accessibilityTraversal");
                myWebView.getSettings().setSavePassword(false);
                reload();


                /**
                 * 给行业标签赋值
                 */
                tags = entity.getList();
                if(tags!=null&&tags.size()>0){
                    LinearLayout linearLayout;
                    //标签的  颜色
                    int[] tagss = {R.drawable.tag01, R.drawable.tag02, R.drawable.tag03, R.drawable.tag04,
                            R.drawable.tag05, R.drawable.tag06, R.drawable.tag07, R.drawable.tag08};
                    int point = 0;      //设置循环的变量

                    for (int i = 0; i < tags.size(); i++) {

                        linearLayout = new LinearLayout(this);
                        tv_tags = new TextView(this);
                        tv_tags.setText(tags.get(i).getLabelname());       //给标签赋值
                        tv_tags.setTextSize(13);            //设置字体大小
                        tv_tags.setTextColor(ContextCompat.getColor(this, R.color.white));       //设置字体颜色
                        tv_tags.setBackgroundResource(tagss[point]);                             //设置背景

                        //让背景循环起来
                        point++;
                        if (point > 7) {
                            point = 0;
                        }
                        //设置边距
                        tv_tags.setPadding(TransitionUtils.dp2px(5, this), TransitionUtils.dp2px(3, this),
                                TransitionUtils.dp2px(5, this), TransitionUtils.dp2px(3, this));

                        setNewsDetailsTagClick(tv_tags, tv_tags.getText().toString());      //设置标签监听

                        linearLayout.setPadding(TransitionUtils.dp2px(10, this), 0, 0, TransitionUtils.dp2px(10, this));
                        linearLayout.setLayoutParams(layoutParams);     //设置模式
                        linearLayout.addView(tv_tags);                  //添加标签
                        myFlowLayout.addView(linearLayout);
                    }
                }else
                    this.findViewById(R.id.flagLayout).setVisibility(View.GONE);
                mPullRefreshScrollView.onRefreshComplete();
            }
        }
    }


    public void reload(){
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head><meta charset=\\\"UTF-8\\\"<title></title></head>\n" +
                "<body>\n" +
                "<div style=\"width: 96%;margin-left: 2%;padding:25px 0 0px;text-align:justify;letter-spacing: 0.6px;text-justify:inter-ideograph;line-height: 24px;color:#4c4c4c;font-size: ");
        sb.append(String.valueOf(textSize)).append("px;\">");
        sb.append(newSum);
        sb.append("</div>\n" +
                "</body></html>");
        textData = sb.toString();
        myWebView.loadDataWithBaseURL("file://", textData, "text/html", "utf-8", "");
    }


    /**
     * FlowLayout  子View的点击事件
     *
     * @param view    点击的子View
     * @param keyword 要传的值
     */
    private void setNewsDetailsTagClick(View view, final String keyword) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this, IndustryRelevanceActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivNewsDetail_back:        //销毁详情页
                finish();
                break;
            case R.id.ivDetailFenXiang:         //分享
                loadingDialog = LoadingDialog.initDialog(this, "加载中…");
                loadingDialog.show();       //显示菊花
                getShare();
                break;
            case R.id.ivDetailA:
                switch (textSize){
                    case 14:
                        textSize = 18;
                        ivDetailA.setImageResource(R.mipmap.textsizesubtract);
                        reload();
                        break;
                    case 18:
                        textSize = 14;
                        ivDetailA.setImageResource(R.mipmap.textsizeplus);
                        reload();
                        break;
                }
                break;
            case R.id.llDetailYiJianZiXuan:     //一键 自选
                SelfStockHelper.oneTimiceAddSelfChoice(TAG, "", beans, new OneTimiceAddSelfChoiceListener() {
                    @Override
                    public void getResult(String result) {
                        SelfStockHelper.explanOneTimiceAddSelfChoiceResult(NewsDetailActivity.this, result);
                    }
                });
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
        map2.put("phone_type","2");
        map1.put("FUNCTIONCODE", "HQFNG001");
        map1.put("PARAMS", map2);
        NetWorkUtil.getInstence().okHttpForPostString("MarketFragment", ConstantUtil.getURL_HQ_BB(), map1, new StringCallback() {
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
