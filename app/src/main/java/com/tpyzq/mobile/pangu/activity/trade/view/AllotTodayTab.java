package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.AllotQueryActivity;
import com.tpyzq.mobile.pangu.adapter.trade.AllotQueryItemAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.pulllayou.PullLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.activity.trade.stock.AllotQueryActivity.allToFragmentManager;


/**
 * Created by zhangwenbo on 2016/9/6.
 * 今天当日调拨记录
 */
public class AllotTodayTab extends BaseTab implements PullLayout.OnPullCallBackListener {

    public AllotTodayTab(Activity activity, ArrayList<BaseTab> tabs) {
        super(activity, tabs);
        mActivity = activity;
    }

    private AllotQueryItemAdapter mAdapter;
    private static final String TAG = "TodayTab";
    private String  mSession;
    private ListView mListView;
    private PullLayout mRefreshLayout;
    private String mNextPager = "";
    private String mLastPager = "";
    private Activity mActivity;
    private ImageView mEmpty;
    private  ArrayList<BankAccountEntity> mBeans;

    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;
        mBeans = new ArrayList<>();
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);

        mEmpty = (ImageView) view.findViewById(R.id.iv_allotQueryTab);

        mRefreshLayout = (PullLayout) view.findViewById(R.id.pull_layout);
        mListView = (ListView) view.findViewById(R.id.allotQueryListView);
        mAdapter = new AllotQueryItemAdapter();
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setOnPullListener(this);
    }

    @Override
    public void onRefresh() {
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        getTodayConnect(mLastPager);
    }

    @Override
    public void onLoad() {
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);

        if (!TextUtils.isEmpty(mNextPager)) {

            if (!TextUtils.isEmpty(mNextPager)) {
                mLastPager = mNextPager;
            }
            getTodayConnect(mNextPager);
        } else {
            getTodayConnect("");
        }
    }

    @Override
    public void initToConnect() {
        super.initToConnect();
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        getTodayConnect("");
    }

    @Override
    public void toStopConnect() {
        super.toStopConnect();
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }

    private void getTodayConnect(String pageNum) {
        ((AllotQueryActivity)mActivity).loadingProgress();
        mEmpty.setVisibility(View.GONE);

        LogHelper.e(TAG, "*********pageNum:" + pageNum);

        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("REQUEST_NUM","30");    //查询条数，用于翻页时使用
        map2.put("POSITION_STR",pageNum);   //第一次为空，第二次输入第一次返回的数据，用于翻页时使用
        map2.put("MONEY_TYPE", "0");
        map1.put("funcid","300412");
        map1.put("token",mSession);
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                if (mRefreshLayout != null) {
                    mRefreshLayout.finishPull("网络错误", false);
                }

                ((AllotQueryActivity)mActivity).complitedProgress();
                mEmpty.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(String response, int id) {
                //{"code":"0","msg":"(当日调拨记录查询成功)","data":[{"BANK_ACCOUNT_SRC":"5907359","OCCUR_BALANCE":"1009.00","FUND_ACCOUNT_SRC":"520004302","BANK_NAME_SRC":"交行存管","BANK_NAME_DEST":"建行存管","BANK_NO_SRC":"1","BANK_NO_DEST":"3","MONEY_TYPE":"0","BUSINESS_NAME":"手动发起资金调拨","SERIAL_NO":"101","BANK_ACCOUNT_DEST":"8888034001197191","INIT_DATE":"20161206","CURR_TIME":"94017","FUND_ACCOUNT_DEST":"520500009","POSITION_STR":"20161206005200000000101"}]}
                ((AllotQueryActivity)mActivity).complitedProgress();

                if (mRefreshLayout != null) {
                    mRefreshLayout.finishPull();
                }


                if (TextUtils.isEmpty(response)) {
                    showDialog(ConstantUtil.SERVICE_NO_DATA);
                    return ;
                }

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<BankAccountEntity>() {}.getType();
                BankAccountEntity bean = gson.fromJson(response, type);

                if (bean.getCode().equals("-6")) {

                    Intent intent = new Intent();
                    intent.setClass(mActivity, TransactionLoginActivity.class);
                    mActivity.startActivity(intent);

                    return;
                }

                if (!bean.getCode().equals("0")) {
                    mEmpty.setVisibility(View.VISIBLE);
                    showDialog("" + response);
                    return;
                }

                if (bean.getData() == null || bean.getData().size() <= 0) {
                    mEmpty.setVisibility(View.VISIBLE);
                    return;
                }

                if (bean.getData() == null && mBeans.size() <= 0 || bean.getData().size() <= 0 && mBeans.size() <= 0 ) {
                    mEmpty.setVisibility(View.VISIBLE);
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
                mAdapter.setDatas(beans);

                if (mBeans.size() > 0) {
                    mNextPager = mBeans.get(mBeans.size() -1).getPOSITION_STR();
                }

            }
        });
    }

    public void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(allToFragmentManager,AllotTodayTab.class.toString());
    }

    @Override
    public int getLayoutId() {
        return R.layout.banksbusiness_today;
    }
}
