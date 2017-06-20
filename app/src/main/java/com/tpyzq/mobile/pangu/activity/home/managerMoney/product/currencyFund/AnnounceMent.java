package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.currencyFund;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.detail.newsTab.AnnouncementStydyDetailActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.AnnounceMentAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.http.doConnect.home.AnnounceMentConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToAnnounceMentConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/9/29.
 * 公告
 */
public class AnnounceMent extends MoneyFundBaseView implements ICallbackResult {

    private AnnounceMentAdapter mAdapter;
    private static final String TAG = "AnnounceMent";
    private ArrayList<CleverManamgerMoneyEntity> mEntities;
    private RelativeLayout mKongLayout;
    private MyListView mListView;

    public AnnounceMent(Activity activity, Object object) {
        super(activity, object);
    }

    @Override
    public void initView(View view, final Activity activity, Object object) {
        mKongLayout = (RelativeLayout) view.findViewById(R.id.rl_announcement_kong);
        mListView = (MyListView) view.findViewById(R.id.announcementListView);
        mAdapter = new AnnounceMentAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (mEntities != null && mEntities.size() > 0) {
                    CleverManamgerMoneyEntity bean = mEntities.get(position);
                    String msgId = bean.getAnnounceId();
                    intent.putExtra("msgId",msgId);
                    bean.getOtcNoticeDate();
                    intent.putExtra("type",1);
                }
                intent.setClass(activity,AnnouncementStydyDetailActivity.class);
                activity.startActivity(intent);
            }
        });

        CleverManamgerMoneyEntity entity = (CleverManamgerMoneyEntity) object;

        if (entity != null && !TextUtils.isEmpty(entity.getSECURITYCODE())) {
            SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
            simpleRemoteControl.setCommand(new ToAnnounceMentConnect(new AnnounceMentConnect(TAG, entity.getSECURITYCODE())));
            simpleRemoteControl.startConnect();
        }
    }

    @Override
    public void getResult(Object result, String tag) {

        Map<String, Object> datas = (Map<String, Object>) result;

        try {

            if (datas == null) {

                mKongLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);

                return;
            }

            String code = (String)datas.get("code");
            String msg = (String) datas.get("msg");

            if (!TextUtils.isEmpty(code) && "-1".equals(code)) {
                mKongLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                Helper.getInstance().showToast(CustomApplication.getContext(), msg);
                return;
            }

            ArrayList<ArrayList<String>> itemsData =(ArrayList<ArrayList<String>>) datas.get("data");

            if (itemsData == null || itemsData.size() <= 0) {
                mKongLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                return;
            }

            ArrayList<CleverManamgerMoneyEntity> entities = new ArrayList<>();
            for (ArrayList<String> items : itemsData) {
                CleverManamgerMoneyEntity entity = new CleverManamgerMoneyEntity();

                entity.setAnnounceId(items.get(0));
                entity.setAnnounceTitle(items.get(1));
                entity.setAnnounceFrom(items.get(7));
                entity.setAnnounceDate(items.get(19));


//                for (int i = 0; i< items.size(); i++){
//
//                }

                entities.add(entity);
            }
            mEntities = entities;
            mAdapter.setDatas(entities);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_announcement;
    }
}
