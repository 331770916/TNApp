package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.view.OneKeyPopupWindow;
import com.tpyzq.mobile.pangu.activity.trade.view.UpdataeSubscribeLimitPopupWindow;
import com.tpyzq.mobile.pangu.adapter.trade.OneKeySubscribeAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeBean;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeItem;
import com.tpyzq.mobile.pangu.data.OneKeySubscribeListBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 刘泽鹏
 * 一键申购界面
 */
public class OneKeySubscribeActivity extends BaseActivity implements View.OnClickListener
        , OneKeySubscribeAdapter.ICallBackPositionListener, OneKeySubscribeAdapter.ICallBackIdListListener {

    private static final String TAG = "OneKeySubscribe";
    private ImageView imageView_back;
    private ListView listView;
    private View ll_query;
    private ArrayList<OneKeySubscribeItem> list = new ArrayList<OneKeySubscribeItem>();
    private ArrayList<OneKeySubscribeItem> Islist;
    private HashMap<String, String> hashMap;
    private TextView tvOneKeySubscribe, tvHuA, tvHuANum, tvShenA, tvShenANum;
    private OneKeyPopupWindow oneKeyPopupWindow;
    private CheckBox allCheckBox;
    private OneKeySubscribeAdapter adapter;
    private String session, huA, shenA;
    private ImageView ivOneKeyKong;     //无数据显示 空的图片\
    public static FragmentManager fragmentManager;


    @Override
    public void initView() {
        fragmentManager=getFragmentManager();
        this.ivOneKeyKong = (ImageView) this.findViewById(R.id.ivOneKeyKong);
        this.tvHuA = (TextView) this.findViewById(R.id.tvHuA);                //沪A的证券账户
        this.tvHuANum = (TextView) this.findViewById(R.id.tvHuANum);         //沪A的可购买数量
        this.tvShenA = (TextView) this.findViewById(R.id.tvShenA);           //深A的证券账户
        this.tvShenANum = (TextView) this.findViewById(R.id.tvShenANum);     //深A的值可购买数量
        this.imageView_back = (ImageView) this.findViewById(R.id.activityOneKey_back);
        this.imageView_back.setOnClickListener(this);                         //点击返回按钮
        this.ll_query =  this.findViewById(R.id.ll_query);
        this.ll_query.setOnClickListener(this);                        //点击查看详情
        this.tvOneKeySubscribe = (TextView) this.findViewById(R.id.tvOneKeySubscribe);
        this.tvOneKeySubscribe.setOnClickListener(this);                      //点击一键申购
        this.allCheckBox = (CheckBox) this.findViewById(R.id.cbChoice);       //全选按钮


        adapter = new OneKeySubscribeAdapter(this, this);                       //实例化一键申购界面的adapter
        adapter.setList(list);
        this.listView = (ListView) this.findViewById(R.id.lvBubscribeStockList);
        this.listView.setAdapter(adapter);                                     //适配

        adapter.setListener(this);                                              //回调勾选的  id

        /**
         * 获取数据
         */
//        Intent intent = getIntent();
//        session = intent.getStringExtra("session");                             //从上个界面传过来的   token  值
        session = SpUtils.getString(this, "mSession", "");                          //从上个界面传过来的   token  值
        getBuyNumData();                                                         //获取可购买数量的数据
//        getListViewData();                                                       //获取listView的数据

        /**
         * 全选
         */

        this.allCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = allCheckBox.isChecked();
                Log.d("allCheckBox", "onCheckedChanged: " + allCheckBox.isFocusableInTouchMode() + "," + allCheckBox.isFocused() + "," + allCheckBox.isClickable());
                for (OneKeySubscribeItem item : list) {
                    if (isChecked) {
                        item.setCheck(true);
                    } else {
                        item.setCheck(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * 获取 ListView的数据
     */
    private void getListViewData(final String huA, final String shenA) {
        Map map3 = new HashMap();
        Map map4 = new HashMap();
        map4.put("SEC_ID", "tpyzq");             //获取列表中的数据
        map4.put("FLAG", "true");
        map3.put("funcid", "300381");
        map3.put("token", session);
        map3.put("parms", map4);

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map3, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ivOneKeyKong.setVisibility(View.VISIBLE);      //显示空
                CentreToast.showText(OneKeySubscribeActivity.this, ConstantUtil.NETWORK_ERROR);
            }

            @Override
            public void onResponse(String response, int id) {
//                responses = response;
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<OneKeySubscribeListBean>() {
                }.getType();
                OneKeySubscribeListBean bean = gson.fromJson(response, type);
                List<OneKeySubscribeListBean.DataBean> data = bean.getData();
                String code = bean.getCode();
                if (code.equals("-6")) {
                    Intent intent = new Intent(OneKeySubscribeActivity.this, TransactionLoginActivity.class);
                    OneKeySubscribeActivity.this.startActivity(intent);
                    OneKeySubscribeActivity.this.finish();
                } else if (data != null && data.size() > 0 && code.equals("0")) {
                    for (int i = 0; i < data.size(); i++) {
                        OneKeySubscribeListBean.DataBean dataBean = data.get(i);
                        OneKeySubscribeItem item = new OneKeySubscribeItem();
                        item.setId(i);                                  //id
                        item.setName(dataBean.getSTOCK_NAME());         //股票名称
                        item.setCode(dataBean.getSTOCK_CODE());         //股票代码
                        String price = getValue(dataBean.getLAST_PRICE(), 2);//dataBean.getLAST_PRICE().substring(0, dataBean.getLAST_PRICE().indexOf(".") + 3);
                        item.setPrice(price);   //申购价格
                        String num = getValue(dataBean.getHIGH_AMOUNT(), 0);//dataBean.getHIGH_AMOUNT().substring(0, dataBean.getHIGH_AMOUNT().indexOf("."));
                        item.setLimit(num);     //申购上线
                        String market = dataBean.getMARKET();       //市场

                        if (market.equals("1")) {
                            long num_int = Long.parseLong(num);
                            long huA_int = Long.parseLong(huA);
                            if (huA_int <= num_int) {
                                item.setNum(huA);       //申购股数
                            } else {
                                item.setNum(num);       //申购股数
                            }

                        } else if (market.equals("2")) {
                            long num_int = Long.parseLong(num);
                            long shenA_int = Long.parseLong(shenA);
                            if (shenA_int <= num_int) {
                                item.setNum(shenA);       //申购股数
                            } else {
                                item.setNum(num);       //申购股数
                            }

                        }

                        item.setMarket(market);
                        item.setCheck(true);   //是否选中
                        list.add(item);
                    }

                    if (list.size() != 0) {
                        adapter.setList(list);      //添加数据
                        tvOneKeySubscribe.setVisibility(View.VISIBLE);
                    }
                } else if (data == null || data.size() == 0) {   //如果 数据为 空   "data" = [];
                    listView.setVisibility(View.GONE);              //隐藏  listView
                    ivOneKeyKong.setVisibility(View.VISIBLE);      //显示空
                    tvOneKeySubscribe.setVisibility(View.GONE);
                } else {
                    ivOneKeyKong.setVisibility(View.VISIBLE);      //显示空
                    CentreToast.showText(OneKeySubscribeActivity.this, ConstantUtil.SERVICE_NO_DATA);
                }

            }
        });
    }

    private String getValue(String str, int pointCount) {
        String ret = "";
        try {
            DecimalFormat df = new DecimalFormat("#0");
            if (pointCount == 2) {
                df = new DecimalFormat("#0.000");
            }
            ret = df.format(Double.parseDouble(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * //获取可购买数量   的数据
     */
    private void getBuyNumData() {
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map2.put("SEC_ID", "tpyzq");
        map2.put("FLAG", "true");
        map1.put("funcid", "300380");
        map1.put("token", session);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_JY_HS(), map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.toString();
                listView.setVisibility(View.GONE);              //隐藏  listView
                ivOneKeyKong.setVisibility(View.VISIBLE);      //显示空
            }

            @Override
            public void onResponse(String response, int id) {
                if (!response.equals("")) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<OneKeySubscribeBean>() {
                    }.getType();
                    OneKeySubscribeBean bean = gson.fromJson(response, type);
                    List<OneKeySubscribeBean.DataBean> data = bean.getData();
                    String code = bean.getCode();
                    if (code.equals("-6")) {
                        Intent intent = new Intent(OneKeySubscribeActivity.this, TransactionLoginActivity.class);
                        OneKeySubscribeActivity.this.startActivity(intent);
                        OneKeySubscribeActivity.this.finish();
                    } else

//                    if(data == null || data.size() == 0){   //如果 数据为 空   "data" = [];
//                        listView.setVisibility(View.GONE);              //隐藏  listView
//                        ivOneKeyKong.setVisibility(View.VISIBLE);      //显示空
//                    }

                        if (data != null && code.equals("0") && data.size() > 0) {

                            for (int i = 0; i < data.size(); i++) {
                                OneKeySubscribeBean.DataBean dataBean = data.get(i);
                                String market = dataBean.getMARKET();
                                if (market.equals("1")) {
                                    huA = dataBean.getENABLE_AMOUNT().substring(0, dataBean.getENABLE_AMOUNT().indexOf("."));
                                    tvHuANum.setText(huA + "股");
                                } else if (market.equals("2")) {
                                    shenA = dataBean.getENABLE_AMOUNT().substring(0, dataBean.getENABLE_AMOUNT().indexOf("."));
                                    tvShenANum.setText(shenA + "股");
                                }
                            }
                            if (!TextUtils.isEmpty(huA) && !TextUtils.isEmpty(shenA)) {
                                getListViewData(huA, shenA);    //获取listView的数据
                            } else if (!TextUtils.isEmpty(huA) && TextUtils.isEmpty(shenA)) {
                                getListViewData(huA, "0");    //获取listView的数据
                            } else if (TextUtils.isEmpty(huA) && !TextUtils.isEmpty(shenA)) {
                                getListViewData("0", shenA);    //获取listView的数据
                            }
                        } else if (data != null && code.equals("0") && data.size() == 0) {
                            getListViewData("0", "0");    //获取listView的数据
                        } else {
                            CentreToast.showText(OneKeySubscribeActivity.this,ConstantUtil.NETWORK_ERROR);
                        }
                }
            }

        });


    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_one_key_subscribe;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activityOneKey_back:  //点击返回按钮销毁当前Activity
                this.finish();
                break;
            case R.id.ll_query:      //点击查看详情按钮跳转界面
                Intent intent = new Intent();
                intent.setClass(OneKeySubscribeActivity.this, QueryLimitActivity.class);
                intent.putExtra("session", session);
                startActivity(intent);
                break;
            case R.id.tvOneKeySubscribe:    //点击一键申购按钮，弹出确认信息框

                if (Islist == null || Islist.size() == 0) {     //如果没有选中   则不弹出确认信息框
                    CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog("未选择申购股票",CustomCenterDialog.SHOWCENTER);
                    customCenterDialog.show(getFragmentManager(),OneKeySubscribeActivity.class.toString());
                    return;
                }

                //实例化SelectPicPopupWindow
                OneKeyPopupWindow oneKeyPopupWindow = new OneKeyPopupWindow(this, this, Islist);

                oneKeyPopupWindow.setFocusable(true);
                ColorDrawable dw = new ColorDrawable(0xf0000000);     //0x60000000
                oneKeyPopupWindow.setBackgroundDrawable(dw);
                oneKeyPopupWindow.setOutsideTouchable(true);
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.7f;
                getWindow().setAttributes(lp);
                //消失的时候设置窗体背景变亮
                oneKeyPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().setAttributes(lp);
                    }
                });

                //显示窗口
                oneKeyPopupWindow.showAtLocation(this.findViewById(R.id.one_key_subscribe),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
        }
    }

    @Override
    public void callBack(final int position) {
        OneKeySubscribeItem item = list.get(position);
        //实例化  修改数量的  popupWindow
        String data_HuANum  =tvHuANum.getText().toString().trim().replace("股", "");  //沪 总数据
        String data_ShenANum  =tvShenANum.getText().toString().trim().replace("股", "");  //深 总数据


        UpdataeSubscribeLimitPopupWindow updataePopupWindow = new UpdataeSubscribeLimitPopupWindow(this, item, data_HuANum, data_ShenANum, this,
                new UpdataeSubscribeLimitPopupWindow.UpdataSubscribeNumberListener() {
                    @Override
                    public void onClick(OneKeySubscribeItem item) {
                        list.remove(position);
                        list.add(position, item);
                        adapter.setList(list);
                    }
                });

        updataePopupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xf0000000);     //0x60000000
        updataePopupWindow.setBackgroundDrawable(dw);
        updataePopupWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //消失的时候设置窗体背景变亮
        updataePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        //显示窗口
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_one_key_subscribe, null);
        updataePopupWindow.showAtLocation(inflate.findViewById(R.id.one_key_subscribe),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    @Override
    public void callBack() {

        Islist = new ArrayList<>();
        Islist.clear();
        for (OneKeySubscribeItem item : list) {
            Log.d("item.isCheck", "callBack: " + item.getName() + ": " + item.isCheck());
            if (item.isCheck()) {
                Islist.add(item);
            }
        }

        //在不点击全选按钮的情况下，全部选中后全选按钮的显示状态
        if (Islist.size() != list.size()) {
            allCheckBox.setChecked(false);
        } else {
            allCheckBox.setChecked(true);
        }

    }
}
