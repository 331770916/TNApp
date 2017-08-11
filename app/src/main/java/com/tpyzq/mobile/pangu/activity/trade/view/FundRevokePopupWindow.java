package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;


/**
 * Created by 刘泽鹏 on 2016/8/12.
 * 货币基金撤单  信息确认  popupWindow
 */
public class FundRevokePopupWindow extends PopupWindow{

    private static final String TAG = "FundRevokePopupWindow";
    private Context context;
    private View popupWindow;
    private TextView tvRevokeName,tvWeiTuoShiJian,tvShenGouShuHui,tvShenGouShuLiang,tvWeiTuoJinE=null;
    private HashMap<String, String> map;
    private IClick click;
    private String tv_market;
    private String entrust_no;
    private int  position;
    private String mSession;
    private Activity mActivity;

    public FundRevokePopupWindow(final Context context, Activity activity, HashMap<String,String> map, final int position, final IClick click ) {
        super(context);
        mSession = SpUtils.getString(context, "mSession", "");
        this.context=context;
        this.mActivity = activity;
        this.map=map;
        this.click=click;
        this.position=position;
        tv_market=map.get("tv_market");     //市场
        entrust_no=map.get("entrust_no");     //市场
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupWindow=inflater.inflate(R.layout.fund_revoke_popupwindow, null);

        tvRevokeName= (TextView) popupWindow.findViewById(R.id.tvRevokeName);               //基金名称
        tvRevokeName.setText(map.get("tv_stockName"));

        tvWeiTuoShiJian= (TextView) popupWindow.findViewById(R.id.tvWeiTuoShiJian);        //委托时间
        tvWeiTuoShiJian.setText(map.get("tv_Time"));

        tvShenGouShuHui= (TextView) popupWindow.findViewById(R.id.tvShenGouShuHui);        //申购赎回
        tvShenGouShuHui.setText(map.get("tv_type"));

        tvShenGouShuLiang= (TextView) popupWindow.findViewById(R.id.tvShenGouShuLiang);    //申购数量
        tvShenGouShuLiang.setText(map.get("tv_EntrustNumber"));

        tvWeiTuoJinE= (TextView) popupWindow.findViewById(R.id.tvWeiTuoJinE);              //申购金额
        tvWeiTuoJinE.setText(map.get("tv_EntrustMoney"));

        //点击确定
        popupWindow.findViewById(R.id.bnQueDing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                login();    //登陆赎回
                commit(mSession);//申购撤回

            }
        });

        //点击取消
        popupWindow.findViewById(R.id.bnQuXiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FundRevokePopupWindow.this.dismiss();
            }
        });

        this.setContentView(popupWindow);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
    }


    private void commit(String mSession) {
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map2.put("SEC_ID","tpyzq");
        map2.put("FLAG","true");
        map2.put("MARKET",tv_market);
        map2.put("ENTRUST_NO",entrust_no);
        map1.put("funcid","300150");
        map1.put("token",mSession);
        map1.put("parms",map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CentreToast.showText(context,ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
                if(TextUtils.isEmpty(response)){
                    return;
                }
                try{
                    FundRevokePopupWindow.this.dismiss();       //销毁当前popupWindow
                    JSONObject res = new JSONObject(response);
                    String code = res.optString("code");
                    if("0".equals(code)){
                        CentreToast.showText(context,"委托已提交",true);
                        click.OnClickListener(position);            //回调给 acitity  使刷新数据源
//                        FundRevokePopupWindow.this.dismiss();       //销毁当前popupWindow
                    }else if("-6".equals(code)){
                        Intent intent = new Intent(context, TransactionLoginActivity.class);
                        context.startActivity(intent);
//                        dismiss();
                    }else{
                        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(res.optString("msg"),CustomCenterDialog.SHOWCENTER);
                        customCenterDialog.show(mActivity.getFragmentManager(),FundRevokePopupWindow.class.toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                /**
                Gson gson = new Gson();
                Type type = new TypeToken<CurrencyFundCheDanCommit>() {}.getType();
                CurrencyFundCheDanCommit bean = gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                if(code.equals("-6")){
                    Intent intent = new Intent(context, TransactionLogin_First.class);
                    context.startActivity(intent);
                    dismiss();
                    ((Activity)context).finish();
                }else
                if(code.equals("0")){
                    ResultDialog.getInstance().show("委托已提交", R.mipmap.duigou);
                    click.OnClickListener(position);            //回调给 acitity  使刷新数据源
                    FundRevokePopupWindow.this.dismiss();       //销毁当前popupWindow

                }else {
                    MistakeDialog.showDialog(msg, mActivity);

                    FundRevokePopupWindow.this.dismiss();       //销毁当前popupWindow
                }
                */
            }
        });
    }

    public interface IClick{
        void OnClickListener(int position);
    }
}
