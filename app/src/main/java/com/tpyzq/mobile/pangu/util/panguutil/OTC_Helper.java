package com.tpyzq.mobile.pangu.util.panguutil;

/**
 * Created by longfeng on 2017/3/22.
 */

public class OTC_Helper {
    public static  String getEntrustStaus(String status){
        StringBuilder entrust_status = new StringBuilder();
        if ("0".equals(status)) {
            entrust_status.append("未报") ;
        }
        if ("1".equals(status)) {
            entrust_status.append("待报");
        }
        if ("2".equals(status)) {
            entrust_status.append("已报");
        }
        if ("3".equals(status)) {
            entrust_status.append("已报待撤");
        }
        if ("4".equals(status)) {
            entrust_status.append("部成待撤");
        }
        if ("6".equals(status)) {
            entrust_status.append("已撤");
        }
        if ("7".equals(status)) {
            entrust_status.append("部成");
        }
        if ("8".equals(status)) {
            entrust_status.append("已成");
        }
        if ("9".equals(status)) {
            entrust_status.append("废单");
        }
        return entrust_status.toString() ;
    }
}
