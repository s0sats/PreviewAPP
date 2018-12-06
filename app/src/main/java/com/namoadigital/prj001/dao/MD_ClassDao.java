package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Class;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class MD_ClassDao extends BaseDao implements Dao<MD_Class> {

    private final Mapper<MD_Class, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Class> toMD_ClassMapper;
    //
    public static final String TABLE = "md_classes";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CLASS_CODE = "class_code";
    public static final String CLASS_ID = "class_id";
    public static final String CLASS_TYPE = "class_type";
    public static final String CLASS_COLOR = "class_color";
    public static final String CLASS_AVAILABLE = "class_available";

    public String[] columns = {
                                CUSTOMER_CODE, CLASS_CODE, CLASS_ID,
                                CLASS_TYPE, CLASS_COLOR,CLASS_AVAILABLE
                                };


    public MD_ClassDao(Context context) {
        super(  context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM,
                Constant.DB_MODE_MULTI
        );
        this.toContentValuesMapper = new MD_ClassToContentValuesMapper();
        this.toMD_ClassMapper = new CursorMD_ClassMapper();
    }

    public MD_ClassDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new MD_ClassToContentValuesMapper();
        this.toMD_ClassMapper = new CursorMD_ClassMapper();
    }

    @Override
    public void addUpdate(MD_Class md_class) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_class)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_class.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CLASS_CODE).append(" = '").append(String.valueOf(md_class.getClass_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_class), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_Class> md_classes, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }
            //
            for (MD_Class md_class: md_classes) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_class)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_class.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CLASS_CODE).append(" = '").append(String.valueOf(md_class.getClass_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_class), sbWhere.toString(), null);
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
    public MD_Class getByString(String sQuery) {
        MD_Class md_class = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_class = toMD_ClassMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_class;
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
    public List<MD_Class> query(String sQuery) {
        List<MD_Class> md_classes = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Class uAux = toMD_ClassMapper.map(cursor);
                md_classes.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();
        return md_classes;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_classes = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_classes.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_classes;
    }


    private class CursorMD_ClassMapper implements Mapper<Cursor,MD_Class> {
        @Override
        public MD_Class map(Cursor cursor) {
            MD_Class md_class = new MD_Class();
            //
            md_class.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_class.setClass_code(cursor.getInt(cursor.getColumnIndex(CLASS_CODE)));
            md_class.setClass_id(cursor.getString(cursor.getColumnIndex(CLASS_ID)));
            md_class.setClass_type(cursor.getString(cursor.getColumnIndex(CLASS_TYPE)));
            md_class.setClass_color(cursor.getString(cursor.getColumnIndex(CLASS_COLOR)));
            md_class.setClass_available(cursor.getInt(cursor.getColumnIndex(CLASS_AVAILABLE)));
            //
            return md_class;
        }
    }

    private class MD_ClassToContentValuesMapper implements Mapper<MD_Class,ContentValues> {
        @Override
        public ContentValues map(MD_Class md_class) {
            ContentValues contentValues = new ContentValues();
            //
            if(md_class.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,md_class.getCustomer_code());
            }
            if(md_class.getClass_code() > -1){
                contentValues.put(CLASS_CODE,md_class.getClass_code());
            }
            if(md_class.getClass_id() != null){
                contentValues.put(CLASS_ID,md_class.getClass_id());
            }
            if(md_class.getClass_type() != null){
                contentValues.put(CLASS_TYPE,md_class.getClass_type());
            }
            if(md_class.getClass_color() != null){
                contentValues.put(CLASS_COLOR,md_class.getClass_color());
            }
            if(md_class.getClass_available() > -1){
                contentValues.put(CLASS_AVAILABLE,md_class.getClass_available());
            }
            //
            return contentValues;
        }
    }
}
