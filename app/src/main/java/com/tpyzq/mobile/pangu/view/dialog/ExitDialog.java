package com.tpyzq.mobile.pangu.view.dialog;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;

import static android.content.Context.ACTIVITY_SERVICE;


/**
 * Created by wangqi on 2016/9/22.
 * 退出App
 */
public class ExitDialog extends BaseDialog implements View.OnClickListener {
    private Button mConfirmbtn, mCancelbtn;

    public ExitDialog(Context context) {
        super(context);
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
        mPromptTv.setText("退出程序");
        mPromptDetailsTV.setText("您确认退出太平洋证券APP吗?");

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
//                CustomApplication.getInstance().finish();
//                ((Activity) context).finish();
                int currentVersion = android.os.Build.VERSION.SDK_INT;
                if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(startMain);
                    System.exit(0);
                } else {// android2.1
                    ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                    am.restartPackage(context.getPackageName());
                }
                UserEntity userEntity = new UserEntity();
//                if ("true".equals(Db_PUB_USERS.queryingKeyboard())){
                    userEntity.setCertification("");
//                    userEntity.setPlugins("false");
//                    userEntity.setLegitimacy("false");
//
//                    Db_PUB_USERS.UpdateCertification(userEntity);
//                    Db_PUB_USERS.UpdatePlugins(userEntity);
//                    Db_PUB_USERS.UpdateLegitimacy(userEntity);
//                }
//                else {
                    userEntity.setCertification("false");
                    userEntity.setKeyboard("false");

//                }
                userEntity.setIslogin("false");
                Db_PUB_USERS.UpdateIslogin(userEntity);
                Db_PUB_USERS.UpdateCertification(userEntity);
                Db_PUB_USERS.UpdateKeyboard(userEntity);
                break;
        }
    }
}


