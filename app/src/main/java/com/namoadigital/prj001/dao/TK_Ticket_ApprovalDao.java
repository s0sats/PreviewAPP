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
import com.namoadigital.prj001.model.TK_Ticket_Approval;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class TK_Ticket_ApprovalDao extends BaseDao implements DaoWithReturn<TK_Ticket_Approval>, DaoWithReturnSharedDbInstance<TK_Ticket_Approval>  {

    private final Mapper<TK_Ticket_Approval, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor,TK_Ticket_Approval> toTK_Ticket_ApprovalMapper;

    public static final String TABLE = "tk_ticket_approval";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String TICKET_SEQ = "ticket_seq";
    public static final String STEP_CODE = "step_code";
    public static final String APPROVAL_STATUS = "approval_status";
    public static final String APPROVAL_QUESTION = "approval_question";
    public static final String APPROVAL_TYPE = "approval_type";
    public static final String APPROVAL_COMMENTS = "approval_comments";

    public TK_Ticket_ApprovalDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        this. toTK_Ticket_ApprovalMapper = new CursorToTK_Ticket_ApprovalMapper();
        this.toContentValuesMapper = new TK_Ticket_ApprovalToContentValueMapper();
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Approval tk_ticket_approval) {
        return addUpdate(tk_ticket_approval,null);
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Approval> tk_ticket_approvals, boolean status) {
        return addUpdate(tk_ticket_approvals,status,null);
    }
    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Approval tk_ticket_approval, SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_approval.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_approval.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_approval.getTicket_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_approval.getTicket_seq()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_approval.getStep_code()).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_approval), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_approval));
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
    public DaoObjReturn addUpdate(List<TK_Ticket_Approval> tk_ticket_approvals, boolean status, SQLiteDatabase dbInstance) {
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
            for (TK_Ticket_Approval tk_ticket_approval : tk_ticket_approvals) {

                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_approval.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_approval.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_approval.getTicket_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_approval.getTicket_seq()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_approval.getStep_code()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_approval), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_approval));
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
    public DaoObjReturn remove(TK_Ticket_Approval tk_ticket_approval, @Nullable SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_approval.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_approval.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_approval.getTicket_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_approval.getTicket_seq()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_approval.getStep_code()).append("'");
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
    public TK_Ticket_Approval getByString(String sQuery) {
        TK_Ticket_Approval tk_ticket_approval = null;
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                tk_ticket_approval = toTK_Ticket_ApprovalMapper.map(cursor);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }
        closeDB();
        return tk_ticket_approval;
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
    public List<TK_Ticket_Approval> query(String sQuery) {
        List<TK_Ticket_Approval> tk_ticket_approvals = new ArrayList<>();;
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                TK_Ticket_Approval uAux = toTK_Ticket_ApprovalMapper.map(cursor);
                tk_ticket_approvals.add(uAux);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }
        closeDB();
        return tk_ticket_approvals;
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

    private class CursorToTK_Ticket_ApprovalMapper implements Mapper<Cursor, TK_Ticket_Approval> {
        @Override
        public TK_Ticket_Approval map(Cursor cursor) {
            TK_Ticket_Approval tk_ticket_approval = new TK_Ticket_Approval();
            tk_ticket_approval.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket_approval.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket_approval.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket_approval.setTicket_seq(cursor.getInt(cursor.getColumnIndex(TICKET_SEQ)));
            tk_ticket_approval.setStep_code(cursor.getInt(cursor.getColumnIndex(STEP_CODE)));
            tk_ticket_approval.setApproval_status(cursor.getString(cursor.getColumnIndex(APPROVAL_STATUS)));
            if (cursor.isNull(cursor.getColumnIndex(APPROVAL_QUESTION))) {
                tk_ticket_approval.setApproval_question(null);
            } else {
                tk_ticket_approval.setApproval_question(cursor.getString(cursor.getColumnIndex(APPROVAL_QUESTION)));
            }
            if (cursor.isNull(cursor.getColumnIndex(APPROVAL_TYPE))) {
                tk_ticket_approval.setApproval_type(null);
            } else {
                tk_ticket_approval.setApproval_type(cursor.getString(cursor.getColumnIndex(APPROVAL_TYPE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(APPROVAL_COMMENTS))) {
                tk_ticket_approval.setApproval_comments(null);
            } else {
                tk_ticket_approval.setApproval_comments(cursor.getString(cursor.getColumnIndex(APPROVAL_COMMENTS)));
            }
            return tk_ticket_approval;
        }
    }

    private class TK_Ticket_ApprovalToContentValueMapper implements Mapper<TK_Ticket_Approval, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket_Approval tk_ticket_approval) {
            ContentValues contentValues = new ContentValues();
            //
            if (tk_ticket_approval.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE,tk_ticket_approval.getCustomer_code());
            }
            if (tk_ticket_approval.getTicket_prefix() > -1) {
                contentValues.put(TICKET_PREFIX,tk_ticket_approval.getTicket_prefix());
            }
            if (tk_ticket_approval.getTicket_code() > -1) {
                contentValues.put(TICKET_CODE,tk_ticket_approval.getTicket_code());
            }
            if (tk_ticket_approval.getTicket_seq() > -1) {
                contentValues.put(TICKET_SEQ,tk_ticket_approval.getTicket_seq());
            }
            if (tk_ticket_approval.getStep_code() > -1) {
                contentValues.put(STEP_CODE,tk_ticket_approval.getStep_code());
            }
            if (tk_ticket_approval.getApproval_status() != null) {
                contentValues.put(APPROVAL_STATUS,tk_ticket_approval.getApproval_status());
            }
            contentValues.put(APPROVAL_QUESTION, tk_ticket_approval.getApproval_question());
            contentValues.put(APPROVAL_TYPE, tk_ticket_approval.getApproval_type());
            contentValues.put(APPROVAL_COMMENTS, tk_ticket_approval.getApproval_comments());
            return contentValues;
        }
    }
}
