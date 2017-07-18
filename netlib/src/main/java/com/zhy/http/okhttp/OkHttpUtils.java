package com.zhy.http.okhttp;

import android.util.Log;

import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.HeadBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostFileBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.PostStringRequest;
import com.zhy.http.okhttp.request.RequestCall;
import com.zhy.http.okhttp.utils.Platform;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils
{
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpUtils(OkHttpClient okHttpClient)
    {
        if (okHttpClient == null)
        {
            mOkHttpClient = new OkHttpClient();
        } else
        {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient)
    {
        if (mInstance == null)
        {
            synchronized (OkHttpUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance()
    {
        return initClient(null);
    }


    public Executor getDelivery()
    {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient()
    {
        return mOkHttpClient;
    }

    public static GetBuilder get()
    {
        return new GetBuilder();
    }

    public static PostStringBuilder postString()
    {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile()
    {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post()
    {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put()
    {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head()
    {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete()
    {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch()
    {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    /**
     * 生产环境 置为false
     */
    public static final  boolean DEBUG=true;

    public void execute(final RequestCall requestCall, Callback callback)
    {
        if (DEBUG){
            Log.d("OkHttp","===================================================");
            if (requestCall.getOkHttpRequest() instanceof PostStringRequest)
            Log.d("OkHttp",requestCall.getOkHttpRequest().url+"=======  startRequest"+((PostStringRequest) requestCall.getOkHttpRequest()).content);
            else
            Log.d("OkHttp",requestCall.getOkHttpRequest().url+"=======  startRequest");
            Log.d("OkHttp","===================================================");
        }


        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        requestCall.getCall().enqueue(new okhttp3.Callback()
        {
            @Override
            public void onFailure(Call call, final IOException e)
            {
                if (DEBUG){
                    Log.e("OkHttp","===================================================");
                    Log.e("OkHttp",requestCall.getOkHttpRequest().url+"  onError "+(e==null?"":e.getMessage()));
                    Log.e("OkHttp","===================================================");
                }
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response)
            {
                try
                {
                    if (call.isCanceled())
                    {
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id))
                    {
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }

                    Object o = finalCallback.parseNetworkResponse(response, id);
                    if (DEBUG){
                        Log.d("OkHttp","===================================================");
                        Log.d("OkHttp",requestCall.getOkHttpRequest().url+"======= onSuccess==="+o.toString());
                        Log.d("OkHttp","===================================================");
                    }
                    sendSuccessResultCallback(call,o, finalCallback, id);
                } catch (Exception e)
                {
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally
                {
                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }


    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id)
    {
        if (callback == null) return;

        mPlatform.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    if (!(e.getMessage().toLowerCase().matches(".*(cancel|closed).*")))
                        callback.onError(call, e, id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.onAfter(id);
            }
        });
    }

    public void sendSuccessResultCallback(final Call call,final Object object, final Callback callback, final int id)
    {
        if (callback == null) return;
        mPlatform.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    callback.onResponse(object, id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.onAfter(id);
            }
        });
    }

    public void cancelTag(Object tag)
    {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls())
        {
            if (call.request().tag() instanceof String&&tag instanceof String){
                String tagStr=(String)call.request().tag();
                if (tagStr.contains("||")&&tagStr.contains((CharSequence) tag)){
                    call.cancel();
                }
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls())
        {
            if (call.request().tag() instanceof String&&tag instanceof String){
                String tagStr=(String)call.request().tag();
                if (tagStr.contains("||")&&tagStr.contains((CharSequence) tag)){
                    call.cancel();
                }
            }
        }
    }

    public static void setUserAgent(String userAgent) {
        sUserAgent=userAgent;
    }

    public static String sUserAgent;

    public static class METHOD
    {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

