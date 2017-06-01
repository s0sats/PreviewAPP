package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product_Group;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class MD_Product_GroupDao extends BaseDao implements Dao<MD_Product_Group> {

    private final Mapper<MD_Product_Group, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product_Group> toMD_ProductGroupMapper;

    public static final String TABLE = "md_product_groups";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String GROUP_CODE = "group_code";
    public static final String RECURSIVE_CODE = "recursive_code";
    public static final String RECURSIVE_CODE_FATHER = "recursive_code_father";
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_DESC = "group_desc";
    private String[] columns = {CUSTOMER_CODE, GROUP_CODE, RECURSIVE_CODE, RECURSIVE_CODE_FATHER, GROUP_ID, GROUP_DESC};

    public MD_Product_GroupDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new MD_Product_GroupDao.MD_ProductGroupToContentValuesMapper();
        this.toMD_ProductGroupMapper = new MD_Product_GroupDao.CursorMD_Product_GroupMapper();
    }

    @Override
    public void addUpdate(MD_Product_Group md_product_group) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_group)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_group.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(GROUP_CODE).append(" = '").append(String.valueOf(md_product_group.getGroup_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_product_group), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_Product_Group> md_product_groups, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Product_Group md_product_group : md_product_groups) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_group)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_group.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(GROUP_CODE).append(" = '").append(String.valueOf(md_product_group.getGroup_code())).append("'");
                    sbWhere.append(" and ");

                    db.update(TABLE, toContentValuesMapper.map(md_product_group), sbWhere.toString(), null);
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
    public MD_Product_Group getByString(String sQuery) {

        MD_Product_Group md_product_group = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_product_group = toMD_ProductGroupMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return md_product_group;
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

        } finally {
        }

        closeDB();

        return hmAux;
    }


    @Override
    public List<MD_Product_Group> query(String sQuery) {
        List<MD_Product_Group> md_product_groups = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Product_Group uAux = toMD_ProductGroupMapper.map(cursor);
                md_product_groups.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return md_product_groups;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_product_groups = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_product_groups.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

            String results = e.toString();

        } finally {
        }

        closeDB();

        return md_product_groups;
    }

    private class CursorMD_Product_GroupMapper implements Mapper<Cursor, MD_Product_Group> {
        @Override
        public MD_Product_Group map(Cursor cursor) {
            MD_Product_Group md_product_group = new MD_Product_Group();

            md_product_group.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product_group.setGroup_code(cursor.getLong(cursor.getColumnIndex(GROUP_CODE)));
            md_product_group.setRecursive_code(cursor.getLong(cursor.getColumnIndex(RECURSIVE_CODE)));

            if (cursor.isNull(cursor.getColumnIndex(RECURSIVE_CODE_FATHER))) {
                md_product_group.setRecursive_code_father(null);
            } else {
                md_product_group.setRecursive_code_father(cursor.getLong(cursor.getColumnIndex(RECURSIVE_CODE_FATHER)));
            }

            md_product_group.setGroup_id(cursor.getString(cursor.getColumnIndex(GROUP_ID)));
            md_product_group.setGroup_desc(cursor.getString(cursor.getColumnIndex(GROUP_DESC)));

            return md_product_group;
        }
    }

    public class MD_ProductGroupToContentValuesMapper implements Mapper<MD_Product_Group,ContentValues> {
        @Override
        public ContentValues map(MD_Product_Group md_product_group) {
            ContentValues contentValues = new ContentValues();

            if (md_product_group.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_product_group.getCustomer_code());
            }
            if (md_product_group.getGroup_code() > -1) {
                contentValues.put(GROUP_CODE, md_product_group.getGroup_code());
            }
            if (md_product_group.getRecursive_code() > -1) {
                contentValues.put(RECURSIVE_CODE, md_product_group.getRecursive_code());
            }

            contentValues.put(RECURSIVE_CODE_FATHER, md_product_group.getRecursive_code_father());

            //if (md_product_group.getRecursive_code_father() > -1) {
            //    contentValues.put(RECURSIVE_CODE_FATHER, md_product_group.getRecursive_code_father());
            //}

            if (md_product_group.getGroup_id() != null) {
                contentValues.put(GROUP_ID, md_product_group.getGroup_id());
            }
            if (md_product_group.getGroup_desc() != null) {
                contentValues.put(GROUP_DESC, md_product_group.getGroup_desc());
            }

            return contentValues;
        }
    }
}
