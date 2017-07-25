package com.tpyzq.mobile.pangu.db.util;

import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author huwwds
 * @title ReflectUtils
 * @description 反射工具类
 * @time 2016/6/18 11:36
 */
public class ReflectUtils {

    /**
     * Gets all fields.
     *
     * @param beanClass java bean class对象
     * @return the all fields
     */
    @NonNull
    public static List<Field> getAllFields(Class<?> beanClass) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazz = beanClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return fields;
    }

    /**
     * 解析字段名称
     * 字段名称需为驼峰命名法
     * 解析结果为 xxx_xxx_xx 格式
     *
     * @param field the field
     * @return the field name
     * @author xiejinhong
     * @title parseFieldName
     * @date 2016 -06-18 11:40:10
     */
    @NonNull
    public static String parseFieldName(Field field) {
        String name = field.getName();
        char[] chars = name.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];
            if (i == 0) {
                sb.append(currentChar);
                continue;
            }
            if (currentChar >= 'A' && currentChar <= 'Z') {
                sb.append("_").append(currentChar);
                continue;
            }
            sb.append(currentChar);
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 字段名->>数据库列名（只支持驼峰命名法）
     *
     * @param fieldName the field name
     * @return the name 2 column name
     * @author xiejinhong
     * @title fieldName2ColumnName
     * @date 2016 -07-03 12:11:36
     */
    public static String fieldName2ColumnName(String fieldName) {
        char[] chars = fieldName.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            char currentChar = chars[i];
            if (i == 0) {
                sb.append(currentChar);
                continue;
            }
            if (currentChar >= 'A' && currentChar <= 'Z') {
                sb.append("_").append(currentChar);
                continue;
            }
            sb.append(currentChar);
        }
        return sb.toString().toLowerCase();
    }
}
