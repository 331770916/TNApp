package com.tpyzq.mobile.pangu.util.panguutil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.util.ConstantUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangwenbo on 2016/7/27.
 *
 * 自选股临时数据，从数据库中取出，以后某些逻辑在这里处理后，在更新数据库
 */
public class SelfChoiceStockTempData {

    private static SelfChoiceStockTempData mSelfChoiceStockTempData;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;

    private SelfChoiceStockTempData() {
        mSharedPreferences = CustomApplication.getContext().getSharedPreferences(ConstantUtil.PUB_STOCKLIST, Activity.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

    }

    public static SelfChoiceStockTempData getInstance() {

        if (mSelfChoiceStockTempData == null) {
            mSelfChoiceStockTempData = new SelfChoiceStockTempData();
        }

        return mSelfChoiceStockTempData;
    }

    /**
     * 获取所有数据
     * @return
     */
    public Map<String, ?> getAll() {
        Map<String, ?> maps = mSharedPreferences.getAll();
        return maps;
    }

    /**
     * @return
     */
    public ArrayList<StockInfoEntity> getBeans() {
        Map<String, ?> maps = getAll();
        Set<String> set = maps.keySet();
        Iterator<String> iterator = set.iterator();

        ArrayList<StockInfoEntity> entities = new ArrayList<>();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String stockName = getSelfchoicestockTempValue(key);
            StockInfoEntity entity = new StockInfoEntity();
            entity.setStockNumber(key);
            entity.setStockName(stockName);
            entities.add(entity);
        }

        return entities;
    }

    public void setAll() {
        ArrayList<StockInfoEntity> beans = Db_PUB_STOCKLIST.queryStockListDatas();

        if (beans != null && beans.size() > 0) {
            for (StockInfoEntity bean : beans) {
                if (!TextUtils.isEmpty(bean.getCode()) && !TextUtils.isEmpty(bean.getStockName()) && bean.getIsSelfChoiceStock()) {
                    mEditor.putString(bean.getCode(), bean.getStockName());
                    mEditor.commit();
                }
            }
        }

    }

    /**
     * 清空数据
     */
    public void clearFile () {
        mEditor.clear();
        mEditor.commit();
    }

    /**
     * 是否包含key
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        boolean isContains = mSelfChoiceStockTempData.getAll().containsKey(key);
        return isContains;
    }

    /**
     * 获取某条数据
     * @param key
     * @return
     */
    public String getSelfchoicestockTempValue(String key) {
        String value = mSharedPreferences.getString(key, "");
        return value;
    }

    /**
     * 存入数据
     * @param key
     * @param value
     */
    public void setSelfchoicestockTempValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * 修改某条数据
     * @param key
     * @param value
     */
    public void updateSelfchoicestockTempValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * 移除某条数据
     * @param key
     */
    public void removeSelfchoicestockTempValue(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

}
