package com.tpyzq.mobile.pangu.http;

import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.Map;

/**
 *
 * 这是一个调用网络的公共类
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
public class NetWorkUtil {

    private static NetWorkUtil mNetWorkUtil;

    private NetWorkUtil(){}

    //注意 这里是否需要使用 synchronize  这里需要看看
    public static synchronized NetWorkUtil  getInstence() {
        if (mNetWorkUtil == null) {
            mNetWorkUtil = new NetWorkUtil();
        }

        return mNetWorkUtil;
    }

    /**
     * OkHttp相关
     */


    /**
     *  okhttp的一般get请求
     * @param url           地址
     * @param params        传入的参数
     * @param callback      回调接口
     */
    public void okHttpForGet (Object tag, String url, Map <String, String> params, Callback callback) {
        OkHttpUtil.OkHttpForGet(tag, url, params, callback);
    }

    /**
     *  一般的post请求
     * @param url           地址
     * @param params        传入的参数
     * @param callback      回调接口
     */
    public void okHttpForPost (Object tag, String url, Map <String, String> params, Callback callback) {
        OkHttpUtil.okHttpForPost(tag, url, params, callback);
    }

    /**
     * 提交一个Gson字符串到服务器端
     * @param url       地址
     * @param object    实体类对象
     * @param callback  回调
     */
    public void okHttpForPostString (Object tag, String url, Object object,  Callback callback) {
        OkHttpUtil.okHttpForPostString(tag, url, object, callback);
    }

    /**
     * 将文件作为请求体，发送到服务器
     * @param url           地址
     * @param file          文件
     * @param callback      回调
     */
    public void okHttpForPostFile (Object tag, String url, File file, Callback callback) {
        OkHttpUtil.okHttpForPostFile(tag, url, file, callback);
    }

    /**
     * post表单形式上传文件   支持单个多个文件
     * @param url           地址
     * @param name          表单中的name  addFile的第一个参数为文件的key，即类别表单中<input type="file" name="mFile"/>的name属性。
     * @param files         文件集合
     * @param params        上传的其他的数据
     * @param headers       希望接受的数据类型 一般填写   key : Accept  value ： *斜杠*
     * @param callback      回调接口
     */
    public void okHttpForPostFileForm(Object tag, String url, String name, Map<String, File> files, Map<String, String> params, Map<String, String> headers, Callback callback) {
        OkHttpUtil.okHttpForPostFileForm(tag, url, name, files, params, headers, callback);
    }

    /**
     * 下载文件
     * @param url       url地址
     * @param callback  回调接口
     */
    public static void okHttpForDownLoadFile(Object tag, String url, FileCallBack callback) {
        OkHttpUtil.okHttpForDownLoasFile(tag, url, callback);
    }

    /**
     *显示图片
     * @param url           url地址
     * @param callback      回调接口
     */
    public static void okHttpForShowBitmap(Object tag, String url, BitmapCallback callback) {
        OkHttpUtil.okHttpForShowBitmap(tag, url, callback);
    }

    /** 取消单个请求 */
    public static void cancelSingleRequestByUrl(String url) {
        OkHttpUtil.cancelSingleRequestByUrl(url);
    }

    /** 取消单个请求 */
    public static void cancelSingleRequestByTag(Object tag) {
        OkHttpUtil.cancelSingleRequestByTag(tag);
    }


}
