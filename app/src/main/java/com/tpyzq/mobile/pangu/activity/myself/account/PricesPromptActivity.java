package com.tpyzq.mobile.pangu.activity.myself.account;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.myself.InformAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.InformEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by wangqi on 2016/10/13.
 * 行情预警
 */
public class PricesPromptActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static String TAG = "PricesPrompt";
    private PullToRefreshListView mListView;
    private ImageView item_image;
    private RelativeLayout it_relativeLayout;
    private InformAdapter adapter;
    private TextView item_TextView, publish_title;
    private List<InformEntity> list;
    private ImageView iv_isEmpty;
    private int i = 0;
    int refresh = 10;

    @Override
    public void initView() {
        SpUtils.putString(this, "mDivnum", "true");
        SpUtils.putString(this, "mDivnum_PricesPrompt", "true");

        list = new ArrayList<InformEntity>();
        findViewById(R.id.ASpublish_back).setOnClickListener(this);
        item_image = (ImageView) findViewById(R.id.item_image);

        it_relativeLayout = (RelativeLayout) findViewById(R.id.it_RelativeLayout);
        mListView = (PullToRefreshListView) findViewById(R.id.mListView);
        item_TextView = (TextView) findViewById(R.id.item_TextView);
        publish_title = (TextView) findViewById(R.id.publish_title);
        iv_isEmpty = (ImageView) findViewById(R.id.iv_isEmpty);
        initData();
        setInfoNum(String.valueOf(i), false);
        getData();
    }

    private void getData() {
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mListView.isShownHeader()) {
                    //设置头布局样式文字
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mListView.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1500);
                                i = 1;
                                setInfoNum(String.valueOf(i), false);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            //将下拉视图收起
                            mListView.onRefreshComplete();
                        }
                    }.execute();
                } else if (mListView.isShownFooter()) {
                    //设置尾布局样式文字
                    mListView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    mListView.getLoadingLayoutProxy().setPullLabel("上拉加载数据");
                    mListView.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                Thread.sleep(1500);
                                i++;
                                setInfoNum(String.valueOf(i), true);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            //将下拉视图收起
                            mListView.onRefreshComplete();
                        }
                    }.execute();
                }

            }
        });
    }

    private void setInfoNum(String initial, final boolean isState) {
        HashMap map = new HashMap();
        HashMap map1 = new HashMap();
        HashMap map2=new HashMap();
        map.put("funcid", "800123");
        map.put("token", "");
        map.put("parms", map1);

        map1.put("ACCOUNT", UserUtil.Mobile);
        map1.put("pageIndex", initial);
        map1.put("pageSize", "10");
        map1.put("OBJECTIVE", map2);

        map2.put("1", "");
        map2.put("2", "");
        map2.put("3", "");
        map2.put("4", "");
        map2.put("5", "");

        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.getURL_NEW(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.e("", e.toString());
                Helper.getInstance().showToast(PricesPromptActivity.this, "网络异常");
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String count_one = jsonObject.getString("count_one");                  //到价提醒未读条数
                    JSONArray data = jsonObject.getJSONArray("data");
                    JSONArray list_2 = null;
                    JSONArray list_3 = null;
                    JSONArray list_4 = null;
                    for (int i = 0; i < data.length(); i++) {
                        list_2 = data.optJSONArray(0);
                        list_3 = data.optJSONArray(3);
                        list_4 = data.optJSONArray(4);

                        if (!isState) {
                            list.clear();
                        }
                    }
                    //通知公告
                    if (list_2 != null && list_2.length() > 0) {
                        if (isState) {
                            refresh += 10;
                        }
                        for (int j = 0; j < list_2.length(); j++) {
                            InformEntity bean = new InformEntity();
                            bean.setTitle(list_2.getJSONObject(j).getString("TITLE"));
                            bean.setPush_time(list_2.getJSONObject(j).getString("PUSH_TIME"));
                            bean.setContene(list_2.getJSONObject(j).getString("CONTENE"));
                            bean.setBizid(list_2.getJSONObject(j).getString("BIZID"));
                            list.add(bean);
                        }
                    }
                    //通知公告
                    if (list_3 != null && list_3.length() > 0) {
                        if (isState) {
                            refresh += 10;
                        }
                        for (int j = 0; j < list_3.length(); j++) {
                            InformEntity bean = new InformEntity();
                            bean.setTitle(list_3.getJSONObject(j).getString("TITLE"));
                            bean.setPush_time(list_3.getJSONObject(j).getString("PUSH_TIME"));
                            bean.setContene(list_3.getJSONObject(j).getString("CONTENE"));
                            bean.setBizid(list_3.getJSONObject(j).getString("BIZID"));
                            list.add(bean);
                        }

                    }
                    //通知公告
                    if (list_4 != null && list_4.length() > 0) {
                        if (isState) {
                            refresh += 10;
                        }
                        for (int j = 0; j < list_4.length(); j++) {
                            InformEntity bean = new InformEntity();
                            bean.setTitle(list_4.getJSONObject(j).getString("TITLE"));
                            bean.setPush_time(list_4.getJSONObject(j).getString("PUSH_TIME"));
                            bean.setContene(list_4.getJSONObject(j).getString("CONTENE"));
                            bean.setBizid(list_4.getJSONObject(j).getString("BIZID"));
                            list.add(bean);
                        }

                    }
                    adapter.setData(list);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initData() {
        publish_title.setText(getString(R.string.MyInformationText3));
        item_image.setImageResource(R.mipmap.hangqingyujing_small);
        adapter = new InformAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(iv_isEmpty);
        mListView.setOnItemClickListener(this);
        String token_Warning = getIntent().getStringExtra("token_Warning");
        if (!TextUtils.isEmpty(token_Warning)) {
            item_TextView.setText("还有" + token_Warning + "条未读消息");
            show(it_relativeLayout);
        } else {
            item_TextView.setText("还有0条未读消息");
            show(it_relativeLayout);
        }
    }

    private void show(final View v) {
        v.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            v.setVisibility(View.GONE);
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_information;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ASpublish_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AnnouncementActivity.class);
        intent.putExtra("BIZID", list.get(position - 1).getBizid().toString());
        intent.putExtra("Marked","true");
        startActivity(intent);
    }
}
