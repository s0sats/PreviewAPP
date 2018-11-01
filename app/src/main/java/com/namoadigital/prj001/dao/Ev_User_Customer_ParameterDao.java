package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.ErrorCfg;
import com.namoadigital.prj001.model.Ev_User_Customer_Parameter;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 30/03/2017.
 */

public class Ev_User_Customer_ParameterDao extends BaseDao implements DaoN<Ev_User_Customer_Parameter> {

    private final Mapper<Ev_User_Customer_Parameter, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, Ev_User_Customer_Parameter> toEv_User_Customer_ParameterMapper;

    public static final String TABLE = "ev_user_customer_parameters";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PARAMETER_CODE = "parameter_code";

    public Ev_User_Customer_ParameterDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_SINGLE);

        this.toContentValuesMapper = new Ev_User_Customer_ParameterToContentValuesMapper();
        this.toEv_User_Customer_ParameterMapper = new CursorToEv_User_Customer_ParameterMapper();

    }


    @Override
    public void addUpdate(Ev_User_Customer_Parameter customer_parameter, ErrorCfg mError) {
        openDB();

        try {

            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(customer_parameter.getCustomer_code())).append("'");

            long rows = db.update(TABLE, toContentValuesMapper.map(customer_parameter), sbWhere.toString(), null);

            if (rows == 0) {
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(customer_parameter));
            }

            mError.clearError();

        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        }
        closeDB();
    }

    @Override
    public void addUpdate(List<Ev_User_Customer_Parameter> customer_parameters, boolean status, ErrorCfg mError) {

        openDB();

        try {
            if (!ismIgnoreCounter()) {
                db.beginTransaction();
            }

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (Ev_User_Customer_Parameter customer_parameter : customer_parameters) {

                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(customer_parameter.getCustomer_code())).append("'");

                long rows = db.update(TABLE, toContentValuesMapper.map(customer_parameter), sbWhere.toString(), null);

                if (rows == 0) {
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(customer_parameter));
                }

                mError.clearError();
            }

            if (!ismIgnoreCounter()) {
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            if (!ismIgnoreCounter()) {
                db.endTransaction();
            }
        }

        closeDB();
    }

    @Override
    public void addUpdate(String sQuery, ErrorCfg mError) {
        openDB();

        try {

            db.execSQL(sQuery);
            mError.clearError();

            // Metodo nao confiavel ja que nao garante  a operacao em sequencia. Outro metodo em paralelo pode invalidar o contador
            // long rows = DatabaseUtils.longForQuery(db, "SELECT changes()", null);

        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        //
        closeDB();
    }

    @Override
    public void remove(String sQuery, ErrorCfg mError) {
        openDB();

        try {

            db.execSQL(sQuery);
            mError.clearError();

            // Metodo nao confiavel ja que nao garante  a operacao em sequencia. Outro metodo em paralelo pode invalidar o contador
            // long rows = DatabaseUtils.longForQuery(db, "SELECT changes()", null);

        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        //
        closeDB();
    }

    @Override
    public Ev_User_Customer_Parameter getByString(String sQuery, ErrorCfg mError) {
        Ev_User_Customer_Parameter customer_parameter = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                customer_parameter = toEv_User_Customer_ParameterMapper.map(cursor);
            }

            mError.clearError();
            cursor.close();
        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return customer_parameter;
    }

    @Override
    public HMAux getByStringHM(String sQuery, ErrorCfg mError) {
        HMAux hmAux = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                hmAux = CursorToHMAuxMapper.mapN(cursor);
            }

            mError.clearError();
            cursor.close();
        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return hmAux;
    }

    @Override
    public List<Ev_User_Customer_Parameter> query(String sQuery, ErrorCfg mError) {
        List<Ev_User_Customer_Parameter> customer_parameters = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                Ev_User_Customer_Parameter uAux = toEv_User_Customer_ParameterMapper.map(cursor);
                customer_parameters.add(uAux);
            }

            mError.clearError();
            cursor.close();
        } catch (SQLiteException e) {
            mError.copyError(ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage()));
            //
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return customer_parameters;
    }

    @Override
    public List<HMAux> query_HM(String sQuery, ErrorCfg mError) {

        List<HMAux> customer_parameters = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                customer_parameters.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
            String st = e.toString();
        } finally {
        }

        closeDB();


        return customer_parameters;
    }

    private class Ev_User_Customer_ParameterToContentValuesMapper implements Mapper<Ev_User_Customer_Parameter, ContentValues> {
        @Override
        public ContentValues map(Ev_User_Customer_Parameter ev_user_customer_parameter) {
            ContentValues contentValues = new ContentValues();

            if (ev_user_customer_parameter.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, ev_user_customer_parameter.getCustomer_code());
            }
            if (ev_user_customer_parameter.getParameter_code() != null) {
                contentValues.put(PARAMETER_CODE, ev_user_customer_parameter.getParameter_code());
            }

            return contentValues;
        }

    }

    private class CursorToEv_User_Customer_ParameterMapper implements Mapper<Cursor, Ev_User_Customer_Parameter> {
        @Override
        public Ev_User_Customer_Parameter map(Cursor cursor) {
            Ev_User_Customer_Parameter customerParameter = new Ev_User_Customer_Parameter();

            customerParameter.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            customerParameter.setParameter_code(cursor.getString(cursor.getColumnIndex(PARAMETER_CODE)));

            return customerParameter;
        }
    }


}
