package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/10/20.
 * 基金分红dialog
 */
public class FundShareDialog extends BaseDialog implements View.OnClickListener {
    private Button bt_true;
    private Button bt_false;
    private String code;
    private int way;

    public FundShareDialog(Context context, String code, int way) {
        super(context);
        this.code = code;
        this.way = way;
    }

    @Override
    public void setView() {
        bt_true = (Button) findViewById(R.id.bt_true);
        bt_false = (Button) findViewById(R.id.bt_false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_fundshare;
    }

    @Override
    public void initData() {
        bt_true.setOnClickListener(this);
        bt_false.setOnClickListener(this);
    }

    /**
     * 获取基金份额
     */
    private void fundQuery() {
        HashMap map720205 = new HashMap();
        map720205.put("funcid", "720205");
        map720205.put("token", SpUtils.getString(context, "mSession", null));
        HashMap map720205_1 = new HashMap();
        map720205_1.put("SEC_ID", "tpyzq");
        map720205_1.put("FUND_CODE", code);
        if (way == 0){      //条目：0 现金分红 1 份额分红    DIVIDEND_WAY值：0 份额分红 1现金分红
            map720205_1.put("DIVIDEND_WAY", "1");   //
        }else {
            map720205_1.put("DIVIDEND_WAY", "0");
        }
        map720205_1.put("FLAG", "true");
        map720205.put("parms", map720205_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_JY_HS(), map720205, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(context, ConstantUtil.NETWORK_ERROR);
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
                        CentreToast.showText(context,"委托已提交",true);
                    } else if ("-6".equals(code)) {
                        context.startActivity(new Intent(context, TransactionLoginActivity.class));
                    } else {
                        CentreToast.showText(context, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_true:
                fundQuery();
                dismiss();
                break;
            case R.id.bt_false:
                dismiss();
                break;
        }
    }
}
