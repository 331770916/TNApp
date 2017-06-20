package com.tpyzq.mobile.pangu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangqi on 2016/11/19.
 */

public class HtmlUtil {
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符

    public static String delHTMLTag(String htmlStr) {
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}