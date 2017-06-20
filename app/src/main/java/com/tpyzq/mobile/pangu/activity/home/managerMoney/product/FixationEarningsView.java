package com.tpyzq.mobile.pangu.activity.home.managerMoney.product;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.PdfActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.ProductInterduceAdapter;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.ProductNoticeAdapter;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.product.precontract.PrecontractFlowActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.dialog.DownloadDocPdfDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.tpyzq.mobile.pangu.view.progress.FornightHorizontalProgressBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/9/24.
 * 固定收益
 */
public class FixationEarningsView extends BaseProductView implements DownloadDocPdfDialog.DownloadPdfCallback {

    private FornightHorizontalProgressBar mProgressBar;
    private ProductInterduceAdapter mProductInterduceAdapter;
    private ProductNoticeAdapter mProductNoticeAdapter;
    private ProductNoticeAdapter mProductArgmentAdapter;

    private ArrayList<CleverManamgerMoneyEntity> mProductNoticeData;
    private ArrayList<CleverManamgerMoneyEntity> mProductArgmentData;

    private String mProductType;
    private String mProductCode;
    private String mPersent;
    private Activity mActivity;
    private static final String TAG = "FixationEarningsView";

    public FixationEarningsView(Activity activity, int type, Object object) {
        super(activity, type, object);
        mActivity = activity;
    }

    @Override
    public void initView(View view, final Activity activity, int type, Object object) {
        mActivity = activity;
        final CleverManamgerMoneyEntity entity = (CleverManamgerMoneyEntity) object;

        view.findViewById(R.id.llayout_fengxian).setVisibility(View.GONE);

        TextView textView = (TextView) view.findViewById(R.id.precontract_flowText);
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isClick", entity.isForce());
                intent.setClass(activity, PrecontractFlowActivity.class);
                activity.startActivity(intent);
            }
        });

        if (TextUtils.isEmpty(entity.getOreder()) || !"0".equals(entity.getOreder())) {
            textView.setVisibility(View.GONE);
        }

        mProgressBar = (FornightHorizontalProgressBar) view.findViewById(R.id.otcProgress);
        MyListView productInterduceListview = (MyListView) view.findViewById(R.id.otcProductIntrudceListView);
        MyListView productNoticeListView = (MyListView) view.findViewById(R.id.otcProductNoticeListView);
        MyListView productArgementListView = (MyListView) view.findViewById(R.id.otcProductAgreementListView);

        //暂无数据
        View view1 = LayoutInflater.from(activity).inflate(R.layout.adapter_nodate, null);

        mProductInterduceAdapter = new ProductInterduceAdapter();
        mProductNoticeAdapter = new ProductNoticeAdapter(ContextCompat.getColor(activity, R.color.black));
        mProductArgmentAdapter = new ProductNoticeAdapter(ContextCompat.getColor(activity, R.color.blue));

        productInterduceListview.setAdapter(mProductInterduceAdapter);
        productNoticeListView.setAdapter(mProductNoticeAdapter);

        productNoticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mProductNoticeData != null && mProductNoticeData.size() > 0) {
                    String url = mProductNoticeData.get(position).getGeneralSituationContentUrl();
                    String name = mProductNoticeData.get(position).getGeneralSituationContent();

                    String filePath = Helper.getExternalDirPath(CustomApplication.getContext(), "pdf", name) + ".pdf";
                    ;
                    File file = new File(filePath);

                    if (file.exists()) {
                        file.delete();
                    }

                    boolean isUrl = Helper.isUrl(url);
                    isUrl = true;
                    if (isUrl) {
                        DownloadDocPdfDialog.getInstance().showDialog(activity, FixationEarningsView.this, url, name);
                    } else {
                        MistakeDialog.showDialog("该url不正确", mActivity);
                    }
                }

            }
        });

        productArgementListView.setAdapter(mProductArgmentAdapter);
        productArgementListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mProductArgmentData != null && mProductArgmentData.size() > 0) {
                    String url = mProductArgmentData.get(position).getGeneralSituationContentUrl();
                    String name = mProductArgmentData.get(position).getGeneralSituationContent();


                    String filePath = Helper.getExternalDirPath(CustomApplication.getContext(), "pdf", name) + ".pdf";
                    File file = new File(filePath);

                    if (file.exists()) {
                        Intent intent = new Intent();
                        intent.putExtra("filePath", filePath);
                        intent.putExtra("fileName", name);
                        intent.putExtra("flag", 1);
                        intent.setClass(activity, PdfActivity.class);
                        activity.startActivity(intent);
                    } else {
                        boolean isUrl = Helper.isUrl(url);
                        isUrl = true;
                        if (isUrl) {
                            //下载
                            DownloadDocPdfDialog.getInstance().showDialog(activity, FixationEarningsView.this, url, name);
                        } else {
                            MistakeDialog.showDialog("该url不正确", mActivity);
                        }
                    }

                }
            }
        });

        if (entity != null) {
            mProductCode = entity.getPRODCODE();
            mProductType = entity.getTYPE();
            mPersent = entity.getPRODRATIO();

            initProgressDate(entity);
            initProductProduce(entity);

        } else {
            productInterduceListview.addFooterView(view1);
            productNoticeListView.addFooterView(view1);
            productArgementListView.addFooterView(view1);
        }
    }

    private void initProgressDate(CleverManamgerMoneyEntity entity) {

        int persentOne = 100 / 4;                        // 第一段的比例尺分子
        int persentTwo = (100 * 2 / 3) - persentOne;    // 第二段的比例尺分子
        int persentThree = 100 * 2 / 3;            //第三段的比例尺分子

        String creatTime = getCreatTime(entity);                        //起购时间
        int creatTimeYear = getCreatTimeYear(creatTime);                //起购时间年

        String startEearnTime = getEearnTime(entity);                   //起息时间
        int startEearnTimeYear = getEarnTimeYear(startEearnTime);       //起息时间年

        String endTime = getEndTime(entity);                            //到期时间
        int endTimeYear = getEndTimeYear(endTime);                      //到期时间年

        String overTime = getOverTime("2018-12-12");                          //到账时间
        int overTimeYear = getOverTimeYear(overTime);                   //到账时间年

        String tempNowTime = "0000-00-00";

        if (!TextUtils.isEmpty(entity.getCREAT_TIME())) {
            tempNowTime = entity.getCREAT_TIME();
        }

        String nowTime = tempNowTime;         //获取当前日期

        boolean isYYYYMMdd = Helper.isDateFromatYYYYMMdd(nowTime);
        if (isYYYYMMdd) {
            nowTime = Helper.StringToDate(nowTime, "yyyyMMdd", "yyyy-MM-dd");
        }

        boolean isYYY_MM_dd = Helper.isDateFromatYYYY_MM_dd(nowTime);
        if (!isYYY_MM_dd) {
            nowTime = "0000-00-00";
        }

        int progressBetweenTime1 = Helper.daysBetween(creatTime, startEearnTime);       //起购和起息时间相差多少天
        int sundy1 = Helper.getSundayNum(creatTime, startEearnTime);        //起购和起息时间周六日的天数

        int progressBetweenTime2 = Helper.daysBetween(startEearnTime, endTime);         //起息和到期时间相差多少天
        int sundy2 = Helper.getSundayNum(startEearnTime, endTime);          //起息和到期时间周六日的天数

        int progressBetweenTime3 = Helper.daysBetween(endTime, overTime);               //到期和截止时间相差多少天
        int sundy3 = Helper.getSundayNum(endTime, overTime);                //到期和截止时间周六日的天数

        int finalTime1 = progressBetweenTime1 - sundy1;                                 //起点 和第一个节点之间的 除了周六日 的天数

        int finalTime2 = progressBetweenTime2 - sundy2;                                 //第一个节点 和第二个节点之间的 除了周六日 的天数

        int finalTime3 = progressBetweenTime3 - sundy3;                                 //终点 和第二个节点之间的 除了周六日 的天数


        /*
         * 获取当天的时间后，首先要和起点时间作比较， 如果小于起点时间，
         * 则购买按钮的颜色显示为灰色 否则 就显示可以购买的颜色  如果通过进行下一步
         */
        int temTime = Helper.compareToDate(nowTime, creatTime);
        if (temTime == 2) {
            mProgressBar.setProgress(-1);
        } else if (temTime == 0) {
            mProgressBar.setProgress(0);
        } else if (temTime == 1) {
            if (!comparenowTimeBystartEearnTime(nowTime, startEearnTime, creatTime, persentOne, finalTime1)) {
                if (!comparenowTimeByoverTime(nowTime, overTime)) {
                    if (!comparenoewTimeByendTime(nowTime, endTime, persentThree, finalTime3)) {
                        comparenowTimeBystartEearnTime2(nowTime, startEearnTime, persentTwo, persentOne, finalTime2);
                    }
                }
            }
        }

        creatTime = Helper.getProTime(creatTime);
        //如果是跨年的话需要显示yyMMdd 否则显示MM-dd
        if (getDifferentYear(creatTimeYear, startEearnTimeYear) <= 0) {
            startEearnTime = Helper.getProTime(startEearnTime);
        }

        if (getDifferentYear(startEearnTimeYear, endTimeYear) <= 0) {
            endTime = Helper.getProTime(endTime);
        }

        if (getDifferentYear(endTimeYear, overTimeYear) <= 0) {
            overTime = Helper.getProTime(overTime);
        }

//        overTime = "T+3";
        overTime = "";
        mProgressBar.setStartDate(creatTime, startEearnTime, endTime, overTime);
    }


    /**
     * 获取当天的时间后，
     * 然后再把现在时间比较是否大于到期时间 ，
     * 如果大于到期则 persentOne + persentTwo + （当前时间 - 到期时间）* （persentThree / finalTime3）
     */
    private boolean comparenoewTimeByendTime(String nowTime, String endTime, int persentThree, int finalTime3) {

        boolean falg = false;

        int temTime3 = Helper.compareToDate(nowTime, endTime);

        if (temTime3 == 1) {

            int sundy = Helper.getSundayNum(nowTime, endTime);
            int betweenDay = Helper.daysBetween(endTime, nowTime) - sundy;
            int persent = ((100 - persentThree) / finalTime3);
            int position = persentThree + betweenDay * persent;
            mProgressBar.setProgress(position);
            falg = true;
        } else if (temTime3 == 0) {
            mProgressBar.setProgress(persentThree);
            falg = true;
        }

        return falg;
    }

    /**
     * 当日和截止日期比较
     *
     * @param nowTime
     * @param overTime
     * @return
     */
    private boolean comparenowTimeByoverTime(String nowTime, String overTime) {
        boolean falg = false;

        int temTime4 = Helper.compareToDate(nowTime, overTime);

        if (temTime4 == 1 || temTime4 == 0) {
            mProgressBar.setProgress(100);
            falg = true;
        }
        return falg;
    }

    /**
     * 然后再把现在的时间比较是否大于起息时间
     * 如果大于起息时间，且小于到期时间， 则 persentOne +（当前时间 - 起息时间）* （persentTwo/finalTime2） 如果通过进行下一步
     */
    private void comparenowTimeBystartEearnTime2(String nowTime, String startEearnTime, int persentTwo, int persentOne, int finalTime2) {
        int sundy = Helper.getSundayNum(startEearnTime, nowTime);
        int betweenDay = Helper.daysBetween(startEearnTime, nowTime) - sundy;
        int persent = (persentTwo / finalTime2);
        int position = persentOne + betweenDay * persent;
        mProgressBar.setProgress(position);
    }

    /**
     * 然后再把现在的时间比较是否大于起息时间
     * 如果小于起息时间，则 （当前时间 - 起购时间所得的天数） * (persentOne/finalTime1)
     * 如果大于起息时间，且小于到期时间， 则 persentOne +（当前时间 - 起息时间）* （persentTwo/finalTime2） 如果通过进行下一步
     */
    private boolean comparenowTimeBystartEearnTime(String nowTime, String startEearnTime, String creatTime, int persentOne, int finalTime1) {
        boolean flag = false;

        int temTime2 = Helper.compareToDate(nowTime, startEearnTime);

        if (temTime2 == 2) {

            int sundy = Helper.getSundayNum(creatTime, nowTime);
            int betweenDay = Helper.daysBetween(creatTime, nowTime) - sundy;
            int persent = persentOne / finalTime1;
            int position = betweenDay * persent;

            mProgressBar.setProgress(position);
            flag = true;
        } else if (temTime2 == 0) {
            mProgressBar.setProgress(persentOne);
            flag = true;
        }
        return flag;
    }


    /**
     * 获取起购时间  时间格式 yyyyMMdd
     *
     * @param entity
     * @return
     */
    private String getCreatTime(CleverManamgerMoneyEntity entity) {
        String creatTime = "00000000";              //起购

        if (!TextUtils.isEmpty(entity.getIPO_BEGIN_DATE())) {
            creatTime = entity.getIPO_BEGIN_DATE();

            boolean isYYYYMMdd = Helper.isDateFromatYYYYMMdd(creatTime);

            if (isYYYYMMdd) {
                creatTime = Helper.StringToDate(creatTime, "yyyyMMdd", "yyyy-MM-dd");
            }

            Date date_SSS = null;
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                date_SSS = s.parse(creatTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date_SSS != null) {
                creatTime = Helper.StringToDate(creatTime, "yyyyMMddHHmmssSSS", "yyyy-MM-dd");
            }

            Date date_ss = null;
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
                date_ss = s.parse(creatTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date_ss != null) {
                creatTime = Helper.StringToDate(creatTime, "yyyyMMddHHmmss", "yyyy-MM-dd");
            }

        }
        return creatTime;
    }

    private int getCreatTimeYear(String creatTime) {
        int creatTimeYear = 0;          //起购时间年

        if ("00000000".equals(creatTime)) {
            return 0;
        }

        if (!TextUtils.isEmpty(creatTime)) {
            creatTimeYear = Helper.getYEAR(creatTime);
        }
        return creatTimeYear;
    }

    /**
     * 获取起息时间
     *
     * @param entity
     * @return
     */
    private String getEearnTime(CleverManamgerMoneyEntity entity) {
        String startEearnTime = "00000000";         //起息

        if (!TextUtils.isEmpty(entity.getINTERESTDAY())) {
            startEearnTime = entity.getINTERESTDAY();
            boolean isYYYYMMdd = Helper.isDateFromatYYYYMMdd(startEearnTime);
            if (isYYYYMMdd) {
                startEearnTime = Helper.StringToDate(startEearnTime, "yyyyMMdd", "yyyy-MM-dd");
            }

            Date date_SSS = null;
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                date_SSS = s.parse(startEearnTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date_SSS != null) {
                startEearnTime = Helper.StringToDate(startEearnTime, "yyyyMMddHHmmssSSS", "yyyy-MM-dd");
            }

            Date date_ss = null;
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
                date_ss = s.parse(startEearnTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date_ss != null) {
                startEearnTime = Helper.StringToDate(startEearnTime, "yyyyMMddHHmmss", "yyyy-MM-dd");
            }
        }

        return startEearnTime;
    }

    /**
     * 获取起息时间年
     *
     * @param earnTime
     * @return
     */
    private int getEarnTimeYear(String earnTime) {
        int startEearnTimeYear = 0;
        if ("00000000".equals(earnTime)) {
            return 0;
        }

        if (!TextUtils.isEmpty(earnTime)) {
            startEearnTimeYear = Helper.getYEAR(earnTime);
        }
        return startEearnTimeYear;
    }

    /**
     * 获取到期时间
     *
     * @param entity
     * @return
     */
    private String getEndTime(CleverManamgerMoneyEntity entity) {
        String endTime = "00000000";                //到期

        if (!TextUtils.isEmpty(entity.getENDDAY())) {
            endTime = entity.getENDDAY();
            boolean isYYYYMMdd = Helper.isDateFromatYYYYMMdd(endTime);
            if (isYYYYMMdd) {
                endTime = Helper.StringToDate(endTime, "yyyyMMdd", "yyyy-MM-dd");
            }



            Date date_SSS = null;
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                date_SSS = s.parse(endTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date_SSS != null) {
                endTime = Helper.StringToDate(endTime, "yyyyMMddHHmmssSSS", "yyyy-MM-dd");
            }

            Date date_ss = null;
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
                date_ss = s.parse(endTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date_ss != null) {
                endTime = Helper.StringToDate(endTime, "yyyyMMddHHmmss", "yyyy-MM-dd");
            }

        }

        return endTime;
    }

    /**
     * 获取到期时间年
     *
     * @param endTime
     * @return
     */
    private int getEndTimeYear(String endTime) {
        int endTimeYear = 0;

        if ("00000000".equals(endTime)) {
            return 0;
        }

        if (!TextUtils.isEmpty(endTime)) {
            endTimeYear = Helper.getYEAR(endTime);
        }

        return endTimeYear;
    }

    /**
     * 获取结束时间
     *
     * @return
     */
    private String getOverTime(String time) {
        String overTime = time;                 //到账
        boolean isYYYYMMdd = Helper.isDateFromatYYYYMMdd(overTime);
        if (isYYYYMMdd) {
            overTime = Helper.StringToDate(overTime, "yyyyMMdd", "yyyy-MM-dd");
        }

            Date date_SSS = null;
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                date_SSS = s.parse(overTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date_SSS != null) {
                overTime = Helper.StringToDate(overTime, "yyyyMMddHHmmssSSS", "yyyy-MM-dd");
            }

            Date date_ss = null;
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
                date_ss = s.parse(overTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (date_ss != null) {
                overTime = Helper.StringToDate(overTime, "yyyyMMddHHmmss", "yyyy-MM-dd");
            }

        return overTime;
    }

    /**
     * 获取结束时间年
     *
     * @param overTime
     * @return
     */
    private int getOverTimeYear(String overTime) {

        int overTimeYear = 0;
        if (!TextUtils.isEmpty(overTime)) {
            overTimeYear = Helper.getYEAR(overTime);
        }
        return overTimeYear;
    }

    /**
     * 比较两个年份是否相同，若相同则返0，不相同则返大的那一个
     *
     * @param year1
     * @param year2
     * @return
     */
    private int getDifferentYear(int year1, int year2) {
        if (year2 - year1 > 0) {
            return year2;
        }
        return year2 - year1 > 0 ? year2 : 0;
    }

    private void initProductProduce(CleverManamgerMoneyEntity entity) {

        //产品名称
        String productName = "-";

        if (!TextUtils.isEmpty(entity.getPRODNAME())) {
            productName = entity.getPRODNAME();
        }

        //产品代码
        String productCode = "-";

        if (!TextUtils.isEmpty(entity.getPRODCODE())) {
            productCode = entity.getPRODCODE();
        }


        //产品类型
        String PROD_STATUS = "-";

        if (!TextUtils.isEmpty(entity.getPROD_STATUS())) {
            PROD_STATUS = entity.getPROD_STATUS();
        }

        //发现公司
        String productCommpany = "-";

        if (!TextUtils.isEmpty(entity.getPUBCOMPANY())) {
            productCommpany = entity.getPUBCOMPANY();
        }

        //投资期限
        String productDeadline = "-";

        if (!TextUtils.isEmpty(entity.getINVESTDAYS())) {
            productDeadline = entity.getINVESTDAYS();
        }

        //挂钩标的
        String subjectMatter = "-";

        if (!TextUtils.isEmpty(entity.getTIP())) {
            subjectMatter = entity.getTIP();
        }

        //收益规则

        String INVEST_TYPE = "-";

        if (!TextUtils.isEmpty(entity.getINVEST_TYPE())) {
            INVEST_TYPE = entity.getINVEST_TYPE();
        }

        String prodType = "-";
        if (!TextUtils.isEmpty(entity.getPRODTYPE())) {
            prodType = entity.getPRODTYPE();
        }

        //相关说明
        String INCOME_TYPE = "-";

        if (!TextUtils.isEmpty(entity.getINCOMETYPE())) {
            INCOME_TYPE = entity.getINCOMETYPE();
        }

        String[] titles = {"产品名称", "产品代码", "产品类型", "交易状态", "投资类别", "收益类别", "产品挂钩标的", "发行公司"};
        String[] contents = {productName, productCode, prodType, PROD_STATUS, INVEST_TYPE, INCOME_TYPE, subjectMatter, productCommpany};
        ArrayList<CleverManamgerMoneyEntity> productInterducedatas = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            CleverManamgerMoneyEntity cleverManamgerMoneyEntity = new CleverManamgerMoneyEntity();
            cleverManamgerMoneyEntity.setGeneralSituationTitle(titles[i]);
            cleverManamgerMoneyEntity.setGeneralSituationContent(contents[i]);
            productInterducedatas.add(cleverManamgerMoneyEntity);
        }

        mProductInterduceAdapter.setDatas(productInterducedatas);

        mProductNoticeData = getProductNoticeData(entity);
        mProductArgmentData = getProductArgmentData(entity);

        mProductNoticeAdapter.setDatas(mProductNoticeData);
        mProductArgmentAdapter.setDatas(mProductArgmentData);

    }


    private ArrayList<CleverManamgerMoneyEntity> getProductNoticeData(CleverManamgerMoneyEntity entity) {

        List<Map<String, String>> maps = entity.getOtcNoticeDate();
        ArrayList<CleverManamgerMoneyEntity> entities = new ArrayList<>();
        if (maps != null && maps.size() > 0) {
            for (Map<String, String> map : maps) {
                String fileName = map.get("name");
                String fileUrl = map.get("url");
                CleverManamgerMoneyEntity cleverManamgerMoneyEntity = new CleverManamgerMoneyEntity();
                cleverManamgerMoneyEntity.setGeneralSituationContent(fileName);
                cleverManamgerMoneyEntity.setGeneralSituationContentUrl(fileUrl);
                entities.add(cleverManamgerMoneyEntity);
            }


        }

        return entities;
    }

    private ArrayList<CleverManamgerMoneyEntity> getProductArgmentData(CleverManamgerMoneyEntity entity) {

        List<Map<String, String>> maps = entity.getOtcProaoclDate();
        ArrayList<CleverManamgerMoneyEntity> entities = new ArrayList<>();

        if (maps != null && maps.size() > 0) {
            for (Map<String, String> map : maps) {
                String fileName = map.get("name");
                String fileUrl = map.get("url");
                CleverManamgerMoneyEntity cleverManamgerMoneyEntity = new CleverManamgerMoneyEntity();
                cleverManamgerMoneyEntity.setGeneralSituationContent(fileName);
                cleverManamgerMoneyEntity.setGeneralSituationContentUrl(fileUrl);
                entities.add(cleverManamgerMoneyEntity);
            }
        }

        return entities;
    }

    @Override
    public void downloadResult(String filePath, String name) {
        if (!TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(name)) {
            Intent intent = new Intent();
            intent.putExtra("flag", 1);
            intent.putExtra("filePath", filePath);
            intent.putExtra("fileName", name);
            intent.setClass(mActivity, PdfActivity.class);
            mActivity.startActivity(intent);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_manager_money_otc;
    }

}
