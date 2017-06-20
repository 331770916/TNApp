package com.tpyzq.mobile.pangu.activity.myself.handhall;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ColorUtils;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.HtmlUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;


/**
 * Created by wangqi on 2016/9/2.
 * 修改个人资料
 */
public class PersonalDataActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "PersonalData";
    private TextView mCompile, ContactAddressTV, ZipCodeTV, ProfessionTV, Profession, EducationBackgroundTV,
            EducationBackground, TheSecondNameTV, PhoneTV, RelationTV, Relation, mIdAddress, mIDNumber, mName,
            mIssuingAuthority,validity;
    private EditText ContactAddress, ZipCode, TheSecondName, Phone;
    int mState = 0;
    String mState1;
    private int REQUSET_1 = 5;
    private int REQUSET_2 = 4;
    private int REQUSET_3 = 3;
    int points = -1;
    private String professionCode,educationCode,relationCode ;

    /**
     * ID
     */
    @Override
    public void initView() {
        findViewById(R.id.Personaldata_back).setOnClickListener(this);
        mCompile = (TextView) findViewById(R.id.Compile);
        mName = (TextView) findViewById(R.id.Name);
        mIdAddress = (TextView) findViewById(R.id.IdAddress);
        mIDNumber = (TextView) findViewById(R.id.IDNumber);
        mIssuingAuthority = (TextView) findViewById(R.id.IssuingAuthority);
        validity = (TextView) findViewById(R.id.validity);

        ContactAddressTV = (TextView) findViewById(R.id.ContactAddressTV);
        ZipCodeTV = (TextView) findViewById(R.id.ZipCodeTV);
        ProfessionTV = (TextView) findViewById(R.id.ProfessionTV);
        EducationBackgroundTV = (TextView) findViewById(R.id.EducationBackgroundTV);
        TheSecondNameTV = (TextView) findViewById(R.id.TheSecondNameTV);
        PhoneTV = (TextView) findViewById(R.id.PhoneTV);
        RelationTV = (TextView) findViewById(R.id.RelationTV);

        ContactAddress = (EditText) findViewById(R.id.ContactAddress);
        ZipCode = (EditText) findViewById(R.id.ZipCode);
        Profession = (TextView) findViewById(R.id.Profession);
        EducationBackground = (TextView) findViewById(R.id.EducationBackground);
        TheSecondName = (EditText) findViewById(R.id.TheSecondName);
        Phone = (EditText) findViewById(R.id.Phone);
        Relation = (TextView) findViewById(R.id.Relation);

        initClick();
        initEditTextClick();
        toConnect();
        setColor();

    }

    private void setColor() {
        mName.setTextColor(ColorUtils.BLACK);
        mIdAddress.setTextColor(ColorUtils.BLACK);
        mIDNumber.setTextColor(ColorUtils.BLACK);
        mIssuingAuthority.setTextColor(ColorUtils.BLACK);
        ContactAddress.setTextColor(ColorUtils.BLACK);
        ZipCode.setTextColor(ColorUtils.BLACK);
        Profession.setTextColor(ColorUtils.BLACK);
        EducationBackground.setTextColor(ColorUtils.BLACK);
        TheSecondName.setTextColor(ColorUtils.BLACK);
        Phone.setTextColor(ColorUtils.BLACK);
        Relation.setTextColor(ColorUtils.BLACK);
        validity.setTextColor(ColorUtils.BLACK);
    }

    private void setGreyColor() {
        mName.setTextColor(ColorUtils.TEXTGRAY);
        mIdAddress.setTextColor(ColorUtils.TEXTGRAY);
        mIDNumber.setTextColor(ColorUtils.TEXTGRAY);
        mIssuingAuthority.setTextColor(ColorUtils.TEXTGRAY);
        ContactAddress.setTextColor(ColorUtils.BLACK);
        ZipCode.setTextColor(ColorUtils.BLACK);
        Profession.setTextColor(ColorUtils.BLACK);
        EducationBackground.setTextColor(ColorUtils.BLACK);
        TheSecondName.setTextColor(ColorUtils.BLACK);
        Phone.setTextColor(ColorUtils.BLACK);
        Relation.setTextColor(ColorUtils.BLACK);
        validity.setTextColor(ColorUtils.TEXTGRAY);
    }

    /**
     * 查询客户资料 网络请求
     */
    private void toConnect() {
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();

        map.put("funcid", "700010");  //700010  300040
        map.put("token", mSession);
        map.put("parms", map1);

        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(PersonalDataActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                LogUtil.e("身份证",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    JSONArray data = jsonObject.getJSONArray("data");
                    if ("0".equals(code)) {
                        if (data != null && data.length() > 0) {
                            JSONObject json = data.getJSONObject(0);
                            /**
                            for (int i = 0; i < data.length(); i++) {
                                _bean = new PersonalDataBean.PersonalBean();
                                _bean.setCLIENT_NAME(data.getJSONObject(i).getString("CLIENT_NAME"));
                                _bean.setID_NO(data.getJSONObject(i).getString("ID_NO"));
                                _bean.setADDRESS(data.getJSONObject(i).getString("ADDRESS"));
                                _bean.setISSUED_DEPART(data.getJSONObject(i).getString("ISSUED_DEPART"));
                                _bean.setID_ADDRESS(data.getJSONObject(i).getString("ID_ADDRESS"));
                                _bean.setZIP_CODE(data.getJSONObject(i).getString("ZIP_CODE"));
                                _bean.setPROFESSION_CODE(data.getJSONObject(i).getString("PROFESSION_CODE"));
                                _bean.setDEGREE_CODE(data.getJSONObject(i).getString("DEGREE_CODE"));
                                _bean.setSECOND_NAME(data.getJSONObject(i).getString("SECOND_NAME"));
                                _bean.setSECOND_MOBILE(data.getJSONObject(i).getString("SECOND_MOBILE"));
                                _bean.setRELATIONSHIP(data.getJSONObject(i).getString("RELATIONSHIP"));
                                _bean.setBEGIN_DATE(Helper.getMyDateY_M_D(data.getJSONObject(i).getString("BEGIN_DATE")));
                                _bean.setEND_DATE(Helper.getMyDateY_M_D(data.getJSONObject(i).getString("END_DATE")));

                            }
                             */
                            mName.setText(json.optString("CLIENT_NAME"));                  //姓名
                            mIDNumber.setText(json.optString("ID_NO"));                    //身份证号
                            mIssuingAuthority.setText(json.optString("ISSUED_DEPART"));    //证件签发机关
                            mIdAddress.setText(json.optString("ID_ADDRESS"));              //证件地址
                            ContactAddress.setText(HtmlUtil.delHTMLTag(json.optString("ADDRESS")));             //联系地址
                            ZipCode.setText(json.optString("ZIP_CODE"));                   //邮编
                            validity.setText(Helper.getMyDateY_M_D(json.optString("BEGIN_DATE"))+"至"+Helper.getMyDateY_M_D(json.optString("END_DATE")));    //有效期

                            ZipCode.setSelection(json.optString("ZIP_CODE").length());
                            ContactAddress.setSelection(HtmlUtil.delHTMLTag(json.optString("ADDRESS")).length());
                            professionCode = json.optString("PROFESSION_CODE");
                            switch (professionCode) {
                                case "01":
                                    Profession.setText("文教科卫专业人员");
                                    break;
                                case "02":
                                    Profession.setText("党政 ( 在职，离退休 ) 机关干部");
                                    break;
                                case "03":
                                    Profession.setText("企事业单位干部");
                                    break;
                                case "04":
                                    Profession.setText("行政企事业单位工人");
                                    break;
                                case "05":
                                    Profession.setText("农民");
                                    break;
                                case "06":
                                    Profession.setText("个体");
                                    break;
                                case "07":
                                    Profession.setText("无业");
                                    break;
                                case "08":
                                    Profession.setText("军人");
                                    break;
                                case "09":
                                    Profession.setText("学生");
                                    break;
                                case "10":
                                    Profession.setText("证券从业人员");
                                    break;
                                case "11":
                                    Profession.setText("离退休");
                                    break;
                                case "99":
                                    Profession.setText("其他");
                                    break;
                            }
                            educationCode = json.optString("DEGREE_CODE");
                            switch (educationCode) {                       //学历
                                case "1":
                                    EducationBackground.setText("博士");
                                    break;
                                case "2":
                                    EducationBackground.setText("硕士");
                                    break;
                                case "3":
                                    EducationBackground.setText("学士");
                                    break;
                                case "4":
                                    EducationBackground.setText("大专");
                                    break;
                                case "5":
                                    EducationBackground.setText("中专");
                                    break;
                                case "6":
                                    EducationBackground.setText("高中");
                                    break;
                                case "7":
                                    EducationBackground.setText("初中及其以下");
                                    break;
                                case "8":
                                    EducationBackground.setText("其他");
                                    break;
                            }
                            TheSecondName.setText(json.optString("SECOND_NAME"));    //第二联系人姓名
                            TheSecondName.setSelection(HtmlUtil.delHTMLTag(json.optString("SECOND_NAME")).length());
                            Phone.setText(json.optString("SECOND_MOBILE"));          //第二联系人电话
                            Phone.setSelection(HtmlUtil.delHTMLTag(json.optString("SECOND_MOBILE")).length());
                            relationCode = json.optString("RELATIONSHIP");
                            switch (relationCode) {                //关系
                                case "1":
                                    Relation.setText("父母");
                                    break;
                                case "2":
                                    Relation.setText("夫妻");
                                    break;
                                case "3":
                                    Relation.setText("子女");
                                    break;
                                case "4":
                                    Relation.setText("朋友");
                                    break;
                                case "5":
                                    Relation.setText("其它");
                                    break;
                            }
                        }
                    } else if ("-6".equals(code)) {
                        Intent intent = new Intent(PersonalDataActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    } else {
                        MistakeDialog.showDialog(msg, PersonalDataActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 初始化点击事件
     */
    private void initClick() {
        mCompile.setOnClickListener(this);

    }

    /**
     * 初始化布局
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_personaldata;
    }


    /**
     * 所有 可以点事件的
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Drawable bianji = getResources().getDrawable(R.mipmap.bianji);
        Drawable bianji1 = getResources().getDrawable(R.mipmap.fanhui);
        bianji.setBounds(0, 0, bianji.getMinimumWidth(), bianji.getMinimumHeight());  //此为必须写的
        bianji1.setBounds(0, 0, bianji.getMinimumWidth(), bianji.getMinimumHeight());  //此为必须写的
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.Personaldata_back:
                finish();
                break;
            case R.id.Compile:
                if (mState == 0) {
                    mCompile.setText("保存");
                    Phone.setMaxLines(11);
                    setCompoundDrawables(bianji, bianji1);
                    setGreyColor();
                    setEditTextClick();
                    mState = 1;
                } else if (mState == 1) {
                    if (TextUtils.isEmpty(ContactAddress.getText().toString().trim())) {
                        Helper.getInstance().showToast(this, "请填写您联系地址");
                    } else if (!Helper.checkPost(ZipCode.getText().toString().toString())) {
                        Helper.getInstance().showToast(this, "邮政编码错误");
                    } else if (TextUtils.isEmpty(EducationBackground.getText().toString().toString())) {
                        Helper.getInstance().showToast(this, "学历不能为空");
                    } else if (!Helper.isMobileNO(Phone.getText().toString())) {
                        Helper.getInstance().showToast(this, "请输入正确手机号");
                    } else if (TextUtils.isEmpty(TheSecondName.getText().toString().trim())) {
                        Helper.getInstance().showToast(this, "请填写联系人");
                    } else if (TextUtils.isEmpty(Relation.getText().toString().trim())) {
                        Helper.getInstance().showToast(this, "请填写您跟第二联系人关系");
                    } else {
                        setDatatoConnect();
                    }
                }
                break;
            case R.id.Profession:
                intent.setClass(this, ProfessionListActivity.class);
                intent.putExtra("point", points);
                intent.putExtra("Profession", professionCode);
                startActivityForResult(intent, REQUSET_1);
                break;
            case R.id.EducationBackground:
                intent.setClass(this, EducationListActivity.class);
                intent.putExtra("EducationListView", educationCode);
                intent.putExtra("point", points);
                startActivityForResult(intent, REQUSET_2);
                break;
            case R.id.Relation:
                intent.setClass(this, RelationListActivity.class);
                intent.putExtra("RelationListView", relationCode);
                intent.putExtra("point", points);
                startActivityForResult(intent, REQUSET_3);
                break;
        }
    }


    /**
     * 保存上传网络请求
     */
    private void setDatatoConnect() {
//        String mProfession = null;
//        String mEducationBackground = null;
//        String mRelation = null;
//        switch (Profession.getText().toString().trim()) {
//            case "文教科卫专业人员":
//                mProfession = "01";
//                break;
//            case "党政 ( 在职，离退休 ) 机关干部":
//                mProfession = "02";
//                break;
//            case "企事业单位干部":
//                mProfession = "03";
//                break;
//            case "行政企事业单位工人":
//                mProfession = "04";
//                break;
//            case "农民":
//                mProfession = "05";
//                break;
//            case "个体":
//                mProfession = "06";
//                break;
//            case "无业":
//                mProfession = "07";
//                break;
//            case "军人":
//                mProfession = "08";
//                break;
//            case "学生":
//                mProfession = "09";
//                break;
//            case "证券从业人员":
//                mProfession = "10";
//                break;
//            case "离退休":
//                mProfession = "11";
//                break;
//            case "其他":
//                mProfession = "99";
//                break;
//        }
//        switch (EducationBackground.getText().toString().trim()) {
//            case "博士":
//                mEducationBackground = "1";
//                break;
//            case "硕士":
//                mEducationBackground = "2";
//                break;
//            case "学士":
//                mEducationBackground = "3";
//                break;
//            case "大专":
//                mEducationBackground = "4";
//                break;
//            case "中专":
//                mEducationBackground = "5";
//                break;
//            case "高中":
//                mEducationBackground = "6";
//                break;
//            case "初中及其以下":
//                mEducationBackground = "7";
//                break;
//            case "其他":
//                mEducationBackground = "8";
//                break;
//        }

//        switch (Relation.getText().toString().trim()) {
//            case "父母":
//                mRelation = "1";
//                break;
//            case "夫妻":
//                mRelation = "2";
//                break;
//            case "子女":
//                mRelation = "3";
//                break;
//            case "朋友":
//                mRelation = "4";
//                break;
//            case "其它":
//                mRelation = "5";
//                break;
//        }
        //联系地址
        String ContactAddress_str = ContactAddress.getText().toString().trim();
        if (!TextUtils.isEmpty(ContactAddress_str)) {
            ContactAddress_str = HtmlUtil.delHTMLTag(ContactAddress_str);
        }

        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();

        map.put("funcid", "700012");  //700010  300040
        map.put("token", mSession);
        map.put("parms", map1);

        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        map1.put("FLAG", "true");
        map1.put("ADDRESS", ContactAddress_str);
        map1.put("PROFESSION_CODE", professionCode);
        map1.put("DEGREE_CODE", educationCode);
        map1.put("ZIP_CODE", ZipCode.getText().toString().trim());
        map1.put("SECOND_NAME", TheSecondName.getText().toString().trim());
        map1.put("SECOND_MOBILE", Phone.getText().toString().trim());
        map1.put("RELATIONSHIP", relationCode);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(PersonalDataActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("0".equals(code)){
                        ResultDialog.getInstance().show("" + "编辑成功", R.mipmap.lc_success);
                        mCompile.setText("编辑");
                        setCompoundDrawables();
                        setColor();
                        initEditTextClick();
                        mState = 0;
                    }else if("-6".equals(code)){
                        Intent intent = new Intent(PersonalDataActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    }else{
                        MistakeDialog.showDialog(res.optString("msg"), PersonalDataActivity.this);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    MistakeDialog.showDialog(e.toString(), PersonalDataActivity.this);
                }
                /**
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<PersonalDataBean>() {
                }.getType();
                PersonalDataBean bean = gson.fromJson(response, type);
                if (bean.getCode().equals("0")) {
                    ResultDialog.getInstance().show("" + "编辑成功", R.mipmap.lc_success);
                } else if (bean.getCode().equals("-6")) {
                    Intent intent = new Intent(PersonalData.this, TransactionLoginActivity.class);
                    startActivity(intent);
                } else {
                    MistakeDialog.showDialog(bean.getMsg(), PersonalData.this);
                }
                */
            }
        });
    }

    /**
     * 编辑状态 可以修改的 图片添加
     *
     * @param Image
     */
    private void setCompoundDrawables(Drawable Image, Drawable Image1) {
        ContactAddressTV.setCompoundDrawablePadding(10);
        ZipCodeTV.setCompoundDrawablePadding(10);
        ProfessionTV.setCompoundDrawablePadding(10);
        EducationBackgroundTV.setCompoundDrawablePadding(10);
        TheSecondNameTV.setCompoundDrawablePadding(10);
        PhoneTV.setCompoundDrawablePadding(10);
        RelationTV.setCompoundDrawablePadding(10);

        ContactAddressTV.setCompoundDrawables(null, null, Image, null);
        ZipCodeTV.setCompoundDrawables(null, null, Image, null);
        ProfessionTV.setCompoundDrawables(null, null, Image, null);
        EducationBackgroundTV.setCompoundDrawables(null, null, Image, null);
        TheSecondNameTV.setCompoundDrawables(null, null, Image, null);
        PhoneTV.setCompoundDrawables(null, null, Image, null);
        RelationTV.setCompoundDrawables(null, null, Image, null);

        Profession.setCompoundDrawablePadding(5);
        EducationBackground.setCompoundDrawablePadding(5);
        Relation.setCompoundDrawablePadding(5);

        Profession.setCompoundDrawables(null, null, Image1, null);
        EducationBackground.setCompoundDrawables(null, null, Image1, null);
        Relation.setCompoundDrawables(null, null, Image1, null);

    }

    /**
     * 保存状态  可修改图片 去除
     */
    private void setCompoundDrawables() {
        ContactAddressTV.setCompoundDrawables(null, null, null, null);
        ZipCodeTV.setCompoundDrawables(null, null, null, null);
        ProfessionTV.setCompoundDrawables(null, null, null, null);
        EducationBackgroundTV.setCompoundDrawables(null, null, null, null);
        TheSecondNameTV.setCompoundDrawables(null, null, null, null);
        PhoneTV.setCompoundDrawables(null, null, null, null);
        RelationTV.setCompoundDrawables(null, null, null, null);


        Profession.setCompoundDrawables(null, null, null, null);
        EducationBackground.setCompoundDrawables(null, null, null, null);
        Relation.setCompoundDrawables(null, null, null, null);

    }

    /**
     * 保存状态 不可点击状态
     */
    private void initEditTextClick() {
        ContactAddress.setEnabled(false);
        ZipCode.setEnabled(false);
        TheSecondName.setEnabled(false);
        Phone.setEnabled(false);

        Profession.setClickable(false);
        EducationBackground.setClickable(false);
        Relation.setClickable(false);

    }

    /**
     * 编辑 可以点击状态
     */
    private void setEditTextClick() {
        ContactAddress.setEnabled(true);
        ZipCode.setEnabled(true);
        TheSecondName.setEnabled(true);
        Phone.setEnabled(true);

        Profession.setOnClickListener(this);
        EducationBackground.setOnClickListener(this);
        Relation.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mState1 = getIntent().getStringExtra("keyint");
        Drawable bianji = getResources().getDrawable(R.mipmap.bianji);
        Drawable bianji1 = getResources().getDrawable(R.mipmap.fanhui);
        bianji.setBounds(0, 0, bianji.getMinimumWidth(), bianji.getMinimumHeight());  //此为必须写的
        bianji1.setBounds(0, 0, bianji.getMinimumWidth(), bianji.getMinimumHeight());  //此为必须写的
        if ("1".equals(mState1)) {
            mCompile.setText("保存");
            setCompoundDrawables(bianji, bianji1);
            setEditTextClick();
            mState = 0;
        }
    }

    /**
     * 接收  跳转之后返回的数据
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUSET_1 && resultCode == RESULT_OK) {
            points = intent.getIntExtra("point", -1);
            Profession.setText(intent.getStringExtra("keyName"));
            professionCode = intent.getStringExtra("keyCode");
        } else if (requestCode == REQUSET_2 && resultCode == RESULT_OK) {
            points = intent.getIntExtra("point", -1);
            EducationBackground.setText(intent.getStringExtra("keyName"));
            educationCode = intent.getStringExtra("keyCode");
        } else if (requestCode == REQUSET_3 && resultCode == RESULT_OK) {
            points = intent.getIntExtra("point", -1);
            Relation.setText(intent.getStringExtra("keyName"));
            relationCode = intent.getStringExtra("keyCode");
        }
    }


}
