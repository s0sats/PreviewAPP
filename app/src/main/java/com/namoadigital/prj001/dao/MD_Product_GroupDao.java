package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product_Group;
import com.namoadigital.prj001.util.Constant;

import java.util.List;


/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class MD_Product_GroupDao extends BaseDao implements Dao<MD_Product_Group> {

    private final Mapper<MD_Product_Group, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product_Group> toMD_ProductGroupMapper;

    public static final String TABLE = "md_product_group";
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
    public void addUpdate(MD_Product_Group item) {

    }

    @Override
    public void addUpdate(Iterable<MD_Product_Group> items, boolean status) {

    }

    @Override
    public void addUpdate(String sQuery) {

    }

    @Override
    public void remove(String sQuery) {

    }

    @Override
    public MD_Product_Group getByString(String sQuery) {
        return null;
    }

    @Override
    public List<MD_Product_Group> query(String sQuery) {
        return null;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        return null;
    }


    private class CursorMD_Product_GroupMapper implements Mapper<Cursor, MD_Product_Group> {
        @Override
        public MD_Product_Group map(Cursor cursor) {
            MD_Product_Group md_product_group = new MD_Product_Group();

            md_product_group.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product_group.setGroup_code(cursor.getLong(cursor.getColumnIndex(GROUP_CODE)));
            md_product_group.setRecursive_code(cursor.getLong(cursor.getColumnIndex(RECURSIVE_CODE)));
            md_product_group.setRecursive_code_father(cursor.getLong(cursor.getColumnIndex(RECURSIVE_CODE_FATHER)));
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
            if (md_product_group.getRecursive_code_father() > -1) {
                contentValues.put(RECURSIVE_CODE_FATHER, md_product_group.getRecursive_code_father());
            }

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
