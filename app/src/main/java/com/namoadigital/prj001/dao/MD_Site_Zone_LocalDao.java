package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Site_Zone_Local;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Site_Zone_LocalDao extends BaseDao implements Dao<MD_Site_Zone_Local> {

    private final Mapper<MD_Site_Zone_Local, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Site_Zone_Local> toMD_Site_Zone_LocalMapper;


    public static final String TABLE = "md_site_zone_locals";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SITE_CODE = "site_code";
    public static final String ZONE_CODE = "zone_code";
    public static final String LOCAL_CODE = "local_code";
    public static final String LOCAL_ID = "local_id";
    public static final String CAPACITY = "capacity";


    private String[] columns = {CUSTOMER_CODE, SITE_CODE, ZONE_CODE, LOCAL_CODE,LOCAL_ID,CAPACITY};

    public MD_Site_Zone_LocalDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_Site_Zone_LocalToContentValuesMapper();
        this.toMD_Site_Zone_LocalMapper = new CursorMD_Site_Zone_LocalMapper();

    }


    @Override
    public void addUpdate(MD_Site_Zone_Local md_site_zone_local) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_site_zone_local)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_site_zone_local.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(md_site_zone_local.getSite_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(ZONE_CODE).append(" = '").append(String.valueOf(md_site_zone_local.getZone_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(LOCAL_CODE).append(" = '").append(String.valueOf(md_site_zone_local.getLocal_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_site_zone_local), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Site_Zone_Local> md_site_zone_locals, boolean status) {

        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Site_Zone_Local md_site_zone_local : md_site_zone_locals) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_site_zone_local)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_site_zone_local.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(md_site_zone_local.getZone_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(ZONE_CODE).append(" = '").append(String.valueOf(md_site_zone_local.getSite_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_site_zone_local), sbWhere.toString(), null);
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
    public MD_Site_Zone_Local getByString(String sQuery) {

        MD_Site_Zone_Local md_site_zone_local = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_site_zone_local = toMD_Site_Zone_LocalMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_site_zone_local;
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
    public List<MD_Site_Zone_Local> query(String sQuery) {
        List<MD_Site_Zone_Local> md_site_zone_locals = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Site_Zone_Local uAux = toMD_Site_Zone_LocalMapper.map(cursor);
                md_site_zone_locals.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_site_zone_locals;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_site_zone_locals = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_site_zone_locals.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_site_zone_locals;
    }

    private class CursorMD_Site_Zone_LocalMapper implements Mapper<Cursor,MD_Site_Zone_Local> {
        @Override
        public MD_Site_Zone_Local map(Cursor cursor) {
            MD_Site_Zone_Local siteZoneLocal = new MD_Site_Zone_Local();

            siteZoneLocal.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            siteZoneLocal.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            siteZoneLocal.setZone_code(cursor.getInt(cursor.getColumnIndex(ZONE_CODE)));
            siteZoneLocal.setLocal_code(cursor.getInt(cursor.getColumnIndex(LOCAL_CODE)));
            siteZoneLocal.setLocal_id(cursor.getString(cursor.getColumnIndex(LOCAL_ID)));
            siteZoneLocal.setCapacity(cursor.getInt(cursor.getColumnIndex(CAPACITY)));

            return siteZoneLocal;
        }
    }

    private class MD_Site_Zone_LocalToContentValuesMapper implements Mapper<MD_Site_Zone_Local,ContentValues> {
        @Override
        public ContentValues map(MD_Site_Zone_Local md_site_zone_local) {
            ContentValues contentValues = new ContentValues();

            if(md_site_zone_local.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,md_site_zone_local.getCustomer_code());
            }

            if(md_site_zone_local.getSite_code() > -1){
                contentValues.put(SITE_CODE,md_site_zone_local.getSite_code());
            }

            if(md_site_zone_local.getZone_code() > -1){
                contentValues.put(ZONE_CODE,md_site_zone_local.getZone_code());
            }

            if(md_site_zone_local.getLocal_code() > -1){
                contentValues.put(LOCAL_CODE,md_site_zone_local.getLocal_code());
            }

            if(md_site_zone_local.getLocal_id() != null){
                contentValues.put(LOCAL_ID,md_site_zone_local.getLocal_id());
            }

            if(md_site_zone_local.getCapacity() > -1){
                contentValues.put(CAPACITY,md_site_zone_local.getCapacity());
            }

            return contentValues;
        }
    }
}
