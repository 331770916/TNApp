package com.tpyzq.mobile.pangu.db.util;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * **************************************************************
 * <p>
 * **************************************************************
 * Authors:huweidong on 2017/6/23 0023 16:12
 * Email：huwwds@gmail.com
 */
public class JsonUtils {
    private static Gson gson = new Gson();


    public static <T> T object(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static <T> T object(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static String toJson(Object param) {
        try {
            return gson.toJson(param);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * json转成list
     */
    public static <T> List<T> json2List(String gsonString, Class<T> cls) {
        try {
            List<T> list = null;
            ListDynamicGenericType type = new ListDynamicGenericType(cls);
            list = gson.fromJson(gsonString, type);

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * 对集合内部泛型Type的封装
     */
    public static class ListDynamicGenericType implements ParameterizedType {

        private Type type;

        public ListDynamicGenericType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{type};
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        @Override
        public Type getRawType() {
            return List.class;
        }
    }
}
