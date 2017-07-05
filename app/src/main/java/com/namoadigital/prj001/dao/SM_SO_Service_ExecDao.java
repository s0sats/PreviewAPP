package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Service_Exec;
import com.namoadigital.prj001.util.Constant;

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
    public static final String CATEGORY_PRICE_CODE = "category_price_code";
    public static final String SERVICE_CODE = "service_code";
    public static final String EXEC_CODE = "exec_code";
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
    public void addUpdate(SM_SO_Service_Exec item) {

    }

    @Override
    public void addUpdate(Iterable<SM_SO_Service_Exec> items, boolean status) {

    }

    @Override
    public void addUpdate(String sQuery) {

    }

    @Override
    public void remove(String sQuery) {

    }

    @Override
    public SM_SO_Service_Exec getByString(String sQuery) {
        return null;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        return null;
    }

    @Override
    public List<SM_SO_Service_Exec> query(String sQuery) {
        return null;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        return null;
    }


    private class CursorSM_SO_Service_ExecMapper implements Mapper<Cursor, SM_SO_Service_Exec> {
        @Override
        public SM_SO_Service_Exec map(Cursor cursor) {
            return null;
        }
    }

    private class SM_SO_Service_ExecToContentValuesMapper implements Mapper<SM_SO_Service_Exec, ContentValues> {

        @Override
        public ContentValues map(SM_SO_Service_Exec sm_so_service_exec) {
            return null;
        }
    }
}
