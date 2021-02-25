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
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.TK_Ticket_Form;
import com.namoadigital.prj001.sql.TK_Ticket_Form_Sql_002;
import com.namoadigital.prj001.sql.TK_Ticket_Form_Sql_003;
import com.namoadigital.prj001.sql.TK_Ticket_Form_Sql_004;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class TK_Ticket_FormDao extends BaseDao implements DaoWithReturn<TK_Ticket_Form>, DaoWithReturnSharedDbInstance<TK_Ticket_Form> {

    private final Mapper<TK_Ticket_Form, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor,TK_Ticket_Form> toTk_Ticket_FormMapper;

    public static final String TABLE = "tk_ticket_form";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String TICKET_SEQ = "ticket_seq";
    public static final String TICKET_SEQ_TMP = "ticket_seq_tmp";
    public static final String STEP_CODE = "step_code";
    public static final String FORM_STATUS = "form_status";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_TYPE_DESC = "custom_form_type_desc";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String CUSTOM_FORM_DESC = "custom_form_desc";
    public static final String CUSTOM_FORM_DATA = "custom_form_data";

    public static final String SCORE_STATUS = "score_status";
    public static final String SCORE_PERC = "score_perc";
    public static final String NC = "nc";

    public static final String CUSTOM_FORM_DATA_TMP = "custom_form_data_tmp";
    public static final String PDF_CODE = "pdf_code";
    public static final String PDF_NAME = "pdf_name";
    public static final String PDF_URL = "pdf_url";
    public static final String PDF_URL_LOCAL = "pdf_url_local";

    public TK_Ticket_FormDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        toContentValuesMapper = new TK_Ticket_FormToContentValuesMapper();
        toTk_Ticket_FormMapper = new CursorToTk_Ticket_FormMapper();
    }

    private StringBuilder getWherePkClause(TK_Ticket_Form tk_ticket_form) {
        return
            new StringBuilder()
                .append(CUSTOMER_CODE).append(" = '").append(tk_ticket_form.getCustomer_code()).append("'")
                .append(" and ")
                .append(TICKET_PREFIX).append(" = '").append(tk_ticket_form.getTicket_prefix()).append("'")
                .append(" and ")
                .append(TICKET_CODE).append(" = '").append(tk_ticket_form.getTicket_code()).append("'")
                .append(" and ")
                .append(TICKET_SEQ).append(" = '").append(tk_ticket_form.getTicket_seq()).append("'")
                .append(" and ")
                .append(STEP_CODE).append(" = '").append(tk_ticket_form.getStep_code()).append("'");
    }

    private StringBuilder getWherePkTmpClause(TK_Ticket_Form tk_ticket_form) {
        return
            new StringBuilder()
                .append(CUSTOMER_CODE).append(" = '").append(tk_ticket_form.getCustomer_code()).append("'")
                .append(" and ")
                .append(TICKET_PREFIX).append(" = '").append(tk_ticket_form.getTicket_prefix()).append("'")
                .append(" and ")
                .append(TICKET_CODE).append(" = '").append(tk_ticket_form.getTicket_code()).append("'")
                .append(" and ")
                .append(TICKET_SEQ_TMP).append(" = '").append(tk_ticket_form.getTicket_seq_tmp()).append("'")
                .append(" and ")
                .append(STEP_CODE).append(" = '").append(tk_ticket_form.getStep_code()).append("'");
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Form tk_ticket_form) {
        return addUpdate(tk_ticket_form,null);
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Form> tk_ticket_forms, boolean status) {
        return addUpdate(tk_ticket_forms, status,null);
    }


    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Form tk_ticket_form, SQLiteDatabase dbInstance) {
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
            StringBuilder sbWhere = getWherePkClause(tk_ticket_form);
            /* LUCHE - 04/09/2020
             * Antes de inserir, verifica a necessidade cancelar forms vinculados a esse obj.
             */
            checkIfFormNeedToBeCancelled(tk_ticket_form,db);
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_form), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_form));
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

    private void checkIfFormNeedToBeCancelled(TK_Ticket_Form tk_ticket_form, SQLiteDatabase db) throws Exception {
        TK_Ticket_Form dbTicketForm = getByString(
            new TK_Ticket_Form_Sql_002(
                tk_ticket_form.getCustomer_code(),
                tk_ticket_form.getTicket_prefix(),
                tk_ticket_form.getTicket_code(),
                tk_ticket_form.getTicket_seq_tmp(),
                tk_ticket_form.getStep_code()
            ).toSqlQuery()
        );
        //
        if(dbTicketForm != null) {
            GE_Custom_Form_LocalDao formLocalDao = new GE_Custom_Form_LocalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
            GE_Custom_Form_DataDao formDataDao = new GE_Custom_Form_DataDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
            //
            /**
             * BARRIONUEVO - 11-11-2020
             * ADICIONADO CLAUSULA  dbTicketForm.getTicket_seq() > 0 PARA EVITAR CANCELAR FORM ESPONTANEOS.
             */
            if (tk_ticket_form.getCustom_form_data() != null
                && tk_ticket_form.getCustom_form_data() > 0
                && dbTicketForm.getCustom_form_data_tmp() != null
                && dbTicketForm.getCustom_form_data_tmp() > 0
                && dbTicketForm.getTicket_seq() > 0
            ) {
                if (ConstantBaseApp.SYS_STATUS_PENDING.equals(dbTicketForm.getForm_status())
                    || ConstantBaseApp.SYS_STATUS_PROCESS.equals(dbTicketForm.getForm_status())
                    || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(dbTicketForm.getForm_status())
                ) {
                    GE_Custom_Form_Local formLocalToCancel = formLocalDao.getByStringSharedDbInstance(
                        new TK_Ticket_Form_Sql_003(
                            tk_ticket_form.getCustomer_code(),
                            tk_ticket_form.getTicket_prefix(),
                            tk_ticket_form.getTicket_code(),
                            tk_ticket_form.getTicket_seq_tmp(),
                            tk_ticket_form.getStep_code()
                        ).toSqlQuery(),
                        db
                    );
                    //
                    GE_Custom_Form_Data formDataToCancel = formDataDao.getByStringSharedDbInstance(
                        new TK_Ticket_Form_Sql_004(
                            tk_ticket_form.getCustomer_code(),
                            tk_ticket_form.getTicket_prefix(),
                            tk_ticket_form.getTicket_code(),
                            tk_ticket_form.getTicket_seq_tmp(),
                            tk_ticket_form.getStep_code()
                        ).toSqlQuery(),
                        db
                    );
                    //
                    if (formLocalToCancel != null) {
                        formLocalToCancel.setCustom_form_status(ConstantBaseApp.SYS_STATUS_CANCELLED);
                        DaoObjReturn daoObjReturn = formLocalDao.addUpdateThrowExceptionWithSharedDbInstance(formLocalToCancel,db);
                        if (daoObjReturn.hasError()) {
                            throw new Exception(daoObjReturn.getErrorMsg());
                        }
                    }
                    if (formDataToCancel != null) {
                        formDataToCancel.setCustom_form_status(ConstantBaseApp.SYS_STATUS_CANCELLED);
                        formDataToCancel.setLocation_pendency(0);
                        DaoObjReturn daoObjReturn = formDataDao.addUpdateWithReturnAndSharedDbInstance(formDataToCancel,db);
                        if (daoObjReturn.hasError()) {
                            throw new Exception(daoObjReturn.getErrorMsg());
                        }
                    }
                    //
                }
            }
            //
        }
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Form> tk_ticket_forms, boolean status, SQLiteDatabase dbInstance) {
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
            for (TK_Ticket_Form tk_ticket_form : tk_ticket_forms) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = getWherePkClause(tk_ticket_form);
                /* LUCHE - 04/09/2020
                 * Antes de inserir, verifica a necessidade cancelar forms vinculados a esse obj.
                 */
                checkIfFormNeedToBeCancelled(tk_ticket_form,db);
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_form), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_form));
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


    public DaoObjReturn addUpdateTmp(TK_Ticket_Form tk_ticket_form, SQLiteDatabase dbInstance) {
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
            StringBuilder sbWhere = getWherePkTmpClause(tk_ticket_form);
            /* LUCHE - 04/09/2020
             * Antes de inserir, verifica a necessidade cancelar forms vinculados a esse obj.
             */
            checkIfFormNeedToBeCancelled(tk_ticket_form,db);
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_form), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_form));
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

    public DaoObjReturn addUpdateTmp(List<TK_Ticket_Form> tk_ticket_forms, boolean status, SQLiteDatabase dbInstance) {
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
            for (TK_Ticket_Form tk_ticket_form : tk_ticket_forms) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = getWherePkTmpClause(tk_ticket_form);
                /* LUCHE - 04/09/2020
                 * Antes de inserir, verifica a necessidade cancelar forms vinculados a esse obj.
                 */
                checkIfFormNeedToBeCancelled(tk_ticket_form,db);
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_form), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_form));
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
    public DaoObjReturn remove(TK_Ticket_Form tk_ticket_form, @Nullable SQLiteDatabase dbInstance) {
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
            StringBuilder sbWhere = getWherePkClause(tk_ticket_form);
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
    public TK_Ticket_Form getByString(String sQuery) {
      return getByString(sQuery,null);
    }

    private TK_Ticket_Form getByString(String sQuery, @Nullable SQLiteDatabase dbInstance) {
        TK_Ticket_Form tk_ticket_form = null;

        if (dbInstance == null) {
            openDB();
        } else {
            this.db = dbInstance;
        }

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                tk_ticket_form = toTk_Ticket_FormMapper.map(cursor);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        if(dbInstance == null) {
            closeDB();
        }

        return tk_ticket_form;
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
    public List<TK_Ticket_Form> query(String sQuery) {
        List<TK_Ticket_Form> tk_ticket_forms = new ArrayList<>();
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                TK_Ticket_Form uAux = toTk_Ticket_FormMapper.map(cursor);
                tk_ticket_forms.add(uAux);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return tk_ticket_forms;
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

    private class CursorToTk_Ticket_FormMapper implements Mapper<Cursor, TK_Ticket_Form> {
        @Override
        public TK_Ticket_Form map(Cursor cursor) {
            TK_Ticket_Form tk_ticket_form = new TK_Ticket_Form();
            tk_ticket_form.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket_form.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket_form.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket_form.setTicket_seq(cursor.getInt(cursor.getColumnIndex(TICKET_SEQ)));
            tk_ticket_form.setTicket_seq_tmp(cursor.getInt(cursor.getColumnIndex(TICKET_SEQ_TMP)));
            tk_ticket_form.setStep_code(cursor.getInt(cursor.getColumnIndex(STEP_CODE)));
            //
            tk_ticket_form.setForm_status(cursor.getString(cursor.getColumnIndex(FORM_STATUS)));
            tk_ticket_form.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            tk_ticket_form.setCustom_form_type_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_TYPE_DESC)));
            tk_ticket_form.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            tk_ticket_form.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            tk_ticket_form.setCustom_form_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_DESC)));
            if(cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_DATA))){
                tk_ticket_form.setCustom_form_data(null);
            }else{
                tk_ticket_form.setCustom_form_data(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_DATA)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SCORE_STATUS))){
                tk_ticket_form.setScore_status(null);
            }else{
                tk_ticket_form.setScore_status(cursor.getString(cursor.getColumnIndex(SCORE_STATUS)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SCORE_PERC))){
                tk_ticket_form.setScore_perc(null);
            }else{
                tk_ticket_form.setScore_perc(cursor.getString(cursor.getColumnIndex(SCORE_PERC)));
            }
            tk_ticket_form.setNc(cursor.getInt(cursor.getColumnIndex(NC)));
            if(cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_DATA_TMP))){
                tk_ticket_form.setCustom_form_data_tmp(null);
            }else{
                tk_ticket_form.setCustom_form_data_tmp(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_DATA_TMP)));
            }

            if(cursor.isNull(cursor.getColumnIndex(PDF_CODE))){
                tk_ticket_form.setPdf_code(null);
            }else{
                tk_ticket_form.setPdf_code(cursor.getInt(cursor.getColumnIndex(PDF_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PDF_NAME))){
                tk_ticket_form.setPdf_name(null);
            }else{
                tk_ticket_form.setPdf_name(cursor.getString(cursor.getColumnIndex(PDF_NAME)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PDF_URL))){
                tk_ticket_form.setPdf_url(null);
            }else{
                tk_ticket_form.setPdf_url(cursor.getString(cursor.getColumnIndex(PDF_URL)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PDF_URL_LOCAL))){
                tk_ticket_form.setPdf_url_local(null);
            }else{
                tk_ticket_form.setPdf_url_local(cursor.getString(cursor.getColumnIndex(PDF_URL_LOCAL)));
            }
            return tk_ticket_form;
        }
    }

    private class TK_Ticket_FormToContentValuesMapper implements Mapper<TK_Ticket_Form, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket_Form tk_ticket_form) {
            ContentValues contentValues = new ContentValues();
            //
            if (tk_ticket_form.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE,tk_ticket_form.getCustomer_code());
            }
            if (tk_ticket_form.getTicket_prefix() > -1) {
                contentValues.put(TICKET_PREFIX,tk_ticket_form.getTicket_prefix());
            }
            if (tk_ticket_form.getTicket_code() > -1) {
                contentValues.put(TICKET_CODE,tk_ticket_form.getTicket_code());
            }
            if (tk_ticket_form.getTicket_seq() > -1) {
                contentValues.put(TICKET_SEQ,tk_ticket_form.getTicket_seq());
            }
            if (tk_ticket_form.getTicket_seq_tmp() > -1) {
                contentValues.put(TICKET_SEQ_TMP,tk_ticket_form.getTicket_seq_tmp());
            }
            if (tk_ticket_form.getStep_code() > -1) {
                contentValues.put(STEP_CODE,tk_ticket_form.getStep_code());
            }
            if (tk_ticket_form.getForm_status() != null) {
                contentValues.put(FORM_STATUS,tk_ticket_form.getForm_status());
            }
            if (tk_ticket_form.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE,tk_ticket_form.getCustom_form_type());
            }
            if (tk_ticket_form.getCustom_form_type_desc() != null) {
                contentValues.put(CUSTOM_FORM_TYPE_DESC,tk_ticket_form.getCustom_form_type_desc());
            }
            if (tk_ticket_form.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE,tk_ticket_form.getCustom_form_code());
            }
            if (tk_ticket_form.getCustom_form_desc() != null) {
                contentValues.put(CUSTOM_FORM_DESC,tk_ticket_form.getCustom_form_desc());
            }
            if (tk_ticket_form.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION,tk_ticket_form.getCustom_form_version());
            }
            contentValues.put(CUSTOM_FORM_DATA,tk_ticket_form.getCustom_form_data());

            contentValues.put(SCORE_STATUS,tk_ticket_form.getScore_status());
            contentValues.put(SCORE_PERC,tk_ticket_form.getScore_perc());
            if (tk_ticket_form.getNc() > -1) {
                contentValues.put(NC, tk_ticket_form.getNc());
            }
            contentValues.put(CUSTOM_FORM_DATA_TMP,tk_ticket_form.getCustom_form_data_tmp());
            contentValues.put(PDF_CODE,tk_ticket_form.getPdf_code());
            contentValues.put(PDF_NAME,tk_ticket_form.getPdf_name());
            contentValues.put(PDF_URL,tk_ticket_form.getPdf_url());
            contentValues.put(PDF_URL_LOCAL,tk_ticket_form.getPdf_url_local());
            //
            return contentValues;
        }
    }
}
