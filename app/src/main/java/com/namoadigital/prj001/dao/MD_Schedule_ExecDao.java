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
    public static final String SITE_CODE = "site_code";
    public static final String OPERATION_CODE  = "operation_code";
    public static final String PRODUCT_CODE  = "product_code";
    public static final String SERIAL_CODE   = "serial_code";
    public static final String SERIAL_ID = "serial_id";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION  = "custom_form_version";
    public static final String DATE_START = "date_start";
    public static final String DATE_END  = "date_end";
    public static final String COMMENTS  = "comments";
    public static final String STATUS  = "status";
    public static final String SYNC_PROCESS  = "sync_process";

    public MD_Schedule_ExecDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        toContentValuesMapper = new MD_Schedule_ExecToContentValuesMapper();
        toMD_Schedule_ExecMapper = new CursorToMD_Schedule_ExecMapper();
    }

    @Override
    public DaoObjReturn addUpdate(MD_Schedule_Exec md_schedule_exec) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
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
                    && !dbSchedule.getStatus().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_PENDING)
                ){
                    dbSchedule.setSync_process(1);
                    receivedScheduleExecs.set(i,dbSchedule);
                }else{
                    //Se agendamento não existia ou existia com status pending, seta sync_process e
                    //mantem o agendamento do server para atualização.
                    scheduleExec.setSync_process(1);
                    receivedScheduleExecs.set(i,scheduleExec);
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
            md_schedule_exec.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            md_schedule_exec.setOperation_code(cursor.getInt(cursor.getColumnIndex(OPERATION_CODE)));
            md_schedule_exec.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
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
            md_schedule_exec.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            md_schedule_exec.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            md_schedule_exec.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            md_schedule_exec.setDate_start(cursor.getString(cursor.getColumnIndex(DATE_START)));
            md_schedule_exec.setDate_end(cursor.getString(cursor.getColumnIndex(DATE_END)));
            if(cursor.isNull(cursor.getColumnIndex(COMMENTS))){
                md_schedule_exec.setComments(null);
            }else{
                md_schedule_exec.setComments(cursor.getString(cursor.getColumnIndex(COMMENTS)));
            }
            md_schedule_exec.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
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
            if(md_schedule_exec.getSite_code() > -1){
                contentValues.put(SITE_CODE,md_schedule_exec.getSite_code());
            }
            if(md_schedule_exec.getOperation_code() > -1){
                contentValues.put(OPERATION_CODE,md_schedule_exec.getOperation_code());
            }
            if(md_schedule_exec.getProduct_code() > -1){
                contentValues.put(PRODUCT_CODE,md_schedule_exec.getProduct_code());
            }
            contentValues.put(SERIAL_CODE,md_schedule_exec.getSerial_code());
            contentValues.put(SERIAL_ID,md_schedule_exec.getSerial_id());
            if(md_schedule_exec.getCustom_form_type() > -1){
                contentValues.put(CUSTOM_FORM_TYPE,md_schedule_exec.getCustom_form_type());
            }
            if(md_schedule_exec.getCustom_form_code() > -1){
                contentValues.put(CUSTOM_FORM_CODE,md_schedule_exec.getCustom_form_code());
            }
            if(md_schedule_exec.getCustom_form_version() > -1){
                contentValues.put(CUSTOM_FORM_VERSION,md_schedule_exec.getCustom_form_version());
            }
            if(md_schedule_exec.getDate_start() != null){
                contentValues.put(DATE_START,md_schedule_exec.getDate_start());
            }
            if(md_schedule_exec.getDate_end() != null){
                contentValues.put(DATE_END,md_schedule_exec.getDate_end());
            }
            contentValues.put(COMMENTS,md_schedule_exec.getComments());
            if(md_schedule_exec.getStatus() != null){
                contentValues.put(STATUS,md_schedule_exec.getStatus().toUpperCase());
            }
            if(md_schedule_exec.getSync_process() > -1){
                contentValues.put(SYNC_PROCESS,md_schedule_exec.getSync_process());
            }
            //
            return contentValues;
        }
    }
}
