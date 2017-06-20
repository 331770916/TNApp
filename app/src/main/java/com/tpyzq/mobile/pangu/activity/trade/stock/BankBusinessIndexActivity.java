package com.tpyzq.mobile.pangu.activity.trade.stock;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.trade.BuinessIndexAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;

import java.util.ArrayList;

/**
 * Created by zhangwenbo on 2016/8/22.
 * 银证转账业务首页
 */
public class BankBusinessIndexActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ArrayList<BuinessBankIndex> mDatas;
    private ArrayList<String> mTitles = new ArrayList<>();

    @Override
    public void initView() {
        mDatas = new ArrayList<>();

        findViewById(R.id.banksIndex_back).setOnClickListener(this);
        ListView listView = (ListView) findViewById(R.id.bankIndeListview);
        BuinessIndexAdapter adapter = new BuinessIndexAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        int [] imgResource1 = {R.mipmap.buiness_a,
                R.mipmap.buiness_b,
                R.mipmap.buiness_c};

        int [] imgResource2 = {
                R.mipmap.buiness_d,
                R.mipmap.buiness_e,
                R.mipmap.buiness_f,
                R.mipmap.buiness_g,};

        String [] titles = {"0银行转证券", "1证券转银行", "2转账流水查询"};

        String [] titles2 = {"3多银行余额查询", "4资金调拨", "5资金归集", "6调拨记录查询"};

        String [] discribs = {"交易第一步，从银行卡转入资金",
                "取出资金到银行卡",
                "资金动向一览" };

        String [] discribs2 = {
                "个人多银行卡余额查询",
                "主辅资金账户资金划转",
                "多个辅助资金账户资金一键转入主资金账户",
                "主辅资金账户间资金变动一览"
        };

        ArrayList<BuinessBankIndex> beans = new ArrayList<>();
        ArrayList<BuinessBankIndex> beans1 = new ArrayList<>();

        for (int i = 0; i < imgResource1.length; i++) {
            BuinessBankIndex bean = new BuinessBankIndex();
            bean.setDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), imgResource1[i]));
            bean.setTitle(titles[i]);
            mTitles.add(titles[i]);
            bean.setDisribtion(discribs[i]);
            bean.setType("1");
            beans.add(bean);
        }

        for (int i = 0; i < imgResource2.length; i++) {
            BuinessBankIndex bean = new BuinessBankIndex();
            bean.setDrawable(ContextCompat.getDrawable(CustomApplication.getContext(), imgResource2[i]));
            bean.setTitle(titles2[i]);
            bean.setDisribtion(discribs2[i]);
            bean.setType("1");
            beans1.add(bean);
        }

        BuinessBankIndex bean1 = new BuinessBankIndex();
        bean1.setTitle("银证转账");
        bean1.setType("0");

        BuinessBankIndex bean2 = new BuinessBankIndex();
        bean2.setTitle("多银行业务");
        bean2.setType("0");

        mDatas.add(bean1);
        mDatas.addAll(beans);
        mDatas.add(bean2);
        mDatas.addAll(beans1);

        adapter.setDatas(mDatas);

    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent();
        String flag = mDatas.get(position).getTitle().substring(0,1);

       if ("0".equals(flag)) {

           intent.putExtra("tag", "100");
           intent.setClass(BankBusinessIndexActivity.this, BanksTransferAccountsActivity.class);
           startActivity(intent);

       } else if ("1".equals(flag)) {

           intent.putExtra("tag", "200");
           intent.setClass(BankBusinessIndexActivity.this, BanksTransferAccountsActivity.class);
           startActivity(intent);

       } else if ("2".equals(flag)) {
           intent.putExtra("tag", "300");
           intent.setClass(BankBusinessIndexActivity.this, BanksTransferAccountsActivity.class);
           startActivity(intent);

       } else if ("3".equals(flag)) {
           intent.setClass(BankBusinessIndexActivity.this, BanksBalanceQueryActivity.class);
           startActivity(intent);
       } else if ("4".equals(flag)) {
           intent.setClass(BankBusinessIndexActivity.this, PriceAllotActivity.class);
           startActivity(intent);
       } else if ("5".equals(flag)) {
           intent.setClass(BankBusinessIndexActivity.this, PriceCollectionActivity.class);
           startActivity(intent);
       } else if ("6".equals(flag)) {
           intent.setClass(BankBusinessIndexActivity.this, AllotQueryActivity.class);
           startActivity(intent);
       }


    }

    @Override
    public int getLayoutId() {
        return R.layout.business_index;
    }

    public class BuinessBankIndex {
        private Drawable drawable;      //图片
        private String title;           //标题
        private String disribtion;      //说明
        private String type;            //对应的布局类型

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDisribtion() {
            return disribtion;
        }

        public void setDisribtion(String disribtion) {
            this.disribtion = disribtion;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
