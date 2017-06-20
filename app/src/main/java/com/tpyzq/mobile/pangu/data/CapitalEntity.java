package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by wangqi on 2016/8/3.
 * 资金流水查询  交割单
 * 今日  一周  一月  三月  自定义 实体类
 */
public class CapitalEntity {
    private String code;              //0 成功  -6session失效
    private String msg;

    private String Date;                        //成交日期
    private String Business;                    //业务
    private String Name;                        //业务名称
    private String StockName;                   //股票名称
    private String Money;                       //发生金额
    private String Balance;                     //资金余额  成交余额
    private String Stockcde;                    //证券代码
    private String StockCode;                   //股票代码
    private String SecurityPrice;               //证券价格
    private String Hbcjsl;                      //成交数量
    private String Otherexpenses;               //其它费用
    private String Currency;                    //币种
    private String Commission;                  //佣金
    private String Stamps;                      //印花税
    private String TransferFee;                 //过户费
    private String TransactionPrice;            //成交价格
    private String shareholder;                 //股东代码
    private String Remarks;                     //备注
    private String Transaction;                 //买卖
    private String Business_type;                 //买卖

    private List<BeanData> data;

  public static   class BeanData {

        private String STOCK_CODE;
        private String BUSINESS_AMOUNT;
        private String REMARK;
        private String BUSINESS_PRICE;        //证券价格
        private String MARKET;
        private String MONEY_TYPE;
        private String EXCHANGE_FARE6;
        private String BUSINESS_NAME;
        private String STOCK_ACCOUNT;
        private String BUSINESS_BALANCE;
        private String BUSINESS_TIME;
        private String STOCK_NAME;
        private String ENTRUST_BS;
        private String BUSINESS_TYPE; // 0:股票相关流水 1:银证相关流水
        private String FARE2;
        private String INIT_DATE;
        private String FARE0;
        private String FARE1;
        private String POSITION_STR;
        private String CLEAR_BALANCE;       //资金清算金额
        private String POSITION_STR_LONG;


      public String getPOSITION_STR_LONG() {
          return POSITION_STR_LONG;
      }

      public void setPOSITION_STR_LONG(String POSITION_STR_LONG) {
          this.POSITION_STR_LONG = POSITION_STR_LONG;
      }

      public String getCLEAR_BALANCE() {
          return CLEAR_BALANCE;
      }

      public void setCLEAR_BALANCE(String CLEAR_BALANCE) {
          this.CLEAR_BALANCE = CLEAR_BALANCE;
      }

      public String getSTOCK_CODE() {
            return STOCK_CODE;
        }

        public void setSTOCK_CODE(String STOCK_CODE) {
            this.STOCK_CODE = STOCK_CODE;
        }

        public String getBUSINESS_AMOUNT() {
            return BUSINESS_AMOUNT;
        }

        public void setBUSINESS_AMOUNT(String BUSINESS_AMOUNT) {
            this.BUSINESS_AMOUNT = BUSINESS_AMOUNT;
        }

        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }

        public String getBUSINESS_PRICE() {
            return BUSINESS_PRICE;
        }

        public void setBUSINESS_PRICE(String BUSINESS_PRICE) {
            this.BUSINESS_PRICE = BUSINESS_PRICE;
        }

        public String getMARKET() {
            return MARKET;
        }

        public void setMARKET(String MARKET) {
            this.MARKET = MARKET;
        }

        public String getMONEY_TYPE() {
            return MONEY_TYPE;
        }

        public void setMONEY_TYPE(String MONEY_TYPE) {
            this.MONEY_TYPE = MONEY_TYPE;
        }

        public String getEXCHANGE_FARE6() {
            return EXCHANGE_FARE6;
        }

        public void setEXCHANGE_FARE6(String EXCHANGE_FARE6) {
            this.EXCHANGE_FARE6 = EXCHANGE_FARE6;
        }

        public String getBUSINESS_NAME() {
            return BUSINESS_NAME;
        }

        public void setBUSINESS_NAME(String BUSINESS_NAME) {
            this.BUSINESS_NAME = BUSINESS_NAME;
        }

        public String getSTOCK_ACCOUNT() {
            return STOCK_ACCOUNT;
        }

        public void setSTOCK_ACCOUNT(String STOCK_ACCOUNT) {
            this.STOCK_ACCOUNT = STOCK_ACCOUNT;
        }

        public String getBUSINESS_BALANCE() {
            return BUSINESS_BALANCE;
        }

        public void setBUSINESS_BALANCE(String BUSINESS_BALANCE) {
            this.BUSINESS_BALANCE = BUSINESS_BALANCE;
        }

        public String getBUSINESS_TIME() {
            return BUSINESS_TIME;
        }

        public void setBUSINESS_TIME(String BUSINESS_TIME) {
            this.BUSINESS_TIME = BUSINESS_TIME;
        }

        public String getSTOCK_NAME() {
            return STOCK_NAME;
        }

        public void setSTOCK_NAME(String STOCK_NAME) {
            this.STOCK_NAME = STOCK_NAME;
        }

        public String getENTRUST_BS() {
            return ENTRUST_BS;
        }

        public void setENTRUST_BS(String ENTRUST_BS) {
            this.ENTRUST_BS = ENTRUST_BS;
        }

        public String getBUSINESS_TYPE() {
            return BUSINESS_TYPE;
        }

        public void setBUSINESS_TYPE(String BUSINESS_TYPE) {
            this.BUSINESS_TYPE = BUSINESS_TYPE;
        }

        public String getFARE2() {
            return FARE2;
        }

        public void setFARE2(String FARE2) {
            this.FARE2 = FARE2;
        }

        public String getINIT_DATE() {
            return INIT_DATE;
        }

        public void setINIT_DATE(String INIT_DATE) {
            this.INIT_DATE = INIT_DATE;
        }

        public String getFARE0() {
            return FARE0;
        }

        public void setFARE0(String FARE0) {
            this.FARE0 = FARE0;
        }

        public String getFARE1() {
            return FARE1;
        }

        public void setFARE1(String FARE1) {
            this.FARE1 = FARE1;
        }

        public String getPOSITION_STR() {
            return POSITION_STR;
        }

        public void setPOSITION_STR(String POSITION_STR) {
            this.POSITION_STR = POSITION_STR;
        }
    }

    public String getTransactionPrice() {
        return TransactionPrice;
    }

    public void setTransactionPrice(String transactionPrice) {
        TransactionPrice = transactionPrice;
    }

    public String getShareholder() {
        return shareholder;
    }

    public void setShareholder(String shareholder) {
        this.shareholder = shareholder;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getBusiness() {
        return Business;
    }

    public void setBusiness(String business) {
        Business = business;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getStockcde() {
        return Stockcde;
    }

    public void setStockcde(String stockcde) {
        Stockcde = stockcde;
    }

    public String getSecurityPrice() {
        return SecurityPrice;
    }

    public void setSecurityPrice(String securityPrice) {
        SecurityPrice = securityPrice;
    }

    public String getHbcjsl() {
        return Hbcjsl;
    }

    public void setHbcjsl(String hbcjsl) {
        Hbcjsl = hbcjsl;
    }

    public String getOtherexpenses() {
        return Otherexpenses;
    }

    public void setOtherexpenses(String otherexpenses) {
        Otherexpenses = otherexpenses;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getCommission() {
        return Commission;
    }

    public void setCommission(String commission) {
        Commission = commission;
    }

    public String getStamps() {
        return Stamps;
    }

    public void setStamps(String stamps) {
        Stamps = stamps;
    }

    public String getTransferFee() {
        return TransferFee;
    }

    public void setTransferFee(String transferFee) {
        TransferFee = transferFee;
    }

    public List<BeanData> getData() {
        return data;
    }

    public void setData(List<BeanData> data) {
        this.data = data;
    }

    public String getStockCode() {
        return StockCode;
    }

    public void setStockCode(String stockCode) {
        StockCode = stockCode;
    }

    public String getStockName() {
        return StockName;
    }

    public void setStockName(String stockName) {
        StockName = stockName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTransaction() {
        return Transaction;
    }

    public void setTransaction(String transaction) {
        Transaction = transaction;
    }

    public String getBusiness_type() {
        return Business_type;
    }

    public void setBusiness_type(String business_type) {
        Business_type = business_type;
    }
}
