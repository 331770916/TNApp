package com.tpyzq.mobile.pangu.adapter.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.base.SimpleRemoteControl;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_HOME_INFO;
import com.tpyzq.mobile.pangu.db.Db_PUB_SEARCHHISTORYSTOCK;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.StockTable;
import com.tpyzq.mobile.pangu.http.doConnect.self.AddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.DeleteSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToAddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToDelteSlefChoiceStockConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.SelfChoiceStockTempData;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by wangqi on 2016/10/10.
 * 热搜股票  Adapter
 */
public class HotSearchAdapter extends BaseAdapter {
    private static final String TAG = "HotSearchAdapter";
    private Context mContext;
    private List<StockInfoEntity> mList;
    private String Curprice_null = "0.0";     //当价钱是 -  或空的时候 默认传值

    public HotSearchAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<StockInfoEntity> data) {
        mList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mList != null && mList.size() > 0) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mList != null && mList.size() > 0) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_hotsearch_listview, null);
        }
        TextView item1 = (TextView) convertView.findViewById(R.id.item_name);
        TextView item2 = (TextView) convertView.findViewById(R.id.item_stockcode);
        TextView item3 = (TextView) convertView.findViewById(R.id.item_change);
        TextView item4 = (TextView) convertView.findViewById(R.id.item_amount);
        final ImageView imageitem1 = (ImageView) convertView.findViewById(R.id.hold_iv);
        imageitem1.setVisibility(View.GONE);
        final ImageView imageitem2 = (ImageView) convertView.findViewById(R.id.item_icon);

        if (!TextUtils.isEmpty(mList.get(position).getStockName())) {
            item1.setText(mList.get(position).getStockName());
        } else {
            item1.setText("--");
        }


        String mStockNumber = mList.get(position).getStockNumber();
        if (!TextUtils.isEmpty(mStockNumber) && !mStockNumber.equals("-")) {
            String mFrl = String.valueOf(mStockNumber.charAt(0));
            if ("1".equals(mFrl)) {
                item2.setText("SH" + mStockNumber.substring(2));
            } else if ("2".equals(mFrl)) {
                item2.setText("SZ" + mStockNumber.substring(2));
            }
        } else {
            item2.setText("--");
        }

        if (!TextUtils.isEmpty(mList.get(position).getRead())) {
            if (mList.get(position).getRead().contains("-")) {
                if (mList.get(position).getRead().equals("-")) {
                    item3.setText(mList.get(position).getRead() + "%");
                    item3.setTextColor(Color.parseColor("#4c4c4c"));
                } else {
                    item3.setText(mList.get(position).getRead() + "%");
                    item3.setTextColor(Color.parseColor("#27aa46"));
                }
            } else if (mList.get(position).getRead().equals("0")) {
                item3.setText(mList.get(position).getRead() + "%");
                item3.setTextColor(Color.parseColor("#4c4c4c"));
            } else {
                item3.setText("+" + mList.get(position).getRead() + "%");
                item3.setTextColor(Color.parseColor("#e84242"));
            }
        } else {
            item3.setText("--");
        }



        if (!TextUtils.isEmpty(mList.get(position).getViewcount())) {
            item4.setText(mList.get(position).getViewcount());
        } else {
            item4.setText("--");
        }


        if (!TextUtils.isEmpty(mList.get(position).getIsHoldStock())) {
            String appearHold = SpUtils.getString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, "false");
            if (!TextUtils.isEmpty(mList.get(position).getApperHoldStock()) && "true".equals(mList.get(position).getStockholdon()) && "true".equals(appearHold)) {
                imageitem1.setVisibility(View.VISIBLE);
            } else {
                imageitem1.setVisibility(View.GONE);
            }
        }


        if (mList.get(position).getIsSelfChoiceStock()) {
            imageitem2.setImageResource(R.mipmap.search_remove);
        } else {
            imageitem2.setImageResource(R.mipmap.search_add);
        }

        imageitem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //添加数据库， 处理shared文件， 转换图片显示， 转换值状态
                final String stockNumber = mList.get(position).getStockNumber();//股票代码
                final String stockName = mList.get(position).getStockName(); //股票名称
                final String price = mList.get(position).getCurprice();

                boolean isSelfChoiceStock = mList.get(position).getIsSelfChoiceStock();

                if (isSelfChoiceStock) {
                    if (!TextUtils.isEmpty(price)) {
                        if (!price.equals("-")) {
                            BRutil.doListenAddOrRemoveSelfStock(stockNumber, stockName, BRutil.ACTIONREMOVESELFSTOCK, price);
                        } else {
                            BRutil.doListenAddOrRemoveSelfStock(stockNumber, stockName, BRutil.ACTIONREMOVESELFSTOCK, Curprice_null);
                        }
                    } else {
                        BRutil.doListenAddOrRemoveSelfStock(stockNumber, stockName, BRutil.ACTIONREMOVESELFSTOCK, Curprice_null);
                    }
                    if (Db_PUB_USERS.isRegister()) {
                        final Dialog dialog = LoadingDialog.initDialog((Activity) mContext, "正同步到云端");
                        dialog.show();
                        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(new ICallbackResult() {
                            @Override
                            public void getResult(Object result, String tag) {

                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                String msg = (String) result;
                                if (!msg.contains("成功")) {
                                    showDialog("" + msg);
                                    return;
                                }

                                try {
                                    JSONObject jsonObject = new JSONObject(msg);
                                    String _msg = jsonObject.getString("msg");
                                    String code = jsonObject.getString("code");
                                    String totalcount = jsonObject.getString("totalcount");
                                    if ("0".equals(totalcount)) {
                                        showDialog("totalCount:" + totalcount + ",服务器无该数据");
                                    } else {
                                        boolean tag1 = Db_PUB_STOCKLIST.deleteStockFromID(stockNumber);
                                        Db_HOME_INFO.deleteOneSelfNewsData(stockNumber);

                                        mList.get(position).setSelfChoicStock(false);
                                        mList.get(position).setStock_flag(StockTable.STOCK_HISTORY_OPTIONAL);
                                        Db_PUB_SEARCHHISTORYSTOCK.addOneData(mList.get(position));
                                        SelfChoiceStockTempData.getInstance().removeSelfchoicestockTempValue(stockNumber);
                                        imageitem2.setImageResource(R.mipmap.search_add);

                                        if (tag1) {
                                            CentreToast.showText(mContext,"删除自选股成功");
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        simpleRemoteControl.setCommand(new ToDelteSlefChoiceStockConnect(new DeleteSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stockNumber, UserUtil.userId)));
                        simpleRemoteControl.startConnect();
                    } else {
                        boolean tag1 = Db_PUB_STOCKLIST.deleteStockFromID(stockNumber);
                        Db_HOME_INFO.deleteOneSelfNewsData(stockNumber);
                        mList.get(position).setSelfChoicStock(false);
                        mList.get(position).setStock_flag(StockTable.STOCK_HISTORY_OPTIONAL);
                        Db_PUB_SEARCHHISTORYSTOCK.addOneData(mList.get(position));
                        SelfChoiceStockTempData.getInstance().removeSelfchoicestockTempValue(stockNumber);
                        imageitem2.setImageResource(R.mipmap.search_add);
                        if (tag1) {
                            CentreToast.showText(mContext,"删除自选股成功");
                        }
                    }
                } else {
                    mList.get(position).setSelfChoicStock(true);
                    if (!TextUtils.isEmpty(price)) {
                        if (!price.equals("-")) {
                            BRutil.doListenAddOrRemoveSelfStock(stockNumber, stockName, BRutil.ACTIONADDSELFSTOCK, price);
                        } else {
                            BRutil.doListenAddOrRemoveSelfStock(stockNumber, stockName, BRutil.ACTIONADDSELFSTOCK, Curprice_null);
                        }
                    } else {
                        BRutil.doListenAddOrRemoveSelfStock(stockNumber, stockName, BRutil.ACTIONADDSELFSTOCK, Curprice_null);
                    }
                    if (Db_PUB_USERS.isRegister()) {
                        //同步网络 从云端增加刚才增加的自选股
                        final Dialog dialog = LoadingDialog.initDialog((Activity) mContext, "正在加载");
                        dialog.show();
                        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(new ICallbackResult() {
                            @Override
                            public void getResult(Object result, String tag) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }

                                String msg = (String) result;

                                if (!msg.contains("成功")) {
                                    showDialog("" + msg);
                                    return;
                                }

                                mList.get(position).setStock_flag(StockTable.STOCK_OPTIONAL);
                                boolean tag1 = Db_PUB_STOCKLIST.addOneStockListData(mList.get(position));

                                SelfStockHelper.sendUpdateSelfChoiceBrodcast(CustomApplication.getContext(), mList.get(position).getStockNumber());

                                StockInfoEntity tempBean = Db_PUB_SEARCHHISTORYSTOCK.queryFromID(stockNumber);
                                if (tempBean != null) {
                                    Db_PUB_SEARCHHISTORYSTOCK.deleteFromID(stockNumber);
                                }

                                SelfChoiceStockTempData.getInstance().setSelfchoicestockTempValue(stockNumber, stockName);
                                imageitem2.setImageResource(R.mipmap.search_remove);

                                if (tag1) {
                                    CentreToast.showText(mContext,"添加自选股成功");
                                }
                            }
                        });
                        simpleRemoteControl.setCommand(new ToAddSelfChoiceStockConnect(new AddSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stockNumber, UserUtil.userId, stockName, price)));
                        simpleRemoteControl.startConnect();
                    } else {
                        mList.get(position).setStock_flag(StockTable.STOCK_OPTIONAL);
                        boolean tag1 = Db_PUB_STOCKLIST.addOneStockListData(mList.get(position));
                        SelfStockHelper.sendUpdateSelfChoiceBrodcast(CustomApplication.getContext(), mList.get(position).getStockNumber());
                        StockInfoEntity tempBean = Db_PUB_SEARCHHISTORYSTOCK.queryFromID(stockNumber);
                        if (tempBean != null) {
                            Db_PUB_SEARCHHISTORYSTOCK.deleteFromID(stockNumber);
                        }
                        SelfChoiceStockTempData.getInstance().setSelfchoicestockTempValue(stockNumber, stockName);
                        if (tag1) {
                            CentreToast.showText(mContext,"添加自选股成功");
                        }
                        imageitem2.setImageResource(R.mipmap.search_remove);
                    }
                }
            }

        });
        return convertView;
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(((Activity)mContext).getFragmentManager(),HotSearchAdapter.class.toString());
    }


}



