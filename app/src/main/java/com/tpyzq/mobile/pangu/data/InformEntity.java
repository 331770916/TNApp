package com.tpyzq.mobile.pangu.data;

/**
 * Created by wangqi on 2016/10/13.
 * 推送消息实体类
 */
public class InformEntity {
    private String push_record_id;   //主键ID
    private String account;          //账户
    private String token;            //TOKEN
    private String contene;          //内容带样式
    private String contenebrief;          //内容
    private String objective;        //推送目的
    private String push_time;        //推送时间
    private String start;            //状态（ 苹果 1.成功 0失败）
    private String title;            //标题
    private String remarks;          //安卓推送结果msg
    private String read_type;        //已读状态 0为未读 1为已读
    private String bizid;            //唯一的ID

    public String getPush_record_id() {
        return push_record_id;
    }

    public void setPush_record_id(String push_record_id) {
        this.push_record_id = push_record_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContene() {
        return contene;
    }

    public void setContene(String contene) {
        this.contene = contene;
    }

    public String getContenebrief() {
        return contenebrief;
    }

    public void setContenebrief(String contenebrief) {
        this.contenebrief = contenebrief;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getPush_time() {
        return push_time;
    }

    public void setPush_time(String push_time) {
        this.push_time = push_time;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRead_type() {
        return read_type;
    }

    public void setRead_type(String read_type) {
        this.read_type = read_type;
    }

    public String getBizid() {
        return bizid;
    }

    public void setBizid(String bizid) {
        this.bizid = bizid;
    }
}
