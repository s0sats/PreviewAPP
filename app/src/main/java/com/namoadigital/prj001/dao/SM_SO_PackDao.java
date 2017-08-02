package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Pack;
import com.namoadigital.prj001.model.SM_SO_Service;
import com.namoadigital.prj001.sql.SM_SO_Service_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
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
    public void addUpdate(SM_SO_Pack sm_so_pack) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_pack)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_pack.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_pack.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_pack.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_pack.getPrice_list_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_pack.getPack_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_pack.getPack_seq())).append("'");

                db.update(TABLE, toContentValuesMapper.map(sm_so_pack), sbWhere.toString(), null);

                SM_SO_ServiceDao sm_so_serviceDao = new SM_SO_ServiceDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );

                sm_so_serviceDao.addUpdate(sm_so_pack.getService(), false);
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SM_SO_Pack> sm_so_packs, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            SM_SO_ServiceDao sm_so_serviceDao = new SM_SO_ServiceDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            for (SM_SO_Pack sm_so_pack : sm_so_packs) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_pack)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_pack.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_pack.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_pack.getSo_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRICE_LIST_CODE).append(" = '").append(String.valueOf(sm_so_pack.getPrice_list_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PACK_CODE).append(" = '").append(String.valueOf(sm_so_pack.getPack_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PACK_SEQ).append(" = '").append(String.valueOf(sm_so_pack.getPack_seq())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(sm_so_pack), sbWhere.toString(), null);
                }

//                SM_SO_ServiceDao sm_so_serviceDao = new SM_SO_ServiceDao(
//                        context,
//                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                        Constant.DB_VERSION_CUSTOM
//                );

                sm_so_serviceDao.addUpdate(sm_so_pack.getService(), false);
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
    public SM_SO_Pack getByString(String sQuery) {
        SM_SO_Pack sm_so_pack = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_pack = toSM_SO_PackMapper.map(cursor);

                if (sm_so_pack != null) {
                    SM_SO_ServiceDao sm_so_serviceDao = new SM_SO_ServiceDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    sm_so_pack.setService((ArrayList<SM_SO_Service>) sm_so_serviceDao.query(new SM_SO_Service_Sql_002(
                            sm_so_pack.getCustomer_code(),
                            sm_so_pack.getSo_prefix(),
                            sm_so_pack.getSo_code(),
                            sm_so_pack.getPrice_list_code(),
                            sm_so_pack.getPack_code(),
                            sm_so_pack.getPack_seq()
                    ).toSqlQuery()));
                }
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();


        return sm_so_pack;
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
    public List<SM_SO_Pack> query(String sQuery) {
        List<SM_SO_Pack> sm_so_packs = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                SM_SO_Pack uAux = toSM_SO_PackMapper.map(cursor);
                //
                if (uAux != null) {
                    SM_SO_ServiceDao sm_so_serviceDao = new SM_SO_ServiceDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    uAux.setService((ArrayList<SM_SO_Service>) sm_so_serviceDao.query(new SM_SO_Service_Sql_002(
                            uAux.getCustomer_code(),
                            uAux.getSo_prefix(),
                            uAux.getSo_code(),
                            uAux.getPrice_list_code(),
                            uAux.getPack_code(),
                            uAux.getPack_seq()
                    ).toSqlQuery()));
                }
                //
                sm_so_packs.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_packs;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        ArrayList<HMAux> sm_so_packs = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                sm_so_packs.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_packs;
    }


    private class CursorSM_SO_PackMapper implements Mapper<Cursor, SM_SO_Pack> {
        @Override
        public SM_SO_Pack map(Cursor cursor) {

            SM_SO_Pack sm_so_pack = new SM_SO_Pack();

            sm_so_pack.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            sm_so_pack.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            sm_so_pack.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            sm_so_pack.setPrice_list_code(cursor.getInt(cursor.getColumnIndex(PRICE_LIST_CODE)));
            sm_so_pack.setPrice_list_id(cursor.getString(cursor.getColumnIndex(PRICE_LIST_ID)));
            sm_so_pack.setPrice_list_desc(cursor.getString(cursor.getColumnIndex(PRICE_LIST_DESC)));
            sm_so_pack.setPack_code(cursor.getInt(cursor.getColumnIndex(PACK_CODE)));
            sm_so_pack.setPack_seq(cursor.getInt(cursor.getColumnIndex(PACK_SEQ)));
            sm_so_pack.setPack_id(cursor.getString(cursor.getColumnIndex(PACK_ID)));
            sm_so_pack.setPack_desc(cursor.getString(cursor.getColumnIndex(PACK_DESC)));
            sm_so_pack.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));

            if (cursor.isNull(cursor.getColumnIndex(RULE))) {
                sm_so_pack.setRule(null);
            } else {
                sm_so_pack.setRule(cursor.getString(cursor.getColumnIndex(RULE)));
            }

            sm_so_pack.setBilling_type(cursor.getString(cursor.getColumnIndex(BILLING_TYPE)));
            sm_so_pack.setExpress(cursor.getInt(cursor.getColumnIndex(EXPRESS)));
            sm_so_pack.setSelection_type(cursor.getString(cursor.getColumnIndex(SELECTION_TYPE)));

            return sm_so_pack;
        }
    }

    private class SM_SO_PackToContentValuesMapper implements Mapper<SM_SO_Pack, ContentValues> {

        @Override
        public ContentValues map(SM_SO_Pack sm_so_pack) {

            ContentValues contentValues = new ContentValues();

            if (sm_so_pack.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, sm_so_pack.getCustomer_code());
            }

            if (sm_so_pack.getSo_prefix() > -1) {
                contentValues.put(SO_PREFIX, sm_so_pack.getSo_prefix());
            }

            if (sm_so_pack.getSo_code() > -1) {
                contentValues.put(SO_CODE, sm_so_pack.getSo_code());
            }

            if (sm_so_pack.getPrice_list_code() > -1) {
                contentValues.put(PRICE_LIST_CODE, sm_so_pack.getPrice_list_code());
            }

            if (sm_so_pack.getPrice_list_id() != null) {
                contentValues.put(PRICE_LIST_ID, sm_so_pack.getPrice_list_id());
            }

            if (sm_so_pack.getPrice_list_desc() != null) {
                contentValues.put(PRICE_LIST_DESC, sm_so_pack.getPrice_list_desc());
            }

            if (sm_so_pack.getPack_code() > -1) {
                contentValues.put(PACK_CODE, sm_so_pack.getPack_code());
            }

            if (sm_so_pack.getPack_seq() > -1) {
                contentValues.put(PACK_SEQ, sm_so_pack.getPack_seq());
            }

            if (sm_so_pack.getPack_id() != null) {
                contentValues.put(PACK_ID, sm_so_pack.getPack_id());
            }

            if (sm_so_pack.getPack_desc() != null) {
                contentValues.put(PACK_DESC, sm_so_pack.getPack_desc());
            }

            if (sm_so_pack.getStatus() != null) {
                contentValues.put(STATUS, sm_so_pack.getStatus());
            }

            if (sm_so_pack.getRule() != null) {
                contentValues.put(RULE, sm_so_pack.getRule());
            }

            if (sm_so_pack.getBilling_type() != null) {
                contentValues.put(BILLING_TYPE, sm_so_pack.getBilling_type());
            }

            if (sm_so_pack.getExpress() > -1) {
                contentValues.put(EXPRESS, sm_so_pack.getExpress());
            }

            if (sm_so_pack.getSelection_type() != null) {
                contentValues.put(SELECTION_TYPE, sm_so_pack.getSelection_type());
            }

            return contentValues;
        }
    }
}
