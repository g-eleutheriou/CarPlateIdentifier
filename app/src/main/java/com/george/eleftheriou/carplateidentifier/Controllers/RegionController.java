package com.george.eleftheriou.carplateidentifier.Controllers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;

import com.george.eleftheriou.carplateidentifier.Models.RegionModel;
import com.george.eleftheriou.carplateidentifier.helpers.DBHelper;
import com.george.eleftheriou.carplateidentifier.helpers.LogHelper;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RegionController {

    public static final String CREATE_TABLE_STATEMENT = "CREATE TABLE \"region\" (\n" +
            "\t\"region_id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t\"country_id\"\tINTEGER NOT NULL,\n" +
            "\t\"name\"\tTEXT NOT NULL,\n" +
            "\t\"code\"\tTEXT NOT NULL\n" +
            ")";

    public static Type TYPE = new TypeToken<List<RegionModel>>(){}.getType();

    private static final String TABLE_NAME = "region";
    private static final String ID_COLUMN_NAME = "region_id";
    private static final String COUNTRY_COLUMN_NAME = "country_id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String CODE_COLUMN_NAME = "code";

    private static final String GET_MODELS_QUERY
            = "SELECT * FROM region ORDER BY code";
    private static final String GET_MODELS_BY_CODE_QUERY
            = "SELECT * FROM region WHERE code LIKE '%s%%' ORDER BY code";

    public static final List<RegionModel> getModelsByCode(String code) {
        SQLiteDatabase con = null;
        SQLiteCursor rs = null;

        try {
            con = DBHelper.getConnection();

            if (code != null && !code.isEmpty()) {
                rs = (SQLiteCursor) con.rawQuery(String.format(GET_MODELS_BY_CODE_QUERY, code), null);
            } else {
                rs = (SQLiteCursor) con.rawQuery(GET_MODELS_QUERY, null);
            }

            if (rs.moveToFirst()) {
                final List<RegionModel> models = new ArrayList<>();

                do {
                    final RegionModel model = new RegionModel();

                    model.setId(rs.getInt(0));
                    model.setCountry(CountryController.getModel(rs.getInt(1)));
                    model.setName(rs.getString(2));
                    model.setCode(rs.getString(3));

                    models.add(model);
                } while (rs.moveToNext());

                return !models.isEmpty() ? models : null;
            }
        } catch (Exception e) {
            LogHelper.logStackTrace(e);
        } finally {
            DBHelper.closeConnection(con, null, rs);
        }

        return null;
    }

    public static final boolean insertModels(List<RegionModel> models) {
        if (models != null && !models.isEmpty()) {
            SQLiteDatabase con = null;

            try {
                con = DBHelper.getConnection();
                con.beginTransaction();

                for (final RegionModel model : models) {
                    con.insert(TABLE_NAME, null, getModelContentValues(model));
                }

                con.setTransactionSuccessful();

                return true;
            } catch (Exception e) {
                LogHelper.logStackTrace(e);
            } finally {
                con.endTransaction();

                DBHelper.closeConnection(con, null, null);
            }
        }

        return false;
    }

    public static final boolean insertModels(SQLiteDatabase con, List<RegionModel> models) {
        if (con != null && con.isOpen() && models != null && !models.isEmpty()) {

            try {
                con.beginTransaction();

                for (final RegionModel model : models) {
                    con.insert(TABLE_NAME, null, getModelContentValues(model));
                }

                con.setTransactionSuccessful();

                return true;
            } catch (Exception e) {
                LogHelper.logStackTrace(e);
            } finally {
                con.endTransaction();
            }
        }

        return false;
    }

    public static final ContentValues getModelContentValues(RegionModel model) {
        if (model != null) {
            final ContentValues cv = new ContentValues();

            cv.put(ID_COLUMN_NAME, model.getId());
            cv.put(COUNTRY_COLUMN_NAME, model.getCountry() != null ? model.getCountry().getId() : 0);
            cv.put(NAME_COLUMN_NAME, model.getName());
            cv.put(CODE_COLUMN_NAME, model.getCode());

            return cv;
        }

        return null;
    }

}
