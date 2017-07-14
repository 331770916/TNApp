package com.tpyzq.mobile.pangu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据转换处理类
 */
public class TransitionUtils {
    // dp转换px
    public static int dp2px(int dp, Context context) {
        // px = dp * 密度比
        float density = context.getResources().getDisplayMetrics().density;// 0.75 1 1.5 2

        return (int) (dp * density + 0.5f);// 4舍5入
    }

    //double取小数点后2位
    public static String string2doubleS(String s) {
        if (!s.equals("")) {
            DecimalFormat df = new DecimalFormat("######0.00");
//            double d = Double.parseDouble(s);
            BigDecimal b = new BigDecimal(s);
            double c = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return df.format(c);
        } else {
            return "0.00";
        }
    }

    //根据字符串转换成double类型并乘以100保留2位小数
    public static String s2d2(String s) {
        if (!s.equals("")) {
            DecimalFormat df = new DecimalFormat("######0.00");
//            double d = Double.parseDouble(s);
            BigDecimal b = new BigDecimal(s);
            double c = b.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            return df.format(b);
        } else {
            return "0.00";
        }
    }

    //根据字符串转换成double类型保留4位小数
    public static String s2d4(String s) {
        if (!TextUtils.isEmpty(s) && !"-".equals(s)) {
            DecimalFormat df = new DecimalFormat("######0.0000");
//            double d = Double.parseDouble(s);
            BigDecimal b = new BigDecimal(s);
            double c = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            return df.format(c);
        } else {
            return "-";
        }
    }

    //double取小数点后2位
    public static String string2doubleS3(String s) {
        if (!s.equals("")) {
            DecimalFormat df = new DecimalFormat("######0.000");
//            double d = Double.parseDouble(s);
            BigDecimal b = new BigDecimal(s);
            double c = b.setScale(3, BigDecimal.ROUND_DOWN).doubleValue();
            return df.format(c);
        } else {
            return "0.000";
        }

    }
    //double取小数点后4位
    public static String string2double4(String s) {
        if (!s.equals("")) {
            DecimalFormat df = new DecimalFormat("######0.0000");
//            double d = Double.parseDouble(s);
            BigDecimal b = new BigDecimal(s);
            double c = b.setScale(4, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            return df.format(c);
        } else {
            return "0.0000";
        }
    }

    //double不取小数点
    public static String string2doubleS4(String s) {
        if (!s.equals("")) {
            DecimalFormat df = new DecimalFormat("######0");
//            double d = Double.parseDouble(s);
            BigDecimal b = new BigDecimal(s);
            double c = b.setScale(0, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            return df.format(c);
        } else {
            return "0";
        }
    }

    public static String fundPirce(String code, String price) {
        String market = code.substring(0, 2);
        String backNum = "";
        switch (market) {
            case "10":
                backNum = string2doubleS(price);
                break;
            case "11":
                backNum = string2doubleS(price);
                break;
            case "12":
                backNum = string2doubleS3(price);
                break;
            case "13":
                backNum = string2doubleS3(price);
                break;
            case "15":
                backNum = string2doubleS3(price);
                break;
            case "16":
                backNum = string2doubleS(price);
                break;
            case "17":
                backNum = string2doubleS3(price);
                break;
            case "20":
                backNum = string2doubleS(price);
                break;
            case "21":
                backNum = string2doubleS(price);
                break;
            case "22":
                backNum = string2doubleS(price);
                break;
            case "23":
                backNum = string2doubleS3(price);
                break;
            case "24":
                backNum = string2doubleS3(price);
                break;
            case "26":
                backNum = string2doubleS(price);
                break;
            case "27":
                backNum = string2doubleS(price);
                break;
            case "28":
                backNum = string2doubleS(price);
                break;
            case "29":
                backNum = string2doubleS3(price);
                break;
            default:
                backNum = "0";
                break;
        }
        return backNum;
    }

    //字符串转换成double类型
    public static double string2double(String s) {
        try{
            if (!s.equals("")) {
                return Double.parseDouble(s);
            } else {
                return 0;
            }
        }catch (Exception e){
            return 0;
        }
    }
    //字符串转换成int类型
    public static int string2int(String s) {
        try{
            if (!s.equals("")) {
                return Integer.parseInt(s);
            } else {
                return 0;
            }
        }catch (Exception e){
            return 0;
        }
    }
    /**
     * 判断字符串是否是中文
     *
     * @param str
     * @return
     */
    public static boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    //字符串转换成日期格式long类型
    public static long string2dateL(String s) {
        if (!s.equals("")) {
            String ss = s.substring(0, 4) + s.substring(5, 7) + s.substring(8, 10);
            return Long.parseLong(ss);
        } else {
            return 0;
        }
    }

    /**
     * 让Listview高度自适应
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //字符串转换成日期格式int类型
    public static int string2dateI(String s) {
        if (!s.equals("")) {
            String ss = s.substring(0, 4) + s.substring(5, 7) + s.substring(8, 10);
            return Integer.parseInt(ss);
        } else {
            return 0;
        }
    }

    //转换成亿或者万数据（字符串可为double类型long类型）
    public static String long2million(String s) {
        DecimalFormat df = new DecimalFormat("######0.00");
        if (!s.contains("E")) {
            if (!s.equals("")) {
                if (s.contains(" ")) {
                    return "0.00";
                } else {
                    double d = Double.parseDouble(s);
                    if (Math.abs(d) > 100000000) {
                        return df.format(d / 100000000) + "亿";
                    } else if (Math.abs(d) > 10000) {
                        return df.format(d / 10000) + "万";
                    } else {
                        return df.format(d);
                    }
                }
            } else {
                return "0.00";
            }
        } else {
            BigDecimal bd = new BigDecimal(s);
            String ss = bd.toPlainString();
            double d = Double.parseDouble(ss);
            if (Math.abs(d) > 100000000) {
                return df.format(d / 100000000) + "亿";
            } else if (Math.abs(d) > 10000) {
                return df.format(d / 10000) + "万";
            } else {
                return df.format(d);
            }
        }
    }

    //把String类型自动转换成万或亿类型数据
    public static String s2million(String s) {
        if (!TextUtils.isEmpty(s) && !"-".equals(s)) {
            DecimalFormat df = new DecimalFormat("######0.00");
            DecimalFormat dz = new DecimalFormat("######0");
            double d = Double.parseDouble(s);
            if (Math.abs(d) > 100000000) {
                if (isInt(d/100000000)){
                    return (int) d / 100000000 + "亿";
                }else {
                    return df.format(d)+"亿";
                }
            } else if (Math.abs(d) > 10000) {
                if (isInt(d/10000)){
                    return (int) d / 10000 + "万";
                }else {
                    return df.format(d)+"万";
                }
            } else if (Math.abs(d) >= 1) {
                if (isInt(d)){
                    return (int)d+"";
                }else {
                    return dz.format(d);
                }
            }else {
                return df.format(d);
            }
        } else {
            return "-";
        }
    }
    public static boolean isInt(double d){
        if ((int)d == d){
            return true;
        }else {
            return false;
        }
    }

    //转换成亿或者万数据（字符串可为double类型long类型）
    public static String long2string(String s) {
        DecimalFormat df = new DecimalFormat("######0.00");
        if (!s.contains("E")) {
            if (!s.equals("")) {
                if (s.contains(" ")) {
                    return "0.00";
                } else {
                    double d = Double.parseDouble(s);
                    if (Math.abs(d) >= 100000000) {
                        return df.format(d / 100000000) + "亿";
                    } else if (Math.abs(d) >= 10000) {
                        return df.format(d / 10000) + "万";
                    } else {
                        return "" + (int) d;
                    }
                }
            } else {
                return "0.00";
            }
        } else {
            BigDecimal bd = new BigDecimal(s);
            String ss = bd.toPlainString();
            double d = Double.parseDouble(ss);
            if (Math.abs(d) >= 100000000) {
                return df.format(d / 100000000) + "亿";
            } else if (Math.abs(d) >= 10000) {
                return df.format(d / 10000) + "万";
            } else {
                return "" + (int) d;
            }
        }
    }

    //转换成亿或者万数据（字符串可为double类型long类型）
    public static String price2tenthousand(String s) {
        try {
            DecimalFormat df = new DecimalFormat("######0.00");
            if (!s.contains("E")) {
                if (!s.equals("")) {
                    if (s.contains(" ")) {
                        return "0.00";
                    } else {
                        double d = Double.parseDouble(s);
                        return df.format(d / 10000);
                    }
                } else {
                    return "0.00";
                }
            } else {
                BigDecimal bd = new BigDecimal(s);
                String ss = bd.toPlainString();
                double d = Double.parseDouble(ss);
                return df.format(d / 10000);
            }
        } catch (Exception e) {
            return "0.00";
        }

    }

    //转换成亿或者万数据（字符串可为double类型long类型）
    public static float price2tenthousandF(String s) {
        DecimalFormat df = new DecimalFormat("######0.00");
        if (!s.contains("E")) {
            if (!s.equals("")) {
                if (s.contains(" ")) {
                    return Float.parseFloat("0.00");
                } else {
                    float d = Float.parseFloat(s);
                    return Float.parseFloat(df.format(d / 10000));
                }
            } else {
                return Float.parseFloat("0.00");
            }
        } else {
            BigDecimal bd = new BigDecimal(s);
            String ss = bd.toPlainString();
            float d = Float.parseFloat(ss);
            return Float.parseFloat(df.format(d / 10000));
        }
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dp2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String delHeaderZero(String tempString) {
        int initlen = tempString.length();
        char[] chars = tempString.toCharArray();
        for (int i = 0; i < initlen; i++) {
            if (chars[i] == '0') {
                chars[i] = ' ';
            } else {
                break;
            }
        }
        return String.valueOf(chars).trim();
    }
}
