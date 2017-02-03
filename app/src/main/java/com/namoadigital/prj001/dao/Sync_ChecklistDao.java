package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.Sync_Checklist;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 03/02/2017.
 */

public class Sync_ChecklistDao extends BaseDao implements Dao<Sync_Checklist> {

    private final Mapper<Sync_Checklist, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, Sync_Checklist> toSync_ChecklistMapper;

    public static final String TABLE = "sync_checklist";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String LAST_UPDATE = "last_update";

    private String[] columns = {CUSTOMER_CODE, PRODUCT_CODE, LAST_UPDATE};

    public Sync_ChecklistDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        toContentValuesMapper =  new Sync_ChecklistToContentValuesMapper();
        toSync_ChecklistMapper = new CursorSync_ChecklistMapper();
    }


    @Override
    public void addUpdate(Sync_Checklist syncChecklist) {
        openDB();
        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(syncChecklist)) == -1){
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE)
                        .append(" = '").append(String.valueOf(syncChecklist.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE)
                        .append(" = '").append(String.valueOf(syncChecklist.getProduct_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(syncChecklist), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<Sync_Checklist> syncChecklists, boolean status) {
        openDB();
        //
        try {
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (Sync_Checklist syncChecklist : syncChecklists) {

                if (db.insert(TABLE, null, toContentValuesMapper.map(syncChecklist)) == -1){
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE)
                            .append(" = '").append(String.valueOf(syncChecklist.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE)
                            .append(" = '").append(String.valueOf(syncChecklist.getProduct_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(syncChecklist), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
        closeDB();

    }

    @Override
    public void addUpdate(String sQuery) {
        openDB();
        //
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
        //
        try {
            db.execSQL(sQuery);

        } catch (Exception e) {
        } finally {
        }
        closeDB();
    }

    @Override
    public Sync_Checklist getByString(String sQuery) {
        Sync_Checklist syncChecklist = new Sync_Checklist();
        openDB();
        //
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                syncChecklist = toSync_ChecklistMapper.map(cursor);
            }
            cursor.close();
        } catch (Exception e) {

        } finally {
        }
        closeDB();

        return syncChecklist;
    }

    @Override
    public List<Sync_Checklist> query(String sQuery) {

        List<Sync_Checklist> syncChecklists = new ArrayList<>();

        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                Sync_Checklist uAux = toSync_ChecklistMapper.map(cursor);
                syncChecklists.add(uAux);
            }
            cursor.close();
        } catch (Exception e) {

        } finally {
        }
        closeDB();

        return syncChecklists;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> syncChecklists = new ArrayList<>();

        openDB();

        String s_query_div[] = sQuery.split(";");

            Mapper<Cursor,HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                syncChecklists.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }
        closeDB();

        return syncChecklists;
    }

    private class CursorSync_ChecklistMapper implements Mapper<Cursor,Sync_Checklist> {
        @Override
        public Sync_Checklist map(Cursor cursor) {
            Sync_Checklist syncChecklist =  new Sync_Checklist();

            syncChecklist.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            syncChecklist.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            syncChecklist.setLast_update(cursor.getString(cursor.getColumnIndex(LAST_UPDATE)));

            return syncChecklist;
        }
    }

    private class Sync_ChecklistToContentValuesMapper implements Mapper<Sync_Checklist,ContentValues> {
        @Override
        public ContentValues map(Sync_Checklist sync_checklist) {
            ContentValues contentValues = new ContentValues();
            //
            if (sync_checklist.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,sync_checklist.getCustomer_code());
            }
            //
            if (sync_checklist.getProduct_code() > -1){
                contentValues.put(PRODUCT_CODE,sync_checklist.getProduct_code());
            }
            //
            if (sync_checklist.getLast_update() != null){
                contentValues.put(LAST_UPDATE,sync_checklist.getLast_update());
            }
            return contentValues;
        }
    }
}
