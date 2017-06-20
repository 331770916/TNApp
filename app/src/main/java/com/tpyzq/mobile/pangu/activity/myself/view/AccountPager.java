package com.tpyzq.mobile.pangu.activity.myself.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.TagCloudView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AccountPowerActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AgreementActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AgreementSignActvity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.ChangePasswordActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.PersonalDataActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskEvaluationActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.StockHolderInfoActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.HotlineActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiVerificationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.myself.TagCloudAdapter;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.ToastUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

/**
 * Created by 陈新宇 on 2016/9/14.
 * 掌厅 页面
 */
public class AccountPager extends BaseMySelfPager implements View.OnClickListener {
    private TagCloudView tag_cloud;
    private TagCloudAdapter tagCloudAdapter;
    private List<String[]> list;
    private TextView tv_open_account/*开户*/, tv_myaccount_rights/*我的账户权限*/, tv_electronic_signature_agreement/*电子签名约定书*/, tv_myself_evaluating/*风险等级评测*/,
            tv_userdata_change/*修改个人资料*/, tv_warn/*退市和风险警示*/, tv_myself_partner/*股东账户查询*/, tv_transactionpass_change/*修改交易密码*/,
            tv_server /*联系客服*/, tv_gem_sign,/*创业板转签*/
            tv_three_bankdeposit,/*三存银行*/
            tv_refresh_iccard/*更新身份证有效期*/, tv_forget_transactionpw/*忘记交易密码*/;
    private ImageView iv_open_account;
    private TextView tv_login;
    private ImageView iv_title;
    private TextView tv_headertitle;
    private boolean flag = true;
    public AccountPager(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        tag_cloud = (TagCloudView) rootView.findViewById(R.id.tag_cloud);
        tv_myaccount_rights = (TextView) rootView.findViewById(R.id.tv_myaccount_rights);
        tv_electronic_signature_agreement = (TextView) rootView.findViewById(R.id.tv_electronic_signature_agreement);
        tv_myself_evaluating = (TextView) rootView.findViewById(R.id.tv_myself_evaluating);
        tv_userdata_change = (TextView) rootView.findViewById(R.id.tv_userdata_change);
        tv_warn = (TextView) rootView.findViewById(R.id.tv_warn);
        tv_open_account = (TextView) rootView.findViewById(R.id.tv_open_account);
        tv_myself_partner = (TextView) rootView.findViewById(R.id.tv_myself_partner);
        tv_transactionpass_change = (TextView) rootView.findViewById(R.id.tv_transactionpass_change);
        tv_server = (TextView) rootView.findViewById(R.id.tv_server);
        tv_gem_sign = (TextView) rootView.findViewById(R.id.tv_gem_sign);
        tv_three_bankdeposit = (TextView) rootView.findViewById(R.id.tv_three_bankdeposit);
        tv_refresh_iccard = (TextView) rootView.findViewById(R.id.tv_refresh_iccard);
        tv_forget_transactionpw = (TextView) rootView.findViewById(R.id.tv_forget_transactionpw);
        iv_title = (ImageView) rootView.findViewById(R.id.iv_title);
        tv_login = (TextView) rootView.findViewById(R.id.tv_login);
        iv_open_account = (ImageView) rootView.findViewById(R.id.iv_open_account);
        tv_headertitle = (TextView) rootView.findViewById(R.id.tv_headertitle);
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        userHeaderListen = new UserHeaderListen() {
            @Override
            public void setTextView(int position) {
                tv_headertitle.setVisibility(View.VISIBLE);
                tv_headertitle.setText(Html.fromHtml(list.get(position)[1]));
            }
        };
        tagCloudAdapter = new TagCloudAdapter(list, userHeaderListen);
        tv_headertitle.setVisibility(View.GONE);
        tag_cloud.setAdapter(tagCloudAdapter);

        iv_title.setOnClickListener(this);
        tv_myaccount_rights.setOnClickListener(this);
        tv_electronic_signature_agreement.setOnClickListener(this);
        tv_myself_evaluating.setOnClickListener(this);
        tv_userdata_change.setOnClickListener(this);
        tv_warn.setOnClickListener(this);
        tv_myself_partner.setOnClickListener(this);
        tv_transactionpass_change.setOnClickListener(this);
        tv_server.setOnClickListener(this);
        tv_myaccount_rights.setOnClickListener(this);
        iv_open_account.setOnClickListener(this);
        tv_open_account.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_gem_sign.setOnClickListener(this);
        tv_three_bankdeposit.setOnClickListener(this);
        tv_refresh_iccard.setOnClickListener(this);
        tv_forget_transactionpw.setOnClickListener(this);
    }


    public void refreshView() {
        if ("true".equals(Db_PUB_USERS.queryingIslogin())) {
            iv_title.setClickable(false);
            tv_login.setClickable(false);
            tv_login.setVisibility(View.INVISIBLE);
            iv_open_account.setVisibility(View.GONE);
            if (flag){
                getTagCloud();
                flag = false;
            }
        } else {
            clearList();
            flag = true;
            tv_login.setVisibility(View.VISIBLE);
            iv_open_account.setVisibility(View.GONE);
            tagCloudAdapter = new TagCloudAdapter(list, userHeaderListen);
            tag_cloud.setAdapter(tagCloudAdapter);
            iv_title.setClickable(true);
            tv_login.setClickable(true);
            iv_title.setImageResource(R.mipmap.login_no);
        }
    }

    public void clearList() {
        if (list != null) {
            list.clear();
        }
    }

    public void getTagCloud() {
        getHeadText1();
        UserUtil.refrushUserInfo();
    }

    private void getHeadText1() {
        HashMap<Object, Object> mapHQTNG104 = new HashMap<>();
        mapHQTNG104.put("FUNCTIONCODE", "HQTNG104");
        mapHQTNG104.put("TOKEN", SpUtils.getString(mContext, "mSession", null));
        HashMap<Object, Object> mapHQTNG104_1 = new HashMap<>();
        mapHQTNG104_1.put("CUST_ID", UserUtil.capitalAccount);
        mapHQTNG104.put("PARAMS", mapHQTNG104_1);
        NetWorkUtil.getInstence().okHttpForPostString("", ConstantUtil.URL_RS, mapHQTNG104, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                tagCloudAdapter = new TagCloudAdapter(list, userHeaderListen);
                tag_cloud.setAdapter(tagCloudAdapter);
                ToastUtils.showShort(mContext, "头像信息获取失败");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String message = object.getString("message");
                    String type = object.getString("type");
                    if ("200".equals(code)) {
                        JSONObject jsonObject = new JSONObject(message);
                        HashMap hashMap = toHashMap(jsonObject);
                        list = new ArrayList<String[]>();
                        String sexid = "";
                        Set set = hashMap.entrySet();
                        Iterator iterator = set.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry mapentry = (Map.Entry) iterator.next();
                            if ("sexid".equals(mapentry.getKey())) {
                                sexid = jsonObject.getString("sexid");
                            } else {
                                JSONArray jsonArray = new JSONArray((String) mapentry.getValue());
                                String[] value = new String[2];
                                value[0] = jsonArray.getString(0);
                                value[1] = jsonArray.getString(1);
                                list.add(value);
                            }
                        }
                        if ("001".equals(sexid)) {
                            iv_title.setImageResource(R.mipmap.login_bull);
                        } else if ("002".equals(sexid)) {
                            iv_title.setImageResource(R.mipmap.login_cow);
                        }
                        tagCloudAdapter = new TagCloudAdapter(list, userHeaderListen);
                        tag_cloud.setAdapter(tagCloudAdapter);
                    } else {
                        ToastUtils.showShort(mContext, type);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Json串转换成map
     *
     * @param object
     * @return
     */
    private static HashMap<String, String> toHashMap(JSONObject object) {
        HashMap<String, String> data = new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = object;
        Iterator it = jsonObject.keys();
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext()) {
            String key = String.valueOf(it.next());
            String value = null;
            try {
                value = jsonObject.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.put(key, value);
        }
        return data;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pager_myself_account;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_server:
                intent.setClass(mContext, HotlineActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.tv_open_account:
            case R.id.iv_open_account:
                intent.putExtra("type", 0);//开户 ，开户传此，
                intent.putExtra("channel", ConstantUtil.OPEN_ACCOUNT_CHANNEL);// 开户id
                intent.setClass(mContext, com.cairh.app.sjkh.MainActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.tv_gem_sign:
//                intent.setClass(mContext,StartyUpBoardActivity.class);
//                mContext.startActivity(intent);
                break;
//            case R.id.tv_three_bankdeposit:
//                intent.setClass(mContext, ChangBank.class);
//                mContext.startActivity(intent);
//                break;
            case R.id.tv_refresh_iccard:

                break;
//            case R.id.tv_forget_transactionpw:
//                intent.setClass(mContext, GetUserIdActivity.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());
//                } else {
//                    mContext.startActivity(intent);
//                }
//                break;
            default:
                clickButton(v, intent);
                break;
        }
    }

    public void clickButton(View v, Intent intent) {
        switch (v.getId()) {
            case R.id.tv_myaccount_rights:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_AccountPowerActivity, intent);
                break;
            case R.id.iv_title:
                if (!Db_PUB_USERS.isRegister()) {
                    mContext.startActivity(new Intent(mContext, ShouJiZhuCeActivity.class));
                } else {
                    if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                        mContext.startActivity(new Intent(mContext, TransactionLoginActivity.class));
                    } else {
                        mContext.startActivity(new Intent(mContext, ShouJiVerificationActivity.class));
                    }
                }
                break;
            case R.id.tv_login:
                if (!Db_PUB_USERS.isRegister()) {
                    intent.setClass(mContext, ShouJiZhuCeActivity.class);
                } else {
                    if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                        mContext.startActivity(new Intent(mContext, TransactionLoginActivity.class));
                    } else {
                        mContext.startActivity(new Intent(mContext, ShouJiVerificationActivity.class));
                    }
                }
                break;
            case R.id.tv_electronic_signature_agreement:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_Agreement, intent);
                break;
            case R.id.tv_myself_evaluating:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_RiskEvaluation, intent);
                break;
            case R.id.tv_userdata_change:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_PersonalData, intent);
                break;
            case R.id.tv_warn:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_AgreementSigned, intent);
                break;
            case R.id.tv_myself_partner:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_Information, intent);
                break;
            case R.id.tv_transactionpass_change:
                gotoPage((Activity) mContext, TransactionLoginActivity.PAGE_INDEX_Password, intent);
                break;
        }
    }


    UserHeaderListen userHeaderListen;

    /**
     * 云标签 回调
     */
    public interface UserHeaderListen {
        void setTextView(int textView);
    }


    private void gotoPage(Activity activity, int a_pageIndex, Intent intent) {
        intent.putExtra("pageindex", a_pageIndex);
        if (!Db_PUB_USERS.isRegister()) {
            intent.setClass(activity, ShouJiZhuCeActivity.class);
        } else {
            if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                if ("true".equals(Db_PUB_USERS.queryingIslogin())) {
                    switch (a_pageIndex) {
                        case TransactionLoginActivity.PAGE_INDEX_AccountPowerActivity:  //我的账户权限
                            intent.setClass(activity, AccountPowerActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_Agreement: //电子签名约定书
                            intent.setClass(activity, AgreementActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_RiskEvaluation: //风险测评
                            intent.setClass(activity, RiskEvaluationActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_PersonalData: //修改个人资料
                            intent.setClass(activity, PersonalDataActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_Information://股东资料
                            intent.setClass(activity, StockHolderInfoActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_AgreementSigned://退市和风险警示协议签署
                            intent.setClass(activity, AgreementSignActvity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_Password://修改密码
                            intent.setClass(activity, ChangePasswordActivity.class);
                            break;
                    }
                } else {
                    intent.setClass(activity, TransactionLoginActivity.class);
                }
            } else {
                intent.setClass(activity, ShouJiVerificationActivity.class);
            }
        }
        activity.startActivity(intent);
    }
}
