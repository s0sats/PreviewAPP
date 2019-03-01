package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.IO_Move_Reason;
import com.namoadigital.prj001.util.Constant;

import java.util.List;

public class IO_Move_ReasonDao extends BaseDao implements Dao<IO_Move_Reason> {

    private final Mapper<IO_Move_Reason, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, IO_Move_Reason> toIO_Move_ReasonMapper;

    public static final String TABLE = "io_move_reason";
    public static final String CUSTOMER_CODE = "CUSTOMER_CODE";
    public static final String REASON_CODE = "REASON_CODE";
    public static final String REASON_ID = "REASON_ID";
    public static final String REASON_DESC = "REASON_DESC";


    public IO_Move_ReasonDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper  = new IO_Move_ReasonToContentValuesMapper();
        this.toIO_Move_ReasonMapper = new CursorIO_Move_ReasonMapper();
    }

    @Override
    public void addUpdate(IO_Move_Reason item) {

    }

    @Override
    public void addUpdate(Iterable<IO_Move_Reason> items, boolean status) {

    }

    @Override
    public void addUpdate(String sQuery) {

    }

    @Override
    public void remove(String sQuery) {

    }

    @Override
    public IO_Move_Reason getByString(String sQuery) {
        return null;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        return null;
    }

    @Override
    public List<IO_Move_Reason> query(String sQuery) {
        return null;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        return null;
    }

    private class CursorIO_Move_ReasonMapper implements Mapper<Cursor, IO_Move_Reason> {
        @Override
        public IO_Move_Reason map(Cursor cursor) {
            IO_Move_Reason ioMoveReason = new IO_Move_Reason();
            //
            ioMoveReason.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            ioMoveReason.setReason_code(cursor.getInt(cursor.getColumnIndex(REASON_CODE)));
            ioMoveReason.setReason_id(cursor.getString(cursor.getColumnIndex(REASON_ID)));
            ioMoveReason.setReason_desc(cursor.getString(cursor.getColumnIndex(REASON_DESC)));
            //
            return ioMoveReason;
        }
    }

    private class IO_Move_ReasonToContentValuesMapper implements Mapper<IO_Move_Reason, ContentValues> {
        @Override
        public ContentValues map(IO_Move_Reason io_move_reason) {
            ContentValues cv = new ContentValues();
            //
            if(io_move_reason.getCustomer_code() > -1){
                cv.put(CUSTOMER_CODE,io_move_reason.getCustomer_code());
            }

            if(io_move_reason.getReason_code() > -1){
                cv.put(REASON_CODE,io_move_reason.getReason_code());
            }

            if(io_move_reason.getReason_id() != null){
                cv.put(REASON_ID,io_move_reason.getReason_id());
            }

            if(io_move_reason.getReason_desc() != null){
                cv.put(REASON_DESC,io_move_reason.getReason_desc());
            }

            return cv;
        }
    }
}
