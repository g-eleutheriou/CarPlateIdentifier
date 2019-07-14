package com.george.eleftheriou.carplateidentifier.helpers;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JSONHelper {

    private static final Gson gson;

    static {
        gson = new Gson();
    }

    public static final String toJSON(Object o) {
        return gson.toJson(o);
    }

    public static final <T> T fromJSON(String json, Class<T> typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static final <T> T fromJSON(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

}
