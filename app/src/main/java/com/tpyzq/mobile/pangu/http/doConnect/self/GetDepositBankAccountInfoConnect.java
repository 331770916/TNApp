package com.tpyzq.mobile.pangu.http.doConnect.self;

import android.text.TextUtils;
import com.tpyzq.mobile.pangu.http.OkHttpUtil;
import com.tpyzq.mobile.pangu.interfac.IChangeDepositBankResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zhangwenbo on 2017/7/4.
 * 三存银行变更接口
 */

public class GetDepositBankAccountInfoConnect {

    /**
     *三存银行变更获取主资金账号
     * @param session
     * @param tag
     * @param changeDepositBankResult
     */
    public void getDepositBankAccountInfo(String session, String tag, final IChangeDepositBankResult changeDepositBankResult) {
        HashMap map = new HashMap();
        map.put("funcid", "300360");
        map.put("token", session);
        HashMap hashMap = new HashMap();
        hashMap.put("SEC_ID", "tpyzq");
        hashMap.put("FLAG", "true");
        map.put("parms", hashMap);

        OkHttpUtil.okHttpForPostString(tag, ConstantUtil.URL_SCYHBG, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (changeDepositBankResult != null) {
                    changeDepositBankResult.loadingClose();
                    changeDepositBankResult.error(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {

                if (changeDepositBankResult != null) {
                    changeDepositBankResult.loadingClose();
                }

                if (TextUtils.isEmpty(response)) {
                    if (changeDepositBankResult != null) {
                        changeDepositBankResult.error(ConstantUtil.SERVICE_NO_DATA);
                    }
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");

                    if ("-6".equals(code)) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.toLogin();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.error(msg);
                        }
                        return;
                    }

                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data == null || data.length() <= 0) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.error(ConstantUtil.SERVICE_NO_DATA);
                        }
                        return;
                    }

                    List<Map<String, String>> bankInfos = new ArrayList<Map<String, String>>();

                    for (int i = 0; i < data.length(); i++) {
                        Map<String, String> bank = new HashMap<String, String>();
                        JSONObject object = data.getJSONObject(i);

                        String BANK_NO = object.optString("BANK_NO");
                        String CURRENCY = object.optString("CURRENCY");
                        String EXT_ACC = object.optString("EXT_ACC");
                        String EXT_INST = object.optString("EXT_INST");
                        String STATUS = object.optString("STATUS");
                        String BANK_NAME = object.optString("BANK_NAME");
                        String STATUS_NAME = object.optString("STATUS_NAME");

                        bank.put("BANK_NO", BANK_NO);
                        bank.put("CURRENCY", CURRENCY);
                        bank.put("EXT_ACC", EXT_ACC);
                        bank.put("EXT_INST", EXT_INST);
                        bank.put("STATUS", STATUS);
                        bank.put("BANK_NAME", BANK_NAME);
                        bank.put("STATUS_NAME", STATUS_NAME);

                        bankInfos.add(bank);
                    }

                    if (changeDepositBankResult != null) {
                        changeDepositBankResult.getBankInfos(bankInfos);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (changeDepositBankResult != null) {
                        changeDepositBankResult.error(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });

    }

    /**
     * 销户检查
     */
    public void checkCloseAccount(String tag, String session, String bank_no, String currency, final IChangeDepositBankResult changeDepositBankResult) {
        HashMap map = new HashMap();
        map.put("funcid", "300603");
        map.put("token", session);
        HashMap hashMap = new HashMap();
        hashMap.put("SEC_ID", "tpyzq");
        hashMap.put("FLAG", "true");
        hashMap.put("BANK_NO", bank_no);
        hashMap.put("MONEY_TYPE", currency);
        map.put("parms", hashMap);

        OkHttpUtil.okHttpForPostString(tag, ConstantUtil.URL_SCYHBG, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                if (changeDepositBankResult != null) {
                    changeDepositBankResult.loadingClose();
                    changeDepositBankResult.error(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {

                if (changeDepositBankResult != null) {
                    changeDepositBankResult.loadingClose();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");

                    if ("-6".equals(code)) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.toLogin();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.error(msg);
                        }
                        return;
                    }

                    JSONArray data = jsonObject.getJSONArray("data");

                    if (data == null || data.length() <= 0) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.error(ConstantUtil.SERVICE_NO_DATA);
                        }
                        return;
                    }

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        String STATUS = object.optString("STATUS");
                        String ERROR_INFO = object.optString("ERROR_INFO");

                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.checkCloseAccount(STATUS, ERROR_INFO);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (changeDepositBankResult != null) {
                        changeDepositBankResult.loadingClose();
                        changeDepositBankResult.error(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });

    }


    /**
     * 三存银行  查询注销和新开三存银行是否需要密码
     */
    public void needPwd(String tag, String session, String bank_no, final IChangeDepositBankResult changeDepositBankResult) {
        HashMap map = new HashMap();
        map.put("funcid", "711035");
        map.put("token", session);
        HashMap hashMap = new HashMap();
        hashMap.put("SEC_ID", "tpyzq");
        hashMap.put("FLAG", "true");
        hashMap.put("BANK_NO", bank_no);
        map.put("parms", hashMap);

        OkHttpUtil.okHttpForPostString(tag, ConstantUtil.URL_SCYHBG, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                if (changeDepositBankResult != null) {
                    changeDepositBankResult.loadingClose();
                    changeDepositBankResult.error(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (changeDepositBankResult != null) {
                    changeDepositBankResult.loadingClose();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");

                    if ("-6".equals(code)) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.toLogin();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.error(msg);
                        }
                        return;
                    }

                    JSONArray data = jsonObject.getJSONArray("data");

                    if (data == null || data.length() <= 0) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.error(ConstantUtil.SERVICE_NO_DATA);
                        }
                        return;
                    }

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);

                        String BANK_NO = object.optString("BANK_NO");
                        String BKOPENPWD_FLAG = object.optString("BKOPENPWD_FLAG");
                        String BKCANCELPWD_FLAG = object.optString("BKCANCELPWD_FLAG");
                        String BANK_NAME = object.optString("BANK_NAME");

                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.needPwd(BANK_NO, BKOPENPWD_FLAG, BKCANCELPWD_FLAG, BANK_NAME);
                        }
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    if (changeDepositBankResult != null) {
                        changeDepositBankResult.loadingClose();
                        changeDepositBankResult.error(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });
    }

    /**
     * 预销户处理
     */
    public void pretreatmentCloseUser(String tag, String session, String bankNo, String bankPassword, String accPassword, final IChangeDepositBankResult changeDepositBankResult) {
        HashMap map = new HashMap();
        map.put("funcid", "300610");
        map.put("token", session);
        HashMap hashMap = new HashMap();
        hashMap.put("SEC_ID", "tpyzq");
        hashMap.put("FLAG", "true");
        hashMap.put("BANK_NO", bankNo);
        hashMap.put("BK_PASSWORD", bankPassword);
        hashMap.put("ACC_PWD", accPassword);
        map.put("parms", hashMap);

        OkHttpUtil.okHttpForPostString(tag, ConstantUtil.URL_SCYHBG, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                if (changeDepositBankResult != null) {
                    changeDepositBankResult.loadingClose();
                    changeDepositBankResult.error(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (changeDepositBankResult != null) {
                    changeDepositBankResult.loadingClose();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");

                    if ("-6".equals(code)) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.toLogin();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (changeDepositBankResult != null) {
                            changeDepositBankResult.error(msg);
                        }
                        return;
                    }

                    if (changeDepositBankResult != null) {
                        changeDepositBankResult.closeAccountSuccess();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (changeDepositBankResult != null) {
                        changeDepositBankResult.loadingClose();
                        changeDepositBankResult.error(ConstantUtil.JSON_ERROR);
                    }
                }

            }
        });
    }


    /*** 三存银行变更 二级页面 接口及网络连接***/

    public interface IDoChangeDepBank {

        void loadingClose();

        void error(String errorMsg);//失败

        void toLogin();//注册或登录

        void getPdfInfo(String pdfName, String pdfUrl);

        void needInputPassword(String bkcancelPwd_flag);

        void changeBank(String stauts, String errorinfo);
    }

    /**
     * 获取协议pdf文档
     * @param bankNo
     */
    public void getPdf(String tag, String bankNo, final IDoChangeDepBank iDoChangeDepBank) {

        HashMap map = new HashMap();
        map.put("funcid", "900106");
        map.put("token", "");
        HashMap hashMap = new HashMap();
        hashMap.put("bankflag", bankNo);
        map.put("parms", hashMap);

        OkHttpUtil.okHttpForPostString(tag, ConstantUtil.getURL_NEW(), map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                if (iDoChangeDepBank != null) {
                    iDoChangeDepBank.loadingClose();
                    iDoChangeDepBank.error(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (iDoChangeDepBank != null) {
                    iDoChangeDepBank.loadingClose();
                }
//{"PageCount":"1","bytes":27,"CurCount":"2","totalCount":"2","data":[["1111","http://tnhq.tpyzq.com/Manager/attachment/201705/12/1494575656230.pdf","交行一步式协议"],["1111","http://tnhq.tpyzq.com/Manager/",""]],"code":"0"}
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg=jsonObject.optString("msg");

                    if (!"0".equals(code)) {
                        if (iDoChangeDepBank != null) {
                            if (TextUtils.isEmpty(msg)) {
                                msg = "服务未返回错误信息";
                            }
                            iDoChangeDepBank.error(msg);
                        }
                        return;
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (jsonArray == null || jsonArray.length() <= 0) {
                        return;
                    }

                    String pdfUrl = jsonArray.getJSONArray(0).optString(1);
                    String pdfName = jsonArray.getJSONArray(0).optString(2);

                    iDoChangeDepBank.getPdfInfo(pdfName, pdfUrl);

                } catch (Exception e) {
                    e.printStackTrace();
                    if (iDoChangeDepBank != null) {
                        iDoChangeDepBank.loadingClose();
                        iDoChangeDepBank.error(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });
    }

    /**
     * 判断是否需要密码
     * @param session
     * @param oldBankNo     老的银行账户， 也就是上一个界面传过来的银行账户
     */
    public void getNeedPassword(String tag, String session, String oldBankNo, final IDoChangeDepBank iDoChangeDepBank) {
        HashMap map = new HashMap();
        map.put("funcid", "711035");
        map.put("token", session);
        HashMap hashMap = new HashMap();
        hashMap.put("SEC_ID", "tpyzq");
        hashMap.put("FLAG", "true");
        hashMap.put("BANK_NO", oldBankNo);
        map.put("parms", hashMap);

        OkHttpUtil.okHttpForPostString(tag, ConstantUtil.URL_SCYHBG, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                if (iDoChangeDepBank != null) {
                    iDoChangeDepBank.loadingClose();
                    iDoChangeDepBank.error(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (iDoChangeDepBank != null) {
                    iDoChangeDepBank.loadingClose();
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");

                    if ("-6".equals(code)) {
                        if (iDoChangeDepBank != null) {
                            iDoChangeDepBank.toLogin();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (iDoChangeDepBank != null) {
                            iDoChangeDepBank.error(msg);
                        }
                        return;
                    }

                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data == null || data.length() <= 0) {
                        return;
                    }

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        String BKCANCELPWD_FLAG = object.optString("BKCANCELPWD_FLAG");

                        if (iDoChangeDepBank != null) {
                            iDoChangeDepBank.needInputPassword(BKCANCELPWD_FLAG);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (iDoChangeDepBank != null) {
                        iDoChangeDepBank.loadingClose();
                        iDoChangeDepBank.error(ConstantUtil.JSON_ERROR);
                    }
                }
            }
        });

    }


    /**
     * 三存银行变更
     * @param tag
     * @param session
     * @param oldBankNo         上一个界面传过来bankNo
     * @param selectBankNo      去银行列表，选择银行时，带回来的银行账户
     * @param extAcc            上一个界面传过来的Account
     * @param bk_password       原银行密码，需要711035来判断需不需要输入， 如果不需要就传空
     * @param inputBankNo       界面输入的银行账户
     * @param inputPassword     界面中需要输入的银行密码
     * @param currency          上一个界面传入过来的
     */
    public void changeBank(String tag, String session, String oldBankNo, String selectBankNo,
                           String extAcc, String bk_password, String inputBankNo,String inputPassword,
                           String currency, final IDoChangeDepBank iDoChangeDepBank) {
        HashMap map = new HashMap();
        map.put("funcid", "300604");
        map.put("token", session);
        HashMap hashMap = new HashMap();
        hashMap.put("SEC_ID", "tpyzq");
        hashMap.put("FLAG", "true");
        hashMap.put("BANK_NO", oldBankNo);//上一个界面传过来bankNo
        hashMap.put("BANK_ACCOUNT", extAcc); //上一个界面传过来的Account
        hashMap.put("BK_PASSWORD", bk_password);//原银行密码，需要711035来判断需不需要输入， 如果不需要就传空
        hashMap.put("BANK_NO_NEW", selectBankNo);//去银行列表，选择银行时，带回来的银行账户
        hashMap.put("BKACCOUNT_NEW", inputBankNo);//界面输入的银行账户
        hashMap.put("BK_PASSWORD_NEW", inputPassword);//界面中需要输入的银行密码
        hashMap.put("MONEY_TYPE", currency);    //上一个界面传入过来的
        map.put("parms", hashMap);

        OkHttpUtil.okHttpForPostString(tag, ConstantUtil.URL_SCYHBG, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                if (iDoChangeDepBank != null) {
                    iDoChangeDepBank.loadingClose();
                    iDoChangeDepBank.error(ConstantUtil.NETWORK_ERROR);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (iDoChangeDepBank != null) {
                    iDoChangeDepBank.loadingClose();
                }

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");

                    if ("-6".equals(code)) {
                        if (iDoChangeDepBank != null) {
                            iDoChangeDepBank.toLogin();
                        }
                        return;
                    }

                    if (!"0".equals(code)) {
                        if (iDoChangeDepBank != null) {
                            iDoChangeDepBank.error(msg);
                        }
                        return;
                    }

                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data == null || data.length() <= 0) {
                        return;
                    }

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        String ERROR_INFO = object.optString("ERROR_INFO");
                        String ACPT_STATUS = object.optString("ACPT_STATUS");
                        if (iDoChangeDepBank != null) {
                            iDoChangeDepBank.changeBank(ACPT_STATUS, ERROR_INFO);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (iDoChangeDepBank != null) {
                        iDoChangeDepBank.loadingClose();
                        iDoChangeDepBank.error(ConstantUtil.JSON_ERROR);
                    }
                }

            }
        });
    }

}
