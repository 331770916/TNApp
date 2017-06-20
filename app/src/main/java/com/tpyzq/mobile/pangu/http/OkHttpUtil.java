package com.tpyzq.mobile.pangu.http;

import com.google.gson.Gson;
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
        getBuilder.tag(tag).build().execute(callback);
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
        postFileBuilder.tag(tag).build().execute(callback);
    }

    /** 提交一个Gson字符串到服务器端 */
    public static void okHttpForPostString (Object tag, String url, Object object,  Callback callback) {
        PostStringBuilder postStringBuilder =  OkHttpUtils.postString().url(url);

        if (object == null) {
            return;
        }

        postStringBuilder.content(new Gson().toJson(object)).tag(tag).build().connTimeOut(6000).readTimeOut(6000).execute(callback);
    }

    /** 将文件作为请求体，发送到服务器 */
    public static void okHttpForPostFile(Object tag,String url, File file, Callback callback) {

        if (file == null) {
            return;
        }

        OkHttpUtils.postFile().url(url).file(file).tag(tag).build().execute(callback);
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

        postFormBuilder.url(url).params(params).headers(headers).tag(tag).build().execute(callback);
    }

    /** 下载文件 */

    public static void okHttpForDownLoasFile(Object tag, String url, FileCallBack callback) {
        OkHttpUtils.get().url(url).tag(tag).build().execute(callback);
    }

    /** 显示图片 */

    public static void okHttpForShowBitmap(Object tag, String url, BitmapCallback callback) {
        OkHttpUtils.get().url(url).tag(tag).build().execute(callback);
    }

    /** 取消单个请求 */

    public static void cancelSingleRequestByUrl(String url) {
        RequestCall call = OkHttpUtils.get().url(url).build();
        call.cancel();
    }

    /** 根据tag取消单个请求 */

    public static void cancelSingleRequestByTag(Object tag){
        OkHttpUtils.getInstance().cancelTag(tag);
    }

}
