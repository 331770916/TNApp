package com.tpyzq.mobile.pangu.activity.market.view;

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
import com.tpyzq.mobile.pangu.db.Db_PUB_SEARCHHISTORYSTOCK;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.db.StockTable;
import com.tpyzq.mobile.pangu.http.doConnect.self.AddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.http.doConnect.self.ToAddSelfChoiceStockConnect;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.SelfStockHelper;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.tpyzq.mobile.pangu.view.CustomCenterDialog;
import com.tpyzq.mobile.pangu.view.dialog.DoSelfChoiceResultDialog;
import com.tpyzq.mobile.pangu.view.dialog.LoadingDialog;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/8/10.
 * 历史自选Adapter
 */
public class HistoryChoiceAdapter extends BaseAdapter {
    private ArrayList<StockInfoEntity> mDatas;
    private static final String TAG = "HistoryChoiceAdapter";

    private Activity mActivity;

    public HistoryChoiceAdapter(Activity activity) {
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
        ViewHolder viewHolder = null;
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

        viewHolder.stockNumber.setText(mDatas.get(position).getStockNumber().substring(2));
        viewHolder.stockName.setText(mDatas.get(position).getStockName());


        String appearHold = SpUtils.getString(CustomApplication.getContext(), ConstantUtil.APPEARHOLD, "false");
        if ((mDatas.get(position).getStock_flag() & StockTable.STOCK_HOLD) == StockTable.STOCK_HOLD  && "true".equals(appearHold)) {
            viewHolder.holdIv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.holdIv.setVisibility(View.GONE);
        }

        if (mDatas.get(position).getIsSelfChoiceStock()) {
            viewHolder.operationIv.setImageResource(R.mipmap.search_remove);
        } else {
            viewHolder.operationIv.setImageResource(R.mipmap.search_add);
        }

        viewHolder.operationIv.setOnClickListener(new HistoryClick(position, viewHolder.operationIv));
        return convertView;
    }

    class HistoryClick implements View.OnClickListener {

        private int mPosition;
        private ImageView mOperationIv;

        public HistoryClick(int position, ImageView operationIv) {
            mPosition = position;
            mOperationIv = operationIv;
        }

        @Override
        public void onClick(View v) {
            onclick(mPosition, mOperationIv);
        }
    }

    /**
     * 1.如果不是自选股 并且 从未添加过数据库，添加进数据库，添加进临时sharepreferce文件
     */
    private void onclick(final int position, final ImageView operationIv) {
        //添加数据库， 处理shared文件， 转换图片显示， 转换值状态
        final String stockNumber = mDatas.get(position).getStockNumber();
        final String stockName = mDatas.get(position).getStockName();
        String _price = "0";
        if (!TextUtils.isEmpty(mDatas.get(position).getNewPrice())) {
            _price = mDatas.get(position).getNewPrice();
        }

        final String price = _price;
        boolean isSelfChoiceStock = mDatas.get(position).getIsSelfChoiceStock();

        if (!isSelfChoiceStock) {
            BRutil.doListenAddOrRemoveSelfStock(stockNumber, stockName, BRutil.ACTIONREMOVESELFSTOCK, "0");


            if (Db_PUB_USERS.isRegister()) {
                final Dialog dialog = LoadingDialog.initDialog(mActivity, "正在加载");
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
                                CustomCenterDialog customCenterDialog = CustomCenterDialog.CustomCenterDialog("添加失败",CustomCenterDialog.SHOWCENTER);
                                customCenterDialog.show(mActivity.getFragmentManager(),HistoryChoiceAdapter.class.toString());
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mDatas.get(position).setStock_flag(StockTable.STOCK_OPTIONAL);
                        boolean tag1 = Db_PUB_STOCKLIST.addOneStockListData(mDatas.get(position));
                        if (tag1) {
                            Db_PUB_SEARCHHISTORYSTOCK.deleteFromID(stockNumber);
                            SelfStockHelper.sendUpdateSelfChoiceBrodcast(CustomApplication.getContext(), stockNumber);
                            mDatas.remove(position);
                            notifyDataSetChanged();
                            operationIv.setImageResource(R.mipmap.search_remove);
                            SelfStockHelper.explanOneTimiceAddSelfChoiceResult(mActivity, msg);
                        } else {
                            Helper.getInstance().showToast(CustomApplication.getContext(), "自选股超出50条上线，请删除再添加");
                        }
                    }
                });
                simpleRemoteControl.setCommand(new ToAddSelfChoiceStockConnect(new AddSelfChoiceStockConnect(TAG, "", UserUtil.capitalAccount, stockNumber, UserUtil.userId, stockName, price)));
                simpleRemoteControl.startConnect();

            } else {

                mDatas.get(position).setStock_flag(StockTable.STOCK_OPTIONAL);
                boolean tag1 = Db_PUB_STOCKLIST.addOneStockListData(mDatas.get(position));

                if (tag1) {
                    SelfStockHelper.sendUpdateSelfChoiceBrodcast(CustomApplication.getContext(), stockNumber);
                    mDatas.remove(position);
                    notifyDataSetChanged();
                    operationIv.setImageResource(R.mipmap.search_remove);
                    Db_PUB_SEARCHHISTORYSTOCK.deleteFromID(stockNumber);
                    DoSelfChoiceResultDialog.getInstance().singleDialog("添加自选股成功", mActivity);
                } else {
                    Helper.getInstance().showToast(CustomApplication.getContext(), "自选股超出50条上线，请删除再添加");
                }
            }

        }
    }

    class ViewHolder {
        TextView  stockName;
        TextView  stockNumber;
        ImageView holdIv;
        ImageView operationIv;
    }
}
