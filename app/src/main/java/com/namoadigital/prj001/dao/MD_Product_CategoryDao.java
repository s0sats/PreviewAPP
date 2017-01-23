package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.DatabaseHelper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product_Category;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class MD_Product_CategoryDao extends BaseDao implements Dao<MD_Product_Category> {
    private final Mapper<MD_Product_Category, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product_Category> toMD_Product_CategoryMapper;

    public static final String TABLE = "md_product_categorys";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CATEGORY_CODE = "category_code";
    public static final String CATEGORY_CODE_FATHER = "category_code_father";
    public static final String STRUC_TYPE = "struc_type";
    public static final String PRODUCT_CODE = "product_code";
    public static final String CATEGORY_DESC = "category_desc";
    public static final String ACTIVE = "active";
    private String[] columns = {CUSTOMER_CODE, CATEGORY_CODE, CATEGORY_CODE_FATHER, STRUC_TYPE, PRODUCT_CODE, CATEGORY_DESC, ACTIVE};


    public MD_Product_CategoryDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new MD_Product_CategoryToContentValuesMapper();
        this.toMD_Product_CategoryMapper = new CursorMD_Product_CategoryMapper();
    }

    @Override
    public void addUpdate(MD_Product_Category product_category) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(product_category)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(product_category.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CATEGORY_CODE).append(" = '").append(String.valueOf(product_category.getCategory_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(product_category), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Product_Category> product_categories, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Product_Category product_category : product_categories) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(product_category)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(product_category.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CATEGORY_CODE).append(" = '").append(String.valueOf(product_category.getCategory_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(product_category), sbWhere.toString(), null);
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
    public MD_Product_Category getByString(String s_query) {
        MD_Product_Category product_category = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                product_category = toMD_Product_CategoryMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return product_category;
    }

    @Override
    public List<MD_Product_Category> query(String s_query) {
        List<MD_Product_Category> product_categories = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                MD_Product_Category uAux = toMD_Product_CategoryMapper.map(cursor);
                product_categories.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return product_categories;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> product_categories = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                product_categories.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return product_categories;
    }

    private class CursorMD_Product_CategoryMapper implements Mapper<Cursor, MD_Product_Category> {
        @Override
        public MD_Product_Category map(Cursor cursor) {
            MD_Product_Category md_product_category = new MD_Product_Category();

            md_product_category.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product_category.setCategory_code(cursor.getInt(cursor.getColumnIndex(CATEGORY_CODE)));
            md_product_category.setCategory_code_father(cursor.getInt(cursor.getColumnIndex(CATEGORY_CODE_FATHER)));
            md_product_category.setStruc_type(cursor.getString(cursor.getColumnIndex(STRUC_TYPE)));
            md_product_category.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            md_product_category.setCategory_desc(cursor.getString(cursor.getColumnIndex(CATEGORY_DESC)));
            md_product_category.setActive(cursor.getInt(cursor.getColumnIndex(ACTIVE)));

            return md_product_category;
        }
    }

    private class MD_Product_CategoryToContentValuesMapper implements Mapper<MD_Product_Category, ContentValues> {
        @Override
        public ContentValues map(MD_Product_Category md_product_category) {
            ContentValues contentValues = new ContentValues();

            if (md_product_category.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_product_category.getCustomer_code());
            }
            if (md_product_category.getCategory_code_father() > -1) {
                contentValues.put(CATEGORY_CODE_FATHER, md_product_category.getCategory_code_father());
            }
            if (md_product_category.getCategory_code() > -1) {
                contentValues.put(CATEGORY_CODE, md_product_category.getCategory_code());
            }
            if (md_product_category.getStruc_type() != null) {
                contentValues.put(STRUC_TYPE, md_product_category.getStruc_type());
            }
            if (md_product_category.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, md_product_category.getProduct_code());
            }
            if (md_product_category.getCategory_desc() != null) {
                contentValues.put(CATEGORY_DESC, md_product_category.getCategory_desc());
            }
            if (md_product_category.getActive() > -1) {
                contentValues.put(ACTIVE, md_product_category.getActive());
            }

            return contentValues;

        }
    }


}