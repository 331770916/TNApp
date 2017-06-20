package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by 陈新宇 on 2016/8/8.
 */
public class TimeShareEntity {
    public String mStockCode;         //股票名称
    public float mClosePrice;         //前收盘价格
    public String mHighPrice;      //最高
    public String mlowPrice;          //最低
    public String mTotalTranAmount;              //总成交金额
    public String mOuterDisc ;             //内盘
    public String mTurnover;          //外盘
    public String mNowNum;        //现量
    public String mTurnoverRate;               //换手率
    public String mTrade;              //行业
    public String mTradeCode;              //行业代码
    public String mMarketProfitRate;     //市盈率
    public String mFlowOfEquity;         //流通股本
    public String mGeneralCapital;         //总股本
    public String mStockMarketRate;         //市净率
    public String mTradeChangeRate;    //行业涨跌幅
    public String mVolumeRate;          //量比
    public String mVolume;    //成交量
    public String mFromStartSecond;  //距开始还有多少秒
    public String mStockName;  //股票名称

    public String buy1;                //买一
    public String buy2;                //买二
    public String buy3;                //买三
    public String buy4;                //买四
    public String buy5;                //买五

    public String sell1;               //卖一
    public String sell2;               //卖二
    public String sell3;               //卖三
    public String sell4;               //卖四
    public String sell5;               //卖五

    public String buyNumber1;          //买一数量
    public String buyNumber2;          //买二数量
    public String buyNumber3;          //买三数量
    public String buyNumber4;          //买四数量
    public String buyNumber5;          //买五数量

    public String sellNumber1;         //卖一数量
    public String sellNumber2;         //卖二数量
    public String sellNumber3;         //卖三数量
    public String sellNumber4;         //卖四数量
    public String sellNumber5;         //卖五数量

    public List<Stock> stock;
    public class Stock{
        public String time;         //时间
        public float currentPrice;     //当前价格
        public float volume;   //成交量
        public float maTimeSharing;  //均值
    }
}