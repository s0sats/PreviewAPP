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
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class TK_Ticket_StepDao extends BaseDao implements DaoWithReturn<TK_Ticket_Step> {
    private final Mapper<TK_Ticket_Step, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, TK_Ticket_Step> toTK_Ticket_StepMapper;

    public static final String TABLE = "tk_ticket_step";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String STEP_CODE = "step_code";
    public static final String STEP_ID = "step_id";
    public static final String STEP_DESC = "step_desc";
    public static final String STEP_ORDER = "step_order";
    public static final String FORECAST_START = "forecast_start";
    public static final String FORECAST_END = "forecast_end";
    public static final String EXEC_TYPE = "exec_type";
    public static final String SCAN_SERIAL = "scan_serial";
    public static final String ALLOW_NEW_OBJ = "allow_new_obj";
    public static final String PARTNER_CODE = "partner_code";
    public static final String PARTNER_ID = "partner_id";
    public static final String PARTNER_DESC = "partner_desc";
    public static final String AP_QUESTION = "ap_question";
    public static final String AP_TYPE = "ap_type";
    public static final String AP_COMMENTS = "ap_comments";
    public static final String STEP_START_DATE = "step_start_date";
    public static final String STEP_START_USER = "step_start_user";
    public static final String STEP_START_USER_NICK = "step_start_user_nick";
    public static final String STEP_END_DATE = "step_end_date";
    public static final String STEP_END_USER = "step_end_user";
    public static final String STEP_END_USER_NICK = "step_end_user_nick";
    public static final String STEP_STATUS = "step_status";

    public TK_Ticket_StepDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        toContentValuesMapper = new TK_Ticket_SteptoContentValuesMapper();
        toTK_Ticket_StepMapper = new CursorToTK_Ticket_StepMapper();
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Step tk_ticket_step) {
        return addUpdate(tk_ticket_step,null);
    }

    public DaoObjReturn addUpdate(TK_Ticket_Step tk_ticket_step, SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        if (dbInstance == null) {
            openDB();
        } else {
            this.db = dbInstance;
        }

        try {
            //Se db não foi passado, inicializa transaction
            if (dbInstance == null) {
                db.beginTransaction();
            }
            //
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_step.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_step.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_step.getTicket_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_step.getStep_code()).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_step), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0) {
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_step));
            }
            //
            //Tenta inserir action
            TK_Ticket_CtrlDao ticketCtrlDao = new TK_Ticket_CtrlDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
            //Chama insertUpdate do Ctrl,passando db como param aguardando retorno.
            daoObjReturn = ticketCtrlDao.addUpdate(tk_ticket_step.getCtrl(), false, db);
            //Se erro durante insert, dispara exception abortando o processamento.
            if (daoObjReturn.hasError()) {
                throw new Exception(daoObjReturn.getRawMessage());
            }
            //Se db não foi passado, finaliza transaction com sucesso
            if (dbInstance == null) {
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
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
        } catch (Exception e) {
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true);
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            if (dbInstance == null) {
                db.endTransaction();
            }
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }
        //
        if (dbInstance == null) {
            closeDB();
        }
        //
        return daoObjReturn;
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Step> tk_ticket_steps, boolean status) {
        return addUpdate(tk_ticket_steps,status,null);
    }

    public DaoObjReturn addUpdate(List<TK_Ticket_Step> tk_ticket_steps, boolean status, SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        if (dbInstance == null) {
            openDB();
        } else {
            this.db = dbInstance;
        }

        try {
            //Se db não foi passado, inicializa transaction
            if (dbInstance == null) {
                db.beginTransaction();
            }
            if (status) {
                db.delete(TABLE, null, null);
            }
            //
            for (TK_Ticket_Step tk_ticket_step : tk_ticket_steps) {
                //
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_step.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_step.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_step.getTicket_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_step.getStep_code()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_step), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0) {
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_step));
                }
                //
                //Tenta inserir action
                TK_Ticket_CtrlDao ticketCtrlDao = new TK_Ticket_CtrlDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                //Chama insertUpdate do Ctrl,passando db como param aguardando retorno.
                daoObjReturn = ticketCtrlDao.addUpdate(tk_ticket_step.getCtrl(), false, db);
                //Se erro durante insert, dispara exception abortando o processamento.
                if (daoObjReturn.hasError()) {
                    throw new Exception(daoObjReturn.getRawMessage());
                }
            }
            //Se db não foi passado, finaliza transaction com sucesso
            if (dbInstance == null) {
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
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

        } catch (Exception e) {
            //Seta obj de retorno com flag de erro e gera arquivo de exception
            daoObjReturn.setError(true);
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            if (dbInstance == null) {
                db.endTransaction();
            }
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }
        //
        if (dbInstance == null) {
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

    /**
     * Metodo usado no removeFull do ticket
     *
     * @param tk_ticket - Obj ticket a ser removido
     * @param dbInstance - Instancia compartilhada do banco
     * @return DaoObjReturn com resultados
     */
    public DaoObjReturn removeFull(TK_Ticket tk_ticket , SQLiteDatabase dbInstance ){
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long sqlRet = 0;
        String curAction = DaoObjReturn.DELETE;
        daoObjReturn.setTable(TABLE);
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

            for (TK_Ticket_Step tk_ticket_step  : tk_ticket.getStep()) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_step.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_step.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_step.getTicket_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_step.getStep_code()).append("'");
                //
                TK_Ticket_CtrlDao ticketCtrlDao = new TK_Ticket_CtrlDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                //Tenta o delete do tipo do controle
                DaoObjReturn tkCtrlDelRet = ticketCtrlDao.removeFull(tk_ticket_step.getCtrl(), db);
                sqlRet = tkCtrlDelRet.getActionReturn();
                //Se delete do processo "filho" OK, segue para o delete do ctrl
                if(sqlRet != 0){
                    sqlRet = 0;
                    daoObjReturn.setTable(TABLE);
                    sqlRet = db.delete(TABLE,sbWhere.toString(),null);
                    if(sqlRet == 0){
                        daoObjReturn.setRawMessage(daoObjReturn.DELETE_ERROR_0_ROWS_AFFECTED);
                        throw new Exception(daoObjReturn.getErrorMsg());
                    }
                }else{
                    daoObjReturn.setRawMessage(daoObjReturn.DELETE_ERROR_0_ROWS_AFFECTED);
                    throw new Exception(daoObjReturn.getErrorMsg());
                }
            }
            //
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
    public TK_Ticket_Step getByString(String sQuery) {
        TK_Ticket_Step tk_ticket_step = null;
        //
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                tk_ticket_step = toTK_Ticket_StepMapper.map(cursor);
            }
            //
            if (tk_ticket_step != null) {
                getTicketCtrls(tk_ticket_step);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        //
        closeDB();
        //
        return tk_ticket_step;
    }

    /**
     * Retorna lista de ctrl do ticket selecionado.
     * @param tk_ticket_step
     */
    private void getTicketCtrls(TK_Ticket_Step tk_ticket_step) {
        TK_Ticket_CtrlDao ticketCtrlDao = new TK_Ticket_CtrlDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        tk_ticket_step.setCtrl(
            (ArrayList<TK_Ticket_Ctrl>) ticketCtrlDao.query(
                new TK_Ticket_Ctrl_Sql_002(
                    tk_ticket_step.getCustomer_code(),
                    tk_ticket_step.getTicket_prefix(),
                    tk_ticket_step.getTicket_code(),
                    tk_ticket_step.getStep_code()
                ).toSqlQuery()
            )
        );
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
    public List<TK_Ticket_Step> query(String sQuery) {
        List<TK_Ticket_Step> tk_ticket_steps = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                TK_Ticket_Step uAux = toTK_Ticket_StepMapper.map(cursor);
                //
                if (uAux != null) {
                    getTicketCtrls(uAux);
                }
                //
                tk_ticket_steps.add(uAux);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        closeDB();
        return tk_ticket_steps;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> tk_ticket_steps = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                tk_ticket_steps.add(CursorToHMAuxMapper.mapN(cursor));
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        closeDB();
        return tk_ticket_steps;
    }

    private class CursorToTK_Ticket_StepMapper implements Mapper<Cursor, TK_Ticket_Step> {
        @Override
        public TK_Ticket_Step map(Cursor cursor) {
            TK_Ticket_Step tk_ticket_step = new TK_Ticket_Step();
            tk_ticket_step.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket_step.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket_step.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket_step.setStep_code(cursor.getInt(cursor.getColumnIndex(STEP_CODE)));
            if (cursor.isNull(cursor.getColumnIndex(STEP_ID))) {
                tk_ticket_step.setStep_id(null);
            } else {
                tk_ticket_step.setStep_id(cursor.getString(cursor.getColumnIndex(STEP_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(STEP_DESC))) {
                tk_ticket_step.setStep_desc(null);
            } else {
                tk_ticket_step.setStep_desc(cursor.getString(cursor.getColumnIndex(STEP_DESC)));
            }
            tk_ticket_step.setStep_order(cursor.getInt(cursor.getColumnIndex(STEP_ORDER)));
            if (cursor.isNull(cursor.getColumnIndex(FORECAST_START))) {
                tk_ticket_step.setForecast_start(null);
            } else {
                tk_ticket_step.setForecast_start(cursor.getString(cursor.getColumnIndex(FORECAST_START)));
            }
            if (cursor.isNull(cursor.getColumnIndex(FORECAST_END))) {
                tk_ticket_step.setForecast_end(null);
            } else {
                tk_ticket_step.setForecast_end(cursor.getString(cursor.getColumnIndex(FORECAST_END)));
            }
            tk_ticket_step.setExec_type(cursor.getString(cursor.getColumnIndex(EXEC_TYPE)));
            tk_ticket_step.setScan_serial(cursor.getInt(cursor.getColumnIndex(SCAN_SERIAL)));
            tk_ticket_step.setAllow_new_obj(cursor.getInt(cursor.getColumnIndex(ALLOW_NEW_OBJ)));
            if (cursor.isNull(cursor.getColumnIndex(PARTNER_CODE))) {
                tk_ticket_step.setPartner_code(null);
            } else {
                tk_ticket_step.setPartner_code(cursor.getInt(cursor.getColumnIndex(PARTNER_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PARTNER_ID))) {
                tk_ticket_step.setPartner_id(null);
            } else {
                tk_ticket_step.setPartner_id(cursor.getString(cursor.getColumnIndex(PARTNER_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PARTNER_DESC))) {
                tk_ticket_step.setPartner_desc(null);
            } else {
                tk_ticket_step.setPartner_desc(cursor.getString(cursor.getColumnIndex(PARTNER_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(AP_QUESTION))) {
                tk_ticket_step.setAp_question(null);
            } else {
                tk_ticket_step.setAp_question(cursor.getString(cursor.getColumnIndex(AP_QUESTION)));
            }
            if (cursor.isNull(cursor.getColumnIndex(AP_TYPE))) {
                tk_ticket_step.setAp_type(null);
            } else {
                tk_ticket_step.setAp_type(cursor.getString(cursor.getColumnIndex(AP_TYPE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(AP_COMMENTS))) {
                tk_ticket_step.setAp_comments(null);
            } else {
                tk_ticket_step.setAp_comments(cursor.getString(cursor.getColumnIndex(AP_COMMENTS)));
            }
            if (cursor.isNull(cursor.getColumnIndex(STEP_START_DATE))) {
                tk_ticket_step.setStep_start_date(null);
            } else {
                tk_ticket_step.setStep_start_date(cursor.getString(cursor.getColumnIndex(STEP_START_DATE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(STEP_START_USER))) {
                tk_ticket_step.setStep_start_user(null);
            } else {
                tk_ticket_step.setStep_start_user(cursor.getInt(cursor.getColumnIndex(STEP_START_USER)));
            }
            if (cursor.isNull(cursor.getColumnIndex(STEP_START_USER_NICK))) {
                tk_ticket_step.setStep_start_user_nick(null);
            } else {
                tk_ticket_step.setStep_start_user_nick(cursor.getString(cursor.getColumnIndex(STEP_START_USER_NICK)));
            }
            if (cursor.isNull(cursor.getColumnIndex(STEP_END_DATE))) {
                tk_ticket_step.setStep_end_date(null);
            } else {
                tk_ticket_step.setStep_end_date(cursor.getString(cursor.getColumnIndex(STEP_END_DATE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(STEP_END_USER))) {
                tk_ticket_step.setStep_end_user(null);
            } else {
                tk_ticket_step.setStep_end_user(cursor.getInt(cursor.getColumnIndex(STEP_END_USER)));
            }
            if (cursor.isNull(cursor.getColumnIndex(STEP_END_USER_NICK))) {
                tk_ticket_step.setStep_end_user_nick(null);
            } else {
                tk_ticket_step.setStep_end_user_nick(cursor.getString(cursor.getColumnIndex(STEP_END_USER_NICK)));
            }
            tk_ticket_step.setStep_status(cursor.getString(cursor.getColumnIndex(STEP_STATUS)));
            return tk_ticket_step;
        }
    }

    private class TK_Ticket_SteptoContentValuesMapper implements Mapper<TK_Ticket_Step, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket_Step tk_ticket_step) {
            ContentValues contentValues = new ContentValues();
            if (tk_ticket_step.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, tk_ticket_step.getCustomer_code());
            }
            if (tk_ticket_step.getTicket_prefix() > -1) {
                contentValues.put(TICKET_PREFIX, tk_ticket_step.getTicket_prefix());
            }
            if (tk_ticket_step.getTicket_code() > -1) {
                contentValues.put(TICKET_CODE, tk_ticket_step.getTicket_code());
            }
            if (tk_ticket_step.getStep_code() > -1) {
                contentValues.put(STEP_CODE, tk_ticket_step.getStep_code());
            }
            contentValues.put(STEP_ID, tk_ticket_step.getStep_id());
            contentValues.put(STEP_DESC, tk_ticket_step.getStep_desc());
            if (tk_ticket_step.getStep_order() > -1) {
                contentValues.put(STEP_ORDER, tk_ticket_step.getStep_order());
            }
            contentValues.put(FORECAST_START, tk_ticket_step.getForecast_start());
            contentValues.put(FORECAST_END, tk_ticket_step.getForecast_end());
            if (tk_ticket_step.getExec_type() != null) {
                contentValues.put(EXEC_TYPE, tk_ticket_step.getExec_type());
            }
            if (tk_ticket_step.getScan_serial() > -1) {
                contentValues.put(SCAN_SERIAL, tk_ticket_step.getScan_serial());
            }
            if (tk_ticket_step.getAllow_new_obj() > -1) {
                contentValues.put(ALLOW_NEW_OBJ, tk_ticket_step.getAllow_new_obj());
            }
            contentValues.put(PARTNER_CODE, tk_ticket_step.getPartner_code());
            contentValues.put(PARTNER_ID, tk_ticket_step.getPartner_id());
            contentValues.put(PARTNER_DESC, tk_ticket_step.getPartner_desc());
            contentValues.put(AP_QUESTION, tk_ticket_step.getAp_question());
            contentValues.put(AP_TYPE, tk_ticket_step.getAp_type());
            contentValues.put(AP_COMMENTS, tk_ticket_step.getAp_comments());
            contentValues.put(STEP_START_DATE, tk_ticket_step.getStep_start_date());
            contentValues.put(STEP_START_USER, tk_ticket_step.getStep_start_user());
            contentValues.put(STEP_START_USER_NICK, tk_ticket_step.getStep_start_user_nick());
            contentValues.put(STEP_END_DATE, tk_ticket_step.getStep_end_date());
            contentValues.put(STEP_END_USER, tk_ticket_step.getStep_end_user());
            contentValues.put(STEP_END_USER_NICK, tk_ticket_step.getStep_end_user_nick());
            if (tk_ticket_step.getStep_status() != null) {
                contentValues.put(STEP_STATUS, tk_ticket_step.getStep_status());
            }
            return contentValues;
        }
    }
}
