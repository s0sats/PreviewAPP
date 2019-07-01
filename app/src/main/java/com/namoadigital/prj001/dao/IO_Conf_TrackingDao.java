package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Conf_Tracking;
import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class IO_Conf_TrackingDao extends BaseDao implements DaoWithReturn<IO_Conf_Tracking> {

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
    public DaoObjReturn addUpdate(IO_Conf_Tracking io_conf_tracking ) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        openDB();

        try{
            //
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(io_conf_tracking.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PREFIX).append(" = '").append(io_conf_tracking.getPrefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CODE).append(" = '").append(io_conf_tracking.getCode()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(ITEM).append(" = '").append(io_conf_tracking.getItem()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TYPE).append(" = '").append(io_conf_tracking.getType()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TRACKING).append(" = '").append(io_conf_tracking.getTracking()).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_conf_tracking), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_conf_tracking));
            }
            //
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
    public DaoObjReturn addUpdate(List<IO_Conf_Tracking> io_conf_trackings, boolean status) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        openDB();

        try {
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for(IO_Conf_Tracking io_conf_tracking :io_conf_trackings){
                //
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(io_conf_tracking.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PREFIX).append(" = '").append(io_conf_tracking.getPrefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CODE).append(" = '").append(io_conf_tracking.getCode()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(ITEM).append(" = '").append(io_conf_tracking.getItem()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TYPE).append(" = '").append(io_conf_tracking.getType()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TRACKING).append(" = '").append(io_conf_tracking.getTracking()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_conf_tracking), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_conf_tracking));
                }
            }

            db.setTransactionSuccessful();
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
            db.endTransaction();
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }

        closeDB();

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

    public DaoObjReturn removeByInboundItem(IO_Inbound_Item io_inbound_item, @Nullable SQLiteDatabase dbInstance){
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.DELETE;
        //
        if(dbInstance == null){
            openDB();
        }else{
            this.db = dbInstance;
        }
        try{
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(io_inbound_item.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PREFIX).append(" = '").append(io_inbound_item.getInbound_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CODE).append(" = '").append(io_inbound_item.getInbound_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(ITEM).append(" = '").append(io_inbound_item.getInbound_item()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TYPE).append(" = '").append(ConstantBaseApp.IO_INBOUND).append("'");
            //Tenta remoer e armazena retorno
            addUpdateRet = db.delete(TABLE, sbWhere.toString(), null);

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
    public IO_Conf_Tracking getByString(String sQuery) {
        IO_Conf_Tracking io_conf_tracking = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_conf_tracking = toIO_Conf_TrackingMapper.map(cursor);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_conf_tracking;
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
    public List<IO_Conf_Tracking> query(String sQuery) {
        List<IO_Conf_Tracking> io_conf_trackings = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                IO_Conf_Tracking uAux = toIO_Conf_TrackingMapper.map(cursor);
                //
                io_conf_trackings.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_conf_trackings;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> io_blind_moves = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_blind_moves.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_blind_moves;
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
