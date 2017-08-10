package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.QueryTransferAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.BanksTransferAccountsResultCode;
import com.tpyzq.mobile.pangu.interfac.ITab;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.pickTime.TimePickerView;
import com.tpyzq.mobile.pangu.view.pulllayou.PullLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by zhangwenbo on 2016/8/24.
 * 自定义Tab
 */
public class CustomTab extends BaseTransferObserverTabView implements
        TimePickerView.OnTimeSelectListener, View.OnClickListener, PullLayout.OnPullCallBackListener {

    public CustomTab(Activity activity, ITab iTab, ArrayList<BaseTransferObserverTabView> tabs, BanksTransferAccountsResultCode banksTransferAccountsResultCode) {
        super(activity, iTab, tabs, banksTransferAccountsResultCode);
        mActivity = activity;
        mBanksTransferAccountsResultCode = banksTransferAccountsResultCode;
    }

    private TextView mQueryStartDate;
    private TextView mQueryEndDate;
    private TextView mQueryBtn;
    private TimePickerView mPvTime;
    private boolean mJuedgeTv;
    private SimpleDateFormat mFormate;

    private String mSession;
    private QueryTransferAdapter mAdapter;
    private Activity mActivity;
    private BanksTransferAccountsResultCode mBanksTransferAccountsResultCode;
    public static final String REQUESTCUSTOMTRANSFERRECORD = "requestCustomTransferRecord";
    private PullLayout mPullLayout;
    private String mPageNum = "10";
    private Dialog mLoadingDialg;
    private ImageView mEmptyBg;
    public static final String TAG = "CustomTab";

    @Override
    public void initView(View view, Activity activity) {

        mLoadingDialg = LoadingDialog.initDialog(activity, "正在加载...");

        view.findViewById(R.id.transferCalenderLayout).setVisibility(View.VISIBLE);

        mFormate = new SimpleDateFormat("yyyy-MM-dd");
        mQueryStartDate = (TextView) view.findViewById(R.id.transferQueryStartDate);
        mQueryEndDate = (TextView) view.findViewById(R.id.transferQueryEndDate);
        mQueryBtn = (TextView) view.findViewById(R.id.transferQueryBtn);
        mPullLayout = (PullLayout) view.findViewById(R.id.transferPullLayout);
        mPullLayout.setOnPullListener(this);

        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);

        mEmptyBg = (ImageView) view.findViewById(R.id.iv_allotQueryTab);

        ListView listView = (ListView) view.findViewById(R.id.queryTabsList);
        mAdapter = new QueryTransferAdapter();
        listView.setAdapter(mAdapter);

        mQueryStartDate.setText(mFormate.format(Helper.getBeforeDate()));
        mQueryEndDate.setText(mFormate.format(Helper.getBeforeDate()));

        mQueryStartDate.setOnClickListener(this);
        mQueryBtn.setOnClickListener(this);
        mQueryEndDate.setOnClickListener(this);

        mPvTime = new TimePickerView(activity, TimePickerView.Type.YEAR_MONTH_DAY);
        mPvTime.setTime(Helper.getBeforeDate());
        mPvTime.setCyclic(false);
        mPvTime.setCancelable(true);
        mPvTime.setTitle("选择日期");
        //时间选择后回调
        mPvTime.setOnTimeSelectListener(this);
    }

    @Override
    public void onTimeSelect(Date date) {
        if (mJuedgeTv) {
            mQueryStartDate.setText(mFormate.format(date));
        } else {
            mQueryEndDate.setText(mFormate.format(date));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transferQueryStartDate:

                mJuedgeTv = true;
                mPvTime.show();

                break;
            case R.id.transferQueryEndDate:

                mJuedgeTv = false;
                mPvTime.show();

                break;
            case R.id.transferQueryBtn:

                if (!TextUtils.isEmpty(mQueryStartDate.getText().toString()) && !TextUtils.isEmpty(mQueryEndDate.getText().toString())) {

                    String startDay = Helper.getMyDate(mQueryStartDate.getText().toString());
                    String endDay = Helper.getMyDate(mQueryEndDate.getText().toString());

                    String str = Helper.compareTo(startDay, endDay);

                    int days = Helper.daysBetween(startDay, endDay);

                    if (str.equalsIgnoreCase(startDay) && !str.equals(endDay)) {
                        MistakeDialog.showDialog("起始时间不能大于等于截止时间", mActivity);
                    } else if (days > 90) {
                        MistakeDialog.showDialog("起始时间和截止时间不能大于3个月", mActivity);
                    } else {

                        mEmptyBg.setVisibility(View.GONE);

                        if (mLoadingDialg != null) {
                            mLoadingDialg.show();
                        }

                        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
                        requestCustomTransferRecord(mPageNum, startDay, endDay);
                    }
                }


                break;
        }
    }

    /**
     * 请求自定义转账记录
     */
    private void requestCustomTransferRecord(String num, String startDay, String endDay) {
        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("CURRENCY", "0");
        map2.put("EXT_INST", "");
        map2.put("HIS_TYPE", "0");
        map2.put("BEGIN_DATE", startDay);
        map2.put("END_DATE", endDay);
        map2.put("KEY_STR", "");
        map2.put("REC_COUNT", num);
        map2.put("SERIAL_NO", "");
        map1.put("funcid", "300236");
        map1.put("token", mSession);
        map1.put("parms", map2);

        NetWorkUtil.getInstence().okHttpForPostString(TransferQueryTab.TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogHelper.e(TransferQueryTab.TAG, e.toString());

                        if (mLoadingDialg != null) {
                            mLoadingDialg.dismiss();
                        }

                        if (mPullLayout != null) {
                            mPullLayout.finishPull("网络错误", false);
                        }

                        mEmptyBg.setVisibility(View.VISIBLE);
                        mAdapter.setDatas(null);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (mLoadingDialg != null) {
                            mLoadingDialg.dismiss();
                        }

                        if (mPullLayout != null) {
                            mPullLayout.finishPull();
                        }

                        if (TextUtils.isEmpty(response)) {
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.optString("code");
                            String msg = jsonObject.optString("msg");
                            JSONArray data = jsonObject.optJSONArray("data");
                            if ("0".equals(code)) {
                                ArrayList<BankAccountEntity> beans = new ArrayList<BankAccountEntity>();
                                if (data != null && data.length() == 0) {
                                    mAdapter.setDatas(beans);
                                    mEmptyBg.setVisibility(View.VISIBLE);
                                    return;
                                }
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonData = data.optJSONObject(i);
                                    BankAccountEntity bankAccountEntity = new BankAccountEntity();
                                    bankAccountEntity.setKEY_STR(jsonData.optString("KEY_STR"));
                                    bankAccountEntity.setEXT_ACC(jsonData.optString("EXT_ACC"));
                                    bankAccountEntity.setTRAN_TIME(jsonData.optString("TRAN_TIME"));
                                    bankAccountEntity.setSTATUS(jsonData.optString("STATUS"));
                                    bankAccountEntity.setBANK_NAME(jsonData.optString("BANK_NAME"));
                                    bankAccountEntity.setTRD_DATE(jsonData.optString("TRAN_DATE"));
                                    bankAccountEntity.setBIZ_NAME(jsonData.optString("BIZ_NAME"));
                                    bankAccountEntity.setEXT_RET_CODE(jsonData.optString("EXT_RET_CODE"));
                                    bankAccountEntity.setCURRENCY(jsonData.optString("CURRENCY"));
                                    bankAccountEntity.setEXT_RET_MSG(jsonData.optString("EXT_RET_MSG"));
                                    bankAccountEntity.setSERIAL_NO(jsonData.optString("SERIAL_NO"));
                                    bankAccountEntity.setBIZ_CODE(jsonData.optString("BIZ_CODE"));
                                    bankAccountEntity.setSTATUS_NAME(jsonData.optString("STATUS_NAME"));
                                    bankAccountEntity.setCPTL_AMT(jsonData.optString("CPTL_AMT"));
                                    bankAccountEntity.setEXT_INST(jsonData.optString("EXT_INST"));
                                    beans.add(bankAccountEntity);
                                }
                                mAdapter.setDatas(beans);
                            } else if ("-6".equals(code)) {
                                mBanksTransferAccountsResultCode.getCode("-6", REQUESTCUSTOMTRANSFERRECORD, false);
                            } else {
                                MistakeDialog.showDialog(msg, mActivity, new MistakeDialog.MistakeDialgoListener() {
                                    @Override
                                    public void doPositive() {
                                        mActivity.finish();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//
//                try {
//                    Map<String, Object> result = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//
//                    String code = "";
//                    if (null != result.get("code")) {
//                        code = String.valueOf(result.get("code"));
//                    }
//
//                    String msg = "";
//                    if (null != result.get("msg")) {
//                        msg = String.valueOf(result.get("msg"));
//                    }
//
//                    if (!"0".equals(code)) {
//                        MistakeDialog.showDialog(msg, mActivity, new MistakeDialog.MistakeDialgoListener() {
//                            @Override
//                            public void doPositive() {
//                                mActivity.finish();
//                            }
//                        });
//
//                        return;
//                    }
//
//                    if ("-6".equals(code)) {
//                        mBanksTransferAccountsResultCode.getCode("-6",REQUESTCUSTOMTRANSFERRECORD , false);
//                        return;
//                    }
//
//                Object dataObj = result.get("data");
//
//                if (null == dataObj) {
//                    mEmptyBg.setVisibility(View.VISIBLE);
//                    return;
//                }
//
//                List<Map<String, Object>> data = (List<Map<String, Object>>) dataObj;
//
//                if (data == null || data.size() <= 0) {
//                    mEmptyBg.setVisibility(View.VISIBLE);
//                    return;
//                }
//
//                ArrayList<BankAccountEntity> beans = new ArrayList<BankAccountEntity>();
//
//                for (Map<String, Object> map : data) {
//
//                    BankAccountEntity bankAccountEntity = new BankAccountEntity();
//
//                    String KEY_STR = "";
//                    if (null != map.get("KEY_STR")) {
//                        KEY_STR = String.valueOf(map.get("KEY_STR"));
//                    }
//
//                    bankAccountEntity.setKEY_STR(KEY_STR);
//
//                    String EXT_ACC = "";
//                    if (null != map.get("EXT_ACC")) {
//                        EXT_ACC = String.valueOf(map.get("EXT_ACC"));
//                    }
//
//                    bankAccountEntity.setEXT_ACC(EXT_ACC);
//
//                    String TRAN_TIME = "";
//                    if (null != map.get("TRAN_TIME")) {
//                        TRAN_TIME = String.valueOf(map.get("TRAN_TIME"));
//                    }
//
//                    bankAccountEntity.setTRAN_TIME(TRAN_TIME);
//
//                    String STATUS = "";
//                    if (null != map.get("STATUS")) {
//                        STATUS = String.valueOf(map.get("STATUS"));
//                    }
//
//                    bankAccountEntity.setSTATUS(STATUS);
//
//                    String BANK_NAME = "";
//                    if (null != map.get("BANK_NAME")) {
//                        BANK_NAME = String.valueOf(map.get("BANK_NAME"));
//                    }
//                    bankAccountEntity.setBANK_NAME(BANK_NAME);
//
//                    String TRAN_DATE = "";
//                    if (null != map.get("TRAN_DATE")) {
//                        TRAN_DATE = String.valueOf(map.get("TRAN_DATE"));
//                    }
//
//                    bankAccountEntity.setTRD_DATE(TRAN_DATE);
//
//                    String BIZ_NAME = "";
//                    if (null != map.get("BIZ_NAME")) {
//                        BIZ_NAME = String.valueOf(map.get("BIZ_NAME"));
//                    }
//
//                    bankAccountEntity.setBIZ_NAME(BIZ_NAME);
//
//                    String EXT_RET_CODE = "";
//                    if (null != map.get("EXT_RET_CODE")) {
//                        EXT_RET_CODE = String.valueOf(map.get("EXT_RET_CODE"));
//                    }
//                    bankAccountEntity.setEXT_RET_CODE(EXT_RET_CODE);
//
//                    String CURRENCY = "";
//                    if (null != map.get("CURRENCY")) {
//                        CURRENCY = String.valueOf(map.get("CURRENCY"));
//                    }
//
//                    bankAccountEntity.setCURRENCY(CURRENCY);
//
//                    String EXT_RET_MSG = "";
//                    if (null != map.get("EXT_RET_MSG")) {
//                        EXT_RET_MSG = String.valueOf(map.get("EXT_RET_MSG"));
//                    }
//
//                    bankAccountEntity.setEXT_RET_MSG(EXT_RET_MSG);
//
//                    String SERIAL_NO = "";
//                    if (null != map.get("SERIAL_NO")) {
//                        SERIAL_NO = String.valueOf(map.get("SERIAL_NO"));
//                    }
//
//                    bankAccountEntity.setSERIAL_NO(SERIAL_NO);
//
//                    String BIZ_CODE = "";
//                    if (null != map.get("BIZ_CODE")) {
//                        BIZ_CODE = String.valueOf(map.get("BIZ_CODE"));
//                    }
//
//                    bankAccountEntity.setBIZ_CODE(BIZ_CODE);
//
//                    String EXT_INST = "";
//                    if (null != map.get("EXT_INST")) {
//                        EXT_INST = String.valueOf(map.get("EXT_INST"));
//                    }
//
//                    bankAccountEntity.setEXT_INST(EXT_INST);
//
//                    String STATUS_NAME = "";
//                    if (null != map.get("STATUS_NAME")) {
//                        STATUS_NAME = String.valueOf(map.get("STATUS_NAME"));
//                    }
//
//                    bankAccountEntity.setSTATUS_NAME(STATUS_NAME);
//
//                    String CPTL_AMT = "";
//                    if (null != map.get("CPTL_AMT")) {
//                        CPTL_AMT = String.valueOf(map.get("CPTL_AMT"));
//                    }
//
//                    bankAccountEntity.setCPTL_AMT(CPTL_AMT);
//
//                    beans.add(bankAccountEntity);
//                }
//
//                mAdapter.setDatas(beans);
//
//            }
//
//            catch(
//            Exception e
//            )
//
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    );
//}

    @Override
    public int getTabLayoutId() {
        return R.layout.transfer_query_tabs;
    }

    @Override
    public void onRefresh() {

        String startDay = Helper.getMyDate(mQueryStartDate.getText().toString());
        String endDay = Helper.getMyDate(mQueryEndDate.getText().toString());

        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        requestCustomTransferRecord(mPageNum, startDay, endDay);
    }

    @Override
    public void onLoad() {
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        String startDay = Helper.getMyDate(mQueryStartDate.getText().toString());
        String endDay = Helper.getMyDate(mQueryEndDate.getText().toString());

        mPageNum = String.valueOf((Integer.parseInt(mPageNum) + 10));
        requestCustomTransferRecord(mPageNum, startDay, endDay);
    }
}
