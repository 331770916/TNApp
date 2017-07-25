package com.tpyzq.mobile.pangu.db.util;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author huwwds
 * @title CursorUtils
 * @description Sqlite游标工具类
 * @time 2016/6/13 17:43
 */
public class CursorUtils {

    /**
     * @param cursor     游标
     * @param columnName 列名
     * @return 列值
     */
    public static Integer getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    /**
     * @param cursor     游标
     * @param columnName 列名
     * @return 列值
     */
    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    /**
     * @param cursor     游标
     * @param columnName 列名
     * @return 列值
     */
    public static Float getFloat(Cursor cursor, String columnName) {
        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }

    /**
     * @param cursor     游标
     * @param columnName 列名
     * @return 列值
     */
    public static byte[] getBlob(Cursor cursor, String columnName) {
        return cursor.getBlob(cursor.getColumnIndex(columnName));
    }

    /**
     * @param cursor     游标
     * @param columnName 列名
     * @return 列值
     */
    public static Double getDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    /**
     * @param cursor     游标
     * @param columnName 列名
     * @return 列值
     */
    public static Long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    /**
     * @param cursor     游标
     * @param columnName 列名
     * @return 列值
     */
    public static Short getShort(Cursor cursor, String columnName) {
        return cursor.getShort(cursor.getColumnIndex(columnName));
    }

    /**
     * @param cursor     游标
     * @param columnName 列名
     * @return 列值
     */
    public static Date getDate(Cursor cursor, String columnName) {
        String valueStr = cursor.getString(cursor.getColumnIndex(columnName));
        if (StringUtils.isEmpty(valueStr))
            return null;
        try {
//            long dateVal = new Date(valueStr).getTime();
            long dateVal = Long.parseLong(valueStr);
            Date date = new Date(dateVal);
            return date;
        } catch (Exception e) {
            // 日期型(YYYY-MM-DD)字符串长度
            int length = 10;
            if (valueStr.length() == length) {
                // Date型
                return DateUtils.parseToDate(valueStr, DateUtils.defaultPattern);
            } else if (valueStr.length() > length) {
                // DateTime型
                return DateUtils.parseToDate(valueStr, DateUtils.fullPattern);
            }
            return null;
        }
    }


    /**
     * @param cursor     游标
     * @param columnName 列名
     * @return 列值
     */
    public static BigDecimal getDecimal(Cursor cursor, String columnName) {
        Double val = null;
        try {
            val = cursor.getDouble(cursor.getColumnIndex(columnName));
            if (val == null) throw new ParseException("getDecimal 转换异常", 0);
        } catch (Exception e) {
            e.printStackTrace();
            String valStr = cursor.getString(cursor.getColumnIndex(columnName));
            if (StringUtils.isEmpty(valStr)) return null;
            return new BigDecimal(valStr).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 将结果集映射到java bean
     *
     * @param <T>          Java bean 类型
     * @param beanClass    the bean class
     * @param cursor       sqlite 结果集游标
     * @param ignoreFields 要忽略的字段
     * @return the to bean
     * @throws IllegalAccessException the illegal access exception
     * @throws InstantiationException the instantiation exception
     * @author xiejinhong
     * @title mapToBean
     * @date 2016 -06-18 11:55:45
     */
    public static <T> T mapToBean(Class<T> beanClass, Cursor cursor, String... ignoreFields) throws IllegalAccessException, InstantiationException {
        List<Field> fields = ReflectUtils.getAllFields(beanClass);
        T t = beanClass.newInstance();
        String[] columnNames = cursor.getColumnNames();
        List<String> columns = Arrays.asList(columnNames);
        for (Field field : fields) {
            setFieldValue(t, field, cursor, columns, ignoreFields);
        }
        return t;
    }

    /**
     * 将结果集映射到java bean
     *
     * @param <T>       Java bean 类型
     * @param beanClass the bean class
     * @param cursor    sqlite 结果集游标
     * @return the to bean
     * @throws IllegalAccessException the illegal access exception
     * @throws InstantiationException the instantiation exception
     * @author xiejinhong
     * @title mapToBean
     * @date 2016 -06-18 11:55:45
     */
    public static <T> T mapToBean(Class<T> beanClass, Cursor cursor) throws IllegalAccessException, InstantiationException {
        List<Field> fields = ReflectUtils.getAllFields(beanClass);
        T t = beanClass.newInstance();
        String[] columnNames = cursor.getColumnNames();
        List<String> columns = Arrays.asList(columnNames);
        for (Field field : fields) {
            setFieldValue(t, field, cursor, columns);
        }
        return t;
    }

    /**
     * Sets field value.
     * 设置字段值
     *
     * @param <T>          the type parameter
     * @param t            the t
     * @param field        the field
     * @param cursor       the cursor
     * @param columns      the columns
     * @param ignoreFields the ignore fields
     * @throws IllegalAccessException the illegal access exception
     */
    private static <T> void setFieldValue(T t, Field field, Cursor cursor, List<String> columns, String... ignoreFields) throws IllegalAccessException {
        Class<?> type = field.getType();
        //解析字段名称
        String columnName = ReflectUtils.parseFieldName(field);
        boolean ignore = ignoreFields != null && Arrays.asList(ignoreFields).contains(field.getName());
        if (!columns.contains(columnName) || ignore)
            return;
        field.setAccessible(true);
        if (String.class.equals(type)) {
            field.set(t, CursorUtils.getString(cursor, columnName));
        } else if (Integer.class.equals(type)) {
            field.set(t, CursorUtils.getInt(cursor, columnName));
        } else if (Date.class.equals(type)) {
            field.set(t, CursorUtils.getDate(cursor, columnName));
        } else if (BigDecimal.class.equals(type)) {
            field.set(t, CursorUtils.getDecimal(cursor, columnName));
        } else if (Float.class.equals(type)) {
            field.set(t, CursorUtils.getFloat(cursor, columnName));
        } else if (Short.class.equals(type)) {
            field.set(t, CursorUtils.getShort(cursor, columnName));
        } else if (Double.class.equals(type)) {
            field.set(t, CursorUtils.getDouble(cursor, columnName));
        } else if (Byte[].class.equals(type)) {
            field.set(t, CursorUtils.getBlob(cursor, columnName));
        } else if (Long.class.equals(type)) {
            field.set(t, CursorUtils.getLong(cursor, columnName));
        }
    }

    /**
     * Sets field value.
     * 设置字段值
     *
     * @param <T>     the type parameter
     * @param t       the t
     * @param field   the field
     * @param cursor  the cursor
     * @param columns the columns
     * @throws IllegalAccessException the illegal access exception
     */
    private static <T> void setFieldValue(T t, Field field, Cursor cursor, List<String> columns) throws IllegalAccessException {
        Class<?> type = field.getType();
        //解析字段名称
        String columnName = ReflectUtils.parseFieldName(field);
        if (!columns.contains(columnName))
            return;
        field.setAccessible(true);
        if (String.class.equals(type)) {
            field.set(t, CursorUtils.getString(cursor, columnName));
        } else if (Integer.class.equals(type)) {
            field.set(t, CursorUtils.getInt(cursor, columnName));
        } else if (Date.class.equals(type)) {
            field.set(t, CursorUtils.getDate(cursor, columnName));
        } else if (BigDecimal.class.equals(type)) {
            field.set(t, CursorUtils.getDecimal(cursor, columnName));
        } else if (Float.class.equals(type)) {
            field.set(t, CursorUtils.getFloat(cursor, columnName));
        } else if (Short.class.equals(type)) {
            field.set(t, CursorUtils.getShort(cursor, columnName));
        } else if (Double.class.equals(type)) {
            field.set(t, CursorUtils.getDouble(cursor, columnName));
        } else if (Byte[].class.equals(type)) {
            field.set(t, CursorUtils.getBlob(cursor, columnName));
        } else if (Long.class.equals(type)) {
            field.set(t, CursorUtils.getLong(cursor, columnName));
        }
    }
}

