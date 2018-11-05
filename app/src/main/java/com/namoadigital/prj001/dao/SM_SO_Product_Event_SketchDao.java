package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Product_Event_Sketch;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 30/10/2017.
 */

public class SM_SO_Product_Event_SketchDao extends BaseDao implements Dao<SM_SO_Product_Event_Sketch>, DaoTmp<SM_SO_Product_Event_Sketch> {

    private final Mapper<SM_SO_Product_Event_Sketch,ContentValues> toContentValuesMapper;
    private final Mapper<Cursor,SM_SO_Product_Event_Sketch> toSM_SO_Product_Event_SketchMapper;

    public static final String TABLE = "sm_so_product_event_sketchs";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String SEQ = "seq";
    public static final String SEQ_TMP = "seq_tmp";
    public static final String LINE = "line";
    public static final String COL = "col";

    public static final String[] columns = {
            CUSTOMER_CODE, SO_PREFIX, SO_CODE, SEQ, SEQ_TMP, LINE,COL
    };

    public SM_SO_Product_Event_SketchDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new SM_SO_Product_Event_SketchToContentValuesMapper();
        this.toSM_SO_Product_Event_SketchMapper = new CursorSM_SO_Product_Event_SketchMapper();
    }

    @Override
    public void addUpdateTmp(SM_SO_Product_Event_Sketch sm_so_product_event_sketch) {
        openDB();

        try {
            if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event_sketch)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SEQ_TMP).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSeq_tmp())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(LINE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getLine())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(COL).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getCol())).append("'");

                db.update(TABLE, toContentValuesMapper.map(sm_so_product_event_sketch), sbWhere.toString(), null);
            }
        } catch (Exception e) {
            //ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdateTmp(Iterable<SM_SO_Product_Event_Sketch> sm_so_product_event_sketches, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (SM_SO_Product_Event_Sketch sm_so_product_event_sketch : sm_so_product_event_sketches ) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event_sketch)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSo_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SEQ_TMP).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSeq_tmp())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(LINE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getLine())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(COL).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getCol())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(sm_so_product_event_sketch), sbWhere.toString(), null);
                }
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            //db.endTransaction();
        }

        closeDB();
    }

    @Override
    public void addUpdate(SM_SO_Product_Event_Sketch sm_so_product_event_sketch) {
        openDB();

        try {
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSo_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSo_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SEQ).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSeq())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(LINE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getLine())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(COL).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getCol())).append("'");
            //
            if (db.update(TABLE, toContentValuesMapper.map(sm_so_product_event_sketch), sbWhere.toString(), null) == 0) {
                db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event_sketch));
            }
        } catch (Exception e) {
            //ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SM_SO_Product_Event_Sketch> sm_so_product_event_sketches , boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (SM_SO_Product_Event_Sketch sm_so_product_event_sketch:sm_so_product_event_sketches) {

                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SEQ).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getSeq())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(LINE).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getLine())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(COL).append(" = '").append(String.valueOf(sm_so_product_event_sketch.getCol())).append("'");
                //
                if (db.update(TABLE, toContentValuesMapper.map(sm_so_product_event_sketch), sbWhere.toString(), null) == 0) {
                    db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event_sketch));
                }
            }
            //db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            //db.endTransaction();
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
    public SM_SO_Product_Event_Sketch getByString(String sQuery) {

        SM_SO_Product_Event_Sketch sm_so_product_event_sketch = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_product_event_sketch = toSM_SO_Product_Event_SketchMapper.map(cursor);
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_product_event_sketch;
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
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return hmAux;
    }

    @Override
    public List<SM_SO_Product_Event_Sketch> query(String sQuery) {

        List<SM_SO_Product_Event_Sketch> sm_so_product_event_sketches = new ArrayList<>();


        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                SM_SO_Product_Event_Sketch uAux = toSM_SO_Product_Event_SketchMapper.map(cursor);
                sm_so_product_event_sketches.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();


        return sm_so_product_event_sketches;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        ArrayList<HMAux> sm_so_product_event_files = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_product_event_files.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_product_event_files;
    }

    private class CursorSM_SO_Product_Event_SketchMapper implements Mapper<Cursor,SM_SO_Product_Event_Sketch> {
        @Override
        public SM_SO_Product_Event_Sketch map(Cursor cursor) {
            SM_SO_Product_Event_Sketch sm_so_product_event_sketch = new SM_SO_Product_Event_Sketch();

            sm_so_product_event_sketch.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            sm_so_product_event_sketch.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            sm_so_product_event_sketch.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            sm_so_product_event_sketch.setSeq(cursor.getInt(cursor.getColumnIndex(SEQ)));
            sm_so_product_event_sketch.setSeq_tmp(cursor.getInt(cursor.getColumnIndex(SEQ_TMP)));
            sm_so_product_event_sketch.setLine(cursor.getInt(cursor.getColumnIndex(LINE)));
            sm_so_product_event_sketch.setCol(cursor.getInt(cursor.getColumnIndex(COL)));

            return sm_so_product_event_sketch;
        }
    }

    private class SM_SO_Product_Event_SketchToContentValuesMapper implements Mapper<SM_SO_Product_Event_Sketch,ContentValues> {
        @Override
        public ContentValues map(SM_SO_Product_Event_Sketch sm_so_product_event_sketch) {
            ContentValues contentValues = new ContentValues();

            if(sm_so_product_event_sketch.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE, sm_so_product_event_sketch.getCustomer_code());
            }
            if(sm_so_product_event_sketch.getSo_prefix() > -1){
                contentValues.put(SO_PREFIX, sm_so_product_event_sketch.getSo_prefix());
            }
            if(sm_so_product_event_sketch.getSo_code() > -1){
                contentValues.put(SO_CODE, sm_so_product_event_sketch.getSo_code());
            }
            if(sm_so_product_event_sketch.getSeq() > -1){
                contentValues.put(SEQ, sm_so_product_event_sketch.getSeq());
            }
            if(sm_so_product_event_sketch.getSeq_tmp() > -1){
                contentValues.put(SEQ_TMP, sm_so_product_event_sketch.getSeq_tmp());
            }
            if(sm_so_product_event_sketch.getLine() > -1){
                contentValues.put(LINE, sm_so_product_event_sketch.getLine());
            }
            if(sm_so_product_event_sketch.getCol() > -1){
                contentValues.put(COL, sm_so_product_event_sketch.getCol());
            }

            return contentValues;
        }
    }
}
