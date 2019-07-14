package com.george.eleftheriou.carplateidentifier.application;

import android.app.Application;
import android.content.Context;

public class CarPlateIdentifier extends Application {

    private static CarPlateIdentifier instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static final CarPlateIdentifier getInstance() {
        if (instance != null) {
            return instance;
        }

        return null;
    }
}
