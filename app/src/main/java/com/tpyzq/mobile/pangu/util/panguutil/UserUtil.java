package com.tpyzq.mobile.pangu.util.panguutil;


import com.tpyzq.mobile.pangu.data.UserEntity;
import com.tpyzq.mobile.pangu.db.Db_PUB_USERS;
import com.tpyzq.mobile.pangu.util.keyboard.KeyEncryptionUtils;

import java.util.List;

/**
 * Created by zhangwenbo on 2016/9/16.
 * 用户相关工具类
 */
public class UserUtil {

    public static String userId = Db_PUB_USERS.queryingRegister();  //查询手机  也是注册标示

    public static String Mobile = KeyEncryptionUtils.getInstance().localDecryptMobile();      //查询手机号

    public static String Typescno = Db_PUB_USERS.queryingTypescno();  //注册类型

    public static String capitalAccount = getAccountNumber();       //第一个资金账号

    public static String Keyboard = "0";

    private static String getAccountNumber() {
        String account = "";
        List<UserEntity> list = KeyEncryptionUtils.getInstance().localDecryptTradescno();

        if (list != null && list.size() > 0) {
            account = list.get(0).getTradescno();
        }

        if (account.contains(",")) {

            String[] accounts = account.split(",");

            return accounts[0];
        } else {
            return account;
        }

    }

    /**
     * 刷新数据
     */
    public static void refrushUserInfo() {
        userId = Db_PUB_USERS.queryingRegister();
        capitalAccount = getAccountNumber();
        Typescno = Db_PUB_USERS.queryingTypescno();  //注册类型
        Mobile = KeyEncryptionUtils.getInstance().localDecryptMobile();      //查询手机号

    }

    public static String getHeaderString(String s) {
        String backs = null;
        switch (s) {
            case "60前":
                backs = "退休不无聊，生活更精彩，追求投资，一直到老！";
                break;
            case "60后":
                backs = "历经沧桑，把握改革，我们是投资界的中坚力量！";
                break;
            case "70后":
                backs = "社会的中流砥柱，家里的顶梁柱，把握投资的方向，我们追求卓越！";
                break;
            case "80后":
                backs = "朝气蓬勃、勇于突破，我们虽然不一定有运气做富二代，但我有目标成为富一代";
                break;
            case "90后":
                backs = "个性张扬，不走寻常路，谁说花钱是年轻的标签，我说会赚钱才是！";
                break;
            case "高富帅":
                backs = "谁说我的天性仅仅是追求刺激，也许投资中偶尔会有稍许的冲动，但懂得听取和尊重他人的意见后，我一定会“身价倍涨”！";
                break;
            case "白富美":
                backs = "谁说我的天性仅仅是安定保守，也许投资中我经常瞻前顾后，但努力接受专业理财顾问的建议后，我的投资收益也能遍地开花！";
                break;
            case "闪电战":
                backs = "“短线是银”是我的投资信仰，运指如飞、杀伐果断，关注各项技术指标，股海市场任我遨游。";
                break;
            case "阵地战":
                backs = "中线投资的我们可谓是最庞大的群体，高抛低吸、运筹帷幄，“题材、趋势、价值”，作为中线投资的心法，我牢记于心。";
                break;
            case "持久战":
                backs = "“长线是金”是我的投资信仰，气定神闲、处之泰然，关注行业和个股的基本面，终有一日我将满载而归。";
                break;
            case "价值投资":
                backs = "主板投资是我的主战场，作为价值投资的高手，我更为关注“内在价值”的波动，市盈率、市净率往往是我投资关注的焦点。";
                break;
            case "独具匠心":
                backs = "中小板是牛股的发源地，我作为成长股投资法爱好者，掌握好行业和选股往往为我轻松带来高额回报，我还渴望在全球金融投资史上缔造另一个财富传奇。";
                break;
            case "新兴成长":
                backs = "创业板是“中国的纳斯达克”，我在这些高科技新兴产业上可下了不少功夫，点滴投入，超额回报，当然我不会忘记注意创业板投资的风险，亏损的公司可是会直接退市呢。";
                break;
            case "猛龙过江":
                backs = "港股是境外投资的首选，资金南下的投资新热点，沪港通、深港通AH两市不再有距离，我和东方之珠有个约会。";
                break;
            case "敢作敢为":
                backs = "我是个高风险的投资者，不入虎穴，焉得虎子，风险总是伴随着机遇，敢想才有机会，爱拼的我才会成功。";
                break;
            case "脚踏实地":
                backs = "我是个中风险的投资者，稳扎稳打，步步为营，经历牛熊轮回后，我懂得坚持平衡才是一种智慧。";
                break;
            case "聚沙成塔":
                backs = "我是个低风险的投资者，细水长流，聚沙成塔，不奢求大富大贵，我追求的是稳定的投资收益。";
                break;
        }
        return backs;
    }
}
