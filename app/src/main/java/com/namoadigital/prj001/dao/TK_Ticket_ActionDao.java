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
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class TK_Ticket_ActionDao extends BaseDao implements DaoWithReturn<TK_Ticket_Action>, DaoWithReturnSharedDbInstance<TK_Ticket_Action> {

    private final Mapper<TK_Ticket_Action, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, TK_Ticket_Action> toTK_Ticket_ActionMapper;

    public static final String TABLE = "tk_ticket_action";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String TICKET_SEQ = "ticket_seq";
    public static final String STEP_CODE = "step_code";
    public static final String ACTION_COMMENTS = "action_comments";
    public static final String ACTION_PHOTO = "action_photo";
    public static final String ACTION_PHOTO_LOCAL = "action_photo_local";
    public static final String ACTION_PHOTO_NAME = "action_photo_name";
    public static final String ACTION_STATUS = "action_status";
    public static final String ACTION_PHOTO_CHANGED = "action_photo_changed";
    public static final String ACTION_PHOTO_CODE = "action_photo_code";

    public TK_Ticket_ActionDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new TK_TicketActionToContentValues();
        this.toTK_Ticket_ActionMapper = new CursorToTk_Ticket_ActionMapper();
    }


    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Action tk_ticket_action) {
        return addUpdate(tk_ticket_action,null);
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Action tk_ticket_action, SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_action.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_action.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_action.getTicket_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_action.getTicket_seq()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_action.getStep_code()).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_action), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_action));
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
    public DaoObjReturn addUpdate(List<TK_Ticket_Action> tk_ticket_actions, boolean status) {
        return addUpdate(tk_ticket_actions,status,null);
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Action> tk_ticket_actions, boolean status, SQLiteDatabase dbInstance) {
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
            for (TK_Ticket_Action tk_ticket_action : tk_ticket_actions) {

                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_action.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_action.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_action.getTicket_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_action.getTicket_seq()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_action.getStep_code()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_action), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_action));
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
    public DaoObjReturn remove(TK_Ticket_Action tk_ticket_action, @Nullable SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_action.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_action.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_action.getTicket_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_action.getTicket_seq()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_action.getStep_code()).append("'");
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
    public TK_Ticket_Action getByString(String sQuery) {
        TK_Ticket_Action tk_ticket_action = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                tk_ticket_action = toTK_Ticket_ActionMapper.map(cursor);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return tk_ticket_action;
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
    public List<TK_Ticket_Action> query(String sQuery) {
        List<TK_Ticket_Action> tk_ticket_actions = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                TK_Ticket_Action uAux = toTK_Ticket_ActionMapper.map(cursor);
                tk_ticket_actions.add(uAux);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return tk_ticket_actions;
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


    private class CursorToTk_Ticket_ActionMapper implements Mapper<Cursor, TK_Ticket_Action> {
        @Override
        public TK_Ticket_Action map(Cursor cursor) {
            TK_Ticket_Action tk_ticket_action = new TK_Ticket_Action();
            //
            tk_ticket_action.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket_action.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket_action.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket_action.setTicket_seq(cursor.getInt(cursor.getColumnIndex(TICKET_SEQ)));
            tk_ticket_action.setStep_code(cursor.getInt(cursor.getColumnIndex(STEP_CODE)));
            if(cursor.isNull(cursor.getColumnIndex(ACTION_COMMENTS))){
                tk_ticket_action.setAction_comments(null);
            }else{
                tk_ticket_action.setAction_comments(cursor.getString(cursor.getColumnIndex(ACTION_COMMENTS)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ACTION_PHOTO))){
                tk_ticket_action.setAction_photo_url(null);
            }else{
                tk_ticket_action.setAction_photo_url(cursor.getString(cursor.getColumnIndex(ACTION_PHOTO)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ACTION_PHOTO_LOCAL))){
                tk_ticket_action.setAction_photo_local(null);
            }else{
                tk_ticket_action.setAction_photo_local(cursor.getString(cursor.getColumnIndex(ACTION_PHOTO_LOCAL)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ACTION_PHOTO_NAME))){
                tk_ticket_action.setAction_photo_name(null);
            }else{
                tk_ticket_action.setAction_photo_name(cursor.getString(cursor.getColumnIndex(ACTION_PHOTO_NAME)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ACTION_STATUS))){
                tk_ticket_action.setAction_status(null);
            }else{
                tk_ticket_action.setAction_status(cursor.getString(cursor.getColumnIndex(ACTION_STATUS)));
            }
            tk_ticket_action.setAction_photo_changed(cursor.getInt(cursor.getColumnIndex(ACTION_PHOTO_CHANGED)));
            if(cursor.isNull(cursor.getColumnIndex(ACTION_PHOTO_CODE))){
                tk_ticket_action.setAction_photo_code(null);
            }else{
                tk_ticket_action.setAction_photo_code(cursor.getInt(cursor.getColumnIndex(ACTION_PHOTO_CODE)));
            }
            //
            return tk_ticket_action;
        }
    }

    private class TK_TicketActionToContentValues implements Mapper<TK_Ticket_Action, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket_Action tk_ticket_action) {
            ContentValues contentValues = new ContentValues();
            //
            if (tk_ticket_action.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE,tk_ticket_action.getCustomer_code());
            }
            if (tk_ticket_action.getTicket_prefix() > -1) {
                contentValues.put(TICKET_PREFIX,tk_ticket_action.getTicket_prefix());
            }
            if (tk_ticket_action.getTicket_code() > -1) {
                contentValues.put(TICKET_CODE,tk_ticket_action.getTicket_code());
            }
            if (tk_ticket_action.getTicket_seq() > -1) {
                contentValues.put(TICKET_SEQ,tk_ticket_action.getTicket_seq());
            }
            if (tk_ticket_action.getStep_code() > -1) {
                contentValues.put(STEP_CODE,tk_ticket_action.getStep_code());
            }
            contentValues.put(ACTION_COMMENTS,tk_ticket_action.getAction_comments());
            contentValues.put(ACTION_PHOTO,tk_ticket_action.getAction_photo_url());
            contentValues.put(ACTION_PHOTO_LOCAL,tk_ticket_action.getAction_photo_local());
            contentValues.put(ACTION_PHOTO_NAME,tk_ticket_action.getAction_photo_name());
            if (tk_ticket_action.getAction_photo_changed() > -1) {
                contentValues.put(ACTION_PHOTO_CHANGED,tk_ticket_action.getAction_photo_changed());
            }
            contentValues.put(ACTION_STATUS,tk_ticket_action.getAction_status());
            contentValues.put(ACTION_PHOTO_CODE,tk_ticket_action.getAction_photo_code());
            //
            return contentValues;
        }
    }
}
