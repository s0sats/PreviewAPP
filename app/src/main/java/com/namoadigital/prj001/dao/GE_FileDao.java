package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class GE_FileDao extends BaseDao implements Dao<GE_File> {
    private final Mapper<GE_File, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_File> toGE_FileMapper;

    public static final String TABLE = "ge_files";
    public static final String FILE_CODE = "file_code";
    public static final String FILE_PATH = "file_path";
    public static final String FILE_STATUS = "file_status";
    public static final String FILE_DATE = "file_date";


    public GE_FileDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new GE_FileToContentValuesMapper();
        this.toGE_FileMapper = new CursorGE_FileMapper();
    }

    @Override
    public void addUpdate(GE_File ge_file) {
        try {

            openDB();

            if (db.insert(TABLE, null, toContentValuesMapper.map(ge_file)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(FILE_CODE).append(" = '").append(ge_file.getFile_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(ge_file), sbWhere.toString(), null);
            }

        } catch (Exception e) {
            String resultado = e.toString();
            int i = 10;
        } finally {
        }

        closeDB();


    }

    @Override
    public void addUpdate(Iterable<GE_File> ge_files, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_File ge_file : ge_files) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(ge_file)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(FILE_CODE).append(" = '").append(ge_file.getFile_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(ge_file), sbWhere.toString(), null);
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
    public GE_File getByString(String s_query) {
        GE_File ge_file = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                ge_file = toGE_FileMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return ge_file;
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
    public List<GE_File> query(String s_query) {
        List<GE_File> ge_files = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_File uAux = toGE_FileMapper.map(cursor);
                ge_files.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return ge_files;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> ge_files = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                ge_files.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return ge_files;
    }

    private class CursorGE_FileMapper implements Mapper<Cursor,GE_File> {
        @Override
        public GE_File map(Cursor cursor) {
            GE_File ge_file = new GE_File();

            ge_file.setFile_code(cursor.getString(cursor.getColumnIndex(FILE_CODE)));
            ge_file.setFile_path(cursor.getString(cursor.getColumnIndex(FILE_PATH)));
            ge_file.setFile_status(cursor.getString(cursor.getColumnIndex(FILE_STATUS)));
            ge_file.setFile_date(cursor.getString(cursor.getColumnIndex(FILE_DATE)));

            return ge_file;
        }

    }

    private class GE_FileToContentValuesMapper implements Mapper<GE_File,ContentValues> {
        @Override
        public ContentValues map(GE_File ge_file) {
            ContentValues contentValues = new ContentValues();

            if (ge_file.getFile_code() != null) {
                contentValues.put(FILE_CODE, ge_file.getFile_code());
            }
            if (ge_file.getFile_path() != null) {
                contentValues.put(FILE_PATH, ge_file.getFile_path());
            }
            if (ge_file.getFile_status() != null) {
                contentValues.put(FILE_STATUS, ge_file.getFile_status());
            }
            if (ge_file.getFile_date() != null) {
                contentValues.put(FILE_DATE, ge_file.getFile_date());
            }

            return contentValues;

        }
    }
}
