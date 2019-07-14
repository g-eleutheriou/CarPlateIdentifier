package com.george.eleftheriou.carplateidentifier.helpers;

import android.util.Log;

public class LogHelper {

    private static final String APPLICATION_TAG = "CarPlateIdentifier";

    public static final void logError(String error) {
        if (error != null && !error.isEmpty()) {
            Log.e(APPLICATION_TAG, error);
        }
    }

    public static final void logStackTrace(Exception ex) {
        if (ex != null) {
            Log.e(APPLICATION_TAG, "Exception", ex);
        }
    }

    public static final void logInfo(String msg) {
        if (msg != null && !msg.isEmpty()) {
            Log.i(APPLICATION_TAG, msg);
        }
    }

}
