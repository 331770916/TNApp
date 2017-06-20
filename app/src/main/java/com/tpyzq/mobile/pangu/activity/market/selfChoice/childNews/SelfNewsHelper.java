package com.tpyzq.mobile.pangu.activity.market.selfChoice.childNews;

import com.tpyzq.mobile.pangu.data.NewsInofEntity;
import com.tpyzq.mobile.pangu.log.LogHelper;

import java.util.ArrayList;


/**
 * Created by zhangwenbo on 2016/12/7.
 * 自选股相关工具类
 */

public class SelfNewsHelper {

    private static final String TAG = "SelfNewsHelper";

    public static ArrayList<NewsInofEntity> explantSelfNews(String response) {
        LogHelper.e("SelfNewsHelper","explantSelfNews:"+response);
//        ObjectMapper objectMapper = JacksonMapper.getInstance();
//        ArrayList<NewsInofEntity> entities = new ArrayList<NewsInofEntity>();
//        try {
//            Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//
//            String code = "";
//            if (null != responseValues.get("code")) {
//                code = String.valueOf(responseValues.get("code"));
//            }
//
//            String type = "";
//            if (null != responseValues.get("type")) {
//                type = String.valueOf(responseValues.get("type"));
//            }
//
//            if (!TextUtils.isEmpty(type) && !"SUCCESS".equals(type)) {
//                LogHelper.e(TAG, "code =" +code + "type =" + type);
//                return null;
//            }
//
//            if (null != responseValues.get("message") && responseValues.get("message") instanceof List) {
//                List<Object> message = (List<Object>) responseValues.get("message");
//
//
//                for (Object object : message) {
//                    if (null != object && object instanceof Map) {
//                        NewsInofEntity _bean = new NewsInofEntity();
//
//                        Map<String, Object> params = (Map<String, Object>) object;
//
//                        String dt = "";
//                        if (null != params.get("dt")) {
//                            dt = String.valueOf(params.get("dt"));
//                        }
//
//                        String title = "";
//                        if (null != params.get("title")) {
//                            title = String.valueOf(params.get("title"));
//                        }
//
//                        String newsId = "";
//                        if (null != params.get("id")) {
//                            newsId = String.valueOf(params.get("id"));
//                        }
//
//                        String sum = "";
//                        if (null != params.get("sum")) {
//                            sum = String.valueOf(params.get("sum"));
//                        }
//
//                        String auth = "";
//                        if (null != params.get("auth")) {
//                            auth = String.valueOf(params.get("auth"));
//                        }
//
//                        _bean.setAuth(auth);  // 作者
//
//                        if (!TextUtils.isEmpty(dt)) {
//                            _bean.setDt(Long.valueOf(dt));//时间
//                        }
//
//                        _bean.setId(newsId);      //id
//
//                        if (!TextUtils.isEmpty(sum)) {
//                            _bean.setSum(sum.replace("#&#", ""));    //内容
//                        }
//
//                        if (!TextUtils.isEmpty(title)) {
//                            _bean.setTitle(title.replace("#&#", ""));
//                        }
//
//                        if (null != params.get("relateList") && params.get("relateList") instanceof List) {
//                            List<Map<String, Object>> subData = (List<Map<String, Object>>) params.get("relateList");
//
//                            if (subData != null && subData.size() > 0) {
//                                for (Map<String, Object> subValues : subData) {
//                                    String secu = "";
//
//                                    if (null != subValues.get("secu")) {
//                                        secu = String.valueOf(subValues.get("secu"));
//                                    }
//
//                                    String comp = "";
//                                    if (null != subValues.get("comp")) {
//                                        comp = String.valueOf(subValues.get("comp"));
//                                    }
//
//                                    String tick = "";
//                                    if (null != subValues.get("tick")) {
//                                        tick = String.valueOf(subValues.get("tick"));
//                                    }
//
//                                    _bean.setComp(comp);
//                                    _bean.setTick(tick);
//                                }
//                            }
//
//                            entities.add(_bean);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }

        return null;
    }
}
