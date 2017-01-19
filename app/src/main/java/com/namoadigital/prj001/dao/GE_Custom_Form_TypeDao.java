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
import com.namoadigital.prj001.model.GE_Custom_Form_Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class GE_Custom_Form_TypeDao implements Dao<GE_Custom_Form_Type> {
    private final SQLiteOpenHelper openHelper;
    private final Mapper<GE_Custom_Form_Type, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Type> toGE_Custom_Form_TypeMapper;

    public static final String TABLE = "ge_custom_form_types";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String ACTIVE = "active";
    private String[] columns = {CUSTOMER_CODE, CUSTOM_FORM_TYPE, ACTIVE};

    public GE_Custom_Form_TypeDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        //
        this.toContentValuesMapper = new GE_Custom_Form_TypeToContentValuesMapper();
        this.toGE_Custom_Form_TypeMapper = new CursorGE_Custom_Form_TypeMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Type custom_form_type) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_type)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_type.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_type.getCustom_form_type())).append("'");

                db.update(TABLE, toContentValuesMapper.map(custom_form_type), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Type> custom_form_types, boolean status) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Type custom_form_type : custom_form_types) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_type)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_type.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_type.getCustom_form_type())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form_type), sbWhere.toString(), null);
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
    public GE_Custom_Form_Type getByString(String s_query) {
        GE_Custom_Form_Type custom_form_type = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form_type = toGE_Custom_Form_TypeMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return custom_form_type;
    }

    @Override
    public List<GE_Custom_Form_Type> query(String s_query) {
        List<GE_Custom_Form_Type> custom_form_types = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Type uAux = toGE_Custom_Form_TypeMapper.map(cursor);
                custom_form_types.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return custom_form_types;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> custom_form_types = new ArrayList<>();
        SQLiteDatabase db = null;

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                custom_form_types.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return custom_form_types;
    }

    private class CursorGE_Custom_Form_TypeMapper implements Mapper<Cursor, GE_Custom_Form_Type> {
        @Override
        public GE_Custom_Form_Type map(Cursor cursor) {
            GE_Custom_Form_Type custom_form_type = new GE_Custom_Form_Type();

            custom_form_type.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form_type.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form_type.setActive(cursor.getInt(cursor.getColumnIndex(ACTIVE)));

            return custom_form_type;
        }
    }

    private class GE_Custom_Form_TypeToContentValuesMapper implements Mapper<GE_Custom_Form_Type, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Type custom_form_type) {
            ContentValues contentValues = new ContentValues();

            if (custom_form_type.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, custom_form_type.getCustomer_code());
            }
            if (custom_form_type.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, custom_form_type.getCustom_form_type());
            }
            if (custom_form_type.getActive() > -1) {
                contentValues.put(ACTIVE, custom_form_type.getActive());
            }

            return contentValues;
        }
    }
}
