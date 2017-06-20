package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;
import java.util.List;

/**
 * 认购状态对象
 */
public class SubsStatusEntity implements Serializable{

    public String msg;
    public String code;
    public List<Data> data;

    public class Data implements Serializable{

        public String SERIAL_NO;        //流水号
        public String OFRISK_FLAG;
        public String IS_VALIB_RISK_LEVEL;
        public String IS_AGREEMENT;
        public String IS_OPEN;
        public String RISK_LEVEL_NAME;      //产品类型
        public String IS_ABLE;              //是否认购或申购成功
        public String OFUND_RISKLEVEL_NAME; //产品风险等级名称
        public String RISK_LEVEL;       //风险等级
        public String RISK_RATING;      //产品风险等级
    }

}