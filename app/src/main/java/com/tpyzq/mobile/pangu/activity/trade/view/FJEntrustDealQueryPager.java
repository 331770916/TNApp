package com.tpyzq.mobile.pangu.activity.trade.view;

import android.content.Context;
import com.tpyzq.mobile.pangu.base.BasePager;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.log.LogUtil;

/**
 * Created by ltyhome on 23/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: entrust deal query 委托 成交查询
 */
public class FJEntrustDealQueryPager extends BasePager implements InterfaceCollection.InterfaceCallback{
    private String TAG ;
    public FJEntrustDealQueryPager(Context context,String params) {
        super(context,params);
        TAG = params;
    }

    @Override
    public void setView(String params) {

    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    @Override
    public void callResult(ResultInfo info) {
        String code = info.getCode();
        if(code.equals("0")){
            LogUtil.e("--------Success-----");
        }else{
            LogUtil.e("--------Fail------");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.page_fj_entrustquery_today;
    }

    @Override
    public void initData() {
        ifc.setInterfaceCallback(this);
        ifc.getData(20);
    }

    @Override
    public void destroy() {

    }
}
