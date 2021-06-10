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
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.sql.Sql_Act068_002;
import com.namoadigital.prj001.sql.TK_Ticket_Product_Sql_002;
import com.namoadigital.prj001.sql.TK_Ticket_Step_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class TK_TicketDao extends BaseDao implements DaoWithReturn<TK_Ticket> {
    private final Mapper<TK_Ticket, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, TK_Ticket> toTK_TicketMapper;

    public static final String TABLE = "tk_ticket";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String SCN = "scn";
    public static final String USER_LEVEL_MIN = "user_level_min";
    public static final String TICKET_ID = "ticket_id";
    public static final String TYPE_CODE = "type_code";
    public static final String TYPE_ID = "type_id";
    public static final String TYPE_DESC = "type_desc";
    public static final String APP_PERSONAL_DATA = "app_personal_data";
    public static final String TYPE_PATH = "type_path";
    public static final String OPEN_COMMENTS = "open_comments";
    public static final String OPEN_PHOTO = "open_photo";
    public static final String OPEN_PHOTO_LOCAL = "open_photo_local";
    public static final String OPEN_NAME = "open_name";
    public static final String OPEN_EMAIL = "open_email";
    public static final String OPEN_PHONE = "open_phone";
    public static final String OPEN_DATE = "open_date";
    public static final String OPEN_USER = "open_user";
    public static final String OPEN_USER_NAME = "open_user_name";
    public static final String INTERNAL_COMMENTS = "internal_comments";
    public static final String OPEN_SITE_CODE = "open_site_code";
    public static final String OPEN_SITE_ID = "open_site_id";
    public static final String OPEN_SITE_DESC = "open_site_desc";
    public static final String OPEN_OPERATION_CODE = "open_operation_code";
    public static final String OPEN_OPERATION_ID = "open_operation_id";
    public static final String OPEN_OPERATION_DESC = "open_operation_desc";
    public static final String OPEN_PRODUCT_CODE = "open_product_code";
    public static final String OPEN_PRODUCT_ID = "open_product_id";
    public static final String OPEN_PRODUCT_DESC = "open_product_desc";
    public static final String OPEN_SERIAL_CODE = "open_serial_code";
    public static final String OPEN_SERIAL_ID = "open_serial_id";
    public static final String START_DATE = "start_date";
    public static final String FORECAST_DATE = "forecast_date";
    public static final String FORECAST_TIME = "forecast_time";
    public static final String TICKET_STATUS = "ticket_status";
    public static final String CLOSE_DATE = "close_date";
    public static final String CLOSE_USER = "close_user";
    public static final String CLOSE_USER_NAME = "close_user_name";
    public static final String DURATION_MINUTES = "duration_minutes";
    public static final String BARCODE_CODE = "barcode_code";
    public static final String PC_CODE = "pc_code";
    public static final String PC_ID = "pc_id";
    public static final String PC_DESC = "pc_desc";
    public static final String MAIN_USER = "main_user";
    public static final String MAIN_USER_NICK = "main_user_nick";
    public static final String MAIN_USER_NAME = "main_user_name";
    public static final String PIPELINE_CODE = "pipeline_code";
    public static final String PIPELINE_ID = "pipeline_id";
    public static final String PIPELINE_DESC = "pipeline_desc";
    public static final String CURRENT_STEP_ORDER = "current_step_order";
    public static final String APPROVAL_REJECTED = "approval_rejected";
    public static final String ORIGIN_TYPE = "origin_type";
    public static final String ORIGIN_DESC = "origin_desc";
    public static final String INVENTORY_CONTROL = "inventory_control";
    public static final String VALID_STRUCTURE_STEP = "valid_structure_step";
    public static final String USER_FOCUS = "user_focus";
    public static final String ALLOW_STEP_APPROVAL = "allow_step_approval";
    public static final String SYNC_REQUIRED = "sync_required";
    public static final String UPDATE_REQUIRED = "update_required";
    public static final String UPDATE_REQUIRED_PRODUCT = "update_required_product";
    public static final String TOKEN = "token";
    public static final String SCHEDULE_PREFIX = "schedule_prefix";
    public static final String SCHEDULE_CODE = "schedule_code";
    public static final String SCHEDULE_EXEC = "schedule_exec";
    public static final String CLIENT_CODE = "client_code";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_NAME = "client_name";
    public static final String ADDRESS_COUNTRY = "address_country";
    public static final String ADDRESS_STATE = "address_state";
    public static final String ADDRESS_CITY = "address_city";
    public static final String ADDRESS_DISTRICT = "address_district";
    public static final String ADDRESS_STREET = "address_street";
    public static final String ADDRESS_NUM = "address_num";
    public static final String ADDRESS_COMPLEMENT = "address_complement";
    public static final String ADDRESS_ZIPCODE = "address_zipcode";
    public static final String ADDRESS_LAT = "address_lat";
    public static final String ADDRESS_LNG = "address_lng";
    public static final String CONTRACT_CODE = "contract_code";
    public static final String CONTRACT_ID = "contract_id";
    public static final String CONTRACT_DESC = "contract_desc";
    public static final String FCM_SCN = "fcm_scn";
    public static final String TAG_OPERATIONAL_CODE = "tag_operational_code";
    public static final String TAG_OPERATIONAL_ID = "tag_operational_id";
    public static final String TAG_OPERATIONAL_DESC = "tag_operational_desc";

    public TK_TicketDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new TK_TicketToContentValuesMapper();
        this.toTK_TicketMapper = new CursorToTK_TicketMapper();
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket tk_ticket) {
        return addUpdate(tk_ticket, null);
    }

    public DaoObjReturn addUpdate(TK_Ticket tk_ticket, SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket.getTicket_code()).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0) {
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket));
            }
            //LUCHE - 21/07/2020
            //Ctrl será dependendo do step e não do ticket.
            //Tenta inserir action
//                TK_Ticket_CtrlDao ticketCtrlDao = new TK_Ticket_CtrlDao(
//                    context,
//                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                    Constant.DB_VERSION_CUSTOM
//                );
//                //Chama insertUpdate do Ctrl,passando db como param aguardando retorno.
//                daoObjReturn = ticketCtrlDao.addUpdate(tk_ticket.getCtrl(), false, db);
//                //Se erro durante insert, dispara exception abortando o processamento.
//                if (daoObjReturn.hasError()) {
//                    throw new Exception(daoObjReturn.getRawMessage());
//                }
            //Tenta inserir steps
            daoObjReturn = tryAddUpdateSteps(tk_ticket.getStep(),db);
            //Se erro durante insert, dispara exception abortando o processamento.
            if (daoObjReturn.hasError()) {
                throw new Exception(daoObjReturn.getRawMessage());
            }
            //Chama insertUpdate de Produtos, passando db como param aguardando retorno.
            daoObjReturn = tryAddUpdateProducts(tk_ticket.getProduct(),db);
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
    public DaoObjReturn addUpdate(List<TK_Ticket> tk_tickets, boolean status) {
        return addUpdate(tk_tickets, status, null);
    }

    public DaoObjReturn addUpdate(List<TK_Ticket> tk_tickets, boolean status, SQLiteDatabase dbInstance) {
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
            for (TK_Ticket tk_ticket : tk_tickets) {
                //
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket.getTicket_code()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0) {
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket));
                }
                //LUCHE - 21/07/2020
                //Ctrl será dependendo do step e não do ticket.
                //Tenta inserir action
//                TK_Ticket_CtrlDao ticketCtrlDao = new TK_Ticket_CtrlDao(
//                    context,
//                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                    Constant.DB_VERSION_CUSTOM
//                );
//                //Chama insertUpdate do Ctrl,passando db como param aguardando retorno.
//                daoObjReturn = ticketCtrlDao.addUpdate(tk_ticket.getCtrl(), false, db);
//                //Se erro durante insert, dispara exception abortando o processamento.
//                if (daoObjReturn.hasError()) {
//                    throw new Exception(daoObjReturn.getRawMessage());
//                }
                //Chama insertUpdate do Ctrl,passando db como param aguardando retorno.
                daoObjReturn = tryAddUpdateSteps(tk_ticket.getStep(),db);
                //Se erro durante insert, dispara exception abortando o processamento.
                if (daoObjReturn.hasError()) {
                    throw new Exception(daoObjReturn.getRawMessage());
                }
                //Chama insertUpdate de Produtos, passando db como param aguardando retorno.
                daoObjReturn = tryAddUpdateProducts(tk_ticket.getProduct(),db);
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

    public DaoObjReturn addUpdateBySchedulePk(TK_Ticket tk_ticket, SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SCHEDULE_PREFIX).append(" = '").append(tk_ticket.getSchedule_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SCHEDULE_CODE).append(" = '").append(tk_ticket.getSchedule_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SCHEDULE_EXEC).append(" = '").append(tk_ticket.getSchedule_exec()).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0) {
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket));
            }
            //LUCHE - 21/07/2020
            //Ctrl será dependendo do step e não do ticket.
//            TK_Ticket_CtrlDao ticketCtrlDao = new TK_Ticket_CtrlDao(
//                context,
//                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                Constant.DB_VERSION_CUSTOM
//            );
//            //Chama insertUpdate do Ctrl,passando db como param aguardando retorno.
//            daoObjReturn = ticketCtrlDao.addUpdate(tk_ticket.getCtrl(), false, db);
//            //Se erro durante insert, dispara exception abortando o processamento.
//            if (daoObjReturn.hasError()) {
//                throw new Exception(daoObjReturn.getRawMessage());
//            }
            daoObjReturn = tryAddUpdateSteps(tk_ticket.getStep(),db);
            //Se erro durante insert, dispara exception abortando o processamento.
            if (daoObjReturn.hasError()) {
                throw new Exception(daoObjReturn.getRawMessage());
            }
            //Chama insertUpdate de Produtos, passando db como param aguardando retorno.
            daoObjReturn = tryAddUpdateProducts(tk_ticket.getProduct(),db);
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
            daoObjReturn.setTable(TABLE);
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
            ToolBox_Inf.registerException(getClass().getName(),e);
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

    public DaoObjReturn removeFullV2(TK_Ticket tk_ticket) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.DELETE;
        daoObjReturn.setTable(TABLE);
        //
        openDB();
        try {
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket.getTicket_code()).append("'");
            //
            db.beginTransaction();
            //
            db.delete(TABLE, sbWhere.toString(), null);
            db.delete(TK_Ticket_StepDao.TABLE, sbWhere.toString(), null);
            db.delete(TK_Ticket_ProductDao.TABLE, sbWhere.toString(), null);
            db.delete(TK_Ticket_CtrlDao.TABLE, sbWhere.toString(), null);
            db.delete(TK_Ticket_ActionDao.TABLE, sbWhere.toString(), null);
            db.delete(TK_Ticket_ApprovalDao.TABLE, sbWhere.toString(), null);
            db.delete(TK_Ticket_Approval_RejectionDao.TABLE, sbWhere.toString(), null);
            db.delete(TK_Ticket_MeasureDao.TABLE, sbWhere.toString(), null);
            //
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


    /**
     * LUCHE - 04/08/2020
     * COMO NÃO EXISTE MAIS REGRA E QUASE TUDO PODE SER NULL OU NÃO TER ITEM, SUBSTITUIDO METODO
     * PELO removeFullV2
     */
//    @Deprecated
//    public DaoObjReturn removeFull(TK_Ticket tk_ticket) {
//        DaoObjReturn daoObjReturn = new DaoObjReturn();
//        long addUpdateRet = 0;
//        String curAction = DaoObjReturn.DELETE;
//        daoObjReturn.setTable(TABLE);
//        //
//        TK_Ticket_StepDao ticketStepDao = new TK_Ticket_StepDao(
//            context,
//            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//            Constant.DB_VERSION_CUSTOM
//        );
//        TK_Ticket_ProductDao ticketProductDao = new TK_Ticket_ProductDao(
//            context,
//            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//            Constant.DB_VERSION_CUSTOM
//        );
//        //
//        openDB();
//
//        try {
//            db.beginTransaction();
//            //Tenta o delete dos steps e actions
//            daoObjReturn = ticketStepDao.removeFull(tk_ticket, db);
//            //verifica se erro ao remover itens
//            if(daoObjReturn.hasError()){
//                throw new Exception(daoObjReturn.getRawMessage());
//            }
//            //Tenta o delete dos produtos e actions
//            daoObjReturn = ticketProductDao.removeFull(tk_ticket, db);
//            //verifica se erro ao remover itens
//            if(daoObjReturn.hasError()){
//                throw new Exception(daoObjReturn.getRawMessage());
//            }
//            //Se sucesso ao deletar ctrl
//            if (!daoObjReturn.hasError()) {
//                curAction = DaoObjReturn.DELETE;
//                //Where para update
//                StringBuilder sbWhere = new StringBuilder();
//                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket.getCustomer_code()).append("'");
//                sbWhere.append(" and ");
//                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket.getTicket_prefix()).append("'");
//                sbWhere.append(" and ");
//                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket.getTicket_code()).append("'");
//                //Tenta update e armazena retorno
//                addUpdateRet = db.delete(TABLE, sbWhere.toString(), null);
//            }
//            //
//            db.setTransactionSuccessful();
//        }catch (SQLiteException e){
//            //Chama metodo que baseado na exception gera obj de retorno setado como erro
//            //e contendo msg de erro tratada.
//            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage());
//            //
//            ToolBox_Inf.registerException(
//                getClass().getName(),
//                new Exception(
//                    e.getMessage() + "\n" + daoObjReturn.getErrorMsg()
//                )
//            );
//
//        } catch (Exception e) {
//            //Seta obj de retorno com flag de erro e gera arquivo de exception
//            daoObjReturn.setError(true);
//            ToolBox_Inf.registerException(getClass().getName(), e);
//        } finally {
//            db.endTransaction();
//            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
//            //ou rowId do ultimo insert.
//            daoObjReturn.setAction(curAction);
//            daoObjReturn.setActionReturn(addUpdateRet);
//        }
//
//        closeDB();
//
//        return daoObjReturn;
//    }

    @Override
    public TK_Ticket getByString(String sQuery) {
        TK_Ticket tk_ticket = null;
        //
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                tk_ticket = toTK_TicketMapper.map(cursor);
            }
            //
            if (tk_ticket != null) {
                getTicketSteps(tk_ticket);
                getTicketProducts(tk_ticket);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        //
        closeDB();
        //
        return tk_ticket;
    }

    private void getTicketSteps(TK_Ticket tk_ticket) {
        TK_Ticket_StepDao ticketStepDao = new TK_Ticket_StepDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        tk_ticket.setStep(
            (ArrayList<TK_Ticket_Step>) ticketStepDao.query(
                new TK_Ticket_Step_Sql_002(
                    tk_ticket.getCustomer_code(),
                    tk_ticket.getTicket_prefix(),
                    tk_ticket.getTicket_code()
                ).toSqlQuery()
            )
        );
    }

    private void getTicketProducts(TK_Ticket tk_ticket) {
        TK_Ticket_ProductDao ticketProductDao = new TK_Ticket_ProductDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        tk_ticket.setProduct(
            (ArrayList<TK_Ticket_Product>) ticketProductDao.query(
                new TK_Ticket_Product_Sql_002(
                    tk_ticket.getCustomer_code(),
                    tk_ticket.getTicket_prefix(),
                    tk_ticket.getTicket_code()
                ).toSqlQuery()
            )
        );
    }

    private DaoObjReturn tryAddUpdateSteps(ArrayList<TK_Ticket_Step> tk_ticket_steps, SQLiteDatabase db) {
        TK_Ticket_StepDao ticketStepDao = new TK_Ticket_StepDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //Chama insertUpdate do Step,passando db como param aguardando retorno.
        return ticketStepDao.addUpdate(tk_ticket_steps, false, db);
    }

    private DaoObjReturn tryAddUpdateProducts(ArrayList<TK_Ticket_Product> tk_ticket_products, SQLiteDatabase db) {
        TK_Ticket_ProductDao ticketProductDao = new TK_Ticket_ProductDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //Chama insertUpdate do Step,passando db como param aguardando retorno.
        return ticketProductDao.addUpdate(tk_ticket_products, false, db);
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
    public List<TK_Ticket> query(String sQuery) {
        List<TK_Ticket> tk_tickets = new ArrayList<>();
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                TK_Ticket uAux = toTK_TicketMapper.map(cursor);
                //
                if (uAux != null) {
                    getTicketSteps(uAux);
                    getTicketProducts(uAux);
                }
                //
                tk_tickets.add(uAux);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        closeDB();
        return tk_tickets;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> tk_tickets = new ArrayList<>();
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);
            while (cursor.moveToNext()) {
                tk_tickets.add(CursorToHMAuxMapper.mapN(cursor));
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        closeDB();
        return tk_tickets;
    }

    private class CursorToTK_TicketMapper implements Mapper<Cursor, TK_Ticket> {
        @Override
        public TK_Ticket map(Cursor cursor) {
            TK_Ticket tk_ticket = new TK_Ticket();
            //
            tk_ticket.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket.setScn(cursor.getInt(cursor.getColumnIndex(SCN)));
            tk_ticket.setUser_level_min(cursor.getInt(cursor.getColumnIndex(USER_LEVEL_MIN)));
            tk_ticket.setTicket_id(cursor.getString(cursor.getColumnIndex(TICKET_ID)));
            tk_ticket.setType_code(cursor.getInt(cursor.getColumnIndex(TYPE_CODE)));
            tk_ticket.setType_id(cursor.getString(cursor.getColumnIndex(TYPE_ID)));
            tk_ticket.setType_desc(cursor.getString(cursor.getColumnIndex(TYPE_DESC)));
            tk_ticket.setApp_personal_data(cursor.getInt(cursor.getColumnIndex(APP_PERSONAL_DATA)));
            if (cursor.isNull(cursor.getColumnIndex(TYPE_PATH))) {
                tk_ticket.setType_path(null);
            } else {
                tk_ticket.setType_path(cursor.getString(cursor.getColumnIndex(TYPE_PATH)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OPEN_COMMENTS))) {
                tk_ticket.setOpen_comments(null);
            } else {
                tk_ticket.setOpen_comments(cursor.getString(cursor.getColumnIndex(OPEN_COMMENTS)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OPEN_PHOTO))) {
                tk_ticket.setOpen_photo(null);
            } else {
                tk_ticket.setOpen_photo(cursor.getString(cursor.getColumnIndex(OPEN_PHOTO)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OPEN_PHOTO_LOCAL))) {
                tk_ticket.setOpen_photo_local(null);
            } else {
                tk_ticket.setOpen_photo_local(cursor.getString(cursor.getColumnIndex(OPEN_PHOTO_LOCAL)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OPEN_NAME))) {
                tk_ticket.setOpen_name(null);
            } else {
                tk_ticket.setOpen_name(cursor.getString(cursor.getColumnIndex(OPEN_NAME)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OPEN_EMAIL))) {
                tk_ticket.setOpen_email(null);
            } else {
                tk_ticket.setOpen_email(cursor.getString(cursor.getColumnIndex(OPEN_EMAIL)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OPEN_PHONE))) {
                tk_ticket.setOpen_phone(null);
            } else {
                tk_ticket.setOpen_phone(cursor.getString(cursor.getColumnIndex(OPEN_PHONE)));
            }
            tk_ticket.setOpen_date(cursor.getString(cursor.getColumnIndex(OPEN_DATE)));
            tk_ticket.setOpen_user(cursor.getInt(cursor.getColumnIndex(OPEN_USER)));
            tk_ticket.setOpen_user_name(cursor.getString(cursor.getColumnIndex(OPEN_USER_NAME)));
            if (cursor.isNull(cursor.getColumnIndex(INTERNAL_COMMENTS))) {
                tk_ticket.setInternal_comments(null);
            } else {
                tk_ticket.setInternal_comments(cursor.getString(cursor.getColumnIndex(INTERNAL_COMMENTS)));
            }
            tk_ticket.setOpen_site_code(cursor.getInt(cursor.getColumnIndex(OPEN_SITE_CODE)));
            tk_ticket.setOpen_site_id(cursor.getString(cursor.getColumnIndex(OPEN_SITE_ID)));
            tk_ticket.setOpen_site_desc(cursor.getString(cursor.getColumnIndex(OPEN_SITE_DESC)));
            tk_ticket.setOpen_operation_code(cursor.getInt(cursor.getColumnIndex(OPEN_OPERATION_CODE)));
            tk_ticket.setOpen_operation_id(cursor.getString(cursor.getColumnIndex(OPEN_OPERATION_ID)));
            tk_ticket.setOpen_operation_desc(cursor.getString(cursor.getColumnIndex(OPEN_OPERATION_DESC)));
            tk_ticket.setOpen_product_code(cursor.getInt(cursor.getColumnIndex(OPEN_PRODUCT_CODE)));
            tk_ticket.setOpen_product_id(cursor.getString(cursor.getColumnIndex(OPEN_PRODUCT_ID)));
            tk_ticket.setOpen_product_desc(cursor.getString(cursor.getColumnIndex(OPEN_PRODUCT_DESC)));
            tk_ticket.setOpen_serial_code(cursor.getInt(cursor.getColumnIndex(OPEN_SERIAL_CODE)));
            tk_ticket.setOpen_serial_id(cursor.getString(cursor.getColumnIndex(OPEN_SERIAL_ID)));
            tk_ticket.setStart_date(cursor.getString(cursor.getColumnIndex(START_DATE)));

            if (cursor.isNull(cursor.getColumnIndex(FORECAST_DATE))) {
                tk_ticket.setForecast_date(null);
            } else {
                tk_ticket.setForecast_date(cursor.getString(cursor.getColumnIndex(FORECAST_DATE)));
            }

            tk_ticket.setForecast_time(cursor.getString(cursor.getColumnIndex(FORECAST_TIME)));
            tk_ticket.setTicket_status(cursor.getString(cursor.getColumnIndex(TICKET_STATUS)));
            if (cursor.isNull(cursor.getColumnIndex(CLOSE_DATE))) {
                tk_ticket.setClose_date(null);
            } else {
                tk_ticket.setClose_date(cursor.getString(cursor.getColumnIndex(CLOSE_DATE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLOSE_USER))) {
                tk_ticket.setClose_user(null);
            } else {
                tk_ticket.setClose_user(cursor.getInt(cursor.getColumnIndex(CLOSE_USER)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLOSE_USER_NAME))) {
                tk_ticket.setClose_user_name(null);
            } else {
                tk_ticket.setClose_user_name(cursor.getString(cursor.getColumnIndex(CLOSE_USER_NAME)));
            }
            if (cursor.isNull(cursor.getColumnIndex(DURATION_MINUTES))) {
                tk_ticket.setDuration_minutes(null);
            } else {
                tk_ticket.setDuration_minutes(cursor.getInt(cursor.getColumnIndex(DURATION_MINUTES)));
            }
            if (cursor.isNull(cursor.getColumnIndex(BARCODE_CODE))) {
                tk_ticket.setBarcode_code(null);
            } else {
                tk_ticket.setBarcode_code(cursor.getInt(cursor.getColumnIndex(BARCODE_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PC_CODE))) {
                tk_ticket.setPc_code(null);
            } else {
                tk_ticket.setPc_code(cursor.getInt(cursor.getColumnIndex(PC_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PC_ID))) {
                tk_ticket.setPc_id(null);
            } else {
                tk_ticket.setPc_id(cursor.getString(cursor.getColumnIndex(PC_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PC_DESC))) {
                tk_ticket.setPc_desc(null);
            } else {
                tk_ticket.setPc_desc(cursor.getString(cursor.getColumnIndex(PC_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(MAIN_USER))) {
                tk_ticket.setMain_user(null);
            } else {
                tk_ticket.setMain_user(cursor.getInt(cursor.getColumnIndex(MAIN_USER)));
            }
            if (cursor.isNull(cursor.getColumnIndex(MAIN_USER_NICK))) {
                tk_ticket.setMain_user_nick(null);
            } else {
                tk_ticket.setMain_user_nick(cursor.getString(cursor.getColumnIndex(MAIN_USER_NICK)));
            }
            if (cursor.isNull(cursor.getColumnIndex(MAIN_USER_NAME))) {
                tk_ticket.setMain_user_name(null);
            } else {
                tk_ticket.setMain_user_name(cursor.getString(cursor.getColumnIndex(MAIN_USER_NAME)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PIPELINE_CODE))) {
                tk_ticket.setPipeline_code(null);
            } else {
                tk_ticket.setPipeline_code(cursor.getInt(cursor.getColumnIndex(PIPELINE_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PIPELINE_ID))) {
                tk_ticket.setPipeline_id(null);
            } else {
                tk_ticket.setPipeline_id(cursor.getString(cursor.getColumnIndex(PIPELINE_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PIPELINE_DESC))) {
                tk_ticket.setPipeline_desc(null);
            } else {
                tk_ticket.setPipeline_desc(cursor.getString(cursor.getColumnIndex(PIPELINE_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CURRENT_STEP_ORDER))) {
                tk_ticket.setCurrent_step_order(null);
            } else {
                tk_ticket.setCurrent_step_order(cursor.getInt(cursor.getColumnIndex(CURRENT_STEP_ORDER)));
            }
            tk_ticket.setApproval_rejected(cursor.getInt(cursor.getColumnIndex(APPROVAL_REJECTED)));
            tk_ticket.setOrigin_type(cursor.getString(cursor.getColumnIndex(ORIGIN_TYPE)));
            tk_ticket.setOrigin_desc(cursor.getString(cursor.getColumnIndex(ORIGIN_DESC)));
            tk_ticket.setValid_structure_step(cursor.getInt(cursor.getColumnIndex(VALID_STRUCTURE_STEP)));
            tk_ticket.setInventory_control(cursor.getInt(cursor.getColumnIndex(INVENTORY_CONTROL)));
            tk_ticket.setUser_focus(cursor.getInt(cursor.getColumnIndex(USER_FOCUS)));
            tk_ticket.setAllow_step_approval(cursor.getInt(cursor.getColumnIndex(ALLOW_STEP_APPROVAL)));
            //
            tk_ticket.setSync_required(cursor.getInt(cursor.getColumnIndex(SYNC_REQUIRED)));
            tk_ticket.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));
            tk_ticket.setUpdate_required_product(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED_PRODUCT)));
            if (cursor.isNull(cursor.getColumnIndex(TOKEN))) {
                tk_ticket.setToken(null);
            } else {
                tk_ticket.setToken(cursor.getString(cursor.getColumnIndex(TOKEN)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SCHEDULE_PREFIX))) {
                tk_ticket.setSchedule_prefix(null);
            } else {
                tk_ticket.setSchedule_prefix(cursor.getInt(cursor.getColumnIndex(SCHEDULE_PREFIX)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SCHEDULE_CODE))) {
                tk_ticket.setSchedule_code(null);
            } else {
                tk_ticket.setSchedule_code(cursor.getInt(cursor.getColumnIndex(SCHEDULE_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SCHEDULE_EXEC))) {
                tk_ticket.setSchedule_exec(null);
            } else {
                tk_ticket.setSchedule_exec(cursor.getInt(cursor.getColumnIndex(SCHEDULE_EXEC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLIENT_CODE))) {
                tk_ticket.setClient_code(null);
            } else {
                tk_ticket.setClient_code(cursor.getInt(cursor.getColumnIndex(CLIENT_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLIENT_ID))) {
                tk_ticket.setClient_id(null);
            } else {
                tk_ticket.setClient_id(cursor.getString(cursor.getColumnIndex(CLIENT_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLIENT_NAME))) {
                tk_ticket.setClient_name(null);
            } else {
                tk_ticket.setClient_name(cursor.getString(cursor.getColumnIndex(CLIENT_NAME)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ADDRESS_COUNTRY))) {
                tk_ticket.setAddress_country(null);
            } else {
                tk_ticket.setAddress_country(cursor.getString(cursor.getColumnIndex(ADDRESS_COUNTRY)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ADDRESS_STATE))) {
                tk_ticket.setAddress_state(null);
            } else {
                tk_ticket.setAddress_state(cursor.getString(cursor.getColumnIndex(ADDRESS_STATE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ADDRESS_CITY))) {
                tk_ticket.setAddress_city(null);
            } else {
                tk_ticket.setAddress_city(cursor.getString(cursor.getColumnIndex(ADDRESS_CITY)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ADDRESS_DISTRICT))) {
                tk_ticket.setAddress_district(null);
            } else {
                tk_ticket.setAddress_district(cursor.getString(cursor.getColumnIndex(ADDRESS_DISTRICT)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ADDRESS_STREET))) {
                tk_ticket.setAddress_street(null);
            } else {
                tk_ticket.setAddress_street(cursor.getString(cursor.getColumnIndex(ADDRESS_STREET)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ADDRESS_NUM))) {
                tk_ticket.setAddress_num(null);
            } else {
                tk_ticket.setAddress_num(cursor.getString(cursor.getColumnIndex(ADDRESS_NUM)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ADDRESS_COMPLEMENT))) {
                tk_ticket.setAddress_complement(null);
            } else {
                tk_ticket.setAddress_complement(cursor.getString(cursor.getColumnIndex(ADDRESS_COMPLEMENT)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ADDRESS_ZIPCODE))) {
                tk_ticket.setAddress_zipcode(null);
            } else {
                tk_ticket.setAddress_zipcode(cursor.getString(cursor.getColumnIndex(ADDRESS_ZIPCODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ADDRESS_LAT))) {
                tk_ticket.setAddress_lat(null);
            } else {
                tk_ticket.setAddress_lat(cursor.getString(cursor.getColumnIndex(ADDRESS_LAT)));
            }
            if (cursor.isNull(cursor.getColumnIndex(ADDRESS_LNG))) {
                tk_ticket.setAddress_lng(null);
            } else {
                tk_ticket.setAddress_lng(cursor.getString(cursor.getColumnIndex(ADDRESS_LNG)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CONTRACT_CODE))) {
                tk_ticket.setContract_code(null);
            } else {
                tk_ticket.setContract_code(cursor.getInt(cursor.getColumnIndex(CONTRACT_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CONTRACT_ID))) {
                tk_ticket.setContract_id(null);
            } else {
                tk_ticket.setContract_id(cursor.getString(cursor.getColumnIndex(CONTRACT_ID)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CONTRACT_DESC))) {
                tk_ticket.setContract_desc(null);
            } else {
                tk_ticket.setContract_desc(cursor.getString(cursor.getColumnIndex(CONTRACT_DESC)));
            }
            tk_ticket.setFcm_scn(cursor.getInt(cursor.getColumnIndex(FCM_SCN)));
            tk_ticket.setTag_operational_code(cursor.getInt(cursor.getColumnIndex(TAG_OPERATIONAL_CODE)));
            tk_ticket.setTag_operational_id(cursor.getString(cursor.getColumnIndex(TAG_OPERATIONAL_ID)));
            tk_ticket.setTag_operational_desc(cursor.getString(cursor.getColumnIndex(TAG_OPERATIONAL_DESC)));
            return tk_ticket;
        }
    }

    private class TK_TicketToContentValuesMapper implements Mapper<TK_Ticket, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket tk_ticket) {
            ContentValues contentValues = new ContentValues();
            if (tk_ticket.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, tk_ticket.getCustomer_code());
            }
            if (tk_ticket.getTicket_prefix() > -1) {
                contentValues.put(TICKET_PREFIX, tk_ticket.getTicket_prefix());
            }
            if (tk_ticket.getTicket_code() > -1) {
                contentValues.put(TICKET_CODE, tk_ticket.getTicket_code());
            }
            if (tk_ticket.getScn() > -1) {
                contentValues.put(SCN, tk_ticket.getScn());
            }
            if (tk_ticket.getUser_level_min() > -1) {
                contentValues.put(USER_LEVEL_MIN, tk_ticket.getUser_level_min());
            }
            if (tk_ticket.getTicket_id() != null) {
                contentValues.put(TICKET_ID, tk_ticket.getTicket_id());
            }
            if (tk_ticket.getType_code() > -1) {
                contentValues.put(TYPE_CODE, tk_ticket.getType_code());
            }
            if (tk_ticket.getType_id() != null) {
                contentValues.put(TYPE_ID, tk_ticket.getType_id());
            }
            if (tk_ticket.getType_desc() != null) {
                contentValues.put(TYPE_DESC, tk_ticket.getType_desc());
            }
            if (tk_ticket.getApp_personal_data() > -1) {
                contentValues.put(APP_PERSONAL_DATA, tk_ticket.getApp_personal_data());
            }
            contentValues.put(TYPE_PATH, tk_ticket.getType_path());
            contentValues.put(OPEN_COMMENTS, tk_ticket.getOpen_comments());
            contentValues.put(OPEN_PHOTO, tk_ticket.getOpen_photo());
            contentValues.put(OPEN_PHOTO_LOCAL, tk_ticket.getOpen_photo_local());
            contentValues.put(OPEN_NAME, tk_ticket.getOpen_name());
            contentValues.put(OPEN_EMAIL, tk_ticket.getOpen_email());
            contentValues.put(OPEN_PHONE, tk_ticket.getOpen_phone());
            if (tk_ticket.getOpen_date() != null) {
                contentValues.put(OPEN_DATE, tk_ticket.getOpen_date());
            }
            if (tk_ticket.getOpen_user() > -1) {
                contentValues.put(OPEN_USER, tk_ticket.getOpen_user());
            }
            if (tk_ticket.getOpen_user_name() != null) {
                contentValues.put(OPEN_USER_NAME, tk_ticket.getOpen_user_name());
            }
            contentValues.put(INTERNAL_COMMENTS, tk_ticket.getInternal_comments());
            if (tk_ticket.getOpen_site_code() > -1) {
                contentValues.put(OPEN_SITE_CODE, tk_ticket.getOpen_site_code());
            }
            if (tk_ticket.getOpen_site_id() != null) {
                contentValues.put(OPEN_SITE_ID, tk_ticket.getOpen_site_id());
            }
            if (tk_ticket.getOpen_site_desc() != null) {
                contentValues.put(OPEN_SITE_DESC, tk_ticket.getOpen_site_desc());
            }
            if (tk_ticket.getOpen_operation_code() > -1) {
                contentValues.put(OPEN_OPERATION_CODE, tk_ticket.getOpen_operation_code());
            }
            if (tk_ticket.getOpen_operation_id() != null) {
                contentValues.put(OPEN_OPERATION_ID, tk_ticket.getOpen_operation_id());
            }
            if (tk_ticket.getOpen_operation_desc() != null) {
                contentValues.put(OPEN_OPERATION_DESC, tk_ticket.getOpen_operation_desc());
            }
            if (tk_ticket.getOpen_product_code() > -1) {
                contentValues.put(OPEN_PRODUCT_CODE, tk_ticket.getOpen_product_code());
            }
            if (tk_ticket.getOpen_product_id() != null) {
                contentValues.put(OPEN_PRODUCT_ID, tk_ticket.getOpen_product_id());
            }
            if (tk_ticket.getOpen_product_desc() != null) {
                contentValues.put(OPEN_PRODUCT_DESC, tk_ticket.getOpen_product_desc());
            }
            if (tk_ticket.getOpen_serial_code() > -1) {
                contentValues.put(OPEN_SERIAL_CODE, tk_ticket.getOpen_serial_code());
            }
            if (tk_ticket.getOpen_serial_id() != null) {
                contentValues.put(OPEN_SERIAL_ID, tk_ticket.getOpen_serial_id());
            }
            if (tk_ticket.getStart_date() != null) {
                contentValues.put(START_DATE, tk_ticket.getStart_date());
            }
            contentValues.put(FORECAST_DATE, tk_ticket.getForecast_date());

            if (tk_ticket.getForecast_time() != null) {
                contentValues.put(FORECAST_TIME, tk_ticket.getForecast_time());
            }
            if (tk_ticket.getTicket_status() != null) {
                contentValues.put(TICKET_STATUS, tk_ticket.getTicket_status());
            }

            contentValues.put(CLOSE_DATE, tk_ticket.getClose_date());
            contentValues.put(CLOSE_USER, tk_ticket.getClose_user());
            contentValues.put(CLOSE_USER_NAME, tk_ticket.getClose_user_name());
            contentValues.put(DURATION_MINUTES, tk_ticket.getDuration_minutes());
            contentValues.put(BARCODE_CODE, tk_ticket.getBarcode_code());
            contentValues.put(BARCODE_CODE, tk_ticket.getBarcode_code());
            contentValues.put(PC_CODE,tk_ticket.getPc_code());
            contentValues.put(PC_ID, tk_ticket.getPc_id());
            contentValues.put(PC_DESC,tk_ticket.getPc_desc());
            contentValues.put(MAIN_USER, tk_ticket.getMain_user());
            contentValues.put(MAIN_USER_NICK,tk_ticket.getMain_user_nick());
            contentValues.put(MAIN_USER_NAME,tk_ticket.getMain_user_name());
            contentValues.put(PIPELINE_CODE, tk_ticket.getPipeline_code());
            contentValues.put(PIPELINE_ID, tk_ticket.getPipeline_id());
            contentValues.put(PIPELINE_DESC, tk_ticket.getPipeline_desc());
            contentValues.put(CURRENT_STEP_ORDER, tk_ticket.getCurrent_step_order());
            if (tk_ticket.getApproval_rejected() > -1) {
                contentValues.put(APPROVAL_REJECTED, tk_ticket.getApproval_rejected());
            }
            if (tk_ticket.getOrigin_type() != null) {
                contentValues.put(ORIGIN_TYPE, tk_ticket.getOrigin_type());
            }
            if (tk_ticket.getOrigin_desc() != null) {
                contentValues.put(ORIGIN_DESC, tk_ticket.getOrigin_desc());
            }
            if (tk_ticket.getValid_structure_step() > -1) {
                contentValues.put(VALID_STRUCTURE_STEP, tk_ticket.getValid_structure_step());
            }
            if (tk_ticket.getApproval_rejected() > -1) {
                contentValues.put(INVENTORY_CONTROL, tk_ticket.getInventory_control());
            }
            if (tk_ticket.getApproval_rejected() > -1) {
                contentValues.put(USER_FOCUS, tk_ticket.getUser_focus());
            }
            if (tk_ticket.getApproval_rejected() > -1) {
                contentValues.put(ALLOW_STEP_APPROVAL, tk_ticket.getAllow_step_approval());
            }
            /**
             * Atualizar somente via query update para evitar sobreposicao com o update do Ticket.
             * Atualiza com 0 quando Ticket Full e através do recebimento do GCM
             */
//            if (tk_ticket.getSync_required() > -1) {
//                contentValues.put(SYNC_REQUIRED, tk_ticket.getSync_required());
//            }
            if (tk_ticket.getUpdate_required() > -1) {
                contentValues.put(UPDATE_REQUIRED, tk_ticket.getUpdate_required());
            }
            if (tk_ticket.getUpdate_required_product() > -1) {
                contentValues.put(UPDATE_REQUIRED_PRODUCT, tk_ticket.getUpdate_required_product());
            }
            //TODO REVER CAMPO TOKEN POIS ESTA MUITO ESTRANHO
            tk_ticket.setToken(tk_ticket.getToken());
            contentValues.put(SCHEDULE_PREFIX,tk_ticket.getSchedule_prefix());
            contentValues.put(SCHEDULE_CODE,tk_ticket.getSchedule_code());
            contentValues.put(SCHEDULE_EXEC,tk_ticket.getSchedule_exec());
            //Campos show off
            contentValues.put(CLIENT_CODE, tk_ticket.getClient_code());
            contentValues.put(CLIENT_ID, tk_ticket.getClient_id());
            contentValues.put(CLIENT_NAME, tk_ticket.getClient_name());
            contentValues.put(ADDRESS_COUNTRY, tk_ticket.getAddress_country());
            contentValues.put(ADDRESS_STATE, tk_ticket.getAddress_state());
            contentValues.put(ADDRESS_CITY, tk_ticket.getAddress_city());
            contentValues.put(ADDRESS_DISTRICT, tk_ticket.getAddress_district());
            contentValues.put(ADDRESS_STREET, tk_ticket.getAddress_street());
            contentValues.put(ADDRESS_NUM, tk_ticket.getAddress_num());
            contentValues.put(ADDRESS_COMPLEMENT, tk_ticket.getAddress_complement());
            contentValues.put(ADDRESS_ZIPCODE, tk_ticket.getAddress_zipcode());
            contentValues.put(ADDRESS_LAT, tk_ticket.getAddress_lat());
            contentValues.put(ADDRESS_LNG, tk_ticket.getAddress_lng());
            contentValues.put(CONTRACT_CODE, tk_ticket.getContract_code());
            contentValues.put(CONTRACT_ID, tk_ticket.getContract_id());
            contentValues.put(CONTRACT_DESC, tk_ticket.getContract_desc());
            //
            if (tk_ticket.getFcm_scn() > -1) {
                contentValues.put(FCM_SCN, tk_ticket.getFcm_scn());
            }
            if (tk_ticket.getTag_operational_code() > -1) {
                contentValues.put(TAG_OPERATIONAL_CODE, tk_ticket.getTag_operational_code());
            }
            if (tk_ticket.getTag_operational_id() != null) {
                contentValues.put(TAG_OPERATIONAL_ID, tk_ticket.getTag_operational_id());
            }
            if (tk_ticket.getTag_operational_desc() != null) {
                contentValues.put(TAG_OPERATIONAL_DESC, tk_ticket.getTag_operational_desc());
            }
            //
            return contentValues;
        }
    }


    public boolean hasSyncRequired(){
        ArrayList<HMAux> tickets = (ArrayList<HMAux>)  this.query_HM(
                new Sql_Act068_002(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        int qty = 0;
        if (tickets != null) {
            qty = tickets.size();
        }
        //
        return qty > 0;
    }
}
