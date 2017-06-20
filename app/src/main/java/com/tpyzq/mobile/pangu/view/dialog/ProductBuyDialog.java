package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.ProductBuyActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;


/**
 * Created by zhangwenbo on 2016/10/8.
 */
public class ProductBuyDialog extends BaseDialog {
    private TextView tv_true;
    private TextView tv_cancel;
    private TextView tv_stock_name;
    private TextView tv_stock_code;
    private TextView tv_stock_price;
    private TextView tv_account;
    private ProductBuyDialogPositiveListener listener;
    private ProductBuyActivity.DialogBean dialogBean;

    public ProductBuyDialog(Context context, final ProductBuyDialogPositiveListener listener, ProductBuyActivity.DialogBean dialogBean) {
        super(context);
        this.listener = listener;
        this.dialogBean = dialogBean;
    }

    @Override
    public void setView() {
        tv_true = (TextView) findViewById(R.id.tv_true);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_stock_name = (TextView) findViewById(R.id.tv_stock_name);
        tv_stock_code = (TextView) findViewById(R.id.tv_stock_code);
        tv_stock_price = (TextView) findViewById(R.id.tv_stock_price);
        tv_account = (TextView) findViewById(R.id.tv_account);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_productbuy;
    }

    @Override
    public void initData() {
        tv_stock_name.setText(dialogBean.stockname);
        tv_stock_code.setText(dialogBean.stockcode);
        tv_stock_price.setText(dialogBean.stockprice);
        tv_account.setText(dialogBean.account);
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
