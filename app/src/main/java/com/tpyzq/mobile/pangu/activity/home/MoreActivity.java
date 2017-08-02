package com.tpyzq.mobile.pangu.activity.home;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.adapter.home.MoreAdapter;
import com.tpyzq.mobile.pangu.adapter.home.MoreUpAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.FunctionEntity;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dragsortlistview.DragSortListView;
import com.tpyzq.mobile.pangu.view.listview.AutoListview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangwenbo on 2016/10/19.
 * 首页中的   全部编辑界面
 */
public class MoreActivity extends BaseActivity implements View.OnClickListener {

    private TextView publish_ok;                            //保存按钮
    private FunctionEntity functionBean;                      //存储数据用的实体类
    private ArrayList<FunctionEntity> listUp;                 //上边 listView 的数据集合
    private ArrayList<FunctionEntity> listDown;               //下班 listView 的数据集合
    private MoreUpAdapter adapterUp;                       //上边 listView 的适配器
    private MoreAdapter adapterDown;                       //下边 listView 的适配器

    @Override
    public void initView() {
        findViewById(R.id.more_back).setOnClickListener(this);
        publish_ok = (TextView) findViewById(R.id.publish_ok);
        publish_ok.setOnClickListener(this);
        listUp = new ArrayList<FunctionEntity>();
        listDown = new ArrayList<FunctionEntity>();

        AutoListview listViewUp = new AutoListview(this);                                   //上边的 listView
        listViewUp.setDivider(null);
        adapterUp = new MoreUpAdapter(this);
        listViewUp.setAdapter(adapterUp);                //适配 上面的  ListView

        DragSortListView listViewDown = (DragSortListView) this.findViewById(R.id.lvMoreTab);  //下边的  listView
        listViewDown.setDivider(null);
        listViewDown.addHeaderView(listViewUp);        //添加头布局
        listViewDown.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewDown.setDragEnabled(true);                 //设置是否可拖动。
        adapterDown = new MoreAdapter(this);
        listViewDown.setAdapter(adapterDown);           //适配 下面的  ListView
        listViewDown.setDropListener(onDrop);              //添加删减条目

        initData();
    }

    private void initData() {
        //初始化上面listview
        Map<String, Integer> upDataMap =  getUpDataMap();
        Set<String> set = upDataMap.keySet();
        Iterator<String> iterator = set.iterator();

        while (iterator.hasNext()) {
            FunctionEntity functionBean = new FunctionEntity();
            String key = iterator.next();
            functionBean.setTitle(key);
            functionBean.setIconId(upDataMap.get(key));
            listUp.add(functionBean);
        }
        //初始化下面listview
        listDown = SpUtils.getDataList(this,"sortTitleTab");
        //图片硬编码bug修复
        for (FunctionEntity entity:listDown) {
            String title = entity.getTitle();
            Map<String, Integer> downDataMap = getDownDataMap();
            entity.setIconId(downDataMap.get(title));
        }

        String homeTitleTab = SpUtils.getString(this, "homeTitleTab", "");       //获取  选中的Tab 的名称
        String[] title = null;
        if (!TextUtils.isEmpty(homeTitleTab)) {
            title = homeTitleTab.split(",");
        }
        if(null == listDown ||listDown.size()==0){
            Map<String, Integer> downDataMap =  getDownDataMap();
            Set<String> set2 = downDataMap.keySet();
            Iterator<String> iterator2 = set2.iterator();
            while (iterator2.hasNext()) {
                FunctionEntity _functionBean = new FunctionEntity();
                String key2 = iterator2.next();
                _functionBean.setTitle(key2);
                _functionBean.setIconId(downDataMap.get(key2));
                _functionBean.setChild(false);
                if (title != null && title.length > 0) {
                    for (String _key : title) {
                        if (_key.equals(key2)) {
                            _functionBean.setChild(true);
                        }
                    }
                }
                listDown.add(_functionBean);
            }
        }else if(null != title){
            int point = title.length;
            adapterDown.setPoint(point);
        }
        adapterUp.setList(listUp);                          //塞数据
        adapterDown.setList(listDown);                     //塞数据
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_back:        //返回按钮
                finish();
                break;
            case R.id.publish_ok:       //保存按钮
                List<FunctionEntity> sortTitleList = listDown;
                StringBuilder checkedTitleList = new StringBuilder();
                for (int i = 0; i < listDown.size(); i++) {
                    FunctionEntity functionBean = listDown.get(i);
                    if(functionBean.isChild()){
                        checkedTitleList.append(functionBean.getTitle()).append(",");
                    }
                }
                SpUtils.putString(this, "homeTitleTab", checkedTitleList.toString());       //存储  选中的Tab 的名称
                SpUtils.setDataList(this, "sortTitleTab", sortTitleList);
                finish();
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_more;
    }

    /**
     * 获取数据键值对
     * @return
     */
    private Map<String, Integer> getUpDataMap() {
        Map<String, Integer> map = new LinkedHashMap<String,Integer>();            //存储  title 和 icon  的 map
//        map.put("我的钱包",R.mipmap.sy_wodeqianbao);
        map.put("我的账户",R.mipmap.sy_wodezhanghu);
//        map.put("金融生活",R.mipmap.sy_jinrongshenghuo);
        map.put("自选股",R.mipmap.sy_zixuangu);
        map.put("搜索股票",R.mipmap.sy_sousuogupiao);
        map.put("新股",R.mipmap.sy_xingu);
        map.put("资产分析",R.mipmap.sy_zichanfenxi);
        map.put("稳赢理财", R.mipmap.sy_wenying);
        return map;
    }

    private Map<String, Integer> getDownDataMap(){
        Map<String, Integer> map = new LinkedHashMap<String,Integer>();
        map.put("热搜股票",R.mipmap.sy_jinriregu);
        map.put("资讯",R.mipmap.sy_zixun);
        map.put("交易动态",R.mipmap.sy_jiaoyidongtai);
        map.put("股市回忆录",R.mipmap.sy_gushihuiyilu);
        map.put("股票买入",R.mipmap.sybj_gupiaomairu);
        map.put("股票卖出",R.mipmap.sybj_gupiaomaichu);
        map.put("股票持仓",R.mipmap.sy_gupiaochicang);
        map.put("OTC持仓",R.mipmap.sy_otcchicang);
        map.put("可取资金",R.mipmap.sy_keyongzijin);
        map.put("银行转账",R.mipmap.sy_yinhangzhuanzhang);
        map.put("业务办理",R.mipmap.sy_yewubanli);
        map.put("自选股新闻",R.mipmap.sy_zixuanguxinwen);
        map.put("基金持仓",R.mipmap.sy_jijinchicang);
        map.put("我的消息",R.mipmap.sy_wodexiaoxi);
        map.put("开户", R.mipmap.open_user);
//        map.put("热销理财",R.mipmap.sy_rexiaolicai);
//        map.put("智选理财",R.mipmap.sy_zhixuanlicai);
        map.put("股市月账单",R.mipmap.sy_gushiyuezhangdan);
        map.put("国债逆回购", R.mipmap.nihuigou);
//        map.put("牛掌柜", R.mipmap.nihuigou);
        return map;
    }

    //listView  实现挪动个的方法
    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                FunctionEntity item = (FunctionEntity) adapterDown.getItem(from);//得到listview的适配器
                adapterDown.remove(from);//在适配器中”原位置“的数据。
                adapterDown.insert(item, to);//在目标位置中插入被拖动的控件。
            }
        }
    };
}
