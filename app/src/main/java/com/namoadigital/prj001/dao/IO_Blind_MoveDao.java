package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.IO_Blind_Move;
import com.namoadigital.prj001.model.IO_Blind_Move_Tracking;
import com.namoadigital.prj001.sql.IO_Blind_Move_Tracking_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class IO_Blind_MoveDao extends BaseDao implements DaoWithReturn<IO_Blind_Move> {

    private final Mapper<IO_Blind_Move, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor,IO_Blind_Move> toIO_Blind_MoveMapper;

    public static final String TABLE = "io_blind_move";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String BLIND_TMP = "blind_tmp";
    public static final String PRODUCT_CODE = "product_code";
    public static final String SERIAL_CODE = "serial_code";
    public static final String SERIAL_ID = "serial_id";
    public static final String SITE_CODE = "site_code";
    public static final String ZONE_CODE = "zone_code";
    public static final String LOCAL_CODE = "local_code";
    public static final String REASON_CODE = "reason_code";
    public static final String CLASS_CODE = "class_code";
    public static final String FLAG_BLIND = "flag_blind";
    public static final String STATUS = "status";
    public static final String SAVE_DATE = "save_date";
    public static final String TOKEN = "token";
    public static final String ERROR_MSG = "error_msg";


    public IO_Blind_MoveDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new IO_Blind_MovetoContentValuesMapper();
        this.toIO_Blind_MoveMapper = new CursorToIO_Blind_MoveMapper();
    }

    @Override
    public DaoObjReturn addUpdate(IO_Blind_Move io_blind_move) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        openDB();

        try{
            db.beginTransaction();
            //
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf( io_blind_move.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(BLIND_TMP).append(" = '").append(String.valueOf( io_blind_move.getBlind_tmp())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf( io_blind_move.getProduct_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SERIAL_ID).append(" = '").append(String.valueOf( io_blind_move.getSerial_id())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map( io_blind_move), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map( io_blind_move));
            }
            //Se operação de insert ou update executada com sucesso
            //Segue para inserção dos tracking.
            IO_Blind_Move_TrackingDao blindMoveTrackingDao  = new IO_Blind_Move_TrackingDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
            //Chama insertUpdate de lista de item,passando db como param aguardando retorno.
            DaoObjReturn inboundItemRet = blindMoveTrackingDao.addUpdate(io_blind_move.getTracking(),false,db);
            //Se erro durante insert, dispara exception abortando o processamento.
            if(inboundItemRet.hasError()){
                throw new Exception(inboundItemRet.getErrorMsg());
            }
            //
            db.setTransactionSuccessful();
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
            db.endTransaction();
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }

        closeDB();

        return daoObjReturn;
    }

    @Override
    public DaoObjReturn addUpdate(List<IO_Blind_Move> io_blind_moves, boolean status) {
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

            for(IO_Blind_Move io_blind_move :io_blind_moves){
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf( io_blind_move.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(BLIND_TMP).append(" = '").append(String.valueOf( io_blind_move.getBlind_tmp())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(String.valueOf( io_blind_move.getProduct_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SERIAL_ID).append(" = '").append(String.valueOf( io_blind_move.getSerial_id())).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map( io_blind_move), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map( io_blind_move));
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

    @Override
    public IO_Blind_Move getByString(String sQuery) {
        IO_Blind_Move io_blind_move = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_blind_move = toIO_Blind_MoveMapper.map(cursor);
            }
            //
            if(io_blind_move != null){
                //Se operação de insert ou update executada com sucesso
                //Segue para inserção dos itens.
                IO_Blind_Move_TrackingDao blindMoveTrackingDao  = new IO_Blind_Move_TrackingDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                //
                io_blind_move.setTracking(
                    (ArrayList<IO_Blind_Move_Tracking>) blindMoveTrackingDao.query(
                       new IO_Blind_Move_Tracking_Sql_001(
                           io_blind_move.getCustomer_code(),
                           io_blind_move.getBlind_tmp()
                       ).toSqlQuery()

                   )
                );
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_blind_move;
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
    public List<IO_Blind_Move> query(String sQuery) {
        List<IO_Blind_Move> io_blind_moves = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                IO_Blind_Move uAux = toIO_Blind_MoveMapper.map(cursor);
                io_blind_moves.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_blind_moves;

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

    private class CursorToIO_Blind_MoveMapper implements Mapper<Cursor, IO_Blind_Move> {
        @Override
        public IO_Blind_Move map(Cursor cursor) {
            IO_Blind_Move io_blind_move = new IO_Blind_Move();
            //
            io_blind_move.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            io_blind_move.setBlind_tmp(cursor.getInt(cursor.getColumnIndex(BLIND_TMP)));
            io_blind_move.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
            io_blind_move.setSerial_code(cursor.getInt(cursor.getColumnIndex(SERIAL_CODE)));
            io_blind_move.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            io_blind_move.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            io_blind_move.setZone_code(cursor.getInt(cursor.getColumnIndex(ZONE_CODE)));
            io_blind_move.setLocal_code(cursor.getInt(cursor.getColumnIndex(LOCAL_CODE)));
            io_blind_move.setReason_code(cursor.getInt(cursor.getColumnIndex(REASON_CODE)));
            if(cursor.isNull(cursor.getColumnIndex(CLASS_CODE))){
                io_blind_move.setClass_code(null);
            }else{
                io_blind_move.setClass_code(cursor.getInt(cursor.getColumnIndex(CLASS_CODE)));
            }
            io_blind_move.setFlag_blind(cursor.getInt(cursor.getColumnIndex(FLAG_BLIND)));
            io_blind_move.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            io_blind_move.setSave_date(cursor.getString(cursor.getColumnIndex(SAVE_DATE)));
            if(cursor.isNull(cursor.getColumnIndex(TOKEN))){
                io_blind_move.setToken(null);
            }else{
                io_blind_move.setToken(cursor.getString(cursor.getColumnIndex(TOKEN)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(ERROR_MSG))){
                io_blind_move.setError_msg(null);
            }else{
                io_blind_move.setError_msg(cursor.getString(cursor.getColumnIndex(ERROR_MSG)));
            }
            //
            return io_blind_move;
        }
    }


    private class IO_Blind_MovetoContentValuesMapper implements Mapper<IO_Blind_Move, ContentValues> {
        @Override
        public ContentValues map(IO_Blind_Move io_blind_move) {
            ContentValues contentValues = new ContentValues();

            if(io_blind_move.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,io_blind_move.getCustomer_code());
            }
            if(io_blind_move.getBlind_tmp() > -1){
                contentValues.put(BLIND_TMP,io_blind_move.getBlind_tmp());
            }
            if(io_blind_move.getProduct_code() > -1){
                contentValues.put(PRODUCT_CODE,io_blind_move.getProduct_code());
            }
            if(io_blind_move.getSerial_code() > -1){
                contentValues.put(SERIAL_CODE,io_blind_move.getSerial_code());
            }
            if(io_blind_move.getSerial_id() != null){
                contentValues.put(SERIAL_ID,io_blind_move.getSerial_id());
            }
            if(io_blind_move.getSite_code() > -1){
                contentValues.put(SITE_CODE,io_blind_move.getSite_code());
            }
            if(io_blind_move.getZone_code() > -1){
                contentValues.put(ZONE_CODE,io_blind_move.getZone_code());
            }
            if(io_blind_move.getLocal_code() > -1){
                contentValues.put(LOCAL_CODE,io_blind_move.getLocal_code());
            }
            if(io_blind_move.getReason_code() > -1){
                contentValues.put(REASON_CODE,io_blind_move.getReason_code());
            }
            contentValues.put(CLASS_CODE,io_blind_move.getClass_code());
            if(io_blind_move.getFlag_blind() > -1){
                contentValues.put(FLAG_BLIND,io_blind_move.getFlag_blind());
            }
            if(io_blind_move.getStatus() != null){
                contentValues.put(STATUS,io_blind_move.getStatus());
            }
            if(io_blind_move.getSave_date() != null){
                contentValues.put(SAVE_DATE,io_blind_move.getSave_date());
            }
            //
            contentValues.put(TOKEN,io_blind_move.getToken());
            contentValues.put(ERROR_MSG,io_blind_move.getError_msg());
            //
            return contentValues;
        }
    }

}
