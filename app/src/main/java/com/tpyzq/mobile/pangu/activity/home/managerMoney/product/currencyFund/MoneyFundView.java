package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.currencyFund;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.BaseProductView;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.MoneyFundChartView;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.view.chartView.MoneyFundFundChartView;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.tabLineIndicator.TabLineDisClickIndicator;


/**
 * Created by zhangwenbo on 2016/9/24.
 * 货币基金
 */
public class MoneyFundView extends BaseProductView {

    private RelativeLayout mChartLayout;
    private RelativeLayout mBaseInfoLayout;
    private MoneyFundChartView mChartView;
    private MoneyFundFundChartView mMistakeChartView;
    private MoneyFundFundHistoryValue mHistoryValueView;
    private LinearLayout.LayoutParams mChartLp;
    private LinearLayout.LayoutParams mListViewLp;

    private GeneralSituation mGeneralSituationView; //基金概况
    private Conductor mConductorView;               //基金经理
    private WareHouseView mWareHouseView;           //重仓
    private AnnounceMent mAnnounceMentView;        //公告
    private Activity mActivity;
    private String mProductType;
    private String mProductCode;

    private final String TAG = "MoneyFundView";

    public MoneyFundView(Activity activity, int type, Object object) {
        super(activity, type, object);
    }

    @Override
    public void initView(View view, Activity activity, int type, Object object) {
        mActivity = activity;
        CleverManamgerMoneyEntity entity = (CleverManamgerMoneyEntity) object;

        if (entity != null) {
            mProductCode = entity.getPRODCODE();
            mProductType = entity.getTYPE();
        }

        mChartLayout = (RelativeLayout) view.findViewById(R.id.moneyFundChartLayout);

        TabLineDisClickIndicator tabLineDisClickIndicator = (TabLineDisClickIndicator) view.findViewById(R.id.moneyFundDisClickIndicator);
        TabLineDisClickIndicator tabLineDisClickIndicator2 = (TabLineDisClickIndicator) view.findViewById(R.id.moneyFundDisClickIndicator2);

        if (type == ConstantUtil.FUND_TYPE) {
            String [] strings = {"七日年化收益率", "历史净值"};
            tabLineDisClickIndicator.setTitles(strings);
        } else if (type == ConstantUtil.FUND_MISTAKETYPE) {
            String [] strings = {"净值走势", "历史净值"};
            tabLineDisClickIndicator.setTitles(strings);
        }

        tabLineDisClickIndicator.setOnTitleClickListener(new TabLineDisClickIndicator.OnTitleClickListener() {
            @Override
            public void click(int position) {
                switch (position) {
                    case 0:

                        mChartLayout.removeAllViews();

                        if (mChartView != null) {
                            mChartLayout.addView(mChartView.getContentView());
                        }

                        if (mMistakeChartView != null) {
                            mChartLayout.addView(mMistakeChartView.getContentView());
                        }

                        mChartLayout.setLayoutParams(mChartLp);
                        break;
                    case 1:

                        mChartLayout.removeAllViews();

                        mChartLayout.addView(mHistoryValueView.getContentView());

                        mChartLayout.setLayoutParams(mListViewLp);
                        break;
                }
            }
        });

        tabLineDisClickIndicator2.setTitles(new String[] {"基金概况", "基金经理", "公告"});
//        tabLineDisClickIndicator2.setTitles(new String[] {"基金概况", "基金经理", "重仓", "公告"});
        tabLineDisClickIndicator2.setOnTitleClickListener(new TabLineDisClickIndicator.OnTitleClickListener() {
            @Override
            public void click(int position) {
                switch (position) {
                    case 0:
                        mBaseInfoLayout.removeAllViews();
                        mBaseInfoLayout.addView(mGeneralSituationView.getContentView());
                        break;
                    case 1:
                        mBaseInfoLayout.removeAllViews();
                        mBaseInfoLayout.addView(mConductorView.getContentView());
                        break;
//                    case 2:
//                        mBaseInfoLayout.removeAllViews();
//                        mBaseInfoLayout.addView(mWareHouseView.getContentView());
//                        break;
                    case 2:
                        mBaseInfoLayout.removeAllViews();
                        mBaseInfoLayout.addView(mAnnounceMentView.getContentView());
                        break;
                }
            }
        });


        mBaseInfoLayout = (RelativeLayout) view.findViewById(R.id.moneyFundBaseInfo);

        if (type == ConstantUtil.FUND_TYPE) {
            mChartView = new MoneyFundChartView(activity, entity);
        } else if (type == ConstantUtil.FUND_MISTAKETYPE) {
            mMistakeChartView = new MoneyFundFundChartView(activity, entity);
        }


        mHistoryValueView = new MoneyFundFundHistoryValue(activity, entity);

        mGeneralSituationView = new GeneralSituation(activity, entity);
        mConductorView = new Conductor(activity, entity);
        mWareHouseView = new WareHouseView(activity, entity);
        mAnnounceMentView = new AnnounceMent(activity, entity);


        mChartLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Helper.dip2px(CustomApplication.getContext(), 200));
        mListViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (type == ConstantUtil.FUND_TYPE) {
            mChartLayout.addView(mChartView.getContentView());
        } else if (type == ConstantUtil.FUND_MISTAKETYPE) {
            mMistakeChartView = new MoneyFundFundChartView(activity, entity);
            mChartLayout.addView(mMistakeChartView.getContentView());
        }

        mChartLayout.setLayoutParams(mChartLp);

        mBaseInfoLayout.addView(mGeneralSituationView.getContentView());
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_manager_money_fund;
    }
}
