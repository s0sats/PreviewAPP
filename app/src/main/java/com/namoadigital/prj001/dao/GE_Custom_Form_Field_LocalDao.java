package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form_Field_Local;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class GE_Custom_Form_Field_LocalDao extends BaseDao implements DaoLocal<GE_Custom_Form_Field_Local> {
    private final Mapper<GE_Custom_Form_Field_Local, ContentValues> toContentValuesMapper;
    private final Mapper<HMAux, ContentValues> toHMAux_ContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Field_Local> toGE_Custom_Form_Field_LocalMapper;

    public static final String TABLE = "ge_custom_form_fields_local";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String CUSTOM_FORM_DATA = "custom_form_data";
    public static final String CUSTOM_FORM_SEQ = "custom_form_seq";
    public static final String CUSTOM_FORM_DATA_TYPE = "custom_form_data_type";
    public static final String CUSTOM_FORM_DATA_SIZE = "custom_form_data_size";
    public static final String CUSTOM_FORM_DATA_MASK = "custom_form_data_mask";
    public static final String CUSTOM_FORM_DATA_CONTENT = "custom_form_data_content";
    public static final String CUSTOM_FORM_LOCAL_LINK = "custom_form_local_link";
    public static final String CUSTOM_FORM_ORDER = "custom_form_order";
    public static final String PAGE = "page";
    public static final String REQUIRED = "required";
    public static final String AUTOMATIC = "automatic";
    public static final String COMMENT = "comment";
    public static final String CUSTOM_FORM_FIELD_DESC = "custom_form_field_desc";

    public GE_Custom_Form_Field_LocalDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new GE_Custom_Form_Field_LocalToContentValuesMapper();
        this.toHMAux_ContentValuesMapper = new HMAux_ToContentValuesMapper();
        this.toGE_Custom_Form_Field_LocalMapper = new CursorGE_Custom_Form_Field_LocalMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Field_Local custom_form_field_local) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_field_local)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_field_local.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_field_local.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_field_local.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_field_local.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_field_local.getCustom_form_data())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_SEQ).append(" = '").append(String.valueOf(custom_form_field_local.getCustom_form_seq())).append("'");

                db.update(TABLE, toContentValuesMapper.map(custom_form_field_local), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Field_Local> custom_form_field_locals, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Field_Local custom_form_field_local : custom_form_field_locals) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_field_local)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_field_local.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_field_local.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_field_local.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_field_local.getCustom_form_version())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_field_local.getCustom_form_data())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_SEQ).append(" = '").append(String.valueOf(custom_form_field_local.getCustom_form_seq())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form_field_local), sbWhere.toString(), null);
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
    public void addUpdate(Iterable<HMAux> items) {
        openDB();

        try {

            db.beginTransaction();

            for (HMAux item : items) {
                if (db.insert(TABLE, null, toHMAux_ContentValuesMapper.map(item)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(item.get(CUSTOMER_CODE)).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(item.get(CUSTOM_FORM_TYPE)).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(item.get(CUSTOM_FORM_CODE)).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(item.get(CUSTOM_FORM_VERSION)).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(item.get(CUSTOM_FORM_DATA)).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_SEQ).append(" = '").append(item.get(CUSTOM_FORM_SEQ)).append("'");

                    db.update(TABLE, toHMAux_ContentValuesMapper.map(item), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            String st = e.toString();
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
    public GE_Custom_Form_Field_Local getByString(String s_query) {
        GE_Custom_Form_Field_Local custom_form_field_local = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form_field_local = toGE_Custom_Form_Field_LocalMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_field_local;
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
    public List<GE_Custom_Form_Field_Local> query(String s_query) {
        List<GE_Custom_Form_Field_Local> custom_form_field_locals = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Field_Local uAux = toGE_Custom_Form_Field_LocalMapper.map(cursor);
                custom_form_field_locals.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_field_locals;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        ArrayList<HMAux> custom_form_field_locals = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                custom_form_field_locals.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_field_locals;
    }

    private class CursorGE_Custom_Form_Field_LocalMapper implements Mapper<Cursor, GE_Custom_Form_Field_Local> {
        @Override
        public GE_Custom_Form_Field_Local map(Cursor cursor) {
            GE_Custom_Form_Field_Local custom_form_field_local = new GE_Custom_Form_Field_Local();

            custom_form_field_local.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form_field_local.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form_field_local.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            custom_form_field_local.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            custom_form_field_local.setCustom_form_data(cursor.getLong(cursor.getColumnIndex(CUSTOM_FORM_DATA)));
            custom_form_field_local.setCustom_form_seq(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_SEQ)));
            custom_form_field_local.setCustom_form_data_type(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_DATA_TYPE)));

            if (cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_DATA_SIZE))) {
                custom_form_field_local.setCustom_form_data_size(null);
            } else {
                custom_form_field_local.setCustom_form_data_size(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_DATA_SIZE)));
            }

            custom_form_field_local.setCustom_form_data_mask(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_DATA_MASK)));
            custom_form_field_local.setCustom_form_data_content(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_DATA_CONTENT)));
            custom_form_field_local.setCustom_form_local_link(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_LOCAL_LINK)));
            custom_form_field_local.setCustom_form_order(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_ORDER)));
            custom_form_field_local.setPage(cursor.getInt(cursor.getColumnIndex(PAGE)));
            custom_form_field_local.setRequired(cursor.getInt(cursor.getColumnIndex(REQUIRED)));

            custom_form_field_local.setAutomatic(cursor.getString(cursor.getColumnIndex(AUTOMATIC)));

            custom_form_field_local.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
            custom_form_field_local.setCustom_form_field_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_FIELD_DESC)));

            return custom_form_field_local;
        }
    }

    private class GE_Custom_Form_Field_LocalToContentValuesMapper implements Mapper<GE_Custom_Form_Field_Local, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Field_Local custom_form_field_local) {
            ContentValues contentValues = new ContentValues();

            if (custom_form_field_local.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, custom_form_field_local.getCustomer_code());
            }
            if (custom_form_field_local.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, custom_form_field_local.getCustom_form_type());
            }
            if (custom_form_field_local.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE, custom_form_field_local.getCustom_form_code());
            }
            if (custom_form_field_local.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION, custom_form_field_local.getCustom_form_version());
            }
            if (custom_form_field_local.getCustom_form_data() > -1) {
                contentValues.put(CUSTOM_FORM_DATA, custom_form_field_local.getCustom_form_data());
            }
            if (custom_form_field_local.getCustom_form_seq() > -1) {
                contentValues.put(CUSTOM_FORM_SEQ, custom_form_field_local.getCustom_form_seq());
            }
            if (custom_form_field_local.getCustom_form_data_type() != null) {
                contentValues.put(CUSTOM_FORM_DATA_TYPE, custom_form_field_local.getCustom_form_data_type());
            }

            contentValues.put(CUSTOM_FORM_DATA_SIZE, custom_form_field_local.getCustom_form_data_size());

            if (custom_form_field_local.getCustom_form_data_mask() != null) {
                contentValues.put(CUSTOM_FORM_DATA_MASK, custom_form_field_local.getCustom_form_data_mask());
            }
            if (custom_form_field_local.getCustom_form_data_content() != null) {
                contentValues.put(CUSTOM_FORM_DATA_CONTENT, custom_form_field_local.getCustom_form_data_content());
            }
            if (custom_form_field_local.getCustom_form_local_link() != null) {
                contentValues.put(CUSTOM_FORM_LOCAL_LINK, custom_form_field_local.getCustom_form_local_link());
            }
            if (custom_form_field_local.getCustom_form_order() > -1) {
                contentValues.put(CUSTOM_FORM_ORDER, custom_form_field_local.getCustom_form_order());
            }
            if (custom_form_field_local.getPage() > -1) {
                contentValues.put(PAGE, custom_form_field_local.getPage());
            }
            if (custom_form_field_local.getRequired() > -1) {
                contentValues.put(REQUIRED, custom_form_field_local.getRequired());
            }
            if (custom_form_field_local.getAutomatic() != null) {
                contentValues.put(AUTOMATIC, custom_form_field_local.getAutomatic());
            }
            if (custom_form_field_local.getComment() != null) {
                contentValues.put(COMMENT, custom_form_field_local.getComment());
            }
            if (custom_form_field_local.getCustom_form_field_desc() != null) {
                contentValues.put(CUSTOM_FORM_FIELD_DESC, custom_form_field_local.getCustom_form_field_desc());
            }

            return contentValues;

        }
    }

    private class HMAux_ToContentValuesMapper implements Mapper<HMAux, ContentValues> {

        @Override
        public ContentValues map(HMAux hmAux) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(CUSTOMER_CODE, hmAux.get(CUSTOMER_CODE));
            contentValues.put(CUSTOM_FORM_TYPE, hmAux.get(CUSTOM_FORM_TYPE));
            contentValues.put(CUSTOM_FORM_CODE, hmAux.get(CUSTOM_FORM_CODE));
            contentValues.put(CUSTOM_FORM_VERSION, hmAux.get(CUSTOM_FORM_VERSION));
            contentValues.put(CUSTOM_FORM_DATA, hmAux.get(CUSTOM_FORM_DATA));
            contentValues.put(CUSTOM_FORM_SEQ, hmAux.get(CUSTOM_FORM_SEQ));
            contentValues.put(CUSTOM_FORM_DATA_TYPE, hmAux.get(CUSTOM_FORM_DATA_TYPE));
            contentValues.put(CUSTOM_FORM_DATA_SIZE, hmAux.get(CUSTOM_FORM_DATA_SIZE));
            contentValues.put(CUSTOM_FORM_DATA_MASK, hmAux.get(CUSTOM_FORM_DATA_MASK));
            contentValues.put(CUSTOM_FORM_DATA_CONTENT, hmAux.get(CUSTOM_FORM_DATA_CONTENT));
            contentValues.put(CUSTOM_FORM_LOCAL_LINK, hmAux.get(CUSTOM_FORM_LOCAL_LINK));
            contentValues.put(CUSTOM_FORM_ORDER, hmAux.get(CUSTOM_FORM_ORDER));
            contentValues.put(PAGE, hmAux.get(PAGE));
            contentValues.put(REQUIRED, hmAux.get(REQUIRED));
            contentValues.put(AUTOMATIC, hmAux.get(AUTOMATIC));
            contentValues.put(COMMENT, hmAux.get(COMMENT));
            //contentValues.put(CUSTOM_FORM_FIELD_DESC, hmAux.get(CUSTOM_FORM_FIELD_DESC));
            contentValues.put(CUSTOM_FORM_FIELD_DESC, hmAux.get("TXT_VALUE".toLowerCase()));

            return contentValues;

        }
    }

}
