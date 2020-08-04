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
import com.namoadigital.prj001.model.TK_Ticket_Action;
import com.namoadigital.prj001.model.TK_Ticket_Approval;
import com.namoadigital.prj001.model.TK_Ticket_Approval_Rejection;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.sql.TK_Ticket_Action_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Approval_Rejection_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Approval_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_003;
import com.namoadigital.prj001.sql.TK_Ticket_Measure_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class TK_Ticket_CtrlDao extends BaseDao implements DaoWithReturn<TK_Ticket_Ctrl> {

    private final Mapper<TK_Ticket_Ctrl, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, TK_Ticket_Ctrl> toTK_Ticket_CtrlMapper;

    public static final String TABLE  = "tk_ticket_ctrl";
    public static final String CUSTOMER_CODE  = "customer_code";
    public static final String TICKET_PREFIX  = "ticket_prefix";
    public static final String TICKET_CODE  = "ticket_code";
    public static final String TICKET_SEQ  = "ticket_seq";
    public static final String TICKET_SEQ_TMP  = "ticket_seq_tmp";
    public static final String STEP_CODE = "step_code";
    public static final String CTRL_TYPE  = "ctrl_type";
    public static final String PRODUCT_CODE  = "product_code";
    public static final String PRODUCT_ID  = "product_id";
    public static final String PRODUCT_DESC  = "product_desc";
    public static final String SERIAL_CODE  = "serial_code";
    public static final String SERIAL_ID  = "serial_id";
    public static final String CTRL_START_DATE  = "ctrl_start_date";
    public static final String CTRL_START_USER  = "ctrl_start_user";
    public static final String CTRL_START_USER_NAME  = "ctrl_start_user_name";
    public static final String CTRL_END_DATE  = "ctrl_end_date";
    public static final String CTRL_END_USER  = "ctrl_end_user";
    public static final String CTRL_END_USER_NAME  = "ctrl_end_user_name";
    public static final String CTRL_STATUS  = "ctrl_status";
    public static final String PARTNER_CODE  = "partner_code";
    public static final String PARTNER_ID  = "partner_id";
    public static final String PARTNER_DESC  = "partner_desc";
    public static final String STEP_ORDER = "step_order";
    public static final String OBJ_PLANNED = "obj_planned";
    public static final String UPDATE_REQUIRED = "update_required";

    public TK_Ticket_CtrlDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new TK_Ticket_CtrlToContentValuesMapper();
        this.toTK_Ticket_CtrlMapper = new CursorToTK_Ticket_CtrlMapper();
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Ctrl tk_ticket_ctrl) {
        return addUpdate(tk_ticket_ctrl,null);
    }

    public DaoObjReturn addUpdate(TK_Ticket_Ctrl tk_ticket_ctrl, SQLiteDatabase dbInstance) {
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
            //Se db não foi passado, inicializa transaction
            if(dbInstance == null) {
                db.beginTransaction();
            }
            //
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_ctrl.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_ctrl.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_ctrl.getTicket_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_ctrl.getTicket_seq()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_ctrl.getStep_code()).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_ctrl), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_ctrl));
            }
            //Tenta inserir o "processo" filho
            insertUpdateByCtrlType(tk_ticket_ctrl,db,daoObjReturn,false);
            //Se db não foi passado, finaliza transaction com sucesso
            if(dbInstance == null) {
                db.setTransactionSuccessful();
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
            if(dbInstance == null) {
                db.endTransaction();
            }
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
    public DaoObjReturn addUpdate(List<TK_Ticket_Ctrl> tk_ticket_ctrls, boolean status) {
        return addUpdate(tk_ticket_ctrls,status,null);
    }

    public DaoObjReturn addUpdate(List<TK_Ticket_Ctrl> tk_ticket_ctrls, boolean status, SQLiteDatabase dbInstance) {
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
            //Se db não foi passado, inicializa transaction
            if(dbInstance == null) {
                db.beginTransaction();
            }
            if (status) {
                db.delete(TABLE, null, null);
            }
            //
            for (TK_Ticket_Ctrl tk_ticket_ctrl : tk_ticket_ctrls) {
                //
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_ctrl.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_ctrl.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_ctrl.getTicket_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_ctrl.getTicket_seq()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_ctrl.getStep_code()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_ctrl), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0) {
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_ctrl));
                }
                //Tenta inserir o "processo" filho
                insertUpdateByCtrlType(tk_ticket_ctrl,db,daoObjReturn,false);
            }
            //Se db não foi passado, finaliza transaction com sucesso
            if(dbInstance == null) {
                db.setTransactionSuccessful();
            }

        }catch (Exception e){
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
        }finally {
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            if(dbInstance == null) {
                db.endTransaction();
            }
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

    public DaoObjReturn addUpdateTmp(TK_Ticket_Ctrl tk_ticket_ctrl, SQLiteDatabase dbInstance) {
        ArrayList<TK_Ticket_Ctrl> tk_ticket_ctrls = new ArrayList<>();
        tk_ticket_ctrls.add(tk_ticket_ctrl);
        return addUpdateTmp(tk_ticket_ctrls,false,dbInstance);
    }

    public DaoObjReturn addUpdateTmp(List<TK_Ticket_Ctrl> tk_ticket_ctrls, boolean status, SQLiteDatabase dbInstance) {
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
            //Se db não foi passado, inicializa transaction
            if(dbInstance == null) {
                db.beginTransaction();
            }
            if (status) {
                db.delete(TABLE, null, null);
            }
            //
            for (TK_Ticket_Ctrl tk_ticket_ctrl : tk_ticket_ctrls) {
                //Chama metodo que devolve o ticket_seq_tmp do ctrl ou ja gera um novo.
                tk_ticket_ctrl.setTicket_seq_tmp(
                    getTicketSeqTmp(tk_ticket_ctrl,dbInstance)
                );
                //
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_ctrl.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_ctrl.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_ctrl.getTicket_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_SEQ_TMP).append(" = '").append(tk_ticket_ctrl.getTicket_seq_tmp()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_ctrl.getStep_code()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_ctrl), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0) {
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_ctrl));
                }
                //Tenta inserir o "processo" filho
                insertUpdateByCtrlType(tk_ticket_ctrl,db,daoObjReturn,true);
            }
            //Se db não foi passado, finaliza transaction com sucesso
            if(dbInstance == null) {
                db.setTransactionSuccessful();
            }
        }catch (Exception e){
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
        }finally {
            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
            //ou rowId do ultimo insert.
            if(dbInstance == null) {
                db.endTransaction();
            }
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

    private int getTicketSeqTmp(TK_Ticket_Ctrl tk_ticket_ctrl, SQLiteDatabase dbInstance) throws Exception {
        if(tk_ticket_ctrl.getTicket_seq_tmp() > 0){
            return tk_ticket_ctrl.getTicket_seq_tmp();
        } else if(tk_ticket_ctrl.getTicket_seq() > 0) {
            TK_Ticket_Ctrl dbTicketCtrl = getByString(
                new TK_Ticket_Ctrl_Sql_001(
                    tk_ticket_ctrl.getCustomer_code(),
                    tk_ticket_ctrl.getTicket_prefix(),
                    tk_ticket_ctrl.getTicket_code(),
                    tk_ticket_ctrl.getTicket_seq(),
                    tk_ticket_ctrl.getStep_code()
                ).toSqlQuery()
            );
            //
            if (dbTicketCtrl != null && dbTicketCtrl.getTicket_seq() > 0 && dbTicketCtrl.getTicket_seq_tmp() > 0) {
                return dbTicketCtrl.getTicket_seq_tmp();
            }
        }
        //
        HMAux nextTmp = getByStringHM(
            new TK_Ticket_Ctrl_Sql_003(
                tk_ticket_ctrl.getCustomer_code(),
                tk_ticket_ctrl.getTicket_prefix(),
                tk_ticket_ctrl.getTicket_code(),
                tk_ticket_ctrl.getStep_code()
            ).toSqlQuery(),
            dbInstance
        );
        //
        if (nextTmp != null && nextTmp.hasConsistentValue(TK_Ticket_Ctrl_Sql_003.NEXT_TICKET_SEQ_TMP)) {
            return Integer.parseInt(nextTmp.get(TK_Ticket_Ctrl_Sql_003.NEXT_TICKET_SEQ_TMP));
        } else {
            throw new Exception("ERROR_ON_GET_NEXT_TICKET_SEQ_TMP");
        }
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
     * TODO Revisar metodo modificad para compilar e testar......
     * Metodo usado no removeFull do ticket
     *
     * @param tk_ticket_ctrls - Obj lista de controles
     * @param dbInstance - Instancia compartilhada do banco
     * @return DaoObjReturn com resultados
     */
    public DaoObjReturn removeFull(List<TK_Ticket_Ctrl> tk_ticket_ctrls , SQLiteDatabase dbInstance ){
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

            for (TK_Ticket_Ctrl tk_ticket_ctrl : tk_ticket_ctrls) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_ctrl.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_ctrl.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_ctrl.getTicket_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_SEQ).append(" = '").append(tk_ticket_ctrl.getTicket_seq()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(STEP_CODE).append(" = '").append(tk_ticket_ctrl.getStep_code()).append("'");
                //Tenta o delete do tipo do controle
                deleteByCtrlType(tk_ticket_ctrl, sbWhere,db,daoObjReturn);
                //Se delete do processo "filho" OK, segue para o delete do ctrl
                sqlRet = 0;
                daoObjReturn.setTable(TABLE);
                sqlRet = db.delete(TABLE,sbWhere.toString(),null);
                if(sqlRet == 0){
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
    //region Method By Ctrl Type

    /**
     * Insere ou altera o processo filho do control, baseado no seu tipo
     * @param tk_ticket_ctrl - Control
     * @param db - Instancia do Db
     * @param daoObjReturn - Obj de retorno do Dao
     * @throws Exception
     */
    private void insertUpdateByCtrlType(TK_Ticket_Ctrl tk_ticket_ctrl, SQLiteDatabase db, DaoObjReturn daoObjReturn, boolean useTmpMethod) throws Exception {
        //Tenta inserir action
        switch (tk_ticket_ctrl.getCtrl_type()) {
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                if(tk_ticket_ctrl.getAction() != null) {
                    tk_ticket_ctrl.getAction().setPK(tk_ticket_ctrl);
                    daoObjReturn = tryAddUpdateAction(tk_ticket_ctrl.getAction(), db,useTmpMethod);
                    //Se erro durante insert, dispara exception abortando o processamento.
                    if (daoObjReturn.hasError()) {
                        throw new Exception(daoObjReturn.getRawMessage());
                    }
                }
                break;
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
                if(tk_ticket_ctrl.getApproval() != null) {
                    daoObjReturn = tryAddUpdateApproval(tk_ticket_ctrl.getApproval(), db);
                    //Se erro durante insert, dispara exception abortando o processamento.
                    if (daoObjReturn.hasError()) {
                        throw new Exception(daoObjReturn.getRawMessage());
                    }
                }
                if(tk_ticket_ctrl.getRejection() != null && tk_ticket_ctrl.getRejection().size() > 0 ) {
                    daoObjReturn = tryAddUpdateApprovalRejection(tk_ticket_ctrl.getRejection(), db);
                    //Se erro durante insert, dispara exception abortando o processamento.
                    if (daoObjReturn.hasError()) {
                        throw new Exception(daoObjReturn.getRawMessage());
                    }
                }
                break;

            /*case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                TK_Ticket_MeasureDao ticketMeasureDao = new TK_Ticket_MeasureDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                //Chama insertUpdate da action,passando db como param aguardando retorno.
                daoObjReturn = ticketMeasureDao.addUpdate(tk_ticket_ctrl.getMeasure(), db);
                //Se erro durante insert, dispara exception abortando o processamento.
                if (daoObjReturn.hasError()) {
                    throw new Exception(daoObjReturn.getRawMessage());
                }
                break;*/
            default:
                break;
        }
    }

    private DaoObjReturn tryAddUpdateAction(TK_Ticket_Action tk_ticket_action, SQLiteDatabase db, boolean useTmpMethod) {
        TK_Ticket_ActionDao ticketActionDao = new TK_Ticket_ActionDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //Chama insertUpdate da action,passando db como param aguardando retorno.
        //Se for criação, usa metodo TMP
        if(useTmpMethod){
            return ticketActionDao.addUpdateTmp(tk_ticket_action, db);
        }else{
            return ticketActionDao.addUpdate(tk_ticket_action, db);
        }
    }

    private DaoObjReturn tryAddUpdateApproval(TK_Ticket_Approval tk_ticket_approval, SQLiteDatabase db) {
        TK_Ticket_ApprovalDao ticketApprovalDao = new TK_Ticket_ApprovalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //Chama insertUpdate da action,passando db como param aguardando retorno.
        return ticketApprovalDao.addUpdate(tk_ticket_approval, db);
    }

    private DaoObjReturn tryAddUpdateApprovalRejection(ArrayList<TK_Ticket_Approval_Rejection> ticket_approval_rejections, SQLiteDatabase db) {
        TK_Ticket_Approval_RejectionDao ticketApprovalRejectionDao = new TK_Ticket_Approval_RejectionDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //Chama insertUpdate da action,passando db como param aguardando retorno.
        return ticketApprovalRejectionDao.addUpdate(ticket_approval_rejections, false,db);
    }

    /**
     * Deleta o processo filho do controle baseado no tipo
     * @param tk_ticket_ctrl - Control
     * @param sbWhere - Where para delete
     * @param db - Instancia do db
     * @param daoObjReturn - Obj de retorno do Dao
     * @return - Qtd de registros removidos
     * @throws Exception
     */
    private long deleteByCtrlType(TK_Ticket_Ctrl tk_ticket_ctrl, StringBuilder sbWhere, SQLiteDatabase db, DaoObjReturn daoObjReturn) throws Exception {
        String ctrlTypeTable = "";
        //
        switch (tk_ticket_ctrl.getCtrl_type()){
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                ctrlTypeTable = TK_Ticket_MeasureDao.TABLE;
                //Tenta o delete se houver
                if(tk_ticket_ctrl.getMeasure() != null) {
                    //Como a pk é a mesma e a relação é 1 para 1 será feito um db.delete diretamente daqui
                    daoObjReturn.setTable(ctrlTypeTable);
                    return db.delete(ctrlTypeTable, sbWhere.toString(), null);
                }
                return 1;
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                //Tenta o delete
                //Como a pk é a mesma e a relação é 1 para 1 será feito um db.delete diretamente daqui
                ctrlTypeTable = TK_Ticket_ActionDao.TABLE;
                if(tk_ticket_ctrl.getAction() != null) {
                    daoObjReturn.setTable(ctrlTypeTable);
                    return db.delete(ctrlTypeTable, sbWhere.toString(), null);
                }
                return 1;
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
                int approvalDelete = 1;
                //LUCHE - 24/07/2020
                //Aqui ja mudou tudo, não é necessariamente 1 x 1, pode ter só aproval ou só rejeição
                //e rejeição é uma lista...TOP
                //Como não é 1 pra 1
                ctrlTypeTable = TK_Ticket_ApprovalDao.TABLE;
                if(tk_ticket_ctrl.getApproval() != null) {
                    daoObjReturn.setTable(ctrlTypeTable);
                    approvalDelete = db.delete(ctrlTypeTable, sbWhere.toString(), null);
                }
                //
                ctrlTypeTable = TK_Ticket_Approval_RejectionDao.TABLE;
                if(tk_ticket_ctrl.getRejection() != null && tk_ticket_ctrl.getRejection().size() > 0 ) {
                    daoObjReturn.setTable(ctrlTypeTable);
                    approvalDelete += db.delete(ctrlTypeTable, sbWhere.toString(), null);
                }
                return approvalDelete;
            default:
                return 1;
        }
    }

    /**
     * Busca process filho do Ctrl e seta no objeto
     * @param tk_ticket_ctrl
     */
    private void getCtrlTypeSon(TK_Ticket_Ctrl tk_ticket_ctrl) {
        switch (tk_ticket_ctrl.getCtrl_type()){
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                //Busca measure do control
                TK_Ticket_MeasureDao ticketMeasureDao = new TK_Ticket_MeasureDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                //
                tk_ticket_ctrl.setMeasure(
                    ticketMeasureDao.getByString(
                        new TK_Ticket_Measure_Sql_001(
                            tk_ticket_ctrl.getCustomer_code(),
                            tk_ticket_ctrl.getTicket_prefix(),
                            tk_ticket_ctrl.getTicket_code(),
                            tk_ticket_ctrl.getTicket_seq()
                        ).toSqlQuery()
                    )
                );

                break;
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                //Busca action do control
                TK_Ticket_ActionDao ticketActionDao = new TK_Ticket_ActionDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                //
                tk_ticket_ctrl.setAction(
                    ticketActionDao.getByString(
                        new TK_Ticket_Action_Sql_001(
                            tk_ticket_ctrl.getCustomer_code(),
                            tk_ticket_ctrl.getTicket_prefix(),
                            tk_ticket_ctrl.getTicket_code(),
                            tk_ticket_ctrl.getTicket_seq()
                        ).toSqlQuery()

                    )
                );
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
                //Busca approval do control
                TK_Ticket_ApprovalDao ticketApprovalDao = new TK_Ticket_ApprovalDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                TK_Ticket_Approval_RejectionDao approvalRejectionDao = new TK_Ticket_Approval_RejectionDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                //
                tk_ticket_ctrl.setApproval(
                    ticketApprovalDao.getByString(
                        new TK_Ticket_Approval_Sql_001(
                            tk_ticket_ctrl.getCustomer_code(),
                            tk_ticket_ctrl.getTicket_prefix(),
                            tk_ticket_ctrl.getTicket_code(),
                            tk_ticket_ctrl.getTicket_seq(),
                            tk_ticket_ctrl.getStep_code()
                            ).toSqlQuery()
                    )
                );
                //
                tk_ticket_ctrl.setRejection(
                    (ArrayList<TK_Ticket_Approval_Rejection>) approvalRejectionDao.query(
                        new TK_Ticket_Approval_Rejection_Sql_001(
                            tk_ticket_ctrl.getCustomer_code(),
                            tk_ticket_ctrl.getTicket_prefix(),
                            tk_ticket_ctrl.getTicket_code(),
                            tk_ticket_ctrl.getTicket_seq(),
                            tk_ticket_ctrl.getStep_code()
                            ).toSqlQuery()
                    )
                );
            default:
                break;
        }
    }

    //endregion

    @Override
    public TK_Ticket_Ctrl getByString(String sQuery) {
        TK_Ticket_Ctrl tk_ticket_ctrl = null;
        //
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                tk_ticket_ctrl = toTK_Ticket_CtrlMapper.map(cursor);
            }
            //
            if(tk_ticket_ctrl != null){
                getCtrlTypeSon(tk_ticket_ctrl);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }
        //
        closeDB();
        //
        return tk_ticket_ctrl;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        return getByStringHM(sQuery,null);
    }

    public HMAux getByStringHM(String sQuery, SQLiteDatabase dbInstance) {
        HMAux hmAux = null;
        if (dbInstance == null) {
            openDB();
        } else {
            this.db = dbInstance;
        }
        //
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
        //
        if(dbInstance == null) {
            closeDB();
        }
        return hmAux;
    }

    @Override
    public List<TK_Ticket_Ctrl> query(String sQuery) {
        List<TK_Ticket_Ctrl> tk_ticket_ctrls = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                TK_Ticket_Ctrl uAux = toTK_Ticket_CtrlMapper.map(cursor);
                //
                if(uAux != null){
                    getCtrlTypeSon(uAux);
                }
                //
                tk_ticket_ctrls.add(uAux);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }
        closeDB();
        return tk_ticket_ctrls;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> tk_ticket_ctrls = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                tk_ticket_ctrls.add(CursorToHMAuxMapper.mapN(cursor));
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }
        closeDB();

        return tk_ticket_ctrls;
    }

    private class CursorToTK_Ticket_CtrlMapper implements Mapper<Cursor, TK_Ticket_Ctrl> {
        @Override
        public TK_Ticket_Ctrl map(Cursor cursor) {
            TK_Ticket_Ctrl tk_ticket_ctrl = new TK_Ticket_Ctrl();
            //
            tk_ticket_ctrl.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket_ctrl.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket_ctrl.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket_ctrl.setTicket_seq(cursor.getInt(cursor.getColumnIndex(TICKET_SEQ)));
            tk_ticket_ctrl.setTicket_seq_tmp(cursor.getInt(cursor.getColumnIndex(TICKET_SEQ_TMP)));
            tk_ticket_ctrl.setStep_code(cursor.getInt(cursor.getColumnIndex(STEP_CODE)));
            tk_ticket_ctrl.setStep_order(cursor.getInt(cursor.getColumnIndex(STEP_ORDER)));
            tk_ticket_ctrl.setObj_planned(cursor.getInt(cursor.getColumnIndex(OBJ_PLANNED)));
            tk_ticket_ctrl.setCtrl_type(cursor.getString(cursor.getColumnIndex(CTRL_TYPE)));
            if(cursor.isNull(cursor.getColumnIndex(PRODUCT_CODE))){
                tk_ticket_ctrl.setProduct_code(null);
            }else {
                tk_ticket_ctrl.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PRODUCT_ID))){
                tk_ticket_ctrl.setProduct_id(null);
            }else{
                tk_ticket_ctrl.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PRODUCT_DESC))){
                tk_ticket_ctrl.setProduct_desc(null);
            }else{
                tk_ticket_ctrl.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_CODE))){
                tk_ticket_ctrl.setSerial_code(null);
            }else {
                tk_ticket_ctrl.setSerial_code(cursor.getInt(cursor.getColumnIndex(SERIAL_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_ID))){
                tk_ticket_ctrl.setSerial_id(null);
            }else{
                tk_ticket_ctrl.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CTRL_START_DATE))){
                tk_ticket_ctrl.setCtrl_start_date(null);
            }else {
                tk_ticket_ctrl.setCtrl_start_date(cursor.getString(cursor.getColumnIndex(CTRL_START_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CTRL_START_USER))){
                tk_ticket_ctrl.setCtrl_start_user(null);
            }else {
                tk_ticket_ctrl.setCtrl_start_user(cursor.getInt(cursor.getColumnIndex(CTRL_START_USER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CTRL_START_USER_NAME))){
                tk_ticket_ctrl.setCtrl_start_user_name(null);
            }else {
                tk_ticket_ctrl.setCtrl_start_user_name(cursor.getString(cursor.getColumnIndex(CTRL_START_USER_NAME)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CTRL_END_DATE))){
                tk_ticket_ctrl.setCtrl_end_date(null);
            }else{
                tk_ticket_ctrl.setCtrl_end_date(cursor.getString(cursor.getColumnIndex(CTRL_END_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CTRL_END_USER))){
                tk_ticket_ctrl.setCtrl_end_user(null);
            }else{
                tk_ticket_ctrl.setCtrl_end_user(cursor.getInt(cursor.getColumnIndex(CTRL_END_USER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CTRL_END_USER_NAME))){
                tk_ticket_ctrl.setCtrl_end_user_name(null);
            }else{
                tk_ticket_ctrl.setCtrl_end_user_name(cursor.getString(cursor.getColumnIndex(CTRL_END_USER_NAME)));
            }
            //
            tk_ticket_ctrl.setCtrl_status(cursor.getString(cursor.getColumnIndex(CTRL_STATUS)));
            //
            if(cursor.isNull(cursor.getColumnIndex(PARTNER_CODE))){
                tk_ticket_ctrl.setPartner_code(null);
            }else{
                tk_ticket_ctrl.setPartner_code(cursor.getInt(cursor.getColumnIndex(PARTNER_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PARTNER_ID))){
                tk_ticket_ctrl.setPartner_id(null);
            }else{
                tk_ticket_ctrl.setPartner_id(cursor.getString(cursor.getColumnIndex(PARTNER_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PARTNER_DESC))){
                tk_ticket_ctrl.setPartner_desc(null);
            }else{
                tk_ticket_ctrl.setPartner_desc(cursor.getString(cursor.getColumnIndex(PARTNER_DESC)));
            }
            tk_ticket_ctrl.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));
            //
            return tk_ticket_ctrl;
        }
    }

    private class TK_Ticket_CtrlToContentValuesMapper implements Mapper<TK_Ticket_Ctrl, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket_Ctrl tk_ticket_ctrl) {
            ContentValues contentValues = new ContentValues();
            //
            if(tk_ticket_ctrl.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,tk_ticket_ctrl.getCustomer_code());
            }
            if(tk_ticket_ctrl.getTicket_prefix() > -1){
                contentValues.put(TICKET_PREFIX,tk_ticket_ctrl.getTicket_prefix());
            }
            if(tk_ticket_ctrl.getTicket_code() > -1){
                contentValues.put(TICKET_CODE,tk_ticket_ctrl.getTicket_code());
            }
            if(tk_ticket_ctrl.getTicket_seq() > -1){
                contentValues.put(TICKET_SEQ,tk_ticket_ctrl.getTicket_seq());
            }
            if(tk_ticket_ctrl.getTicket_seq_tmp() > -1){
                contentValues.put(TICKET_SEQ_TMP,tk_ticket_ctrl.getTicket_seq_tmp());
            }
            if(tk_ticket_ctrl.getStep_code() > -1){
                contentValues.put(STEP_CODE,tk_ticket_ctrl.getStep_code());
            }
            if(tk_ticket_ctrl.getStep_order() > -1){
                contentValues.put(STEP_ORDER,tk_ticket_ctrl.getStep_order());
            }
            if(tk_ticket_ctrl.getObj_planned() > -1){
                contentValues.put(OBJ_PLANNED,tk_ticket_ctrl.getObj_planned());
            }
            if(tk_ticket_ctrl.getCtrl_type() != null){
                contentValues.put(CTRL_TYPE,tk_ticket_ctrl.getCtrl_type());
            }
            contentValues.put(PRODUCT_CODE,tk_ticket_ctrl.getProduct_code());
            contentValues.put(PRODUCT_ID,tk_ticket_ctrl.getProduct_id());
            contentValues.put(PRODUCT_DESC,tk_ticket_ctrl.getProduct_desc());
            contentValues.put(SERIAL_CODE,tk_ticket_ctrl.getSerial_code());
            contentValues.put(SERIAL_ID,tk_ticket_ctrl.getSerial_id());
            contentValues.put(CTRL_START_DATE,tk_ticket_ctrl.getCtrl_start_date());
            contentValues.put(CTRL_START_USER,tk_ticket_ctrl.getCtrl_start_user());
            contentValues.put(CTRL_START_USER_NAME,tk_ticket_ctrl.getCtrl_start_user_name());
            contentValues.put(CTRL_END_DATE,tk_ticket_ctrl.getCtrl_end_date());
            contentValues.put(CTRL_END_USER,tk_ticket_ctrl.getCtrl_end_user());
            contentValues.put(CTRL_END_USER_NAME,tk_ticket_ctrl.getCtrl_end_user_name());
            if(tk_ticket_ctrl.getCtrl_status() != null){
                contentValues.put(CTRL_STATUS,tk_ticket_ctrl.getCtrl_status());
            }
            contentValues.put(PARTNER_CODE,tk_ticket_ctrl.getPartner_code());
            contentValues.put(PARTNER_ID,tk_ticket_ctrl.getPartner_id());
            contentValues.put(PARTNER_DESC,tk_ticket_ctrl.getPartner_desc());
            if(tk_ticket_ctrl.getUpdate_required() > -1){
                contentValues.put(UPDATE_REQUIRED,tk_ticket_ctrl.getUpdate_required());
            }
            //
            return contentValues;
        }
    }
}
