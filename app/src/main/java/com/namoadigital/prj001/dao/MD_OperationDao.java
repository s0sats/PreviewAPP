package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 16/01/2017.
 */

public class MD_OperationDao extends BaseDao implements Dao<MD_Operation> {
    private final Mapper<MD_Operation, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Operation> toMD_OperationMapper;

    public static final String TABLE = "md_operations";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String OPERATION_CODE = "operation_code";
    public static final String OPERATION_DESC = "operation_desc";
    public static final String ACTIVE = "active";
    public static final String ALIAS_SERVICE_OPER = "alias_service_oper";
    public static final String ALIAS_SERVICE_COM = "alias_service_com";

    private String[] columns = {CUSTOMER_CODE, OPERATION_CODE, OPERATION_DESC,ACTIVE, ALIAS_SERVICE_OPER, ALIAS_SERVICE_COM};

    public MD_OperationDao(Context context,String DB_NAME, int DB_VERSION) {
        //Ultimo parametro refrece se a tabela fica no banco principal
        //ou no banco por customer.
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        this.toContentValuesMapper = new MD_OperationToContentValuesMapper();
        this.toMD_OperationMapper = new CursorMD_OperationMapper();
    }

    @Override
    public void addUpdate(MD_Operation md_operation) {
        openDB();
        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_operation)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_operation.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(OPERATION_CODE).append(" = '").append(String.valueOf(md_operation.getOperation_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(ACTIVE).append(" = '").append(String.valueOf(1)).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_operation), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Operation> md_operations, boolean status) {
        openDB();
        //
        try {
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Operation md_operation : md_operations) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_operation)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_operation.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(OPERATION_CODE).append(" = '").append(String.valueOf(md_operation.getOperation_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(ACTIVE).append(" = '").append(String.valueOf(1)).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_operation), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
        closeDB();
    }

    @Override
    public void addUpdate(String s_query) {
        openDB();
        //
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
        //
        try {
            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }
        closeDB();
    }

    @Override
    public MD_Operation getByString(String s_query) {
        MD_Operation md_operation = null;
        openDB();
        //
        try {
            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                md_operation = toMD_OperationMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }
        closeDB();

        return md_operation;
    }

    @Override
    public List<MD_Operation> query(String s_query) {
        List<MD_Operation> md_operations = new ArrayList<>();
        openDB();

        try {
            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                MD_Operation uAux = toMD_OperationMapper.map(cursor);
                md_operations.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }
        closeDB();
        return md_operations;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> md_operations = new ArrayList<>();
        openDB();

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                md_operations.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }
        closeDB();
        return md_operations;
    }

    private class CursorMD_OperationMapper implements Mapper<Cursor, MD_Operation> {
        @Override
        public MD_Operation map(Cursor cursor) {
            MD_Operation md_operation = new MD_Operation();

            md_operation.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_operation.setOperation_code(cursor.getLong(cursor.getColumnIndex(OPERATION_CODE)));
            md_operation.setOperation_desc(cursor.getString(cursor.getColumnIndex(OPERATION_DESC)));
            md_operation.setAlias_service_oper(cursor.getInt(cursor.getColumnIndex(ALIAS_SERVICE_OPER)));
            md_operation.setAlias_service_com(cursor.getInt(cursor.getColumnIndex(ALIAS_SERVICE_COM)));
            
            return md_operation;
        }
    }

    private class MD_OperationToContentValuesMapper implements Mapper<MD_Operation, ContentValues> {
        @Override
        public ContentValues map(MD_Operation md_operation) {
            ContentValues contentValues = new ContentValues();

            if (md_operation.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_operation.getCustomer_code());
            }
            if (md_operation.getOperation_code() > -1) {
                contentValues.put(OPERATION_CODE, md_operation.getOperation_code());
            }
            if (md_operation.getOperation_desc() != null) {
                contentValues.put(OPERATION_DESC, md_operation.getOperation_desc());
            }
            if (md_operation.getAlias_service_oper() > -1) {
                contentValues.put(ALIAS_SERVICE_OPER, md_operation.getAlias_service_oper());
            }
            if (md_operation.getAlias_service_com() > -1) {
                contentValues.put(ALIAS_SERVICE_COM, md_operation.getAlias_service_com());
            }

            return contentValues;
        }
    }    
    
}
