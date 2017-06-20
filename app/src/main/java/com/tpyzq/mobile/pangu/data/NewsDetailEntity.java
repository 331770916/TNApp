package com.tpyzq.mobile.pangu.data;

import java.util.List;

/**
 * 作者：刘泽鹏 on 2016/10/8 17:17
 * 新闻详情页面   解析数据的实体类
 */
public class NewsDetailEntity {
    public Long dt;                //时间
    public Ind ind;                 //
    public String sum;              //摘要
    public List<String> tags;       //存储标签的 集合
    public String title;            //标题
    public List<Stocks> stocks;     //存储  相关股票信息的集合
    public String url;

    public class Ind{
        public List<Chain> chain;   //存储
        public String samName;      //中间展示的  行业名称
        //存储 上游 下游  的名称
        public class Chain{
            public String level;
            public String name;
        }
    }

    //股票信息
    public class Stocks{
        public String name;
        public String secu;
        public String tick;
    }
}
