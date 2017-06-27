package com.tpyzq.mobile.pangu.base;

import android.content.Context;
import android.view.View;

import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SkipUtils;

import java.text.SimpleDateFormat;

/**
 * Created by ltyhome on 22/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: delet repat interface
 */

public abstract class BasePager {
    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    protected Context mContext;
    public InterfaceCollection ifc;
    public View rootView;
    public Helper helper;
    public SkipUtils skip;
    public NetWorkUtil net;
    public String mSession;
    public int type;

    public BasePager(Context context, String params){
        this.mContext = context;
        rootView = View.inflate(mContext, getLayoutId(), null);
        mSession = SpUtils.getString(mContext, "mSession", "");
        ifc = InterfaceCollection.getInstance();
        net = NetWorkUtil.getInstence();
        skip = SkipUtils.getInstance();
        helper = Helper.getInstance();
        setView(params);
    }

    public void setType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public abstract void setView(String params);
    public abstract  void initData();
    public abstract int getLayoutId();
    public abstract void destroy();
}

