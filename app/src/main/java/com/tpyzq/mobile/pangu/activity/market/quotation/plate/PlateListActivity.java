package com.tpyzq.mobile.pangu.activity.market.quotation.plate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.StockDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.SearchActivity;
import com.tpyzq.mobile.pangu.activity.market.quotation.StockListActivity;
import com.tpyzq.mobile.pangu.activity.market.quotation.StockListIntent;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockDetailEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.IListViewScrollObserver;
import com.tpyzq.mobile.pangu.interfac.IListViewScrollSubject;
import com.tpyzq.mobile.pangu.interfac.ListViewScroller;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/7/17.
 * 板块列表
 */
public class PlateListActivity extends BaseActivity implements View.OnClickListener, ListViewScroller.ListViewScrollerListener,IListViewScrollSubject{
    private String mType = "1";       //1.行业2.板块3.地域
    private TextView mHeadTv2;
    private TextView mHeadTv3;
    private String[] mFiledNames;
    private ListView mListView;
    private CustomAdapter mAdapter;
    private boolean middleFlag = true;
    private String mStart; //开始查询条数
    private String mOrder;//1.涨幅 0.跌幅
    private FrameLayout mEmptyLayout;

    private StockListIntent mBean;
    private static final String TAG = "PlateListActivity";
    private ViewHodler viewHodler = null;

    private boolean doSelectMiddle;         //是否加载list列表
//    private ArrayList<IListViewScrollObserver> mObservers;
//    public static ArrayList<PlateListTimeObsever> mObserves = new ArrayList<>();
    private int mNetTotalCount;
    private ProgressBar mPlateListProgress;

//    private Intent mServiceIntent;
    private boolean mJudegeGesture;
    private ArrayList<StockInfoEntity> mBeans;

    @Override
    public void initView() {

        Intent intent = getIntent();
        mBean = intent.getParcelableExtra("stockIntent");
        if (mBean == null) {
            return;
        }

        mEmptyLayout = (FrameLayout) findViewById(R.id.fl_stocklistLayout);
        mEmptyLayout.setVisibility(View.VISIBLE);
        mEmptyLayout.setOnClickListener(this);


        mPlateListProgress = (ProgressBar) findViewById(R.id.stocklist_progress);

//        mObservers = new ArrayList<>();
        mStart = "0";
        mOrder = "1";
        mType = intent.getStringExtra("plateType");
        if(mType==null){
            mType = "1";
        }

        findViewById(R.id.stocklist_back).setOnClickListener(this);
        findViewById(R.id.stocklist_search).setOnClickListener(this);

        setHeadTv(mBean);

        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.sort_p);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        middleFlag = false;
        mHeadTv2.setCompoundDrawables(null, null, drawable, null);//画在右边

        mFiledNames = mBean.getFiledNames();

        if (mFiledNames == null) {
            return;
        }

        mListView = (ListView) findViewById(R.id.stockListView);
        mAdapter = new CustomAdapter();

        mListView.setAdapter(mAdapter);
        lvser = new ListViewScroller(TAG, mListView, mStart, PlateListActivity.this, PlateListActivity.this);
        mListView.setOnScrollListener(lvser);
    }
    private ListViewScroller lvser;
    @Override
    protected void onResume() {
        super.onResume();
        LogHelper.e(TAG,"onResume plateType:"+mType);
        timerConnect();
        startTimerTask();
//        reStartConnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimerTask();
    }

    @Override
    public void nextPage(String startNumber) {
        mPlateListProgress.setVisibility(View.VISIBLE);
        mStart = startNumber;
        cancel();
        startTimerTask();
        reStartConnect();
    }

    @Override
    public void lastPage(String startNumber) {
        cancel();
        mPlateListProgress.setVisibility(View.VISIBLE);
        mStart = startNumber;
        startTimerTask();
        reStartConnect();
    }

    @Override
    public void juedgeGesture(boolean isUp) {
        mJudegeGesture = isUp;
    }

    @Override
    public void connectBySrollStop() {
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

    @Override
    public void doSelectMiddle(boolean doSelectMiddle) {
        this.doSelectMiddle = doSelectMiddle;
    }

    /**
     * 处理网络返回的板块数据
     * @param entity
     * @return
     */
    private ArrayList<StockInfoEntity> initData(StockInfoEntity entity) {
        ArrayList<StockInfoEntity> entities = new ArrayList<>();
        if (entity != null && !entity.getCode().equals("-5") && entity.getData() != null && entity.getData().length() > 0) {
            for (int i = 0; i < entity.getData().length(); i++) {
                StockInfoEntity _bean = new StockInfoEntity();
                JSONArray arrayT = entity.getData().optJSONArray(i);
                _bean.setType("4");
                _bean.setCode(entity.getCode());
                _bean.setTotalCount(entity.getTotalCount());

                if (!TextUtils.isEmpty(arrayT.optString(0))) {
                    _bean.setIndustryNumber(arrayT.optString(0));
                }

                if (!TextUtils.isEmpty(arrayT.optString(1))) {
                    _bean.setIndustryName(arrayT.optString(1));
                }

                if (!TextUtils.isEmpty(arrayT.optString(2))) {
                    _bean.setIndustryUpAndDown(arrayT.optString(2));
                }

                if (!TextUtils.isEmpty(arrayT.optString(3))) {
                    _bean.setStockNumber(arrayT.optString(3));
                }

                if (!TextUtils.isEmpty(arrayT.optString(4))) {
                    _bean.setStockName(arrayT.optString(4));
                }

                if (!TextUtils.isEmpty(arrayT.optString(5))) {
                    _bean.setPriceChangeRatio(TransitionUtils.string2double(arrayT.optString(5)));
                }
                entities.add(_bean);
            }
        }

        return entities;
    }

    /**
     * 初始化头部信息
     *
     * @param bean
     */
    private void setHeadTv(StockListIntent bean) {
        TextView titleTv = (TextView) findViewById(R.id.stocklist_title);
        TextView headTv1 = (TextView) findViewById(R.id.stocklist_headTv1);
        headTv1.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));
        mHeadTv2 = (TextView) findViewById(R.id.stocklist_headTv2);
        mHeadTv3 = (TextView) findViewById(R.id.stocklist_headTv3);
        mHeadTv3.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hideTextColor));

        mHeadTv2.setOnClickListener(this);

        String title = bean.getTitle();
        String head1 = bean.getHead1();
        String head2 = bean.getHead2();
        String head3 = bean.getHead3();

        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }

        if (!TextUtils.isEmpty(head1)) {
            headTv1.setText(head1);
        }

        if (!TextUtils.isEmpty(head2)) {
            mHeadTv2.setText(head2);
        }

        if (!TextUtils.isEmpty(head3)) {
            mHeadTv3.setText(head3);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stocklist_back:
                cancel();
                finish();
                break;
            case R.id.stocklist_search:
                Intent intent = new Intent();
                intent.setClass(PlateListActivity.this, SearchActivity.class);
                startActivity(intent);
                break                                                                                                                                                                                                                                                       ;
            case R.id.stocklist_headTv2:
                if (mPlateListProgress != null) {
                    mPlateListProgress.setVisibility(View.VISIBLE);
                }

                mHeadTv2.setClickable(false);
                mHeadTv2.setFocusable(false);

                Drawable drawable = null;
                mStart = "0";

                if (middleFlag) {
                    try {
                        mOrder = "1";

                        cancel();
                        reStartConnect();

                        drawable = ContextCompat.getDrawable(this, R.mipmap.sort_p);
                        middleFlag = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        mOrder = "0";

                        cancel();
                        reStartConnect();

                        drawable = ContextCompat.getDrawable(this, R.mipmap.sort_n);
                        middleFlag = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                mHeadTv2.setCompoundDrawables(null, null, drawable, null);//画在右边
                break;
            case R.id.fl_stocklistLayout:

                reStartConnect();

                break;
        }
    }


    private void timerConnect() {
//        mServiceIntent = new Intent(PlateListActivity.this, PlateListService.class);
//        String [] keys = {"startNo", "order", "type"};
//        String [] values = {mStart, mOrder, "1"};
//        PlateListService.handler.sendMessage(sendMessage(keys, values));
//        PlateListService.mOberservers.add(this);
//        notifyObservers(3 * 1000);
//        PlateListActivity.this.startService(mServiceIntent);
        request();
    }

    private void reStartConnect() {
        loadingHandler.sendEmptyMessage(0);
//        PlateListService.mOberservers.add(this);
//        String [] keys = {"startNo", "order", "type"};
//        String [] values = {mStart, mOrder, "1"};
//        PlateListService.handler.sendMessage(sendMessage(keys, values));
//        if (mServiceIntent != null) {
//            PlateListActivity.this.startService(mServiceIntent);
//        } else {
//            timerConnect();
//        }
        request();
    }

    private void cancel() {
//        PlateListService.mOberservers.remove(this);
//        if (mServiceIntent != null) {
//            stopService(mServiceIntent);
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancel();
    }


    public void fillArrayList(ArrayList<StockInfoEntity> entities) {
        mPlateListProgress.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.GONE);
        mHeadTv2.setClickable(true);
        mHeadTv2.setFocusable(true);

        if (entities == null || entities.size() <= 0) {
            if (mBeans == null || mBeans.size() <= 0) {
                mEmptyLayout.setVisibility(View.VISIBLE);
            }
//            notifyObservers(60 * 1000);
            return;
        }

        mBeans = entities; //得到返回的结果
        for (StockInfoEntity bean : mBeans) {
            if (bean.getCode().equals("-5") || bean.getCode().equals("-3") || bean.getCode().equals("-1")) {
                Helper.getInstance().showToast(PlateListActivity.this, "Code:-5");
                return;
            }

//            if (!TextUtils.isEmpty(mBeans.get(0).getTotalCount())) {
//                mNetTotalCount = Integer.parseInt(mBeans.get(0).getTotalCount());
//            }
            lvser.update(TransitionUtils.string2int(mBeans.get(0).getTotalCount()));
//            notifyObservers();

            mAdapter.setData(initData(bean));

            if (doSelectMiddle) {
                int index = 0;
                if (mJudegeGesture) {
                    index = 10;//mListView.getLastVisiblePosition()- mListView.getFirstVisiblePosition() ;
                } else {
                    index = 20-(mListView.getLastVisiblePosition()- mListView.getFirstVisiblePosition()+1);
                }
                View v = mListView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                mListView.setSelectionFromTop(index, top);
            }

            String time = mBeans.get(0).getTime();

            if (!TextUtils.isEmpty(time) && !"0".equals(time)) {
                int intTime = Integer.parseInt(time);
//                notifyObservers(intTime * 1000);

            }
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_stocklist;
    }

    @Override
    public void registerObserver(IListViewScrollObserver observer) {

    }

    @Override
    public void removeObserver(IListViewScrollObserver observer) {

    }

    @Override
    public void notifyObservers() {
    }

    /**
     * 板块列表 自定义Adapter
     */
    private class CustomAdapter extends BaseAdapter {

        private ArrayList<StockInfoEntity> mDatas;

        public void setData(ArrayList<StockInfoEntity> datas) {
            mDatas = datas;
            notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {
            if (mDatas != null && mDatas.size() > 0) {
                return mDatas.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {

            if (mDatas != null && mDatas.size() > 0) {
                return mDatas.size();
            }

            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                viewHodler = new ViewHodler();
                convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_platelist_item, null);
                viewHodler.turnoverName = (TextView) convertView.findViewById(R.id.stockItemName);
                viewHodler.turnoverPrice = (TextView) convertView.findViewById(R.id.stockItemPrice);
                viewHodler.turnoverRate = (TextView) convertView.findViewById(R.id.stockItemRate);
                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }

            if (!TextUtils.isEmpty(mDatas.get(position).getIndustryName())) {
                viewHodler.turnoverName.setText(mDatas.get(position).getIndustryName());
            } else {
                viewHodler.turnoverName.setText("-");
            }

            viewHodler.turnoverName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent();
                    StockListIntent data = new StockListIntent();

                    if (!TextUtils.isEmpty(mDatas.get(position).getIndustryNumber())) {

                        String industryNumber = mDatas.get(position).getIndustryNumber();

                        if (!TextUtils.isEmpty(mDatas.get(position).getIndustryName())) {
                            data.setTitle(mDatas.get(position).getIndustryName());
                        }

                        data.setHead2("现价");
                        data.setHead3("涨跌幅");
                        data.setHead1("股票名称");

                        if (!TextUtils.isEmpty(industryNumber)) {
                            intent2.putExtra("code", industryNumber);
                        }

                        intent2.putExtra("market", "0");
                        intent2.putExtra("type", "1");
                        intent2.putExtra("isIndustryStockList", true);
                        intent2.putExtra("tag", "lingzhang");
                        intent2.putExtra("stockIntent", data);

                        intent2.setClass(PlateListActivity.this, StockListActivity.class);
                        startActivity(intent2);
                    }
                }
            });


            if (!TextUtils.isEmpty(mDatas.get(position).getIndustryUpAndDown())) {
                if (Helper.isDecimal(mDatas.get(position).getIndustryUpAndDown())
                        || Helper.isENum(mDatas.get(position).getIndustryUpAndDown())) {

                    DecimalFormat format = new DecimalFormat("#0.00%");
                    double zdf = Double.parseDouble(mDatas.get(position).getIndustryUpAndDown());
                    viewHodler.turnoverPrice.setText(format.format(zdf));

                    if (zdf > 0) {
                        viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.red));
                    } else if (zdf == 0) {
                        viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
                    } else if (zdf < 0) {
                        viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.green));
                    }
                }
            } else {
                viewHodler.turnoverPrice.setText("-");
                viewHodler.turnoverPrice.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));
            }


            viewHodler.turnoverRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    StockDetailEntity stockDetailEntity = new StockDetailEntity();
                    stockDetailEntity.setStockName(mDatas.get(position).getStockName());
                    stockDetailEntity.setStockCode(mDatas.get(position).getStockNumber());
                    if (!TextUtils.isEmpty(stockDetailEntity.getStockNumber()) && !TextUtils.isEmpty(stockDetailEntity.getStockName())) {
                        intent.putExtra("stockIntent", stockDetailEntity);
                        intent.setClass(PlateListActivity.this, StockDetailActivity.class);
                        PlateListActivity.this.startActivity(intent);
                    } else {
                        Helper.getInstance().showToast(PlateListActivity.this, "股票代码或股票名称是空");
                    }
                }
            });
            String stockName = mDatas.get(position).getStockName();

            if (!TextUtils.isEmpty(stockName)) {
                viewHodler.turnoverRate.setText(stockName);
            } else {
                viewHodler.turnoverRate.setText("-");
            }

            viewHodler.turnoverRate.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.hushenTab_titleColor));

//            int val = Helper.dip2px(getApplicationContext(), 10);
//            convertView.setPadding(0, val, 0, val);

            return convertView;
        }
    }

    private class ViewHodler {
        TextView turnoverName;
        TextView turnoverPrice;
        TextView turnoverRate;
    }
    public void request() {
        isCallBackSuccess = false;
        Map<String, String> params = new HashMap();
        Object []  object = new Object[1];
        Map map2 = new HashMap();
        map2.put("Type", mType);
        map2.put("order", mOrder);
        map2.put("start", mStart);
        map2.put("num", "30");
        object[0] = map2;

        Gson gson = new Gson();
        String strJson = gson.toJson(object);
        params.put("PARAMS", strJson);

        params.put("FUNCTIONCODE","HQING009");

        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.getURL_HQ_HHN(), params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, e.toString());
                ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                fillArrayList(beanss);
            }

            @Override
            public void onResponse(String response, int id) {
                isCallBackSuccess = true;
                LogHelper.e(TAG, "response:"+response);
                ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                if (TextUtils.isEmpty(response)) {
                    fillArrayList(beanss);
                    return ;
                }
                try {
                    JSONArray array = new JSONArray(response);
                    if(array==null||array.length()==0){
                        fillArrayList(beanss);
                        return;
                    }
                    JSONObject jsonObject = array.optJSONObject(0);
                    StockInfoEntity stockInfoEntity = new StockInfoEntity();
                    String code = jsonObject.optString("code");
                    String time = jsonObject.optString("date");
                    refleshTime = TransitionUtils.string2int(time);
                    if (code.equals("-5") || code.equals("-1") || code.equals("-3")) {
                        code = "-5";
                        stockInfoEntity.setCode(code);
                    } else {
                        int bytes = TransitionUtils.string2int(jsonObject.optString("bytes"));
                        String totalCount =jsonObject.optString("totalCount");
                        JSONArray array1 = jsonObject.optJSONArray("data");

                        stockInfoEntity.setCode(code);
                        stockInfoEntity.setTime(time);
                        stockInfoEntity.setBytes(bytes);
                        stockInfoEntity.setTotalCount(totalCount);
                        stockInfoEntity.setData(array1);
                    }
                    beanss.add(stockInfoEntity);
                    fillArrayList(beanss);
                } catch (Exception e) {
                    e.printStackTrace();
                    fillArrayList(beanss);
                }
            }
        });

    }
    private int refleshCount = 0;
    private Timer mTimer;
    private int refleshTime = 0;
    private boolean isCallBackSuccess = false;
    private void startTimerTask() {
        if(mTimer!=null){
            mTimer.cancel();
        }
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogHelper.e(TAG, "Timer request:"+isCallBackSuccess+"|"+refleshTime+"  refleshCount:"+refleshCount);
                if(isCallBackSuccess)
                {
                    if(refleshTime>0){
                        refleshCount +=3;
                        if(refleshCount>=refleshTime){
                            reStartConnect();
                        }
                    }else{
                        refleshCount = 0;
                        reStartConnect();
                    }
                }
            }
        }, 3000, 3000/* 表示3000毫秒之後，每隔3000毫秒執行一次 */);
    }
    private void stopTimerTask(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }
    Handler loadingHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPlateListProgress.setVisibility(View.VISIBLE);
        }
    };
}
