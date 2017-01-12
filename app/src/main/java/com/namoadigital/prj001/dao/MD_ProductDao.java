package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.DatabaseHelper;
import com.namoadigital.prj001.database.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class MD_ProductDao implements Dao<MD_Product> {
    private final SQLiteOpenHelper openHelper;
    private final Mapper<MD_Product, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product> toMD_ProductMapper;

    public static final String TABLE = "md_products";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DESC = "product_desc";
    public static final String ACTIVE = "active";
    public static final String REQUIRE_SERIAL = "require_serial";
    public static final String ALLOW_NEW_SERIAL_CL = "allow_new_serial_cl";
    private String[] columns = {CUSTOMER_CODE, PRODUCT_CODE, PRODUCT_ID, PRODUCT_DESC, ACTIVE, REQUIRE_SERIAL, ALLOW_NEW_SERIAL_CL};

    public MD_ProductDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        //
        this.toContentValuesMapper = new MD_ProductToContentValuesMapper();
        this.toMD_ProductMapper = new CursorMD_ProductMapper();
    }

    @Override
    public void addUpdate(MD_Product md_product) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_product)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product.getProduct_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(ACTIVE).append(" = '").append(String.valueOf(1)).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_product), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(Iterable<MD_Product> md_products, boolean status) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
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
                    sbWhere.append(ACTIVE).append(" = '").append(String.valueOf(1)).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_product), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();

            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(String s_query) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void remove(String s_query) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public MD_Product getByString(String s_query) {
        MD_Product md_product = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                md_product = toMD_ProductMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return md_product;
    }

    @Override
    public List<MD_Product> query(String s_query) {
        List<MD_Product> md_products = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                MD_Product uAux = toMD_ProductMapper.map(cursor);
                md_products.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return md_products;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> md_products = new ArrayList<>();
        SQLiteDatabase db = null;

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                md_products.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return md_products;
    }

    private class CursorMD_ProductMapper implements Mapper<Cursor, MD_Product> {
        @Override
        public MD_Product map(Cursor cursor) {
            MD_Product md_product = new MD_Product();

            md_product.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            md_product.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            md_product.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            md_product.setActive(cursor.getInt(cursor.getColumnIndex(ACTIVE)));
            md_product.setRequire_serial(cursor.getInt(cursor.getColumnIndex(REQUIRE_SERIAL)));
            md_product.setAllow_new_serial_cl(cursor.getInt(cursor.getColumnIndex(ALLOW_NEW_SERIAL_CL)));

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
            if (md_product.getActive() > -1) {
                contentValues.put(ACTIVE, md_product.getActive());
            }
            if (md_product.getRequire_serial() > -1) {
                contentValues.put(REQUIRE_SERIAL, md_product.getRequire_serial());
            }
            if (md_product.getAllow_new_serial_cl() > -1) {
                contentValues.put(ALLOW_NEW_SERIAL_CL, md_product.getAllow_new_serial_cl());
            }

            return contentValues;

        }
    }


}
