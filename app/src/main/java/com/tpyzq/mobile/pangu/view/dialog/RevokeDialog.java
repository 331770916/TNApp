package com.tpyzq.mobile.pangu.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/9/11.
 * 撤单  Dialog
 */
public class RevokeDialog extends BaseDialog implements View.OnClickListener {
    private String mName, mTitm, mWithdrawnQty, mPrice, mEntrust, mEntrusNo;
    private TextView tv_revoke_name, tv_revoke_code, tv_revoke_price, tv_revoke_count, tv_revoke_entrus_BS;
    private Button bt_true, bt_false;
    private Expression expression;

    public RevokeDialog(Context context, String name, String titm, String price, String withdrawnQty,
                        String entrustBs, String entrusNo, Expression expression) {
        super(context);
        this.mName = name;
        this.mTitm = titm;
        this.mPrice = price;
        this.mWithdrawnQty = withdrawnQty;
        this.mEntrust = entrustBs;
        this.mEntrusNo = entrusNo;
        this.expression = expression;

    }

    @Override
    public void setView() {
        tv_revoke_name = (TextView) findViewById(R.id.tv_revoke_name);
        tv_revoke_code = (TextView) findViewById(R.id.tv_revoke_code);
        tv_revoke_price = (TextView) findViewById(R.id.tv_revoke_price);
        tv_revoke_count = (TextView) findViewById(R.id.tv_revoke_count);
        tv_revoke_entrus_BS = (TextView) findViewById(R.id.tv_revoke_entrus_BS);
        bt_true = (Button) findViewById(R.id.bt_true);
        bt_false = (Button) findViewById(R.id.bt_false);

    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_revoke;
    }

    private void toConnect(String data) {
        String mSession = SpUtils.getString(context, "mSession", "");
        Map map = new HashMap();
        Map map1 = new HashMap();
        map.put("funcid", "300150");//150
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        map1.put("MARKET", "");
        map1.put("ENTRUST_NO", data);

        NetWorkUtil.getInstence().okHttpForPostString(context, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e("撤单", e.toString());
                expression.State();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("code"))) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                data.getJSONObject(i).getString("ORDER_ID");
                            }
                        }
                        ResultDialog.getInstance().show("" + "委托已提交", R.mipmap.lc_success);
                        expression.State();

                    } else if ("-6".equals(jsonObject.getString("code"))) {
                        context.startActivity(new Intent(context, TransactionLoginActivity.class));
                    } else {
                        MistakeDialog.showDialog(jsonObject.getString("msg"), (Activity) context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initData() {
        bt_true.setOnClickListener(this);
        bt_false.setOnClickListener(this);

        tv_revoke_name.setText(mName);

        tv_revoke_code.setText(Helper.getMyDateHMS(mTitm));

        tv_revoke_price.setText(mPrice);
        tv_revoke_count.setText(mWithdrawnQty);
        switch (mEntrust) {
            case "1":
                tv_revoke_entrus_BS.setText("买入");
                break;
            case "2":
                tv_revoke_entrus_BS.setText("卖出");
                break;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_true:
                toConnect(mEntrusNo);

                dismiss();

                break;
//            ResultDialog.getInstance().show("" + mMsg_Str, R.mipmap.duigou);
//            MistakeDialog.showDialog(mMsg_Str, Password.this);
            case R.id.bt_false:
                dismiss();
                break;
        }
    }

    public interface Expression {
        void State();
    }

    private class RevokeDialogBean {
        private String code;
        private String msg;
        private List<DialogBean> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<DialogBean> getData() {
            return data;
        }

        public void setData(List<DialogBean> data) {
            this.data = data;
        }

        public class DialogBean {
            private String ORDER_ID;

            public String getORDER_ID() {
                return ORDER_ID;
            }

            public void setORDER_ID(String ORDER_ID) {
                this.ORDER_ID = ORDER_ID;
            }
        }
    }
}
