package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class GE_Custom_FormDao extends BaseDao implements Dao<GE_Custom_Form> {
    private final Mapper<GE_Custom_Form, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form> toGE_Custom_FormMapper;

    public static final String TABLE = "ge_custom_forms";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String REQUIRE_SIGNATURE = "require_signature";
    //Não é campo da tabela, descrição vem da tradução
    public static final String CUSTOM_FORM_DESC = "custom_form_desc";
    private String[] columns = {CUSTOMER_CODE, CUSTOM_FORM_TYPE, CUSTOM_FORM_CODE, CUSTOM_FORM_VERSION, REQUIRE_SIGNATURE};

    public GE_Custom_FormDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new GE_Custom_FormToContentValuesMapper();
        this.toGE_Custom_FormMapper = new CursorGE_Custom_FormMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form custom_form) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form.getCustom_form_version())).append("'");

                db.update(TABLE, toContentValuesMapper.map(custom_form), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form> custom_forms, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form custom_form : custom_forms) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form.getCustom_form_version())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form), sbWhere.toString(), null);
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
    public GE_Custom_Form getByString(String s_query) {
        GE_Custom_Form custom_form = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form = toGE_Custom_FormMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form;
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
    public List<GE_Custom_Form> query(String s_query) {
        List<GE_Custom_Form> custom_forms = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form uAux = toGE_Custom_FormMapper.map(cursor);
                custom_forms.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_forms;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> custom_forms = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                custom_forms.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_forms;
    }

    private class CursorGE_Custom_FormMapper implements Mapper<Cursor, GE_Custom_Form> {
        @Override
        public GE_Custom_Form map(Cursor cursor) {
            GE_Custom_Form custom_form = new GE_Custom_Form();

            custom_form.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            custom_form.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            custom_form.setRequire_signature(cursor.getInt(cursor.getColumnIndex(REQUIRE_SIGNATURE)));

            return custom_form;
        }
    }

    private class GE_Custom_FormToContentValuesMapper implements Mapper<GE_Custom_Form, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form custom_form) {
            ContentValues contentValues = new ContentValues();

            if (custom_form.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, custom_form.getCustomer_code());
            }
            if (custom_form.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, custom_form.getCustom_form_type());
            }
            if (custom_form.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE, custom_form.getCustom_form_code());
            }
            if (custom_form.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION, custom_form.getCustom_form_version());
            }
            if (custom_form.getRequire_signature() > -1) {
                contentValues.put(REQUIRE_SIGNATURE, custom_form.getRequire_signature());
            }

            return contentValues;
        }
    }

}
