package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_All_Product_Group;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class MD_All_Product_GroupDao extends BaseDao implements Dao<MD_All_Product_Group> {

    private final Mapper<MD_All_Product_Group, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_All_Product_Group> toMD_ProductGroupMapper;

    public static final String TABLE = "md_all_product_groups";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String GROUP_CODE = "group_code";
    public static final String RECURSIVE_CODE = "recursive_code";
    public static final String RECURSIVE_CODE_FATHER = "recursive_code_father";
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_DESC = "group_desc";
    public static final String SPARE_PART = "spare_part";
    private String[] columns = {CUSTOMER_CODE, GROUP_CODE, RECURSIVE_CODE, RECURSIVE_CODE_FATHER, GROUP_ID, GROUP_DESC, SPARE_PART};

    public MD_All_Product_GroupDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new MD_All_Product_GroupDao.MD_ProductGroupToContentValuesMapper();
        this.toMD_ProductGroupMapper = new MD_All_Product_GroupDao.CursorMD_All_Product_GroupMapper();
    }

    public MD_All_Product_GroupDao(Context context) {
        super(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM,
                Constant.DB_MODE_MULTI
        );
        //
        this.toContentValuesMapper = new MD_All_Product_GroupDao.MD_ProductGroupToContentValuesMapper();
        this.toMD_ProductGroupMapper = new MD_All_Product_GroupDao.CursorMD_All_Product_GroupMapper();
    }

    @Override
    public void addUpdate(MD_All_Product_Group md_all_product_group) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_all_product_group)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_all_product_group.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(GROUP_CODE).append(" = '").append(String.valueOf(md_all_product_group.getGroup_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_all_product_group), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_All_Product_Group> md_all_product_groups, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_All_Product_Group md_all_product_group : md_all_product_groups) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_all_product_group)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_all_product_group.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(GROUP_CODE).append(" = '").append(String.valueOf(md_all_product_group.getGroup_code())).append("'");
                    sbWhere.append(" and ");

                    db.update(TABLE, toContentValuesMapper.map(md_all_product_group), sbWhere.toString(), null);
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
    public MD_All_Product_Group getByString(String sQuery) {

        MD_All_Product_Group md_all_product_group = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_all_product_group = toMD_ProductGroupMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_all_product_group;
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
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return hmAux;
    }


    @Override
    public List<MD_All_Product_Group> query(String sQuery) {
        List<MD_All_Product_Group> md_all_product_groups = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_All_Product_Group uAux = toMD_ProductGroupMapper.map(cursor);
                md_all_product_groups.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_all_product_groups;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_all_product_groups = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_all_product_groups.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
            String results = e.toString();

        } finally {
        }

        closeDB();

        return md_all_product_groups;
    }

    private class CursorMD_All_Product_GroupMapper implements Mapper<Cursor, MD_All_Product_Group> {
        @Override
        public MD_All_Product_Group map(Cursor cursor) {
            MD_All_Product_Group md_all_product_group = new MD_All_Product_Group();

            md_all_product_group.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_all_product_group.setGroup_code(cursor.getLong(cursor.getColumnIndex(GROUP_CODE)));
            md_all_product_group.setRecursive_code(cursor.getLong(cursor.getColumnIndex(RECURSIVE_CODE)));

            if (cursor.isNull(cursor.getColumnIndex(RECURSIVE_CODE_FATHER))) {
                md_all_product_group.setRecursive_code_father(null);
            } else {
                md_all_product_group.setRecursive_code_father(cursor.getLong(cursor.getColumnIndex(RECURSIVE_CODE_FATHER)));
            }

            md_all_product_group.setGroup_id(cursor.getString(cursor.getColumnIndex(GROUP_ID)));
            md_all_product_group.setGroup_desc(cursor.getString(cursor.getColumnIndex(GROUP_DESC)));
            md_all_product_group.setSpare_part(cursor.getInt(cursor.getColumnIndex(SPARE_PART)));
            return md_all_product_group;
        }
    }

    public class MD_ProductGroupToContentValuesMapper implements Mapper<MD_All_Product_Group,ContentValues> {
        @Override
        public ContentValues map(MD_All_Product_Group md_all_product_group) {
            ContentValues contentValues = new ContentValues();

            if (md_all_product_group.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_all_product_group.getCustomer_code());
            }
            if (md_all_product_group.getGroup_code() > -1) {
                contentValues.put(GROUP_CODE, md_all_product_group.getGroup_code());
            }
            if (md_all_product_group.getRecursive_code() > -1) {
                contentValues.put(RECURSIVE_CODE, md_all_product_group.getRecursive_code());
            }

            contentValues.put(RECURSIVE_CODE_FATHER, md_all_product_group.getRecursive_code_father());

            //if (md_all_product_group.getRecursive_code_father() > -1) {
            //    contentValues.put(RECURSIVE_CODE_FATHER, md_all_product_group.getRecursive_code_father());
            //}

            if (md_all_product_group.getGroup_id() != null) {
                contentValues.put(GROUP_ID, md_all_product_group.getGroup_id());
            }
            if (md_all_product_group.getGroup_desc() != null) {
                contentValues.put(GROUP_DESC, md_all_product_group.getGroup_desc());
            }
            if (md_all_product_group.getSpare_part() > -1) {
                contentValues.put(SPARE_PART, md_all_product_group.getSpare_part());
            }

            return contentValues;
        }
    }
}
