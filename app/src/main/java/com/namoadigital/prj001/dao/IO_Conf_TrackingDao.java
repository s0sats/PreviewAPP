package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.IO_Conf_Tracking;
import com.namoadigital.prj001.util.Constant;

import java.util.List;

public class IO_Conf_TrackingDao extends BaseDao implements Dao<IO_Conf_Tracking> {

    private final Mapper<IO_Conf_Tracking, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor,IO_Conf_Tracking> toIO_Conf_TrackingMapper;

    public static final String TABLE = "io_conf_tracking";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String PREFIX = "prefix";
    public static final String CODE = "code";
    public static final String ITEM = "item";
    public static final String TYPE = "type";
    public static final String TRACKING = "tracking";

    public IO_Conf_TrackingDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new IO_Conf_TrackingToContentValuesMapper();
        this.toIO_Conf_TrackingMapper = new CursorToIO_Conf_TrackingMapper();
    }

    @Override
    public void addUpdate(IO_Conf_Tracking item) {

    }

    @Override
    public void addUpdate(Iterable<IO_Conf_Tracking> items, boolean status) {

    }

    @Override
    public void addUpdate(String sQuery) {

    }

    @Override
    public void remove(String sQuery) {

    }

    @Override
    public IO_Conf_Tracking getByString(String sQuery) {
        return null;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        return null;
    }

    @Override
    public List<IO_Conf_Tracking> query(String sQuery) {
        return null;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        return null;
    }

    private class CursorToIO_Conf_TrackingMapper implements Mapper<Cursor, IO_Conf_Tracking> {
        @Override
        public IO_Conf_Tracking map(Cursor cursor) {
            IO_Conf_Tracking ioConfTracking = new IO_Conf_Tracking();
            //
            ioConfTracking.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            ioConfTracking.setPrefix(cursor.getInt(cursor.getColumnIndex(PREFIX)));
            ioConfTracking.setCode(cursor.getInt(cursor.getColumnIndex(CODE)));
            ioConfTracking.setItem(cursor.getInt(cursor.getColumnIndex(ITEM)));
            ioConfTracking.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
            ioConfTracking.setTracking(cursor.getString(cursor.getColumnIndex(TRACKING)));
            //
            return ioConfTracking;
        }
    }

    private class IO_Conf_TrackingToContentValuesMapper implements Mapper<IO_Conf_Tracking, ContentValues> {
        @Override
        public ContentValues map(IO_Conf_Tracking io_conf_tracking) {
            ContentValues contentValues = new ContentValues();
            if(io_conf_tracking.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,io_conf_tracking.getCustomer_code());
            }
            if(io_conf_tracking.getPrefix() > -1){
                contentValues.put(PREFIX,io_conf_tracking.getPrefix());
            }
            if(io_conf_tracking.getCode() > -1){
                contentValues.put(CODE,io_conf_tracking.getCode());
            }
            if(io_conf_tracking.getItem() > -1){
                contentValues.put(ITEM,io_conf_tracking.getItem());
            }
            if(io_conf_tracking.getType() != null){
                contentValues.put(TYPE,io_conf_tracking.getType());
            }
            if(io_conf_tracking.getTracking() != null){
                contentValues.put(TRACKING,io_conf_tracking.getTracking());
            }
            //
            return contentValues;
        }
    }
}
