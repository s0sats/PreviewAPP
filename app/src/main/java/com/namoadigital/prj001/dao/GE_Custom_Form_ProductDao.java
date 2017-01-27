package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form_Product;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class GE_Custom_Form_ProductDao extends BaseDao implements Dao<GE_Custom_Form_Product> {
    private final Mapper<GE_Custom_Form_Product, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Product> toGE_Custom_Form_ProductMapper;

    public static final String TABLE = "ge_custom_form_products";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String PRODUCT_CODE = "product_code";

    private String[] columns = {CUSTOMER_CODE, CUSTOM_FORM_TYPE, CUSTOM_FORM_CODE, CUSTOM_FORM_VERSION, PRODUCT_CODE};


    public GE_Custom_Form_ProductDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new GE_Custom_Form_ProductToContentValuesMapper();
        this.toGE_Custom_Form_ProductMapper = new CursorGE_Custom_Form_ProductMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Product custom_form_product) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_product)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_product.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_product.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_product.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_product.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(custom_form_product.getProduct_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(custom_form_product), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Product> custom_form_products, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Product custom_form_product : custom_form_products) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_product)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_product.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_product.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_product.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_product.getCustom_form_version())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(custom_form_product.getProduct_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form_product), sbWhere.toString(), null);
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
    public GE_Custom_Form_Product getByString(String s_query) {
        GE_Custom_Form_Product custom_form_product = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form_product = toGE_Custom_Form_ProductMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_product;
    }

    @Override
    public List<GE_Custom_Form_Product> query(String s_query) {
        List<GE_Custom_Form_Product> custom_form_products = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Product uAux = toGE_Custom_Form_ProductMapper.map(cursor);
                custom_form_products.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_products;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> custom_form_products = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                custom_form_products.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return custom_form_products;
    }

    private class CursorGE_Custom_Form_ProductMapper implements Mapper<Cursor, GE_Custom_Form_Product> {
        @Override
        public GE_Custom_Form_Product map(Cursor cursor) {
            GE_Custom_Form_Product custom_form_product = new GE_Custom_Form_Product();

            custom_form_product.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form_product.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form_product.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            custom_form_product.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            custom_form_product.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));

            return custom_form_product;
        }
    }

    private class GE_Custom_Form_ProductToContentValuesMapper implements Mapper<GE_Custom_Form_Product, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Product custom_form_product) {
            ContentValues contentValues = new ContentValues();

            if (custom_form_product.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, custom_form_product.getCustomer_code());
            }
            if (custom_form_product.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, custom_form_product.getCustom_form_type());
            }
            if (custom_form_product.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE, custom_form_product.getCustom_form_code());
            }
            if (custom_form_product.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION, custom_form_product.getCustom_form_version());
            }
            if (custom_form_product.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, custom_form_product.getProduct_code());
            }

            return contentValues;

        }
    }

}
