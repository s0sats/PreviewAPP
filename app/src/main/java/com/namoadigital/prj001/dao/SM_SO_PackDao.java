package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Pack;
import com.namoadigital.prj001.util.Constant;

import java.util.List;

/**
 * Created by neomatrix on 05/07/17.
 */

public class SM_SO_PackDao extends BaseDao implements Dao<SM_SO_Pack> {

    private final Mapper<SM_SO_Pack, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SM_SO_Pack> toSM_SO_PackMapper;

    public static final String TABLE = "sm_so_packs";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String PRICE_LIST_CODE = "price_list_code";
    public static final String PRICE_LIST_ID = "price_list_id";
    public static final String PRICE_LIST_DESC = "price_list_desc";
    public static final String PACK_CODE = "pack_code";
    public static final String PACK_SEQ = "pack_seq";
    public static final String PACK_ID = "pack_id";
    public static final String PACK_DESC = "pack_desc";
    public static final String STATUS = "status";
    public static final String RULE = "rule";
    public static final String BILLING_TYPE = "billing_type";
    public static final String EXPRESS = "express";
    public static final String SELECTION_TYPE = "selection_type";

    public SM_SO_PackDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SM_SO_PackToContentValuesMapper();
        this.toSM_SO_PackMapper = new CursorSM_SO_PackMapper();
    }

    @Override
    public void addUpdate(SM_SO_Pack item) {

    }

    @Override
    public void addUpdate(Iterable<SM_SO_Pack> items, boolean status) {

    }

    @Override
    public void addUpdate(String sQuery) {

    }

    @Override
    public void remove(String sQuery) {

    }

    @Override
    public SM_SO_Pack getByString(String sQuery) {
        return null;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        return null;
    }

    @Override
    public List<SM_SO_Pack> query(String sQuery) {
        return null;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        return null;
    }


    private class CursorSM_SO_PackMapper implements Mapper<Cursor, SM_SO_Pack> {
        @Override
        public SM_SO_Pack map(Cursor cursor) {
            return null;
        }
    }

    private class SM_SO_PackToContentValuesMapper implements Mapper<SM_SO_Pack, ContentValues> {

        @Override
        public ContentValues map(SM_SO_Pack sm_so_pack) {
            return null;
        }
    }
}
