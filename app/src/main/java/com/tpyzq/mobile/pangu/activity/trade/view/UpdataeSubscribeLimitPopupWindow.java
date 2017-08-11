package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.trade.stock.OneKeySubscribeActivity;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeItem;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;


/**
 * Created by 刘泽鹏 on 2016/8/10.
 * 修改  申购额度的   popupWindow
 */
public class UpdataeSubscribeLimitPopupWindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private View updataLimitWindow = null;    //弹出的窗口
    private LinearLayout llJianShao, llTianJian = null;      //减少和添加按钮
    private TextView tvQueDing, tvQuXiao = null;       //确定和取消按钮
    private EditText etUpdatedSubscribeLimit = null;
    private OneKeySubscribeItem item;
    private String num, market, mData_HuANum, mData_ShenANum;
    private long oldNum;
    private UpdataSubscribeNumberListener mListener;
    private Activity mActivity;


    public UpdataeSubscribeLimitPopupWindow(final Context context, final OneKeySubscribeItem item, String data_HuANum, String data_ShenANum, Activity activity, UpdataSubscribeNumberListener listener) {
        this.context = context;
        this.item = item;
        this.mListener = listener;
        this.mActivity = activity;
        this.mData_HuANum = data_HuANum;
        this.mData_ShenANum = data_ShenANum;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        updataLimitWindow = inflater.inflate(R.layout.one_key_updated_subscribe_limit, null);
        this.etUpdatedSubscribeLimit = (EditText) updataLimitWindow.findViewById(R.id.etUpdatedSubscribeLimit);


        market = item.getMarket();
        num = item.getNum();                //通过数据源拿到对应的    Num
        if (market.equals("1")) {
            String limit = mData_HuANum;     //拿到最大上限

            if (limit != null) {
                oldNum = Long.parseLong(limit);
            }
        } else if (market.equals("2")) {
            String limit = data_ShenANum;     //拿到最大上限

            if (limit != null) {
                oldNum = Long.parseLong(limit);
            }
        }
        if (num.equals("0")){
            this.etUpdatedSubscribeLimit.setText("");
        }else {
            this.etUpdatedSubscribeLimit.setText(num);
        }

        this.etUpdatedSubscribeLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = etUpdatedSubscribeLimit.getText().toString();
                if (str.indexOf('0') == 0) {
                    etUpdatedSubscribeLimit.setText("");
                    CentreToast.showText(context, "首位不能为0");
                }

                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(num)) {
//                    int newS = Integer.parseInt(s.toString());
                    long newS = Long.parseLong(s.toString());

                    if (newS > oldNum) {
                        tvQueDing.setEnabled(false);
                        CentreToast.showText(mActivity, "超出申购股数");
                    } else {
                        tvQueDing.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                num = s.toString();
            }
        });

        llJianShao = (LinearLayout) updataLimitWindow.findViewById(R.id.llJianShao);        //减号
        llJianShao.setOnClickListener(this);

        llTianJian = (LinearLayout) updataLimitWindow.findViewById(R.id.llTianJia);      //加号
        llTianJian.setOnClickListener(this);

        tvQueDing = (TextView) updataLimitWindow.findViewById(R.id.tvQuDing);            //确定
        tvQueDing.setOnClickListener(this);

        tvQuXiao = (TextView) updataLimitWindow.findViewById(R.id.tvQuXiao);             //取消
        tvQuXiao.setOnClickListener(this);

        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setContentView(updataLimitWindow);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llJianShao:   //减号
                if (!TextUtils.isEmpty(num)) {
//                    Integer number = Integer.valueOf(num);
                    Long number = Long.parseLong(num);
                    //根据市场判断   沪市一次减1000  深市一次减500
                    if (market.equals("1")) {
                        number -= 1000;
                    } else if (market.equals("2")) {
                        number -= 500;
                    }

                    if (number > 0) {
                        num = String.valueOf(number);
                        this.etUpdatedSubscribeLimit.setText(num);
                    } else {
                        // TODO: 2017/8/9
                        showDialog("当前无法更改数量");
                    }
                } else {
                    showDialog("申购股数不能空");
                }

                break;
            case R.id.llTianJia:    //加号
                if (!TextUtils.isEmpty(num)) {
//                    Integer number = Integer.valueOf(num);
                    Long number = Long.parseLong(num);
                    //根据市场判断   沪市一次加1000  深市一次加500
                    if (market.equals("1")) {
                        number += 1000;
                    } else if (market.equals("2")) {
                        number += 500;
                    }

                    if (number < oldNum + 1) {
                        num = String.valueOf(number);
                        this.etUpdatedSubscribeLimit.setText(num);
                    } else {
                        // TODO: 2017/8/9
                        showDialog("当前无法更改数量");
                    }
                } else {
                    showDialog("申购股数不能空");
                }
                break;
            case R.id.tvQuDing:     //确定
                if (!TextUtils.isEmpty(num)) {
//                    int changeNum = Integer.parseInt(num);
                    Long changeNum = Long.parseLong(num);
                    if (market.equals("1")) {
                        if (changeNum % 1000 == 0) {
                            if (changeNum > oldNum) {
                                showDialog("您输入的申购股数不能大于最高申购数");
                                dismiss();
                            }
                            item.setNum(num);
                            mListener.onClick(item);
                            dismiss();
                        } else {
                            showDialog("申购股数必须为1000的整数倍");
                            dismiss();
                        }
                    } else if (market.equals("2")) {
                        if (changeNum % 500 == 0) {
                            if (changeNum > oldNum) {
                                showDialog("您输入的申购股数不能大于最高申购数");
                                dismiss();
                            }
                            item.setNum(num);
                            mListener.onClick(item);
                            dismiss();

                        } else {
                            showDialog("申购股数必须为500的整数倍");
                            dismiss();
                        }
                    }
                } else {
                    showDialog("申购股数不能空");
                }
                break;
            case R.id.tvQuXiao:     //取消
                dismiss();//销毁当前弹框
                break;
        }
    }

    public void showDialog(String msg){
        CustomCenterDialog customCenterDialog =CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(OneKeySubscribeActivity.fragmentManager,UpdataeSubscribeLimitPopupWindow.class.toString());
    }

    public interface UpdataSubscribeNumberListener {
        void onClick(OneKeySubscribeItem item);
    }
}
