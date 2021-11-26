package com.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 *  JSON和对象转换工具类
 */
public class JsonUtil {
    private JsonUtil() {
    }

    /**
     * 对象转换成json字符串
     * @param obj
     * @return
     */
    public static String toJson(Object obj){
        Gson json = new Gson();
        return json.toJson(obj);
    }

    /**
     * json字符串转换成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * json字符串转成对象
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }
}
