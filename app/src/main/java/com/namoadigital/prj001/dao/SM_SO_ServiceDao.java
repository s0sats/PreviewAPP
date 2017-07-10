package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 05/07/17.
 */

public class SM_SO_ServiceDao extends BaseDao implements Dao<SM_SO_Service> {

    private final Mapper<SM_SO_Service, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SM_SO_Service> toSM_SO_ServiceMapper;

    public static final String TABLE = "sm_so_services";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String PRICE_LIST_CODE = "price_list_code";
    public static final String PACK_CODE = "pack_code";
    public static final String PACK_SEQ = "pack_seq";
    public static final String CATEGORY_PRICE_CODE = "category_price_code";
    public static final String SERVICE_CODE = "service_code";
    public static final String SERVICE_SEQ = "service_seq";
    public static final String SERVICE_ID = "service_id";
    public static final String SERVICE_DESC = "service_desc";
    public static final String SERVICE_OPER_ID = "service_oper_id";
    public static final String STATUS = "status";
    public static final String QTY = "qty";
    public static final String OPTIONAL = "optional";
    public static final String MANUAL_PRICE = "manual_price";
    public static final String EXPRESS = "express";
    public static final String EXEC_TIME_STANDARD = "exec_time_standard";
    public static final String PRICE = "price";
    public static final String COST = "cost";
    public static final String EXEC_TYPE = "exec_type";
    public static final String EXEC_SEQ_OPER = "exec_seq_oper";
    public static final String APPROVAL_BUDGET_USER = "approval_budget_user";
    public static final String APPROVAL_BUDGET_DATE = "approval_budget_date";
    public static final String PARTNER_CODE = "partner_code";
    public static final String PARTNER_ID = "partner_id";
    public static final String PARTNER_DESC = "partner_desc";
    public static final String REQUIRE_APPROVAL = "require_approval";


    public SM_SO_ServiceDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SM_SO_ServiceToContentValuesMapper();
        this.toSM_SO_ServiceMapper = new CursorSM_SO_ServiceMapper();
    }

    @Override
    public void addUpdate(SM_SO_Service sm_so_service) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service.getPrice_list_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service.getPack_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service.getPack_seq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service.getCategory_price_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service.getService_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service.getService_seq())).append("'");

                db.update(TABLE, toContentValuesMapper.map(sm_so_service), sbWhere.toString(), null);
            }

            SM_SO_Service_ExecDao sm_so_service_execDao = new SM_SO_Service_ExecDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            sm_so_service_execDao.addUpdate(sm_so_service.getExec(), false);

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SM_SO_Service> sm_so_services, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            SM_SO_Service_ExecDao sm_so_service_execDao = new SM_SO_Service_ExecDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            for (SM_SO_Service sm_so_service : sm_so_services) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_service)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_service.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_service.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_service.getSo_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_service.getPrice_list_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_service.getPack_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_service.getPack_seq())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CATEGORY_PRICE_CODE).append(" = '").append(String.valueOf(sm_so_service.getCategory_price_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERVICE_CODE).append(" = '").append(String.valueOf(sm_so_service.getService_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERVICE_SEQ).append(" = '").append(String.valueOf(sm_so_service.getService_seq())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(sm_so_service), sbWhere.toString(), null);
                }

//                SM_SO_Service_ExecDao sm_so_service_execDao = new SM_SO_Service_ExecDao(
//                        context,
//                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                        Constant.DB_VERSION_CUSTOM
//                );

                sm_so_service_execDao.addUpdate(sm_so_service.getExec(), false);

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
    public SM_SO_Service getByString(String sQuery) {
        SM_SO_Service sm_so_service = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_service = toSM_SO_ServiceMapper.map(cursor);

                if (sm_so_service != null) {
                    SM_SO_Service_ExecDao sm_so_service_execDao = new SM_SO_Service_ExecDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    sm_so_service.setExec((ArrayList<SM_SO_Service_Exec>) sm_so_service_execDao.query(new SM_SO_Service_Exec_Sql_002(
                            sm_so_service.getCustomer_code(),
                            sm_so_service.getSo_prefix(),
                            sm_so_service.getSo_code(),
                            sm_so_service.getPrice_list_code(),
                            sm_so_service.getPack_code(),
                            sm_so_service.getPack_seq(),
                            sm_so_service.getCategory_price_code(),
                            sm_so_service.getService_code(),
                            sm_so_service.getService_seq()
                    ).toSqlQuery()));
                }
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();


        return sm_so_service;
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
    public List<SM_SO_Service> query(String sQuery) {
        List<SM_SO_Service> sm_so_services = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                SM_SO_Service uAux = toSM_SO_ServiceMapper.map(cursor);
                //
                if (uAux != null) {
                    SM_SO_Service_ExecDao sm_so_service_execDao = new SM_SO_Service_ExecDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    uAux.setExec((ArrayList<SM_SO_Service_Exec>) sm_so_service_execDao.query(new SM_SO_Service_Exec_Sql_002(
                            uAux.getCustomer_code(),
                            uAux.getSo_prefix(),
                            uAux.getSo_code(),
                            uAux.getPrice_list_code(),
                            uAux.getPack_code(),
                            uAux.getPack_seq(),
                            uAux.getCategory_price_code(),
                            uAux.getService_code(),
                            uAux.getService_seq()
                    ).toSqlQuery()));
                }
                //
                sm_so_services.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_services;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        ArrayList<HMAux> sm_so_services = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                sm_so_services.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_services;
    }

    private class CursorSM_SO_ServiceMapper implements Mapper<Cursor, SM_SO_Service> {
        @Override
        public SM_SO_Service map(Cursor cursor) {

            SM_SO_Service sm_so_service = new SM_SO_Service();

            sm_so_service.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            sm_so_service.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            sm_so_service.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            sm_so_service.setPrice_list_code(cursor.getInt(cursor.getColumnIndex(PRICE_LIST_CODE)));
            sm_so_service.setPack_code(cursor.getInt(cursor.getColumnIndex(PACK_CODE)));
            sm_so_service.setPack_seq(cursor.getInt(cursor.getColumnIndex(PACK_SEQ)));
            sm_so_service.setCategory_price_code(cursor.getInt(cursor.getColumnIndex(CATEGORY_PRICE_CODE)));
            sm_so_service.setService_code(cursor.getInt(cursor.getColumnIndex(SERVICE_CODE)));
            sm_so_service.setService_seq(cursor.getInt(cursor.getColumnIndex(SERVICE_SEQ)));

            sm_so_service.setService_id(cursor.getString(cursor.getColumnIndex(SERVICE_ID)));
            sm_so_service.setService_desc(cursor.getString(cursor.getColumnIndex(SERVICE_DESC)));

            if (cursor.isNull(cursor.getColumnIndex(SERVICE_OPER_ID))) {
                sm_so_service.setService_oper_id(null);
            } else {
                sm_so_service.setService_oper_id(cursor.getString(cursor.getColumnIndex(SERVICE_OPER_ID)));
            }

            sm_so_service.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));

            sm_so_service.setQty(cursor.getInt(cursor.getColumnIndex(QTY)));
            sm_so_service.setOptional(cursor.getInt(cursor.getColumnIndex(OPTIONAL)));
            sm_so_service.setManual_price(cursor.getInt(cursor.getColumnIndex(MANUAL_PRICE)));
            sm_so_service.setExpress(cursor.getInt(cursor.getColumnIndex(EXPRESS)));
            sm_so_service.setExec_time_standard(cursor.getInt(cursor.getColumnIndex(EXEC_TIME_STANDARD)));

            if (cursor.isNull(cursor.getColumnIndex(PRICE))) {
                sm_so_service.setPrice(null);
            } else {
                sm_so_service.setPrice(cursor.getDouble(cursor.getColumnIndex(PRICE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(COST))) {
                sm_so_service.setCost(null);
            } else {
                sm_so_service.setCost(cursor.getDouble(cursor.getColumnIndex(COST)));
            }

            sm_so_service.setExec_type(cursor.getString(cursor.getColumnIndex(EXEC_TYPE)));
            sm_so_service.setExec_seq_oper(cursor.getInt(cursor.getColumnIndex(EXEC_SEQ_OPER)));

            if (cursor.isNull(cursor.getColumnIndex(APPROVAL_BUDGET_USER))) {
                sm_so_service.setApproval_budget_user(null);
            } else {
                sm_so_service.setApproval_budget_user(cursor.getInt(cursor.getColumnIndex(APPROVAL_BUDGET_USER)));
            }

            if (cursor.isNull(cursor.getColumnIndex(APPROVAL_BUDGET_DATE))) {
                sm_so_service.setApproval_budget_date(null);
            } else {
                sm_so_service.setApproval_budget_date(cursor.getString(cursor.getColumnIndex(APPROVAL_BUDGET_DATE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(PARTNER_CODE))) {
                sm_so_service.setPartner_code(null);
            } else {
                sm_so_service.setPack_code(cursor.getInt(cursor.getColumnIndex(PARTNER_CODE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(PARTNER_ID))) {
                sm_so_service.setPartner_id(null);
            } else {
                sm_so_service.setPartner_id(cursor.getString(cursor.getColumnIndex(PARTNER_ID)));
            }

            if (cursor.isNull(cursor.getColumnIndex(PARTNER_DESC))) {
                sm_so_service.setPartner_desc(null);
            } else {
                sm_so_service.setPartner_desc(cursor.getString(cursor.getColumnIndex(PARTNER_DESC)));
            }

            sm_so_service.setRequire_approval(cursor.getString(cursor.getColumnIndex(REQUIRE_APPROVAL)));

            return sm_so_service;
        }
    }

    private class SM_SO_ServiceToContentValuesMapper implements Mapper<SM_SO_Service, ContentValues> {

        @Override
        public ContentValues map(SM_SO_Service sm_so_service) {

            ContentValues contentValues = new ContentValues();

            if (sm_so_service.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, sm_so_service.getCustomer_code());
            }

            if (sm_so_service.getSo_prefix() > -1) {
                contentValues.put(SO_PREFIX, sm_so_service.getSo_prefix());
            }

            if (sm_so_service.getSo_code() > -1) {
                contentValues.put(SO_CODE, sm_so_service.getSo_code());
            }

            if (sm_so_service.getPrice_list_code() > -1) {
                contentValues.put(PRICE_LIST_CODE, sm_so_service.getPrice_list_code());
            }

            if (sm_so_service.getPack_code() > -1) {
                contentValues.put(PACK_CODE, sm_so_service.getPack_code());
            }

            if (sm_so_service.getPack_seq() > -1) {
                contentValues.put(PACK_SEQ, sm_so_service.getPack_seq());
            }

            if (sm_so_service.getCategory_price_code() > -1) {
                contentValues.put(CATEGORY_PRICE_CODE, sm_so_service.getCategory_price_code());
            }

            if (sm_so_service.getService_code() > -1) {
                contentValues.put(SERVICE_CODE, sm_so_service.getService_code());
            }

            if (sm_so_service.getService_seq() > -1) {
                contentValues.put(SERVICE_SEQ, sm_so_service.getService_seq());
            }

            if (sm_so_service.getService_id() != null) {
                contentValues.put(SERVICE_ID, sm_so_service.getService_id());
            }

            if (sm_so_service.getService_desc() != null) {
                contentValues.put(SERVICE_DESC, sm_so_service.getService_desc());
            }

            if (sm_so_service.getService_oper_id() != null) {
                contentValues.put(SERVICE_OPER_ID, sm_so_service.getService_oper_id());
            }

            if (sm_so_service.getStatus() != null) {
                contentValues.put(STATUS, sm_so_service.getStatus());
            }

            if (sm_so_service.getQty() > -1) {
                contentValues.put(QTY, sm_so_service.getQty());
            }

            if (sm_so_service.getOptional() > -1) {
                contentValues.put(OPTIONAL, sm_so_service.getOptional());
            }

            if (sm_so_service.getManual_price() > -1) {
                contentValues.put(MANUAL_PRICE, sm_so_service.getManual_price());
            }

            if (sm_so_service.getExpress() > -1) {
                contentValues.put(EXPRESS, sm_so_service.getExpress());
            }

            if (sm_so_service.getExec_time_standard() > -1) {
                contentValues.put(EXEC_TIME_STANDARD, sm_so_service.getExec_time_standard());
            }

            if (sm_so_service.getPrice() != null) {
                contentValues.put(PRICE, sm_so_service.getPrice());
            }

            if (sm_so_service.getCost() != null) {
                contentValues.put(COST, sm_so_service.getCost());
            }

            if (sm_so_service.getExec_type() != null) {
                contentValues.put(EXEC_TYPE, sm_so_service.getExec_type());
            }

            if (sm_so_service.getExec_seq_oper() > -1) {
                contentValues.put(EXEC_SEQ_OPER, sm_so_service.getExec_seq_oper());
            }

            if (sm_so_service.getApproval_budget_user() != null) {
                contentValues.put(APPROVAL_BUDGET_USER, sm_so_service.getApproval_budget_user());
            }

            if (sm_so_service.getApproval_budget_date() != null) {
                contentValues.put(APPROVAL_BUDGET_DATE, sm_so_service.getApproval_budget_date());
            }

            if (sm_so_service.getPartner_code() != null) {
                contentValues.put(PACK_CODE, sm_so_service.getPartner_code());
            }

            if (sm_so_service.getPartner_id() != null) {
                contentValues.put(PARTNER_ID, sm_so_service.getPartner_id());
            }

            if (sm_so_service.getPartner_desc() != null) {
                contentValues.put(PARTNER_DESC, sm_so_service.getPartner_desc());
            }

            if (sm_so_service.getRequire_approval() != null) {
                contentValues.put(REQUIRE_APPROVAL, sm_so_service.getRequire_approval());
            }

            return contentValues;
        }
    }
}
