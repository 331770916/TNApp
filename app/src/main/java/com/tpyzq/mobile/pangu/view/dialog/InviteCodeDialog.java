package com.tpyzq.mobile.pangu.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialogCenter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.DeviceUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.panguutil.APPInfoUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/12/8.
 */

public class InviteCodeDialog extends BaseDialogCenter implements View.OnClickListener{

    private static final String TAG = "InviteCodeDialog";
    private EditText mInviteEdit;
    private TextView mResultText;
    private ProgressBar mProgressBar;

    public InviteCodeDialog(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_invite_code;
    }

    @Override
    public void initData() {

        findViewById(R.id.inviteBtn).setOnClickListener(this);
        mInviteEdit = (EditText) findViewById(R.id.inviteEdt);
        mResultText = (TextView) findViewById(R.id.inviteResult);
        mProgressBar = (ProgressBar) findViewById(R.id.inviteLoadProgress);
        mInviteEdit.addTextChangedListener(watcher);
    }

    @Override
    public void onClick(View v) {

        if (checkInvite()) {
            mProgressBar.setVisibility(View.VISIBLE);
            request(mInviteEdit.getText().toString());

        }

    }

    private void request(String inviteCode) {
        Map params = new HashMap();
        Map map = new HashMap();

        String hardwareCode = DeviceUtil.getDeviceId(CustomApplication.getContext());
        map.put("hardwareCode", hardwareCode);//硬件码
        map.put("activateCode",inviteCode);  //邀请码
        String version = APPInfoUtils.getVersionName(CustomApplication.getContext());
        map.put("buildNo", version);        //版本号

        params.put("funcid","400110");
        params.put("token", "");
        params.put("parms",map);


        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                mProgressBar.setVisibility(View.GONE);
                mResultText.setTextColor(Color.RED);
                mResultText.setText("网络异常，邀请失败");
            }

            @Override
            public void onResponse(String response, int id) {
                mProgressBar.setVisibility(View.GONE);
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                LogUtil.i(TAG,response);
//暂时屏蔽网络解析
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                try {
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//
//                    String code = "";
//
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
//                    }
//
//                    if ("0".equals(code)) {
//                        SpUtils.putString(CustomApplication.getContext(), "inviteCode", "0");
//                        dismiss();
//                    } else if ("-5".equals(code)) {
//                        mResultText.setTextColor(Color.RED);
//                        mResultText.setText("邀请失败");
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
    }


    private boolean checkInvite() {
        int length = mInviteEdit.getText().length();
        if (TextUtils.isEmpty(mInviteEdit.getText().toString())) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "邀请码不能为空");
            return false;
        } else if (length < 19) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "请输入完整的邀请码");
            return false;
        }

        return true;
    }


    private TextWatcher watcher = new TextWatcher() {

        String beforeStr = "";
        String afterStr = "";
        String changeStr = "";
        int index = 0;
        boolean changeIndex = true;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            afterStr = s.toString();
            if (changeIndex)
                index = mInviteEdit.getSelectionStart();

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeStr = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            if ("".equals(s.toString()) || s.toString() == null || beforeStr.equals(afterStr)) {
                changeIndex = true;
                return;
            }
            changeIndex = false;
            char c[] = s.toString().replace("-", "").toCharArray();
            changeStr = "";
            for (int i = 0; i < c.length; i++) {
                changeStr = changeStr + c[i] + ((i + 1) % 4 == 0 && i + 1 != c.length ? "-" : "");
            }
            if (afterStr.length() > beforeStr.length()) {
                if (changeStr.length() == index + 1) {
                    index = changeStr.length() - afterStr.length() + index;
                }
                if (index % 5 == 0 && changeStr.length() > index + 1) {
                    index++;
                }
            } else if (afterStr.length() < beforeStr.length()) {
                if ((index + 1) % 5 == 0 && index > 0 && changeStr.length() > index + 1) {
                    //  index--;
                } else {
                    index = changeStr.length() - afterStr.length() + index;
                    if (afterStr.length() % 5 == 0 && changeStr.length() > index + 1) {
                        index++;
                    }
                }
            }
            mInviteEdit.setText(changeStr);
            mInviteEdit.setSelection(index);
        }
    };
}
