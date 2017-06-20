package com.tpyzq.mobile.pangu.util.keyboard;

import android.os.AsyncTask;
import android.util.Log;

import com.tpyzq.mobile.pangu.log.LogUtil;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.yzd.unikeysdk.UniKey;
import com.yzd.unikeysdk.UnikeyException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by 陈新宇 on 2016/9/12.
 */
public class HandleResponse {

    public static void HandleResponseHelper(final ResponseInterface responseInterface, final int req, String... params) {
        new AsyncTask<String, Void, Object>() {
            @Override
            protected Object doInBackground(String... params) {
                Object result = null;
                try {
                    UniKey uniKey = UniKey.getInstance();
                    switch (req) {
                        case Constants.APPLY_PLUGIN_REQ:
                            uniKey.applyPlugin();
                            result = true;
                            break;
//                        case Constants.GET_CODE_REQ:
//                            result = uniKey.getCode(params[0]);
//                            break;
//                        case Constants.VERIFY_CODE_REQ:
//                            result = uniKey.verifyCode(params[0], params[1]);
//                            break;
                        case Constants.GET_SERVER_CRYPTSTR:
                            try {
                                String data = params[0];
                                String diviceid = params[1];
                                String su = String.format("%s/unikeyAuth/servlet?funid=200008&appid=%s&mobile=%s&unikeyid=%s&data=%s", ConstantUtil.SJYZM, ConstantUtil.APP_ID, diviceid, uniKey.getUnikeyId(), data);
                                LogUtil.e("url参数", su);
                                URL url = new URL(su);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.connect();
                                if (conn.getResponseCode() == 200) {
                                    InputStream inputStream = conn.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                                    StringBuilder sb = new StringBuilder();

                                    String line = null;

                                    while ((line = reader.readLine()) != null) {
                                        sb.append(line + "\n");
                                    }
                                    inputStream.close();
                                    String str = sb.toString();
//                                    Log.d("HandleResponse", conn.getResponseCode() + " " + str);
                                    HashMap<String, String> stringStringHashMap = parseJson(str);
                                    if (stringStringHashMap.containsKey("data")) {
                                        result = stringStringHashMap.get("data");
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case Constants.Bi_AUTH_FIRST_REQ:
                            result = uniKey.checkServer();
                            break;
                        case Constants.VERIFY_APP_REQ:
                            result = uniKey.checkApp();
                            break;

                        case Constants.GET_KEYBOARD_INPUT_DECRYPTED_REQ:
                            String data = params[0];
                            String diviceid = params[1];
                            try {
                                String s = String.format("%s/unikeyAuth/servlet?funid=200005&appid=%s&mobile=%s&unikeyid=%s&data=%s", ConstantUtil.SJYZM, ConstantUtil.APP_ID, diviceid, uniKey.getUnikeyId(), data);
                                LogUtil.e("url参数", s);
                                URL url = new URL(s);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.connect();
                                if (conn.getResponseCode() == 200) {
                                    InputStream inputStream = conn.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                                    StringBuilder sb = new StringBuilder();

                                    String line = null;

                                    while ((line = reader.readLine()) != null) {
                                        sb.append(line + "\n");
                                    }
                                    inputStream.close();
                                    String str = sb.toString();
                                    Log.d("HandleResponse", conn.getResponseCode() + " " + str);
                                    HashMap<String, String> stringStringHashMap = parseJson(str);
                                    if (stringStringHashMap.containsKey("data")) {
                                        result = stringStringHashMap.get("data");
                                    }

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            break;
                    }
                } catch (UnikeyException e) {
                    e.printStackTrace();
                    Log.d("HandleResponse", "Number" + e.getNumber() + " Message " + e.getMessage());
                    return String.valueOf(e.getNumber()) + "::" + e.getMessage();
                }
                return result;
            }

            /**
             * Uses the logging framework to display the output of the fetch
             * operation in the log fragment.
             */
            @Override
            protected void onPostExecute(Object result) {
                responseInterface.onGetResponse(result);
            }
        }.execute(params);

    }

    public static String makeRandomArray(int digit) {
        String R = "";
        Random random = new Random();
        for (int i = 0; i < digit; i++) {
            R += random.nextInt(8) + 1;
        }
        return R;
    }

    public static HashMap<String, String> parseJson(String response) throws JSONException {
        if (response == null) {
            return null;
        }
        HashMap<String, String> map = new HashMap<String, String>();

        JSONObject jsonObject = new JSONObject(response);
        for (Iterator<String> keys = jsonObject.keys(); keys.hasNext(); ) {
            String key1 = keys.next();
            map.put(key1, jsonObject.get(key1).toString());
        }

        return map;
    }
}
