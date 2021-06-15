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
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class GE_Custom_Form_LocalDao extends BaseDao implements Dao<GE_Custom_Form_Local> {
    private final Mapper<GE_Custom_Form_Local, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form_Local> toGE_Custom_Form_LocalMapper;

    public static final String TABLE = "ge_custom_forms_local";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String CUSTOM_FORM_DATA = "custom_form_data";
    public static final String CUSTOM_FORM_PRE = "custom_form_pre";
    public static final String CUSTOM_FORM_STATUS = "custom_form_status";
    public static final String REQUIRE_SIGNATURE = "require_signature";
    public static final String AUTOMATIC_FILL = "automatic_fill";
    public static final String CUSTOM_PRODUCT_CODE = "custom_product_code";
    public static final String CUSTOM_PRODUCT_DESC = "custom_product_desc";
    public static final String CUSTOM_PRODUCT_ID = "custom_product_id";
    public static final String CUSTOM_PRODUCT_ICON_NAME = "custom_product_icon_name";
    public static final String CUSTOM_PRODUCT_ICON_URL = "custom_product_icon_url";
    public static final String CUSTOM_PRODUCT_ICON_URL_LOCAL = "custom_product_icon_url_local";
    public static final String CUSTOM_FORM_DESC = "custom_form_desc";
    public static final String SERIAL_ID = "serial_id";
    public static final String SCHEDULE_DATE_START_FORMAT = "schedule_date_start_format";
    public static final String SCHEDULE_DATE_END_FORMAT = "schedule_date_end_format";
    public static final String SCHEDULE_DATE_START_FORMAT_MS = "schedule_date_start_format_ms";
    public static final String SCHEDULE_DATE_END_FORMAT_MS = "schedule_date_end_format_ms";
    public static final String REQUIRE_SERIAL = "require_serial";
    public static final String ALLOW_NEW_SERIAL_CL = "allow_new_serial_cl";
    public static final String REQUIRE_LOCATION = "require_location";
    public static final String REQUIRE_SERIAL_DONE = "require_serial_done";
    public static final String ALL_SITE = "all_site";
    public static final String ALL_OPERATION = "all_operation";
    public static final String ALL_PRODUCT = "all_product";

    public static final String SITE_CODE = "site_code";
    public static final String SITE_ID = "site_id";
    public static final String SITE_DESC = "site_desc";

    public static final String IO_CONTROL = "io_control";
    public static final String INBOUND_AUTO_CREATE = "inbound_auto_create";

    public static final String OPERATION_CODE = "operation_code";
    public static final String OPERATION_ID = "operation_id";
    public static final String OPERATION_DESC = "operation_desc";

    public static final String LOCAL_CONTROL = "local_control";

    public static final String PRODUCT_IO_CONTROL = "product_io_control";

    public static final String SITE_RESTRICTION = "site_restriction";
    public static final String SERIAL_RULE = "serial_rule";
    public static final String SERIAL_MIN_LENGTH = "serial_min_length";
    public static final String SERIAL_MAX_LENGTH = "serial_max_length";
    public static final String SCHEDULE_COMMENTS = "schedule_comments";
    public static final String SCHEDULE_PREFIX = "schedule_prefix";
    public static final String SCHEDULE_CODE = "schedule_code";
    public static final String SCHEDULE_EXEC = "schedule_exec";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String TICKET_SEQ = "ticket_seq";
    public static final String TICKET_SEQ_TMP = "ticket_seq_tmp";
    public static final String STEP_CODE = "step_code";
    public static final String TAG_OPERATIONAL_CODE = "tag_operational_code";
    public static final String TAG_OPERATIONAL_ID = "tag_operational_id";
    public static final String TAG_OPERATIONAL_DESC = "tag_operational_desc";

    public GE_Custom_Form_LocalDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new GE_Custom_FormToContentValuesMapper();
        this.toGE_Custom_Form_LocalMapper = new CursorGE_Custom_FormMapper();
    }

    /**
     * LUCHE - 15/03/2019
     *
     * Metodo insert ou update criado usando nova metodologia.
     * Diferente dos metodo usados até hoje, nesse metodo, esse metodo retorna
     * um objeto com informações referente a ação executada.
     *
     * Esse metodo primeiro tenta executar o update do registro e caso a qtd
     * de linhas alterdas seja 0, executa o insert usando o metodo insertOrThrow
     * que retorna SQLiteException em caso de erro no insert.
     *
     * O obj de retorno contem a informação de se houve erro ao executar o metodo
     * e deve ser SEMPRE avalidado após ser retornado.
     *
     * Em caso de exception, o obj de retorno recebe a flag de erro e msg do erro
     * além de gerar um arquivo de exception
     *
     * @param custom_form_local -> form a ser inserido
     * @return Obj com informação referentes a operação executada, seu sucesso e
     * info de qtd de registros alterados ou row id do insert
     */
    public DaoObjReturn addUpdateThrowException(GE_Custom_Form_Local custom_form_local){
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        openDB();

        try{
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_type())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_version())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_data())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(custom_form_local), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(custom_form_local));
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

        closeDB();
        return daoObjReturn;
    }

    /**
     * Metodo de insert ou update em massa usando nova metodologia com obj de retorno da operação.
     *
     * @param custom_form_locals
     * @param status -> Var que identifica se antes da operação deve executar um TRUNCATE  na tabela.
     * @return
     */
    public DaoObjReturn addUpdateThrowException(Iterable<GE_Custom_Form_Local> custom_form_locals, boolean status){
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        openDB();

        try{
            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Local custom_form_local : custom_form_locals) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_data())).append("'");
                //
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(custom_form_local), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0) {
                    curAction = DaoObjReturn.INSERT;
                    addUpdateRet = db.insertOrThrow(TABLE, null, toContentValuesMapper.map(custom_form_local));
                }
            }
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
        //
        closeDB();
        //
        return daoObjReturn;
    }


    //region SCHEDULE_EXEC
    /**
     * LUCHE - 15/05/2020
     *
     * Metodo que executa insert / update do obj usando instance de banco de dados comparilhada
     * @param custom_form_local
     * @param dbInstance
     * @return - DaoObjReturn com informações
     */
    public DaoObjReturn addUpdateThrowExceptionWithSharedDbInstance(GE_Custom_Form_Local custom_form_local , SQLiteDatabase dbInstance){
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_type())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_version())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_data())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(custom_form_local), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(custom_form_local));
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

        if (dbInstance == null) {
            closeDB();
        }
        return daoObjReturn;
    }

    /**
     * LUCHE - 15/05/2020
     *
     * Metodo que executa insert / update de lista usando instance de banco de dados comparilhada
     * @param custom_form_locals
     * @param status
     * @param dbInstance
     * @return - DaoObjReturn com informações
     */
    public DaoObjReturn addUpdateThrowExceptionWithSharedDbInstance(Iterable<GE_Custom_Form_Local> custom_form_locals, boolean status, SQLiteDatabase dbInstance){
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

            for (GE_Custom_Form_Local custom_form_local : custom_form_locals) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_data())).append("'");
                //
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(custom_form_local), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0) {
                    curAction = DaoObjReturn.INSERT;
                    addUpdateRet = db.insertOrThrow(TABLE, null, toContentValuesMapper.map(custom_form_local));
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
        //
        return daoObjReturn;
    }

    /**
     * LUCHE - 15/05/2020
     * <p></p>
     * Criado metodo que busca GE_Custom_Form_Local usando instancia de banco de dados compartilhada
     * @param s_query
     * @param dbInstance
     * @return Obj GE_Custom_Form_Local ou null
     */
    public GE_Custom_Form_Local getByStringSharedDbInstance(String s_query, SQLiteDatabase dbInstance) {
        GE_Custom_Form_Local custom_form_local = null;
        if (dbInstance == null) {
            openDB();
        } else {
            this.db = dbInstance;
        }

        try {
            Cursor cursor = db.rawQuery(s_query, null);
            while (cursor.moveToNext()) {
                custom_form_local = toGE_Custom_Form_LocalMapper.map(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        if(dbInstance == null) {
            closeDB();
        }
        //
        return custom_form_local;
    }

    //endregion

    @Override
    public void addUpdate(GE_Custom_Form_Local custom_form_local) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_local)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_version())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_data())).append("'");


                db.update(TABLE, toContentValuesMapper.map(custom_form_local), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form_Local> custom_form_locals, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form_Local custom_form_local : custom_form_locals) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form_local)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_version())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_DATA).append(" = '").append(String.valueOf(custom_form_local.getCustom_form_data())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form_local), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            db.endTransaction();
        }

        closeDB();
    }

    @Override
    public void addUpdate(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void remove(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public GE_Custom_Form_Local getByString(String s_query) {
        GE_Custom_Form_Local custom_form_local = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form_local = toGE_Custom_Form_LocalMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);

        } finally {
        }

        closeDB();

        return custom_form_local;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        HMAux hmAux = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                //hmAux = toHMAuxMapper.map(cursor);
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
    public List<GE_Custom_Form_Local> query(String s_query) {
        List<GE_Custom_Form_Local> custom_form_locals = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form_Local uAux = toGE_Custom_Form_LocalMapper.map(cursor);
                custom_form_locals.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return custom_form_locals;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> custom_form_locals = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form_locals.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return custom_form_locals;
    }

    private class CursorGE_Custom_FormMapper implements Mapper<Cursor, GE_Custom_Form_Local> {
        @Override
        public GE_Custom_Form_Local map(Cursor cursor) {
            GE_Custom_Form_Local custom_form_local = new GE_Custom_Form_Local();

            custom_form_local.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form_local.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form_local.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            custom_form_local.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            custom_form_local.setCustom_form_data(cursor.getLong(cursor.getColumnIndex(CUSTOM_FORM_DATA)));
            custom_form_local.setCustom_form_pre(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_PRE)));
            custom_form_local.setCustom_form_status(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_STATUS)));
            custom_form_local.setRequire_signature(cursor.getInt(cursor.getColumnIndex(REQUIRE_SIGNATURE)));
            custom_form_local.setAutomatic_fill(cursor.getString(cursor.getColumnIndex(AUTOMATIC_FILL)));
            custom_form_local.setCustom_product_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_PRODUCT_CODE)));
            custom_form_local.setCustom_product_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_PRODUCT_DESC)));
            custom_form_local.setCustom_product_id(cursor.getString(cursor.getColumnIndex(CUSTOM_PRODUCT_ID)));
            custom_form_local.setCustom_product_icon_name(cursor.getString(cursor.getColumnIndex(CUSTOM_PRODUCT_ICON_NAME)));
            custom_form_local.setCustom_product_icon_url(cursor.getString(cursor.getColumnIndex(CUSTOM_PRODUCT_ICON_URL)));
            custom_form_local.setCustom_product_icon_url_local(cursor.getString(cursor.getColumnIndex(CUSTOM_PRODUCT_ICON_URL_LOCAL)));
            custom_form_local.setCustom_form_desc(cursor.getString(cursor.getColumnIndex(CUSTOM_FORM_DESC)));
            custom_form_local.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            if (cursor.isNull(cursor.getColumnIndex(SCHEDULE_DATE_START_FORMAT))) {
                custom_form_local.setSchedule_date_start_format("1900-01-01 00:00:00 +00:00");
            } else {
                custom_form_local.setSchedule_date_start_format(cursor.getString(cursor.getColumnIndex(SCHEDULE_DATE_START_FORMAT)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SCHEDULE_DATE_END_FORMAT))) {
                custom_form_local.setSchedule_date_end_format("1900-01-01 00:00:00 +00:00");
            } else {
                custom_form_local.setSchedule_date_end_format(cursor.getString(cursor.getColumnIndex(SCHEDULE_DATE_END_FORMAT)));
            }
            custom_form_local.setRequire_serial(cursor.getInt(cursor.getColumnIndex(REQUIRE_SERIAL)));
            custom_form_local.setAllow_new_serial_cl(cursor.getInt(cursor.getColumnIndex(ALLOW_NEW_SERIAL_CL)));
            custom_form_local.setRequire_location(cursor.getInt(cursor.getColumnIndex(REQUIRE_LOCATION)));
            custom_form_local.setAll_product(cursor.getInt(cursor.getColumnIndex(ALL_PRODUCT)));
            custom_form_local.setAll_site(cursor.getInt(cursor.getColumnIndex(ALL_SITE)));
            custom_form_local.setAll_operation(cursor.getInt(cursor.getColumnIndex(ALL_OPERATION)));

            custom_form_local.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            custom_form_local.setSite_id(cursor.getString(cursor.getColumnIndex(SITE_ID)));
            custom_form_local.setSite_desc(cursor.getString(cursor.getColumnIndex(SITE_DESC)));
            custom_form_local.setIo_control(cursor.getInt(cursor.getColumnIndex(IO_CONTROL)));
            custom_form_local.setInbound_auto_create(cursor.getInt(cursor.getColumnIndex(INBOUND_AUTO_CREATE)));
            custom_form_local.setOperation_code(cursor.getInt(cursor.getColumnIndex(OPERATION_CODE)));
            custom_form_local.setOperation_id(cursor.getString(cursor.getColumnIndex(OPERATION_ID)));
            custom_form_local.setOperation_desc(cursor.getString(cursor.getColumnIndex(OPERATION_DESC)));
            custom_form_local.setLocal_control(cursor.getInt(cursor.getColumnIndex(LOCAL_CONTROL)));
            custom_form_local.setProduct_io_control(cursor.getInt(cursor.getColumnIndex(PRODUCT_IO_CONTROL)));
            custom_form_local.setSite_restriction(cursor.getInt(cursor.getColumnIndex(SITE_RESTRICTION)));
            custom_form_local.setSerial_rule(cursor.getString(cursor.getColumnIndex(SERIAL_RULE)));

            if (cursor.isNull(cursor.getColumnIndex(SERIAL_MIN_LENGTH))) {
                custom_form_local.setSerial_min_length(null);
            } else {
                custom_form_local.setSerial_min_length(cursor.getInt(cursor.getColumnIndex(SERIAL_MIN_LENGTH)));
            }

            if (cursor.isNull(cursor.getColumnIndex(SERIAL_MAX_LENGTH))) {
                custom_form_local.setSerial_max_length(null);
            } else {
                custom_form_local.setSerial_max_length(cursor.getInt(cursor.getColumnIndex(SERIAL_MAX_LENGTH)));
            }
            //
            custom_form_local.setRequire_serial_done(cursor.getInt(cursor.getColumnIndex(REQUIRE_SERIAL_DONE)));
            //
            if(cursor.isNull(cursor.getColumnIndex(SCHEDULE_COMMENTS))){
                custom_form_local.setSchedule_comments(null);
            }else{
                custom_form_local.setSchedule_comments(cursor.getString(cursor.getColumnIndex(SCHEDULE_COMMENTS)));
            }

            if(cursor.isNull(cursor.getColumnIndex(SCHEDULE_PREFIX))){
                custom_form_local.setSchedule_prefix(null);
            }else{
                custom_form_local.setSchedule_prefix(cursor.getInt(cursor.getColumnIndex(SCHEDULE_PREFIX)));
            }

            if(cursor.isNull(cursor.getColumnIndex(SCHEDULE_CODE))){
                custom_form_local.setSchedule_code(null);
            }else{
                custom_form_local.setSchedule_code(cursor.getInt(cursor.getColumnIndex(SCHEDULE_CODE)));
            }

            if(cursor.isNull(cursor.getColumnIndex(SCHEDULE_EXEC))){
                custom_form_local.setSchedule_exec(null);
            }else{
                custom_form_local.setSchedule_exec(cursor.getInt(cursor.getColumnIndex(SCHEDULE_EXEC)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(TICKET_PREFIX))){
                custom_form_local.setTicket_prefix(null);
            }else{
                custom_form_local.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            }

            if(cursor.isNull(cursor.getColumnIndex(TICKET_CODE))){
                custom_form_local.setTicket_code(null);
            }else{
                custom_form_local.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            }

            if(cursor.isNull(cursor.getColumnIndex(TICKET_SEQ))){
                custom_form_local.setTicket_seq(null);
            }else{
                custom_form_local.setTicket_seq(cursor.getInt(cursor.getColumnIndex(TICKET_SEQ)));
            }

            if(cursor.isNull(cursor.getColumnIndex(TICKET_SEQ_TMP))){
                custom_form_local.setTicket_seq_tmp(null);
            }else{
                custom_form_local.setTicket_seq_tmp(cursor.getInt(cursor.getColumnIndex(TICKET_SEQ_TMP)));
            }

            if(cursor.isNull(cursor.getColumnIndex(STEP_CODE))){
                custom_form_local.setStep_code(null);
            }else{
                custom_form_local.setStep_code(cursor.getInt(cursor.getColumnIndex(STEP_CODE)));
            }
            custom_form_local.setTag_operational_code(cursor.getInt(cursor.getColumnIndex(TAG_OPERATIONAL_CODE)));
            custom_form_local.setTag_operational_id(cursor.getString(cursor.getColumnIndex(TAG_OPERATIONAL_ID)));
            custom_form_local.setTag_operational_desc(cursor.getString(cursor.getColumnIndex(TAG_OPERATIONAL_DESC)));

            return custom_form_local;
        }
    }

    private class GE_Custom_FormToContentValuesMapper implements Mapper<GE_Custom_Form_Local, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form_Local custom_form_local) {
            ContentValues contentValues = new ContentValues();

            if (custom_form_local.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, custom_form_local.getCustomer_code());
            }
            if (custom_form_local.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, custom_form_local.getCustom_form_type());
            }
            if (custom_form_local.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE, custom_form_local.getCustom_form_code());
            }
            if (custom_form_local.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION, custom_form_local.getCustom_form_version());
            }
            if (custom_form_local.getCustom_form_data() > -1) {
                contentValues.put(CUSTOM_FORM_DATA, custom_form_local.getCustom_form_data());
            }
            if (custom_form_local.getCustom_form_pre() != null) {
                contentValues.put(CUSTOM_FORM_PRE, custom_form_local.getCustom_form_pre());
            }
            if (custom_form_local.getCustom_form_status() != null) {
                contentValues.put(CUSTOM_FORM_STATUS, custom_form_local.getCustom_form_status());
            }

            if (custom_form_local.getRequire_signature() > -1) {
                contentValues.put(REQUIRE_SIGNATURE, custom_form_local.getRequire_signature());
            }
            if (custom_form_local.getAutomatic_fill() != null) {
                contentValues.put(AUTOMATIC_FILL, custom_form_local.getAutomatic_fill());
            }
            if (custom_form_local.getCustom_product_code() > -1) {
                contentValues.put(CUSTOM_PRODUCT_CODE, custom_form_local.getCustom_product_code());
            }
            if (custom_form_local.getCustom_product_desc() != null) {
                contentValues.put(CUSTOM_PRODUCT_DESC, custom_form_local.getCustom_product_desc());
            }
            if (custom_form_local.getCustom_product_id() != null) {
                contentValues.put(CUSTOM_PRODUCT_ID, custom_form_local.getCustom_product_id());
            }
            if (custom_form_local.getCustom_product_icon_name() != null) {
                contentValues.put(CUSTOM_PRODUCT_ICON_NAME, custom_form_local.getCustom_product_icon_name());
            }
            if (custom_form_local.getCustom_product_icon_url() != null) {
                contentValues.put(CUSTOM_PRODUCT_ICON_URL, custom_form_local.getCustom_product_icon_url());
            }
            if (custom_form_local.getCustom_product_icon_url_local() != null) {
                contentValues.put(CUSTOM_PRODUCT_ICON_URL_LOCAL, custom_form_local.getCustom_product_icon_url_local());
            }
            if (custom_form_local.getCustom_form_desc() != null) {
                contentValues.put(CUSTOM_FORM_DESC, custom_form_local.getCustom_form_desc());
            }
            if (custom_form_local.getSerial_id() != null) {
                contentValues.put(SERIAL_ID, custom_form_local.getSerial_id());
            }
            if (custom_form_local.getSchedule_date_start_format() != null) {
                contentValues.put(SCHEDULE_DATE_START_FORMAT, custom_form_local.getSchedule_date_start_format());
            } else {
                contentValues.put(SCHEDULE_DATE_START_FORMAT, "1900-01-01 00:00:00 +00:00");
            }
            if (custom_form_local.getSchedule_date_end_format() != null) {
                contentValues.put(SCHEDULE_DATE_END_FORMAT, custom_form_local.getSchedule_date_end_format());
            } else {
                contentValues.put(SCHEDULE_DATE_END_FORMAT, "1900-01-01 00:00:00 +00:00");
            }
            if (custom_form_local.getSchedule_date_start_format_ms() > -1) {
                contentValues.put(SCHEDULE_DATE_START_FORMAT_MS, custom_form_local.getSchedule_date_start_format_ms());
            }
            if (custom_form_local.getSchedule_date_end_format_ms() > -1) {
                contentValues.put(SCHEDULE_DATE_END_FORMAT_MS, custom_form_local.getSchedule_date_end_format_ms());
            }
            if (custom_form_local.getRequire_serial() > -1) {
                contentValues.put(REQUIRE_SERIAL, custom_form_local.getRequire_serial());
            }
            if (custom_form_local.getAllow_new_serial_cl() > -1) {
                contentValues.put(ALLOW_NEW_SERIAL_CL, custom_form_local.getAllow_new_serial_cl());
            }
            if (custom_form_local.getRequire_location() > -1) {
                contentValues.put(REQUIRE_LOCATION, custom_form_local.getRequire_location());
            }
            if (custom_form_local.getAll_product() > -1) {
                contentValues.put(ALL_PRODUCT, custom_form_local.getAll_product());
            }
            if (custom_form_local.getAll_site() > -1) {
                contentValues.put(ALL_SITE, custom_form_local.getAll_site());
            }
            if (custom_form_local.getAll_operation() > -1) {
                contentValues.put(ALL_OPERATION, custom_form_local.getAll_operation());
            }

            if (custom_form_local.getSite_code() > -1) {
                contentValues.put(SITE_CODE, custom_form_local.getSite_code());
            }
            if (custom_form_local.getSite_id() != null) {
                contentValues.put(SITE_ID, custom_form_local.getSite_id());
            }
            if (custom_form_local.getSite_desc() != null) {
                contentValues.put(SITE_DESC, custom_form_local.getSite_desc());
            }
            if (custom_form_local.getIo_control() > -1) {
                contentValues.put(IO_CONTROL, custom_form_local.getIo_control());
            }
            if (custom_form_local.getInbound_auto_create() > -1) {
                contentValues.put(INBOUND_AUTO_CREATE, custom_form_local.getInbound_auto_create());
            }
            if (custom_form_local.getOperation_code() > -1) {
                contentValues.put(OPERATION_CODE, custom_form_local.getOperation_code());
            }
            if (custom_form_local.getOperation_id() != null) {
                contentValues.put(OPERATION_ID, custom_form_local.getOperation_id());
            }
            if (custom_form_local.getOperation_desc() != null) {
                contentValues.put(OPERATION_DESC, custom_form_local.getOperation_desc());
            }
            if (custom_form_local.getLocal_control() > -1) {
                contentValues.put(LOCAL_CONTROL, custom_form_local.getLocal_control());
            }
            if (custom_form_local.getProduct_io_control() > -1) {
                contentValues.put(PRODUCT_IO_CONTROL, custom_form_local.getProduct_io_control());
            }
            if (custom_form_local.getSite_restriction() > -1) {
                contentValues.put(SITE_RESTRICTION, custom_form_local.getSite_restriction());
            }
            if (custom_form_local.getSerial_rule() != null) {
                contentValues.put(SERIAL_RULE, custom_form_local.getSerial_rule());
            }
            //
            contentValues.put(SERIAL_MIN_LENGTH, custom_form_local.getSerial_min_length());
            contentValues.put(SERIAL_MAX_LENGTH, custom_form_local.getSerial_max_length());
            //
            if (custom_form_local.getRequire_serial_done() > -1) {
                contentValues.put(REQUIRE_SERIAL_DONE, custom_form_local.getRequire_serial_done());
            }
            //
            contentValues.put(SCHEDULE_COMMENTS, custom_form_local.getSchedule_comments());
            contentValues.put(SCHEDULE_PREFIX, custom_form_local.getSchedule_prefix());
            contentValues.put(SCHEDULE_CODE, custom_form_local.getSchedule_code());
            contentValues.put(SCHEDULE_EXEC, custom_form_local.getSchedule_exec());
            //
            contentValues.put(TICKET_PREFIX, custom_form_local.getTicket_prefix());
            contentValues.put(TICKET_CODE, custom_form_local.getTicket_code());
            contentValues.put(TICKET_SEQ, custom_form_local.getTicket_seq());
            contentValues.put(TICKET_SEQ_TMP, custom_form_local.getTicket_seq_tmp());
            contentValues.put(STEP_CODE, custom_form_local.getStep_code());

            if(custom_form_local.getTag_operational_code() > -1){
                contentValues.put(TAG_OPERATIONAL_CODE, custom_form_local.getTag_operational_code());
            }
            if(custom_form_local.getTag_operational_id() != null){
                contentValues.put(TAG_OPERATIONAL_ID, custom_form_local.getTag_operational_id());
            }
            if(custom_form_local.getTag_operational_desc() != null){
                contentValues.put(TAG_OPERATIONAL_DESC, custom_form_local.getTag_operational_desc());
            }
            //
            return contentValues;
        }
    }

}
