package com.tpyzq.mobile.pangu.http.doConnect.home;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.data.CleverManamgerMoneyEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/10/10.
 * 获取产品信息网络连接
 */
public class GetProductInfoConnect {


    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetProductInfoConnect";
    private String mHttpTAG;
    private String mToken;
    private String mProCode;
    private String mProType;


    public GetProductInfoConnect(String httpTag, String token, String proCode, String proType) {
        mHttpTAG = httpTag;
        mToken = token;
        mProCode = proCode;
        mProType = proType;
    }

    public void doConnect(ICallbackResult callbackResult){
        mCallbackResult = callbackResult;
        request();
    }

    public void request() {

        Map params = new HashMap();
        params.put("funcid", "100234");
        params.put("token", mToken);

        Map[] array = new Map[1];
        Map map = new HashMap();
        map.put("prodcode", mProCode);
        map.put("type", mProType);

        array[0] = map;

        params.put("parms", map);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.ULR_MANAGEMONEY, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                ArrayList<CleverManamgerMoneyEntity> entities = new ArrayList<CleverManamgerMoneyEntity>();
                CleverManamgerMoneyEntity entity = new CleverManamgerMoneyEntity();
                entity.setMistackMsg("" + e.toString());
                mCallbackResult.getResult(entities, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                LogUtil.e(TAG,"json----"+response);
                //{"totalcount":"1","iTotalDisplayRecords":"1","iTotalRecords":1,"data":[{"RISKLEVEL":"0","TIP":"12","BUY_LOW_AMOUNT":"12","PAYDAY":"2016-09-08","MODIFYTIME":"2016-09-22 13:54:57","DESCRIPTION":"12","INTERESTDAY":"2016-08-29","PRODCODE":"SC702","PUBCOMPANY":"12","CREATETIME":"2016-09-22 14:00:56","PRODTYPE":"123","REVENUERULE":"12","PRODNAME":"测试产品","IPO_END_DATE":"2016-08-28","SUBNAME":"1","ISHOT":"1","ORDERNUM":"0","INCOMETYPE":"1","PRODRATIO":"12%","ENDDAY":"2016-08-31","INVESTDAYS":"12","TYPE":"2","ISSHOW":"1"}],"code":"0","msg":"查询成功"}
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<CustomEntitiy>() {}.getType();
                CustomEntitiy bean = gson.fromJson(response, type);

                ArrayList<CleverManamgerMoneyEntity> entities = new ArrayList<CleverManamgerMoneyEntity>();
                if (bean == null|| bean.getData() == null || bean.getData().size() <= 0) {
                    mCallbackResult.getResult(entities, TAG);
                    return ;
                }
                for (SubCustomEntitiy subCustomEntitiy : bean.getData()) {
                    CleverManamgerMoneyEntity entity = new CleverManamgerMoneyEntity();
                    entity.setPRODCODE(subCustomEntitiy.getPRODCODE());           //产品代码
                    entity.setPRODNAME(subCustomEntitiy.getPRODNAME());        //产品名称
                    entity.setCOMPREF(subCustomEntitiy.getCOMPREF());         //业绩比较基准
                    entity.setBUY_LOW_AMOUNT(subCustomEntitiy.getBUY_LOW_AMOUNT());  //起购金额（元）（整型数字）
                    entity.setINVESTDAYS(subCustomEntitiy.getINVESTDAYS());      //投资期限（天）（整型数字）
                    entity.setRISKLEVEL(subCustomEntitiy.getRISKLEVEL());       //风险等级（整型数字）0:默认等级 1:保本等级 2:低风险等级 3:中风险等级 4:高风险等级
                    entity.setFIRSTAMMOUNT(subCustomEntitiy.getFIRSTAMMOUNT());    //首次参与最低金额（整型数字，元）
                    entity.setAPPENDAMMOUNT(subCustomEntitiy.getAPPENDAMMOUNT());   //追加参与最低金额（整型数字，元）
                    entity.setPRODTYPE(subCustomEntitiy.getPRODTYPE());        //产品类型
                    entity.setPUBCOMPANY(subCustomEntitiy.getPUBCOMPANY());      //发行公司
                    entity.setDESCRIPITION(subCustomEntitiy.getDESCRIPITION());    //相关说明
                    entity.setTYPE(subCustomEntitiy.getTYPE());            //产品大类，固定为3 (1-开放式基金2-OTC 3-14天现金增益
                    entity.setORDERNUM(subCustomEntitiy.getORDERNUM());        //排序字段，小的靠前
                    entity.setISHOT(subCustomEntitiy.getISHOT());           //是否热销0否1是
                    entity.setISSHOW(subCustomEntitiy.getISSHOW());          //是否展示(0展示-1不展示)
                    entity.setCREATETIME(subCustomEntitiy.getCREATETIME());      //创建时间yyyy-MM-dd
                    entity.setMODIFYTIME(subCustomEntitiy.getMODIFYTIME());      //修改时间yyyy-MM-dd
                    entity.setSUBNAME(subCustomEntitiy.getSUBNAME());         //产品简称

                    List<NoticeArgument> noticeDates = subCustomEntitiy.getNOTICEDATE();

                    if (noticeDates != null && noticeDates.size() > 0) {
                        ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
                        for (NoticeArgument _bean : noticeDates) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("name", _bean.getFileName());
                            map.put("url", _bean.getFileUrl());
                            maps.add(map);
                        }
                        entity.setNoticeDates(maps);
                    }

                    List<NoticeArgument> proaoclDates = subCustomEntitiy.getPROAOCOLDATE();

                    if (proaoclDates != null && proaoclDates.size() > 0) {
                        ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
                        for (NoticeArgument _bean : proaoclDates) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("name", _bean.getFileName());
                            map.put("url", _bean.getFileUrl());
                            maps.add(map);
                        }
                        entity.setProaoclDates(maps);
                    }

                    //otc
                    entity.setDESCRIPTION(subCustomEntitiy.getDESCRIPTION());     //相关说明
                    entity.setTIP(subCustomEntitiy.getTIP());             //挂钩标的
                    entity.setINCOMETYPE(subCustomEntitiy.getINCOMETYPE());      //收益类型（1固定2浮动3固定+浮动）
                    entity.setPRODRATIO(subCustomEntitiy.getPRODRATIO());       //收益率
                    entity.setREVENUERULE(subCustomEntitiy.getREVENUERULE());     //收益规则
                    entity.setIPO_END_DATE(subCustomEntitiy.getIPO_END_DATE());    //募集结束
                    entity.setINTERESTDAY(subCustomEntitiy.getINTERESTDAY());     //起息日期（yyyy-MM-dd）
                    entity.setENDDAY(subCustomEntitiy.getENDDAY());          //到期日（yyyy-MM-dd）
                    entity.setPAYDAY(subCustomEntitiy.getPAYDAY());          //资金到账日;
                    List<NoticeArgument> otcNoticeDate = subCustomEntitiy.getOTCNOTICEDATE();

                    if (otcNoticeDate != null && otcNoticeDate.size() > 0) {
                        ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
                        for (NoticeArgument _bean : otcNoticeDate) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("name", _bean.getFileName());
                            map.put("url", _bean.getFileUrl());
                            maps.add(map);
                        }
                        entity.setOtcNoticeDate(maps);
                    }


                    List<NoticeArgument> otcProaoclDate = subCustomEntitiy.getOTCPROAOCOLDATE();


                    if (otcProaoclDate != null && otcProaoclDate.size() > 0) {
                        ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
                        for (NoticeArgument _bean : otcProaoclDate) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("name", _bean.getFileName());
                            map.put("url", _bean.getFileUrl());
                            maps.add(map);
                        }
                        entity.setOtcProaoclDate(maps);
                    }

                    //基金

                    entity.setSECURITYCODE(subCustomEntitiy.getSECURITYCODE());    //基金代码
                    entity.setFUNDNAME(subCustomEntitiy.getFUNDNAME());        //基金名称
                    entity.setKFZT(subCustomEntitiy.getKFZT());            //开放状态
                    entity.setFXDJ(subCustomEntitiy.getFXDJ());            //风险等级
                    entity.setQGJE(subCustomEntitiy.getQGJE());            //起购金额
                    entity.setFUNDTYPE(subCustomEntitiy.getFUNDTYPE());        //基金类型
                    entity.setESTABLISHMENTDATE(subCustomEntitiy.getESTABLISHMENTDATE());   //成立日期（yyyy-mm-dd）
                    entity.setUNITNV(subCustomEntitiy.getUNITNV());              //最新单位净值
                    entity.setACCUMULATEDUNITNV(subCustomEntitiy.getACCUMULATEDUNITNV());   //单位累计净值
                    entity.setDAILYPROFIT(subCustomEntitiy.getDAILYPROFIT());         //每万份基金单位当日收益(元)
                    entity.setLATESTWEEKLYYIELD(subCustomEntitiy.getLATESTWEEKLYYIELD());   //最近7日折算年收益率
                    entity.setCHANGEPCTRM(subCustomEntitiy.getCHANGEPCTRM());         //月涨跌幅（%）
                    entity.setINVESTADVISORNAME(subCustomEntitiy.getINVESTADVISORNAME());   //基金管理人（基金公司）
                    entity.setFUNDTYPECODE(subCustomEntitiy.getFUNDTYPECODE());        //1101-股票型；1103-混合型；1105-债券型；1107-保本型；1109-货币型；1199-其他型
                    entities.add(entity);
                }

                mCallbackResult.getResult(entities, TAG);

            }
        });
    }


    private class CustomEntitiy {

        //14天, otc
        private String code;                    //0表示成功 -1参数不正确 -3数据库异常 -4其他异常
        private String msg;                     //错误的原因
        private String iTotalRecords;           //本次查询到的总条数
        private String iTotalDisplayRecords;    //记录总条数
        private List<SubCustomEntitiy> data;    //

        //基金
        private String totalcount;

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

        public String getiTotalRecords() {
            return iTotalRecords;
        }

        public void setiTotalRecords(String iTotalRecords) {
            this.iTotalRecords = iTotalRecords;
        }

        public String getiTotalDisplayRecords() {
            return iTotalDisplayRecords;
        }

        public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
            this.iTotalDisplayRecords = iTotalDisplayRecords;
        }

        public List<SubCustomEntitiy> getData() {
            return data;
        }

        public void setData(List<SubCustomEntitiy> data) {
            this.data = data;
        }
    }

    private class SubCustomEntitiy {
        //14天

        private String PRODCODE;        //产品代码
        private String PRODNAME;        //产品名称
        private String COMPREF;         //业绩比较基准
        private String BUY_LOW_AMOUNT;  //起购金额（元）（整型数字）
        private String INVESTDAYS;      //投资期限（天）（整型数字）
        private String RISKLEVEL;       //风险等级（整型数字）0:默认等级 1:保本等级 2:低风险等级 3:中风险等级 4:高风险等级
        private String FIRSTAMMOUNT;    //首次参与最低金额（整型数字，元）
        private String APPENDAMMOUNT;   //追加参与最低金额（整型数字，元）
        private String PRODTYPE;        //产品类型
        private String PUBCOMPANY;      //发行公司
        private String DESCRIPITION;    //相关说明
        private String TYPE;            //产品大类，固定为3 (1-开放式基金2-OTC 3-14天现金增益
        private String ORDERNUM;        //排序字段，小的靠前
        private String ISHOT;           //是否热销0否1是
        private String ISSHOW;          //是否展示(0展示-1不展示)
        private String CREATETIME;      //创建时间yyyy-MM-dd
        private String MODIFYTIME;      //修改时间yyyy-MM-dd
        private String SUBNAME;         //产品简称
        private List<NoticeArgument> NOTICEDATE;    //产品公告
        private List<NoticeArgument> PROAOCOLDATE;  //服务协议


        //otc
        private String DESCRIPTION;     //相关说明
        private String TIP;             //挂钩标的
        private String INCOMETYPE;      //收益类型（1固定2浮动3固定+浮动）
        private String PRODRATIO;       //收益率
        private String REVENUERULE;     //收益规则
        private String IPO_END_DATE;    //募集结束
        private String INTERESTDAY;     //起息日期（yyyy-MM-dd）
        private String ENDDAY;          //到期日（yyyy-MM-dd）
        private String PAYDAY;          //资金到账日;
        private List<NoticeArgument> OTCPROAOCOLDATE;   //otc服务协议
        private List<NoticeArgument> OTCNOTICEDATE;     //otc产品公告

        //基金

        private String SECURITYCODE;    //基金代码
        private String FUNDNAME;        //基金名称
        private String KFZT;            //开放状态
        private String FXDJ;            //风险等级
        private String QGJE;            //起购金额
        private String FUNDTYPE;        //基金类型
        private String ESTABLISHMENTDATE;   //成立日期（yyyy-mm-dd）
        private String UNITNV;              //最新单位净值
        private String ACCUMULATEDUNITNV;   //单位累计净值
        private String DAILYPROFIT;         //每万份基金单位当日收益(元)
        private String LATESTWEEKLYYIELD;   //最近7日折算年收益率
        private String CHANGEPCTRM;         //月涨跌幅（%）
        private String INVESTADVISORNAME;   //基金管理人（基金公司）
        private String FUNDTYPECODE;        //1101-股票型；1103-混合型；1105-债券型；1107-保本型；1109-货币型；1199-其他型


        public List<NoticeArgument> getNOTICEDATE() {
            return NOTICEDATE;
        }

        public void setNOTICEDATE(List<NoticeArgument> NOTICEDATE) {
            this.NOTICEDATE = NOTICEDATE;
        }

        public List<NoticeArgument> getPROAOCOLDATE() {
            return PROAOCOLDATE;
        }

        public void setPROAOCOLDATE(List<NoticeArgument> PROAOCOLDATE) {
            this.PROAOCOLDATE = PROAOCOLDATE;
        }

        public List<NoticeArgument> getOTCPROAOCOLDATE() {
            return OTCPROAOCOLDATE;
        }

        public void setOTCPROAOCOLDATE(List<NoticeArgument> OTCPROAOCOLDATE) {
            this.OTCPROAOCOLDATE = OTCPROAOCOLDATE;
        }

        public List<NoticeArgument> getOTCNOTICEDATE() {
            return OTCNOTICEDATE;
        }

        public void setOTCNOTICEDATE(List<NoticeArgument> OTCNOTICEDATE) {
            this.OTCNOTICEDATE = OTCNOTICEDATE;
        }

        public String getPRODCODE() {
            return PRODCODE;
        }

        public void setPRODCODE(String PRODCODE) {
            this.PRODCODE = PRODCODE;
        }

        public String getPRODNAME() {
            return PRODNAME;
        }

        public void setPRODNAME(String PRODNAME) {
            this.PRODNAME = PRODNAME;
        }

        public String getCOMPREF() {
            return COMPREF;
        }

        public void setCOMPREF(String COMPREF) {
            this.COMPREF = COMPREF;
        }

        public String getBUY_LOW_AMOUNT() {
            return BUY_LOW_AMOUNT;
        }

        public void setBUY_LOW_AMOUNT(String BUY_LOW_AMOUNT) {
            this.BUY_LOW_AMOUNT = BUY_LOW_AMOUNT;
        }

        public String getINVESTDAYS() {
            return INVESTDAYS;
        }

        public void setINVESTDAYS(String INVESTDAYS) {
            this.INVESTDAYS = INVESTDAYS;
        }

        public String getRISKLEVEL() {
            return RISKLEVEL;
        }

        public void setRISKLEVEL(String RISKLEVEL) {
            this.RISKLEVEL = RISKLEVEL;
        }

        public String getFIRSTAMMOUNT() {
            return FIRSTAMMOUNT;
        }

        public void setFIRSTAMMOUNT(String FIRSTAMMOUNT) {
            this.FIRSTAMMOUNT = FIRSTAMMOUNT;
        }

        public String getAPPENDAMMOUNT() {
            return APPENDAMMOUNT;
        }

        public void setAPPENDAMMOUNT(String APPENDAMMOUNT) {
            this.APPENDAMMOUNT = APPENDAMMOUNT;
        }

        public String getPRODTYPE() {
            return PRODTYPE;
        }

        public void setPRODTYPE(String PRODTYPE) {
            this.PRODTYPE = PRODTYPE;
        }

        public String getPUBCOMPANY() {
            return PUBCOMPANY;
        }

        public void setPUBCOMPANY(String PUBCOMPANY) {
            this.PUBCOMPANY = PUBCOMPANY;
        }

        public String getDESCRIPITION() {
            return DESCRIPITION;
        }

        public void setDESCRIPITION(String DESCRIPITION) {
            this.DESCRIPITION = DESCRIPITION;
        }

        public String getTYPE() {
            return TYPE;
        }

        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
        }

        public String getORDERNUM() {
            return ORDERNUM;
        }

        public void setORDERNUM(String ORDERNUM) {
            this.ORDERNUM = ORDERNUM;
        }

        public String getISHOT() {
            return ISHOT;
        }

        public void setISHOT(String ISHOT) {
            this.ISHOT = ISHOT;
        }

        public String getISSHOW() {
            return ISSHOW;
        }

        public void setISSHOW(String ISSHOW) {
            this.ISSHOW = ISSHOW;
        }

        public String getCREATETIME() {
            return CREATETIME;
        }

        public void setCREATETIME(String CREATETIME) {
            this.CREATETIME = CREATETIME;
        }

        public String getMODIFYTIME() {
            return MODIFYTIME;
        }

        public void setMODIFYTIME(String MODIFYTIME) {
            this.MODIFYTIME = MODIFYTIME;
        }

        public String getSUBNAME() {
            return SUBNAME;
        }

        public void setSUBNAME(String SUBNAME) {
            this.SUBNAME = SUBNAME;
        }

        public String getDESCRIPTION() {
            return DESCRIPTION;
        }

        public void setDESCRIPTION(String DESCRIPTION) {
            this.DESCRIPTION = DESCRIPTION;
        }

        public String getTIP() {
            return TIP;
        }

        public void setTIP(String TIP) {
            this.TIP = TIP;
        }

        public String getINCOMETYPE() {
            return INCOMETYPE;
        }

        public void setINCOMETYPE(String INCOMETYPE) {
            this.INCOMETYPE = INCOMETYPE;
        }

        public String getPRODRATIO() {
            return PRODRATIO;
        }

        public void setPRODRATIO(String PRODRATIO) {
            this.PRODRATIO = PRODRATIO;
        }

        public String getREVENUERULE() {
            return REVENUERULE;
        }

        public void setREVENUERULE(String REVENUERULE) {
            this.REVENUERULE = REVENUERULE;
        }

        public String getIPO_END_DATE() {
            return IPO_END_DATE;
        }

        public void setIPO_END_DATE(String IPO_END_DATE) {
            this.IPO_END_DATE = IPO_END_DATE;
        }

        public String getINTERESTDAY() {
            return INTERESTDAY;
        }

        public void setINTERESTDAY(String INTERESTDAY) {
            this.INTERESTDAY = INTERESTDAY;
        }

        public String getENDDAY() {
            return ENDDAY;
        }

        public void setENDDAY(String ENDDAY) {
            this.ENDDAY = ENDDAY;
        }

        public String getPAYDAY() {
            return PAYDAY;
        }

        public void setPAYDAY(String PAYDAY) {
            this.PAYDAY = PAYDAY;
        }

        public String getSECURITYCODE() {
            return SECURITYCODE;
        }

        public void setSECURITYCODE(String SECURITYCODE) {
            this.SECURITYCODE = SECURITYCODE;
        }

        public String getFUNDNAME() {
            return FUNDNAME;
        }

        public void setFUNDNAME(String FUNDNAME) {
            this.FUNDNAME = FUNDNAME;
        }

        public String getKFZT() {
            return KFZT;
        }

        public void setKFZT(String KFZT) {
            this.KFZT = KFZT;
        }

        public String getFXDJ() {
            return FXDJ;
        }

        public void setFXDJ(String FXDJ) {
            this.FXDJ = FXDJ;
        }

        public String getQGJE() {
            return QGJE;
        }

        public void setQGJE(String QGJE) {
            this.QGJE = QGJE;
        }

        public String getFUNDTYPE() {
            return FUNDTYPE;
        }

        public void setFUNDTYPE(String FUNDTYPE) {
            this.FUNDTYPE = FUNDTYPE;
        }

        public String getESTABLISHMENTDATE() {
            return ESTABLISHMENTDATE;
        }

        public void setESTABLISHMENTDATE(String ESTABLISHMENTDATE) {
            this.ESTABLISHMENTDATE = ESTABLISHMENTDATE;
        }

        public String getUNITNV() {
            return UNITNV;
        }

        public void setUNITNV(String UNITNV) {
            this.UNITNV = UNITNV;
        }

        public String getACCUMULATEDUNITNV() {
            return ACCUMULATEDUNITNV;
        }

        public void setACCUMULATEDUNITNV(String ACCUMULATEDUNITNV) {
            this.ACCUMULATEDUNITNV = ACCUMULATEDUNITNV;
        }

        public String getDAILYPROFIT() {
            return DAILYPROFIT;
        }

        public void setDAILYPROFIT(String DAILYPROFIT) {
            this.DAILYPROFIT = DAILYPROFIT;
        }

        public String getLATESTWEEKLYYIELD() {
            return LATESTWEEKLYYIELD;
        }

        public void setLATESTWEEKLYYIELD(String LATESTWEEKLYYIELD) {
            this.LATESTWEEKLYYIELD = LATESTWEEKLYYIELD;
        }

        public String getCHANGEPCTRM() {
            return CHANGEPCTRM;
        }

        public void setCHANGEPCTRM(String CHANGEPCTRM) {
            this.CHANGEPCTRM = CHANGEPCTRM;
        }

        public String getINVESTADVISORNAME() {
            return INVESTADVISORNAME;
        }

        public void setINVESTADVISORNAME(String INVESTADVISORNAME) {
            this.INVESTADVISORNAME = INVESTADVISORNAME;
        }

        public String getFUNDTYPECODE() {
            return FUNDTYPECODE;
        }

        public void setFUNDTYPECODE(String FUNDTYPECODE) {
            this.FUNDTYPECODE = FUNDTYPECODE;
        }
    }

    private class NoticeArgument {
        private String fileName;
        private String fileUrl;


        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }

}
