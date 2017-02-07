package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class GE_Custom_Form_LocalDao extends BaseDao implements Dao<GE_Custom_Form_Local> {
    private final Mapper<GE_Custom_Form_Local, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Local> toGE_Custom_Form_LocalMapper;

    public static final String TABLE = "ge_custom_forms";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String CUSTOM_FORM_DATA = "custom_form_data";
    public static final String CUSTOM_FORM_PRE = "custom_form_pre";
    public static final String CUSTOM_FORM_STATUS = "custom_form_status";
    public static final String CUSTOM_FORM_SRC = "custom_form_src";
    public static final String REQUIRE_SIGNATURE = "require_signature";
    public static final String CUSTOM_PRODUCT_DESC = "custom_product_desc";
    public static final String CUSTOM_FORM_TYPE_DESC = "custom_form_type_desc";
    public static final String CUSTOM_FORM_DESC = "custom_form_desc";

    public GE_Custom_Form_LocalDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new GE_Custom_FormToContentValuesMapper();
        this.toGE_Custom_Form_LocalMapper = new CursorGE_Custom_FormMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Local custom_form_local) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_local)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_data())).append("'");


                db.update(TABLE, toContentValuesMapper.map(custom_form_local), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Local> custom_form_locals, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Local custom_form_local : custom_form_locals) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_local)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_version())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_data())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form_local), sbWhere.toString(), null);
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
    public GE_Custom_Form_Local getByString(String s_query) {
        GE_Custom_Form_Local custom_form_local = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form_local = toGE_Custom_Form_LocalMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_local;
    }

    @Override
    public List<GE_Custom_Form_Local> query(String s_query) {
        List<GE_Custom_Form_Local> custom_form_locals = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Local uAux = toGE_Custom_Form_LocalMapper.map(cursor);
                custom_form_locals.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_locals;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> custom_form_locals = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                custom_form_locals.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_locals;
    }

    private class CursorGE_Custom_FormMapper implements Mapper<Cursor, GE_Custom_Form_Local> {
        @Override
        public GE_Custom_Form_Local map(Cursor cursor) {
            GE_Custom_Form_Local custom_form_local = new GE_Custom_Form_Local();

            custom_form_local.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form_local.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form_local.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            custom_form_local.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            custom_form_local.setCustom_form_data(cursor.getLong(cursor.getColumnIndex(CUSTOM_FORM_DATA)));
            custom_form_local.setCustom_form_pre(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_PRE)));
            custom_form_local.setCustom_form_status(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_STATUS)));
            custom_form_local.setCustom_form_src(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_SRC)));
            custom_form_local.setRequire_signature(cursor.getInt(cursor.getColumnIndex(REQUIRE_SIGNATURE)));
            custom_form_local.setCustom_product_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_PRODUCT_DESC)));
            custom_form_local.setCustom_form_type_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_TYPE_DESC)));
            custom_form_local.setCustom_form_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_DESC)));

            return custom_form_local;
        }
    }

    private class GE_Custom_FormToContentValuesMapper implements Mapper<GE_Custom_Form_Local, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Local custom_form_local) {
            ContentValues contentValues = new ContentValues();

            if (custom_form_local.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, custom_form_local.getCustomer_code());
            }
            if (custom_form_local.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, custom_form_local.getCustom_form_type());
            }
            if (custom_form_local.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE, custom_form_local.getCustom_form_code());
            }
            if (custom_form_local.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION, custom_form_local.getCustom_form_version());
            }
            if (custom_form_local.getCustom_form_data() > -1){
                contentValues.put(CUSTOM_FORM_DATA, custom_form_local.getCustom_form_data());
            }
            if (custom_form_local.getCustom_form_pre() != null) {
                contentValues.put(CUSTOM_FORM_PRE, custom_form_local.getCustom_form_pre());
            }
            if (custom_form_local.getCustom_form_status() != null) {
                contentValues.put(CUSTOM_FORM_STATUS, custom_form_local.getCustom_form_status());
            }
            if (custom_form_local.getCustom_form_src() != null) {
                contentValues.put(CUSTOM_FORM_SRC, custom_form_local.getCustom_form_src());
            }
            if (custom_form_local.getRequire_signature() > -1) {
                contentValues.put(REQUIRE_SIGNATURE, custom_form_local.getRequire_signature());
            }
            if (custom_form_local.getCustom_product_desc() != null) {
                contentValues.put(CUSTOM_PRODUCT_DESC, custom_form_local.getCustom_product_desc());
            }
            if (custom_form_local.getCustom_form_type_desc() != null) {
                contentValues.put(CUSTOM_FORM_TYPE_DESC, custom_form_local.getCustom_form_type_desc());
            }
            if (custom_form_local.getCustom_form_desc() != null) {
                contentValues.put(CUSTOM_FORM_DESC, custom_form_local.getCustom_form_desc());
            }

            return contentValues;
        }
    }

}
