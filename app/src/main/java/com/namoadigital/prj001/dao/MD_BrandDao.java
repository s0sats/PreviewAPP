package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Brand;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_BrandDao extends BaseDao implements Dao<MD_Brand> {

    private final Mapper<MD_Brand, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Brand> toMD_BrandMapper;

    public static final String TABLE = "md_brands";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String BRAND_CODE = "brand_code";
    public static final String BRAND_ID = "brand_id";
    public static final String BRAND_DESC = "brand_desc";

    private String[] columns = {CUSTOMER_CODE, BRAND_CODE, BRAND_ID, BRAND_DESC};

    public MD_BrandDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_BrandToContentValuesMapper();
        this.toMD_BrandMapper = new CursorMD_BrandMapper();
    }

    @Override
    public void addUpdate(MD_Brand md_brand) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_brand)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_brand.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(BRAND_CODE).append(" = '").append(String.valueOf(md_brand.getBrand_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_brand), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Brand> md_brands, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Brand md_brand:md_brands) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_brand)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_brand.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(BRAND_CODE).append(" = '").append(String.valueOf(md_brand.getBrand_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_brand), sbWhere.toString(), null);
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
    public MD_Brand getByString(String sQuery) {
        MD_Brand md_brand = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_brand = toMD_BrandMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_brand;
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
    public List<MD_Brand> query(String sQuery) {
        List<MD_Brand> md_brands = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Brand uAux = toMD_BrandMapper.map(cursor);
                md_brands.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_brands;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_brands = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_brands.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_brands;
    }

    private class CursorMD_BrandMapper implements Mapper<Cursor,MD_Brand> {
        @Override
        public MD_Brand map(Cursor cursor) {
            MD_Brand md_brand = new MD_Brand();

            md_brand.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_brand.setBrand_code(cursor.getInt(cursor.getColumnIndex(BRAND_CODE)));
            md_brand.setBrand_id(cursor.getString(cursor.getColumnIndex(BRAND_ID)));
            md_brand.setBrand_desc(cursor.getString(cursor.getColumnIndex(BRAND_DESC)));

            return md_brand;
        }
    }

    private class MD_BrandToContentValuesMapper implements Mapper<MD_Brand,ContentValues> {
        @Override
        public ContentValues map(MD_Brand md_brand) {

            ContentValues contentValues = new ContentValues();

            if(md_brand.getCustomer_code() > - 1){
                contentValues.put(CUSTOMER_CODE, md_brand.getCustomer_code());
            }

            if(md_brand.getBrand_code() > - 1){
                contentValues.put(BRAND_CODE, md_brand.getBrand_code());
            }

            if(md_brand.getBrand_id() != null){
                contentValues.put(BRAND_ID, md_brand.getBrand_id());
            }

            if(md_brand.getBrand_desc() != null){
                contentValues.put(BRAND_DESC, md_brand.getBrand_desc());
            }

            return contentValues;
        }
    }
}
