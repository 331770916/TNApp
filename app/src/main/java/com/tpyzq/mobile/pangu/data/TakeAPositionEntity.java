package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * Created by wangqi on 2016/7/26.
 * 交易   持倉  实体类
 */
public class TakeAPositionEntity {
    private String transactionName;                     //股票名称
    private String transactionNumber;                   //股票市值
    private String transactionProfit;                   //盈亏
    private String transactionProfit1;                  //盈亏
    private String transactionPositions;                //持仓/可用
    private String transactionPositions1;               //持仓/可用
    private String transactionPositionsCurrentPrice;    //现价/成本
    private String transactionPositionsCurrentPrice1;    //现价/成本
    private String mCODE;

    private List<BeanData> data;

    public static class BeanData {
        private String MKT_VAL;
        private String DRAW_AMT;
        private String TOTAL_INCOME_BAL;
        private String AVAILABLE;
        private String ASSERT_VAL;

        private List<INCOMELIST> INCOME_LIST;

        public String getMKT_VAL() {
            return MKT_VAL;
        }

        public void setMKT_VAL(String MKT_VAL) {
            this.MKT_VAL = MKT_VAL;
        }

        public String getDRAW_AMT() {
            return DRAW_AMT;
        }

        public void setDRAW_AMT(String DRAW_AMT) {
            this.DRAW_AMT = DRAW_AMT;
        }

        public String getTOTAL_INCOME_BAL() {
            return TOTAL_INCOME_BAL;
        }

        public void setTOTAL_INCOME_BAL(String TOTAL_INCOME_BAL) {
            this.TOTAL_INCOME_BAL = TOTAL_INCOME_BAL;
        }

        public String getAVAILABLE() {
            return AVAILABLE;
        }

        public void setAVAILABLE(String AVAILABLE) {
            this.AVAILABLE = AVAILABLE;
        }

        public String getASSERT_VAL() {
            return ASSERT_VAL;
        }

        public void setASSERT_VAL(String ASSERT_VAL) {
            this.ASSERT_VAL = ASSERT_VAL;
        }

        public List<INCOMELIST> getINCOME_LIST() {
            return INCOME_LIST;
        }

        public void setINCOME_LIST(List<INCOMELIST> INCOME_LIST) {
            this.INCOME_LIST = INCOME_LIST;
        }

        public static class INCOMELIST {
            private String MKT_VAL;             //市值
            private String SECU_ACC;            //股东代码
            private String SHARE_BLN;           //股份余额
            private String SHARE_AVL;           //股份可卖数量
            private String SHARE_QTY;           //股份总数量
            private String PROFIT_RATIO;        //没有文档
            private String MARKET;              //交易市场
            private String BRANCH;              //分支机构
            private String MKT_PRICE;           //现价
            private String SECU_CODE;           //证券代码
            private String INCOME_AMT;          //盈亏金额
            private String SECU_NAME;           //证券名称
            private String CURRENT_COST;        //当前成本价


            public String getSHARE_QTY() {
                return SHARE_QTY;
            }

            public void setSHARE_QTY(String SHARE_QTY) {
                this.SHARE_QTY = SHARE_QTY;
            }

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
        }
    }

    public List<BeanData> getData() {
        return data;
    }

    public void setData(List<BeanData> data) {
        this.data = data;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getTransactionProfit() {
        return transactionProfit;
    }

    public void setTransactionProfit(String transactionProfit) {
        this.transactionProfit = transactionProfit;
    }

    public String getTransactionProfit1() {
        return transactionProfit1;
    }

    public void setTransactionProfit1(String transactionProfit1) {
        this.transactionProfit1 = transactionProfit1;
    }

    public String getTransactionPositions() {
        return transactionPositions;
    }

    public void setTransactionPositions(String transactionPositions) {
        this.transactionPositions = transactionPositions;
    }

    public String getTransactionPositions1() {
        return transactionPositions1;
    }

    public void setTransactionPositions1(String transactionPositions1) {
        this.transactionPositions1 = transactionPositions1;
    }

    public String getTransactionPositionsCurrentPrice() {
        return transactionPositionsCurrentPrice;
    }

    public void setTransactionPositionsCurrentPrice(String transactionPositionsCurrentPrice) {
        this.transactionPositionsCurrentPrice = transactionPositionsCurrentPrice;
    }

    public String getTransactionPositionsCurrentPrice1() {
        return transactionPositionsCurrentPrice1;
    }

    public void setTransactionPositionsCurrentPrice1(String transactionPositionsCurrentPrice1) {
        this.transactionPositionsCurrentPrice1 = transactionPositionsCurrentPrice1;
    }

    public String getmCODE() {
        return mCODE;
    }

    public void setmCODE(String mCODE) {
        this.mCODE = mCODE;
    }
}
