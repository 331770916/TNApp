package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.ProductBuyActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;


/**
 * Created by zhangwenbo on 2016/10/8.
 */
public class ProductBuyDialog extends BaseDialog {
    private String type;
    private TextView tv_true;
    private TextView tv_cancel;
    private TextView tv_stock_name;
    private TextView tv_stock_code;
    private TextView tv_stock_price;
    private TextView tv_account;
    private  TextView tv_fhfs;
    private ProductBuyDialogPositiveListener listener;
    private ProductBuyActivity.DialogBean dialogBean;
    private LinearLayout ll_fhfs;

    public ProductBuyDialog(Context context, final ProductBuyDialogPositiveListener listener, ProductBuyActivity.DialogBean dialogBean,String type) {
        super(context);
        this.listener = listener;
        this.dialogBean = dialogBean;
        this.type = type;
    }

    @Override
    public void setView() {
        tv_true = (TextView) findViewById(R.id.tv_true);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_stock_name = (TextView) findViewById(R.id.tv_stock_name);
        tv_stock_code = (TextView) findViewById(R.id.tv_stock_code);
        tv_stock_price = (TextView) findViewById(R.id.tv_stock_price);
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_fhfs = (TextView) findViewById(R.id.tv_fhfs);
        ll_fhfs = (LinearLayout) findViewById(R.id.ll_fhfs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_productbuy;
    }

    @Override
    public void initData() {
        if ("3".equalsIgnoreCase(type)) {
            ll_fhfs.setVisibility(View.GONE);
        } else {
            ll_fhfs.setVisibility(View.VISIBLE);
        }
        tv_stock_name.setText(dialogBean.stockname);
        tv_stock_code.setText(dialogBean.stockcode);
        tv_stock_price.setText(dialogBean.stockprice);
        tv_account.setText(dialogBean.account);
        tv_fhfs.setText(dialogBean.fhfs);
        tv_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                listener.doPositive();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface ProductBuyDialogPositiveListener {
        void doPositive();
    }

}
