package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.AllotQueryItemAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;
import com.tpyzq.mobile.pangu.view.pulllayou.PullLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.activity.trade.stock.AllotQueryActivity.allToFragmentManager;


/**
 * Created by zhangwenbo on 2016/9/6.
 * 自定义
 */
public class AllotCustomTab extends BaseTab implements TimePickerView.OnTimeSelectListener,
        View.OnClickListener, PullLayout.OnPullCallBackListener {

    public AllotCustomTab(Activity activity, ArrayList<BaseTab> tabs) {
        super(activity, tabs);
    }

    private AllotQueryItemAdapter mAdapter;
    private TimePickerView mPvTime;
    private TextView mQueryStartDate;
    private TextView mQueryEndDate;
    private SimpleDateFormat mFormate;
    private boolean mJuedgeTv;
    private static final String TAG = "AllotCustomTab";
    private String  mSession;

    private ListView mListView;
    private String mNextPager = "";
    private String mLastPager = "";
    private Activity mActivity;
    private PullLayout mPullLayout;
    private Dialog mLoadingDialg;
    private ImageView mEmptyBg;
    private ArrayList<BankAccountEntity> mBeans;

    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;
        mBeans = new ArrayList<>();
        mLoadingDialg = LoadingDialog.initDialog(activity, "正在加载...");
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        mFormate = new SimpleDateFormat("yyyy-MM-dd");
        view.findViewById(R.id.allotCalenderLayout).setVisibility(View.VISIBLE);
        mListView = (ListView) view.findViewById(R.id.allotQueryListView);
        mAdapter = new AllotQueryItemAdapter();
        mListView.setAdapter(mAdapter);
        mPullLayout =  (PullLayout) view.findViewById(R.id.pull_layout);
        mPullLayout.setOnPullListener(this);

        mEmptyBg = (ImageView) view.findViewById(R.id.iv_allotQueryTab);
        mEmptyBg.setVisibility(View.VISIBLE);
        mQueryStartDate = (TextView) view.findViewById(R.id.allotQueryStartDate);
        mQueryEndDate = (TextView) view.findViewById(R.id.allotQueryEndDate);
        mQueryEndDate.setOnClickListener(this);
        mQueryStartDate.setOnClickListener(this);
        view.findViewById(R.id.allotQueryBtn).setOnClickListener(this);

        mQueryStartDate.setText(mFormate.format(new Date()));
        mQueryEndDate.setText(mFormate.format(new Date()));

        mPvTime = new TimePickerView(activity, TimePickerView.Type.YEAR_MONTH_DAY);
        mPvTime.setTime(Helper.getBeforeDate());
        mPvTime.setCyclic(false);
        mPvTime.setCancelable(true);
        mPvTime.setTitle("选择日期");
        //时间选择后回调
        mPvTime.setOnTimeSelectListener(this);
    }

    @Override
    public void onTimeSelect(Date date) {
        if (mJuedgeTv) {
            mQueryStartDate.setText(mFormate.format(date));
        } else {
            mQueryEndDate.setText(mFormate.format(date));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allotQueryStartDate:
                mJuedgeTv = true;
                mPvTime.show();
                break;
            case R.id.allotQueryEndDate:
                mJuedgeTv = false;
                mPvTime.show();
                break;
            case R.id.allotQueryBtn:
                if (!TextUtils.isEmpty(mQueryStartDate.getText().toString())
                        && !TextUtils.isEmpty(mQueryEndDate.getText().toString())) {

                    String startDay = Helper.getMyDate(mQueryStartDate.getText().toString());
                    String endDay = Helper.getMyDate(mQueryEndDate.getText().toString());

                    String str = Helper.compareTo(startDay, endDay);
                    int days = Helper.daysBetween(startDay, endDay);

                    if (str.equalsIgnoreCase(startDay) &&!str.equals(endDay)) {
                        showDialog("起始时间不能大于等于截止时间");
                    } else if (days > 90) {
                        showDialog("起始时间和截止时间不能大于3个月");
                    } else {
                        mEmptyBg.setVisibility(View.GONE);

                        if (mLoadingDialg != null) {
                            mLoadingDialg.show();
                        }

                        getCustomConnect("", startDay, endDay);
                    }
                }
                break;
        }
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(allToFragmentManager,AllotCustomTab.class.toString());
    }

//    @Override
//    public void onLoad() {
//
//        if (!TextUtils.isEmpty(mQueryStartDate.getText().toString()) && !TextUtils.isEmpty(mQueryEndDate.getText().toString())) {
//            getCustomConnect(pager, mQueryStartDate.getText().toString(), mQueryEndDate.getText().toString());
//        }
//    }

    @Override
    public void toStopConnect() {
        super.toStopConnect();
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }

    private void getCustomConnect(String pageNum, String beginDate, String endDate) {

        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);

        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("REQUEST_NUM","30");    //查询条数，用于翻页时使用
        map2.put("POSITION_STR",pageNum);   //第一次为空，第二次输入第一次返回的数据，用于翻页时使用
        map2.put("HIS_TYPE","0");
        map2.put("BEGIN_DATE", beginDate);
        map2.put("END_DATE", endDate);

        map2.put("MONEY_TYPE", "0");
        map1.put("funcid","300410");
        map1.put("token",mSession);
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());

                if (mLoadingDialg != null) {
                    mLoadingDialg.dismiss();
                }

                if (mPullLayout != null) {
                    mPullLayout.finishPull("网络错误", false);
                }

                mEmptyBg.setVisibility(View.VISIBLE);
                mAdapter.setDatas(null);
            }

            @Override
            public void onResponse(String response, int id) {

                if (mLoadingDialg != null) {
                    mLoadingDialg.dismiss();
                }

                if (mPullLayout != null) {
                    mPullLayout.finishPull();
                }

                if (TextUtils.isEmpty(response)) {
                    CentreToast.showText(CustomApplication.getContext(), "" + response);
                    return ;
                }

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<BankAccountEntity>() {}.getType();
                BankAccountEntity bean = gson.fromJson(response, type);

                if (!bean.getCode().equals("0") && !bean.getCode().equals("-6")) {
                    CentreToast.showText(CustomApplication.getContext(), "" + response);
                    return;
                }

                if (bean.getCode().equals("-6")) {

                    Intent intent = new Intent();
                    intent.setClass(mActivity, TransactionLoginActivity.class);
                    mActivity.startActivity(intent);

                    return;
                }


                ArrayList<BankAccountEntity> beans = new ArrayList<BankAccountEntity>();
                for (BankAccountEntity.AccountInfo accountInfo : bean.getData()) {
                    BankAccountEntity _bean = new BankAccountEntity();

                    _bean.setOCCUR_BALANCE(accountInfo.getOCCUR_BALANCE());
                    _bean.setFUND_ACCOUNT_SRC(accountInfo.getFUND_ACCOUNT_SRC());
                    _bean.setBANK_NAME_SRC(accountInfo.getBANK_NAME_SRC());
                    _bean.setBANK_NAME_DEST(accountInfo.getBANK_NAME_DEST());
                    _bean.setBANK_NO_SRC(accountInfo.getBANK_NO_SRC());
                    _bean.setBANK_NO_DEST(accountInfo.getBANK_NO_DEST());
                    _bean.setMONEY_TYPE(accountInfo.getMONEY_TYPE());
                    _bean.setBUSINESS_NAME(accountInfo.getBUSINESS_NAME());
                    _bean.setINIT_DATE(accountInfo.getINIT_DATE());
                    _bean.setCURR_TIME(accountInfo.getCURR_TIME());
                    _bean.setFUND_ACCOUNT_DEST(accountInfo.getFUND_ACCOUNT_DEST());
                    _bean.setPOSITION_STR(accountInfo.getPOSITION_STR());
                    _bean.setBANK_ACCOUNT_DEST(accountInfo.getBANK_ACCOUNT_DEST());
                    _bean.setBANK_ACCOUNT_SRC(accountInfo.getBANK_ACCOUNT_SRC());

                    beans.add(_bean);
                }

                mBeans = beans;

                if (bean.getData() == null && mBeans.size() <= 0 || bean.getData().size() <= 0 && mBeans.size() <= 0 ) {
                    mEmptyBg.setVisibility(View.VISIBLE);
                }

                mAdapter.setDatas(beans);

                if (mBeans.size() > 0) {
                    mNextPager = mBeans.get(mBeans.size() -1).getPOSITION_STR();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        String startDay = Helper.getMyDate(mQueryStartDate.getText().toString());
        String endDay = Helper.getMyDate(mQueryEndDate.getText().toString());
        getCustomConnect(mLastPager, startDay, endDay);
    }

    @Override
    public void onLoad() {
        String startDay = Helper.getMyDate(mQueryStartDate.getText().toString());
        String endDay = Helper.getMyDate(mQueryEndDate.getText().toString());

        if (!TextUtils.isEmpty(mNextPager)) {

            if (!TextUtils.isEmpty(mNextPager)) {
                mLastPager = mNextPager;
            }
            getCustomConnect(mNextPager, startDay, endDay);
        } else {
            getCustomConnect("", startDay, endDay);
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.allot_query_tab;
    }
}
