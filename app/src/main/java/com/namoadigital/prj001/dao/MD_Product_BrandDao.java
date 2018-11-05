package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product_Brand;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 03/07/2017.
 */

public class MD_Product_BrandDao extends BaseDao implements Dao<MD_Product_Brand> {

    private final Mapper<MD_Product_Brand, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product_Brand> toMD_Product_BrandMapper;

    public static final String TABLE = "md_product_brands";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String BRAND_CODE = "brand_code";

    private String[] columns = {CUSTOMER_CODE, PRODUCT_CODE, BRAND_CODE};

    public MD_Product_BrandDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_Product_BrandToContentValuesMapper();
        this.toMD_Product_BrandMapper = new CursorMD_Product_BrandMapper();
    }


    @Override
    public void addUpdate(MD_Product_Brand md_product_brand) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_brand)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_brand.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_brand.getProduct_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(BRAND_CODE).append(" = '").append(String.valueOf(md_product_brand.getBrand_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_product_brand), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_Product_Brand> md_product_brands, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Product_Brand md_product_brand : md_product_brands) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_brand)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_brand.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_brand.getProduct_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(BRAND_CODE).append(" = '").append(String.valueOf(md_product_brand.getBrand_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_product_brand), sbWhere.toString(), null);
                }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
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
    public MD_Product_Brand getByString(String sQuery) {
        MD_Product_Brand md_product_brand = null;
        openDB();
        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_product_brand = toMD_Product_BrandMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_product_brand;
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
    public List<MD_Product_Brand> query(String sQuery) {
        List<MD_Product_Brand> md_product_brands = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Product_Brand uAux = toMD_Product_BrandMapper.map(cursor);
                md_product_brands.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_product_brands;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_product_brands = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_product_brands.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();
        return md_product_brands;
    }


    private class CursorMD_Product_BrandMapper implements Mapper<Cursor, MD_Product_Brand> {
        @Override
        public MD_Product_Brand map(Cursor cursor) {
            MD_Product_Brand md_product_brand = new MD_Product_Brand();

            md_product_brand.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product_brand.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            md_product_brand.setBrand_code(cursor.getInt(cursor.getColumnIndex(BRAND_CODE)));

            return md_product_brand;
        }
    }

    private class MD_Product_BrandToContentValuesMapper implements Mapper<MD_Product_Brand, ContentValues> {
        @Override
        public ContentValues map(MD_Product_Brand md_product_brand) {
            ContentValues contentValues = new ContentValues();

            if (md_product_brand.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_product_brand.getCustomer_code());
            }

            if (md_product_brand.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, md_product_brand.getProduct_code());
            }

            if (md_product_brand.getBrand_code() > -1) {
                contentValues.put(BRAND_CODE, md_product_brand.getBrand_code());
            }

            return contentValues;
        }
    }
}
