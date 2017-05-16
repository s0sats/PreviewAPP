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

public class GE_Custom_Form_LocalDao extends BaseDao implements DaoFormLocal<GE_Custom_Form_Local> {
    private final Mapper<GE_Custom_Form_Local, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Local> toGE_Custom_Form_LocalMapper;

    public static final String TABLE = "ge_custom_forms_local";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String CUSTOM_FORM_DATA = "custom_form_data";
    public static final String CUSTOM_FORM_PRE = "custom_form_pre";
    public static final String CUSTOM_FORM_STATUS = "custom_form_status";
    public static final String CUSTOM_FORM_DATA_SERV = "custom_form_data_serv";
    public static final String REQUIRE_SIGNATURE = "require_signature";
    public static final String AUTOMATIC_FILL = "automatic_fill";
    public static final String CUSTOM_PRODUCT_CODE = "custom_product_code";
    public static final String CUSTOM_PRODUCT_DESC = "custom_product_desc";
    public static final String CUSTOM_PRODUCT_ID = "custom_product_id";
    public static final String CUSTOM_FORM_TYPE_DESC = "custom_form_type_desc";
    public static final String CUSTOM_FORM_DESC = "custom_form_desc";
    public static final String SERIAL_ID = "serial_id";
    public static final String SCHEDULE_DATE_START_FORMAT = "schedule_date_start_format";
    public static final String SCHEDULE_DATE_END_FORMAT = "schedule_date_end_format";
    public static final String SCHEDULE_DATE_START_FORMAT_MS = "schedule_date_start_format_ms";
    public static final String SCHEDULE_DATE_END_FORMAT_MS = "schedule_date_end_format_ms";
    public static final String REQUIRE_SERIAL = "require_serial";
    public static final String ALLOW_NEW_SERIAL_CL = "allow_new_serial_cl";
    public static final String REQUIRE_LOCATION = "require_location";


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
    public void remove(Iterable<GE_Custom_Form_Local> custom_form_locals) {
        openDB();

        try {

            db.beginTransaction();

            for (GE_Custom_Form_Local custom_form_local : custom_form_locals) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_DATA_SERV).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_data_serv())).append("'");

                db.delete(TABLE, sbWhere.toString(), null);

            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
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
            if(cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_DATA_SERV))){
                custom_form_local.setCustom_form_data_serv(null);
            }else{
                custom_form_local.setCustom_form_data_serv(cursor.getLong(cursor.getColumnIndex(CUSTOM_FORM_DATA_SERV)));
            }
            custom_form_local.setRequire_signature(cursor.getInt(cursor.getColumnIndex(REQUIRE_SIGNATURE)));
            custom_form_local.setAutomatic_fill(cursor.getString(cursor.getColumnIndex(AUTOMATIC_FILL)));
            custom_form_local.setCustom_product_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_PRODUCT_CODE)));
            custom_form_local.setCustom_product_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_PRODUCT_DESC)));
            custom_form_local.setCustom_product_id(cursor.getString(cursor.getColumnIndex(CUSTOM_PRODUCT_ID)));
            custom_form_local.setCustom_form_type_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_TYPE_DESC)));
            custom_form_local.setCustom_form_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_DESC)));
            custom_form_local.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            if(cursor.isNull(cursor.getColumnIndex(SCHEDULE_DATE_START_FORMAT))){
                custom_form_local.setSchedule_date_start_format("1900-01-01 00:00:00 +00:00");
            }else{
                custom_form_local.setSchedule_date_start_format(cursor.getString(cursor.getColumnIndex(SCHEDULE_DATE_START_FORMAT)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SCHEDULE_DATE_END_FORMAT))){
                custom_form_local.setSchedule_date_end_format("1900-01-01 00:00:00 +00:00");
            }else{
                custom_form_local.setSchedule_date_end_format(cursor.getString(cursor.getColumnIndex(SCHEDULE_DATE_END_FORMAT)));
            }
            custom_form_local.setRequire_serial(cursor.getInt(cursor.getColumnIndex(REQUIRE_SERIAL)));
            custom_form_local.setAllow_new_serial_cl(cursor.getInt(cursor.getColumnIndex(ALLOW_NEW_SERIAL_CL)));
            custom_form_local.setRequire_location(cursor.getInt(cursor.getColumnIndex(REQUIRE_LOCATION)));

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
            if (custom_form_local.getCustom_form_data() > -1) {
                contentValues.put(CUSTOM_FORM_DATA, custom_form_local.getCustom_form_data());
            }
            if (custom_form_local.getCustom_form_pre() != null) {
                contentValues.put(CUSTOM_FORM_PRE, custom_form_local.getCustom_form_pre());
            }
            if (custom_form_local.getCustom_form_status() != null) {
                contentValues.put(CUSTOM_FORM_STATUS, custom_form_local.getCustom_form_status());
            }

            contentValues.put(CUSTOM_FORM_DATA_SERV, custom_form_local.getCustom_form_data_serv());

            if (custom_form_local.getRequire_signature() > -1) {
                contentValues.put(REQUIRE_SIGNATURE, custom_form_local.getRequire_signature());
            }
            if (custom_form_local.getAutomatic_fill() != null) {
                contentValues.put(AUTOMATIC_FILL, custom_form_local.getAutomatic_fill());
            }
            if (custom_form_local.getCustom_product_code() > -1) {
                contentValues.put(CUSTOM_PRODUCT_CODE, custom_form_local.getCustom_product_code());
            }
            if (custom_form_local.getCustom_product_desc() != null) {
                contentValues.put(CUSTOM_PRODUCT_DESC, custom_form_local.getCustom_product_desc());
            }
            if (custom_form_local.getCustom_product_id() != null) {
                contentValues.put(CUSTOM_PRODUCT_ID, custom_form_local.getCustom_product_id());
            }
            if (custom_form_local.getCustom_form_type_desc() != null) {
                contentValues.put(CUSTOM_FORM_TYPE_DESC, custom_form_local.getCustom_form_type_desc());
            }
            if (custom_form_local.getCustom_form_desc() != null) {
                contentValues.put(CUSTOM_FORM_DESC, custom_form_local.getCustom_form_desc());
            }
            if (custom_form_local.getSerial_id() != null) {
                contentValues.put(SERIAL_ID, custom_form_local.getSerial_id());
            }
            if (custom_form_local.getSchedule_date_start_format() != null) {
                contentValues.put(SCHEDULE_DATE_START_FORMAT, custom_form_local.getSchedule_date_start_format());
            }else{
                contentValues.put(SCHEDULE_DATE_START_FORMAT, "1900-01-01 00:00:00 +00:00");
            }
            if (custom_form_local.getSchedule_date_end_format() != null) {
                contentValues.put(SCHEDULE_DATE_END_FORMAT, custom_form_local.getSchedule_date_end_format());
            }else{
                contentValues.put(SCHEDULE_DATE_END_FORMAT, "1900-01-01 00:00:00 +00:00");
            }
            if (custom_form_local.getSchedule_date_start_format_ms() > -1) {
                contentValues.put(SCHEDULE_DATE_START_FORMAT_MS, custom_form_local.getSchedule_date_start_format_ms());
            }
            if (custom_form_local.getSchedule_date_end_format_ms() > -1) {
                contentValues.put(SCHEDULE_DATE_END_FORMAT_MS, custom_form_local.getSchedule_date_end_format_ms());
            }
            if (custom_form_local.getRequire_serial() > -1) {
                contentValues.put(REQUIRE_SERIAL, custom_form_local.getRequire_serial());
            }
            if (custom_form_local.getAllow_new_serial_cl() > -1) {
                contentValues.put(ALLOW_NEW_SERIAL_CL, custom_form_local.getAllow_new_serial_cl());
            }
            if (custom_form_local.getRequire_location() > -1) {
                contentValues.put(REQUIRE_LOCATION, custom_form_local.getRequire_location());
            }

            return contentValues;
        }
    }

}
