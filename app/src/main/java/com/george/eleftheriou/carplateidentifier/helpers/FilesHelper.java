package com.george.eleftheriou.carplateidentifier.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FilesHelper {

    public static final String getFileContent(InputStream is) {
        if (is != null) {
            BufferedReader br = null;

            try {
                br = new BufferedReader(new InputStreamReader(is));

                final StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while(line != null){
                    sb.append(line).append("\n");
                    line = br.readLine();
                }

                return sb.length() > 0 ? sb.toString() : null;
            } catch (Exception e) {
                LogHelper.logStackTrace(e);

                return null;
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (Exception ex) {
                        LogHelper.logStackTrace(ex);
                    }

                    br = null;
                }

                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception ex) {
                        LogHelper.logStackTrace(ex);
                    }

                    is = null;
                }
            }
        }

        return null;
    }


}
