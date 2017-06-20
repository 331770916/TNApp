package com.tpyzq.mobile.pangu.http.doConnect.home;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.db.Db_HOME_INFO;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/9/18.
 */
public class GetHomeInfoConnect {
    private ICallbackResult mCallbackResult;
    private static final String TAG = "GetHomeInfoConnect";
    private String mHttpTAG;

    private int mInfoNumber;     //资讯条数
    public GetHomeInfoConnect(String httpTag, int infoNumber) {
        mInfoNumber = infoNumber;
        mHttpTAG = httpTag;
    }

    public void doConnect(ICallbackResult callbackResult){
        mCallbackResult = callbackResult;
        request();
    }

    public void request() {
        Map map1 = new HashMap<>();
        map1.put("FUNCTIONCODE", "HQONG007");
        map1.put("TOKEN", "");
        Map map2 = new HashMap();
        map2.put("offset", "0");
        map2.put("limit", mInfoNumber);
        map1.put("PARAMS", map2);

        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.URL_NEW_ZX, map1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                mCallbackResult.getResult("网络异常", TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return ;
                }

//                response = "{\"code\":\"200\",\"type\":\"SUCCESS\",\"message\":[{\"id\":\"57df7d06e4b0a3b2adb62b71\",\"dt\":1474264211000,\"title\":\"一线房源告急，谁在制造中国“地王”?\",\"auth\":\"棱镜\",\"pos\":0,\"stocks\":[{\"code\":\"001979_SZ_EQ\",\"name\":\"招商蛇口\",\"tick\":\"001979\"}]},{\"id\":\"57df7cf4e4b0a3b2adb62b6a\",\"dt\":1474263900000,\"title\":\"丽珠集团：定增价接近市场价 公司成长性获市场认可\",\"auth\":\"\",\"pos\":0,\"stocks\":[{\"code\":\"000513_SZ_EQ\",\"name\":\"丽珠集团\",\"tick\":\"000513\"}]},{\"id\":\"57df7c99e4b0a3b2adb62b47\",\"dt\":1474263780000,\"title\":\"万家文化：拟收购电竞经纪公司 亚文化泛娱乐布局再加码\",\"auth\":\"\",\"pos\":0,\"stocks\":[{\"code\":\"600576_SH_EQ\",\"name\":\"万家文化\",\"tick\":\"600576\"}]}]}";
               try{
                   JSONObject res = new JSONObject(response);
                   //资讯信息获取成功，则保存数据库和进行解析处理
                   if("200".equals(res.optString("code"))){
                       Db_HOME_INFO.deleteAll();
                       Db_HOME_INFO.addOneHomeInfo(response);
                       ArrayList<InformationEntity> informationEntities = HomeFragmentHelper.getInstance().getInfoListfromJson(response,mInfoNumber);
                       mCallbackResult.getResult(informationEntities, TAG);
                   }else{
                       mCallbackResult.getResult("请求资讯信息失败", TAG);
                   }
               }catch (JSONException e){
                   e.printStackTrace();
                   mCallbackResult.getResult("报文解析异常", TAG);
               }


//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//                try {
//
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//
//                    ArrayList<InformationEntity> informationEntities = new ArrayList<InformationEntity>();
//
//                    String code = "";
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
//                    }
//
//                    String type = "";
//
//                    if (null != responseValues.get("type")) {
//                        type = String.valueOf(responseValues.get("type"));
//                    }
//
//                    if ("SUCCESS".equals(type)) {
//                        Db_HOME_INFO.deleteAll();
//                        Db_HOME_INFO.addOneHomeInfo(response);
//                    }
//
//                    List<Object> message = new ArrayList<Object>();
//
//                    if (null != responseValues.get("message")) {
//                        message = (List<Object>) responseValues.get("message");
//                    }
//
//                    if (message != null && message.size() > 0) {
//
//                        for (Object object : message) {
//                            InformationEntity entity = new InformationEntity();
//                            Map<String, Object> items = (Map<String, Object>) object;
//
//                            String _id = "";
//                            if (null != items.get("id")) {
//                                _id = String.valueOf(items.get("id"));
//                            }
//
//                            String dt = "";
//                            if (null != items.get("dt")) {
//                                dt = String.valueOf(items.get("dt"));
//                            }
//
//                            String title = "";
//                            if (null != items.get("title")) {
//                                title = String.valueOf(items.get("title"));
//                            }
//
//                            String auth = "";
//                            if (null != items.get("auth")) {
//                                auth = String.valueOf(items.get("auth"));
//                            }
//
//                            String pos = "";
//                            if (null != items.get("pos")) {
//                                pos = String.valueOf(items.get("pos"));
//                            }
//
//                            entity.setNewsId(_id);
//                            entity.setPublishTitle(title);
//                            entity.setPublishTime(Helper.getTimeByTimeC(dt));
//
//                            List<Map<String, Object>> stocks = new ArrayList<Map<String, Object>>();
//                            Object subObj = items.get("stocks");
//                            if (null != items.get("stocks") && subObj instanceof List) {
//                                stocks = (List<Map<String, Object>>) items.get("stocks");
//
//                                StringBuilder sb = new StringBuilder();
//
//                                int tempNum = 0;
//                                for (Map<String, Object> subItem : stocks) {
//
//                                    String _code = "";
//
//                                    if (null != subItem.get("code")) {
//                                        _code = String.valueOf(subItem.get("code"));
//                                    }
//
//                                    String name = "";
//                                    if (null != subItem.get("name")) {
//                                        tempNum = tempNum + 1;
//                                        name = String.valueOf(subItem.get("name"));
//                                        if (tempNum <= 2) {
//                                            sb.append(name + "\u2000");
//                                        }
//                                    }
//
//                                    String tick = "";
//
//                                    if (null != subItem.get("tick")) {
//                                        tick = String.valueOf(subItem.get("tick"));
//                                    }
//                                }
//
//                                String aboutStock = sb.toString();
//
//                                if (!TextUtils.isEmpty(aboutStock)) {
//                                    entity.setPublishAboutStock(aboutStock);
//                                } else {
//                                    entity.setPublishAboutStock("无");
//                                }
//                                informationEntities.add(entity);
//                            }
//                        }
//                        mCallbackResult.getResult(informationEntities, TAG);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    mCallbackResult.getResult("" + e.toString(), TAG);
//                }
            }
        });


    }


    private class NewsInfos{
        private String code;
        private String type;
        private List<NewInfo> message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<NewInfo> getMessage() {
            return message;
        }

        public void setMessage(List<NewInfo> message) {
            this.message = message;
        }
    }

    private class NewInfo {
        private String id;      //新闻ID
        private String dt;      //发布时间（时间戳
        private String title;   //标题
        private String auth;    //作者
        private String pos;     //新闻正负面（热点新闻独有）0. 中性 1.正面  2.负面
        private List<Stocks> stocks;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDt() {
            return dt;
        }

        public void setDt(String dt) {
            this.dt = dt;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public List<Stocks> getStocks() {
            return stocks;
        }

        public void setStocks(List<Stocks> stocks) {
            this.stocks = stocks;
        }
    }

    private class Stocks{
        private String code;    //数库股票代码（六位代码加市场后缀）
        private String name;    //股票名称
        private String tick;    //股票代码（六位代码）

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTick() {
            return tick;
        }

        public void setTick(String tick) {
            this.tick = tick;
        }
    }

}
