package com.tpyzq.mobile.pangu.activity.market.selfChoice.defaultSelfChoic;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.SearchActivity;
import com.tpyzq.mobile.pangu.activity.home.hotsearchstock.HotSearchStockActivity;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.db.StockTable;
import com.tpyzq.mobile.pangu.http.doConnect.self.AddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.HoldCloudConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToAddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToHoldCloudConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/10/25.
 */
public class SelfChoiceDefaultTab extends BaseTabPager implements View.OnClickListener , ICallbackResult {

    private Activity mActivity;
    private Dialog mLoadingDialog;
    private SimpleRemoteControl mSimpleRemoteControl;
    private SelfChoicDefaultImportListener mSelfChoicDefaultImportListener;
    private static final String TAG = "SelfChoiceDefaultTab";

    public SelfChoiceDefaultTab(Activity activity, ArrayList<BaseTabPager> tabs) {
        super(activity, tabs);
        mActivity = activity;
    }

    public void setImportListener(SelfChoicDefaultImportListener selfChoicDefaultImportListener) {
        mSelfChoicDefaultImportListener = selfChoicDefaultImportListener;
    }

    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;

        view.findViewById(R.id.headAddtv).setOnClickListener(this);
        view.findViewById(R.id.importTv).setOnClickListener(this);
        view.findViewById(R.id.importHote).setOnClickListener(this);
    }

    @Override
    public void myTabonResume() {

    }

    @Override
    public void toRunConnect() {

    }

    @Override
    public void toStopConnect() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.headAddtv:
                intent = new Intent(mActivity, SearchActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.importTv:
                if (!Db_PUB_USERS.isRegister()) {
                    intent = new Intent(mActivity, ShouJiZhuCeActivity.class);
                    mActivity.startActivity(intent);
                } else if (!Db_PUB_USERS.islogin()) {
                    intent = new Intent();
                    intent.setClass(mActivity, TransactionLoginActivity.class);
                    mActivity.startActivity(intent);
                } else {
                    //导入持仓
                    mLoadingDialog = LoadingDialog.initDialog(mActivity, "正在导入，请稍后...");
                    mLoadingDialog.show();
                    mSimpleRemoteControl = new SimpleRemoteControl(this);
                    String session = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
                    mSimpleRemoteControl.setCommand(new ToHoldCloudConnect(new HoldCloudConnect(TAG, session)));
                    mSimpleRemoteControl.startConnect();

                }
                break;
            case R.id.importHote:
                intent = new Intent();
                intent.setClass(mActivity, HotSearchStockActivity.class);
                mActivity.startActivity(intent);

                break;
        }
    }

    @Override
    public void getResult(Object result, String tag) {

        if ("HoldCloudConnect".equals(tag)) {

            if (null != result && result instanceof String) {

                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }

                showDialog("" + result);
                return;
            }

            ArrayList<StockInfoEntity> stockInfoEntities = (ArrayList<StockInfoEntity>) result;

            if (stockInfoEntities != null && stockInfoEntities.size() > 0) {
                HOLD_SEQ.deleteAll();

                String stockCodes = "";
                String stockNames = "";
                String stockPricees = "";

                StringBuilder sbCode = new StringBuilder();
                StringBuilder sbName = new StringBuilder();
                StringBuilder sbPrice = new StringBuilder();


                boolean holdFlag = HOLD_SEQ.addHoldDatas(stockInfoEntities);
                if (!holdFlag) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    Helper.getInstance().showToast(CustomApplication.getContext(), "导入持仓自选股数据库失败");
                    return;
                }

                for (int i = 0; i < stockInfoEntities.size(); i++) {
                    stockInfoEntities.get(i).setStock_flag(StockTable.STOCK_OPTIONAL);
                    Db_PUB_STOCKLIST.addOneStockListData(stockInfoEntities.get(i));

                    if (i == stockInfoEntities.size() -1) {
                        sbCode.append(stockInfoEntities.get(i).getStockNumber());
                        sbName.append(stockInfoEntities.get(i).getStockName());
                        sbPrice.append(stockInfoEntities.get(i).getNewPrice());
                    } else {
                        sbCode.append(stockInfoEntities.get(i).getStockNumber()).append(",");
                        sbName.append(stockInfoEntities.get(i).getStockName()).append(",");
                        sbPrice.append(stockInfoEntities.get(i).getNewPrice()).append(",");
                    }

                    SelfStockHelper.sendUpdateSelfChoiceBrodcast(mActivity, stockInfoEntities.get(i).getStockNumber());
                }


                stockCodes = sbCode.toString();
                stockNames = sbName.toString();
                stockPricees = sbPrice.toString();

                mSimpleRemoteControl.setCommand(new ToAddSelfChoiceStockConnect(new AddSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stockCodes, UserUtil.userId, stockNames, stockPricees)));
                mSimpleRemoteControl.startConnect();

                if (mSelfChoicDefaultImportListener != null) {
                    mSelfChoicDefaultImportListener.importHoldResult();
                }

            } else {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                showDialog("无持仓股票");
            }
        } else if ("AddSelfChoiceStockConnect".equals(tag)) {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
            String json = String.valueOf(result);
            SelfStockHelper.explanImportHoldResult(mActivity, json);

        }

    }

    private void showDialog(String msg){
        CustomCenterDialog cu = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        cu.show(mActivity.getFragmentManager(),SelfChoiceDefaultTab.class.toString());
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_choice_default;
    }


    public interface SelfChoicDefaultImportListener {
        void importHoldResult();
    }
}
