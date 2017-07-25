package com.tpyzq.mobile.pangu.data;

/**
 * Created by zhangwenbo on 2016/8/17.
 * 自选股新闻
 */
public class NewsInofEntity {
    private String auth;
    private long dt;
    private String id;
    private String sum;
    private String title;
    private String comp;
    private String tick;
    private String apperholdstock;
    private String stockholdon;
    private String stockCode;//相关股票代码
    private String date;
    private int flag;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public long getDt() {
        return dt;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getTick() {
        return tick;
    }

    public void setTick(String tick) {
        this.tick = tick;
    }

    public String getApperholdstock() {
        return apperholdstock;
    }

    public void setApperholdstock(String apperholdstock) {
        this.apperholdstock = apperholdstock;
    }

    public String getStockholdon() {
        return stockholdon;
    }

    public void setStockholdon(String stockholdon) {
        this.stockholdon = stockholdon;
    }
}
