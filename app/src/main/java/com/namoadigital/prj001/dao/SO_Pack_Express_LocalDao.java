package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SO_Pack_Express_Local;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 3/22/18.
 */

public class SO_Pack_Express_LocalDao extends BaseDao implements Dao<SO_Pack_Express_Local> {

    private final Mapper<SO_Pack_Express_Local, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SO_Pack_Express_Local> toSO_Pack_Express_LocalMapper;

    public static final String TABLE = "so_pack_expresss_local";

    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SITE_CODE = "site_code";
    public static final String OPERATION_CODE = "operation_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String EXPRESS_CODE = "express_code";

    public static final String EXPRESS_TMP = "express_tmp";
    public static final String SERIAL_ID = "serial_id";
    public static final String PARTNER_CODE = "partner_code";

    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String SO_ID = "so_id";
    public static final String SO_DESC = "so_desc";
    public static final String SO_STATUS = "so_status";
    public static final String CONTRACT_CODE = "contract_code";
    public static final String CONTRACT_DESC = "contract_desc";
    public static final String PRIORITY_CODE = "priority_code";
    public static final String PRIORITY_DESC = "priority_desc";
    public static final String SITE_ID = "site_id";
    public static final String SITE_DESC = "site_desc";
    public static final String OPERATION_ID = "operation_id";
    public static final String OPERATION_DESC = "operation_desc";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DESC = "product_desc";
    public static final String SERIAL_CODE = "serial_code";
    public static final String SEGMENT_CODE = "segment_code";
    public static final String SEGMENT_ID = "segment_id";
    public static final String SEGMENT_DESC = "segment_desc";
    public static final String RET_CODE = "ret_code";
    public static final String RET_MSG = "ret_msg";
    public static final String STATUS = "status";
    public static final String LOG_DATE = "log_date";
    public static final String TOKEN = "token";

    public static String[] columns = {CUSTOMER_CODE, SITE_CODE, OPERATION_CODE, PRODUCT_CODE, EXPRESS_CODE, EXPRESS_TMP, SERIAL_ID, PARTNER_CODE, SO_PREFIX, SO_CODE, SO_ID, SO_DESC, SO_STATUS, CONTRACT_CODE, CONTRACT_DESC, PRIORITY_CODE, PRIORITY_DESC, SITE_ID, SITE_DESC, OPERATION_ID, OPERATION_DESC, PRODUCT_ID, PRODUCT_DESC, SERIAL_CODE, SEGMENT_CODE, SEGMENT_ID, SEGMENT_DESC, RET_CODE, RET_MSG, STATUS, LOG_DATE, TOKEN};

    public SO_Pack_Express_LocalDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SO_Pack_Express_LocalToContentValuesMapper();
        this.toSO_Pack_Express_LocalMapper = new CursorSO_Pack_Express_LocalMapper();
    }

    public SO_Pack_Express_LocalDao(Context context) {
        super(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM,
                Constant.DB_MODE_MULTI
        );
        //
        this.toContentValuesMapper = new SO_Pack_Express_LocalToContentValuesMapper();
        this.toSO_Pack_Express_LocalMapper = new CursorSO_Pack_Express_LocalMapper();
    }

    @Override
    public void addUpdate(SO_Pack_Express_Local so_pack_express_local) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(so_pack_express_local)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(so_pack_express_local.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(so_pack_express_local.getSite_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(OPERATION_CODE).append(" = '").append(String.valueOf(so_pack_express_local.getOperation_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(so_pack_express_local.getProduct_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(EXPRESS_CODE).append(" = '").append(so_pack_express_local.getExpress_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(EXPRESS_TMP).append(" = '").append(so_pack_express_local.getExpress_tmp()).append("'");

                db.update(TABLE, toContentValuesMapper.map(so_pack_express_local), sbWhere.toString(), null);
            }

        } catch (Exception e) {
            String error = e.toString();
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SO_Pack_Express_Local> so_pack_express_locals, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (SO_Pack_Express_Local so_pack_express_local : so_pack_express_locals) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(so_pack_express_local)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(so_pack_express_local.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(so_pack_express_local.getSite_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(OPERATION_CODE).append(" = '").append(String.valueOf(so_pack_express_local.getOperation_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(so_pack_express_local.getProduct_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(EXPRESS_CODE).append(" = '").append(so_pack_express_local.getExpress_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(EXPRESS_TMP).append(" = '").append(so_pack_express_local.getExpress_tmp()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(so_pack_express_local), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            db.endTransaction();
        }

        closeDB();
    }

    @Override
    public void addUpdate(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void remove(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public SO_Pack_Express_Local getByString(String s_query) {
        SO_Pack_Express_Local so_pack_express_local = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                so_pack_express_local = toSO_Pack_Express_LocalMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return so_pack_express_local;
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
    public List<SO_Pack_Express_Local> query(String s_query) {
        List<SO_Pack_Express_Local> so_pack_express_locals = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                SO_Pack_Express_Local uAux = toSO_Pack_Express_LocalMapper.map(cursor);
                so_pack_express_locals.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return so_pack_express_locals;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> md_sites = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_sites.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return md_sites;
    }

    private class CursorSO_Pack_Express_LocalMapper implements Mapper<Cursor, SO_Pack_Express_Local> {
        @Override
        public SO_Pack_Express_Local map(Cursor cursor) {
            SO_Pack_Express_Local so_pack_express_local = new SO_Pack_Express_Local();

            so_pack_express_local.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            so_pack_express_local.setSite_code(cursor.getLong(cursor.getColumnIndex(SITE_CODE)));
            so_pack_express_local.setOperation_code(cursor.getLong(cursor.getColumnIndex(OPERATION_CODE)));
            so_pack_express_local.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            so_pack_express_local.setExpress_code(cursor.getString(cursor.getColumnIndex(EXPRESS_CODE)));
            so_pack_express_local.setExpress_tmp(cursor.getLong(cursor.getColumnIndex(EXPRESS_TMP)));
            so_pack_express_local.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            so_pack_express_local.setPartner_code(cursor.getLong(cursor.getColumnIndex(PARTNER_CODE)));

            if (cursor.isNull(cursor.getColumnIndex(SO_PREFIX))) {
                so_pack_express_local.setSo_prefix(null);
            } else {
                so_pack_express_local.setSo_prefix(cursor.getLong(cursor.getColumnIndex(SO_PREFIX)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SO_CODE))) {
                so_pack_express_local.setSo_code(null);
            } else {
                so_pack_express_local.setSo_code(cursor.getLong(cursor.getColumnIndex(SO_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SO_ID))) {
                so_pack_express_local.setSo_id(null);
            } else {
                so_pack_express_local.setSo_id(cursor.getString(cursor.getColumnIndex(SO_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SO_DESC))) {
                so_pack_express_local.setSo_desc(null);
            } else {
                so_pack_express_local.setSo_desc(cursor.getString(cursor.getColumnIndex(SO_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SO_STATUS))) {
                so_pack_express_local.setSo_status(null);
            } else {
                so_pack_express_local.setSo_status(cursor.getString(cursor.getColumnIndex(SO_STATUS)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CONTRACT_CODE))) {
                so_pack_express_local.setContract_code(null);
            } else {
                so_pack_express_local.setContract_code(cursor.getLong(cursor.getColumnIndex(CONTRACT_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CONTRACT_DESC))) {
                so_pack_express_local.setContract_desc(null);
            } else {
                so_pack_express_local.setContract_desc(cursor.getString(cursor.getColumnIndex(CONTRACT_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PRIORITY_CODE))) {
                so_pack_express_local.setPriority_code(null);
            } else {
                so_pack_express_local.setPriority_code(cursor.getLong(cursor.getColumnIndex(PRIORITY_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PRIORITY_DESC))) {
                so_pack_express_local.setPriority_desc(null);
            } else {
                so_pack_express_local.setPriority_desc(cursor.getString(cursor.getColumnIndex(PRIORITY_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SITE_ID))) {
                so_pack_express_local.setSite_id(null);
            } else {
                so_pack_express_local.setSite_id(cursor.getString(cursor.getColumnIndex(SITE_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SITE_DESC))) {
                so_pack_express_local.setSite_desc(null);
            } else {
                so_pack_express_local.setSite_desc(cursor.getString(cursor.getColumnIndex(SITE_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OPERATION_ID))) {
                so_pack_express_local.setOperation_id(null);
            } else {
                so_pack_express_local.setOperation_id(cursor.getString(cursor.getColumnIndex(OPERATION_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OPERATION_DESC))) {
                so_pack_express_local.setOperation_desc(null);
            } else {
                so_pack_express_local.setOperation_desc(cursor.getString(cursor.getColumnIndex(OPERATION_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PRODUCT_ID))) {
                so_pack_express_local.setProduct_id(null);
            } else {
                so_pack_express_local.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PRODUCT_DESC))) {
                so_pack_express_local.setProduct_desc(null);
            } else {
                so_pack_express_local.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SERIAL_CODE))) {
                so_pack_express_local.setSerial_code(null);
            } else {
                so_pack_express_local.setSerial_code(cursor.getLong(cursor.getColumnIndex(SERIAL_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SEGMENT_CODE))) {
                so_pack_express_local.setSegment_code(null);
            } else {
                so_pack_express_local.setSegment_code(cursor.getInt(cursor.getColumnIndex(SEGMENT_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SEGMENT_ID))) {
                so_pack_express_local.setSegment_id(null);
            } else {
                so_pack_express_local.setSegment_id(cursor.getString(cursor.getColumnIndex(SEGMENT_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SEGMENT_CODE))) {
                so_pack_express_local.setSegment_desc(null);
            } else {
                so_pack_express_local.setSegment_desc(cursor.getString(cursor.getColumnIndex(SEGMENT_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(RET_CODE))) {
                so_pack_express_local.setRet_code(null);
            } else {
                so_pack_express_local.setRet_code(cursor.getString(cursor.getColumnIndex(RET_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(RET_MSG))) {
                so_pack_express_local.setRet_msg(null);
            } else {
                so_pack_express_local.setRet_msg(cursor.getString(cursor.getColumnIndex(RET_MSG)));
            }
            so_pack_express_local.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            so_pack_express_local.setLog_date(cursor.getString(cursor.getColumnIndex(LOG_DATE)));
            so_pack_express_local.setToken(cursor.getString(cursor.getColumnIndex(TOKEN)));
            //
            return so_pack_express_local;
        }

    }

    private class SO_Pack_Express_LocalToContentValuesMapper implements Mapper<SO_Pack_Express_Local, ContentValues> {
        @Override
        public ContentValues map(SO_Pack_Express_Local so_pack_express_local) {
            ContentValues contentValues = new ContentValues();

            if (so_pack_express_local.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, so_pack_express_local.getCustomer_code());
            }
            if (so_pack_express_local.getSite_code() > -1) {
                contentValues.put(SITE_CODE, so_pack_express_local.getSite_code());
            }
            if (so_pack_express_local.getOperation_code() > -1) {
                contentValues.put(OPERATION_CODE, so_pack_express_local.getOperation_code());
            }
            if (so_pack_express_local.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, so_pack_express_local.getProduct_code());
            }
            if (so_pack_express_local.getExpress_code() != null) {
                contentValues.put(EXPRESS_CODE, so_pack_express_local.getExpress_code());
            }
            if (so_pack_express_local.getExpress_tmp() > -1) {
                contentValues.put(EXPRESS_TMP, so_pack_express_local.getExpress_tmp());
            }
            //
            if (so_pack_express_local.getSerial_id() != null) {
                contentValues.put(SERIAL_ID, so_pack_express_local.getSerial_id());
            }
            if (so_pack_express_local.getPartner_code() > -1) {
                contentValues.put(PARTNER_CODE, so_pack_express_local.getPartner_code());
            }
            if (so_pack_express_local.getSo_prefix() != null) {
                contentValues.put(SO_PREFIX, so_pack_express_local.getSo_prefix());
            }
            if (so_pack_express_local.getSo_code() != null) {
                contentValues.put(SO_CODE, so_pack_express_local.getSo_code());
            }
            if (so_pack_express_local.getSo_id() != null) {
                contentValues.put(SO_ID, so_pack_express_local.getSo_id());
            }
            if (so_pack_express_local.getSo_desc() != null) {
                contentValues.put(SO_DESC, so_pack_express_local.getSo_desc());
            }
            if (so_pack_express_local.getSo_status() != null) {
                contentValues.put(SO_STATUS, so_pack_express_local.getSo_status());
            }
            if (so_pack_express_local.getContract_code() != null) {
                contentValues.put(CONTRACT_CODE, so_pack_express_local.getContract_code());
            }
            if (so_pack_express_local.getContract_desc() != null) {
                contentValues.put(CONTRACT_DESC, so_pack_express_local.getContract_desc());
            }
            if (so_pack_express_local.getPriority_code() != null) {
                contentValues.put(PRIORITY_CODE, so_pack_express_local.getPriority_code());
            }
            if (so_pack_express_local.getPriority_desc() != null) {
                contentValues.put(PRIORITY_DESC, so_pack_express_local.getPriority_desc());
            }
            if (so_pack_express_local.getSite_id() != null) {
                contentValues.put(SITE_ID, so_pack_express_local.getSite_id());
            }
            if (so_pack_express_local.getSite_desc() != null) {
                contentValues.put(SITE_DESC, so_pack_express_local.getSite_desc());
            }
            if (so_pack_express_local.getOperation_id() != null) {
                contentValues.put(OPERATION_ID, so_pack_express_local.getOperation_id());
            }
            if (so_pack_express_local.getOperation_desc() != null) {
                contentValues.put(OPERATION_DESC, so_pack_express_local.getOperation_desc());
            }
            if (so_pack_express_local.getProduct_id() != null) {
                contentValues.put(PRODUCT_ID, so_pack_express_local.getProduct_id());
            }
            if (so_pack_express_local.getProduct_desc() != null) {
                contentValues.put(PRODUCT_DESC, so_pack_express_local.getProduct_desc());
            }
            if (so_pack_express_local.getSerial_code() != null) {
                contentValues.put(SERIAL_CODE, so_pack_express_local.getSerial_code());
            }
            if (so_pack_express_local.getSegment_code() != null) {
                contentValues.put(SEGMENT_CODE, so_pack_express_local.getSegment_code());
            }
            if (so_pack_express_local.getSegment_id() != null) {
                contentValues.put(SEGMENT_ID, so_pack_express_local.getSegment_id());
            }
            if (so_pack_express_local.getSegment_desc() != null) {
                contentValues.put(SEGMENT_DESC, so_pack_express_local.getSegment_desc());
            }
            if (so_pack_express_local.getRet_code() != null) {
                contentValues.put(RET_CODE, so_pack_express_local.getRet_code());
            }
            if (so_pack_express_local.getRet_msg() != null) {
                contentValues.put(RET_MSG, so_pack_express_local.getRet_msg());
            }
            if (so_pack_express_local.getStatus() != null) {
                contentValues.put(STATUS, so_pack_express_local.getStatus());
            }
            if (so_pack_express_local.getLog_date() != null) {
                contentValues.put(LOG_DATE, so_pack_express_local.getLog_date());
            }
            if (so_pack_express_local.getToken() != null) {
                contentValues.put(TOKEN, so_pack_express_local.getToken());
            }
            //
            return contentValues;
        }
    }
}
