package com.tpyzq.mobile.pangu.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.DbUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.TransitionUtils;

import java.util.ArrayList;

/**
 * Created by 刘泽鹏 on 2016/7/19.
 *
 * 自选股股票列表
 */
public class Db_PUB_STOCKLIST {
    private static final String TAG = "Db_PUB_STOCKLIST";


    /**
     * 获取数据库数据总数
     * @return
     */
    public static int getStockListCount() {
        StringBuilder sql = new StringBuilder("select * from PUB_STOCKLIST");
        int count = 0;
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return 0;
            }
            count = c.getCount();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
//            if(db != null){
//                db.close();
//            }
        }

        return count;
    }


    /**
     * 批量添加自选股
     * @param entities
     * @return
     */
    public static boolean addStockListDatas(ArrayList<StockInfoEntity> entities) {
        boolean isSuccelssFul = false;
        SQLiteDatabase db = null;
        if (null != entities && entities.size() <= 0) {
            return isSuccelssFul;
        }

        try {
            StringBuilder sql = new StringBuilder("insert into PUB_STOCKLIST (ID, ISHOLD, STOCKCODE, " +
                    "NAME, PRICECHANGEVALUE, PRICECHANGERATIO, CURRENTPRICE, " +
                    "BEFORECLOSEPRICE, TODAYOPENPRICE, TIME, DATE, HIGH, LOW, PLATE," +
                    "PRESENTAMOUNT, TOTALVOLUME, TOTALPRICE, INFLOWMONEY, OUTFLOWMONEY, BUYORDER, SELLORDER, TURNOVER," +
                    "TRADE, TYPE, TRADECODE, PEG, FLOWEQUITY, TOTALEQUITY, PBR, INDYSTRYUPANDDOWN, EQUIVALENTRATIO, BUY1, BUY2," +
                    "BUY3, BUY4, BUY5, BUY1AMOUNT, BUY2AMOUNT, BUY3AMOUNT, BUY4AMOUNT, BUY5AMOUNT, SELL1, SELL2, SELL3, SELL4, SELL5," +
                    "SELL1AMOUNT, SELL2AMOUNT, SELL3AMOUNT, SELL4AMOUNT, SELL5AMOUNT, isHoldStock, apperHoldStock, MKT_VAL, SECU_ACC, SHARE_BLN" +
                    ", SHARE_AVL, SHARE_QTY, PROFIT_RATIO, MARKET, BRANCH, MKT_PRICE, SECU_CODE, INCOME_AMT, " +
                    "SECU_NAME, CURRENT_COST) VALUES ((SELECT max(seq) FROM sqlite_sequence) + 1 , " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ? )");

            db = DbUtil.getInstance().getDB();
            SQLiteStatement stat = db.compileStatement(sql.toString());
            db.beginTransaction();

            int totalAddCount = 0;//统计添加到数据库的条数

            for (StockInfoEntity entitiy : entities) {
                //如果数据库中有重复数据， 则先删除重复数据
                StockInfoEntity temEntity = queryStockFromID(entitiy.getStockNumber());

                if (temEntity != null) {
                    deleteStockFromID(temEntity.getStockNumber());
                }

//                //查询数据条数
//                int count = getStockListCount();
//
//                if (count > 50) {
//
//                }


                stat.bindString(1, entitiy.getApperHoldStock());
                stat.bindString(2, entitiy.getStockNumber());
                stat.bindString(3, entitiy.getStockName());
                stat.bindString(4, ""+entitiy.getUpAndDownValue());
                stat.bindString(5, ""+entitiy.getPriceChangeRatio());
                stat.bindString(6, entitiy.getNewPrice());
                stat.bindString(7, entitiy.getClose());
                stat.bindString(8, entitiy.getTodayOpenPrice());
                stat.bindString(9, entitiy.getTime());
                stat.bindString(10, entitiy.getDate());
                stat.bindString(11, entitiy.getHigh());
                stat.bindString(12, entitiy.getLow());
                stat.bindString(13, entitiy.getPlate());
                stat.bindString(14, entitiy.getPresentAmount());
                stat.bindString(15, entitiy.getTotalVolume());
                stat.bindString(16, entitiy.getTotalPrice());
                stat.bindString(17, entitiy.getInflowMoney());
                stat.bindString(18, entitiy.getOutflowMoney());
                stat.bindString(19, entitiy.getBuyOrder());
                stat.bindString(20, entitiy.getSellOrder());
                stat.bindString(21, entitiy.getTurnover());
                stat.bindString(22, entitiy.getTrade());
                stat.bindString(23, entitiy.getIndustryName());
                stat.bindString(24, entitiy.getIndustryNumber());
                stat.bindString(25, entitiy.getPeg());
                stat.bindString(26, entitiy.getFlowEquity());
                stat.bindString(27, entitiy.getTotalEquity());
                stat.bindString(28, entitiy.getPbr());
                stat.bindString(29, entitiy.getIndustryUpAndDown());
                stat.bindString(30, entitiy.getEquivalentRatio());
                stat.bindString(31, entitiy.getBuy1());
                stat.bindString(32, entitiy.getBuy2());
                stat.bindString(33, entitiy.getBuy3());
                stat.bindString(34, entitiy.getBuy4());
                stat.bindString(35, entitiy.getBuy5());
                stat.bindString(36, entitiy.getBuy1Amount());
                stat.bindString(37, entitiy.getBuy2Amount());
                stat.bindString(38, entitiy.getBuy3Amount());
                stat.bindString(39, entitiy.getBuy4Amount());
                stat.bindString(40, entitiy.getBuy5Amount());
                stat.bindString(41, entitiy.getSell1());
                stat.bindString(42, entitiy.getSell2());
                stat.bindString(43, entitiy.getSell3());
                stat.bindString(44, entitiy.getSell4());
                stat.bindString(45, entitiy.getSell5());
                stat.bindString(46, entitiy.getSell1Amount());
                stat.bindString(47, entitiy.getSell2Amount());
                stat.bindString(48, entitiy.getSell3Amount());
                stat.bindString(49, entitiy.getSell4Amount());
                stat.bindString(50, entitiy.getSell5Amount());
                stat.bindString(51, entitiy.getIsHoldStock());
                stat.bindString(52, entitiy.getApperHoldStock());
                stat.bindString(53, entitiy.getMKT_VAL());
                stat.bindString(54, entitiy.getSECU_ACC());
                stat.bindString(55, entitiy.getSHARE_BLN());
                stat.bindString(56, entitiy.getSHARE_AVL());
                stat.bindString(57, entitiy.getSHARE_QTY());
                stat.bindString(58, entitiy.getPROFIT_RATIO());
                stat.bindString(59, entitiy.getMARKET());
                stat.bindString(60, entitiy.getBRANCH());
                stat.bindString(61, entitiy.getMKT_PRICE());
                stat.bindString(62, entitiy.getSECU_CODE());
                stat.bindString(63, entitiy.getINCOME_AMT());
                stat.bindString(64, entitiy.getSECU_NAME());
                stat.bindString(65, entitiy.getCURRENT_COST());

                long result = stat.executeInsert();
                if (result < 0) {
                    return isSuccelssFul;
                }
            }
            db.setTransactionSuccessful();
            isSuccelssFul = true;
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.e(TAG, e.toString());
            isSuccelssFul = false;
        } finally {
            try {
                if (null != db) {
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return isSuccelssFul;
    }

    /**
     * 增加一条
     * @param entitiy
     */
    public static boolean addOneStockListData(StockInfoEntity entitiy){

        boolean tag = false;

        StockInfoEntity temEntity = queryStockFromID(entitiy.getStockNumber());

        if (temEntity != null) {
            deleteStockFromID(temEntity.getStockNumber());
        }

        int count = getStockListCount();
        if (count > 50) {
            return false;
        }

        StringBuilder sql = new StringBuilder("insert into PUB_STOCKLIST (ID, ISHOLD, STOCKCODE, " +
                "NAME, PRICECHANGEVALUE, PRICECHANGERATIO, CURRENTPRICE,BEFORECLOSEPRICE,TODAYOPENPRICE,TIME,DATE,HIGH,LOW,PLATE," +
                "PRESENTAMOUNT,TOTALVOLUME,TOTALPRICE,INFLOWMONEY,OUTFLOWMONEY,BUYORDER,SELLORDER,TURNOVER," +
                "TRADE,TYPE,TRADECODE,PEG,FLOWEQUITY,TOTALEQUITY,PBR,INDYSTRYUPANDDOWN,EQUIVALENTRATIO,BUY1,BUY2," +
                "BUY3,BUY4,BUY5,BUY1AMOUNT,BUY2AMOUNT,BUY3AMOUNT,BUY4AMOUNT,BUY5AMOUNT,SELL1,SELL2,SELL3,SELL4,SELL5," +
                "SELL1AMOUNT,SELL2AMOUNT,SELL3AMOUNT,SELL4AMOUNT,SELL5AMOUNT, isHoldStock, apperHoldStock, MKT_VAL, SECU_ACC, SHARE_BLN" +
                ", SHARE_AVL, SHARE_QTY, PROFIT_RATIO, MARKET, BRANCH, MKT_PRICE, SECU_CODE, INCOME_AMT, SECU_NAME, CURRENT_COST) VALUES ((SELECT max(seq) FROM sqlite_sequence) + 1 ,");

        sql.append(Helper.getSafeString(entitiy.getApperHoldStock())).append(",")
                .append(Helper.getSafeString(entitiy.getStockNumber())).append(",")
                .append(Helper.getSafeString(entitiy.getStockName())).append(",")
                .append(Helper.getSafeString(""+entitiy.getUpAndDownValue())).append(",")
                .append(Helper.getSafeString(""+entitiy.getPriceChangeRatio())).append(",")
                .append(Helper.getSafeString(entitiy.getNewPrice())).append(",")
                .append(Helper.getSafeString(entitiy.getClose())).append(",")
                .append(Helper.getSafeString(entitiy.getTodayOpenPrice())).append(",")
                .append(Helper.getSafeString(entitiy.getTime())).append(",")
                .append(Helper.getSafeString(entitiy.getDate())).append(",")
                .append(Helper.getSafeString(entitiy.getHigh())).append(",")
                .append(Helper.getSafeString(entitiy.getLow())).append(",")
                .append(Helper.getSafeString(entitiy.getPlate())).append(",")
                .append(Helper.getSafeString(entitiy.getPresentAmount())).append(",")
                .append(Helper.getSafeString(entitiy.getTotalVolume())).append(",")
                .append(Helper.getSafeString(entitiy.getTotalPrice())).append(",")
                .append(Helper.getSafeString(entitiy.getInflowMoney())).append(",")
                .append(Helper.getSafeString(entitiy.getOutflowMoney())).append(",")
                .append(Helper.getSafeString(entitiy.getBuyOrder())).append(",")
                .append(Helper.getSafeString(entitiy.getSellOrder())).append(",")
                .append(Helper.getSafeString(entitiy.getTurnover())).append(",")
                .append(Helper.getSafeString(entitiy.getTrade())).append(",")
                .append(Helper.getSafeString(entitiy.getIndustryName())).append(",")
                .append(Helper.getSafeString(entitiy.getIndustryNumber())).append(",")
                .append(Helper.getSafeString(entitiy.getPeg())).append(",")
                .append(Helper.getSafeString(entitiy.getFlowEquity())).append(",")
                .append(Helper.getSafeString(entitiy.getTotalEquity())).append(",")
                .append(Helper.getSafeString(entitiy.getPbr())).append(",")
                .append(Helper.getSafeString(entitiy.getIndustryUpAndDown())).append(",")
                .append(Helper.getSafeString(entitiy.getEquivalentRatio())).append(",")
                .append(Helper.getSafeString(entitiy.getBuy1())).append(",")
                .append(Helper.getSafeString(entitiy.getBuy2())).append(",")
                .append(Helper.getSafeString(entitiy.getBuy3())).append(",")
                .append(Helper.getSafeString(entitiy.getBuy4())).append(",")
                .append(Helper.getSafeString(entitiy.getBuy5())).append(",")
                .append(Helper.getSafeString(entitiy.getBuy1Amount())).append(",")
                .append(Helper.getSafeString(entitiy.getBuy2Amount())).append(",")
                .append(Helper.getSafeString(entitiy.getBuy3Amount())).append(",")
                .append(Helper.getSafeString(entitiy.getBuy4Amount())).append(",")
                .append(Helper.getSafeString(entitiy.getBuy5Amount())).append(",")
                .append(Helper.getSafeString(entitiy.getSell1())).append(",")
                .append(Helper.getSafeString(entitiy.getSell2())).append(",")
                .append(Helper.getSafeString(entitiy.getSell3())).append(",")
                .append(Helper.getSafeString(entitiy.getSell4())).append(",")
                .append(Helper.getSafeString(entitiy.getSell5())).append(",")
                .append(Helper.getSafeString(entitiy.getSell1Amount())).append(",")
                .append(Helper.getSafeString(entitiy.getSell2Amount())).append(",")
                .append(Helper.getSafeString(entitiy.getSell3Amount())).append(",")
                .append(Helper.getSafeString(entitiy.getSell4Amount())).append(",")
                .append(Helper.getSafeString(entitiy.getSell5Amount())).append(",")
                .append(Helper.getSafeString(entitiy.getIsHoldStock())).append(",")
                .append(Helper.getSafeString(entitiy.getApperHoldStock())).append(",")
                .append(Helper.getSafeString(entitiy.getMKT_VAL())).append(",")
                .append(Helper.getSafeString(entitiy.getSECU_ACC())).append(",")
                .append(Helper.getSafeString(entitiy.getSHARE_BLN())).append(",")
                .append(Helper.getSafeString(entitiy.getSHARE_AVL())).append(",")
                .append(Helper.getSafeString(entitiy.getSHARE_QTY())).append(",")
                .append(Helper.getSafeString(entitiy.getPROFIT_RATIO())).append(",")
                .append(Helper.getSafeString(entitiy.getMARKET())).append(",")
                .append(Helper.getSafeString(entitiy.getBRANCH())).append(",")
                .append(Helper.getSafeString(entitiy.getMKT_PRICE())).append(",")
                .append(Helper.getSafeString(entitiy.getSECU_CODE())).append(",")
                .append(Helper.getSafeString(entitiy.getINCOME_AMT())).append(",")
                .append(Helper.getSafeString(entitiy.getSECU_NAME())).append(",")
                .append(Helper.getSafeString(entitiy.getCURRENT_COST())).append(")");


        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
            tag = true;
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return tag;
    }


    /**
     * 按现价正序排列查询所有股票数据
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatasByPriceAsc(){
        String sql = "SELECT * from PUB_STOCKLIST  LEFT JOIN HOLD_SEQ ON PUB_STOCKLIST.STOCKCODE = HOLD_SEQ.CODE  ORDER BY CURRENTPRICE ASC";

        SQLiteDatabase db =  DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("ISHOLD")));
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
                entitiy.setUpAndDownValue(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGEVALUE"))));
                entitiy.setPriceChangeRatio(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGERATIO"))));
                entitiy.setNewPrice(c.getString(c.getColumnIndexOrThrow("CURRENTPRICE")));
                entitiy.setClose(c.getString(c.getColumnIndexOrThrow("BEFORECLOSEPRICE")));
                entitiy.setTodayOpenPrice(c.getString(c.getColumnIndexOrThrow("TODAYOPENPRICE")));
                entitiy.setTime(c.getString(c.getColumnIndexOrThrow("TIME")));
                entitiy.setDate(c.getString(c.getColumnIndexOrThrow("DATE")));
                entitiy.setHigh(c.getString(c.getColumnIndexOrThrow("HIGH")));
                entitiy.setLow(c.getString(c.getColumnIndexOrThrow("LOW")));
                entitiy.setPlate(c.getString(c.getColumnIndexOrThrow("PLATE")));
                entitiy.setPresentAmount(c.getString(c.getColumnIndexOrThrow("PRESENTAMOUNT")));
                entitiy.setTotalVolume(c.getString(c.getColumnIndexOrThrow("TOTALVOLUME")));
                entitiy.setTotalPrice(c.getString(c.getColumnIndexOrThrow("TOTALPRICE")));
                entitiy.setInflowMoney(c.getString(c.getColumnIndexOrThrow("INFLOWMONEY")));
                entitiy.setOutflowMoney(c.getString(c.getColumnIndexOrThrow("OUTFLOWMONEY")));
                entitiy.setBuyOrder(c.getString(c.getColumnIndexOrThrow("BUYORDER")));
                entitiy.setSellOrder(c.getString(c.getColumnIndexOrThrow("SELLORDER")));
                entitiy.setTurnover(c.getString(c.getColumnIndexOrThrow("TURNOVER")));
                entitiy.setTrade(c.getString(c.getColumnIndexOrThrow("TRADE")));
                entitiy.setIndustryName(c.getString(c.getColumnIndexOrThrow("TYPE")));
                entitiy.setIndustryNumber(c.getString(c.getColumnIndexOrThrow("TRADECODE")));
                entitiy.setPeg(c.getString(c.getColumnIndexOrThrow("PEG")));
                entitiy.setFlowEquity(c.getString(c.getColumnIndexOrThrow("FLOWEQUITY")));
                entitiy.setTotalEquity(c.getString(c.getColumnIndexOrThrow("TOTALEQUITY")));
                entitiy.setPbr(c.getString(c.getColumnIndexOrThrow("PBR")));
                entitiy.setIndustryUpAndDown(c.getString(c.getColumnIndexOrThrow("INDYSTRYUPANDDOWN")));
                entitiy.setEquivalentRatio(c.getString(c.getColumnIndexOrThrow("EQUIVALENTRATIO")));
                entitiy.setBuy1(c.getString(c.getColumnIndexOrThrow("BUY1")));
                entitiy.setBuy2(c.getString(c.getColumnIndexOrThrow("BUY2")));
                entitiy.setBuy3(c.getString(c.getColumnIndexOrThrow("BUY3")));
                entitiy.setBuy4(c.getString(c.getColumnIndexOrThrow("BUY4")));
                entitiy.setBuy5(c.getString(c.getColumnIndexOrThrow("BUY5")));
                entitiy.setBuy1Amount(c.getString(c.getColumnIndexOrThrow("BUY1AMOUNT")));
                entitiy.setBuy2Amount(c.getString(c.getColumnIndexOrThrow("BUY2AMOUNT")));
                entitiy.setBuy3Amount(c.getString(c.getColumnIndexOrThrow("BUY3AMOUNT")));
                entitiy.setBuy4Amount(c.getString(c.getColumnIndexOrThrow("BUY4AMOUNT")));
                entitiy.setBuy5Amount(c.getString(c.getColumnIndexOrThrow("BUY5AMOUNT")));
                entitiy.setSell1(c.getString(c.getColumnIndexOrThrow("SELL1")));
                entitiy.setSell2(c.getString(c.getColumnIndexOrThrow("SELL2")));
                entitiy.setSell3(c.getString(c.getColumnIndexOrThrow("SELL3")));
                entitiy.setSell4(c.getString(c.getColumnIndexOrThrow("SELL4")));
                entitiy.setSell5(c.getString(c.getColumnIndexOrThrow("SELL5")));
                entitiy.setSell1Amount(c.getString(c.getColumnIndexOrThrow("SELL1AMOUNT")));
                entitiy.setSell2Amount(c.getString(c.getColumnIndexOrThrow("SELL2AMOUNT")));
                entitiy.setSell3Amount(c.getString(c.getColumnIndexOrThrow("SELL3AMOUNT")));
                entitiy.setSell4Amount(c.getString(c.getColumnIndexOrThrow("SELL4AMOUNT")));
                entitiy.setSell5Amount(c.getString(c.getColumnIndexOrThrow("SELL5AMOUNT")));
                entitiy.setIsHoldStock(c.getString(c.getColumnIndexOrThrow("isHoldStock")));
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("apperHoldStock")));

                entitiy.setMKT_VAL(c.getString(c.getColumnIndexOrThrow("MKT_VAL")));
                entitiy.setSECU_ACC(c.getString(c.getColumnIndexOrThrow("SECU_ACC")));
                entitiy.setSHARE_BLN(c.getString(c.getColumnIndexOrThrow("SHARE_BLN")));
                entitiy.setSHARE_AVL(c.getString(c.getColumnIndexOrThrow("SHARE_AVL")));
                entitiy.setSHARE_QTY(c.getString(c.getColumnIndexOrThrow("SHARE_QTY")));
                entitiy.setPROFIT_RATIO(c.getString(c.getColumnIndexOrThrow("PROFIT_RATIO")));
                entitiy.setMARKET(c.getString(c.getColumnIndexOrThrow("MARKET")));
                entitiy.setBRANCH(c.getString(c.getColumnIndexOrThrow("BRANCH")));
                entitiy.setMKT_PRICE(c.getString(c.getColumnIndexOrThrow("MKT_PRICE")));
                entitiy.setSECU_CODE(c.getString(c.getColumnIndexOrThrow("SECU_CODE")));
                entitiy.setINCOME_AMT(c.getString(c.getColumnIndexOrThrow("INCOME_AMT")));
                entitiy.setSECU_NAME(c.getString(c.getColumnIndexOrThrow("SECU_NAME")));
                entitiy.setCURRENT_COST(c.getString(c.getColumnIndexOrThrow("CURRENT_COST")));
                entitiy.setStockholdon(c.getString(c.getColumnIndexOrThrow("STOCKHOLDON")));

                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return entitiys;
    }

    /**
     * 按现价倒序排列查询所有股票数据
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatasByPriceDesc(){
        String sql = "SELECT * from PUB_STOCKLIST LEFT JOIN HOLD_SEQ ON PUB_STOCKLIST.STOCKCODE = HOLD_SEQ.CODE ORDER BY CURRENTPRICE DESC";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("ISHOLD")));
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
                entitiy.setUpAndDownValue(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGEVALUE"))));
                entitiy.setPriceChangeRatio(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGERATIO"))));
                entitiy.setNewPrice(c.getString(c.getColumnIndexOrThrow("CURRENTPRICE")));
                entitiy.setClose(c.getString(c.getColumnIndexOrThrow("BEFORECLOSEPRICE")));
                entitiy.setTodayOpenPrice(c.getString(c.getColumnIndexOrThrow("TODAYOPENPRICE")));
                entitiy.setTime(c.getString(c.getColumnIndexOrThrow("TIME")));
                entitiy.setDate(c.getString(c.getColumnIndexOrThrow("DATE")));
                entitiy.setHigh(c.getString(c.getColumnIndexOrThrow("HIGH")));
                entitiy.setLow(c.getString(c.getColumnIndexOrThrow("LOW")));
                entitiy.setPlate(c.getString(c.getColumnIndexOrThrow("PLATE")));
                entitiy.setPresentAmount(c.getString(c.getColumnIndexOrThrow("PRESENTAMOUNT")));
                entitiy.setTotalVolume(c.getString(c.getColumnIndexOrThrow("TOTALVOLUME")));
                entitiy.setTotalPrice(c.getString(c.getColumnIndexOrThrow("TOTALPRICE")));
                entitiy.setInflowMoney(c.getString(c.getColumnIndexOrThrow("INFLOWMONEY")));
                entitiy.setOutflowMoney(c.getString(c.getColumnIndexOrThrow("OUTFLOWMONEY")));
                entitiy.setBuyOrder(c.getString(c.getColumnIndexOrThrow("BUYORDER")));
                entitiy.setSellOrder(c.getString(c.getColumnIndexOrThrow("SELLORDER")));
                entitiy.setTurnover(c.getString(c.getColumnIndexOrThrow("TURNOVER")));
                entitiy.setTrade(c.getString(c.getColumnIndexOrThrow("TRADE")));
                entitiy.setIndustryName(c.getString(c.getColumnIndexOrThrow("TYPE")));
                entitiy.setIndustryNumber(c.getString(c.getColumnIndexOrThrow("TRADECODE")));
                entitiy.setPeg(c.getString(c.getColumnIndexOrThrow("PEG")));
                entitiy.setFlowEquity(c.getString(c.getColumnIndexOrThrow("FLOWEQUITY")));
                entitiy.setTotalEquity(c.getString(c.getColumnIndexOrThrow("TOTALEQUITY")));
                entitiy.setPbr(c.getString(c.getColumnIndexOrThrow("PBR")));
                entitiy.setIndustryUpAndDown(c.getString(c.getColumnIndexOrThrow("INDYSTRYUPANDDOWN")));
                entitiy.setEquivalentRatio(c.getString(c.getColumnIndexOrThrow("EQUIVALENTRATIO")));
                entitiy.setBuy1(c.getString(c.getColumnIndexOrThrow("BUY1")));
                entitiy.setBuy2(c.getString(c.getColumnIndexOrThrow("BUY2")));
                entitiy.setBuy3(c.getString(c.getColumnIndexOrThrow("BUY3")));
                entitiy.setBuy4(c.getString(c.getColumnIndexOrThrow("BUY4")));
                entitiy.setBuy5(c.getString(c.getColumnIndexOrThrow("BUY5")));
                entitiy.setBuy1Amount(c.getString(c.getColumnIndexOrThrow("BUY1AMOUNT")));
                entitiy.setBuy2Amount(c.getString(c.getColumnIndexOrThrow("BUY2AMOUNT")));
                entitiy.setBuy3Amount(c.getString(c.getColumnIndexOrThrow("BUY3AMOUNT")));
                entitiy.setBuy4Amount(c.getString(c.getColumnIndexOrThrow("BUY4AMOUNT")));
                entitiy.setBuy5Amount(c.getString(c.getColumnIndexOrThrow("BUY5AMOUNT")));
                entitiy.setSell1(c.getString(c.getColumnIndexOrThrow("SELL1")));
                entitiy.setSell2(c.getString(c.getColumnIndexOrThrow("SELL2")));
                entitiy.setSell3(c.getString(c.getColumnIndexOrThrow("SELL3")));
                entitiy.setSell4(c.getString(c.getColumnIndexOrThrow("SELL4")));
                entitiy.setSell5(c.getString(c.getColumnIndexOrThrow("SELL5")));
                entitiy.setSell1Amount(c.getString(c.getColumnIndexOrThrow("SELL1AMOUNT")));
                entitiy.setSell2Amount(c.getString(c.getColumnIndexOrThrow("SELL2AMOUNT")));
                entitiy.setSell3Amount(c.getString(c.getColumnIndexOrThrow("SELL3AMOUNT")));
                entitiy.setSell4Amount(c.getString(c.getColumnIndexOrThrow("SELL4AMOUNT")));
                entitiy.setSell5Amount(c.getString(c.getColumnIndexOrThrow("SELL5AMOUNT")));
                entitiy.setIsHoldStock(c.getString(c.getColumnIndexOrThrow("isHoldStock")));
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("apperHoldStock")));

                entitiy.setMKT_VAL(c.getString(c.getColumnIndexOrThrow("MKT_VAL")));
                entitiy.setSECU_ACC(c.getString(c.getColumnIndexOrThrow("SECU_ACC")));
                entitiy.setSHARE_BLN(c.getString(c.getColumnIndexOrThrow("SHARE_BLN")));
                entitiy.setSHARE_AVL(c.getString(c.getColumnIndexOrThrow("SHARE_AVL")));
                entitiy.setSHARE_QTY(c.getString(c.getColumnIndexOrThrow("SHARE_QTY")));
                entitiy.setPROFIT_RATIO(c.getString(c.getColumnIndexOrThrow("PROFIT_RATIO")));
                entitiy.setMARKET(c.getString(c.getColumnIndexOrThrow("MARKET")));
                entitiy.setBRANCH(c.getString(c.getColumnIndexOrThrow("BRANCH")));
                entitiy.setMKT_PRICE(c.getString(c.getColumnIndexOrThrow("MKT_PRICE")));
                entitiy.setSECU_CODE(c.getString(c.getColumnIndexOrThrow("SECU_CODE")));
                entitiy.setINCOME_AMT(c.getString(c.getColumnIndexOrThrow("INCOME_AMT")));
                entitiy.setSECU_NAME(c.getString(c.getColumnIndexOrThrow("SECU_NAME")));
                entitiy.setCURRENT_COST(c.getString(c.getColumnIndexOrThrow("CURRENT_COST")));
                entitiy.setStockholdon(c.getString(c.getColumnIndexOrThrow("STOCKHOLDON")));

                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return entitiys;
    }


    /**
     * 按涨跌幅正序排列查询所有股票数据
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatasByUpAndDownRatioAsc(){
        String sql = "SELECT * from PUB_STOCKLIST LEFT JOIN HOLD_SEQ ON PUB_STOCKLIST.STOCKCODE = HOLD_SEQ.CODE ORDER BY PRICECHANGERATIO ASC";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("ISHOLD")));
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
                entitiy.setUpAndDownValue(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGEVALUE"))));
                entitiy.setPriceChangeRatio(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGERATIO"))));
                entitiy.setNewPrice(c.getString(c.getColumnIndexOrThrow("CURRENTPRICE")));
                entitiy.setClose(c.getString(c.getColumnIndexOrThrow("BEFORECLOSEPRICE")));
                entitiy.setTodayOpenPrice(c.getString(c.getColumnIndexOrThrow("TODAYOPENPRICE")));
                entitiy.setTime(c.getString(c.getColumnIndexOrThrow("TIME")));
                entitiy.setDate(c.getString(c.getColumnIndexOrThrow("DATE")));
                entitiy.setHigh(c.getString(c.getColumnIndexOrThrow("HIGH")));
                entitiy.setLow(c.getString(c.getColumnIndexOrThrow("LOW")));
                entitiy.setPlate(c.getString(c.getColumnIndexOrThrow("PLATE")));
                entitiy.setPresentAmount(c.getString(c.getColumnIndexOrThrow("PRESENTAMOUNT")));
                entitiy.setTotalVolume(c.getString(c.getColumnIndexOrThrow("TOTALVOLUME")));
                entitiy.setTotalPrice(c.getString(c.getColumnIndexOrThrow("TOTALPRICE")));
                entitiy.setInflowMoney(c.getString(c.getColumnIndexOrThrow("INFLOWMONEY")));
                entitiy.setOutflowMoney(c.getString(c.getColumnIndexOrThrow("OUTFLOWMONEY")));
                entitiy.setBuyOrder(c.getString(c.getColumnIndexOrThrow("BUYORDER")));
                entitiy.setSellOrder(c.getString(c.getColumnIndexOrThrow("SELLORDER")));
                entitiy.setTurnover(c.getString(c.getColumnIndexOrThrow("TURNOVER")));
                entitiy.setTrade(c.getString(c.getColumnIndexOrThrow("TRADE")));
                entitiy.setIndustryName(c.getString(c.getColumnIndexOrThrow("TYPE")));
                entitiy.setIndustryNumber(c.getString(c.getColumnIndexOrThrow("TRADECODE")));
                entitiy.setPeg(c.getString(c.getColumnIndexOrThrow("PEG")));
                entitiy.setFlowEquity(c.getString(c.getColumnIndexOrThrow("FLOWEQUITY")));
                entitiy.setTotalEquity(c.getString(c.getColumnIndexOrThrow("TOTALEQUITY")));
                entitiy.setPbr(c.getString(c.getColumnIndexOrThrow("PBR")));
                entitiy.setIndustryUpAndDown(c.getString(c.getColumnIndexOrThrow("INDYSTRYUPANDDOWN")));
                entitiy.setEquivalentRatio(c.getString(c.getColumnIndexOrThrow("EQUIVALENTRATIO")));
                entitiy.setBuy1(c.getString(c.getColumnIndexOrThrow("BUY1")));
                entitiy.setBuy2(c.getString(c.getColumnIndexOrThrow("BUY2")));
                entitiy.setBuy3(c.getString(c.getColumnIndexOrThrow("BUY3")));
                entitiy.setBuy4(c.getString(c.getColumnIndexOrThrow("BUY4")));
                entitiy.setBuy5(c.getString(c.getColumnIndexOrThrow("BUY5")));
                entitiy.setBuy1Amount(c.getString(c.getColumnIndexOrThrow("BUY1AMOUNT")));
                entitiy.setBuy2Amount(c.getString(c.getColumnIndexOrThrow("BUY2AMOUNT")));
                entitiy.setBuy3Amount(c.getString(c.getColumnIndexOrThrow("BUY3AMOUNT")));
                entitiy.setBuy4Amount(c.getString(c.getColumnIndexOrThrow("BUY4AMOUNT")));
                entitiy.setBuy5Amount(c.getString(c.getColumnIndexOrThrow("BUY5AMOUNT")));
                entitiy.setSell1(c.getString(c.getColumnIndexOrThrow("SELL1")));
                entitiy.setSell2(c.getString(c.getColumnIndexOrThrow("SELL2")));
                entitiy.setSell3(c.getString(c.getColumnIndexOrThrow("SELL3")));
                entitiy.setSell4(c.getString(c.getColumnIndexOrThrow("SELL4")));
                entitiy.setSell5(c.getString(c.getColumnIndexOrThrow("SELL5")));
                entitiy.setSell1Amount(c.getString(c.getColumnIndexOrThrow("SELL1AMOUNT")));
                entitiy.setSell2Amount(c.getString(c.getColumnIndexOrThrow("SELL2AMOUNT")));
                entitiy.setSell3Amount(c.getString(c.getColumnIndexOrThrow("SELL3AMOUNT")));
                entitiy.setSell4Amount(c.getString(c.getColumnIndexOrThrow("SELL4AMOUNT")));
                entitiy.setSell5Amount(c.getString(c.getColumnIndexOrThrow("SELL5AMOUNT")));
                entitiy.setIsHoldStock(c.getString(c.getColumnIndexOrThrow("isHoldStock")));
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("apperHoldStock")));

                entitiy.setMKT_VAL(c.getString(c.getColumnIndexOrThrow("MKT_VAL")));
                entitiy.setSECU_ACC(c.getString(c.getColumnIndexOrThrow("SECU_ACC")));
                entitiy.setSHARE_BLN(c.getString(c.getColumnIndexOrThrow("SHARE_BLN")));
                entitiy.setSHARE_AVL(c.getString(c.getColumnIndexOrThrow("SHARE_AVL")));
                entitiy.setSHARE_QTY(c.getString(c.getColumnIndexOrThrow("SHARE_QTY")));
                entitiy.setPROFIT_RATIO(c.getString(c.getColumnIndexOrThrow("PROFIT_RATIO")));
                entitiy.setMARKET(c.getString(c.getColumnIndexOrThrow("MARKET")));
                entitiy.setBRANCH(c.getString(c.getColumnIndexOrThrow("BRANCH")));
                entitiy.setMKT_PRICE(c.getString(c.getColumnIndexOrThrow("MKT_PRICE")));
                entitiy.setSECU_CODE(c.getString(c.getColumnIndexOrThrow("SECU_CODE")));
                entitiy.setINCOME_AMT(c.getString(c.getColumnIndexOrThrow("INCOME_AMT")));
                entitiy.setSECU_NAME(c.getString(c.getColumnIndexOrThrow("SECU_NAME")));
                entitiy.setCURRENT_COST(c.getString(c.getColumnIndexOrThrow("CURRENT_COST")));
                entitiy.setStockholdon(c.getString(c.getColumnIndexOrThrow("STOCKHOLDON")));

                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return entitiys;
    }

    /**
     * 按涨跌幅倒序排列查询所有股票数据
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatasByUpAndDownRatioDesc(){
        String sql = "SELECT * from PUB_STOCKLIST LEFT JOIN HOLD_SEQ ON PUB_STOCKLIST.STOCKCODE = HOLD_SEQ.CODE ORDER BY PRICECHANGERATIO DESC";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("ISHOLD")));
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
                entitiy.setUpAndDownValue(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGEVALUE"))));
                entitiy.setPriceChangeRatio(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGERATIO"))));
                entitiy.setNewPrice(c.getString(c.getColumnIndexOrThrow("CURRENTPRICE")));
                entitiy.setClose(c.getString(c.getColumnIndexOrThrow("BEFORECLOSEPRICE")));
                entitiy.setTodayOpenPrice(c.getString(c.getColumnIndexOrThrow("TODAYOPENPRICE")));
                entitiy.setTime(c.getString(c.getColumnIndexOrThrow("TIME")));
                entitiy.setDate(c.getString(c.getColumnIndexOrThrow("DATE")));
                entitiy.setHigh(c.getString(c.getColumnIndexOrThrow("HIGH")));
                entitiy.setLow(c.getString(c.getColumnIndexOrThrow("LOW")));
                entitiy.setPlate(c.getString(c.getColumnIndexOrThrow("PLATE")));
                entitiy.setPresentAmount(c.getString(c.getColumnIndexOrThrow("PRESENTAMOUNT")));
                entitiy.setTotalVolume(c.getString(c.getColumnIndexOrThrow("TOTALVOLUME")));
                entitiy.setTotalPrice(c.getString(c.getColumnIndexOrThrow("TOTALPRICE")));
                entitiy.setInflowMoney(c.getString(c.getColumnIndexOrThrow("INFLOWMONEY")));
                entitiy.setOutflowMoney(c.getString(c.getColumnIndexOrThrow("OUTFLOWMONEY")));
                entitiy.setBuyOrder(c.getString(c.getColumnIndexOrThrow("BUYORDER")));
                entitiy.setSellOrder(c.getString(c.getColumnIndexOrThrow("SELLORDER")));
                entitiy.setTurnover(c.getString(c.getColumnIndexOrThrow("TURNOVER")));
                entitiy.setTrade(c.getString(c.getColumnIndexOrThrow("TRADE")));
                entitiy.setIndustryName(c.getString(c.getColumnIndexOrThrow("TYPE")));
                entitiy.setIndustryNumber(c.getString(c.getColumnIndexOrThrow("TRADECODE")));
                entitiy.setPeg(c.getString(c.getColumnIndexOrThrow("PEG")));
                entitiy.setFlowEquity(c.getString(c.getColumnIndexOrThrow("FLOWEQUITY")));
                entitiy.setTotalEquity(c.getString(c.getColumnIndexOrThrow("TOTALEQUITY")));
                entitiy.setPbr(c.getString(c.getColumnIndexOrThrow("PBR")));
                entitiy.setIndustryUpAndDown(c.getString(c.getColumnIndexOrThrow("INDYSTRYUPANDDOWN")));
                entitiy.setEquivalentRatio(c.getString(c.getColumnIndexOrThrow("EQUIVALENTRATIO")));
                entitiy.setBuy1(c.getString(c.getColumnIndexOrThrow("BUY1")));
                entitiy.setBuy2(c.getString(c.getColumnIndexOrThrow("BUY2")));
                entitiy.setBuy3(c.getString(c.getColumnIndexOrThrow("BUY3")));
                entitiy.setBuy4(c.getString(c.getColumnIndexOrThrow("BUY4")));
                entitiy.setBuy5(c.getString(c.getColumnIndexOrThrow("BUY5")));
                entitiy.setBuy1Amount(c.getString(c.getColumnIndexOrThrow("BUY1AMOUNT")));
                entitiy.setBuy2Amount(c.getString(c.getColumnIndexOrThrow("BUY2AMOUNT")));
                entitiy.setBuy3Amount(c.getString(c.getColumnIndexOrThrow("BUY3AMOUNT")));
                entitiy.setBuy4Amount(c.getString(c.getColumnIndexOrThrow("BUY4AMOUNT")));
                entitiy.setBuy5Amount(c.getString(c.getColumnIndexOrThrow("BUY5AMOUNT")));
                entitiy.setSell1(c.getString(c.getColumnIndexOrThrow("SELL1")));
                entitiy.setSell2(c.getString(c.getColumnIndexOrThrow("SELL2")));
                entitiy.setSell3(c.getString(c.getColumnIndexOrThrow("SELL3")));
                entitiy.setSell4(c.getString(c.getColumnIndexOrThrow("SELL4")));
                entitiy.setSell5(c.getString(c.getColumnIndexOrThrow("SELL5")));
                entitiy.setSell1Amount(c.getString(c.getColumnIndexOrThrow("SELL1AMOUNT")));
                entitiy.setSell2Amount(c.getString(c.getColumnIndexOrThrow("SELL2AMOUNT")));
                entitiy.setSell3Amount(c.getString(c.getColumnIndexOrThrow("SELL3AMOUNT")));
                entitiy.setSell4Amount(c.getString(c.getColumnIndexOrThrow("SELL4AMOUNT")));
                entitiy.setSell5Amount(c.getString(c.getColumnIndexOrThrow("SELL5AMOUNT")));
                entitiy.setIsHoldStock(c.getString(c.getColumnIndexOrThrow("isHoldStock")));
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("apperHoldStock")));

                entitiy.setMKT_VAL(c.getString(c.getColumnIndexOrThrow("MKT_VAL")));
                entitiy.setSECU_ACC(c.getString(c.getColumnIndexOrThrow("SECU_ACC")));
                entitiy.setSHARE_BLN(c.getString(c.getColumnIndexOrThrow("SHARE_BLN")));
                entitiy.setSHARE_AVL(c.getString(c.getColumnIndexOrThrow("SHARE_AVL")));
                entitiy.setSHARE_QTY(c.getString(c.getColumnIndexOrThrow("SHARE_QTY")));
                entitiy.setPROFIT_RATIO(c.getString(c.getColumnIndexOrThrow("PROFIT_RATIO")));
                entitiy.setMARKET(c.getString(c.getColumnIndexOrThrow("MARKET")));
                entitiy.setBRANCH(c.getString(c.getColumnIndexOrThrow("BRANCH")));
                entitiy.setMKT_PRICE(c.getString(c.getColumnIndexOrThrow("MKT_PRICE")));
                entitiy.setSECU_CODE(c.getString(c.getColumnIndexOrThrow("SECU_CODE")));
                entitiy.setINCOME_AMT(c.getString(c.getColumnIndexOrThrow("INCOME_AMT")));
                entitiy.setSECU_NAME(c.getString(c.getColumnIndexOrThrow("SECU_NAME")));
                entitiy.setCURRENT_COST(c.getString(c.getColumnIndexOrThrow("CURRENT_COST")));
                entitiy.setStockholdon(c.getString(c.getColumnIndexOrThrow("STOCKHOLDON")));
                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return entitiys;
    }

    /**
     * 按涨跌值正序排列查询所有股票数据
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatasByUpAndDownValueAsc(){
        String sql = "SELECT * from PUB_STOCKLIST LEFT JOIN HOLD_SEQ ON PUB_STOCKLIST.STOCKCODE = HOLD_SEQ.CODE  ORDER BY PRICECHANGEVALUE ASC";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("ISHOLD")));
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
                entitiy.setUpAndDownValue(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGEVALUE"))));
                entitiy.setPriceChangeRatio(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGERATIO"))));
                entitiy.setNewPrice(c.getString(c.getColumnIndexOrThrow("CURRENTPRICE")));
                entitiy.setClose(c.getString(c.getColumnIndexOrThrow("BEFORECLOSEPRICE")));
                entitiy.setTodayOpenPrice(c.getString(c.getColumnIndexOrThrow("TODAYOPENPRICE")));
                entitiy.setTime(c.getString(c.getColumnIndexOrThrow("TIME")));
                entitiy.setDate(c.getString(c.getColumnIndexOrThrow("DATE")));
                entitiy.setHigh(c.getString(c.getColumnIndexOrThrow("HIGH")));
                entitiy.setLow(c.getString(c.getColumnIndexOrThrow("LOW")));
                entitiy.setPlate(c.getString(c.getColumnIndexOrThrow("PLATE")));
                entitiy.setPresentAmount(c.getString(c.getColumnIndexOrThrow("PRESENTAMOUNT")));
                entitiy.setTotalVolume(c.getString(c.getColumnIndexOrThrow("TOTALVOLUME")));
                entitiy.setTotalPrice(c.getString(c.getColumnIndexOrThrow("TOTALPRICE")));
                entitiy.setInflowMoney(c.getString(c.getColumnIndexOrThrow("INFLOWMONEY")));
                entitiy.setOutflowMoney(c.getString(c.getColumnIndexOrThrow("OUTFLOWMONEY")));
                entitiy.setBuyOrder(c.getString(c.getColumnIndexOrThrow("BUYORDER")));
                entitiy.setSellOrder(c.getString(c.getColumnIndexOrThrow("SELLORDER")));
                entitiy.setTurnover(c.getString(c.getColumnIndexOrThrow("TURNOVER")));
                entitiy.setTrade(c.getString(c.getColumnIndexOrThrow("TRADE")));
                entitiy.setIndustryName(c.getString(c.getColumnIndexOrThrow("TYPE")));
                entitiy.setIndustryNumber(c.getString(c.getColumnIndexOrThrow("TRADECODE")));
                entitiy.setPeg(c.getString(c.getColumnIndexOrThrow("PEG")));
                entitiy.setFlowEquity(c.getString(c.getColumnIndexOrThrow("FLOWEQUITY")));
                entitiy.setTotalEquity(c.getString(c.getColumnIndexOrThrow("TOTALEQUITY")));
                entitiy.setPbr(c.getString(c.getColumnIndexOrThrow("PBR")));
                entitiy.setIndustryUpAndDown(c.getString(c.getColumnIndexOrThrow("INDYSTRYUPANDDOWN")));
                entitiy.setEquivalentRatio(c.getString(c.getColumnIndexOrThrow("EQUIVALENTRATIO")));
                entitiy.setBuy1(c.getString(c.getColumnIndexOrThrow("BUY1")));
                entitiy.setBuy2(c.getString(c.getColumnIndexOrThrow("BUY2")));
                entitiy.setBuy3(c.getString(c.getColumnIndexOrThrow("BUY3")));
                entitiy.setBuy4(c.getString(c.getColumnIndexOrThrow("BUY4")));
                entitiy.setBuy5(c.getString(c.getColumnIndexOrThrow("BUY5")));
                entitiy.setBuy1Amount(c.getString(c.getColumnIndexOrThrow("BUY1AMOUNT")));
                entitiy.setBuy2Amount(c.getString(c.getColumnIndexOrThrow("BUY2AMOUNT")));
                entitiy.setBuy3Amount(c.getString(c.getColumnIndexOrThrow("BUY3AMOUNT")));
                entitiy.setBuy4Amount(c.getString(c.getColumnIndexOrThrow("BUY4AMOUNT")));
                entitiy.setBuy5Amount(c.getString(c.getColumnIndexOrThrow("BUY5AMOUNT")));
                entitiy.setSell1(c.getString(c.getColumnIndexOrThrow("SELL1")));
                entitiy.setSell2(c.getString(c.getColumnIndexOrThrow("SELL2")));
                entitiy.setSell3(c.getString(c.getColumnIndexOrThrow("SELL3")));
                entitiy.setSell4(c.getString(c.getColumnIndexOrThrow("SELL4")));
                entitiy.setSell5(c.getString(c.getColumnIndexOrThrow("SELL5")));
                entitiy.setSell1Amount(c.getString(c.getColumnIndexOrThrow("SELL1AMOUNT")));
                entitiy.setSell2Amount(c.getString(c.getColumnIndexOrThrow("SELL2AMOUNT")));
                entitiy.setSell3Amount(c.getString(c.getColumnIndexOrThrow("SELL3AMOUNT")));
                entitiy.setSell4Amount(c.getString(c.getColumnIndexOrThrow("SELL4AMOUNT")));
                entitiy.setSell5Amount(c.getString(c.getColumnIndexOrThrow("SELL5AMOUNT")));
                entitiy.setIsHoldStock(c.getString(c.getColumnIndexOrThrow("isHoldStock")));
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("apperHoldStock")));

                entitiy.setMKT_VAL(c.getString(c.getColumnIndexOrThrow("MKT_VAL")));
                entitiy.setSECU_ACC(c.getString(c.getColumnIndexOrThrow("SECU_ACC")));
                entitiy.setSHARE_BLN(c.getString(c.getColumnIndexOrThrow("SHARE_BLN")));
                entitiy.setSHARE_AVL(c.getString(c.getColumnIndexOrThrow("SHARE_AVL")));
                entitiy.setSHARE_QTY(c.getString(c.getColumnIndexOrThrow("SHARE_QTY")));
                entitiy.setPROFIT_RATIO(c.getString(c.getColumnIndexOrThrow("PROFIT_RATIO")));
                entitiy.setMARKET(c.getString(c.getColumnIndexOrThrow("MARKET")));
                entitiy.setBRANCH(c.getString(c.getColumnIndexOrThrow("BRANCH")));
                entitiy.setMKT_PRICE(c.getString(c.getColumnIndexOrThrow("MKT_PRICE")));
                entitiy.setSECU_CODE(c.getString(c.getColumnIndexOrThrow("SECU_CODE")));
                entitiy.setINCOME_AMT(c.getString(c.getColumnIndexOrThrow("INCOME_AMT")));
                entitiy.setSECU_NAME(c.getString(c.getColumnIndexOrThrow("SECU_NAME")));
                entitiy.setCURRENT_COST(c.getString(c.getColumnIndexOrThrow("CURRENT_COST")));
                entitiy.setStockholdon(c.getString(c.getColumnIndexOrThrow("STOCKHOLDON")));
                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return entitiys;
    }

    /**
     * 按涨跌值倒序排列查询所有股票数据
     * @return
     */
    public static ArrayList<StockInfoEntity> queryAllDatasByUpAndDownValueDesc(){
        String sql = "SELECT * from PUB_STOCKLIST LEFT JOIN HOLD_SEQ ON PUB_STOCKLIST.STOCKCODE = HOLD_SEQ.CODE ORDER BY PRICECHANGEVALUE DESC";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("ISHOLD")));
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
                entitiy.setUpAndDownValue(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGEVALUE"))));
                entitiy.setPriceChangeRatio(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGERATIO"))));
                entitiy.setNewPrice(c.getString(c.getColumnIndexOrThrow("CURRENTPRICE")));
                entitiy.setClose(c.getString(c.getColumnIndexOrThrow("BEFORECLOSEPRICE")));
                entitiy.setTodayOpenPrice(c.getString(c.getColumnIndexOrThrow("TODAYOPENPRICE")));
                entitiy.setTime(c.getString(c.getColumnIndexOrThrow("TIME")));
                entitiy.setDate(c.getString(c.getColumnIndexOrThrow("DATE")));
                entitiy.setHigh(c.getString(c.getColumnIndexOrThrow("HIGH")));
                entitiy.setLow(c.getString(c.getColumnIndexOrThrow("LOW")));
                entitiy.setPlate(c.getString(c.getColumnIndexOrThrow("PLATE")));
                entitiy.setPresentAmount(c.getString(c.getColumnIndexOrThrow("PRESENTAMOUNT")));
                entitiy.setTotalVolume(c.getString(c.getColumnIndexOrThrow("TOTALVOLUME")));
                entitiy.setTotalPrice(c.getString(c.getColumnIndexOrThrow("TOTALPRICE")));
                entitiy.setInflowMoney(c.getString(c.getColumnIndexOrThrow("INFLOWMONEY")));
                entitiy.setOutflowMoney(c.getString(c.getColumnIndexOrThrow("OUTFLOWMONEY")));
                entitiy.setBuyOrder(c.getString(c.getColumnIndexOrThrow("BUYORDER")));
                entitiy.setSellOrder(c.getString(c.getColumnIndexOrThrow("SELLORDER")));
                entitiy.setTurnover(c.getString(c.getColumnIndexOrThrow("TURNOVER")));
                entitiy.setTrade(c.getString(c.getColumnIndexOrThrow("TRADE")));
                entitiy.setIndustryName(c.getString(c.getColumnIndexOrThrow("TYPE")));
                entitiy.setIndustryNumber(c.getString(c.getColumnIndexOrThrow("TRADECODE")));
                entitiy.setPeg(c.getString(c.getColumnIndexOrThrow("PEG")));
                entitiy.setFlowEquity(c.getString(c.getColumnIndexOrThrow("FLOWEQUITY")));
                entitiy.setTotalEquity(c.getString(c.getColumnIndexOrThrow("TOTALEQUITY")));
                entitiy.setPbr(c.getString(c.getColumnIndexOrThrow("PBR")));
                entitiy.setIndustryUpAndDown(c.getString(c.getColumnIndexOrThrow("INDYSTRYUPANDDOWN")));
                entitiy.setEquivalentRatio(c.getString(c.getColumnIndexOrThrow("EQUIVALENTRATIO")));
                entitiy.setBuy1(c.getString(c.getColumnIndexOrThrow("BUY1")));
                entitiy.setBuy2(c.getString(c.getColumnIndexOrThrow("BUY2")));
                entitiy.setBuy3(c.getString(c.getColumnIndexOrThrow("BUY3")));
                entitiy.setBuy4(c.getString(c.getColumnIndexOrThrow("BUY4")));
                entitiy.setBuy5(c.getString(c.getColumnIndexOrThrow("BUY5")));
                entitiy.setBuy1Amount(c.getString(c.getColumnIndexOrThrow("BUY1AMOUNT")));
                entitiy.setBuy2Amount(c.getString(c.getColumnIndexOrThrow("BUY2AMOUNT")));
                entitiy.setBuy3Amount(c.getString(c.getColumnIndexOrThrow("BUY3AMOUNT")));
                entitiy.setBuy4Amount(c.getString(c.getColumnIndexOrThrow("BUY4AMOUNT")));
                entitiy.setBuy5Amount(c.getString(c.getColumnIndexOrThrow("BUY5AMOUNT")));
                entitiy.setSell1(c.getString(c.getColumnIndexOrThrow("SELL1")));
                entitiy.setSell2(c.getString(c.getColumnIndexOrThrow("SELL2")));
                entitiy.setSell3(c.getString(c.getColumnIndexOrThrow("SELL3")));
                entitiy.setSell4(c.getString(c.getColumnIndexOrThrow("SELL4")));
                entitiy.setSell5(c.getString(c.getColumnIndexOrThrow("SELL5")));
                entitiy.setSell1Amount(c.getString(c.getColumnIndexOrThrow("SELL1AMOUNT")));
                entitiy.setSell2Amount(c.getString(c.getColumnIndexOrThrow("SELL2AMOUNT")));
                entitiy.setSell3Amount(c.getString(c.getColumnIndexOrThrow("SELL3AMOUNT")));
                entitiy.setSell4Amount(c.getString(c.getColumnIndexOrThrow("SELL4AMOUNT")));
                entitiy.setSell5Amount(c.getString(c.getColumnIndexOrThrow("SELL5AMOUNT")));
                entitiy.setIsHoldStock(c.getString(c.getColumnIndexOrThrow("isHoldStock")));
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("apperHoldStock")));

                entitiy.setMKT_VAL(c.getString(c.getColumnIndexOrThrow("MKT_VAL")));
                entitiy.setSECU_ACC(c.getString(c.getColumnIndexOrThrow("SECU_ACC")));
                entitiy.setSHARE_BLN(c.getString(c.getColumnIndexOrThrow("SHARE_BLN")));
                entitiy.setSHARE_AVL(c.getString(c.getColumnIndexOrThrow("SHARE_AVL")));
                entitiy.setSHARE_QTY(c.getString(c.getColumnIndexOrThrow("SHARE_QTY")));
                entitiy.setPROFIT_RATIO(c.getString(c.getColumnIndexOrThrow("PROFIT_RATIO")));
                entitiy.setMARKET(c.getString(c.getColumnIndexOrThrow("MARKET")));
                entitiy.setBRANCH(c.getString(c.getColumnIndexOrThrow("BRANCH")));
                entitiy.setMKT_PRICE(c.getString(c.getColumnIndexOrThrow("MKT_PRICE")));
                entitiy.setSECU_CODE(c.getString(c.getColumnIndexOrThrow("SECU_CODE")));
                entitiy.setINCOME_AMT(c.getString(c.getColumnIndexOrThrow("INCOME_AMT")));
                entitiy.setSECU_NAME(c.getString(c.getColumnIndexOrThrow("SECU_NAME")));
                entitiy.setCURRENT_COST(c.getString(c.getColumnIndexOrThrow("CURRENT_COST")));
                entitiy.setStockholdon(c.getString(c.getColumnIndexOrThrow("STOCKHOLDON")));
                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }

            if (db != null) {
                db.close();
            }
        }
        return entitiys;
    }

    /**
     * 倒序搜索全部
     * @return
     */
    public static ArrayList<StockInfoEntity> queryStockListDatasByDesc() {
        String sql = "SELECT * from PUB_STOCKLIST ORDER BY ID DESC";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("ISHOLD")));
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
                entitiy.setUpAndDownValue(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGEVALUE"))));
                entitiy.setPriceChangeRatio(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGERATIO"))));
                entitiy.setNewPrice(c.getString(c.getColumnIndexOrThrow("CURRENTPRICE")));
                entitiy.setClose(c.getString(c.getColumnIndexOrThrow("BEFORECLOSEPRICE")));
                entitiy.setTodayOpenPrice(c.getString(c.getColumnIndexOrThrow("TODAYOPENPRICE")));
                entitiy.setTime(c.getString(c.getColumnIndexOrThrow("TIME")));
                entitiy.setDate(c.getString(c.getColumnIndexOrThrow("DATE")));
                entitiy.setHigh(c.getString(c.getColumnIndexOrThrow("HIGH")));
                entitiy.setLow(c.getString(c.getColumnIndexOrThrow("LOW")));
                entitiy.setPlate(c.getString(c.getColumnIndexOrThrow("PLATE")));
                entitiy.setPresentAmount(c.getString(c.getColumnIndexOrThrow("PRESENTAMOUNT")));
                entitiy.setTotalVolume(c.getString(c.getColumnIndexOrThrow("TOTALVOLUME")));
                entitiy.setTotalPrice(c.getString(c.getColumnIndexOrThrow("TOTALPRICE")));
                entitiy.setInflowMoney(c.getString(c.getColumnIndexOrThrow("INFLOWMONEY")));
                entitiy.setOutflowMoney(c.getString(c.getColumnIndexOrThrow("OUTFLOWMONEY")));
                entitiy.setBuyOrder(c.getString(c.getColumnIndexOrThrow("BUYORDER")));
                entitiy.setSellOrder(c.getString(c.getColumnIndexOrThrow("SELLORDER")));
                entitiy.setTurnover(c.getString(c.getColumnIndexOrThrow("TURNOVER")));
                entitiy.setTrade(c.getString(c.getColumnIndexOrThrow("TRADE")));
                entitiy.setIndustryName(c.getString(c.getColumnIndexOrThrow("TYPE")));
                entitiy.setIndustryNumber(c.getString(c.getColumnIndexOrThrow("TRADECODE")));
                entitiy.setPeg(c.getString(c.getColumnIndexOrThrow("PEG")));
                entitiy.setFlowEquity(c.getString(c.getColumnIndexOrThrow("FLOWEQUITY")));
                entitiy.setTotalEquity(c.getString(c.getColumnIndexOrThrow("TOTALEQUITY")));
                entitiy.setPbr(c.getString(c.getColumnIndexOrThrow("PBR")));
                entitiy.setIndustryUpAndDown(c.getString(c.getColumnIndexOrThrow("INDYSTRYUPANDDOWN")));
                entitiy.setEquivalentRatio(c.getString(c.getColumnIndexOrThrow("EQUIVALENTRATIO")));
                entitiy.setBuy1(c.getString(c.getColumnIndexOrThrow("BUY1")));
                entitiy.setBuy2(c.getString(c.getColumnIndexOrThrow("BUY2")));
                entitiy.setBuy3(c.getString(c.getColumnIndexOrThrow("BUY3")));
                entitiy.setBuy4(c.getString(c.getColumnIndexOrThrow("BUY4")));
                entitiy.setBuy5(c.getString(c.getColumnIndexOrThrow("BUY5")));
                entitiy.setBuy1Amount(c.getString(c.getColumnIndexOrThrow("BUY1AMOUNT")));
                entitiy.setBuy2Amount(c.getString(c.getColumnIndexOrThrow("BUY2AMOUNT")));
                entitiy.setBuy3Amount(c.getString(c.getColumnIndexOrThrow("BUY3AMOUNT")));
                entitiy.setBuy4Amount(c.getString(c.getColumnIndexOrThrow("BUY4AMOUNT")));
                entitiy.setBuy5Amount(c.getString(c.getColumnIndexOrThrow("BUY5AMOUNT")));
                entitiy.setSell1(c.getString(c.getColumnIndexOrThrow("SELL1")));
                entitiy.setSell2(c.getString(c.getColumnIndexOrThrow("SELL2")));
                entitiy.setSell3(c.getString(c.getColumnIndexOrThrow("SELL3")));
                entitiy.setSell4(c.getString(c.getColumnIndexOrThrow("SELL4")));
                entitiy.setSell5(c.getString(c.getColumnIndexOrThrow("SELL5")));
                entitiy.setSell1Amount(c.getString(c.getColumnIndexOrThrow("SELL1AMOUNT")));
                entitiy.setSell2Amount(c.getString(c.getColumnIndexOrThrow("SELL2AMOUNT")));
                entitiy.setSell3Amount(c.getString(c.getColumnIndexOrThrow("SELL3AMOUNT")));
                entitiy.setSell4Amount(c.getString(c.getColumnIndexOrThrow("SELL4AMOUNT")));
                entitiy.setSell5Amount(c.getString(c.getColumnIndexOrThrow("SELL5AMOUNT")));
                entitiy.setIsHoldStock(c.getString(c.getColumnIndexOrThrow("isHoldStock")));
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("apperHoldStock")));

                entitiy.setMKT_VAL(c.getString(c.getColumnIndexOrThrow("MKT_VAL")));
                entitiy.setSECU_ACC(c.getString(c.getColumnIndexOrThrow("SECU_ACC")));
                entitiy.setSHARE_BLN(c.getString(c.getColumnIndexOrThrow("SHARE_BLN")));
                entitiy.setSHARE_AVL(c.getString(c.getColumnIndexOrThrow("SHARE_AVL")));
                entitiy.setSHARE_QTY(c.getString(c.getColumnIndexOrThrow("SHARE_QTY")));
                entitiy.setPROFIT_RATIO(c.getString(c.getColumnIndexOrThrow("PROFIT_RATIO")));
                entitiy.setMARKET(c.getString(c.getColumnIndexOrThrow("MARKET")));
                entitiy.setBRANCH(c.getString(c.getColumnIndexOrThrow("BRANCH")));
                entitiy.setMKT_PRICE(c.getString(c.getColumnIndexOrThrow("MKT_PRICE")));
                entitiy.setSECU_CODE(c.getString(c.getColumnIndexOrThrow("SECU_CODE")));
                entitiy.setINCOME_AMT(c.getString(c.getColumnIndexOrThrow("INCOME_AMT")));
                entitiy.setSECU_NAME(c.getString(c.getColumnIndexOrThrow("SECU_NAME")));
                entitiy.setCURRENT_COST(c.getString(c.getColumnIndexOrThrow("CURRENT_COST")));

                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }

            if (db != null) {
                db.close();
            }
        }
        return entitiys;
    }

    /**
     * 搜索全部
     * @return
     */
    public static ArrayList<StockInfoEntity> queryStockListDatas() {

        String sql = "SELECT * FROM PUB_STOCKLIST LEFT JOIN HOLD_SEQ ON PUB_STOCKLIST.STOCKCODE = HOLD_SEQ.CODE ORDER BY ID DESC";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        ArrayList<StockInfoEntity> entitiys = new ArrayList<>();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            do {
                StockInfoEntity entitiy = new StockInfoEntity();
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("ISHOLD")));
                entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
                entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
                entitiy.setUpAndDownValue(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGEVALUE"))));
                entitiy.setPriceChangeRatio(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGERATIO"))));
                entitiy.setNewPrice(c.getString(c.getColumnIndexOrThrow("CURRENTPRICE")));
                entitiy.setClose(c.getString(c.getColumnIndexOrThrow("BEFORECLOSEPRICE")));
                entitiy.setTodayOpenPrice(c.getString(c.getColumnIndexOrThrow("TODAYOPENPRICE")));
                entitiy.setTime(c.getString(c.getColumnIndexOrThrow("TIME")));
                entitiy.setDate(c.getString(c.getColumnIndexOrThrow("DATE")));
                entitiy.setHigh(c.getString(c.getColumnIndexOrThrow("HIGH")));
                entitiy.setLow(c.getString(c.getColumnIndexOrThrow("LOW")));
                entitiy.setPlate(c.getString(c.getColumnIndexOrThrow("PLATE")));
                entitiy.setPresentAmount(c.getString(c.getColumnIndexOrThrow("PRESENTAMOUNT")));
                entitiy.setTotalVolume(c.getString(c.getColumnIndexOrThrow("TOTALVOLUME")));
                entitiy.setTotalPrice(c.getString(c.getColumnIndexOrThrow("TOTALPRICE")));
                entitiy.setInflowMoney(c.getString(c.getColumnIndexOrThrow("INFLOWMONEY")));
                entitiy.setOutflowMoney(c.getString(c.getColumnIndexOrThrow("OUTFLOWMONEY")));
                entitiy.setBuyOrder(c.getString(c.getColumnIndexOrThrow("BUYORDER")));
                entitiy.setSellOrder(c.getString(c.getColumnIndexOrThrow("SELLORDER")));
                entitiy.setTurnover(c.getString(c.getColumnIndexOrThrow("TURNOVER")));
                entitiy.setTrade(c.getString(c.getColumnIndexOrThrow("TRADE")));
                entitiy.setIndustryName(c.getString(c.getColumnIndexOrThrow("TYPE")));
                entitiy.setIndustryNumber(c.getString(c.getColumnIndexOrThrow("TRADECODE")));
                entitiy.setPeg(c.getString(c.getColumnIndexOrThrow("PEG")));
                entitiy.setFlowEquity(c.getString(c.getColumnIndexOrThrow("FLOWEQUITY")));
                entitiy.setTotalEquity(c.getString(c.getColumnIndexOrThrow("TOTALEQUITY")));
                entitiy.setPbr(c.getString(c.getColumnIndexOrThrow("PBR")));
                entitiy.setIndustryUpAndDown(c.getString(c.getColumnIndexOrThrow("INDYSTRYUPANDDOWN")));
                entitiy.setEquivalentRatio(c.getString(c.getColumnIndexOrThrow("EQUIVALENTRATIO")));
                entitiy.setBuy1(c.getString(c.getColumnIndexOrThrow("BUY1")));
                entitiy.setBuy2(c.getString(c.getColumnIndexOrThrow("BUY2")));
                entitiy.setBuy3(c.getString(c.getColumnIndexOrThrow("BUY3")));
                entitiy.setBuy4(c.getString(c.getColumnIndexOrThrow("BUY4")));
                entitiy.setBuy5(c.getString(c.getColumnIndexOrThrow("BUY5")));
                entitiy.setBuy1Amount(c.getString(c.getColumnIndexOrThrow("BUY1AMOUNT")));
                entitiy.setBuy2Amount(c.getString(c.getColumnIndexOrThrow("BUY2AMOUNT")));
                entitiy.setBuy3Amount(c.getString(c.getColumnIndexOrThrow("BUY3AMOUNT")));
                entitiy.setBuy4Amount(c.getString(c.getColumnIndexOrThrow("BUY4AMOUNT")));
                entitiy.setBuy5Amount(c.getString(c.getColumnIndexOrThrow("BUY5AMOUNT")));
                entitiy.setSell1(c.getString(c.getColumnIndexOrThrow("SELL1")));
                entitiy.setSell2(c.getString(c.getColumnIndexOrThrow("SELL2")));
                entitiy.setSell3(c.getString(c.getColumnIndexOrThrow("SELL3")));
                entitiy.setSell4(c.getString(c.getColumnIndexOrThrow("SELL4")));
                entitiy.setSell5(c.getString(c.getColumnIndexOrThrow("SELL5")));
                entitiy.setSell1Amount(c.getString(c.getColumnIndexOrThrow("SELL1AMOUNT")));
                entitiy.setSell2Amount(c.getString(c.getColumnIndexOrThrow("SELL2AMOUNT")));
                entitiy.setSell3Amount(c.getString(c.getColumnIndexOrThrow("SELL3AMOUNT")));
                entitiy.setSell4Amount(c.getString(c.getColumnIndexOrThrow("SELL4AMOUNT")));
                entitiy.setSell5Amount(c.getString(c.getColumnIndexOrThrow("SELL5AMOUNT")));
                entitiy.setIsHoldStock(c.getString(c.getColumnIndexOrThrow("isHoldStock")));
                entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("apperHoldStock")));

                entitiy.setMKT_VAL(c.getString(c.getColumnIndexOrThrow("MKT_VAL")));
                entitiy.setSECU_ACC(c.getString(c.getColumnIndexOrThrow("SECU_ACC")));
                entitiy.setSHARE_BLN(c.getString(c.getColumnIndexOrThrow("SHARE_BLN")));
                entitiy.setSHARE_AVL(c.getString(c.getColumnIndexOrThrow("SHARE_AVL")));
                entitiy.setSHARE_QTY(c.getString(c.getColumnIndexOrThrow("SHARE_QTY")));
                entitiy.setPROFIT_RATIO(c.getString(c.getColumnIndexOrThrow("PROFIT_RATIO")));
                entitiy.setMARKET(c.getString(c.getColumnIndexOrThrow("MARKET")));
                entitiy.setBRANCH(c.getString(c.getColumnIndexOrThrow("BRANCH")));
                entitiy.setMKT_PRICE(c.getString(c.getColumnIndexOrThrow("MKT_PRICE")));
                entitiy.setSECU_CODE(c.getString(c.getColumnIndexOrThrow("SECU_CODE")));
                entitiy.setINCOME_AMT(c.getString(c.getColumnIndexOrThrow("INCOME_AMT")));
                entitiy.setSECU_NAME(c.getString(c.getColumnIndexOrThrow("SECU_NAME")));
                entitiy.setCURRENT_COST(c.getString(c.getColumnIndexOrThrow("CURRENT_COST")));
                entitiy.setStockholdon(c.getString(c.getColumnIndexOrThrow("STOCKHOLDON")));
//                LogHelper.e(TAG,entitiy.getStockNumber()+":"+entitiy.getStockName());
                entitiys.add(entitiy);
            } while (c.moveToNext());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }

            if (db != null) {
                db.close();
            }
        }
        return entitiys;
    }


    /**
     * 根据主键搜索
     * @return
     */
    public static StockInfoEntity queryStockFromID(String stockCode) {
        String sql = "SELECT * FROM PUB_STOCKLIST  LEFT JOIN HOLD_SEQ ON PUB_STOCKLIST.STOCKCODE = HOLD_SEQ.CODE WHERE PUB_STOCKLIST.STOCKCODE = " + Helper.getSafeString(stockCode) + "ORDER BY ID DESC";
        SQLiteDatabase db = DbUtil.getInstance().getDB();
        Cursor c = null;
        StockInfoEntity entitiy = new StockInfoEntity();
        try {
            c = db.rawQuery(sql.toString(), null);
            if (c == null || !c.moveToFirst()) {
                return null;
            }
            entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("ISHOLD")));
            entitiy.setStockNumber(c.getString(c.getColumnIndexOrThrow("STOCKCODE")));
            entitiy.setStockName(c.getString(c.getColumnIndexOrThrow("NAME")));
            entitiy.setUpAndDownValue(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGEVALUE"))));
            entitiy.setPriceChangeRatio(TransitionUtils.string2double(c.getString(c.getColumnIndexOrThrow("PRICECHANGERATIO"))));
            entitiy.setNewPrice(c.getString(c.getColumnIndexOrThrow("CURRENTPRICE")));
            entitiy.setClose(c.getString(c.getColumnIndexOrThrow("BEFORECLOSEPRICE")));
            entitiy.setTodayOpenPrice(c.getString(c.getColumnIndexOrThrow("TODAYOPENPRICE")));
            entitiy.setTime(c.getString(c.getColumnIndexOrThrow("TIME")));
            entitiy.setDate(c.getString(c.getColumnIndexOrThrow("DATE")));
            entitiy.setHigh(c.getString(c.getColumnIndexOrThrow("HIGH")));
            entitiy.setLow(c.getString(c.getColumnIndexOrThrow("LOW")));
            entitiy.setPlate(c.getString(c.getColumnIndexOrThrow("PLATE")));
            entitiy.setPresentAmount(c.getString(c.getColumnIndexOrThrow("PRESENTAMOUNT")));
            entitiy.setTotalVolume(c.getString(c.getColumnIndexOrThrow("TOTALVOLUME")));
            entitiy.setTotalPrice(c.getString(c.getColumnIndexOrThrow("TOTALPRICE")));
            entitiy.setInflowMoney(c.getString(c.getColumnIndexOrThrow("INFLOWMONEY")));
            entitiy.setOutflowMoney(c.getString(c.getColumnIndexOrThrow("OUTFLOWMONEY")));
            entitiy.setBuyOrder(c.getString(c.getColumnIndexOrThrow("BUYORDER")));
            entitiy.setSellOrder(c.getString(c.getColumnIndexOrThrow("SELLORDER")));
            entitiy.setTurnover(c.getString(c.getColumnIndexOrThrow("TURNOVER")));
            entitiy.setTrade(c.getString(c.getColumnIndexOrThrow("TRADE")));
            entitiy.setIndustryName(c.getString(c.getColumnIndexOrThrow("TYPE")));
            entitiy.setIndustryNumber(c.getString(c.getColumnIndexOrThrow("TRADECODE")));
            entitiy.setPeg(c.getString(c.getColumnIndexOrThrow("PEG")));
            entitiy.setFlowEquity(c.getString(c.getColumnIndexOrThrow("FLOWEQUITY")));
            entitiy.setTotalEquity(c.getString(c.getColumnIndexOrThrow("TOTALEQUITY")));
            entitiy.setPbr(c.getString(c.getColumnIndexOrThrow("PBR")));
            entitiy.setIndustryUpAndDown(c.getString(c.getColumnIndexOrThrow("INDYSTRYUPANDDOWN")));
            entitiy.setEquivalentRatio(c.getString(c.getColumnIndexOrThrow("EQUIVALENTRATIO")));
            entitiy.setBuy1(c.getString(c.getColumnIndexOrThrow("BUY1")));
            entitiy.setBuy2(c.getString(c.getColumnIndexOrThrow("BUY2")));
            entitiy.setBuy3(c.getString(c.getColumnIndexOrThrow("BUY3")));
            entitiy.setBuy4(c.getString(c.getColumnIndexOrThrow("BUY4")));
            entitiy.setBuy5(c.getString(c.getColumnIndexOrThrow("BUY5")));
            entitiy.setBuy1Amount(c.getString(c.getColumnIndexOrThrow("BUY1AMOUNT")));
            entitiy.setBuy2Amount(c.getString(c.getColumnIndexOrThrow("BUY2AMOUNT")));
            entitiy.setBuy3Amount(c.getString(c.getColumnIndexOrThrow("BUY3AMOUNT")));
            entitiy.setBuy4Amount(c.getString(c.getColumnIndexOrThrow("BUY4AMOUNT")));
            entitiy.setBuy5Amount(c.getString(c.getColumnIndexOrThrow("BUY5AMOUNT")));
            entitiy.setSell1(c.getString(c.getColumnIndexOrThrow("SELL1")));
            entitiy.setSell2(c.getString(c.getColumnIndexOrThrow("SELL2")));
            entitiy.setSell3(c.getString(c.getColumnIndexOrThrow("SELL3")));
            entitiy.setSell4(c.getString(c.getColumnIndexOrThrow("SELL4")));
            entitiy.setSell5(c.getString(c.getColumnIndexOrThrow("SELL5")));
            entitiy.setSell1Amount(c.getString(c.getColumnIndexOrThrow("SELL1AMOUNT")));
            entitiy.setSell2Amount(c.getString(c.getColumnIndexOrThrow("SELL2AMOUNT")));
            entitiy.setSell3Amount(c.getString(c.getColumnIndexOrThrow("SELL3AMOUNT")));
            entitiy.setSell4Amount(c.getString(c.getColumnIndexOrThrow("SELL4AMOUNT")));
            entitiy.setSell5Amount(c.getString(c.getColumnIndexOrThrow("SELL5AMOUNT")));
            entitiy.setIsHoldStock(c.getString(c.getColumnIndexOrThrow("isHoldStock")));
            entitiy.setApperHoldStock(c.getString(c.getColumnIndexOrThrow("apperHoldStock")));

            entitiy.setMKT_VAL(c.getString(c.getColumnIndexOrThrow("MKT_VAL")));
            entitiy.setSECU_ACC(c.getString(c.getColumnIndexOrThrow("SECU_ACC")));
            entitiy.setSHARE_BLN(c.getString(c.getColumnIndexOrThrow("SHARE_BLN")));
            entitiy.setSHARE_AVL(c.getString(c.getColumnIndexOrThrow("SHARE_AVL")));
            entitiy.setSHARE_QTY(c.getString(c.getColumnIndexOrThrow("SHARE_QTY")));
            entitiy.setPROFIT_RATIO(c.getString(c.getColumnIndexOrThrow("PROFIT_RATIO")));
            entitiy.setMARKET(c.getString(c.getColumnIndexOrThrow("MARKET")));
            entitiy.setBRANCH(c.getString(c.getColumnIndexOrThrow("BRANCH")));
            entitiy.setMKT_PRICE(c.getString(c.getColumnIndexOrThrow("MKT_PRICE")));
            entitiy.setSECU_CODE(c.getString(c.getColumnIndexOrThrow("SECU_CODE")));
            entitiy.setINCOME_AMT(c.getString(c.getColumnIndexOrThrow("INCOME_AMT")));
            entitiy.setSECU_NAME(c.getString(c.getColumnIndexOrThrow("SECU_NAME")));
            entitiy.setCURRENT_COST(c.getString(c.getColumnIndexOrThrow("CURRENT_COST")));
            entitiy.setStockholdon(c.getString(c.getColumnIndexOrThrow("STOCKHOLDON")));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return entitiy;
    }

    /**
     * 删除所有数据
     */
    public static void deleteAllStocListkDatas() {
        String sql = "DELETE FROM PUB_STOCKLIST";
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 根据主键删除数据
     */
    public static boolean deleteStockFromID(String stockCode) {
        String sql = "DELETE FROM PUB_STOCKLIST WHERE  STOCKCODE = " + Helper.getSafeString(stockCode);
        boolean tag = false;
        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql.toString());
            tag = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, e.toString());
            tag = false;
        } finally {
//            if (db != null) {
//                db.close();
//            }
        }

        return tag;
    }
    /**
     * 根据股票代码修改一条数据
     */
    public static void updateOneStockDataByStockNumber(StockInfoEntity bean) {
        String sql = "update PUB_STOCKLIST set "
                + "NAME = " +  Helper.getSafeString(bean.getStockName())
                + ", PRICECHANGEVALUE = " + Helper.getSafeString(""+bean.getUpAndDownValue())
                + ", PRICECHANGERATIO = " + Helper.getSafeString(""+bean.getPriceChangeRatio())
                + ", CURRENTPRICE = " + Helper.getSafeString(bean.getNewPrice())
                + ", BEFORECLOSEPRICE = " + Helper.getSafeString(bean.getClose())
                + ", TODAYOPENPRICE = " + Helper.getSafeString(bean.getTodayOpenPrice())
                + ", TIME = " + Helper.getSafeString(bean.getTime())
                + ", DATE = " + Helper.getSafeString(bean.getDate())
                + ", HIGH = " + Helper.getSafeString(bean.getHigh())
                + ", LOW = " + Helper.getSafeString(bean.getLow())
                + ", PLATE = " + Helper.getSafeString(bean.getPlate())
                + ", PRESENTAMOUNT = " + Helper.getSafeString(bean.getPresentAmount())
                + ", TOTALVOLUME = " + Helper.getSafeString(bean.getTotalVolume())
                + ", TOTALPRICE = " + Helper.getSafeString(bean.getTotalPrice())
                + ", INFLOWMONEY = " + Helper.getSafeString(bean.getInflowMoney())
                + ", OUTFLOWMONEY = " + Helper.getSafeString(bean.getOutflowMoney())
                + ", BUYORDER = " + Helper.getSafeString(bean.getBuyOrder())
                + ", SELLORDER = " + Helper.getSafeString(bean.getSellOrder())
                + ", TURNOVER = " + Helper.getSafeString(bean.getTurnover())
                + ", TRADE = " + Helper.getSafeString(bean.getTrade())
                + ", TYPE = " + Helper.getSafeString(bean.getIndustryName())
                + ", TRADECODE = " + Helper.getSafeString(bean.getIndustryNumber())
                + ", PEG = " + Helper.getSafeString(bean.getPeg())
                + ", FLOWEQUITY = " + Helper.getSafeString(bean.getFlowEquity())
                + ", TOTALEQUITY = " + Helper.getSafeString(bean.getTotalEquity())
                + ", PBR = " + Helper.getSafeString(bean.getPbr())
                + ", INDYSTRYUPANDDOWN = " + Helper.getSafeString(bean.getIndustryUpAndDown())
                + ", EQUIVALENTRATIO = " + Helper.getSafeString(bean.getEquivalentRatio())
                + ", BUY1 = " + Helper.getSafeString(bean.getBuy1())
                + ", BUY2 = " + Helper.getSafeString(bean.getBuy2())
                + ", BUY3 = " + Helper.getSafeString(bean.getBuy3())
                + ", BUY4 = " + Helper.getSafeString(bean.getBuy4())
                + ", BUY5 = " + Helper.getSafeString(bean.getBuy5())
                + ", BUY1AMOUNT = " + Helper.getSafeString(bean.getBuy1Amount())
                + ", BUY2AMOUNT = " + Helper.getSafeString(bean.getBuy2Amount())
                + ", BUY3AMOUNT = " + Helper.getSafeString(bean.getBuy3Amount())
                + ", BUY4AMOUNT = " + Helper.getSafeString(bean.getBuy4Amount())
                + ", BUY5AMOUNT = " + Helper.getSafeString(bean.getBuy5Amount())
                + ", SELL1 = " + Helper.getSafeString(bean.getSell1())
                + ", SELL2 = " + Helper.getSafeString(bean.getSell2())
                + ", SELL3 = " + Helper.getSafeString(bean.getSell3())
                + ", SELL4 = " + Helper.getSafeString(bean.getSell4())
                + ", SELL5 = " + Helper.getSafeString(bean.getSell5())
                + ", SELL1AMOUNT = " + Helper.getSafeString(bean.getSell1Amount())
                + ", SELL2AMOUNT = " + Helper.getSafeString(bean.getSell2Amount())
                + ", SELL3AMOUNT = " + Helper.getSafeString(bean.getSell3Amount())
                + ", SELL4AMOUNT = " + Helper.getSafeString(bean.getSell4Amount())
                + ", SELL5AMOUNT = " + Helper.getSafeString(bean.getSell5Amount())
                + ", isHoldStock = " + Helper.getSafeString(bean.getIsHoldStock())
                + ", apperHoldStock = " + Helper.getSafeString(bean.getApperHoldStock())

                + ", MKT_VAL = " + Helper.getSafeString(bean.getMKT_VAL())
                + ", SECU_ACC = "+ Helper.getSafeString(bean.getSECU_ACC())
                + ", SHARE_BLN = "+ Helper.getSafeString(bean.getSHARE_BLN())
                + ", SHARE_AVL = "+ Helper.getSafeString(bean.getSHARE_AVL())
                + ", SHARE_QTY = "+ Helper.getSafeString(bean.getSHARE_QTY())
                + ", PROFIT_RATIO = "+ Helper.getSafeString(bean.getPROFIT_RATIO())
                + ", MARKET = "+ Helper.getSafeString(bean.getMARKET())
                + ", BRANCH = "+ Helper.getSafeString(bean.getBRANCH())
                + ", MKT_PRICE = "+ Helper.getSafeString(bean.getMKT_PRICE())
                + ", SECU_CODE = "+ Helper.getSafeString(bean.getSECU_CODE())
                + ", INCOME_AMT = "+ Helper.getSafeString(bean.getINCOME_AMT())
                + ", SECU_NAME = "+ Helper.getSafeString(bean.getSECU_NAME())
                + ", CURRENT_COST = "+ Helper.getSafeString(bean.getCURRENT_COST())

                +" where STOCKCODE  = " + Helper.getSafeString(bean.getStockNumber());

        SQLiteDatabase db = null;
        try {
            db = DbUtil.getInstance().getDB();
            db.execSQL(sql);
        } catch (Exception e) {
            LogHelper.e(TAG, e.toString());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
