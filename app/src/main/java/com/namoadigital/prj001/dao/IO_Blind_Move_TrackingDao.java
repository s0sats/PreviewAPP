package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import androidx.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Blind_Move_Tracking;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class IO_Blind_Move_TrackingDao extends BaseDao implements
    DaoWithReturn<IO_Blind_Move_Tracking>,
    DaoWithReturnSharedDbInstance<IO_Blind_Move_Tracking>{

    private final Mapper<IO_Blind_Move_Tracking, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, IO_Blind_Move_Tracking> toIO_Blind_Move_TrackingMapper;

    public static final String TABLE = "io_blind_move_tracking";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String BLIND_TMP = "blind_tmp";
    public static final String TRACKING = "tracking";

    public IO_Blind_Move_TrackingDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new IO_Blind_Move_TrackingToContentValuesMapper();
        this.toIO_Blind_Move_TrackingMapper = new CursorToIO_Blind_Move_TrackingMapper();
    }


    @Override
    public DaoObjReturn addUpdate(IO_Blind_Move_Tracking io_blind_move_tracking, SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        if(dbInstance == null){
            openDB();
        }else{
            this.db = dbInstance;
        }

        try{
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf( io_blind_move_tracking.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(BLIND_TMP).append(" = '").append(String.valueOf( io_blind_move_tracking.getBlind_tmp())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TRACKING).append(" = '").append(String.valueOf( io_blind_move_tracking.getTracking())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map( io_blind_move_tracking), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map( io_blind_move_tracking));
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

        if(dbInstance == null){
            closeDB();
        }

        return daoObjReturn;
    }

    @Override
    public DaoObjReturn addUpdate(IO_Blind_Move_Tracking io_blind_move_tracking) {
        return  addUpdate(io_blind_move_tracking,null);
    }

    @Override
    public DaoObjReturn addUpdate(List<IO_Blind_Move_Tracking> io_blind_move_trackings, boolean status) {
        return  addUpdate(io_blind_move_trackings,status,null);
    }

    @Override
    public DaoObjReturn addUpdate(List<IO_Blind_Move_Tracking> io_blind_move_trackings, boolean status, SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
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

            for(IO_Blind_Move_Tracking io_blind_move_tracking :io_blind_move_trackings){
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf( io_blind_move_tracking.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(BLIND_TMP).append(" = '").append(String.valueOf( io_blind_move_tracking.getBlind_tmp())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TRACKING).append(" = '").append(String.valueOf( io_blind_move_tracking.getTracking())).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map( io_blind_move_tracking), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map( io_blind_move_tracking));
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
    public DaoObjReturn remove(IO_Blind_Move_Tracking io_blind_move_tracking, @Nullable SQLiteDatabase dbInstance) {

        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long sqlRet = 0;
        String curAction = DaoObjReturn.DELETE;
        //
        if(dbInstance == null){
            openDB();
        }else{
            this.db = dbInstance;
        }

        try{
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_blind_move_tracking.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(BLIND_TMP).append(" = '").append(String.valueOf(io_blind_move_tracking.getBlind_tmp())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TRACKING).append(" = '").append(String.valueOf(io_blind_move_tracking.getTracking())).append("'");
            //
            sqlRet = db.delete(TABLE,sbWhere.toString(),null);
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
            //Atualiza ação realizada no metodo e informação de qtd de registros alterados.
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(sqlRet);
        }
        //
        if(dbInstance == null){
            closeDB();
        }
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

    @Override
    public IO_Blind_Move_Tracking getByString(String sQuery) {

        IO_Blind_Move_Tracking io_blind_move_tracking = null;

        openDB();
        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_blind_move_tracking = toIO_Blind_Move_TrackingMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_blind_move_tracking;
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
    public List<IO_Blind_Move_Tracking> query(String sQuery) {
        List<IO_Blind_Move_Tracking> io_blind_move_trackings = new ArrayList<>();
        //
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                IO_Blind_Move_Tracking uAux = toIO_Blind_Move_TrackingMapper.map(cursor);
                io_blind_move_trackings.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_blind_move_trackings;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> io_blind_move_trackings = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_blind_move_trackings.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);

        } finally {
        }

        closeDB();

        return io_blind_move_trackings;
    }

    private class CursorToIO_Blind_Move_TrackingMapper implements Mapper<Cursor, IO_Blind_Move_Tracking> {
        @Override
        public IO_Blind_Move_Tracking map(Cursor cursor) {
            IO_Blind_Move_Tracking io_blind_move_tracking = new IO_Blind_Move_Tracking();
            //
            io_blind_move_tracking.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            io_blind_move_tracking.setBlind_tmp(cursor.getInt(cursor.getColumnIndex(BLIND_TMP)));
            io_blind_move_tracking.setTracking(cursor.getString(cursor.getColumnIndex(TRACKING)));
            //
            return io_blind_move_tracking;
        }
    }

    private class IO_Blind_Move_TrackingToContentValuesMapper implements Mapper<IO_Blind_Move_Tracking, ContentValues> {
        @Override
        public ContentValues map(IO_Blind_Move_Tracking io_blind_move_tracking) {
            ContentValues contentValues = new ContentValues();
            //
            if(io_blind_move_tracking.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,io_blind_move_tracking.getCustomer_code());
            }
            if(io_blind_move_tracking.getBlind_tmp() > -1){
                contentValues.put(BLIND_TMP,io_blind_move_tracking.getBlind_tmp());
            }
            if(io_blind_move_tracking.getTracking()!= null){
                contentValues.put(TRACKING,io_blind_move_tracking.getTracking());
            }
            //
            return contentValues;
        }
    }
}
