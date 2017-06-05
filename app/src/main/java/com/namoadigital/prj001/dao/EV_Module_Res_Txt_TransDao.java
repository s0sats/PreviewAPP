package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.EV_Module_Res_Txt_Trans;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_Module_Res_Txt_TransDao extends BaseDao implements Dao<EV_Module_Res_Txt_Trans> {
    private final Mapper<EV_Module_Res_Txt_Trans, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_Module_Res_Txt_Trans> toEV_Module_Res_Txt_TransMapper;

    public static final String TABLE = "ev_module_res_txt_transs";
    public static final String MODULE_CODE = "module_code";
    public static final String RESOURCE_CODE = "resource_code";
    public static final String TXT_CODE = "txt_code";
    public static final String TRANSLATE_CODE = "translate_code";
    public static final String TXT_VALUE = "txt_value";
    private String[] columns = {MODULE_CODE, RESOURCE_CODE, TXT_CODE, TRANSLATE_CODE, TXT_VALUE};

    public EV_Module_Res_Txt_TransDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new EV_Module_Res_Txt_TransToContentValuesMapper();
        this.toEV_Module_Res_Txt_TransMapper = new CursorEV_Module_Res_Txt_TransMapper();
    }

    @Override
    public void addUpdate(EV_Module_Res_Txt_Trans module_res_txt_trans) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(module_res_txt_trans)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(MODULE_CODE).append(" = '").append(module_res_txt_trans.getModule_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(RESOURCE_CODE).append(" = '").append(module_res_txt_trans.getResource_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TXT_CODE).append(" = '").append(module_res_txt_trans.getTxt_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TRANSLATE_CODE).append(" = '").append(module_res_txt_trans.getTranslate_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(module_res_txt_trans), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<EV_Module_Res_Txt_Trans> module_res_txt_transs, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_Module_Res_Txt_Trans module_res_txt_trans : module_res_txt_transs) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(module_res_txt_trans)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(MODULE_CODE).append(" = '").append(module_res_txt_trans.getModule_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(RESOURCE_CODE).append(" = '").append(module_res_txt_trans.getResource_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(TXT_CODE).append(" = '").append(module_res_txt_trans.getTxt_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(TRANSLATE_CODE).append(" = '").append(module_res_txt_trans.getTranslate_code()).append("'");


                    db.update(TABLE, toContentValuesMapper.map(module_res_txt_trans), sbWhere.toString(), null);
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
    public EV_Module_Res_Txt_Trans getByString(String s_query) {
        EV_Module_Res_Txt_Trans module_res_txt_trans = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                module_res_txt_trans = toEV_Module_Res_Txt_TransMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return module_res_txt_trans;
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
    public List<EV_Module_Res_Txt_Trans> query(String s_query) {
        List<EV_Module_Res_Txt_Trans> module_res_txt_transs = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                EV_Module_Res_Txt_Trans uAux = toEV_Module_Res_Txt_TransMapper.map(cursor);
                module_res_txt_transs.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return module_res_txt_transs;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> module_res_txt_transs = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                module_res_txt_transs.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return module_res_txt_transs;
    }

    private class CursorEV_Module_Res_Txt_TransMapper implements Mapper<Cursor, EV_Module_Res_Txt_Trans> {
        @Override
        public EV_Module_Res_Txt_Trans map(Cursor cursor) {
            EV_Module_Res_Txt_Trans module_res_txt_trans = new EV_Module_Res_Txt_Trans();

            module_res_txt_trans.setModule_code(cursor.getString(cursor.getColumnIndex(MODULE_CODE)));
            module_res_txt_trans.setResource_code(cursor.getInt(cursor.getColumnIndex(RESOURCE_CODE)));
            module_res_txt_trans.setTxt_code(cursor.getString(cursor.getColumnIndex(TXT_CODE)));
            module_res_txt_trans.setTranslate_code(cursor.getInt(cursor.getColumnIndex(TRANSLATE_CODE)));
            module_res_txt_trans.setTxt_value(cursor.getString(cursor.getColumnIndex(TXT_VALUE)));

            return module_res_txt_trans;
        }
    }

    private class EV_Module_Res_Txt_TransToContentValuesMapper implements Mapper<EV_Module_Res_Txt_Trans, ContentValues> {
        @Override
        public ContentValues map(EV_Module_Res_Txt_Trans module_res_txt_trans) {
            ContentValues contentValues = new ContentValues();

            if (module_res_txt_trans.getModule_code() != null) {
                contentValues.put(MODULE_CODE, module_res_txt_trans.getModule_code());
            }
            if (module_res_txt_trans.getResource_code() > -1) {
                contentValues.put(RESOURCE_CODE, module_res_txt_trans.getResource_code());
            }
            if (module_res_txt_trans.getTxt_code() != null) {
                contentValues.put(TXT_CODE, module_res_txt_trans.getTxt_code());
            }
            if (module_res_txt_trans.getTranslate_code() > -1) {
                contentValues.put(TRANSLATE_CODE, module_res_txt_trans.getTranslate_code());
            }
            if (module_res_txt_trans.getTxt_value() != null) {
                contentValues.put(TXT_VALUE, module_res_txt_trans.getTxt_value());
            }

            return contentValues;
        }
    }
}
