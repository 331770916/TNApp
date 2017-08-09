package com.tpyzq.mobile.pangu.http;

import com.google.gson.Gson;
import com.tpyzq.mobile.pangu.BuildConfig;
import com.tpyzq.mobile.pangu.http.manager.NetworkManager;
import com.tpyzq.mobile.pangu.log.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/4/25.
 *
 * 目前内部包含StringCallBack,FileCallBack,BitmapCallback，可以根据自己的需求去自定义Callback
 *
 * 例如希望回调User对象：
 *public abstract class UserCallback extends Callback<User> {
 public User parseNetworkResponse(Response response) throws IOException {
 String string = response.body().string();
 User user = new Gson().fromJson(string, User.class);
 return user;
 }
 }

 */
public class OkHttpUtil {

    public static final String NO_TAG="no_tag";

    /** okhttp的一般get请求 */
    public static void OkHttpForGet (Object tag, String url, Map <String, String> params, Callback callback) {
        GetBuilder getBuilder = OkHttpUtils.get().url(url);

        if (params == null) {
            return;
        }

        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();

            if (entry != null) {
                getBuilder.addParams(entry.getKey(), entry.getValue());
            }

        }
        getBuilder.tag(pairTag(tag)).build().execute(callback);
    }

    /** okhttp的一般post请求 */
    public static void okHttpForPost (Object tag, String url, Map <String, String> params, Callback callback) {
        PostFormBuilder postFileBuilder = OkHttpUtils.post().url(url);

        if (params == null) {
            return;
        }

        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();


        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();

            if (entry != null) {
                postFileBuilder.addParams(entry.getKey(), entry.getValue());
            }

        }
        postFileBuilder.tag(pairTag(tag)).build().execute(callback);
    }
    /** 提交一个Gson字符串到服务器端 */
    public static void okHttpForPostString (Object tag, String url, Object object,  Callback callback) {
        PostStringBuilder postStringBuilder =  OkHttpUtils.postString().url(url);
        if (object == null) {
            return;
        }
        if(BuildConfig.DEBUG)
            LogUtil.i("请求参数："+new Gson().toJson(object));
        postStringBuilder.content(new Gson().toJson(object)).tag(pairTag(tag)).build().readTimeOut(30000).execute(callback);
    }

    /** 提交一个Gson字符串到服务器端自定义时常 */
    public static void okHttpForPostStringTime (Object tag, String url, Object object,  Callback callback,int time) {
        PostStringBuilder postStringBuilder =  OkHttpUtils.postString().url(url);
        if (object == null) {
            return;
        }
        if(BuildConfig.DEBUG)
            LogUtil.i("请求参数："+new Gson().toJson(object));
        postStringBuilder.content(new Gson().toJson(object)).tag(pairTag(tag)).build().readTimeOut(time).execute(callback);
    }

    /** 将文件作为请求体，发送到服务器 */
    public static void okHttpForPostFile(Object tag,String url, File file, Callback callback) {

        if (file == null) {
            return;
        }

        OkHttpUtils.postFile().url(url).file(file).tag(pairTag(tag)).build().execute(callback);
    }

    /** post表单形式上传文件
     * 支持单个多个文件，addFile的第一个参数为文件的key，即类别表单中<input type="file" name="mFile"/>的name属性。*/
    public static void okHttpForPostFileForm(Object tag, String url, String name, Map<String, File> files, Map <String, String> params, Map<String, String> headers, Callback callback) {
        PostFormBuilder postFormBuilder = OkHttpUtils.post();

        if (files == null) {
            return;
        }

        Iterator<Map.Entry<String, File>> iterator = files.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<String, File> entry = iterator.next();

            if (entry != null) {
                postFormBuilder.addFile(name, entry.getKey(), entry.getValue());//
            }

        }

        postFormBuilder.url(url).params(params).headers(headers).tag(pairTag(tag)).build().execute(callback);
    }

    /** 下载文件 */

    public static void okHttpForDownLoasFile(Object tag, String url, FileCallBack callback) {
        OkHttpUtils.get().url(url).tag(pairTag(tag)).build().execute(callback);
    }

    /** 显示图片 */

    public static void okHttpForShowBitmap(Object tag, String url, BitmapCallback callback) {
        OkHttpUtils.get().url(url).tag(pairTag(tag)).build().execute(callback);
    }

    /** 取消单个请求 */

    public static void cancelSingleRequestByUrl(String url) {
        RequestCall call = OkHttpUtils.get().url(url).build();
        call.cancel();
    }

    /** 根据tag取消单个请求 */

    public static void cancelSingleRequestByTag(Object tag){
        OkHttpUtils.getInstance().cancelTag((tag instanceof String)?tag:tag.getClass().getName());
    }

    private static String pairTag(Object originTag){
        if (NO_TAG.equals(originTag)){
            return "";
        }
        if (null==originTag)
            return NetworkManager.getLatestActivityTag();
        if (originTag instanceof String){
            return originTag+"||"+NetworkManager.getLatestActivityTag();
        }
        return originTag.getClass().getName()+"||"+NetworkManager.getLatestActivityTag();
    }


}
