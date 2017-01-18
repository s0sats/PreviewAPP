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
import com.namoadigital.prj001.model.MD_Site;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 16/01/2017.
 */

public class MD_SiteDao implements Dao<MD_Site> {
    private final SQLiteOpenHelper openHelper;
    private final Mapper<MD_Site, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Site> toMD_SiteMapper;

    public static final String TABLE = "md_sites";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SITE_CODE = "site_code";
    public static final String SITE_ID = "site_id";
    public static final String SITE_DESC = "site_desc";
    public static final String ACTIVE = "active";

    public MD_SiteDao(Context context) {
        this.openHelper = DatabaseHelper.getInstance(context);
        this.toContentValuesMapper = new MD_OperationToContentValuesMapper();
        this.toMD_SiteMapper = new CursorMD_OperationMapper();
    }

    @Override
    public void addUpdate(MD_Site md_site) {
        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_site)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_site.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(md_site.getSite_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(ACTIVE).append(" = '").append(String.valueOf(1)).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_site), sbWhere.toString(), null);
            }
            
        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }


    }

    @Override
    public void addUpdate(Iterable<MD_Site> md_sites, boolean status) {

        SQLiteDatabase db = null;

        try {

            db = openHelper.getWritableDatabase();
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Site md_site : md_sites) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_site)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_site.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(md_site.getSite_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(ACTIVE).append(" = '").append(String.valueOf(1)).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_site), sbWhere.toString(), null);
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
    public MD_Site getByString(String s_query) {
        MD_Site md_site = null;
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                md_site = toMD_SiteMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return md_site;
    }

    @Override
    public List<MD_Site> query(String s_query) {
        List<MD_Site> md_sites = new ArrayList<>();
        SQLiteDatabase db = null;

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                MD_Site uAux = toMD_SiteMapper.map(cursor);
                md_sites.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return md_sites;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> md_sites = new ArrayList<>();
        SQLiteDatabase db = null;

        String s_query_div[] = s_query.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            db = openHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(s_query_div[0], null);


            while (cursor.moveToNext()) {
                md_sites.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
            if (db != null) {
                db.close();
            }
        }

        return md_sites;
    }

    private class CursorMD_OperationMapper implements Mapper<Cursor,MD_Site> {
        @Override
        public MD_Site map(Cursor cursor) {
            MD_Site md_site = new MD_Site();

            md_site.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_site.setSite_code(cursor.getLong(cursor.getColumnIndex(SITE_CODE)));
            md_site.setSite_id(cursor.getString(cursor.getColumnIndex(SITE_ID)));
            md_site.setSite_desc(cursor.getString(cursor.getColumnIndex(SITE_DESC)));
            return md_site;
        }

    }

    private class MD_OperationToContentValuesMapper implements Mapper<MD_Site,ContentValues> {
        @Override
        public ContentValues map(MD_Site md_site) {
            ContentValues contentValues = new ContentValues();

            if (md_site.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_site.getCustomer_code());
            }
            if (md_site.getSite_code() > -1) {
                contentValues.put(SITE_CODE, md_site.getSite_code());
            }
            if (md_site.getSite_id() != null) {
                contentValues.put(SITE_ID, md_site.getSite_id());
            }
            if (md_site.getSite_desc() != null) {
                contentValues.put(SITE_DESC, md_site.getSite_desc());
            }
            return contentValues;

        }        
    }
}
