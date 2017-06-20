package com.tpyzq.mobile.pangu.http.doConnect.detail;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.log.LogHelper;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.TransitionUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2016/10/26.
 */
public class ExponentConnect {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "ExponentConnect";
    private String mFlag = "";
    private String mAsc = "";
    private String mExponentNumber = "";
    private String mHttpTAG;
    private String mType;

    public ExponentConnect(String httpTag, String flag, String asc,String exponentNumber, String type) {
        mHttpTAG = httpTag;
        mExponentNumber = exponentNumber;
        mAsc = asc;
        mFlag = flag;
        mType = type;
    }

    public void doConnect(ICallbackResult callbackResult){
        mCallbackResult = callbackResult;
        request();
    }

    public void request() {

        Map<String, String> params = new HashMap();

        try {
            Object []  object = new Object[1];

            Map map2 = new HashMap();
            map2.put("flag", mFlag);//1行情2换手率
            map2.put("asc", mAsc);//1为涨 其他为跌
            map2.put("startNo", "0");//起始的记录数，从0开始
            map2.put("number", "20");//返回的记录数 -1从起始数到最后
            map2.put("exponential", mExponentNumber);
            object[0] = map2;

            Gson gson = new Gson();
            String strJson = gson.toJson(object);
            params.put("PARAMS", strJson);

            params.put("FUNCTIONCODE","HQING015");

        } catch (Exception e) {
            e.printStackTrace();
        }

        NetWorkUtil.getInstence().okHttpForGet(mHttpTAG, ConstantUtil.URL, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, e.toString());
                ArrayList<StockInfoEntity> beanss = new ArrayList<StockInfoEntity>();
                mCallbackResult.getResult(beanss, TAG);
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)) {
                    return ;
                }
//                LogHelper.e(TAG, response);
                //[{"time":"0","bytes":28,"totalCount":"22","data":[["11603258","N电魂   ","22.49","15.62","0.4398207366466522","0.0","挂"],["11603859","能科股份","14.47","13.15","0.10038027912378311","1.8316308559351884E-4","挂"],["11600275","武昌鱼  ","18.54","16.85","0.10029676556587219","0.20067202707361603","挂"],["11603667","五洲新春","13.94","12.67","0.10023674368858337","0.0","挂"],["11600250","南纺股份","17.02","15.47","0.10019393265247345","0.10689333581659087","挂"],["11601020","华钰矿业","34.94","31.76","0.10012589395046234","0.32884423076923075","挂"],["11600759","洲际油气","8.79","7.99","0.10012518614530563","0.06519489448560066","挂"],["11603090","宏盛股份","66.94","60.85","0.10008223354816437","0.485716","挂"],["11603887","城地股份","54.85","49.86","0.10008017718791962","0.00941869918699187","挂"],["11600716","凤凰股份","8.69","7.9","0.09999993443489075","0.038905178685007716","挂"],["11600158","中体产业","18.27","16.61","0.09993978589773178","0.11290208403468172","挂"],["11603160","汇顶科技","54.48","49.53","0.09993945062160492","5.533333333333333E-4","挂"],["11603777","来伊份  ","43.59","39.63","0.09992427378892899","0.40541166666666667","挂"],["11600862","中航高科","14.11","12.83","0.09976615011692047","0.055240204290735485","挂"],["11600868","梅雁吉祥","6.74","6.13","0.0995105430483818","0.018821444492336314","挂"],["11603031","安德利  ","73.9","67.71","0.09141932427883148","0.38055","挂"],["11600313","农发种业","6.11","5.74","0.06445999443531036","0.04916413542350918","挂"],["11600747","大连控股","5.32","5.02","0.05976099520921707","0.06119173373668478","挂"],["11600051","宁波联合","12.0","11.34","0.05820104479789734","0.07636334501883633","挂"],["11603919","金徽酒  ","34.86","32.97","0.05732481926679611","0.20226714285714287","挂"],["11600809","山西汾酒","23.45","22.19","0.05678234249353409","0.032520594087555754","挂"],["11600887","伊利股份","18.43","17.46","0.05555562674999237","0.026707449133874166","挂"]],"code":"0","ceshi":"挂","isOpen":"true"}]
                ArrayList<StockInfoEntity> beans = new ArrayList<StockInfoEntity>();
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject res = jsonArray.getJSONObject(0);
                    String code = res.optString("code") ;
                    if("0".equals(code)){
                        JSONArray json = res.getJSONArray("data");
                        if(null != json && json.length() > 0){
                            for(int i = 0 ; i < json.length();i++){
                                JSONArray data = json.getJSONArray(i);
//                                for(int j = 0; j < data.length();j++){
                                    StockInfoEntity stockInfoEntity = new StockInfoEntity();
                                    stockInfoEntity.setStockNumber(data.getString(0));
                                    stockInfoEntity.setStockName(data.getString(1));
                                    stockInfoEntity.setNewPrice(data.getString(2));
                                    stockInfoEntity.setClose(data.getString(3));
                                    stockInfoEntity.setPriceChangeRatio(TransitionUtils.string2double(data.getString(4)));
                                    stockInfoEntity.setTurnover(data.getString(5));
                                    stockInfoEntity.setCode(code);
                                    stockInfoEntity.setType(mType);
                                    beans.add(stockInfoEntity);
//                                }
                            }
                        }
                        mCallbackResult.getResult(beans, TAG);
                    }else{
                        mCallbackResult.getResult("网络请求无数据",TAG);
                    }

                }catch (JSONException e){
                   e.printStackTrace();
                    mCallbackResult.getResult(e.toString(), TAG);
                }
            }
        });

    }
}
