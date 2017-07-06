package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_File;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 05/07/17.
 */

public class SM_SO_FileDao extends BaseDao implements Dao<SM_SO_File> {

    private final Mapper<SM_SO_File, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SM_SO_File> toSM_SO_FileMapper;

    public static final String TABLE = "sm_so_files";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String FILE_CODE = "file_code";
    public static final String FILE_NAME = "file_name";
    public static final String FILE_URL = "file_url";

    public SM_SO_FileDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SM_SO_FileToContentValuesMapper();
        this.toSM_SO_FileMapper = new CursorSM_SO_FileMapper();
    }

    @Override
    public void addUpdate(SM_SO_File sm_so_file) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_file)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_file.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_file.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_file.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(FILE_CODE).append(" = '").append(String.valueOf(sm_so_file.getFile_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(sm_so_file), sbWhere.toString(), null);
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SM_SO_File> sm_so_files, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (SM_SO_File sm_so_file : sm_so_files) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_file)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_file.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_file.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_file.getSo_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(FILE_CODE).append(" = '").append(String.valueOf(sm_so_file.getFile_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(sm_so_file), sbWhere.toString(), null);
                }
            }

            //db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            //db.endTransaction();
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
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public SM_SO_File getByString(String sQuery) {
        SM_SO_File sm_so_file = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_file = toSM_SO_FileMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();


        return sm_so_file;
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
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return hmAux;
    }

    @Override
    public List<SM_SO_File> query(String sQuery) {
        List<SM_SO_File> sm_so_files = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                SM_SO_File uAux = toSM_SO_FileMapper.map(cursor);
                sm_so_files.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_files;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        ArrayList<HMAux> sm_so_packs = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                sm_so_packs.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_packs;
    }


    private class CursorSM_SO_FileMapper implements Mapper<Cursor, SM_SO_File> {
        @Override
        public SM_SO_File map(Cursor cursor) {
            SM_SO_File sm_so_file = new SM_SO_File();

            sm_so_file.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            sm_so_file.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            sm_so_file.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            sm_so_file.setFile_code(cursor.getInt(cursor.getColumnIndex(FILE_CODE)));
            sm_so_file.setFile_name(cursor.getString(cursor.getColumnIndex(FILE_NAME)));
            sm_so_file.setFile_url(cursor.getString(cursor.getColumnIndex(FILE_URL)));

            return sm_so_file;
        }
    }

    private class SM_SO_FileToContentValuesMapper implements Mapper<SM_SO_File, ContentValues> {

        @Override
        public ContentValues map(SM_SO_File sm_so_file) {

            ContentValues contentValues = new ContentValues();

            if (sm_so_file.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, sm_so_file.getCustomer_code());
            }

            if (sm_so_file.getSo_prefix() > -1) {
                contentValues.put(SO_PREFIX, sm_so_file.getSo_prefix());
            }

            if (sm_so_file.getSo_code() > -1) {
                contentValues.put(SO_CODE, sm_so_file.getSo_code());
            }

            if (sm_so_file.getFile_code() > -1) {
                contentValues.put(FILE_CODE, sm_so_file.getFile_code());
            }

            if (sm_so_file.getFile_name() != null) {
                contentValues.put(FILE_NAME, sm_so_file.getFile_name());
            }

            if (sm_so_file.getFile_url() != null) {
                contentValues.put(FILE_URL, sm_so_file.getFile_url());
            }

            return contentValues;
        }
    }
}
