package com.tpyzq.mobile.pangu.util.panguutil;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.data.AddPositionEntity;
import com.tpyzq.mobile.pangu.util.Helper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wangqi on 2017/2/9.
 * 添加持仓
 */

public class AddPosition {

    private static AddPosition addPosition;
    private boolean is = false;
    private static ArrayList<AddPositionEntity> list;

    private AddPosition() {
    }

    public static AddPosition getInstance() {
        if (addPosition == null) {
            addPosition = new AddPosition();
            list = new ArrayList<>();

        }

        return addPosition;
    }


    /**
     * 存数据
     *
     * @param tag  类名
     * @param data JSONArray 数组
     */
    public final void setData(String tag, Object data) {
//        LogHelper.e("AddPostion",tag+":"+data.toString());
        list.clear();
        is = true;
//        if ("HoldCloudConnect".equals(tag)) {
//            List<Map<String, Object>> mData = (List<Map<String, Object>>) data;
//            for (Map<String, Object> map : mData) {
//                AddPositionEntity addPositionBean = new AddPositionEntity();
//
//                //成本价
//                String MKT_PRICE = "";
//                if (null != map.get("CURRENT_COST")) {
//                    MKT_PRICE = String.valueOf(map.get("CURRENT_COST"));
//                }
//
//                //证券代码
//                String SECU_CODE = "";
//                if (null != map.get("SECU_CODE")) {
//                    SECU_CODE = String.valueOf(map.get("SECU_CODE"));
//                }
//
//                //交易市场
//                String MARKET = "";
//                if (null != map.get("MARKET")) {
//                    MARKET = String.valueOf(map.get("MARKET"));
//                }
//
//                if ("1".equals(MARKET)) {
//                    String stockNumber = Helper.getStockCode(SECU_CODE, "83");
//                    addPositionBean.setStockcode(stockNumber);
//                    addPositionBean.setPrice(MKT_PRICE);
//
//                } else if ("2".equals(MARKET)) {
//                    String stockNumber = Helper.getStockCode(SECU_CODE, "90");
//                    addPositionBean.setStockcode(stockNumber);
//                    addPositionBean.setPrice(MKT_PRICE);
//                }
//                list.add(addPositionBean);
//            }
//        } else if ("TakeAPosition".equals(tag) || "PositionTransactionPager".equals(tag)||"StockDetailActivity".equals(tag)) {
            JSONArray mData = (JSONArray) data;
            if (data != null && mData.length() > 0) {
                try {
                    for (int j = 0; j < mData.length(); j++) {
                        AddPositionEntity addPositionBean = new AddPositionEntity();
                        addPositionBean.setPrice(mData.getJSONObject(j).getString("CURRENT_COST"));    //成本价
                        if (mData.getJSONObject(j).getString("MARKET").equals("2")) {
                            addPositionBean.setStockcode(Helper.getStockCode(mData.getJSONObject(j).getString("SECU_CODE"), "90"));
                        } else {
                            addPositionBean.setStockcode(Helper.getStockCode(mData.getJSONObject(j).getString("SECU_CODE"), "83"));
                        }
                        list.add(addPositionBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//        }
    }


    /**
     * 判断存是否有数据
     *
     * @return
     */
    public final boolean isHaveData() {
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isSuccessGetData(){
        return  is;
    }
    /**
     * 获取持仓数据
     *
     * @param stockcode 8位股票代码
     * @return
     */
    public final HashMap getData(String stockcode) {
        if (list != null && list.size() > 0) {
            HashMap map = new HashMap();
            for (int i = 0; i < list.size(); i++) {
                if (!TextUtils.isEmpty(stockcode) && stockcode.equals(list.get(i).getStockcode())) {
                    map.put("SECU_CODE", list.get(i).getStockcode());
                    map.put("MKT_PRICE", list.get(i).getPrice());
                }
            }
            if (map != null && map.size() > 0) {
                return map;
            } else {
                return null;
            }
        }
        return null;
    }
}

