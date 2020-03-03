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
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_003;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_005;
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
    public static final String CUSTOM_FORM_TYPE_DESC = "custom_form_type_desc";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION  = "custom_form_version";
    public static final String CUSTOM_FORM_DESC = "custom_form_desc";
    public static final String TICKET_TYPE = "ticket_type";
    public static final String TICKET_TYPE_ID = "ticket_type_id";
    public static final String TICKET_TYPE_DESC = "ticket_type_desc";
    public static final String DATE_START = "date_start";
    public static final String DATE_END  = "date_end";
    public static final String COMMENTS  = "comments";
    public static final String STATUS  = "status";
    public static final String SYNC_PROCESS  = "sync_process";
    public static final String REQUIRE_SERIAL = "require_serial";
    public static final String ALLOW_NEW_SERIAL_CL = "allow_new_serial_cl";
    public static final String REQUIRE_SERIAL_DONE = "require_serial_done";
    //NÃO SÃO CAMPOS DA TABELA, mas são usados em queries
    public static final String SCHEDULE_DATE_START_FORMAT = "schedule_date_start_format";
    public static final String SCHEDULE_DATE_END_FORMAT = "schedule_date_end_format";

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

    /**
     * LUCHE - 11/02/2020
     *
     * Metodo responsavel pela conciliação dos agendamentos durante o sincronismo.
     *
     *  - Inclui novos agendamentos
     *  - Atualiza agendamentos ja existentes e que NÃO foram iniciados ou executados
     *  - Exclui agendamentos que NÃO foram recebidos no sincronismo e que NÃO foram iniciados ou executados
     * @param receivedScheduleExecs - Agendamentos recebidos no sincronismo.
     * @return
     */
    public DaoObjReturn processConciliation(ArrayList<MD_Schedule_Exec> receivedScheduleExecs) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.DELETE;
        //
        openDB();
        //
        try {
            db.beginTransaction();
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
                //Se existir o agendamento e ele ja tiver sido iniciado, seta sync_process para 1 e
                // substitui o agendamento do server pelo do banco de dados, evitando a substituição.
                if( dbSchedule != null
                    && !dbSchedule.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_SCHEDULE)
                ){
                    dbSchedule.setSync_process(1);
                    receivedScheduleExecs.set(i,dbSchedule);
                }else{
                    //Se agendamento não existia ou existia com status pending, seta sync_process e
                    //mantem o agendamento do server para atualização.
                    scheduleExec.setSync_process(1);
                    //Se não existe no banco de dados, ou seja insert, seta status e dados do MD
                    if(dbSchedule == null) {
                        scheduleExec.setStatus(ConstantBaseApp.SYS_STATUS_SCHEDULE);
                        if (scheduleExec.getSchedule_type() != null
                            && scheduleExec.getSchedule_type().equalsIgnoreCase(ConstantBaseApp.MD_SCHEDULE_TYPE_FORM)
                        ) {
                            //Tenta setar os dados do master data na tabela.
                            HMAux mdAux = getByStringHM(
                                new MD_Schedule_Exec_Sql_005(
                                    scheduleExec.getCustomer_code(),
                                    scheduleExec.getSite_code(),
                                    scheduleExec.getOperation_code(),
                                    scheduleExec.getProduct_code(),
                                    scheduleExec.getCustom_form_type(),
                                    scheduleExec.getCustom_form_code(),
                                    scheduleExec.getCustom_form_version()
                                ).toSqlQuery(), db
                            );
                            //
                            if (mdAux != null && mdAux.size() > 0) {
                                scheduleExec.setSite_id(mdAux.get(SITE_ID));
                                scheduleExec.setSite_desc(mdAux.get(SITE_DESC));
                                scheduleExec.setOperation_id(mdAux.get(OPERATION_ID));
                                scheduleExec.setOperation_desc(mdAux.get(OPERATION_DESC));
                                scheduleExec.setProduct_id(mdAux.get(PRODUCT_ID));
                                scheduleExec.setProduct_desc(mdAux.get(PRODUCT_DESC));
                                scheduleExec.setRequire_serial(ToolBox_Inf.convertStringToInt(mdAux.get(REQUIRE_SERIAL)));
                                scheduleExec.setAllow_new_serial_cl(ToolBox_Inf.convertStringToInt(mdAux.get(ALLOW_NEW_SERIAL_CL)));
                                scheduleExec.setRequire_serial_done(ToolBox_Inf.convertStringToInt(mdAux.get(REQUIRE_SERIAL_DONE)));
                                scheduleExec.setCustom_form_type_desc(mdAux.get(CUSTOM_FORM_TYPE_DESC));
                                scheduleExec.setCustom_form_desc(mdAux.get(CUSTOM_FORM_DESC));
                            }
                        }
                    } else{
                        //Se agendamento ja existe, pega os dados "realacionais" e atualiza no obj vindo do server.
                        scheduleExec.setSite_id(dbSchedule.getSite_id());
                        scheduleExec.setSite_desc(dbSchedule.getSite_desc());
                        scheduleExec.setOperation_id(dbSchedule.getOperation_id());
                        scheduleExec.setOperation_desc(dbSchedule.getOperation_desc());
                        scheduleExec.setProduct_id(dbSchedule.getProduct_id());
                        scheduleExec.setProduct_desc(dbSchedule.getProduct_desc());
                        scheduleExec.setRequire_serial(dbSchedule.getRequire_serial());
                        scheduleExec.setAllow_new_serial_cl(dbSchedule.getAllow_new_serial_cl());
                        scheduleExec.setRequire_serial_done(dbSchedule.getRequire_serial_done());
                        scheduleExec.setCustom_form_type_desc(dbSchedule.getCustom_form_type_desc());
                        scheduleExec.setCustom_form_desc(dbSchedule.getCustom_form_desc());
                    }
                    //
                    //receivedScheduleExecs.set(i,scheduleExec);
                }
            }
            //Atualiza/ Insere lista no banco
            daoObjReturn = addUpdate(receivedScheduleExecs, false,db);
            //Se erro ao inserir, dispara exception que por sua vez executa rollback
            if (daoObjReturn.hasError()) {
                throw new Exception(daoObjReturn.getRawMessage());
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
            if(cursor.isNull(cursor.getColumnIndex(CUSTOM_FORM_TYPE_DESC))){
                md_schedule_exec.setCustom_form_type_desc(null);
            }else{
                md_schedule_exec.setCustom_form_type_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_TYPE_DESC)));
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
            contentValues.put(CUSTOM_FORM_TYPE_DESC,md_schedule_exec.getCustom_form_type_desc());
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
            //
            return contentValues;
        }
    }
}
