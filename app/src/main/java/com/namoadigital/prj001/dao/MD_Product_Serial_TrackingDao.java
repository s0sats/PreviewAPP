package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 02/09/2017.
 */

public class MD_Product_Serial_TrackingDao extends BaseDao implements Dao<MD_Product_Serial_Tracking> {


    private final Mapper<MD_Product_Serial_Tracking, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product_Serial_Tracking> toMD_Product_Serial_TrackingMapper;

    public static final String TABLE = "md_product_serial_trackings";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String SERIAL_CODE = "serial_code";
    public static final String SERIAL_TMP = "serial_tmp";
    public static final String TRACKING = "tracking";

    public static final String[] columns = {CUSTOMER_CODE,PRODUCT_CODE,SERIAL_CODE,SERIAL_TMP,TRACKING};


    public MD_Product_Serial_TrackingDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_Product_Serial_TrackingToContentValuesMapper();
        this.toMD_Product_Serial_TrackingMapper = new CursorMD_Product_Serial_TrackingMapper();

    }

    public MD_Product_Serial_TrackingDao(Context context) {
        super(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM,
                Constant.DB_MODE_MULTI
        );

        this.toContentValuesMapper = new MD_Product_Serial_TrackingToContentValuesMapper();
        this.toMD_Product_Serial_TrackingMapper = new CursorMD_Product_Serial_TrackingMapper();

    }

    @Override
    public void addUpdate(MD_Product_Serial_Tracking md_product_serial_tracking) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_serial_tracking)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_serial_tracking.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_serial_tracking.getProduct_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERIAL_TMP).append(" = '").append(String.valueOf(md_product_serial_tracking.getSerial_tmp())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TRACKING).append(" = '").append(String.valueOf(md_product_serial_tracking.getTracking())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_product_serial_tracking), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Product_Serial_Tracking> md_product_serial_trackings, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Product_Serial_Tracking md_product_serial_tracking :md_product_serial_trackings) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_serial_tracking)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_serial_tracking.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_serial_tracking.getProduct_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERIAL_TMP).append(" = '").append(String.valueOf(md_product_serial_tracking.getSerial_tmp())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(TRACKING).append(" = '").append(String.valueOf(md_product_serial_tracking.getTracking())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_product_serial_tracking), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
            db.endTransaction();
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
        } finally {
        }

        closeDB();
    }

    @Override
    public MD_Product_Serial_Tracking getByString(String sQuery) {

        MD_Product_Serial_Tracking md_product_serial_tracking = null;

        openDB();
        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_product_serial_tracking = toMD_Product_Serial_TrackingMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_product_serial_tracking;
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
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return hmAux;
    }

    @Override
    public List<MD_Product_Serial_Tracking> query(String sQuery) {
        List<MD_Product_Serial_Tracking> md_product_serial_trackings = new ArrayList<>();

        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Product_Serial_Tracking uAux = toMD_Product_Serial_TrackingMapper.map(cursor);
                md_product_serial_trackings.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_product_serial_trackings;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {

        List<HMAux> md_product_serial_trackings = new ArrayList<>();

        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_product_serial_trackings.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);

        } finally {
        }

        closeDB();

        return md_product_serial_trackings;
    }

    private class CursorMD_Product_Serial_TrackingMapper implements Mapper<Cursor,MD_Product_Serial_Tracking> {
        @Override
        public MD_Product_Serial_Tracking map(Cursor cursor) {
            MD_Product_Serial_Tracking md_product_serial_tracking = new MD_Product_Serial_Tracking();

            md_product_serial_tracking.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product_serial_tracking.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            md_product_serial_tracking.setSerial_code(cursor.getLong(cursor.getColumnIndex(SERIAL_CODE)));
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_TMP))){
                md_product_serial_tracking.setSerial_tmp(null);
            }else{
                md_product_serial_tracking.setSerial_tmp(cursor.getLong(cursor.getColumnIndex(SERIAL_TMP)));
            }
            md_product_serial_tracking.setSerial_tmp(cursor.getLong(cursor.getColumnIndex(SERIAL_TMP)));
            md_product_serial_tracking.setTracking(cursor.getString(cursor.getColumnIndex(TRACKING)));

            return md_product_serial_tracking;
        }
    }

    private class MD_Product_Serial_TrackingToContentValuesMapper implements Mapper<MD_Product_Serial_Tracking,ContentValues> {

        @Override
        public ContentValues map(MD_Product_Serial_Tracking md_product_serial_tracking) {
            ContentValues contentValues = new ContentValues();

            if(md_product_serial_tracking.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,md_product_serial_tracking.getCustomer_code());
            }

            if(md_product_serial_tracking.getProduct_code() > -1){
                contentValues.put(PRODUCT_CODE,md_product_serial_tracking.getProduct_code());
            }

            if(md_product_serial_tracking.getSerial_code() > -1){
                contentValues.put(SERIAL_CODE,md_product_serial_tracking.getSerial_code());
            }

            if(md_product_serial_tracking.getSerial_tmp() > -1){
                contentValues.put(SERIAL_TMP,md_product_serial_tracking.getSerial_tmp());
            }

            if(md_product_serial_tracking.getTracking() != null){
                contentValues.put(TRACKING,md_product_serial_tracking.getTracking());
            }

            return contentValues;
        }
    }
}
