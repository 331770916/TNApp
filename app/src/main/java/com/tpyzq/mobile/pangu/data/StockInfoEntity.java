package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwenbo on 2016/7/14.
 * 更多  指数， 板块列表数据实体类
 */
public class StockInfoEntity implements Parcelable {

    private String stockName;           //股票名称
    private String stockNumber;         //股票代码
    private String time;                //时间
    private String newPrice;            //最新价
    private String close;               //前收盘价格
    private String industryNumber;      //行业代码
    private String industryName;        //行业名称
    private double upAndDownValue = 0;      //涨跌值
    private double priceChangeRatio=0;    //涨跌幅
    private String industryUpAndDown;   //行业涨跌幅
    private String priceToal;           //资金流向
    private String equivalentRatio;     //量比
    private String flowEquity;          //流通股本
    private String totalEquity;         //总股本
    private String pbr;                 //市净率
    private String presentAmount;       //现量
    private String turnover;            //换手率
    private String peg;                 //市盈率
    private String inflowMoney;         //流入金额
    private String outflowMoney;        //流出金额
    private String buyOrder;            //内盘
    private String sellOrder;           //外盘
    private String date;                //日期
    private String high;                //最高
    private String low;                 //最低
    private String totalVolume;         //总成交量
    private String totalPrice;          //总成交金额
    private String todayOpenPrice;      //今开盘价
    private String trade;               //类型 0－沪深A，1－沪深B，2－沪深基金，3－沪深债券，4－深证A，5－上证A，6－中小板，7－创业板，8－指数，9－退市整理，10－风险警示
    private String plate;               //板块 0 行业板块 1 概念板块 2 地域板块]
    private String hot;                 //热搜
    private String hold;                //持仓
    private String selectdate;          //删除自选股时间
    private String see;                 //判断是浏览过的股票  0 表示浏览过的股票
    private String seeDate;             //浏览时间
    private String select;              //判断是历史自选股票  0 历史自选股票
    private int foundFlag;           //资金流入和资金流出的标志位 1000是资金流入， 1001资金流入
    private int isChangeHand;        //是否是换手率 1003是换手率项 1004不是

    private String buy1;                //买1
    private String buy2;                //买2
    private String buy3;                //买3
    private String buy4;                //买4
    private String buy5;                //买5
    private String sell1;               //卖1
    private String sell2;               //卖2
    private String sell3;               //卖3
    private String sell4;               //卖4
    private String sell5;               //卖5

    private String buy1Amount;          //买1数量
    private String buy2Amount;          //买2数量
    private String buy3Amount;          //买3数量
    private String buy4Amount;          //买4数量
    private String buy5Amount;          //买5数量
    private String sell1Amount;         //卖1数量
    private String sell2Amount;         //卖2数量
    private String sell3Amount;         //卖3数量
    private String sell4Amount;         //卖4数量
    private String sell5Amount;         //卖5数量


    private boolean isSelfChoicStock;   //是否是自选股
    private boolean isAdded;            //是否已添加数据库
    private String isHoldStock;        //是否是持仓股票
    private String apperHoldStock;     //是否显示持仓
    private String stockholdon;        //是否是持仓股票
    private String isHotStock;         //是否热搜

    //持仓

    private String MKT_VAL;             //市值
    private String SECU_ACC;            //股东代码
    private String SHARE_BLN;           //股份余额
    private String SHARE_AVL;           //股份可卖数量(可用数量)
    private String SHARE_QTY;           //股份总数量(持有数量)
    private String PROFIT_RATIO;        //盈亏比
    private String MARKET;              //交易市场
    private String BRANCH;              //分支机构
    private String MKT_PRICE;           //现价
    private String SECU_CODE;           //证券代码
    private String INCOME_AMT;          //盈亏金额
    private String SECU_NAME;           //证券代码
    private String CURRENT_COST;        //当前成本价


    private List<StockInfoEntity> subDatas;//子数据
    private String totalCount;          // 返回数据数量
    private JSONArray data;    // 数组数据
    private String code;                // 是否成功的code
    private String type;                // 类型(listvie中不同布局的类型)
    private String titleTv;             // 类型名称
    private int bytes;               //返回字节数
    private boolean isChecked;          //编辑自选股是否被选择
    private String remainType;          //提醒类型


    //热搜
    private String message;     //返回数据
    private String viewcount;   //浏览次数
    private String timestamp;   //计算时间
    private String curprice;
    private String read;

    public String getMKT_VAL() {
        return MKT_VAL;
    }

    public void setMKT_VAL(String MKT_VAL) {
        this.MKT_VAL = MKT_VAL;
    }

    public String getSECU_ACC() {
        return SECU_ACC;
    }

    public void setSECU_ACC(String SECU_ACC) {
        this.SECU_ACC = SECU_ACC;
    }

    public String getSHARE_BLN() {
        return SHARE_BLN;
    }

    public void setSHARE_BLN(String SHARE_BLN) {
        this.SHARE_BLN = SHARE_BLN;
    }

    public String getSHARE_AVL() {
        return SHARE_AVL;
    }

    public void setSHARE_AVL(String SHARE_AVL) {
        this.SHARE_AVL = SHARE_AVL;
    }

    public String getSHARE_QTY() {
        return SHARE_QTY;
    }

    public void setSHARE_QTY(String SHARE_QTY) {
        this.SHARE_QTY = SHARE_QTY;
    }

    public String getPROFIT_RATIO() {
        return PROFIT_RATIO;
    }

    public void setPROFIT_RATIO(String PROFIT_RATIO) {
        this.PROFIT_RATIO = PROFIT_RATIO;
    }

    public String getMARKET() {
        return MARKET;
    }

    public void setMARKET(String MARKET) {
        this.MARKET = MARKET;
    }

    public String getBRANCH() {
        return BRANCH;
    }

    public void setBRANCH(String BRANCH) {
        this.BRANCH = BRANCH;
    }

    public String getMKT_PRICE() {
        return MKT_PRICE;
    }

    public void setMKT_PRICE(String MKT_PRICE) {
        this.MKT_PRICE = MKT_PRICE;
    }

    public String getSECU_CODE() {
        return SECU_CODE;
    }

    public void setSECU_CODE(String SECU_CODE) {
        this.SECU_CODE = SECU_CODE;
    }

    public String getINCOME_AMT() {
        return INCOME_AMT;
    }

    public void setINCOME_AMT(String INCOME_AMT) {
        this.INCOME_AMT = INCOME_AMT;
    }

    public String getSECU_NAME() {
        return SECU_NAME;
    }

    public void setSECU_NAME(String SECU_NAME) {
        this.SECU_NAME = SECU_NAME;
    }

    public String getCURRENT_COST() {
        return CURRENT_COST;
    }

    public void setCURRENT_COST(String CURRENT_COST) {
        this.CURRENT_COST = CURRENT_COST;
    }

    public String getIsHoldStock() {
        return isHoldStock;
    }

    public void setIsHoldStock(String isHoldStock) {
        this.isHoldStock = isHoldStock;
    }

    public String getApperHoldStock() {
        return apperHoldStock;
    }

    public void setApperHoldStock(String apperHoldStock) {
        this.apperHoldStock = apperHoldStock;
    }

    public double getUpAndDownValue() {
        return upAndDownValue;
    }

    public void setUpAndDownValue(double upAndDownValue) {
        this.upAndDownValue = upAndDownValue;
    }

    public void setRemainType(String remainType) {
        this.remainType = remainType;
    }

    public String getRemainType() {
        return remainType;
    }

    public int getIsChangeHand() {
        return isChangeHand;
    }

    public void setIsChangeHand(int changeHand) {
        isChangeHand = changeHand;
    }

    public int getFoundFlag() {
        return foundFlag;
    }

    public void setFoundFlag(int foundFlag) {
        this.foundFlag = foundFlag;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        isChecked = checked;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public int getBytes() {
        return bytes;
    }

    public void setSelfChoicStock(boolean selfChoicStock) {
        isSelfChoicStock = selfChoicStock;
    }

    public boolean getIsSelfChoiceStock() {
        return isSelfChoicStock;
    }

    public void setSubDatas(ArrayList<StockInfoEntity> subDatas) {
        this.subDatas = subDatas;
    }

    public List<StockInfoEntity> getSubDatas() {
        return subDatas;
    }

    public void setTitleTv(String titleTv) {
        this.titleTv = titleTv;
    }

    public String getTitleTv() {
        return titleTv;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public void setPriceToal(String priceToal) {
        this.priceToal = priceToal;
    }

    public String getPriceToal() {
        return priceToal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockName() {
        return stockName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public void setIndustryNumber(String industryNumber) {
        this.industryNumber = industryNumber;
    }

    public void setPriceChangeRatio(double priceChangeRatio) {
        this.priceChangeRatio = priceChangeRatio;
    }

    public String getIndustryName() {
        return industryName;
    }

    public double getPriceChangeRatio() {
        return priceChangeRatio;
    }

    public String getIndustryNumber() {
        return industryNumber;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public void setStockNumber(String stockNumber) {
        this.stockNumber = stockNumber;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public JSONArray getData() {
        return data;
    }

    public String getClose() {
        return close;
    }

    public String getCode() {
        return code;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public String getStockNumber() {
        return stockNumber;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public String getTime() {
        return time;
    }

    public String getEquivalentRatio() {
        return equivalentRatio;
    }

    public void setEquivalentRatio(String equivalentRatio) {
        this.equivalentRatio = equivalentRatio;
    }

    public String getFlowEquity() {
        return flowEquity;
    }

    public void setFlowEquity(String flowEquity) {
        this.flowEquity = flowEquity;
    }

    public String getTotalEquity() {
        return totalEquity;
    }

    public void setTotalEquity(String totalEquity) {
        this.totalEquity = totalEquity;
    }

    public String getPbr() {
        return pbr;
    }

    public void setPbr(String pbr) {
        this.pbr = pbr;
    }

    public String getPresentAmount() {
        return presentAmount;
    }

    public void setPresentAmount(String presentAmount) {
        this.presentAmount = presentAmount;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getPeg() {
        return peg;
    }

    public void setPeg(String peg) {
        this.peg = peg;
    }

    public String getInflowMoney() {
        return inflowMoney;
    }

    public void setInflowMoney(String inflowMoney) {
        this.inflowMoney = inflowMoney;
    }

    public String getOutflowMoney() {
        return outflowMoney;
    }

    public void setOutflowMoney(String outflowMoney) {
        this.outflowMoney = outflowMoney;
    }

    public String getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(String buyOrder) {
        this.buyOrder = buyOrder;
    }

    public String getSellOrder() {
        return sellOrder;
    }

    public void setSellOrder(String sellOrder) {
        this.sellOrder = sellOrder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTodayOpenPrice() {
        return todayOpenPrice;
    }

    public void setTodayOpenPrice(String todayOpenPrice) {
        this.todayOpenPrice = todayOpenPrice;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBuy1() {
        return buy1;
    }

    public void setBuy1(String buy1) {
        this.buy1 = buy1;
    }

    public String getBuy2() {
        return buy2;
    }

    public void setBuy2(String buy2) {
        this.buy2 = buy2;
    }

    public String getBuy3() {
        return buy3;
    }

    public void setBuy3(String buy3) {
        this.buy3 = buy3;
    }

    public String getBuy4() {
        return buy4;
    }

    public void setBuy4(String buy4) {
        this.buy4 = buy4;
    }

    public String getBuy5() {
        return buy5;
    }

    public void setBuy5(String buy5) {
        this.buy5 = buy5;
    }

    public String getSell1() {
        return sell1;
    }

    public void setSell1(String sell1) {
        this.sell1 = sell1;
    }

    public String getSell2() {
        return sell2;
    }

    public void setSell2(String sell2) {
        this.sell2 = sell2;
    }

    public String getSell3() {
        return sell3;
    }

    public void setSell3(String sell3) {
        this.sell3 = sell3;
    }

    public String getSell4() {
        return sell4;
    }

    public void setSell4(String sell4) {
        this.sell4 = sell4;
    }

    public String getSell5() {
        return sell5;
    }

    public void setSell5(String sell5) {
        this.sell5 = sell5;
    }

    public String getBuy1Amount() {
        return buy1Amount;
    }

    public void setBuy1Amount(String buy1Amount) {
        this.buy1Amount = buy1Amount;
    }

    public String getBuy2Amount() {
        return buy2Amount;
    }

    public void setBuy2Amount(String buy2Amount) {
        this.buy2Amount = buy2Amount;
    }

    public String getBuy3Amount() {
        return buy3Amount;
    }

    public void setBuy3Amount(String buy3Amount) {
        this.buy3Amount = buy3Amount;
    }

    public String getBuy4Amount() {
        return buy4Amount;
    }

    public void setBuy4Amount(String buy4Amount) {
        this.buy4Amount = buy4Amount;
    }

    public String getBuy5Amount() {
        return buy5Amount;
    }

    public void setBuy5Amount(String buy5Amount) {
        this.buy5Amount = buy5Amount;
    }

    public String getSell1Amount() {
        return sell1Amount;
    }

    public void setSell1Amount(String sell1Amount) {
        this.sell1Amount = sell1Amount;
    }

    public String getSell2Amount() {
        return sell2Amount;
    }

    public void setSell2Amount(String sell2Amount) {
        this.sell2Amount = sell2Amount;
    }

    public String getSell3Amount() {
        return sell3Amount;
    }

    public void setSell3Amount(String sell3Amount) {
        this.sell3Amount = sell3Amount;
    }

    public String getSell4Amount() {
        return sell4Amount;
    }

    public void setSell4Amount(String sell4Amount) {
        this.sell4Amount = sell4Amount;
    }

    public String getSell5Amount() {
        return sell5Amount;
    }

    public void setSell5Amount(String sell5Amount) {
        this.sell5Amount = sell5Amount;
    }

    public String getIndustryUpAndDown() {
        return industryUpAndDown;
    }

    public void setIndustryUpAndDown(String industryUpAndDown) {
        this.industryUpAndDown = industryUpAndDown;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getHold() {
        return hold;
    }

    public void setHold(String hold) {
        this.hold = hold;
    }

    public String getSelectdate() {
        return selectdate;
    }

    public void setSelectdate(String selectdate) {
        this.selectdate = selectdate;
    }

    public String getSee() {
        return see;
    }

    public void setSee(String see) {
        this.see = see;
    }

    public String getSeeDate() {
        return seeDate;
    }

    public void setSeeDate(String seeDate) {
        this.seeDate = seeDate;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getIsHotStock() {
        return isHotStock;
    }

    public void setIsHotStock(String isHotStock) {
        this.isHotStock = isHotStock;
    }

    public String getStockholdon() {
        return stockholdon;
    }

    public void setStockholdon(String stockholdon) {
        this.stockholdon = stockholdon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getViewcount() {
        return viewcount;
    }

    public void setViewcount(String viewcount) {
        this.viewcount = viewcount;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurprice() {
        return curprice;
    }

    public void setCurprice(String curprice) {
        this.curprice = curprice;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stockName);
        dest.writeString(stockNumber);
        dest.writeString(time);
        dest.writeString(newPrice);
        dest.writeString(close);

        dest.writeString(industryNumber);
        dest.writeString(industryName);
        dest.writeDouble(upAndDownValue);
        dest.writeDouble(priceChangeRatio);
        dest.writeString(industryUpAndDown);
        dest.writeString(priceToal);

        dest.writeString(equivalentRatio);
        dest.writeString(flowEquity);
        dest.writeString(totalEquity);
        dest.writeString(pbr);
        dest.writeString(presentAmount);
        dest.writeString(turnover);
        dest.writeString(peg);

        dest.writeString(inflowMoney);
        dest.writeString(outflowMoney);
        dest.writeString(buyOrder);
        dest.writeString(sellOrder);
        dest.writeString(date);
        dest.writeString(high);
        dest.writeString(low);

        dest.writeString(totalVolume);
        dest.writeString(totalPrice);
        dest.writeString(todayOpenPrice);
        dest.writeString(trade);
        dest.writeString(plate);
        dest.writeString(hot);
        dest.writeString(hold);


        dest.writeString(selectdate);
        dest.writeString(see);
        dest.writeString(seeDate);
        dest.writeString(select);
        dest.writeInt(foundFlag);
        dest.writeInt(isChangeHand);


        dest.writeString(buy1);
        dest.writeString(buy2);
        dest.writeString(buy3);
        dest.writeString(buy4);
        dest.writeString(buy5);
        dest.writeString(sell1);
        dest.writeString(sell2);
        dest.writeString(sell3);
        dest.writeString(sell4);
        dest.writeString(sell5);

        dest.writeString(buy1Amount);
        dest.writeString(buy2Amount);
        dest.writeString(buy3Amount);
        dest.writeString(buy4Amount);
        dest.writeString(buy5Amount);
        dest.writeString(sell1Amount);
        dest.writeString(sell2Amount);
        dest.writeString(sell3Amount);
        dest.writeString(sell4Amount);
        dest.writeString(sell5Amount);
        dest.writeString(isHoldStock);
        dest.writeString(apperHoldStock);
        dest.writeString(stockholdon);
        dest.writeString(isHotStock);

        dest.writeString(MKT_VAL);
        dest.writeString(SECU_ACC);
        dest.writeString(SHARE_BLN);
        dest.writeString(SHARE_AVL);
        dest.writeString(SHARE_QTY);
        dest.writeString(PROFIT_RATIO);
        dest.writeString(MARKET);
        dest.writeString(BRANCH);
        dest.writeString(MKT_PRICE);
        dest.writeString(SECU_CODE);
        dest.writeString(INCOME_AMT);
        dest.writeString(SECU_NAME);
        dest.writeString(CURRENT_COST);

        dest.writeString(message);
        dest.writeString(viewcount);
        dest.writeString(timestamp);
        dest.writeString(curprice);
        dest.writeString(read);
    }


    public static final Creator<StockInfoEntity> CREATOR = new Creator<StockInfoEntity>() {
        @Override
        public StockInfoEntity createFromParcel(Parcel source) {
            StockInfoEntity stockInfoEntity = new StockInfoEntity();
            stockInfoEntity.stockName = source.readString();
            stockInfoEntity.stockNumber = source.readString();
            stockInfoEntity.time = source.readString();
            stockInfoEntity.newPrice = source.readString();
            stockInfoEntity.close = source.readString();
            stockInfoEntity.industryNumber = source.readString();
            stockInfoEntity.industryName = source.readString();
            stockInfoEntity.upAndDownValue = source.readDouble();
            stockInfoEntity.priceChangeRatio = source.readDouble();
            stockInfoEntity.industryUpAndDown = source.readString();
            stockInfoEntity.priceToal = source.readString();


            stockInfoEntity.equivalentRatio = source.readString();
            stockInfoEntity.flowEquity = source.readString();
            stockInfoEntity.totalEquity = source.readString();
            stockInfoEntity.pbr = source.readString();
            stockInfoEntity.presentAmount = source.readString();
            stockInfoEntity.turnover = source.readString();
            stockInfoEntity.peg = source.readString();
            stockInfoEntity.inflowMoney = source.readString();
            stockInfoEntity.outflowMoney = source.readString();
            stockInfoEntity.buyOrder = source.readString();
            stockInfoEntity.sellOrder = source.readString();
            stockInfoEntity.date = source.readString();
            stockInfoEntity.high = source.readString();
            stockInfoEntity.low = source.readString();
            stockInfoEntity.totalVolume = source.readString();
            stockInfoEntity.totalPrice = source.readString();
            stockInfoEntity.todayOpenPrice = source.readString();
            stockInfoEntity.trade = source.readString();
            stockInfoEntity.plate = source.readString();
            stockInfoEntity.hot = source.readString();
            stockInfoEntity.hold = source.readString();
            stockInfoEntity.selectdate = source.readString();
            stockInfoEntity.see = source.readString();
            stockInfoEntity.seeDate = source.readString();
            stockInfoEntity.select = source.readString();
            stockInfoEntity.foundFlag = source.readInt();
            stockInfoEntity.isChangeHand = source.readInt();

            stockInfoEntity.buy1 = source.readString();
            stockInfoEntity.buy2 = source.readString();
            stockInfoEntity.buy3 = source.readString();
            stockInfoEntity.buy4 = source.readString();
            stockInfoEntity.buy5 = source.readString();
            stockInfoEntity.sell1 = source.readString();
            stockInfoEntity.sell2 = source.readString();
            stockInfoEntity.sell3 = source.readString();
            stockInfoEntity.sell4 = source.readString();
            stockInfoEntity.sell5 = source.readString();
            stockInfoEntity.buy1Amount = source.readString();
            stockInfoEntity.buy2Amount = source.readString();
            stockInfoEntity.buy3Amount = source.readString();
            stockInfoEntity.buy4Amount = source.readString();
            stockInfoEntity.buy5Amount = source.readString();
            stockInfoEntity.sell1Amount = source.readString();
            stockInfoEntity.sell2Amount = source.readString();
            stockInfoEntity.sell3Amount = source.readString();
            stockInfoEntity.sell4Amount = source.readString();
            stockInfoEntity.sell5Amount = source.readString();
            stockInfoEntity.isHoldStock = source.readString();
            stockInfoEntity.apperHoldStock = source.readString();
            stockInfoEntity.stockholdon = source.readString();
            stockInfoEntity.isHotStock = source.readString();
            stockInfoEntity.MKT_VAL = source.readString();
            stockInfoEntity.SECU_ACC = source.readString();
            stockInfoEntity.SHARE_BLN = source.readString();
            stockInfoEntity.SHARE_AVL = source.readString();
            stockInfoEntity.SHARE_QTY = source.readString();
            stockInfoEntity.PROFIT_RATIO = source.readString();
            stockInfoEntity.MARKET = source.readString();
            stockInfoEntity.BRANCH = source.readString();
            stockInfoEntity.MKT_PRICE = source.readString();
            stockInfoEntity.SECU_CODE = source.readString();
            stockInfoEntity.INCOME_AMT = source.readString();
            stockInfoEntity.SECU_NAME = source.readString();
            stockInfoEntity.CURRENT_COST = source.readString();


            stockInfoEntity.message = source.readString();
            stockInfoEntity.viewcount = source.readString();
            stockInfoEntity.timestamp = source.readString();
            stockInfoEntity.curprice = source.readString();
            stockInfoEntity.read = source.readString();


            return stockInfoEntity;
        }

        @Override
        public StockInfoEntity[] newArray(int size) {
            return new StockInfoEntity[size];
        }
    };
}
