package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.currencyFund;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.GeneralSituationAdapter;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/9/29.
 * 基金概况
 */
public class GeneralSituation extends MoneyFundBaseView {

    private GeneralSituationAdapter mGeneralSituationAdapter;

    public GeneralSituation(Activity activity, Object object) {
        super(activity, object);
    }

    @Override
    public void initView(View view, Activity activity, Object object) {

        CleverManamgerMoneyEntity entity = (CleverManamgerMoneyEntity) object;
        RelativeLayout kongLayout = (RelativeLayout) view.findViewById(R.id.rl_genersituation_kong);


        MyListView listView = (MyListView) view.findViewById(R.id.fundSurveyInfo);
        mGeneralSituationAdapter = new GeneralSituationAdapter();
        listView.setAdapter(mGeneralSituationAdapter);

        ArrayList<CleverManamgerMoneyEntity> datas = initData(entity);
        if (datas == null || datas.size() < 0) {
            kongLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            mGeneralSituationAdapter.setDatas(datas);
        }

    }

    private ArrayList<CleverManamgerMoneyEntity> initData(CleverManamgerMoneyEntity entity) {

        if (entity == null) {
            return null;
        }

        ArrayList<CleverManamgerMoneyEntity> datas = new ArrayList<>();
        String [] title = {"产品代码", "基金公司", "风险等级", "开放状态", "起购金额", "基金类型", "成立时间"};

        String fundCode = "";
        String fundCommpany = "";
        String fundLeavle = "";
        String openState = "";
        String strPrice = "";
        String fundType = "";
        String creatTime = "";

        if (!TextUtils.isEmpty(entity.getSECURITYCODE())) {
            fundCode = entity.getSECURITYCODE();
        }

        if (!TextUtils.isEmpty(entity.getINVESTADVISORNAME())) {
            fundCommpany = entity.getINVESTADVISORNAME();
        }

        if (!TextUtils.isEmpty(entity.getFXDJ())) {
            fundLeavle = entity.getFXDJ();
        }

        if (!TextUtils.isEmpty(entity.getKFZT())) {
            openState = entity.getKFZT();
        }

        if (!TextUtils.isEmpty(entity.getQGJE())) {
            strPrice = entity.getQGJE();
        }

        if (!TextUtils.isEmpty(entity.getFUNDTYPE())) {
            fundType = entity.getFUNDTYPE();
        }

        if (!TextUtils.isEmpty(entity.getESTABLISHMENTDATE())) {
            creatTime = entity.getESTABLISHMENTDATE();
        }



        String [] contents = {fundCode, fundCommpany, fundLeavle, openState, strPrice, fundType, creatTime};

        for (int i = 0; i< title.length; i++) {
            CleverManamgerMoneyEntity tempEntity = new CleverManamgerMoneyEntity();
            tempEntity.setGeneralSituationTitle(title[i]);
            tempEntity.setGeneralSituationContent(contents[i]);
            datas.add(tempEntity);
        }

        return datas;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_genersituation;
    }
}
