package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form_Operation;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 11/05/2017.
 */

public class GE_Custom_Form_OperationDao extends BaseDao implements Dao<GE_Custom_Form_Operation> {
    private final Mapper<GE_Custom_Form_Operation, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Operation> toGE_Custom_Form_Operation;

    public static final String TABLE = "ge_custom_form_operations";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String OPERATION_CODE = "operation_code";

    public GE_Custom_Form_OperationDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        this.toContentValuesMapper = new GE_Custom_Form_OperationToContentValuesMapper();
        this.toGE_Custom_Form_Operation = new CursorGE_Custom_Form_Operation();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Operation custom_form_operation) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_operation)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_operation.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_operation.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_operation.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_operation.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(OPERATION_CODE).append(" = '").append(String.valueOf(custom_form_operation.getOperation_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(custom_form_operation), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Operation> custom_form_operations, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Operation custom_form_operation : custom_form_operations) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_operation)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_operation.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_operation.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_operation.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_operation.getCustom_form_version())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(OPERATION_CODE).append(" = '").append(String.valueOf(custom_form_operation.getOperation_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form_operation), sbWhere.toString(), null);
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
    public GE_Custom_Form_Operation getByString(String sQuery) {
        GE_Custom_Form_Operation custom_form_operation = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                custom_form_operation = toGE_Custom_Form_Operation.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();


        return custom_form_operation;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        HMAux hmAux =  null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                hmAux = CursorToHMAuxMapper.mapN(cursor);
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
    public List<GE_Custom_Form_Operation> query(String sQuery) {

        List<GE_Custom_Form_Operation> custom_form_operations = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Operation uAux = toGE_Custom_Form_Operation.map(cursor);
                custom_form_operations.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return custom_form_operations;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> custom_form_operations = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                custom_form_operations.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return custom_form_operations;
    }

    private class GE_Custom_Form_OperationToContentValuesMapper implements Mapper<GE_Custom_Form_Operation,ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Operation custom_form_operation) {
            ContentValues contentValues =  new ContentValues();

            if(custom_form_operation.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,custom_form_operation.getCustomer_code());
            }

            if(custom_form_operation.getCustom_form_type() > -1){
                contentValues.put(CUSTOM_FORM_TYPE,custom_form_operation.getCustom_form_type());
            }

            if(custom_form_operation.getCustom_form_code() > -1){
                contentValues.put(CUSTOM_FORM_CODE,custom_form_operation.getCustom_form_code());
            }

            if(custom_form_operation.getCustom_form_version() > -1){
                contentValues.put(CUSTOM_FORM_VERSION,custom_form_operation.getCustom_form_version());
            }

            if(custom_form_operation.getOperation_code() > -1){
                contentValues.put(OPERATION_CODE,custom_form_operation.getOperation_code());
            }

            return contentValues;
        }
    }

    private class CursorGE_Custom_Form_Operation implements Mapper<Cursor,GE_Custom_Form_Operation> {
        @Override
        public GE_Custom_Form_Operation map(Cursor cursor) {

            GE_Custom_Form_Operation formOperation = new GE_Custom_Form_Operation();

            formOperation.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            formOperation.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            formOperation.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            formOperation.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            formOperation.setOperation_code(cursor.getLong(cursor.getColumnIndex(OPERATION_CODE)));

            return formOperation;
        }
    }
}
