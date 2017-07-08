package com.tpyzq.mobile.pangu.activity.myself.handhall.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.handhall.ChooseRelationshipActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.StartyUpBoardActivity;
import com.tpyzq.mobile.pangu.base.BaseFragment;
import com.tpyzq.mobile.pangu.data.SecondContactsEntity;
import com.tpyzq.mobile.pangu.http.doConnect.self.StartUpBoardActivityPresenter;


/**
 * anthor:Created by tianchen on 2017/3/21.
 * email:963181974@qq.com
 */

public class LXRFragment extends BaseFragment implements View.OnClickListener {
    private StartUpBoardActivityPresenter presenter;
    private Button bt_next;
    private EditText et_name;
    private EditText et_phone;
    private TextView tv_relationship;
    private SecondContactsEntity secondContacts;
    private int point;

    public static LXRFragment newInstance(StartUpBoardActivityPresenter presenter) {
        LXRFragment lxrFragment = new LXRFragment();
        lxrFragment.setPresenter(presenter);
        return lxrFragment;
    }

    private void setPresenter(StartUpBoardActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView(View view) {
        bt_next = (Button) view.findViewById(R.id.bt_next);
        et_name = (EditText) view.findViewById(R.id.tv_name);
        et_phone = (EditText) view.findViewById(R.id.tv_phone);
        tv_relationship = (TextView) view.findViewById(R.id.tv_relationship);
        initData();
    }

    private void initData() {
        bt_next.setOnClickListener(this);
        tv_relationship.setOnClickListener(this);
        presenter.getSecondContacts(new StartyUpBoardActivity.SecondInfo() {
            @Override
            public void getSecondContacts(SecondContactsEntity secondContacts) {
                secondContacts = presenter.getActivity().getSecondContacts();
                if (secondContacts == null) {
                    return;
                }
                et_name.setText(secondContacts.SECOND_NAME);
                et_phone.setText(secondContacts.SECOND_MOBILE);
                String RELATIONSHIP = secondContacts.RELATIONSHIP;
                if (TextUtils.isEmpty(RELATIONSHIP)) {
                    return;
                }
                tv_relationship.setText(getRelationShip(Integer.parseInt(RELATIONSHIP)));
            }
        });
    }

    private String getRelationShip(int i) {
        String relationship = "";
        switch (i) {
            case 1:
                point = 0;
                relationship = "父母";
                break;
            case 2:
                point = 1;
                relationship = "夫妻";
                break;
            case 3:
                point = 2;
                relationship = "子女";
                break;
            case 4:
                point = 3;
                relationship = "朋友";
                break;
            case 5:
                point = 4;
                relationship = "其他";
                break;
        }
        return relationship;
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_lxr;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500 && resultCode == 100) {
            int position = data.getIntExtra("point", 0);
            tv_relationship.setText(getRelationShip(position + 1));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                String name = et_name.getText().toString();
                String phone = et_phone.getText().toString();
                presenter.setSecondContacts(name, phone, point + 1);
                break;
            case R.id.tv_relationship:
                Intent intent = new Intent();
                intent.setClass(presenter.getActivity(), ChooseRelationshipActivity.class);
                intent.putExtra("point", point);
                startActivityForResult(intent, 500);
                break;
        }
    }
}
