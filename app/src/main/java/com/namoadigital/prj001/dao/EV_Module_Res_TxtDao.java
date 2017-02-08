package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.EV_Module_Res_Txt;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_Module_Res_TxtDao extends BaseDao implements Dao<EV_Module_Res_Txt> {
    private final Mapper<EV_Module_Res_Txt, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_Module_Res_Txt> toEV_Module_Res_TxtMapper;

    public static final String TABLE = "ev_module_res_txts";
    public static final String MODULE_CODE = "module_code";
    public static final String RESOURCE_CODE = "resource_code";
    public static final String TXT_CODE = "txt_code";
    public static final String TXT_REF = "txt_ref";
    private String[] columns = {MODULE_CODE, RESOURCE_CODE, TXT_CODE, TXT_REF};


    public EV_Module_Res_TxtDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new EV_Module_Res_TxtToContentValuesMapper();
        this.toEV_Module_Res_TxtMapper = new CursorEV_Module_Res_TxtMapper();
    }

    @Override
    public void addUpdate(EV_Module_Res_Txt module_res_txt) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(module_res_txt)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(MODULE_CODE).append(" = '").append(module_res_txt.getModule_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(RESOURCE_CODE).append(" = '").append(module_res_txt.getResource_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TXT_CODE).append(" = '").append(module_res_txt.getTxt_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(module_res_txt), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<EV_Module_Res_Txt> ev_module_res_txts, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_Module_Res_Txt module_res_txt : ev_module_res_txts) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(module_res_txt)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(MODULE_CODE).append(" = '").append(module_res_txt.getModule_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(RESOURCE_CODE).append(" = '").append(module_res_txt.getResource_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(TXT_CODE).append(" = '").append(module_res_txt.getTxt_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(module_res_txt), sbWhere.toString(), null);
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
    public EV_Module_Res_Txt getByString(String s_query) {
        EV_Module_Res_Txt module_res_txt = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                module_res_txt = toEV_Module_Res_TxtMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return module_res_txt;
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
    public List<EV_Module_Res_Txt> query(String s_query) {
        List<EV_Module_Res_Txt> module_res_txts = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                EV_Module_Res_Txt uAux = toEV_Module_Res_TxtMapper.map(cursor);
                module_res_txts.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return module_res_txts;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> module_res_txts = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                module_res_txts.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return module_res_txts;
    }

    private class CursorEV_Module_Res_TxtMapper implements Mapper<Cursor, EV_Module_Res_Txt> {
        @Override
        public EV_Module_Res_Txt map(Cursor cursor) {
            EV_Module_Res_Txt module_res_txt = new EV_Module_Res_Txt();

            module_res_txt.setModule_code(cursor.getString(cursor.getColumnIndex(MODULE_CODE)));
            module_res_txt.setResource_code(cursor.getInt(cursor.getColumnIndex(RESOURCE_CODE)));
            module_res_txt.setTxt_code(cursor.getString(cursor.getColumnIndex(TXT_CODE)));
            module_res_txt.setTxt_ref(cursor.getInt(cursor.getColumnIndex(TXT_REF)));

            return module_res_txt;
        }
    }

    private class EV_Module_Res_TxtToContentValuesMapper implements Mapper<EV_Module_Res_Txt, ContentValues> {
        @Override
        public ContentValues map(EV_Module_Res_Txt module_res_txt) {
            ContentValues contentValues = new ContentValues();

            if (module_res_txt.getModule_code() != null) {
                contentValues.put(MODULE_CODE, module_res_txt.getModule_code());
            }
            if (module_res_txt.getResource_code() > -1) {
                contentValues.put(RESOURCE_CODE, module_res_txt.getResource_code());
            }
            if (module_res_txt.getTxt_code() != null) {
                contentValues.put(TXT_CODE, module_res_txt.getTxt_code());
            }
            if (module_res_txt.getTxt_ref() > -1) {
                contentValues.put(TXT_REF, module_res_txt.getTxt_ref());
            }

            return contentValues;
        }
    }


}
