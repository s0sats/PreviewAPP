package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Category_Price;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Category_PriceDao extends BaseDao implements Dao<MD_Category_Price> {

    private final Mapper<MD_Category_Price, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Category_Price> toMD_Category_PriceMapper;

    public static final String TABLE = "md_category_prices";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CATEGORY_PRICE_CODE = "category_price_code";
    public static final String CATEGORY_PRICE_ID = "category_price_id";
    public static final String CATEGORY_PRICE_DESC = "category_price_desc";

    private String[] columns = {CUSTOMER_CODE, CATEGORY_PRICE_CODE, CATEGORY_PRICE_ID, CATEGORY_PRICE_DESC};
    public MD_Category_PriceDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_Category_PriceToContentValuesMapper();
        this.toMD_Category_PriceMapper = new CursoMD_Category_PriceMapper();
    }


    @Override
    public void addUpdate(MD_Category_Price md_category_price) {
        openDB();

        try {
            if (db.insert(TABLE, null, toContentValuesMapper.map(md_category_price)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_category_price.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(md_category_price.getCategory_price_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_category_price), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_Category_Price> md_category_prices, boolean status) {

        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Category_Price md_category_price :md_category_prices) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_category_price)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_category_price.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(md_category_price.getCategory_price_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_category_price), sbWhere.toString(), null);
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
    public MD_Category_Price getByString(String sQuery) {
        MD_Category_Price md_category_price = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_category_price = toMD_Category_PriceMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_category_price;
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
    public List<MD_Category_Price> query(String sQuery) {
        List<MD_Category_Price> md_category_prices = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Category_Price uAux = toMD_Category_PriceMapper.map(cursor);
                md_category_prices.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_category_prices;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_category_prices = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_category_prices.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();


        return md_category_prices;
    }

    private class CursoMD_Category_PriceMapper implements Mapper<Cursor,MD_Category_Price> {
        @Override
        public MD_Category_Price map(Cursor cursor) {
            MD_Category_Price md_category_price = new MD_Category_Price();

            md_category_price.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_category_price.setCategory_price_code(cursor.getInt(cursor.getColumnIndex(CATEGORY_PRICE_CODE)));
            md_category_price.setCategory_price_id(cursor.getString(cursor.getColumnIndex(CATEGORY_PRICE_ID)));
            md_category_price.setCategory_price_desc(cursor.getString(cursor.getColumnIndex(CATEGORY_PRICE_DESC)));

            return md_category_price;
        }
    }

    private class MD_Category_PriceToContentValuesMapper implements Mapper<MD_Category_Price,ContentValues> {
        @Override
        public ContentValues map(MD_Category_Price md_category_price) {
            ContentValues contentValues = new ContentValues();

            if(md_category_price.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE, md_category_price.getCustomer_code());
            }

            if(md_category_price.getCategory_price_code() > -1){
                contentValues.put(CATEGORY_PRICE_CODE, md_category_price.getCategory_price_code());
            }

            if(md_category_price.getCategory_price_id() != null){
                contentValues.put(CATEGORY_PRICE_ID, md_category_price.getCategory_price_id());
            }

            if(md_category_price.getCategory_price_desc() != null){
                contentValues.put(CATEGORY_PRICE_DESC, md_category_price.getCategory_price_desc());
            }

            return contentValues;
        }
    }
}
