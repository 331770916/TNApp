package com.tpyzq.mobile.pangu.data;

import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangwenbo on 2016/9/17.
 * 资讯实体类
 */
public class InformationEntity {
    //1.7.1要闻
    //1.7.3查询栏目信息列表
    private String newsno;//信息id
    private String title;//标题
    private String digest;//摘要
    private String time;//时间
    private String image_url;//图片地址
    //1.7.2直播
    private String date;//日期
    //1.7.4信息详情
    private String content;//内容
    private String source;//来源
    private String statement;//声明
    private String labelno;//标签id
    private String labelname;//标签名称
    //1.7.5栏目list
    private String classno;//id
    private String classname;//栏目名称
    //1.7.6股票新闻
    private String secuCode;
    private String secuAbbr;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSecuCode() {
        return secuCode;
    }

    public void setSecuCode(String secuCode) {
        this.secuCode = secuCode;
    }

    public String getSecuAbbr() {
        return secuAbbr;
    }

    public void setSecuAbbr(String secuAbbr) {
        this.secuAbbr = secuAbbr;
    }

    private List<InformationEntity> list;

    public String getNewsno() {
        return newsno;
    }

    public void setNewsno(String newsno) {
        this.newsno = newsno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public String getTrimDigest() {
        return digest.replaceAll("\\s+","");
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getLabelno() {
        return labelno;
    }

    public void setLabelno(String labelno) {
        this.labelno = labelno;
    }

    public String getLabelname() {
        return labelname;
    }

    public void setLabelname(String labelname) {
        this.labelname = labelname;
    }

    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public List<InformationEntity> getList() {
        return list;
    }

    public void setList(List<InformationEntity> list) {
        this.list = list;
    }
}
