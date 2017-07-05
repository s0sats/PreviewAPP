package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_File;
import com.namoadigital.prj001.util.Constant;

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
    public static final String file_url = "file_url";

    public SM_SO_FileDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SM_SO_FileToContentValuesMapper();
        this.toSM_SO_FileMapper = new CursorSM_SO_FileMapper();
    }

    @Override
    public void addUpdate(SM_SO_File item) {

    }

    @Override
    public void addUpdate(Iterable<SM_SO_File> items, boolean status) {

    }

    @Override
    public void addUpdate(String sQuery) {

    }

    @Override
    public void remove(String sQuery) {

    }

    @Override
    public SM_SO_File getByString(String sQuery) {
        return null;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        return null;
    }

    @Override
    public List<SM_SO_File> query(String sQuery) {
        return null;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        return null;
    }



    private class CursorSM_SO_FileMapper implements Mapper<Cursor, SM_SO_File> {
        @Override
        public SM_SO_File map(Cursor cursor) {
            return null;
        }
    }

    private class SM_SO_FileToContentValuesMapper implements Mapper<SM_SO_File, ContentValues> {

        @Override
        public ContentValues map(SM_SO_File sm_so_file) {
            return null;
        }
    }
}
