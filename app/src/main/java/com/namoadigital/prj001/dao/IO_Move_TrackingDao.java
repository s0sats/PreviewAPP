package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class IO_Move_TrackingDao extends BaseDao implements DaoWithReturn<IO_Move_Tracking> {

    private final Mapper<IO_Move_Tracking, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor,IO_Move_Tracking> toIO_Move_TrackingMapper;

    public static final String TABLE = "io_move_tracking";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String MOVE_PREFIX = "move_prefix";
    public static final String MOVE_CODE = "move_code";
    public static final String TRACKING = "tracking";


    public IO_Move_TrackingDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new IO_Move_TrackingToContentValuesMapper();
        this.toIO_Move_TrackingMapper = new CursorIO_Move_TrackingMapper();
    }


    @Override
    public DaoObjReturn addUpdate(IO_Move_Tracking io_move_tracking) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        openDB();

        try{
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_move_tracking.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(MOVE_PREFIX).append(" = '").append(String.valueOf(io_move_tracking.getMove_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(MOVE_CODE).append(" = '").append(String.valueOf(io_move_tracking.getMove_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TRACKING).append(" = '").append(String.valueOf(io_move_tracking.getTracking())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_move_tracking), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_move_tracking));
            }
        }catch (SQLiteException e){
            //Chama metodo que baseado na exception gera obj de retorno setado como erro
            //e contendo msg de erro tratada.
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage());
            //Gera arquivo de exception usando dados da exception e do obj de retorno
            ToolBox_Inf.registerException(
                    getClass().getName(),
                    new Exception(
                            e.getMessage() + "\n" + daoObjReturn.getErrorMsg()
                    )
            );

        }catch (Exception e){
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true);
            ToolBox_Inf.registerException(getClass().getName(), e);
        }finally {
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }

        closeDB();

        return daoObjReturn;
    }

    @Override
    public DaoObjReturn addUpdate(List<IO_Move_Tracking> io_move_trackings, boolean status) {
        return addUpdate(io_move_trackings,status,null);
    }

    public DaoObjReturn addUpdate(List<IO_Move_Tracking> io_move_trackings, boolean status, SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;

        if(dbInstance == null){
            openDB();
        }else{
            this.db = dbInstance;
        }
        try {
            //Se db não foi passado, inicializa transaction
            if(dbInstance == null) {
                db.beginTransaction();
            }

            if (status) {
                db.delete(TABLE, null, null);
            }

            for(IO_Move_Tracking io_move_tracking :io_move_trackings){
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_move_tracking.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(MOVE_PREFIX).append(" = '").append(String.valueOf(io_move_tracking.getMove_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(MOVE_CODE).append(" = '").append(String.valueOf(io_move_tracking.getMove_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TRACKING).append(" = '").append(String.valueOf(io_move_tracking.getTracking())).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_move_tracking), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_move_tracking));
                }
            }

            //Se db não foi passado, finaliza transaction com sucesso
            if(dbInstance == null) {
                db.setTransactionSuccessful();
            }
        }catch (SQLiteException e){
            //Chama metodo que baseado na exception gera obj de retorno setado como erro
            //e contendo msg de erro tratada.
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage());
            //
            ToolBox_Inf.registerException(
                getClass().getName(),
                new Exception(
                    e.getMessage() + "\n" + daoObjReturn.getErrorMsg()
                )
            );

        } catch (Exception e) {
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true);
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            if(dbInstance == null) {
                db.endTransaction();
            }
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }
        //
        if(dbInstance == null){
            closeDB();
        }
        //
        return daoObjReturn;
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

    public DaoObjReturn delete(IO_Move_Tracking ioMoveTracking) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.DELETE;
        //

        try{
            openDB();
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(ioMoveTracking.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(MOVE_PREFIX).append(" = '").append(String.valueOf(ioMoveTracking.getMove_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(MOVE_CODE).append(" = '").append(String.valueOf(ioMoveTracking.getMove_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TRACKING).append(" = '").append(String.valueOf(ioMoveTracking.getTracking())).append("'");

            addUpdateRet = db.delete(TABLE, sbWhere.toString(), null);

        }catch (SQLiteException e){
            //Chama metodo que baseado na exception gera obj de retorno setado como erro
            //e contendo msg de erro tratada.
            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage());
            //Gera arquivo de exception usando dados da exception e do obj de retorno
            ToolBox_Inf.registerException(
                    getClass().getName(),
                    new Exception(
                            e.getMessage() + "\n" + daoObjReturn.getErrorMsg()
                    )
            );

        }catch (Exception e){
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true);
            ToolBox_Inf.registerException(getClass().getName(), e);
        }finally {
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }

        closeDB();
        return daoObjReturn;
    }

    @Override
    public IO_Move_Tracking getByString(String sQuery) {
        IO_Move_Tracking io_move_tracking = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_move_tracking = toIO_Move_TrackingMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_move_tracking;
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
    public List<IO_Move_Tracking> query(String sQuery) {
        List<IO_Move_Tracking> io_move_trackings = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                IO_Move_Tracking uAux = toIO_Move_TrackingMapper.map(cursor);
                io_move_trackings.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_move_trackings;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> io_moves = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_moves.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_moves;
    }

    private class CursorIO_Move_TrackingMapper implements Mapper<Cursor, IO_Move_Tracking> {
        @Override
        public IO_Move_Tracking map(Cursor cursor) {
            IO_Move_Tracking io_move_tracking = new IO_Move_Tracking();
            //
            io_move_tracking.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            io_move_tracking.setMove_prefix(cursor.getInt(cursor.getColumnIndex(MOVE_PREFIX)));
            io_move_tracking.setMove_code(cursor.getInt(cursor.getColumnIndex(MOVE_CODE)));
            io_move_tracking.setTracking(cursor.getString(cursor.getColumnIndex(TRACKING)));
            //
            return io_move_tracking;

        }
    }

    private class IO_Move_TrackingToContentValuesMapper implements Mapper<IO_Move_Tracking, ContentValues> {
        @Override
        public ContentValues map(IO_Move_Tracking io_move_tracking) {
            ContentValues contentValues = new ContentValues();
            //
            if(io_move_tracking.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,io_move_tracking.getCustomer_code());
            }
            if(io_move_tracking.getMove_prefix() > -1){
                contentValues.put(MOVE_PREFIX,io_move_tracking.getMove_prefix());
            }
            if(io_move_tracking.getMove_code() > -1){
                contentValues.put(MOVE_CODE,io_move_tracking.getMove_code());
            }
            if(io_move_tracking.getTracking() != null){
                contentValues.put(TRACKING,io_move_tracking.getTracking());
            }
            return contentValues;
        }
    }
}
