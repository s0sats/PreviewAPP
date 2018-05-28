package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.EV_Profile;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 26/05/2017.
 */

public class EV_ProfileDao extends BaseDao implements Dao<EV_Profile> {


    private final Mapper<EV_Profile, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, EV_Profile> toEV_ProfileMapper;

    public static final String TABLE = "ev_profiles";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String MENU_CODE = "menu_code";
    public static final String PARAMETER_CODE = "parameter_code";

    public static final String[] columns = {CUSTOMER_CODE, MENU_CODE, PARAMETER_CODE};

    public EV_ProfileDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        this.toContentValuesMapper = new EV_ProfileToContentValuesMapper();
        this.toEV_ProfileMapper = new CursorEV_ProfileMapper();
    }

    @Override
    public void addUpdate(EV_Profile ev_profile) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(ev_profile)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(ev_profile.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(MENU_CODE).append(" = '").append(ev_profile.getMenu_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PARAMETER_CODE).append(" = '").append(ev_profile.getParameter_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(ev_profile), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<EV_Profile> ev_profiles, boolean status) {

        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (EV_Profile ev_profile : ev_profiles) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(ev_profile)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(ev_profile.getCustomer_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(MENU_CODE).append(" = '").append(ev_profile.getMenu_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PARAMETER_CODE).append(" = '").append(ev_profile.getParameter_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(ev_profile), sbWhere.toString(), null);
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
    public EV_Profile getByString(String sQuery) {
        EV_Profile ev_profile = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                ev_profile = toEV_ProfileMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return ev_profile;
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
    public List<EV_Profile> query(String sQuery) {
        List<EV_Profile> ev_profiles =  new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                EV_Profile uAux = toEV_ProfileMapper.map(cursor);
                ev_profiles.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();


        return ev_profiles;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {

        List<HMAux> ev_profiles =  new ArrayList<>();

        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                ev_profiles.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return ev_profiles;
    }

    private class CursorEV_ProfileMapper implements Mapper<Cursor, EV_Profile> {

        @Override
        public EV_Profile map(Cursor cursor) {
            EV_Profile ev_profile = new EV_Profile();

            ev_profile.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            ev_profile.setMenu_code(cursor.getString(cursor.getColumnIndex(MENU_CODE)));

            if (cursor.isNull(cursor.getColumnIndex(PARAMETER_CODE))) {
                ev_profile.setParameter_code(null);
            } else {
                ev_profile.setParameter_code(cursor.getString(cursor.getColumnIndex(PARAMETER_CODE)));
            }

            return ev_profile;
        }
    }

    private class EV_ProfileToContentValuesMapper implements Mapper<EV_Profile, ContentValues> {
        @Override
        public ContentValues map(EV_Profile ev_profile) {

            ContentValues contentValues = new ContentValues();

            if (ev_profile.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, ev_profile.getCustomer_code());
            }
            if (ev_profile.getMenu_code() != null) {
                contentValues.put(MENU_CODE, ev_profile.getMenu_code());
            }

            contentValues.put(PARAMETER_CODE, ev_profile.getParameter_code());

            return contentValues;
        }
    }
}
