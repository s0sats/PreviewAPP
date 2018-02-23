package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Department;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 23/02/2018.
 */

public class MD_DepartmentDao extends BaseDao implements Dao<MD_Department> {

    private final Mapper<MD_Department, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Department> toMD_DepartmentMapper;

    public static final String TABLE = "md_departments";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String DEPARTMENT_CODE = "department_code";
    public static final String DEPARTMENT_ID = "department_id";
    public static final String DEPARTMENT_DESC = "department_desc";

    public static String[] columns = {CUSTOMER_CODE, DEPARTMENT_CODE, DEPARTMENT_ID, DEPARTMENT_DESC};

    public MD_DepartmentDao(Context context) {
        super(  context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM,
                Constant.DB_MODE_MULTI
        );
        //
        this.toContentValuesMapper = new MD_DepartmentToContentValuesMapper();
        this.toMD_DepartmentMapper = new CursorToMD_DepartmentMapper();
    }

    @Override
    public void addUpdate(MD_Department md_department) {
        try {
            if (db.insert(TABLE, null, toContentValuesMapper.map(md_department)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_department.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(DEPARTMENT_CODE).append(" = '").append(String.valueOf(md_department.getDepartment_code())).append("'");
                db.update(TABLE, toContentValuesMapper.map(md_department), sbWhere.toString(), null);
            }
        } catch (Exception e) {
        } finally {
        }
        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Department> md_departments, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Department md_department : md_departments) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_department)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_department.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(DEPARTMENT_CODE).append(" = '").append(String.valueOf(md_department.getDepartment_code())).append("'");
                    db.update(TABLE, toContentValuesMapper.map(md_department), sbWhere.toString(), null);
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
    public MD_Department getByString(String sQuery) {
        MD_Department md_department = null;
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                md_department = toMD_DepartmentMapper.map(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_department;
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
    public List<MD_Department> query(String sQuery) {
        List<MD_Department> md_departments = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Department uAux = toMD_DepartmentMapper.map(cursor);
                md_departments.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();
        return md_departments;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_departments  = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_departments.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
            String st = e.toString();
        } finally {
        }

        closeDB();

        return md_departments;
    }

    private class CursorToMD_DepartmentMapper implements Mapper<Cursor,MD_Department> {
        @Override
        public MD_Department map(Cursor cursor) {
            MD_Department md_department = new MD_Department();
            //
            md_department.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_department.setDepartment_code(cursor.getInt(cursor.getColumnIndex(DEPARTMENT_CODE)));
            md_department.setDepartment_id(cursor.getString(cursor.getColumnIndex(DEPARTMENT_ID)));
            md_department.setDepartment_desc(cursor.getString(cursor.getColumnIndex(DEPARTMENT_DESC)));
            //
            return md_department;
        }
    }

    private class MD_DepartmentToContentValuesMapper implements Mapper<MD_Department,ContentValues> {
        @Override
        public ContentValues map(MD_Department md_department) {
            ContentValues contentValues = new ContentValues();
            //
            if(md_department.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,md_department.getCustomer_code());
            }
            if(md_department.getDepartment_code() > -1){
                contentValues.put(DEPARTMENT_CODE,md_department.getDepartment_code());
            }
            if(md_department.getDepartment_id() != null){
                contentValues.put(DEPARTMENT_ID,md_department.getDepartment_id());
            }
            if(md_department.getDepartment_desc() != null){
                contentValues.put(DEPARTMENT_DESC,md_department.getDepartment_desc());
            }
            return contentValues;
        }
    }
}
