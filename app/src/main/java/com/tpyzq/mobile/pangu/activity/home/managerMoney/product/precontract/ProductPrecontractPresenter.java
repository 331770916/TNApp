package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract;


import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.ProductPrecontractView;
import com.tpyzq.mobile.pangu.interfac.AddPrecontractImpl;
import com.tpyzq.mobile.pangu.interfac.IAddPrecontract;
import com.tpyzq.mobile.pangu.interfac.IAddPrecontractResult;

/**
 * Created by zhangwenbo on 2017/4/10.
 */

public class ProductPrecontractPresenter {

    private IAddPrecontract mIAddPrecontract;
    private ProductPrecontractView mProductPrecontractView;

    public ProductPrecontractPresenter(ProductPrecontractView productPrecontractView) {
        mIAddPrecontract = new AddPrecontractImpl();
        mProductPrecontractView = productPrecontractView;
    }

    public void addPrecontract() {
        mIAddPrecontract.addPrecontract(mProductPrecontractView.getToken(),
                mProductPrecontractView.getFUNCTIONCODE(),
                mProductPrecontractView.getFund_account(),
                mProductPrecontractView.getOrder_money(),
                mProductPrecontractView.getOrder_prod_code(), new IAddPrecontractResult() {
                    @Override
                    public void getAddPreconractResultSuccess(String code, String type, String message) {
                        mProductPrecontractView.addPrecontractSuccess(code, type, message);
                    }

                    @Override
                    public void getAddPreconractResultError(String error) {
                        mProductPrecontractView.addPrecontractError(error);
                    }
                });
    }
}
