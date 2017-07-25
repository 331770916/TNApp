package com.tpyzq.mobile.pangu.activity.market.selfChoice.editorSelfChoiceStock;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.StockTable;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/8/10.
 * 编辑自选股适配器
 */
public class EditorSelfChoiceStockAdapter extends BaseAdapter {

    private ArrayList<StockInfoEntity> mData;
    private RemainCallback mRemainCallback;
    private UpdateEditorSelfChoiceData mUpdateEditorSelfChoiceData;

    public EditorSelfChoiceStockAdapter(RemainCallback remainCallback) {
        mRemainCallback = remainCallback;
    }

    public void setData(ArrayList<StockInfoEntity> data) {
        mData = data;
//        mAppearHold = SpUtils.getString(CustomApplication.getContext(), FileUtil.APPEARHOLD, "false");
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mData != null && mData.size() > 0) {
            return mData.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (mData != null && mData.size() > 0) {
            return mData.get(position);
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
            convertView = LayoutInflater.from(CustomApplication.getContext()).inflate(R.layout.adapter_edit_self, null);
        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.adapter_edit_checkBox);
        TextView stockName = (TextView) convertView.findViewById(R.id.adapter_edit_stockName);
        TextView stockNumber = (TextView) convertView.findViewById(R.id.adapter_edit_stockNumber);

        ImageView assignIv  = (ImageView) convertView.findViewById(R.id.adapter_edit_assign);
        ImageView remainIv  = (ImageView) convertView.findViewById(R.id.adapter_edit_remain);
        ImageView moveIv  = (ImageView) convertView.findViewById(R.id.drag_handle);

        ImageView holdIv = (ImageView) convertView.findViewById(R.id.editChoice_HoldIc);
        holdIv.setVisibility(View.GONE);

        ImageView hotIv = (ImageView) convertView.findViewById(R.id.editChoice_HotIc);
        hotIv.setVisibility(View.GONE);

        String appearHold = SpUtils.getString(CustomApplication.getContext(),ConstantUtil.APPEARHOLD, ConstantUtil.HOLD_DISAPPEAR);
        if ((mData.get(position).getStock_flag()& StockTable.STOCK_HOLD)==StockTable.STOCK_HOLD && "true".equals(appearHold)) {
            holdIv.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(mData.get(position).getHot()) && "1".equals(mData.get(position).getHot())) {
            hotIv.setVisibility(View.VISIBLE);
        } else {
            hotIv.setVisibility(View.GONE);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mData.get(position).setIsChecked(true);
                } else {
                    mData.get(position).setIsChecked(false);
                }

                if (mUpdateEditorSelfChoiceData != null) {
                    mUpdateEditorSelfChoiceData.updateDatas(mData);
                }
            }
        });

        assignIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTop(position);
            }
        });

        remainIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemainCallback.remainClickListener(mData.get(position));
            }
        });

        /*CheckBox监听事件必须放在setChecked之前，否则后果自负*/
        checkBox.setChecked(mData.get(position).getIsChecked());
        stockName.setText(mData.get(position).getStockName());

        stockNumber.setText(Helper.getStockNumber(mData.get(position).getStockNumber()));


        return convertView;
    }

    private void setTop(int position) {
        mData.add(0, mData.get(position));
        mData.remove(position + 1);
        notifyDataSetChanged();
    }

    public void remove(int position) {//删除指定位置的item

        mData.remove(position);
        notifyDataSetChanged();
    }


    public void insert(StockInfoEntity item, int i) {//在指定位置插入item
        mData.add(i, item);
        notifyDataSetChanged();
    }


    public void setDatasListener(UpdateEditorSelfChoiceData updateCollectionData) {
        mUpdateEditorSelfChoiceData = updateCollectionData;
    }

    public interface UpdateEditorSelfChoiceData {
        void updateDatas(ArrayList<StockInfoEntity> datas);
    }

    public interface RemainCallback {
        void remainClickListener(StockInfoEntity entity);
    }
}
