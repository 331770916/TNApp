package com.tpyzq.mobile.pangu.activity.home;


import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.LocalImageHolderView;

/**
 * Created by zhangwenbo on 2016/9/17.
 */
public class BanderHolderCreator implements CBViewHolderCreator<LocalImageHolderView> {

    @Override
    public LocalImageHolderView createHolder() {
        return new LocalImageHolderView();
    }
}
