package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.adapter.trade.OneKeyPopupWindowAdapter;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeCommit;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeItem;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 刘泽鹏 on 2016/8/10.
 * 确认申购信息  弹出界面
 */
public class OneKeyPopupWindow extends PopupWindow implements View.OnClickListener {

    private static final String TAG = "OneKeyPopupWindow";
    private Context context;
    private View popupWindow;
    private TextView ok, close;
    private ListView listView = null;
    private String session;
    private ArrayList<OneKeySubscribeItem> list;
    private Activity mActivity;

    public OneKeyPopupWindow(Activity context, Activity activity, ArrayList<OneKeySubscribeItem> list) {
        super(context);
        session = SpUtils.getString(context, "mSession", "");
        this.context=context;
        this.mActivity=activity;
        this.list=list;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupWindow = inflater.inflate(R.layout.one_key_subscribe_popupwindow, null);       //找到popupWindow
        ok = (TextView) popupWindow.findViewById(R.id.tvOK);                                   //确定按钮
        close = (TextView) popupWindow.findViewById(R.id.tvClose);                            //取消按钮
        listView = (ListView) popupWindow.findViewById(R.id.lvSubscribeMsgAffirm);          //listView

        OneKeyPopupWindowAdapter listViewAdapter = new OneKeyPopupWindowAdapter(context);       //实例化 适配器
        listViewAdapter.setList(list);                                                          //添加数据源
        this.listView.setAdapter(listViewAdapter);                                             //适配

        ok.setOnClickListener(this);                                                            //点击确定
        close.setOnClickListener(this);                                                         //点击取消



        this.setContentView(popupWindow);
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
        switch (v.getId()){
            case R.id.tvOK:
                commit();
                break;
            case R.id.tvClose:
                dismiss();
                break;
        }
    }

    private HashMap[] stockList;
    private void commit() {
        stockList = new HashMap[list.size()];
        for(int i=0;i<list.size();i++){
            OneKeySubscribeItem item = list.get(i);
            HashMap map = new HashMap();
            map.put("MARKET",item.getMarket());
            map.put("SECU_CODE",item.getCode());
            map.put("ORDER_PRICE",item.getPrice());
            map.put("ORDER_QTY",item.getNum());
            stockList[i]=map;
        }

        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map2.put("STOCK_LIST", stockList);
        map1.put("funcid", "300382");
        map1.put("token", session);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OneKeySubscribeCommit>() {}.getType();
                OneKeySubscribeCommit bean= gson.fromJson(response, type);
                String code = bean.getCode();
                String msg = bean.getMsg();
                if("-6".endsWith(code)){
                    Intent intent = new Intent(context, TransactionLoginActivity.class);
                    context.startActivity(intent);
                    dismiss();
                    ((Activity)context).finish();
                }else
                if ("0".endsWith(code)) {
//                    ResultDialog.getInstance().show("委托已提交", R.mipmap.duigou);
                    CentreToast.showText(context,"委托已提交",true);
                    dismiss();
                }else {
                    MistakeDialog.showDialog(msg, mActivity);
                    dismiss();
                }
            }
        });
    }
}
