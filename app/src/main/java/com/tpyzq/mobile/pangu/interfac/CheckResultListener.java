package com.tpyzq.mobile.pangu.interfac;

import java.util.Map;

/**
 * Created by zhangwenbo on 2017/3/2.
 */

public interface CheckResultListener {

    void checkResult(Map<String, Boolean> checkedResult);

    void checkCancelResult(String tag);

}
