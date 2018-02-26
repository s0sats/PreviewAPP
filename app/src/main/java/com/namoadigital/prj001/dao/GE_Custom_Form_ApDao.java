package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 23/02/2018.
 */

public class GE_Custom_Form_ApDao extends BaseDao implements Dao<GE_Custom_Form_Ap> {

    private final Mapper<GE_Custom_Form_Ap, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Ap> toGE_Custom_Form_ApMapper;

    public static final String TABLE = "ge_custom_form_aps";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_TYPE_DESC = "custom_form_type_desc";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String CUSTOM_FORM_DESC = "custom_form_desc";
    public static final String CUSTOM_FORM_DATA = "custom_form_data";
    public static final String AP_CODE = "ap_code";
    public static final String AP_DESCRIPTION = "ap_description";
    public static final String AP_STATUS = "ap_status";
    public static final String AP_COMMENTS = "ap_comments";
    public static final String AP_WHAT = "ap_what";
    public static final String AP_WHERE = "ap_where";
    public static final String AP_WHY = "ap_why";
    public static final String AP_WHO = "ap_who";
    public static final String AP_HOW = "ap_how";
    public static final String AP_HOW_MUCH = "ap_how_much";
    public static final String AP_WHEN = "ap_when";
    public static final String DEPARTMENT_CODE = "department_code";
    public static final String ROOM_CODE = "room_code";
    public static final String AP_SCN = "ap_scn";
    public static final String PRODUCT_CODE = "product_code";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DESC = "product_desc";
    public static final String SERIAL_CODE = "serial_code";
    public static final String SERIAL_ID = "serial_id";
    public static final String SYNC_REQUIRED = "sync_required";
    public static final String UPLOAD_REQUIRED = "upload_required";

    public static String[] columns = {
            CUSTOM_FORM_TYPE, CUSTOM_FORM_TYPE_DESC, CUSTOM_FORM_CODE, CUSTOM_FORM_VERSION, CUSTOM_FORM_DESC,
            CUSTOM_FORM_DATA, AP_CODE, AP_DESCRIPTION, AP_STATUS, AP_COMMENTS, AP_WHAT, AP_WHERE, AP_WHY, AP_WHO,
            AP_HOW, AP_HOW_MUCH, AP_WHEN, DEPARTMENT_CODE, ROOM_CODE, AP_SCN, PRODUCT_CODE, PRODUCT_ID, PRODUCT_DESC,
            SERIAL_CODE, SERIAL_ID, SYNC_REQUIRED,UPLOAD_REQUIRED
    };


    public GE_Custom_Form_ApDao(Context context) {
        super(  context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM,
                Constant.DB_MODE_MULTI
        );
        //
        this.toContentValuesMapper = new GE_Custom_Form_ApToContentValuesMapper();
        this.toGE_Custom_Form_ApMapper = new CursorToGE_Custom_Form_ApMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Ap custom_form_ap) {
        openDB();
        try {
            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_ap)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_ap.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_ap.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_ap.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_ap.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_ap.getCustom_form_data())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(AP_CODE).append(" = '").append(String.valueOf(custom_form_ap.getAp_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(custom_form_ap), sbWhere.toString(), null);
            }
        } catch (Exception e) {
        } finally {
        }
        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Ap> custom_form_aps, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Ap custom_form_ap : custom_form_aps) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_ap)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_ap.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_ap.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_ap.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_ap.getCustom_form_version())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_ap.getCustom_form_data())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(AP_CODE).append(" = '").append(String.valueOf(custom_form_ap.getAp_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form_ap), sbWhere.toString(), null);
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
    public GE_Custom_Form_Ap getByString(String sQuery) {
        GE_Custom_Form_Ap custom_form_ap = null;
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                custom_form_ap = toGE_Custom_Form_ApMapper.map(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        closeDB();
        return custom_form_ap;
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
    public List<GE_Custom_Form_Ap> query(String sQuery) {
        List<GE_Custom_Form_Ap> custom_form_aps = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Ap uAux = toGE_Custom_Form_ApMapper.map(cursor);
                custom_form_aps.add(uAux);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
        return custom_form_aps;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> custom_form_aps = new ArrayList<>();
        openDB();
        String s_query_div[] = sQuery.split(";");
        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);
        try {
            Cursor cursor = db.rawQuery(s_query_div[0], null);
            while (cursor.moveToNext()) {
                custom_form_aps.add(toHMAuxMapper.map(cursor));
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            String st = e.toString();
        } finally {
        }
        closeDB();
        return custom_form_aps;
    }

    private class CursorToGE_Custom_Form_ApMapper implements Mapper<Cursor, GE_Custom_Form_Ap> {
        @Override
        public GE_Custom_Form_Ap map(Cursor cursor) {
            GE_Custom_Form_Ap custom_form_ap = new GE_Custom_Form_Ap();
            //
            custom_form_ap.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form_ap.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form_ap.setCustom_form_type_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_TYPE_DESC)));
            custom_form_ap.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            custom_form_ap.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            custom_form_ap.setCustom_form_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_DESC)));
            custom_form_ap.setCustom_form_data(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_DATA)));
            custom_form_ap.setAp_code(cursor.getInt(cursor.getColumnIndex(AP_CODE)));
            custom_form_ap.setAp_description(cursor.getString(cursor.getColumnIndex(AP_DESCRIPTION)));
            custom_form_ap.setAp_status(cursor.getString(cursor.getColumnIndex(AP_STATUS)));
            if (cursor.isNull(cursor.getColumnIndex(AP_COMMENTS))) {
                custom_form_ap.setAp_comments(null);
            } else {
                custom_form_ap.setAp_comments(cursor.getString(cursor.getColumnIndex(AP_COMMENTS)));
            }
            if (cursor.isNull(cursor.getColumnIndex(AP_WHAT))) {
                custom_form_ap.setAp_what(null);
            } else {
                custom_form_ap.setAp_what(cursor.getString(cursor.getColumnIndex(AP_WHAT)));
            }
            if (cursor.isNull(cursor.getColumnIndex(AP_WHERE))) {
                custom_form_ap.setAp_where(null);
            } else {
                custom_form_ap.setAp_where(cursor.getString(cursor.getColumnIndex(AP_WHERE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(AP_WHY))) {
                custom_form_ap.setAp_why(null);
            } else {
                custom_form_ap.setAp_why(cursor.getString(cursor.getColumnIndex(AP_WHY)));
            }
            if (cursor.isNull(cursor.getColumnIndex(AP_WHO))) {
                custom_form_ap.setAp_who(null);
            } else {
                custom_form_ap.setAp_who(cursor.getInt(cursor.getColumnIndex(AP_WHO)));
            }
            if (cursor.isNull(cursor.getColumnIndex(AP_HOW))) {
                custom_form_ap.setAp_how(null);
            } else {
                custom_form_ap.setAp_how(cursor.getString(cursor.getColumnIndex(AP_HOW)));
            }
            if (cursor.isNull(cursor.getColumnIndex(AP_HOW_MUCH))) {
                custom_form_ap.setAp_how_much(null);
            } else {
                custom_form_ap.setAp_how_much(cursor.getDouble(cursor.getColumnIndex(AP_HOW_MUCH)));
            }
            if (cursor.isNull(cursor.getColumnIndex(AP_WHEN))) {
                custom_form_ap.setAp_when(null);
            } else {
                custom_form_ap.setAp_when(cursor.getString(cursor.getColumnIndex(AP_WHEN)));
            }
            if (cursor.isNull(cursor.getColumnIndex(DEPARTMENT_CODE))) {
                custom_form_ap.setDepartment_code(null);
            } else {
                custom_form_ap.setDepartment_code(cursor.getInt(cursor.getColumnIndex(DEPARTMENT_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ROOM_CODE))) {
                custom_form_ap.setRoom_code(null);
            } else {
                custom_form_ap.setRoom_code(cursor.getString(cursor.getColumnIndex(ROOM_CODE)));
            }
            custom_form_ap.setAp_scn(cursor.getInt(cursor.getColumnIndex(AP_SCN)));
            custom_form_ap.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
            custom_form_ap.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            custom_form_ap.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            custom_form_ap.setSerial_code(cursor.getInt(cursor.getColumnIndex(SERIAL_CODE)));
            custom_form_ap.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            custom_form_ap.setSync_required(cursor.getInt(cursor.getColumnIndex(SYNC_REQUIRED)));
            custom_form_ap.setUpload_required(cursor.getInt(cursor.getColumnIndex(UPLOAD_REQUIRED)));
            //
            return custom_form_ap;
        }
    }

    private class GE_Custom_Form_ApToContentValuesMapper implements Mapper<GE_Custom_Form_Ap, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Ap custom_form_ap) {
            ContentValues contentValues = new ContentValues();
            //
            if (custom_form_ap.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, custom_form_ap.getCustomer_code());
            }
            if (custom_form_ap.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, custom_form_ap.getCustom_form_type());
            }
            if (custom_form_ap.getCustom_form_type_desc() != null) {
                contentValues.put(CUSTOM_FORM_TYPE_DESC, custom_form_ap.getCustom_form_type_desc());
            }
            if (custom_form_ap.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE, custom_form_ap.getCustom_form_code());
            }
            if (custom_form_ap.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION, custom_form_ap.getCustom_form_version());
            }
            if (custom_form_ap.getCustom_form_desc() != null) {
                contentValues.put(CUSTOM_FORM_DESC, custom_form_ap.getCustom_form_desc());
            }
            if (custom_form_ap.getCustom_form_data() > -1) {
                contentValues.put(CUSTOM_FORM_DATA, custom_form_ap.getCustom_form_data());
            }
            if (custom_form_ap.getAp_code() > -1) {
                contentValues.put(AP_CODE, custom_form_ap.getAp_code());
            }
            if (custom_form_ap.getAp_description() != null) {
                contentValues.put(AP_DESCRIPTION, custom_form_ap.getAp_description());
            }
            if (custom_form_ap.getAp_status() != null) {
                contentValues.put(AP_STATUS, custom_form_ap.getAp_status());
            }
            contentValues.put(AP_COMMENTS, custom_form_ap.getAp_comments());
            contentValues.put(AP_WHAT, custom_form_ap.getAp_what());
            contentValues.put(AP_WHERE, custom_form_ap.getAp_where());
            contentValues.put(AP_WHY, custom_form_ap.getAp_why());
            contentValues.put(AP_WHO, custom_form_ap.getAp_who());
            contentValues.put(AP_HOW, custom_form_ap.getAp_how());
            contentValues.put(AP_HOW_MUCH, custom_form_ap.getAp_how_much());
            contentValues.put(AP_WHEN, custom_form_ap.getAp_when());
            contentValues.put(DEPARTMENT_CODE, custom_form_ap.getDepartment_code());
            contentValues.put(ROOM_CODE, custom_form_ap.getRoom_code());
            if (custom_form_ap.getAp_scn() > -1) {
                contentValues.put(AP_SCN, custom_form_ap.getAp_scn());
            }
            if (custom_form_ap.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, custom_form_ap.getProduct_code());
            }
            if (custom_form_ap.getProduct_id() != null) {
                contentValues.put(PRODUCT_ID, custom_form_ap.getProduct_id());
            }
            if (custom_form_ap.getProduct_desc() != null) {
                contentValues.put(PRODUCT_DESC, custom_form_ap.getProduct_desc());
            }
            if (custom_form_ap.getSerial_code() > -1) {
                contentValues.put(SERIAL_CODE, custom_form_ap.getSerial_code());
            }
            if (custom_form_ap.getSerial_id() != null) {
                contentValues.put(SERIAL_ID, custom_form_ap.getSerial_id());
            }
            if (custom_form_ap.getSync_required() > -1) {
                contentValues.put(SYNC_REQUIRED, custom_form_ap.getSync_required());
            }
            if (custom_form_ap.getUpload_required() > -1) {
                contentValues.put(UPLOAD_REQUIRED, custom_form_ap.getUpload_required());
            }
            //
            return contentValues;
        }
    }
}
