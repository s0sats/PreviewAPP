package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.model.SM_SO_Service_Exec_Task;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 05/07/17.
 */

public class SM_SO_Service_ExecDao extends BaseDao implements Dao<SM_SO_Service_Exec> {

    private final Mapper<SM_SO_Service_Exec, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SM_SO_Service_Exec> toSM_SO_Service_ExecMapper;

    public static final String TABLE = "sm_so_service_execs";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String PRICE_LIST_CODE = "price_list_code";
    public static final String PACK_CODE = "pack_code";
    public static final String PACK_SEQ = "pack_seq";
    public static final String CATEGORY_PRICE_CODE = "category_price_code";
    public static final String SERVICE_CODE = "service_code";
    public static final String SERVICE_SEQ = "service_seq";
    public static final String EXEC_CODE = "exec_code";
    public static final String EXEC_TMP = "exec_tmp";
    public static final String STATUS = "status";
    public static final String PARTNER_CODE = "partner_code";
    public static final String PARTNER_ID = "partner_id";
    public static final String PARTNER_DESC = "partner_desc";

    public SM_SO_Service_ExecDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SM_SO_Service_ExecToContentValuesMapper();
        this.toSM_SO_Service_ExecMapper = new CursorSM_SO_Service_ExecMapper();
    }

    @Override
    public void addUpdate(SM_SO_Service_Exec sm_so_service_exec) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service_exec)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service_exec.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getPrice_list_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getPack_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec.getPack_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getCategory_price_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getService_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec.getService_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(EXEC_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getExec_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(sm_so_service_exec), sbWhere.toString(), null);
            }

            SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            sm_so_service_exec_taskDao.addUpdate(sm_so_service_exec.getTask(), false);

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SM_SO_Service_Exec> sm_so_service_execs, boolean status) {

        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            for (SM_SO_Service_Exec sm_so_service_exec : sm_so_service_execs) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service_exec)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service_exec.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getSo_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getPrice_list_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getPack_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec.getPack_seq())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getCategory_price_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getService_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service_exec.getService_seq())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(EXEC_CODE).append(" = '").append(String.valueOf(sm_so_service_exec.getExec_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(sm_so_service_exec), sbWhere.toString(), null);
                }

//                SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
//                        context,
//                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                        Constant.DB_VERSION_CUSTOM
//                );

                sm_so_service_exec_taskDao.addUpdate(sm_so_service_exec.getTask(), false);
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
    public SM_SO_Service_Exec getByString(String sQuery) {
        SM_SO_Service_Exec sm_so_service_exec = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_service_exec = toSM_SO_Service_ExecMapper.map(cursor);

                if (sm_so_service_exec != null) {
                    SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    sm_so_service_exec.setTask((ArrayList<SM_SO_Service_Exec_Task>) sm_so_service_exec_taskDao.query(new SM_SO_Service_Exec_Task_Sql_002(
                            sm_so_service_exec.getCustomer_code(),
                            sm_so_service_exec.getSo_prefix(),
                            sm_so_service_exec.getSo_code(),
                            sm_so_service_exec.getPrice_list_code(),
                            sm_so_service_exec.getPack_code(),
                            sm_so_service_exec.getPack_seq(),
                            sm_so_service_exec.getCategory_price_code(),
                            sm_so_service_exec.getService_code(),
                            sm_so_service_exec.getService_seq(),
                            sm_so_service_exec.getExec_code()
                    ).toSqlQuery()));
                }

            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_service_exec;
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
    public List<SM_SO_Service_Exec> query(String sQuery) {
        List<SM_SO_Service_Exec> sm_so_service_execs = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                SM_SO_Service_Exec uAux = toSM_SO_Service_ExecMapper.map(cursor);
                //
                if (uAux != null) {
                    SM_SO_Service_Exec_TaskDao sm_so_service_exec_taskDao = new SM_SO_Service_Exec_TaskDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    uAux.setTask((ArrayList<SM_SO_Service_Exec_Task>) sm_so_service_exec_taskDao.query(new SM_SO_Service_Exec_Task_Sql_002(
                            uAux.getCustomer_code(),
                            uAux.getSo_prefix(),
                            uAux.getSo_code(),
                            uAux.getPrice_list_code(),
                            uAux.getPack_code(),
                            uAux.getPack_seq(),
                            uAux.getCategory_price_code(),
                            uAux.getService_code(),
                            uAux.getService_seq(),
                            uAux.getExec_code()
                    ).toSqlQuery()));

                    Log.d("TASKS", String.valueOf(uAux.getTask().size()));
                }
                //
                sm_so_service_execs.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_service_execs;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        ArrayList<HMAux> sm_so_service_execs = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                sm_so_service_execs.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_service_execs;
    }


    private class CursorSM_SO_Service_ExecMapper implements Mapper<Cursor, SM_SO_Service_Exec> {
        @Override
        public SM_SO_Service_Exec map(Cursor cursor) {

            SM_SO_Service_Exec sm_so_service_exec = new SM_SO_Service_Exec();

            sm_so_service_exec.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            sm_so_service_exec.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            sm_so_service_exec.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            sm_so_service_exec.setPrice_list_code(cursor.getInt(cursor.getColumnIndex(PRICE_LIST_CODE)));
            sm_so_service_exec.setPack_code(cursor.getInt(cursor.getColumnIndex(PACK_CODE)));
            sm_so_service_exec.setPack_seq(cursor.getInt(cursor.getColumnIndex(PACK_SEQ)));
            sm_so_service_exec.setCategory_price_code(cursor.getInt(cursor.getColumnIndex(CATEGORY_PRICE_CODE)));
            sm_so_service_exec.setService_code(cursor.getInt(cursor.getColumnIndex(SERVICE_CODE)));
            sm_so_service_exec.setService_seq(cursor.getInt(cursor.getColumnIndex(SERVICE_SEQ)));
            sm_so_service_exec.setExec_code(cursor.getInt(cursor.getColumnIndex(EXEC_CODE)));
            sm_so_service_exec.setExec_tmp(cursor.getInt(cursor.getColumnIndex(EXEC_TMP)));
            sm_so_service_exec.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));

            if (cursor.isNull(cursor.getColumnIndex(PARTNER_CODE))) {
                sm_so_service_exec.setPartner_code(null);
            } else {
                sm_so_service_exec.setPartner_code(cursor.getInt(cursor.getColumnIndex(PARTNER_CODE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(PARTNER_ID))) {
                sm_so_service_exec.setPartner_id(null);
            } else {
                sm_so_service_exec.setPartner_id(cursor.getString(cursor.getColumnIndex(PARTNER_ID)));
            }

            if (cursor.isNull(cursor.getColumnIndex(PARTNER_DESC))) {
                sm_so_service_exec.setPartner_desc(null);
            } else {
                sm_so_service_exec.setPartner_desc(cursor.getString(cursor.getColumnIndex(PARTNER_DESC)));
            }

            return sm_so_service_exec;
        }
    }

    private class SM_SO_Service_ExecToContentValuesMapper implements Mapper<SM_SO_Service_Exec, ContentValues> {

        @Override
        public ContentValues map(SM_SO_Service_Exec sm_so_service_exec) {

            ContentValues contentValues = new ContentValues();

            if (sm_so_service_exec.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, sm_so_service_exec.getCustomer_code());
            }

            if (sm_so_service_exec.getSo_prefix() > -1) {
                contentValues.put(SO_PREFIX, sm_so_service_exec.getSo_prefix());
            }

            if (sm_so_service_exec.getSo_code() > -1) {
                contentValues.put(SO_CODE, sm_so_service_exec.getSo_code());
            }

            if (sm_so_service_exec.getPrice_list_code() > -1) {
                contentValues.put(PRICE_LIST_CODE, sm_so_service_exec.getPrice_list_code());
            }

            if (sm_so_service_exec.getPack_code() > -1) {
                contentValues.put(PACK_CODE, sm_so_service_exec.getPack_code());
            }

            if (sm_so_service_exec.getPack_seq() > -1) {
                contentValues.put(PACK_SEQ, sm_so_service_exec.getPack_seq());
            }

            if (sm_so_service_exec.getCategory_price_code() > -1) {
                contentValues.put(CATEGORY_PRICE_CODE, sm_so_service_exec.getCategory_price_code());
            }

            if (sm_so_service_exec.getService_code() > -1) {
                contentValues.put(SERVICE_CODE, sm_so_service_exec.getService_code());
            }

            if (sm_so_service_exec.getService_seq() > -1) {
                contentValues.put(SERVICE_SEQ, sm_so_service_exec.getService_seq());
            }

            if (sm_so_service_exec.getExec_code() > -1) {
                contentValues.put(EXEC_CODE, sm_so_service_exec.getExec_code());
            }

            if (sm_so_service_exec.getExec_tmp() > -1) {
                contentValues.put(EXEC_TMP, sm_so_service_exec.getExec_tmp());
            }

            if (sm_so_service_exec.getStatus() !=  null) {
                contentValues.put(STATUS, sm_so_service_exec.getStatus());
            }

            if (sm_so_service_exec.getPartner_code() !=  null) {
                contentValues.put(PARTNER_CODE, sm_so_service_exec.getPartner_code());
            }

            if (sm_so_service_exec.getPartner_id() != null) {
                contentValues.put(PARTNER_ID, sm_so_service_exec.getPartner_id());
            }

            if (sm_so_service_exec.getPartner_desc() != null) {
                contentValues.put(PARTNER_DESC, sm_so_service_exec.getPartner_desc());
            }

            return contentValues;
        }
    }
}
