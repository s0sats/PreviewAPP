package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 3/22/18.
 */

public class SO_Pack_ExpressDao extends BaseDao implements Dao<SO_Pack_Express> {

    private final Mapper<SO_Pack_Express, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SO_Pack_Express> toSO_Pack_ExpressMapper;

    public static final String TABLE = "so_pack_expresss";

    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SITE_CODE = "site_code";
    public static final String OPERATION_CODE = "operation_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String EXPRESS_CODE = "express_code";
    public static final String PACK_DESC = "pack_desc";
    public static final String CATEGORY_PRICE_CODE = "category_price_code";
    public static final String CONTRACT_CODE = "contract_code";
    public static final String SEGMENT_CODE = "segment_code";
    public static final String PRICE_LIST_CODE = "price_list_code";
    public static final String PIPELINE_DESC = "pipeline_desc";
    public static final String PACK_CODE = "pack_code";
    public static final String ADD_PACK_SERVICE = "add_pack_service";
    public static final String PRICE = "price";
    public static final String BILLING_ADD_INF1_VIEW = "billing_add_inf1_view";
    public static final String BILLING_ADD_INF1_TEXT = "billing_add_inf1_text";
    public static final String BILLING_ADD_INF2_VIEW = "billing_add_inf2_view";
    public static final String BILLING_ADD_INF2_TEXT = "billing_add_inf2_text";
    public static final String BILLING_ADD_INF3_VIEW = "billing_add_inf3_view";
    public static final String BILLING_ADD_INF3_TEXT = "billing_add_inf3_text";
    public static final String BILLING_ADD_INF1_TRACKING = "billing_add_inf1_tracking";
    public static final String BILLING_ADD_INF2_TRACKING = "billing_add_inf2_tracking";
    public static final String BILLING_ADD_INF3_TRACKING = "billing_add_inf3_tracking";

    private String[] columns = {CUSTOMER_CODE, SITE_CODE, OPERATION_CODE, PRODUCT_CODE, CATEGORY_PRICE_CODE, CONTRACT_CODE, SEGMENT_CODE, PRICE_LIST_CODE, PACK_CODE, ADD_PACK_SERVICE, PRICE, EXPRESS_CODE, PACK_DESC};

    public SO_Pack_ExpressDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SO_Pack_ExpressToContentValuesMapper();
        this.toSO_Pack_ExpressMapper = new CursorSO_Pack_ExpressMapper();
    }

    @Override
    public void addUpdate(SO_Pack_Express so_pack_express) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(so_pack_express)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(so_pack_express.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(so_pack_express.getSite_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(OPERATION_CODE).append(" = '").append(String.valueOf(so_pack_express.getOperation_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(so_pack_express.getProduct_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(EXPRESS_CODE).append(" = '").append(so_pack_express.getExpress_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(so_pack_express), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SO_Pack_Express> so_pack_expresss, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (SO_Pack_Express so_pack_express : so_pack_expresss) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(so_pack_express)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(so_pack_express.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(so_pack_express.getSite_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(OPERATION_CODE).append(" = '").append(String.valueOf(so_pack_express.getOperation_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(so_pack_express.getProduct_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(EXPRESS_CODE).append(" = '").append(so_pack_express.getExpress_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(so_pack_express), sbWhere.toString(), null);
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
    public SO_Pack_Express getByString(String s_query) {
        SO_Pack_Express so_pack_express = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                so_pack_express = toSO_Pack_ExpressMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return so_pack_express;
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
    public List<SO_Pack_Express> query(String s_query) {
        List<SO_Pack_Express> so_pack_expresss = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                SO_Pack_Express uAux = toSO_Pack_ExpressMapper.map(cursor);
                so_pack_expresss.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return so_pack_expresss;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> md_sites = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                md_sites.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return md_sites;
    }

    private class CursorSO_Pack_ExpressMapper implements Mapper<Cursor, SO_Pack_Express> {
        @Override
        public SO_Pack_Express map(Cursor cursor) {
            SO_Pack_Express so_pack_express = new SO_Pack_Express();

            so_pack_express.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            so_pack_express.setSite_code(cursor.getLong(cursor.getColumnIndex(SITE_CODE)));
            so_pack_express.setOperation_code(cursor.getLong(cursor.getColumnIndex(OPERATION_CODE)));
            so_pack_express.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            so_pack_express.setExpress_code(cursor.getString(cursor.getColumnIndex(EXPRESS_CODE)));
            so_pack_express.setPack_desc(cursor.getString(cursor.getColumnIndex(PACK_DESC)));

            so_pack_express.setCategory_price_code(cursor.getInt(cursor.getColumnIndex(CATEGORY_PRICE_CODE)));
            so_pack_express.setContract_code(cursor.getInt(cursor.getColumnIndex(CONTRACT_CODE)));
            so_pack_express.setSegment_code(cursor.getInt(cursor.getColumnIndex(SEGMENT_CODE)));
            so_pack_express.setPrice_list_code(cursor.getInt(cursor.getColumnIndex(PRICE_LIST_CODE)));
            if(cursor.isNull(cursor.getColumnIndex(PIPELINE_DESC))){
                so_pack_express.setPipeline_desc(null);
            }else{
                so_pack_express.setPipeline_desc(cursor.getString(cursor.getColumnIndex(PIPELINE_DESC)));
            }
            so_pack_express.setPack_code(cursor.getInt(cursor.getColumnIndex(PACK_CODE)));
            so_pack_express.setAdd_pack_service(cursor.getInt(cursor.getColumnIndex(ADD_PACK_SERVICE)));
            so_pack_express.setPrice(cursor.getFloat(cursor.getColumnIndex(PRICE)));

            so_pack_express.setBilling_add_inf1_view(cursor.getString(cursor.getColumnIndex(BILLING_ADD_INF1_VIEW)));
            so_pack_express.setBilling_add_inf1_tracking(cursor.getInt(cursor.getColumnIndex(BILLING_ADD_INF1_TRACKING)));
            if(cursor.isNull(cursor.getColumnIndex(BILLING_ADD_INF1_TEXT))){
                so_pack_express.setBilling_add_inf1_text(null);
            }else{
                so_pack_express.setBilling_add_inf1_text(cursor.getString(cursor.getColumnIndex(BILLING_ADD_INF1_TEXT)));
            }
            so_pack_express.setBilling_add_inf2_view(cursor.getString(cursor.getColumnIndex(BILLING_ADD_INF2_VIEW)));
            so_pack_express.setBilling_add_inf2_tracking(cursor.getInt(cursor.getColumnIndex(BILLING_ADD_INF2_TRACKING)));
            if(cursor.isNull(cursor.getColumnIndex(BILLING_ADD_INF2_TEXT))){
                so_pack_express.setBilling_add_inf2_text(null);
            }else{
                so_pack_express.setBilling_add_inf2_text(cursor.getString(cursor.getColumnIndex(BILLING_ADD_INF2_TEXT)));
            }
            so_pack_express.setBilling_add_inf3_view(cursor.getString(cursor.getColumnIndex(BILLING_ADD_INF3_VIEW)));
            so_pack_express.setBilling_add_inf3_tracking(cursor.getInt(cursor.getColumnIndex(BILLING_ADD_INF3_TRACKING)));
            if(cursor.isNull(cursor.getColumnIndex(BILLING_ADD_INF3_TEXT))){
                so_pack_express.setBilling_add_inf3_text(null);
            }else{
                so_pack_express.setBilling_add_inf3_text(cursor.getString(cursor.getColumnIndex(BILLING_ADD_INF3_TEXT)));
            }

            return so_pack_express;
        }

    }

    private class SO_Pack_ExpressToContentValuesMapper implements Mapper<SO_Pack_Express, ContentValues> {
        @Override
        public ContentValues map(SO_Pack_Express so_pack_express) {
            ContentValues contentValues = new ContentValues();

            if (so_pack_express.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, so_pack_express.getCustomer_code());
            }
            if (so_pack_express.getSite_code() > -1) {
                contentValues.put(SITE_CODE, so_pack_express.getSite_code());
            }
            if (so_pack_express.getOperation_code() > -1) {
                contentValues.put(OPERATION_CODE, so_pack_express.getOperation_code());
            }
            if (so_pack_express.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, so_pack_express.getProduct_code());
            }
            if (so_pack_express.getExpress_code() != null) {
                contentValues.put(EXPRESS_CODE, so_pack_express.getExpress_code());
            }
            if (so_pack_express.getPack_desc() != null) {
                contentValues.put(PACK_DESC, so_pack_express.getPack_desc());
            }
            if (so_pack_express.getCategory_price_code() > -1) {
                contentValues.put(CATEGORY_PRICE_CODE, so_pack_express.getCategory_price_code());
            }
            if (so_pack_express.getContract_code() > -1) {
                contentValues.put(CONTRACT_CODE, so_pack_express.getContract_code());
            }
            if (so_pack_express.getSegment_code() > -1) {
                contentValues.put(SEGMENT_CODE, so_pack_express.getSegment_code());
            }
            if (so_pack_express.getPrice_list_code() > -1) {
                contentValues.put(PRICE_LIST_CODE, so_pack_express.getPrice_list_code());
            }
            contentValues.put(PIPELINE_DESC, so_pack_express.getPipeline_desc());
            if (so_pack_express.getPack_code() > -1) {
                contentValues.put(PACK_CODE, so_pack_express.getPack_code());
            }
            if (so_pack_express.getAdd_pack_service() > -1) {
                contentValues.put(ADD_PACK_SERVICE, so_pack_express.getAdd_pack_service());
            }
            if (so_pack_express.getPrice() > -1) {
                contentValues.put(PRICE, so_pack_express.getPrice());
            }
            if (so_pack_express.getBilling_add_inf1_view() != null) {
                contentValues.put(BILLING_ADD_INF1_VIEW, so_pack_express.getBilling_add_inf1_view());
            }
            if (so_pack_express.getBilling_add_inf1_tracking() > -1) {
                contentValues.put(BILLING_ADD_INF1_TRACKING, so_pack_express.getBilling_add_inf1_tracking());
            }
            contentValues.put(BILLING_ADD_INF1_TEXT, so_pack_express.getBilling_add_inf1_text());
            if (so_pack_express.getBilling_add_inf2_view() != null) {
                contentValues.put(BILLING_ADD_INF2_VIEW, so_pack_express.getBilling_add_inf2_view());
            }
            if (so_pack_express.getBilling_add_inf2_tracking() > -1) {
                contentValues.put(BILLING_ADD_INF2_TRACKING, so_pack_express.getBilling_add_inf2_tracking());
            }
            contentValues.put(BILLING_ADD_INF2_TEXT, so_pack_express.getBilling_add_inf2_text());
            if (so_pack_express.getBilling_add_inf3_view() != null) {
                contentValues.put(BILLING_ADD_INF3_VIEW, so_pack_express.getBilling_add_inf3_view());
            }
            if (so_pack_express.getBilling_add_inf3_tracking() > -1) {
                contentValues.put(BILLING_ADD_INF3_TRACKING, so_pack_express.getBilling_add_inf3_tracking());
            }
            contentValues.put(BILLING_ADD_INF3_TEXT, so_pack_express.getBilling_add_inf3_text());
            //
            return contentValues;
        }
    }
}
