package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product_Group_Product;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DANIEL.LUCHE on 24/01/2017.
 */

public class MD_Product_Group_ProductDao extends BaseDao implements Dao<MD_Product_Group_Product> {

    private final Mapper<MD_Product_Group_Product, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product_Group_Product> toMD_ProductGroupProductMapper;

    public static final String TABLE = "md_product_group_products";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String GROUP_CODE = "group_code";
    public static final String PRODUCT_CODE = "product_code";

    private String[] columns = {CUSTOMER_CODE, GROUP_CODE, PRODUCT_CODE};

    public MD_Product_Group_ProductDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        this.toContentValuesMapper = new  MD_ProductGroupProductToContentValuesMapper();
        this.toMD_ProductGroupProductMapper = new CursorMD_Product_Group_ProductMapper();
    }

    @Override
    public void addUpdate(MD_Product_Group_Product md_product_group_product) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_group_product)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_group_product.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(GROUP_CODE).append(" = '").append(String.valueOf(md_product_group_product.getGroup_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_group_product.getProduct_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_product_group_product), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_Product_Group_Product> md_product_group_products, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Product_Group_Product md_product_group_product : md_product_group_products) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_group_product)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_group_product.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(GROUP_CODE).append(" = '").append(String.valueOf(md_product_group_product.getGroup_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_group_product.getProduct_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_product_group_product), sbWhere.toString(), null);
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
    public MD_Product_Group_Product getByString(String sQuery) {

        MD_Product_Group_Product md_product_group_product = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_product_group_product = toMD_ProductGroupProductMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return md_product_group_product;
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
    public List<MD_Product_Group_Product> query(String sQuery) {
        List<MD_Product_Group_Product> md_product_group_products = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Product_Group_Product uAux = toMD_ProductGroupProductMapper.map(cursor);
                md_product_group_products.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return md_product_group_products;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_product_group_products = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_product_group_products.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return md_product_group_products;
    }

    private class CursorMD_Product_Group_ProductMapper implements Mapper<Cursor, MD_Product_Group_Product> {
        @Override
        public MD_Product_Group_Product map(Cursor cursor) {
            MD_Product_Group_Product md_product_group_product = new MD_Product_Group_Product();

            md_product_group_product.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product_group_product.setGroup_code(cursor.getLong(cursor.getColumnIndex(GROUP_CODE)));
            md_product_group_product.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));

            return md_product_group_product;
        }
    }    



    private class MD_ProductGroupProductToContentValuesMapper implements Mapper<MD_Product_Group_Product, ContentValues> {
        @Override
        public ContentValues map(MD_Product_Group_Product md_product_group_product) {
            ContentValues contentValues = new ContentValues();

            if (md_product_group_product.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_product_group_product.getCustomer_code());
            }
            if (md_product_group_product.getGroup_code() > -1) {
                contentValues.put(GROUP_CODE, md_product_group_product.getGroup_code());
            }
            if (md_product_group_product.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, md_product_group_product.getProduct_code());
            }
            return contentValues;

        }
    }
}
