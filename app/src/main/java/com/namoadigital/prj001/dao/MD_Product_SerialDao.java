package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 30/06/2017.
 */

public class MD_Product_SerialDao extends BaseDao implements Dao<MD_Product_Serial> {

    private final Mapper<MD_Product_Serial, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product_Serial> toMD_Product_SerialMapper;

    public static final String TABLE = "md_product_serials";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String SERIAL_CODE = "serial_code";
    public static final String SERIAL_ID = "serial_id";
    public static final String SITE_CODE = "site_code";
    public static final String ZONE_CODE = "zone_code";
    public static final String LOCAL_CODE = "local_code";
    public static final String SITE_CODE_OWNER = "site_code_owner";
    public static final String BRAND_CODE = "brand_code";
    public static final String MODEL_CODE = "model_code";
    public static final String COLOR_CODE = "color_code";
    public static final String SEGMENT_CODE = "segment_code";
    public static final String CATEGORY_PRICE_CODE = "category_price_code";
    public static final String ADD_INF1 = "add_inf1";
    public static final String ADD_INF2 = "add_inf2";
    public static final String ADD_INF3 = "add_inf3";
    public static final String UPDATE_REQUIRED = "update_required";

    private String[] columns = {CUSTOMER_CODE, PRODUCT_CODE, SERIAL_CODE, SERIAL_ID, SITE_CODE, ZONE_CODE, LOCAL_CODE, SITE_CODE_OWNER, BRAND_CODE, MODEL_CODE, COLOR_CODE, SEGMENT_CODE, CATEGORY_PRICE_CODE, ADD_INF1, ADD_INF2, ADD_INF3, UPDATE_REQUIRED};

    public MD_Product_SerialDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_Product_SerialToContentValuesMapper();
        this.toMD_Product_SerialMapper = new CursorMD_Product_SerialMapper();
    }


    @Override
    public void addUpdate(MD_Product_Serial md_product_serial) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_serial)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_serial.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_serial.getProduct_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERIAL_CODE).append(" = '").append(String.valueOf(md_product_serial.getSerial_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_product_serial), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Product_Serial> md_product_serials, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Product_Serial md_product_serial :md_product_serials) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_serial)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_serial.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_serial.getProduct_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERIAL_CODE).append(" = '").append(String.valueOf(md_product_serial.getSerial_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_product_serial), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
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
    public MD_Product_Serial getByString(String sQuery) {
        MD_Product_Serial md_product_serial = null;

        openDB();
        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_product_serial = toMD_Product_SerialMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_product_serial;
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
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return hmAux;
    }

    @Override
    public List<MD_Product_Serial> query(String sQuery) {

        List<MD_Product_Serial> md_product_serials = new ArrayList<>();

        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Product_Serial uAux = toMD_Product_SerialMapper.map(cursor);
                md_product_serials.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();
        return md_product_serials;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_product_serials = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_product_serials.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);

        } finally {
        }

        closeDB();
        return md_product_serials;
    }

    private class CursorMD_Product_SerialMapper implements Mapper<Cursor,MD_Product_Serial> {
        @Override
        public MD_Product_Serial map(Cursor cursor) {
            MD_Product_Serial md_product_serial = new MD_Product_Serial();

            md_product_serial.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product_serial.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            md_product_serial.setSerial_code(cursor.getInt(cursor.getColumnIndex(SERIAL_CODE)));
            md_product_serial.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            if(cursor.isNull(cursor.getColumnIndex(SITE_CODE))){
                md_product_serial.setSite_code(null);
            }else{
                md_product_serial.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ZONE_CODE))){
                md_product_serial.setZone_code(null);
            }else{
                md_product_serial.setZone_code(cursor.getInt(cursor.getColumnIndex(ZONE_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(LOCAL_CODE))){
                md_product_serial.setLocal_code(null);
            }else{
                md_product_serial.setLocal_code(cursor.getInt(cursor.getColumnIndex(LOCAL_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SITE_CODE_OWNER))){
                md_product_serial.setSite_code_owner(null);
            }else{
                md_product_serial.setSite_code_owner(cursor.getInt(cursor.getColumnIndex(SITE_CODE_OWNER)));
            }
            if (cursor.isNull(cursor.getColumnIndex(BRAND_CODE))){
                md_product_serial.setBrand_code(null);
            }else{
                md_product_serial.setBrand_code(cursor.getInt(cursor.getColumnIndex(BRAND_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(MODEL_CODE))){
                md_product_serial.setModel_code(null);
            }else {
                md_product_serial.setModel_code(cursor.getInt(cursor.getColumnIndex(MODEL_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(COLOR_CODE))){
                md_product_serial.setColor_code(null);
            }else{
                md_product_serial.setColor_code(cursor.getInt(cursor.getColumnIndex(COLOR_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SEGMENT_CODE))){
                md_product_serial.setSegment_code(null);
            }else {
                md_product_serial.setSegment_code(cursor.getInt(cursor.getColumnIndex(SEGMENT_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CATEGORY_PRICE_CODE))) {
                md_product_serial.setCategory_price_code(null);
            }else{
                md_product_serial.setCategory_price_code(cursor.getInt(cursor.getColumnIndex(CATEGORY_PRICE_CODE)));
            }

            md_product_serial.setAdd_inf1(cursor.getString(cursor.getColumnIndex(ADD_INF1)));
            md_product_serial.setAdd_inf2(cursor.getString(cursor.getColumnIndex(ADD_INF2)));
            md_product_serial.setAdd_inf3(cursor.getString(cursor.getColumnIndex(ADD_INF3)));
            md_product_serial.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));

            return md_product_serial;
        }
    }

    private class MD_Product_SerialToContentValuesMapper implements Mapper<MD_Product_Serial,ContentValues> {
        @Override
        public ContentValues map(MD_Product_Serial md_product_serial) {
            ContentValues contentValues = new ContentValues();

            if (md_product_serial.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,md_product_serial.getCustomer_code());
            }

            if (md_product_serial.getProduct_code() > -1){
                contentValues.put(PRODUCT_CODE, md_product_serial.getProduct_code());
            }

            if (md_product_serial.getSerial_code() > -1){
                contentValues.put(SERIAL_CODE, md_product_serial.getSerial_code());
            }

            if (md_product_serial.getSerial_id() != null){
                contentValues.put(SERIAL_ID , md_product_serial.getSerial_id());
            }
            contentValues.put(SITE_CODE, md_product_serial.getSite_code());
            contentValues.put(ZONE_CODE, md_product_serial.getZone_code());
            contentValues.put(LOCAL_CODE, md_product_serial.getLocal_code());
            contentValues.put(SITE_CODE_OWNER, md_product_serial.getSite_code_owner());
            contentValues.put(BRAND_CODE, md_product_serial.getBrand_code());
            contentValues.put(MODEL_CODE, md_product_serial.getModel_code());
            contentValues.put(COLOR_CODE, md_product_serial.getColor_code());
            contentValues.put(SEGMENT_CODE, md_product_serial.getSegment_code());
            contentValues.put(CATEGORY_PRICE_CODE, md_product_serial.getCategory_price_code());
//            if (md_product_serial.getSite_code() > -1){
//                contentValues.put(SITE_CODE, md_product_serial.getSite_code());
//            }
//            if (md_product_serial.getZone_code() > -1){
//                contentValues.put(ZONE_CODE, md_product_serial.getZone_code());
//            }
//            if (md_product_serial.getLocal_code() > -1){
//                contentValues.put(LOCAL_CODE, md_product_serial.getLocal_code());
//            }
//            if (md_product_serial.getSite_code_owner() > -1){
//                contentValues.put(SITE_CODE_OWNER, md_product_serial.getSite_code_owner());
//            }
//            if (md_product_serial.getBrand_code() > -1){
//                contentValues.put(BRAND_CODE, md_product_serial.getBrand_code());
//            }
//            if (md_product_serial.getModel_code() > -1){
//                contentValues.put(MODEL_CODE, md_product_serial.getModel_code());
//            }
//            if (md_product_serial.getColor_code() > -1){
//                contentValues.put(COLOR_CODE, md_product_serial.getColor_code());
//            }
//            if (md_product_serial.getSegment_code() > -1){
//                contentValues.put(SEGMENT_CODE, md_product_serial.getSegment_code());
//            }
//            if (md_product_serial.getCategory_price_code() > -1){
//                contentValues.put(CATEGORY_PRICE_CODE, md_product_serial.getCategory_price_code());
//            }
            if (md_product_serial.getAdd_inf1() != null){
                contentValues.put(ADD_INF1 , md_product_serial.getAdd_inf1());
            }
            if (md_product_serial.getAdd_inf2() != null){
                contentValues.put(ADD_INF2 , md_product_serial.getAdd_inf2());
            }
            if (md_product_serial.getAdd_inf3() != null){
                contentValues.put(ADD_INF3 , md_product_serial.getAdd_inf3());
            }

            if (md_product_serial.getUpdate_required() > -1){
                contentValues.put(UPDATE_REQUIRED, md_product_serial.getUpdate_required());
            }

            return contentValues;
        }
    }
}
