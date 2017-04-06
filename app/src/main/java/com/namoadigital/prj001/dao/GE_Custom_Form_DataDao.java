package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neonhugo on 11/01/17.
 */

public class GE_Custom_Form_DataDao extends BaseDao implements Dao<GE_Custom_Form_Data> {
    private final Mapper<GE_Custom_Form_Data, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Data> toGE_Custom_Form_DataMapper;

    public static final String TABLE = "ge_custom_form_datas";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String CUSTOM_FORM_DATA = "custom_form_data";
    public static final String CUSTOM_FORM_DATA_SERV = "custom_form_data_serv";
    public static final String CUSTOM_FORM_STATUS = "custom_form_status";
    public static final String PRODUCT_CODE = "product_code";
    public static final String SERIAL_ID = "serial_id";
    public static final String DATE_START = "date_start";
    public static final String DATE_END = "date_end";
    public static final String USER_CODE = "user_code";
    public static final String SITE_CODE = "site_code";
    public static final String OPERATION_CODE = "operation_code";
    public static final String SIGNATURE = "signature";
    public static final String SIGNATURE_NAME = "signature_name";
    public static final String TOKEN = "token";
    public static final String USER_CODE_END = "user_code_end";

    //private String[] columns = {CUSTOMER_CODE, CUSTOM_FORM_TYPE, CUSTOM_FORM_CODE, CUSTOM_FORM_VERSION, CUSTOM_FORM_DATA, CUSTOM_FORM_STATUS, PRODUCT_CODE, SERIAL_ID, DATE_START, DATE_END, USER_CODE, SITE_CODE , OPERATION_CODE , SIGNAURE, TOKEN};

    public GE_Custom_Form_DataDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new GE_Custom_Form_DataToContentValuesMapper();
        this.toGE_Custom_Form_DataMapper = new GE_Custom_Form_DataMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Data custom_form_data) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_data)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_data.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_data.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_data.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_data.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_data.getCustom_form_data())).append("'");

                db.update(TABLE, toContentValuesMapper.map(custom_form_data), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Data> custom_form_datas, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Data custom_form_data : custom_form_datas) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_data)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_data.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_data.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_data.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_data.getCustom_form_version())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_data.getCustom_form_data())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form_data), sbWhere.toString(), null);
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
            String res = e.toString();
        } finally {
        }

        closeDB();
    }

    @Override
    public GE_Custom_Form_Data getByString(String s_query) {
        GE_Custom_Form_Data custom_form_data = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form_data = toGE_Custom_Form_DataMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_data;
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
    public List<GE_Custom_Form_Data> query(String s_query) {
        List<GE_Custom_Form_Data> custom_form_datas = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Data uAux = toGE_Custom_Form_DataMapper.map(cursor);
                custom_form_datas.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_datas;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        ArrayList<HMAux> custom_form_datas = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                custom_form_datas.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_datas;
    }

    private class GE_Custom_Form_DataMapper implements Mapper<Cursor, GE_Custom_Form_Data> {
        @Override
        public GE_Custom_Form_Data map(Cursor cursor) {
            GE_Custom_Form_Data custom_form_data = new GE_Custom_Form_Data();

            custom_form_data.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form_data.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form_data.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            custom_form_data.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            custom_form_data.setCustom_form_data(cursor.getLong(cursor.getColumnIndex(CUSTOM_FORM_DATA)));

            if(cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_DATA_SERV))){
                custom_form_data.setCustom_form_data_serv(0);
            }else{
                custom_form_data.setCustom_form_data_serv(cursor.getLong(cursor.getColumnIndex(CUSTOM_FORM_DATA_SERV)));
            }

            custom_form_data.setCustom_form_status(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_STATUS)));
            custom_form_data.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            custom_form_data.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            custom_form_data.setDate_start(cursor.getString(cursor.getColumnIndex(DATE_START)));
            custom_form_data.setDate_end(cursor.getString(cursor.getColumnIndex(DATE_END)));
            custom_form_data.setUser_code(cursor.getLong(cursor.getColumnIndex(USER_CODE)));
            custom_form_data.setSite_code(cursor.getString(cursor.getColumnIndex(SITE_CODE)));
            custom_form_data.setOperation_code(cursor.getLong(cursor.getColumnIndex(OPERATION_CODE)));

            if(cursor.isNull(cursor.getColumnIndex(SIGNATURE))){
                custom_form_data.setSignature("");
            }else{
                custom_form_data.setSignature(cursor.getString(cursor.getColumnIndex(SIGNATURE)));
            }

            if(cursor.isNull(cursor.getColumnIndex(SIGNATURE_NAME))){
                custom_form_data.setSignature_name("");
            }else{
                custom_form_data.setSignature_name(cursor.getString(cursor.getColumnIndex(SIGNATURE_NAME)));
            }

            custom_form_data.setToken(cursor.getString(cursor.getColumnIndex(TOKEN)));

            return custom_form_data;
        }
    }

    private class GE_Custom_Form_DataToContentValuesMapper implements Mapper<GE_Custom_Form_Data, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Data custom_form_data) {
            ContentValues contentValues = new ContentValues();

            if (custom_form_data.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, custom_form_data.getCustomer_code());
            }
            if (custom_form_data.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, custom_form_data.getCustom_form_type());
            }
            if (custom_form_data.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE, custom_form_data.getCustom_form_code());
            }
            if (custom_form_data.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION, custom_form_data.getCustom_form_version());
            }
            if (custom_form_data.getCustom_form_data() > -1) {
                contentValues.put(CUSTOM_FORM_DATA, custom_form_data.getCustom_form_data());
            }
            if (custom_form_data.getCustom_form_data_serv() > -1) {
                contentValues.put(CUSTOM_FORM_DATA_SERV, custom_form_data.getCustom_form_data_serv());
            }
            if (custom_form_data.getCustom_form_status() != null) {
                contentValues.put(CUSTOM_FORM_STATUS, custom_form_data.getCustom_form_status());
            }
            if (custom_form_data.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, custom_form_data.getProduct_code());
            }
            if (custom_form_data.getSerial_id() != null) {
                contentValues.put(SERIAL_ID, custom_form_data.getSerial_id());
            }
            if (custom_form_data.getDate_start() != null) {
                contentValues.put(DATE_START, custom_form_data.getDate_start());
            }
            if (custom_form_data.getDate_end() != null) {
                contentValues.put(DATE_END, custom_form_data.getDate_end());
            }
            if (custom_form_data.getUser_code() > -1) {
                contentValues.put(USER_CODE, custom_form_data.getUser_code());
            }
            if (custom_form_data.getSite_code() != null) {
                contentValues.put(SITE_CODE, custom_form_data.getSite_code());
            }
            if (custom_form_data.getOperation_code() > -1) {
                contentValues.put(OPERATION_CODE, custom_form_data.getOperation_code());
            }
            if(custom_form_data.getSignature() != null){
                contentValues.put(SIGNATURE, custom_form_data.getSignature());
            }
            if(custom_form_data.getSignature_name() != null){
                contentValues.put(SIGNATURE_NAME, custom_form_data.getSignature_name());
            }
            if(custom_form_data.getToken() != null){
                contentValues.put(TOKEN, custom_form_data.getToken());
            }

            return contentValues;
        }
    }
}
