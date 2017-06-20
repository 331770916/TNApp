package com.tpyzq.mobile.pangu.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhangwenbo on 2016/8/8.
 * 银行账户实体类
 */
public class BankAccountEntity implements Parcelable {

    private String code;
    private String msg;
    private String BANK_NO;         //银行代码
    private String BALANCE;         //资金余额
    private String FETCH_BALANCE;   //可取资金
    private String ENABLE_BALANCE;  //可用资金

    private String BANK_ACCOUNT;    //银行账号
    private String FUND_ACCOUNT;    //资金账号
    private String MAIN_FLAG;       //主账号 1  辅账号0
    private String BANK_NAME;       //银行名称
    private String queryType;       //查询类型  0不校验1校验银行密码2不支持银行查询

    //300231用   查询当日转账结果

    private String TRD_DATE;       //转账日期
    private String TRAN_TIME;       //转帐时间，HH:MM:SS
    private String EXT_INST;        //银行代码
    private String EXT_ACC;         //银行账号
    private String CURRENCY;          //货币代码，详见数据字典
    private String BIZ_NAME;        //业务名称
    private String BIZ_CODE;        //业务代码： 1000：转入，1001：转出，1003：查询余额
    private String CPTL_AMT;        //转账金额
    private String STATUS;          //处理结果：  1:已报,2:成功,3:失败,4:超时,6:已冲正,7:其他
    private String STATUS_NAME;      //
    private String SERIAL_NO;       //合同序号：券商流水号
    private String KEY_STR;         //定位串
    private String EXT_RET_CODE;    //银行返回信息代码
    private String EXT_RET_MSG;     //银行返回信息

    //查询余额用

    private String LAST_BALANCE;

    //资金调拨
    private String CURRENT_BALANCE_DEST; //转入方当前余额
    private String ENABLE_BALANCE_DEST; //转入方可用资金

    //资金收集
    private boolean collected;
    private String  occurBalance;

    //调拨记录
    private String OCCUR_BALANCE;       //发生金额
    private String FUND_ACCOUNT_SRC;    //转出账号
    private String BANK_NAME_SRC;       //转出银行
    private String BANK_NAME_DEST;      //转入银行
    private String BANK_NO_SRC;
    private String BANK_NO_DEST;
    private String MONEY_TYPE;          //币种类别0.人民币 1.美元 2.港币
    private String BUSINESS_NAME;       //业务名称
    private String INIT_DATE;           //交易日期格式：yyyymmdd
    private String CURR_TIME;           //时间
    private String FUND_ACCOUNT_DEST;   //转入账号
    private String POSITION_STR;        //定位串，用于翻页时使用
    private String BANK_ACCOUNT_SRC;    //银行账户编号
    private String BANK_ACCOUNT_DEST;   //转入账号

    private List<AccountInfo> data;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(msg);
        dest.writeString(BANK_NO);
        dest.writeString(BALANCE);
        dest.writeString(FETCH_BALANCE);
        dest.writeString(ENABLE_BALANCE);
        dest.writeString(BANK_ACCOUNT);
        dest.writeString(FUND_ACCOUNT);
        dest.writeString(MAIN_FLAG);
        dest.writeString(BANK_NAME);
        dest.writeString(queryType);
        dest.writeString(TRD_DATE);
        dest.writeString(TRAN_TIME);
        dest.writeString(EXT_INST);
        dest.writeString(EXT_ACC);
        dest.writeString(CURRENCY);
        dest.writeString(BIZ_NAME);
        dest.writeString(BIZ_CODE);
        dest.writeString(CPTL_AMT);
        dest.writeString(STATUS);
        dest.writeString(STATUS_NAME);
        dest.writeString(SERIAL_NO);
        dest.writeString(KEY_STR);
        dest.writeString(EXT_RET_CODE);
        dest.writeString(EXT_RET_MSG);
        dest.writeString(LAST_BALANCE);

        dest.writeString(CURRENT_BALANCE_DEST);
        dest.writeString(ENABLE_BALANCE_DEST);
        dest.writeByte((byte) (collected ? 1 : 0));
        dest.writeString(occurBalance);

        dest.writeString(OCCUR_BALANCE);
        dest.writeString(FUND_ACCOUNT_SRC);
        dest.writeString(BANK_NAME_SRC);
        dest.writeString(BANK_NAME_DEST);
        dest.writeString(BANK_NO_SRC);
        dest.writeString(BANK_NO_DEST);
        dest.writeString(MONEY_TYPE);
        dest.writeString(BUSINESS_NAME);
        dest.writeString(INIT_DATE);
        dest.writeString(CURR_TIME);
        dest.writeString(FUND_ACCOUNT_DEST);
        dest.writeString(POSITION_STR);
        dest.writeString(BANK_ACCOUNT_SRC);
        dest.writeString(BANK_ACCOUNT_DEST);
    }

    public static final Creator<BankAccountEntity> CREATOR = new Creator<BankAccountEntity>(){
        @Override
        public BankAccountEntity createFromParcel(Parcel source) {
            BankAccountEntity bean = new BankAccountEntity();
            bean.code = source.readString();
            bean.msg  = source.readString() ;
            bean.BANK_NO  = source.readString() ;
            bean.BALANCE  = source.readString() ;
            bean.FETCH_BALANCE = source.readString();
            bean.ENABLE_BALANCE = source.readString();
            bean.BANK_ACCOUNT  = source.readString() ;
            bean.FUND_ACCOUNT  = source.readString() ;
            bean.MAIN_FLAG  = source.readString() ;
            bean.BANK_NAME  = source.readString() ;
            bean.queryType = source.readString();
            bean.TRD_DATE  = source.readString() ;
            bean.TRAN_TIME  = source.readString() ;
            bean.EXT_INST  = source.readString() ;
            bean.EXT_ACC  = source.readString() ;
            bean.CURRENCY  = source.readString() ;
            bean.BIZ_NAME  = source.readString() ;
            bean.BIZ_CODE  = source.readString() ;
            bean.CPTL_AMT  = source.readString() ;
            bean.STATUS  = source.readString() ;
            bean.STATUS_NAME = source.readString();
            bean.SERIAL_NO  = source.readString() ;
            bean.KEY_STR  = source.readString() ;
            bean.EXT_RET_CODE  = source.readString() ;
            bean.EXT_RET_MSG  = source.readString() ;
            bean.LAST_BALANCE  = source.readString() ;
            bean.CURRENT_BALANCE_DEST = source.readString();
            bean.ENABLE_BALANCE_DEST = source.readString();
            bean.collected = source.readByte() != 0;

            bean.occurBalance = source.readString();
            bean.OCCUR_BALANCE = source.readString();
            bean.FUND_ACCOUNT_SRC = source.readString();
            bean.BANK_NAME_SRC = source.readString();
            bean.BANK_NAME_DEST = source.readString();
            bean.BANK_NO_SRC = source.readString();
            bean.BANK_NO_DEST = source.readString();
            bean.MONEY_TYPE = source.readString();
            bean.BUSINESS_NAME = source.readString();
            bean.INIT_DATE = source.readString();
            bean.CURR_TIME = source.readString();
            bean.FUND_ACCOUNT_DEST = source.readString();
            bean.POSITION_STR = source.readString();
            bean.BANK_ACCOUNT_SRC = source.readString();
            bean.BANK_ACCOUNT_DEST = source.readString();
            return bean;
        }

        @Override
        public BankAccountEntity[] newArray(int size) {
            return new BankAccountEntity[size];
        }
    };

    public String getBANK_ACCOUNT_DEST() {
        return BANK_ACCOUNT_DEST;
    }

    public void setBANK_ACCOUNT_DEST(String BANK_ACCOUNT_DEST) {
        this.BANK_ACCOUNT_DEST = BANK_ACCOUNT_DEST;
    }

    public String getFETCH_BALANCE() {
        return FETCH_BALANCE;
    }

    public void setFETCH_BALANCE(String FETCH_BALANCE) {
        this.FETCH_BALANCE = FETCH_BALANCE;
    }

    public String getENABLE_BALANCE() {
        return ENABLE_BALANCE;
    }

    public void setENABLE_BALANCE(String ENABLE_BALANCE) {
        this.ENABLE_BALANCE = ENABLE_BALANCE;
    }

    public String getSTATUS_NAME() {
        return STATUS_NAME;
    }

    public void setSTATUS_NAME(String STATUS_NAME) {
        this.STATUS_NAME = STATUS_NAME;
    }

    public String getOCCUR_BALANCE() {
        return OCCUR_BALANCE;
    }

    public void setOCCUR_BALANCE(String OCCUR_BALANCE) {
        this.OCCUR_BALANCE = OCCUR_BALANCE;
    }

    public String getFUND_ACCOUNT_SRC() {
        return FUND_ACCOUNT_SRC;
    }

    public void setFUND_ACCOUNT_SRC(String FUND_ACCOUNT_SRC) {
        this.FUND_ACCOUNT_SRC = FUND_ACCOUNT_SRC;
    }

    public String getBANK_ACCOUNT_SRC() {
        return BANK_ACCOUNT_SRC;
    }

    public void setBANK_ACCOUNT_SRC(String BANK_ACCOUNT_SRC) {
        this.BANK_ACCOUNT_SRC = BANK_ACCOUNT_SRC;
    }

    public String getBANK_NAME_SRC() {
        return BANK_NAME_SRC;
    }

    public void setBANK_NAME_SRC(String BANK_NAME_SRC) {
        this.BANK_NAME_SRC = BANK_NAME_SRC;
    }

    public String getBANK_NAME_DEST() {
        return BANK_NAME_DEST;
    }

    public void setBANK_NAME_DEST(String BANK_NAME_DEST) {
        this.BANK_NAME_DEST = BANK_NAME_DEST;
    }

    public String getBANK_NO_SRC() {
        return BANK_NO_SRC;
    }

    public void setBANK_NO_SRC(String BANK_NO_SRC) {
        this.BANK_NO_SRC = BANK_NO_SRC;
    }

    public String getBANK_NO_DEST() {
        return BANK_NO_DEST;
    }

    public void setBANK_NO_DEST(String BANK_NO_DEST) {
        this.BANK_NO_DEST = BANK_NO_DEST;
    }

    public String getMONEY_TYPE() {
        return MONEY_TYPE;
    }

    public void setMONEY_TYPE(String MONEY_TYPE) {
        this.MONEY_TYPE = MONEY_TYPE;
    }

    public String getBUSINESS_NAME() {
        return BUSINESS_NAME;
    }

    public void setBUSINESS_NAME(String BUSINESS_NAME) {
        this.BUSINESS_NAME = BUSINESS_NAME;
    }

    public String getINIT_DATE() {
        return INIT_DATE;
    }

    public void setINIT_DATE(String INIT_DATE) {
        this.INIT_DATE = INIT_DATE;
    }

    public String getCURR_TIME() {
        return CURR_TIME;
    }

    public void setCURR_TIME(String CURR_TIME) {
        this.CURR_TIME = CURR_TIME;
    }

    public String getFUND_ACCOUNT_DEST() {
        return FUND_ACCOUNT_DEST;
    }

    public void setFUND_ACCOUNT_DEST(String FUND_ACCOUNT_DEST) {
        this.FUND_ACCOUNT_DEST = FUND_ACCOUNT_DEST;
    }

    public String getPOSITION_STR() {
        return POSITION_STR;
    }

    public void setPOSITION_STR(String POSITION_STR) {
        this.POSITION_STR = POSITION_STR;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public void setOccurBalance(String occurBalance) {
        this.occurBalance = occurBalance;
    }

    public String getOccurBalance() {
        return occurBalance;
    }

    public boolean getCollected() {
        return collected;
    }

    public String getENABLE_BALANCE_DEST() {
        return ENABLE_BALANCE_DEST;
    }

    public void setENABLE_BALANCE_DEST(String ENABLE_BALANCE_DEST) {
        this.ENABLE_BALANCE_DEST = ENABLE_BALANCE_DEST;
    }

    public String getCURRENT_BALANCE_DEST() {
        return CURRENT_BALANCE_DEST;
    }

    public void setCURRENT_BALANCE_DEST(String CURRENT_BALANCE_DEST) {
        this.CURRENT_BALANCE_DEST = CURRENT_BALANCE_DEST;
    }

    public void setLAST_BALANCE(String LAST_BALANCE) {
        this.LAST_BALANCE = LAST_BALANCE;
    }

    public String getLAST_BALANCE() {
        return LAST_BALANCE;
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

    public String getBANK_NO() {
        return BANK_NO;
    }

    public void setBANK_NO(String BANK_NO) {
        this.BANK_NO = BANK_NO;
    }

    public String getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(String BALANCE) {
        this.BALANCE = BALANCE;
    }

    public String getBANK_ACCOUNT() {
        return BANK_ACCOUNT;
    }

    public void setBANK_ACCOUNT(String BANK_ACCOUNT) {
        this.BANK_ACCOUNT = BANK_ACCOUNT;
    }

    public String getFUND_ACCOUNT() {
        return FUND_ACCOUNT;
    }

    public void setFUND_ACCOUNT(String FUND_ACCOUNT) {
        this.FUND_ACCOUNT = FUND_ACCOUNT;
    }

    public String getMAIN_FLAG() {
        return MAIN_FLAG;
    }

    public void setMAIN_FLAG(String MAIN_FLAG) {
        this.MAIN_FLAG = MAIN_FLAG;
    }

    public String getBANK_NAME() {
        return BANK_NAME;
    }

    public void setBANK_NAME(String BANK_NAME) {
        this.BANK_NAME = BANK_NAME;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getTRD_DATE() {
        return TRD_DATE;
    }

    public void setTRD_DATE(String TRAN_DATE) {
        this.TRD_DATE = TRAN_DATE;
    }

    public String getTRAN_TIME() {
        return TRAN_TIME;
    }

    public void setTRAN_TIME(String TRAN_TIME) {
        this.TRAN_TIME = TRAN_TIME;
    }

    public String getEXT_INST() {
        return EXT_INST;
    }

    public void setEXT_INST(String EXT_INST) {
        this.EXT_INST = EXT_INST;
    }

    public String getEXT_ACC() {
        return EXT_ACC;
    }

    public void setEXT_ACC(String EXT_ACC) {
        this.EXT_ACC = EXT_ACC;
    }

    public String getCURRENCY() {
        return CURRENCY;
    }

    public void setCURRENCY(String CURRENCY) {
        this.CURRENCY = CURRENCY;
    }

    public String getBIZ_NAME() {
        return BIZ_NAME;
    }

    public void setBIZ_NAME(String BIZ_NAME) {
        this.BIZ_NAME = BIZ_NAME;
    }

    public String getBIZ_CODE() {
        return BIZ_CODE;
    }

    public void setBIZ_CODE(String BIZ_CODE) {
        this.BIZ_CODE = BIZ_CODE;
    }

    public String getCPTL_AMT() {
        return CPTL_AMT;
    }

    public void setCPTL_AMT(String CPTL_AMT) {
        this.CPTL_AMT = CPTL_AMT;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getSERIAL_NO() {
        return SERIAL_NO;
    }

    public void setSERIAL_NO(String SERIAL_NO) {
        this.SERIAL_NO = SERIAL_NO;
    }

    public String getKEY_STR() {
        return KEY_STR;
    }

    public void setKEY_STR(String KEY_STR) {
        this.KEY_STR = KEY_STR;
    }

    public String getEXT_RET_CODE() {
        return EXT_RET_CODE;
    }

    public void setEXT_RET_CODE(String EXT_RET_CODE) {
        this.EXT_RET_CODE = EXT_RET_CODE;
    }

    public String getEXT_RET_MSG() {
        return EXT_RET_MSG;
    }

    public void setEXT_RET_MSG(String EXT_RET_MSG) {
        this.EXT_RET_MSG = EXT_RET_MSG;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<AccountInfo> getData() {
        return data;
    }

    public void setData(List<AccountInfo> data) {
        this.data = data;
    }

    public class AccountInfo implements Parcelable{
        //300390用  查询银行卡账户
        private String BANK_NO;         //银行代码
        private String BALANCE;         //资金余额
        private String FETCH_BALANCE;   //可取资金
        private String ENABLE_BALANCE;  //可用资金
        private String BANK_ACCOUNT;    //银行账号
        private String FUND_ACCOUNT;    //资金账号
        private String MONEY_INTO;      //银证资金转入 0不校验 1校验交易密码 2校验银行密码 3校验电话银行密码
        private String MONEY_OUT;       //银证资金转出 0不校验 1校验资金密码2校验资金密码和交易
        private String MAIN_FLAG;       //主账号 1  辅账号0
        private String BANK_NAME;       //银行名称
        private String BANK_FUNDS_QUERY;//银行资金余额查询0不校验1校验银行密码2不支持银行查询

        //300231用   查询当日转账结果

        private String TRD_DATE;       //转账日期
        private String TRAN_TIME;       //转帐时间，HH:MM:SS
        private String EXT_INST;        //银行代码
        private String EXT_ACC;         //银行账号
        private String CURRENCY;        //货币代码，详见数据字典
        private String BIZ_NAME;        //业务名称
        private String BIZ_CODE;        //业务代码： 1000：转入，1001：转出，1003：查询余额
        private String CPTL_AMT;        //转账金额
        private String STATUS;          //处理结果：  1:已报,2:成功,3:失败,4:超时,6:已冲正,7:其他
        private String SERIAL_NO;       //合同序号：券商流水号
        private String KEY_STR;         //定位串
        private String EXT_RET_CODE;    //银行返回信息代码
        private String EXT_RET_MSG;     //银行返回信息
        private String STATUS_NAME;
        //查询余额

        private String LAST_BALANCE;    //余额

        //资金调拨
        private String CURRENT_BALANCE_DEST; //转入方当前余额
        private String ENABLE_BALANCE_DEST; //转入方可用资金

        //调拨记录

        private String OCCUR_BALANCE;       //发生金额
        private String FUND_ACCOUNT_SRC;    //转出账号
        private String BANK_NAME_SRC;       //转出银行
        private String BANK_NAME_DEST;      //转入银行
        private String BANK_NO_SRC;
        private String BANK_NO_DEST;
        private String MONEY_TYPE;          //币种类别0.人民币 1.美元 2.港币
        private String BUSINESS_NAME;       //业务名称
        private String INIT_DATE;           //交易日期格式：yyyymmdd
        private String CURR_TIME;           //时间
        private String FUND_ACCOUNT_DEST;   //转入账号
        private String POSITION_STR;        //定位串，用于翻页时使用
        private String BANK_ACCOUNT_SRC;
        private String BANK_ACCOUNT_DEST;



        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(BANK_NO);
            dest.writeString(BALANCE);
            dest.writeString(FETCH_BALANCE);
            dest.writeString(ENABLE_BALANCE);
            dest.writeString(BANK_ACCOUNT);
            dest.writeString(FUND_ACCOUNT);

            dest.writeString(MONEY_INTO);
            dest.writeString(MONEY_OUT);
            dest.writeString(MAIN_FLAG);
            dest.writeString(BANK_NAME);
            dest.writeString(BANK_FUNDS_QUERY);

            dest.writeString(TRD_DATE);
            dest.writeString(TRAN_TIME);
            dest.writeString(EXT_INST);
            dest.writeString(EXT_ACC);
            dest.writeString(CURRENCY);
            dest.writeString(BIZ_NAME);
            dest.writeString(BIZ_CODE);
            dest.writeString(CPTL_AMT);
            dest.writeString(STATUS);
            dest.writeString(SERIAL_NO);
            dest.writeString(KEY_STR);
            dest.writeString(EXT_RET_CODE);
            dest.writeString(EXT_RET_MSG);
            dest.writeString(STATUS_NAME);
            dest.writeString(LAST_BALANCE);

            dest.writeString(CURRENT_BALANCE_DEST);
            dest.writeString(ENABLE_BALANCE_DEST);

            dest.writeString(OCCUR_BALANCE);
            dest.writeString(FUND_ACCOUNT_SRC);
            dest.writeString(BANK_NAME_SRC);
            dest.writeString(BANK_NAME_DEST);
            dest.writeString(BANK_NO_SRC);
            dest.writeString(BANK_NO_DEST);
            dest.writeString(MONEY_TYPE);
            dest.writeString(BUSINESS_NAME);
            dest.writeString(INIT_DATE);
            dest.writeString(CURR_TIME);
            dest.writeString(FUND_ACCOUNT_DEST);
            dest.writeString(POSITION_STR);
            dest.writeString(BANK_ACCOUNT_SRC);
            dest.writeString(BANK_ACCOUNT_DEST);

        }

        public final Creator<AccountInfo> CREATOR = new Creator<AccountInfo>(){
            @Override
            public AccountInfo createFromParcel(Parcel source) {
                AccountInfo bean = new AccountInfo();
                bean.BANK_NO = source.readString();
                bean.BALANCE  = source.readString() ;
                bean.FETCH_BALANCE = source.readString();
                bean.ENABLE_BALANCE = source.readString();
                bean.BANK_ACCOUNT = source.readString();
                bean.FUND_ACCOUNT = source.readString();
                bean.MONEY_INTO = source.readString();
                bean.MONEY_OUT = source.readString();
                bean.MAIN_FLAG = source.readString();
                bean.BANK_NAME = source.readString();
                bean.BANK_FUNDS_QUERY = source.readString();

                bean.TRD_DATE = source.readString();
                bean.TRAN_TIME = source.readString();
                bean.EXT_INST = source.readString();
                bean.EXT_ACC = source.readString();
                bean.CURRENCY = source.readString();
                bean.BIZ_NAME = source.readString();
                bean.BIZ_CODE = source.readString();
                bean.CPTL_AMT = source.readString();
                bean.STATUS = source.readString();
                bean.SERIAL_NO = source.readString();
                bean.KEY_STR = source.readString();
                bean.EXT_RET_CODE = source.readString();
                bean.EXT_RET_MSG = source.readString();
                bean.STATUS_NAME = source.readString();
                bean.LAST_BALANCE = source.readString();

                bean.CURRENT_BALANCE_DEST = source.readString();
                bean.ENABLE_BALANCE_DEST = source.readString();



                bean.OCCUR_BALANCE = source.readString();
                bean.FUND_ACCOUNT_SRC = source.readString();
                bean.BANK_NAME_SRC = source.readString();
                bean.BANK_NAME_DEST = source.readString();
                bean.BANK_NO_SRC = source.readString();
                bean.BANK_NO_DEST = source.readString();
                bean.MONEY_TYPE = source.readString();
                bean.BUSINESS_NAME = source.readString();
                bean.INIT_DATE = source.readString();
                bean.CURR_TIME = source.readString();
                bean.FUND_ACCOUNT_DEST = source.readString();
                bean.POSITION_STR = source.readString();
                bean.BANK_ACCOUNT_SRC = source.readString();
                bean.BANK_ACCOUNT_DEST = source.readString();

                return bean;
            }

            @Override
            public AccountInfo[] newArray(int size) {
                return new AccountInfo[size];
            }
        };

        public String getBANK_ACCOUNT_DEST() {
            return BANK_ACCOUNT_DEST;
        }

        public void setBANK_ACCOUNT_DEST(String BANK_ACCOUNT_DEST) {
            this.BANK_ACCOUNT_DEST = BANK_ACCOUNT_DEST;
        }

        public String getFETCH_BALANCE() {
            return FETCH_BALANCE;
        }

        public void setFETCH_BALANCE(String FETCH_BALANCE) {
            this.FETCH_BALANCE = FETCH_BALANCE;
        }

        public String getENABLE_BALANCE() {
            return ENABLE_BALANCE;
        }

        public void setENABLE_BALANCE(String ENABLE_BALANCE) {
            this.ENABLE_BALANCE = ENABLE_BALANCE;
        }

        public String getSTATUS_NAME() {
            return STATUS_NAME;
        }

        public void setSTATUS_NAME(String STATUS_NAME) {
            this.STATUS_NAME = STATUS_NAME;
        }

        public String getOCCUR_BALANCE() {
            return OCCUR_BALANCE;
        }

        public void setOCCUR_BALANCE(String OCCUR_BALANCE) {
            this.OCCUR_BALANCE = OCCUR_BALANCE;
        }

        public String getFUND_ACCOUNT_SRC() {
            return FUND_ACCOUNT_SRC;
        }

        public void setFUND_ACCOUNT_SRC(String FUND_ACCOUNT_SRC) {
            this.FUND_ACCOUNT_SRC = FUND_ACCOUNT_SRC;
        }

        public String getBANK_NAME_SRC() {
            return BANK_NAME_SRC;
        }

        public void setBANK_NAME_SRC(String BANK_NAME_SRC) {
            this.BANK_NAME_SRC = BANK_NAME_SRC;
        }

        public String getBANK_NAME_DEST() {
            return BANK_NAME_DEST;
        }

        public void setBANK_NAME_DEST(String BANK_NAME_DEST) {
            this.BANK_NAME_DEST = BANK_NAME_DEST;
        }

        public String getBANK_ACCOUNT_SRC() {
            return BANK_ACCOUNT_SRC;
        }

        public void setBANK_ACCOUNT_SRC(String BANK_ACCOUNT_SRC) {
            this.BANK_ACCOUNT_SRC = BANK_ACCOUNT_SRC;
        }

        public String getMONEY_INTO() {
            return MONEY_INTO;
        }

        public void setMONEY_INTO(String MONEY_INTO) {
            this.MONEY_INTO = MONEY_INTO;
        }

        public String getMONEY_OUT() {
            return MONEY_OUT;
        }

        public void setMONEY_OUT(String MONEY_OUT) {
            this.MONEY_OUT = MONEY_OUT;
        }

        public String getBANK_FUNDS_QUERY() {
            return BANK_FUNDS_QUERY;
        }

        public void setBANK_FUNDS_QUERY(String BANK_FUNDS_QUERY) {
            this.BANK_FUNDS_QUERY = BANK_FUNDS_QUERY;
        }

        public String getBANK_NO_SRC() {
            return BANK_NO_SRC;
        }

        public void setBANK_NO_SRC(String BANK_NO_SRC) {
            this.BANK_NO_SRC = BANK_NO_SRC;
        }

        public String getBANK_NO_DEST() {
            return BANK_NO_DEST;
        }

        public void setBANK_NO_DEST(String BANK_NO_DEST) {
            this.BANK_NO_DEST = BANK_NO_DEST;
        }

        public String getMONEY_TYPE() {
            return MONEY_TYPE;
        }

        public void setMONEY_TYPE(String MONEY_TYPE) {
            this.MONEY_TYPE = MONEY_TYPE;
        }

        public String getBUSINESS_NAME() {
            return BUSINESS_NAME;
        }

        public void setBUSINESS_NAME(String BUSINESS_NAME) {
            this.BUSINESS_NAME = BUSINESS_NAME;
        }

        public String getINIT_DATE() {
            return INIT_DATE;
        }

        public void setINIT_DATE(String INIT_DATE) {
            this.INIT_DATE = INIT_DATE;
        }

        public String getCURR_TIME() {
            return CURR_TIME;
        }

        public void setCURR_TIME(String CURR_TIME) {
            this.CURR_TIME = CURR_TIME;
        }

        public String getFUND_ACCOUNT_DEST() {
            return FUND_ACCOUNT_DEST;
        }

        public void setFUND_ACCOUNT_DEST(String FUND_ACCOUNT_DEST) {
            this.FUND_ACCOUNT_DEST = FUND_ACCOUNT_DEST;
        }

        public String getPOSITION_STR() {
            return POSITION_STR;
        }

        public void setPOSITION_STR(String POSITION_STR) {
            this.POSITION_STR = POSITION_STR;
        }

        public String getBANK_NO() {
            return BANK_NO;
        }

        public void setBANK_NO(String BANK_NO) {
            this.BANK_NO = BANK_NO;
        }

        public String getBALANCE() {
            return BALANCE;
        }

        public void setBALANCE(String BALANCE) {
            this.BALANCE = BALANCE;
        }

        public String getBANK_ACCOUNT() {
            return BANK_ACCOUNT;
        }

        public void setBANK_ACCOUNT(String BANK_ACCOUNT) {
            this.BANK_ACCOUNT = BANK_ACCOUNT;
        }

        public String getFUND_ACCOUNT() {
            return FUND_ACCOUNT;
        }

        public void setFUND_ACCOUNT(String FUND_ACCOUNT) {
            this.FUND_ACCOUNT = FUND_ACCOUNT;
        }

        public String getMAIN_FLAG() {
            return MAIN_FLAG;
        }

        public void setMAIN_FLAG(String MAIN_FLAG) {
            this.MAIN_FLAG = MAIN_FLAG;
        }

        public String getBANK_NAME() {
            return BANK_NAME;
        }

        public void setBANK_NAME(String BANK_NAME) {
            this.BANK_NAME = BANK_NAME;
        }

        public String getTRD_DATE() {
            return TRD_DATE;
        }

        public void setTRD_DATE(String TRAN_DATE) {
            this.TRD_DATE = TRAN_DATE;
        }

        public String getTRAN_TIME() {
            return TRAN_TIME;
        }

        public void setTRAN_TIME(String TRAN_TIME) {
            this.TRAN_TIME = TRAN_TIME;
        }

        public String getEXT_INST() {
            return EXT_INST;
        }

        public void setEXT_INST(String EXT_INST) {
            this.EXT_INST = EXT_INST;
        }

        public String getEXT_ACC() {
            return EXT_ACC;
        }

        public void setEXT_ACC(String EXT_ACC) {
            this.EXT_ACC = EXT_ACC;
        }

        public String getCURRENCY() {
            return CURRENCY;
        }

        public void setCURRENCY(String CURRENCY) {
            this.CURRENCY = CURRENCY;
        }

        public String getBIZ_NAME() {
            return BIZ_NAME;
        }

        public void setBIZ_NAME(String BIZ_NAME) {
            this.BIZ_NAME = BIZ_NAME;
        }

        public String getBIZ_CODE() {
            return BIZ_CODE;
        }

        public void setBIZ_CODE(String BIZ_CODE) {
            this.BIZ_CODE = BIZ_CODE;
        }

        public String getCPTL_AMT() {
            return CPTL_AMT;
        }

        public void setCPTL_AMT(String CPTL_AMT) {
            this.CPTL_AMT = CPTL_AMT;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getSERIAL_NO() {
            return SERIAL_NO;
        }

        public void setSERIAL_NO(String SERIAL_NO) {
            this.SERIAL_NO = SERIAL_NO;
        }

        public String getKEY_STR() {
            return KEY_STR;
        }

        public void setKEY_STR(String KEY_STR) {
            this.KEY_STR = KEY_STR;
        }

        public String getEXT_RET_CODE() {
            return EXT_RET_CODE;
        }

        public void setEXT_RET_CODE(String EXT_RET_CODE) {
            this.EXT_RET_CODE = EXT_RET_CODE;
        }

        public String getEXT_RET_MSG() {
            return EXT_RET_MSG;
        }

        public void setEXT_RET_MSG(String EXT_RET_MSG) {
            this.EXT_RET_MSG = EXT_RET_MSG;
        }

        public void setLAST_BALANCE(String LAST_BALANCE) {
            this.LAST_BALANCE = LAST_BALANCE;
        }

        public String getLAST_BALANCE() {
            return LAST_BALANCE;
        }

        public String getCURRENT_BALANCE_DEST() {
            return CURRENT_BALANCE_DEST;
        }

        public void setCURRENT_BALANCE_DEST(String CURRENT_BALANCE_DEST) {
            this.CURRENT_BALANCE_DEST = CURRENT_BALANCE_DEST;
        }

        public String getENABLE_BALANCE_DEST() {
            return ENABLE_BALANCE_DEST;
        }

        public void setENABLE_BALANCE_DEST(String ENABLE_BALANCE_DEST) {
            this.ENABLE_BALANCE_DEST = ENABLE_BALANCE_DEST;
        }
    }

}
