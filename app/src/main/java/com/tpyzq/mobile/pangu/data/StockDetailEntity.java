package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guohuiz on 2016/10/14.
 * 个股明细页面传递数据实体类
 */
public class StockDetailEntity implements Parcelable {

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockNumber() {
        return stockNumber;
    }

    public void setStockCode(String stockNumber) {
        this.stockNumber = stockNumber;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getIndustryNumber() {
        return industryNumber;
    }

    public void setIndustryNumber(String industryNumber) {
        this.industryNumber = industryNumber;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getUpAndDownValue() {
        return upAndDownValue;
    }

    public void setUpAndDownValue(String upAndDownValue) {
        this.upAndDownValue = upAndDownValue;
    }

    public String getPriceChangeRatio() {
        return priceChangeRatio;
    }

    public void setPriceChangeRatio(String priceChangeRatio) {
        this.priceChangeRatio = priceChangeRatio;
    }

    private String stockName="";           //股票名称
    private String stockNumber="";         //股票代码
    private String newPrice="";            //最新价
    private String close="";               //前收盘价格
    private String industryNumber="";      //行业代码
    private String industryName="";        //行业名称
    private String upAndDownValue="";      //涨跌值
    private String priceChangeRatio="";    //涨跌幅



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stockName);
        dest.writeString(stockNumber);
        dest.writeString(newPrice);
        dest.writeString(close);

        dest.writeString(industryNumber);
        dest.writeString(industryName);
        dest.writeString(upAndDownValue);
        dest.writeString(priceChangeRatio);


    }


    public static final Creator<StockDetailEntity> CREATOR = new Creator<StockDetailEntity>(){
        @Override
        public StockDetailEntity createFromParcel(Parcel source) {
            StockDetailEntity stockInfoEntity = new StockDetailEntity();
            stockInfoEntity.stockName = source.readString();
            stockInfoEntity.stockNumber = source.readString();
            stockInfoEntity.newPrice = source.readString();
            stockInfoEntity.close = source.readString();
            stockInfoEntity.industryNumber = source.readString();
            stockInfoEntity.industryName = source.readString();
            stockInfoEntity.upAndDownValue = source.readString();
            stockInfoEntity.priceChangeRatio = source.readString();

            return stockInfoEntity;
        }

        @Override
        public StockDetailEntity[] newArray(int size) {
            return new StockDetailEntity[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
}
