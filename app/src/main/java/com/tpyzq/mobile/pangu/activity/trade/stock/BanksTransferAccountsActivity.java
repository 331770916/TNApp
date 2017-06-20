package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.view.TransferAcountsTab;
import com.tpyzq.mobile.pangu.activity.trade.view.TransferQueryTab;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.interfac.BanksTransferAccountsResultCode;
import com.tpyzq.mobile.pangu.interfac.ITabDataObserver;
import com.tpyzq.mobile.pangu.view.keybody.CustomGlobalLayoutListener;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/4.
 *
 * 多银行转账页面
 */
public class BanksTransferAccountsActivity extends BaseActivity implements View.OnClickListener, BanksTransferAccountsResultCode {

    private ImageView mTabLine;
    private LinearLayout mMainLayout;
    private TextView mHeadTv1;
    private TextView mHeadTv2;
    private TextView mHeadTv3;

    private int mBanks1_3;
    private FrameLayout mContentLayout;
    private TransferAcountsTab mTab1;
    private TransferQueryTab mTab2;
    private TextView mTitleView;
    private ProgressBar mProgressBar;
    private  String tag;
    private boolean isPass = false;
    private boolean isInterception = false;
    private BanksTransferBackListener backListener;

    @Override
    public void initView() {

        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        mMainLayout = (LinearLayout)findViewById(R.id.businessMainLayout);
        mMainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new CustomGlobalLayoutListener(mMainLayout, this));

        findViewById(R.id.banksTbackBtn).setOnClickListener(this);
        findViewById(R.id.banksTtab1).setOnClickListener(this);
        findViewById(R.id.banksTtab2).setOnClickListener(this);
        findViewById(R.id.banksTtab3).setOnClickListener(this);

        mTitleView = (TextView) findViewById(R.id.banks_transfer_title);
        mProgressBar = (ProgressBar) findViewById(R.id.allotquery_progress);

        mContentLayout = (FrameLayout) findViewById(R.id.contentFramLayout);

        mHeadTv1 = (TextView) findViewById(R.id.banksTtab1);
        mHeadTv2 = (TextView) findViewById(R.id.banksTtab2);
        mHeadTv3 = (TextView) findViewById(R.id.banksTtab3);

        mTabLine = (ImageView) findViewById(R.id.banksTtabLine);
        intTabLine();
    }

    public void loadingProgress() {

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }

    public void complitedProgress() {

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isPass) {
            return;
        }

        isPass = true;

        ArrayList<ITabDataObserver> datas = new ArrayList<>();
        ArrayList<ITabDataObserver> datas2 = new ArrayList<>();

        mTab1 = new TransferAcountsTab(mMainLayout, this, false, datas, this);
        mTab2 = new TransferQueryTab(mMainLayout, this, false, datas2,this);

        LinearLayout.LayoutParams lp = ( LinearLayout.LayoutParams) mTabLine.getLayoutParams();
        if ("100".equals(tag)) {
            lp.leftMargin = 0;
            mTabLine.setLayoutParams(lp);
            mTitleView.setText("银行转证券");
            mTab1.setMainTab(false);
            mContentLayout.addView(mTab1.getContentView());

        } else if ("200".equals(tag)) {
            lp = ( LinearLayout.LayoutParams) mTabLine.getLayoutParams();
            lp.leftMargin = mBanks1_3;
            mTabLine.setLayoutParams(lp);
            mTitleView.setText("证券转银行");
            mTab1.setMainTab(true);
            mContentLayout.addView(mTab1.getContentView());
        } else if ("300".equals(tag)) {

            lp = ( LinearLayout.LayoutParams) mTabLine.getLayoutParams();
            lp.leftMargin = mBanks1_3 * 2;
            mTabLine.setLayoutParams(lp);
            mTitleView.setText("转账查询");

            mTab2.setIsFromIndexActivity(true);
            mContentLayout.addView(mTab2.getContentView());
        }

        if (mTab1 != null) {
            mTab1.onResume();
        }

        if (mTab2 != null) {
            mTab2.onResume();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mTab1 != null) {
            mTab1.onStop();
        }

        if (mTab2 != null) {
            mTab2.onStop();
        }
    }

    private void intTabLine(){
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mBanks1_3 = outMetrics.widthPixels/3;
        ViewGroup.LayoutParams lp = mTabLine.getLayoutParams();
        lp.width = mBanks1_3;
        mTabLine.setLayoutParams(lp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banksTbackBtn:
                finish();
                break;
            case R.id.banksTtab1:
                LinearLayout.LayoutParams lp = ( LinearLayout.LayoutParams) mTabLine.getLayoutParams();
                lp.leftMargin = 0;
                mTabLine.setLayoutParams(lp);
                mHeadTv1.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                mHeadTv2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                mHeadTv3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                mTabLine.setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));

                mContentLayout.removeAllViews();

                mTitleView.setText("银行转证券");
                mTab1.isMainTab(false);
                mContentLayout.addView(mTab1.getContentView());
                break;
            case R.id.banksTtab2:
                lp = ( LinearLayout.LayoutParams) mTabLine.getLayoutParams();
                lp.leftMargin = mBanks1_3;
                mTabLine.setLayoutParams(lp);
                mHeadTv1.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                mHeadTv2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                mHeadTv3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                mTabLine.setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));

                mContentLayout.removeAllViews();

                mTitleView.setText("证券转银行");
                mTab1.isMainTab(true);
                mContentLayout.addView(mTab1.getContentView());
                break;
            case R.id.banksTtab3:
                lp = ( LinearLayout.LayoutParams) mTabLine.getLayoutParams();
                lp.leftMargin = mBanks1_3 * 2;
                mTabLine.setLayoutParams(lp);
                mHeadTv1.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                mHeadTv2.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                mHeadTv3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));
                mTabLine.setBackgroundColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.white));

                mTitleView.setText("转账查询");
                mContentLayout.removeAllViews();
                mContentLayout.addView(mTab2.getContentView());
                mTab2.setIsFromIndexActivity(false);
                mTab2.setUpdateData();

                break;
        }
    }

    private boolean codeFlag = true;

    @Override
    public void getCode(String code, String tag, boolean backIndexActivity) {

        if (codeFlag) {
            isPass = false;
            codeFlag = false;
            Intent intent = new Intent();
            intent.setClass(BanksTransferAccountsActivity.this, TransactionLoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isInterception()) {
                if (backListener != null) {
                    backListener.onbackForward();
                    return false;
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public BanksTransferBackListener getBackListener() {
        return backListener;
    }

    public void setBackListener(BanksTransferBackListener backListener) {
        this.backListener = backListener;
    }

    public boolean isInterception() {
        return isInterception;
    }

    public void setInterception(boolean isInterception) {
        this.isInterception = isInterception;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == FileUtil.SEEEIOIN_FAILED && resultCode == RESULT_OK) {
//            finish();
//        }
    }

    public interface BanksTransferBackListener {

        void  onbackForward();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bans_transfer_accounts;
    }
}
