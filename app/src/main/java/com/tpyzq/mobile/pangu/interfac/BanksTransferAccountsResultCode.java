package com.tpyzq.mobile.pangu.interfac;

/**
 * Created by zhangwenbo on 2016/9/1.
 * 银证转账模块 网络请求返回code值接口
 */
public interface BanksTransferAccountsResultCode {

    /**
     *
     * @param code                  获取的code值
     * @param tag                   标识
     * @param backIndexActivity     登录后是否返回到上一层界面
     */
    void getCode(String code, String tag, boolean backIndexActivity);
}
