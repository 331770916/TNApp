package com.tpyzq.mobile.pangu.activity.trade.margin_trading;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.RevokeAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.BaseDialog;
import com.tpyzq.mobile.pangu.base.InterfaceCollection;
import com.tpyzq.mobile.pangu.data.ResultInfo;
import com.tpyzq.mobile.pangu.data.RevokeEntity;
import com.tpyzq.mobile.pangu.view.CentreToast;
import com.tpyzq.mobile.pangu.view.dialog.RevokeDialog;

import java.util.List;
import java.util.Map;

/**
 * Created by ltyhome on 14/08/2017.
 * Email: ltyhome@yahoo.com.hk
 * Describe:融资融券 撤单
 */

public class RevokeOrderActivity extends BaseActivity implements  AdapterView.OnItemClickListener,View.OnClickListener,InterfaceCollection.InterfaceCallback{
    private PullToRefreshListView mRKListView = null;
    private RevokeOrderAdapter adapter;
    private List<Map<String, String>> mList;
    private ImageView iv_isEmpty;
    @Override
    public void initView() {
        findViewById(R.id.RKpublish_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.revokTitle1)).setText("名称/代码");
        ((TextView)findViewById(R.id.revokTitle2)).setText("买卖/状态");
        ((TextView)findViewById(R.id.revokTitle3)).setText("委托量/成交量");
        ((TextView)findViewById(R.id.revokTitle4)).setText("委托价");
        mRKListView = (PullToRefreshListView) findViewById(R.id.RKListView);
        mRKListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        iv_isEmpty = (ImageView) findViewById(R.id.iv_isEmpty);
        adapter = new RevokeOrderAdapter(this);
        mRKListView.setAdapter(adapter);
        mRKListView.setEmptyView(iv_isEmpty);
        mRKListView.setOnItemClickListener(this);
        mRKListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.RKpublish_back:
                finish();
                break;
        }
    }

    @Override
    public void callResult(ResultInfo info) {
        if(info.getCode().equals("0")){
            Object object = info.getData();
            if(object!=null && object instanceof  List){
                mList = (List<Map<String,String>>)object;
                adapter.setData(mList);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RevokeOrderDialog dialog = new RevokeOrderDialog(this,mList.get(position));
        dialog.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_revok;
    }

    class RevokeOrderAdapter extends BaseAdapter {
        private List<Map<String, String>> mList;
        Context mContext;

        public RevokeOrderAdapter(Context context) {
            mContext = context;
        }

        public void setData(List<Map<String, String>> data) {
            mList = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mList != null && mList.size() > 0)
                return mList.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mList != null && mList.size() > 0)
                return mList.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler viewHodler;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_revoke_list, null);
                viewHodler = new ViewHodler();
                viewHodler.textView1 = (TextView) convertView.findViewById(R.id.tv_revoke_text1);
                viewHodler.textView2 = (TextView) convertView.findViewById(R.id.tv_revoke_text2);
                viewHodler.textView3 = (TextView) convertView.findViewById(R.id.tv_revoke_text3);
                viewHodler.textView4 = (TextView) convertView.findViewById(R.id.tv_revoke_text4);
                viewHodler.textView5 = (TextView) convertView.findViewById(R.id.tv_revoke_text5);
                viewHodler.textView6 = (TextView) convertView.findViewById(R.id.tv_revoke_text6);
                viewHodler.textView7 = (TextView) convertView.findViewById(R.id.tv_revoke_text7);
                convertView.findViewById(R.id.tv_revoke_text8).setVisibility(View.GONE);
                convertView.setTag(viewHodler);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            final Map<String,String> map = mList.get(position);
            viewHodler.textView1.setText(map.get(""));
            viewHodler.textView2.setText(map.get(""));
            viewHodler.textView3.setText(map.get(""));
            viewHodler.textView4.setText(map.get(""));
            viewHodler.textView5.setText(map.get(""));
            viewHodler.textView6.setText(map.get(""));
            viewHodler.textView7.setText(map.get(""));
            return convertView;
        }

        class ViewHodler {
            TextView textView1;
            TextView textView2;
            TextView textView3;
            TextView textView4;
            TextView textView5;
            TextView textView6;
            TextView textView7;
        }
    }

    class RevokeOrderDialog extends BaseDialog{
        private TextView tv1,tv2,tv3,tv4,tv5,tv6;
        private Map<String,String> data;
        public RevokeOrderDialog(Context context,Map<String,String> map) {
            super(context);
            this.data = map;
        }
        @Override
        public void setView() {
            findViewById(R.id.bt_false).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            findViewById(R.id.bt_true).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调用提交接口
                    mList.clear();
                    CentreToast.showText(context,"委托已提交",true);
                    dismiss();
                }
            });
            tv1 = (TextView) findViewById(R.id.dialog_text1);
            tv2 = (TextView) findViewById(R.id.dialog_text2);
            tv3 = (TextView) findViewById(R.id.dialog_text3);
            tv4 = (TextView) findViewById(R.id.dialog_text4);
            tv5 = (TextView) findViewById(R.id.dialog_text5);
            tv6 = (TextView) findViewById(R.id.dialog_text6);
        }

        @Override
        public void initData() {
            tv1.setText(data.get(""));
            tv2.setText(data.get(""));
            tv3.setText(data.get(""));
            tv4.setText(data.get(""));
            tv5.setText(data.get(""));
            tv6.setText(data.get(""));
        }

        @Override
        public int getLayoutId() {
            return R.layout.dialog_revokeorder;
        }

    }
}
