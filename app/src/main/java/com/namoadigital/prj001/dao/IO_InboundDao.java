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
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.model.IO_Inbound_Item;
import com.namoadigital.prj001.sql.IO_Inbound_Item_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class IO_InboundDao extends BaseDao implements DaoWithReturn<IO_Inbound>{

    private final Mapper<IO_Inbound, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, IO_Inbound> toIO_InboundMapper;

    public static final String TABLE = "io_inbound";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String INBOUND_PREFIX = "inbound_prefix";
    public static final String INBOUND_CODE = "inbound_code";
    public static final String INBOUND_DESC = "inbound_desc";
    public static final String INBOUND_ID = "inbound_id";
    public static final String SCN = "scn";
    public static final String ORIGIN = "origin";
    public static final String TRANSPORT_ORDER = "transport_order";
    public static final String INVOICE_NUMBER = "invoice_number";
    public static final String INVOICE_DATE = "invoice_date";
    public static final String ETA_DATE = "eta_date";
    public static final String ARRIVAL_DATE = "arrival_date";
    public static final String FROM_TYPE = "from_type";
    public static final String FROM_PARTNER_CODE = "from_partner_code";
    public static final String FROM_PARTNER_ID = "from_partner_id";
    public static final String FROM_PARTNER_DESC = "from_partner_desc";
    public static final String FROM_SITE_CODE = "from_site_code";
    public static final String FROM_SITE_ID = "from_site_id";
    public static final String FROM_SITE_DESC = "from_site_desc";
    public static final String TO_SITE_CODE = "to_site_code";
    public static final String CARRIER_CODE = "carrier_code";
    public static final String CARRIER_ID = "carrier_id";
    public static final String CARRIER_DESC = "carrier_desc";
    public static final String TRUCK_NUMBER = "truck_number";
    public static final String DRIVER = "driver";
    public static final String COMMENTS = "comments";
    public static final String STATUS = "status";
    public static final String PERC_DONE = "perc_done";
    public static final String INBOUND_AUTO_SEQ = "inbound_auto_seq";
    public static final String MODAL_CODE = "modal_code";
    public static final String MODAL_ID = "modal_id";
    public static final String MODAL_DESC = "modal_desc";
    public static final String ALLOW_NEW_ITEM = "allow_new_item";
    public static final String ZONE_CODE_CONF = "zone_code_conf";
    public static final String ZONE_ID_CONF = "zone_id_conf";
    public static final String ZONE_DESC_CONF = "zone_desc_conf";
    public static final String LOCAL_CODE_CONF = "local_code_conf";
    public static final String LOCAL_ID_CONF = "local_id_conf";
    public static final String PUT_AWAY_PROCESS = "put_away_process";
    public static final String DONE_AUTOMATIC = "done_automatic";
    public static final String UPDATE_REQUIRED = "update_required";
    public static final String SYNC_REQUIRED = "sync_required";
    public static final String TOKEN = "token";

    public IO_InboundDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new IO_InboundToContentValuesMapper();
        this.toIO_InboundMapper = new CursorIO_InboundMapper();

    }

    /**
     * LUCHE - 18/03/2019
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
     * @param io_inbound -> Cabeçalho da inbound
     * @return Obj com informação referentes a operação executada, seu sucesso e
     * info de qtd de registros alterados ou row id do insert
     */
    @Override
    public DaoObjReturn addUpdate(IO_Inbound io_inbound) {
        return addUpdate(io_inbound,null);
//        DaoObjReturn daoObjReturn = new DaoObjReturn();
//        long addUpdateRet = 0;
//        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
//        //
//        openDB();
//
//        try{
//            db.beginTransaction();
//            //
//            curAction = DaoObjReturn.UPDATE;
//            //Where para update
//            StringBuilder sbWhere = new StringBuilder();
//            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_inbound.getCustomer_code())).append("'");
//            sbWhere.append(" and ");
//            sbWhere.append(INBOUND_PREFIX).append(" = '").append(String.valueOf(io_inbound.getInbound_prefix())).append("'");
//            sbWhere.append(" and ");
//            sbWhere.append(INBOUND_CODE).append(" = '").append(String.valueOf(io_inbound.getInbound_code())).append("'");
//            //Tenta update e armazena retorno
//            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_inbound), sbWhere.toString(), null);
//            //Se nenhuma linha afetada, tenta insert
//            if(addUpdateRet == 0){
//                curAction = DaoObjReturn.INSERT;
//                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_inbound));
//            }
//            //Se operação de insert ou update executada com sucesso
//            //Segue para inserção dos itens.
//            IO_Inbound_ItemDao inboundItemDao = new IO_Inbound_ItemDao(
//                    context,
//                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
//                    Constant.DB_VERSION_CUSTOM
//            );
//            //Chama insertUpdate de lista de item,passando db como param aguardando retorno.
//            DaoObjReturn inboundItemRet = inboundItemDao.addUpdate(io_inbound.getItems(),false,db);
//            //Se erro durante insert, dispara exception abortando o processamento.
//            if(inboundItemRet.hasError()){
//                throw new Exception(inboundItemRet.getErrorMsg());
//            }
//            //
//            db.setTransactionSuccessful();
//        }catch (SQLiteException e){
//            //Chama metodo que baseado na exception gera obj de retorno setado como erro
//            //e contendo msg de erro tratada.
//            daoObjReturn = ToolBox_Con.getSQLiteErrorCodeDescription(e.getMessage());
//            //Gera arquivo de exception usando dados da exception e do obj de retorno
//            ToolBox_Inf.registerException(
//                    getClass().getName(),
//                    new Exception(
//                            e.getMessage() + "\n" + daoObjReturn.getErrorMsg()
//                    )
//            );
//
//        }catch (Exception e){
//            //Seta obj de retorno com flag de erro e gera arquivo de exception
//            daoObjReturn.setError(true);
//            ToolBox_Inf.registerException(getClass().getName(), e);
//        }finally {
//            //Atualiza ação realizada no metodo e informação de qtd de registros alterado (update)
//            //ou rowId do ultimo insert.
//            db.endTransaction();
//            daoObjReturn.setAction(curAction);
//            daoObjReturn.setActionReturn(addUpdateRet);
//        }
//
//        closeDB();
//
//        return daoObjReturn;
    }

    public DaoObjReturn addUpdate(IO_Inbound io_inbound, SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_inbound.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_PREFIX).append(" = '").append(String.valueOf(io_inbound.getInbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_CODE).append(" = '").append(String.valueOf(io_inbound.getInbound_code())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_inbound), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_inbound));
            }
            //Se operação de insert ou update executada com sucesso
            //Segue para inserção dos itens.
            IO_Inbound_ItemDao inboundItemDao = new IO_Inbound_ItemDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
            //Chama insertUpdate de lista de item,passando db como param aguardando retorno.
            daoObjReturn = inboundItemDao.addUpdate(io_inbound.getItems(),false,db);
            //Se erro durante insert, dispara exception abortando o processamento.
            if(daoObjReturn.hasError()){
                throw new Exception(daoObjReturn.getRawMessage());
            }
            //
            IO_MoveDao moveDao = new IO_MoveDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
            //
            daoObjReturn = moveDao.addUpdate(io_inbound.getMove(),false,db);
            if(daoObjReturn.hasError()){
                throw new Exception(daoObjReturn.getRawMessage());
            }
            MD_Product_SerialDao serialDao = new MD_Product_SerialDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
            //Tenta insert dos Seriais NÃO POSSUI RETURN....
            if(io_inbound.getSerial() != null && io_inbound.getSerial().size() > 0){
                serialDao.addUpdateTmpByIOProcess(io_inbound.getSerial(),db);
            }
            //
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

    public DaoObjReturn addUpdate(List<IO_Inbound>  io_inbounds, boolean status, SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        IO_Inbound_ItemDao inboundItemDao = new IO_Inbound_ItemDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        IO_MoveDao moveDao = new IO_MoveDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );

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

            for (IO_Inbound io_inbound : io_inbounds) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_inbound.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(INBOUND_PREFIX).append(" = '").append(String.valueOf(io_inbound.getInbound_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(INBOUND_CODE).append(" = '").append(String.valueOf(io_inbound.getInbound_code())).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_inbound), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_inbound));
                }
                //Tenta insert dos itens e avalia return
                daoObjReturn = inboundItemDao.addUpdate(io_inbound.getItems(),false,db);
                if(daoObjReturn.hasError()){
                    throw new Exception(daoObjReturn.getRawMessage());
                }
                //Tenta insert das moves e avalia return
                daoObjReturn = moveDao.addUpdate(io_inbound.getMove(),false,db);
                if(daoObjReturn.hasError()){
                    throw new Exception(daoObjReturn.getRawMessage());
                }
                //Tenta insert dos Seriais NÃO POSSUI RETURN....
                if(io_inbound.getSerial() != null && io_inbound.getSerial().size() > 0){
                    serialDao.addUpdateTmpByIOProcess(io_inbound.getSerial(),db);
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
    public DaoObjReturn addUpdate(List<IO_Inbound>  io_inbounds, boolean status) {
        return addUpdate(io_inbounds, status,null);
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
     * LUCHE - 22/04/2019
     *
     * Metodo criado para executar a INBOUND FULL, agrupando tudo dentro da transaction
     *  as tarefas de , deletar itens, movimentação put_away deletar cabeçalho e finalmente reinserção de
     *  cabeçalho e item
     * @param io_inbounds
     * @return
     */
    public DaoObjReturn processFull(ArrayList<IO_Inbound> io_inbounds){
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.DELETE;
        IO_Inbound_ItemDao itemDao = new IO_Inbound_ItemDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        IO_MoveDao moveDao = new IO_MoveDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            Constant.DB_VERSION_CUSTOM
        );
        //
        openDB();

        try {
            db.beginTransaction();
            for(IO_Inbound io_inbound :io_inbounds) {
                //Onde a magica acontece
                //Tenta o delete do items da inbound
                daoObjReturn = itemDao.remove(io_inbound, db);
                //verifica se erro ao remover itens
                if(daoObjReturn.hasError()){
                    throw new Exception(daoObjReturn.getRawMessage());
                }
                //verifica se erro ao remover moves
                daoObjReturn = moveDao.removeInboundMoves(io_inbound, db);
                //
                if(daoObjReturn.hasError()){
                    throw new Exception(daoObjReturn.getRawMessage());
                }
                //Se sucesso ao deleta itens
                if (!daoObjReturn.hasError()) {
                    curAction = DaoObjReturn.DELETE;
                    //Where para update
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(io_inbound.getCustomer_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(INBOUND_PREFIX).append(" = '").append(io_inbound.getInbound_prefix()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(INBOUND_CODE).append(" = '").append(io_inbound.getInbound_code()).append("'");
                    //Tenta update e armazena retorno
                    addUpdateRet = db.delete(TABLE, sbWhere.toString(), null);
                    //Se deletou e não rolou exception, executa insert
                    //Seta Pk no itens
                    io_inbound.setPK();
                    daoObjReturn = addUpdate(io_inbound, db);
                    //Esse if não teria necessidade, pq se desse merda, ja teria dado exception
                    if (daoObjReturn.hasError()) {
                        throw new Exception(daoObjReturn.getErrorMsg());
                    }

                }
            }
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

    @Override
    public IO_Inbound getByString(String sQuery) {
        IO_Inbound io_inbound = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_inbound = toIO_InboundMapper.map(cursor);
            }
            //
            if(io_inbound != null){
                //Se operação de insert ou update executada com sucesso
                //Segue para inserção dos itens.
                IO_Inbound_ItemDao inboundItemDao = new IO_Inbound_ItemDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );
                //
                io_inbound.setItems(
                        (ArrayList<IO_Inbound_Item>) inboundItemDao.query(
                                new IO_Inbound_Item_Sql_001(
                                        io_inbound.getCustomer_code(),
                                        io_inbound.getInbound_prefix(),
                                        io_inbound.getInbound_code()
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

        return io_inbound;
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
    public List<IO_Inbound> query(String sQuery) {
        List<IO_Inbound> io_inbounds = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                IO_Inbound uAux = toIO_InboundMapper.map(cursor);
                //
                if(uAux != null){
                    //Se operação de insert ou update executada com sucesso
                    //Segue para inserção dos itens.
                    IO_Inbound_ItemDao inboundItemDao = new IO_Inbound_ItemDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );
                    //
                    uAux.setItems(
                            (ArrayList<IO_Inbound_Item>) inboundItemDao.query(
                                    new IO_Inbound_Item_Sql_001(
                                            uAux.getCustomer_code(),
                                            uAux.getInbound_prefix(),
                                            uAux.getInbound_code()
                                    ).toSqlQuery()

                            )
                    );
                }
                //
                io_inbounds.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_inbounds;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> io_inbounds = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_inbounds.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_inbounds;
    }

    private class CursorIO_InboundMapper implements Mapper<Cursor, IO_Inbound> {
        @Override
        public IO_Inbound map(Cursor cursor) {
            IO_Inbound io_inbound = new IO_Inbound();
            //
            io_inbound.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            io_inbound.setInbound_prefix(cursor.getInt(cursor.getColumnIndex(INBOUND_PREFIX)));
            io_inbound.setInbound_code(cursor.getInt(cursor.getColumnIndex(INBOUND_CODE)));
            io_inbound.setInbound_id(cursor.getString(cursor.getColumnIndex(INBOUND_ID)));
            if(cursor.isNull(cursor.getColumnIndex(INBOUND_DESC))){
                io_inbound.setInbound_desc(null);
            }else {
                io_inbound.setInbound_desc(cursor.getString(cursor.getColumnIndex(INBOUND_DESC)));
            }
            io_inbound.setScn(cursor.getInt(cursor.getColumnIndex(SCN)));
            io_inbound.setOrigin(cursor.getString(cursor.getColumnIndex(ORIGIN)));
            if(cursor.isNull(cursor.getColumnIndex(TRANSPORT_ORDER))){
                io_inbound.setTransport_order(null);
            }else {
                io_inbound.setTransport_order(cursor.getString(cursor.getColumnIndex(TRANSPORT_ORDER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(INVOICE_NUMBER))){
                io_inbound.setInvoice_number(null);
            }else {
                io_inbound.setInvoice_number(cursor.getString(cursor.getColumnIndex(INVOICE_NUMBER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(INVOICE_DATE))){
                io_inbound.setInvoice_date(null);
            }else {
                io_inbound.setInvoice_date(cursor.getString(cursor.getColumnIndex(INVOICE_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ETA_DATE))){
                io_inbound.setEta_date(null);
            }else {
                io_inbound.setEta_date(cursor.getString(cursor.getColumnIndex(ETA_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ARRIVAL_DATE))){
                io_inbound.setArrival_date(null);
            }else {
                io_inbound.setArrival_date(cursor.getString(cursor.getColumnIndex(ARRIVAL_DATE)));
            }
            io_inbound.setFrom_type(cursor.getString(cursor.getColumnIndex(FROM_TYPE)));
            if(cursor.isNull(cursor.getColumnIndex(FROM_PARTNER_CODE))){
                io_inbound.setFrom_partner_code(null);
            }else {
                io_inbound.setFrom_partner_code(cursor.getInt(cursor.getColumnIndex(FROM_PARTNER_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(FROM_PARTNER_ID))){
                io_inbound.setFrom_partner_id(null);
            }else {
                io_inbound.setFrom_partner_id(cursor.getString(cursor.getColumnIndex(FROM_PARTNER_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(FROM_PARTNER_DESC))){
                io_inbound.setFrom_partner_desc(null);
            }else {
                io_inbound.setFrom_partner_desc(cursor.getString(cursor.getColumnIndex(FROM_PARTNER_DESC)));
            }
            if(cursor.isNull(cursor.getColumnIndex(FROM_SITE_CODE))){
                io_inbound.setFrom_site_code(null);
            }else {
                io_inbound.setFrom_site_code(cursor.getInt(cursor.getColumnIndex(FROM_SITE_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(FROM_SITE_ID))){
                io_inbound.setFrom_site_id(null);
            }else {
                io_inbound.setFrom_site_id(cursor.getString(cursor.getColumnIndex(FROM_SITE_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(FROM_SITE_DESC))){
                io_inbound.setFrom_site_desc(null);
            }else {
                io_inbound.setFrom_site_desc(cursor.getString(cursor.getColumnIndex(FROM_SITE_DESC)));
            }
            io_inbound.setTo_site_code(cursor.getInt(cursor.getColumnIndex(TO_SITE_CODE)));
            if(cursor.isNull(cursor.getColumnIndex(CARRIER_CODE))){
                io_inbound.setCarrier_code(null);
            }else {
                io_inbound.setCarrier_code(cursor.getInt(cursor.getColumnIndex(CARRIER_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CARRIER_ID))){
                io_inbound.setCarrier_id(null);
            }else {
                io_inbound.setCarrier_id(cursor.getString(cursor.getColumnIndex(CARRIER_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CARRIER_DESC))){
                io_inbound.setCarrier_desc(null);
            }else {
                io_inbound.setCarrier_desc(cursor.getString(cursor.getColumnIndex(CARRIER_DESC)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TRUCK_NUMBER))){
                io_inbound.setTruck_number(null);
            }else {
                io_inbound.setTruck_number(cursor.getString(cursor.getColumnIndex(TRUCK_NUMBER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(DRIVER))){
                io_inbound.setDriver(null);
            }else {
                io_inbound.setDriver(cursor.getString(cursor.getColumnIndex(DRIVER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(COMMENTS))){
                io_inbound.setComments(null);
            }else {
                io_inbound.setComments(cursor.getString(cursor.getColumnIndex(COMMENTS)));
            }
            io_inbound.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            if(cursor.isNull(cursor.getColumnIndex(PERC_DONE))){
                io_inbound.setPerc_done(null);
            }else {
                io_inbound.setPerc_done(cursor.getDouble(cursor.getColumnIndex(PERC_DONE)));
            }
            io_inbound.setInbound_auto_seq(cursor.getInt(cursor.getColumnIndex(INBOUND_AUTO_SEQ)));
            if(cursor.isNull(cursor.getColumnIndex(MODAL_CODE))){
                io_inbound.setModal_code(null);
            }else {
                io_inbound.setModal_code(cursor.getInt(cursor.getColumnIndex(MODAL_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(MODAL_ID))){
                io_inbound.setModal_id(null);
            }else {
                io_inbound.setModal_id(cursor.getString(cursor.getColumnIndex(MODAL_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(MODAL_DESC))){
                io_inbound.setModal_desc(null);
            }else {
                io_inbound.setModal_desc(cursor.getString(cursor.getColumnIndex(MODAL_DESC)));
            }
            io_inbound.setAllow_new_item(cursor.getInt(cursor.getColumnIndex(ALLOW_NEW_ITEM)));
            if(cursor.isNull(cursor.getColumnIndex(ZONE_CODE_CONF))){
                io_inbound.setZone_code_conf(null);
            }else {
                io_inbound.setZone_code_conf(cursor.getInt(cursor.getColumnIndex(ZONE_CODE_CONF)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ZONE_ID_CONF))){
                io_inbound.setZone_id_conf(null);
            }else {
                io_inbound.setZone_id_conf(cursor.getString(cursor.getColumnIndex(ZONE_ID_CONF)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ZONE_DESC_CONF))){
                io_inbound.setZone_desc_conf(null);
            }else {
                io_inbound.setZone_desc_conf(cursor.getString(cursor.getColumnIndex(ZONE_DESC_CONF)));
            }
            if(cursor.isNull(cursor.getColumnIndex(LOCAL_CODE_CONF))){
                io_inbound.setLocal_code_conf(null);
            }else {
                io_inbound.setLocal_code_conf(cursor.getInt(cursor.getColumnIndex(LOCAL_CODE_CONF)));
            }
            if(cursor.isNull(cursor.getColumnIndex(LOCAL_ID_CONF))){
                io_inbound.setLocal_id_conf(null);
            }else {
                io_inbound.setLocal_id_conf(cursor.getString(cursor.getColumnIndex(LOCAL_ID_CONF)));
            }
            io_inbound.setPut_away_process(cursor.getInt(cursor.getColumnIndex(PUT_AWAY_PROCESS)));
            io_inbound.setDone_automatic(cursor.getInt(cursor.getColumnIndex(DONE_AUTOMATIC)));
            if (cursor.isNull(cursor.getColumnIndex(UPDATE_REQUIRED))) {
                io_inbound.setUpdate_required(0);
            } else {
                io_inbound.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SYNC_REQUIRED))) {
                io_inbound.setSync_required(0);
            } else {
                io_inbound.setSync_required(cursor.getInt(cursor.getColumnIndex(SYNC_REQUIRED)));
            }
            io_inbound.setToken(cursor.getString(cursor.getColumnIndex(TOKEN)));
            //
            return io_inbound;
        }
    }

    private class IO_InboundToContentValuesMapper implements Mapper<IO_Inbound, ContentValues> {
        @Override
        public ContentValues map(IO_Inbound io_inbound) {
            ContentValues contentValues = new ContentValues();
            //
            if(io_inbound.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,io_inbound.getCustomer_code());
            }
            if(io_inbound.getInbound_prefix() > -1){
                contentValues.put(INBOUND_PREFIX,io_inbound.getInbound_prefix());
            }
            if(io_inbound.getInbound_code() > -1){
                contentValues.put(INBOUND_CODE,io_inbound.getInbound_code());
            }
            if(io_inbound.getInbound_id() != null){
                contentValues.put(INBOUND_ID,io_inbound.getInbound_id());
            }
            contentValues.put(INBOUND_DESC,io_inbound.getInbound_desc());
            if(io_inbound.getScn() > -1){
                contentValues.put(SCN,io_inbound.getScn());
            }
            if(io_inbound.getOrigin() != null){
                contentValues.put(ORIGIN,io_inbound.getOrigin());
            }
            contentValues.put(TRANSPORT_ORDER,io_inbound.getTransport_order());
            contentValues.put(INVOICE_NUMBER,io_inbound.getInvoice_number());
            contentValues.put(INVOICE_DATE,io_inbound.getInvoice_date());
            contentValues.put(ETA_DATE,io_inbound.getEta_date());
            contentValues.put(ARRIVAL_DATE,io_inbound.getArrival_date());
            if(io_inbound.getFrom_type() != null){
                contentValues.put(FROM_TYPE,io_inbound.getFrom_type());
            }
            contentValues.put(FROM_PARTNER_CODE,io_inbound.getFrom_partner_code());
            contentValues.put(FROM_PARTNER_ID,io_inbound.getFrom_partner_id());
            contentValues.put(FROM_PARTNER_DESC,io_inbound.getFrom_partner_desc());
            contentValues.put(FROM_SITE_CODE,io_inbound.getFrom_site_code());
            contentValues.put(FROM_SITE_ID,io_inbound.getFrom_site_id());
            contentValues.put(FROM_SITE_DESC,io_inbound.getFrom_site_desc());
            if(io_inbound.getTo_site_code() > -1){
                contentValues.put(TO_SITE_CODE,io_inbound.getTo_site_code());
            }
            contentValues.put(CARRIER_CODE,io_inbound.getCarrier_code());
            contentValues.put(CARRIER_ID,io_inbound.getCarrier_id());
            contentValues.put(CARRIER_DESC,io_inbound.getCarrier_desc());
            contentValues.put(TRUCK_NUMBER,io_inbound.getTruck_number());
            contentValues.put(DRIVER,io_inbound.getDriver());
            contentValues.put(COMMENTS,io_inbound.getComments());
            if(io_inbound.getStatus() != null){
                contentValues.put(STATUS,io_inbound.getStatus());
            }
            contentValues.put(PERC_DONE,io_inbound.getPerc_done());
            if(io_inbound.getInbound_auto_seq() > -1){
                contentValues.put(INBOUND_AUTO_SEQ,io_inbound.getInbound_auto_seq());
            }
            contentValues.put(MODAL_CODE,io_inbound.getModal_code());
            contentValues.put(MODAL_ID,io_inbound.getModal_id());
            contentValues.put(MODAL_DESC,io_inbound.getModal_desc());
            if(io_inbound.getAllow_new_item() > -1){
                contentValues.put(ALLOW_NEW_ITEM,io_inbound.getAllow_new_item());
            }
            contentValues.put(ZONE_CODE_CONF,io_inbound.getZone_code_conf());
            contentValues.put(ZONE_ID_CONF,io_inbound.getZone_id_conf());
            contentValues.put(ZONE_DESC_CONF,io_inbound.getZone_desc_conf());
            contentValues.put(LOCAL_CODE_CONF,io_inbound.getLocal_code_conf());
            contentValues.put(LOCAL_ID_CONF,io_inbound.getLocal_id_conf());
            if(io_inbound.getPut_away_process() > -1){
                contentValues.put(PUT_AWAY_PROCESS,io_inbound.getPut_away_process());
            }
            if(io_inbound.getDone_automatic() > -1){
                contentValues.put(DONE_AUTOMATIC,io_inbound.getDone_automatic());
            }
            if (io_inbound.getUpdate_required() > -1) {
                contentValues.put(UPDATE_REQUIRED, io_inbound.getUpdate_required());
            }
            /**
             * Atualizar somente via query update para evitar sobreposicao com o update da Inbound.
             * Atualiza com 0 quando inbound Full e para um através do recebimento do GCM
             */
//            if (io_inbound.getSync_required() > -1) {
//                contentValues.put(SYNC_REQUIRED, io_inbound.getSync_required());
//            }
            if (io_inbound.getToken() != null) {
                contentValues.put(TOKEN, io_inbound.getToken());
            }

            //
            return contentValues;
        }
    }
}
