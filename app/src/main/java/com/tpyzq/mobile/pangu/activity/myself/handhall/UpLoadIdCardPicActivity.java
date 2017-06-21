package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.UpdateIdCodeValidityEntity;

/**
 * Created by zhangwenbo on 2017/6/21.
 * 上传身份证照片
 */

public class UpLoadIdCardPicActivity extends BaseActivity implements View.OnClickListener{

    private String fromWhere;
    private UpdateIdCodeValidityEntity mEntity;
    @Override
    public void initView() {
        Intent intent = getIntent();
        fromWhere = intent.getStringExtra("flag");
        mEntity = intent.getParcelableExtra("entity");

        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("上传身份证");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_uploadidcard_pic;
    }
}
