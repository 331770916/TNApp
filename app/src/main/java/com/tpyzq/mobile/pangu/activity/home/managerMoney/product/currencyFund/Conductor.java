package com.tpyzq.mobile.pangu.activity.home.managerMoney.product.currencyFund;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.ConductorAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetFundManagerConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.GetHistoryFundManagerConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToGetFundManagerConnect;
import com.tpyzq.mobile.pangu.http.doConnect.home.ToGetHistoryFundManagerConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.imageUtil.ImageUtil;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/9/29.
 * 基金经理
 */
public class Conductor extends MoneyFundBaseView implements ICallbackResult {

    private LinearLayout mManagerLayout;
    private RelativeLayout mKongLayout;
    private LinearLayout   mConductorLayout;
    private boolean        hasManager;
    private boolean        hasHistoryManger;
    private Activity         mActivity;
    private ConductorAdapter mAdapter;
    private ImageView        mManagerPic;
    private TextView         mManagerText;
    private Display         mDisplay;

    private static final String TAG = "Conductor";

    public Conductor(Activity activity, Object object) {
        super(activity, object);
    }

    @Override
    public void initView(View view, Activity activity, Object object) {
        mActivity = activity;
        mDisplay = activity.getWindowManager().getDefaultDisplay();

        initManagerModle(view);

        mManagerLayout = (LinearLayout) view.findViewById(R.id.conductorHorizontalLayout);
        mKongLayout = (RelativeLayout) view.findViewById(R.id.rl_conductor_kong);
        mConductorLayout = (LinearLayout) view.findViewById(R.id.conductorLayout);

        MyListView listView = (MyListView) view.findViewById(R.id.conductorListView);
        mAdapter = new ConductorAdapter();
        listView.setAdapter(mAdapter);

        CleverManamgerMoneyEntity entity = (CleverManamgerMoneyEntity) object;

        if (entity != null && !TextUtils.isEmpty(entity.getSECURITYCODE())) {
            SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(this);
            simpleRemoteControl.setCommand(new ToGetFundManagerConnect(new GetFundManagerConnect(TAG, entity.getSECURITYCODE())));
            simpleRemoteControl.startConnect();


            simpleRemoteControl.setCommand(new ToGetHistoryFundManagerConnect(new GetHistoryFundManagerConnect(TAG, entity.getSECURITYCODE())));
            simpleRemoteControl.startConnect();
        }


    }

    private void initManagerModle(View view) {
        mManagerText = (TextView) view.findViewById(R.id.managerContent);
        mManagerPic = (ImageView) view.findViewById(R.id.managerPicture);
    }

    @Override
    public void getResult(Object result, String tag) {

        if ("GetFundManagerConnect".equals(tag)) {
            explantManager(result);
        } else if ("GetHistoryFundManagerConnect".equals(tag)) {
            explantHistoryManager(result);
        }

        if (hasHistoryManger && hasManager) {
            mConductorLayout.setVisibility(View.GONE);
            mKongLayout.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 解析现任经理类
     * @param result
     */
    private void explantManager(Object result) {
        Map<String, Object> datas = (Map<String, Object>) result;

        try {
            if (datas == null) {
                hasManager = true;
                return;
            }

            String code = (String)datas.get("code");
            String msg = (String) datas.get("msg");

            if (!TextUtils.isEmpty(code) && "-1".equals(code)) {
                Helper.getInstance().showToast(CustomApplication.getContext(), msg);
                hasManager = true;
                return;
            }

//            List<Object> data =(List<Object>) datas.get("data");
//
//            if (data == null || data.size() <= 0) {
//                hasManager = true;
//                return;
//            }

            List<Map<String, String>> managerList = (List<Map<String, String>>) datas.get("managerList");

            if (managerList == null || managerList.size() <= 0) {
                hasManager = true;
                return;
            }

            creatManagerTextView(managerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void explantHistoryManager(Object result) {
        Map<String, Object> datas = (Map<String, Object>) result;

        try {
            if (datas == null) {
                hasHistoryManger = true;
                return;
            }

            String code = (String)datas.get("code");
            String msg = (String) datas.get("msg");

            if (!TextUtils.isEmpty(code) && "-1".equals(code)) {
                hasHistoryManger = true;
                Helper.getInstance().showToast(CustomApplication.getContext(), msg);
                return;
            }

//            List<Object> data =(List<Object>) datas.get("data");
//
//            if (data == null || data.size() <= 0) {
//                hasHistoryManger = true;
//                return;
//            }

            List<Map<String, String>> managerList = (List<Map<String, String>>) datas.get("managerList");

            if (managerList == null || managerList.size() <= 0) {
                hasHistoryManger = true;
                return;
            }

            ArrayList<CleverManamgerMoneyEntity> entities = new ArrayList<>();

            for (Map<String, String> map : managerList) {
                CleverManamgerMoneyEntity entity = new CleverManamgerMoneyEntity();
                entity.setStartTime(map.get("startDate"));
                entity.setEndTime(map.get("dimissionDate"));
                entity.setManager(map.get("name"));
                entity.setPeriod(map.get("atDate"));
                entity.setEarn(Helper.fromMateByPersent().format(Double.parseDouble(map.get("performance"))));
                entities.add(entity);
            }

            mAdapter.setDatas(entities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void settingManagerContent(Map<String, Map<String, String>> content, String name) {

        if (content == null) {
            return;
        }

        Map<String, String> managerDeail = content.get(name);

        String background = managerDeail.get("background");
        String image = managerDeail.get("image");

        Bitmap bitmap = Helper.base64ToBitmap(image);

        if (bitmap != null) {
            bitmap = ImageUtil.zoomImg(bitmap,  Helper.dip2px(CustomApplication.getContext(), 70),  Helper.dip2px(CustomApplication.getContext(), 100));
        } else {
            bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.user);
        }

        if (bitmap != null) {
            mManagerPic.setImageBitmap(bitmap);
        }

        mManagerText.setText(background);

    }

    private void creatManagerTextView(final List<Map<String, String>> managerTvs) {

        final Map<String, Map<String, String>> content = new HashMap<>();

        for (int i = 0; i < managerTvs.size(); i++) {
            Map<String, String> subMap = new HashMap<>();
            final TextView textView = new TextView(CustomApplication.getContext());
            textView.setGravity(Gravity.CENTER);


            final String name = managerTvs.get(i).get("name");
            subMap.put("background", managerTvs.get(i).get("background"));
            subMap.put("image", managerTvs.get(i).get("image"));
            subMap.put("managerCode", managerTvs.get(i).get("managerCode"));
            content.put(name, subMap);


            textView.setText(name);
            if (i == 0) {
                textView.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));

                settingManagerContent(content , name);

            } else {
                textView.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.black));
            }
            int mpNum = Helper.dip2px(CustomApplication.getContext(), 10);
            textView.setPadding(mpNum, mpNum, mpNum, mpNum);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            llp.setMargins(mpNum, 0, mpNum, 0);
            textView.setLayoutParams(llp);
            mManagerLayout.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restTextViewColor();
                    textView.setTextColor(ContextCompat.getColor(CustomApplication.getContext(), R.color.blue));
                    settingManagerContent(content , name);
                }
            });
        }
    }

    /**
     * 重置颜色
     */
    private void restTextViewColor() {
        for (int i = 0; i< mManagerLayout.getChildCount(); i++) {
            View view = mManagerLayout.getChildAt(i);

            if (view instanceof  TextView) {
                ((TextView) view).setTextColor(Color.BLACK);
            }
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.view_conductor;
    }

}
