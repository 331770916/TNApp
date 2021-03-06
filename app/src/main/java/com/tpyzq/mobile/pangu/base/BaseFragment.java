package com.tpyzq.mobile.pangu.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentHostCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.log.capture.CrashHandler;

import java.lang.reflect.Field;

/**
 * Fragment基类
 */
public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected Activity mActivity;
    private FragmentHostCallback mHost;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(getFragmentLayoutId(), container, false);
        try {
            this.mContext = getContext();
            this.mActivity = getActivity();
            initView(view);
            Field hostField = getClass().getSuperclass().getDeclaredField("mHost");
            hostField.setAccessible(true);
            mHost = (FragmentHostCallback) hostField.get(this);
        } catch (Exception e) {
            e.printStackTrace();
            storeErr(e);
        }
        if (Build.VERSION.SDK_INT<19){
            View fitView= view.findViewById(R.id.rl_top_bar);
            if (fitView!=null)
                ((ViewGroup) view).removeView(fitView);
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field hostField = getClass().getSuperclass().getDeclaredField("mHost");
            hostField.setAccessible(true);
            if (null == hostField.get(this))
                hostField.set(this, mHost);
        } catch (Exception e) {
            e.printStackTrace();
            storeErr(e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public abstract void initView(View view);

    public abstract int getFragmentLayoutId();

    protected void storeErr(Throwable th){
        CrashHandler.getInstance(mContext).handleException(th);
    }

    public View findViewById(@IdRes int resId){
        return view.findViewById(resId);
    }
}
