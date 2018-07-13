package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.CH_File;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class CH_FileDao extends BaseDao implements Dao<CH_File> {
    private final Mapper<CH_File, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, CH_File> toCH_FileMapper;

    public static final String TABLE = "ch_files";
    public static final String FILE_CODE = "file_code";
    public static final String FILE_PATH = "file_path";
    public static final String FILE_PATH_NEW = "file_path_new";
    public static final String FILE_STATUS = "file_status";
    public static final String FILE_DATE = "file_date";


    public CH_FileDao(Context context) {
        super(context, Constant.DB_FULL_CHAT, Constant.DB_VERSION_CHAT, Constant.DB_MODE_CHAT);

        this.toContentValuesMapper = new CH_FileToContentValuesMapper();
        this.toCH_FileMapper = new CursorCH_FileMapper();
    }

    public CH_FileDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new CH_FileToContentValuesMapper();
        this.toCH_FileMapper = new CursorCH_FileMapper();
    }

    @Override
    public void addUpdate(CH_File ch_file) {
        try {
            openDB();

            if (db.insert(TABLE, null, toContentValuesMapper.map(ch_file)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(FILE_CODE).append(" = '").append(ch_file.getFile_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(ch_file), sbWhere.toString(), null);
            }

        } catch (Exception e) {
            String resultado = e.toString();
            int i = 10;
        } finally {
        }

        closeDB();


    }

    @Override
    public void addUpdate(Iterable<CH_File> ch_files, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (CH_File ch_file : ch_files) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(ch_file)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(FILE_CODE).append(" = '").append(ch_file.getFile_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(ch_file), sbWhere.toString(), null);
                }
            }

            //db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
            //db.endTransaction();
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
    public CH_File getByString(String s_query) {
        CH_File ch_file = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                ch_file = toCH_FileMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return ch_file;
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
    public List<CH_File> query(String s_query) {
        List<CH_File> ch_files = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                CH_File uAux = toCH_FileMapper.map(cursor);
                ch_files.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return ch_files;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> ch_files = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                ch_files.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return ch_files;
    }

    private class CursorCH_FileMapper implements Mapper<Cursor,CH_File> {
        @Override
        public CH_File map(Cursor cursor) {
            CH_File ch_file = new CH_File();

            ch_file.setFile_code(cursor.getString(cursor.getColumnIndex(FILE_CODE)));
            ch_file.setFile_path(cursor.getString(cursor.getColumnIndex(FILE_PATH)));
            if(cursor.isNull(cursor.getColumnIndex(FILE_PATH_NEW))){
                ch_file.setFile_path_new(null);
            }else{
                ch_file.setFile_path_new(cursor.getString(cursor.getColumnIndex(FILE_PATH_NEW)));
            }

            ch_file.setFile_status(cursor.getString(cursor.getColumnIndex(FILE_STATUS)));
            ch_file.setFile_date(cursor.getString(cursor.getColumnIndex(FILE_DATE)));

            return ch_file;
        }

    }

    private class CH_FileToContentValuesMapper implements Mapper<CH_File,ContentValues> {
        @Override
        public ContentValues map(CH_File ch_file) {
            ContentValues contentValues = new ContentValues();

            if (ch_file.getFile_code() != null) {
                contentValues.put(FILE_CODE, ch_file.getFile_code());
            }
            if (ch_file.getFile_path() != null) {
                contentValues.put(FILE_PATH, ch_file.getFile_path());
            }

            contentValues.put(FILE_PATH_NEW, ch_file.getFile_path_new());

            if (ch_file.getFile_status() != null) {
                contentValues.put(FILE_STATUS, ch_file.getFile_status());
            }
            if (ch_file.getFile_date() != null) {
                contentValues.put(FILE_DATE, ch_file.getFile_date());
            }

            return contentValues;
        }
    }
}
