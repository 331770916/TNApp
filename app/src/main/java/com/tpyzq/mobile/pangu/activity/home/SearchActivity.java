package com.tpyzq.mobile.pangu.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.keyboardlibrary.KeyboardTouchListener;
import com.android.keyboardlibrary.KeyboardUtil;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.hotsearchstock.HotSearchStockActivity;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.activity.market.QuotaionAdapter;
import com.tpyzq.mobile.pangu.activity.market.selfChoice.SelfChoiceNavigatorAdapter;
import com.tpyzq.mobile.pangu.activity.market.view.HisTorySelfChoiceStockTab;
import com.tpyzq.mobile.pangu.activity.market.view.LimitLookStockTab;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.market.SearchAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.db.StockTable;
import com.tpyzq.mobile.pangu.http.doConnect.detail.GetSearchStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.detail.ToGetSearchStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.AddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.HoldCloudConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToAddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToHoldCloudConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.IListViewScrollObserver;
import com.tpyzq.mobile.pangu.interfac.IListViewScrollSubject;
import com.tpyzq.mobile.pangu.interfac.ListViewScroller;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.magicindicator.FragmentContainerHelper;
import com.tpyzq.mobile.pangu.view.magicindicator.MagicIndicator;
import com.tpyzq.mobile.pangu.view.magicindicator.ViewPagerHelper;
import com.tpyzq.mobile.pangu.view.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/11/30.
 * 新搜索股票页面
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener, ICallbackResult,
        AdapterView.OnItemClickListener, ListViewScroller.ListViewScrollerListener ,
        IListViewScrollSubject {

    private EditText mSearchEdit;
    private ListView mRecyclerView;
    private ArrayList<BaseTabPager> mTabs;
    private HisTorySelfChoiceStockTab mHisTorySelfChoiceStockTab;
    private LimitLookStockTab mLimitLookStockTab;
    private Dialog mLoadingDialog;
    private ArrayList<IListViewScrollObserver> mObservers;
    private KeyboardUtil mKeyBoardUtil;
    private SimpleRemoteControl mSimpleRemoteControl;
    private LinearLayout mItemLayout;
    private LinearLayout mHotHoldLayout;
    private TextView mNoSearchText;
    private ArrayList<StockInfoEntity> mBeans;
    private SearchAdapter mAdapter;
    private boolean doSelectMiddle;         //是否加载list列表
    private int mNetTotalCount;
    private String mStart;
    private static final String TAG = "SearchActivity";

    @Override
    public void initView() {
        mStart = "0";
        mObservers = new ArrayList<>();
        mSimpleRemoteControl = new SimpleRemoteControl(this);
        mSearchEdit = (EditText) findViewById(R.id.special_ed);
        mItemLayout = (LinearLayout) findViewById(R.id.searchItemLayout);
        mHotHoldLayout = (LinearLayout) findViewById(R.id.holdAndhotLayout);
        findViewById(R.id.search_cancle).setOnClickListener(this);
        findViewById(R.id.search2_hot).setOnClickListener(this);
        findViewById(R.id.search2_hold).setOnClickListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.search_ViewPager);
        mRecyclerView = (ListView) findViewById(R.id.search_RecylerView);
        mRecyclerView.setOnItemClickListener(this);
        mAdapter = new SearchAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new ListViewScroller(TAG, mRecyclerView, mStart, this, this));

        mNoSearchText = (TextView) findViewById(R.id.noSearchResultTv);
        mRecyclerView.setVisibility(View.GONE);
        LinearLayout rootLayout = (LinearLayout) findViewById(R.id.searchRootView);

        initMagicIndicator(viewPager);
        initMoveKeyBoard(rootLayout, null);

        mSearchEdit.addTextChangedListener(watcher);

        ArrayList<BaseTabPager> tabs1 = new ArrayList<>();
        QuotaionAdapter quotaionAdapter = new QuotaionAdapter();
        mTabs = new ArrayList<>();

        mLimitLookStockTab = new LimitLookStockTab(this, tabs1);
        mHisTorySelfChoiceStockTab = new HisTorySelfChoiceStockTab(this, tabs1);

        mTabs.add(mLimitLookStockTab);
        mTabs.add(mHisTorySelfChoiceStockTab);
        quotaionAdapter.setDatas(mTabs);

        viewPager.setAdapter(quotaionAdapter);
        viewPager.setCurrentItem(0);

        mLimitLookStockTab.setViewPager(viewPager);
        mHisTorySelfChoiceStockTab.setViewPager(viewPager);

    }

    /**
     * 初始化指示器
     */
    private void initMagicIndicator(ViewPager viewPager) {
        MagicIndicator tabLineDisClickIndicator = (MagicIndicator) findViewById(R.id.search_tabPageIndicator);
        String [] strings = {"最近浏览过的股票", "历史自选股"};
        List<String> titleList = Arrays.asList(strings);

        FragmentContainerHelper fragmentContainerHelper = new FragmentContainerHelper();
        fragmentContainerHelper.handlePageSelected(0, false);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);

        commonNavigator.setAdapter(new SelfChoiceNavigatorAdapter(titleList, viewPager, this));
        tabLineDisClickIndicator.setNavigator(commonNavigator);
        fragmentContainerHelper.attachMagicIndicator(tabLineDisClickIndicator);
        ViewPagerHelper.bind(tabLineDisClickIndicator, viewPager);
    }

    /**
     * 初始化键盘
     * @param rootLayout
     */
    private void initMoveKeyBoard(LinearLayout rootLayout, ScrollView scrollView) {
        mKeyBoardUtil = new KeyboardUtil(this, rootLayout, scrollView);
        mKeyBoardUtil.setOtherEdittext(mSearchEdit);
        // monitor the KeyBarod state
        mKeyBoardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        // monitor the finish or next Key
        mKeyBoardUtil.setInputOverListener(new InputOverListener());
        mSearchEdit.setOnTouchListener(new KeyboardTouchListener(mKeyBoardUtil, KeyboardUtil.INPUTTYPE_NUM_ABC, -1));
    }

    @Override
    public void getResult(Object result, String tag) {
        if ("HoldCloudConnect".equals(tag)) {
            if (null != result && result instanceof String) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                MistakeDialog.showDialog("" + result, SearchActivity.this);
                return;
            }
            ArrayList<StockInfoEntity> stockInfoEntities = (ArrayList<StockInfoEntity>) result;
            if (stockInfoEntities != null && stockInfoEntities.size() > 0) {
                HOLD_SEQ.deleteAll();
                String stockCodes = "";
                String stockNames = "";
                String stockPricees = "";
                StringBuilder sbCode = new StringBuilder();
                StringBuilder sbName = new StringBuilder();
                StringBuilder sbPrice = new StringBuilder();
                boolean holdFlag = HOLD_SEQ.addHoldDatas(stockInfoEntities);
                if (!holdFlag) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    Helper.getInstance().showToast(CustomApplication.getContext(), "导入持仓自选股数据库失败");
                }
                for (int i = 0; i < stockInfoEntities.size(); i++) {
                    stockInfoEntities.get(i).setStock_flag(StockTable.STOCK_OPTIONAL);
                    Db_PUB_STOCKLIST.addOneStockListData(stockInfoEntities.get(i));
                    if (i == stockInfoEntities.size() -1) {
                        sbCode.append(stockInfoEntities.get(i).getStockNumber());
                        sbName.append(stockInfoEntities.get(i).getStockName());
                        sbPrice.append(stockInfoEntities.get(i).getNewPrice());
                    } else {
                        sbCode.append(stockInfoEntities.get(i).getStockNumber()).append(",");
                        sbName.append(stockInfoEntities.get(i).getStockName()).append(",");
                        sbPrice.append(stockInfoEntities.get(i).getNewPrice()).append(",");
                    }
                    SelfStockHelper.sendUpdateSelfChoiceBrodcast(SearchActivity.this, stockInfoEntities.get(i).getStockNumber());
                }
                stockCodes = sbCode.toString();
                stockNames = sbName.toString();
                stockPricees = sbPrice.toString();
                mSimpleRemoteControl.setCommand(new ToAddSelfChoiceStockConnect(new AddSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stockCodes, UserUtil.userId, stockNames, stockPricees)));
                mSimpleRemoteControl.startConnect();
            } else {
                MistakeDialog.showDialog("无持仓股票", SearchActivity.this);
            }
        } else if ("AddSelfChoiceStockConnect".equals(tag)) {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
            String json = String.valueOf(result);
            SelfStockHelper.explanImportHoldResult(SearchActivity.this, json);
        } else {
            if (result instanceof String) {
                String strResult = (String) result;
                if (strResult.contains("-1")) {
                    MistakeDialog.showDialog("网络异常", this);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNoSearchText.setVisibility(View.VISIBLE);
                }

            } else  {
                mBeans = (ArrayList<StockInfoEntity>) result;

                mNoSearchText.setVisibility(View.GONE);
                if (mBeans == null || mBeans.size() <= 0) {
                    return;
                }

                for (StockInfoEntity entity : mBeans) {
                    entity.setStock_flag(StockTable.STOCK_OPTIONAL);
                }

                if (!TextUtils.isEmpty(mBeans.get(0).getTotalCount())) {
                    mNetTotalCount = Integer.parseInt(mBeans.get(0).getTotalCount());
                }
                notifyObservers();
                mAdapter.setDatas(mBeans);
                if (doSelectMiddle) {
                    int index = mRecyclerView.getLastVisiblePosition()- mRecyclerView.getFirstVisiblePosition() + 1;
                    View v = mRecyclerView.getChildAt(0);
                    int top = (v == null) ? 0 : v.getTop();
                    mRecyclerView.setSelectionFromTop(index, top);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            StockInfoEntity sie = mBeans.get(position);
            Intent intent = new Intent();
            StockDetailEntity data = new StockDetailEntity();
            data.setStockName(sie.getStockName());
            data.setStockCode(sie.getStockNumber());

            intent.putExtra("stockIntent", data);

            intent.setClass(this, StockDetailActivity.class);
            startActivity(intent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_cancle:
                finish();
                break;
            case R.id.search2_hold:
                if (!Db_PUB_USERS.isRegister()) {
                    Intent intent = new Intent(this, ShouJiZhuCeActivity.class);
                    startActivity(intent);
                } else if (!Db_PUB_USERS.islogin()) {
                    Intent intent = new Intent();
                    intent.setClass(this, TransactionLoginActivity.class);
                    startActivity(intent);
                } else {
                    //导入持仓

                    mLoadingDialog = LoadingDialog.initDialog(this, "正在导入，请稍后...");
                    mLoadingDialog.show();

                    String session = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
                    mSimpleRemoteControl.setCommand(new ToHoldCloudConnect(new HoldCloudConnect(TAG, session)));
                    mSimpleRemoteControl.startConnect();

                }
                break;
            case R.id.search2_hot:
                Intent intent = new Intent();
                intent.setClass(this, HotSearchStockActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLimitLookStockTab.onResume();
        mHisTorySelfChoiceStockTab.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            if(mKeyBoardUtil.isShow){
                mKeyBoardUtil.hideSystemKeyBoard();
                mKeyBoardUtil.hideAllKeyBoard();
                mKeyBoardUtil.hideKeyboardLayout();
            }else {
                return super.onKeyDown(keyCode, event);
            }

            return false;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search2;
    }

    private class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {
        @Override
        public void KeyBoardStateChange(int state, EditText editText) {

        }
    }

    private class InputOverListener implements KeyboardUtil.InputFinishListener{

        @Override
        public void inputHasOver(int onclickType, EditText editText) {


            if (!TextUtils.isEmpty(mSearchEdit.getText().toString()) && mSearchEdit.getText().length() >= 3) {
                mItemLayout.setVisibility(View.GONE);
                mHotHoldLayout.setVisibility(View.GONE);
                mSimpleRemoteControl.setCommand(new ToGetSearchStockConnect(new GetSearchStockConnect(TAG, mSearchEdit.getText().toString(), mStart)));
                mSimpleRemoteControl.startConnect();
            }


        }
    }


    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            // 等到用户输入完数据后进行网络连接模糊查询相关股票
            if (editable.length() > 0) {

                int Len = 3;
                if(!Helper.isNumeric(editable.toString())){
                    Len = 2;
                }
                if (editable.length() >= Len) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mNoSearchText.setVisibility(View.GONE);
                    mItemLayout.setVisibility(View.GONE);
                    mHotHoldLayout.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSimpleRemoteControl.setCommand(new ToGetSearchStockConnect(new GetSearchStockConnect(TAG, editable.toString(), mStart)));
                            mSimpleRemoteControl.startConnect();
                        }
                    }, 300);
                }

            } else if (editable.length() == 0) {
                //当搜索股票删除搜索数字或简称到0时 更新页面数据
                mNoSearchText.setVisibility(View.GONE);
                mItemLayout.setVisibility(View.VISIBLE);
                mHotHoldLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    };

    //  《 ----关于翻页 -----》


    @Override
    public void registerObserver(IListViewScrollObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void removeObserver(IListViewScrollObserver observer) {
        int num = mObservers.indexOf(observer);
        if (num >= 0) {
            mObservers.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        for (IListViewScrollObserver observer : mObservers) {
            observer.update(mNetTotalCount);
        }
    }

    @Override
    public void nextPage(String startNumber) {
        mStart = startNumber;

        mSimpleRemoteControl.setCommand(new ToGetSearchStockConnect(new GetSearchStockConnect(TAG, mSearchEdit.getText().toString(), mStart)));
        mSimpleRemoteControl.startConnect();
    }

    @Override
    public void lastPage(String startNumber) {
        mStart = startNumber;
        mSimpleRemoteControl.setCommand(new ToGetSearchStockConnect(new GetSearchStockConnect(TAG, mSearchEdit.getText().toString(), mStart)));
        mSimpleRemoteControl.startConnect();
    }

    @Override
    public void connectBySrollStop() {
    }

    @Override
    public void doSelectMiddle(boolean doSelectMiddle) {
        this.doSelectMiddle = doSelectMiddle;
    }

    @Override
    public void juedgeGesture(boolean isUp) {

    }
}
