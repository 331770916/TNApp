package com.tpyzq.yybkh_baidu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class YYBkhMainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_test_main);
    Intent intent = new Intent();
    intent.putExtra("type", 0);//开户 ，开户传此，
//    intent.putExtra("channel", "bzbj");// 开户时可以传此参数
//    intent.putExtra("mobileNo", "18695702158");// 开户时可以传此参数，有就传 没有不传，传了开户系统将不再校验手机号
//    intent.putExtra("lcs_uid", "1111999990");
//    intent.putExtra("validateCode", "2222");
    intent.setClass(YYBkhMainActivity.this, com.cairh.app.sjkh.MainActivity.class);
    startActivity(intent);
    finish();
  }

}
