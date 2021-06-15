package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.MD_Schedule_Exec_Operation;
import com.namoadigital.prj001.model.MD_Schedule_Exec_Product;
import com.namoadigital.prj001.model.MD_Schedule_Exec_Site;
import com.namoadigital.prj001.model.MdTag;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Dao_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_003;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_008;
import com.namoadigital.prj001.sql.MdTagSql001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class MD_Schedule_ExecDao extends BaseDao implements DaoWithReturn<MD_Schedule_Exec> {
    private final Mapper <MD_Schedule_Exec, ContentValues> toContentValuesMapper;
    private final Mapper <Cursor, MD_Schedule_Exec> toMD_Schedule_ExecMapper;

    public static final String TABLE = "md_schedule_exec";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SCHEDULE_PREFIX = "schedule_prefix";
    public static final String SCHEDULE_CODE = "schedule_code";
    public static final String SCHEDULE_EXEC = "schedule_exec";
    public static final String SCHEDULE_DESC = "schedule_desc";
    public static final String SCHEDULE_TYPE = "schedule_type";
    public static final String SITE_CODE = "site_code";
    public static final String SITE_ID = "site_id";
    public static final String SITE_DESC = "site_desc";
    public static final String OPERATION_CODE  = "operation_code";
    public static final String OPERATION_ID  = "operation_id";
    public static final String OPERATION_DESC  = "operation_desc";
    public static final String PRODUCT_CODE  = "product_code";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DESC = "product_desc";
    public static final String SERIAL_CODE   = "serial_code";
    public static final String SERIAL_ID = "serial_id";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION  = "custom_form_version";
    public static final String CUSTOM_FORM_DESC = "custom_form_desc";
    public static final String TICKET_TYPE = "ticket_type";
    public static final String TICKET_TYPE_ID = "ticket_type_id";
    public static final String TICKET_TYPE_DESC = "ticket_type_desc";
    public static final String LOCAL_CONTROL = "local_control";
    public static final String IO_CONTROL = "io_control";
    public static final String SERIAL_RULE = "serial_rule";
    public static final String SERIAL_MIN_LENGTH = "serial_min_length";
    public static final String SERIAL_MAX_LENGTH = "serial_max_length";
    public static final String SITE_RESTRICTION = "site_restriction";
    public static final String PRODUCT_ICON_NAME = "product_icon_name";
    public static final String PRODUCT_ICON_URL = "product_icon_url";
    public static final String PRODUCT_ICON_URL_LOCAL = "product_icon_url_local";
    public static final String REQUIRE_LOCATION = "require_location";
    public static final String DATE_START = "date_start";
    public static final String DATE_END  = "date_end";
    public static final String COMMENTS  = "comments";
    public static final String STATUS  = "status";
    public static final String SYNC_PROCESS  = "sync_process";
    public static final String REQUIRE_SERIAL = "require_serial";
    public static final String ALLOW_NEW_SERIAL_CL = "allow_new_serial_cl";
    public static final String REQUIRE_SERIAL_DONE = "require_serial_done";
    public static final String FCM_NEW_STATUS = "fcm_new_status";
    public static final String FCM_USER_NICK = "fcm_user_nick";
    public static final String SCHEDULE_ERRO_MSG = "schedule_erro_msg";
    public static final String CLOSE_DATE = "close_date";
    public static final String TAG_OPERATIONAL_CODE = "tag_operational_code";
    public static final String TAG_OPERATIONAL_ID = "tag_operational_id";
    public static final String TAG_OPERATIONAL_DESC = "tag_operational_desc";

    //NÃO SÃO CAMPOS DA TABELA, mas são usados em queries
    public static final String SCHEDULE_DATE_START_FORMAT = "schedule_date_start_format";
    public static final String SCHEDULE_DATE_END_FORMAT = "schedule_date_end_format";
    //LUCHE - 17/03/2020
    //Constante não usada no banco, mas usada por varias telas.
    public static final String SCHEDULE_PK = "schedule_pk";
    //LUCHE - 28/05/2021
    //Constante não usada no banco, mas usada por varias telas.
    public static final String HAS_NC = "HAS_NC";


    public MD_Schedule_ExecDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        toContentValuesMapper = new MD_Schedule_ExecToContentValuesMapper();
        toMD_Schedule_ExecMapper = new CursorToMD_Schedule_ExecMapper();
    }

    @Override
    public DaoObjReturn addUpdate(MD_Schedule_Exec md_schedule_exec) {
        DaoObjReturn daoObjReturn = new DaoObjReturn(TABLE);
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        openDB();
        //
        try {
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(md_schedule_exec.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SCHEDULE_PREFIX).append(" = '").append(md_schedule_exec.getSchedule_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SCHEDULE_CODE).append(" = '").append(md_schedule_exec.getSchedule_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SCHEDULE_EXEC).append(" = '").append(md_schedule_exec.getSchedule_exec()).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(md_schedule_exec), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0) {
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(md_schedule_exec));
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
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }
        //
        closeDB();
        //
        return daoObjReturn;
    }

    @Override
    public DaoObjReturn addUpdate(List<MD_Schedule_Exec> md_schedule_execs, boolean status) {
        return addUpdate(md_schedule_execs,status,null);
    }

    public DaoObjReturn addUpdate(List<MD_Schedule_Exec> md_schedule_execs, boolean status, SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn(TABLE);
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
            for (MD_Schedule_Exec md_schedule_exec : md_schedule_execs) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(md_schedule_exec.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SCHEDULE_PREFIX).append(" = '").append(md_schedule_exec.getSchedule_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SCHEDULE_CODE).append(" = '").append(md_schedule_exec.getSchedule_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SCHEDULE_EXEC).append(" = '").append(md_schedule_exec.getSchedule_exec()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(md_schedule_exec), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0) {
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(md_schedule_exec));
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
            //
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

    @Override
    public MD_Schedule_Exec getByString(String sQuery) {
        return getByString(sQuery,null);
    }

    public MD_Schedule_Exec getByString(String sQuery,SQLiteDatabase dbInstance) {
        MD_Schedule_Exec md_schedule_exec = null;
        //
        if (dbInstance == null) {
            openDB();
        } else {
            this.db = dbInstance;
        }
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_schedule_exec = toMD_Schedule_ExecMapper.map(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        //
        if (dbInstance == null) {
            closeDB();
        }
        //
        return md_schedule_exec;
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
    public List<MD_Schedule_Exec> query(String sQuery) {
        List<MD_Schedule_Exec> md_schedule_execs = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                MD_Schedule_Exec uAux = toMD_Schedule_ExecMapper.map(cursor);
                //
                md_schedule_execs.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return md_schedule_execs;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> md_schedule_execs = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                md_schedule_execs.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return md_schedule_execs;
    }

    /**
     * LUCHE - 11/02/2020
     *
     * Metodo responsavel pela conciliação dos agendamentos durante o sincronismo.
     *
     *  - Inclui novos agendamentos
     *  - Atualiza agendamentos ja existentes e que NÃO foram iniciados ou executados
     *  - Exclui agendamentos que NÃO foram recebidos no sincronismo e que NÃO foram iniciados ou executados
     *
     * LUCHE - 15/05/2020
     * Modificado metodo , adicionando a atualização de informações sempre que o agendamento ainda estiver no status SCHEDULE.
     * Antigamente, como os dados não se alteravam e só era possivel criar agendamento pra produtos que o usr tem,
     * os dados relacionais eram inseridos apenas no insert.
     *
     * @param receivedScheduleExecs - Agendamentos recebidos no sincronismo.
     * @param scheduleExecSiteList
     * @param scheduleExecOperationList
     * @param scheduleExecProductList
     * @return
     */
    public DaoObjReturn processConciliation(ArrayList<MD_Schedule_Exec> receivedScheduleExecs, ArrayList<MD_Schedule_Exec_Site> scheduleExecSiteList, ArrayList<MD_Schedule_Exec_Operation> scheduleExecOperationList, ArrayList<MD_Schedule_Exec_Product> scheduleExecProductList) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.DELETE;
        //
        openDB();
        //
        try {
            db.beginTransaction();
            //Lista de form_locals a serem atualizados.
            ArrayList<GE_Custom_Form_Local> formLocalsToUpdate = new ArrayList<>();
            //
            for (int i = 0; i < receivedScheduleExecs.size(); i++) {
                MD_Schedule_Exec scheduleExec = receivedScheduleExecs.get(i);
                //
                MD_Schedule_Exec dbSchedule = getByString(new MD_Schedule_Exec_Sql_001(
                        scheduleExec.getCustomer_code(),
                        scheduleExec.getSchedule_prefix(),
                        scheduleExec.getSchedule_code(),
                        scheduleExec.getSchedule_exec()
                    ).toSqlQuery(),db
                );
                //LUCHE - 15/05/2020
                //Alterado conciliação para atualiza tb os dados de agendamento ja existentes desde
                // que seu status seja schedule. Além disso, incluido atualiação do registro de
                //form local, caso exista.
                //Se a existe o agendamento, verifica necessidade atualização do ddados.
                if(dbSchedule != null){
                    //Se o agendamento esta em status schedule atualiza os dados do produto e caso
                    //ja exista formLocal, atualiza os dados do produto la tb.
                    //Se status diferente de SYS_STATUS_SCHEDULE, não atualiza nenhuma info,
                    //só substitui o obj vendo do server pelo do bd
                    if(dbSchedule.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_SCHEDULE)){
                        scheduleExec.setSync_process(1);
                        scheduleExec.setStatus(dbSchedule.getStatus());
                        //Se agendamento ja existe, pega os dados "realacionais" e atualiza no obj vindo do server.
                        //Seta infos gerais a todos agendamentos
                        //Até 15/05/2020, dados de site e oper NUNCA SE ALTERAM no agendamento.
                        scheduleExec.setSite_id(dbSchedule.getSite_id());
                        scheduleExec.setSite_desc(dbSchedule.getSite_desc());
                        scheduleExec.setOperation_id(dbSchedule.getOperation_id());
                        scheduleExec.setOperation_desc(dbSchedule.getOperation_desc());
                        //Seta informações especifica por tipo
                        String scheduleType = scheduleExec.getSchedule_type();
                        switch (scheduleType){
                            case ConstantBaseApp.MD_SCHEDULE_TYPE_FORM:
                                GE_Custom_Form_Local customFormLocal = getCustomFormLocal(
                                    scheduleExec.getCustomer_code(),
                                    scheduleExec.getSchedule_prefix(),
                                    scheduleExec.getSchedule_code(),
                                    scheduleExec.getSchedule_exec(),
                                    db
                                );
                                //Até 15/05/2020, os dados do form no agendamento NUNCA SE ALTERAM
                                scheduleExec.setCustom_form_desc(dbSchedule.getCustom_form_desc());
                                scheduleExec.setRequire_serial_done(dbSchedule.getRequire_serial_done());
                                scheduleExec.setRequire_location(dbSchedule.getRequire_location());
                                //LUCHE - 15/05/2020
                                //Atualiza dados do produto diretamente do produto enviado.
                                updateScheduleExecProductInfos(scheduleExecProductList, scheduleExec, dbSchedule);
                                //Caso o agendamento ja possua o form_local,atualiza dados do produto nele tb
                                //e adiciona form para ser atualizado.
                                if(scheduelFormLocalExists(customFormLocal)){
                                    updateCustomFormLocalProductsInfos(scheduleExec,customFormLocal);
                                    formLocalsToUpdate.add(customFormLocal);
                                }
                                break;
                            case ConstantBaseApp.MD_SCHEDULE_TYPE_TICKET:
                                //LUCHE - 15/05/2020
                                //Atualiza dados do produto diretamente do produto enviado.
                                updateScheduleExecProductInfos(scheduleExecProductList, scheduleExec, dbSchedule);
                            default:
                                break;
                        }
                        //LUCHE -12/05/2021 - Seta infos da tag no obj agendamento.
                        setTagInfos(scheduleExec);
                        //ATENÇÃO, COMO AGENDAMENTO NO STATUS SCHEDULE, O OBJETO VINDO DO SERVER É
                        //ATUALIZADO COM AS INFORMAÇÕES RELACIONAIS ENTÃO NÃO É NECESSARIO TROCAR O ITEM
                        //USADO NA LISTA COMO NO CASO DO ELSE ABAIXO.
                    }else{
                        //Se agendamento em status diferente do de agendado, usa dados vindo do banco
                        dbSchedule.setSync_process(1);
                        receivedScheduleExecs.set(i, dbSchedule);
                    }

                }else{
                    scheduleExec.setSync_process(1);
                    //Caso seja um novo agendamento, seta as informações no obj para ser inserido no banco.
                    scheduleExec.setStatus(ConstantBaseApp.SYS_STATUS_SCHEDULE);
                    if (scheduleExec.getSchedule_type() != null) {
                        String scheduleType = scheduleExec.getSchedule_type();
                        //
                        switch (scheduleType){
                            case ConstantBaseApp.MD_SCHEDULE_TYPE_FORM:
                                setFormsInfos(
                                    scheduleExec,
                                    scheduleExecSiteList,
                                    scheduleExecOperationList,
                                    scheduleExecProductList
                                );
                                break;
                            case ConstantBaseApp.MD_SCHEDULE_TYPE_TICKET:
                                setTicketInfos(
                                    scheduleExec,
                                    scheduleExecSiteList,
                                    scheduleExecOperationList,
                                    scheduleExecProductList
                                );
                                break;
                        }
                        setTagInfos(scheduleExec);
                    }
                }
            }
            //Atualiza/ Insere lista no banco
            daoObjReturn = addUpdate(receivedScheduleExecs, false,db);
            //Se erro ao inserir, dispara exception que por sua vez executa rollback
            if (daoObjReturn.hasError()) {
                throw new Exception(daoObjReturn.getRawMessage());
            }
            //Atualiza os FormLocal caso exista.
            if(formLocalsToUpdate != null && formLocalsToUpdate.size() > 0){
                daoObjReturn = getFormLocalDao().addUpdateThrowExceptionWithSharedDbInstance(formLocalsToUpdate, false, db);
                //Se erro ao atualizar, dispara exception que por sua vez executa rollback
                if (daoObjReturn.hasError()) {
                    throw new Exception(daoObjReturn.getRawMessage());
                }
            }
            //Se sucesso ao inserir  / atualizar , deleta agedamentos que não foram enviados.
            ArrayList<MD_Schedule_Exec> scheduleToDell = (ArrayList<MD_Schedule_Exec>)
                query(
                    new MD_Schedule_Exec_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context)
                    ).toSqlQuery()
                );
            //Se existem itens para delete, tenta o delete.
            if(scheduleToDell!= null && scheduleToDell.size() > 0){
                //Deleta agendamentos que não foram processados.
                daoObjReturn = delete(scheduleToDell, db);
                //
                //Se erro ao deletar, dispara exception que por sua vez executa rollback
                if (daoObjReturn.hasError()) {
                    throw new Exception(daoObjReturn.getRawMessage());
                }
            }
            //
            db.setTransactionSuccessful();
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
            db.endTransaction();
            //
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }
        //
        closeDB();
        //
        return daoObjReturn;
    }

    /**
     * LUCHE - 09/06/2021
     * Metodo que faz o delete dos agendamento não sincronizados.
     */
    public void deleteUnsyncSchedule() {
        DaoObjReturn daoObjReturn = new DaoObjReturn(TABLE);
        daoObjReturn.setAction(DaoObjReturn.DELETE);
        openDB();
        try {
            //Se sucesso ao inserir  / atualizar , deleta agedamentos que não foram enviados.
            ArrayList<MD_Schedule_Exec> scheduleToDell = (ArrayList<MD_Schedule_Exec>)
                query(
                    new MD_Schedule_Exec_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context)
                    ).toSqlQuery()
                );
            //Se existem itens para delete, tenta o delete.
            if (scheduleToDell != null && scheduleToDell.size() > 0) {
                //Deleta agendamentos que não foram processados.
                daoObjReturn = delete(scheduleToDell, db);
                //
                //Se erro ao deletar, dispara exception que por sua vez executa rollback
                if (daoObjReturn.hasError()) {
                    throw new Exception(daoObjReturn.getRawMessage());
                }
            }
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
        }finally {
            closeDB();
        }
    }

    /**
     * Luche - 10/05/2021
     * Metdo que resgata os dados relacionais da tag e seta no obj schedule
     * @param scheduleExec
     */
    private void setTagInfos(MD_Schedule_Exec scheduleExec) {
        MdTagDao tagDao = getMdTagDao();
        //
        if(tagDao != null) {
            MdTag tag = tagDao.getByString(
                new MdTagSql001(
                    (int) scheduleExec.getCustomer_code(),
                    scheduleExec.getTag_operational_code()
                ).toSqlQuery()
            );
            //
            if(tag != null){
                scheduleExec.setTag_operational_id(tag.getTag_id());
                scheduleExec.setTag_operational_desc(tag.getTag_desc());
            }
        }
    }

    private void updateScheduleExecProductInfos(ArrayList<MD_Schedule_Exec_Product> scheduleExecProductList, MD_Schedule_Exec scheduleExec, MD_Schedule_Exec dbSchedule) {
        MD_Schedule_Exec_Product execProductInfo = getExecProductInfo(scheduleExec.getCustomer_code(), scheduleExec.getProduct_code(), scheduleExecProductList);
        //
        if(execProductInfo != null) {
            scheduleExec.setProduct_id(execProductInfo.getProduct_id());
            scheduleExec.setProduct_desc(execProductInfo.getProduct_desc());
            scheduleExec.setRequire_serial(execProductInfo.getRequire_serial());
            scheduleExec.setAllow_new_serial_cl(execProductInfo.getAllow_new_serial_cl());
            scheduleExec.setSerial_rule(execProductInfo.getSerial_rule());
            scheduleExec.setSerial_max_length(execProductInfo.getSerial_max_length());
            scheduleExec.setSerial_min_length(execProductInfo.getSerial_min_length());
            scheduleExec.setLocal_control(execProductInfo.getLocal_control());
            scheduleExec.setIo_control(execProductInfo.getIo_control());
            scheduleExec.setSite_restriction(execProductInfo.getSite_restriction());
            scheduleExec.setProduct_icon_name(execProductInfo.getProduct_icon_name());
            scheduleExec.setProduct_icon_url(execProductInfo.getProduct_icon_url());
        }else{
            //LUCHE - 14/05/2020
            //Isso nunca deveria acontecer, mas como o nunca é o novo amanha, ta aqui.
            //Melhor parecer um bug que quebrar...
            scheduleExec.setProduct_id(dbSchedule.getProduct_id());
            scheduleExec.setProduct_desc(dbSchedule.getProduct_desc());
            scheduleExec.setRequire_serial(dbSchedule.getRequire_serial());
            scheduleExec.setAllow_new_serial_cl(dbSchedule.getAllow_new_serial_cl());
            scheduleExec.setSerial_rule(dbSchedule.getSerial_rule());
            scheduleExec.setSerial_max_length(dbSchedule.getSerial_max_length());
            scheduleExec.setSerial_min_length(dbSchedule.getSerial_min_length());
            scheduleExec.setLocal_control(dbSchedule.getLocal_control());
            scheduleExec.setIo_control(dbSchedule.getIo_control());
            scheduleExec.setSite_restriction(dbSchedule.getSite_restriction());
            scheduleExec.setProduct_icon_name(dbSchedule.getProduct_icon_name());
            scheduleExec.setProduct_icon_url(dbSchedule.getProduct_icon_url());
        }
    }

    private void updateCustomFormLocalProductsInfos(MD_Schedule_Exec scheduleExec, GE_Custom_Form_Local customFormLocal) {
        customFormLocal.setCustom_product_id(scheduleExec.getProduct_id());
        customFormLocal.setCustom_product_desc(scheduleExec.getProduct_desc());
        customFormLocal.setRequire_serial(scheduleExec.getRequire_serial());
        customFormLocal.setAllow_new_serial_cl(scheduleExec.getAllow_new_serial_cl());
        customFormLocal.setSerial_rule(scheduleExec.getSerial_rule());
        customFormLocal.setSerial_max_length(scheduleExec.getSerial_max_length());
        customFormLocal.setSerial_min_length(scheduleExec.getSerial_min_length());
        customFormLocal.setLocal_control(scheduleExec.getLocal_control());
        customFormLocal.setIo_control(scheduleExec.getIo_control());
        customFormLocal.setSite_restriction(scheduleExec.getSite_restriction());
        customFormLocal.setCustom_product_icon_name(scheduleExec.getProduct_icon_name());
        customFormLocal.setCustom_product_icon_url(scheduleExec.getProduct_icon_url());
    }

    /**
     * LUCHE - 11/03/2020
     * <p></p>
     * Metodo que seleciona os dados de master data do agendamento do tipo FORM
     * @param scheduleExec - Agendamento
     * @param scheduleExecSiteList - Lista de todos os site que são usado no agendamento. Dados relacionais
     * @param scheduleExecOperationList- Lista de todos as operations que são usado no agendamento. Dados relacionais
     * @param scheduleExecProductList- Lista de todos os produtos que são usado no agendamento. Dados relacionais
     */
    private void setFormsInfos(MD_Schedule_Exec scheduleExec, ArrayList<MD_Schedule_Exec_Site> scheduleExecSiteList, ArrayList<MD_Schedule_Exec_Operation> scheduleExecOperationList, ArrayList<MD_Schedule_Exec_Product> scheduleExecProductList) {
        //Tenta setar os dados do master data na tabela.
        MD_Schedule_Exec_Site execSite = getExecSiteInfo(scheduleExec.getCustomer_code(),scheduleExec.getSite_code(),scheduleExecSiteList);
        MD_Schedule_Exec_Operation execOperation = getExecOperationInfo(scheduleExec.getCustomer_code(),scheduleExec.getOperation_code(),scheduleExecOperationList);
        MD_Schedule_Exec_Product execProduct = getExecProductInfo(scheduleExec.getCustomer_code(),scheduleExec.getProduct_code(),scheduleExecProductList);
        //
        HMAux mdAux = getByStringHM(
            new MD_Schedule_Exec_Dao_Sql_001(
                scheduleExec.getCustomer_code(),
                scheduleExec.getCustom_form_type(),
                scheduleExec.getCustom_form_code(),
                scheduleExec.getCustom_form_version(),
                ToolBox_Con.getPreference_Translate_Code(context)
            ).toSqlQuery(), db
        );
        //Se existir tenta colocar infos do site
        if(execSite != null){
            scheduleExec.setSite_id(execSite.getSite_id());
            scheduleExec.setSite_desc(execSite.getSite_desc());
        }
        //Se existir tenta colocar infos da operacao
        if(execOperation != null){
            scheduleExec.setOperation_id(execOperation.getOperation_id());
            scheduleExec.setOperation_desc(execOperation.getOperation_desc());
        }
        //Se existir tenta colocar infos do produto
        if(execProduct != null){
            scheduleExec.setProduct_id(execProduct.getProduct_id());
            scheduleExec.setProduct_desc(execProduct.getProduct_desc());
            scheduleExec.setRequire_serial(execProduct.getRequire_serial());
            scheduleExec.setAllow_new_serial_cl(execProduct.getAllow_new_serial_cl());
            scheduleExec.setSerial_rule(execProduct.getSerial_rule());
            scheduleExec.setSerial_max_length(execProduct.getSerial_max_length());
            scheduleExec.setSerial_min_length(execProduct.getSerial_min_length());
            scheduleExec.setLocal_control(execProduct.getLocal_control());
            scheduleExec.setIo_control(execProduct.getIo_control());
            scheduleExec.setSite_restriction(execProduct.getSite_restriction());
            scheduleExec.setProduct_icon_name(execProduct.getProduct_icon_name());
            scheduleExec.setProduct_icon_url(execProduct.getProduct_icon_url());
        }
        //Se existir tenta colocar infos do form
        if (mdAux != null && mdAux.size() > 0) {
            scheduleExec.setRequire_serial_done(ToolBox_Inf.convertStringToInt(mdAux.get(REQUIRE_SERIAL_DONE)));
            scheduleExec.setRequire_location(ToolBox_Inf.convertStringToInt(mdAux.get(REQUIRE_LOCATION)));
            scheduleExec.setCustom_form_desc(mdAux.get(CUSTOM_FORM_DESC));
        }
    }

    private MD_Schedule_Exec_Product getExecProductInfo(long customer_code, int product_code, ArrayList<MD_Schedule_Exec_Product> scheduleExecProductList) {
        for (MD_Schedule_Exec_Product scheduleExecProduct : scheduleExecProductList) {
            if( customer_code == scheduleExecProduct.getCustomer_code()
                && product_code ==  scheduleExecProduct.getProduct_code()
            ){
                return scheduleExecProduct;
            }
        }
        //JAMAIS DEVERIA ACONTECER......
        return null;
    }

    private MD_Schedule_Exec_Operation getExecOperationInfo(long customer_code, int operation_code, ArrayList<MD_Schedule_Exec_Operation> scheduleExecOperationList) {
        for (MD_Schedule_Exec_Operation scheduleExecOperation : scheduleExecOperationList) {
            if( customer_code == scheduleExecOperation.getCustomer_code()
                && operation_code ==  scheduleExecOperation.getOperation_code()
            ){
                return scheduleExecOperation;
            }
        }
        //JAMAIS DEVERIA ACONTECER......
        return null;
    }

    @Nullable
    private MD_Schedule_Exec_Site getExecSiteInfo(long customer_code, int site_code, ArrayList<MD_Schedule_Exec_Site> scheduleExecSiteList) {
        for (MD_Schedule_Exec_Site scheduleExecSite : scheduleExecSiteList) {
            if( customer_code == scheduleExecSite.getCustomer_code()
                && site_code ==  scheduleExecSite.getSite_code()
            ){
                return scheduleExecSite;
            }
        }
        //JAMAIS DEVERIA ACONTECER......
        return null;
    }

    /**
     * LUCHE - 11/03/2020
     * Metodo que seleciona os dados de master data do agendamento do tipo TICKET
     * @param scheduleExec - Agendamento
     * @param scheduleExecSiteList - Lista de todos os site que são usado no agendamento. Dados relacionais
     * @param scheduleExecOperationList- Lista de todos as operations que são usado no agendamento. Dados relacionais
     * @param scheduleExecProductList- Lista de todos os produtos que são usado no agendamento. Dados relacionais
     */
    private void setTicketInfos(MD_Schedule_Exec scheduleExec, ArrayList<MD_Schedule_Exec_Site> scheduleExecSiteList, ArrayList<MD_Schedule_Exec_Operation> scheduleExecOperationList, ArrayList<MD_Schedule_Exec_Product> scheduleExecProductList) {
        //Tenta setar os dados do master data na tabela.
        MD_Schedule_Exec_Site execSite = getExecSiteInfo(scheduleExec.getCustomer_code(),scheduleExec.getSite_code(),scheduleExecSiteList);
        MD_Schedule_Exec_Operation execOperation = getExecOperationInfo(scheduleExec.getCustomer_code(),scheduleExec.getOperation_code(),scheduleExecOperationList);
        MD_Schedule_Exec_Product execProduct = getExecProductInfo(scheduleExec.getCustomer_code(),scheduleExec.getProduct_code(),scheduleExecProductList);
        //
        //Tenta setar os dados do master data na tabela.a
/*
        METODOLOGIA ORIGINAL SUBSTITUIDA EM 13/04/2020  PARA USAR AS TABELAS RELACIONAIS TMP
//        HMAux mdAux = getByStringHM(
//            new MD_Schedule_Exec_Dao_Sql_002(
//                scheduleExec.getCustomer_code(),
//                scheduleExec.getSite_code(),
//                scheduleExec.getOperation_code(),
//                scheduleExec.getProduct_code()
//            ).toSqlQuery(), db
//        );
//        //
//        if (mdAux != null && mdAux.size() > 0) {
//            scheduleExec.setSite_id(mdAux.get(SITE_ID));
//            scheduleExec.setSite_desc(mdAux.get(SITE_DESC));
//            scheduleExec.setOperation_id(mdAux.get(OPERATION_ID));
//            scheduleExec.setOperation_desc(mdAux.get(OPERATION_DESC));
//            scheduleExec.setProduct_id(mdAux.get(PRODUCT_ID));
//            scheduleExec.setProduct_desc(mdAux.get(PRODUCT_DESC));
//        }
*/
        //Se existir tenta colocar infos do site
        if(execSite != null){
            scheduleExec.setSite_id(execSite.getSite_id());
            scheduleExec.setSite_desc(execSite.getSite_desc());
        }
        //Se existir tenta colocar infos da operacao
        if(execOperation != null){
            scheduleExec.setOperation_id(execOperation.getOperation_id());
            scheduleExec.setOperation_desc(execOperation.getOperation_desc());
        }
        //Se existir tenta colocar infos do produto
        if(execProduct != null){
            scheduleExec.setProduct_id(execProduct.getProduct_id());
            scheduleExec.setProduct_desc(execProduct.getProduct_desc());
        }
    }

    /**
     * LUCHE - 03/03/2020
     *
     * Metodo que verifica se já existe form_local para o agendamento.
     * Mesmo que o agendamento estaja no status agendado, se o serial não estiver definido, é possivel
     * que durante a "coleta do serial" o usuario tente abortar o processo e como o status só é alterado
     * quando o usr acessa o form, temos essa situação.
     *
     * @param customer_code
     * @param schedule_prefix
     * @param schedule_code
     * @param schedule_exec
     * @param db
     * @return - Verdadeiro se o form_local ja existir
     */
    private boolean scheduelFormLocalExists(long customer_code, int schedule_prefix, int schedule_code, int schedule_exec, SQLiteDatabase db) {
//        HMAux mdAux = getByStringHM(
//            new MD_Schedule_Exec_Sql_006(
//                String.valueOf(customer_code),
//                String.valueOf(schedule_prefix),
//                String.valueOf(schedule_code),
//                String.valueOf(schedule_exec)
//            ).toSqlQuery(), db
//        );
//        //
//        if (mdAux != null && mdAux.size() > 0
//            && mdAux.hasConsistentValue(SCHEDULE_PREFIX)
//            && mdAux.hasConsistentValue(SCHEDULE_CODE)
//            && mdAux.hasConsistentValue(SCHEDULE_EXEC)
//        ) {
//            return true;
//        }
//        return false;
        GE_Custom_Form_Local formLocal = getCustomFormLocal( customer_code,
            schedule_prefix,
            schedule_code,
            schedule_exec,
            db
        );
        //
        if(formLocal != null && schedule_prefix > 0 && schedule_code > 0 && schedule_exec > 0 ){
            return true;
        }
        return false;
    }

    /**
     * LUCHE - 15/05/2020
     *
     * Metodo que verifica se o GE_Custom_Form_Local carregado possui agendamentos.
     * @param customFormLocal - Custom form retornado pela query. Pode ser null, por isso existe
     * esse metodo
     * @return True se GE_Custom_Form_Local != null e valores inteiros maiores que 0 ou seja, preenchidos.
     */
    private boolean scheduelFormLocalExists(GE_Custom_Form_Local customFormLocal) {
       return
           customFormLocal != null
               && customFormLocal.getSchedule_prefix() != null
               && customFormLocal.getSchedule_prefix() > 0
               && customFormLocal.getSchedule_code() != null
               && customFormLocal.getSchedule_code() > 0
               && customFormLocal.getSchedule_exec() != null
               && customFormLocal.getSchedule_exec() > 0
           ;
    }

    /**
     * LUCHE - 15/05/2020
     *
     * Metodo que busca tenta buscar o GE_Custom_Form_Local relacionado ao agendamento.
     * @param customer_code
     * @param schedule_prefix
     * @param schedule_code
     * @param schedule_exec
     * @param dbInstance
     * @return Retorno o GE_Custom_Form_Local encontrado ou null caso não encontre
     */
    private GE_Custom_Form_Local getCustomFormLocal(long customer_code, int schedule_prefix, int schedule_code, int schedule_exec, SQLiteDatabase dbInstance){
        GE_Custom_Form_LocalDao formLocalDao = getFormLocalDao();
        return formLocalDao.getByStringSharedDbInstance(
            new MD_Schedule_Exec_Sql_008(
                customer_code,
                schedule_prefix,
                schedule_code,
                schedule_exec
            ).toSqlQuery(),dbInstance
        );
    }

    @NonNull
    /**
     * LUCHE - 15/05/2020
     * Metodo que gera instancia do GE_Custom_Form_LocalDao
     */
    private GE_Custom_Form_LocalDao getFormLocalDao() {
        return new GE_Custom_Form_LocalDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
    }

    @NonNull
    /**
     * LUCHE - 10/05/2021
     * Metodo que gera instancia do MdTagDao
     */
    private MdTagDao getMdTagDao() {
        return new MdTagDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
    }

    /**
     * LUCHE - 27/03/2020
     * <p></p>
     * Metodo que deleta os agendamentos passados.
     * @param md_schedule_execs Lista de agendamento
     * @return DaoObj
     */
    public DaoObjReturn remove(ArrayList<MD_Schedule_Exec> md_schedule_execs){
        return delete(md_schedule_execs,null);
    }

    private DaoObjReturn delete(ArrayList<MD_Schedule_Exec> md_schedule_execs, SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.DELETE;
        //
        if(dbInstance == null){
            openDB();
        }else{
            this.db = dbInstance;
        }
        try {
            //Se db não foi passado, inicializa transaction
            if (dbInstance == null) {
                db.beginTransaction();
            }
            //
            for (MD_Schedule_Exec md_schedule_exec : md_schedule_execs) {
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(md_schedule_exec.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SCHEDULE_PREFIX).append(" = '").append(md_schedule_exec.getSchedule_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SCHEDULE_CODE).append(" = '").append(md_schedule_exec.getSchedule_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SCHEDULE_EXEC).append(" = '").append(md_schedule_exec.getSchedule_exec()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = this.db.delete(TABLE, sbWhere.toString(), null);
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
            //
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

    private class CursorToMD_Schedule_ExecMapper implements Mapper<Cursor, MD_Schedule_Exec> {
        @Override
        public MD_Schedule_Exec map(Cursor cursor) {
            MD_Schedule_Exec md_schedule_exec = new MD_Schedule_Exec();
            //
            md_schedule_exec.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            md_schedule_exec.setSchedule_prefix(cursor.getInt(cursor.getColumnIndex(SCHEDULE_PREFIX)));
            md_schedule_exec.setSchedule_code(cursor.getInt(cursor.getColumnIndex(SCHEDULE_CODE)));
            md_schedule_exec.setSchedule_exec(cursor.getInt(cursor.getColumnIndex(SCHEDULE_EXEC)));
            md_schedule_exec.setSchedule_desc(cursor.getString(cursor.getColumnIndex(SCHEDULE_DESC)));
            md_schedule_exec.setSchedule_type(cursor.getString(cursor.getColumnIndex(SCHEDULE_TYPE)));
            md_schedule_exec.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            md_schedule_exec.setTag_operational_code(cursor.getInt(cursor.getColumnIndex(TAG_OPERATIONAL_CODE)));
            if(cursor.isNull(cursor.getColumnIndex(TAG_OPERATIONAL_ID))) {
                md_schedule_exec.setTag_operational_id(null);
            }else{
                md_schedule_exec.setTag_operational_id(cursor.getString(cursor.getColumnIndex(TAG_OPERATIONAL_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TAG_OPERATIONAL_DESC))) {
                md_schedule_exec.setTag_operational_desc(null);
            }else{
                md_schedule_exec.setTag_operational_desc(cursor.getString(cursor.getColumnIndex(TAG_OPERATIONAL_DESC)));
            }
            md_schedule_exec.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            if(cursor.isNull(cursor.getColumnIndex(SITE_ID))){
                md_schedule_exec.setSite_id(null);
            }else {
                md_schedule_exec.setSite_id(cursor.getString(cursor.getColumnIndex(SITE_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SITE_DESC))){
                md_schedule_exec.setSite_desc(null);
            }else {
                md_schedule_exec.setSite_desc(cursor.getString(cursor.getColumnIndex(SITE_DESC)));
            }
            md_schedule_exec.setOperation_code(cursor.getInt(cursor.getColumnIndex(OPERATION_CODE)));
            if(cursor.isNull(cursor.getColumnIndex(OPERATION_ID))){
                md_schedule_exec.setOperation_id(null);
            }else {
                md_schedule_exec.setOperation_id(cursor.getString(cursor.getColumnIndex(OPERATION_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(OPERATION_DESC))){
                md_schedule_exec.setOperation_desc(null);
            }else {
                md_schedule_exec.setOperation_desc(cursor.getString(cursor.getColumnIndex(OPERATION_DESC)));
            }
            md_schedule_exec.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
            if(cursor.isNull(cursor.getColumnIndex(PRODUCT_ID))){
                md_schedule_exec.setProduct_id(null);
            }else {
                md_schedule_exec.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PRODUCT_DESC))){
                md_schedule_exec.setProduct_desc(null);
            }else {
                md_schedule_exec.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_CODE))){
                md_schedule_exec.setSerial_code(null);
            }else{
                md_schedule_exec.setSerial_code(cursor.getInt(cursor.getColumnIndex(SERIAL_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SERIAL_ID))){
                md_schedule_exec.setSerial_id(null);
            }else{
                md_schedule_exec.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_TYPE))){
                md_schedule_exec.setCustom_form_type(null);
            }else{
                md_schedule_exec.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_CODE))){
                md_schedule_exec.setCustom_form_code(null);
            }else{
                md_schedule_exec.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_VERSION))){
                md_schedule_exec.setCustom_form_version(null);
            }else{
                md_schedule_exec.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_DESC))){
                md_schedule_exec.setCustom_form_desc(null);
            }else{
                md_schedule_exec.setCustom_form_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_DESC)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TICKET_TYPE))){
                md_schedule_exec.setTicket_type(null);
            }else{
                md_schedule_exec.setTicket_type(cursor.getInt(cursor.getColumnIndex(TICKET_TYPE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TICKET_TYPE_ID))){
                md_schedule_exec.setTicket_type_id(null);
            }else{
                md_schedule_exec.setTicket_type_id(cursor.getString(cursor.getColumnIndex(TICKET_TYPE_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TICKET_TYPE_DESC))){
                md_schedule_exec.setTicket_type_desc(null);
            }else{
                md_schedule_exec.setTicket_type_desc(cursor.getString(cursor.getColumnIndex(TICKET_TYPE_DESC)));
            }

            //region new fields
            md_schedule_exec.setLocal_control(cursor.getInt(cursor.getColumnIndex(LOCAL_CONTROL)));
            md_schedule_exec.setIo_control(cursor.getInt(cursor.getColumnIndex(IO_CONTROL)));

            if(cursor.isNull(cursor.getColumnIndex(SERIAL_RULE))){
                md_schedule_exec.setSerial_rule(null);
            }else{
                md_schedule_exec.setSerial_rule(cursor.getString(cursor.getColumnIndex(SERIAL_RULE)));
            }

            if(cursor.isNull(cursor.getColumnIndex(SERIAL_MIN_LENGTH))){
                md_schedule_exec.setSerial_min_length(null);
            }else{
                md_schedule_exec.setSerial_min_length(cursor.getInt(cursor.getColumnIndex(SERIAL_MIN_LENGTH)));
            }

            if(cursor.isNull(cursor.getColumnIndex(SERIAL_MAX_LENGTH))){
                md_schedule_exec.setSerial_max_length(null);
            }else{
                md_schedule_exec.setSerial_max_length(cursor.getInt(cursor.getColumnIndex(SERIAL_MAX_LENGTH)));
            }

            md_schedule_exec.setSite_restriction(cursor.getInt(cursor.getColumnIndex(SITE_RESTRICTION)));


            if(cursor.isNull(cursor.getColumnIndex(PRODUCT_ICON_NAME))){
                md_schedule_exec.setProduct_icon_name(null);
            }else{
                md_schedule_exec.setProduct_icon_name(cursor.getString(cursor.getColumnIndex(PRODUCT_ICON_NAME)));
            }

            if(cursor.isNull(cursor.getColumnIndex(PRODUCT_ICON_URL ))){
                md_schedule_exec.setProduct_icon_url(null);
            }else{
                md_schedule_exec.setProduct_icon_url(cursor.getString(cursor.getColumnIndex(PRODUCT_ICON_URL)));
            }

            md_schedule_exec.setProduct_icon_url_local(cursor.getString(cursor.getColumnIndex(PRODUCT_ICON_URL_LOCAL)));
            md_schedule_exec.setRequire_location(cursor.getInt(cursor.getColumnIndex(REQUIRE_LOCATION)));

            //endregion

            md_schedule_exec.setDate_start(cursor.getString(cursor.getColumnIndex(DATE_START)));
            md_schedule_exec.setDate_end(cursor.getString(cursor.getColumnIndex(DATE_END)));
            if(cursor.isNull(cursor.getColumnIndex(COMMENTS))){
                md_schedule_exec.setComments(null);
            }else{
                md_schedule_exec.setComments(cursor.getString(cursor.getColumnIndex(COMMENTS)));
            }
            md_schedule_exec.setRequire_serial(cursor.getInt(cursor.getColumnIndex(REQUIRE_SERIAL)));
            md_schedule_exec.setAllow_new_serial_cl(cursor.getInt(cursor.getColumnIndex(ALLOW_NEW_SERIAL_CL)));
            md_schedule_exec.setRequire_serial_done(cursor.getInt(cursor.getColumnIndex(REQUIRE_SERIAL_DONE)));
            md_schedule_exec.setSync_process(cursor.getInt(cursor.getColumnIndex(SYNC_PROCESS)));
            if(cursor.isNull(cursor.getColumnIndex(FCM_NEW_STATUS))){
                md_schedule_exec.setFcm_new_status(null);
            }else{
                md_schedule_exec.setFcm_new_status(cursor.getString(cursor.getColumnIndex(FCM_NEW_STATUS)));
            }
            if(cursor.isNull(cursor.getColumnIndex(FCM_USER_NICK))){
                md_schedule_exec.setFcm_user_nick(null);
            }else{
                md_schedule_exec.setFcm_user_nick(cursor.getString(cursor.getColumnIndex(FCM_USER_NICK)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SCHEDULE_ERRO_MSG))){
                md_schedule_exec.setSchedule_erro_msg(null);
            }else{
                md_schedule_exec.setSchedule_erro_msg(cursor.getString(cursor.getColumnIndex(SCHEDULE_ERRO_MSG)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CLOSE_DATE))){
                md_schedule_exec.setClose_date(null);
            }else{
                md_schedule_exec.setClose_date(cursor.getString(cursor.getColumnIndex(CLOSE_DATE)));
            }
            //LUCHE - 28/05/2021 - Só vem em uma query da lista de actions done NÃO SALVA NO BANCO
            if(cursor.isNull(cursor.getColumnIndex(HAS_NC))){
                md_schedule_exec.setHas_Nc(null);
            }else{
                md_schedule_exec.setHas_Nc(cursor.getInt(cursor.getColumnIndex(HAS_NC)));
            }
            //
            return md_schedule_exec;
        }
    }

    private class MD_Schedule_ExecToContentValuesMapper implements Mapper<MD_Schedule_Exec, ContentValues> {
        @Override
        public ContentValues map(MD_Schedule_Exec md_schedule_exec) {
            ContentValues contentValues = new ContentValues();
            //
            if(md_schedule_exec.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,md_schedule_exec.getCustomer_code());
            }
            if(md_schedule_exec.getSchedule_prefix() > -1){
                contentValues.put(SCHEDULE_PREFIX,md_schedule_exec.getSchedule_prefix());
            }
            if(md_schedule_exec.getSchedule_code() > -1){
                contentValues.put(SCHEDULE_CODE,md_schedule_exec.getSchedule_code());
            }
            if(md_schedule_exec.getSchedule_exec() > -1){
                contentValues.put(SCHEDULE_EXEC,md_schedule_exec.getSchedule_exec());
            }
            if(md_schedule_exec.getSchedule_desc() != null){
                contentValues.put(SCHEDULE_DESC,md_schedule_exec.getSchedule_desc());
            }
            if(md_schedule_exec.getSchedule_type() != null){
                contentValues.put(SCHEDULE_TYPE,md_schedule_exec.getSchedule_type());
            }
            if(md_schedule_exec.getStatus() != null){
                contentValues.put(STATUS,md_schedule_exec.getStatus().toUpperCase());
            }
            if(md_schedule_exec.getTag_operational_code() > -1){
                contentValues.put(TAG_OPERATIONAL_CODE,md_schedule_exec.getTag_operational_code());
            }
            contentValues.put(TAG_OPERATIONAL_ID,md_schedule_exec.getTag_operational_id());
            contentValues.put(TAG_OPERATIONAL_DESC,md_schedule_exec.getTag_operational_desc());
            if(md_schedule_exec.getSite_code() > -1){
                contentValues.put(SITE_CODE,md_schedule_exec.getSite_code());
            }
            contentValues.put(SITE_ID,md_schedule_exec.getSite_id());
            contentValues.put(SITE_DESC,md_schedule_exec.getSite_desc());
            if(md_schedule_exec.getOperation_code() > -1){
                contentValues.put(OPERATION_CODE,md_schedule_exec.getOperation_code());
            }
            contentValues.put(OPERATION_ID,md_schedule_exec.getOperation_id());
            contentValues.put(OPERATION_DESC,md_schedule_exec.getOperation_desc());
            if(md_schedule_exec.getProduct_code() > -1){
                contentValues.put(PRODUCT_CODE,md_schedule_exec.getProduct_code());
            }
            contentValues.put(PRODUCT_ID,md_schedule_exec.getProduct_id());
            contentValues.put(PRODUCT_DESC,md_schedule_exec.getProduct_desc());
            contentValues.put(SERIAL_CODE,md_schedule_exec.getSerial_code());
            contentValues.put(SERIAL_ID,md_schedule_exec.getSerial_id());
            contentValues.put(CUSTOM_FORM_TYPE,md_schedule_exec.getCustom_form_type());
            contentValues.put(CUSTOM_FORM_CODE,md_schedule_exec.getCustom_form_code());
            contentValues.put(CUSTOM_FORM_VERSION,md_schedule_exec.getCustom_form_version());
            contentValues.put(CUSTOM_FORM_DESC,md_schedule_exec.getCustom_form_desc());
            contentValues.put(TICKET_TYPE,md_schedule_exec.getTicket_type());
            contentValues.put(TICKET_TYPE_ID,md_schedule_exec.getTicket_type_id());
            contentValues.put(TICKET_TYPE_DESC,md_schedule_exec.getTicket_type_desc());
            if(md_schedule_exec.getDate_start() != null){
                contentValues.put(DATE_START,md_schedule_exec.getDate_start());
            }
            if(md_schedule_exec.getDate_end() != null){
                contentValues.put(DATE_END,md_schedule_exec.getDate_end());
            }
            //region new fields

            if(md_schedule_exec.getLocal_control() > -1){
                contentValues.put(LOCAL_CONTROL,md_schedule_exec.getLocal_control());
            }

            if(md_schedule_exec.getIo_control() > -1){
                contentValues.put(IO_CONTROL,md_schedule_exec.getIo_control());
            }

            contentValues.put(SERIAL_RULE,md_schedule_exec.getSerial_rule());
            contentValues.put(SERIAL_MIN_LENGTH,md_schedule_exec.getSerial_min_length());
            contentValues.put(SERIAL_MAX_LENGTH,md_schedule_exec.getSerial_max_length());

            if(md_schedule_exec.getSite_restriction() > -1){
                contentValues.put(SITE_RESTRICTION ,md_schedule_exec.getSite_restriction());
            }

            contentValues.put(PRODUCT_ICON_NAME,md_schedule_exec.getProduct_icon_name());
            contentValues.put(PRODUCT_ICON_URL ,md_schedule_exec.getProduct_icon_url());

            if(md_schedule_exec.getProduct_icon_url_local() != null){
                contentValues.put(PRODUCT_ICON_URL_LOCAL,md_schedule_exec.getProduct_icon_url_local());
            }

            if(md_schedule_exec.getRequire_location() > -1){
                contentValues.put(REQUIRE_LOCATION ,md_schedule_exec.getRequire_location());
            }
            //endregion

            contentValues.put(COMMENTS,md_schedule_exec.getComments());
            if(md_schedule_exec.getRequire_serial() > -1){
                contentValues.put(REQUIRE_SERIAL,md_schedule_exec.getRequire_serial());
            }
            if(md_schedule_exec.getAllow_new_serial_cl() > -1){
                contentValues.put(ALLOW_NEW_SERIAL_CL,md_schedule_exec.getAllow_new_serial_cl());
            }
            if(md_schedule_exec.getRequire_serial_done() > -1){
                contentValues.put(REQUIRE_SERIAL_DONE,md_schedule_exec.getRequire_serial_done());
            }
            if(md_schedule_exec.getSync_process() > -1){
                contentValues.put(SYNC_PROCESS,md_schedule_exec.getSync_process());
            }
            contentValues.put(FCM_NEW_STATUS,md_schedule_exec.getFcm_new_status());
            contentValues.put(FCM_USER_NICK,md_schedule_exec.getFcm_user_nick());
            contentValues.put(SCHEDULE_ERRO_MSG,md_schedule_exec.getSchedule_erro_msg());
            contentValues.put(CLOSE_DATE,md_schedule_exec.getClose_date());
            //
            return contentValues;
        }
    }
}
