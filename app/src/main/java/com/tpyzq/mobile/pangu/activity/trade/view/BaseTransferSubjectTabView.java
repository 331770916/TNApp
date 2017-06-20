package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.interfac.BanksTransferAccountsResultCode;
import com.tpyzq.mobile.pangu.interfac.ITab;
import com.tpyzq.mobile.pangu.interfac.ITabDataObserver;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/8/24.
 * 银证转账用TabView
 */
public abstract class BaseTransferSubjectTabView implements ITab {

    private View mView;
    private ArrayList<ITabDataObserver> mObservers;
    private Object mObject;
    private boolean mIsMainTab;

    public BaseTransferSubjectTabView(ViewGroup viewGroup, Activity activity, boolean isMainTab, ArrayList<ITabDataObserver> observers, BanksTransferAccountsResultCode banksTransferAccountsResultCode) {
        mView = LayoutInflater.from(CustomApplication.getContext()).inflate(getTabLayoutId(), null);
        if (observers != null) {
            mObservers = observers;
        }
        initView(viewGroup, mView, activity, isMainTab, banksTransferAccountsResultCode);
    }

    @Override
    public void registerObserver(ITabDataObserver observer) {
        if (mObservers != null) {
            mObservers.add(observer);
        }
    }

    @Override
    public void removeObserver(ITabDataObserver observer) {

        if (mObservers != null) {
            int index  = mObservers.indexOf(observer);
            if (index >= 0) {
                mObservers.remove(index);
            }
        }
    }

    public void setData(Object object, boolean isMainTab) {
        mObject = object;
        mIsMainTab = isMainTab;
        notifyObservers();
    }

    @Override
    public void notifyObservers() {

        if (mObservers != null) {
            for (ITabDataObserver observer : mObservers) {
                observer.update(mObject, mIsMainTab, mObservers.indexOf(observer));
            }
        }
    }

    public View getContentView(){

        if (mView != null) {
            return mView;
        }

        return null;
    }

    public abstract void initView(ViewGroup viewGroup,View view, Activity activity, boolean isMainTab, BanksTransferAccountsResultCode banksTransferAccountsResultCode);

    public abstract void onResume();

    public abstract void onStop();

    public abstract int getTabLayoutId();

}
