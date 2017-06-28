package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;
import java.util.List;

/**
 * 基金信息
 */
public class FundDataEntity implements Serializable {

    public String msg;
    public String code;
    public List<Data> data;

    public class Data  implements Serializable {

        public String NAV;
        public String OPER_TYPE;
        public String FUND_CODE;
        public String ENABLE_BALANCE;
        public String NEED_CONTRACT;
        public String FUND_NAME;
        public String PERSON_INVEST;
        public String ENABLE_CNT;
        public String LEASE_CNT;
        public String OPEN_SHARE;
        public String FUND_COMPANY;
        public String FUND_STATUS;
    }

}