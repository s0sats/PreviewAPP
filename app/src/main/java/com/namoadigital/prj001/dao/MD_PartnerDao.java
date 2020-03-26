package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 26/06/2017.
 */

public class MD_PartnerDao extends BaseDao implements Dao<MD_Partner> {

    private final Mapper<MD_Partner, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Partner> toMD_PartnerMapper;

    public static final String TABLE = "md_partners";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PARTNER_CODE = "partner_code";
    public static final String PARTNER_ID = "partner_id";
    public static final String PARTNER_DESC = "partner_desc";
    public static final String OPERATIONAL = "operational";
    public static final String BILLING = "billing";

    private String[] columns = {CUSTOMER_CODE, PARTNER_CODE, PARTNER_ID, PARTNER_DESC};
    public MD_PartnerDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_PartnerToContentValuesMapper();
        this.toMD_PartnerMapper = new CursorMD_PartnerMapper();


    }

    @Override
    public void addUpdate(MD_Partner md_partner) {
        openDB();

        try {
            if (db.insert(TABLE, null, toContentValuesMapper.map(md_partner)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_partner.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PARTNER_CODE).append(" = '").append(String.valueOf(md_partner.getPartner_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_partner), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_Partner> md_partners, boolean status) {

        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Partner md_partner : md_partners) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_partner)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_partner.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PARTNER_CODE).append(" = '").append(String.valueOf(md_partner.getPartner_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_partner), sbWhere.toString(), null);
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
    public MD_Partner getByString(String sQuery) {
        MD_Partner md_partner = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_partner = toMD_PartnerMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_partner;
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
    public List<MD_Partner> query(String sQuery) {
        List<MD_Partner> md_partners = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Partner uAux = toMD_PartnerMapper.map(cursor);
                md_partners.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();
        return md_partners;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_partners = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_partners.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_partners;
    }

    private class CursorMD_PartnerMapper implements Mapper<Cursor,MD_Partner> {
        @Override
        public MD_Partner map(Cursor cursor) {
            MD_Partner md_partner = new MD_Partner();

            md_partner.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_partner.setPartner_code(cursor.getInt(cursor.getColumnIndex(PARTNER_CODE)));
            md_partner.setPartner_id(cursor.getString(cursor.getColumnIndex(PARTNER_ID)));
            md_partner.setPartner_desc(cursor.getString(cursor.getColumnIndex(PARTNER_DESC)));
            md_partner.setOperational(cursor.getInt(cursor.getColumnIndex(OPERATIONAL)));
            md_partner.setBilling(cursor.getInt(cursor.getColumnIndex(BILLING)));
            return md_partner;
        }
    }

    private class MD_PartnerToContentValuesMapper implements Mapper<MD_Partner,ContentValues> {
        @Override
        public ContentValues map(MD_Partner md_partner) {
            ContentValues  contentValues = new ContentValues();

            if(md_partner.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,md_partner.getCustomer_code());
            }

            if(md_partner.getPartner_code() > -1){
                contentValues.put(PARTNER_CODE,md_partner.getPartner_code());
            }

            if(md_partner.getPartner_id() != null){
                contentValues.put(PARTNER_ID,md_partner.getPartner_id());
            }

            if(md_partner.getPartner_desc() != null){
                contentValues.put(PARTNER_DESC,md_partner.getPartner_desc());
            }

            if(md_partner.getOperational() > -1){
                contentValues.put(OPERATIONAL,md_partner.getOperational());
            }

            if(md_partner.getBilling() > -1){
                contentValues.put(BILLING,md_partner.getBilling());
            }

            return contentValues;
        }
    }
}
