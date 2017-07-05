package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.util.Constant;

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
    public void addUpdate(SM_SO_Service item) {

    }

    @Override
    public void addUpdate(Iterable<SM_SO_Service> items, boolean status) {

    }

    @Override
    public void addUpdate(String sQuery) {

    }

    @Override
    public void remove(String sQuery) {

    }

    @Override
    public SM_SO_Service getByString(String sQuery) {
        return null;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        return null;
    }

    @Override
    public List<SM_SO_Service> query(String sQuery) {
        return null;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        return null;
    }


    private class CursorSM_SO_ServiceMapper implements Mapper<Cursor, SM_SO_Service> {
        @Override
        public SM_SO_Service map(Cursor cursor) {
            return null;
        }
    }

    private class SM_SO_ServiceToContentValuesMapper implements Mapper<SM_SO_Service, ContentValues> {

        @Override
        public ContentValues map(SM_SO_Service sm_so_service) {
            return null;
        }
    }
}
