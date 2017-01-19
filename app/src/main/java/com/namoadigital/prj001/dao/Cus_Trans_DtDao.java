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
import com.namoadigital.prj001.model.Cus_Trans_Dt;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by neomatrix on 11/01/17.
 */

public class Cus_Trans_DtDao implements Dao<Cus_Trans_Dt> {
    private final SQLiteOpenHelper openHelper;
    private final Mapper<Cus_Trans_Dt, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, Cus_Trans_Dt> toCus_Trans_DtMapper;

    public static final String TABLE = "cus_trans_dts";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TRANSLATE_CODE = "translate_code";
    public static final String DATE_DB_CUSTOMER_TRANSLATE = "date_db_customer_translate";
    private String[] columns = {CUSTOMER_CODE, TRANSLATE_CODE, DATE_DB_CUSTOMER_TRANSLATE};

    public Cus_Trans_DtDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        //
        this.toContentValuesMapper = new Cus_Trans_DtToContentValuesMapper();
        this.toCus_Trans_DtMapper = new CursorToCus_Trans_DtMapper();
    }

    @Override
    public void addUpdate(Cus_Trans_Dt cus_trans_dt) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(cus_trans_dt)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(cus_trans_dt.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TRANSLATE_CODE).append(" = '").append(cus_trans_dt.getTranslate_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(cus_trans_dt), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(Iterable<Cus_Trans_Dt> cus_trans_dts, boolean status) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (Cus_Trans_Dt cus_trans_dt : cus_trans_dts) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(cus_trans_dt)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(cus_trans_dt.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(TRANSLATE_CODE).append(" = '").append(cus_trans_dt.getTranslate_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(cus_trans_dt), sbWhere.toString(), null);
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
    public Cus_Trans_Dt getByString(String s_query) {
        Cus_Trans_Dt cus_trans_dt = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                cus_trans_dt = toCus_Trans_DtMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return cus_trans_dt;
    }

    @Override
    public List<Cus_Trans_Dt> query(String s_query) {
        List<Cus_Trans_Dt> cus_trans_dts = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                Cus_Trans_Dt uAux = toCus_Trans_DtMapper.map(cursor);
                cus_trans_dts.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return cus_trans_dts;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> cus_trans_dts = new ArrayList<>();
        SQLiteDatabase db = null;

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                cus_trans_dts.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return cus_trans_dts;
    }

    private class CursorToCus_Trans_DtMapper implements Mapper<Cursor, Cus_Trans_Dt> {
        @Override
        public Cus_Trans_Dt map(Cursor cursor) {
            Cus_Trans_Dt cus_trans_dt = new Cus_Trans_Dt();

            cus_trans_dt.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            cus_trans_dt.setTranslate_code(cursor.getInt(cursor.getColumnIndex(TRANSLATE_CODE)));
            cus_trans_dt.setDate_db_customer_translate(cursor.getString(cursor.getColumnIndex(DATE_DB_CUSTOMER_TRANSLATE)));

            return cus_trans_dt;
        }
    }

    private class Cus_Trans_DtToContentValuesMapper implements Mapper<Cus_Trans_Dt, ContentValues> {
        @Override
        public ContentValues map(Cus_Trans_Dt cus_trans_dt) {
            ContentValues contentValues = new ContentValues();

            if (cus_trans_dt.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, cus_trans_dt.getCustomer_code());
            }
            if (cus_trans_dt.getTranslate_code() > -1) {
                contentValues.put(TRANSLATE_CODE, cus_trans_dt.getTranslate_code());
            }
            if (cus_trans_dt.getDate_db_customer_translate() != null) {
                contentValues.put(DATE_DB_CUSTOMER_TRANSLATE, cus_trans_dt.getDate_db_customer_translate());
            }

            return contentValues;
        }
    }
}
