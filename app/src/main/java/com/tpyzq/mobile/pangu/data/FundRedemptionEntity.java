package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 基金份额（赎回）
 */
public class FundRedemptionEntity {

    public String AVAILABLE;
    public String OPFUND_MARKET_VALUE;
    public String TOTAL_INCOME;
    public List<RESULT_LIST> RESULT_LIST;

    public class RESULT_LIST {

        public String NAV;
        public String FUND_CODE;
        public String CURRENT_AMOUNT;
        public String ENABLE_AMOUNT;
        public String DIVIDEND_WAY;
        public String FUND_COMPANY_NAME;
        public String MARKET_VALUE;
        public String AUTO_BUY;
        public String FUND_NAME;
        public String INCOME_BALANCE;
        public String CURRENT_SHARE;
        public String OF_COST_PRICE;
        public String FUND_COMPANY_CODE;
        public String INCOME;
        public String COST_PRICE;
        public String ENABLE_REDEEM_SHARE;
        public String FROZEN_AMOUNT;
    }

}