package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task_File;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

import static com.namoadigital.prj001.dao.SM_SO_PackDao.PACK_SEQ;
import static com.namoadigital.prj001.dao.SM_SO_ServiceDao.SERVICE_SEQ;

/**
 * Created by neomatrix on 05/07/17.
 */

public class SM_SO_Service_Exec_TaskDao extends BaseDao implements DaoTmp<SM_SO_Service_Exec_Task> {

    private final Mapper<SM_SO_Service_Exec_Task, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SM_SO_Service_Exec_Task> toSM_SO_Service_Exec_TaskMapper;

    public static final String TABLE = "sm_so_service_exec_tasks";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String PRICE_LIST_CODE = "price_list_code";
    public static final String PACK_CODE = "pack_code";
    public static final String CATEGORY_PRICE_CODE = "category_price_code";
    public static final String SERVICE_CODE = "service_code";
    public static final String EXEC_CODE = "exec_code";
    public static final String TASK_CODE = "task_code";
    public static final String EXEC_TMP = "exec_tmp";
    public static final String TASK_TMP = "task_tmp";
    public static final String TASK_SEQ_OPER = "task_seq_oper";
    public static final String TASK_USER = "task_user";
    public static final String TASK_USER_NICK = "task_user_nick";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String EXEC_TIME = "exec_time";
    public static final String EXEC_TIME_FORMAT = "exec_time_format";
    public static final String TASK_PERC = "task_perc";
    public static final String QTY_PEOPLE = "qty_people";
    public static final String STATUS = "status";
    public static final String SITE_CODE = "site_code";
    public static final String SITE_ID = "site_id";
    public static final String SITE_DESC = "site_desc";
    public static final String ZONE_CODE = "zone_code";
    public static final String ZONE_ID = "zone_id";
    public static final String ZONE_DESC = "zone_desc";
    public static final String LOCAL_CODE = "local_code";
    public static final String LOCAL_ID = "local_id";
    public static final String COMMENTS = "comments";

    public SM_SO_Service_Exec_TaskDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SM_SO_Service_Exec_TaskToContentValuesMapper();
        this.toSM_SO_Service_Exec_TaskMapper = new CursorSM_SO_Service_Exec_TaskMapper();
    }

    @Override
    public void addUpdate(SM_SO_Service_Exec_Task sm_so_service_exec_task) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service_exec_task)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service_exec_task.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPrice_list_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPack_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPack_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getCategory_price_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getService_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task.getService_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(EXEC_TMP).append(" = '").append(String.valueOf(sm_so_service_exec_task.getExec_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TASK_TMP).append(" = '").append(String.valueOf(sm_so_service_exec_task.getTask_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(sm_so_service_exec_task), sbWhere.toString(), null);
            }

            SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            sm_so_service_exec_task_fileDao.addUpdate(sm_so_service_exec_task.getTask_file(), false);

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SM_SO_Service_Exec_Task> sm_so_service_exec_tasks, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            for (SM_SO_Service_Exec_Task sm_so_service_exec_task : sm_so_service_exec_tasks) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service_exec_task)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service_exec_task.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getSo_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPrice_list_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPack_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPack_seq())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getCategory_price_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getService_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task.getService_seq())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(EXEC_TMP).append(" = '").append(String.valueOf(sm_so_service_exec_task.getExec_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(TASK_TMP).append(" = '").append(String.valueOf(sm_so_service_exec_task.getTask_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(sm_so_service_exec_task), sbWhere.toString(), null);
                }

//                SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
//                        context,
//                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                        Constant.DB_VERSION_CUSTOM
//                );

                sm_so_service_exec_task_fileDao.addUpdate(sm_so_service_exec_task.getTask_file(), false);
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
    public void addUpdateServer(SM_SO_Service_Exec_Task sm_so_service_exec_task) {
        openDB();

        try {

            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service_exec_task.getSo_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getSo_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPrice_list_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPack_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPack_seq())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getCategory_price_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getService_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task.getService_seq())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(EXEC_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getExec_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TASK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getTask_code())).append("'");

            if (db.update(TABLE, toContentValuesMapper.map(sm_so_service_exec_task), sbWhere.toString(), null) == 0) {
                db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service_exec_task));

            }

            SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            sm_so_service_exec_task_fileDao.addUpdate(sm_so_service_exec_task.getTask_file(), false);

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdateServer(Iterable<SM_SO_Service_Exec_Task> sm_so_service_exec_tasks, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            for (SM_SO_Service_Exec_Task sm_so_service_exec_task : sm_so_service_exec_tasks) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service_exec_task.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPrice_list_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPack_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task.getPack_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getCategory_price_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getService_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec_task.getService_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(EXEC_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getExec_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TASK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec_task.getTask_code())).append("'");

                if (db.update(TABLE, toContentValuesMapper.map(sm_so_service_exec_task), sbWhere.toString(), null) == 0) {
                    db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service_exec_task));

                }

//                SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
//                        context,
//                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                        Constant.DB_VERSION_CUSTOM
//                );

                sm_so_service_exec_task_fileDao.addUpdate(sm_so_service_exec_task.getTask_file(), false);
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
    public SM_SO_Service_Exec_Task getByString(String sQuery) {
        SM_SO_Service_Exec_Task sm_so_service_exec_task = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_service_exec_task = toSM_SO_Service_Exec_TaskMapper.map(cursor);

                if (sm_so_service_exec_task != null) {
                    SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    sm_so_service_exec_task.setTask_file((ArrayList<SM_SO_Service_Exec_Task_File>) sm_so_service_exec_task_fileDao.query(new SM_SO_Service_Exec_Task_File_Sql_002(
                            sm_so_service_exec_task.getCustomer_code(),
                            sm_so_service_exec_task.getSo_prefix(),
                            sm_so_service_exec_task.getSo_code(),
                            sm_so_service_exec_task.getPrice_list_code(),
                            sm_so_service_exec_task.getPack_code(),
                            sm_so_service_exec_task.getPack_seq(),
                            sm_so_service_exec_task.getCategory_price_code(),
                            sm_so_service_exec_task.getService_code(),
                            sm_so_service_exec_task.getService_seq(),
                            sm_so_service_exec_task.getExec_tmp(),
                            sm_so_service_exec_task.getTask_tmp()
                    ).toSqlQuery()));
                }
                //
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();


        return sm_so_service_exec_task;
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
    public List<SM_SO_Service_Exec_Task> query(String sQuery) {
        List<SM_SO_Service_Exec_Task> sm_so_service_exec_tasks = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                SM_SO_Service_Exec_Task uAux = toSM_SO_Service_Exec_TaskMapper.map(cursor);
                //
                if (uAux != null) {
                    SM_SO_Service_Exec_Task_FileDao sm_so_service_exec_task_fileDao = new SM_SO_Service_Exec_Task_FileDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    uAux.setTask_file((ArrayList<SM_SO_Service_Exec_Task_File>) sm_so_service_exec_task_fileDao.query(new SM_SO_Service_Exec_Task_File_Sql_002(
                            uAux.getCustomer_code(),
                            uAux.getSo_prefix(),
                            uAux.getSo_code(),
                            uAux.getPrice_list_code(),
                            uAux.getPack_code(),
                            uAux.getPack_seq(),
                            uAux.getCategory_price_code(),
                            uAux.getService_code(),
                            uAux.getService_seq(),
                            uAux.getExec_tmp(),
                            uAux.getTask_tmp()
                    ).toSqlQuery()));
                }
                //
                sm_so_service_exec_tasks.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_service_exec_tasks;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        ArrayList<HMAux> sm_so_service_exec_tasks = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                sm_so_service_exec_tasks.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_service_exec_tasks;
    }


    private class CursorSM_SO_Service_Exec_TaskMapper implements Mapper<Cursor, SM_SO_Service_Exec_Task> {
        @Override
        public SM_SO_Service_Exec_Task map(Cursor cursor) {

            SM_SO_Service_Exec_Task sm_so_service_exec_task = new SM_SO_Service_Exec_Task();

            sm_so_service_exec_task.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            sm_so_service_exec_task.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            sm_so_service_exec_task.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            sm_so_service_exec_task.setPrice_list_code(cursor.getInt(cursor.getColumnIndex(PRICE_LIST_CODE)));
            sm_so_service_exec_task.setPack_code(cursor.getInt(cursor.getColumnIndex(PACK_CODE)));
            sm_so_service_exec_task.setPack_seq(cursor.getInt(cursor.getColumnIndex(PACK_SEQ)));
            sm_so_service_exec_task.setCategory_price_code(cursor.getInt(cursor.getColumnIndex(CATEGORY_PRICE_CODE)));
            sm_so_service_exec_task.setService_code(cursor.getInt(cursor.getColumnIndex(SERVICE_CODE)));
            sm_so_service_exec_task.setService_seq(cursor.getInt(cursor.getColumnIndex(SERVICE_SEQ)));
            sm_so_service_exec_task.setExec_code(cursor.getInt(cursor.getColumnIndex(EXEC_CODE)));
            sm_so_service_exec_task.setExec_tmp(cursor.getLong(cursor.getColumnIndex(EXEC_TMP)));
            sm_so_service_exec_task.setTask_code(cursor.getInt(cursor.getColumnIndex(TASK_CODE)));
            sm_so_service_exec_task.setTask_tmp(cursor.getLong(cursor.getColumnIndex(TASK_TMP)));
            sm_so_service_exec_task.setTask_seq_oper(cursor.getInt(cursor.getColumnIndex(TASK_SEQ_OPER)));
            sm_so_service_exec_task.setTask_user(cursor.getInt(cursor.getColumnIndex(TASK_USER)));
            sm_so_service_exec_task.setTask_user_nick(cursor.getString(cursor.getColumnIndex(TASK_USER_NICK)));
            sm_so_service_exec_task.setStart_date(cursor.getString(cursor.getColumnIndex(START_DATE)));

            if (cursor.isNull(cursor.getColumnIndex(END_DATE))) {
                sm_so_service_exec_task.setEnd_date(null);
            } else {
                sm_so_service_exec_task.setEnd_date(cursor.getString(cursor.getColumnIndex(END_DATE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(EXEC_TIME))) {
                sm_so_service_exec_task.setExec_time(null);
            } else {
                sm_so_service_exec_task.setExec_time(cursor.getInt(cursor.getColumnIndex(EXEC_TIME)));
            }

            if (cursor.isNull(cursor.getColumnIndex(EXEC_TIME_FORMAT))) {
                sm_so_service_exec_task.setExec_time_format(null);
            } else {
                sm_so_service_exec_task.setExec_time_format(cursor.getString(cursor.getColumnIndex(EXEC_TIME_FORMAT)));
            }

            sm_so_service_exec_task.setTask_perc(cursor.getInt(cursor.getColumnIndex(TASK_PERC)));
            sm_so_service_exec_task.setQty_people(cursor.getInt(cursor.getColumnIndex(QTY_PEOPLE)));
            sm_so_service_exec_task.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));

            sm_so_service_exec_task.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            sm_so_service_exec_task.setSite_id(cursor.getString(cursor.getColumnIndex(SITE_ID)));
            sm_so_service_exec_task.setSite_desc(cursor.getString(cursor.getColumnIndex(SITE_DESC)));

            sm_so_service_exec_task.setZone_code(cursor.getInt(cursor.getColumnIndex(ZONE_CODE)));
            sm_so_service_exec_task.setZone_id(cursor.getString(cursor.getColumnIndex(ZONE_ID)));
            sm_so_service_exec_task.setZone_desc(cursor.getString(cursor.getColumnIndex(ZONE_DESC)));

            sm_so_service_exec_task.setLocal_code(cursor.getInt(cursor.getColumnIndex(LOCAL_CODE)));
            sm_so_service_exec_task.setLocal_id(cursor.getString(cursor.getColumnIndex(LOCAL_ID)));

            if (cursor.isNull(cursor.getColumnIndex(COMMENTS))) {
                sm_so_service_exec_task.setComments(null);
            } else {
                sm_so_service_exec_task.setComments(cursor.getString(cursor.getColumnIndex(COMMENTS)));
            }

            return sm_so_service_exec_task;
        }
    }

    private class SM_SO_Service_Exec_TaskToContentValuesMapper implements Mapper<SM_SO_Service_Exec_Task, ContentValues> {

        @Override
        public ContentValues map(SM_SO_Service_Exec_Task sm_so_service_exec_task) {

            ContentValues contentValues = new ContentValues();

            if (sm_so_service_exec_task.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, sm_so_service_exec_task.getCustomer_code());
            }

            if (sm_so_service_exec_task.getSo_prefix() > -1) {
                contentValues.put(SO_PREFIX, sm_so_service_exec_task.getSo_prefix());
            }

            if (sm_so_service_exec_task.getSo_code() > -1) {
                contentValues.put(SO_CODE, sm_so_service_exec_task.getSo_code());
            }

            if (sm_so_service_exec_task.getPrice_list_code() > -1) {
                contentValues.put(PRICE_LIST_CODE, sm_so_service_exec_task.getPrice_list_code());
            }

            if (sm_so_service_exec_task.getPack_code() > -1) {
                contentValues.put(PACK_CODE, sm_so_service_exec_task.getPack_code());
            }

            if (sm_so_service_exec_task.getPack_seq() > -1) {
                contentValues.put(PACK_SEQ, sm_so_service_exec_task.getPack_seq());
            }

            if (sm_so_service_exec_task.getCategory_price_code() > -1) {
                contentValues.put(CATEGORY_PRICE_CODE, sm_so_service_exec_task.getCategory_price_code());
            }

            if (sm_so_service_exec_task.getService_code() > -1) {
                contentValues.put(SERVICE_CODE, sm_so_service_exec_task.getService_code());
            }

            if (sm_so_service_exec_task.getService_seq() > -1) {
                contentValues.put(SERVICE_SEQ, sm_so_service_exec_task.getService_seq());
            }

            if (sm_so_service_exec_task.getExec_code() > -1) {
                contentValues.put(EXEC_CODE, sm_so_service_exec_task.getExec_code());
            }

            if (sm_so_service_exec_task.getTask_code() > -1) {
                contentValues.put(TASK_CODE, sm_so_service_exec_task.getTask_code());
            }

            if (sm_so_service_exec_task.getExec_tmp() > -1) {
                contentValues.put(EXEC_TMP, sm_so_service_exec_task.getExec_tmp());
            }

            if (sm_so_service_exec_task.getTask_tmp() > -1) {
                contentValues.put(TASK_TMP, sm_so_service_exec_task.getTask_tmp());
            }

            if (sm_so_service_exec_task.getTask_seq_oper() > -1) {
                contentValues.put(TASK_SEQ_OPER, sm_so_service_exec_task.getTask_seq_oper());
            }

            if (sm_so_service_exec_task.getTask_user() > -1) {
                contentValues.put(TASK_USER, sm_so_service_exec_task.getTask_user());
            }

            if (sm_so_service_exec_task.getTask_user_nick() != null) {
                contentValues.put(TASK_USER_NICK, sm_so_service_exec_task.getTask_user_nick());
            }

            if (sm_so_service_exec_task.getStart_date() != null) {
                contentValues.put(START_DATE, sm_so_service_exec_task.getStart_date());
            }

            if (sm_so_service_exec_task.getEnd_date() != null) {
                contentValues.put(END_DATE, sm_so_service_exec_task.getEnd_date());
            }

            if (sm_so_service_exec_task.getExec_time() != null) {
                contentValues.put(EXEC_TIME, sm_so_service_exec_task.getExec_time());
            }

            if (sm_so_service_exec_task.getExec_time_format() != null) {
                contentValues.put(EXEC_TIME_FORMAT, sm_so_service_exec_task.getExec_time_format());
            }

            if (sm_so_service_exec_task.getTask_perc() > -1) {
                contentValues.put(TASK_PERC, sm_so_service_exec_task.getTask_perc());
            }

            if (sm_so_service_exec_task.getQty_people() > -1) {
                contentValues.put(QTY_PEOPLE, sm_so_service_exec_task.getQty_people());
            }

            if (sm_so_service_exec_task.getStatus() != null) {
                contentValues.put(STATUS, sm_so_service_exec_task.getStatus());
            }

            if (sm_so_service_exec_task.getSite_code() > -1) {
                contentValues.put(SITE_CODE, sm_so_service_exec_task.getSite_code());
            }

            if (sm_so_service_exec_task.getSite_id() != null) {
                contentValues.put(SITE_ID, sm_so_service_exec_task.getSite_id());
            }

            if (sm_so_service_exec_task.getSite_desc() != null) {
                contentValues.put(SITE_DESC, sm_so_service_exec_task.getSite_desc());
            }

            if (sm_so_service_exec_task.getZone_code() > -1) {
                contentValues.put(ZONE_CODE, sm_so_service_exec_task.getZone_code());
            }

            if (sm_so_service_exec_task.getSite_id() != null) {
                contentValues.put(ZONE_ID, sm_so_service_exec_task.getZone_id());
            }

            if (sm_so_service_exec_task.getZone_desc() != null) {
                contentValues.put(ZONE_DESC, sm_so_service_exec_task.getZone_desc());
            }

            if (sm_so_service_exec_task.getLocal_code() > -1) {
                contentValues.put(LOCAL_CODE, sm_so_service_exec_task.getLocal_code());
            }

            if (sm_so_service_exec_task.getLocal_id() != null) {
                contentValues.put(LOCAL_ID, sm_so_service_exec_task.getLocal_id());
            }

            if (sm_so_service_exec_task.getComments() != null) {
                contentValues.put(COMMENTS, sm_so_service_exec_task.getComments());
            }

            return contentValues;
        }
    }

}
