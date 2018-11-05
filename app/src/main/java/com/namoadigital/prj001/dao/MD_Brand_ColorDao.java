package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Brand_Color;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Brand_ColorDao extends BaseDao implements Dao<MD_Brand_Color> {

    private final Mapper<MD_Brand_Color, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Brand_Color> toMD_Brand_ColorMapper;


    public static final String TABLE = "md_brand_colors";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String BRAND_CODE = "brand_code";
    public static final String COLOR_CODE = "color_code";
    public static final String COLOR_ID = "color_id";
    public static final String COLOR_DESC = "color_desc";

    private String[] columns = {CUSTOMER_CODE, BRAND_CODE, COLOR_CODE, COLOR_ID, COLOR_DESC};

    public MD_Brand_ColorDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_Brand_ColorToContentValuesMapper();
        this.toMD_Brand_ColorMapper = new CursorMD_Brand_ColorMapper();
    }


    @Override
    public void addUpdate(MD_Brand_Color md_brand_color) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_brand_color)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_brand_color.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(BRAND_CODE).append(" = '").append(String.valueOf(md_brand_color.getBrand_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(COLOR_CODE).append(" = '").append(String.valueOf(md_brand_color.getColor_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_brand_color), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_Brand_Color> md_brand_colors, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Brand_Color md_brand_color:md_brand_colors) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_brand_color)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_brand_color.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(BRAND_CODE).append(" = '").append(String.valueOf(md_brand_color.getBrand_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(COLOR_CODE).append(" = '").append(String.valueOf(md_brand_color.getColor_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_brand_color), sbWhere.toString(), null);
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
    public MD_Brand_Color getByString(String sQuery) {
        MD_Brand_Color md_brand_color = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_brand_color = toMD_Brand_ColorMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_brand_color;
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
    public List<MD_Brand_Color> query(String sQuery) {
        List<MD_Brand_Color> md_brand_colors = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Brand_Color uAux = toMD_Brand_ColorMapper.map(cursor);
                md_brand_colors.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_brand_colors;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_brand_colors = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_brand_colors.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_brand_colors;
    }


    private class CursorMD_Brand_ColorMapper implements Mapper<Cursor,MD_Brand_Color> {
        @Override
        public MD_Brand_Color map(Cursor cursor) {
            MD_Brand_Color md_brand_color = new MD_Brand_Color();

            md_brand_color.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_brand_color.setBrand_code(cursor.getInt(cursor.getColumnIndex(BRAND_CODE)));
            md_brand_color.setColor_code(cursor.getInt(cursor.getColumnIndex(COLOR_CODE)));
            md_brand_color.setColor_id(cursor.getString(cursor.getColumnIndex(COLOR_ID)));
            md_brand_color.setColor_desc(cursor.getString(cursor.getColumnIndex(COLOR_DESC)));

            return md_brand_color;
        }
    }

    private class MD_Brand_ColorToContentValuesMapper implements Mapper<MD_Brand_Color,ContentValues> {
        @Override
        public ContentValues map(MD_Brand_Color md_brand_color) {
            ContentValues contentValues = new ContentValues();

            if(md_brand_color.getCustomer_code() > -1 ){
                contentValues.put(CUSTOMER_CODE, md_brand_color.getCustomer_code());
            }

            if(md_brand_color.getBrand_code() > -1 ){
                contentValues.put(BRAND_CODE, md_brand_color.getBrand_code());
            }

            if(md_brand_color.getColor_code() > -1 ){
                contentValues.put(COLOR_CODE, md_brand_color.getColor_code());
            }

            if(md_brand_color.getColor_id() != null ){
                contentValues.put(COLOR_ID, md_brand_color.getColor_id());
            }

            if(md_brand_color.getColor_desc() != null ){
                contentValues.put(COLOR_DESC, md_brand_color.getColor_desc());
            }

            return contentValues;
        }
    }
}
