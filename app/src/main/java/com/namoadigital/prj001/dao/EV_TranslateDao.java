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
import com.namoadigital.prj001.model.EV_Translate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_TranslateDao implements Dao<EV_Translate>{
    private final SQLiteOpenHelper openHelper;
    private final Mapper<EV_Translate, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_Translate> toEV_TranslateMapper;

    public static final String TABLE = "ev_translates";
    public static final String TRANSLATE_CODE = "translate_code";
    public static final String DATE_DB_TRANSLATE = "date_db_translate";
    private String[] columns = {TRANSLATE_CODE, DATE_DB_TRANSLATE};

    public EV_TranslateDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        //
        this.toContentValuesMapper = new EV_TranslateToContentValuesMapper();
        this.toEV_TranslateMapper = new CursorToEV_TranslateMapper();
    }

    @Override
    public void addUpdate(EV_Translate translate) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(translate)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(TRANSLATE_CODE).append(" = '").append(translate.getTranslate_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(translate), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(Iterable<EV_Translate> trnalates, boolean status) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_Translate translate : trnalates) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(translate)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(TRANSLATE_CODE).append(" = '").append(translate.getTranslate_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(translate), sbWhere.toString(), null);
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

//    @Override
//    public EV_Translate getById(long id) {
//        EV_Translate translate = null;
//        SQLiteDatabase db = null;
//
//        try {
//
//            db = openHelper.getReadableDatabase();
//            StringBuilder sbWhere = new StringBuilder();
//            sbWhere.append(TRANSLATE_CODE).append(" = '").append(id).append("'");
//
//            Cursor cursor = db.query(TABLE, columns, sbWhere.toString(), null, null, null, null);
//
//            while (cursor.moveToNext()) {
//                translate = toEV_TranslateMapper.map(cursor);
//            }
//
//            cursor.close();
//        } catch (Exception e) {
//
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return translate;
//    }

    @Override
    public EV_Translate getByString(String s_query) {
        EV_Translate translate = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                translate = toEV_TranslateMapper.map(cursor);
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
    public List<EV_Translate> query(String s_query) {
        List<EV_Translate> translates = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                EV_Translate uAux = toEV_TranslateMapper.map(cursor);
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
        List<HMAux> trnalates = new ArrayList<>();
        SQLiteDatabase db = null;

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                trnalates.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return trnalates;
    }

    private class CursorToEV_TranslateMapper implements Mapper<Cursor, EV_Translate> {
        @Override
        public EV_Translate map(Cursor cursor) {
            EV_Translate translate = new EV_Translate();

            translate.setTranslate_code(cursor.getInt(cursor.getColumnIndex(TRANSLATE_CODE)));
            translate.setDate_db_translate(cursor.getString(cursor.getColumnIndex(DATE_DB_TRANSLATE)));

            return translate;
        }
    }

    private class EV_TranslateToContentValuesMapper implements Mapper<EV_Translate, ContentValues> {
        @Override
        public ContentValues map(EV_Translate translate) {
            ContentValues contentValues = new ContentValues();

            if (translate.getTranslate_code() > -1) {
                contentValues.put(TRANSLATE_CODE, translate.getTranslate_code());
            }
            if (translate.getDate_db_translate() != null) {
                contentValues.put(DATE_DB_TRANSLATE, translate.getDate_db_translate());
            }

            return contentValues;
        }
    }

}
