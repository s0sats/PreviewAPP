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
import com.namoadigital.prj001.model.TK_Ticket_Measure;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class TK_Ticket_MeasureDao extends BaseDao implements DaoWithReturn<TK_Ticket_Measure>, DaoWithReturnSharedDbInstance<TK_Ticket_Measure> {

    private final Mapper<TK_Ticket_Measure, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, TK_Ticket_Measure> toTK_Ticket_MeasureMapper;

    public static final String TABLE = "tk_ticket_measure";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String TICKET_SEQ = "ticket_seq";
    public static final String MEASURE_TP_CODE = "measure_tp_code";
    public static final String MEASURE_TP_ID = "measure_tp_id";
    public static final String MEASURE_TP_DESC = "measure_tp_desc";
    public static final String MEASURE_VALUE = "measure_value";
    public static final String VALUE_SUFIX = "value_sufix";
    public static final String MEASURE_DATE = "measure_date";
    public static final String MEASURE_INFO = "measure_info";

    public TK_Ticket_MeasureDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new TK_Ticket_MeasureToContentValues();
        this.toTK_Ticket_MeasureMapper = new CursorToTK_Ticket_MeasureMapper();
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Measure tk_ticket_measure) {
        return addUpdate(tk_ticket_measure,null) ;
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Measure tk_ticket_measure, SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        if(dbInstance == null) {
            openDB();
        }else{
            this.db = dbInstance;
        }

        try{
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_measure.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_measure.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_measure.getTicket_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_measure.getTicket_seq()).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_measure), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_measure));
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
        //
        if(dbInstance == null){
            closeDB();
        }

        return daoObjReturn;
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Measure> tk_ticket_measures, boolean status) {
        return addUpdate(tk_ticket_measures,status,null);
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Measure> tk_ticket_measures, boolean status, SQLiteDatabase dbInstance) {
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
            //Se db não foi passado, inicializa transaction
            if(dbInstance == null) {
                db.beginTransaction();
            }

            if (status) {
                db.delete(TABLE, null, null);
            }
            //
            for (TK_Ticket_Measure tk_ticket_measure : tk_ticket_measures) {

                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_measure.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_measure.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_measure.getTicket_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_measure.getTicket_seq()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_measure), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_measure));
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
    public DaoObjReturn remove(TK_Ticket_Measure tk_ticket_measure, @Nullable SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_measure.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_measure.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_measure.getTicket_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_measure.getTicket_seq()).append("'");
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
    public TK_Ticket_Measure getByString(String sQuery) {
        TK_Ticket_Measure tk_ticket_measure = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                tk_ticket_measure = toTK_Ticket_MeasureMapper.map(cursor);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return tk_ticket_measure;
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
    public List<TK_Ticket_Measure> query(String sQuery) {
        List<TK_Ticket_Measure> tk_ticket_measures = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                TK_Ticket_Measure uAux = toTK_Ticket_MeasureMapper.map(cursor);
                tk_ticket_measures.add(uAux);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return tk_ticket_measures;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> tk_ticket_actions = new ArrayList<>();
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                tk_ticket_actions.add(CursorToHMAuxMapper.mapN(cursor));
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return tk_ticket_actions;
    }

    private class CursorToTK_Ticket_MeasureMapper implements Mapper<Cursor, TK_Ticket_Measure> {
        @Override
        public TK_Ticket_Measure map(Cursor cursor) {
            TK_Ticket_Measure tk_ticket_measure = new TK_Ticket_Measure();
            //
            tk_ticket_measure.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket_measure.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket_measure.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket_measure.setTicket_seq(cursor.getInt(cursor.getColumnIndex(TICKET_SEQ)));
            tk_ticket_measure.setMeasure_tp_code(cursor.getInt(cursor.getColumnIndex(MEASURE_TP_CODE)));
            tk_ticket_measure.setMeasure_tp_id(cursor.getString(cursor.getColumnIndex(MEASURE_TP_ID)));
            tk_ticket_measure.setMeasure_tp_desc(cursor.getString(cursor.getColumnIndex(MEASURE_TP_DESC)));
            tk_ticket_measure.setMeasure_value(cursor.getInt(cursor.getColumnIndex(MEASURE_VALUE)));
            if(cursor.isNull(cursor.getColumnIndex(VALUE_SUFIX))) {
                tk_ticket_measure.setValue_sufix(null);
            }else{
                tk_ticket_measure.setValue_sufix(cursor.getString(cursor.getColumnIndex(VALUE_SUFIX)));
            }
            tk_ticket_measure.setMeasure_date(cursor.getString(cursor.getColumnIndex(MEASURE_DATE)));
            if(cursor.isNull(cursor.getColumnIndex(MEASURE_INFO))) {
                tk_ticket_measure.setMeasure_info(null);
            }else{
                tk_ticket_measure.setMeasure_info(cursor.getString(cursor.getColumnIndex(MEASURE_INFO)));
            }
            //
            return tk_ticket_measure;
        }
    }


    private class TK_Ticket_MeasureToContentValues implements Mapper<TK_Ticket_Measure, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket_Measure tk_ticket_measure) {
            ContentValues contentValues = new ContentValues();
            //
            if(tk_ticket_measure.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE, tk_ticket_measure.getCustomer_code());
            }
            if(tk_ticket_measure.getTicket_prefix() > -1){
                contentValues.put(TICKET_PREFIX, tk_ticket_measure.getTicket_prefix());
            }
            if(tk_ticket_measure.getTicket_code() > -1){
                contentValues.put(TICKET_CODE, tk_ticket_measure.getTicket_code());
            }
            if(tk_ticket_measure.getTicket_seq() > -1){
                contentValues.put(TICKET_SEQ, tk_ticket_measure.getTicket_seq());
            }
            if(tk_ticket_measure.getMeasure_tp_code() > -1){
                contentValues.put(MEASURE_TP_CODE, tk_ticket_measure.getMeasure_tp_code());
            }
            if(tk_ticket_measure.getMeasure_tp_id() != null){
                contentValues.put(MEASURE_TP_ID, tk_ticket_measure.getMeasure_tp_id());
            }
            if(tk_ticket_measure.getMeasure_tp_desc() != null){
                contentValues.put(MEASURE_TP_DESC, tk_ticket_measure.getMeasure_tp_desc());
            }
            if(tk_ticket_measure.getMeasure_value() > -1){
                contentValues.put(MEASURE_VALUE, tk_ticket_measure.getMeasure_value());
            }
            contentValues.put(VALUE_SUFIX, tk_ticket_measure.getValue_sufix());
            if(tk_ticket_measure.getMeasure_date()!= null){
                contentValues.put(MEASURE_DATE, tk_ticket_measure.getMeasure_date());
            }
            //
            contentValues.put(MEASURE_INFO, tk_ticket_measure.getMeasure_info());
            //
            return contentValues;
        }
    }
}
