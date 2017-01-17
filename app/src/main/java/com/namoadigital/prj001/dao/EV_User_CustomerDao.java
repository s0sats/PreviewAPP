package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.DatabaseHelper;
import com.namoadigital.prj001.database.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.EV_User_Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_User_CustomerDao implements Dao<EV_User_Customer> {
    private final SQLiteOpenHelper openHelper;
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
    private String[] columns = {USER_CODE, CUSTOMER_CODE, CUSTOMER_NAME, TRANSLATE_CODE, LANGUAGE_CODE, TRANSLATE_DESC, NLS_DATE_FORMAT, KEYUSER};

    public EV_User_CustomerDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        //
        this.toContentValuesMapper = new EV_CustomerToContentValuesMapper();
        this.toEV_User_CustomerMapper = new CursorToEV_CustomerMapper();
    }

    @Override
    public void addUpdate(EV_User_Customer customer) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(customer)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(USER_CODE).append(" = '").append(String.valueOf(customer.getUser_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(customer.getCustomer_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(customer), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(Iterable<EV_User_Customer> customers, boolean status) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_User_Customer customer : customers) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(customer)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(USER_CODE).append(" = '").append(String.valueOf(customer.getUser_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(customer.getCustomer_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(customer), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();

            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void addUpdate(String s_query) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public void remove(String s_query) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public EV_User_Customer getByString(String s_query) {
        EV_User_Customer customer = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                customer = toEV_User_CustomerMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return customer;
    }

    @Override
    public List<EV_User_Customer> query(String s_query) {
        List<EV_User_Customer> customers = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                EV_User_Customer uAux = toEV_User_CustomerMapper.map(cursor);
                customers.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return customers;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> customers = new ArrayList<>();
        SQLiteDatabase db = null;

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                customers.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return customers;
    }

    private class CursorToEV_CustomerMapper implements Mapper<Cursor, EV_User_Customer> {
        @Override
        public EV_User_Customer map(Cursor cursor) {
            EV_User_Customer ev_user_customer = new EV_User_Customer();

            ev_user_customer.setUser_code(cursor.getLong(cursor.getColumnIndex(USER_CODE)));
            ev_user_customer.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            ev_user_customer.setCustomer_name(cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME)));
            ev_user_customer.setTranslate_code(cursor.getInt(cursor.getColumnIndex(TRANSLATE_CODE)));
            ev_user_customer.setLanguage_code(cursor.getString(cursor.getColumnIndex(LANGUAGE_CODE)));
            ev_user_customer.setNls_date_format(cursor.getString(cursor.getColumnIndex(NLS_DATE_FORMAT)));
            ev_user_customer.setKeyuser(cursor.getInt(cursor.getColumnIndex(KEYUSER)));

            return ev_user_customer;
        }
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

            return contentValues;
        }
    }
}
