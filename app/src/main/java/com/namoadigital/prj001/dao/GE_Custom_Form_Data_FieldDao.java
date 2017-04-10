package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form_Data_Field;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neonhugo on 11/01/17.
 */

public class GE_Custom_Form_Data_FieldDao  extends BaseDao implements Dao<GE_Custom_Form_Data_Field> {
    private final Mapper<GE_Custom_Form_Data_Field, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Data_Field> toGE_Custom_Form_Data_FieldMapper;

    public static final String TABLE = "ge_custom_form_data_fields";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String CUSTOM_FORM_DATA = "custom_form_data";
    public static final String CUSTOM_FORM_SEQ = "custom_form_seq";
    public static final String CUSTOM_FORM_DATA_SERV = "custom_form_data_serv";
    public static final String VALUE = "value";
    public static final String VALUE_EXTRA = "value_extra";

    private String[] columns = {CUSTOMER_CODE, CUSTOM_FORM_TYPE, CUSTOM_FORM_CODE, CUSTOM_FORM_VERSION, CUSTOM_FORM_DATA, CUSTOM_FORM_SEQ, VALUE, VALUE_EXTRA};

    public GE_Custom_Form_Data_FieldDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new GE_Custom_Form_Data_FieldToContentValuesMapper();
        this.toGE_Custom_Form_Data_FieldMapper = new GE_Custom_Form_Data_FieldMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Data_Field custom_form_data_field) {
        openDB();

        try {

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
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Data_Field> custom_form_data_fields, boolean status) {
        openDB();

        try {

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
        }

        closeDB();
    }

    @Override
    public void addUpdate(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void remove(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public GE_Custom_Form_Data_Field getByString(String s_query) {
        GE_Custom_Form_Data_Field custom_form_data_field = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form_data_field = toGE_Custom_Form_Data_FieldMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_data_field;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        HMAux hmAux = null;
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                hmAux = toHMAuxMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return hmAux;
    }


    @Override
    public List<GE_Custom_Form_Data_Field> query(String s_query) {
        List<GE_Custom_Form_Data_Field> custom_form_data_fields = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Data_Field uAux = toGE_Custom_Form_Data_FieldMapper.map(cursor);
                custom_form_data_fields.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_data_fields;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        ArrayList<HMAux> custom_form_data_fields = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                custom_form_data_fields.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

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
            custom_form_data_field.setCustom_form_data_serv(cursor.getLong(cursor.getColumnIndex(CUSTOM_FORM_DATA_SERV)));
            if (cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_DATA_SERV))) {
                custom_form_data_field.setCustom_form_data_serv(null);
            }else {
                custom_form_data_field.setCustom_form_data_serv(cursor.getLong(cursor.getColumnIndex(CUSTOM_FORM_DATA_SERV)));
            }
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

            contentValues.put(CUSTOM_FORM_DATA_SERV, custom_form_data_field.getCustom_form_data_serv());

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
