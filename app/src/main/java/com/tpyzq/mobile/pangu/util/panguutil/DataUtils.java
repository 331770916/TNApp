package com.tpyzq.mobile.pangu.util.panguutil;

import com.tpyzq.mobile.pangu.R;

/**
 * Created by 陈新宇 on 2016/8/11.
 */
public class DataUtils {

    public static String[] sell = {"卖5", "卖4", "卖3", "卖2", "卖1"};
    public static String[] buy = {"买1", "买2", "买3", "买4", "买5"};

    public static String[] loan = {"借出5", "借出4", "借出3", "借出2", "借出1"};
    public static String[] borrow = {"借入1", "借入2", "借入3", "借入4", "借入5"};


    public static String[] transaction_name = {"股票", "开放式基金", "OTC业务"};
    public static int[] transaction_icon = {R.mipmap.jy_gupiao, R.mipmap.jy_kaifangshijijin, R.mipmap.jy_otc};
    public static String[][] transaction_grid_name = {{"我的持仓", "买入", "卖出", "撤单", "查询", "新股", "银证转账", ""},
            {"基金份额", "基金认购", "基金申购", "基金赎回", "基金撤单", "基金信息", "基金开户", ""},
            {"OTC份额", "OTC认购", "OTC申购", "OTC赎回", "OTC撤单", "查询", "OTC账户", "电子合同"}};
    public static int[][] transaction_grid_icon = {{R.mipmap.jy_wodechicang, R.mipmap.jy_mairu, R.mipmap.jy_maichu, R.mipmap.jy_chedan, R.mipmap.jy_chaxun, R.mipmap.jy_xingu, R.mipmap.jy_yinzhengzhuanzhang, R.mipmap.jy_gengduo},
            {R.mipmap.jy_jijinfene, R.mipmap.jy_jijinrengou, R.mipmap.jy_jijinshengou, R.mipmap.jy_jijingouhui, R.mipmap.jy_jijinchedan, R.mipmap.jy_jijinxinxi, R.mipmap.jy_jijinkaihu, R.mipmap.jy_gengduo},
            {R.mipmap.jy_jijinfene, R.mipmap.jy_jijinrengou, R.mipmap.jy_jijinshengou, R.mipmap.jy_jijingouhui, R.mipmap.jy_jijinchedan, R.mipmap.jy_chaxun, R.mipmap.otc_zhanghu, R.mipmap.jy_dianzihetong}};


    public static String[] open_fund_name = {"委托查询", "历史成交", "分红设置", "基金转换", "基金账户查询", "基金电子合同签署"};
    public static int[] open_fund_icon = {R.mipmap.open_weituochaxun, R.mipmap.open_lishichengjiao, R.mipmap.open_fenhongshezhi, R.mipmap.open_jijinzhuanhuan, R.mipmap.open_jijinzhanghuchaxun, R.mipmap.open_jijindianzihetongqianshu};

    public static String[] stock_morelist_name = {"场内基金", "国债逆回购","盘后分级基金"};//,"网络投票"
    public static int[] stock_morelist_icon = {R.mipmap.item_changnei, R.mipmap.item_nihuigou,R.mipmap.item_changnei};//,R.mipmap.open_lishichengjiao

    public static String[] stock_votelist_name = {"网络投票","投票查询"};
    public static int[] stock_votelist_icon = {R.mipmap.item_changnei, R.mipmap.item_nihuigou};


    public static String[] relationship_name = {"父母", "夫妻", "子女", "朋友", "其他"};

    public static String[] myself_name = {"股票", "基金", "OTC", "可取资金"};
    public static int[] myself_icon = {R.mipmap.myself_gupiao, R.mipmap.myself_jijin, R.mipmap.myself_licai, R.mipmap.myself_keyongzijin};


    public static String[] structuredfunda_name ={"分级基金合并","分级基金分拆","分级基金撤单","委托查询","成交查询"};
    public static int[] structuredfunda_icon ={R.mipmap.structuredfund_merge,R.mipmap.structuredfund_partition,R.mipmap.structuredfund_revoke,R.mipmap.structuredfund_entrust_inquire,R.mipmap.structuredfund_conclusionofbusiness_inquire};
}
