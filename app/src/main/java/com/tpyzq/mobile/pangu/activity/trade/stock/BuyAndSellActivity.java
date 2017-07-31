package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.keyboardlibrary.KeyboardTouchListener;
import com.android.keyboardlibrary.KeyboardUtil;
import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.BaseTransactionPager;
import com.tpyzq.mobile.pangu.activity.trade.view.EntrustTransactionPager;
import com.tpyzq.mobile.pangu.activity.trade.view.FreeStockTransactionPager;
import com.tpyzq.mobile.pangu.activity.trade.view.PositionTransactionPager;
import com.tpyzq.mobile.pangu.activity.trade.view.ScaleTransitionPagerTitleView;
import com.tpyzq.mobile.pangu.activity.trade.view.StockPw;
import com.tpyzq.mobile.pangu.activity.trade.view.SuccessTransactionPager;
import com.tpyzq.mobile.pangu.adapter.trade.BuySellAdapter;
import com.tpyzq.mobile.pangu.adapter.trade.StockVpAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.AuthorizeEntity;
import com.tpyzq.mobile.pangu.data.TimeShareEntity;
import com.tpyzq.mobile.pangu.data.TransStockEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.interfac.StockCodeCallBack;
import com.tpyzq.mobile.pangu.interfac.StockItemCallBack;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.JudgeStockUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.CancelDialog;
import com.tpyzq.mobile.pangu.view.dialog.CommissionedBuyAndSellDialog;
import com.tpyzq.mobile.pangu.view.dialog.FundEntrustDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.magicindicator.MagicIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.ViewPagerHelper;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.util.TransitionUtils.fundPirce;
import static com.tpyzq.mobile.pangu.util.TransitionUtils.string2doubleS;
import static com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils.encryptBySessionKey;


/**
 * 买入卖出界面
 */
public class BuyAndSellActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView iv_delete/*EditText删除小按钮*/;
    private ImageView iv_sub_price, iv_add_price;    //价格减加
    private ImageView iv_sub_sum, iv_add_sum;    //数量减加
    private RadioGroup rg_buysell;  /*买卖状态选择器*/
    private RadioButton rb_buy; //买
    private RadioButton rb_sell; //卖
    private ListView lv_sell, lv_buy;   //卖1~5,买1~5列表
    private EditText et_stock_code, et_price, et_num;
//    private TextView tv_stock_name;
    private TextView tv_drop;
    private TextView tv_rise;
    private TextView tv_sum;
    private BuySellAdapter buyAdapter;
    private BuySellAdapter sellAdapter;
    private String[] price_buy = new String[5];
    private String[] price_sell = new String[5];
    private String[] sum_buy = new String[5];
    private String[] sum_sell = new String[5];
    private ViewPager vp_view;
    private List<BaseTransactionPager> listBuy = new ArrayList<>();
    private List<BaseTransactionPager> listSell = new ArrayList<>();
    private TimeShareEntity timeShareData;
    private TextView iv_depute_way;    //委托方式
    private boolean flag = false;
    private RelativeLayout traLayout1;
    private double price = 0.0;
    private long amount = 0;
    private int num = 0;
    private ImageView publish_detail_back;
    private Button bt_sell, bt_buy;
    private ArrayList<TransStockEntity> transStockBeen;
    private MagicIndicator mi_buy;
    private static final String[] buy_vp = new String[]{"自选", "持仓", "可撤", "已成"};
    private static final String[] sell_vp = new String[]{"持仓", "可撤", "已成"};
    private List<String> buy_vp_list = Arrays.asList(buy_vp);
    private List<String> sell_vp_ist = Arrays.asList(sell_vp);
    private String stockCode;
    private String transactiontype;
    private String entrusttype;
    private Intent intent;
    private RadioGroup.LayoutParams pbuy, psell;
    private String entrustWays;
    private TextView tv_max_buysell;
    private String final_price;
    private KeyboardUtil mKeyBoardUtil;
    private LinearLayout ll_father;
    private TimeCount timeCount;
    private Boolean priceflag = true;
    private StockPw stockPw;
    private final String TAG = BuyAndSellActivity.class.getSimpleName();
    private String stockName = "";
    private boolean isClearHeadData = true;//是否清头部数据，用于修改股票代码
    private boolean isSearchAgin = true;//是否再次查询，searchStock返回-5时，走柜台；再次点击还会走搜索
    private String delist = "";//股票退市提示信息
    private boolean isChangeSearchStatus = true;

    @Override
    public void initView() {
        bt_sell = (Button) findViewById(R.id.bt_sell);
        bt_buy = (Button) findViewById(R.id.bt_buy);
        ll_father = (LinearLayout) findViewById(R.id.ll_father);
        publish_detail_back = (ImageView) findViewById(R.id.publish_detail_back);
        traLayout1 = (RelativeLayout) findViewById(R.id.traLayout1);
        traLayout1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    traLayout1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                if (null ==stockPw) {
                    stockPw = new StockPw(BuyAndSellActivity.this, transStockBeen, traLayout1.getMeasuredWidth(), stockItemCallBack);
                }
            }
        });
        rg_buysell = (RadioGroup) findViewById(R.id.rg_buysell);
        rb_buy = (RadioButton) findViewById(R.id.rb_buy);
        rb_sell = (RadioButton) findViewById(R.id.rb_sell);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_depute_way = (TextView) findViewById(R.id.iv_depute_way);
        iv_sub_price = (ImageView) findViewById(R.id.iv_sub_price);
        iv_add_price = (ImageView) findViewById(R.id.iv_add_price);
        iv_sub_sum = (ImageView) findViewById(R.id.iv_sub_sum);
        iv_add_sum = (ImageView) findViewById(R.id.iv_add_sum);
        lv_buy = (ListView) findViewById(R.id.lv_buy);
        lv_sell = (ListView) findViewById(R.id.lv_sell);
        et_stock_code = (EditText) findViewById(R.id.et_stock_code);
        et_price = (EditText) findViewById(R.id.et_price);
        et_num = (EditText) findViewById(R.id.et_num);
//        tv_stock_name = (TextView) findViewById(R.id.tv_stock_name);
        vp_view = (ViewPager) findViewById(R.id.vp_view);
        tv_drop = (TextView) findViewById(R.id.tv_drop);
        tv_rise = (TextView) findViewById(R.id.tv_rise);
        tv_sum = (TextView) findViewById(R.id.tv_sum);
        mi_buy = (MagicIndicator) findViewById(R.id.mi_buy);
        tv_max_buysell = (TextView) findViewById(R.id.tv_max_buysell);
        initData();
        initMoveKeyBoard(ll_father, null);
    }

    /**
     * 初始化键盘
     *
     * @param rootLayout
     */
    private void initMoveKeyBoard(LinearLayout rootLayout, ScrollView scrollView) {
        mKeyBoardUtil = new KeyboardUtil(this, rootLayout, scrollView);
        mKeyBoardUtil.setOtherEdittext(et_stock_code);
        // monitor the KeyBarod state
        mKeyBoardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        // monitor the finish or next Key
        mKeyBoardUtil.setInputOverListener(new InputOverListener());
        mKeyBoardUtil.setClickOkListener(new KeyboardUtil.ClickOkListener() {
            @Override
            public void onclickOk() {
                //判断股票代码框中股票代码变化没有，如果没有把股票名称 股票代码设置到输入框中
                String currentStockCode = et_stock_code.getText().toString();
                if (!TextUtils.isEmpty(stockName) && !TextUtils.isEmpty(stockCode) && !TextUtils.isEmpty(currentStockCode)&&currentStockCode.equalsIgnoreCase(stockCode.substring(2))) {
                    //拼接股票信息，同getHeaderView
                    String market = JudgeStockUtils.getStockMarket(stockCode);
                    String content = market + stockCode.substring(2);
                    isClearHeadData = false;
                    et_stock_code.setText(stockName +"  "+ content);
                    //焦点设置在价格输入框
//                    et_price.setEnabled(true);
                    et_price.requestFocus();
                    if (!TextUtils.isEmpty(price+"")) {
                        et_price.setSelection((price+"").length());
                    }
                }
            }
        });
        et_stock_code.setOnTouchListener(new KeyboardTouchListener(mKeyBoardUtil, KeyboardUtil.INPUTTYPE_NUM_ABC, -1));
    }

    private class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {
        @Override
        public void KeyBoardStateChange(int state, EditText editText) {

        }
    }

    private class InputOverListener implements KeyboardUtil.InputFinishListener {

        @Override
        public void inputHasOver(int onclickType, EditText editText) {
            if (!TextUtils.isEmpty(et_stock_code.getText().toString()) && et_stock_code.getText().length() >= 3) {
//                mItemLayout.setVisibility(View.GONE);
//                mSimpleRemoteControl.setCommand(new ToGetSearchStockConnect(new GetSearchStockConnect(TAG, mSearchEdit.getText().toString(), mStart)));
//                mSimpleRemoteControl.startConnect();
            }
        }
    }

    private void initData() {
        timeCount = new TimeCount(Helper.getTime(), 1000);
        iv_delete.setVisibility(View.GONE);
        pbuy = new RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        psell = new RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        transStockBeen = new ArrayList<TransStockEntity>();
        timeShareData = new TimeShareEntity();
        FreeStockTransactionPager freeStockTransactionPager = new FreeStockTransactionPager(this, stockCodeCallBack);
        PositionTransactionPager positionTransactionPager = new PositionTransactionPager(this, stockCodeCallBack);
        EntrustTransactionPager entrustTransactionPager = new EntrustTransactionPager(this);
        SuccessTransactionPager successTransactionPager = new SuccessTransactionPager(this);
        listBuy.add(freeStockTransactionPager);
        listBuy.add(positionTransactionPager);
        listBuy.add(entrustTransactionPager);
        listBuy.add(successTransactionPager);
        listSell.add(positionTransactionPager);
        listSell.add(entrustTransactionPager);
        listSell.add(successTransactionPager);
        iv_sub_price.setOnClickListener(this);
        iv_add_price.setOnClickListener(this);
        iv_sub_sum.setOnClickListener(this);
        iv_add_sum.setOnClickListener(this);
        bt_sell.setOnClickListener(this);
        bt_buy.setOnClickListener(this);
        iv_depute_way.setOnClickListener(this);
        publish_detail_back.setOnClickListener(this);
        et_stock_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus&&!TextUtils.isEmpty(stockCode)) {
                    et_stock_code.setText(stockCode.substring(2));
                    et_stock_code.setSelection(6);
                }
            }
        });
//        et_price.setEnabled(false);
//        et_num.setEnabled(false);
        iv_depute_way.setClickable(false);
        iv_add_price.setClickable(false);
        iv_sub_price.setClickable(false);
        iv_add_sum.setClickable(false);
        iv_sub_sum.setClickable(false);
        entrusttype = "0";     //初始化委托方式
        buyAdapter = new BuySellAdapter(this, price_buy, sum_buy, "买");
        sellAdapter = new BuySellAdapter(this, price_sell, sum_sell, "卖");
        et_stock_code.addTextChangedListener(stockCodeWatch);
        rg_buysell.setOnCheckedChangeListener(new ChooseStatus());
        iv_delete.setOnClickListener(this);
        lv_buy.setAdapter(buyAdapter);
        lv_sell.setAdapter(sellAdapter);
        setIndicatorListen(buy_vp_list);        //设置indicator指针
        lv_buy.setOnItemClickListener(this);
        lv_sell.setOnItemClickListener(this);
        vp_view.setAdapter(new StockVpAdapter(listBuy));
        et_price.addTextChangedListener(stockPriceWatch);
        et_num.addTextChangedListener(stockNumWatch);
        intent = getIntent();
        refresh("");
        final String istatus = intent.getStringExtra("status");
        final String istockcode = intent.getStringExtra("stockcode");
        if (!TextUtils.isEmpty(istatus) && "买".equals(istatus)) {
            rb_buy.setChecked(true);        //初始化
        } else if (!TextUtils.isEmpty(istatus) && "卖".equals(istatus)) {
            rb_sell.setChecked(true);        //初始化
        } else {
            rb_buy.setChecked(true);        //初始化
        }
        if (!TextUtils.isEmpty(istockcode)){
            if (istockcode.startsWith("20") || istockcode.startsWith("10") || istockcode.startsWith("12") || istockcode.startsWith("22")) {
                ToastUtils.showShort(BuyAndSellActivity.this, "当前股票代码不可交易");
            } else {
                refresh(istockcode);
                stockCode = istockcode;
                iv_depute_way.setClickable(true);
                iv_depute_way.setBackgroundResource(R.mipmap.icon_entrust_way);
            }
        }

        //底部Viewpager切换监听器
        vp_view.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (rb_buy.isChecked()) {
                    listBuy.get(position).setRefresh();
                } else if (rb_sell.isChecked()) {
                    listSell.get(position).setRefresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        transStockBeen = new ArrayList<TransStockEntity>();
    }


    /**
     * 手机获取验证码倒计时
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            if (!TextUtils.isEmpty(stockCode)) {
                getHeaderView(stockCode);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示

        }
    }

    private void refresh(String refreshcode) {
//        stockCode = refreshcode;
        et_num.setText("");
        getHeaderView(refreshcode);
        priceflag = true;
        priceflag = true;
        timeCount.start();
    }

    /**
     * 设置底部页签切换监听器
     */
    private void setIndicatorListen(final List<String> list) {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(list.get(index));
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setNormalColor(Color.BLACK);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.blue));
                simplePagerTitleView.setText(list.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vp_view.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(getResources().getColor(R.color.blue));
                return linePagerIndicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 1.0f;
                } else if (index == 1) {
                    return 1.0f;
                } else if (index == 2) {
                    return 1.0f;
                } else {
                    return 1.0f;
                }
            }

        });
        mi_buy.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mi_buy, vp_view);
    }

    /**
     * 获取顶部布局的数据
     * 分时图接口
     *
     * @param code
     */
    private void getHeaderView(final String code) {
        timeCount.start();
        Map<String, String> map003 = new HashMap();
        Object[] object = new Object[1];
        map003.put("FUNCTIONCODE", "HQING003");
        Map map2 = new HashMap();
        map2.put("market", "0");
        map2.put("stockcode", code);
        map2.put("time", "93000");
        map2.put("type", "0");
        object[0] = map2;
        map003.put("PARAMS", Arrays.toString(object));
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.URL, map003, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                delist = "";
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    if (TextUtils.isEmpty(response)) {
                        delist = "";
                        return;
                    }
                    JSONArray object = new JSONArray(response);
                    JSONObject jsonObject = object.getJSONObject(0);
                    delist = jsonObject.optString("message");
//                    delist = "欣泰电气股票已于2017年7月17日进入退市整理板交易，退市整理期为30个交易曰，退市整理期届满的次—交易日将终止上市，请您关注投资风险，慎重参与退市股票交易。\n特別提醒您，根据欣泰电气先行賠付方案，退市整理期买入欣泰电气股票的投资者不属于先行赔付对象，产生的损失不会得到先行赔付基金的赔偿。";
                    if (!TextUtils.isEmpty(delist)) {
                        delist = "\u3000\u3000" + delist;
                        delist = delist.replaceAll("\n", "\n\u3000\u3000");
                    }
                    JSONArray jsArray = jsonObject.getJSONArray("data");
                    JSONArray jsArray_stock = jsArray.getJSONArray(0);
                    String closePrice = jsArray.getString(2);
                    timeShareData.mClosePrice = Float.parseFloat(closePrice);

                    price_sell[0] = fundPirce(code,jsArray.getString(29));
                    price_sell[1] = fundPirce(code,jsArray.getString(28));
                    price_sell[2] = fundPirce(code,jsArray.getString(27));
                    price_sell[3] = fundPirce(code,jsArray.getString(26));
                    price_sell[4] = fundPirce(code,jsArray.getString(25));
                    sum_sell[0] = fundPirce(code,jsArray.getString(39));
                    sum_sell[1] = fundPirce(code,jsArray.getString(38));
                    sum_sell[2] = fundPirce(code,jsArray.getString(37));
                    sum_sell[3] = fundPirce(code,jsArray.getString(36));
                    sum_sell[4] = fundPirce(code,jsArray.getString(35));
                    price_buy[0] = fundPirce(code,jsArray.getString(20));
                    price_buy[1] = fundPirce(code,jsArray.getString(21));
                    price_buy[2] = fundPirce(code,jsArray.getString(22));
                    price_buy[3] = fundPirce(code,jsArray.getString(23));
                    price_buy[4] = fundPirce(code,jsArray.getString(24));
                    sum_buy[0] = fundPirce(code,jsArray.getString(30));
                    sum_buy[1] = fundPirce(code,jsArray.getString(31));
                    sum_buy[2] = fundPirce(code,jsArray.getString(32));
                    sum_buy[3] = fundPirce(code,jsArray.getString(33));
                    sum_buy[4] = fundPirce(code,jsArray.getString(34));
                    sellAdapter.setYesterdayPrice(jsArray.getString(2), code);
                    buyAdapter.setYesterdayPrice(jsArray.getString(2), code);
                    String market = JudgeStockUtils.getStockMarket(code);
                    String content = market + code.substring(2);
                    /*et_stock_code.setText(content);
                    et_stock_code.setSelection(content.length());
                    tv_stock_name.setText(jsArray.getString(19));*/
                    stockCode = code;
                    stockName = jsArray.getString(19);
                    isClearHeadData = false;
                    et_stock_code.setText(stockName + "  "+content);
                    String down = string2doubleS(TransitionUtils.string2double(jsArray.getString(2)) * 0.9 + "");
                    String up = string2doubleS(TransitionUtils.string2double(jsArray.getString(2)) * 1.1 + "");
                    tv_drop.setText("跌停:" + down);
                    tv_rise.setText("涨停:" + up);
                    if (priceflag){
                        if ("买".equals(transactiontype)) {
                            if (!"0.0".equals(jsArray.getString(25))) {
                                price = Double.parseDouble(jsArray.getString(25));
                                et_price.setText(fundPirce(code,price + ""));
                            } else {
                                price = Double.parseDouble(jsArray.getString(20));
                                et_price.setText(fundPirce(code,price + ""));
                            }
                        } else if ("卖".equals(transactiontype)) {
                            if (!"0.0".equals(jsArray.getString(20))) {
                                price = Double.parseDouble(jsArray.getString(20));
                                et_price.setText(fundPirce(code,price + ""));
                            } else {
                                price = Double.parseDouble(jsArray.getString(25));
                                et_price.setText(fundPirce(code,price + ""));
                            }
                        }
                    }
//                    et_price.setEnabled(true);
//                    et_price.requestFocus();
                    if (!TextUtils.isEmpty(price+"")) {
                        et_price.setSelection(et_price.getText().length());
                    }
                    mKeyBoardUtil.hideKeyboardLayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    priceflag = false;
                }
            }
        });
    }


    /**
     * 获取最大购买数量
     *
     * @param code  证券代码
     * @param price 证券委托价格
     */
    private void getAmount(String code, String price) {
        HashMap<Object, Object> map300130 = new HashMap<>();
        map300130.put("funcid", "300130");
        map300130.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap<Object, Object> map300130_1 = new HashMap<>();
        map300130_1.put("FLAG", true);
        map300130_1.put("SEC_ID", "tpyzq");
        String market = JudgeStockUtils.getStockMarketCode(code);
        map300130_1.put("MARKET", market);
        map300130_1.put("TRD_ID", entrusttype);
        map300130_1.put("SECU_CODE", code.substring(2));
        map300130_1.put("ORDER_PRICE", price);
        map300130.put("parms", map300130_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300130, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(BuyAndSellActivity.this, "网络访问失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        AuthorizeEntity authorizeBean = new Gson().fromJson(jsonArray.getString(0), AuthorizeEntity.class);
                        num = (int) Double.parseDouble(authorizeBean.TRD_QTY);
                        tv_sum.setText(num + "");

                        if (Helper.isDecimal(et_price.getText().toString())) {
                            Double stock_price = Double.valueOf(et_price.getText().toString());
                            Double stock_sum = Double.valueOf(amount);
                            BigDecimal a1 = new BigDecimal(stock_price);
                            BigDecimal b1 = new BigDecimal(stock_sum);
                            BigDecimal result1 = a1.multiply(b1);

                            if (rb_buy.isChecked()) {
                                bt_buy.setText("买(" + string2doubleS(result1 + "") + ")");
                                bt_sell.setText("卖");
                            } else if (rb_sell.isChecked()) {
                                bt_buy.setText("买");
                                bt_sell.setText("卖(" + string2doubleS(result1 + "") + ")");
                            }
                        }

                        iv_add_sum.setClickable(true);
                        iv_sub_sum.setClickable(true);
//                        et_num.setEnabled(true);
                    } else if ("-6".equals(code)){
                        startActivity(new Intent(BuyAndSellActivity.this, TransactionLoginActivity.class));
                        finish();
                    }else{
                        MistakeDialog.showDialog(msg,BuyAndSellActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 获取最大可卖数量
     *
     * @param code  证券代码
     * @param price 证券委托价格
     */
    private void getMaxSell(String code, String price) {
        HashMap<Object, Object> map300200 = new HashMap<>();
        map300200.put("funcid", "300200");
        map300200.put("token", SpUtils.getString(getApplication(), "mSession", null));
        HashMap<Object, Object> map300200_1 = new HashMap<>();
        map300200_1.put("FLAG", true);
        map300200_1.put("SEC_ID", "tpyzq");
        String market = JudgeStockUtils.getStockMarketCode(code);
        map300200_1.put("MARKET", market);
        map300200_1.put("SECU_CODE", code.substring(2));
        map300200.put("parms", map300200_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300200, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(BuyAndSellActivity.this, "网络访问失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        if (jsonArray.length() == 0) {
                            num = 0;
                            tv_sum.setText(num + "");
                            iv_add_sum.setClickable(true);
                            iv_sub_sum.setClickable(true);
//                            et_num.setEnabled(true);
                        } else {
                            AuthorizeEntity authorizeBean = new Gson().fromJson(jsonArray.getString(0), AuthorizeEntity.class);
                            num = (int) Double.parseDouble(authorizeBean.SHARE_AVL);
                            tv_sum.setText(num + "");
                            iv_add_sum.setClickable(true);
                            iv_sub_sum.setClickable(true);
//                            et_num.setEnabled(true);
                        }
                    }else if ("-6".equals(code)){
                        startActivity(new Intent(BuyAndSellActivity.this, TransactionLoginActivity.class));
                        finish();
                    } else {
                        MistakeDialog.showDialog(msg,BuyAndSellActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 股票代码文字变化监听器
     */
    private TextWatcher stockCodeWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //股票代码输入6位之后，修改编辑框文字，清空头部信息
            if (s.length() == 6){
                if (isChangeSearchStatus&&!isSearchAgin) {
                    isSearchAgin = true;
                } else {
                    isChangeSearchStatus = true;
                }
                if (isClearHeadData) {//请求头部数据返回设置编辑框不清空头部数据
                    clearView(false);
                } else {
                    isClearHeadData = true;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String code = s.toString();
            if (TextUtils.isEmpty(code) || code.length() == 0){
                iv_delete.setVisibility(View.GONE);
            }else {
                iv_delete.setVisibility(View.VISIBLE);
            }
            int Len = 3;
            if(!Helper.isNumeric(code)){
                Len = 2;
            }
            if(code.length() >= Len&&isSearchAgin&&code.length()<=6){
                searchNetStock(code);
            }
//            switch (code.length()) {
//                case 6:
//                    searchNetStock(s.toString());
//                    break;
//                case 8:
//
//                    break;
//                default:
//                    int i = code.length();
//                    et_stock_code.setSelection(et_stock_code.getText().toString().length());
//                    clearView();
//                    break;
//            }
        }
    };

    /**
     * 清空界面数据
     * @param isClearEtCode 是否清空股票代码输入框
     */
    private void clearView(boolean isClearEtCode) {
        stockName = "";
        stockCode = "";
        timeCount.cancel();
        for (int i = 0; i < 5; i++) {
            price_buy[i] = "- -";
            price_sell[i] = "- -";
            sum_buy[i] = "- -";
            sum_sell[i] = "- -";
        }
        sellAdapter.setYesterdayPrice("0", "");
        buyAdapter.setYesterdayPrice("0", "");
        iv_depute_way.setClickable(false);
        iv_depute_way.setText("限价委托");
        iv_depute_way.setBackgroundResource(R.mipmap.default_delegate);
//        iv_depute_way.setBackgroundResource(R.mipmap.icon_entrust_way);
//        et_price.setEnabled(false);
//        et_num.setEnabled(false);
        iv_add_price.setClickable(false);
        iv_sub_price.setClickable(false);
        iv_add_sum.setClickable(false);
        iv_sub_sum.setClickable(false);
//        bt_buy.setClickable(false);
//        bt_sell.setClickable(false);
        entrusttype = "0";
        if (rb_buy.isChecked()) {
            bt_buy.setText("买");
            bt_sell.setText("卖");
        } else {
            bt_buy.setText("买");
            bt_sell.setText("卖");
        }
        if (isClearEtCode) {
            et_stock_code.setText("");
        }
        tv_sum.setText("----");
//        tv_stock_name.setText("");
        et_price.setText("");
        et_num.setText("");
        tv_drop.setText("跌停: - -");
        tv_rise.setText("涨停: - -");
    }

    /**
     * 数量变化监听器
     */
    private TextWatcher stockNumWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String numString = s.toString();

            if (!TextUtils.isEmpty(stockCode)) {
                iv_add_sum.setClickable(true);
                iv_sub_sum.setClickable(true);
            } else {
                iv_add_sum.setClickable(false);
                iv_sub_sum.setClickable(false);
            }
            if (TextUtils.isEmpty(numString)) {
                bt_buy.setText("买");
                bt_sell.setText("卖");
            }
            if (numString.startsWith("0")) {
                ToastUtils.showShort(BuyAndSellActivity.this, "交易数量首位不能为0");
                et_num.setText(TransitionUtils.delHeaderZero(numString));
                return;
            }
            if (numString == null || numString.equals("")) {
                amount = 0;
            } else {
                long numInt = Long.parseLong(numString);
                if (numInt < 0) {
                    Toast.makeText(getApplication(), "请输入一个大于0的数字", Toast.LENGTH_SHORT).show();
                    et_num.setText("");

                } else {
                    amount = numInt;
                    if (Helper.isDecimal(et_price.getText().toString())) {
                        Double stock_price = Double.valueOf(et_price.getText().toString());
                        Double stock_sum = Double.valueOf(amount);
                        BigDecimal a1 = new BigDecimal(stock_price);
                        BigDecimal b1 = new BigDecimal(stock_sum);
                        BigDecimal result1 = a1.multiply(b1);

                        if (rb_buy.isChecked()) {
                            bt_buy.setText("买(" + string2doubleS(result1 + "") + ")");
                            bt_sell.setText("卖");
                        } else if (rb_sell.isChecked()) {
                            bt_buy.setText("买");
                            bt_sell.setText("卖(" + string2doubleS(result1 + "") + ")");
                        }
                    }
                }
            }
        }
    };
    /**
     * 价格变化监听器
     */
    private TextWatcher stockPriceWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String numString = s.toString();
            iv_add_price.setClickable(false);
            iv_sub_price.setClickable(false);
            if (Helper.isDecimal(numString)) {
                if (!TextUtils.isEmpty(stockCode)) {
                    iv_add_price.setClickable(true);
                    iv_sub_price.setClickable(true);
                }
                if (TextUtils.isEmpty(numString) || "0.0".equals(numString) || ".".equals(numString) || "- -".equals(numString)) {
                    price = 0;
                } else {
                    double numInt = Double.parseDouble(numString);
                    if (numInt < 0) {
                        Toast.makeText(getApplication(), "请输入一个大于0的数字", Toast.LENGTH_SHORT).show();
                        et_price.setText("0");
                    } else {
                        price = numInt;

                        if (!TextUtils.isEmpty(stockCode)) {
                            final_price = numString;
                            if ("买".equals(transactiontype)) {
                                if (price > 0) {
                                    getAmount(stockCode, price + "");
                                }
                            } else if ("卖".equals(transactiontype)) {
                                getMaxSell(stockCode, price + "");
                            }
                        }

                        if (Helper.isDecimal(et_price.getText().toString())) {
                            Double stock_price = Double.valueOf(et_price.getText().toString());
                            Double stock_sum = Double.valueOf(amount);
                            BigDecimal a1 = new BigDecimal(stock_price);
                            BigDecimal b1 = new BigDecimal(stock_sum);
                            BigDecimal result1 = a1.multiply(b1);

                            if (rb_buy.isChecked()) {
                                bt_buy.setText("买(" + string2doubleS(result1 + "") + ")");
                                bt_sell.setText("卖");
                            } else if (rb_sell.isChecked()) {
                                bt_buy.setText("买");
                                bt_sell.setText("卖(" + string2doubleS(result1 + "") + ")");
                            }
                        }
                    }
                }
            }
        }
    };

    /**
     * 买一到买五 卖一到卖五
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_buy:
                if ("0".equals(entrusttype) && !TextUtils.isEmpty(price_buy[position]) && !"- -".equals(price_buy[position]) && !"0.0".equals(price_buy[position])) {
                    et_price.setText(price_buy[position]);
                }

                break;
            case R.id.lv_sell:
                if ("0".equals(entrusttype) && !TextUtils.isEmpty(price_sell[position]) && !"- -".equals(price_sell[position]) && !"0.0".equals(price_sell[position])) {
                    et_price.setText(price_sell[position]);
                }
                break;
        }
    }

    /**
     * 顶部RadioGroup选中监听器
     */
    class ChooseStatus implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (group.getId()) {
                case R.id.rg_buysell:
                    switch (checkedId) {
                        case R.id.rb_buy:
                            vp_view.setCurrentItem(0);
                            pbuy.weight = 4;
                            bt_buy.setLayoutParams(pbuy);
                            psell.weight = 1;
                            bt_sell.setLayoutParams(psell);
                            psell.setMargins(TransitionUtils.dp2px(5, BuyAndSellActivity.this), 0, 0, 0);
                            vp_view.setAdapter(new StockVpAdapter(listBuy));
                            transactiontype = "买";
                            tv_max_buysell.setText("最大可买:");
                            setIndicatorListen(buy_vp_list);
                            if (!TextUtils.isEmpty(stockCode)) {
//                                getHeaderView(stockCode);
                                refresh(stockCode);
                            }
                            entrusttype = "0";
                            entrustWays = "";
                            iv_depute_way.setText("限价委托");
                            break;
                        case R.id.rb_sell:
                            vp_view.setCurrentItem(0);
                            psell.weight = 4;
                            psell.setMargins(TransitionUtils.dp2px(5, BuyAndSellActivity.this), 0, 0, 0);
                            bt_sell.setLayoutParams(psell);
                            pbuy.weight = 1;
                            bt_buy.setLayoutParams(pbuy);
                            tv_max_buysell.setText("最大可卖:");
                            vp_view.setAdapter(new StockVpAdapter(listSell));
                            transactiontype = "卖";
                            setIndicatorListen(sell_vp_ist);
                            if (!TextUtils.isEmpty(stockCode)) {
//                                getHeaderView(stockCode);
                                refresh(stockCode);
                            }
                            entrusttype = "0";
                            entrustWays = "";
                            iv_depute_way.setText("限价委托");
                            break;
                    }
                    break;
            }
        }

    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        String stocknum = et_num.getText().toString();
        switch (v.getId()) {
            case R.id.iv_delete:
                isSearchAgin = true;
                clearView(true);
                break;
            case R.id.iv_sub_price:
                price -= 0.01;
                et_price.setText(string2doubleS(price + ""));
                break;
            case R.id.iv_add_price:
                price += 0.01;
                et_price.setText(string2doubleS(price + ""));
                break;
            case R.id.iv_sub_sum:
                amount -= 100;
                et_num.setText(amount + "");
                break;
            case R.id.iv_add_sum:
                String tvSumText = tv_sum.getText().toString();
//                if (!TextUtils.isEmpty(tvSumText) && Helper.isDecimal(tvSumText) && amount < Integer.valueOf(tvSumText)) {
                if (!TextUtils.isEmpty(tvSumText) && Helper.isDecimal(tvSumText)) {
                    amount += 100;
                    et_num.setText(amount + "");
                } else {
                    if(rb_buy.isChecked()){
                        ToastUtils.showShort(this, "请确认可买数量为有效数值");
                    }else if(rb_sell.isChecked()){
                        ToastUtils.showShort(this, "请确认可卖数量为有效数值");
                    }
                }
                break;
            case R.id.publish_detail_back:
                finish();
                break;
            case R.id.bt_buy:
                final String tmpStockNum = stocknum;
                if (!TextUtils.isEmpty(tmpStockNum)) {
                    if (rb_buy.isChecked()) {
                        if (TextUtils.isEmpty(delist)) {
                            toBuy(tmpStockNum);
                        } else {
                            CancelDialog.cancleDialog(BuyAndSellActivity.this, delist, 5000, new CancelDialog.PositiveClickListener() {
                                @Override
                                public void onPositiveClick() {
                                    toBuy(tmpStockNum);
                                }
                            }, null);

                        }
                    } else {
                        rb_buy.setChecked(true);
                    }
                } else {
                    if (rb_buy.isChecked()){
                        ToastUtils.showShort(BuyAndSellActivity.this, "请确认数量为有效数值");
                    }else {
                        rb_buy.setChecked(true);
                    }
                }
                break;
            case R.id.bt_sell:
                if (!TextUtils.isEmpty(stockCode)) {
                    if (rb_sell.isChecked()) {
                        if ("0".equals(et_num.getText().toString()) || TextUtils.isEmpty(et_num.getText().toString())) {
                            ToastUtils.showShort(BuyAndSellActivity.this, "请确认价格或数量为有效数值");
                        } else if ("0".equals(et_price.getText().toString()) || TextUtils.isEmpty(et_price.getText().toString())) {
                            ToastUtils.showShort(BuyAndSellActivity.this, "请确认价格或数量为有效数值");
                        } else {
                            CommissionedBuyAndSellDialog commissionedBuyAndSellDialog = new CommissionedBuyAndSellDialog(this, commissionedBuyAndSell, stockName, stockCode, price + "", stocknum, transactiontype, entrustWays);
                            commissionedBuyAndSellDialog.show();
                        }
                    } else {
                        rb_sell.setChecked(true);
                    }
                } else {
                    if (rb_sell.isChecked()){
                        ToastUtils.showShort(BuyAndSellActivity.this, "请输入股票代码后进行操作");
                    }else {
                        rb_sell.setChecked(true);
                    }
                }
                break;
            case R.id.iv_depute_way:
                FundEntrustDialog fundEntrustDialog = new FundEntrustDialog(this, fundEntrustWays, stockCode);
                fundEntrustDialog.show();
                break;
        }
    }

    private void toBuy(String stocknum) {
        if ("0".equals(et_num.getText().toString()) || TextUtils.isEmpty(et_num.getText().toString()) || "0".equals(et_price.getText().toString()) || TextUtils.isEmpty(et_price.getText().toString())) {
            ToastUtils.showShort(this, "请确认价格或数量为有效数值");
        } else {
            CommissionedBuyAndSellDialog commissionedBuyAndSellDialog = new CommissionedBuyAndSellDialog(this, commissionedBuyAndSell, stockName, stockCode, price + "", stocknum, transactiontype, entrustWays);
            commissionedBuyAndSellDialog.show();
        }
    }

    /**
     * 委托方式回调
     */
    FundEntrustDialog.FundEntrustWays fundEntrustWays = new FundEntrustDialog.FundEntrustWays() {
        @Override
        public void setItem(String type) {
            entrusttype = type;
            if (rb_buy.isChecked()) {
                if (price > 0) {
                    getAmount(stockCode, price + "");
                }
            } else {
                getMaxSell(stockCode, "");
            }

        }

        @Override
        public void setText(String ways) {
            if (TextUtils.isEmpty(ways)) {
                return;
            }
            if ("限价委托".equals(ways)) {
                entrustWays = final_price;
                iv_depute_way.setText(ways);
                et_price.setText(final_price);
                price = Double.valueOf(final_price);

            } else {
                entrustWays = ways;
                iv_depute_way.setText(ways);
                et_price.setText(ways);
                if (rb_buy.isChecked()) {
                    bt_buy.setText("买入");
                    bt_sell.setText("卖");
                } else if (rb_sell.isChecked()) {
                    bt_buy.setText("买");
                    bt_sell.setText("卖出");
                }
            }
        }
    };
    /**
     * 买卖回调
     */
    CommissionedBuyAndSellDialog.CommissionedBuyAndSell commissionedBuyAndSell = new CommissionedBuyAndSellDialog.CommissionedBuyAndSell() {

        @Override
        public void setBuy(String code, String price, String num) {
            getBuy(code, price, num);
        }

        @Override
        public void setSell(String code, String price, String num) {
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(BuyAndSellActivity.this, "股票代码为空", Toast.LENGTH_SHORT).show();
                return;
            }

            getSell(code, price, num);
        }
    };

    /**
     * 购买
     *
     * @param code
     * @param price
     */
    private void getBuy(String code, String price, String num) {
        HashMap map300140 = new HashMap();
        map300140.put("funcid", "300140");
        map300140.put("token", SpUtils.getString(this, "mSession", null));
        map300140.put("secret",  UserUtil.Keyboard);
        HashMap map300140_1 = new HashMap();
        map300140_1.put("SEC_ID", encryptBySessionKey("tpyzq"));
        String market_code = "";
        if (code.startsWith("2")) {
            market_code = "2";
        } else if (code.startsWith("1")) {
            market_code = "1";
        }
        map300140_1.put("MARKET", encryptBySessionKey(market_code));
        map300140_1.put("TRD_ID", encryptBySessionKey("1"));
        map300140_1.put("SECU_CODE", encryptBySessionKey(code.substring(2)));
        map300140_1.put("ORDER_PRICE", encryptBySessionKey(price));
        map300140_1.put("ORDER_QTY", encryptBySessionKey(num));
        map300140_1.put("ENTRUST_PROP", encryptBySessionKey(entrusttype));
        map300140_1.put("FLAG", encryptBySessionKey("true"));
        map300140.put("parms", map300140_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300140, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(BuyAndSellActivity.this, "网络访问失败");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equals(code)) {
                        clearView(true);
                        CentreToast.showText(BuyAndSellActivity.this,"委托已提交",true);
                    }else if ("-6".equals(code)){
                        startActivity(new Intent(BuyAndSellActivity.this, TransactionLoginActivity.class));
                        finish();
                    }  else {
                        MistakeDialog.showDialog(msg,BuyAndSellActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 卖出
     *
     * @param code
     * @param price
     */
    private void getSell(String code, String price, String num) {
        HashMap map300140 = new HashMap();
        map300140.put("funcid", "300140");
        map300140.put("token", SpUtils.getString(this, "mSession", null));
        map300140.put("secret",  UserUtil.Keyboard);
        HashMap map300140_1 = new HashMap();
        map300140_1.put("SEC_ID", encryptBySessionKey("tpyzq"));
        String market_code = "";
        if (code.startsWith("2")) {
            market_code = "2";
        } else if (code.startsWith("1")) {
            market_code = "1";
        }
        map300140_1.put("MARKET", encryptBySessionKey(market_code));
        map300140_1.put("TRD_ID", encryptBySessionKey("2"));
        map300140_1.put("SECU_CODE", encryptBySessionKey(code.substring(2)));
        map300140_1.put("ORDER_PRICE", encryptBySessionKey(price));
        map300140_1.put("ORDER_QTY", encryptBySessionKey(num));
        map300140_1.put("ENTRUST_PROP", encryptBySessionKey(entrusttype));
        map300140_1.put("FLAG", encryptBySessionKey("true"));
        map300140.put("parms", map300140_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300140, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showShort(BuyAndSellActivity.this, "网络访问失败");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    if ("0".equals(code)) {
                        clearView(true);
                        ToastUtils.showShort(BuyAndSellActivity.this,"委托已提交");
                    }else if ("-6".equals(code)){
                        startActivity(new Intent(BuyAndSellActivity.this, TransactionLoginActivity.class));
                        finish();
                    } else {
                        MistakeDialog.showDialog(msg,BuyAndSellActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    /**
//     * 重写onStart方法
//     */
//    @Override
//    protected void onStart() {
//        listBuy.clear();
//        listBuy.add(new FreeStockTransactionPager(this, stockCodeCallBack));
//        listBuy.add(new PositionTransactionPager(this, stockCodeCallBack));
//        listBuy.add(new EntrustTransactionPager(this));
//        listBuy.add(new SuccessTransactionPager(this));
//        super.onStart();
//    }

    /**
     * 股票搜索接口
     *
     * @param stockInfo
     */
    private void searchNetStock(final String stockInfo) {

        timeCount.cancel();

        Map map = new HashMap();
        Object[] object = new Object[1];
        Map map2 = new HashMap();
        map2.put("arg", stockInfo);
        map2.put("start", "0");
        map2.put("num", "20");
        object[0] = map2;
        Gson gson = new Gson();
        String strJson = gson.toJson(object);
        map.put("FUNCTIONCODE", "HQING006");
        map.put("PARAMS", strJson);
        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.URL, map, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e("BuyAndSellActivity", e.toString());
                if (stockInfo.length()==6) {
                    confirmCode(stockInfo);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        String data = jsonObject.getString("data");
                        JSONArray jaData = new JSONArray(data);
                        transStockBeen.clear();
                        for (int i = 0; i < jaData.length(); i++) {
                            TransStockEntity transStockBean = new TransStockEntity();
                            transStockBean.stockCode = jaData.getJSONArray(i).getString(0);
                            transStockBean.stockName = jaData.getJSONArray(i).getString(1);
                            transStockBeen.add(transStockBean);
                        }
                        if (this != null) {
                            if (!BuyAndSellActivity.this.isFinishing()) {
                                if (null!=stockPw&&!stockPw.isShowing()) {
                                    stockPw.showPopupWindow(traLayout1);
                                }
                                if (null!=stockPw) {
                                    stockPw.refreshView();
                                }
                            }
                        }
                    }else if ("-5".equals(code)) {
//                        et_stock_code.setText("");
                        if (null!=stockPw&&stockPw.isShowing()) {
                            stockPw.dismiss();
                        }
//                        Helper.getInstance().showToast(BuyAndSellActivity.this, "该股票不存在");
                        // TODO: 2017/7/20 调用333000接口 获取股票代码名称和最新价
                        if (stockInfo.length()==6) {
                            confirmCode(stockInfo);
                        }
                    } else {
                        if (null!=stockPw&&stockPw.isShowing()) {
                            stockPw.dismiss();
                        }
                        if (stockInfo.length()==6) {
                            confirmCode(stockInfo);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void confirmCode(String stockInfo) {
        Map map1 = new HashMap<>();
        map1.put("funcid", "333000");
        map1.put("token", SpUtils.getString(CustomApplication.getContext(), "mSession", ""));
        Map map2 = new HashMap<>();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("STOCK_CODE", stockInfo);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Helper.getInstance().showToast(BuyAndSellActivity.this, ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    isSearchAgin = false;
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        jsonObject = jsonArray.optJSONObject(0);
                        String EXCHANGE_TYPE = jsonObject.optString("EXCHANGE_TYPE");
                        stockName = jsonObject.optString("STOCK_NAME");
                        String g_code = jsonObject.optString("STOCK_CODE");
                        String LAST_PRICE = jsonObject.optString("LAST_PRICE");
                        /**
                         * 柜台返回价格取2位小数，并且对6位股票代码转换为8位
                         */
                        DecimalFormat mFormat1 = new DecimalFormat("#0.00");
                        LAST_PRICE = mFormat1.format(Double.parseDouble(LAST_PRICE));
                        String market = "";
                        if("1".equals(EXCHANGE_TYPE)){
                            market = "SH";
                            stockCode = Helper.getStockCode(g_code,"83");
                        }else{
                            market = "SZ";
                            stockCode = Helper.getStockCode(g_code,"90");
                        }
                        isClearHeadData = false;
                        et_price.setText(LAST_PRICE);
                        String content = market + g_code;
                        isChangeSearchStatus = false;
                        et_stock_code.setText(stockName + "  "+content);
//                        et_price.setEnabled(true);
                        et_price.requestFocus();
                        mKeyBoardUtil.hideAllKeyBoard();

                        String closePrice = jsonObject.optString("CLOSE_PRICE");
                        if (TextUtils.isEmpty(closePrice)) {
                            closePrice = "0.00";
                        }
                        timeShareData.mClosePrice = Float.parseFloat(closePrice);

                        price_sell[0] = fundPirce(stockCode,jsonObject.optString("SALE_PRICE1"));
                        price_sell[1] = fundPirce(stockCode,jsonObject.optString("SALE_PRICE2"));
                        price_sell[2] = fundPirce(stockCode,jsonObject.optString("SALE_PRICE3"));
                        price_sell[3] = fundPirce(stockCode,jsonObject.optString("SALE_PRICE4"));
                        price_sell[4] = fundPirce(stockCode,jsonObject.optString("SALE_PRICE5"));
                        sum_sell[0] = fundPirce(stockCode,jsonObject.optString("SALE_AMOUNT1"));
                        sum_sell[1] = fundPirce(stockCode,jsonObject.optString("SALE_AMOUNT2"));
                        sum_sell[2] = fundPirce(stockCode,jsonObject.optString("SALE_AMOUNT3"));
                        sum_sell[3] = fundPirce(stockCode,jsonObject.optString("SALE_AMOUNT4"));
                        sum_sell[4] = fundPirce(stockCode,jsonObject.optString("SALE_AMOUNT5"));
                        price_buy[0] = fundPirce(stockCode,jsonObject.optString("BUY_PRICE1"));
                        price_buy[1] = fundPirce(stockCode,jsonObject.optString("BUY_PRICE2"));
                        price_buy[2] = fundPirce(stockCode,jsonObject.optString("BUY_PRICE3"));
                        price_buy[3] = fundPirce(stockCode,jsonObject.optString("BUY_PRICE4"));
                        price_buy[4] = fundPirce(stockCode,jsonObject.optString("BUY_PRICE5"));
                        sum_buy[0] = fundPirce(stockCode,jsonObject.optString("SALE_AMOUNT1"));
                        sum_buy[1] = fundPirce(stockCode,jsonObject.optString("SALE_AMOUNT2"));
                        sum_buy[2] = fundPirce(stockCode,jsonObject.optString("SALE_AMOUNT3"));
                        sum_buy[3] = fundPirce(stockCode,jsonObject.optString("SALE_AMOUNT4"));
                        sum_buy[4] = fundPirce(stockCode,jsonObject.optString("SALE_AMOUNT5"));
                        sellAdapter.setYesterdayPrice(closePrice, stockCode);
                        buyAdapter.setYesterdayPrice(closePrice, stockCode);

                        String down = string2doubleS(TransitionUtils.string2double(closePrice) * 0.9 + "");
                        String up = string2doubleS(TransitionUtils.string2double(closePrice) * 1.1 + "");
                        tv_drop.setText("跌停:" + down);
                        tv_rise.setText("涨停:" + up);
                    } else {
                        Helper.getInstance().showToast(BuyAndSellActivity.this, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 股票搜索回调
     */
    StockItemCallBack stockItemCallBack = new StockItemCallBack() {
        @Override
        public void stockCode(String stockName, String code) {
            if (TextUtils.isEmpty(code) || code.startsWith("20") || code.startsWith("10") || code.startsWith("12") || code.startsWith("22")) {
                et_stock_code.setText("");
                ToastUtils.showShort(BuyAndSellActivity.this, "当前股票代码不可交易");
            } else {
                setStock(stockName, code);
                refresh(code);
                iv_depute_way.setClickable(true);
                iv_depute_way.setBackgroundResource(R.mipmap.icon_entrust_way);
            }
        }
    };

    private void setStock(String stockName, String code) {
        stockCode = code;
        String market = JudgeStockUtils.getStockMarket(code);
        String content = market + code.substring(2);
        et_stock_code.setText(stockName + "  "+content);
    }

    /**
     * 自选 持仓股票代码回调
     */
    StockCodeCallBack stockCodeCallBack = new StockCodeCallBack() {
        @Override
        public void setStockCode(String stockName, String code) {
            if (TextUtils.isEmpty(code) || code.startsWith("20") || code.startsWith("10") || code.startsWith("12") || code.startsWith("22")) {
                ToastUtils.showShort(BuyAndSellActivity.this, "当前股票代码不可交易");
            } else {
                setStock(stockName, code);
                refresh(code);
//                stockCode = code;
//                stockName = name;
                iv_depute_way.setClickable(true);
                iv_depute_way.setBackgroundResource(R.mipmap.icon_entrust_way);

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtil.cancelSingleRequestByTag(TAG);
    }

    /**
     * 获取布局
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_and_sell;
    }
}
