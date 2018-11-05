package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Brand_Model;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Brand_ModelDao extends BaseDao implements Dao<MD_Brand_Model> {

    private final Mapper<MD_Brand_Model, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Brand_Model> toMD_Brand_ModelMapper;

    public static final String TABLE = "md_brand_models";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String BRAND_CODE = "brand_code";
    public static final String MODEL_CODE = "model_code";
    public static final String MODEL_ID = "model_id";
    public static final String MODEL_DESC = "model_desc";

    private String[] columns = {CUSTOMER_CODE, BRAND_CODE, MODEL_CODE, MODEL_ID, MODEL_DESC};

    public MD_Brand_ModelDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_Brand_ModelToContentValuesMapper();
        this.toMD_Brand_ModelMapper = new CursorMD_Brand_ModelMapper();
    }

    @Override
    public void addUpdate(MD_Brand_Model md_brand_model) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_brand_model)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_brand_model.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(BRAND_CODE).append(" = '").append(String.valueOf(md_brand_model.getBrand_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(MODEL_CODE).append(" = '").append(String.valueOf(md_brand_model.getModel_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_brand_model), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_Brand_Model> md_brand_models, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Brand_Model md_brand_model:md_brand_models) {

                if (db.insert(TABLE, null, toContentValuesMapper.map(md_brand_model)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_brand_model.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(BRAND_CODE).append(" = '").append(String.valueOf(md_brand_model.getBrand_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(MODEL_CODE).append(" = '").append(String.valueOf(md_brand_model.getModel_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_brand_model), sbWhere.toString(), null);
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
    public MD_Brand_Model getByString(String sQuery) {
        MD_Brand_Model md_brand_model = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_brand_model = toMD_Brand_ModelMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_brand_model;
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
    public List<MD_Brand_Model> query(String sQuery) {
        List<MD_Brand_Model> md_brand_models = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Brand_Model uAux = toMD_Brand_ModelMapper.map(cursor);
                md_brand_models.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_brand_models;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_brand_models = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_brand_models.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_brand_models;
    }



    private class CursorMD_Brand_ModelMapper implements Mapper<Cursor,MD_Brand_Model> {
        @Override
        public MD_Brand_Model map(Cursor cursor) {
            MD_Brand_Model md_brand_model = new MD_Brand_Model();

            md_brand_model.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_brand_model.setBrand_code(cursor.getInt(cursor.getColumnIndex(BRAND_CODE)));
            md_brand_model.setModel_code(cursor.getInt(cursor.getColumnIndex(MODEL_CODE)));
            md_brand_model.setModel_id(cursor.getString(cursor.getColumnIndex(MODEL_ID)));
            md_brand_model.setModel_desc(cursor.getString(cursor.getColumnIndex(MODEL_DESC)));

            return md_brand_model;
        }
    }

    private class MD_Brand_ModelToContentValuesMapper implements Mapper<MD_Brand_Model,ContentValues> {
        @Override
        public ContentValues map(MD_Brand_Model md_brand_model) {
            ContentValues contentValues = new ContentValues();

            if(md_brand_model.getCustomer_code() > - 1){
                contentValues.put(CUSTOMER_CODE, md_brand_model.getCustomer_code());
            }

            if(md_brand_model.getBrand_code() > - 1){
                contentValues.put(BRAND_CODE, md_brand_model.getBrand_code());
            }

            if(md_brand_model.getModel_code() > - 1){
                contentValues.put(MODEL_CODE, md_brand_model.getModel_code());
            }

            if(md_brand_model.getModel_id() != null){
                contentValues.put(MODEL_ID, md_brand_model.getModel_id());
            }

            if(md_brand_model.getModel_desc() != null){
                contentValues.put(MODEL_DESC, md_brand_model.getModel_desc());
            }

            return contentValues;
        }
    }
}
