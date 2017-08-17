package com.tpyzq.mobile.pangu.activity.trade.margin_trading;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.view.listview.AutoListview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ltyhome on 14/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:融资融券 资产负债
 */

public class LiabilityActivity extends BaseActivity{
    private TextView line1_tv1,line1_tvtext1,line1_tv2,line1_tvtext2,line1_tv3,line1_tvtext3,line2_tvtext1,line2_tvtext2,line2_tvtext3;
    private String[] mTitle = {"资产信息","融资信息","融券信息"};
    private PagerAdapter pagerAdapter;
    private AutoListview listview;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<View> viewList;

    @Override
    public void initView() {
        View view1 = LayoutInflater.from(this).inflate(R.layout.adapter_liability_vp,null);
        line1_tv1 = (TextView)view1.findViewById(R.id.line1_tv1);
        line1_tvtext1= (TextView)view1.findViewById(R.id.line1_tvtext1);
        line1_tv2= (TextView)view1.findViewById(R.id.line1_tv2);
        line1_tvtext2= (TextView)view1.findViewById(R.id.line1_tvtext2);
        line1_tv3= (TextView)view1.findViewById(R.id.line1_tv3);
        line1_tvtext3= (TextView)view1.findViewById(R.id.line1_tvtext3);
        line2_tvtext1 = (TextView)view1.findViewById(R.id.line2_tvtext1);
        line2_tvtext2 = (TextView)view1.findViewById(R.id.line2_tvtext2);
        line2_tvtext3 = (TextView)view1.findViewById(R.id.line2_tvtext3);
        viewList = new ArrayList<>();
        viewList.add(view1);
        View view2 = LayoutInflater.from(this).inflate(R.layout.adapter_liability_vp,null);
        view2.findViewById(R.id.goneLayout).setVisibility(View.GONE);
        view2.findViewById(R.id.goneLine).setVisibility(View.GONE);
        view2.findViewById(R.id.line1).setPadding(0,0,0,5);
        view2.findViewById(R.id.line2).setPadding(0,0,0,5);
        viewList.add(view2);
        View view3 = LayoutInflater.from(this).inflate(R.layout.adapter_liability_vp,null);
        view3.findViewById(R.id.goneLayout).setVisibility(View.GONE);
        view3.findViewById(R.id.goneLine).setVisibility(View.GONE);
        view3.findViewById(R.id.line1).setPadding(0,0,0,5);
        view3.findViewById(R.id.line2).setPadding(0,0,0,5);
        viewList.add(view3);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v = viewList.get(position);
                ViewGroup parent = (ViewGroup) v.getParent();
                if (parent != null)
                    parent.removeAllViews();
                container.addView(v);
                return v;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle[position];
            }
        };
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_liability;
    }


}
