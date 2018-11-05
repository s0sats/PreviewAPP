package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Product_Event_File;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 30/10/2017.
 */

public class SM_SO_Product_Event_FileDao extends BaseDao implements Dao<SM_SO_Product_Event_File>, DaoTmp<SM_SO_Product_Event_File> {


    private final Mapper<SM_SO_Product_Event_File,ContentValues> toContentValuesMapper;
    private final Mapper<Cursor,SM_SO_Product_Event_File> toSM_SO_Product_Event_FileMapper;

    public static final String TABLE = "sm_so_product_event_files";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String SEQ = "seq";
    public static final String SEQ_TMP = "seq_tmp";
    public static final String FILE_CODE = "file_code";
    public static final String FILE_TMP = "file_tmp";
    public static final String FILE_NAME = "file_name";
    public static final String FILE_URL = "file_url";
    public static final String FILE_URL_LOCAL = "file_url_local";

    public static final String[] columns = {
            CUSTOMER_CODE, SO_PREFIX, SO_CODE, SEQ, SEQ_TMP, FILE_CODE,
            FILE_TMP, FILE_NAME, FILE_URL ,FILE_URL_LOCAL
    };

    public SM_SO_Product_Event_FileDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new SM_SO_Product_Event_FileToContentValuesMapper();
        this.toSM_SO_Product_Event_FileMapper = new CursorSM_SO_Product_Event_FileMapper();
    }

        @Override
    public void addUpdateTmp(SM_SO_Product_Event_File sm_so_product_event_file) {
            openDB();

            try {
                if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event_file)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event_file.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event_file.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event_file.getSo_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SEQ_TMP).append(" = '").append(String.valueOf(sm_so_product_event_file.getSeq_tmp())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(FILE_TMP).append(" = '").append(String.valueOf(sm_so_product_event_file.getFile_tmp())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(sm_so_product_event_file), sbWhere.toString(), null);
                }

            } catch (Exception e) {
                //ToolBox_Inf.registerException(getClass().getName(), e);
            } finally {
            }

            closeDB();
    }

    @Override
    public void addUpdateTmp(Iterable<SM_SO_Product_Event_File> sm_so_product_event_files, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (SM_SO_Product_Event_File sm_so_product_event_file : sm_so_product_event_files) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event_file)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event_file.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event_file.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event_file.getSo_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SEQ_TMP).append(" = '").append(String.valueOf(sm_so_product_event_file.getSeq_tmp())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(FILE_TMP).append(" = '").append(String.valueOf(sm_so_product_event_file.getFile_tmp())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(sm_so_product_event_file), sbWhere.toString(), null);
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
    public void addUpdate(SM_SO_Product_Event_File sm_so_product_event_file ) {
        openDB();

        try {
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event_file.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event_file.getSo_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event_file.getSo_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SEQ).append(" = '").append(String.valueOf(sm_so_product_event_file.getSeq_tmp())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(FILE_CODE).append(" = '").append(String.valueOf(sm_so_product_event_file.getFile_tmp())).append("'");

            if (db.update(TABLE, toContentValuesMapper.map(sm_so_product_event_file), sbWhere.toString(), null) == 0) {
                db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event_file));
            }

        } catch (Exception e) {
           // ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SM_SO_Product_Event_File> sm_so_product_event_files , boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            //Define contador inicial para o criação do temp;
            int file_tmp = 200;
            //
            for (SM_SO_Product_Event_File sm_so_product_event_file : sm_so_product_event_files) {
                file_tmp++;
                //
                sm_so_product_event_file.setFile_tmp(file_tmp);
                //
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event_file.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event_file.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event_file.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SEQ).append(" = '").append(String.valueOf(sm_so_product_event_file.getSeq_tmp())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(FILE_CODE).append(" = '").append(String.valueOf(sm_so_product_event_file.getFile_tmp())).append("'");

                if (db.update(TABLE, toContentValuesMapper.map(sm_so_product_event_file), sbWhere.toString(), null) == 0) {
                    db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event_file));
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
        } finally {
        }

        closeDB();
    }

    @Override
    public SM_SO_Product_Event_File getByString(String sQuery) {
        SM_SO_Product_Event_File sm_so_product_event_file = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_product_event_file = toSM_SO_Product_Event_FileMapper.map(cursor);
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_product_event_file;
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
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return hmAux;
    }

    @Override
    public List<SM_SO_Product_Event_File> query(String sQuery) {
        List<SM_SO_Product_Event_File> sm_so_product_event_files = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                SM_SO_Product_Event_File uAux = toSM_SO_Product_Event_FileMapper.map(cursor);
                sm_so_product_event_files.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return sm_so_product_event_files;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        ArrayList<HMAux> sm_so_product_event_files = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_product_event_files.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_product_event_files;
    }

    private class CursorSM_SO_Product_Event_FileMapper implements Mapper<Cursor,SM_SO_Product_Event_File> {
        @Override
        public SM_SO_Product_Event_File map(Cursor cursor) {
            SM_SO_Product_Event_File sm_so_product_event_file = new SM_SO_Product_Event_File();

            sm_so_product_event_file.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            sm_so_product_event_file.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            sm_so_product_event_file.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            sm_so_product_event_file.setSeq(cursor.getInt(cursor.getColumnIndex(SEQ)));
            sm_so_product_event_file.setSeq_tmp(cursor.getInt(cursor.getColumnIndex(SEQ_TMP)));
            sm_so_product_event_file.setFile_code(cursor.getInt(cursor.getColumnIndex(FILE_CODE)));
            sm_so_product_event_file.setFile_tmp(cursor.getInt(cursor.getColumnIndex(FILE_TMP)));
            sm_so_product_event_file.setFile_name(cursor.getString(cursor.getColumnIndex(FILE_NAME)));
            if(cursor.isNull(cursor.getColumnIndex(FILE_URL))){
                sm_so_product_event_file.setFile_url(null);
            }else{
                sm_so_product_event_file.setFile_url(cursor.getString(cursor.getColumnIndex(FILE_URL)));
            }
            if(cursor.isNull(cursor.getColumnIndex(FILE_URL_LOCAL))){
                sm_so_product_event_file.setFile_url_local(null);
            }else{
                sm_so_product_event_file.setFile_url_local(cursor.getString(cursor.getColumnIndex(FILE_URL_LOCAL)));
            }
            return sm_so_product_event_file;
        }
    }


    private class SM_SO_Product_Event_FileToContentValuesMapper implements Mapper<SM_SO_Product_Event_File,ContentValues> {
        @Override
        public ContentValues map(SM_SO_Product_Event_File sm_so_product_event_file) {
            ContentValues contentValues = new ContentValues();

            if(sm_so_product_event_file.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,sm_so_product_event_file.getCustomer_code());
            }
            if(sm_so_product_event_file.getSo_prefix() > -1){
                contentValues.put(SO_PREFIX,sm_so_product_event_file.getSo_prefix());
            }
            if(sm_so_product_event_file.getSo_code() > -1){
                contentValues.put(SO_CODE,sm_so_product_event_file.getSo_code());
            }
            if(sm_so_product_event_file.getSeq() > -1){
                contentValues.put(SEQ,sm_so_product_event_file.getSeq());
            }
            if(sm_so_product_event_file.getSeq_tmp() > -1){
                contentValues.put(SEQ_TMP,sm_so_product_event_file.getSeq_tmp());
            }
            if(sm_so_product_event_file.getFile_code() > -1){
                contentValues.put(FILE_CODE,sm_so_product_event_file.getFile_code());
            }
            if(sm_so_product_event_file.getFile_tmp() > -1){
                contentValues.put(FILE_TMP,sm_so_product_event_file.getFile_tmp());
            }
            if(sm_so_product_event_file.getFile_name() != null){
                contentValues.put(FILE_NAME,sm_so_product_event_file.getFile_name());
            }
            if(sm_so_product_event_file.getFile_url() != null){
                contentValues.put(FILE_URL,sm_so_product_event_file.getFile_url());
            }
            if(sm_so_product_event_file.getFile_url_local() != null){
                contentValues.put(FILE_URL_LOCAL,sm_so_product_event_file.getFile_url_local());
            }

            return contentValues;
        }
    }

}
