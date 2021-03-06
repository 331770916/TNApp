package com.tpyzq.mobile.pangu.data;


import com.tpyzq.mobile.pangu.log.LogUtil;

/**
 * Created by ltyhome on 23/06/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe: bean info
 */

public class ResultInfo {
   private String code;//0成功  -1失败
   private String msg;//信息
   private String tag;//标记
   private Object data;//数据
   public ResultInfo(){
   }

    public ResultInfo(String info){
       LogUtil.i(info);
   }

   public String getCode(){
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
