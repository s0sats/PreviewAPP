package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task_File;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.dao.SM_SO_PackDao.PACK_SEQ;
import static com.namoadigital.prj001.dao.SM_SO_ServiceDao.SERVICE_SEQ;


/**
 * Created by neomatrix on 05/07/17.
 */

public class SM_SO_Service_Exec_Task_FileDao extends BaseDao implements DaoTmp<SM_SO_Service_Exec_Task_File> {

    private final Mapper<SM_SO_Service_Exec_Task_File, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SM_SO_Service_Exec_Task_File> toSM_SO_Service_Exec_Task_FileMapper;

    public static final String TABLE = "sm_so_service_exec_task_files";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String PRICE_LIST_CODE = "price_list_code";
    public static final String PACK_CODE = "pack_code";
    public static final String CATEGORY_PRICE_CODE = "category_price_code";
    public static final String SERVICE_CODE = "service_code";
    public static final String EXEC_CODE = "exec_code";
    public static final String EXEC_TMP = "exec_tmp";
    public static final String TASK_CODE = "task_code";
    public static final String TASK_TMP = "task_tmp";
    public static final String FILE_CODE = "file_code";
    public static final String FILE_TMP = "file_tmp";
    public static final String FILE_NAME = "file_name";
    public static final String FILE_URL = "file_url";

    public SM_SO_Service_Exec_Task_FileDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SM_SO_Service_Exec_Task_FileToContentValuesMapper();
        this.toSM_SO_Service_Exec_Task_FileMapper = new CursorSM_SO_Service_Exec_Task_FileMapper();
    }

    @Override
    public void addUpdateTmp(SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service_exec_task_file)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPrice_list_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPack_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPack_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getCategory_price_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getService_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getService_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(EXEC_TMP).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getExec_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TASK_TMP).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getTask_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(FILE_TMP).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getFile_code())).append("'");


                db.update(TABLE, toContentValuesMapper.map(sm_so_service_exec_task_file), sbWhere.toString(), null);
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdateTmp(Iterable<SM_SO_Service_Exec_Task_File> sm_so_service_exec_task_files, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file : sm_so_service_exec_task_files) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service_exec_task_file)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getSo_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPrice_list_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPack_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPack_seq())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getCategory_price_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getService_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getService_seq())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(EXEC_TMP).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getExec_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(TASK_TMP).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getTask_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(FILE_TMP).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getFile_code())).append("'");


                    db.update(TABLE, toContentValuesMapper.map(sm_so_service_exec_task_file), sbWhere.toString(), null);
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
    public void addUpdate(SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file) {
        openDB();

        try {
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getSo_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getSo_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPrice_list_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPack_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPack_seq())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getCategory_price_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getService_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getService_seq())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(EXEC_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getExec_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TASK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getTask_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(FILE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getFile_code())).append("'");


            if (db.update(TABLE, toContentValuesMapper.map(sm_so_service_exec_task_file), sbWhere.toString(), null) == 0) {
                db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service_exec_task_file));
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SM_SO_Service_Exec_Task_File> sm_so_service_exec_task_files, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }
            long file_temp = 300;
            //
            for (SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file : sm_so_service_exec_task_files) {
                //
                sm_so_service_exec_task_file.setFile_tmp(file_temp);
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPrice_list_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPack_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getPack_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getCategory_price_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getService_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getService_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(EXEC_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getExec_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TASK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getTask_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(FILE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task_file.getFile_code())).append("'");


                if (db.update(TABLE, toContentValuesMapper.map(sm_so_service_exec_task_file), sbWhere.toString(), null) == 0) {
                    db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service_exec_task_file));
                }
                //Atualiza valor do file.
                file_temp++;
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
    public SM_SO_Service_Exec_Task_File getByString(String sQuery) {
        SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_service_exec_task_file = toSM_SO_Service_Exec_Task_FileMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();


        return sm_so_service_exec_task_file;
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
    public List<SM_SO_Service_Exec_Task_File> query(String sQuery) {
        List<SM_SO_Service_Exec_Task_File> sm_so_service_exec_task_files = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                SM_SO_Service_Exec_Task_File uAux = toSM_SO_Service_Exec_Task_FileMapper.map(cursor);
                sm_so_service_exec_task_files.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_service_exec_task_files;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        ArrayList<HMAux> sm_so_service_exec_task_files = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                sm_so_service_exec_task_files.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_service_exec_task_files;
    }

    private class CursorSM_SO_Service_Exec_Task_FileMapper implements Mapper<Cursor, SM_SO_Service_Exec_Task_File> {
        @Override
        public SM_SO_Service_Exec_Task_File map(Cursor cursor) {

            SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file = new SM_SO_Service_Exec_Task_File();

            sm_so_service_exec_task_file.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            sm_so_service_exec_task_file.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            sm_so_service_exec_task_file.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            sm_so_service_exec_task_file.setPrice_list_code(cursor.getInt(cursor.getColumnIndex(PRICE_LIST_CODE)));
            sm_so_service_exec_task_file.setPack_code(cursor.getInt(cursor.getColumnIndex(PACK_CODE)));
            sm_so_service_exec_task_file.setPack_seq(cursor.getInt(cursor.getColumnIndex(PACK_SEQ)));
            sm_so_service_exec_task_file.setCategory_price_code(cursor.getInt(cursor.getColumnIndex(CATEGORY_PRICE_CODE)));
            sm_so_service_exec_task_file.setService_code(cursor.getInt(cursor.getColumnIndex(SERVICE_CODE)));
            sm_so_service_exec_task_file.setService_seq(cursor.getInt(cursor.getColumnIndex(SERVICE_SEQ)));
            sm_so_service_exec_task_file.setExec_code(cursor.getInt(cursor.getColumnIndex(EXEC_CODE)));
            sm_so_service_exec_task_file.setTask_code(cursor.getInt(cursor.getColumnIndex(TASK_CODE)));
            sm_so_service_exec_task_file.setFile_code(cursor.getInt(cursor.getColumnIndex(FILE_CODE)));
            sm_so_service_exec_task_file.setExec_tmp(cursor.getLong(cursor.getColumnIndex(FILE_TMP)));
            sm_so_service_exec_task_file.setTask_tmp(cursor.getLong(cursor.getColumnIndex(FILE_TMP)));
            sm_so_service_exec_task_file.setFile_tmp(cursor.getLong(cursor.getColumnIndex(FILE_TMP)));
            sm_so_service_exec_task_file.setFile_name(cursor.getString(cursor.getColumnIndex(FILE_NAME)));
            sm_so_service_exec_task_file.setFile_url(cursor.getString(cursor.getColumnIndex(FILE_URL)));

            return sm_so_service_exec_task_file;
        }
    }

    private class SM_SO_Service_Exec_Task_FileToContentValuesMapper implements Mapper<SM_SO_Service_Exec_Task_File, ContentValues> {

        @Override
        public ContentValues map(SM_SO_Service_Exec_Task_File sm_so_service_exec_task_file) {
            ContentValues contentValues = new ContentValues();

            if (sm_so_service_exec_task_file.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, sm_so_service_exec_task_file.getCustomer_code());
            }

            if (sm_so_service_exec_task_file.getSo_prefix() > -1) {
                contentValues.put(SO_PREFIX, sm_so_service_exec_task_file.getSo_prefix());
            }

            if (sm_so_service_exec_task_file.getSo_code() > -1) {
                contentValues.put(SO_CODE, sm_so_service_exec_task_file.getSo_code());
            }

            if (sm_so_service_exec_task_file.getPrice_list_code() > -1) {
                contentValues.put(PRICE_LIST_CODE, sm_so_service_exec_task_file.getPrice_list_code());
            }

            if (sm_so_service_exec_task_file.getPack_code() > -1) {
                contentValues.put(PACK_CODE, sm_so_service_exec_task_file.getPack_code());
            }

            if (sm_so_service_exec_task_file.getPack_seq() > -1) {
                contentValues.put(PACK_SEQ, sm_so_service_exec_task_file.getPack_seq());
            }

            if (sm_so_service_exec_task_file.getCategory_price_code() > -1) {
                contentValues.put(CATEGORY_PRICE_CODE, sm_so_service_exec_task_file.getCategory_price_code());
            }

            if (sm_so_service_exec_task_file.getService_code() > -1) {
                contentValues.put(SERVICE_CODE, sm_so_service_exec_task_file.getService_code());
            }

            if (sm_so_service_exec_task_file.getService_seq() > -1) {
                contentValues.put(SERVICE_SEQ, sm_so_service_exec_task_file.getService_seq());
            }

            if (sm_so_service_exec_task_file.getExec_code() > -1) {
                contentValues.put(EXEC_CODE, sm_so_service_exec_task_file.getExec_code());
            }

            if (sm_so_service_exec_task_file.getTask_code() > -1) {
                contentValues.put(TASK_CODE, sm_so_service_exec_task_file.getTask_code());
            }

            if (sm_so_service_exec_task_file.getFile_code() > -1) {
                contentValues.put(FILE_CODE, sm_so_service_exec_task_file.getFile_code());
            }

            if (sm_so_service_exec_task_file.getExec_tmp() > -1) {
                contentValues.put(EXEC_TMP, sm_so_service_exec_task_file.getExec_tmp());
            }

            if (sm_so_service_exec_task_file.getTask_tmp() > -1) {
                contentValues.put(TASK_TMP, sm_so_service_exec_task_file.getTask_tmp());
            }
            if (sm_so_service_exec_task_file.getFile_tmp() > -1) {
                contentValues.put(FILE_TMP, sm_so_service_exec_task_file.getFile_tmp());
            }

            if (sm_so_service_exec_task_file.getFile_name() != null) {
                contentValues.put(FILE_NAME, sm_so_service_exec_task_file.getFile_name());
            }

            if (sm_so_service_exec_task_file.getFile_url() != null) {
                contentValues.put(FILE_URL, sm_so_service_exec_task_file.getFile_url());
            }

            return contentValues;
        }
    }

}
