package com.tpyzq.mobile.pangu.util.panguutil;

import android.text.TextUtils;

import com.bonree.agent.android.harvest.Statistics;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangwenbo on 2016/10/24.
 */
public class BRutil {

    //1-新增，2-删除
    public static final String ACTIONREMOVESELFSTOCK = "2";
    public static final String ACTIONADDSELFSTOCK = "1";

    public static void doListenAddOrRemoveSelfStock(String stockNumber, String stockName, String action, String price) {

        String market = stockNumber.substring(0, 2);
        if (!market.equals("10") && !market.equals("20")) {
            Map<String, Object> map = new HashMap<>();
            map.put("reportid", "002002");
            if (stockNumber.length() == 8) {
                map.put("symbol", stockNumber);
            }
            map.put("name", stockName);
            map.put("action", action);

            if (TextUtils.isEmpty(price)) {
                price = "0.0";
            }
            map.put("price", String.valueOf(Math.round(Double.parseDouble(price))));
            Statistics.onEvent("Z1-1-2", "增删自选股动作", map);
        }
    }

    public static void doListenLookSelfStock(String stockNumber, String stockName, String price) {
        String market = stockNumber.substring(0, 2);
        if (!market.equals("10") && !market.equals("20")) {
            Map<String, Object> map = new HashMap<>();
            map.put("reportid", "002003");
            if (stockNumber.length() == 8) {
                map.put("symbol", stockNumber);
            }
            map.put("name", stockName);
            map.put("price", String.valueOf(Math.round(Double.parseDouble(price))));
            Statistics.onEvent("Z1-1-1", "查看自选股动作", map);
        }
    }


    /**
     * 首页：  N001我的钱包 N002新股 N003 金融生活N004 资讯 N005 资产分析 N006 我的账户
     * N007 交易动态 N008 股市回忆录 N009 股票买入 N010 股票卖出N011股票持仓 N012 OTC持仓
     * N013 基金持仓 N014 可用资金 N015 银行转账 N016 业务办理 N017 自选股 018 自选股新闻
     * N019 搜索股票 N020 业务办理 N021 我的消息 N022热销理财 N023 股市月账单 N024 今日热股
     * <p>
     * 行情：A001自选股 B001行情
     * <p>
     * 交易：C001 股票 C002开放式 C003 OTC
     * <p>
     * 我：K001 账户 M001钱包
     * <p>
     * 首页：z001
     * <p>
     * 行情：z002
     * <p>
     * 交易：z003
     * <p>
     * 我：    z004
     * <p>
     * 以后预制领航员：z005
     *
     * @param eventId
     */
    public static void menuSelect(String eventId) {

        Map<String, Object> map = new HashMap<>();
        map.put("reportid", "N1-1-1");
        map.put("name", eventId);
        /**
         * 埋点eventName在博睿管理端配置，重新上传不会覆盖,后端已经配置，传空字符串
         *  2017/4/20@zhangkl
         */
        Statistics.onEvent(eventId, "", map);

        /*if ("N001".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N001", "快捷菜单-我的钱包", map);
        } else if ("N002".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N002", "快捷菜单-新股", map);
        } else if ("N003".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N003", "快捷菜单-金融生活", map);
        } else if ("N004".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N004", "快捷菜单-资讯", map);
        } else if ("N005".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N005", "快捷菜单-资产分析", map);
        } else if ("N006".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N006", "快捷菜单-我的账户", map);
        } else if ("N007".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N007", "快捷菜单-交易动态 ", map);
        } else if ("N008".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N008", "快捷菜单-股市回忆录", map);
        } else if ("N009".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N009", "快捷菜单-股票买入", map);
        } else if ("N010".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N010", "快捷菜单-股票卖出", map);
        } else if ("N011".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N011", "快捷菜单-股票持仓", map);
        } else if ("N012".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N012", "快捷菜单-OTC持仓", map);
        } else if ("N013".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N013", "快捷菜单-基金持仓", map);
        } else if ("N014".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N014", "快捷菜单-可用资金", map);
        } else if ("N015".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N015", "快捷菜单-银行转账", map);
        } else if ("N016".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N016", "快捷菜单-业务办理", map);
        } else if ("N017".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N017", "快捷菜单-自选股", map);
        } else if ("N018".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N018", "快捷菜单-自选股新闻", map);
        } else if ("N019".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N019", "快捷菜单-搜索股票", map);
        } else if ("N020".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N020", "快捷菜单-业务办理", map);
        } else if ("N021".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N021", "快捷菜单-我的消息", map);
        } else if ("N022".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N022", "快捷菜单-热销理财", map);
        } else if ("N023".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N023", "快捷菜单-股市月账单", map);
        } else if ("N024".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("N024", "快捷菜单-今日热股", map);
        } else if ("A001".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("A001", "行情-自选", map);
        } else if ("B001".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("B001", "行情-行情", map);
        } else if ("C001".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("C001", "交易-股票", map);
        } else if ("C002".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("C002", "交易-开放式", map);
        } else if ("C003".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("C003", "交易-OTC", map);
        } else if ("K001".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("K001", "账户", map);
        } else if ("M001".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("M001", "钱包", map);
        } else if ("z001".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("z001", "导航-首页", map);
        } else if ("z002".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("z002", "导航-行情", map);
        } else if ("z003".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("z003", "导航-交易", map);
        } else if ("z004".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("z004", "导航-我", map);
        } else if ("z005".equals(name)) {
            map.put("name", name);
            Statistics.onEvent("z005", "导航-领航员", map);
        }*/
    }

    /**
     * 稳赢
     *
     * @param sessionld     上报接口编号
     * @param schema_id     方案ID
     * @param prod_code     产品代码 如何浏览方案，产品code为 -1
     * @param event_type    事件类型 1：浏览 2：点击 3：委托成功
     * @param event_time    事件时间 格式：YYYY-MM-DD HH:MM:SS
     * @param entrust_no    委托流水号 委托成功才会有流水号，其他为默认-1
     * @param entrust_money 委托金额  委托成功才会有流水号，其他为默认-1
     */

    public static void menuNewSelect(String sessionld, String schema_id, String prod_code, String event_type,
                                     Date event_time, String entrust_no, String entrust_money) {

        UserUtil.refrushUserInfo();

        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(sessionld)) {
            map.put("sessionld", sessionld);
        }

        if (!TextUtils.isEmpty(schema_id)) {
            map.put("schema_id", schema_id);
        }

        if (!TextUtils.isEmpty(prod_code)) {
            map.put("prod_code", prod_code);
        }

        UserUtil.refrushUserInfo();
        if (!Db_PUB_USERS.isRegister()) {
            map.put("client_type", "0");
            map.put("client_code", "-1");
        } else if (Db_PUB_USERS.isRegister() && UserUtil.capitalAccount == null) {
            map.put("client_type", "1");
            map.put("client_code", UserUtil.userId);
        } else if (Db_PUB_USERS.isRegister() && UserUtil.capitalAccount != null) {
            map.put("client_type", "2");
            map.put("client_code", UserUtil.capitalAccount);
        }

        if (!TextUtils.isEmpty(event_type)) {
            map.put("event_type", event_type);
        }

        if (event_time != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("event_time", sdf.format(event_time));
        }

        if (!TextUtils.isEmpty(entrust_no)) {
            map.put("entrust_no", entrust_no);
        }

        if (!TextUtils.isEmpty(entrust_money)) {
            map.put("entrust_money", entrust_money);
        }

        Statistics.onEvent("Z1-4-4", "直接营销结果上报", map);
    }
    public static void menuLogIn(String os_type, String phone_number, String open_udid, String version, String out_ip, String capital_account,
                                 String local_time) {
        HashMap<String, String> map = new HashMap<>();
        map.put("reportid", "k1-001-2");
        if (TextUtils.isEmpty(os_type)) {
            map.put("OS_type", "-1");
        } else {
            map.put("OS_type", os_type);
        }
        if (TextUtils.isEmpty(phone_number)) {
            map.put("phone_number", "-1");
        } else {
            map.put("phone_number", phone_number);
        }
        if (TextUtils.isEmpty(open_udid)) {
            map.put("open_UDID", "-1");
        } else {
            map.put("open_UDID", open_udid);
        }
        if (TextUtils.isEmpty(version)) {
            map.put("version", "-1");
        } else {
            map.put("version", version);
        }
        if (TextUtils.isEmpty(out_ip)) {
            map.put("out_IP", "-1");
        } else {
            map.put("out_IP", out_ip);
        }
        if (TextUtils.isEmpty(capital_account)) {
            map.put("capital_account", "-1");
        } else {
            map.put("capital_account", capital_account);
        }
        if (TextUtils.isEmpty(local_time)) {
            map.put("local_time", "-1");
        } else {
            map.put("local_time", local_time);
        }
        Statistics.onEvent("k1-001-2", "交易登录动作上报接口", map);
    }
}
