package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Product_Segment;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 03/07/2017.
 */

public class MD_Product_SegmentDao extends BaseDao implements Dao<MD_Product_Segment> {

    private final Mapper<MD_Product_Segment, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Product_Segment> toMD_Product_SegmentMapper;

    public static final String TABLE = "md_product_segments";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String SEGMENT_CODE = "segment_code";

    private String[] columns = {CUSTOMER_CODE, PRODUCT_CODE, SEGMENT_CODE};

    public MD_Product_SegmentDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_Product_SegmentToContentValuesMapper();
        this.toMD_Product_SegmentMapper = new CursorMD_Product_SegmentMapper();
    }

    @Override
    public void addUpdate(MD_Product_Segment md_product_segment) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_segment)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_segment.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_segment.getProduct_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SEGMENT_CODE).append(" = '").append(String.valueOf(md_product_segment.getSegment_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_product_segment), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_Product_Segment> md_product_segments, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Product_Segment md_product_segment : md_product_segments) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_product_segment)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_product_segment.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf(md_product_segment.getProduct_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SEGMENT_CODE).append(" = '").append(String.valueOf(md_product_segment.getSegment_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_product_segment), sbWhere.toString(), null);
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
    public MD_Product_Segment getByString(String sQuery) {
        MD_Product_Segment md_product_segment = null;
        openDB();

        try{
            Cursor cursor = db.rawQuery(sQuery,null);

            while (cursor.moveToNext()){
                md_product_segment = toMD_Product_SegmentMapper.map(cursor);
            }

        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }finally {
        }

        closeDB();

        return md_product_segment;
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
    public List<MD_Product_Segment> query(String sQuery) {
        List<MD_Product_Segment> md_product_segments = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Product_Segment uAux = toMD_Product_SegmentMapper.map(cursor);
                md_product_segments.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_product_segments;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux>  md_product_segments = new ArrayList<>();

        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_product_segments.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_product_segments;
    }


    private class CursorMD_Product_SegmentMapper implements Mapper<Cursor,MD_Product_Segment> {
        @Override
        public MD_Product_Segment map(Cursor cursor) {
            MD_Product_Segment md_product_segment = new MD_Product_Segment();

            md_product_segment.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_product_segment.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            md_product_segment.setSegment_code(cursor.getInt(cursor.getColumnIndex(SEGMENT_CODE)));

            return md_product_segment;
        }
    }

    private class MD_Product_SegmentToContentValuesMapper implements Mapper<MD_Product_Segment,ContentValues> {
        @Override
        public ContentValues map(MD_Product_Segment md_product_segment) {
            ContentValues contentValues = new ContentValues();

            if (md_product_segment.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, md_product_segment.getCustomer_code());
            }

            if (md_product_segment.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, md_product_segment.getProduct_code());
            }

            if (md_product_segment.getSegment_code() > -1) {
                contentValues.put(SEGMENT_CODE, md_product_segment.getSegment_code());
            }

            return contentValues;
        }
    }
}
