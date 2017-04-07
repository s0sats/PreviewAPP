package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form_Blob_Local;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 25/01/2017.
 */

public class GE_Custom_Form_Blob_LocalDao extends BaseDao implements Dao<GE_Custom_Form_Blob_Local> {

    private final Mapper<GE_Custom_Form_Blob_Local, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Blob_Local> toGE_Custom_Form_Blob_LocalMapper;

    public static final String TABLE = "ge_custom_form_blobs_local";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String BLOB_CODE = "blob_code";
    public static final String BLOB_NAME = "blob_name";
    public static final String BLOB_URL = "blob_url";
    public static final String BLOB_URL_LOCAL = "blob_url_local";

    private String[] columns = {CUSTOMER_CODE, CUSTOM_FORM_TYPE, CUSTOM_FORM_CODE, CUSTOM_FORM_VERSION, BLOB_CODE, BLOB_NAME, BLOB_URL, BLOB_URL_LOCAL};

    public GE_Custom_Form_Blob_LocalDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        this.toContentValuesMapper = new GE_Custom_Form_Blob_LocalToContentValuesMapper();
        this.toGE_Custom_Form_Blob_LocalMapper = new CursorGE_Custom_Form_Blob_LocalMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Blob_Local ge_custom_form_blob_local) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(ge_custom_form_blob_local)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(ge_custom_form_blob_local.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(ge_custom_form_blob_local.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(ge_custom_form_blob_local.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(ge_custom_form_blob_local.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(BLOB_CODE).append(" = '").append(String.valueOf(ge_custom_form_blob_local.getBlob_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(ge_custom_form_blob_local), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Blob_Local> ge_custom_form_blob_locals, boolean status) {
        openDB();
        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Blob_Local ge_custom_form_blob_local : ge_custom_form_blob_locals ) {

                if (db.insert(TABLE, null, toContentValuesMapper.map(ge_custom_form_blob_local)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(ge_custom_form_blob_local.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(ge_custom_form_blob_local.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(ge_custom_form_blob_local.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(ge_custom_form_blob_local.getCustom_form_version())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(BLOB_CODE).append(" = '").append(String.valueOf(ge_custom_form_blob_local.getBlob_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(ge_custom_form_blob_local), sbWhere.toString(), null);
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
    public void addUpdate(String sQuery) {

        openDB();

        try {

            db.execSQL(sQuery);

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void remove(String sQuery) {

        openDB();

        try {

            db.execSQL(sQuery);

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public GE_Custom_Form_Blob_Local getByString(String sQuery) {
        GE_Custom_Form_Blob_Local  ge_custom_form_blob_local = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                ge_custom_form_blob_local = toGE_Custom_Form_Blob_LocalMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return ge_custom_form_blob_local;
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
    public List<GE_Custom_Form_Blob_Local> query(String sQuery) {

        List<GE_Custom_Form_Blob_Local> ge_custom_form_blob_locals =  new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Blob_Local uAux = toGE_Custom_Form_Blob_LocalMapper.map(cursor);
                ge_custom_form_blob_locals.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return ge_custom_form_blob_locals;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> ge_custom_form_blob_locals = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                ge_custom_form_blob_locals.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return ge_custom_form_blob_locals;
    }

    private class CursorGE_Custom_Form_Blob_LocalMapper implements Mapper<Cursor,GE_Custom_Form_Blob_Local> {
        @Override
        public GE_Custom_Form_Blob_Local map(Cursor cursor) {

            GE_Custom_Form_Blob_Local geCustomFormBlobLocal =  new GE_Custom_Form_Blob_Local();

            geCustomFormBlobLocal.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            geCustomFormBlobLocal.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            geCustomFormBlobLocal.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            geCustomFormBlobLocal.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            geCustomFormBlobLocal.setBlob_code(cursor.getInt(cursor.getColumnIndex(BLOB_CODE)));
            geCustomFormBlobLocal.setBlob_name(cursor.getString(cursor.getColumnIndex(BLOB_NAME)));
            geCustomFormBlobLocal.setBlob_url(cursor.getString(cursor.getColumnIndex(BLOB_URL)));
            geCustomFormBlobLocal.setBlob_url_local(cursor.getString(cursor.getColumnIndex(BLOB_URL_LOCAL)));

            return geCustomFormBlobLocal;
        }
    }

    private class GE_Custom_Form_Blob_LocalToContentValuesMapper implements Mapper<GE_Custom_Form_Blob_Local,ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Blob_Local ge_custom_form_blob_local) {
            ContentValues contentValues = new ContentValues();

            if (ge_custom_form_blob_local.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, ge_custom_form_blob_local.getCustomer_code());
            }

            if (ge_custom_form_blob_local.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, ge_custom_form_blob_local.getCustom_form_type());
            }

            if (ge_custom_form_blob_local.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE, ge_custom_form_blob_local.getCustom_form_code());
            }

            if (ge_custom_form_blob_local.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION, ge_custom_form_blob_local.getCustom_form_version());
            }

            if (ge_custom_form_blob_local.getBlob_code() > -1) {
                contentValues.put(BLOB_CODE, ge_custom_form_blob_local.getBlob_code());
            }

            if (ge_custom_form_blob_local.getBlob_name() != null) {
                contentValues.put(BLOB_NAME, ge_custom_form_blob_local.getBlob_name());
            }

            if (ge_custom_form_blob_local.getBlob_url() != null) {
                contentValues.put(BLOB_URL, ge_custom_form_blob_local.getBlob_url());
            }

            if (ge_custom_form_blob_local.getBlob_url_local() != null) {
                contentValues.put(BLOB_URL_LOCAL, ge_custom_form_blob_local.getBlob_url_local());
            }

            return contentValues;
        }
    }
}
