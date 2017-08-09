package com.tpyzq.mobile.pangu.activity.market.selfChoice.childQuotation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.market.BaseTabPager;
import com.tpyzq.mobile.pangu.activity.market.MarketFragment;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.StockTable;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.pulllayou.PullLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

import static com.tpyzq.mobile.pangu.util.ConstantUtil.MARKET_TAG;


/**
 * Created by zhangwenbo on 2016/8/22.
 * 自选股行情界面
 */
public class SelfChoiceQuotationTab extends BaseTabPager implements View.OnClickListener,
        SelfChoiceQuotationAdapter.OnSelfClickListener, AdapterView.OnItemClickListener,
         PriceHeadTvChangeState.HeadStateChangeListener,
        PullLayout.OnPullCallBackListener {
    private Activity mActivity;
    private View mRelativeLayout;
    private SelfChoiceQuotationAdapter mAdapter;
    private ArrayList<StockInfoEntity> mStockData;
    private TextView mHeadTv2;
    private TextView mHeadTv3;
    private TextView mWinProTv;
    private TextView mZdProTv;
    private TextView mTotalTv;
    private Handler handler;
//    private RoundProgressBar mRightProgress;
//    private RoundProgressBar mLeftProgress;

    private int mHeadFlag1State;   // 状态0 默认， 状态1 箭头向下；状态2 箭头向上
    private int mHeadFlag2State;    // 状态0 默认， 状态1 箭头向下；状态2 箭头向上

    private String mCodes = "";
    private String mOrder = "0";
    private String mHolsJson = "";
    private PriceHeadTvChangeState mPriceHeadTvChangeState1;
    private PriceHeadTvChangeState mPriceHeadTvChangeState2;
    private PullLayout mPullDownScrollView;
    private boolean doClickHeadTv = true; //在未做任何操作的情况下 第一次点击 涨跌值 和涨跌幅切换
    private boolean mJuedgeOption = true; //判断item中的值是涨跌幅还是涨跌值   默认是涨跌幅 为true
//    public static ArrayList<SelfChoicTimeObserver> mObserves = new ArrayList<>();
    private SelfChoiceDataCallback mSelfChoiceDataCallback;

    private static final String TAG = "SelfChoiceQuotationTab";

    private static final int HEADSTATE_NO = 1000;       //箭头的在头部是否显示的状态  1000表示没有显示箭头
    private static final int HEADSTATE_PRICE = 2000;    //箭头的在头部是否显示的状态  2000 表示箭头在head2显示
    private static final int HEADSTATE_ZD = 3000;       //箭头的在头部是否显示的状态  3000箭头表示在head3显示
    private int headIcPositionState = HEADSTATE_NO;     //箭头的在头部是否显示的状态  1000表示没有显示箭头， 2000 表示箭头在head2显示， 3000箭头表示在head3显示

    private static final String NOARRAY = "4";
    private static final String NEWPRICEARRAY = "1";
    private static final String ZDZARRAY = "2";
    private static final String ZDFARRAY = "3";
    private String mHesdType = NOARRAY; //排序类型：1.根据最新价  2.根据涨跌  3.根据涨跌幅 4.不排序
    private Dialog mLoadingDialog;
//    private boolean isFirstInto = true;
    private StockInfoEntity hushen300Entity = null;

    public SelfChoiceQuotationTab(Activity activity, ArrayList<BaseTabPager> tabs) {
        super(activity, tabs);
    }

    @Override
    public void initView(View view, Activity activity) {
        isOnCreate = true;
        mActivity = activity;
        mStockData = new ArrayList<>();
        hushen300Entity = new StockInfoEntity();
        mLoadingDialog = LoadingDialog.initDialog(activity, "正在加载");
        mRelativeLayout = (View) view.findViewById(R.id.selfQTopLayout);
        mPullDownScrollView = (PullLayout) view.findViewById(R.id.selfchoice_PullDownScroll);
        mHeadTv2 = (TextView) view.findViewById(R.id.currentPriceHeadtv2);
        mWinProTv = (TextView) view.findViewById(R.id.roundLeftTv);
        mZdProTv = (TextView) view.findViewById(R.id.roundRightTv);
        mTotalTv = (TextView) view.findViewById(R.id.selfQpersentTv);
        mHeadTv3 = (TextView) view.findViewById(R.id.zdfHeadtv3);
        mHeadTv2.setOnClickListener(this);
        mHeadTv3.setOnClickListener(this);
        mPullDownScrollView.setOnPullListener(this);


        SelfChoicQuotationUtil.settingHeadIv(SelfChoicQuotationUtil.DEFAULT, mHeadTv2, mHeadTv3);
        headIcPositionState = HEADSTATE_NO;
//        mRightProgress = (RoundProgressBar) view.findViewById(R.id.roundProgressRight);
//        mLeftProgress = (RoundProgressBar) view.findViewById(R.id.roundProgressLeft);

        ListView listView = (ListView) view.findViewById(R.id.choice_quotation_listview);
        mAdapter = new SelfChoiceQuotationAdapter();
        mAdapter.setSelfChlickListener(this);
        mAdapter.setDatas(mStockData,mJuedgeOption);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//                LogHelper.e(TAG,"hushen300Price:"+hushen300Entity);
                if(hushen300Entity!=null){
                    SelfChoicQuotationUtil.setSelfTopValue(mStockData,hushen300Entity,mRelativeLayout, mTotalTv,mWinProTv,mZdProTv);
                }
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String stockName = "";
        String stockNumber = "";

        if (mStockData != null && mStockData.size() > 0) {
            stockName = mStockData.get(position).getStockName();
            stockNumber = mStockData.get(position).getStockNumber();
        }

        if (!TextUtils.isEmpty(stockName) && !TextUtils.isEmpty(stockNumber)) {
            Intent intent = new Intent();
            StockDetailEntity data = new StockDetailEntity();
            data.setStockName(stockName);
            data.setStockCode(stockNumber);
            intent.putExtra("stockIntent", data);
            intent.setClass(mActivity, StockDetailActivity.class);
            mActivity.startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
//        LogHelper.e(TAG,"onClick ID:"+v.getId());
        switch (v.getId()) {
            case R.id.currentPriceHeadtv2:

                if (mPriceHeadTvChangeState1 == null) {
                    mPriceHeadTvChangeState1 = new PriceHeadTvChangeState(mHeadFlag1State, R.id.currentPriceHeadtv2, SelfChoiceQuotationTab.this);
                }

                mHeadFlag1State = mPriceHeadTvChangeState1.changeState();
                break;
            case R.id.zdfHeadtv3:

                if (mPriceHeadTvChangeState2 == null) {
                    mPriceHeadTvChangeState2 = new PriceHeadTvChangeState(mHeadFlag2State, R.id.zdfHeadtv3, SelfChoiceQuotationTab.this);
                }

                mHeadFlag2State = mPriceHeadTvChangeState2.changeState();
                break;
        }
    }

    @Override
    public void stateDefault(boolean hasNet, int clickId, int state) {
        doClickHeadTv = true;
        doDefault(hasNet);

        SelfChoicQuotationUtil.settingHeadIv(SelfChoicQuotationUtil.DEFAULT, mHeadTv2, mHeadTv3);

//        switch (clickId) {
//            case R.id.currentPriceHeadtv2:
//                SelfChoicQuotationUtil.settingHeadIv(SelfChoicQuotationUtil.DEFAULT, mHeadTv2, null);
//                break;
//            case R.id.zdfHeadtv3:
//                SelfChoicQuotationUtil.settingHeadIv(SelfChoicQuotationUtil.DEFAULT, null, mHeadTv3);
//                break;
//        }

        headIcPositionState = HEADSTATE_NO;
    }

    @Override
    public void onResume() {
        isOnCreate = false;
        super.onResume();
//        LogHelper.e("SelfChoceQuoteationTab","onResume");
    }

    /**
     * 当点击头部Head,箭头变成默认状态
     *
     * @param hasNet
     */
    private void doDefault(boolean hasNet) {
//        cancle();
        if (hasNet && !TextUtils.isEmpty(mCodes)) {
            if (isFirst) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.show();
                }
                isFirst = false;
            }
            mHesdType = NOARRAY;
            mOrder = "0";
//            if (SelfChoicService.mOberservers != null && SelfChoicService.mOberservers.size() < 1) {
//                SelfChoicService.mOberservers.add(this);
//            }
            requestData();
        } else {

        }
    }

    @Override
    public void stateUp(boolean hasNet, int clickId, int state) {
        doClickHeadTv = false;
//        cancle();
        switch (clickId) {
            case R.id.currentPriceHeadtv2:
                doUpByClickHeadPriceTv(hasNet);
                SelfChoicQuotationUtil.settingHeadIv(SelfChoicQuotationUtil.UP, mHeadTv2, mHeadTv3);

                //如果之前的状态是 在涨跌页签上有箭头 则点击 价格页签时 要还原mHeadFlag2State 的状态
                if (headIcPositionState == HEADSTATE_ZD) {
                    mHeadFlag2State = mPriceHeadTvChangeState2.cancelState();
                }

                headIcPositionState = HEADSTATE_PRICE;
                break;
            case R.id.zdfHeadtv3:
                doUpByClickHeadZdTv(hasNet);
                SelfChoicQuotationUtil.settingHeadIv(SelfChoicQuotationUtil.UP, mHeadTv3, mHeadTv2);

                //如果之前的状态是 在价格页签上有箭头 则点击 涨跌页签时 要还原mHeadFlag1State 的状态
                if (headIcPositionState == HEADSTATE_PRICE) {
                    mHeadFlag1State = mPriceHeadTvChangeState1.cancelState();
                }

                headIcPositionState = HEADSTATE_ZD;
                break;
        }
    }

    /**
     * 点击按价格排序箭头向上的操作
     *
     * @param hasNet
     */
    private void doUpByClickHeadPriceTv(boolean hasNet) {
        mHesdType = NEWPRICEARRAY;
        if (hasNet && !TextUtils.isEmpty(mCodes)) {

            if (mLoadingDialog != null) {
                mLoadingDialog.show();
            }

            mOrder = "0";
//            if (SelfChoicService.mOberservers != null && SelfChoicService.mOberservers.size() < 1) {
//                SelfChoicService.mOberservers.add(this);
//            }
            requestData();
        } else {
        }
    }

    /**
     * 点击按涨跌排序箭头向上的操作
     *
     * @param hasNet
     */
    private void doUpByClickHeadZdTv(boolean hasNet) {
        if (mJuedgeOption) {
            mHesdType = ZDFARRAY;
        } else {
            mHesdType = ZDZARRAY;
        }

        if (hasNet && !TextUtils.isEmpty(mCodes)) {

            if (mLoadingDialog != null) {
                mLoadingDialog.show();
            }

            mOrder = "0";

//            if (SelfChoicService.mOberservers != null && SelfChoicService.mOberservers.size() < 1) {
//                SelfChoicService.mOberservers.add(this);
//            }

            requestData();
        } else {
//            if (ZDZARRAY.equals(mHesdType)) {
//                mBeans = Db_PUB_STOCKLIST.queryAllDatasByUpAndDownValueAsc();
//                mAdapter.setDatas(mBeans, mJuedgeOption);
//            } else {
//                mBeans = Db_PUB_STOCKLIST.queryAllDatasByUpAndDownRatioAsc();
//                mAdapter.setDatas(mBeans, mJuedgeOption);
//            }
        }
    }

    @Override
    public void stateDown(boolean hasNet, int clickId, int state) {
        doClickHeadTv = false;
//        cancle();
        switch (clickId) {
            case R.id.currentPriceHeadtv2:
                doDownByClickHeadPriceTv(hasNet);
                SelfChoicQuotationUtil.settingHeadIv(SelfChoicQuotationUtil.DOWN, mHeadTv2, mHeadTv3);

                //如果之前的状态是 在涨跌页签上有箭头 则点击 价格页签时 要还原mHeadFlag2State 的状态
                if (headIcPositionState == HEADSTATE_ZD) {
                    mHeadFlag2State = mPriceHeadTvChangeState2.cancelState();
                }

                headIcPositionState = HEADSTATE_PRICE;
                break;
            case R.id.zdfHeadtv3:
                doDownByClickHeadZdTv(hasNet);
                SelfChoicQuotationUtil.settingHeadIv(SelfChoicQuotationUtil.DOWN, mHeadTv3, mHeadTv2);

                //如果之前的状态是 在价格页签上有箭头 则点击 涨跌页签时 要还原mHeadFlag1State 的状态
                if (headIcPositionState == HEADSTATE_PRICE) {
                    mHeadFlag1State = mPriceHeadTvChangeState1.cancelState();
                }

                headIcPositionState = HEADSTATE_ZD;
                break;
        }
    }

    private void doDownByClickHeadPriceTv(boolean hasNet) {
        if (hasNet && !TextUtils.isEmpty(mCodes)) {

            if (mLoadingDialog != null) {
                mLoadingDialog.show();
            }

            mHesdType = NEWPRICEARRAY;
            mOrder = "1";
//            if (SelfChoicService.mOberservers != null && SelfChoicService.mOberservers.size() < 1) {
//                SelfChoicService.mOberservers.add(this);
//            }
            requestData();
        } else {
//            mBeans = Db_PUB_STOCKLIST.queryAllDatasByPriceDesc();
//            mAdapter.setDatas(mBeans, mJuedgeOption);
        }
    }

    private void doDownByClickHeadZdTv(boolean hasNet) {
        if (mJuedgeOption) {
            mHesdType = ZDFARRAY;
        } else {
            mHesdType = ZDZARRAY;
        }

        if (hasNet && !TextUtils.isEmpty(mCodes)) {
            if (mLoadingDialog != null) {
                mLoadingDialog.show();
            }

            mOrder = "1";
//            if (SelfChoicService.mOberservers != null && SelfChoicService.mOberservers.size() < 1) {
//                SelfChoicService.mOberservers.add(this);
//            }
            requestData();
        } else {
//            if (ZDZARRAY.equals(mHesdType)) {
//                mBeans = Db_PUB_STOCKLIST.queryAllDatasByUpAndDownValueDesc();
//                mAdapter.setDatas(mBeans, mJuedgeOption);
//            } else {
//                mBeans = Db_PUB_STOCKLIST.queryAllDatasByUpAndDownRatioDesc();
//                mAdapter.setDatas(mBeans, mJuedgeOption);
//            }
        }
    }


    @Override
    public void onClick(View v, boolean falg) {
        mJuedgeOption = falg;   // 如果 falg == false 就是转换到涨跌值， falg == true 就是转换成涨跌幅
        if (falg) {
            mHeadTv3.setText("涨跌幅");
            mHesdType = ZDFARRAY;
        } else {
            mHeadTv3.setText("涨跌值");
            mHesdType = ZDZARRAY;
        }

        //1.如果没有任何操作的情况下，点击涨跌值和涨跌幅的转换不进入网络排序
        //2.如果在现价和涨跌值或涨跌幅项，没有向上或向下箭头，点击涨跌值和涨跌幅的转换不进入网络排序
        if (!doClickHeadTv && headIcPositionState == HEADSTATE_ZD) {

            if (Helper.isNetWorked()) {
//                cancle();
                if (!TextUtils.isEmpty(mCodes)) {
//                    if (SelfChoicService.mOberservers != null && SelfChoicService.mOberservers.size() < 1) {
//                        SelfChoicService.mOberservers.add(this);
//                    }
                    requestData();

                }
            }
        }
    }

    private void timerConnet() {
        requestData();
    }

    private void reStartConnect() {
    }

    private int count=0;
    private void requestData(){
//        count++;
//        if(count%20==1)
//        {
//            LogHelper.e(TAG, "******************requestData codes:"+mCodes);
//        }
        if("-1".equals(mCodes)){
            LogUtil.e("no mystock");
            return;
        }
        LogHelper.e(TAG, "******************requestData*********");
        isCallBackSuccess = false;
        Map map = new HashMap();
        Object[] object = new Object[1];
        Map map2 = new HashMap();
        map2.put("code", mCodes);
        map2.put("market", "0");
        map2.put("type", mHesdType);
        map2.put("order", mOrder);
        object[0] = map2;

        Gson gson = new Gson();
        String strJson = gson.toJson(object);
        map.put("FUNCTIONCODE", "HQING005");
        map.put("PARAMS", strJson);

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                isCallBackSuccess = true;
                closeWating();
                if(isShow&&!isAutoRequest){
                    isAutoRequest = false;
                    Helper.getInstance().showToast(CustomApplication.getContext(), "请求数据失败" );
                }
//                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
//                mCallbackResult.getResult(beans, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                //[{"bytes":"0","totalCount":"0","data":"","code":"-1"}]
                int totalCount = 0;
                try {
//                    ObjectMapper objectMapper = JacksonMapper.getInstance();
//                    ArrayList<Map<String, Object>> responseValues = objectMapper.readValue(response, new ArrayList<Map<String, Object>>().getClass());
//                    LogHelper.e(TAG,"response"+response);
                    String code = "";
                    if (TextUtils.isEmpty(response)) {
                        return ;
                    }
                    JSONObject jsonObject = new JSONObject(response.substring(1,response.length()-1));
                    JSONArray dataArr =jsonObject.optJSONArray("data");
                    StockInfoEntity _beanT;
                    refleshTime = TransitionUtils.string2int(jsonObject.optString("time"));
                    code = jsonObject.optString("code");
                    totalCount = TransitionUtils.string2int(jsonObject.optString("totalCount"));
                    if(!"0".equals(code)){
                        return;
                    }
                    int k = -1;
                    ArrayList<StockInfoEntity> tempList = new ArrayList<StockInfoEntity> () ;
                    for (int i = 0; i < dataArr.length(); i++) {
                        JSONArray itemJson = dataArr.getJSONArray(i);
                        String stkCode = itemJson.optString(0);
                        _beanT= new StockInfoEntity();//mStockData.get(i);
                        _beanT.setStockNumber(stkCode);

//                        LogHelper.e(TAG,"mHolsJson.contains :"+mHolsJson.contains(stkCode));
                        if (mHolsJson.contains(stkCode)) {
                            _beanT.setApperHoldStock("true");
                            _beanT.setIsHoldStock("ture");
                            _beanT.setStockholdon("true");
                            _beanT.setStock_flag(StockTable.STOCK_HOLD);
                        }
                        if("20399300".equals(stkCode)){
                            k=i;
                        }
                        String nameT = itemJson.optString(1);
                        if(TextUtils.isEmpty(nameT)||"-".equals(nameT)){
                            StockInfoEntity entity = Db_PUB_STOCKLIST.queryStockFromID(_beanT.getStockNumber());
                            if (null != entity && !TextUtils.isEmpty(entity.getStockName())) {
                                nameT = entity.getStockName();
                            }
                        }
                        _beanT.setStockName(nameT);
                        _beanT.setNewPrice(itemJson.optString(3));
                        _beanT.setClose(itemJson.optString(4));
                        _beanT.setHot(itemJson.optString(7));
                        Helper.getZdfAndzdz(_beanT,_beanT.getClose(),_beanT.getNewPrice());//设置涨跌幅 和涨跌额
                        if(_beanT.getNewPrice().equals("-")){
                            LogHelper.e(TAG,_beanT.getUpAndDownValue()+"||||"+_beanT.getPriceChangeRatio());
                        }
                        tempList.add(_beanT);
//                        mStockData.set(i,_beanT);
                    }
                    if(k!=-1){
                        tempList.remove(k);
                    }
                    mStockData = tempList;
                    if(totalCount>0)
                    {
                        JSONArray itemJson1 = dataArr.getJSONArray(k);
                        StockInfoEntity stkE = new StockInfoEntity();
                        if("20399300".equals(itemJson1.optString(0))){
                            stkE.setStockNumber(itemJson1.optString(0));
                            stkE.setStockName(itemJson1.optString(1));
                            stkE.setNewPrice(itemJson1.optString(3));
                            stkE.setClose(itemJson1.optString(4));
                            stkE.setHot(itemJson1.optString(7));
                            Helper.getZdfAndzdz(stkE,stkE.getClose(),stkE.getNewPrice());//设置涨跌幅 和涨跌额
                            hushen300Entity = stkE;
                        }
                    }

                    mAdapter.setDatas(mStockData,mJuedgeOption);
                    closeWating();
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void closeWating(){
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        if (mLoadInterface != null) {
            mLoadInterface.complited();
        }
        if (mPullDownScrollView != null) {
            mPullDownScrollView.finishPull();
        }
    }
    private void cancle() {
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }

//    private Message sendMessage(String[] keys, String[] values) {
//        Message msg = new Message();
//        Bundle bundle = new Bundle();
//        for (int i = 0; i < keys.length; i++) {
//            bundle.putString(keys[i], values[i]);
//        }
//        msg.setData(bundle);
//        return msg;
//    }

    private boolean isFirst = false;



    @Override
    public void myTabonResume() {
        LogHelper.e(TAG, "myTabonResume isOnCreate:"+isOnCreate);
        boolean tt = SpUtils.getBoolean(CustomApplication.getContext(), MARKET_TAG,false);
        if(tt){
            LogHelper.e(TAG, "行情未显示，跳过……");
           return;
        }
//        ArrayList<StockInfoEntity> tempData = new ArrayList<>();
//        tempData.addAll(mBeans);
//        mAdapter.setDatas(tempData, mJuedgeOption);
//        mBeans.clear();
//
//        ArrayList<StockInfoEntity> tempbeans = new ArrayList<>();
//        if (mSelfChoiceDataCallback != null) {
//            mSelfChoiceDataCallback.callbackSelfchoiceDatas(tempbeans);
//        }

        initDBStockInfo();
        mAdapter.refleshHoldStatus();//刷新一下持仓标签状态
        mAdapter.setDatas(mStockData,true);
//        mAdapter.notifyDataSetChanged();
        timerConnet();

        doClickHeadTv = true;
        mHeadFlag1State = 0;
        mHeadFlag2State = 0;
        if(!isOnCreate){
//            requestData();
            setTimerTask();
        }
//        SelfChoicQuotationUtil.settingHeadIv(SelfChoicQuotationUtil.DEFAULT, mHeadTv2, mHeadTv3);
//        headIcPositionState = HEADSTATE_NO;
    }

    @Override
    public void toRunConnect() {

//        if (mServiceIntent != null) {
//
////            if (SelfChoicService.mOberservers != null && SelfChoicService.mOberservers.size() < 1) {
////                SelfChoicService.mOberservers.add(this);
////            }
//            mActivity.startService(mServiceIntent);
//        } else {
//            timerConnet();
//        }
        LogHelper.e(TAG,"toRunConnect");
        if(MarketFragment.pageIndex==0){
            setTimerTask();
        }
    }

    @Override
    public void onHiddenChanged(boolean hided) {
        super.onHiddenChanged(hided);
        LogHelper.e(TAG,"onHiddenChanged hided:"+hided);
        if(!hided){
            initDBStockInfo();
            requestData();
            setTimerTask();
        }else{
            cancle();
        }
    }

    @Override
    public void toStopConnect() {
        LogHelper.e(TAG,"toStopConnect ");
//        if (mBeans != null && mBeans.size() > 0) {
//            for (StockInfoEntity bean : mBeans) {
//                Db_PUB_STOCKLIST.updateOneStockDataByStockNumber(bean);
//            }
//        }

        cancle();
    }

    /**
     * 初始化数据库中的自选股
     */
    private void initDBStockInfo() {
        mStockData = Db_PUB_STOCKLIST.queryStockListDatas();
        StringBuilder sb = new StringBuilder();

        if (mStockData != null && mStockData.size() > 0) {
            for (StockInfoEntity _entity : mStockData) {
                _entity.setNewPrice("0.0");
                _entity.setClose("0.0");
                _entity.setPriceChangeRatio(0);
                _entity.setUpAndDownValue(0);
                if ((_entity.getStock_flag()&StockTable.STOCK_HOLD) == StockTable.STOCK_HOLD) {
                    sb.append(_entity.getStockNumber()).append(",");
                }
            }
            mHolsJson = sb.toString();
        }
///注释掉了 胜率计算部分，不赋值。后期处理
//        if (isFirstInto) {
//
//            if (beans != null && beans.size() > 0)
//

//            }
//
//            mAdapter.setDatas(beans, mJuedgeOption);
//            isFirstInto = false;
//        }

        if (mStockData != null && mStockData.size() > 0) {
            mCodes = SelfChoicQuotationUtil.getCodes(mStockData, mRelativeLayout);
        } else {
            mCodes = "-1";
        }
        LogHelper.e(TAG,"initDBStockInfo mystks:"+mCodes+"  holdstks:");//mHolsJson
    }


    public void setSelfChoiceDataCallback(SelfChoiceDataCallback selfChoiceDataCallback) {
        mSelfChoiceDataCallback = selfChoiceDataCallback;
    }


    @Override
    public void onRefresh() {
//        cancle();

//        if (SelfChoicService.mOberservers != null && SelfChoicService.mOberservers.size() < 1) {
//            SelfChoicService.mOberservers.add(this);
//        }

        requestData();
    }

    @Override
    public void onLoad() {

    }
//
//    @Override
//    public void registerObserver(SelfChoicTimeObserver observer) {
//        mObserves.add(observer);
//    }
//
//    @Override
//    public void removeObserver(SelfChoicTimeObserver observer) {
//        int num = mObserves.indexOf(observer);
//        if (num >= 0) {
//            mObserves.remove(observer);
//        }
//    }
//
//    @Override
//    public void notifyObservers(long time) {
//        for (SelfChoicTimeObserver observer : mObserves) {
//            observer.update(time);
//        }
//    }

    public interface SelfChoiceDataCallback {
        public void callbackSelfchoiceDatas(ArrayList<StockInfoEntity> mBeans);
    }
    private boolean isAutoRequest = false;
    private boolean isOnCreate = true;
    private int refleshTime = 0;
    private int refleshCount = 0;
    private Timer mTimer;
    private boolean isCallBackSuccess = false;
    private void setTimerTask() {
        if(mTimer!=null){
            mTimer.cancel();
        }
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
//                LogHelper.e(TAG, "stockDetailData Timer request:"+isCallBackSuccess+"|"+refleshTime+"  refleshCount:"+refleshCount);
                if(isCallBackSuccess)
                {
//                    if(!"0".equals(refleshTime)){
                        if(refleshTime>0){
                            refleshCount +=3;
                            if(refleshCount>=refleshTime){
                                requestData();
                            }
                        }else{
                            refleshCount = 0;
                            if("-1".equals(mCodes)){
                                return;
                            }
                            loadingHandler.sendEmptyMessage(0);
                            isAutoRequest = true;
                            requestData();
//                            LogHelper.e(TAG, "stockDetailData Timer request:"+isShowMingxi);
                        }
//                    }
                }
            }
        }, 3000, 3000/* 表示3000毫秒之後，每隔3000毫秒執行一次 */);
    }
    Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLoadInterface != null) {
                mLoadInterface.loading();
            }
        }
    };
    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_choice_quotation;
    }
}
