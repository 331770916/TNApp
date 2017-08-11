package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.myself.RiskEvaluationAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.RiskTableEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.listview.NoScrollListView;
import com.tpyzq.mobile.pangu.view.progress.RoundProgressBar;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/9/5.
 * 风险测评
 */
public class RiskEvaluationActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "RiskEvaluationActivity";
    LinearLayout Answer, Anew, Result;
    NoScrollListView mListView;
    Button Affirm, mRestart, mYse, mRestart1;
    List<RiskTableEntity> riskTableBeans;
    RiskEvaluationAdapter riskEvaluationAdapter;
    int list_height = 0;
    int count = 0;
    List<String> mPenalList, mSerialNumber;
//    private boolean isLogin; //作用 当用户做风险测评时候，未做完退出，需要把login至为未登录

    private TextView mType, mDate, mResultTV, mResultTV1, mResulDate1;
    private RoundProgressBar mRoundProgressBar, mRoundProgressBar1;
    private int showResult;
//    private boolean isFinishAnswer = false;

    @Override
    public void initView() {
//        Intent intent = getIntent();
//        isLogin = intent.getBooleanExtra("isLogin", false);

        ResulttoConnect();
        findViewById(R.id.AGpublish_back).setOnClickListener(this);
        Anew = (LinearLayout) findViewById(R.id.Anew);
        Answer = (LinearLayout) findViewById(R.id.Answer);
        Result = (LinearLayout) findViewById(R.id.Result);
        //风险测评表
        mListView = (NoScrollListView) Answer.findViewById(R.id.mListView);
        //提交
        Affirm = (Button) Answer.findViewById(R.id.Affirm);
        Affirm.setOnClickListener(this);
        //风险评测结果
        mType = (TextView) Result.findViewById(R.id.clasTextView);
        mDate = (TextView) Result.findViewById(R.id.Date);
        mRestart = (Button) Result.findViewById(R.id.Restart);
        mYse = (Button) Result.findViewById(R.id.Yse);
        mRestart.setOnClickListener(this);
        mYse.setOnClickListener(this);
        mRoundProgressBar = (RoundProgressBar) findViewById(R.id.RoundProgressBar);
        mRoundProgressBar.setRoundWidth(15);
        mRoundProgressBar1 = (RoundProgressBar) findViewById(R.id.RoundProgressBar1);
        mRoundProgressBar1.setRoundWidth(15);
        //风险等级查询
        mResultTV = (TextView) Anew.findViewById(R.id.clasTextView1);
        mResultTV1 = (TextView) Anew.findViewById(R.id.ResulTextView1);
        mResulDate1 = (TextView) Anew.findViewById(R.id.ResulDate1);
        Anew.findViewById(R.id.lookRiskResultDetail).setOnClickListener(this);
        mRestart1 = (Button) findViewById(R.id.Restart1);
        mRestart1.setOnClickListener(this);
        showResult = getIntent().getIntExtra("showResult",-1);
        initData();
        if (showResult == -1&&Helper.isNeedShowRiskDialog()) {//判断是否展示答题页面
            showAnswer();
        }
    }

    /**
     * 风险测评表
     */
    private void initData() {
        riskTableBeans = new ArrayList<>();
        mPenalList = new ArrayList<>();
        mSerialNumber = new ArrayList();
        riskEvaluationAdapter = new RiskEvaluationAdapter(this);
        mListView.setAdapter(riskEvaluationAdapter);
        //获取Listview的高度
        ViewTreeObserver vto = mListView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                list_height = mListView.getHeight();
                if (list_height>0) {
                    mListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_riskevaluation;
    }

    /**
     * 风险测评表网络请求
     */
    private void toConnect() {
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "300602");
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                CentreToast.showText(RiskEvaluationActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

//
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String data = jsonObject.getString("data");
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        JSONArray jaData = new JSONArray(data);
                        for (int i = 0; i < jaData.length(); i++) {
                            RiskTableEntity riskTableBean = new Gson().fromJson(jaData.getString(i), RiskTableEntity.class);
                            riskTableBeans.add(riskTableBean);
                        }

                        riskEvaluationAdapter.setRiskTableBeans(riskTableBeans);
                        riskEvaluationAdapter.setHeight(list_height);
                        riskEvaluationAdapter.setCallBack(riskChoose);
                        riskEvaluationAdapter.notifyDataSetChanged();
                    } else if ("-6".equals(code)) {
                        Intent intent = new Intent(RiskEvaluationActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    } else {
                        showDialog(msg.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    CentreToast.showText(RiskEvaluationActivity.this, "网络异常");
                }
            }
        });
    }


    /**
     * 提交网络请求
     */
    private void SubmittoConnect() {
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map = new HashMap();
        map.put("funcid", "715273");
        map.put("token", mSession);
        HashMap map1 = new HashMap();
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        HashMap map2 = new HashMap();
        for (int i = 0; i < mPenalList.size(); i++) {
            map2.put(mPenalList.get(i).toString(), mSerialNumber.get(i).toString());
        }
        map1.put("PAPER_ANSWER", map2);
        map.put("parms", map1);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                CentreToast.showText(RiskEvaluationActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.getString("code");
                    String msg = object.getString("msg");
                    JSONArray jsonArray = object.getJSONArray("data");
//                    String data = object.getString("data");
                    if (code.equals("0")) {
//                        JSONArray jsonArray = new JSONArray(data);
//                        RiskSureEntity riskSureBean = new Gson().fromJson(jsonArray.getString(0), RiskSureEntity.class);
//                        String COR = riskSureBean.CORP_RISK_LEVEL;
                        String COR = jsonArray.getJSONObject(0).optString("CORP_RISK_LEVEL");
                        String IS_OUTOFDATE = jsonArray.getJSONObject(0).optString("IS_OUTOFDATE");
                        String corpEndDate = jsonArray.getJSONObject(0).optString("CORP_END_DATE");
                        mDate.setText(corpEndDate);

                        mType.setText(jsonArray.getJSONObject(0).optString("CORP_RISK_LEVEL_NAME"));
                        switch (COR) {
                            case "0":
//                                mType.setText("默认型");
                                setRoundProgressBar(14);
                                break;
                            case "1":
//                                mType.setText("保守型");
                                setRoundProgressBar(28);
                                break;
                            case "2":
//                                mType.setText("相对保守型");
                                setRoundProgressBar(43);
                                break;
                            case "3":
//                                mType.setText("稳健型");
                                setRoundProgressBar(57);
                                break;
                            case "4":
//                                mType.setText("相对积极型");
                                setRoundProgressBar(72);
                                break;
                            case "5":
//                                mType.setText("积极型");
                                setRoundProgressBar(86);
                                break;
                            case "100":
//                                mType.setText("自定义风险等级");
                                setRoundProgressBar(100);
                                break;
                        }


                        SpUtils.putString(RiskEvaluationActivity.this,"IS_OVERDUE", IS_OUTOFDATE.equals("0") ? "0" : "2");
                        SpUtils.putString(RiskEvaluationActivity.this,"CORP_RISK_LEVEL",COR);
                        SpUtils.putString(RiskEvaluationActivity.this,"CORP_END_DATE",corpEndDate);


                    } else if (code.equals("-6")) {
                        Intent intent = new Intent(RiskEvaluationActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    } else {
                        showDialog(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    CentreToast.showText(RiskEvaluationActivity.this, "网络异常");
                }
            }
        });
    }


    /**
     * 风险评测结果
     */
    public void ResulttoConnect() {
        final Dialog mloadingDialog = LoadingDialog.initDialog(this, "正在加载...");
        mloadingDialog.show();
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "300601");
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mloadingDialog != null&&!isFinishing()) {
                    mloadingDialog.dismiss();
                }
                LogHelper.e(TAG, e.toString());
                CentreToast.showText(RiskEvaluationActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                //{"code":"0","msg":"(客户风险等级查询成功)","data":[{"IS_OUTOFDATE":"0","CORP_RISK_VAILD":"0","RISK_LEVEL_NAME":"积极型","CORP_BEGIN_DATE":"20170629","CORP_END_DATE":"20190628","CORP_RISK_LEVEL":"3"}]}

                if (mloadingDialog != null&&!isFinishing()) {
                    mloadingDialog.dismiss();
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("0".equals(code)){
                        JSONArray jsonArray = res.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            for(int i = 0;i < jsonArray.length();i++){
                                JSONObject json = jsonArray.getJSONObject(i);
                                mDate.setText(json.optString("CORP_BEGIN_DATE"));
                                switch (json.optString("CORP_RISK_VAILD")){
                                    case "0":
                                        Anew.setVisibility(View.VISIBLE);
                                        Answer.setVisibility(View.GONE);
                                        Result.setVisibility(View.GONE);
                                        mResultTV.setText(json.optString("RISK_LEVEL_NAME"));
                                        switch (json.optString("CORP_RISK_LEVEL")){
                                            case "0":
//                                                mResultTV.setText("默认型");
                                                setRoundProgressBar(14);
                                                break;
                                            case "1":
//                                                mResultTV.setText("保守型");
                                                setRoundProgressBar(28);
                                                break;
                                            case "2":
//                                                mResultTV.setText("相对保守型");
                                                setRoundProgressBar(43);
                                                break;
                                            case "3":
//                                                mResultTV.setText("稳健型");
                                                setRoundProgressBar(57);
                                                break;
                                            case "4":
//                                                mResultTV.setText("相对积极型");
                                                setRoundProgressBar(72);
                                                break;
                                            case "5":
//                                                mResultTV.setText("积极型");
                                                setRoundProgressBar(86);
                                                break;
                                            case "100":
//                                                mResultTV.setText("自定义风险等级");
                                                setRoundProgressBar(100);
                                                break;
                                        }
                                        switch (json.optString("IS_OUTOFDATE")) {
                                            case "0":
                                                mResultTV1.setText("否");
                                                break;
                                            case "1":
                                                mResultTV1.setText("是");
                                                break;
                                        }
                                        mResulDate1.setText(Helper.getMyDateY_M_D(json.optString("CORP_END_DATE")));
                                        break;
                                    case "1":
                                        Anew.setVisibility(View.GONE);
                                        Answer.setVisibility(View.VISIBLE);
                                        Result.setVisibility(View.GONE);
                                        toConnect();
                                        break;
                                }
                            }
                        }
                    }else if("-6".equals(code)){
                        Intent intent = new Intent(RiskEvaluationActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    }else{
                        showDialog(res.optString("msg"));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    showDialog(e.toString());
                }
                /**
                 Gson gson = new Gson();
                 java.lang.reflect.Type type = new TypeToken<RiskResult>() {
                 }.getType();
                 RiskResult bean = gson.fromJson(response, type);
                 if (bean.getCode().equals("0")) {
                 for (RiskResult.ResultBean _bean : bean.getData()) {

                 if (mloadingDialog != null) {
                 mloadingDialog.dismiss();
                 }
                 mDate.setText(_bean.getCORP_BEGIN_DATE());
                 switch (_bean.getCORP_RISK_VAILD()) {
                 case "0":
                 Anew.setVisibility(View.VISIBLE);
                 Answer.setVisibility(View.GONE);
                 Result.setVisibility(View.GONE);
                 switch (_bean.getCORP_RISK_LEVEL()) {
                 case "0":
                 mResultTV.setText("默认型");
                 setRoundProgressBar(14);
                 break;
                 case "1":
                 mResultTV.setText("保守型");
                 setRoundProgressBar(28);
                 break;
                 case "2":
                 mResultTV.setText("相对保守型");
                 setRoundProgressBar(43);
                 break;
                 case "3":
                 mResultTV.setText("稳健型");
                 setRoundProgressBar(57);
                 break;
                 case "4":
                 mResultTV.setText("相对积极型");
                 setRoundProgressBar(72);
                 break;
                 case "5":
                 mResultTV.setText("积极型");
                 setRoundProgressBar(86);
                 break;
                 case "100":
                 mResultTV.setText("自定义风险等级");
                 setRoundProgressBar(100);
                 break;
                 }
                 switch (_bean.getIS_OUTOFDATE()) {
                 case "0":
                 mResultTV1.setText("否");
                 break;
                 case "1":
                 mResultTV1.setText("是");
                 break;
                 }
                 mResulDate1.setText(Helper.getMyDateY_M_D(_bean.getCORP_END_DATE()));
                 break;
                 case "1":
                 Anew.setVisibility(View.GONE);
                 Answer.setVisibility(View.VISIBLE);
                 Result.setVisibility(View.GONE);
                 toConnect();
                 break;
                 }
                 }
                 } else if (bean.getCode().equals("-6")) {
                 if (mloadingDialog != null) {
                 mloadingDialog.dismiss();
                 }
                 Intent intent = new Intent(RiskEvaluation.this, TransactionLoginActivity.class);
                 startActivity(intent);
                 } else {
                 if (mloadingDialog != null) {
                 mloadingDialog.dismiss();
                 }
                 MistakeDialog.showDialog(bean.getMsg(), RiskEvaluation.this);
                 }
                 */
            }

        });
    }

    /**
     * 百分比设置
     */
    private void setRoundProgressBar(int number) {
        mRoundProgressBar.setProgress(number);
        mRoundProgressBar1.setProgress(number);
    }

    /**
     * 风险测评表 Adapter回调
     */
    RiskEvaluationAdapter.RiskChoose riskChoose = new RiskEvaluationAdapter.RiskChoose() {

        @Override
        public void flag(Boolean flag) {
            count++;
            if (riskTableBeans.size() >= count) {
                mListView.smoothScrollToPosition(count);
            }
        }

        @Override
        public void position(int point, final int position) {
            mPenalList.add(riskTableBeans.get(position).QUESTION_NO);
            if ("0".equals(riskTableBeans.get(position).QUESTION_KIND)) {//单选
                mSerialNumber.add(riskTableBeans.get(position).OPTION_ANSWER.get(point).ANSWER_NO);
            } else {
                mSerialNumber.add("" + point);
            }

            if (riskTableBeans.size() - 1 == position) {

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        if ("1".equals(riskTableBeans.get(position).QUESTION_KIND) && position == riskTableBeans.size() - 1) {
                            Answer.setVisibility(View.GONE);
                            SubmittoConnect();
                            Result.setVisibility(View.VISIBLE);
                        } else {
                            Affirm.setVisibility(View.VISIBLE);
                        }


                    }
                }.execute();
            }


        }
    };

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//
//            if (!isFinishAnswer && isLogin) {
//                UserEntity userEntity=new UserEntity();
//                userEntity.setIslogin("false");
//                Db_PUB_USERS.UpdateIslogin(userEntity);
//            }
//            return super.onKeyDown(keyCode, event);
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AGpublish_back:
//                if (!isFinishAnswer && isLogin) {
//                    UserEntity userEntity=new UserEntity();
//                    userEntity.setIslogin("false");
//                    Db_PUB_USERS.UpdateIslogin(userEntity);
//                }
                finish();
                break;
            case R.id.Affirm:
//                isFinishAnswer = true;
//                ResulttoConnect1();
                Answer.setVisibility(View.GONE);
                SubmittoConnect();
                Result.setVisibility(View.VISIBLE);
                break;
            case R.id.Restart:
                count = 0;
                riskTableBeans.clear();
                mPenalList.clear();
                mSerialNumber.clear();
                riskEvaluationAdapter.notifyDataSetChanged();
                Answer.setVisibility(View.VISIBLE);
                Result.setVisibility(View.GONE);
                Affirm.setVisibility(View.INVISIBLE);
                initData();
                toConnect();
                break;
            case R.id.Yse:
                finish();
                break;
            case R.id.Restart1:
                showAnswer();
                break;
            case R.id.lookRiskResultDetail:
                Intent intent = new Intent();
                intent.setClass(RiskEvaluationActivity.this, RiskTestDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showAnswer() {
        count = 0;
        riskTableBeans.clear();
        mPenalList.clear();
        mSerialNumber.clear();
        Answer.setVisibility(View.VISIBLE);
        Result.setVisibility(View.GONE);
        Anew.setVisibility(View.GONE);
        Affirm.setVisibility(View.INVISIBLE);
        initData();
        toConnect();
    }

    /**
     * 查询时间
     */
    private void ResulttoConnect1() {
        String mSession = SpUtils.getString(this, "mSession", "");
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        map.put("funcid", "300601");
        map.put("token", mSession);
        map.put("parms", map1);
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                CentreToast.showText(RiskEvaluationActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("-6".equals(code)){
                        Intent intent = new Intent(RiskEvaluationActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    }else if("0".equals(code)){
                        JSONObject json = res.getJSONArray("data").getJSONObject(0);
                        mDate.setText(Helper.getMyDateY_M_D(json.optString("CORP_END_DATE")));
                    }else{
                        showDialog(res.optString("msg"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }


                /**
                 Gson gson = new Gson();
                 java.lang.reflect.Type type = new TypeToken<RiskResult>() {
                 }.getType();
                 RiskResult bean = gson.fromJson(response, type);
                 if (bean.getCode().equals("0")) {
                 for (RiskResult.ResultBean _bean : bean.getData()) {
                 mDate.setText(Helper.getMyDateY_M_D(_bean.getCORP_END_DATE()));
                 break;
                 }
                 } else if (bean.getCode().equals("-6")) {
                 Intent intent = new Intent(RiskEvaluation.this, TransactionLoginActivity.class);
                 startActivity(intent);
                 } else {

                 MistakeDialog.showDialog(bean.getMsg(), RiskEvaluation.this);
                 }
                 */
            }

        });
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(getFragmentManager(),RiskEvaluationActivity.class.toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtil.cancelSingleRequestByTag(TAG);
    }
}
