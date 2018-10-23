package com.niuzj.corelibrary.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JsonUtil {

    public static Object json2Object(String json, Class<?> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

    public static Object json2ObjectArray(String json, TypeToken typeToken) {
        Gson gson = new Gson();
        return gson.fromJson(json, typeToken.getType());
    }

    public static ArrayList json2ObjectArray(String json, Class cls) {
        TypeToken<?> array = TypeToken.getArray(cls);
        Gson gson = new Gson();
        ArrayList arrayList = null;
        Object[] objects = gson.fromJson(json, array.getType());
        if (objects != null && objects.length > 0) {
            arrayList = new ArrayList();
            for (int i = 0; i < objects.length; i++) {
                arrayList.add(objects[i]);
            }
        }
        return arrayList;
    }

    public static String object2Json(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

}
