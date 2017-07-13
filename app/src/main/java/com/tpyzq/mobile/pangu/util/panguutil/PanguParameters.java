package com.tpyzq.mobile.pangu.util.panguutil;


import com.tpyzq.mobile.pangu.R;
import com.tpyzq.mobile.pangu.activity.home.helper.HomeFragmentHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类用于获取app相关配置参数
 * Created by longfeng on 2017/3/1.
 */

public class PanguParameters {
    public static Map<String, Integer> homeGridHelper() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("我的钱包", R.mipmap.sy_wodeqianbao);
        map.put("新股", R.mipmap.sy_xingu);
        map.put("金融生活", R.mipmap.sy_jinrongshenghuo);
        map.put("资讯", R.mipmap.sy_zixun);
        map.put("资产分析", R.mipmap.sy_zichanfenxi);
        map.put("我的账户", R.mipmap.sy_wodezhanghu);
        map.put("交易动态", R.mipmap.sy_jiaoyidongtai);
        map.put("股市回忆录", R.mipmap.sy_gushihuiyilu);
//        map.put("股票买卖",R.mipmap.sy_gupiaomaimai);
        map.put("股票买入", R.mipmap.sybj_gupiaomairu);
        map.put("股票卖出", R.mipmap.sybj_gupiaomaichu);
        map.put("股票持仓", R.mipmap.sy_gupiaochicang);
        map.put("OTC持仓", R.mipmap.sy_otcchicang);
        map.put("可取资金", R.mipmap.sy_keyongzijin);
        map.put("银行转账", R.mipmap.sy_yinhangzhuanzhang);
        map.put("业务办理", R.mipmap.sy_yewubanli);
        map.put("搜索股票", R.mipmap.sy_sousuogupiao);
        map.put("自选股新闻", R.mipmap.sy_zixuanguxinwen);
        map.put("基金持仓", R.mipmap.sy_jijinchicang);
        map.put("我的消息", R.mipmap.sy_wodexiaoxi);
        map.put("自选股", R.mipmap.sy_zixuangu);
        map.put("热销理财", R.mipmap.sy_rexiaolicai);
        map.put("股市月账单", R.mipmap.sy_gushiyuezhangdan);
        map.put("热搜股票", R.mipmap.sy_jinriregu);
        map.put("开户", R.mipmap.open_user);
        map.put(HomeFragmentHelper.REVERSEREPO, R.mipmap.nihuigou);
        map.put("牛掌柜", R.mipmap.nihuigou);
        return map;
    }

    public static Map<String, Integer> getBankLogo() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", R.mipmap.bank_jt);
        map.put("2", R.mipmap.bank_ny);
        map.put("3", R.mipmap.bank_js);
        map.put("4", R.mipmap.bank_icbc);
        map.put("B", R.mipmap.bank_ms);
        map.put("6", R.mipmap.bank_gd);
        map.put("7", R.mipmap.bank_zg);
        map.put("9", R.mipmap.bank_xy);
        map.put("5", R.mipmap.bank_zs);
        map.put("a", R.mipmap.bank_pa);
        map.put("j", R.mipmap.bank_bj);
        map.put("g", R.mipmap.bank_cgb);
        map.put("h", R.mipmap.bank_hx);
        map.put("z", R.mipmap.bank_yz);
        map.put("N", R.mipmap.bank_nb);
        map.put("p", R.mipmap.bank_pf);
        map.put("s", R.mipmap.bank_sh);
        map.put("8", R.mipmap.bank_zx);
        return map;
    }

    public static Map<String, Integer> getBankBackground() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", R.mipmap.bg_bank_jt);
        map.put("2", R.mipmap.bg_bank_ny);
        map.put("3", R.mipmap.bg_bank_js);
        map.put("4", R.mipmap.bg_bank_gs);
        map.put("B", R.mipmap.bg_bank_ms);
        map.put("6", R.mipmap.bg_bank_gd);
        map.put("7", R.mipmap.bg_bank_zg);
        map.put("9", R.mipmap.bg_bank_xy);
        map.put("5", R.mipmap.bg_bank_zs);
        map.put("a", R.mipmap.bg_bank_pa);
        map.put("j", R.mipmap.bg_bank_bj);
        map.put("g", R.mipmap.bg_bank_gf);
        map.put("h", R.mipmap.bg_bank_hx);
        map.put("z", R.mipmap.bg_bank_yz);
        map.put("N", R.mipmap.bg_bank_nb);
        map.put("p", R.mipmap.bg_bank_pf);
        map.put("s", R.mipmap.bg_bank_sh);
        map.put("8", R.mipmap.bg_bank_zx);
        return map;
    }

    public static Map<String, Integer> getBankBackground2() {
        Map<String, Integer> map = new HashMap<>();
        map.put("gfyh", R.mipmap.guangfa);
        map.put("5555", R.mipmap.mingsheng);
        map.put("payh", R.mipmap.pingan);
        map.put("nbyh", R.mipmap.ningbo);
        map.put("bjyh", R.mipmap.beijing);
        map.put("hxyh", R.mipmap.huaxia);
        map.put("3333", R.mipmap.jianshe);
        map.put("2222", R.mipmap.nongye);
        map.put("1111", R.mipmap.jiaotong);
        map.put("7777", R.mipmap.zhongguo);
        map.put("shyh", R.mipmap.shanghai);
        map.put("yzcx", R.mipmap.youzheng);
        map.put("6666", R.mipmap.guangda);
        map.put("cmbc", R.mipmap.zhaoshang);
        map.put("4444", R.mipmap.gongshang);
        map.put("pfyh", R.mipmap.pufa);
        map.put("9999", R.mipmap.yingye);
        map.put("8888", R.mipmap.zhongxin);
        return map;
    }

    public static Map<String, Integer> getBankBgLogo() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", R.mipmap.bg_bank_jt_ic);
        map.put("2", R.mipmap.bg_bank_ny_ic);
        map.put("3", R.mipmap.bg_bank_js_ic);
        map.put("4", R.mipmap.bg_bank_gs_ic);
        map.put("B", R.mipmap.bg_bank_ms_ic);
        map.put("6", R.mipmap.bg_bank_gd_ic);
        map.put("7", R.mipmap.bg_bank_zg_ic);
        map.put("9", R.mipmap.bg_bank_xy_ic);
        map.put("5", R.mipmap.bg_bank_zs_ic);
        map.put("a", R.mipmap.bg_bank_pa_ic);
        map.put("j", R.mipmap.bg_bank_bj_ic);
        map.put("g", R.mipmap.bg_bank_gf_ic);
        map.put("h", R.mipmap.bg_bank_hx_ic);
        map.put("z", R.mipmap.bg_bank_yz_ic);
        map.put("N", R.mipmap.bg_bank_nb_ic);
        map.put("p", R.mipmap.bg_bank_pf_ic);
        map.put("s", R.mipmap.bg_bank_sh_ic);
        map.put("8", R.mipmap.bg_bank_zx_ic);
        return map;
    }
}
