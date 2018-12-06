package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_All_Product;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class MD_All_ProductDao extends BaseDao implements Dao<MD_All_Product>, DaoProduct<MD_All_Product> {
    private final Mapper<MD_All_Product, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_All_Product> toMD_All_ProductMapper;

    public static final String TABLE = "md_all_products";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DESC = "product_desc";
    public static final String REQUIRE_SERIAL = "require_serial";
    public static final String ALLOW_NEW_SERIAL_CL = "allow_new_serial_cl";
    public static final String UN = "un";
    public static final String SKETCH_CODE = "sketch_code";
    public static final String SKETCH_URL = "sketch_url";
    public static final String SKETCH_URL_LOCAL = "sketch_url_local";
    public static final String SKETCH_LINES = "sketch_lines";
    public static final String SKETCH_COLUMNS = "sketch_columns";
    public static final String SKETCH_COLOR = "sketch_color";
    public static final String FLAG_OFFLINE = "flag_offline";
    public static final String LOCAL_CONTROL = "local_control";
    public static final String IO_CONTROL = "io_control" ;
    public static final String SERIAL_RULE = "serial_rule";
    public static final String SERIAL_MIN_LENGTH = "serial_min_length" ;
    public static final String SERIAL_MAX_LENGTH = "serial_max_length" ;

    public static String[] columns = {CUSTOMER_CODE, PRODUCT_CODE, PRODUCT_ID, PRODUCT_DESC,
            REQUIRE_SERIAL, ALLOW_NEW_SERIAL_CL, UN, SKETCH_CODE, SKETCH_URL, SKETCH_URL_LOCAL,
            SKETCH_LINES, SKETCH_COLUMNS, SKETCH_COLOR, FLAG_OFFLINE, LOCAL_CONTROL, IO_CONTROL,
            SERIAL_RULE, SERIAL_MIN_LENGTH, SERIAL_MAX_LENGTH
    };

    public MD_All_ProductDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new MD_All_ProductToContentValuesMapper();
        this.toMD_All_ProductMapper = new CursorMD_All_ProductMapper();
    }

    public MD_All_ProductDao(Context context) {
        super(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM,
                Constant.DB_MODE_MULTI
        );
        //
        this.toContentValuesMapper = new MD_All_ProductToContentValuesMapper();
        this.toMD_All_ProductMapper = new CursorMD_All_ProductMapper();
    }

    @Override
    public void addUpdate(MD_All_Product md_all_product) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_all_product)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_all_product.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_all_product.getProduct_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_all_product), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_All_Product> md_all_products, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_All_Product md_all_product : md_all_products) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_all_product)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_all_product.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_all_product.getProduct_code())).append("'");
                    sbWhere.append(" and ");

                    db.update(TABLE, toContentValuesMapper.map(md_all_product), sbWhere.toString(), null);
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
    public MD_All_Product getByString(String s_query) {
        MD_All_Product md_all_product = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                md_all_product = toMD_All_ProductMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_all_product;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        HMAux hmAux = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                hmAux = CursorToHMAuxMapper.mapN(cursor);
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
    public List<MD_All_Product> query(String s_query) {
        List<MD_All_Product> md_all_products = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                MD_All_Product uAux = toMD_All_ProductMapper.map(cursor);
                md_all_products.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_all_products;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> md_all_products = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                md_all_products.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);

        } finally {
        }

        closeDB();

        return md_all_products;
    }

    @Override
    public List<Long> query_Custom_Product_Code(String sQuery) {
        List<Long> products = new ArrayList<>();

        openDB();
        //
        //String s_query_div[] = sQuery.split(";");

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                products.add(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return products;
    }

    private class CursorMD_All_ProductMapper implements Mapper<Cursor, MD_All_Product> {
        @Override
        public MD_All_Product map(Cursor cursor) {
            MD_All_Product md_all_product = new MD_All_Product();

            md_all_product.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_all_product.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            md_all_product.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            md_all_product.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            md_all_product.setRequire_serial(cursor.getInt(cursor.getColumnIndex(REQUIRE_SERIAL)));
            md_all_product.setAllow_new_serial_cl(cursor.getInt(cursor.getColumnIndex(ALLOW_NEW_SERIAL_CL)));
            md_all_product.setUn(cursor.getString(cursor.getColumnIndex(UN)));
            if(cursor.isNull(cursor.getColumnIndex(SKETCH_CODE))){
                md_all_product.setSketch_code(null);
            }else{
                md_all_product.setSketch_code(cursor.getInt(cursor.getColumnIndex(SKETCH_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SKETCH_URL))){
                md_all_product.setSketch_url(null);
            }else{
                md_all_product.setSketch_url(cursor.getString(cursor.getColumnIndex(SKETCH_URL)));
            }

            md_all_product.setSketch_url_local(cursor.getString(cursor.getColumnIndex(SKETCH_URL_LOCAL)));

            if(cursor.isNull(cursor.getColumnIndex(SKETCH_LINES))){
                md_all_product.setSketch_lines(null);
            }else{
                md_all_product.setSketch_lines(cursor.getInt(cursor.getColumnIndex(SKETCH_LINES)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SKETCH_COLUMNS))){
                md_all_product.setSketch_columns(null);
            }else{
                md_all_product.setSketch_columns(cursor.getInt(cursor.getColumnIndex(SKETCH_COLUMNS)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SKETCH_COLOR))){
                md_all_product.setSketch_color(null);
            }else{
                md_all_product.setSketch_color(cursor.getString(cursor.getColumnIndex(SKETCH_COLOR)));
            }
            md_all_product.setFlag_offline(cursor.getInt(cursor.getColumnIndex(FLAG_OFFLINE)));
            md_all_product.setLocal_control(cursor.getInt(cursor.getColumnIndex(LOCAL_CONTROL)));
            md_all_product.setIo_control(cursor.getInt(cursor.getColumnIndex(IO_CONTROL)));
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_RULE))){
                md_all_product.setSerial_rule(null);
            }else {
                md_all_product.setSerial_rule(cursor.getString(cursor.getColumnIndex(SERIAL_RULE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_MIN_LENGTH))){
                md_all_product.setSerial_min_length(null);
            }else {
                md_all_product.setSerial_min_length(cursor.getInt(cursor.getColumnIndex(SERIAL_MIN_LENGTH)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_MAX_LENGTH))){
                md_all_product.setSerial_max_length(null);
            }else {
                md_all_product.setSerial_max_length(cursor.getInt(cursor.getColumnIndex(SERIAL_MAX_LENGTH)));
            }

            return md_all_product;
        }
    }

    private class MD_All_ProductToContentValuesMapper implements Mapper<MD_All_Product, ContentValues> {
        @Override
        public ContentValues map(MD_All_Product md_all_product) {
            ContentValues contentValues = new ContentValues();

            if (md_all_product.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_all_product.getCustomer_code());
            }
            if (md_all_product.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, md_all_product.getProduct_code());
            }
            if (md_all_product.getProduct_id() != null) {
                contentValues.put(PRODUCT_ID, md_all_product.getProduct_id());
            }
            if (md_all_product.getProduct_desc() != null) {
                contentValues.put(PRODUCT_DESC, md_all_product.getProduct_desc());
            }

            if (md_all_product.getRequire_serial() > -1) {
                contentValues.put(REQUIRE_SERIAL, md_all_product.getRequire_serial());
            }
            if (md_all_product.getAllow_new_serial_cl() > -1) {
                contentValues.put(ALLOW_NEW_SERIAL_CL, md_all_product.getAllow_new_serial_cl());
            }
            if (md_all_product.getUn() != null) {
                contentValues.put(UN, md_all_product.getUn());
            }
            contentValues.put(SKETCH_CODE,md_all_product.getSketch_code());
            contentValues.put(SKETCH_URL,md_all_product.getSketch_url());
            if (md_all_product.getSketch_url_local() != null) {
                contentValues.put(SKETCH_URL_LOCAL, md_all_product.getSketch_url_local());
            }
            contentValues.put(SKETCH_LINES,md_all_product.getSketch_lines());
            contentValues.put(SKETCH_COLUMNS,md_all_product.getSketch_columns());
            contentValues.put(SKETCH_COLOR,md_all_product.getSketch_color());
            if (md_all_product.getFlag_offline() > -1) {
                contentValues.put(FLAG_OFFLINE, md_all_product.getFlag_offline());
            }
            if (md_all_product.getLocal_control() > -1) {
                contentValues.put(LOCAL_CONTROL, md_all_product.getLocal_control());
            }
            if (md_all_product.getIo_control() > -1) {
                contentValues.put(IO_CONTROL, md_all_product.getIo_control());
            }
            contentValues.put(SERIAL_RULE, md_all_product.getSerial_rule());
            contentValues.put(SERIAL_MIN_LENGTH, md_all_product.getSerial_min_length());
            contentValues.put(SERIAL_MAX_LENGTH, md_all_product.getSerial_max_length());

            return contentValues;

        }
    }


}
