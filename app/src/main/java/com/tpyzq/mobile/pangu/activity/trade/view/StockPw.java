package com.tpyzq.mobile.pangu.activity.trade.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.TransStockAdapter;
import com.tpyzq.mobile.pangu.data.TransStockEntity;
import com.tpyzq.mobile.pangu.interfac.StockItemCallBack;

import java.util.ArrayList;


/**
 * Created by 陈新宇 on 2016/10/11.
 * 股票列表的PopupWindow
 */
public class StockPw extends PopupWindow {
    private View conentView;
    private ListView lv_stock;
    private Activity context;
    private ArrayList<TransStockEntity> transStockBeen;
    private int width;
    public StockItemCallBack stockItemCallBack;
    private TransStockAdapter transStockAdapter;

    public StockPw(Activity context, ArrayList<TransStockEntity> transStockBeen, int width, StockItemCallBack stockItemCallBack) {
        super(context);
        this.context = context;
        this.width = width;
        this.stockItemCallBack = stockItemCallBack;
        this.transStockBeen = transStockBeen;
        initView(transStockBeen);

    }

    private void initView(ArrayList<TransStockEntity> transStockBeen) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popupwindow_stocklist, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(h / 3);
        // 设置SelectPicPopupWindow弹出窗体可点击
//        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.stock_popwindow_anim);
        lv_stock = (ListView) conentView.findViewById(R.id.lv_stock);
        initData();
    }

    private void initData() {
        transStockAdapter = new TransStockAdapter(context, transStockBeen);
        lv_stock.setAdapter(transStockAdapter);
        lv_stock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stockItemCallBack.stockCode(transStockBeen.get(position).stockName, transStockBeen.get(position).stockCode);
                StockPw.this.dismiss();
            }
        });
    }
    public void refreshView(){
        transStockAdapter.notifyDataSetChanged();
    }
    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        this.showAsDropDown(parent, 0, 0);
    }

}
