package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.EV_Module;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_ModuleDao extends BaseDao implements Dao<EV_Module> {
    private final Mapper<EV_Module, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_Module> toEV_ModuleMapper;

    public static final String TABLE = "ev_modules";
    public static final String MODULE_CODE = "module_code";
    public static final String MODULE_NAME = "module_name";
    private String[] columns = {MODULE_CODE, MODULE_NAME};


    public EV_ModuleDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new EV_ModuleToContentValuesMapper();
        this.toEV_ModuleMapper = new CursorEV_ModuleMapper();
    }

    @Override
    public void addUpdate(EV_Module module) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(module)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(MODULE_CODE).append(" = '").append(String.valueOf(module.getModule_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(module), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<EV_Module> modules, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_Module module : modules) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(module)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(MODULE_CODE).append(" = ").append(String.valueOf(module.getModule_code()));

                    db.update(TABLE, toContentValuesMapper.map(module), sbWhere.toString(), null);
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
    public EV_Module getByString(String s_query) {
        EV_Module module = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                module = toEV_ModuleMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return module;
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
    public List<EV_Module> query(String s_query) {
        List<EV_Module> modules = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                EV_Module uAux = toEV_ModuleMapper.map(cursor);
                modules.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return modules;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> modules = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                modules.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return modules;
    }

    private class CursorEV_ModuleMapper implements Mapper<Cursor, EV_Module> {
        @Override
        public EV_Module map(Cursor cursor) {

            return null;
        }
    }

    private class EV_ModuleToContentValuesMapper implements Mapper<EV_Module, ContentValues> {
        @Override
        public ContentValues map(EV_Module ev_module) {
            ContentValues contentValues = new ContentValues();

            return contentValues;
        }
    }


}
