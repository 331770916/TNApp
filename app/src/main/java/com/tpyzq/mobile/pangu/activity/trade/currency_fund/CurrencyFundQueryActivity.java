package com.tpyzq.mobile.pangu.activity.trade.currency_fund;

import android.animation.ValueAnimator;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.view.BasePager;
import com.tpyzq.mobile.pangu.activity.trade.view.CurrencyOneMonthPager;
import com.tpyzq.mobile.pangu.activity.trade.view.CurrencyOneWeekPager;
import com.tpyzq.mobile.pangu.activity.trade.view.CurrencyThreeMonthPager;
import com.tpyzq.mobile.pangu.activity.trade.view.CurrencyTodayPager;
import com.tpyzq.mobile.pangu.activity.trade.view.CurrencyZiDingYiPager;
import com.tpyzq.mobile.pangu.adapter.trade.HBJJQueryAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 刘泽鹏
 * 货币基金委托查询界面
 */
public class CurrencyFundQueryActivity extends BaseActivity implements View.OnClickListener {

    private RadioButton[] dataChoose = new RadioButton[5];      //存储radioButton的集合
    private ViewPager mViewPager;
    private RadioGroup wt_choose;
    private int[] wtId;
    private int position = 0;
    private ImageView wt_buttom;
    private List<BasePager> pagers=new ArrayList<BasePager>();      //存储 View 的集合

    @Override
    public void initView() {
        wtId = new int[]{R.id.rb_jinri, R.id.rb_yizhou, R.id.rb_yiyue, R.id.rb_sanyue,R.id.rb_zidingyi};

        wt_choose = (RadioGroup) findViewById(R.id.rgTime_choose);      //radioGroup

        //把radioButton 存入集合
        dataChoose[0] = (RadioButton) findViewById(R.id.rb_jinri);
        dataChoose[1] = (RadioButton) findViewById(R.id.rb_yizhou);
        dataChoose[2]   = (RadioButton) findViewById(R.id.rb_yiyue);
        dataChoose[3]  = (RadioButton) findViewById(R.id.rb_sanyue);
        dataChoose[4]= (RadioButton) findViewById(R.id.rb_zidingyi);

        mViewPager = (ViewPager) findViewById(R.id.vpHBJJEntrustQuery);
        wt_buttom = (ImageView) findViewById(R.id.ivTab_buttom);            //下划线
        findViewById(R.id.ivHBJJWTCX_back).setOnClickListener(this);
        initData();



        pagers.add(new CurrencyTodayPager(this));
        pagers.add(new CurrencyOneWeekPager(this));
        pagers.add(new CurrencyOneMonthPager(this));
        pagers.add(new CurrencyThreeMonthPager(this));
        pagers.add(new CurrencyZiDingYiPager(this));


        mViewPager.setAdapter(new HBJJQueryAdapter(pagers));
    }


    private void initData() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setRadioGroupListener();
            }

            @Override
            public void onPageSelected(int position) {
                pagers.get(position);
                dataChoose[position].setChecked(true);
                intTabLine(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setRadioGroupListener() {
        wt_choose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < wtId.length; i++) {
                    if (wtId[i] == checkedId) {
                        mViewPager.setCurrentItem(i);
                        break;
                    }
                }
            }
        });
    }

    /**
     * RadioButton下部蓝色小横线平移属性动画
     *
     * @param i
     */
    private void intTabLine(int i) {
        ValueAnimator animator = ValueAnimator.ofFloat(position, i * (wt_buttom.getWidth() + TransitionUtils.dp2px(22, this)));
        animator.setTarget(wt_buttom);
        animator.setDuration(200).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                wt_buttom.setTranslationX((Float) animation.getAnimatedValue());
            }

        });
        position = i * (wt_buttom.getWidth() + TransitionUtils.dp2px(24, this));
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_currency_fund_query;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.ivHBJJWTCX_back){
            finish();
        }
    }
}
