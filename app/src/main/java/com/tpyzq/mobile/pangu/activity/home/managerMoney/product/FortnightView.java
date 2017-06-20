package com.tpyzq.mobile.pangu.activity.home.managerMoney.product;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.PdfActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.ProductInterduceAdapter;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.adapter.ProductNoticeAdapter;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.dialog.DownloadDocPdfDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.gridview.MyListView;
import com.tpyzq.mobile.pangu.view.progress.FornightHorizontalProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/9/24.
 * 14天理财
 */
public class FortnightView extends BaseProductView implements  DownloadDocPdfDialog.DownloadPdfCallback {

    private FornightHorizontalProgressBar mProgressBar;
    private Activity mActivity;
    private ProductInterduceAdapter mProductInterduceAdapter;
    private ProductNoticeAdapter mProductNoticeAdapter;
    private ProductNoticeAdapter mProductArgmentAdapter;


    private ArrayList<CleverManamgerMoneyEntity> mProductNoticedatas;
    private ArrayList<CleverManamgerMoneyEntity> mProductArgmentdatas;

    private String mProductType;
    private String mProductCode;
    private String mPersent;
    private static final String TAG = "FortnightView";

    public FortnightView(Activity activity, int type, Object object) {
        super(activity, type, object);
        mActivity = activity;
    }

    @Override
    public void initView(View view, final Activity activity, int type, Object object) {
        mActivity = activity;
        CleverManamgerMoneyEntity entity = (CleverManamgerMoneyEntity) object;
        mProgressBar = (FornightHorizontalProgressBar) view.findViewById(R.id.fortnightProgress);

        MyListView productInterduceListview = (MyListView) view.findViewById(R.id.fornightProductIntrudceListView);
        MyListView productNoticeListView = (MyListView) view.findViewById(R.id.fornightProductNoticeListView);
        MyListView productArgementListView = (MyListView) view.findViewById(R.id.fornightProductAgreementListView);

        View view1 = LayoutInflater.from(activity).inflate(R.layout.adapter_nodate, null);

        mProductInterduceAdapter = new ProductInterduceAdapter();
        mProductNoticeAdapter = new ProductNoticeAdapter(ContextCompat.getColor(activity, R.color.black));
        mProductArgmentAdapter = new ProductNoticeAdapter(ContextCompat.getColor(activity, R.color.blue));

        productInterduceListview.setAdapter(mProductInterduceAdapter);
        productNoticeListView.setAdapter(mProductNoticeAdapter);

        productNoticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mProductNoticedatas != null && mProductNoticedatas.size() > 0) {
                    String url = mProductNoticedatas.get(position).getGeneralSituationContentUrl();
                    String name = mProductNoticedatas.get(position).getGeneralSituationContent();


                    String filePath = Helper.getExternalDirPath(CustomApplication.getContext(), "pdf", name) + ".pdf";;
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
                        if (isUrl) {
                            //下载
                            DownloadDocPdfDialog.getInstance().showDialog(activity, FortnightView.this, url, name);

                        } else {
                            MistakeDialog.showDialog("该url不正确", mActivity);
                        }
                    }

                }
            }
        });

        productArgementListView.setAdapter(mProductArgmentAdapter);
        productArgementListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mProductArgmentdatas != null && mProductArgmentdatas.size() > 0) {
                    String url = mProductArgmentdatas.get(position).getGeneralSituationContentUrl();
                    String name = mProductArgmentdatas.get(position).getGeneralSituationContent();

                    String filePath = Helper.getExternalDirPath(CustomApplication.getContext(), "pdf", name) + ".pdf";;
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
                        if (isUrl) {
                            //下载
                            DownloadDocPdfDialog.getInstance().showDialog(activity, FortnightView.this, url, name);
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
            mPersent = entity.getCOMPREF();

            initProgressDate(entity);
            initProductProduce(entity);

        } else {
            productInterduceListview.addFooterView(view1);
            productNoticeListView.addFooterView(view1);
            productArgementListView.addFooterView(view1);
        }



        mProductNoticedatas = getProductNoticeData(entity);
        mProductArgmentdatas = getProductArgmentData(entity);

        mProductNoticeAdapter.setDatas(mProductNoticedatas);
        mProductArgmentAdapter.setDatas(mProductArgmentdatas);

    }

    private void initProgressDate(CleverManamgerMoneyEntity entity) {
        mProgressBar.setProgress(100);
        mProgressBar.setStartDate("T日", "T+2日", "T+14日", "T+17日");
    }

    private void initProductProduce(CleverManamgerMoneyEntity entity) {

        //产品代码

        String productCode = "";

        if (!TextUtils.isEmpty(entity.getPRODCODE())) {
            productCode = entity.getPRODCODE();
        }


        String productName = "";

        if (!TextUtils.isEmpty(entity.getPRODNAME())) {
            productName = entity.getPRODNAME();
        }

        //产品类型
        String productType = "";

        if (!TextUtils.isEmpty(entity.getPRODTYPE())) {
            productType = entity.getPRODTYPE();
        }

        //发现公司
        String productCommpany = "";

        if (!TextUtils.isEmpty(entity.getPUBCOMPANY())) {
            productCommpany = entity.getPUBCOMPANY();
        }

        //投资期限
        String productDeadline = "";

        if (!TextUtils.isEmpty(entity.getINVESTDAYS())) {
            productDeadline = entity.getINVESTDAYS();
        }

        //相关说明
        String explain = "";

        if (!TextUtils.isEmpty(entity.getDESCRIPITION())) {
            explain = entity.getDESCRIPITION();
        }


        String [] titles = {"产品代码", "产品类型", "发行公司", "投资期限", "相关说明"};
        String [] contents = {productCode, productType, productCommpany, productDeadline, explain};
        ArrayList<CleverManamgerMoneyEntity> productInterducedatas = new ArrayList<>();
        for (int i  = 0; i< titles.length; i++) {
            CleverManamgerMoneyEntity cleverManamgerMoneyEntity = new CleverManamgerMoneyEntity();
            cleverManamgerMoneyEntity.setGeneralSituationTitle(titles[i]);
            cleverManamgerMoneyEntity.setGeneralSituationContent(contents[i]);
            productInterducedatas.add(cleverManamgerMoneyEntity);
        }

        mProductInterduceAdapter.setDatas(productInterducedatas);

    }


    private ArrayList<CleverManamgerMoneyEntity> getProductNoticeData(CleverManamgerMoneyEntity entity) {

        List<Map<String, String>> maps = entity.getNoticeDates();
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


        List<Map<String, String>> maps = entity.getProaoclDates();
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
            intent.putExtra("filePath", filePath);
            intent.putExtra("fileName", name);
            intent.putExtra("flag", 1);
            intent.setClass(mActivity, PdfActivity.class);
            mActivity.startActivity(intent);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_manager_money_fortnight;
    }
}
