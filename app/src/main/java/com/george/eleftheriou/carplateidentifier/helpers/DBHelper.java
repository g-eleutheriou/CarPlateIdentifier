package com.george.eleftheriou.carplateidentifier.helpers;

import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.george.eleftheriou.carplateidentifier.Controllers.CountryController;
import com.george.eleftheriou.carplateidentifier.Controllers.RegionController;
import com.george.eleftheriou.carplateidentifier.Models.CountryModel;
import com.george.eleftheriou.carplateidentifier.Models.RegionModel;
import com.george.eleftheriou.carplateidentifier.R;
import com.george.eleftheriou.carplateidentifier.application.CarPlateIdentifier;

import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;

    private static DBHelper instance = null;

    private DBHelper() {
        super(CarPlateIdentifier.getInstance(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        db.setLocale(Locale.getDefault());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        System.out.println("create db");

        // CREATE COUNTRY TABLE
        sqLiteDatabase.execSQL(CountryController.CREATE_TABLE_STATEMENT);
        // CREATE REGION TABLE
        sqLiteDatabase.execSQL(RegionController.CREATE_TABLE_STATEMENT);

        // INIT COUNTRY TABLE
        final String countriesJSON = FilesHelper.getFileContent(CarPlateIdentifier.getInstance().getResources().openRawResource(R.raw.country));

        if (countriesJSON != null && !countriesJSON.isEmpty()) {
            System.out.println(countriesJSON);

            final List<CountryModel> models = JSONHelper.fromJSON(countriesJSON, CountryController.TYPE);
            CountryController.insertModels(sqLiteDatabase, models);
        }

        // INIT REGION TABLE
        final String regionsJSON = FilesHelper.getFileContent(CarPlateIdentifier.getInstance().getResources().openRawResource(R.raw.region));

        if (regionsJSON != null && !regionsJSON.isEmpty()) {
            final List<RegionModel> models = JSONHelper.fromJSON(regionsJSON, RegionController.TYPE);
            RegionController.insertModels(sqLiteDatabase, models);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {}

    public synchronized static SQLiteDatabase getConnection() {
        if (instance == null) {
            instance = new DBHelper();
        }

        return instance.getWritableDatabase();
    }

    public static void closeConnection(SQLiteDatabase connection, SQLiteStatement stm, SQLiteCursor rs) {
        if (rs != null) {
            rs.close();
            rs = null;
        }

        if (stm != null) {
            stm.close();
            stm = null;
        }

        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        close();
    }
}
