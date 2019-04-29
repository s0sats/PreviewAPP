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
import com.namoadigital.prj001.model.IO_Conf_Tracking;
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.sql.IO_Conf_Tracking_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class IO_Inbound_ItemDao extends BaseDao implements DaoWithReturn<IO_Inbound_Item>,DaoWithReturnSharedDbInstance<IO_Inbound_Item> {

    private final Mapper<IO_Inbound_Item, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor,IO_Inbound_Item> toIO_Inbound_ItemMapper;

    public static final String TABLE = "io_inbound_item";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String INBOUND_PREFIX = "inbound_prefix";
    public static final String INBOUND_CODE = "inbound_code";
    public static final String INBOUND_ITEM = "inbound_item";
    public static final String PRODUCT_CODE = "product_code";
    public static final String SERIAL_CODE = "serial_code";
    public static final String SERIAL_ID = "serial_id";
    public static final String SITE_CODE = "site_code";
    public static final String ZONE_CODE = "zone_code";
    public static final String ZONE_ID = "zone_id";
    public static final String ZONE_DESC =	"zone_desc";
    public static final String LOCAL_CODE = "local_code";
    public static final String LOCAL_ID	= "local_id";
    public static final String CONF_DATE = "conf_date";
    public static final String STATUS = "status";
    public static final String COMMENTS = "comments";
    public static final String PLANNED_ZONE_CODE = "planned_zone_code";
    public static final String PLANNED_LOCAL_CODE = "planned_local_code";
    public static final String PLANNED_CLASS_CODE = "planned_class_code";
    public static final String SAVE_DATE = "save_date";
    public static final String UPDATE_REQUIRED = "update_required";
    public static final String PENDING_QTY = "PENDING_QTY";
    //Constantes abaixo somente SÃO usada em queries
    public static final String PLANNED_ZONE_ID = "planned_zone_id";
    public static final String PLANNED_LOCAL_ID = "planned_local_id";


    public IO_Inbound_ItemDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new IO_Inbound_ItemToContentValuesMapper();
        this.toIO_Inbound_ItemMapper = new CursorIO_Inbound_ItemMapper();
    }

    /**
     *  Metodo insert ou update criado usando nova metodologia.
     *  Diferente dos metodo usados até hoje, nesse metodo, esse metodo retorna
     *  um objeto com informações referente a ação executada.
     *
     *  Esse metodo primeiro tenta executar o update do registro e caso a qtd
     *  de linhas alterdas seja 0, executa o insert usando o metodo insertOrThrow
     *  que retorna SQLiteException em caso de erro no insert.
     *
     *  O obj de retorno contem a informação de se houve erro ao executar o metodo
     *  e deve ser SEMPRE avalidado após ser retornado.
     *
     *  Em caso de exception, o obj de retorno recebe a flag de erro e msg do erro
     *  além de gerar um arquivo de exception
     *
     * @param io_inbound_item - Item da Inbound
     * @returnObj com informação referentes a operação executada, seu sucesso e
     *  info de qtd de registros alterados ou row id do insert
     */
    @Override
    public DaoObjReturn addUpdate(IO_Inbound_Item  io_inbound_item) {
        return addUpdate(io_inbound_item,null);
    }

    /**
     * Metodo usando nova metodologia e compartilhandomento podendo compartilhar
     * instance do banco.
     *
     * @param io_inbound_item
     * @param dbInstance - Instance de banco ja aberta ou null.
     * @return
     */
    @Override
    public DaoObjReturn addUpdate(IO_Inbound_Item io_inbound_item,@Nullable SQLiteDatabase dbInstance) {
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
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_inbound_item.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_PREFIX).append(" = '").append(String.valueOf(io_inbound_item.getInbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_CODE).append(" = '").append(String.valueOf(io_inbound_item.getInbound_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_ITEM).append(" = '").append(String.valueOf(io_inbound_item.getInbound_item())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_inbound_item), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_inbound_item));
            }
            //Tenta remover tracking
            IO_Conf_TrackingDao confTrackingDao = new IO_Conf_TrackingDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
            //
            DaoObjReturn confTrackingDaoRet = confTrackingDao.removeByInboundItem(io_inbound_item, db);
            if(confTrackingDaoRet.hasError()){
                throw new Exception(confTrackingDaoRet.getRawMessage());
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

        if(dbInstance == null){
            closeDB();
        }

        return daoObjReturn;
    }

    @Override
    public DaoObjReturn addUpdate(List<IO_Inbound_Item> io_inbound_items, boolean status) {
        return addUpdate(io_inbound_items,status,null);
    }

    /**
     *
     * @param io_inbound_items
     * @param status
     * @param dbInstance
     * @return
     */
    @Override
    public DaoObjReturn addUpdate(List<IO_Inbound_Item> io_inbound_items, boolean status,@Nullable SQLiteDatabase dbInstance) {
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

            for (IO_Inbound_Item io_inbound_item : io_inbound_items) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_inbound_item.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(INBOUND_PREFIX).append(" = '").append(String.valueOf(io_inbound_item.getInbound_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(INBOUND_CODE).append(" = '").append(String.valueOf(io_inbound_item.getInbound_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(INBOUND_ITEM).append(" = '").append(String.valueOf(io_inbound_item.getInbound_item())).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_inbound_item), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_inbound_item));
                }
                //Tenta remover tracking
                IO_Conf_TrackingDao confTrackingDao = new IO_Conf_TrackingDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                //
                DaoObjReturn confTrackingDaoRet = confTrackingDao.removeByInboundItem(io_inbound_item, db);
                if(confTrackingDaoRet.hasError()){
                    throw new Exception(confTrackingDaoRet.getRawMessage());
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

    /**
     * Remove todos itens baseado na PK do cabeçalho
     *
     * @param io_inbound_item
     * @param dbInstance
     * @return
     */
    @Override
    public DaoObjReturn remove(IO_Inbound_Item io_inbound_item, @Nullable SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_inbound_item.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_PREFIX).append(" = '").append(String.valueOf(io_inbound_item.getInbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_CODE).append(" = '").append(String.valueOf(io_inbound_item.getInbound_code())).append("'");
            /*sbWhere.append(" and ");
            sbWhere.append(INBOUND_ITEM).append(" = '").append(String.valueOf(io_inbound_item.getInbound_item())).append("'");*/
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

    /**
     * Remove todos itens baseado na PK do cabeçalho
     *
     * @param io_inbound
     * @param dbInstance
     * @return
     */
    public DaoObjReturn remove(IO_Inbound io_inbound, @Nullable SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_inbound.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_PREFIX).append(" = '").append(String.valueOf(io_inbound.getInbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_CODE).append(" = '").append(String.valueOf(io_inbound.getInbound_code())).append("'");
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
    public IO_Inbound_Item getByString(String sQuery) {
        IO_Inbound_Item io_inbound_item = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_inbound_item = toIO_Inbound_ItemMapper.map(cursor);
            }
            //Seleciona tracking dos in_confs
            if(io_inbound_item != null){
                IO_Conf_TrackingDao confTrackingDao = new IO_Conf_TrackingDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                //
                io_inbound_item.setTracking_list(
                    (ArrayList<IO_Conf_Tracking>) confTrackingDao.query(
                        new IO_Conf_Tracking_Sql_001(
                            io_inbound_item.getCustomer_code(),
                            io_inbound_item.getInbound_prefix(),
                            io_inbound_item.getInbound_code(),
                            io_inbound_item.getInbound_item(),
                            ConstantBaseApp.IO_INBOUND
                        ).toSqlQuery()
                    )
                );
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_inbound_item;
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
    public List<IO_Inbound_Item> query(String sQuery) {
        List<IO_Inbound_Item> io_inbound_items = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                IO_Inbound_Item uAux = toIO_Inbound_ItemMapper.map(cursor);
                //
                //Seleciona tracking dos in_confs
                if(uAux != null){
                    IO_Conf_TrackingDao confTrackingDao = new IO_Conf_TrackingDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    );
                    //
                    uAux.setTracking_list(
                        (ArrayList<IO_Conf_Tracking>) confTrackingDao.query(
                            new IO_Conf_Tracking_Sql_001(
                                uAux.getCustomer_code(),
                                uAux.getInbound_prefix(),
                                uAux.getInbound_code(),
                                uAux.getInbound_item(),
                                ConstantBaseApp.IO_INBOUND
                            ).toSqlQuery()
                        )
                    );
                }
                //
                io_inbound_items.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_inbound_items;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> io_inbound_items = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_inbound_items.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_inbound_items;
    }

    /**
     * Deleta o inbound item baseado na pk
     * @param io_inbound_item
     * @param dbInstance
     * @return
     */
    public DaoObjReturn delete(IO_Inbound_Item io_inbound_item,@Nullable SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_inbound_item.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_PREFIX).append(" = '").append(String.valueOf(io_inbound_item.getInbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_CODE).append(" = '").append(String.valueOf(io_inbound_item.getInbound_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_ITEM).append(" = '").append(String.valueOf(io_inbound_item.getInbound_item())).append("'");
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

    private class CursorIO_Inbound_ItemMapper implements Mapper<Cursor, IO_Inbound_Item> {
        @Override
        public IO_Inbound_Item map(Cursor cursor) {
            IO_Inbound_Item io_inbound_item = new IO_Inbound_Item();
            //
            io_inbound_item.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            io_inbound_item.setInbound_prefix(cursor.getInt(cursor.getColumnIndex(INBOUND_PREFIX)));
            io_inbound_item.setInbound_code(cursor.getInt(cursor.getColumnIndex(INBOUND_CODE)));
            io_inbound_item.setInbound_item(cursor.getInt(cursor.getColumnIndex(INBOUND_ITEM)));
            io_inbound_item.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
            io_inbound_item.setSerial_code(cursor.getLong(cursor.getColumnIndex(SERIAL_CODE)));
            if(cursor.isNull(cursor.getColumnIndex(SITE_CODE))) {
                io_inbound_item.setSite_code(null);
            }else{
                io_inbound_item.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ZONE_CODE))) {
                io_inbound_item.setZone_code(null);
            }else{
                io_inbound_item.setZone_code(cursor.getInt(cursor.getColumnIndex(ZONE_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ZONE_ID))) {
                io_inbound_item.setZone_id(null);
            }else{
                io_inbound_item.setZone_id(cursor.getString(cursor.getColumnIndex(ZONE_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ZONE_DESC))) {
                io_inbound_item.setZone_desc(null);
            }else{
                io_inbound_item.setZone_desc(cursor.getString(cursor.getColumnIndex(ZONE_DESC)));
            }
            if(cursor.isNull(cursor.getColumnIndex(LOCAL_CODE))) {
                io_inbound_item.setLocal_code(null);
            }else{
                io_inbound_item.setLocal_code(cursor.getInt(cursor.getColumnIndex(LOCAL_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(LOCAL_ID))) {
                io_inbound_item.setLocal_id(null);
            }else{
                io_inbound_item.setLocal_id(cursor.getString(cursor.getColumnIndex(LOCAL_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CONF_DATE))) {
                io_inbound_item.setConf_date(null);
            }else{
                io_inbound_item.setConf_date(cursor.getString(cursor.getColumnIndex(CONF_DATE)));
            }
            io_inbound_item.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            if(cursor.isNull(cursor.getColumnIndex(COMMENTS))) {
                io_inbound_item.setComments(null);
            }else{
                io_inbound_item.setComments(cursor.getString(cursor.getColumnIndex(COMMENTS)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PLANNED_ZONE_CODE))) {
                io_inbound_item.setPlanned_zone_code(null);
            }else{
                io_inbound_item.setPlanned_zone_code(cursor.getInt(cursor.getColumnIndex(PLANNED_ZONE_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PLANNED_LOCAL_CODE))) {
                io_inbound_item.setPlanned_local_code(null);
            }else{
                io_inbound_item.setPlanned_local_code(cursor.getInt(cursor.getColumnIndex(PLANNED_LOCAL_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(PLANNED_CLASS_CODE))) {
                io_inbound_item.setPlanned_class_code(null);
            }else{
                io_inbound_item.setPlanned_class_code(cursor.getInt(cursor.getColumnIndex(PLANNED_CLASS_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SAVE_DATE))) {
                io_inbound_item.setSave_date(null);
            }else{
                io_inbound_item.setSave_date(cursor.getString(cursor.getColumnIndex(SAVE_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(UPDATE_REQUIRED))) {
                io_inbound_item.setUpdate_required(0);
            }else{
                io_inbound_item.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));
            }
            //
            return io_inbound_item;
        }
    }

    private class IO_Inbound_ItemToContentValuesMapper implements Mapper<IO_Inbound_Item, ContentValues> {
        @Override
        public ContentValues map(IO_Inbound_Item io_inbound_item) {
            ContentValues contentValues = new ContentValues();
            //
            if(io_inbound_item.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,io_inbound_item.getCustomer_code());
            }
            if(io_inbound_item.getInbound_prefix() > -1){
                contentValues.put(INBOUND_PREFIX,io_inbound_item.getInbound_prefix());
            }
            if(io_inbound_item.getInbound_code() > -1){
                contentValues.put(INBOUND_CODE,io_inbound_item.getInbound_code());
            }
            if(io_inbound_item.getInbound_item() > -1){
                contentValues.put(INBOUND_ITEM,io_inbound_item.getInbound_item());
            }
            if(io_inbound_item.getProduct_code() > -1){
                contentValues.put(PRODUCT_CODE,io_inbound_item.getProduct_code());
            }
            if(io_inbound_item.getSerial_code() > -1){
                contentValues.put(SERIAL_CODE,io_inbound_item.getSerial_code());
            }
            contentValues.put(SITE_CODE,io_inbound_item.getSite_code());
            contentValues.put(ZONE_CODE,io_inbound_item.getZone_code());
            contentValues.put(ZONE_ID,io_inbound_item.getZone_id());
            contentValues.put(ZONE_DESC,io_inbound_item.getZone_desc());
            contentValues.put(LOCAL_CODE,io_inbound_item.getLocal_code());
            contentValues.put(LOCAL_ID,io_inbound_item.getLocal_id());
            contentValues.put(CONF_DATE,io_inbound_item.getConf_date());
            if(io_inbound_item.getStatus() != null){
                contentValues.put(STATUS,io_inbound_item.getStatus());
            }
            contentValues.put(COMMENTS,io_inbound_item.getComments());
            contentValues.put(PLANNED_ZONE_CODE,io_inbound_item.getPlanned_zone_code());
            contentValues.put(PLANNED_LOCAL_CODE,io_inbound_item.getPlanned_local_code());
            contentValues.put(PLANNED_CLASS_CODE,io_inbound_item.getPlanned_class_code());
            contentValues.put(SAVE_DATE,io_inbound_item.getSave_date());
            if(io_inbound_item.getUpdate_required() > -1){
                contentValues.put(UPDATE_REQUIRED,io_inbound_item.getUpdate_required());
            }
            //
            return contentValues;
        }
    }
}
