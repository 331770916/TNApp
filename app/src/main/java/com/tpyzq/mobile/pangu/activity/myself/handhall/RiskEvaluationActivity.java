package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
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
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
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

    private TextView mType, mDate, mResultTV, mResultTV1, mResulDate1, mLookRiskResultDetail;
    private RoundProgressBar mRoundProgressBar, mRoundProgressBar1;

    @Override
    public void initView() {
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
        mLookRiskResultDetail = (TextView) Anew.findViewById(R.id.lookRiskResultDetail);
        mLookRiskResultDetail.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        mLookRiskResultDetail.setOnClickListener(this);
        mRestart1 = (Button) findViewById(R.id.Restart1);
        mRestart1.setOnClickListener(this);
        initData();
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
                mListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                list_height = mListView.getHeight();
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

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(RiskEvaluationActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
//                response = "{\"code\":\"0\",\"msg\":\"(试题查询成功)\",\"data\":[{\"QUESTION_SCORE\":\"9.00\",\"QUESTION_CONTENT\":\"请问您的年龄处于：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"1\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"126\",\"QUESTION_KIND\":\"1\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"30岁以下；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"126\"},{\"ANSWER_CONTENT\":\"31-40岁；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"126\"},{\"ANSWER_CONTENT\":\"41-50岁；\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"126\"},{\"ANSWER_CONTENT\":\"51-60岁；\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"126\"},{\"ANSWER_CONTENT\":\"60岁以上。\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"126\"}]},{\"QUESTION_SCORE\":\"9.00\",\"QUESTION_CONTENT\":\"您家庭预计进行证券投资的资金占家庭现有总资产（不含自住、自用房及汽车等固定资产）的比例是：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"2\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"127\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"70%以上；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"127\"},{\"ANSWER_CONTENT\":\"50%-70%；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"127\"},{\"ANSWER_CONTENT\":\"30%-50%；\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"127\"},{\"ANSWER_CONTENT\":\"10%-30%；\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"127\"},{\"ANSWER_CONTENT\":\"10%以下。\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"127\"}]},{\"QUESTION_SCORE\":\"9.00\",\"QUESTION_CONTENT\":\"进行一项重大投资后，您通常会觉得：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"3\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"128\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"很高兴，对自己的决定很有信心；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"128\"},{\"ANSWER_CONTENT\":\"轻松，基本持乐观态度；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"128\"},{\"ANSWER_CONTENT\":\"基本没什么影响；\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"128\"},{\"ANSWER_CONTENT\":\"比较担心投资结果；\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"128\"},{\"ANSWER_CONTENT\":\"非常担心投资结果。\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"128\"}]},{\"QUESTION_SCORE\":\"7.00\",\"QUESTION_CONTENT\":\"如果您需要把大量现金整天携带在身的话，您是否会感到：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"4\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"129\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"非常焦虑；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"129\"},{\"ANSWER_CONTENT\":\"有点焦虑；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"129\"},{\"ANSWER_CONTENT\":\"完全不会焦虑。\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"129\"}]},{\"QUESTION_SCORE\":\"9.00\",\"QUESTION_CONTENT\":\"当您独自到外地游玩，遇到三岔路口，您会选择：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"5\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"130\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"仔细研究地图和路标；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"130\"},{\"ANSWER_CONTENT\":\"找别人问路；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"130\"},{\"ANSWER_CONTENT\":\"大致判断一下方向；\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"130\"},{\"ANSWER_CONTENT\":\"也许会用掷骰子的方式来做决定。\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"130\"}]},{\"QUESTION_SCORE\":\"9.00\",\"QUESTION_CONTENT\":\"假如有两种不同的投资：投资A预期获得5%的收益，有可能承担非常小的损失；投资B预期获得20%的收益，但是有可能面临25%甚至更高的亏损。您将您的投资资产分配为：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"6\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"131\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"全部投资于A；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"131\"},{\"ANSWER_CONTENT\":\"大部分投资于A；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"131\"},{\"ANSWER_CONTENT\":\"两种投资各一半；\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"131\"},{\"ANSWER_CONTENT\":\"大部分投资于B；\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"131\"},{\"ANSWER_CONTENT\":\"全部投资于B。\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"131\"}]},{\"QUESTION_SCORE\":\"7.00\",\"QUESTION_CONTENT\":\"假如您前期用25元购入一只股票，该股现在升到30元，而根据预测该股近期有一半机会升到35元，另一半机会跌倒25元，您现在会：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"7\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"132\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"立刻卖出；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"132\"},{\"ANSWER_CONTENT\":\"部分卖出；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"132\"},{\"ANSWER_CONTENT\":\"继续持有；\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"132\"},{\"ANSWER_CONTENT\":\"继续买入。\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"132\"}]},{\"QUESTION_SCORE\":\"7.00\",\"QUESTION_CONTENT\":\"同上题情况，该股现在已经跌到20元，而您估计该股近期有一半机会升回25元，另一半机会继续下跌到15元，您现在会：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"8\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"133\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"立刻卖出；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"133\"},{\"ANSWER_CONTENT\":\"部分卖出；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"133\"},{\"ANSWER_CONTENT\":\"继续持有；\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"133\"},{\"ANSWER_CONTENT\":\"继续买入。\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"133\"}]},{\"QUESTION_SCORE\":\"9.00\",\"QUESTION_CONTENT\":\"当您进行投资时，您的首要目标是：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"9\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"134\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"资产保值，我不愿意承担任何投资风险；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"134\"},{\"ANSWER_CONTENT\":\"尽可能保证本金安全，不在乎收益率比较低；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"134\"},{\"ANSWER_CONTENT\":\"产生较多的收益，可以承担一定的投资风险；\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"134\"},{\"ANSWER_CONTENT\":\"实现资产大幅增长，愿意承担很大的投资风险。\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"134\"}]},{\"QUESTION_SCORE\":\"9.00\",\"QUESTION_CONTENT\":\"您的投资经验可以被概括为：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"10\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"135\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"有限：除银行活期账户和定期存款外，我基本没有其他投资经验；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"135\"},{\"ANSWER_CONTENT\":\"一般：除银行活期账户和定期存款外，我购买过基金、保险等理财产品，但还需要进一步的指导；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"135\"},{\"ANSWER_CONTENT\":\"丰富：我是一位有经验的投资者，参与过股票、基金等产品的交易，并倾向于自己做出投资决策；\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"135\"},{\"ANSWER_CONTENT\":\"非常丰富：我是一位非常有经验的投资者，参与过权证、期货或创业板等高风险产品的交易。\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"135\"}]},{\"QUESTION_SCORE\":\"9.00\",\"QUESTION_CONTENT\":\"您是否了解证券市场的相关知识：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"11\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"136\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"从来没有参与过证券交易，对投资知识完全不了解；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"136\"},{\"ANSWER_CONTENT\":\"学习过证券投资知识，但没有实际操作经验，不懂投资技巧；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"136\"},{\"ANSWER_CONTENT\":\"了解证券市场的投资知识，并且有过实际操作经验，懂得一些投资技巧；\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"136\"},{\"ANSWER_CONTENT\":\"参与过多年的证券交易，投资知识丰富，具有一定的专业水平。\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"136\"}]},{\"QUESTION_SCORE\":\"7.00\",\"QUESTION_CONTENT\":\"您用于证券投资的资金不会用作其它用途的时间段为：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"12\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"137\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"短期--0到1年；\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"137\"},{\"ANSWER_CONTENT\":\"中期--1到5年；\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"137\"},{\"ANSWER_CONTENT\":\"长期--5年以上。\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"137\"}]},{\"QUESTION_SCORE\":\"1.00\",\"QUESTION_CONTENT\":\"账户交易总量（万元）？\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"13\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"231\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"< 50\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"231\"},{\"ANSWER_CONTENT\":\">= 50 and < 100\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"231\"},{\"ANSWER_CONTENT\":\">= 100 and < 200\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"231\"},{\"ANSWER_CONTENT\":\">= 200\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"231\"}]},{\"QUESTION_SCORE\":\"1.00\",\"QUESTION_CONTENT\":\"周转率\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"14\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"232\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"< 1\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"232\"},{\"ANSWER_CONTENT\":\">= 1 and < 2\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"232\"},{\"ANSWER_CONTENT\":\">= 2 and < 3\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"232\"},{\"ANSWER_CONTENT\":\">= 3\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"232\"}]},{\"QUESTION_SCORE\":\"1.00\",\"QUESTION_CONTENT\":\"平均仓位\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"15\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"233\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"< 0.2\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"233\"},{\"ANSWER_CONTENT\":\">= 0.2 and < 0.4\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"233\"},{\"ANSWER_CONTENT\":\">= 0.4 and < 0.6\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"233\"},{\"ANSWER_CONTENT\":\">= 0.6\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"233\"}]},{\"QUESTION_SCORE\":\"1.00\",\"QUESTION_CONTENT\":\"投资能力\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"16\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"234\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"< -0.2\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"234\"},{\"ANSWER_CONTENT\":\">= -0.2 and < 0\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"234\"},{\"ANSWER_CONTENT\":\">= 0 and < 0.2\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"234\"},{\"ANSWER_CONTENT\":\">= 0.2\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"234\"}]}]}" ;
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
                        MistakeDialog.showDialog(msg.toString(), RiskEvaluationActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Helper.getInstance().showToast(RiskEvaluationActivity.this, "网络异常");
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

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(RiskEvaluationActivity.this, "网络异常");
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
                        switch (COR) {
                            case "0":
                                mType.setText("默认型");
                                setRoundProgressBar(14);
                                break;
                            case "1":
                                mType.setText("保守型");
                                setRoundProgressBar(28);
                                break;
                            case "2":
                                mType.setText("相对保守型");
                                setRoundProgressBar(43);
                                break;
                            case "3":
                                mType.setText("稳健型");
                                setRoundProgressBar(57);
                                break;
                            case "4":
                                mType.setText("相对积极型");
                                setRoundProgressBar(72);
                                break;
                            case "5":
                                mType.setText("积极型");
                                setRoundProgressBar(86);
                                break;
                            case "100":
                                mType.setText("自定义风险等级");
                                setRoundProgressBar(100);
                                break;
                        }
                    } else if (code.equals("-6")) {
                        Intent intent = new Intent(RiskEvaluationActivity.this, TransactionLoginActivity.class);
                        startActivity(intent);
                    } else {
                        MistakeDialog.showDialog(msg, RiskEvaluationActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Helper.getInstance().showToast(RiskEvaluationActivity.this, "网络异常");
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

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (mloadingDialog != null) {
                    mloadingDialog.dismiss();
                }
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(RiskEvaluationActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                if (mloadingDialog != null) {
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
                                        switch (json.optString("CORP_RISK_LEVEL")){
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
                        MistakeDialog.showDialog(res.optString("msg"), RiskEvaluationActivity.this);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    MistakeDialog.showDialog(e.toString(), RiskEvaluationActivity.this);
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
        public void position(int point, int position) {
            mPenalList.add(riskTableBeans.get(position).QUESTION_NO);
            if (point < 10) {//如果小于10证明客户选择的是单个题 否则是多选题并且做了多个选择
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
                        Affirm.setVisibility(View.VISIBLE);
                    }
                }.execute();
            }


        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AGpublish_back:
                finish();
                break;
            case R.id.Affirm:
                ResulttoConnect1();
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
                break;
            case R.id.lookRiskResultDetail:
                Intent intent = new Intent();
                intent.setClass(RiskEvaluationActivity.this, RiskTestDetailActivity.class);
                startActivity(intent);
                break;
        }
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

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                Helper.getInstance().showToast(RiskEvaluationActivity.this, "网络异常");
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
                        MistakeDialog.showDialog(res.optString("msg"), RiskEvaluationActivity.this);
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
}
