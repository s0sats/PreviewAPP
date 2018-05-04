package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class MD_ProductDao extends BaseDao implements Dao<MD_Product>, DaoProduct<MD_Product> {
    private final Mapper<MD_Product, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product> toMD_ProductMapper;

    public static final String TABLE = "md_products";
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

    public MD_ProductDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new MD_ProductToContentValuesMapper();
        this.toMD_ProductMapper = new CursorMD_ProductMapper();
    }

    public MD_ProductDao(Context context) {
        super(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM,
                Constant.DB_MODE_MULTI
        );
        //
        this.toContentValuesMapper = new MD_ProductToContentValuesMapper();
        this.toMD_ProductMapper = new CursorMD_ProductMapper();
    }

    @Override
    public void addUpdate(MD_Product md_product) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_product)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product.getProduct_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_product), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Product> md_products, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Product md_product : md_products) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_product)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product.getProduct_code())).append("'");
                    sbWhere.append(" and ");

                    db.update(TABLE, toContentValuesMapper.map(md_product), sbWhere.toString(), null);
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
    public MD_Product getByString(String s_query) {
        MD_Product md_product = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                md_product = toMD_ProductMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_product;
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
    public List<MD_Product> query(String s_query) {
        List<MD_Product> md_products = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                MD_Product uAux = toMD_ProductMapper.map(cursor);
                md_products.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_products;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> md_products = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_products.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);

        } finally {
        }

        closeDB();

        return md_products;
    }

    @Override
    public List<Long> query_Custom_Product_Code(String sQuery) {
        List<Long> products = new ArrayList<>();

        openDB();
        String s_query_div[] = sQuery.split(";");

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

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

    private class CursorMD_ProductMapper implements Mapper<Cursor, MD_Product> {
        @Override
        public MD_Product map(Cursor cursor) {
            MD_Product md_product = new MD_Product();

            md_product.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            md_product.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            md_product.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            md_product.setRequire_serial(cursor.getInt(cursor.getColumnIndex(REQUIRE_SERIAL)));
            md_product.setAllow_new_serial_cl(cursor.getInt(cursor.getColumnIndex(ALLOW_NEW_SERIAL_CL)));
            md_product.setUn(cursor.getString(cursor.getColumnIndex(UN)));
            if(cursor.isNull(cursor.getColumnIndex(SKETCH_CODE))){
                md_product.setSketch_code(null);
            }else{
                md_product.setSketch_code(cursor.getInt(cursor.getColumnIndex(SKETCH_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SKETCH_URL))){
                md_product.setSketch_url(null);
            }else{
                md_product.setSketch_url(cursor.getString(cursor.getColumnIndex(SKETCH_URL)));
            }

            md_product.setSketch_url_local(cursor.getString(cursor.getColumnIndex(SKETCH_URL_LOCAL)));

            if(cursor.isNull(cursor.getColumnIndex(SKETCH_LINES))){
                md_product.setSketch_lines(null);
            }else{
                md_product.setSketch_lines(cursor.getInt(cursor.getColumnIndex(SKETCH_LINES)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SKETCH_COLUMNS))){
                md_product.setSketch_columns(null);
            }else{
                md_product.setSketch_columns(cursor.getInt(cursor.getColumnIndex(SKETCH_COLUMNS)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SKETCH_COLOR))){
                md_product.setSketch_color(null);
            }else{
                md_product.setSketch_color(cursor.getString(cursor.getColumnIndex(SKETCH_COLOR)));
            }
            md_product.setFlag_offline(cursor.getInt(cursor.getColumnIndex(FLAG_OFFLINE)));
            md_product.setLocal_control(cursor.getInt(cursor.getColumnIndex(LOCAL_CONTROL)));
            md_product.setIo_control(cursor.getInt(cursor.getColumnIndex(IO_CONTROL)));
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_RULE))){
                md_product.setSerial_rule(null);
            }else {
                md_product.setSerial_rule(cursor.getString(cursor.getColumnIndex(SERIAL_RULE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_MIN_LENGTH))){
                md_product.setSerial_min_length(null);
            }else {
                md_product.setSerial_min_length(cursor.getInt(cursor.getColumnIndex(SERIAL_MIN_LENGTH)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_MAX_LENGTH))){
                md_product.setSerial_max_length(null);
            }else {
                md_product.setSerial_max_length(cursor.getInt(cursor.getColumnIndex(SERIAL_MAX_LENGTH)));
            }

            return md_product;
        }
    }

    private class MD_ProductToContentValuesMapper implements Mapper<MD_Product, ContentValues> {
        @Override
        public ContentValues map(MD_Product md_product) {
            ContentValues contentValues = new ContentValues();

            if (md_product.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_product.getCustomer_code());
            }
            if (md_product.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, md_product.getProduct_code());
            }
            if (md_product.getProduct_id() != null) {
                contentValues.put(PRODUCT_ID, md_product.getProduct_id());
            }
            if (md_product.getProduct_desc() != null) {
                contentValues.put(PRODUCT_DESC, md_product.getProduct_desc());
            }

            if (md_product.getRequire_serial() > -1) {
                contentValues.put(REQUIRE_SERIAL, md_product.getRequire_serial());
            }
            if (md_product.getAllow_new_serial_cl() > -1) {
                contentValues.put(ALLOW_NEW_SERIAL_CL, md_product.getAllow_new_serial_cl());
            }
            if (md_product.getUn() != null) {
                contentValues.put(UN, md_product.getUn());
            }
            contentValues.put(SKETCH_CODE,md_product.getSketch_code());
            contentValues.put(SKETCH_URL,md_product.getSketch_url());
            if (md_product.getSketch_url_local() != null) {
                contentValues.put(SKETCH_URL_LOCAL, md_product.getSketch_url_local());
            }
            contentValues.put(SKETCH_LINES,md_product.getSketch_lines());
            contentValues.put(SKETCH_COLUMNS,md_product.getSketch_columns());
            contentValues.put(SKETCH_COLOR,md_product.getSketch_color());
            if (md_product.getFlag_offline() > -1) {
                contentValues.put(FLAG_OFFLINE, md_product.getFlag_offline());
            }
            if (md_product.getLocal_control() > -1) {
                contentValues.put(LOCAL_CONTROL, md_product.getLocal_control());
            }
            if (md_product.getIo_control() > -1) {
                contentValues.put(IO_CONTROL, md_product.getIo_control());
            }
            contentValues.put(SERIAL_RULE, md_product.getSerial_rule());
            contentValues.put(SERIAL_MIN_LENGTH, md_product.getSerial_min_length());
            contentValues.put(SERIAL_MAX_LENGTH, md_product.getSerial_max_length());

            return contentValues;

        }
    }


}
