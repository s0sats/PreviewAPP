package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.DatabaseHelper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.EV_Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class EV_CustomerDao implements Dao<EV_Customer> {
    private final SQLiteOpenHelper openHelper;
    private final Mapper<EV_Customer, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_Customer> toEV_CustomerMapper;

    public static final String TABLE = "ev_customers";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String DATE_DB_CUSTOMER = "date_db_customer";
    private String[] columns = {CUSTOMER_CODE, DATE_DB_CUSTOMER};

    public EV_CustomerDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        //
        this.toContentValuesMapper = new EV_CustomerToContentValuesMapper();
        this.toEV_CustomerMapper = new CursorToEV_CustomerMapper();
    }

    @Override
    public void addUpdate(EV_Customer customer) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(customer)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
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
    public void addUpdate(Iterable<EV_Customer> customers, boolean status) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_Customer customer : customers) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(customer)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
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

//    /**
//     * Not Applicable. EV_User String Query
//     *
//     * @param id
//     */
//    @Override
//    public EV_Customer getById(long id) {
//        EV_Customer customer = null;
//        SQLiteDatabase db = null;
//
//        try {
//
//            db = openHelper.getReadableDatabase();
//            StringBuilder sbWhere = new StringBuilder();
//            sbWhere.append(CUSTOMER_CODE).append(" = '").append(id).append("'");
//
//            Cursor cursor = db.query(TABLE, columns, sbWhere.toString(), null, null, null, null);
//
//            while (cursor.moveToNext()) {
//                customer = toEV_CustomerMapper.map(cursor);
//
//            }
//
//            cursor.close();
//        } catch (Exception e) {
//
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        return customer;
//    }

    @Override
    public EV_Customer getByString(String s_query) {
        EV_Customer customer = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                customer = toEV_CustomerMapper.map(cursor);
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
    public List<EV_Customer> query(String s_query) {
        List<EV_Customer> customers = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                EV_Customer uAux = toEV_CustomerMapper.map(cursor);
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

    private class CursorToEV_CustomerMapper implements Mapper<Cursor, EV_Customer> {
        @Override
        public EV_Customer map(Cursor cursor) {
            EV_Customer ev_customer = new EV_Customer();

            ev_customer.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            ev_customer.setDate_db_customer(cursor.getString(cursor.getColumnIndex(DATE_DB_CUSTOMER)));

            return ev_customer;
        }
    }

    private class EV_CustomerToContentValuesMapper implements Mapper<EV_Customer, ContentValues> {
        @Override
        public ContentValues map(EV_Customer ev_customer) {
            ContentValues contentValues = new ContentValues();

            if (ev_customer.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, ev_customer.getCustomer_code());
            }
            if (ev_customer.getDate_db_customer() != null) {
                contentValues.put(DATE_DB_CUSTOMER, ev_customer.getDate_db_customer());
            }

            return contentValues;
        }
    }

}
