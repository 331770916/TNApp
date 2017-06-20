package com.tpyzq.mobile.pangu.activity.market.selfChoice.childQuotation;

import com.tpyzq.mobile.pangu.util.Helper;

/**
 * Created by zhangwenbo on 2016/10/26.
 */
public class PriceHeadTvChangeState implements ChangeStateHeadInterface {

    public static final int DEFAULT_STATE = 0;
    public static final int DOWNSTATE = 1;
    public static final int UP_STATE = 2;

    private int mState = DEFAULT_STATE;
    private int mClickId;
    private HeadStateChangeListener mHeadStateChangeListener;



    public PriceHeadTvChangeState(int state, int clickId,  HeadStateChangeListener headStateChangeListener) {
        mState = state;
        mClickId = clickId;
        mHeadStateChangeListener = headStateChangeListener;
    }

    @Override
    public int changeState() {

        if (mState == DEFAULT_STATE) {
            mState = DOWNSTATE;//起始是默认无箭头状态，点击后箭头向下，改变成向下的状态
            if (Helper.isNetWorked()) {
                mHeadStateChangeListener.stateDown(true, mClickId, UP_STATE);
            } else {
                mHeadStateChangeListener.stateDown(false, mClickId, UP_STATE);
            }

        } else if (mState == DOWNSTATE) {
            mState = UP_STATE;//起始箭头向下状态，点击后箭头向上，改变成向上的状态
            if (Helper.isNetWorked()) {
                mHeadStateChangeListener.stateUp(true, mClickId, UP_STATE);
            } else {
                mHeadStateChangeListener.stateUp(false, mClickId, UP_STATE);
            }

        } else if (mState == UP_STATE) {
            mState = DEFAULT_STATE; //起始箭头是向上状态，点击后箭头消失，恢复默认状态
            if (Helper.isNetWorked()) {
                mHeadStateChangeListener.stateDefault(true, mClickId, DEFAULT_STATE);
            } else {
                mHeadStateChangeListener.stateDefault(false, mClickId, DEFAULT_STATE);
            }
        }

        return mState;
    }

    @Override
    public int cancelState() {
        mState = DEFAULT_STATE;
        return mState;
    }

    public interface HeadStateChangeListener {

        void stateDefault(boolean hasNet, int clickId, int state);

        void stateUp(boolean hasNet, int clickId, int state);

        void stateDown(boolean hasNet, int clickId, int state);


    }

}
