package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract;


import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.PrecontractLoadView;
import com.tpyzq.mobile.pangu.interfac.DoPrecontractImpl;
import com.tpyzq.mobile.pangu.interfac.GetProductInfoImpl;
import com.tpyzq.mobile.pangu.interfac.IDoPrecontract;
import com.tpyzq.mobile.pangu.interfac.IDoPrecontractResult;
import com.tpyzq.mobile.pangu.interfac.IGetProductInfo;
import com.tpyzq.mobile.pangu.interfac.IGetProductInfoResult;
import com.tpyzq.mobile.pangu.interfac.ITotalPrice;
import com.tpyzq.mobile.pangu.interfac.ITotalPriceResult;
import com.tpyzq.mobile.pangu.interfac.TotalPriceImpl;

/**
 * Created by zhangwenbo on 2017/4/10.
 */

public class LoaderPresenter {

    private IGetProductInfo mIGetProductInfo;
    private ITotalPrice mITotalPrice;
    private IDoPrecontract mIDoPrecontract;
    private PrecontractLoadView mPrecontractLoadView;
    public LoaderPresenter(PrecontractLoadView precontractLoadView) {
        mIGetProductInfo = new GetProductInfoImpl();
        mITotalPrice = new TotalPriceImpl();
        mIDoPrecontract = new DoPrecontractImpl();
        mPrecontractLoadView = precontractLoadView;
    }

    public void getProductInfo() {
        mIGetProductInfo.getProductInfo(mPrecontractLoadView.getToken(),
                mPrecontractLoadView.getProd_code(), new IGetProductInfoResult() {
            @Override
            public void getProductInfoResultSuccess(String stauts) {
                mPrecontractLoadView.getProductStauts(stauts);
            }

            @Override
            public void getProductInfoResultError(String error) {
                mPrecontractLoadView.queryPrecontractInfoError(error);
            }
        });
    }

    public void queryTotalPrice() {
        mITotalPrice.getStrockPrice(mPrecontractLoadView.getToken(), new ITotalPriceResult() {
            @Override
            public void getStockPriceResultSuccess(String price) {
                mPrecontractLoadView.getTotalPrice(price);
            }

            @Override
            public void getStockPriceResultError(String error) {
                mPrecontractLoadView.queryPrecontractInfoError(error);
            }
        });
    }

    public void queryPrecontractInfo() {
        mIDoPrecontract.doPrecontract(mPrecontractLoadView.getToken(),
                mPrecontractLoadView.getFUNCTIONCODE(),
                mPrecontractLoadView.getProd_code(), mPrecontractLoadView.getFund_account(), new IDoPrecontractResult() {
                    @Override
                    public void getDoPreconractResultSuccess(String code, String type, String message, String oreder, String force) {
                        mPrecontractLoadView.queryPrecontractInfoSuccess(code, type, message, oreder, force);
                    }

                    @Override
                    public void getDoPreconractResultError(String error) {
                        mPrecontractLoadView.queryPrecontractInfoError(error);
                    }
                });
    }


}
