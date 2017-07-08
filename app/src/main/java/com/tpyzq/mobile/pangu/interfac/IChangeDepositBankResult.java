package com.tpyzq.mobile.pangu.interfac;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2017/7/4.
 * 三存银行变更
 */

public interface IChangeDepositBankResult {

    void loadingClose();

    void error(String errorMsg);//失败

    void toLogin();//注册或登录

    void getBankInfos(List<Map<String, String>> bankInfos);//获取银行账号信息

    void checkCloseAccount(String stauts, String error_info);//检查销户

    void needPwd(String bankNo, String bkopenpwdFlag, String bkcancelpwdFlag, String bankName);//三存银行  查询注销和新开三存银行是否需要密码

    void closeAccountSuccess();
}
