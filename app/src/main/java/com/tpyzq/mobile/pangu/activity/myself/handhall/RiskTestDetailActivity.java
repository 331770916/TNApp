package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.myself.RiskTestDetailAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/6/27.
 * 风险承受能力测评详情查看界面
 */

public class RiskTestDetailActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnCancelListener{

    private final String TAG =  RiskTestDetailActivity.class.getSimpleName();
    private RiskTestDetailAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private List<Map<String, Object>> mDatas;

    @Override
    public void initView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("查看问卷详情");
        findViewById(R.id.userIdBackBtn).setOnClickListener(this);
        ListView listView = (ListView)findViewById(R.id.lv_riskTestDetile);
        mAdapter = new RiskTestDetailAdapter(RiskTestDetailActivity.this);
        listView.setAdapter(mAdapter);
        mDatas = new ArrayList<>();

        getTestCheckDetailResult();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userIdBackBtn:
                finish();
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_risktest_detail;
    }


    private void getTestCheckDetailResult() {
        String session = SpUtils.getString(this, "mSession", "");
        initLoadDialog();
        HashMap map = new HashMap();
        map.put("funcid", "731013");
        map.put("token", session);
        HashMap map1 = new HashMap();
        map1.put("PAPER_TYPE", "1");
        map1.put("PRODTA_NO", "");
        map1.put("POSITION_STR", "");
        map1.put("REQUEST_NUM", "");
        map1.put("SEC_ID", "tpyzq");
        map1.put("FLAG", "true");
        map.put("parms", map1);

        OkHttpUtil.okHttpForPostString(TAG, ConstantUtil.URL_JY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                showMistackDialog(ConstantUtil.NETWORK_ERROR, null);

                if (mProgressDialog!= null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
            }

            @Override
            public void onResponse(String response, int id) {

                if (mProgressDialog!= null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
//{"code":"0","msg":"(适当性管理答卷记录查询成功！)","data":[{"QUESTION_SCORE":"9.00","QUESTION_CONTENT":"请问您的年龄处于：","QUESTION_TYPE":"1","ORDER_NO":"1","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"126","ANSWER_HISTORY":"1","QUESTION_KIND":"1","OPTION_ANSWER":[{"ANSWER_CONTENT":"30岁以下；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"126"},{"ANSWER_CONTENT":"31-40岁；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"126"},{"ANSWER_CONTENT":"41-50岁；","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"126"},{"ANSWER_CONTENT":"51-60岁；","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"126"},{"ANSWER_CONTENT":"60岁以上。","ANSWER_NO":"5","REMARK":" ","QUESTION_NO":"126"}]},{"QUESTION_SCORE":"9.00","QUESTION_CONTENT":"您家庭预计进行证券投资的资金占家庭现有总资产（不含自住、自用房及汽车等固定资产）的比例是：","QUESTION_TYPE":"1","ORDER_NO":"2","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"127","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"70%以上；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"127"},{"ANSWER_CONTENT":"50%-70%；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"127"},{"ANSWER_CONTENT":"30%-50%；","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"127"},{"ANSWER_CONTENT":"10%-30%；","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"127"},{"ANSWER_CONTENT":"10%以下。","ANSWER_NO":"5","REMARK":" ","QUESTION_NO":"127"}]},{"QUESTION_SCORE":"9.00","QUESTION_CONTENT":"进行一项重大投资后，您通常会觉得：","QUESTION_TYPE":"1","ORDER_NO":"3","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"128","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"很高兴，对自己的决定很有信心；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"128"},{"ANSWER_CONTENT":"轻松，基本持乐观态度；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"128"},{"ANSWER_CONTENT":"基本没什么影响；","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"128"},{"ANSWER_CONTENT":"比较担心投资结果；","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"128"},{"ANSWER_CONTENT":"非常担心投资结果。","ANSWER_NO":"5","REMARK":" ","QUESTION_NO":"128"}]},{"QUESTION_SCORE":"7.00","QUESTION_CONTENT":"如果您需要把大量现金整天携带在身的话，您是否会感到：","QUESTION_TYPE":"1","ORDER_NO":"4","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"129","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"非常焦虑；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"129"},{"ANSWER_CONTENT":"有点焦虑；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"129"},{"ANSWER_CONTENT":"完全不会焦虑。","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"129"}]},{"QUESTION_SCORE":"9.00","QUESTION_CONTENT":"当您独自到外地游玩，遇到三岔路口，您会选择：","QUESTION_TYPE":"1","ORDER_NO":"5","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"130","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"仔细研究地图和路标；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"130"},{"ANSWER_CONTENT":"找别人问路；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"130"},{"ANSWER_CONTENT":"大致判断一下方向；","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"130"},{"ANSWER_CONTENT":"也许会用掷骰子的方式来做决定。","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"130"}]},{"QUESTION_SCORE":"9.00","QUESTION_CONTENT":"假如有两种不同的投资：投资A预期获得5%的收益，有可能承担非常小的损失；投资B预期获得20%的收益，但是有可能面临25%甚至更高的亏损。您将您的投资资产分配为：","QUESTION_TYPE":"1","ORDER_NO":"6","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"131","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"全部投资于A；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"131"},{"ANSWER_CONTENT":"大部分投资于A；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"131"},{"ANSWER_CONTENT":"两种投资各一半；","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"131"},{"ANSWER_CONTENT":"大部分投资于B；","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"131"},{"ANSWER_CONTENT":"全部投资于B。","ANSWER_NO":"5","REMARK":" ","QUESTION_NO":"131"}]},{"QUESTION_SCORE":"7.00","QUESTION_CONTENT":"假如您前期用25元购入一只股票，该股现在升到30元，而根据预测该股近期有一半机会升到35元，另一半机会跌倒25元，您现在会：","QUESTION_TYPE":"1","ORDER_NO":"7","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"132","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"立刻卖出；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"132"},{"ANSWER_CONTENT":"部分卖出；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"132"},{"ANSWER_CONTENT":"继续持有；","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"132"},{"ANSWER_CONTENT":"继续买入。","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"132"}]},{"QUESTION_SCORE":"7.00","QUESTION_CONTENT":"同上题情况，该股现在已经跌到20元，而您估计该股近期有一半机会升回25元，另一半机会继续下跌到15元，您现在会：","QUESTION_TYPE":"1","ORDER_NO":"8","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"133","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"立刻卖出；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"133"},{"ANSWER_CONTENT":"部分卖出；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"133"},{"ANSWER_CONTENT":"继续持有；","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"133"},{"ANSWER_CONTENT":"继续买入。","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"133"}]},{"QUESTION_SCORE":"9.00","QUESTION_CONTENT":"当您进行投资时，您的首要目标是：","QUESTION_TYPE":"1","ORDER_NO":"9","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"134","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"资产保值，我不愿意承担任何投资风险；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"134"},{"ANSWER_CONTENT":"尽可能保证本金安全，不在乎收益率比较低；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"134"},{"ANSWER_CONTENT":"产生较多的收益，可以承担一定的投资风险；","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"134"},{"ANSWER_CONTENT":"实现资产大幅增长，愿意承担很大的投资风险。","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"134"}]},{"QUESTION_SCORE":"9.00","QUESTION_CONTENT":"您的投资经验可以被概括为：","QUESTION_TYPE":"1","ORDER_NO":"10","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"135","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"有限：除银行活期账户和定期存款外，我基本没有其他投资经验；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"135"},{"ANSWER_CONTENT":"一般：除银行活期账户和定期存款外，我购买过基金、保险等理财产品，但还需要进一步的指导；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"135"},{"ANSWER_CONTENT":"丰富：我是一位有经验的投资者，参与过股票、基金等产品的交易，并倾向于自己做出投资决策；","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"135"},{"ANSWER_CONTENT":"非常丰富：我是一位非常有经验的投资者，参与过权证、期货或创业板等高风险产品的交易。","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"135"}]},{"QUESTION_SCORE":"9.00","QUESTION_CONTENT":"您是否了解证券市场的相关知识：","QUESTION_TYPE":"1","ORDER_NO":"11","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"136","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"从来没有参与过证券交易，对投资知识完全不了解；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"136"},{"ANSWER_CONTENT":"学习过证券投资知识，但没有实际操作经验，不懂投资技巧；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"136"},{"ANSWER_CONTENT":"了解证券市场的投资知识，并且有过实际操作经验，懂得一些投资技巧；","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"136"},{"ANSWER_CONTENT":"参与过多年的证券交易，投资知识丰富，具有一定的专业水平。","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"136"}]},{"QUESTION_SCORE":"7.00","QUESTION_CONTENT":"您用于证券投资的资金不会用作其它用途的时间段为：","QUESTION_TYPE":"1","ORDER_NO":"12","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"137","ANSWER_HISTORY":"1","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"短期--0到1年；","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"137"},{"ANSWER_CONTENT":"中期--1到5年；","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"137"},{"ANSWER_CONTENT":"长期--5年以上。","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"137"}]},{"QUESTION_SCORE":"1.00","QUESTION_CONTENT":"账户交易总量（万元）？","QUESTION_TYPE":"1","ORDER_NO":"13","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"231","ANSWER_HISTORY":"","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"< 50","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"231"},{"ANSWER_CONTENT":">= 50 and < 100","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"231"},{"ANSWER_CONTENT":">= 100 and < 200","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"231"},{"ANSWER_CONTENT":">= 200","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"231"}]},{"QUESTION_SCORE":"1.00","QUESTION_CONTENT":"周转率","QUESTION_TYPE":"1","ORDER_NO":"14","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"232","ANSWER_HISTORY":"","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"< 1","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"232"},{"ANSWER_CONTENT":">= 1 and < 2","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"232"},{"ANSWER_CONTENT":">= 2 and < 3","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"232"},{"ANSWER_CONTENT":">= 3","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"232"}]},{"QUESTION_SCORE":"1.00","QUESTION_CONTENT":"平均仓位","QUESTION_TYPE":"1","ORDER_NO":"15","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"233","ANSWER_HISTORY":"","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"< 0.2","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"233"},{"ANSWER_CONTENT":">= 0.2 and < 0.4","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"233"},{"ANSWER_CONTENT":">= 0.4 and < 0.6","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"233"},{"ANSWER_CONTENT":">= 0.6","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"233"}]},{"QUESTION_SCORE":"1.00","QUESTION_CONTENT":"投资能力","QUESTION_TYPE":"1","ORDER_NO":"16","REMARK":" ","ORGAN_FLAG":"0","PAPER_TYPE":"1","QUESTION_NO":"234","ANSWER_HISTORY":"","QUESTION_KIND":"0","OPTION_ANSWER":[{"ANSWER_CONTENT":"< -0.2","ANSWER_NO":"1","REMARK":" ","QUESTION_NO":"234"},{"ANSWER_CONTENT":">= -0.2 and < 0","ANSWER_NO":"2","REMARK":" ","QUESTION_NO":"234"},{"ANSWER_CONTENT":">= 0 and < 0.2","ANSWER_NO":"3","REMARK":" ","QUESTION_NO":"234"},{"ANSWER_CONTENT":">= 0.2","ANSWER_NO":"4","REMARK":" ","QUESTION_NO":"234"}]}]}
                if (TextUtils.isEmpty(response)) {
                    showMistackDialog(ConstantUtil.SERVICE_NO_DATA, null);
                    return;
                }


                response = "{\"code\":\"0\",\"msg\":\"(适当性管理答卷记录查询成功！)\",\"data\":[{\"QUESTION_SCORE\":\"3.00\",\"QUESTION_CONTENT\":\"您的主要收入来源是\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"1\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"8018\",\"ANSWER_HISTORY\":\"12\",\"QUESTION_KIND\":\"1\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"工资、劳务报酬\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"8018\"},{\"ANSWER_CONTENT\":\"生产经营所得\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"8018\"},{\"ANSWER_CONTENT\":\"利息、股息、转让证券等金融性资产收入\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"8018\"},{\"ANSWER_CONTENT\":\"出租、出售房地产等非金融性资产收入\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"8018\"},{\"ANSWER_CONTENT\":\"无固定收入\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"8018\"}]},{\"QUESTION_SCORE\":\"6.00\",\"QUESTION_CONTENT\":\"最近您家庭预计进行证券投资的资金占家庭现有总资产(不含自住、自用房产及汽车等固定资产)的比例是：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"2\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"231\",\"ANSWER_HISTORY\":\"4\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"70%以上\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"231\"},{\"ANSWER_CONTENT\":\"50%-70%\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"231\"},{\"ANSWER_CONTENT\":\"30%－50%\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"231\"},{\"ANSWER_CONTENT\":\"10%－30%\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"231\"},{\"ANSWER_CONTENT\":\"10%以下\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"231\"}]},{\"QUESTION_SCORE\":\"3.00\",\"QUESTION_CONTENT\":\"您是否有尚未清偿的数额较大的债务，如有，其性质是：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"3\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"232\",\"ANSWER_HISTORY\":\"3\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"没有\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"232\"},{\"ANSWER_CONTENT\":\"有，住房抵押贷款等长期定额债务\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"232\"},{\"ANSWER_CONTENT\":\"有，信用卡欠款、消费信贷等短期信用债务\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"232\"},{\"ANSWER_CONTENT\":\"有，亲朋之间借款\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"232\"}]},{\"QUESTION_SCORE\":\"3.00\",\"QUESTION_CONTENT\":\"您可用于投资的资产数额（包括金融资产和不动产）为：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"4\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"233\",\"ANSWER_HISTORY\":\"4\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"不超过50万元人民币\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"233\"},{\"ANSWER_CONTENT\":\"50万-300万元（不含）人民币\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"233\"},{\"ANSWER_CONTENT\":\"300万-1000万元（不含）人民币\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"233\"},{\"ANSWER_CONTENT\":\"1000万元人民币以上\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"233\"}]},{\"QUESTION_SCORE\":\"5.00\",\"QUESTION_CONTENT\":\"以下描述中何种符合您的实际情况：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"5\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"234\",\"ANSWER_HISTORY\":\"3\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"现在或此前曾从事金融、经济或财会等与金融产品投资相关的工作超过两年\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"234\"},{\"ANSWER_CONTENT\":\"已取得金融、经济或财会等与金融产品投资相关专业学士以上学位\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"234\"},{\"ANSWER_CONTENT\":\"取得证券从业资格、期货从业资格、注册会计师证书（CPA）或注册金融分析师证书（CFA）中的一项及以上\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"234\"},{\"ANSWER_CONTENT\":\"我不符合以上任何一项描述\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"234\"}]},{\"QUESTION_SCORE\":\"5.00\",\"QUESTION_CONTENT\":\"您的投资经验可以被概括为\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"6\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"8019\",\"ANSWER_HISTORY\":\"4\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"有限：除银行活期账户和定期存款外，我基本没有其他投资经验\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"8019\"},{\"ANSWER_CONTENT\":\"一般：除银行活期账户和定期存款外，我购买过基金、保险等理财产品，但还需要进一步的指导\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"8019\"},{\"ANSWER_CONTENT\":\"丰富：我是一位有经验的投资者，参与过股票、基金等产品的交易，并倾向于自己做出投资决策\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"8019\"},{\"ANSWER_CONTENT\":\"非常丰富：我是一位非常有经验的投资者，参与过权证、期货或创业板等高风险产品的交易\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"8019\"}]},{\"QUESTION_SCORE\":\"4.00\",\"QUESTION_CONTENT\":\"有一位投资者一个月内做了15笔交易（同一品种买卖各一次算一笔），您认为这样的交易频率：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"7\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"236\",\"ANSWER_HISTORY\":\"1\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"太高了\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"236\"},{\"ANSWER_CONTENT\":\"偏高\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"236\"},{\"ANSWER_CONTENT\":\"正常\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"236\"},{\"ANSWER_CONTENT\":\"偏低\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"236\"}]},{\"QUESTION_SCORE\":\"6.00\",\"QUESTION_CONTENT\":\"过去一年时间内，您购买的不同产品或接受的不同服务（含同一类型的不同产品或服务）的数量是：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"8\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"237\",\"ANSWER_HISTORY\":\"1\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"5个以下\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"237\"},{\"ANSWER_CONTENT\":\"6至10个\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"237\"},{\"ANSWER_CONTENT\":\"11至15个\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"237\"},{\"ANSWER_CONTENT\":\"16个以上\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"237\"}]},{\"QUESTION_SCORE\":\"6.00\",\"QUESTION_CONTENT\":\"以下金融产品或服务，您投资经验在两年以上的有：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"9\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"238\",\"ANSWER_HISTORY\":\"3\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"银行存款等\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"238\"},{\"ANSWER_CONTENT\":\"债券、货币市场基金、债券型基金或其它固定收益类产品等\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"238\"},{\"ANSWER_CONTENT\":\"股票、混合型基金、偏股型基金、股票型基金等权益类投资品种等\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"238\"},{\"ANSWER_CONTENT\":\"期货、融资融券\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"238\"},{\"ANSWER_CONTENT\":\"复杂金融产品、其他产品或服务\",\"ANSWER_NO\":\"5\",\"REMARK\":\"（注：本题可多选，但评分以其中最高分值选项为准。）\",\"QUESTION_NO\":\"238\"}]},{\"QUESTION_SCORE\":\"4.00\",\"QUESTION_CONTENT\":\"如果您曾经从事过金融市场投资，在交易较为活跃的月份，平均月交易额大概是多少：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"10\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"239\",\"ANSWER_HISTORY\":\"3\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"10万元以内\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"239\"},{\"ANSWER_CONTENT\":\"10万元-30万元\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"239\"},{\"ANSWER_CONTENT\":\"30万元-100万元\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"239\"},{\"ANSWER_CONTENT\":\"100万元以上\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"239\"},{\"ANSWER_CONTENT\":\"从未从事过金融市场投资\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"239\"}]},{\"QUESTION_SCORE\":\"5.00\",\"QUESTION_CONTENT\":\"您用于证券投资的大部分资金不会用作其它用途的时间段为：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"11\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"240\",\"ANSWER_HISTORY\":\"2\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"0到1年\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"240\"},{\"ANSWER_CONTENT\":\"0到5年\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"240\"},{\"ANSWER_CONTENT\":\"无特别要求\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"240\"}]},{\"QUESTION_SCORE\":\"6.00\",\"QUESTION_CONTENT\":\"您打算重点投资于哪些种类的投资品种？\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"12\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"241\",\"ANSWER_HISTORY\":\"1\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"债券、货币市场基金、债券基金等固定收益类投资品种\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"241\"},{\"ANSWER_CONTENT\":\"股票、混合型基金、偏股型基金、股票型基金等权益类投资品种\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"241\"},{\"ANSWER_CONTENT\":\"期货、融资融券等\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"241\"},{\"ANSWER_CONTENT\":\"复杂或高风险金融产品或服务\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"241\"},{\"ANSWER_CONTENT\":\"其他产品或服务\",\"ANSWER_NO\":\"5\",\"REMARK\":\"（注：本题可多选，但评分以其中最高分值选项为准。）\",\"QUESTION_NO\":\"241\"}]},{\"QUESTION_SCORE\":\"6.00\",\"QUESTION_CONTENT\":\"假设有两种不同的投资：投资A预期获得5%的收益，有可能承担非常小的损失；投资B预期获得20%的收益，但有可能面临25%甚至更高的亏损。您将您的投资资产分配为：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"13\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"242\",\"ANSWER_HISTORY\":\"4\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"全部投资于A\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"242\"},{\"ANSWER_CONTENT\":\"大部分投资于A\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"242\"},{\"ANSWER_CONTENT\":\"两种投资各一半\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"242\"},{\"ANSWER_CONTENT\":\"大部分投资于B\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"242\"},{\"ANSWER_CONTENT\":\"全部投资于B\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"242\"}]},{\"QUESTION_SCORE\":\"4.00\",\"QUESTION_CONTENT\":\"当您进行投资时，您的首要目标是\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"14\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"8020\",\"ANSWER_HISTORY\":\"3\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"尽可能保证本金安全，不在乎收益率比较低\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"8020\"},{\"ANSWER_CONTENT\":\"产生一定的收益，可以承担一定的投资风险\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"8020\"},{\"ANSWER_CONTENT\":\"产生较多的收益，可以承担较大的投资风险\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"8020\"},{\"ANSWER_CONTENT\":\"实现资产大幅增长，愿意承担很大的投资风险\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"8020\"}]},{\"QUESTION_SCORE\":\"6.00\",\"QUESTION_CONTENT\":\"您认为自己能承受的最大投资损失是多少？\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"15\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"244\",\"ANSWER_HISTORY\":\"4\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"尽可能保证本金安全\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"244\"},{\"ANSWER_CONTENT\":\"一定的投资损失\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"244\"},{\"ANSWER_CONTENT\":\"较大的投资损失\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"244\"},{\"ANSWER_CONTENT\":\"损失可能超过本金\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"244\"}]},{\"QUESTION_SCORE\":\"7.00\",\"QUESTION_CONTENT\":\"您打算将自己的投资回报主要用于：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"16\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"245\",\"ANSWER_HISTORY\":\"1\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"改善生活\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"245\"},{\"ANSWER_CONTENT\":\"个体生产经营或证券投资以外的投资行为\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"245\"},{\"ANSWER_CONTENT\":\"履行扶养、抚养或赡养义务\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"245\"},{\"ANSWER_CONTENT\":\"本人养老或医疗\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"245\"},{\"ANSWER_CONTENT\":\"偿付债务\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"245\"}]},{\"QUESTION_SCORE\":\"7.00\",\"QUESTION_CONTENT\":\"您的年龄是：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"17\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"246\",\"ANSWER_HISTORY\":\"3\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"18-30岁\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"246\"},{\"ANSWER_CONTENT\":\"31-40岁\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"246\"},{\"ANSWER_CONTENT\":\"41-50岁\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"246\"},{\"ANSWER_CONTENT\":\"51-60岁\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"246\"},{\"ANSWER_CONTENT\":\"超过60岁\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"246\"}]},{\"QUESTION_SCORE\":\"5.00\",\"QUESTION_CONTENT\":\"今后五年时间内，您的父母、配偶以及未成年子女等需负法定抚养、扶养和赡养义务的人数为：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"18\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"247\",\"ANSWER_HISTORY\":\"2\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"1-2人\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"247\"},{\"ANSWER_CONTENT\":\"3-4人\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"247\"},{\"ANSWER_CONTENT\":\"5人以上\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"247\"}]},{\"QUESTION_SCORE\":\"5.00\",\"QUESTION_CONTENT\":\"您的最高学历是：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"19\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"248\",\"ANSWER_HISTORY\":\"4\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"高中或以下\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"248\"},{\"ANSWER_CONTENT\":\"大学专科\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"248\"},{\"ANSWER_CONTENT\":\"大学本科\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"248\"},{\"ANSWER_CONTENT\":\"硕士及以上\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"248\"}]},{\"QUESTION_SCORE\":\"4.00\",\"QUESTION_CONTENT\":\"您家庭的就业状况是：\",\"QUESTION_TYPE\":\"1\",\"ORDER_NO\":\"20\",\"REMARK\":\" \",\"ORGAN_FLAG\":\"0\",\"PAPER_TYPE\":\"1\",\"QUESTION_NO\":\"249\",\"ANSWER_HISTORY\":\"5\",\"QUESTION_KIND\":\"0\",\"OPTION_ANSWER\":[{\"ANSWER_CONTENT\":\"您与配偶均有稳定收入的工作\",\"ANSWER_NO\":\"1\",\"REMARK\":\" \",\"QUESTION_NO\":\"249\"},{\"ANSWER_CONTENT\":\"您与配偶其中一人有稳定收入的工作\",\"ANSWER_NO\":\"2\",\"REMARK\":\" \",\"QUESTION_NO\":\"249\"},{\"ANSWER_CONTENT\":\"您与配偶均没有稳定收入的工作或者已退休\",\"ANSWER_NO\":\"3\",\"REMARK\":\" \",\"QUESTION_NO\":\"249\"},{\"ANSWER_CONTENT\":\"未婚，但有稳定收入的工作\",\"ANSWER_NO\":\"4\",\"REMARK\":\" \",\"QUESTION_NO\":\"249\"},{\"ANSWER_CONTENT\":\"未婚，目前暂无稳定收入的工作\",\"ANSWER_NO\":\"5\",\"REMARK\":\" \",\"QUESTION_NO\":\"249\"}]}]}";
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    if (!"0".equals(code)) {
                        showMistackDialog(msg, null);
                        return ;
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (jsonArray == null || jsonArray.length() <= 0) {
                        return;
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Map<String, Object> data = new HashMap<>();

                        JSONObject subObj = jsonArray.getJSONObject(i);
                        String question = subObj.optString("QUESTION_CONTENT");//试题内容
                        String quesiontNo = subObj.optString("ORDER_NO");//试题顺序号
                        String answer_history =  subObj.optString("ANSWER_HISTORY");//历史选择答案
                        String question_kind = subObj.optString("QUESTION_KIND");//试题类型0：单选 1：多选 2：可编辑题

                        data.put("question", quesiontNo +"."+ question);
                        data.put("answer_history", answer_history);
                        data.put("question_kind", question_kind);

                        JSONArray answers = subObj.getJSONArray("OPTION_ANSWER");

                        if (answers == null || answers.length() <= 0) {
                            continue;
                        }

                        ArrayList<Map<String, String>> subData = new ArrayList<Map<String, String>>();
                        for (int j = 0; j < answers.length(); j++) {
                            Map<String, String> map = new HashMap<>();

                            JSONObject answer = answers.getJSONObject(j);
                            String answer_content = answer.optString("ANSWER_CONTENT");
                            String answer_no = answer.optString("ANSWER_NO");

                            map.put("answer_content", answer_no + "." + answer_content);
                            subData.add(map);
                        }

                        data.put("subData", subData);
                        mDatas.add(data);
                        mAdapter.setDatas(mDatas);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showMistackDialog(ConstantUtil.JSON_ERROR, null);
                }

            }
        });
    }

    private void initLoadDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    private void showMistackDialog(String errorMsg,  DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(RiskTestDetailActivity.this).create();
        alertDialog.setMessage(errorMsg);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", onClickListener);
        alertDialog.show();
    }
}
