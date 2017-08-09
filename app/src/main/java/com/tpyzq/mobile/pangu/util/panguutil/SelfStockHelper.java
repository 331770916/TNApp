package com.tpyzq.mobile.pangu.util.panguutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.StockTable;
import com.tpyzq.mobile.pangu.http.doConnect.self.AddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToAddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.interfac.OneTimiceAddSelfChoiceListener;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.DoSelfChoiceResultDialog;
import com.tpyzq.mobile.pangu.view.dialog.MistakeDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 自选股信息帮助类
 * Created by longfeng on 2017/3/13.
 */

public class SelfStockHelper {
    public static void explanOneTimiceAddSelfChoiceResult(Activity activity, String json) {
        if (TextUtils.isEmpty(json)) {
            MistakeDialog.showDialog("网络异常", activity);
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(json);
            String code = jsonObject.getString("code");
            String totalcount = String.valueOf(jsonObject.get("totalcount"));
            if ("1".equals(code) && "0".equals(totalcount)) {
                MistakeDialog.showDialog("添加自选股失败，超出50条限制", activity, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {

                    }
                });
                return;
            }

            if ("1".equals(code) && !"0".equals(totalcount)) {
                String msg = "成功添加" + totalcount + "只自选股，其余自选股超出50条限制，请删除再添加";
                DoSelfChoiceResultDialog.getInstance().singleDialog(msg, activity);
            } else {
                DoSelfChoiceResultDialog.getInstance().singleDialog("添加自选股成功", activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MistakeDialog.showDialog("网络异常", activity);
        }
    }

    /**
     * 解析导入自选返回的结果
     *
     * @param activity
     * @param json
     */
    public static void explanImportHoldResult(Activity activity, String json) {
        if (TextUtils.isEmpty(json)) {
            MistakeDialog.showDialog("网络异常", activity);
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            String code = jsonObject.getString("code");
            String totalcount = jsonObject.getString("totalcount");
            if ("1".equals(code) && "0".equals(totalcount)) {
                MistakeDialog.showDialog("导入持仓，同步云自选股上传失败，超出50条限制", activity, new MistakeDialog.MistakeDialgoListener() {
                    @Override
                    public void doPositive() {

                    }
                });
                return;
            }

            if ("1".equals(code) && !"0".equals(totalcount)) {
                String msg = "成功添加" + totalcount + "只自选股，其余自选股超出50条限制，请删除再添加";
                CentreToast.showText(activity,msg,true);
            } else {
                CentreToast.showText(activity,"导入持仓成功",true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            MistakeDialog.showDialog("网络异常", activity);
        }
    }

    /**
     * 一键添加自选股
     *
     * @param TAG   标志
     * @param token token
     * @param datas 股票实体类集合  实体类需要传 股票的名称， 代码 ， 和价格
     */
    public static void oneTimiceAddSelfChoice(String TAG, String token,
                                              ArrayList<StockInfoEntity> datas,
                                              final OneTimiceAddSelfChoiceListener listener) {
        if (datas == null || datas.size() <= 0) {
            return;
        }
        //判断当前用户是否注册
        if (Db_PUB_USERS.isRegister()) {
            String stockNumbers = "";
            String stockNames = "";
            String prices = "";
            StringBuilder sbNumber = new StringBuilder();
            StringBuilder sbName = new StringBuilder();
            StringBuilder sbPrice = new StringBuilder();
            for (StockInfoEntity entity : datas) {
                String stockNumber = entity.getStockNumber();
                if (!TextUtils.isEmpty(stockNumber) && Pattern.compile("[a-zA-Z]").matcher(stockNumber).find()) {
                    String mark = "";
                    if (!TextUtils.isEmpty(stockNumber) && stockNumber.contains("SH")) {
                        mark = "83";
                    } else if (!TextUtils.isEmpty(stockNumber) && stockNumber.contains("SZ")) {
                        mark = "90";
                    }
                    String tempStockCode = Helper.getStockCode(stockNumber, mark);
                    stockNumber = tempStockCode.substring(0, 8);
                    entity.setStockNumber(stockNumber);
                }
                BRutil.doListenAddOrRemoveSelfStock(stockNumber, entity.getStockName(), BRutil.ACTIONADDSELFSTOCK, "0");
                entity.setStock_flag(StockTable.STOCK_OPTIONAL);
                Db_PUB_STOCKLIST.addOneStockListData(entity);
                if (!TextUtils.isEmpty(entity.getStockNumber())) {
                    sbNumber.append(entity.getStockNumber()).append(",");
                }
                if (!TextUtils.isEmpty(entity.getStockName())) {
                    sbName.append(entity.getStockName()).append(",");
                }
                if (!TextUtils.isEmpty(entity.getNewPrice())) {
                    sbPrice.append(entity.getNewPrice()).append(",");
                }
            }
            stockNumbers = sbNumber.toString();
            stockNames = sbName.toString();
            prices = sbPrice.toString();

            if (stockNames.contains(",")) {
                stockNames = stockNames.substring(0, stockNames.length() - 1);
            }

            if (stockNumbers.contains(",")) {
                stockNumbers = stockNumbers.substring(0, stockNumbers.length() - 1);
            }

            if (prices.contains(",")) {
                prices = prices.substring(0, prices.length() - 1);
            }

            //上传到云
            SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(new ICallbackResult() {
                @Override
                public void getResult(Object result, String tag) {

                    String json = String.valueOf(result);

                    if (TextUtils.isEmpty(json)) {
                        listener.getResult("网络异常");
                        return;
                    }

                    listener.getResult(json);
                }
            });

            simpleRemoteControl.setCommand(new ToAddSelfChoiceStockConnect(new AddSelfChoiceStockConnect(TAG, token, UserUtil.capitalAccount, stockNumbers, UserUtil.userId, stockNames, prices)));
            simpleRemoteControl.startConnect();

        } else {

            int totalcount = 0;
            for (StockInfoEntity entity : datas) {
                int count = Db_PUB_STOCKLIST.getStockListCount();

                if (count > 50) {
                    listener.getResult("{\"code\":\"1\",\"totalcount\":" + totalcount + " }");
                    return;
                }

                totalcount = totalcount + 1;
                BRutil.doListenAddOrRemoveSelfStock(entity.getStockNumber(), entity.getStockName(), BRutil.ACTIONADDSELFSTOCK, "0");
                entity.setStock_flag(StockTable.STOCK_OPTIONAL);
                Db_PUB_STOCKLIST.addOneStockListData(entity);

            }

            listener.getResult("{\"code\":\"0\",\"totalcount\":" + totalcount + " }");
        }
    }

    public static void sendUpdateSelfChoiceBrodcast(Context context, String stockNumber) {
        Intent intent = new Intent();
        intent.setAction("com.tpyzq.mobile.pangu.activity.market.selfChoice.action");
        if (!TextUtils.isEmpty(stockNumber)) {
            intent.putExtra("stockNumber", stockNumber);
        }
        intent.putExtra("doAction", "updateSelfChoiceData");
        context.sendBroadcast(intent);
    }

    }
