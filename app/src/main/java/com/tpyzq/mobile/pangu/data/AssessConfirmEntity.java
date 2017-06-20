package com.tpyzq.mobile.pangu.data;

import java.io.Serializable;

/**
 * Created by 陈新宇 on 2016/11/14.
 */
public class AssessConfirmEntity implements Serializable {
    public String productcode;          //产品名称
    public String productcompany;       //产品公司
    public String productprice;         //产品价格
    public String type;                 //产品类型
    public String IS_ABLE;                 //是否或认购申购成功 0:成功 1：不成功
    public String IS_AGREEMENT;           //是否签署电子协议 0.需要 1.不需要
    public String IS_OPEN;                //是否开户 0.已开户 1.未开户
    public String IS_VALIB_RISK_LEVEL;   //客户风险等级是否有效  0:以签署 1:未签署
    public String OFRISK_FLAG;            //客户风险等级和产品风险等级是否匹配  0:合规  2:不匹配 3:风险测评过期
    public String OFUND_RISKLEVEL_NAME;  //产品风险等级名称
    public String RISK_LEVEL;            //客户风险等级 0:默认型 1:保守型 2:稳健性 3:积极型 4:激进型 5:谨慎型 6:成长型 99:专业性
    public String RISK_LEVEL_NAME;      //客户风险等级名称
    public String RISK_RATING;           //产品风险等级
}
