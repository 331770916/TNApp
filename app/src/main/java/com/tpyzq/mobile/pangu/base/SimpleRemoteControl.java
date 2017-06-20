package com.tpyzq.mobile.pangu.base;


import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.ICommand;

/**
 * Created by zhangwenbo on 2016/7/7.
 * 控制器
 */
public class SimpleRemoteControl {

    private ICommand mCommand;
    private ICallbackResult mCallbackResult;

    public SimpleRemoteControl(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
    }

    public void setCommand(ICommand command){
        mCommand = command;
    }

    public void startConnect () {
        mCommand.connect(mCallbackResult);
    }
}
