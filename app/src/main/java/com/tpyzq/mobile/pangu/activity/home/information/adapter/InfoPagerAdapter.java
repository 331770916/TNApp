package com.tpyzq.mobile.pangu.activity.home.information.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.tpyzq.mobile.pangu.activity.home.information.view.BaseInfoPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.CommercePager;
import com.tpyzq.mobile.pangu.activity.home.information.view.ConsumePager;
import com.tpyzq.mobile.pangu.activity.home.information.view.EnergyPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.FinancialPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.HotPintsPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.IndustryPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.InformationPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.MaterialsPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.MedicalPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.PublicUtilitiesPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.RealtyPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.SopCastPager;
import com.tpyzq.mobile.pangu.activity.home.information.view.TelecomPager;

import java.util.List;

/**
 * Created by hackware on 2016/9/10.
 */

public class InfoPagerAdapter extends PagerAdapter {
    private List<String> mDataList;
    private List<BaseInfoPager> pagers;
    private Context context ;
    private int point = 1;

    public InfoPagerAdapter(List<String> dataList,Context context) {
        mDataList = dataList;
        this.context = context;
    }
    public void setBaseInfoPager(List<BaseInfoPager> pagers) {
        this.pagers = pagers;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        BaseInfoPager pager = new SopCastPager(context);
        switch (mDataList.get(position)){
            case "热点":
                pager = (BaseInfoPager) new HotPintsPager(context);
                break;
            case "要闻直播":
                pager = (BaseInfoPager) new SopCastPager(context);
                break;
            case "金融业":
                pager = (BaseInfoPager) new FinancialPager(context);
                break;
            case "房地产业":
                pager = (BaseInfoPager) new RealtyPager(context);
                break;
            case "信息技术":
                pager = (BaseInfoPager) new InformationPager(context);
                break;
            case "电信":
                pager = (BaseInfoPager) new TelecomPager(context);
                break;
            case "医疗":
                pager = (BaseInfoPager) new MedicalPager(context);
                break;
            case "工业":
                pager = (BaseInfoPager) new IndustryPager(context);
                break;
            case "能源":
                pager = (BaseInfoPager) new EnergyPager(context);
                break;
            case "公共事业":
                pager = (BaseInfoPager) new PublicUtilitiesPager(context);
                break;
            case "消费":
                pager = (BaseInfoPager) new ConsumePager(context);
                break;
            case "原材料":
                pager = (BaseInfoPager) new MaterialsPager(context);
                break;
            case "商贸":
                pager = (BaseInfoPager) new CommercePager(context);
                break;
        }
        pager.initData();
        container.addView(pager.rootView);
        return pager.rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position);
    }
}
