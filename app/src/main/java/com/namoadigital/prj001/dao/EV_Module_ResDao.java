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
import com.namoadigital.prj001.model.EV_Module_Res;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_Module_ResDao implements Dao<EV_Module_Res> {
    private final SQLiteOpenHelper openHelper;
    private final Mapper<EV_Module_Res, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_Module_Res> toEV_Module_ResMapper;

    public static final String TABLE = "ev_module_ress";
    public static final String MODULE_CODE = "module_code";
    public static final String RESOURCE_CODE = "resource_code";
    public static final String RESOURCE_NAME = "resource_name";
    private String[] columns = {MODULE_CODE, RESOURCE_CODE, RESOURCE_NAME};

    public EV_Module_ResDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        //
        this.toContentValuesMapper = new EV_Module_ResToContentValuesMapper();
        this.toEV_Module_ResMapper = new CursorEV_Module_ResMapper();
    }

    @Override
    public void addUpdate(EV_Module_Res module_res) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(module_res)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(MODULE_CODE).append(" = '").append(module_res.getModule_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(RESOURCE_CODE).append(" = '").append(module_res.getResource_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(module_res), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(Iterable<EV_Module_Res> module_ress, boolean status) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_Module_Res module_res : module_ress) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(module_res)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(MODULE_CODE).append(" = '").append(module_res.getModule_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(RESOURCE_CODE).append(" = '").append(module_res.getResource_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(module_res), sbWhere.toString(), null);
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
    public EV_Module_Res getByString(String s_query) {
        EV_Module_Res module_res = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                module_res = toEV_Module_ResMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return module_res;
    }

    @Override
    public List<EV_Module_Res> query(String s_query) {
        List<EV_Module_Res> module_ress = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                EV_Module_Res uAux = toEV_Module_ResMapper.map(cursor);
                module_ress.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return module_ress;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> module_ress = new ArrayList<>();
        SQLiteDatabase db = null;

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                module_ress.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return module_ress;
    }

    private class CursorEV_Module_ResMapper implements Mapper<Cursor, EV_Module_Res> {
        @Override
        public EV_Module_Res map(Cursor cursor) {
            EV_Module_Res module_res = new EV_Module_Res();

            module_res.setModule_code(cursor.getString(cursor.getColumnIndex(MODULE_CODE)));
            module_res.setResource_code(cursor.getInt(cursor.getColumnIndex(RESOURCE_CODE)));
            module_res.setResource_name(cursor.getString(cursor.getColumnIndex(RESOURCE_NAME)));

            return module_res;
        }
    }

    private class EV_Module_ResToContentValuesMapper implements Mapper<EV_Module_Res, ContentValues> {
        @Override
        public ContentValues map(EV_Module_Res ev_module_res) {
            ContentValues contentValues = new ContentValues();

            if (ev_module_res.getModule_code() != null) {
                contentValues.put(MODULE_CODE, ev_module_res.getModule_code());
            }
            if (ev_module_res.getResource_code() > -1) {
                contentValues.put(RESOURCE_CODE, ev_module_res.getResource_code());
            }
            if (ev_module_res.getResource_name() != null) {
                contentValues.put(RESOURCE_NAME, ev_module_res.getResource_name());
            }

            return contentValues;
        }
    }
}
