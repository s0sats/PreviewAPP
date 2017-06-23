package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Site_ZoneDao extends BaseDao implements Dao<MD_Site_Zone> {

    private final Mapper<MD_Site_Zone, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Site_Zone> toMD_Site_ZoneMapper;

    public static final String TABLE = "md_site_zones";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SITE_CODE = "site_code";
    public static final String ZONE_CODE = "zone_code";
    public static final String ZONE_ID = "zone_id";
    public static final String ZONE_DESC = "zone_desc";
    public static final String BLOCKED = "blocked";
    public static final String PROCESS_SEQ = "process_seq";

    private String[] columns = {CUSTOMER_CODE, SITE_CODE, ZONE_CODE, ZONE_ID,ZONE_DESC,BLOCKED,PROCESS_SEQ};

    public MD_Site_ZoneDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_Site_ZoneToContentValuesMapper();
        this.toMD_Site_ZoneMapper = new CursorMD_Site_ZoneMapper();
    }


    @Override
    public void addUpdate(MD_Site_Zone md_site_zone) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_site_zone)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_site_zone.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(md_site_zone.getSite_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(ZONE_CODE).append(" = '").append(String.valueOf(md_site_zone.getSite_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_site_zone), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Site_Zone> md_site_zones , boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Site_Zone md_site_zone : md_site_zones) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_site_zone)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_site_zone.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(md_site_zone.getSite_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(ZONE_CODE).append(" = '").append(String.valueOf(md_site_zone.getSite_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_site_zone), sbWhere.toString(), null);
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
    public MD_Site_Zone getByString(String sQuery) {
        MD_Site_Zone md_site_zone = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_site_zone = toMD_Site_ZoneMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_site_zone;
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
    public List<MD_Site_Zone> query(String sQuery) {
        List<MD_Site_Zone> md_site_zones = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Site_Zone uAux = toMD_Site_ZoneMapper.map(cursor);
                md_site_zones.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_site_zones;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_site_zones = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_site_zones.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_site_zones;
    }

    private class CursorMD_Site_ZoneMapper implements Mapper<Cursor,MD_Site_Zone> {

        @Override
        public MD_Site_Zone map(Cursor cursor) {
            MD_Site_Zone siteZone = new MD_Site_Zone();

            siteZone.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            siteZone.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            siteZone.setZone_code(cursor.getInt(cursor.getColumnIndex(ZONE_CODE)));
            siteZone.setZone_id(cursor.getString(cursor.getColumnIndex(ZONE_ID)));
            siteZone.setZone_desc(cursor.getString(cursor.getColumnIndex(ZONE_DESC)));
            siteZone.setBlocked(cursor.getInt(cursor.getColumnIndex(BLOCKED)));
            if(cursor.isNull(cursor.getColumnIndex(PROCESS_SEQ))){
                siteZone.setProcess_seq(null);
            }else{
                siteZone.setProcess_seq(cursor.getInt(cursor.getColumnIndex(PROCESS_SEQ)));
            }
            return siteZone;
        }
    }

    private class MD_Site_ZoneToContentValuesMapper implements Mapper<MD_Site_Zone,ContentValues> {
        @Override
        public ContentValues map(MD_Site_Zone md_site_zone) {
            ContentValues contentValues = new ContentValues();

            if(md_site_zone.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,md_site_zone.getCustomer_code());
            }
            if(md_site_zone.getSite_code() > -1){
                contentValues.put(SITE_CODE,md_site_zone.getSite_code());
            }
            if(md_site_zone.getZone_code() > -1){
                contentValues.put(ZONE_CODE,md_site_zone.getZone_code());
            }
            if(md_site_zone.getZone_id() != null){
                contentValues.put(ZONE_ID,md_site_zone.getZone_id());
            }
            if(md_site_zone.getZone_desc() != null){
                contentValues.put(ZONE_DESC,md_site_zone.getZone_desc());
            }
            if(md_site_zone.getBlocked() > -1){
                contentValues.put(BLOCKED,md_site_zone.getBlocked());
            }
            contentValues.put(PROCESS_SEQ,md_site_zone.getProcess_seq());

            return contentValues;
        }
    }
}
