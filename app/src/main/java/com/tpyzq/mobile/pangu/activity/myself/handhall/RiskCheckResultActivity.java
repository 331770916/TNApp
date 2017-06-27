package com.tpyzq.mobile.pangu.activity.myself.handhall;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.BaseActivity;

/**
 * Created by zhangwenbo on 2017/6/27.
 * 风险审核结果页面
 */

public class RiskCheckResultActivity extends BaseActivity {

    @Override
    public void initView() {
//        String [] strs = {"债券、货币市场基金、债券基金等固定收益类投资品种",
//                "股票、混合型基金、偏股票基金、股票型基金等权益类投资品种",
//                "期货、融资融券等", "其他高风险基金"};
//
//        TextView riskType = (TextView) findViewById(R.id.tv_risk_type);
//        StringBuilder sb = new StringBuilder();
//        sb.append("<html><body>");
//
//        for (String str : strs) {
//            sb.append("<p><img src=\""+R.drawable.ic_li_24dp+"\" style=\"float:left\"/>" + str + "</p>");
//        }
//
//        sb.append("</body></html>");
//
//        Html.ImageGetter imageGetter = new Html.ImageGetter(){
//            public Drawable getDrawable(String source) {
//                int rId = Integer.parseInt(source);
//                Drawable drawable = getResources().getDrawable(rId);
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//                return drawable;
//            }
//        };
//
//        riskType.setText(Html.fromHtml(sb.toString(),imageGetter, null));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_riskcheck_result;
    }
}
