package com.george.eleftheriou.carplateidentifier.Controllers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;

import com.george.eleftheriou.carplateidentifier.Models.CountryModel;
import com.george.eleftheriou.carplateidentifier.helpers.DBHelper;
import com.george.eleftheriou.carplateidentifier.helpers.LogHelper;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CountryController {

    public static final String CREATE_TABLE_STATEMENT = "CREATE TABLE \"country\" (\n" +
            "\t\"country_id\"\tINTEGER,\n" +
            "\t\"name\"\tTEXT NOT NULL UNIQUE,\n" +
            "\t\"code\"\tTEXT NOT NULL UNIQUE,\n" +
            "\tPRIMARY KEY(\"country_id\")\n" +
            ")";

    public static Type TYPE = new TypeToken<List<CountryModel>>(){}.getType();

    private static final String TABLE_NAME = "country";
    private static final String ID_COLUMN_NAME = "country_id";
    private static final String NAME_COLUMN_NAME = "name";
    private static final String CODE_COLUMN_NAME = "code";

    private static final String GET_MODEL_QUERY
            = "SELECT * FROM country WHERE country_id = %d LIMIT 1";

    public static final CountryModel getModel(int id) {
        if (id > 0) {
            SQLiteDatabase con = null;
            SQLiteCursor rs = null;

            try {
                con = DBHelper.getConnection();
                rs = (SQLiteCursor) con.rawQuery(String.format(GET_MODEL_QUERY, id), null);

                if (rs.moveToFirst()) {
                    final CountryModel model = new CountryModel();

                    model.setId(rs.getInt(0));
                    model.setName(rs.getString(1));
                    model.setCode(rs.getString(2));

                    return model;
                }
            } catch (Exception e) {
                LogHelper.logStackTrace(e);
            } finally {
                DBHelper.closeConnection(con, null, rs);
            }
        }

        return null;
    }

    public static final boolean insertModels(List<CountryModel> models) {
        if (models != null && !models.isEmpty()) {
            SQLiteDatabase con = null;

            try {
                con = DBHelper.getConnection();
                con.beginTransaction();

                for (final CountryModel model : models) {
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

    public static final boolean insertModels(SQLiteDatabase con, List<CountryModel> models) {
        if (con != null && con.isOpen() && models != null && !models.isEmpty()) {

            try {
                con.beginTransaction();

                for (final CountryModel model : models) {
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

    public static final ContentValues getModelContentValues(CountryModel model) {
        if (model != null) {
            final ContentValues cv = new ContentValues();

            cv.put(ID_COLUMN_NAME, model.getId());
            cv.put(NAME_COLUMN_NAME, model.getName());
            cv.put(CODE_COLUMN_NAME, model.getCode());

            return cv;
        }

        return null;
    }

}
