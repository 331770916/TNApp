package com.tpyzq.mobile.pangu.data;

/**
 * Created by zhangwenbo on 2016/10/9.
 * 基金份额实体类
 */
public class FundShareEntity {

    private String fundMarketValue;//基金市值
    private String availableMoney;//可用资金
    private String profitLoss; //盈亏


    private String stockName;//基金股票名称
    private String stockCode;//基金股票代码
    private String holdShare;//持有份额
    private String netWorth;//净值
    private String marketValue;//市值

    private String commpanyName;//基金公司名称
    private String commpanyCode;//基金公司代码
    private String freezeNumber;//冻结数量
    private String availableNumber;//可用数量
    private String profitType;//分红方式
    private String costPrice;//成本价
    private boolean unfold;//是否展开


    public String getFundMarketValue() {
        return fundMarketValue;
    }

    public void setFundMarketValue(String fundMarketValue) {
        this.fundMarketValue = fundMarketValue;
    }

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }

    public String getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(String availableMoney) {
        this.availableMoney = availableMoney;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getHoldShare() {
        return holdShare;
    }

    public void setHoldShare(String holdShare) {
        this.holdShare = holdShare;
    }

    public String getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(String netWorth) {
        this.netWorth = netWorth;
    }

    public String getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    public String getCommpanyName() {
        return commpanyName;
    }

    public void setCommpanyName(String commpanyName) {
        this.commpanyName = commpanyName;
    }

    public String getCommpanyCode() {
        return commpanyCode;
    }

    public void setCommpanyCode(String commpanyCode) {
        this.commpanyCode = commpanyCode;
    }

    public String getFreezeNumber() {
        return freezeNumber;
    }

    public void setFreezeNumber(String freezeNumber) {
        this.freezeNumber = freezeNumber;
    }

    public String getAvailableNumber() {
        return availableNumber;
    }

    public void setAvailableNumber(String availableNumber) {
        this.availableNumber = availableNumber;
    }

    public String getProfitType() {
        return profitType;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public boolean isUnfold() {
        return unfold;
    }

    public void setUnfold(boolean unfold) {
        this.unfold = unfold;
    }
}
