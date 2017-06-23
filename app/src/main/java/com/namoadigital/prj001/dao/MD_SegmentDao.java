package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.MD_Segment;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_SegmentDao extends BaseDao implements Dao<MD_Segment>{

    private final Mapper<MD_Segment, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, MD_Segment> toMD_SegmentMapper;

    public static final String TABLE = "md_segments";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SEGMENT_CODE = "segment_code";
    public static final String SEGMENT_ID = "segment_id";
    public static final String SEGMENT_DESC = "segment_desc";

    private String[] columns = {CUSTOMER_CODE, SEGMENT_CODE, SEGMENT_ID, SEGMENT_DESC};

    public MD_SegmentDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new MD_SegmentToContentValuesMapper();
        this.toMD_SegmentMapper = new CursorMD_SegmentMapper();

    }

    @Override
    public void addUpdate(MD_Segment md_segment) {
        openDB();

        try {
            if (db.insert(TABLE, null, toContentValuesMapper.map(md_segment)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_segment.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SEGMENT_CODE).append(" = '").append(String.valueOf(md_segment.getSegment_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(md_segment), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();

    }

    @Override
    public void addUpdate(Iterable<MD_Segment> md_segments, boolean status) {

        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (MD_Segment md_segment:md_segments) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(md_segment)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(md_segment.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SEGMENT_CODE).append(" = '").append(String.valueOf(md_segment.getSegment_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(md_segment), sbWhere.toString(), null);
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
    public MD_Segment getByString(String sQuery) {
        MD_Segment md_segment = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_segment = toMD_SegmentMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_segment;
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
    public List<MD_Segment> query(String sQuery) {
        List<MD_Segment> md_segments = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Segment uAux = toMD_SegmentMapper.map(cursor);
                md_segments.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_segments;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_segments = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                md_segments.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return md_segments;
    }

    private class CursorMD_SegmentMapper implements Mapper<Cursor,MD_Segment> {
        @Override
        public MD_Segment map(Cursor cursor) {
            MD_Segment md_segment = new MD_Segment();

            md_segment.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_segment.setSegment_code(cursor.getInt(cursor.getColumnIndex(SEGMENT_CODE)));
            md_segment.setSegment_id(cursor.getString(cursor.getColumnIndex(SEGMENT_ID)));
            md_segment.setSegment_desc(cursor.getString(cursor.getColumnIndex(SEGMENT_DESC)));

            return md_segment;
        }
    }

    private class MD_SegmentToContentValuesMapper implements Mapper<MD_Segment,ContentValues> {
        @Override
        public ContentValues map(MD_Segment md_segment) {

            ContentValues contentValues = new ContentValues();

            if(md_segment.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,md_segment.getCustomer_code());
            }

            if(md_segment.getSegment_code() > -1){
                contentValues.put(SEGMENT_CODE,md_segment.getSegment_code());
            }

            if(md_segment.getSegment_id() != null){
                contentValues.put(SEGMENT_ID,md_segment.getSegment_id());
            }

            if(md_segment.getSegment_desc() != null){
                contentValues.put(SEGMENT_DESC,md_segment.getSegment_desc());
            }

            return contentValues;
        }
    }
}
