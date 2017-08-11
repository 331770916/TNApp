package com.tpyzq.mobile.pangu.adapter.market;

import android.app.Activity;
import android.app.Dialog;
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

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/12/1.
 * 最近浏览过的股票的Adapter
 */

public class LimitLookStockAdapter extends BaseAdapter {

    private ArrayList<StockInfoEntity> mDatas;
    private Activity mActivity;
    private static final String TAG ="SearchActivity";

    public LimitLookStockAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setDatas(ArrayList<StockInfoEntity> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder ;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.search_item, null);
            viewHolder.stockName = (TextView) convertView.findViewById(R.id.search_item_tv1);
            viewHolder.stockNumber = (TextView) convertView.findViewById(R.id.search_item_tv2);
            viewHolder.holdIv = (ImageView) convertView.findViewById(R.id.search_item_iv2);
            viewHolder.operationIv = (ImageView) convertView.findViewById(R.id.search_item_iv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.holdIv.setVisibility(View.GONE);

        String _stockNumber = mDatas.get(position).getStockNumber();

        if (!TextUtils.isEmpty(_stockNumber)) {
            viewHolder.stockNumber.setText(_stockNumber.substring(2, _stockNumber.length()));
        }

        if (!TextUtils.isEmpty(mDatas.get(position).getStockName())) {
            viewHolder.stockName.setText(mDatas.get(position).getStockName());
        }

        String appearHold = SpUtils.getString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, "false");
        if ((mDatas.get(position).getStock_flag() & StockTable.STOCK_HOLD) == StockTable.STOCK_HOLD && "true".equals(appearHold)) {
            viewHolder.holdIv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.holdIv.setVisibility(View.GONE);
        }

        if (mDatas.get(position).getIsSelfChoiceStock()) {
            viewHolder.operationIv.setImageResource(R.mipmap.search_remove);
        } else {
            viewHolder.operationIv.setImageResource(R.mipmap.search_add);
        }

        /**
         * 1.如果不是自选股 并且 从未添加过数据库，添加进数据库，添加进临时sharepreferce文件
         * 2.如果是自选股 点击删除 时 真删除，并且在历史浏览表中添加一条数据
         * 3.如果是历史自选股 ，点击增加自选股， 删除历史自选股该条数据， 在自选股列表添加一条自选股
         */
        viewHolder.operationIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加数据库， 处理shared文件， 转换图片显示， 转换值状态
                final String stockNumber = mDatas.get(position).getStockNumber();
                final String stockName = mDatas.get(position).getStockName();
                final String price = mDatas.get(position).getNewPrice();

                boolean isSelfChoiceStock = mDatas.get(position).getIsSelfChoiceStock();

                if (isSelfChoiceStock) {

                    if (Db_PUB_USERS.isRegister()) {

                        BRutil.doListenAddOrRemoveSelfStock(stockNumber, stockName, BRutil.ACTIONREMOVESELFSTOCK, price);

                        final Dialog dialog = LoadingDialog.initDialog(mActivity, "正在加载");
                        dialog.show();

                        //同步网络 从云端删除刚才删除的自选股
                        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(new ICallbackResult() {
                            @Override
                            public void getResult(Object result, String tag) {

                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                String msg = (String) result;
                                try {
                                    JSONObject jsonObject = new JSONObject(msg);
                                    String _msg = jsonObject.getString("msg");
                                    String code = jsonObject.getString("code");
                                    String totalcount = jsonObject.getString("totalcount");

                                    if ("0".equals(totalcount)) {
                                        showDialog("totalCount:" + totalcount + ",服务器无该数据");
                                    } else if("0".equals(code)) {
                                        boolean tag1 = Db_PUB_STOCKLIST.deleteStockFromID(stockNumber);
                                        if(tag1){
                                            Db_HOME_INFO.deleteOneSelfNewsData(stockNumber);
                                            mDatas.get(position).setSelfChoicStock(false);
                                            mDatas.get(position).setStock_flag(StockTable.STOCK_HISTORY_OPTIONAL);
                                            Db_PUB_SEARCHHISTORYSTOCK.addOneData(mDatas.get(position));
                                            SelfChoiceStockTempData.getInstance().removeSelfchoicestockTempValue(stockNumber);
                                            viewHolder.operationIv.setImageResource(R.mipmap.search_add);
                                            CentreToast.showText(mActivity,"删除自选股成功");
                                        }
                                    } else {
                                        showDialog("删除自选股失败");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        simpleRemoteControl.setCommand(new ToDelteSlefChoiceStockConnect(new DeleteSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stockNumber, UserUtil.userId)));
                        simpleRemoteControl.startConnect();
                    } else {
                        boolean tag1 = Db_PUB_STOCKLIST.deleteStockFromID(stockNumber);
                        if (tag1) {
                            Db_HOME_INFO.deleteOneSelfNewsData(stockNumber);
                            mDatas.get(position).setSelfChoicStock(false);
                            mDatas.get(position).setStock_flag(StockTable.STOCK_HISTORY_OPTIONAL);
                            Db_PUB_SEARCHHISTORYSTOCK.addOneData(mDatas.get(position));
                            SelfChoiceStockTempData.getInstance().removeSelfchoicestockTempValue(stockNumber);
                            viewHolder.operationIv.setImageResource(R.mipmap.search_add);
                            CentreToast.showText(mActivity,"删除自选股成功");
                        }
                    }


                } else {
                    BRutil.doListenAddOrRemoveSelfStock(stockNumber, stockName, BRutil.ACTIONREMOVESELFSTOCK, price);
                    mDatas.get(position).setSelfChoicStock(true);
                    if (Db_PUB_USERS.isRegister()) {
                        //同步网络 从云端增加刚才增加的自选股
                        final Dialog dialog = LoadingDialog.initDialog(mActivity, "正同步到云端");
                        dialog.show();

                        SimpleRemoteControl simpleRemoteControl = new SimpleRemoteControl(new ICallbackResult() {
                            @Override
                            public void getResult(Object result, String tag) {

                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                String msg = (String) result;
                                try {
                                    JSONObject jsonObject = new JSONObject(msg);
                                    String code = jsonObject.getString("code");
                                    if (!"0".equals(code)) {
                                        showDialog("添加失败");
                                        return;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mDatas.get(position).setStock_flag(StockTable.STOCK_OPTIONAL);
                                boolean tag1 = Db_PUB_STOCKLIST.addOneStockListData(mDatas.get(position));
                                if (tag1) {
                                    SelfStockHelper.sendUpdateSelfChoiceBrodcast(CustomApplication.getContext(), mDatas.get(position).getStockNumber());
                                    StockInfoEntity tempBean = Db_PUB_SEARCHHISTORYSTOCK.queryFromID(stockNumber);
                                    if (tempBean != null) {
                                        Db_PUB_SEARCHHISTORYSTOCK.deleteFromID(stockNumber);
                                    }
                                    mDatas.get(position).setSelfChoicStock(true);
                                    SelfChoiceStockTempData.getInstance().setSelfchoicestockTempValue(stockNumber, stockName);
                                    viewHolder.operationIv.setImageResource(R.mipmap.search_remove);
                                } else {
                                    CentreToast.showText(CustomApplication.getContext(), "自选股超出50条上线，请删除再添加");
                                }
                            }
                        });

                        simpleRemoteControl.setCommand(new ToAddSelfChoiceStockConnect(new AddSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stockNumber, UserUtil.userId, stockName, price)));
                        simpleRemoteControl.startConnect();
                    } else {
                        mDatas.get(position).setStock_flag(StockTable.STOCK_OPTIONAL);
                        boolean tag1 = Db_PUB_STOCKLIST.addOneStockListData(mDatas.get(position));
                        if (tag1) {
                            SelfStockHelper.sendUpdateSelfChoiceBrodcast(CustomApplication.getContext(), mDatas.get(position).getStockNumber());
                            StockInfoEntity tempBean = Db_PUB_SEARCHHISTORYSTOCK.queryFromID(stockNumber);
                            if (tempBean != null) {
                                Db_PUB_SEARCHHISTORYSTOCK.deleteFromID(stockNumber);
                            }
                            SelfChoiceStockTempData.getInstance().setSelfchoicestockTempValue(stockNumber, stockName);
                            mDatas.get(position).setSelfChoicStock(true);
                            viewHolder.operationIv.setImageResource(R.mipmap.search_remove);
                            CentreToast.showText(mActivity,"添加自选股成功");
                        }else {
                            CentreToast.showText(CustomApplication.getContext(), "自选股超出50条上线，请删除再添加");
                        }
                    }
                }
            }
        });
        return convertView;
    }

    private void showDialog(String msg){
        CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog(msg,CustomCenterDialog.SHOWCENTER);
        customCenterDialog.show(mActivity.getFragmentManager(),LimitLookStockAdapter.class.toString());
    }

    private class ViewHolder{
        public TextView stockName ;
        public TextView stockNumber ;
        public ImageView holdIv ;
        public ImageView operationIv ;
    }

}
