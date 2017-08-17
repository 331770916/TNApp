package com.tpyzq.mobile.pangu.activity.trade.margin_trading;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.view.listview.AutoListview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ltyhome on 14/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:融资融券 资产负债
 */

public class LiabilityActivity extends BaseActivity implements InterfaceCollection.InterfaceCallback{
    private List<Map<String,String>> zcData,rzData,rqData;
    //资产信息
    private TextView line1_tvtext1,line1_tvtext2,line1_tvtext3,line2_tvtext1,line2_tvtext2,line2_tvtext3,
    //融资信息
    rzkyedText,rzyyedText,yzyybzjText,
    //融券信息
    rqedsxText,rqyyedText,rqyybzjText;
    private AutoListview zcListView,rzListView,rqListView;
    private MyAdapter zcAdapter,rzAdapter,rqAdapter;
    private String[] mTitle = {"资产信息","融资信息","融券信息"};
    private LinearLayout.LayoutParams params;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<View> viewList;

    @Override
    public void initView() {
        params = new LinearLayout.LayoutParams(1,-1);
        params.topMargin = 5;
        params.bottomMargin = 5;
        View view1 = LayoutInflater.from(this).inflate(R.layout.adapter_liability_vp,null);
        line1_tvtext1= (TextView)view1.findViewById(R.id.line1_tvtext1);
        line1_tvtext2= (TextView)view1.findViewById(R.id.line1_tvtext2);
        line1_tvtext3= (TextView)view1.findViewById(R.id.line1_tvtext3);
        line2_tvtext1 = (TextView)view1.findViewById(R.id.line2_tvtext1);
        line2_tvtext2 = (TextView)view1.findViewById(R.id.line2_tvtext2);
        line2_tvtext3 = (TextView)view1.findViewById(R.id.line2_tvtext3);
        zcListView = (AutoListview)view1.findViewById(R.id.listview);
        zcAdapter = new MyAdapter(this);
        zcListView.setAdapter(zcAdapter);
        viewList = new ArrayList<>();
        viewList.add(view1);
        line1_tvtext1.setText("-1.0000");
        line1_tvtext2.setText("0.00");
        line1_tvtext3.setText("0.00");
        line2_tvtext1.setText("558.00");
        line2_tvtext2.setText("558.00");
        line2_tvtext3.setText("0.00");
        View view2 = LayoutInflater.from(this).inflate(R.layout.adapter_liability_vp,null);
        ((TextView)view2.findViewById(R.id.line1_tv1)).setText("融资可用额度");
        rzkyedText= (TextView)view2.findViewById(R.id.line1_tvtext1);
        rzkyedText.setTextColor(getResources().getColor(R.color.item_red));
        ((TextView)view2.findViewById(R.id.line1_tv2)).setText("融资已用额度");
        rzkyedText= (TextView)view2.findViewById(R.id.line1_tvtext2);
        ((TextView)view2.findViewById(R.id.line1_tv3)).setText("融资已用保证金");
        yzyybzjText = (TextView)view2.findViewById(R.id.line1_tvtext3);
        rzListView =  (AutoListview)view2.findViewById(R.id.listview);
        rzAdapter = new MyAdapter(this);
        rzListView.setAdapter(rzAdapter);
        view2.findViewById(R.id.goneLayout).setVisibility(View.GONE);
        view2.findViewById(R.id.goneLine).setVisibility(View.GONE);
        view2.findViewById(R.id.line1).setLayoutParams(params);
        view2.findViewById(R.id.line2).setLayoutParams(params);
        viewList.add(view2);
        rzkyedText.setText("350000.00");
        rzyyedText.setText("0.00");
        yzyybzjText.setText("0.00");
        View view3 = LayoutInflater.from(this).inflate(R.layout.adapter_liability_vp,null);
        ((TextView)view3.findViewById(R.id.line1_tv1)).setText("融券额度上限");
        rqedsxText= (TextView)view3.findViewById(R.id.line1_tvtext1);
        rqedsxText.setTextColor(getResources().getColor(R.color.item_red));
        ((TextView)view3.findViewById(R.id.line1_tv2)).setText("融券已用额度");
        rqyyedText= (TextView)view3.findViewById(R.id.line1_tvtext2);
        ((TextView)view3.findViewById(R.id.line1_tv2)).setText("融券已用保证金");
        yzyybzjText= (TextView)view3.findViewById(R.id.line1_tvtext3);
        rqListView =  (AutoListview)view3.findViewById(R.id.listview);
        rqAdapter = new MyAdapter(this);
        rqListView.setAdapter(rqAdapter);
        view3.findViewById(R.id.goneLayout).setVisibility(View.GONE);
        view3.findViewById(R.id.goneLine).setVisibility(View.GONE);
        view3.findViewById(R.id.line1).setLayoutParams(params);
        view3.findViewById(R.id.line2).setLayoutParams(params);
        viewList.add(view3);
        rqedsxText.setText("350000.00");
        rqyyedText.setText("0.00");
        rqyybzjText.setText("0.00");
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
    public void callResult(ResultInfo info) {
        if(info.getCode().equals("")){
            Object object = info.getData();
            if(null!=object&& object instanceof List){
                switch (info.getTag()){
                    case "a":
                        zcData = (List<Map<String,String>>) object;
                        zcAdapter.setData(zcData);
                        break;
                    case "b":
                        rzData = (List<Map<String,String>>) object;
                        rzAdapter.setData(rzData);
                        break;
                    case "c":
                        rqData = (List<Map<String,String>>) object;
                        rqAdapter.setData(rqData);
                        break;
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_liability;
    }

    class MyAdapter extends BaseAdapter{
        private List<Map<String,String>> mList;
        private Context mContext;
        public MyAdapter(Context context) {
            mContext = context;
        }
        @Override
        public int getCount() {
            if (mList != null && mList.size() > 0)
                return mList.size();
            return 0;
        }

        public void setData(List<Map<String,String>> data) {
            mList = data;
            notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {
            if (mList != null && mList.size() > 0)
                return mList.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Map<String,String> map = mList.get(position);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_liability_listitem,null);
            TextView tvTitle = (TextView)convertView.findViewById(R.id.textTitle);
            TextView tvValue = (TextView)convertView.findViewById(R.id.textValue);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                tvTitle.setText(entry.getKey());
                tvValue.setText(entry.getValue());
            }
            return convertView;
        }
    }
}
