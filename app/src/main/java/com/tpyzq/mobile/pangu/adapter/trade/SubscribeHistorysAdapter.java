package com.tpyzq.mobile.pangu.adapter.trade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.data.SubscribeHistoryFlowBean;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.view.dialog.ResultDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 刘泽鹏 on 2016/8/10.
 */
public class SubscribeHistorysAdapter extends BaseAdapter {
    private static final String TAG = "SubHistorysAdapter";
    private Context context;

    private ArrayList<HashMap<String, String>> list;
    private ArrayList<String> statelist;
    private int currentItem = -1; //用于记录点击的 Item 的 position，是控制 item 展开的核心

    private boolean isVisible;  //用于记录隐藏布局的可见性


    public SubscribeHistorysAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setList(ArrayList<HashMap<String, String>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setStateList(ArrayList<String> statelist) {
        this.statelist = statelist;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && list.size() > 0) {
            list.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_subscribe_history, parent, false);
            holder = new ViewHolder();
            holder.showArea = (LinearLayout) convertView.findViewById(R.id.layout_showArea);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            holder.textView_num = (TextView) convertView.findViewById(R.id.textView_num);
            holder.textViewS_data = (TextView) convertView.findViewById(R.id.textViewS_data);
            holder.textViewS_price = (TextView) convertView.findViewById(R.id.textViewS_price);
            holder.textViewS_num = (TextView) convertView.findViewById(R.id.textViewS_num);
            holder.textViewS_num2 = (TextView) convertView.findViewById(R.id.textViewS_num2);
            holder.textView_time1 = (TextView) convertView.findViewById(R.id.textView_time1);
            holder.textView_time2 = (TextView) convertView.findViewById(R.id.textView_time2);
            holder.textView_time3 = (TextView) convertView.findViewById(R.id.textView_time3);
            holder.textView_time4 = (TextView) convertView.findViewById(R.id.textView_time4);
            holder.tvZhongQian = (TextView) convertView.findViewById(R.id.tvZhongQian);
            holder.tvPaiHaoShuLiangNum = (TextView) convertView.findViewById(R.id.tvPaiHaoShuLiangNum);
            holder.tvQiShiHao = (TextView) convertView.findViewById(R.id.tvQiShiHao);
            holder.textView3 = (TextView) convertView.findViewById(R.id.textView3);
            holder.tvZhongQianLv = (TextView) convertView.findViewById(R.id.tvZhongQianLv);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView5);

            holder.hideArea = (RelativeLayout) convertView.findViewById(R.id.layout_hideArea);
            holder.anniu = (RelativeLayout) convertView.findViewById(R.id.anniu);
            holder.textView32 = (TextView) convertView.findViewById(R.id.textView32);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        final HashMap<String, String> map = list.get(position);

        // 注意：我们在此给响应点击事件的区域（我的例子里是 showArea 的线性布局）添加Tag，
        // 为了记录点击的 position，我们正好用 position 设置 Tag
        holder.showArea.setTag(position);
        holder.anniu.setTag(position);
        holder.tvZhongQianLv.setTag(position);
        // holder.textView_num.setText(item.get("textView_num"));
        //第一个textView_num为viewholder类的属性  第二个为数据源中集合的键名
        holder.textViewName.setText(map.get("stock_name"));                    //股票名称
        holder.textView_num.setText(map.get("stock_code"));                    //股票代码
        if (!TextUtils.isEmpty(map.get("entrust_date"))) {
            holder.textViewS_data.setText(Helper.getMyDateY_M_D(map.get("entrust_date")));               //申购日期
        }
        holder.textViewS_price.setText(map.get("entrust_price"));              //申购价格
        String entrust_amount = map.get("entrust_amount");
        if (!TextUtils.isEmpty(entrust_amount)) {
            int i = entrust_amount.indexOf(".");
            String substring = entrust_amount.substring(0, i);
            holder.textViewS_num.setText(substring);                            //申购数量
        }
        String occur_amount = map.get("occur_amount");                          //中签数量
        if (!TextUtils.isEmpty(occur_amount)) {
            int a = occur_amount.indexOf(".");
            if (a >= 0) {
                String substring = occur_amount.substring(0, occur_amount.indexOf("."));
                holder.textViewS_num2.setText(substring);                            //中签数
                holder.tvZhongQian.setText("中签数量:" + substring);                   //隐藏部分的中签数量
            } else {
                holder.textViewS_num2.setText(occur_amount);                            //中签数
                holder.tvZhongQian.setText("中签数量:" + occur_amount);                   //隐藏部分的中签数量
            }
        }

        String entrust_date = map.get("entrust_date");
        if (!TextUtils.isEmpty(entrust_date)) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM.dd");
            try {
                Date parse = sdf1.parse(entrust_date);
                String format = sdf2.format(parse);
                holder.textView_time1.setText(format);                          //时间1
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


//        String status = map.get("status");


        if (statelist != null && statelist.size() > 0) {
            if ("".equals(statelist.get(position))) {
                holder.textView3.setText(statelist.get(position));        //状态
                holder.textView3.setBackgroundResource(R.drawable.lonin_white);  //设置背景
//            holder.textView3.setVisibility(View.GONE);      //隐藏控件
            } else {
                holder.textView3.setText(statelist.get(position));        //状态
                holder.textView3.setBackgroundResource(R.drawable.lonin2);  //设置背景
            }
            if (statelist.get(position).equals("已上市")) {
                holder.textView32.setText("    已上市");
            }else if (statelist.get(position).equals("待上市")){
                holder.textView32.setText("    待上市");
            }else if (statelist.get(position).equals("申购中")){
                holder.textView32.setText("    申购中");
            }else if (statelist.get(position).equals("待发行")){
                holder.textView32.setText("    待发行");
            }
        }


        final TextView tvZhongQianLv = holder.tvZhongQianLv;
        final TextView tvPaiHaoShuLiangNum = holder.tvPaiHaoShuLiangNum;
        final TextView tvQiShiHao = holder.tvQiShiHao;
        final TextView textView_time2 = holder.textView_time2;
        final TextView textView_time3 = holder.textView_time3;
        final TextView textView_time4 = holder.textView_time4;
        holder.anniu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //用 currentItem 记录点击位置
                getSubscribeFlowData(map.get("stock_code"), map.get("entrust_date"), tvPaiHaoShuLiangNum, tvQiShiHao, textView_time2, tvZhongQianLv, textView_time3, textView_time4);     //请求 隐藏部分的数据 并赋值
                int tag = (Integer) view.getTag();
                if (tag == currentItem) { //再次点击
                    currentItem = -1; //给 currentItem 一个无效值

                } else {
                    currentItem = tag;

                }
                //通知adapter数据改变需要重新加载
                notifyDataSetChanged(); //必须有的一步

            }

        });

        //根据 currentItem 记录的点击位置来设置"对应Item"的可见
        // 性（在list依次加载列表数据时，每加载一个时都看一下是不是需改变可见性的那一条）
        if (currentItem == position) {
            holder.hideArea.setVisibility(View.VISIBLE);
            isVisible = true;

        } else {
            holder.hideArea.setVisibility(View.GONE);
            isVisible = false;
        }

        if (isVisible) {
            holder.anniu.setBackgroundColor(Color.parseColor("#f0f0f0"));
            holder.imageView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.imageView.setImageResource(R.mipmap.shang);
        } else {
            holder.anniu.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.imageView.setBackgroundColor(Color.parseColor("#f2f2f2"));
            holder.imageView.setImageResource(R.mipmap.xia);
        }

        return convertView;
    }


    private static class ViewHolder {

        private RelativeLayout anniu;
        private LinearLayout showArea;

        private RelativeLayout hideArea;

        //股票名称
        private TextView textViewName;
        //股票代码
        private TextView textView_num;
        //申购日期
        private TextView textViewS_data;
        //申购价格
        private TextView textViewS_price;
        //申购数量
        private TextView textViewS_num;
        //中签数量
        private TextView textViewS_num2;
        //时间一
        private TextView textView_time1;
        //时间二
        private TextView textView_time2;
        //时间三
        private TextView textView_time3;
        //时间四
        private TextView textView_time4;
        //配号数量
        private TextView tvPaiHaoShuLiangNum;
        //起始号
        private TextView tvQiShiHao;
        //状态
        private TextView textView3;
        //安钮图片
        private ImageView imageView;
        //中签率
        private TextView tvZhongQianLv;
        //隐藏部分的 中签数量
        private TextView tvZhongQian;
        //改变上市状态
        private TextView textView32;

    }

    /**
     * 获取申购流程部分的数据
     */
    private void getSubscribeFlowData(final String stockCode, String entrustDate,
                                      final TextView tvPaiHaoShuLiangNum, final TextView tvQiShiHao,
                                      final TextView textView_time2, final TextView textView_time3, final TextView textView_time4, final TextView textView_time5) {
        String mSession = SpUtils.getString(context, "mSession", "");
        Map map3 = new HashMap();
        Map map4 = new HashMap();
        map4.put("SEC_ID", "tpyzq");
        map4.put("FLAG", "true");
        map4.put("STOCK_CODE", stockCode);
        map4.put("ENTRUST_DATE", entrustDate);
        map3.put("funcid", "300384");
        map3.put("token", mSession);
        map3.put("parms", map4);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_JY, map3, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.toString();
            }

            @Override
            public void onResponse(String response, int id) {
                if (response == null && response.equals("")) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<SubscribeHistoryFlowBean>() {
                }.getType();
                SubscribeHistoryFlowBean bean = gson.fromJson(response, type);
                String code = bean.getCode();
                List<SubscribeHistoryFlowBean.DataBean> data = bean.getData();
                if (code.equals("-6")) {
                    Intent intent = new Intent(context, TransactionLoginActivity.class);
                    context.startActivity(intent);
                } else if (code != null && code.equals("0")) {
                    for (int i = 0; i < data.size(); i++) {
                        SubscribeHistoryFlowBean.DataBean dataBean = data.get(i);

                        String remark = dataBean.getREMARK();                           //起始配号
                        String business_amount = dataBean.getBUSINESS_AMOUNT();         //配号数量
                        String next_trade_date = dataBean.getNEXT_TRADE_DATE();         //配号日期

                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("MM.dd");
                        try {
                            Date parse = sdf1.parse(next_trade_date);
                            String format = sdf2.format(parse);
                            textView_time2.setText(format);                             //配号日期
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (business_amount != null && !business_amount.equals("0")) {
                            tvPaiHaoShuLiangNum.setText(business_amount.substring(0, business_amount.indexOf(".")));  //配号数量
                        }
                        tvQiShiHao.setText(remark);                                           //起始号
                    }
                } else {
                    ResultDialog.getInstance().showText("网络异常");
                }
                getPercent(stockCode, textView_time3, textView_time4, textView_time5);
            }
        });
    }


    private void getPercent(String stockCode, final TextView textView,
                            final TextView textView_time3, final TextView textView_time4) {
        String mSession = SpUtils.getString(context, "mSession", "");


        //转换股票 代码
        String STOCK_CODE = stockCode;
        String start = "";
        if (STOCK_CODE.startsWith("730")) {
            start = "600";
        } else if (STOCK_CODE.startsWith("780")) {
            start = "601";
        } else if (STOCK_CODE.startsWith("732")) {
            start = "603";
        } else {
            start = STOCK_CODE.substring(0, 3);
        }
        STOCK_CODE = start + STOCK_CODE.substring(3, 6);


        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map2.put("secucode", STOCK_CODE);
        map1.put("funcid", "100210");
        map1.put("token", mSession);
        map1.put("parms", map2);
        NetWorkUtil.getInstence().okHttpForPostString(TAG, ConstantUtil.URL_NEW, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.toString();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if ("0".equals(code)) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        String percents = null;
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String lotrateonline = item.getString("LOTRATEONLINE");                 //中签率
                            String issueresultpubldate = item.getString("ISSUERESULTPUBLDATE");     //中签时间
                            String listdate = item.getString("LISTDATE");       //上市 时间

                            if (!TextUtils.isEmpty(lotrateonline) && Helper.isDecimal(lotrateonline)){
                                Double percent = Double.valueOf(lotrateonline);
                                DecimalFormat dFormat = new DecimalFormat("0.0#%");
                                percents = dFormat.format(percent);
                            }

                            if (!TextUtils.isEmpty(issueresultpubldate)) {
                                String replace = issueresultpubldate.replace("-", ".");
                                String substring = replace.substring(5, replace.length());
                                textView_time3.setText(substring);                                               //时间3
                            }

                            if (!TextUtils.isEmpty(listdate)) {
                                String replace1 = listdate.replace("-", ".");
                                String substring1 = replace1.substring(5, replace1.length());
                                textView_time4.setText(substring1);                                               //时间3
                            }

                            if (percents != null && percents.length() > 0) {
                                textView.setText("(中签率:" + percents + ")");
                            }
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }
        });
    }
}
