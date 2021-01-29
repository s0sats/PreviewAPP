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
import com.namoadigital.prj001.model.TK_Ticket_Approval_Rejection;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class TK_Ticket_Approval_RejectionDao extends BaseDao implements DaoWithReturn<TK_Ticket_Approval_Rejection>, DaoWithReturnSharedDbInstance<TK_Ticket_Approval_Rejection>  {

    private final Mapper<TK_Ticket_Approval_Rejection, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor,TK_Ticket_Approval_Rejection> toTK_Ticket_Approval_RejectionMapper;

    public static final String TABLE = "tk_ticket_approval_rejection";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String TICKET_SEQ = "ticket_seq";
    public static final String STEP_CODE = "step_code";
    public static final String SEQ = "seq";
    public static final String REJECTION_COMMENTS = "rejection_comments";
    public static final String REJECTION_DATE = "rejection_date";
    public static final String REJECTION_USER = "rejection_user";
    public static final String REJECTION_USER_NICK = "rejection_user_nick";

    public TK_Ticket_Approval_RejectionDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        this.toContentValuesMapper = new TK_Ticket_Approval_RejectionToContentValuesMapper() ;
        this.toTK_Ticket_Approval_RejectionMapper = new CursorToTK_Ticket_Approval_RejectionMapper();
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Approval_Rejection tk_ticket_approval_rejection) {
        return addUpdate(tk_ticket_approval_rejection,null);
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Approval_Rejection> tk_ticket_approval_rejections, boolean status) {
        return addUpdate(tk_ticket_approval_rejections,status,null);
    }
    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Approval_Rejection tk_ticket_approval_rejection, SQLiteDatabase dbInstance) {
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
            StringBuilder sbWhere = getWherePkClause(tk_ticket_approval_rejection);
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_approval_rejection), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_approval_rejection));
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

    private StringBuilder getWherePkClause(TK_Ticket_Approval_Rejection tk_ticket_approval_rejection) {
        return
            new StringBuilder()
                .append(CUSTOMER_CODE).append(" = '").append(tk_ticket_approval_rejection.getCustomer_code()).append("'")
                .append(" and ")
                .append(TICKET_PREFIX).append(" = '").append(tk_ticket_approval_rejection.getTicket_prefix()).append("'")
                .append(" and ")
                .append(TICKET_CODE).append(" = '").append(tk_ticket_approval_rejection.getTicket_code()).append("'")
                .append(" and ")
                .append(TICKET_SEQ).append(" = '").append(tk_ticket_approval_rejection.getTicket_seq()).append("'")
                .append(" and ")
                .append(STEP_CODE).append(" = '").append(tk_ticket_approval_rejection.getStep_code()).append("'")
                .append(" and ")
                .append(SEQ).append(" = '").append(tk_ticket_approval_rejection.getSeq()).append("'");
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Approval_Rejection> tk_ticket_approval_rejections, boolean status, SQLiteDatabase dbInstance) {
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
            for (TK_Ticket_Approval_Rejection tk_ticket_approval_rejection : tk_ticket_approval_rejections) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = getWherePkClause(tk_ticket_approval_rejection);
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_approval_rejection), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_approval_rejection));
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
    public DaoObjReturn remove(TK_Ticket_Approval_Rejection tk_ticket_approval_rejection, @Nullable SQLiteDatabase dbInstance) {
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
            StringBuilder sbWhere = getWherePkClause(tk_ticket_approval_rejection);
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
    public TK_Ticket_Approval_Rejection getByString(String sQuery) {
        TK_Ticket_Approval_Rejection tk_ticket_approval_rejection = null;
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                tk_ticket_approval_rejection = toTK_Ticket_Approval_RejectionMapper.map(cursor);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }
        closeDB();
        return tk_ticket_approval_rejection;
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
    public List<TK_Ticket_Approval_Rejection> query(String sQuery) {
        List<TK_Ticket_Approval_Rejection> tk_ticket_approval_rejections = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                TK_Ticket_Approval_Rejection uAux = toTK_Ticket_Approval_RejectionMapper.map(cursor);
                tk_ticket_approval_rejections.add(uAux);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }
        closeDB();
        return tk_ticket_approval_rejections;
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

    private class CursorToTK_Ticket_Approval_RejectionMapper implements Mapper<Cursor, TK_Ticket_Approval_Rejection> {
        @Override
        public TK_Ticket_Approval_Rejection map(Cursor cursor) {
            TK_Ticket_Approval_Rejection tk_ticket_approval_rejection = new TK_Ticket_Approval_Rejection();
            tk_ticket_approval_rejection.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket_approval_rejection.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket_approval_rejection.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket_approval_rejection.setTicket_seq(cursor.getInt(cursor.getColumnIndex(TICKET_SEQ)));
            tk_ticket_approval_rejection.setStep_code(cursor.getInt(cursor.getColumnIndex(STEP_CODE)));
            tk_ticket_approval_rejection.setSeq(cursor.getInt(cursor.getColumnIndex(SEQ)));
            tk_ticket_approval_rejection.setRejection_comments(cursor.getString(cursor.getColumnIndex(REJECTION_COMMENTS)));
            tk_ticket_approval_rejection.setRejection_date(cursor.getString(cursor.getColumnIndex(REJECTION_DATE)));
            tk_ticket_approval_rejection.setRejection_user(cursor.getInt(cursor.getColumnIndex(REJECTION_USER)));
            tk_ticket_approval_rejection.setRejection_user_nick(cursor.getString(cursor.getColumnIndex(REJECTION_USER_NICK)));
            return tk_ticket_approval_rejection;
        }
    }

    private class TK_Ticket_Approval_RejectionToContentValuesMapper implements Mapper<TK_Ticket_Approval_Rejection, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket_Approval_Rejection tk_ticket_approval_rejection) {
            ContentValues contentValues = new ContentValues();
            //
            if (tk_ticket_approval_rejection.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE,tk_ticket_approval_rejection.getCustomer_code());
            }
            if (tk_ticket_approval_rejection.getTicket_prefix() > -1) {
                contentValues.put(TICKET_PREFIX,tk_ticket_approval_rejection.getTicket_prefix());
            }
            if (tk_ticket_approval_rejection.getTicket_code() > -1) {
                contentValues.put(TICKET_CODE,tk_ticket_approval_rejection.getTicket_code());
            }
            if (tk_ticket_approval_rejection.getTicket_seq() > -1) {
                contentValues.put(TICKET_SEQ,tk_ticket_approval_rejection.getTicket_seq());
            }
            if (tk_ticket_approval_rejection.getStep_code() > -1) {
                contentValues.put(STEP_CODE,tk_ticket_approval_rejection.getStep_code());
            }
            if (tk_ticket_approval_rejection.getSeq() > -1) {
                contentValues.put(SEQ,tk_ticket_approval_rejection.getSeq());
            }
            if (tk_ticket_approval_rejection.getRejection_comments() != null) {
                contentValues.put(REJECTION_COMMENTS,tk_ticket_approval_rejection.getRejection_comments());
            }
            if (tk_ticket_approval_rejection.getRejection_date() != null) {
                contentValues.put(REJECTION_DATE,tk_ticket_approval_rejection.getRejection_date());
            }
            if (tk_ticket_approval_rejection.getRejection_user() > -1) {
                contentValues.put(REJECTION_USER,tk_ticket_approval_rejection.getRejection_user());
            }
            if (tk_ticket_approval_rejection.getRejection_user_nick() != null) {
                contentValues.put(REJECTION_USER_NICK,tk_ticket_approval_rejection.getRejection_user_nick());
            }

            return contentValues;
        }
    }
}
