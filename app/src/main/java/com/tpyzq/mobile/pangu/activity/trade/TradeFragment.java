package com.tpyzq.mobile.pangu.activity.trade;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiVerificationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundInfoActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundOpenAccountActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundPurchaseActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundRedemptionActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundShareActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundSubsActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundWithDrawActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.OpenFundActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_AccountActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_ElectronicContractActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_QueryActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_RedeemActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_RevokeActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_ShareActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_SubscribeActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_SubscriptionActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BankBusinessIndexActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BuyAndSellActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.NewStockSubscribeActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.ReferActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.RevokeActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.TakeAPositionActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.TranMoreActivity;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.data.FundRedemptionEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.DataUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.TransactionFragmentDialog;
import com.tpyzq.mobile.pangu.view.gridview.MyGridView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by zhangwenbo on 2016/5/11.
 * 交易Fragment
 */
public class TradeFragment extends BaseFragment
        implements View.OnClickListener {
    private static final String TAG = "TransactionFragment";
    private ListView lv_transaction;
    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;
    private TextView tv_drop_transaction;
    private String login;
    private boolean flag = false;       //当前页是否隐藏的标识    true 隐藏 false 显示
    private MyAdapter myAdapter;
    private View[] item = new View[3];              //创建数组保存相应的view，用于展开收缩
    private int openPosition = 0;                   //保存被展开的条目位置
    private boolean firstOpenFlag = false;          //用于判断当前页签是否第一次打开，false未打开，true已打开
    private boolean initListFalg = false;          //用于判断当前列表是否初始化布局，false未初始化，true已初始化

    @Override
    public void initView(View view) {

        lv_transaction = (ListView) view.findViewById(R.id.lv_transaction);
        tv_drop_transaction = (TextView) view.findViewById(R.id.tv_drop_transaction);
        initData();
    }

    private void getServer1(final TextView tv1, final TextView tv2) {
        String mSession = SpUtils.getString(getActivity(), "mSession", "");
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("funcid", "300201");
        map.put("token", mSession);
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", true);
        map2.put("MARKET", "");
        map2.put("SECU_CODE", "");
        map.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
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
                        tv1.setText("总市值:" + jo_data.optString("MKT_VAL"));
                        tv2.setText("总盈亏:" + jo_data.optString("TOTAL_INCOME_BAL"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getServer2(final TextView tv1, final TextView tv2) {

        HashMap map720260 = new HashMap();
        map720260.put("funcid", "720260");
        map720260.put("token", SpUtils.getString(getContext(), "mSession", null));
        HashMap map720260_1 = new HashMap();
        map720260_1.put("SEC_ID", "tpyzq");
        map720260_1.put("FLAG", "true");
        map720260.put("parms", map720260_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map720260, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Toast.makeText(getContext(), "网络访问失败", Toast.LENGTH_SHORT).show();
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
                        tv1.setText("总市值:" + fundRedemptionBean.OPFUND_MARKET_VALUE);
                        tv2.setText("总盈亏:" + fundRedemptionBean.TOTAL_INCOME);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getServer3(final TextView tv1, final TextView tv2) {
        HashMap map300501 = new HashMap();
        map300501.put("funcid", "300501");
        map300501.put("token", SpUtils.getString(getContext(), "mSession", null));
        HashMap map300501_1 = new HashMap();
        map300501_1.put("SEC_ID", "tpyzq");
        map300501_1.put("FLAG", "true");
        map300501.put("parms", map300501_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map300501, new StringCallback() {
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
                        tv1.setText("总市值:" + jsonArray.getJSONObject(0).getString("OTC_MARKET_VALUE"));
                        tv2.setText("总盈亏:" + jsonArray.getJSONObject(0).getString("OTC_INCOME"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        login = Db_PUB_USERS.queryingIslogin();
        myAdapter = new MyAdapter();
        lv_transaction.setAdapter(myAdapter);
        tv_drop_transaction.setOnClickListener(this);
    }


    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_trade;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_drop_transaction) {
            login = Db_PUB_USERS.queryingIslogin();
            Intent intent = new Intent();
            if (!Db_PUB_USERS.isRegister()) {
                intent.setClass(getActivity(), ShouJiZhuCeActivity.class);
                startActivity(intent);
            } else {
                if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                    if ("true".equals(login)) {
                        TransactionFragmentDialog.TransActionBack transActionBack = new TransactionFragmentDialog.TransActionBack() {
                            @Override
                            public void callback() {
                                myAdapter.clearDate();
                                login = "false";
                            }
                        };
                        TransactionFragmentDialog dialog = new TransactionFragmentDialog(getContext(), tv_drop_transaction, transActionBack);
                        dialog.show();
                    } else {
                        intent.setClass(getActivity(), TransactionLoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    intent.setClass(getActivity(), ShouJiVerificationActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    private int[] pageArrayPartOne = {TransactionLoginActivity.PAGE_INDEX_TakeAPosition, TransactionLoginActivity.PAGE_INDEX_BUY_SELL,
            TransactionLoginActivity.PAGE_INDEX_BUY_SELL, TransactionLoginActivity.PAGE_INDEX_Revoke,
            TransactionLoginActivity.PAGE_INDEX_Refer, TransactionLoginActivity.PAGE_INDEX_NewStockSubscribe,
            TransactionLoginActivity.PAGE_INDEX_BusinessIndex, TransactionLoginActivity.PAGE_INDEX_TranMoreActivity
    };

    private void part1GotoPage(int position) {
        Intent intent = new Intent();
        intent.putExtra("pageindex", pageArrayPartOne[position]);
        if (position == 1) {
            intent.putExtra("status", "买");
        } else if (position == 2) {
            intent.putExtra("status", "卖");
        }
        if (!Db_PUB_USERS.isRegister()) {
            intent.setClass(getContext(), ShouJiZhuCeActivity.class);
        } else {
            if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                if ("true".equals(login)) {
                    switch (position) {
                        case 0:
                            intent.setClass(getActivity(), TakeAPositionActivity.class);
                            break;
                        case 1:
                            //买入
                            intent.setClass(getActivity(), BuyAndSellActivity.class);
                            break;
                        case 2:
                            //卖出
                            intent.setClass(getActivity(), BuyAndSellActivity.class);
                            break;
                        case 3:
                            //撤单
                            intent.setClass(getActivity(), RevokeActivity.class);
                            break;
                        case 4:
                            //查询
                            intent.setClass(getActivity(), ReferActivity.class);
                            break;
                        case 5:
                            //新股
                            intent.setClass(getActivity(), NewStockSubscribeActivity.class);
                            break;
                        case 6:
                            //银证转账
                            intent.setClass(getActivity(), BankBusinessIndexActivity.class);
                            break;
                        case 7:
                            //银证转账业务首页
                            intent.setClass(getActivity(), TranMoreActivity.class);
                            break;
                    }
                } else {
                    intent.setClass(getActivity(), TransactionLoginActivity.class);
                }
            } else {
                intent.setClass(getActivity(), ShouJiVerificationActivity.class);
            }
        }
        startActivity(intent);
    }

    private int[] pageArrayPartTow = {TransactionLoginActivity.PAGE_INDEX_FundShare, TransactionLoginActivity.PAGE_INDEX_FundSubsActivity,
            TransactionLoginActivity.PAGE_INDEX_FundPurchaseActivity, TransactionLoginActivity.PAGE_INDEX_FundRedemptionActivity,
            TransactionLoginActivity.PAGE_INDEX_FundWithDrawActivity, TransactionLoginActivity.PAGE_INDEX_FundInfoActivity,
            TransactionLoginActivity.PAGE_INDEX_FundOpenAccountActivity, TransactionLoginActivity.PAGE_INDEX_OpenFundActivity};

    private void part2GotoPage(int position) {
        Intent intent = new Intent();
        intent.putExtra("pageindex", pageArrayPartTow[position]);
        if (!Db_PUB_USERS.isRegister()) {
            intent.setClass(getContext(), ShouJiZhuCeActivity.class);
        } else {
            if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                if ("true".equals(login)) {
                    switch (position) {
                        case 0:
                            //跳转到基金份额页面
                            intent.setClass(getActivity(), FundShareActivity.class);
                            break;
                        case 1:
                            //跳转到基金认购页面
                            intent.setClass(getActivity(), FundSubsActivity.class);
                            break;
                        case 2:
                            //跳转到基金申购页面
                            intent.setClass(getActivity(), FundPurchaseActivity.class);
                            break;
                        case 3:
                            //跳转到基金购回页面
                            intent.setClass(getActivity(), FundRedemptionActivity.class);
                            break;
                        case 4:
                            //跳转到基金撤单页面
                            intent.setClass(getActivity(), FundWithDrawActivity.class);
                            break;
                        case 5:
                            //跳转到基金信息页面
                            intent.setClass(getActivity(), FundInfoActivity.class);
                            break;
                        case 6:
                            //跳转到基金开户页面
                            intent.setClass(getActivity(), FundOpenAccountActivity.class);
                            break;
                        case 7:
                            //跳转到更多页面
                            intent.setClass(getActivity(), OpenFundActivity.class);
                            break;
                    }
                } else {
                    intent.setClass(getActivity(), TransactionLoginActivity.class);
                }
            } else {
                intent.setClass(getActivity(), ShouJiVerificationActivity.class);
            }
        }
        startActivity(intent);
    }

    private int[] getPageArrayPartThree = {TransactionLoginActivity.PAGE_INDEX_OTCTakeAPosition, TransactionLoginActivity.PAGE_INDEX_OTC_SubscriptionActivity,
            TransactionLoginActivity.PAGE_INDEX_OTC_SubscribeActivity, TransactionLoginActivity.PAGE_INDEX_OTC_RedeemActivity, TransactionLoginActivity.PAGE_INDEX_OTC_RevokeActivity,
            TransactionLoginActivity.PAGE_INDEX_OTC_QueryActivity, TransactionLoginActivity.PAGE_INDEX_OTC_AccountActivity, TransactionLoginActivity.PAGE_INDEX_OTC_ElectronicContractActivity};

    private void patr3GotoPage(int position) {
        Intent intent = new Intent();
        intent.putExtra("pageindex", getPageArrayPartThree[position]);
        if (!Db_PUB_USERS.isRegister()) {
            intent.setClass(getContext(), ShouJiZhuCeActivity.class);
        } else {
            if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                if ("true".equals(login)) {
                    switch (position) {
                        case 0:
                            //OTC 份额
                            intent.setClass(getActivity(), OTC_ShareActivity.class);
                            break;
                        case 1:
                            //OTC 认购
                            intent.setClass(getActivity(), OTC_SubscriptionActivity.class);
                            break;
                        case 2:
                            //OTC 申购
                            intent.setClass(getActivity(), OTC_SubscribeActivity.class);
                            break;
                        case 3:
                            //OTC 赎回
                            intent.setClass(getActivity(), OTC_RedeemActivity.class);
                            break;
                        case 4:
                            //OTC 撤单
                            intent.setClass(getActivity(), OTC_RevokeActivity.class);
                            break;
                        case 5:
                            //OTC 查询
                            intent.setClass(getActivity(), OTC_QueryActivity.class);
                            break;
                        case 6:
                            //OTC 开户
                            intent.setClass(getActivity(), OTC_AccountActivity.class);
                            break;
                        case 7:
                            //OTC 电子合同
                            intent.setClass(getActivity(), OTC_ElectronicContractActivity.class);
                            break;
                    }
                } else {
                    intent.setClass(getActivity(), TransactionLoginActivity.class);
                }
            } else {
                intent.setClass(getActivity(), ShouJiVerificationActivity.class);
            }
        }
        startActivity(intent);
    }


    class MyAdapter extends BaseAdapter {
        boolean clearflag = false;
        boolean firstflag = false;

        @Override
        public int getCount() {
            return 3;
        }

        public void clearDate() {
            clearflag = true;
            notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ListViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_transaction, null);
                vh = new ListViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ListViewHolder) convertView.getTag();
            }

            if (clearflag) {
                vh.tv_market_value.setText("总市值:-.--");
                vh.tv_today_value.setText("总盈亏:-.--");
            }
            switch (position) {
                case 0:
                    if (getLoginStatus() && !initListFalg) {
                        getServer1(vh.tv_market_value, vh.tv_today_value);
                        initListFalg = true;
                    }
                    item[0] = vh.itemView;
                    break;
                case 1:
                    item[1] = vh.itemView;
                    break;
                case 2:
                    item[2] = vh.itemView;
                    break;
            }

            switch (position) {
                case 0:
                    if (flag1) {
                        vh.itemView.setSelected(true);
                        vh.ll_grid.setVisibility(View.VISIBLE);
                        vh.iv_item_state.setRotation(180);
                    } else {
                        vh.ll_grid.setVisibility(View.GONE);
                        vh.itemView.setSelected(false);
                        vh.iv_item_state.setRotation(0);
                    }

                    if (!firstflag) {
                        vh.itemView.setSelected(true);
                        vh.ll_grid.setVisibility(View.VISIBLE);
                        vh.iv_item_state.setRotation(180);
                        firstflag = true;
                        flag1 = true;
                    }
                    break;
                case 1:
                    if (flag2) {
                        vh.itemView.setSelected(true);
                        vh.ll_grid.setVisibility(View.VISIBLE);
                        vh.iv_item_state.setRotation(180);
                    } else {
                        vh.ll_grid.setVisibility(View.GONE);
                        vh.itemView.setSelected(false);
                        vh.iv_item_state.setRotation(0);
                    }
                    break;
                case 2:
                    if (flag3) {
                        vh.itemView.setSelected(true);
                        vh.ll_grid.setVisibility(View.VISIBLE);
                        vh.iv_item_state.setRotation(180);
                    } else {
                        vh.ll_grid.setVisibility(View.GONE);
                        vh.itemView.setSelected(false);
                        vh.iv_item_state.setRotation(0);
                    }
            }
            vh.tv_item_title.setText(DataUtils.transaction_name[position]);
            vh.iv_item_img.setImageResource(DataUtils.transaction_icon[position]);
            switch (position) {
                case 0:
                    vh.gv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            part1GotoPage(position);
                        }
                    });
                    break;
                case 1:
                    vh.gv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            part2GotoPage(position);
                        }
                    });

                    break;
                case 2:
                    vh.gv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            patr3GotoPage(position);
                        }
                    });
                    break;
            }
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            BRutil.menuSelect("C001");
                            flag1 = true;
                            flag2 = false;
                            flag3 = false;
                            openPosition = 0;
                            if (getLoginStatus()) {
                                getServer1(vh.tv_market_value, vh.tv_today_value);
                            }
                            break;
                        case 1:
                            BRutil.menuSelect("C002");
                            flag1 = false;
                            flag2 = true;
                            flag3 = false;
                            openPosition = 1;
                            if (getLoginStatus()) {
                                getServer2(vh.tv_market_value, vh.tv_today_value);
                            }
                            break;
                        case 2:
                            BRutil.menuSelect("C003");
                            flag1 = false;
                            flag2 = false;
                            flag3 = true;
                            openPosition = 2;
                            if (getLoginStatus()) {
                                getServer3(vh.tv_market_value, vh.tv_today_value);
                            }
                            break;
                    }
                    notifyDataSetChanged();
                }
            });
            vh.gv_item.setAdapter(new TransactionGrid(DataUtils.transaction_grid_name[position], DataUtils.transaction_grid_icon[position]));
            return convertView;
        }

        class ListViewHolder {
            View itemView;
            ImageView iv_item_img;
            ImageView iv_item_state;
            TextView tv_item_title;
            LinearLayout ll_grid;
            TextView tv_market_value;
            TextView tv_today_value;
            MyGridView gv_item;

            public ListViewHolder(View itemView) {
                this.itemView = itemView;
                iv_item_img = (ImageView) itemView.findViewById(R.id.iv_item_img);
                iv_item_state = (ImageView) itemView.findViewById(R.id.iv_item_state);
                tv_item_title = (TextView) itemView.findViewById(R.id.tv_item_title);
                ll_grid = (LinearLayout) itemView.findViewById(R.id.ll_grid);
                tv_market_value = (TextView) ll_grid.findViewById(R.id.tv_market_value);
                tv_today_value = (TextView) ll_grid.findViewById(R.id.tv_today_value);
                gv_item = (MyGridView) ll_grid.findViewById(R.id.gv_item);
            }
        }

        class TransactionGrid extends BaseAdapter {
            String[] grid_item_name;
            int[] grid_item_icon;

            public TransactionGrid(String[] grid_item_name, int[] grid_item_icon) {
                this.grid_item_name = grid_item_name;
                this.grid_item_icon = grid_item_icon;
            }

            @Override
            public int getCount() {
                return grid_item_name.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final GridViewHolder gvh;
                if (convertView == null) {
                    convertView = View.inflate(getActivity(), R.layout.item_trade_grid, null);
                    gvh = new GridViewHolder(convertView);
                    convertView.setTag(gvh);
                } else {
                    gvh = (GridViewHolder) convertView.getTag();
                }
                //给TextView设置text
                gvh.tv_module.setText(grid_item_name[position]);
                //根据条目是否有文字，判断显示隐藏
                if (grid_item_name[position].equals("")) {
                    gvh.tv_module.setVisibility(View.GONE);
                    gvh.iv_module.setPadding(0, 0, 0, 0);
                } else {
                    gvh.tv_module.setVisibility(View.VISIBLE);
                }
                //给ImageView设置src属性
                gvh.iv_module.setImageResource(grid_item_icon[position]);
                return convertView;
            }

            /**
             * listview条目中的girdiview条目布局
             */
            class GridViewHolder {
                View itemView;
                TextView tv_module;
                ImageView iv_module;

                public GridViewHolder(View itemView) {
                    this.itemView = itemView;
                    tv_module = (TextView) itemView.findViewById(R.id.tv_module);
                    iv_module = (ImageView) itemView.findViewById(R.id.iv_module);
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        flag = hidden;
        if (!hidden) {
            setLoginTag();
        }
    }

    public void setLoginTag() {
        if (getLoginStatus()) {     //因已登录所以按钮变为退出
            tv_drop_transaction.setText("退出");
            if (item[0] != null) {
                item[openPosition].performClick();
            }
        } else {                    //因已登录所以按钮变为退出
            tv_drop_transaction.setText("登录");
            myAdapter.clearDate();
        }
    }

    /**
     * 判断当前是否是登录状态
     *
     * @return true 已登录 false 未登录
     */
    private boolean getLoginStatus() {
        UserUtil.refrushUserInfo();
        login = Db_PUB_USERS.queryingIslogin();
        return "true".equals(login);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstOpenFlag) {
            if (!flag) {
                setLoginTag();
            }
        } else {
            if (getLoginStatus()) {     //因已登录所以按钮变为退出
                tv_drop_transaction.setText("退出");
            }
            firstOpenFlag = true;
        }

    }

}
