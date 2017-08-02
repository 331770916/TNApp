package com.tpyzq.mobile.pangu.activity.home.helper;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.tpyzq.mobile.pangu.activity.home.HomeFragment;
import com.tpyzq.mobile.pangu.activity.home.MoreActivity;
import com.tpyzq.mobile.pangu.activity.home.SearchActivity;
import com.tpyzq.mobile.pangu.activity.home.hotsearchstock.HotSearchStockActivity;
import com.tpyzq.mobile.pangu.activity.home.information.InformationHomeActivity;
import com.tpyzq.mobile.pangu.activity.home.managerMoney.OptionalFinancingActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.AssetsAnalysisActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.FinancialLifeActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.MyNewsActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.StockBillActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.StockRecallActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.TradingDynamicsActivity;
import com.tpyzq.mobile.pangu.activity.myself.account.UsableCapitalActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AccountPowerActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AgreementActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.AgreementSignActvity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.PersonalDataActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.RiskEvaluationActivity;
import com.tpyzq.mobile.pangu.activity.myself.handhall.StockHolderInfoActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiVerificationActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.ShouJiZhuCeActivity;
import com.tpyzq.mobile.pangu.activity.myself.login.TransactionLoginActivity;
import com.tpyzq.mobile.pangu.activity.trade.open_fund.FundShareActivity;
import com.tpyzq.mobile.pangu.activity.trade.otc_business.OTC_ShareActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BanksTransferAccountsActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.BuyAndSellActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.MySubscribeActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.NewStockSubscribeActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.OneKeySubscribeActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.ReverseRepoGuideActivity;
import com.tpyzq.mobile.pangu.activity.trade.stock.TakeAPositionActivity;
import com.tpyzq.mobile.pangu.base.CustomApplication;
import com.tpyzq.mobile.pangu.data.InformationEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.tpyzq.mobile.pangu.util.SpUtils;
import com.tpyzq.mobile.pangu.util.panguutil.BRutil;
import com.tpyzq.mobile.pangu.util.panguutil.PanguParameters;
import com.tpyzq.mobile.pangu.util.panguutil.UserUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangwnebo on 2016/10/27.
 * 增加首页资讯获取解析公用方法getInfoListfromJson  by   longfeng
 * 增加开户页面跳转
 */
public class HomeFragmentHelper implements HomeSubject {
    public static final String MYWALLET = "我的钱包";
    public static final String NEWSTOCK = "新股";
    public static final String MESSAGE = "资讯";
    public static final String FLINKLIFE = "金融生活";
    public static final String APPROACH = "资产分析";
    public static final String MYACCOUNT = "我的账户";
    public static final String TRANSACTION = "交易动态";
    public static final String MARKETMEMORY = "股市回忆录";
    public static final String STOCKBUY = "股票买入";
    public static final String STOCKSELL = "股票卖出";
    public static final String HOLDSTOCK= "股票持仓";
    public static final String OTCHOLD = "OTC持仓";
    public static final String ADVISABLEFUND = "可取资金";
    public static final String BANKTRANSFER = "银行转账";
    public static final String BUSINESSHANDLING = "业务办理";
    public static final String SEARCHSTOCK = "搜索股票";
    public static final String NEWSSELFCHOICE = "自选股新闻";
    public static final String HOLDFUND = "基金持仓";
    public static final String MYINFO = "我的消息";
    public static final String SELFCHOICESTOCK = "自选股";
    public static final String HOTMONEY = "热销理财";
    public static final String STOCKBILL = "股市月账单";
    public static final String HOTSEARCH = "热搜股票";
    public static final String AMAZING = "稳赢理财";
    public static final String ALL = "全部";
    public static final String OPEN_USER = "开户";
    public static final String PRODUCT_ORDER = "产品预约";
    public static final String REVERSEREPO = "国债逆回购";

    private static HomeFragmentHelper mHomeFragmentHelper;

    private HomeFragmentHelper() {
    }

    public static HomeFragmentHelper getInstance() {
        if (mHomeFragmentHelper == null) {
            mHomeFragmentHelper = new HomeFragmentHelper();
        }

        return mHomeFragmentHelper;
    }

    public static ArrayList<HomeObsever> mOberservers = new ArrayList<>();

    public static ArrayList<Map<String, Object>> titleNames(String strArrayTitles) {

        ArrayList<Map<String, Object>> dataSourceList = new ArrayList<>();

        Map<String, Integer> parames = PanguParameters.homeGridHelper();

        if (!TextUtils.isEmpty(strArrayTitles)) {
            if (strArrayTitles.contains(",")) {
                String[] strTitles = strArrayTitles.split(",");
                for (String titles : strTitles) {
                    HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
                    itemHashMap.put("img", parames.get(titles));
                    itemHashMap.put("title", titles);

                    dataSourceList.add(itemHashMap);
                }

            } else {
                HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
                itemHashMap.put("img", parames.get(strArrayTitles));
                itemHashMap.put("title", strArrayTitles);
                dataSourceList.add(itemHashMap);
            }
        }

        return dataSourceList;
    }

    public  ArrayList<InformationEntity> getInfoListfromJson(String json,int count) throws JSONException{
        ArrayList<InformationEntity> informationEntities = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            if("200".equals(jsonObject.optString("code"))){
                JSONArray message = jsonObject.getJSONArray("message");
                if((message != null && message.length() > 0)){
                    int length = message.length() >= count ? count : message.length(); //判断取出条数
                    for (int i = 0; i < length; i++) {
                        InformationEntity bean = new InformationEntity();
                        JSONObject data = message.getJSONObject(i);
                        //.....解析存储的数据
                        informationEntities.add(bean);
                    }
                }
            }
        return  informationEntities;
    }


    public void gotoPager(String title, Activity activity, HomeFragment.JumpPageListener jumpPageListener, Object object) {
        Intent intent = new Intent();
        if (MYWALLET.equals(title)) {
            if (jumpPageListener != null) {
                SpUtils.putBoolean(CustomApplication.getContext(), "burse", true);
                SpUtils.putBoolean(CustomApplication.getContext(), "account", false);
                jumpPageListener.onCheckedChanged(3);
                BRutil.menuSelect("N001");
            }
        } else if (NEWSTOCK.equals(title)) {
            if (jumpPageListener != null) {
                ConstantUtil.jumpHangqing = true;
                jumpPageListener.onCheckedChanged(1);
                mHomeFragmentHelper.notifyObservers(1, "hangqing");
            }
            BRutil.menuSelect("N002");
        } else if (FLINKLIFE.equals(title)) {
            intent.setClass(activity, FinancialLifeActivity.class);
            activity.startActivity(intent);
            BRutil.menuSelect("N003");

        } else if (MESSAGE.equals(title)) {
            intent.setClass(activity, InformationHomeActivity.class);
            activity.startActivity(intent);
            BRutil.menuSelect("N004");
        } else if (APPROACH.equals(title)) {
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_AssetsAnalysis, intent);
            BRutil.menuSelect("N005");
        } else if (MYACCOUNT.equals(title)) {
            if (jumpPageListener != null) {
                SpUtils.putBoolean(CustomApplication.getContext(), "burse", false);
                SpUtils.putBoolean(CustomApplication.getContext(), "account", true);
                jumpPageListener.onCheckedChanged(3);
                BRutil.menuSelect("N006");
            }
        } else if (TRANSACTION.equals(title)) {
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_TradingDynamics, intent);
            BRutil.menuSelect("N007");
        } else if (MARKETMEMORY.equals(title)) {
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_StockRecall, intent);
            BRutil.menuSelect("N008");
        } else if (STOCKBUY.equals(title)) {
            intent.putExtra("status", ",买");
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_BUY_SELL, intent);
            BRutil.menuSelect("N009");
        } else if (STOCKSELL.equals(title)) {
            intent.putExtra("status", "卖");
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_BUY_SELL, intent);
            BRutil.menuSelect("N010");
        } else if (HOLDSTOCK.equals(title)) {
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_TakeAPosition, intent);
            BRutil.menuSelect("N011");
        } else if (OTCHOLD.equals(title)) {
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_OTCTakeAPosition, intent);
            BRutil.menuSelect("N012");
        } else if (ADVISABLEFUND.equals(title)) {
            //新宇
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_UsableCapital, intent);
            BRutil.menuSelect("N014");

        } else if (BANKTRANSFER.equals(title)) {
            intent.putExtra("tag", "100");
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_BanksTransferAccounts, intent);
            BRutil.menuSelect("N015");
        } else if (BUSINESSHANDLING.equals(title)) {
            if (jumpPageListener != null) {
                SpUtils.putBoolean(CustomApplication.getContext(), "burse", true);
                SpUtils.putBoolean(CustomApplication.getContext(), "account", false);
                jumpPageListener.onCheckedChanged(3);
            }
            BRutil.menuSelect("N016");
        } else if (SEARCHSTOCK.equals(title)) {
            intent.setClass(activity, SearchActivity.class);
            activity.startActivity(intent);
            BRutil.menuSelect("N019");
        } else if (NEWSSELFCHOICE.equals(title)) {
            if (jumpPageListener != null) {
                ConstantUtil.jumpSelfChoiceNews = true;
                jumpPageListener.onCheckedChanged(1);
                mHomeFragmentHelper.notifyObservers(1, "selfChoiceNews");
            }
            BRutil.menuSelect("N018");
        } else if (HOLDFUND.equals(title)) {
            //新宇
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_FundShare, intent);
            BRutil.menuSelect("N013");
        } else if (MYINFO.equals(title)) {
//            ArrayList<MyNomePageEntity> entities = (ArrayList<MyNomePageEntity>) object;
//            intent.putParcelableArrayListExtra("MyNomePageEntity", entities);
//            intent.putExtra("identify", "true");
            intent.setClass(activity, MyNewsActivity.class);
            activity.startActivity(intent);
            BRutil.menuSelect("N021");
        } else if (SELFCHOICESTOCK.equals(title)) {
            if (jumpPageListener != null) {
                ConstantUtil.jumpSelfChoiceNews = false;
                jumpPageListener.onCheckedChanged(1);
                mHomeFragmentHelper.notifyObservers(0, "selfChoice");
            }
            BRutil.menuSelect("N017");
        } else if (HOTMONEY.equals(title)) {
            intent.setClass(activity, OptionalFinancingActivity.class);
            activity.startActivity(intent);
            BRutil.menuSelect("N022");
        } else if (STOCKBILL.equals(title)) {
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_StockBillActivity, intent);
            BRutil.menuSelect("N023");
        } else if (HOTSEARCH.equals(title)) {
            intent.setClass(activity, HotSearchStockActivity.class);
            activity.startActivity(intent);
            BRutil.menuSelect("N024");
        } else if (AMAZING.equals(title)) {
//            BRutil.menuNewSelect("z001-1", "2", "p2", "2", new Date(), "-1", "-1");     稳赢埋点
            BRutil.menuSelect("N022");
//            intent.setClass(activity, HotSellActivity.class);
            intent.setClass(activity, OptionalFinancingActivity.class);
            activity.startActivity(intent);
        } else if (ALL.equals(title)) {
            intent.setClass(activity, MoreActivity.class);
            activity.startActivity(intent);
        } else if (OPEN_USER.equals(title)) {
//            OpeningAnAccountDialog openingAnAccount = new OpeningAnAccountDialog(activity);
//            openingAnAccount.show();
           intent = new Intent();
            intent.putExtra("type", 0);//开户 ，开户传此，
            intent.putExtra("channel", ConstantUtil.OPEN_ACCOUNT_CHANNEL);// 开户id
            intent.setClass(activity, com.cairh.app.sjkh.MainActivity.class);
            activity.startActivity(intent);
        }else if (REVERSEREPO.equals(title)) {
            gotoPage(activity, TransactionLoginActivity.PAGE_INDEX_ReverseRepoGuideActivity, intent);
        } else if ("牛掌柜".equals(title)) {
            Toast.makeText(activity, "进入牛掌柜", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void registerObserver(HomeObsever observer) {
        mOberservers.add(observer);
    }

    @Override
    public void removeObserver(HomeObsever observer) {
        int num = mOberservers.indexOf(observer);
        if (num >= 0) {
            mOberservers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(int positon, String tag) {
        for (HomeObsever observer : mOberservers) {
            observer.update(positon, tag);
        }
    }

    public void gotoPage(Activity activity, int a_pageIndex, Intent intent) {
        intent.putExtra("pageindex", a_pageIndex);
        if (!Db_PUB_USERS.isRegister()) {
            intent.setClass(activity, ShouJiZhuCeActivity.class);
        } else {
            if (!TextUtils.isEmpty(UserUtil.Mobile)) {
                if ("true".equals(Db_PUB_USERS.queryingIslogin())) {
                    switch (a_pageIndex) {
                        case TransactionLoginActivity.PAGE_INDEX_FINLIFE:
                            intent.setClass(activity, FinancialLifeActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_NewStockSubscribe:
                            intent.setClass(activity, NewStockSubscribeActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_AssetsAnalysis:
                            intent.setClass(activity, AssetsAnalysisActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_TradingDynamics:
                            intent.setClass(activity, TradingDynamicsActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_StockRecall:
                            intent.setClass(activity, StockRecallActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_BUY_SELL:
                            intent.setClass(activity, BuyAndSellActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_TakeAPosition:
                            intent.setClass(activity, TakeAPositionActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_BanksTransferAccounts:
                            intent.setClass(activity, BanksTransferAccountsActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_StockBillActivity:
                            intent.setClass(activity, StockBillActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_OTCTakeAPosition:
                            intent.setClass(activity, OTC_ShareActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_UsableCapital:
                            intent.setClass(activity, UsableCapitalActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_FundShare:
                            intent.setClass(activity, FundShareActivity.class);
                            break;
//                        case TransactionLoginActivity.PAGE_INDEX_PrecontractLoadActivity:
//                            intent.setClass(activity, PrecontractLoadActivity.class);
//                            break;
                        case TransactionLoginActivity.PAGE_INDEX_AccountPowerActivity:  //我的账户权限
                            intent.setClass(activity, AccountPowerActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_Agreement: //电子签名约定书
                            intent.setClass(activity, AgreementActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_RiskEvaluation: //风险测评
                            intent.setClass(activity, RiskEvaluationActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_PersonalData: //修改个人资料
                            intent.setClass(activity, PersonalDataActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_Information://股东资料
                            intent.setClass(activity, StockHolderInfoActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_AgreementSigned://退市和风险警示协议签署
                            intent.setClass(activity, AgreementSignActvity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_ReverseRepoGuideActivity:
                            intent.setClass(activity, ReverseRepoGuideActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_ManagerMySubscribeActivity:
                            intent.setClass(activity, MySubscribeActivity.class);
                            break;
                        case TransactionLoginActivity.PAGE_INDEX_OneKeySubscribe:
                            intent.setClass(activity, OneKeySubscribeActivity.class);
                            break;

//                        case TransactionLoginActivity.PAGE_INDEX_Password://修改密码
//                            intent.setClass(activity, Password.class);
//                            break;
//                        case TransactionLoginActivity.PAGE_INDEX_ChangBank: //三存银行变更
//                            intent.setClass(activity, ChangBank.class);
//                            break;
//                        case TransactionLoginActivity.PAGE_INDEX_StartyUpBoardActivity://创业板转签
//                            intent.setClass(activity, StartyUpBoardActivity.class);
//                            break;
//                        case TransactionLoginActivity.PAGE_INDEX_InputFoundInfoActivity://修改身份证有效期填写信息页面
//                            intent.setClass(activity, LoadingIDCardUpdateActivity.class);
//                            break;
                    }
                } else {
                    intent.setClass(activity, TransactionLoginActivity.class);
                }

            } else {
                    intent.setClass(activity, ShouJiVerificationActivity.class);
            }


        }
        activity.startActivity(intent);
    }
}
