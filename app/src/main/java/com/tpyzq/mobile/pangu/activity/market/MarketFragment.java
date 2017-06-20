package com.tpyzq.mobile.pangu.activity.market;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.SearchActivity;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeObsever;
import com.tpyzq.mobile.pangu.activity.market.quotation.QuotaionFragment;
import com.tpyzq.mobile.pangu.activity.market.selfChoice.SelfChoiceFragment;
import com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock.EditorSelfChoiceStockActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiVerificationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.MySubscribeActivity;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.ScreenShot;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.ShareDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhangwenbo on 2016/6/16.
 * guohuiz 重构 2017/5/12
 * 行情模块
 */
public class MarketFragment extends BaseFragment
        implements View.OnClickListener,HomeObsever,RadioGroup.OnCheckedChangeListener, SelfChoiceFragment.SelfChoiceCallback, SelfChoiceFragment.ShareTypeListener
{
    private final String TAG = "MarketFragment";
    private Fragment mCurrentFragment = null;
    private QuotaionFragment mQuotaionFragment;
    private SelfChoiceFragment mSelfChoiceFragment;
    public static int pageIndex = 0;
    private TextView mEditSelfiv;
    private ProgressBar mProgressBar;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton,mQuotionRadioBtn;
    private ArrayList<StockInfoEntity> mBeans;
    private static final int REQUESTCODE = 1000;
    public static final String MARKET_TAG = "isStartConnect";
    private int mType = 1;
    private View market_share;
    private FrameLayout market_contentLayout;

//    @Override
    public void initView(View view) {
        HomeFragmentHelper.mOberservers.add(this);
        market_contentLayout = (FrameLayout) view.findViewById(R.id.market_contentLayout);
        SpUtils.putBoolean(CustomApplication.getContext(), MARKET_TAG, false);
        view.findViewById(R.id.market_search).setOnClickListener(this);
        market_share = view.findViewById(R.id.market_share);
        market_share.setOnClickListener(this);
        mEditSelfiv = (TextView) view.findViewById(R.id.bianji);
        mEditSelfiv.setOnClickListener(this);
        mEditSelfiv.setText("编辑");
        mEditSelfiv.setVisibility(View.VISIBLE);

        mProgressBar = (ProgressBar) view.findViewById(R.id.market_progress);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.mark_radioGroup);
        mRadioButton = (RadioButton) view.findViewById(R.id.market_RadioBtn);
        mQuotionRadioBtn = (RadioButton) view.findViewById(R.id.market_selfRadioBtn);
        mRadioGroup.setOnCheckedChangeListener(this);
        pageIndex = 0;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        mSelfChoiceFragment = new SelfChoiceFragment();
        mQuotaionFragment = new QuotaionFragment();
        mSelfChoiceFragment.setShareTypeListener(this);
        if(ConstantUtil.jumpHangqing){
            mCurrentFragment = mQuotaionFragment;
            mType = 2;
            transaction.add(R.id.market_contentLayout, mQuotaionFragment);
            transaction.add(R.id.market_contentLayout, mSelfChoiceFragment).hide(mSelfChoiceFragment);
            mQuotionRadioBtn.setChecked(true);
        }else{
            mCurrentFragment = mSelfChoiceFragment;
            mType = 1;
            transaction.add(R.id.market_contentLayout, mSelfChoiceFragment);
            transaction.add(R.id.market_contentLayout, mQuotaionFragment).hide(mQuotaionFragment);
            mRadioButton.setChecked(true);
        }
        transaction.commit();
        mSelfChoiceFragment.setSelfChoiceCallback(this);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        SpUtils.putBoolean(CustomApplication.getContext(), MARKET_TAG, hidden);
        if (!hidden) {
            mSelfChoiceFragment.onResume();
            mQuotaionFragment.onResume();
        } else {
            mSelfChoiceFragment.onStop();
            mQuotaionFragment.onStop();
        }
    }
//
    public void loading() {

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }
//
    public void complited() {

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }

    }
    private String mTag;
    public void editSelfivVisibile(String tag) {
        mTag = tag;
        if (mEditSelfiv != null) {
            mEditSelfiv.setVisibility(View.VISIBLE);
        }
    }

    public void editSelfivGone() {
        if (mEditSelfiv != null) {
            mEditSelfiv.setVisibility(View.GONE);
        }
    }
//
//    /**
//     * 切换页面的重载，优化了fragment的切换
//     * @param to
//     */
    private void switchContent(Fragment to) {
        try {
            if (mCurrentFragment != to) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.hide(mCurrentFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
                mCurrentFragment = to;
            }
        } catch (Exception e) {
            e.printStackTrace();
            storeErr(e);
        }
    }
//
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        LogHelper.e(TAG,"onCheckedChanged:"+checkedId);
        switch (checkedId) {
            case R.id.market_selfRadioBtn:
                BRutil.menuSelect("B001");
                mType = 1;
                switchContent(mQuotaionFragment);
                mEditSelfiv.setText("中签查询");
                mEditSelfiv.setVisibility(View.GONE);
                market_share.setVisibility(View.GONE);
                if ("TabNewStock".equals(mTag)) {
                    mEditSelfiv.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.market_RadioBtn:
                BRutil.menuSelect("A001");
                mType = 2;
                switchContent(mSelfChoiceFragment);
                mEditSelfiv.setText("编辑");
                mEditSelfiv.setVisibility(View.VISIBLE);
                market_share.setVisibility(View.VISIBLE);
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.market_search:
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.bianji:
                intent = new Intent();
                if ("编辑".equals(mEditSelfiv.getText())) {
                    intent.putParcelableArrayListExtra("beans", mBeans);
                    intent.setClass(getActivity(), EditorSelfChoiceStockActivity.class);
                    startActivityForResult(intent, REQUESTCODE);
                } else {
                    intent.putExtra("pageindex", TransactionLoginActivity.PAGE_INDEX_ManagerMySubscribeActivity);
                    if (!Db_PUB_USERS.isRegister()) {
                        intent.setClass(getActivity(), ShouJiZhuCeActivity.class);
                    } else {
                        if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                            if ("true".equals(Db_PUB_USERS.queryingIslogin())) {
                                intent.setClass(getActivity(), MySubscribeActivity.class);
                            } else {
                                intent.setClass(getActivity(), TransactionLoginActivity.class);
                            }
                        } else {
                            intent.setClass(getActivity(), ShouJiVerificationActivity.class);
                        }
                    }
                    startActivity(intent);
                }


                break;
            case R.id.market_share:
//                loadingDialog = LoadingDialog.initDialog(this.getActivity(), "加载中…");
//                loadingDialog.show();       //显示菊花
                getShare();                  //弹出分享的界面
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUESTCODE && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra("finish");
            if ("finish".equals(result)) {
                mBeans = null;
            }
        }
    }




    /**
     * 截屏 获取图片
     * type 1自选股 4行情 2资讯
     */
    private void getShare() {
        String capitalAccount = UserUtil.capitalAccount;                        //注册账户
        String base64 = ScreenShot.shoot((Activity) this.getContext());         //截屏 拿到图片的 base64
        HashMap map1=new HashMap();
        HashMap map2=new HashMap();
        map2.put("base64",base64);
        map2.put("account",capitalAccount);
        map2.put("type","4");
        map2.put("phone_type","2");
        map1.put("FUNCTIONCODE","HQFNG001");
        map1.put("PARAMS",map2);
        NetWorkUtil.getInstence().okHttpForPostString("MarketFragment", ConstantUtil.URL_FX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if("0".equals(code)){
                        String url = jsonObject.getString("msg");
                        ShareDialog dialog = new ShareDialog(getActivity());
                        dialog.setUrl(url);
                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    public void callbackSelfchoiceDatas(ArrayList<StockInfoEntity> beans) {

    }

    @Override
    public void getShareType(String type) {

    }

    @Override
    public void onResume() {
        super.onResume();
//        LogHelper.e(TAG,"**********onResume");
    }

    @Override
    public void update(int position, String tag) {
        if ("selfChoice".equals(tag) || "selfChoiceNews".equals(tag)) {
            mRadioButton.setChecked(true);
        } else if ("hangqing".equals(tag)) {
            mQuotionRadioBtn.setChecked(true);
        }
    }
}
