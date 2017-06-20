package com.tpyzq.mobile.pangu.activity.home.information;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.CompileTabAdapter;
import com.tpyzq.mobile.pangu.activity.home.information.adapter.CompileTabUpAdapter;
import com.tpyzq.mobile.pangu.base.BaseActivity;
import com.tpyzq.mobile.pangu.data.CompileTabEntity;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dragsortlistview.DragSortListView;
import com.tpyzq.mobile.pangu.view.listview.AutoListview;

import java.util.ArrayList;


/**
 * 资讯  编辑Tab的界面
 * 刘泽鹏
 */
public class CompileTabActivity extends BaseActivity implements View.OnClickListener {


    private TextView ivFinish;               //完成
    private AutoListview mListViewUp;        //不可滑动的自适应Listview
    private DragSortListView mListView;      //可滑动条目的Listview
    private CompileTabUpAdapter adapterUp;          //头布局ListView适配器
    private CompileTabAdapter adapter;              //可滑动Listview适配器
    private ArrayList<String> listTitle,checkedTab;             //接收title的list
    private ArrayList<CompileTabEntity> listUp;           //头布局条目
    private ArrayList<CompileTabEntity> listDown;         //下条目
    CompileTabEntity compileTabEntity;
    private ArrayList<String> fragmentList;

    @Override
    public void initView() {
        ivFinish = (TextView) this.findViewById(R.id.ivFinish);                     //完成按钮
        mListView = (DragSortListView) this.findViewById(R.id.lvCompileTab);       //列表listView
        mListViewUp = new AutoListview(this);                                       //listview头布局
        initData();
    }

    private void initData() {

        listTitle = new ArrayList<String>();
        listUp = new ArrayList<CompileTabEntity>();
        listDown = new ArrayList<CompileTabEntity>();
//        fragmentList = new ArrayList<String>();
        listTitle = getIntent().getStringArrayListExtra("listTab");
//        fragmentList = getIntent().getStringArrayListExtra("fragmentList");myTab
        checkedTab = getIntent().getStringArrayListExtra("myTab");
        //给头布局list添加数据
        for (int i = 0; i < 2; i++) {
            compileTabEntity = new CompileTabEntity();
            compileTabEntity.setBiaoTi(listTitle.get(i));
//            compileTabEntity.setFragmentName(fragmentList.get(i));
            listUp.add(compileTabEntity);
        }
        //给下部list添加数据

        for (int i = 2; i < listTitle.size(); i++) {
            compileTabEntity = new CompileTabEntity();
            compileTabEntity.setBiaoTi(listTitle.get(i));
            if(checkedTab.contains(listTitle.get(i))){
                compileTabEntity.setChecked(true);
            }else{
                compileTabEntity.setChecked(false);
            }
//            compileTabEntity.setFragmentName(fragmentList.get(i));
            listDown.add(compileTabEntity);
        }
        ivFinish.setOnClickListener(this);

        adapter = new CompileTabAdapter(this);                                      //实例化 适配器
        adapterUp = new CompileTabUpAdapter(this);                                  //实例化 头布局的适配器

        adapter.setList(listDown);                                                  //给列表添加数据
        adapterUp.setList(listUp);                                                  //给头布局添加数据

        mListViewUp.setAdapter(adapterUp);                                          //适配头布局

        mListView.addHeaderView(mListViewUp);
        mListView.setAdapter(adapter);                                              //适配
//        initStart();

        mListView.setDropListener(onDrop);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setDragEnabled(true); //设置是否可拖动。
    }

    private void initStart() {
        String zixuntab = SpUtils.getString(this, "ziXunTab", "");       //获取  选中的Tab 的名称
        String infopagerpoint = SpUtils.getString(this, "infopagerpoint", "");       //获取  选中的Tab 的名称
        if (TextUtils.isEmpty(zixuntab)) {
            return;
        }
        listDown.clear();
        String[] title = zixuntab.split(",");
        String[] fm = infopagerpoint.split(",");


        //给下部list添加数据
        for (int i = 2; i < title.length; i++) {
            compileTabEntity = new CompileTabEntity();
            compileTabEntity.setBiaoTi(title[i]);
            compileTabEntity.setChecked(true);
//            compileTabEntity.setFragmentName(fm[i]);
            listDown.add(compileTabEntity);
        }

        for (int i = 2; i < listTitle.size(); i++) {
            compileTabEntity = new CompileTabEntity();
            if (!zixuntab.contains(listTitle.get(i))) {
                compileTabEntity.setBiaoTi(listTitle.get(i));
//                compileTabEntity.setFragmentName(fragmentList.get(i));
                compileTabEntity.setChecked(false);
                listDown.add(compileTabEntity);
            }
        }
        adapter.setList(listDown);
    }

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                CompileTabEntity item = (CompileTabEntity) adapter.getItem(from);//得到listview的适配器
                adapter.remove(from);//在适配器中”原位置“的数据。
                adapter.insert(item, to);//在目标位置中插入被拖动的控件。
            }
        }
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_compile_tab;
    }

    @Override
    public void onClick(View v) {
        //点击 完成 按钮
        if (v.getId() == R.id.ivFinish) {

            StringBuilder slist = new StringBuilder();
            StringBuilder sortList = new StringBuilder();
            String pointinfo = "";
            for (int i = 0; i < listUp.size(); i++) {
                CompileTabEntity bean = listUp.get(i);
                slist.append(bean.getBiaoTi()).append(",");
//                slist += bean.getBiaoTi() + ",";
                sortList.append(bean.getBiaoTi()).append(",");
                pointinfo += bean.getFragmentName() + ",";
            }
            for (int i = 0; i < listDown.size(); i++) {
                CompileTabEntity bean = listDown.get(i);
                sortList.append(bean.getBiaoTi()).append(",");
                if (bean.isChecked()) {
                    slist.append(bean.getBiaoTi()).append(",");
                    pointinfo += bean.getFragmentName() + ",";
                }
            }


//            ToastUtils.showLong(this, slist);
            SpUtils.putString(this, "ziXunTab", slist.toString());       //存储  选中的Tab 的名称
            SpUtils.putString(this, "sortTab", sortList.toString());       //调整顺序后Tab 的名称
            Intent intent = new Intent();
            intent.putExtra("point", 1);
            setResult(RESULT_OK, intent);
            finish();       //销毁当前界面
        }
    }

}
