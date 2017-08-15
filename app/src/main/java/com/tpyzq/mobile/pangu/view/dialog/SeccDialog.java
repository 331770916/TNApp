package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.SeccAdapter;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.util.ConstantUtil;

/**
 * Created by 33920_000 on 2017/8/14.
 */

public class SeccDialog extends BaseDialog implements View.OnClickListener {
    private SeccDialogCallListener listener;
    private Context mContext;
    private ListView lv;
    private String sec_code;
    private SeccDialog seccDialog;
    private SeccAdapter seccAdapter;
    private Button bt_false,bt_true;
    private String MARKET_NAME;

    public SeccDialog(Context context,String MARKET_NAME,String sec_code, SeccDialogCallListener listener) {
        super(context);
        this.mContext = context;
        this.sec_code = sec_code;
        this.listener = listener;
        this.MARKET_NAME = MARKET_NAME;
    }
    private void setData(String secc_code){
        this.sec_code = secc_code;
        seccAdapter.setData(secc_code);
    }
    @Override
    public void setView() {
        lv = (ListView)findViewById(R.id.lv);
        bt_true = (Button) findViewById(R.id.bt_true);
        bt_false = (Button) findViewById(R.id.bt_false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_secc;
    }

    @Override
    public void initData() {
        bt_false.setOnClickListener(this);
        bt_true.setOnClickListener(this);
        seccAdapter = new SeccAdapter(mContext,sec_code);
        lv.setAdapter(seccAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sec_code = ConstantUtil.stock_account_list.get(position).get("SECU_ACCOUNT");
                MARKET_NAME = ConstantUtil.stock_account_list.get(position).get("MARKET_NAME");
                seccAdapter.setData(sec_code);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_false:
                dismiss();
                break;
            case R.id.bt_true:
                listener.select(MARKET_NAME,sec_code);
                dismiss();
                break;
        }
    }

    public interface SeccDialogCallListener {
        void select(String MARKET_NAME,String secc_account);
    }
}
