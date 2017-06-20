package com.tpyzq.mobile.pangu.interfac;

import android.os.Handler;
import android.os.Message;
import android.widget.AbsListView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.Helper;


/**
 * Created by zhangwenbo on 2016/8/2.
 * ListView 滑动监听实现类， 用于上下翻页用
 */
public class ListViewScroller implements AbsListView.OnScrollListener, IListViewScrollObserver{

    private String   mCancleTag;
    private ListView mListView;
    private String   mStartNumber;
    private boolean  doSelectMiddle;        //是否第一次加载list列表
    private boolean  isTopOrBottomFlag;      //是否到达顶部或底部
    private boolean  isScrollFlag;           //是否滚动标志位
    private int      mNetTotalCount;

    private ListViewScrollerListener mListViewScrollerListener;

    public ListViewScroller(String cancleTag, ListView listView, String startNumber, ListViewScrollerListener listViewScrollerListener, IListViewScrollSubject subject) {
        mCancleTag = cancleTag;
        mListView = listView;
        mStartNumber = startNumber;
        mListViewScrollerListener = listViewScrollerListener;

        if (subject != null) {
            subject.registerObserver(ListViewScroller.this);
        }
    }

    @Override
    public void update(int netTotalCount) {
        mNetTotalCount = netTotalCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE :
                if (mListView.getFirstVisiblePosition() == 0) {
//                    LogHelper.e("ListView Scroll","滚动到顶部");
                    pageChangeHandler.sendEmptyMessageDelayed(1,300);
                }else
                    // 判断滚动到底部
                    if (mListView.getLastVisiblePosition() == (mListView.getCount() - 1)) {
//                        LogHelper.e("ListView Scroll","滚动到底部");
                        if (mNetTotalCount % 30 == 0) {
                            pageChangeHandler.sendEmptyMessageDelayed(0,300);
                        } else {
                            doSelectMiddle = false;
                            mListViewScrollerListener.doSelectMiddle(doSelectMiddle);
                            Helper.getInstance().showToast(CustomApplication.getContext(), "没有更多数据了");
                        }

                    }  else {
                        pageChangeHandler.sendEmptyMessageDelayed(2,1000);
                    }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL :
                NetWorkUtil.cancelSingleRequestByTag(mCancleTag);
                break;
        }
    }
    private Handler pageChangeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int _tempStartNum = Integer.parseInt(mStartNumber);
            int what = msg.what;
            if(what==0){
                _tempStartNum = _tempStartNum + 10;
                mStartNumber = String.valueOf(_tempStartNum);
                doSelectMiddle = true;
                mListViewScrollerListener.doSelectMiddle(doSelectMiddle);
                mListViewScrollerListener.nextPage(mStartNumber);
                mListViewScrollerListener.juedgeGesture(false);
            }else if(what==1){
                if (_tempStartNum >= 10) {
                    _tempStartNum = _tempStartNum - 10;
                    mStartNumber = String.valueOf(_tempStartNum);

                    doSelectMiddle = true;
                    mListViewScrollerListener.doSelectMiddle(doSelectMiddle);
                    mListViewScrollerListener.lastPage(mStartNumber);
                    mListViewScrollerListener.juedgeGesture(true);
                } else {
                    doSelectMiddle = false;
                    mListViewScrollerListener.doSelectMiddle(doSelectMiddle);
                }
            }else if(what==2){
                doSelectMiddle = false;
                mListViewScrollerListener.doSelectMiddle(doSelectMiddle);
                mListViewScrollerListener.connectBySrollStop();
            }
        }
    };
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        isTopOrBottomFlag = firstVisibleItem == 0 || firstVisibleItem + visibleItemCount == totalItemCount;

        isScrollFlag = firstVisibleItem + visibleItemCount == totalItemCount && !isScrollFlag;
    }

    public interface ListViewScrollerListener {

        void nextPage(String startNumber);     //下一页

        void lastPage(String startNumber);     //上一页

        void connectBySrollStop();   //当滚动停止的时候

        void doSelectMiddle(boolean doSelectMiddle);           //是否第一进入

        void juedgeGesture(boolean isUp);

    }
}
