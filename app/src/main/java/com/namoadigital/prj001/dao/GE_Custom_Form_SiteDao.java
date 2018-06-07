package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form_Site;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class GE_Custom_Form_SiteDao extends BaseDao implements Dao<GE_Custom_Form_Site> {
    private final Mapper<GE_Custom_Form_Site, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Site> toGE_Custom_Form_SitetMapper;

    public static final String TABLE = "ge_custom_form_sites";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String SITE_CODE = "site_code";

    public static String[] columns = {CUSTOMER_CODE, CUSTOM_FORM_TYPE, CUSTOM_FORM_CODE, CUSTOM_FORM_VERSION, SITE_CODE};

    public GE_Custom_Form_SiteDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new GE_Custom_Form_SiteToContentValuesMapper();
        this.toGE_Custom_Form_SitetMapper = new CursorTtoGE_Custom_Form_SitetMapper();
    }

    public GE_Custom_Form_SiteDao(Context context) {
        super(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM,
                Constant.DB_MODE_MULTI
        );
        //
        this.toContentValuesMapper = new GE_Custom_Form_SiteToContentValuesMapper();
        this.toGE_Custom_Form_SitetMapper = new CursorTtoGE_Custom_Form_SitetMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form_Site ge_custom_form_site) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(ge_custom_form_site)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(ge_custom_form_site.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(ge_custom_form_site.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(ge_custom_form_site.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(ge_custom_form_site.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(ge_custom_form_site.getSite_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(ge_custom_form_site), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Site> ge_custom_form_sites, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Site ge_custom_form_site : ge_custom_form_sites) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(ge_custom_form_site)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(ge_custom_form_site.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(ge_custom_form_site.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(ge_custom_form_site.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(ge_custom_form_site.getCustom_form_version())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SITE_CODE).append(" = '").append(String.valueOf(ge_custom_form_site.getSite_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(ge_custom_form_site), sbWhere.toString(), null);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
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
    public GE_Custom_Form_Site getByString(String sQuery) {
        GE_Custom_Form_Site ge_custom_form_site = null;
        //
        openDB();
        //
        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                ge_custom_form_site = toGE_Custom_Form_SitetMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return ge_custom_form_site;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        HMAux hmAux =  null;

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
    public List<GE_Custom_Form_Site> query(String sQuery) {
        List<GE_Custom_Form_Site> ge_custom_form_sites = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Site uAux = toGE_Custom_Form_SitetMapper.map(cursor);
                ge_custom_form_sites.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return ge_custom_form_sites;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> ge_custom_form_sites = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                ge_custom_form_sites.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return ge_custom_form_sites;
    }

    private class CursorTtoGE_Custom_Form_SitetMapper implements Mapper<Cursor, GE_Custom_Form_Site> {
        @Override
        public GE_Custom_Form_Site map(Cursor cursor) {
            GE_Custom_Form_Site custom_form_site = new GE_Custom_Form_Site();
            //
            custom_form_site.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form_site.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form_site.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            custom_form_site.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            custom_form_site.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            //
            return custom_form_site;
        }
    }

    private class GE_Custom_Form_SiteToContentValuesMapper implements Mapper<GE_Custom_Form_Site, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Site custom_form_site) {
            ContentValues contentValues = new ContentValues();
            //
            if(custom_form_site.getCustom_form_code() > -1){
                contentValues.put(CUSTOMER_CODE,custom_form_site.getCustomer_code());
            }
            if(custom_form_site.getCustom_form_type() > -1){
                contentValues.put(CUSTOM_FORM_TYPE,custom_form_site.getCustom_form_type());
            }
            if(custom_form_site.getCustom_form_code() > -1){
                contentValues.put(CUSTOM_FORM_CODE,custom_form_site.getCustom_form_code());
            }
            if(custom_form_site.getCustom_form_version() > -1){
                contentValues.put(CUSTOM_FORM_VERSION,custom_form_site.getCustom_form_version());
            }
            if(custom_form_site.getSite_code() > -1){
                contentValues.put(SITE_CODE,custom_form_site.getSite_code());
            }
            //
            return contentValues;
        }
    }


}
