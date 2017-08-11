package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/9/22.
 * 交易登录退出 提示
 */
public class TransactionFragmentDialog extends BaseDialog implements View.OnClickListener {
    private static final String TAG = "TransactionFragmentDialog";
    private Button mConfirmbtn, mCancelbtn;
    private String login;
    private TextView mMsg;
    private TransActionBack transActionBack;

    public TransactionFragmentDialog(Context context, TextView msg, TransActionBack transActionBack) {
        super(context);
        this.mMsg = msg;
        this.transActionBack = transActionBack;
    }

    @Override
    public void setView() {
        TextView mPromptTv = (TextView) findViewById(R.id.DLPromptIV);
        TextView mPromptDetailsTV = (TextView) findViewById(R.id.DLPromptDetailsTV);
        mConfirmbtn = (Button) findViewById(R.id.Confirmbtn);
        mCancelbtn = (Button) findViewById(R.id.Cancelbtn);
        Drawable drawable = context.getResources().getDrawable(R.mipmap.tuichujiaoyi);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPromptTv.setCompoundDrawables(drawable, null, null, null);
        mPromptTv.setText("退出交易");
        mPromptDetailsTV.setText("您确认退出交易吗?");
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_item;
    }

    @Override
    public void initData() {
        mConfirmbtn.setOnClickListener(this);
        mCancelbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Cancelbtn:
                dismiss();
                break;
            case R.id.Confirmbtn:
                mMsg.setText("登录");
                transActionBack.callback();
                dismiss();
                UserEntity userEntity = new UserEntity();
                userEntity.setIslogin("false");
//                userEntity.setCertification("");
//                Db_PUB_USERS.UpdateCertification(userEntity);
                userEntity.setKeyboard("false");
                Db_PUB_USERS.UpdateIslogin(userEntity);
                Db_PUB_USERS.UpdateKeyboard(userEntity);
                SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);
                login = userEntity.getIslogin();
                TransactionFragment();
                break;
        }
    }

    /**
     * 退出交易登录
     */
    private void TransactionFragment() {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "300011");
        map.put("token", SpUtils.getString(context, "mSession", null));
        map1.put("SEC_ID", "tpyzq");
        map.put("parms", map1);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e(TAG, e.toString());
                CentreToast.showText(context, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("code"))) {
                        SpUtils.putString(context, "mSession", "");
                        LogUtil.e(TAG, jsonObject.getString("msg"));
                    } else {
                        LogUtil.e(TAG, jsonObject.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public interface TransActionBack {
        void callback();
    }
}
