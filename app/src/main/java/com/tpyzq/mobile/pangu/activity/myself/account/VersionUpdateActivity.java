package com.tpyzq.mobile.pangu.activity.myself.account;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.panguutil.APPInfoUtils;
import com.tpyzq.mobile.pangu.view.dialog.APPUpdateDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 版本升级
 */
public class VersionUpdateActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv_back;
    TextView tv_version_name;
    TextView tv_update;
    @Override
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        tv_update = (TextView) findViewById(R.id.tv_update);
        initData();
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        tv_update.setOnClickListener(this);
        tv_update.setClickable(false);
        tv_version_name.setText("V"+ APPInfoUtils.getVersionName(this)+"  Beta");
        getData();
    }

    private void getData() {
        HashMap map400101 = new HashMap();
        map400101.put("funcid", "400101");
        map400101.put("token", "");
        HashMap map400101_1 = new HashMap();
        map400101_1.put("versionType", "2");
        map400101.put("parms", map400101_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.getURL_NEW(), map400101, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
//                LogUtil.e("app更新",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        String result = jsonObject.getString("result");
                        JSONObject joResult = new JSONObject(result);
                        String versionNumber = joResult.getString("versionNumber");  //版本号
                        if (TextUtils.isEmpty(versionNumber)) {
                            return;
                        }
                        String[] versionCode = versionNumber.split("\\.");
                        String[] thisVersionCode = APPInfoUtils.getVersionName(VersionUpdateActivity.this).split("\\.");
                        //如果 版本号  与  当前版本号 相同 删除安装包
                        if (Double.parseDouble(versionCode[0]) > Double.parseDouble(thisVersionCode[0])) {
//                        if (!APPInfoUtils.getVersionName(VersionUpdateActivity.this).equals(versionNumber)){
                            tv_update.setTextColor(ColorUtils.BLUE);
                            tv_update.setText("有更新版本，马上安装？");
                            tv_update.setClickable(true);
                        }
                        //版本名
//                        ToastUtils.showShort(VersionUpdateActivity.this,"最新版本号"+versionNumber);
//                        APPUpdateBean appUpdateBean = new Gson().fromJson(result,APPUpdateBean.class);
//                        ToastUtils.showShort(VersionUpdateActivity.this,"版本号"+appUpdateBean.versionNumber);
//                        MistakeDialog.showDialog("版本需要更新", VersionUpdateActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_version_update;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_update:
                APPUpdateDialog appUpdateDialog = new APPUpdateDialog(this);
                appUpdateDialog.show();
                break;
        }
    }
}
