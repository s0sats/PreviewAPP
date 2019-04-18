package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.IO_Move_Reason;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class IO_Move_ReasonDao extends BaseDao implements Dao<IO_Move_Reason> {

    private final Mapper<IO_Move_Reason, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, IO_Move_Reason> toIO_Move_ReasonMapper;

    public static final String TABLE = "io_move_reason";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String REASON_CODE = "reason_code";
    public static final String REASON_ID = "reason_id";
    public static final String REASON_DESC = "reason_desc";


    public IO_Move_ReasonDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper  = new IO_Move_ReasonToContentValuesMapper();
        this.toIO_Move_ReasonMapper = new CursorIO_Move_ReasonMapper();
    }

    @Override
    public void addUpdate(IO_Move_Reason  io_move_reason) {
        openDB();
        try {
            if (db.insert(TABLE, null, toContentValuesMapper.map(io_move_reason)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_move_reason.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(REASON_CODE).append(" = '").append(String.valueOf(io_move_reason.getReason_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(io_move_reason), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }
        closeDB();
    }

    @Override
    public void addUpdate(Iterable<IO_Move_Reason> io_move_reasons, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (IO_Move_Reason io_move_reason : io_move_reasons) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(io_move_reason)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_move_reason.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(REASON_CODE).append(" = '").append(String.valueOf(io_move_reason.getReason_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(io_move_reason), sbWhere.toString(), null);
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
    public IO_Move_Reason getByString(String sQuery) {
        IO_Move_Reason io_move_reason = null;
        openDB();
        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_move_reason = toIO_Move_ReasonMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        closeDB();

        return io_move_reason;
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
    public List<IO_Move_Reason> query(String sQuery) {
        List<IO_Move_Reason> io_move_reasons = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                IO_Move_Reason uAux = toIO_Move_ReasonMapper.map(cursor);
                io_move_reasons.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return io_move_reasons;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> io_move_reasons = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_move_reasons.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return io_move_reasons;
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
