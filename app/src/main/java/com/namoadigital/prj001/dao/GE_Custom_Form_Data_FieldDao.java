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
import com.namoadigital.prj001.model.GE_Custom_Form_Data_Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neonhugo on 11/01/17.
 */

public class GE_Custom_Form_Data_FieldDao implements Dao<GE_Custom_Form_Data_Field> {

    private final SQLiteOpenHelper openHelper;
    private final Mapper<GE_Custom_Form_Data_Field, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Data_Field> toGE_Custom_Form_Data_FieldMapper;

    public static final String TABLE = "ge_custom_form_data_fields";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String CUSTOM_FORM_DATA = "custom_form_data";
    public static final String CUSTOM_FORM_SEQ = "custom_form_seq";
    public static final String VALUE = "value";
    public static final String VALUE_EXTRA = "value_extra";

    private String[] columns = {CUSTOMER_CODE, CUSTOM_FORM_TYPE, CUSTOM_FORM_CODE, CUSTOM_FORM_VERSION, CUSTOM_FORM_DATA, CUSTOM_FORM_SEQ, VALUE, VALUE_EXTRA};

    public GE_Custom_Form_Data_FieldDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        //
        this.toContentValuesMapper = new GE_Custom_Form_Data_FieldToContentValuesMapper();
        this.toGE_Custom_Form_Data_FieldMapper = new GE_Custom_Form_Data_FieldMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Data_Field custom_form_data_field) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_data_field)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_data_field.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_data_field.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_data_field.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_data_field.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_data_field.getCustom_form_data())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_SEQ).append(" = '").append(String.valueOf(custom_form_data_field.getCustom_form_seq())).append("'");

                db.update(TABLE, toContentValuesMapper.map(custom_form_data_field), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Data_Field> custom_form_data_fields, boolean status) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Data_Field custom_form_data_field : custom_form_data_fields) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_data_field)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_data_field.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_data_field.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_data_field.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_data_field.getCustom_form_version())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_data_field.getCustom_form_data())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_SEQ).append(" = '").append(String.valueOf(custom_form_data_field.getCustom_form_seq())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form_data_field), sbWhere.toString(), null);
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
    public GE_Custom_Form_Data_Field getByString(String s_query) {
        GE_Custom_Form_Data_Field custom_form_data_field = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form_data_field = toGE_Custom_Form_Data_FieldMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return custom_form_data_field;
    }

    @Override
    public List<GE_Custom_Form_Data_Field> query(String s_query) {
        List<GE_Custom_Form_Data_Field> custom_form_data_fields = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Data_Field uAux = toGE_Custom_Form_Data_FieldMapper.map(cursor);
                custom_form_data_fields.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return custom_form_data_fields;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        ArrayList<HMAux> custom_form_data_fields = new ArrayList<>();
        SQLiteDatabase db = null;

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                custom_form_data_fields.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return custom_form_data_fields;
    }

    private class GE_Custom_Form_Data_FieldMapper implements Mapper<Cursor, GE_Custom_Form_Data_Field> {
        @Override
        public GE_Custom_Form_Data_Field map(Cursor cursor) {
            GE_Custom_Form_Data_Field custom_form_data_field = new GE_Custom_Form_Data_Field();

            custom_form_data_field.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form_data_field.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form_data_field.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            custom_form_data_field.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            custom_form_data_field.setCustom_form_data(cursor.getLong(cursor.getColumnIndex(CUSTOM_FORM_DATA)));
            custom_form_data_field.setCustom_form_seq(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_SEQ)));
            custom_form_data_field.setValue(cursor.getString(cursor.getColumnIndex(VALUE)));
            custom_form_data_field.setValue_extra(cursor.getString(cursor.getColumnIndex(VALUE_EXTRA)));

            return custom_form_data_field;
        }
    }

    private class GE_Custom_Form_Data_FieldToContentValuesMapper implements Mapper<GE_Custom_Form_Data_Field, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Data_Field custom_form_data_field) {
            ContentValues contentValues = new ContentValues();

            if (custom_form_data_field.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, custom_form_data_field.getCustomer_code());
            }
            if (custom_form_data_field.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, custom_form_data_field.getCustom_form_type());
            }
            if (custom_form_data_field.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE, custom_form_data_field.getCustom_form_code());
            }
            if (custom_form_data_field.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION, custom_form_data_field.getCustom_form_version());
            }
            if (custom_form_data_field.getCustom_form_data() > -1) {
                contentValues.put(CUSTOM_FORM_DATA, custom_form_data_field.getCustom_form_data());
            }
            if (custom_form_data_field.getCustom_form_seq() > -1) {
                contentValues.put(CUSTOM_FORM_SEQ, custom_form_data_field.getCustom_form_seq());
            }
            if (custom_form_data_field.getValue() != null) {
                contentValues.put(VALUE, custom_form_data_field.getValue());
            }
            if (custom_form_data_field.getValue_extra() != null) {
                contentValues.put(VALUE_EXTRA, custom_form_data_field.getValue_extra());
            }

            return contentValues;

        }
    }
}
