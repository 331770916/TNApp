package com.tpyzq.mobile.pangu.activity.myself.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.account.AssetsAnalysisActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.StockBillActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.StockRecallActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.TradingDynamicsActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.UsableCapitalActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiVerificationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundShareActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_ShareActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.TakeAPositionActivity;
import com.tpyzq.mobile.pangu.adapter.myself.MySelfBurseAdapter;
import com.tpyzq.mobile.pangu.data.FundRedemptionEntity;
import com.tpyzq.mobile.pangu.data.UserMoneyEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SkipUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by 陈新宇 on 2016/9/14.
 * 账户页面
 */
public class BursePager extends BaseMySelfPager implements View.OnClickListener, AdapterView.OnItemClickListener {
    private String TAG = "bursepager";
    private ListView lv_myself;
    private View headerView, footerView;
    private MySelfBurseAdapter mySelfBurseAdapter;
    private String[] money = {"-.--", "-.--", "-.--", "-.--"};
    private TextView tv_property_analyse, tv_transaction_trends, tv_stock_memoirs, tv_stock_bill;
    private TextView tv_allmoney, tv_fund_account, tv_today_money, tv_allposition, tv_usable_money;
    private Dialog mLoadingDialog;

    public BursePager(Context context) {
        super(context);
    }

    private String mSession;

    @Override
    public void setView() {
        lv_myself = (ListView) rootView.findViewById(R.id.lv_myself);
        headerView = View.inflate(mContext, R.layout.item_myself_burse_header, null);
        footerView = View.inflate(mContext, R.layout.item_myself_burse_footer, null);
        tv_allmoney = (TextView) headerView.findViewById(R.id.tv_allmoney);
        tv_fund_account = (TextView) headerView.findViewById(R.id.tv_fund_account);
        tv_today_money = (TextView) headerView.findViewById(R.id.tv_today_money);
        tv_allposition = (TextView) headerView.findViewById(R.id.tv_allposition);
        tv_usable_money = (TextView) headerView.findViewById(R.id.tv_usable_money);
        tv_property_analyse = (TextView) footerView.findViewById(R.id.tv_property_analyse);
        tv_transaction_trends = (TextView) footerView.findViewById(R.id.tv_transaction_trends);
        tv_stock_memoirs = (TextView) footerView.findViewById(R.id.tv_stock_memoirs);
        tv_stock_bill = (TextView) footerView.findViewById(R.id.tv_stock_bill);
    }

    @Override
    public void initData() {
        mySelfBurseAdapter = new MySelfBurseAdapter(mContext);
        lv_myself.addHeaderView(headerView);
        lv_myself.addFooterView(footerView);
        lv_myself.setAdapter(mySelfBurseAdapter);
        tv_property_analyse.setOnClickListener(this);
        lv_myself.setOnItemClickListener(this);
        tv_fund_account.setOnClickListener(this);
        tv_transaction_trends.setOnClickListener(this);
        tv_stock_memoirs.setOnClickListener(this);
        tv_stock_bill.setOnClickListener(this);

    }

    public void refreshView() {
        if ("true".equals(Db_PUB_USERS.queryingIslogin())) {
            getUserInfo();
            tv_fund_account.setClickable(false);
            UserUtil.refrushUserInfo();
        } else {
            tv_fund_account.setClickable(true);
            clearView();
        }
    }

    private void clearView() {
        tv_allmoney.setText("-.--");
        tv_fund_account.setText("登录资金账号");
        tv_today_money.setText("-.--");
        tv_allposition.setText("-.--");
        tv_usable_money.setText("-.--");
        money[0] = "-.--";
        money[1] = "-.--";
        money[2] = "-.--";
        money[3] = "-.--";
        mySelfBurseAdapter.setContext(money);
        mySelfBurseAdapter.notifyDataSetChanged();
    }

    private void getUserInfo() {
        mSession = SpUtils.getString(mContext, "mSession", "");
        HashMap map300608 = new HashMap();
        map300608.put("funcid", "300608");
        map300608.put("token", mSession);
        HashMap map300608_1 = new HashMap();
        map300608_1.put("SEC_ID", "tpyzq");
        map300608_1.put("FLAG", "true");
        map300608.put("parms", map300608_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300608, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String data = object.getString("data");
                    String msg = object.getString("msg");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        UserMoneyEntity userMoneyBean = new Gson().fromJson(jsonArray.getString(0), UserMoneyEntity.class);
                        settingView(userMoneyBean);
                    } else if ("-6".equals(code)) {
                        SkipUtils.getInstance().startLogin(mContext);
//                        mContext.startActivity(new Intent(mContext, TransactionLoginActivity.class));
                    } else {
                        ToastUtils.showShort(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        getStock();

    }

    private void getStock() {

        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "300201");
        map.put("token", mSession);
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("MARKET", "");
        map2.put("SECU_CODE", "");
        map.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        JSONObject jo_data = new JSONObject(jsonArray.getString(0));
                        getFund(jo_data.getString("TOTAL_INCOME_BAL"));
                    }else if ("-6".equals(code)){
                        SkipUtils.getInstance().startLogin(mContext);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFund(final String stock) {
        HashMap map720260 = new HashMap();
        map720260.put("funcid", "720260");
        map720260.put("token", SpUtils.getString(mContext, "mSession", null));
        HashMap map720260_1 = new HashMap();
        map720260_1.put("SEC_ID", "tpyzq");
        map720260_1.put("FLAG", "true");
        map720260.put("parms", map720260_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map720260, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String data = object.getString("data");
                    String code = object.getString("code");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        FundRedemptionEntity fundRedemptionBean = new Gson().fromJson(jsonArray.getString(0), FundRedemptionEntity.class);
                        getOTC(stock, fundRedemptionBean.TOTAL_INCOME);
                    }else if ("-6".equals(code)){
                        SkipUtils.getInstance().startLogin(mContext);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getOTC(final String stock, final String fund) {
        HashMap map300501 = new HashMap();
        map300501.put("funcid", "300501");
        map300501.put("token", mSession);
        HashMap map300501_1 = new HashMap();
        map300501_1.put("SEC_ID", "tpyzq");
        map300501_1.put("FLAG", "true");
        map300501.put("parms", map300501_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_JY, map300501, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String data = object.getString("data");
                    String code = object.getString("code");
                    if ("0".equals(code)) {
                        JSONArray jsonArray = new JSONArray(data);
                        String otc = jsonArray.getJSONObject(0).getString("OTC_INCOME");
                        if (Helper.isDecimal(stock) && Helper.isDecimal(fund) && Helper.isDecimal(otc)) {
                            BigDecimal bd_stock = new BigDecimal(stock);
                            BigDecimal bd_fund = new BigDecimal(fund);
                            BigDecimal bd_otc = new BigDecimal(otc);
                            double todaymoney = bd_stock.add(bd_fund).add(bd_otc).doubleValue();
                            if (todaymoney == 0) {
                                tv_today_money.setText(TransitionUtils.string2doubleS(todaymoney + ""));
                                tv_today_money.setTextColor(ColorUtils.GRAY);
                            } else if (todaymoney > 0) {
                                tv_today_money.setText(TransitionUtils.string2doubleS(todaymoney + ""));
                                tv_today_money.setTextColor(ColorUtils.RED);
                            } else if (todaymoney < 0) {
                                tv_today_money.setText(TransitionUtils.string2doubleS(todaymoney + ""));
                                tv_today_money.setTextColor(ColorUtils.GREEN);
                            }
                        }
                    }else if ("-6".equals(code)){
                        SkipUtils.getInstance().startLogin(mContext);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private BigDecimal getBigDecimal(String s) {
        try {
            if (TextUtils.isEmpty(s) || "".equals(s)) {
                return new BigDecimal("0.0");
            } else {
                return new BigDecimal(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal("0.0");
    }

    private void settingView(UserMoneyEntity userMoneyBean) {
        tv_allmoney.setText(userMoneyBean.ASSET_BALANCE);
        tv_fund_account.setText("资金账号:" + UserUtil.capitalAccount);

        tv_allposition.setText(userMoneyBean.TOTAL_MARKET_VALUE);
        tv_usable_money.setText(userMoneyBean.ENABLE_BALANCE);
        money[0] = userMoneyBean.MARKET_VALUE;
        money[1] = userMoneyBean.OPFUND_MARKET_VALUE;
        money[2] = userMoneyBean.OTC_MARKET_VALUE;
        money[3] = userMoneyBean.FETCH_BALANCE;
        mySelfBurseAdapter.setContext(money);
        mySelfBurseAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() {
        return R.layout.pager_myself_burse;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_property_analyse:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_AssetsAnalysis, intent);
                break;
            case R.id.tv_fund_account:
                if (!Db_PUB_USERS.isRegister()) {
                    mContext.startActivity(new Intent(mContext, ShouJiZhuCeActivity.class));
                } else {
                    if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                        mContext.startActivity(new Intent(mContext, TransactionLoginActivity.class));
                    } else {
                            mContext.startActivity(new Intent(mContext, ShouJiVerificationActivity.class));
                    }
                }

                break;
            case R.id.tv_transaction_trends:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_TradingDynamics, intent);
                break;
            case R.id.tv_stock_memoirs:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_StockRecall, intent);
                break;
            case R.id.tv_stock_bill:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_StockBillActivity, intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0 && position != 5) {
            patr3GotoPage(position);
        }
    }


    private void gotoPage(Activity activity, int a_pageIndex, Intent intent) {
        intent.putExtra("pageindex", a_pageIndex);
        if (!Db_PUB_USERS.isRegister()) {
            intent.setClass(activity, ShouJiZhuCeActivity.class);
        } else {
            if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                if ("true".equals(Db_PUB_USERS.queryingIslogin())) {
                    switch (a_pageIndex) {
                        case TransactionLoginActivity.PAGE_INDEX_AssetsAnalysis:  //资产分析
                            intent.setClass(activity, AssetsAnalysisActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_TradingDynamics: //交易动态
                            intent.setClass(activity, TradingDynamicsActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_StockRecall: //股市回忆录
                            intent.setClass(activity, StockRecallActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_StockBillActivity: //股市月账单
                            intent.setClass(activity, StockBillActivity.class);
                            break;
                    }

                } else {
                    intent.setClass(activity, TransactionLoginActivity.class);
                }
            } else {
                    intent.setClass(activity, ShouJiVerificationActivity.class);
            }
        }
        activity.startActivity(intent);
    }

    private int[] getPageArrayPart = {TransactionLoginActivity.PAGE_INDEX_TakeAPosition, TransactionLoginActivity.PAGE_INDEX_FundShare,
            TransactionLoginActivity.PAGE_INDEX_OTCTakeAPosition, TransactionLoginActivity.PAGE_INDEX_UsableCapital};


    private void patr3GotoPage(int position) {
        Intent intent = new Intent();
        intent.putExtra("pageindex", getPageArrayPart[position - 1]);
        if (!Db_PUB_USERS.isRegister()) {
            intent.setClass(mContext, ShouJiZhuCeActivity.class);
        } else {
            if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                if ("true".equals(Db_PUB_USERS.queryingIslogin())) {
                    switch (position) {
                        case 1:     //持仓股票
                            intent.setClass(mContext, TakeAPositionActivity.class);
                            break;
                        case 2:     //基金份额
                            intent.setClass(mContext, FundShareActivity.class);
                            break;
                        case 3:     //OTC持仓
                            intent.setClass(mContext, OTC_ShareActivity.class);
                            break;
                        case 4:     //可取资金
                            intent.setClass(mContext, UsableCapitalActivity.class);
                            break;
                   }
                } else {
                    intent.setClass(mContext, TransactionLoginActivity.class);
                }
            } else {
                    intent.setClass(mContext, ShouJiVerificationActivity.class);
            }
        }
        mContext.startActivity(intent);
    }

}
