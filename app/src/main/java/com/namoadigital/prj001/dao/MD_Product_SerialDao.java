package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_005;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_006;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_007;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 30/06/2017.
 */

public class MD_Product_SerialDao extends BaseDao implements Dao<MD_Product_Serial>, DaoTmpStatus<MD_Product_Serial> {

    private final Mapper<MD_Product_Serial, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product_Serial> toMD_Product_SerialMapper;

    public static final String TABLE = "md_product_serials";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DESC = "product_desc";
    public static final String SERIAL_CODE = "serial_code";
    public static final String SERIAL_TMP = "serial_tmp";
    public static final String SERIAL_ID = "serial_id";
    public static final String SITE_CODE = "site_code";
    public static final String SITE_ID = "site_id";
    public static final String SITE_DESC = "site_desc";
    public static final String ZONE_CODE = "zone_code";
    public static final String ZONE_ID = "zone_id";
    public static final String ZONE_DESC = "zone_desc";
    public static final String LOCAL_CODE = "local_code";
    public static final String LOCAL_ID = "local_id";
    public static final String SITE_CODE_OWNER = "site_code_owner";
    public static final String BRAND_CODE = "brand_code";
    public static final String BRAND_ID = "brand_id";
    public static final String BRAND_DESC = "brand_desc";
    public static final String MODEL_CODE = "model_code";
    public static final String MODEL_ID = "model_id";
    public static final String MODEL_DESC = "model_desc";
    public static final String COLOR_CODE = "color_code";
    public static final String COLOR_ID = "color_id";
    public static final String COLOR_DESC = "color_desc";
    public static final String SEGMENT_CODE = "segment_code";
    public static final String SEGMENT_ID = "segment_id";
    public static final String SEGMENT_DESC = "segment_desc";
    public static final String CATEGORY_PRICE_CODE = "category_price_code";
    public static final String CATEGORY_PRICE_ID = "category_price_id";
    public static final String CATEGORY_PRICE_DESC = "category_price_desc";
    public static final String ADD_INF1 = "add_inf1";
    public static final String ADD_INF2 = "add_inf2";
    public static final String ADD_INF3 = "add_inf3";
    public static final String UPDATE_REQUIRED = "update_required";
    public static final String ONLY_POSITION = "only_position";
    public static final String FLAG_OFFLINE = "flag_offline";
    public static final String SYNC_PROCESS = "sync_process";
    public static final String CLASS_CODE = "class_code";
    public static final String CLASS_ID = "class_id";
    public static final String CLASS_TYPE = "class_type";
    public static final String CLASS_COLOR = "class_color";
    public static final String CLASS_AVAILABLE = "class_available";
    public static final String INBOUND_CODE = "inbound_code";
    public static final String INBOUND_ID = "inbound_id";
    public static final String INBOUND_CONF_DATE = "inbound_conf_date";
    public static final String MOVE_PREFIX = "move_prefix";
    public static final String MOVE_CODE = "move_code";
    public static final String MOVE_GROUP_CODE = "move_group_code";
    public static final String OUTBOUND_CODE = "outbound_code";
    public static final String OUTBOUND_ID = "outbound_id";

    public static String[] columns = {CUSTOMER_CODE, PRODUCT_CODE, PRODUCT_ID, PRODUCT_DESC, SERIAL_CODE, SERIAL_TMP,
            SERIAL_ID, SITE_CODE, ZONE_CODE, LOCAL_CODE, SITE_CODE_OWNER, BRAND_CODE,
            MODEL_CODE, COLOR_CODE, SEGMENT_CODE, CATEGORY_PRICE_CODE, ADD_INF1,
            ADD_INF2, ADD_INF3, ONLY_POSITION, UPDATE_REQUIRED, FLAG_OFFLINE,
            SYNC_PROCESS, SITE_ID, SITE_DESC, ZONE_ID, ZONE_DESC, LOCAL_ID, BRAND_ID, BRAND_DESC, MODEL_ID, MODEL_DESC, COLOR_ID,
            COLOR_DESC, SEGMENT_ID, SEGMENT_DESC, CATEGORY_PRICE_ID, CATEGORY_PRICE_DESC, CLASS_CODE, CLASS_ID, CLASS_TYPE,
            CLASS_COLOR, CLASS_AVAILABLE, INBOUND_CODE, INBOUND_ID, INBOUND_CONF_DATE, MOVE_PREFIX, MOVE_CODE, MOVE_GROUP_CODE,
            OUTBOUND_CODE, OUTBOUND_ID
    };

    public MD_Product_SerialDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_Product_SerialToContentValuesMapper();
        this.toMD_Product_SerialMapper = new CursorMD_Product_SerialMapper();
    }

    public MD_Product_SerialDao(Context context) {
        super(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM,
                Constant.DB_MODE_MULTI
        );

        this.toContentValuesMapper = new MD_Product_SerialToContentValuesMapper();
        this.toMD_Product_SerialMapper = new CursorMD_Product_SerialMapper();
    }


    @Override
    public void addUpdate(MD_Product_Serial md_product_serial) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_serial)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_serial.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_serial.getProduct_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERIAL_CODE).append(" = '").append(String.valueOf(md_product_serial.getSerial_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_product_serial), sbWhere.toString(), null);
            }
            //
            MD_Product_Serial_TrackingDao md_product_serial_trackingDao =
                    new MD_Product_Serial_TrackingDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

            md_product_serial_trackingDao.addUpdate(md_product_serial.getTracking_list(), false);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Product_Serial> md_product_serials, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Product_Serial md_product_serial : md_product_serials) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_serial)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_serial.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_serial.getProduct_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERIAL_CODE).append(" = '").append(String.valueOf(md_product_serial.getSerial_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_product_serial), sbWhere.toString(), null);
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
    public void addUpdateTmp(MD_Product_Serial md_product_serial) {
        openDB();

        int serial_tmp = 0;
        Cursor cursor = null;

        if (md_product_serial.getSerial_tmp() == 0) {
            if (md_product_serial.getSerial_code() == 0) {
                cursor = db.rawQuery(
                        new MD_Product_Serial_Sql_005(
                                md_product_serial.getCustomer_code(),
                                md_product_serial.getProduct_code()
                        ).toSqlQuery(),
                        null
                );
            } else {
                cursor = db.rawQuery(
                        new MD_Product_Serial_Sql_007(
                                md_product_serial.getCustomer_code(),
                                md_product_serial.getProduct_code(),
                                md_product_serial.getSerial_code()
                        ).toSqlQuery(),
                        null
                );
            }

        } else {
            cursor = db.rawQuery(
                    new MD_Product_Serial_Sql_006(
                            md_product_serial.getCustomer_code(),
                            md_product_serial.getProduct_code(),
                            md_product_serial.getSerial_tmp()
                    ).toSqlQuery(),
                    null
            );
        }
        //
        while (cursor.moveToNext()) {
            serial_tmp = cursor.getInt(cursor.getColumnIndex(SERIAL_TMP));
        }
        //Seta o novo valor no tmp no serial e atualiza pk na listagem de tracking
        //
        if (serial_tmp != 0) {
            md_product_serial.setSerial_tmp(serial_tmp);
            for (int i = 0; i < md_product_serial.getTracking_list().size(); i++) {
                md_product_serial.getTracking_list().get(i).setPk(md_product_serial);
            }
        }

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_serial)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_serial.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_serial.getProduct_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERIAL_TMP).append(" = '").append(String.valueOf(md_product_serial.getSerial_tmp())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_product_serial), sbWhere.toString(), null);
            }
            //
            MD_Product_Serial_TrackingDao md_product_serial_trackingDao =
                    new MD_Product_Serial_TrackingDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );
            //Apaga Trackings antesde chamar addUpdate
            md_product_serial_trackingDao.remove(
                    new MD_Product_Serial_Tracking_Sql_002(
                            md_product_serial.getCustomer_code(),
                            md_product_serial.getProduct_code(),
                            md_product_serial.getSerial_tmp()
                    ).toSqlQuery()
            );
            //
            md_product_serial_trackingDao.addUpdate(md_product_serial.getTracking_list(), false);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdateTmp(Iterable<MD_Product_Serial> md_product_serials, boolean status) {
        openDB();

        try {
            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }
            //
            MD_Product_Serial_TrackingDao md_product_serial_trackingDao =
                    new MD_Product_Serial_TrackingDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );
            //
            for (MD_Product_Serial md_product_serial : md_product_serials) {

                int serial_tmp = 0;
                Cursor cursor = null;

                if (md_product_serial.getSerial_tmp() == 0) {
                    if (md_product_serial.getSerial_code() == 0) {
                        cursor = db.rawQuery(
                                new MD_Product_Serial_Sql_005(
                                        md_product_serial.getCustomer_code(),
                                        md_product_serial.getProduct_code()
                                ).toSqlQuery(),
                                null
                        );
                    } else {
                        cursor = db.rawQuery(
                                new MD_Product_Serial_Sql_007(
                                        md_product_serial.getCustomer_code(),
                                        md_product_serial.getProduct_code(),
                                        md_product_serial.getSerial_code()
                                ).toSqlQuery(),
                                null
                        );
                    }

                } else {
                    cursor = db.rawQuery(
                            new MD_Product_Serial_Sql_006(
                                    md_product_serial.getCustomer_code(),
                                    md_product_serial.getProduct_code(),
                                    md_product_serial.getSerial_tmp()
                            ).toSqlQuery(),
                            null
                    );
                }
                //
                while (cursor.moveToNext()) {
                    serial_tmp = cursor.getInt(cursor.getColumnIndex(SERIAL_TMP));
                }
                //Seta o novo valor no tmp no serial e atualiza pk na listagem de tracking
                //
                if (serial_tmp != 0) {
                    md_product_serial.setSerial_tmp(serial_tmp);
                    for (int i = 0; i < md_product_serial.getTracking_list().size(); i++) {
                        md_product_serial.getTracking_list().get(i).setPk(md_product_serial);
                    }
                }

                if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_serial)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_serial.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_serial.getProduct_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SERIAL_TMP).append(" = '").append(String.valueOf(md_product_serial.getSerial_tmp())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_product_serial), sbWhere.toString(), null);
                }
                //Apaga Trackings antesde chamar addUpdate
                md_product_serial_trackingDao.remove(
                        new MD_Product_Serial_Tracking_Sql_002(
                                md_product_serial.getCustomer_code(),
                                md_product_serial.getProduct_code(),
                                md_product_serial.getSerial_tmp()
                        ).toSqlQuery()
                );
                //
                md_product_serial_trackingDao.addUpdate(md_product_serial.getTracking_list(), false);

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
    public void updateStatusOffLine(MD_Product_Serial item) {

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
    public MD_Product_Serial getByString(String sQuery) {
        MD_Product_Serial md_product_serial = null;

        openDB();
        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_product_serial = toMD_Product_SerialMapper.map(cursor);
            }

            if (md_product_serial != null) {
                MD_Product_Serial_TrackingDao trackingDao = new MD_Product_Serial_TrackingDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );
                //
                md_product_serial.setTracking_list(
                        (ArrayList<MD_Product_Serial_Tracking>)
                                trackingDao.query(
                                        new MD_Product_Serial_Tracking_Sql_001(
                                                md_product_serial.getCustomer_code(),
                                                md_product_serial.getProduct_code(),
                                                md_product_serial.getSerial_tmp()
                                        ).toSqlQuery()
                                )
                );
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return md_product_serial;
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
    public List<MD_Product_Serial> query(String sQuery) {

        List<MD_Product_Serial> md_product_serials = new ArrayList<>();

        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Product_Serial uAux = toMD_Product_SerialMapper.map(cursor);
                //
                if (uAux != null) {
                    MD_Product_Serial_TrackingDao trackingDao = new MD_Product_Serial_TrackingDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );
                    //
                    uAux.setTracking_list(
                            (ArrayList<MD_Product_Serial_Tracking>)
                                    trackingDao.query(
                                            new MD_Product_Serial_Tracking_Sql_001(
                                                    uAux.getCustomer_code(),
                                                    uAux.getProduct_code(),
                                                    uAux.getSerial_tmp()
                                            ).toSqlQuery()
                                    )
                    );
                }
                //
                md_product_serials.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
        return md_product_serials;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_product_serials = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_product_serials.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);

        } finally {
        }

        closeDB();
        return md_product_serials;
    }

    private class CursorMD_Product_SerialMapper implements Mapper<Cursor, MD_Product_Serial> {
        @Override
        public MD_Product_Serial map(Cursor cursor) {
            MD_Product_Serial md_product_serial = new MD_Product_Serial();

            md_product_serial.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product_serial.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            md_product_serial.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            md_product_serial.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            md_product_serial.setSerial_code(cursor.getLong(cursor.getColumnIndex(SERIAL_CODE)));
            md_product_serial.setSerial_tmp(cursor.getLong(cursor.getColumnIndex(SERIAL_TMP)));
            md_product_serial.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            if (cursor.isNull(cursor.getColumnIndex(SITE_CODE))) {
                md_product_serial.setSite_code(null);
            } else {
                md_product_serial.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SITE_ID))) {
                md_product_serial.setSite_id(null);
            } else {
                md_product_serial.setSite_id(cursor.getString(cursor.getColumnIndex(SITE_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SITE_DESC))) {
                md_product_serial.setSite_desc(null);
            } else {
                md_product_serial.setSite_desc(cursor.getString(cursor.getColumnIndex(SITE_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ZONE_CODE))) {
                md_product_serial.setZone_code(null);
            } else {
                md_product_serial.setZone_code(cursor.getInt(cursor.getColumnIndex(ZONE_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ZONE_ID))) {
                md_product_serial.setZone_id(null);
            } else {
                md_product_serial.setZone_id(cursor.getString(cursor.getColumnIndex(ZONE_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ZONE_DESC))) {
                md_product_serial.setZone_desc(null);
            } else {
                md_product_serial.setZone_desc(cursor.getString(cursor.getColumnIndex(ZONE_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(LOCAL_CODE))) {
                md_product_serial.setLocal_code(null);
            } else {
                md_product_serial.setLocal_code(cursor.getInt(cursor.getColumnIndex(LOCAL_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(LOCAL_ID))) {
                md_product_serial.setLocal_id(null);
            } else {
                md_product_serial.setLocal_id(cursor.getString(cursor.getColumnIndex(LOCAL_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SITE_CODE_OWNER))) {
                md_product_serial.setSite_code_owner(null);
            } else {
                md_product_serial.setSite_code_owner(cursor.getInt(cursor.getColumnIndex(SITE_CODE_OWNER)));
            }
            if (cursor.isNull(cursor.getColumnIndex(BRAND_CODE))) {
                md_product_serial.setBrand_code(null);
            } else {
                md_product_serial.setBrand_code(cursor.getInt(cursor.getColumnIndex(BRAND_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(BRAND_ID))) {
                md_product_serial.setBrand_id(null);
            } else {
                md_product_serial.setBrand_id(cursor.getString(cursor.getColumnIndex(BRAND_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(BRAND_DESC))) {
                md_product_serial.setBrand_desc(null);
            } else {
                md_product_serial.setBrand_desc(cursor.getString(cursor.getColumnIndex(BRAND_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(MODEL_CODE))) {
                md_product_serial.setModel_code(null);
            } else {
                md_product_serial.setModel_code(cursor.getInt(cursor.getColumnIndex(MODEL_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(MODEL_ID))) {
                md_product_serial.setModel_id(null);
            } else {
                md_product_serial.setModel_id(cursor.getString(cursor.getColumnIndex(MODEL_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(MODEL_DESC))) {
                md_product_serial.setModel_desc(null);
            } else {
                md_product_serial.setModel_desc(cursor.getString(cursor.getColumnIndex(MODEL_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(COLOR_CODE))) {
                md_product_serial.setColor_code(null);
            } else {
                md_product_serial.setColor_code(cursor.getInt(cursor.getColumnIndex(COLOR_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(COLOR_ID))) {
                md_product_serial.setColor_id(null);
            } else {
                md_product_serial.setColor_id(cursor.getString(cursor.getColumnIndex(COLOR_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(COLOR_DESC))) {
                md_product_serial.setColor_desc(null);
            } else {
                md_product_serial.setColor_desc(cursor.getString(cursor.getColumnIndex(COLOR_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SEGMENT_CODE))) {
                md_product_serial.setSegment_code(null);
            } else {
                md_product_serial.setSegment_code(cursor.getInt(cursor.getColumnIndex(SEGMENT_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SEGMENT_ID))) {
                md_product_serial.setSegment_id(null);
            } else {
                md_product_serial.setSegment_id(cursor.getString(cursor.getColumnIndex(SEGMENT_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SEGMENT_DESC))) {
                md_product_serial.setSegment_desc(null);
            } else {
                md_product_serial.setSegment_desc(cursor.getString(cursor.getColumnIndex(SEGMENT_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CATEGORY_PRICE_CODE))) {
                md_product_serial.setCategory_price_code(null);
            } else {
                md_product_serial.setCategory_price_code(cursor.getInt(cursor.getColumnIndex(CATEGORY_PRICE_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CATEGORY_PRICE_ID))) {
                md_product_serial.setCategory_price_id(null);
            } else {
                md_product_serial.setCategory_price_id(cursor.getString(cursor.getColumnIndex(CATEGORY_PRICE_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CATEGORY_PRICE_DESC))) {
                md_product_serial.setCategory_price_desc(null);
            } else {
                md_product_serial.setCategory_price_desc(cursor.getString(cursor.getColumnIndex(CATEGORY_PRICE_DESC)));
            }
            md_product_serial.setAdd_inf1(cursor.getString(cursor.getColumnIndex(ADD_INF1)));
            md_product_serial.setAdd_inf2(cursor.getString(cursor.getColumnIndex(ADD_INF2)));
            md_product_serial.setAdd_inf3(cursor.getString(cursor.getColumnIndex(ADD_INF3)));
            md_product_serial.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));
            if (cursor.isNull(cursor.getColumnIndex(ONLY_POSITION))) {
                md_product_serial.setOnly_position(null);
            } else {
                md_product_serial.setOnly_position(cursor.getInt(cursor.getColumnIndex(ONLY_POSITION)));
            }
            md_product_serial.setFlag_offline(cursor.getInt(cursor.getColumnIndex(FLAG_OFFLINE)));
            md_product_serial.setSync_process(cursor.getInt(cursor.getColumnIndex(SYNC_PROCESS)));
            if (cursor.isNull(cursor.getColumnIndex(CLASS_CODE))) {
                md_product_serial.setClass_code(null);
            } else {
                md_product_serial.setClass_code(cursor.getInt(cursor.getColumnIndex(CLASS_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLASS_ID))) {
                md_product_serial.setClass_id(null);
            } else {
                md_product_serial.setClass_id(cursor.getString(cursor.getColumnIndex(CLASS_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLASS_TYPE))) {
                md_product_serial.setClass_type(null);
            } else {
                md_product_serial.setClass_type(cursor.getString(cursor.getColumnIndex(CLASS_TYPE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLASS_COLOR))) {
                md_product_serial.setClass_color(null);
            } else {
                md_product_serial.setClass_color(cursor.getString(cursor.getColumnIndex(CLASS_COLOR)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLASS_AVAILABLE))) {
                md_product_serial.setClass_available(null);
            } else {
                md_product_serial.setClass_available(cursor.getInt(cursor.getColumnIndex(CLASS_AVAILABLE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(INBOUND_CODE))) {
                md_product_serial.setInbound_code(null);
            } else {
                md_product_serial.setInbound_code(cursor.getInt(cursor.getColumnIndex(INBOUND_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(INBOUND_ID))) {
                md_product_serial.setInbound_id(null);
            } else {
                md_product_serial.setInbound_id(cursor.getString(cursor.getColumnIndex(INBOUND_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(INBOUND_CONF_DATE))) {
                md_product_serial.setInbound_conf_date(null);
            } else {
                md_product_serial.setInbound_conf_date(cursor.getString(cursor.getColumnIndex(INBOUND_CONF_DATE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(MOVE_PREFIX))) {
                md_product_serial.setMove_prefix(null);
            } else {
                md_product_serial.setMove_prefix(cursor.getInt(cursor.getColumnIndex(MOVE_PREFIX)));
            }
            if (cursor.isNull(cursor.getColumnIndex(MOVE_CODE))) {
                md_product_serial.setMove_code(null);
            } else {
                md_product_serial.setMove_code(cursor.getInt(cursor.getColumnIndex(MOVE_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(MOVE_GROUP_CODE))) {
                md_product_serial.setMove_group_code(null);
            } else {
                md_product_serial.setMove_group_code(cursor.getInt(cursor.getColumnIndex(MOVE_GROUP_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OUTBOUND_CODE))) {
                md_product_serial.setOutbound_code(null);
            } else {
                md_product_serial.setOutbound_code(cursor.getInt(cursor.getColumnIndex(OUTBOUND_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OUTBOUND_ID))) {
                md_product_serial.setOutbound_id(null);
            } else {
                md_product_serial.setOutbound_id(cursor.getString(cursor.getColumnIndex(OUTBOUND_ID)));
            }
            //
            return md_product_serial;
        }
    }

    private class MD_Product_SerialToContentValuesMapper implements Mapper<MD_Product_Serial, ContentValues> {
        @Override
        public ContentValues map(MD_Product_Serial md_product_serial) {
            ContentValues contentValues = new ContentValues();

            if (md_product_serial.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_product_serial.getCustomer_code());
            }

            if (md_product_serial.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, md_product_serial.getProduct_code());
            }

            if (md_product_serial.getProduct_id() != null) {
                contentValues.put(PRODUCT_ID, md_product_serial.getProduct_id());
            }

            if (md_product_serial.getProduct_desc() != null) {
                contentValues.put(PRODUCT_DESC, md_product_serial.getProduct_desc());
            }

            if (md_product_serial.getSerial_code() > -1) {
                contentValues.put(SERIAL_CODE, md_product_serial.getSerial_code());
            }

            if (md_product_serial.getSerial_code() > -1) {
                contentValues.put(SERIAL_TMP, md_product_serial.getSerial_tmp());
            }

            if (md_product_serial.getSerial_id() != null) {
                contentValues.put(SERIAL_ID, md_product_serial.getSerial_id());
            }
            contentValues.put(SITE_CODE, md_product_serial.getSite_code());
            contentValues.put(SITE_ID, md_product_serial.getSite_id());
            contentValues.put(SITE_DESC, md_product_serial.getSite_desc());
            contentValues.put(ZONE_CODE, md_product_serial.getZone_code());
            contentValues.put(ZONE_ID, md_product_serial.getZone_id());
            contentValues.put(ZONE_DESC, md_product_serial.getZone_desc());
            contentValues.put(LOCAL_CODE, md_product_serial.getLocal_code());
            contentValues.put(LOCAL_ID, md_product_serial.getLocal_id());
            contentValues.put(SITE_CODE_OWNER, md_product_serial.getSite_code_owner());
            contentValues.put(BRAND_CODE, md_product_serial.getBrand_code());
            contentValues.put(BRAND_ID, md_product_serial.getBrand_id());
            contentValues.put(BRAND_DESC, md_product_serial.getBrand_desc());
            contentValues.put(MODEL_CODE, md_product_serial.getModel_code());
            contentValues.put(MODEL_ID, md_product_serial.getModel_id());
            contentValues.put(MODEL_DESC, md_product_serial.getModel_desc());
            contentValues.put(COLOR_CODE, md_product_serial.getColor_code());
            contentValues.put(COLOR_ID, md_product_serial.getColor_id());
            contentValues.put(COLOR_DESC, md_product_serial.getColor_desc());
            contentValues.put(SEGMENT_CODE, md_product_serial.getSegment_code());
            contentValues.put(SEGMENT_ID, md_product_serial.getSegment_id());
            contentValues.put(SEGMENT_DESC, md_product_serial.getSegment_desc());
            contentValues.put(CATEGORY_PRICE_CODE, md_product_serial.getCategory_price_code());
            contentValues.put(CATEGORY_PRICE_ID, md_product_serial.getCategory_price_id());
            contentValues.put(CATEGORY_PRICE_DESC, md_product_serial.getCategory_price_desc());
            if (md_product_serial.getAdd_inf1() != null) {
                contentValues.put(ADD_INF1, md_product_serial.getAdd_inf1());
            }
            if (md_product_serial.getAdd_inf2() != null) {
                contentValues.put(ADD_INF2, md_product_serial.getAdd_inf2());
            }
            if (md_product_serial.getAdd_inf3() != null) {
                contentValues.put(ADD_INF3, md_product_serial.getAdd_inf3());
            }
            if (md_product_serial.getUpdate_required() > -1) {
                contentValues.put(UPDATE_REQUIRED, md_product_serial.getUpdate_required());
            }
            contentValues.put(ONLY_POSITION, md_product_serial.getOnly_position());
            if (md_product_serial.getFlag_offline() > -1) {
                contentValues.put(FLAG_OFFLINE, md_product_serial.getFlag_offline());
            }
            //
            if (md_product_serial.getSync_process() > -1) {
                contentValues.put(SYNC_PROCESS, md_product_serial.getSync_process());
            }
            contentValues.put(CLASS_CODE, md_product_serial.getClass_code());
            contentValues.put(CLASS_ID, md_product_serial.getClass_id());
            contentValues.put(CLASS_TYPE, md_product_serial.getClass_type());
            contentValues.put(CLASS_COLOR, md_product_serial.getClass_color());
            contentValues.put(CLASS_AVAILABLE, md_product_serial.getClass_available());
            contentValues.put(INBOUND_CODE, md_product_serial.getInbound_code());
            contentValues.put(INBOUND_ID, md_product_serial.getInbound_id());
            contentValues.put(INBOUND_CONF_DATE, md_product_serial.getInbound_conf_date());
            contentValues.put(MOVE_PREFIX, md_product_serial.getMove_prefix());
            contentValues.put(MOVE_CODE, md_product_serial.getMove_code());
            contentValues.put(MOVE_GROUP_CODE, md_product_serial.getMove_group_code());
            contentValues.put(OUTBOUND_CODE, md_product_serial.getOutbound_code());
            contentValues.put(OUTBOUND_ID, md_product_serial.getOutbound_id());

            return contentValues;
        }
    }
}
