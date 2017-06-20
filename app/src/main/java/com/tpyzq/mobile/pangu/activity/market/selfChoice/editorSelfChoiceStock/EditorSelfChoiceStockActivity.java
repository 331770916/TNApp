package com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock.remain.RemainActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_HOME_INFO;
import com.tpyzq.mobile.pangu.db.Db_PUB_SEARCHHISTORYSTOCK;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.HOLD_SEQ;
import com.tpyzq.mobile.pangu.http.doConnect.self.AddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ArraySelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.DeleteSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.HoldCloudConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToAddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToArraySelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToDelteSlefChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToHoldCloudConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.SelfChoiceStockTempData;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MessageDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.tpyzq.mobile.pangu.view.dragsortlistview.DragSortListView;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by zhangwenbo on 2016/8/10.
 * 编辑自选股页面
 */
public class EditorSelfChoiceStockActivity extends BaseActivity implements ICallbackResult,
        View.OnClickListener, EditorSelfChoiceStockAdapter.UpdateEditorSelfChoiceData, EditorSelfChoiceStockAdapter.RemainCallback {

    private EditorSelfChoiceStockAdapter mAdapter;
    private ArrayList<StockInfoEntity> mDatas;
    private ArrayList<StockInfoEntity> mRemoveBeans;
    private SimpleRemoteControl mSimpleRemoteControl;
    private Dialog mLoadingDialog;
    private String [] mStrPositions = null;
    private String [] mStrCodes = null;
    private TextView mHoldImportTv;
    private ImageView mIcImport;
    private CheckBox mChoiceAll;
    private static final String TAG = "EditorSelfChoiceStockActivity";
    private boolean isDestory = false;
    @Override
    public void initView() {

        Intent intent = getIntent();
        mDatas = intent.getParcelableArrayListExtra("beans");

        if (mDatas == null || mDatas.size() <= 0) {
            mDatas = Db_PUB_STOCKLIST.queryStockListDatas();
        }

        mSimpleRemoteControl = new SimpleRemoteControl(this);
        mRemoveBeans = new ArrayList<>();

        findViewById(R.id.postiveBtn).setOnClickListener(this);
        findViewById(R.id.deleteBtn).setOnClickListener(this);
        mChoiceAll = (CheckBox) findViewById(R.id.editSelfAllcheckBox);
        mChoiceAll.setOnClickListener(this);
        SwitchButton holdSwBtn = (SwitchButton) findViewById(R.id.editSelfSwitchBtn1);

        String holdSwbtnFlag = SpUtils.getString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, "false");

        if ("true".equals(holdSwbtnFlag)) {
            holdSwBtn.setChecked(true);
        } else if ("false".equals(holdSwbtnFlag)) {
            holdSwBtn.setChecked(false);
        }

        holdSwBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_APPEAR);
                    mAdapter.setData(mDatas);
                } else {
                    SpUtils.putString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);
                    mAdapter.setData(mDatas);
                }
            }
        });

        mHoldImportTv = (TextView) findViewById(R.id.editSelfSwitchBtn2);
        mIcImport = (ImageView) findViewById(R.id.ic_importHold);
        mIcImport.setOnClickListener(this);
        mHoldImportTv.setOnClickListener(this);

        if (Db_PUB_USERS.islogin()) {
            mHoldImportTv.setVisibility(View.GONE);
            mIcImport.setVisibility(View.VISIBLE);
        } else {
            mHoldImportTv.setVisibility(View.VISIBLE);
            mIcImport.setVisibility(View.GONE);
        }

        DragSortListView listView = (DragSortListView) findViewById(R.id.editSelfListView);
        mAdapter = new EditorSelfChoiceStockAdapter(this);
        listView.setAdapter(mAdapter);
        listView.setDropListener(onDrop);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setDragEnabled(true); //设置是否可拖动。
        mAdapter.setData(mDatas);
        mAdapter.setDatasListener(this);
    }

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    if (from != to) {
                        StockInfoEntity item = (StockInfoEntity) mAdapter.getItem(from);//得到listview的适配器
                        mAdapter.remove(from);//在适配器中”原位置“的数据。
                        mAdapter.insert(item, to);//在目标位置中插入被拖动的控件。
                    }
                }
            };

    @Override
    protected void onResume() {
        super.onResume();

        if (Db_PUB_USERS.islogin()) {
            mHoldImportTv.setVisibility(View.GONE);
            mIcImport.setVisibility(View.VISIBLE);
        } else {
            mHoldImportTv.setVisibility(View.VISIBLE);
            mIcImport.setVisibility(View.GONE);
        }
    }

    @Override
    public void getResult(Object result, String tag) {
        if (isDestory) {
            return;
        }
        String code1 = "-1";
        if ("ArraySelfChoiceStockConnect".equals(tag)) {

            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }

            Map<String, String> map = (Map<String, String>) result;
            code1 = map.get("code");
            String msg = map.get("msg");

            if ("0".equals(code1)) {
                Db_PUB_STOCKLIST.deleteAllStocListkDatas();
                if (mRemoveBeans != null && mRemoveBeans.size() > 0) {
                    for (StockInfoEntity removeBean : mRemoveBeans) {
                        removeBean.setSelfChoicStock(false);
                        Db_HOME_INFO.deleteOneSelfNewsData(removeBean.getStockNumber());
                        Db_PUB_SEARCHHISTORYSTOCK.addOneData(removeBean);
                        SelfChoiceStockTempData.getInstance().removeSelfchoicestockTempValue(removeBean.getStockNumber());
                    }
                }
                if (mDatas != null && mDatas.size() > 0) {
                    for (int i = mDatas.size() - 1; i >= 0; i--) {
                        Db_PUB_STOCKLIST.addOneStockListData(mDatas.get(i));
                    }
                }
                mDatas = null;
                Intent intent = new Intent();
                intent.putExtra("finish", "finish");
                setResult(RESULT_OK, intent);
                finish();
            } else {
                MessageDialog.showDialog("网络异常\n点击确定重试，取消返回上一页", EditorSelfChoiceStockActivity.this, new MessageDialog.PositiveCliceListener() {
                    @Override
                    public void positiveClick(View v) {
                        if (mSimpleRemoteControl != null && mStrPositions != null && mStrPositions.length > 0 && mStrCodes != null && mStrCodes.length > 0) {
                            mSimpleRemoteControl.setCommand(new ToArraySelfChoiceStockConnect(new ArraySelfChoiceStockConnect(TAG, "", UserUtil.userId, mStrPositions, mStrCodes)));
                            mSimpleRemoteControl.startConnect();
                        }
                    }
                }, new MessageDialog.CancleClickListener() {
                    @Override
                    public void cancleClick(View v) {
                        finish();
                    }
                });
            }

        } else if ("HoldCloudConnect".equals(tag)) {

            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }

            if (null != result && result instanceof String) {
                if (!isDestory) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }

                    MistakeDialog.showDialog("" + result, EditorSelfChoiceStockActivity.this);
                }
                return;
            }

            ArrayList<StockInfoEntity> stockInfoEntities = (ArrayList<StockInfoEntity>) result;

            if (stockInfoEntities != null && stockInfoEntities.size() > 0) {
                HOLD_SEQ.deleteAll();

                String stockCodes = "";
                String stockNames = "";
                String stockPricees = "";

                StringBuilder sbCode = new StringBuilder();
                StringBuilder sbName = new StringBuilder();
                StringBuilder sbPrice = new StringBuilder();


                boolean holdFlag = HOLD_SEQ.addHoldDatas(stockInfoEntities);
                if (!holdFlag) {
                    Helper.getInstance().showToast(CustomApplication.getContext(), "导入持仓自选股数据库失败");
                    return;
                }

                for (int i = 0; i < stockInfoEntities.size(); i++) {
                    Db_PUB_STOCKLIST.addOneStockListData(stockInfoEntities.get(i));

                    if (i == stockInfoEntities.size() -1) {
                        sbCode.append(stockInfoEntities.get(i).getStockNumber());
                        sbName.append(stockInfoEntities.get(i).getStockName());
                        sbPrice.append(stockInfoEntities.get(i).getNewPrice());
                    } else {
                        sbCode.append(stockInfoEntities.get(i).getStockNumber()).append(",");
                        sbName.append(stockInfoEntities.get(i).getStockName()).append(",");
                        sbPrice.append(stockInfoEntities.get(i).getNewPrice()).append(",");
                    }

                    SelfStockHelper.sendUpdateSelfChoiceBrodcast(EditorSelfChoiceStockActivity.this, stockInfoEntities.get(i).getStockNumber());
                }



                stockCodes = sbCode.toString();
                stockNames = sbName.toString();
                stockPricees = sbPrice.toString();

                mSimpleRemoteControl.setCommand(new ToAddSelfChoiceStockConnect(new AddSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stockCodes, UserUtil.userId, stockNames, stockPricees)));
                mSimpleRemoteControl.startConnect();

                mDatas = Db_PUB_STOCKLIST.queryStockListDatas();
                mAdapter.setData(mDatas);

            } else {
                if (!isDestory) {
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                    MistakeDialog.showDialog("无持仓股票", EditorSelfChoiceStockActivity.this);
                }
            }
        } else if ("AddSelfChoiceStockConnect".equals(tag)) {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
            String msg = String.valueOf(result);
            SelfStockHelper.explanImportHoldResult(EditorSelfChoiceStockActivity.this, msg);
        } else if ("DeleteSelfChoiceStockConnect".equals(tag)) {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
            String msg = String.valueOf(result);
            if (msg.contains("成功")) {
                Db_PUB_STOCKLIST.deleteAllStocListkDatas();
                Db_HOME_INFO.deleteAllSelfNewsDatas();
                if (mRemoveBeans != null && mRemoveBeans.size() > 0) {
                    for (StockInfoEntity removeBean : mRemoveBeans) {
                        removeBean.setSelfChoicStock(false);
                        Db_HOME_INFO.deleteOneSelfNewsData(removeBean.getStockNumber());
                        Db_PUB_SEARCHHISTORYSTOCK.addOneData(removeBean);
                    }
                }
                ResultDialog.getInstance().show("删除成功", R.mipmap.lc_success);
                mDatas = null;
                Intent intent = new Intent();
                intent.putExtra("finish", "finish");
                setResult(RESULT_OK, intent);
                finish();
            } else {
                if (!isDestory) {
                    MistakeDialog.showDialog("删除失败", EditorSelfChoiceStockActivity.this, new MistakeDialog.MistakeDialgoListener() {
                        @Override
                        public void doPositive() {

                        }
                    });
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postiveBtn:
                if (Db_PUB_USERS.isRegister()) {
                    if (mDatas == null && mRemoveBeans.size() <= 0) {
                        finish();
                    } else if (mDatas != null && mDatas.size() > 0) {
                        mStrPositions = new String[mDatas.size()];
                        mStrCodes = new String[mDatas.size()];
                        for (int i = 0; i < mDatas.size(); i++) {
                            mStrPositions [i] = "" + i;
                            mStrCodes[i] = mDatas.get(i).getStockNumber();
                        }
                        mLoadingDialog = LoadingDialog.initDialog(EditorSelfChoiceStockActivity.this, "加载中...");
                        mLoadingDialog.show();
                        if (mSimpleRemoteControl != null && mStrPositions != null && mStrPositions.length > 0 && mStrCodes != null && mStrCodes.length > 0) {
                            mSimpleRemoteControl.setCommand(new ToArraySelfChoiceStockConnect(new ArraySelfChoiceStockConnect(TAG, "", UserUtil.userId, mStrPositions, mStrCodes)));
                            mSimpleRemoteControl.startConnect();
                        }
                    } else if (mRemoveBeans != null && mRemoveBeans.size() > 0 && mDatas != null && mDatas.size() <= 0) {
                        mLoadingDialog = LoadingDialog.initDialog(EditorSelfChoiceStockActivity.this, "加载中...");
                        mLoadingDialog.show();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mRemoveBeans.size(); i++) {
                            String stockCode =  mRemoveBeans.get(i).getStockNumber();
                            if (i < mRemoveBeans.size() - 1) {
                                sb.append(stockCode).append(",");
                            } else {
                                sb.append(stockCode);
                            }
                        }
                        mSimpleRemoteControl.setCommand(new ToDelteSlefChoiceStockConnect(new DeleteSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, sb.toString(), UserUtil.userId)));
                        mSimpleRemoteControl.startConnect();
                    }
                } else {
                    Db_PUB_STOCKLIST.deleteAllStocListkDatas();
                    if (mRemoveBeans != null && mRemoveBeans.size() > 0) {
                        for (StockInfoEntity removeBean : mRemoveBeans) {
                            removeBean.setSelfChoicStock(false);
                            Db_PUB_SEARCHHISTORYSTOCK.addOneData(removeBean);
                            Db_HOME_INFO.deleteOneSelfNewsData(removeBean.getStockNumber());
                        }
                    }
                    if (mDatas != null && mDatas.size() > 0) {
                        for (int i = mDatas.size() -1; i >= 0; i--) {
                            Db_PUB_STOCKLIST.addOneStockListData(mDatas.get(i));
                        }
                    }

                    mDatas = null;
                    Intent intent = new Intent();
                    intent.putExtra("finish", "finish");
                    setResult(RESULT_OK, intent);
                    finish();
                }

                break;
            case R.id.deleteBtn:


                StringBuilder sb = new StringBuilder();
                if (mDatas != null && mDatas.size() > 0) {
                    for (StockInfoEntity bean : mDatas) {
                        if (bean.getIsChecked()) {
                            mRemoveBeans.add(bean);
                            sb.append(bean.getStockNumber()).append(",");
                        }
                    }
                }

                if (mDatas == null || mDatas.size() <= 0) {
                    Helper.getInstance().showToast(CustomApplication.getContext(), "无可删除数据");
                } else if (mDatas != null && mDatas.size() > 0 &&  mRemoveBeans.size() <= 0) {
                    Helper.getInstance().showToast(CustomApplication.getContext(), "无勾选删除数据");
                }
                if(mDatas!=null&& mDatas.size() > 0)
                {
                    mDatas.removeAll(mRemoveBeans);
                }
                mAdapter.setData(mDatas);
                break;
            case R.id.editSelfSwitchBtn2:

                if (!Db_PUB_USERS.isRegister()) {
                    Intent intent1 = new Intent();
                    intent1.setClass(EditorSelfChoiceStockActivity.this, ShouJiZhuCeActivity.class);
                    EditorSelfChoiceStockActivity.this.startActivity(intent1);
                } else if (!Db_PUB_USERS.islogin()) {
                    Intent intent1 = new Intent();
                    intent1.setClass(EditorSelfChoiceStockActivity.this, TransactionLoginActivity.class);
                    EditorSelfChoiceStockActivity.this.startActivity(intent1);
                }

                break;
            case R.id.ic_importHold:

                if (!Db_PUB_USERS.isRegister()) {
                    Intent intent = new Intent(EditorSelfChoiceStockActivity.this, ShouJiZhuCeActivity.class);
                    EditorSelfChoiceStockActivity.this.startActivity(intent);
                } else if (!Db_PUB_USERS.islogin()) {
                    Intent intent = new Intent();
                    intent.setClass(EditorSelfChoiceStockActivity.this, TransactionLoginActivity.class);
                    EditorSelfChoiceStockActivity.this.startActivity(intent);
                } else {
                    mLoadingDialog = LoadingDialog.initDialog(EditorSelfChoiceStockActivity.this, "正在导入，请稍后...");
                    mLoadingDialog.show();

                    String session = SpUtils.getString(CustomApplication.getContext(), "mSession", null);
                    mSimpleRemoteControl.setCommand(new ToHoldCloudConnect(new HoldCloudConnect(TAG, session)));
                    mSimpleRemoteControl.startConnect();
                }

                break;

            case R.id.editSelfAllcheckBox:

                ArrayList<StockInfoEntity> checkedEntity = new ArrayList<>();
                if (mDatas != null && mDatas.size() > 0) {
                    for (StockInfoEntity bean : mDatas) {
                        bean.setIsChecked(mChoiceAll.isChecked());
                        checkedEntity.add(bean);
                    }
                }

                mAdapter.setData(checkedEntity);

                break;
        }
    }

    @Override
    public void remainClickListener(StockInfoEntity entity) {
        Intent intent = new Intent();
        String stockNumber = entity.getStockNumber();
        String stockName = entity.getStockName();

        if (!TextUtils.isEmpty(stockNumber)) {
            intent.putExtra("stockNumber", stockNumber);
            String market = stockNumber.substring(1, 2);
            if ("0".equals(market)) {
                Helper.getInstance().showToast(CustomApplication.getContext(), "不支持该股票设置提醒");
                return;
            }
        }
        if (!TextUtils.isEmpty(stockName)) {
            intent.putExtra("stockName", stockName);
        }
        intent.setClass(EditorSelfChoiceStockActivity.this, RemainActivity.class);
        startActivity(intent);
    }

    @Override
    public void updateDatas(ArrayList<StockInfoEntity> datas) {
        ArrayList<Boolean> flags = new ArrayList<>();

        for (StockInfoEntity bean : datas) {
            flags.add(bean.getIsChecked());
        }

        if (flags.contains(false)) {
            mChoiceAll.setChecked(false);
        } else {
            mChoiceAll.setChecked(true);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestory = true;
    }
    @Override
    public int getLayoutId() {        return R.layout.activity_edit_self;    }
}
