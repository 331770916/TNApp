package com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock.remain;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.keyboardlibrary.KeyboardUtil;
import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.http.doConnect.self.AddRemainStockPriceConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToAddRemainStockPriceConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/11/7.
 * 股价提醒
 */
public class StockPriceRemainFragment extends BaseFragment implements  View.OnClickListener,
        ICallbackResult, RemainActivity.FragmentBackListener, RemainActivity.MyTouchListener {

    private EditText mRemainSearchEdit;

    private EditText     mIncreaseEdit;
    private EditText     mDownEdit;
    private EditText     mIncreaseZfEdit;
    private EditText     mDownDfEdit;

    private TextView mRemainTitle;
    private SimpleRemoteControl mSimpleRemoteControl;
    private static final String TAG = "StockPriceRemainFragment";
    private String mStockNumber;
    private String mAddRainStockNumber = "";
    private String mStockName;
    private DecimalFormat mFormat2;
    private boolean mResurchFlag;
    private Dialog mLoadingDialog;

    private LinearLayout mRootLayout;
    private KeyboardUtil mKeyBoardUtil;
    private Activity mActivity;
    private boolean mResultFlag;

    @Override
    public void initView(View view) {
        view.findViewById(R.id.remainSubmitBtn).setOnClickListener(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mStockNumber = bundle.getString("stockNumber");
            mStockName = bundle.getString("stockName");
        }
        mActivity = getActivity();
        mFormat2 = new DecimalFormat("#0.00%");
        mSimpleRemoteControl = new SimpleRemoteControl(this);

        mRootLayout = (LinearLayout) view.findViewById(R.id.root_view);
        mRemainSearchEdit = (EditText) view.findViewById(R.id.special_ed);
        mRemainSearchEdit.addTextChangedListener(watcher);

        initMoveKeyBoard();

        mRemainTitle = (TextView) view.findViewById(R.id.remainTitle);
        mIncreaseEdit = (EditText) view.findViewById(R.id.incrisPriceEdit);
        mIncreaseEdit.setOnFocusChangeListener(new NormalEditTextListener());
        mDownEdit = (EditText) view.findViewById(R.id.filedPriceEdit);
        mDownEdit.setOnFocusChangeListener(new NormalEditTextListener());
        mIncreaseZfEdit = (EditText) view.findViewById(R.id.incrisZf);
        mIncreaseZfEdit.setOnFocusChangeListener(new NormalEditTextListener());
        mDownDfEdit = (EditText) view.findViewById(R.id.incrisDf);
        mDownDfEdit.setOnFocusChangeListener(new NormalEditTextListener());
//        ((RemainActivity)this.getActivity()).registerMyTouchListener(this);

        if (!TextUtils.isEmpty(mStockNumber)) {
            mStockNumber = mStockNumber.substring(2, mStockNumber.length());
            searchNetStock(mStockNumber, "0");
        }
    }

    private void initMoveKeyBoard() {
        mKeyBoardUtil = new KeyboardUtil(getActivity(), mRootLayout, null);
        mKeyBoardUtil.setOtherEdittext(mRemainSearchEdit);
        // monitor the KeyBarod state
        mKeyBoardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        // monitor the finish or next Key
        mKeyBoardUtil.setInputOverListener(new InputOverListener());
        mRemainSearchEdit.setOnTouchListener(new KeyboardTouchListener(mKeyBoardUtil, KeyboardUtil.INPUTTYPE_NUM_ABC, -1));
    }

    @Override
    public void getResult(Object result, String tag) {
        String msg = (String) result;

        mLoadingDialog.dismiss();

        if (msg.contains("成功")) {
            ResultDialog.getInstance().show("提交成功", R.mipmap.lc_success);

            mRemainTitle.setText("");
            mRemainSearchEdit.setText("");
            mIncreaseEdit.setText("");
            mDownEdit.setText("");
            mIncreaseZfEdit.setText("");
            mDownDfEdit.setText("");

        } else {
            MistakeDialog.showDialog("" + msg, mActivity);
        }
    }


    private boolean checkIsNULL() {
        boolean flag = true;

        if (TextUtils.isEmpty(mRemainSearchEdit.getText().toString()) || TextUtils.isEmpty(mAddRainStockNumber) || mAddRainStockNumber.length() < 6) {
            Helper.getInstance().showToast(CustomApplication.getContext(), "请输入完整的股票代码");
            flag = false;
        } else if (TextUtils.isEmpty(mIncreaseEdit.getText().toString())
                && TextUtils.isEmpty(mDownEdit.getText().toString())
                && TextUtils.isEmpty(mIncreaseZfEdit.getText().toString())
                && TextUtils.isEmpty(mDownDfEdit.getText().toString())) {

            Helper.getInstance().showToast(CustomApplication.getContext(), "至少输入一个提醒");

            flag = false;
        }



        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remainSubmitBtn:

                if (Db_PUB_USERS.isRegister() && checkIsNULL()) {

                    if (mLoadingDialog == null) {
                        mLoadingDialog = LoadingDialog.initDialog(mActivity, "正在提交");
                    }

                    mLoadingDialog.show();

                    if (mSimpleRemoteControl != null) {
                        mSimpleRemoteControl.setCommand(new ToAddRemainStockPriceConnect(new AddRemainStockPriceConnect(TAG, "", UserUtil.userId,mStockName, mAddRainStockNumber, mIncreaseEdit.getText().toString(), mDownEdit.getText().toString(), mIncreaseZfEdit.getText().toString(), mDownDfEdit.getText().toString())));
                        mSimpleRemoteControl.startConnect();
                    }

                } else if (!Db_PUB_USERS.isRegister()) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("Identify", "1");
                    intent1.setClass(mActivity, ShouJiZhuCeActivity.class);
                    mActivity.startActivity(intent1);
                }

                break;
        }
    }


    /**
     * 搜索网络股票
     */
    private void searchNetStock(String stockInfo, String startNumber) {

        Map map = new HashMap();

        try {
            Map map1 = new HashMap();
            map1.put("num","30");
            map1.put("start",startNumber);
            map1.put("arg",stockInfo);
            Object[] obj = new Object[1];
            obj[0] = map1;
            Gson gson = new Gson();
            String strJson = gson.toJson(obj);
            map.put("PARAMS", strJson);
            map.put("FUNCTIONCODE", "HQING006");

        } catch (Exception e) {
            e.printStackTrace();
        }


        NetWorkUtil.getInstence().okHttpForGet(TAG, ConstantUtil.URL, map, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());

                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }

                MistakeDialog.showDialog("网络异常", getActivity());
            }

            @Override
            public void onResponse(String response, int id) {
//                [{"bytes":28,"totalCount":"2","data":[["10000912","300消费","8437.453","8547.722"],["21000912","泸天化","12.13","12.75"]],"code":"0"}]
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
//[{"bytes":28,"totalCount":"1","data":[["26300179","四方达","-","-"]],"code":"0"}]
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                try{
                    JSONArray res = new JSONArray(response);
                    JSONObject jsonObject = res.getJSONObject(0);
                    ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                    if("0".equals(jsonObject.optString("code"))){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if(null != jsonArray && jsonArray.length() > 0){
                            for(int i = 0;i < jsonArray.length();i++){
                                JSONArray array = jsonArray.getJSONArray(i);
                                StockInfoEntity _bean = new StockInfoEntity();
                                _bean.setStockNumber(array.getString(0));
                                _bean.setStockName(array.getString(1));
                                if ("-".equals(array.getString(2))) {
                                    _bean.setNewPrice("0");
                                } else {
                                    _bean.setNewPrice(array.getString(2));
                                }
                                if ("-".equals(array.getString(3))) {
                                    _bean.setClose("0");
                                } else {
                                    _bean.setClose(array.getString(3));
                                }
                                //处理涨跌幅和涨跌值
                                double price = Double.parseDouble(_bean.getNewPrice());
                                double close = Double.parseDouble(_bean.getClose());
                                double zdz = price - close;
                                double zdf ;
                                if(close == 0){
                                    zdf = 0;
                                }else{
                                    zdf = zdz / close;
                                }
                                _bean.setUpAndDownValue(zdz);
                                _bean.setPriceChangeRatio(zdf);

                                //判断是不是自选股
                                StockInfoEntity tempBean = Db_PUB_STOCKLIST.queryStockFromID(_bean.getStockNumber());
                                if (tempBean != null) {
                                    _bean.setSelfChoicStock(true);
                                } else {
                                    _bean.setSelfChoicStock(false);
                                }
                                beans.add(_bean);
                            }
                            if (null == beans || beans.size() <= 0) {
                                if (mLoadingDialog != null) {
                                    mLoadingDialog.dismiss();
                                }
                                MistakeDialog.showDialog("不支持该股票设置提醒", getActivity());
                                return;
                            }
                            mStockName = beans.get(0).getStockName();
                            mStockNumber = beans.get(0).getStockNumber();
                            mResultFlag = true;
                            mRemainSearchEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)}); //最大输入长度
                            mAddRainStockNumber = mStockNumber;
                            mStockNumber = mStockNumber.substring(2, mStockNumber.length());
                            String content = mStockName + "\u2000" + mStockNumber;
                            mRemainSearchEdit.setText(content);
                            mRemainSearchEdit.setSelection(content.length());
                            String _newPrice = beans.get(0).getNewPrice();
                            String  _zdf = mFormat2.format(beans.get(0).getPriceChangeRatio());
                            if ("0.0".equals(_newPrice)||"0".equals(_newPrice)) {
                                _newPrice = "-.-";
                                _zdf = "-.-";
                            }
                            mRemainTitle.setText("最新价：" + _newPrice + "\u3000\u3000\u3000涨跌幅：" +_zdf);
                        }
                    }else{
                        MistakeDialog.showDialog("无该只股票", mActivity).show();
                        mRemainSearchEdit.setText("");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof RemainActivity){
            ((RemainActivity)activity).setBackListener(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getActivity() instanceof RemainActivity){
            ((RemainActivity)getActivity()).setBackListener(null);
            ((RemainActivity)getActivity()).setInterception(false);
        }
    }

    @Override
    public void onbackForward() {
        if(mKeyBoardUtil.isShow){
            mKeyBoardUtil.hideSystemKeyBoard();
            mKeyBoardUtil.hideAllKeyBoard();
            mKeyBoardUtil.hideKeyboardLayout();

            if (getActivity() instanceof RemainActivity) {
                ((RemainActivity)getActivity()).setInterception(false);
            }
        }
    }


    @Override
    public void onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getActivity().getCurrentFocus();
            if (v != null && (v instanceof EditText) && mKeyBoardUtil.isShow) {
                ((RemainActivity)getActivity()).setInterception(false);
                mKeyBoardUtil.hideSystemKeyBoard();
                mKeyBoardUtil.hideAllKeyBoard();
                mKeyBoardUtil.hideKeyboardLayout();
            }
        }
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_remain_stockprice;
    }

    private class InputOverListener implements KeyboardUtil.InputFinishListener{

        @Override
        public void inputHasOver(int onclickType, EditText editText) {

        }
    }

    private class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {
        @Override
        public void KeyBoardStateChange(int state, EditText editText) {

            if (!TextUtils.isEmpty(editText.getText().toString()) && editText.getText().length() == 6) {

                if (mLoadingDialog == null) {
                    mLoadingDialog = LoadingDialog.initDialog(mActivity, "正在查询");
                }

                mLoadingDialog.show();

                if(getActivity() instanceof RemainActivity){
                    ((RemainActivity)getActivity()).setInterception(false);
                }

                searchNetStock(editText.getText().toString(),"0");
            }

        }
    }

    private class NormalEditTextListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {//获得焦点
                if(mKeyBoardUtil.isShow){
                    mKeyBoardUtil.hideSystemKeyBoard();
                    mKeyBoardUtil.hideAllKeyBoard();
                    mKeyBoardUtil.hideKeyboardLayout();
                }

                if(getActivity() instanceof RemainActivity){
                    ((RemainActivity)getActivity()).setInterception(false);
                }
            }
        }
    }

    private class KeyboardTouchListener implements View.OnTouchListener{
        private KeyboardUtil keyboardUtil;
        private int keyboardType = 1;
        private int scrollTo = -1;

        public KeyboardTouchListener(KeyboardUtil util,int keyboardType,int scrollTo){
            this.keyboardUtil = util;
            this.keyboardType = keyboardType;
            this.scrollTo = scrollTo;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(getActivity() instanceof RemainActivity){
                ((RemainActivity)getActivity()).setInterception(true);
            }


            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (keyboardUtil != null && keyboardUtil.getEd() !=null &&v.getId() != keyboardUtil.getEd().getId()) {


                    if (mResultFlag && !keyboardUtil.isShow) {
                        mRemainSearchEdit.setText("");
                        mRemainTitle.setText("最新价：" + "-.-" + "\u3000\u3000\u3000涨跌幅：" + "-.-");
                        mResultFlag = false;
                        mRemainSearchEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                    }

                    keyboardUtil.showKeyBoardLayout((EditText) v, keyboardType, scrollTo);
                } else if(keyboardUtil != null && keyboardUtil.getEd() ==null){

                    if (mResultFlag && !keyboardUtil.isShow) {
                        mRemainSearchEdit.setText("");
                        mRemainTitle.setText("最新价：" + "-.-" + "\u3000\u3000\u3000涨跌幅：" + "-.-");
                        mResultFlag = false;
                        mRemainSearchEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                    }

                    keyboardUtil.showKeyBoardLayout((EditText) v,keyboardType,scrollTo);
                }else{
                    if (keyboardUtil != null) {

                        if (mResultFlag && !keyboardUtil.isShow) {
                            mRemainSearchEdit.setText("");
                            mRemainTitle.setText("最新价：" + "-.-" + "\u3000\u3000\u3000涨跌幅：" + "-.-");
                            mResultFlag = false;
                            mRemainSearchEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                        }

                        keyboardUtil.setKeyBoardCursorNew((EditText) v);
                    }
                }


            }
            return false;
        }
    }


    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(final Editable editable) {


            if (editable.length() == 0) {
                mResultFlag = false;
                mRemainSearchEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                mRemainTitle.setText("最新价：" + "-.-" + "\u3000\u3000\u3000涨跌幅：" + "-.-");
            }

            if (mResultFlag) {
                mRemainTitle.setText("最新价：" + "-.-" + "\u3000\u3000\u3000涨跌幅：" + "-.-");
                return;
            }

            // 等到用户输入完数据后进行网络连接模糊查询相关股票
            if (editable.length() > 0) {

                int Len = 6;

                if (editable.length() >= Len) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mStockNumber = editable.toString();
                            searchNetStock(editable.toString(),"0");
                        }
                    }, 300);
                }

            }
        }
    };

}
