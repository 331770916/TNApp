package com.tpyzq.mobile.pangu.interfac;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.tpyzq.mobile.pangu.activity.home.managerMoney.ProductBuyActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract.AlreadyReservationActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract.LoaderPresenter;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract.ProductPrecontractActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract.ProductTransferActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.PrecontractLoadView;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2017/4/17.
 */

public class DoPrecontractLoadImpl implements IDoPrecontractLoad, PrecontractLoadView {

    private LoaderPresenter mPresenter;
    private Dialog mLoadProgress;
    private String mProductCode;
    private Activity mActivity;
    private String mProductStauts;
    private String mProductType;
    private String mSchema_id;
    private ArrayList<CleverManamgerMoneyEntity> mCleverManamgerMoneyEntitys;

    public DoPrecontractLoadImpl(Activity activity, String productCode, String productType, String schema_id,  ArrayList<CleverManamgerMoneyEntity> cleverManamgerMoneyEntities) {
        mActivity = activity;
        mProductCode = productCode;
        mProductType = productType;
        mSchema_id = schema_id;
        mCleverManamgerMoneyEntitys = cleverManamgerMoneyEntities;
        mPresenter = new LoaderPresenter(this);
    }

    @Override
    public void doPrecontractLoad() {
        mLoadProgress = LoadingDialog.initDialog(mActivity, "正在加载...");
        mLoadProgress.show();

        mPresenter.getProductInfo();
    }

    @Override
    public String getToken() {
        String token = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        return token;
    }

    @Override
    public String getFUNCTIONCODE() {
        return "HQLNG106";
    }

    @Override
    public String getProd_code() {
        return mProductCode;
    }

    @Override
    public String getFund_account() {
        //资金账号
        UserUtil.refrushUserInfo();
        return UserUtil.capitalAccount;
    }

    @Override
    public void queryPrecontractInfoError(String error) {
        progressCancel();

        if ("-6".equals(error)) {
            Intent intent1 = new Intent();
            intent1.setClass(mActivity, TransactionLoginActivity.class);
            mActivity.startActivity(intent1);
            mActivity.finish();
        } else {
            MistakeDialog.showDialog(error, mActivity, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {
                    mActivity.finish();
                }
            });
        }
    }

    @Override
    public void queryPrecontractInfoSuccess(String code, String type, String isorder, String oreder, String force) {
        progressCancel();

        if ("3".equals(mProductStauts)) {//热销中
            Intent intent = new Intent();
            intent.putExtra("productCode", mProductCode);
            intent.putExtra("productType", mProductType);
//            intent.putExtra("pageindex", TransactionLogin_First.PAGE_INDEX_PRODUCTBUY);
            intent.putExtra("schema_id", mSchema_id);
            intent.putExtra("prod_code", mProductCode);

            if ("0".equals(force)) {//是否强制预约(0:是 1:否)
                //如果是热销中  是强制预约的话， 那么 oreder 值必须是0时，详情界面的按钮才能点击 ，否则不能点击
                //是否已经预约(0:是 1:否)
                if ("0".equals(isorder)) {
                    intent.setClass(mActivity, ProductBuyActivity.class);
                    mActivity.startActivity(intent);
                } else if ("1".equals(isorder)) {
                    MistakeDialog.showDialog("本产品需要先预约才能购买", mActivity, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
                            mActivity.finish();
                        }
                    });
                }
            } else {
                intent.setClass(mActivity, ProductBuyActivity.class);
                mActivity.startActivity(intent);
            }
        } else if ("1".equals(mProductStauts) || "2".equals(mProductStauts)) {//预约中
            //是否已经预约(0:是 1:否)
            if ("0".equals(isorder)) {
                Intent intent = new Intent();
                intent.putExtra("productCode", mProductCode);
                intent.setClass(mActivity, AlreadyReservationActivity.class);
                mActivity.startActivity(intent);
                mActivity.finish();

            } else if ("1".equals(isorder)) {
                if ("2".equals(mProductStauts)) {
                    MistakeDialog.showDialog("预约已满", mActivity, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
                            mActivity.finish();
                        }
                    });
                    return;
                }

                //是否可以预约(0:可以预约 1:无法预约 )
                if ("0".equals(oreder)) {
                    Intent intent = new Intent();
                    intent.putExtra("productCode", mProductCode);
                    intent.putExtra("cleverManamgerMoneyEntitys", mCleverManamgerMoneyEntitys);
                    intent.setClass(mActivity, ProductPrecontractActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                } else if ("1".equals(oreder)) {
                    MistakeDialog.showDialog("预约已满", mActivity, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {
                            mActivity.finish();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void getTotalPrice(String totalPrice) {
        progressCancel();

        double total = Double.parseDouble(totalPrice);
        if (total > 50000d) {
            mPresenter.queryPrecontractInfo();
        } else {
            //跳转至银证转账
            Intent intent = new Intent();
            intent.putExtra("total", totalPrice);
            intent.setClass(mActivity, ProductTransferActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    @Override
    public void getProductStauts(String stauts) {
        mProductStauts = stauts;
        //1：预约中，2:预约已满，3：认购中，4：已售罄

        if ("3".equals(mProductStauts)) {//热销中
            mPresenter.queryPrecontractInfo();
        } else if ("1".equals(mProductStauts) || "2".equals(mProductStauts)) {//预约中或者预约已满
            mPresenter.queryTotalPrice();
        } else if ("4".equals(mProductStauts)) {
            MistakeDialog.showDialog("已售罄", mActivity, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {
                    mActivity.finish();
                }
            });
        } else {
            MistakeDialog.showDialog("无数据", mActivity, new MistakeDialog.MistakeDialgoListener() {
                @Override
                public void doPositive() {
                    mActivity.finish();
                }
            });
        }
    }

    private void progressCancel() {
        if (mLoadProgress != null) {
            mLoadProgress.cancel();
            mLoadProgress = null;
        }

        System.gc();
        System.runFinalization();
    }
}
