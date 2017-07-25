package com.tpyzq.mobile.pangu.db;

/**
 * **************************************************************
 * <p>
 * **************************************************************
 * Authors:huweidong on 2017/6/23 0023 10:50
 * Email：huwwds@gmail.com
 */
public class StockTable extends BaseTable {
    //自选股
     public static final int STOCK_OPTIONAL=1;
    //持仓股
    public static final int STOCK_HOLD=2;
    //历史自选股
    public static final int STOCK_HISTORY_OPTIONAL =4;
    //最近浏览
    public static final int STOCK_BROWSE_NEARBY =8;
}
