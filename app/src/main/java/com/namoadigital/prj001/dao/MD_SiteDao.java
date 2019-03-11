package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class MD_SiteDao extends BaseDao implements Dao<MD_Site> {
    private final Mapper<MD_Site, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Site> toMD_SiteMapper;

    public static final String TABLE = "md_sites";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SITE_CODE = "site_code";
    public static final String SITE_ID = "site_id";
    public static final String SITE_DESC = "site_desc";
    public static final String IO_CONTROL = "io_control";
    public static final String REASON_CODE = "reason_code";
    public static final String INBOUND_AUTO_CREATE = "inbound_auto_create";
    public static final String IN_ALLOW_NEW_ITEM = "in_allow_new_item";
    public static final String IN_PUT_AWAY_PROCESS = "in_put_away_process" ;
    public static final String IN_ZONE_CODE_CONF = "in_zone_code_conf";
    public static final String IN_LOCAL_CODE_CONF = "in_local_code_conf";
    public static final String IN_DONE_AUTOMATIC = "in_done_automatic";
    public static final String OUT_ALLOW_NEW_ITEM = "out_allow_new_item";
    public static final String OUT_PICKING_PROCESS = "out_picking_process";
    public static final String OUT_ZONE_CODE_PICKING = "out_zone_code_picking";
    public static final String OUT_LOCAL_CODE_PICKING = "out_local_code_picking";
    public static final String OUT_DONE_AUTOMATIC = "out_done_automatic";

    private String[] columns = {
            CUSTOMER_CODE, SITE_CODE, SITE_ID, SITE_DESC,IO_CONTROL,REASON_CODE,INBOUND_AUTO_CREATE,
            IN_ALLOW_NEW_ITEM,IN_PUT_AWAY_PROCESS,IN_ZONE_CODE_CONF,IN_LOCAL_CODE_CONF, IN_DONE_AUTOMATIC,
            OUT_ALLOW_NEW_ITEM, OUT_PICKING_PROCESS, OUT_ZONE_CODE_PICKING, OUT_LOCAL_CODE_PICKING, OUT_DONE_AUTOMATIC
    };

    public MD_SiteDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_SiteToContentValuesMapper();
        this.toMD_SiteMapper = new CursorMD_SiteMapper();
    }

    @Override
    public void addUpdate(MD_Site md_site) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_site)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_site.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(md_site.getSite_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_site), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<MD_Site> md_sites, boolean status) {
        openDB();

        try {

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

                    db.update(TABLE, toContentValuesMapper.map(md_site), sbWhere.toString(), null);
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
    public void addUpdate(String s_query) {
        openDB();

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

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public MD_Site getByString(String s_query) {
        MD_Site md_site = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                md_site = toMD_SiteMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_site;
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
    public List<MD_Site> query(String s_query) {
        List<MD_Site> md_sites = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                MD_Site uAux = toMD_SiteMapper.map(cursor);
                md_sites.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_sites;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> md_sites = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                md_sites.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_sites;
    }

    private class CursorMD_SiteMapper implements Mapper<Cursor,MD_Site> {
        @Override
        public MD_Site map(Cursor cursor) {
            MD_Site md_site = new MD_Site();

            md_site.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_site.setSite_code(cursor.getString(cursor.getColumnIndex(SITE_CODE)));
            md_site.setSite_id(cursor.getString(cursor.getColumnIndex(SITE_ID)));
            md_site.setSite_desc(cursor.getString(cursor.getColumnIndex(SITE_DESC)));
            md_site.setIo_control(cursor.getInt(cursor.getColumnIndex(IO_CONTROL)));
            if(cursor.isNull(cursor.getColumnIndex(REASON_CODE))) {
                md_site.setReason_code(null);
            }else{
                md_site.setReason_code(cursor.getInt(cursor.getColumnIndex(REASON_CODE)));
            }
            md_site.setInbound_auto_create(cursor.getInt(cursor.getColumnIndex(INBOUND_AUTO_CREATE)));

            md_site.setIn_allow_new_item(cursor.getInt(cursor.getColumnIndex(IN_ALLOW_NEW_ITEM)));
            md_site.setIn_put_away_process(cursor.getInt(cursor.getColumnIndex(IN_PUT_AWAY_PROCESS)));
            if(cursor.isNull(cursor.getColumnIndex(IN_ZONE_CODE_CONF))) {
                md_site.setIn_zone_code_conf(null);
            }else{
                md_site.setIn_zone_code_conf(cursor.getInt(cursor.getColumnIndex(IN_ZONE_CODE_CONF)));
            }
            if(cursor.isNull(cursor.getColumnIndex(IN_LOCAL_CODE_CONF))) {
                md_site.setIn_local_code_conf(null);
            }else{
                md_site.setIn_local_code_conf(cursor.getInt(cursor.getColumnIndex(IN_LOCAL_CODE_CONF)));
            }
            md_site.setIn_done_automatic(cursor.getInt(cursor.getColumnIndex(IN_DONE_AUTOMATIC)));
            md_site.setOut_allow_new_item(cursor.getInt(cursor.getColumnIndex(OUT_ALLOW_NEW_ITEM)));
            md_site.setOut_picking_process(cursor.getInt(cursor.getColumnIndex(OUT_PICKING_PROCESS)));
            if(cursor.isNull(cursor.getColumnIndex(OUT_ZONE_CODE_PICKING))) {
                md_site.setOut_zone_code_picking(null);
            }else{
                md_site.setOut_zone_code_picking(cursor.getInt(cursor.getColumnIndex(OUT_ZONE_CODE_PICKING)));
            }
            if(cursor.isNull(cursor.getColumnIndex(OUT_LOCAL_CODE_PICKING))) {
                md_site.setOut_local_code_picking(null);
            }else{
                md_site.setOut_local_code_picking(cursor.getInt(cursor.getColumnIndex(OUT_LOCAL_CODE_PICKING)));
            }
            md_site.setOut_done_automatic(cursor.getInt(cursor.getColumnIndex(OUT_DONE_AUTOMATIC)));

            return md_site;
        }

    }

    private class MD_SiteToContentValuesMapper implements Mapper<MD_Site,ContentValues> {
        @Override
        public ContentValues map(MD_Site md_site) {
            ContentValues contentValues = new ContentValues();

            if (md_site.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_site.getCustomer_code());
            }
            if (md_site.getSite_code() != null) {
                contentValues.put(SITE_CODE, md_site.getSite_code());
            }
            if (md_site.getSite_id() != null) {
                contentValues.put(SITE_ID, md_site.getSite_id());
            }
            if (md_site.getSite_desc() != null) {
                contentValues.put(SITE_DESC, md_site.getSite_desc());
            }
            if (md_site.getIo_control() > -1) {
                contentValues.put(IO_CONTROL, md_site.getIo_control());
            }

            contentValues.put(REASON_CODE, md_site.getReason_code());

            if (md_site.getInbound_auto_create() > -1) {
                contentValues.put(INBOUND_AUTO_CREATE, md_site.getInbound_auto_create());
            }
            if(md_site.getIn_allow_new_item() > -1){
                contentValues.put(IN_ALLOW_NEW_ITEM,md_site.getIn_allow_new_item());
            }
            if(md_site.getIn_put_away_process() > -1){
                contentValues.put(IN_PUT_AWAY_PROCESS,md_site.getIn_put_away_process());
            }
            contentValues.put(IN_ZONE_CODE_CONF,md_site.getIn_zone_code_conf());
            contentValues.put(IN_LOCAL_CODE_CONF,md_site.getIn_local_code_conf());
            if(md_site.getIn_done_automatic() > -1){
                contentValues.put(IN_DONE_AUTOMATIC,md_site.getIn_done_automatic());
            }
            if(md_site.getOut_allow_new_item() > -1){
                contentValues.put(OUT_ALLOW_NEW_ITEM,md_site.getOut_allow_new_item());
            }
            if(md_site.getOut_picking_process() > -1){
                contentValues.put(OUT_PICKING_PROCESS,md_site.getOut_picking_process());
            }
            contentValues.put(OUT_ZONE_CODE_PICKING,md_site.getOut_zone_code_picking());
            contentValues.put(OUT_LOCAL_CODE_PICKING,md_site.getOut_local_code_picking());
            if(md_site.getOut_done_automatic() > -1){
                contentValues.put(OUT_DONE_AUTOMATIC,md_site.getOut_done_automatic());
            }

            return contentValues;

        }
    }
}
