package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.EV_User_Customer;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_User_CustomerDao extends BaseDao implements Dao<EV_User_Customer> {
    private final Mapper<EV_User_Customer, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_User_Customer> toEV_User_CustomerMapper;

    public static final String TABLE = "ev_user_customers";
    public static final String USER_CODE = "user_code";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String TRANSLATE_CODE = "translate_code";
    public static final String LANGUAGE_CODE = "language_code";
    public static final String TRANSLATE_DESC = "translate_desc";
    public static final String NLS_DATE_FORMAT = "nls_date_format";
    public static final String KEYUSER = "keyuser";
    public static final String BLOCKED = "blocked";
    public static final String SESSION_APP = "session_app";
    public static final String PENDING = "pending";
    public static final String LOGO_URL = "logo_url";
    public static final String TRACKING = "tracking";
    public static final String TIMEZONE = "timezone";
    public static final String LICENSE_CONTROL_TYPE = "license_control_type";

    public static String[] columns = {USER_CODE, CUSTOMER_CODE, CUSTOMER_NAME, TRANSLATE_CODE, LANGUAGE_CODE, TRANSLATE_DESC, NLS_DATE_FORMAT, KEYUSER, BLOCKED, SESSION_APP, PENDING, LOGO_URL, TRACKING,TIMEZONE, LICENSE_CONTROL_TYPE};

    public EV_User_CustomerDao(Context context) {
        super(context, Constant.DB_FULL_BASE, Constant.DB_VERSION_BASE, Constant.DB_MODE_SINGLE);

        this.toContentValuesMapper = new EV_CustomerToContentValuesMapper();
        this.toEV_User_CustomerMapper = new CursorToEV_CustomerMapper();
    }

    public EV_User_CustomerDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_SINGLE);

        this.toContentValuesMapper = new EV_CustomerToContentValuesMapper();
        this.toEV_User_CustomerMapper = new CursorToEV_CustomerMapper();
    }

    @Override
    public void addUpdate(EV_User_Customer user_customer) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(user_customer)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(USER_CODE).append(" = '").append(String.valueOf(user_customer.getUser_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(user_customer.getCustomer_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(user_customer), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }
        closeDB();
    }

    @Override
    public void addUpdate(Iterable<EV_User_Customer> userCustomers, boolean status) {
        openDB();

        try {
            if (status) {
                if (!ismIgnoreCounter()) {
                    db.beginTransaction();
                }
                //
                db.delete(TABLE, null, null);
            }

            for (EV_User_Customer userCustomer : userCustomers) {

                if (db.insert(TABLE, null, toContentValuesMapper.map(userCustomer)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(USER_CODE).append(" = '").append(String.valueOf(userCustomer.getUser_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(userCustomer.getCustomer_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(userCustomer), sbWhere.toString(), null);
                }
            }

            if (!ismIgnoreCounter()) {
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            if (!ismIgnoreCounter()) {
                db.endTransaction();
            }
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
        } catch (SQLiteException e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        //
        closeDB();
    }

    @Override
    public EV_User_Customer getByString(String sQuery) {
        EV_User_Customer userCustomer = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                userCustomer = toEV_User_CustomerMapper.map(cursor);
            }
            cursor.close();
        } catch (SQLiteException e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return userCustomer;
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
        } catch (SQLiteException e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return hmAux;
    }


    @Override
    public List<EV_User_Customer> query(String sQuery) {
        List<EV_User_Customer> customers = new ArrayList<>();
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                EV_User_Customer uAux = toEV_User_CustomerMapper.map(cursor);
                customers.add(uAux);
            }
            cursor.close();
        } catch (SQLiteException e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return customers;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> userCustomers = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                userCustomers.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (SQLiteException e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return userCustomers;
    }

    private class EV_CustomerToContentValuesMapper implements Mapper<EV_User_Customer, ContentValues> {
        @Override
        public ContentValues map(EV_User_Customer ev_user_customer) {
            ContentValues contentValues = new ContentValues();

            if (ev_user_customer.getUser_code() > -1) {
                contentValues.put(USER_CODE, ev_user_customer.getUser_code());
            }
            if (ev_user_customer.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, ev_user_customer.getCustomer_code());
            }
            if (ev_user_customer.getCustomer_name() != null) {
                contentValues.put(CUSTOMER_NAME, ev_user_customer.getCustomer_name());
            }
            if (ev_user_customer.getTranslate_code() > -1) {
                contentValues.put(TRANSLATE_CODE, ev_user_customer.getTranslate_code());
            }
            if (ev_user_customer.getLanguage_code() != null) {
                contentValues.put(LANGUAGE_CODE, ev_user_customer.getLanguage_code());
            }
            if (ev_user_customer.getTranslate_desc() != null) {
                contentValues.put(TRANSLATE_DESC, ev_user_customer.getTranslate_desc());
            }
            if (ev_user_customer.getNls_date_format() != null) {
                contentValues.put(NLS_DATE_FORMAT, ev_user_customer.getNls_date_format());
            }
            if (ev_user_customer.getKeyuser() > -1) {
                contentValues.put(KEYUSER, ev_user_customer.getKeyuser());
            }
            if (ev_user_customer.getBlocked() > -1) {
                contentValues.put(BLOCKED, ev_user_customer.getBlocked());
            }
            if (ev_user_customer.getSession_app() != null) {
                contentValues.put(SESSION_APP, ev_user_customer.getSession_app());
            }
            if (ev_user_customer.getPending() > -1) {
                contentValues.put(PENDING, ev_user_customer.getPending());
            }
            if (ev_user_customer.getLogo_url() != null) {
                contentValues.put(LOGO_URL, ev_user_customer.getLogo_url());
            }
            if (ev_user_customer.getTracking() > -1) {
                contentValues.put(TRACKING, ev_user_customer.getTracking());
            }
            if (ev_user_customer.getTimezone() != null) {
                contentValues.put(TIMEZONE, ev_user_customer.getTimezone());
            }
            if (ev_user_customer.getLicense_control_type() != null) {
                contentValues.put(LICENSE_CONTROL_TYPE, ev_user_customer.getLicense_control_type());
            }

            return contentValues;
        }
    }

    private class CursorToEV_CustomerMapper implements Mapper<Cursor, EV_User_Customer> {
        @Override
        public EV_User_Customer map(Cursor cursor) {
            EV_User_Customer ev_user_customer = new EV_User_Customer();

            ev_user_customer.setUser_code(cursor.getLong(cursor.getColumnIndex(USER_CODE)));
            ev_user_customer.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            ev_user_customer.setCustomer_name(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
            ev_user_customer.setTranslate_code(cursor.getInt(cursor.getColumnIndex(TRANSLATE_CODE)));
            ev_user_customer.setTranslate_desc(cursor.getString(cursor.getColumnIndex(TRANSLATE_DESC)));
            ev_user_customer.setLanguage_code(cursor.getString(cursor.getColumnIndex(LANGUAGE_CODE)));
            ev_user_customer.setNls_date_format(cursor.getString(cursor.getColumnIndex(NLS_DATE_FORMAT)));
            ev_user_customer.setKeyuser(cursor.getInt(cursor.getColumnIndex(KEYUSER)));
            ev_user_customer.setBlocked(cursor.getInt(cursor.getColumnIndex(BLOCKED)));
            ev_user_customer.setSession_app(cursor.getString(cursor.getColumnIndex(SESSION_APP)));
            ev_user_customer.setPending(cursor.getInt(cursor.getColumnIndex(PENDING)));
            ev_user_customer.setLogo_url(cursor.getString(cursor.getColumnIndex(LOGO_URL)));
            ev_user_customer.setTracking(cursor.getInt(cursor.getColumnIndex(TRACKING)));
            ev_user_customer.setTimezone(cursor.getString(cursor.getColumnIndex(TIMEZONE)));
            ev_user_customer.setLicense_control_type(cursor.getString(cursor.getColumnIndex(LICENSE_CONTROL_TYPE)));

            return ev_user_customer;
        }
    }
}
