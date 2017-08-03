package com.tpyzq.mobile.pangu.http.doConnect.self;

import android.text.TextUtils;

import com.tpyzq.mobile.pangu.data.NewsInofEntity;
import com.tpyzq.mobile.pangu.data.StockInfoEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_STOCKLIST;
import com.tpyzq.mobile.pangu.http.NetWorkUtil;
import com.tpyzq.mobile.pangu.interfac.ICallbackResult;
import com.tpyzq.mobile.pangu.util.ConstantUtil;
import com.tpyzq.mobile.pangu.util.Helper;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by zhagnwenbo on 2016/9/13.
 * 自选股网络连接
 */
public class SelfChoiceStockNews {

    private ICallbackResult mCallbackResult;
    private static final String TAG = "SelfChoiceStockNews";
    private String mHttpTAG;
    private String mOffset;             //偏移量
    private String mStockNumber;

    public SelfChoiceStockNews(String httpTag, String pagerNumber, String stockNumber) {
        mHttpTAG = httpTag;
        mOffset = pagerNumber;
        mStockNumber = stockNumber;
    }

    public void doConnect(ICallbackResult callbackResult) {
        mCallbackResult = callbackResult;
        request();
    }

    private void request() {
        HashMap mapHQONG002 = new HashMap();
        mapHQONG002.put("FUNCTIONCODE", "HQONG002");
        mapHQONG002.put("TOKEN", "");
        mapHQONG002.put("url", ConstantUtil.getURL_HQ_WA());
        HashMap mapHQONG002_1 = new HashMap();

        String list = "";
        if (!TextUtils.isEmpty(mStockNumber)) {
//            mStockNumber = Helper.getStockNumber(mStockNumber);
            list = mStockNumber;
        } else {
            list = getStockCodeList();
        }

        mapHQONG002_1.put("code", list);
        mapHQONG002_1.put("offset",mOffset);
        mapHQONG002_1.put("limit","10");
        mapHQONG002.put("PARAMS", mapHQONG002_1);
        NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, ConstantUtil.getURL_HQ_WA(), mapHQONG002, new StringCallback() {
//            NetWorkUtil.getInstence().okHttpForPostString(mHttpTAG, FileUtil.URL_NEW_ZX, mapHQONG002, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                mCallbackResult.getResult("网络异常", TAG);
            }
            @Override
            public void onResponse(String response, int id) {

                if (TextUtils.isEmpty(response)) {
                    return;
                }
                try{
                    JSONObject res = new JSONObject(response);
                    if("200".equals(res.optString("code"))){
                        JSONArray jsonArray = res.getJSONArray("message");
                        List<NewsInofEntity> entities = new ArrayList<NewsInofEntity>();
                        if(null != jsonArray && jsonArray.length() >0){
                            for(int i = 0;i < jsonArray.length();i++){
                                JSONObject json = jsonArray.getJSONObject(i);
                                NewsInofEntity _bean = new NewsInofEntity();
                                _bean.setAuth(json.optString("auth"));
                                _bean.setDt(Long.valueOf(json.optString("dt")));
                                _bean.setId(json.optString("id"));
                                _bean.setSum(json.optString("sum").replace("#&#","").replace("\n","").trim());
                                _bean.setTitle(json.optString("title").replace("#&#","").trim());
                                JSONArray array = json.getJSONArray("relateList");
                                if(null != array && array.length() > 0){
                                    for(int j = 0;j < array.length();j++){
                                        JSONObject json1 = array.getJSONObject(j);
                                        String secu = json1.optString("code");
                                        String market = "";
                                        if (secu.contains("SZ")) {
                                            market = "90";
                                        } else if (secu.contains("SH")) {
                                            market = "83";
                                        }
                                        _bean.setStockCode(Helper.getStockCode(secu.substring(0,6), market));
                                        _bean.setComp(json1.optString("name"));
                                        _bean.setTick(json1.optString("tick"));
                                    }
                                    entities.add(_bean);
                                }
                            }
                        }
                        mCallbackResult.getResult(entities, TAG);
                    }else{
                        mCallbackResult.getResult("请求自选股新闻无数据", TAG);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    mCallbackResult.getResult("报文解析失败", TAG);
                }

                //{"message":[{"id":"5897e20ce4b0ccbcb1a825c8","dt":1486348194000,"relateList":[{"tick":"002101","name":"广东鸿图","code":"002101_SZ_EQ"}],"title":"新能源汽车产业发展进入新阶段 关注细分龙头(附股)","sum":"\n新能源汽车产业发展进入新阶段，关注新能源汽车龙头\n我们认为，2017新能源汽车推广目录发布，产业发展持续向好，关注江淮汽车等。此外，拥有业绩支撑和零部件深度国产替代优质标的如广东鸿图、广汇汽车、拓普集团、天成自控、赛轮金宇和银轮股份等值得关注。本周长期投资组合为：广东鸿图（外延+轻量化+国改）、潍柴动力（重卡+海外布局）、天成自控（关键零部件国产替代）、广汇汽车（经销商龙头+业绩高增长）、银轮股份（快速拓展乘用车业务）、拓普集团（NVH龙头+智能化）、均胜电子（外延+智能驾驶）、上汽集团（自主合资产品力齐上行）、江淮汽车（大众合资+SUV）、康盛股份（新能源汽车产业链）。","stockList":[{"tick":"600418","name":"江淮汽车","code":"600418_SH_EQ"},{"tick":"600104","name":"上汽集团","code":"600104_SH_EQ"},{"tick":"600166","name":"福田汽车","code":"600166_SH_EQ"},{"tick":"000800","name":"一汽轿车","code":"000800_SZ_EQ"},{"tick":"000625","name":"长安汽车","code":"000625_SZ_EQ"},{"tick":"002418","name":"康盛股份","code":"002418_SZ_EQ"},{"tick":"002101","name":"广东鸿图","code":"002101_SZ_EQ"},{"tick":"600297","name":"广汇汽车","code":"600297_SH_EQ"},{"tick":"603085","name":"天成自控","code":"603085_SH_EQ"},{"tick":"002126","name":"银轮股份","code":"002126_SZ_EQ"},{"tick":"601689","name":"拓普集团","code":"601689_SH_EQ"}],"auth":"谢志才"},{"id":"58956481e4b0ccbcb1a80d94","dt":1486185340000,"relateList":[{"tick":"601099","name":"太平洋","code":"601099_SH_EQ"}],"title":"国内首单ppp资产证券化项目落地","sum":"\n【财新网】（记者刘彩萍）发改委联合证监会发布《关于推进传统基础设施领域政府和社会资本合作（PPP）项目资产证券化相关工作的通知》一个多月后，国内首单PPP资产证券化项目落地。\n2月3日，太平洋证券新水源污水处理服务收费收益权资产支持专项计划在机构间私募产品报价与服务系统成功发行，总规模8.4亿元，采用结构化分层设计，其中优先级8亿元，共分为10档，评级均为AA+；次级0.4亿元。\n本次专项计划由新疆昆仑新水源科技股份有限公司（下称新水源公司）作为发起人并担任特定原始权益人，由太平洋证券股份有限公司担任计划管理人并进行交易安排。","stockList":[{"tick":"601099","name":"太平洋","code":"601099_SH_EQ"},{"tick":"300070","name":"碧水源","code":"300070_SZ_EQ"}],"auth":"财新网"},{"id":"58954033e4b0ccbcb1a80b1e","dt":1486175964000,"relateList":[{"tick":"601099","name":"太平洋","code":"601099_SH_EQ"}],"title":"国内首单ppp资产证券化项目发行 总规模8.4亿","sum":"\n本次纳入基础资产的甘泉堡工业园区污水处理PPP项目隶属于乌鲁木齐昆仑环保集团有限公司PPP项目，是乌鲁木齐市先行尝试的少数PPP项目之一，后者已纳入财政部公布的PPP项目库。甘泉堡经济技术开发区(工业区)，位于新疆首府乌鲁木齐市北部，距市中心55公里，紧邻五家渠市和阜康市，为乌昌地区东线工业走廊核心节点，总体规划面积360平方公里。其中乌鲁木齐范围内规划建设用地面积171平方公里，2012年9月15日经国务院批准设立为国家级经济技术开发区。开发区以新能源和优势资源深度开发利用为主，区内有国家重点工程北水南调“引额济乌”项目的尾部调节水库“500”水库，区位、水土资源优势明显，系乌鲁木齐确定的六大产业基地之一“战略性新兴产业基地”，是新疆新型工业化重点建设工业区。","stockList":[{"tick":"601099","name":"太平洋","code":"601099_SH_EQ"},{"tick":"300070","name":"碧水源","code":"300070_SZ_EQ"}],"auth":"证券时报网"},{"id":"5888413de4b0ccbcb1a79752","dt":1485324000000,"relateList":[{"tick":"603358","name":"华达科技","code":"603358_SH_EQ"}],"title":"华达科技a股挂牌 行业龙头借上市东风跨越发展","sum":"\n摘要\n【华达科技A股挂牌行业龙头借上市东风跨越发展】华达汽车科技股份有限公司于1月25日起在上海证券交易所主板上市即封涨停，股票简称为“华达科技”，股票代码为“603358”，公司首次公开发行不超过4000万股，本次发行价格为31.18元/股。\n华达汽车科技股份有限公司于1月25日起在上海证券交易所主板上市即封涨停，股票简称为“华达科技”，股票代码为“603358”，公司首次公开发行不超过4000万股，本次发行价格为31.18元/股。\n资料显示，华达科技成立于2002年，在乘用车零部件研发、生产上积累了二十多年的经验，掌握了丰富的车身总成冲压、焊接生产和研发技术，具备了较强的市场竞争力。","stockList":[{"tick":"603358","name":"华达科技","code":"603358_SH_EQ"}],"auth":""},{"id":"588707a9e4b0ccbcb1a77407","dt":1485243676000,"relateList":[{"tick":"603358","name":"华达科技","code":"603358_SH_EQ"}],"title":"华达科技等五新股1月25日上市 定位分析","sum":"\n同兴达1月25日中小板上市定位分析\n深圳同兴达科技股份有限公司于2017年1月25日起在深交所中小板上市。公司首次公开发行不超过24,00万股，本次发行价格为15.99元/股，市盈率为22.99倍，股票简称为“同兴达”，股票代码为“002845”。\n【公司简介】\n深圳同兴达科技股份有限公司是一家专业开发设计、生产、销售液晶显示模块（LCM）的高科技民营企业。公司产品涵盖了COB、TAB、COG等LCD模块产品，TFT、CSTN等彩色LCD显示产品，以及OLED显示产品。","stockList":[{"tick":"603358","name":"华达科技","code":"603358_SH_EQ"},{"tick":"300578","name":"会畅通讯","code":"300578_SZ_EQ"},{"tick":"002845","name":"同兴达","code":"002845_SZ_EQ"},{"tick":"300600","name":"瑞特股份","code":"300600_SZ_EQ"},{"tick":"603966","name":"法兰泰克","code":"603966_SH_EQ"}],"auth":"中国证券网"},{"id":"588683eae4b0ccbcb1a75e34","dt":1485209640000,"relateList":[{"tick":"300128","name":"锦富新材","code":"300128_SZ_EQ"}],"title":"操盘必读：1月24日证券市场要闻","sum":"\n宝钢股份：本次合并异议股东现金选择权股权登记日为1月23日，股票自1月24日起开始连续停牌，直至本次合并的换股完成后复牌。\n博世科：拟推10转15派0.6.\n国海证券：大股东再增持1831万股，董事拟增持不超1亿元。\n全志科技：2016年度拟10转10派3，副总拟减持不超20万股。\n恒锋信息：中签号出炉，末“四”位数：2752、7752；末“五”位数：64941、84941、04941、24941、44941；末“六”位数：720556、845556、970556、095556、220556、345556、470556、595556。","stockList":[{"tick":"603208","name":"欧派股份","code":"603208_SH_EQ"},{"tick":"603839","name":"安正时尚","code":"603839_SH_EQ"},{"tick":"002023","name":"海特高新","code":"002023_SZ_EQ"},{"tick":"001696","name":"宗申动力","code":"001696_SZ_EQ"},{"tick":"002253","name":"川大智胜","code":"002253_SZ_EQ"},{"tick":"002651","name":"利君股份","code":"002651_SZ_EQ"},{"tick":"300159","name":"新研股份","code":"300159_SZ_EQ"},{"tick":"002576","name":"通达动力","code":"002576_SZ_EQ"},{"tick":"300424","name":"航新科技","code":"300424_SZ_EQ"},{"tick":"002338","name":"奥普光电","code":"002338_SZ_EQ"},{"tick":"300306","name":"远方光电","code":"300306_SZ_EQ"},{"tick":"002444","name":"巨星科技","code":"002444_SZ_EQ"},{"tick":"300394","name":"天孚通信","code":"300394_SZ_EQ"},{"tick":"300128","name":"锦富新材","code":"300128_SZ_EQ"},{"tick":"601106","name":"中国一重","code":"601106_SH_EQ"},{"tick":"300458","name":"全志科技","code":"300458_SZ_EQ"}],"auth":""},{"id":"58860439e4b0ccbcb1a75659","dt":1485172800000,"relateList":[{"tick":"603358","name":"华达科技","code":"603358_SH_EQ"}],"title":"法兰泰克、华达科技25日在上交所上市","sum":"\n中证网讯（记者周松林）据上交所消息，法兰泰克重工股份有限公司、华达汽车科技股份有限公司A股股票将于2017年1月25日在上交所上市交易。\n法兰泰克重工股份有限公司A股股本为16,000万股，本次上市数量为4,000万股，证券简称为“法兰泰克”，证券代码为“603966”。华达汽车科技股份有限公司A股股本为16,000万股，本次上市数量为4,000万股，证券简称为\"华达科技\"，证券代码为\"603358\"。","stockList":[{"tick":"603966","name":"法兰泰克","code":"603966_SH_EQ"},{"tick":"603358","name":"华达科技","code":"603358_SH_EQ"}],"auth":"中国证券报·中证网"},{"id":"5885a543e4b0ccbcb1a74485","dt":1485152772000,"relateList":[{"tick":"300128","name":"锦富新材","code":"300128_SZ_EQ"}],"title":"oled概念股活跃 菲利华涨停","sum":"\n菲利华（个股资料操作策略盘中直播我要咨询）\n证券之星讯周一，OLED概念股盘中表现活跃。个股中菲利华涨停，强力新材涨逾4%，锦富新材、濮阳惠成、锡业股份等涨幅超3%。","stockList":[{"tick":"300395","name":"菲利华","code":"300395_SZ_EQ"},{"tick":"300128","name":"锦富新材","code":"300128_SZ_EQ"},{"tick":"300481","name":"濮阳惠成","code":"300481_SZ_EQ"},{"tick":"000960","name":"锡业股份","code":"000960_SZ_EQ"}],"auth":"证券之星综合"},{"id":"588599b1e4b0ccbcb1a7427c","dt":1485150060000,"relateList":[{"tick":"300128","name":"锦富新材","code":"300128_SZ_EQ"}],"title":"千亿元互联网投资基金成立 掘金三大领域（附基）","sum":"\n有市场人士表示，在行业整体景气度向上，高成长性的背景下，互联网板块存在技术性反弹诉求。而国家队领投的千亿元规模互联网投资基金的成立，短期将成为互联网板块的重大利好。\n在经历了PC电脑(互联网，1.0时代)、智能手机(移动互联网，2.0时代)两代硬件产品革新带来的“硬件+软件+商业模式”的洗牌后，以物联网为基础的互联网3.0时代(人机交互、万物互联)是对现有格局的颠覆，物联网相关产业的市场空间也最为广阔。相关受益上市公司主要有：远方光电、巨星科技、天孚通信、锦富新材等。","stockList":[{"tick":"300306","name":"远方光电","code":"300306_SZ_EQ"},{"tick":"002444","name":"巨星科技","code":"002444_SZ_EQ"},{"tick":"300394","name":"天孚通信","code":"300394_SZ_EQ"},{"tick":"300128","name":"锦富新材","code":"300128_SZ_EQ"}],"auth":"中证网"},{"id":"58856157e4b0ccbcb1a7389f","dt":1485135874000,"relateList":[{"tick":"300128","name":"锦富新材","code":"300128_SZ_EQ"}],"title":"石墨烯概念发力 博云新材涨停","sum":"\n博云新材（个股资料操作策略盘中直播我要咨询）\n证券之星讯周一早盘，石墨烯概念股盘中发力。个股中博云新材涨停，南洋科技、拓邦股份、锦富新材、大富科技、中科电气等涨幅居前。","stockList":[{"tick":"002389","name":"南洋科技","code":"002389_SZ_EQ"},{"tick":"002139","name":"拓邦股份","code":"002139_SZ_EQ"},{"tick":"300128","name":"锦富新材","code":"300128_SZ_EQ"},{"tick":"300134","name":"大富科技","code":"300134_SZ_EQ"},{"tick":"300035","name":"中科电气","code":"300035_SZ_EQ"}],"auth":"证券之星综合"}],"code":"200","type":"SUCCESS"}

                //{"code":"10101","type":"NO_PERMISSION","message":null}

                //{"code":"200","type":"SUCCESS","message":[{"stockList":[{"secu":"300104_SZ_EQ","comp":"乐视网","tick":"300104"},{"secu":"600497_SH_EQ","comp":"驰宏锌锗","tick":"600497"},{"secu":"600016_SH_EQ","comp":"民生银行","tick":"600016"},{"secu":"600528_SH_EQ","comp":"中铁二局","tick":"600528"},{"secu":"000338_SZ_EQ","comp":"潍柴动力","tick":"000338"},{"secu":"000001_SZ_EQ","comp":"平安银行","tick":"000001"}],"relateList":[{"secu":"000001_SZ_EQ","comp":"平安银行","tick":"000001"}],"auth":"山东神光","sum":"#\u0026#15日大盘震荡明显，从当天融资角度看，沪市融资买入额355.12亿元，较前一交易日减少116.49亿元，深市融资买入额268.08亿元，较前一交易日减少59.45亿元，从融资余额和融资买入额数据可以看到，随着市场的不断调整，融资买入额出现大幅回落，资金在调整中谨慎性不断增强，但融资余额方面也已实现七连增，市场企稳仍需后期不断试探，短线投资者可逢低布局不宜追高。#\u0026#两市融资买入额环比减少22.02%，其中沪市环比减少24.7%，深市环比减少18.15%；融资余额方面，两市融资余额环比增加0.51%。","id":"582bb482e4b0b74e24606dab","title":"16日融资融券：融资余额七连增","dt":1479258740000},{"stockList":[{"secu":"600798_SH_EQ","comp":"宁波海运","tick":"600798"},{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"},{"secu":"600018_SH_EQ","comp":"上港集团","tick":"600018"},{"secu":"601008_SH_EQ","comp":"连云港","tick":"601008"},{"secu":"601989_SH_EQ","comp":"中国重工","tick":"601989"},{"secu":"600575_SH_EQ","comp":"皖江物流","tick":"600575"},{"secu":"600428_SH_EQ","comp":"中远航运","tick":"600428"},{"secu":"000039_SZ_EQ","comp":"中集集团","tick":"000039"},{"secu":"600751_SH_EQ","comp":"天海投资","tick":"600751"},{"secu":"601866_SH_EQ","comp":"中海集运","tick":"601866"},{"secu":"601872_SH_EQ","comp":"招商轮船","tick":"601872"}],"relateList":[{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"}],"auth":"","sum":"#\u0026#中国重工方面，公司是中船重工集团公司确定的上市旗舰，中船重工是中国最大的造修船集团之一，拥有46个工业企业、28个科研院所，总资产2221亿元。#\u0026#中国重工作为造船产业龙头股之一，受到多家机构的看好。银河证券表示，整合初见成效，预计全年将扭亏。公司产品结构的改善及海洋核动力平台的建设将有效改善公司经营情况，预计公司2016年和2017年每股收益分别为0.06元和0.10元。公司作为集团海洋装备总装平台，关注后续相关的资本运作，维持“推荐”评级。东兴证券则表示，造船企业经营困难的局面仍将持续，预计未来公司将持续推进供给侧改革，通过降低成本、剔除持续亏损子公司等手段，提高产品毛利率，给予“推荐”评级。","id":"582b51f8e4b0b74e2460515f","title":"bdi指数突破千点 港口等三板块机会凸显","dt":1479233040000},{"stockList":[{"secu":"601818_SH_EQ","comp":"光大银行","tick":"601818"},{"secu":"600000_SH_EQ","comp":"浦发银行","tick":"600000"},{"secu":"601998_SH_EQ","comp":"中信银行","tick":"601998"}],"relateList":[{"secu":"600000_SH_EQ","comp":"浦发银行","tick":"600000"}],"auth":"每日经济新闻","sum":"#\u0026#每经记者朱丹丹#\u0026#11月15日，光大银行发布2016年第一次临时股东大会决议公告显示(以下简称公告)，光大银行关于设立信用卡业务独立法人机构的议案审议结果为通过，即已获得出席会议股东或股东代表所持有效表决权股份总数的2/3以上通过。#\u0026#事实上，早在9月初，光大银行就曾发布公告称，拟独资设立信用卡业务独立法人机构，公司名称暂定为\"中国光大信用卡有限责任公司\"，投资金额不超过100亿元。#\u0026#当时,公告进一步表示，首先，信用卡业务实行公司化运作,可以根据自身需求制定更加切合业务特点的经营管理策略,有利于促进信用卡业务的专业化经营,适应不断升级的消费、金融和服务需求。","id":"582b3651e4b0b74e24604aff","title":"光大银行信用卡业务“分拆”议案获股东大会通过","dt":1479225724000},{"stockList":[{"secu":"002174_SZ_EQ","comp":"游族网络","tick":"002174"},{"secu":"300467_SZ_EQ","comp":"迅游科技","tick":"300467"},{"secu":"002082_SZ_EQ","comp":"栋梁新材","tick":"002082"},{"secu":"300077_SZ_EQ","comp":"国民技术","tick":"300077"},{"secu":"002339_SZ_EQ","comp":"积成电子","tick":"002339"},{"secu":"002443_SZ_EQ","comp":"金洲管道","tick":"002443"},{"secu":"002066_SZ_EQ","comp":"瑞泰科技","tick":"002066"},{"secu":"000151_SZ_EQ","comp":"中成股份","tick":"000151"},{"secu":"300103_SZ_EQ","comp":"达刚路机","tick":"300103"},{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"},{"secu":"601008_SH_EQ","comp":"连云港","tick":"601008"},{"secu":"002408_SZ_EQ","comp":"齐翔腾达","tick":"002408"},{"secu":"002098_SZ_EQ","comp":"浔兴股份","tick":"002098"},{"secu":"600843_SH_EQ","comp":"上工申贝","tick":"600843"}],"relateList":[{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"}],"auth":"","sum":"#\u0026#网络游戏板块午后突然发力，中青宝（300052）、盛天网络（300494）瞬间拉板，游族网络（002174）、掌趣科技（300315）、迅游科技（300467）、暴风集团（300431）等直线拉升，带动创业板指数翻红。游戏概念近期却一直不温不火，处于低位滞胀状态。今日突然发力有补涨因素。主板方面，酿酒板块午后出现补涨行情：通葡股份（600365）、酒鬼酒（000799）、金种子酒（600199）、山西汾酒（600809）、ST皇台涨幅超2%。#\u0026#认为市场已经连续三天出现风格转换的苗头，连续多日拉动指数的煤炭、券商今日集体调整，创业板继续领涨两市。","id":"582ab523e4b0b74e245ff0e6","title":"【11月15日巨丰收评】权重集体打压大盘 创业板接力上涨","dt":1479193397000},{"stockList":[{"secu":"601008_SH_EQ","comp":"连云港","tick":"601008"},{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"},{"secu":"600575_SH_EQ","comp":"皖江物流","tick":"600575"}],"relateList":[{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"}],"auth":"优品财经","sum":"#\u0026#优品财富讯11月15日，受益BDI指数连续回暖，港口航运股早盘大幅高开，而后稍有回落，但仍维持高位震荡走势，截至发稿，板块涨1.53%，个股方面，连云港、大连港一字涨停，盐田港涨5.05%，营口港涨2.96%，皖江物流涨2.75%，唐山港涨2.55%。#\u0026#市场分析人士预计，随着BDI指数的连续回暖，大宗商品的交易活跃和价格回升，这些BDI指数正相关的海运个股的业绩回暖将在2016年年报中体现。此外，美股海运股近期连续大涨也印证了基本面改善预期，从供给侧改革到需求端回暖，海运股目前阶段性超额收益明显。","id":"582aa2d1e4b0b74e245fc79d","title":"快讯：bdi指数回暖刺激港口股大涨 连云港等两股一字涨停","dt":1479188031000},{"stockList":[{"secu":"600036_SH_EQ","comp":"招商银行","tick":"600036"},{"secu":"000001_SZ_EQ","comp":"平安银行","tick":"000001"}],"relateList":[{"secu":"000001_SZ_EQ","comp":"平安银行","tick":"000001"}],"auth":"中国经济网","sum":"#\u0026#1989年9至1992年7月，于山东大学外国语言文学专业获硕士学位；#\u0026#1992年7月至2001年9月，历任中国银行山东省分行外汇资金处交易员、纽约分行资金部高级交易员、山东省分行外汇资金处主任科员、法兰克福分行资金部副经理、经理；#\u0026#2001年9月至2007年1月，历任招商银行资金交易中心副主任、资金交易部副总经理；#\u0026#2007年1月至2013年10月，历任平安银行(原深圳发展银行)资金总监、首席资金执行官；#\u0026#2013年10月至2016年10月，历任浙江稠州商业银行常务副行长、行长。","id":"582a8e60e4b069b45b89426e","title":"何之江任平安银行副行长","dt":1479182040000},{"stockList":[{"secu":"601008_SH_EQ","comp":"连云港","tick":"601008"},{"secu":"600428_SH_EQ","comp":"中远航运","tick":"600428"},{"secu":"601919_SH_EQ","comp":"中国远洋","tick":"601919"},{"secu":"600798_SH_EQ","comp":"宁波海运","tick":"600798"},{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"},{"secu":"601872_SH_EQ","comp":"招商轮船","tick":"601872"}],"relateList":[{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"}],"auth":"","sum":"#\u0026#周二早盘，沪指小幅低开，个股涨跌互半。盘面上，港口航运集体大涨。其中，连云港、大连港开盘涨停，盐田港、唐山港等涨幅超过5%。#\u0026#消息面上，有媒体称，奥巴马政府上周五宣布正式放弃跨太平洋伙伴关系协定（TPP），该协议包含亚太地区的12个国家，是奥巴马政府“重返亚太”战略的一个重要组成部分。而美国白宫经济顾问委员会3日发布研究报告称，如果美国不批准IPP，涵盖中国、日本和东盟国家的区域全面经济伙伴关系协定（RCEP）将会填补IPP失败的空白。习近平主席将出席于11月19日至20日在秘鲁利马举行的亚太经合组织（APEC）第二十四次领导人非正式会议。","id":"582a7345e4b069b45b891c25","title":"港口航运集体大涨 连云港等两股涨停","dt":1479176460000},{"stockList":[{"secu":"000507_SZ_EQ","comp":"珠海港","tick":"000507"},{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"},{"secu":"601008_SH_EQ","comp":"连云港","tick":"601008"}],"relateList":[{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"}],"auth":"优品财经","sum":"#\u0026#优品财富讯11月15日早盘，港口运输板块大涨，延续了昨日的强悍表显，连云港、大连港开盘一字涨停，其他个股方面，截至发稿，唐山港涨4.7%，营口港涨4.3%，盐田港涨3.5%，锦州港涨3.3%，宁波港涨2.6%，珠海港涨2.6%，日照港涨2.4%，天津港涨1.9%。#\u0026#近期波罗的海干散货指数(BDI)大幅上涨创年内新高，上周五更是突破千点大关报收于1045点。此次，指数破千主要受大型干散货船——海岬型船运价大涨推动。有行业分析师认为，目前的运价水平已经接近船东的盈亏平衡点，干散货市场有好转的迹象。","id":"582a73abe4b069b45b891d8f","title":"快讯：国际海运市场回暖　港口运输板块维持强势表现","dt":1479174615000},{"stockList":[{"secu":"600428_SH_EQ","comp":"中远航运","tick":"600428"},{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"},{"secu":"601008_SH_EQ","comp":"连云港","tick":"601008"}],"relateList":[{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"}],"auth":"","sum":"#\u0026#新浪财经讯11月15日消息，周二早盘，港口航运板块维持强势，大连港早盘一字涨停。其他个股中，截至发稿，连云港涨停，营口港涨6%，唐山港、中远航运涨5%。#\u0026#有行业分析师认为，目前的运价水平已经接近船东的盈亏平衡点，干散货市场有好转的迹象。#\u0026#上海航交所近期发布的报告显示，随着铁矿石价格不断攀升，中国与澳大利亚、巴西之间的铁矿石船运价格对BDI指数的影响较大。近期，中国对进口煤炭、粮食的需求增多，则带动了相关船型的运价。中国进口干散货运输市场多数航线平均租金、运价都高于上年同期。","id":"582a6793e4b069b45b88f3bf","title":"快讯：港口航运板块持续强势 大连港一字涨停","dt":1479173580000},{"stockList":[{"secu":"601008_SH_EQ","comp":"连云港","tick":"601008"},{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"}],"relateList":[{"secu":"601000_SH_EQ","comp":"唐山港","tick":"601000"}],"auth":"证券之星综合","sum":"#\u0026#连云港（个股资料操作策略盘中直播我要咨询）#\u0026#证券之星讯周二早盘，港口概念爆发。个股中连云港、大连港涨停，营口港涨超6%，唐山港、营口港涨幅超4%。","id":"582a6be8e4b069b45b89097f","title":"港口概念爆发 2股强势涨停","dt":1479173456000}]}

//                ObjectMapper objectMapper = JacksonMapper.getInstance();
//
//                try {
//                    Map<String, Object> responseValues = objectMapper.readValue(response, new HashMap<String, Object>().getClass());
//
//                    String code = "";
//                    if (null != responseValues.get("code")) {
//                        code = String.valueOf(responseValues.get("code"));
//                    }
//
//                    String type = "";
//                    if (null != responseValues.get("type")) {
//                        type = String.valueOf(responseValues.get("type"));
//                    }
//
//                    if (!TextUtils.isEmpty(type) && !"SUCCESS".equals(type)) {
//                        mCallbackResult.getResult("code =" +code + "type =" + type, TAG);
//                        return;
//                    }
//
//                    if (null != responseValues.get("message") && responseValues.get("message") instanceof List) {
//                        List<Object> message = (List<Object>) responseValues.get("message");
//
//                        List<NewsInofEntity> entities = new ArrayList<NewsInofEntity>();
//                            for (Object object : message) {
//                                if (null != object && object instanceof Map) {
//                                    NewsInofEntity _bean = new NewsInofEntity();
//
//                                    Map<String, Object> params = (Map<String, Object>) object;
//
//                                    String dt = "";
//                                    if (null != params.get("dt")) {
//                                        dt = String.valueOf(params.get("dt"));
//                                    }
//
//                                    String title = "";
//                                    if (null != params.get("title")) {
//                                        title = String.valueOf(params.get("title"));
//                                    }
//
//                                    String newsId = "";
//                                    if (null != params.get("id")) {
//                                        newsId = String.valueOf(params.get("id"));
//                                    }
//
//                                    String sum = "";
//                                    if (null != params.get("sum")) {
//                                        sum = String.valueOf(params.get("sum"));
//                                        sum.replace("#&#", "");
//                                        sum.replace("\n","");
//                                        sum.trim();
//                                    }
//
//                                    String auth = "";
//                                    if (null != params.get("auth")) {
//                                        auth = String.valueOf(params.get("auth"));
//                                    }
//
//                                    _bean.setAuth(auth);  // 作者
//
//                                    if (!TextUtils.isEmpty(dt)) {
//                                        _bean.setDt(Long.valueOf(dt));//时间
//                                    }
//
//                                    _bean.setId(newsId);      //id
//
//                                    if (!TextUtils.isEmpty(sum)) {
//                                        _bean.setSum(sum);    //内容
//                                    }
//
//                                    if (!TextUtils.isEmpty(title)) {
//                                        _bean.setTitle(title.replace("#&#", ""));
//                                    }
//
//                                    if (null != params.get("relateList") && params.get("relateList") instanceof List) {
//                                        List<Map<String, Object>> subData = (List<Map<String, Object>>) params.get("relateList");
//
//                                        if (subData != null && subData.size() > 0) {
//                                            for (Map<String, Object> subValues : subData) {
//                                                String secu = "";
//
//                                                if (null != subValues.get("code")) {
//                                                    secu = String.valueOf(subValues.get("code"));
//                                                }
//
//                                                String comp = "";
//                                                if (null != subValues.get("name")) {
//                                                    comp = String.valueOf(subValues.get("name"));
//                                                }
//
//                                                String tick = "";
//                                                if (null != subValues.get("tick")) {
//                                                    tick = String.valueOf(subValues.get("tick"));
//                                                }
//
//                                                String market = "";
//                                                if (secu.contains("SZ")) {
//                                                    market = "90";
//                                                } else if (secu.contains("SH")) {
//                                                    market = "83";
//                                                }
//
//                                                secu = secu.substring(0, 6);
//                                                _bean.setStockCode(Helper.getStockCode(secu, market));
//                                                _bean.setComp(comp);
//                                                _bean.setTick(tick);
//                                            }
//                                        }
//
//                                        entities.add(_bean);
//                                    }
//                                }
//                            }
//                            boolean isSuccessForAddSelfNewsDb = Db_HOME_INFO.addStockListDatas(entities);
//                            if (!isSuccessForAddSelfNewsDb) {
////                                Helper.getInstance().showToast(CustomApplication.getContext(), "批量添加自选股新闻失败");
//                            }
//                            mCallbackResult.getResult(entities, TAG);
//                        }
//                } catch (Exception e) {
//                    e.printStackTrace();
////                    mCallbackResult.getResult(""+ e.toString(), TAG);
//                }
            }
        });
    }

    private String getStockCodeList() {
        List<StockInfoEntity> datas = Db_PUB_STOCKLIST.queryStockListDatas();
        if (datas != null && datas.size() > 0) {
            StringBuilder sb1 = new StringBuilder();
            for (int i = 0; i < datas.size(); i++) {
                if (i < 10) {
                    String stockNumber = datas.get(i).getStockNumber();
                    if (!TextUtils.isEmpty(stockNumber) && !stockNumber.startsWith("10") && !stockNumber.startsWith("20")) {
                         String _stockNumber = stockNumber;
                        if (i == 9) {
                            sb1.append(_stockNumber).append("");
                        } else {
                            sb1.append(_stockNumber).append(",");
                        }
                    }
                }
            }

            String result = sb1.toString();

            if (TextUtils.isEmpty(result) || result.length() <= 0) {
                return "";
            }

            String reglex  = result.substring(result.length() - 1, result.length());
            if (",".equals(reglex)) {
                result = result.substring(0, result.length() - 1);
            }

            return result;
        }

        return "";
    }
}