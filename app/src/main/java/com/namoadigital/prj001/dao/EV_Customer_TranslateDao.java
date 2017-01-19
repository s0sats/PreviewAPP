package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.DatabaseHelper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.EV_Customer_Translate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_Customer_TranslateDao implements Dao<EV_Customer_Translate> {
    private final SQLiteOpenHelper openHelper;
    private final Mapper<EV_Customer_Translate, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_Customer_Translate> toEV_Customer_TranslateMapper;
    public static final String TABLE = "ev_customer_translates";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TRANSLATE_CODE = "translate_code";
    public static final String DATE_DB_CUSTOMER_TRANSLATE = "date_db_customer_translate";
    private String[] columns = {CUSTOMER_CODE, TRANSLATE_CODE, DATE_DB_CUSTOMER_TRANSLATE};

    public EV_Customer_TranslateDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        //
        this.toContentValuesMapper = new EV_Customer_TranslateToContentValuesMapper();
        this.toEV_Customer_TranslateMapper = new CursorToEV_Customer_TranslateMapper();
    }

    @Override
    public void addUpdate(EV_Customer_Translate customer_translate) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(customer_translate)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(customer_translate.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TRANSLATE_CODE).append(" = '").append(customer_translate.getTranslate_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(customer_translate), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(Iterable<EV_Customer_Translate> customer_translates, boolean status) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_Customer_Translate customer_translate : customer_translates) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(customer_translate)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(customer_translate.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(TRANSLATE_CODE).append(" = '").append(customer_translate.getTranslate_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(customer_translate), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();

            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(String s_query) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void remove(String s_query) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public EV_Customer_Translate getByString(String s_query) {
        EV_Customer_Translate translate = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                translate = toEV_Customer_TranslateMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return translate;
    }

    @Override
    public List<EV_Customer_Translate> query(String s_query) {
        List<EV_Customer_Translate> translates = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                EV_Customer_Translate uAux = toEV_Customer_TranslateMapper.map(cursor);
                translates.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return translates;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> translates = new ArrayList<>();
        SQLiteDatabase db = null;

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                translates.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return translates;
    }

    private class CursorToEV_Customer_TranslateMapper implements Mapper<Cursor, EV_Customer_Translate> {
        @Override
        public EV_Customer_Translate map(Cursor cursor) {
            EV_Customer_Translate translate = new EV_Customer_Translate();

            translate.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            translate.setTranslate_code(cursor.getLong(cursor.getColumnIndex(TRANSLATE_CODE)));
            translate.setDate_db_customer_translate(cursor.getString(cursor.getColumnIndex(DATE_DB_CUSTOMER_TRANSLATE)));

            return translate;
        }
    }

    private class EV_Customer_TranslateToContentValuesMapper implements Mapper<EV_Customer_Translate, ContentValues> {
        @Override
        public ContentValues map(EV_Customer_Translate translate) {
            ContentValues contentValues = new ContentValues();

            if (translate.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, translate.getCustomer_code());
            }
            if (translate.getTranslate_code() > -1) {
                contentValues.put(TRANSLATE_CODE, translate.getTranslate_code());
            }

            if (translate.getDate_db_customer_translate() != null) {
                contentValues.put(DATE_DB_CUSTOMER_TRANSLATE, translate.getDate_db_customer_translate());
            }

            return contentValues;
        }
    }

}
