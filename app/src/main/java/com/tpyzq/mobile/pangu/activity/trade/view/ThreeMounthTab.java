package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.stock.BanksTransferAccountsActivity;
import com.tpyzq.mobile.pangu.adapter.trade.QueryTransferAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.BankAccountEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.BanksTransferAccountsResultCode;
import com.tpyzq.mobile.pangu.interfac.ITab;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.pulllayou.PullLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by zhangwenbo on 2016/8/24.
 * 三月内
 */
public class ThreeMounthTab extends BaseTransferObserverTabView  implements PullLayout.OnPullCallBackListener{

    public ThreeMounthTab(Activity activity, ITab iTab, ArrayList<BaseTransferObserverTabView> tabs, BanksTransferAccountsResultCode banksTransferAccountsResultCode) {
        super(activity, iTab, tabs, banksTransferAccountsResultCode);
        mActivity = activity;
        mBanksTransferAccountsResultCode = banksTransferAccountsResultCode;
    }

    private String mSession;
    private QueryTransferAdapter mAdapter;
    private Activity mActivity;
    private BanksTransferAccountsResultCode mBanksTransferAccountsResultCode;
    public static final String REQUESTTHREEMOUNTHTRANSFERRECORD = "requestThreeMounthTransferRecord";
    private PullLayout mPullLayout;
    private String mPageNum = "10";
    private ImageView mEmpty;
    private ArrayList<BankAccountEntity> mBeans;
    public static final String TAG = "ThreeMounthTab";

    @Override
    public void initView(View view, Activity activity) {
        mActivity = activity;
        mBeans = new ArrayList<>();
        mEmpty = (ImageView) view.findViewById(R.id.iv_allotQueryTab);

        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        mPullLayout =  (PullLayout) view.findViewById(R.id.transferPullLayout);
        ListView listView = (ListView) view.findViewById(R.id.queryTabsList);
        mAdapter = new QueryTransferAdapter();
        listView.setAdapter(mAdapter);
        mPullLayout.setOnPullListener(this);
    }


    @Override
    public void initToConnect() {
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        requestThreeMounthTransferRecord(mPageNum);
    }

    @Override
    public void toRunConnect() {
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        requestThreeMounthTransferRecord(mPageNum);
    }

    @Override
    public void toStopConnect() {
        NetWorkUtil.cancelSingleRequestByTag(TAG);
    }

    @Override
    public void update(Object object, boolean isMaintab, int position) {
        mSession = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
        requestThreeMounthTransferRecord(mPageNum);
    }

    /**
     * 请求三月月转账记录
     */
    private void requestThreeMounthTransferRecord(String num) {

        ((BanksTransferAccountsActivity)mActivity).loadingProgress();
        mEmpty.setVisibility(View.GONE);

        Map map1 = new HashMap<>();
        Map map2 = new HashMap<>();

        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG", "true");
        map2.put("CURRENCY","0");
        map2.put("EXT_INST","");
        map2.put("HIS_TYPE","3");
        map2.put("BEGIN_DATE", "");
        map2.put("END_DATE", "");
        map2.put("KEY_STR","");
        map2.put("REC_COUNT",num);
        map2.put("SERIAL_NO","");
        map1.put("funcid","300236");
        map1.put("token",mSession);
        map1.put("parms",map2);

        NetWorkUtil.getInstence().okHttpForPostString(TransferQueryTab.TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                if (mPullLayout != null) {
                    mPullLayout.finishPull("网络错误", false);
                }

                ((BanksTransferAccountsActivity)mActivity).complitedProgress();
                if (mBeans == null || mBeans.size() <= 0) {
                    mEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                ((BanksTransferAccountsActivity)mActivity).complitedProgress();

                if (TextUtils.isEmpty(response)) {
                    return ;
                }

                if (mPullLayout != null) {
                    mPullLayout.finishPull();
                }

//                response = "{\"code\":\"0\",\"msg\":\"(当日转账查询成功)\",\"data\":[{\"KEY_STR\":\"2016083014244522220000000002\",\"EXT_ACC\":\"39424143\",\"TRAN_TIME\":\"142445\",\"STATUS\":\"2\",\"BANK_NAME\":\"农行存管\",\"TRD_DATE\":\"20160830\",\"BIZ_NAME\":\"银行转存\",\"EXT_RET_CODE\":\"0\",\"CURRENCY\":\"0\",\"EXT_RET_MSG\":\"交易成功\",\"SERIAL_NO\":\"2\",\"BIZ_CODE\":\"1\",\"EXT_INST\":\"2\",\"CPTL_AMT\":\"10000000.00\"},{\"KEY_STR\":\"2016083014244822220000000003\",\"EXT_ACC\":\"39424143\",\"TRAN_TIME\":\"142448\",\"STATUS\":\"2\",\"BANK_NAME\":\"农行存管\",\"TRD_DATE\":\"20160830\",\"BIZ_NAME\":\"银行转存\",\"EXT_RET_CODE\":\"0\",\"CURRENCY\":\"0\",\"EXT_RET_MSG\":\"交易成功\",\"SERIAL_NO\":\"3\",\"BIZ_CODE\":\"1\",\"EXT_INST\":\"2\",\"CPTL_AMT\":\"10000000.00\"}]}";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    JSONArray data = jsonObject.getJSONArray("data");
                    if ("-6".equals(code)) {
                        mBanksTransferAccountsResultCode.getCode("-6", REQUESTTHREEMOUNTHTRANSFERRECORD, false);
                        return;
                    }
                    if (!"0".equals(code)) {
                        mEmpty.setVisibility(View.VISIBLE);
                        final CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
                        customCenterDialog.show(mActivity.getFragmentManager(),ThreeMounthTab.class.toString());
                        customCenterDialog.setOnClickListener(new CustomCenterDialog.ConfirmOnClick() {
                            @Override
                            public void confirmOnclick() {
                                customCenterDialog.dismiss();
                                mActivity.finish();
                            }
                        });

                        return;
                    }
                    if (data != null && data.length() > 0) {
                        ArrayList<BankAccountEntity> beans = new ArrayList<BankAccountEntity>();
                        for (int i = 0; i < data.length(); i++) {
                            BankAccountEntity entity = new BankAccountEntity();
                            entity.setTRD_DATE(data.getJSONObject(i).getString("TRAN_DATE"));
                            entity.setTRAN_TIME(data.getJSONObject(i).getString("TRAN_TIME"));
                            entity.setEXT_INST(data.getJSONObject(i).getString("EXT_INST"));
                            entity.setEXT_ACC(data.getJSONObject(i).getString("EXT_ACC"));
                            entity.setCURRENCY(data.getJSONObject(i).getString("CURRENCY"));
                            entity.setBANK_NAME(data.getJSONObject(i).getString("BANK_NAME"));
                            entity.setBIZ_NAME(data.getJSONObject(i).getString("BIZ_NAME"));
                            entity.setBIZ_CODE(data.getJSONObject(i).getString("BIZ_CODE"));
                            entity.setCPTL_AMT(data.getJSONObject(i).getString("CPTL_AMT"));
                            entity.setSTATUS(data.getJSONObject(i).getString("STATUS"));
                            entity.setSERIAL_NO(data.getJSONObject(i).getString("SERIAL_NO"));
                            entity.setKEY_STR(data.getJSONObject(i).getString("KEY_STR"));
                            entity.setEXT_RET_CODE(data.getJSONObject(i).getString("EXT_RET_CODE"));
                            entity.setEXT_RET_MSG(data.getJSONObject(i).getString("EXT_RET_MSG"));
                            entity.setSTATUS_NAME(data.getJSONObject(i).getString("STATUS_NAME"));
                            beans.add(entity);
                        }
                        mBeans = beans;
                        mAdapter.setDatas(beans);
                    }else {
                        mEmpty.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    if (mPullLayout != null) {
                        mPullLayout.finishPull("网络错误", false);
                    }

                    ((BanksTransferAccountsActivity) mActivity).complitedProgress();

                    if (mBeans == null || mBeans.size() <= 0) {
                        mEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public int getTabLayoutId() {
        return R.layout.transfer_query_tabs;
    }

    @Override
    public void onRefresh() {
        requestThreeMounthTransferRecord(mPageNum);
    }

    @Override
    public void onLoad() {
        mPageNum = String.valueOf((Integer.parseInt(mPageNum) + 10));
        requestThreeMounthTransferRecord(mPageNum);
    }
}
