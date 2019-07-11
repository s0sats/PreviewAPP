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
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.IO_Outbound_Item;
import com.namoadigital.prj001.sql.IO_Outbound_Item_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class IO_OutboundDao extends BaseDao implements DaoWithReturn<IO_Outbound> {

    private final Mapper<IO_Outbound, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, IO_Outbound> toIO_OutboundMapper;

    public static final String TABLE = "io_outbound";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String OUTBOUND_PREFIX = "outbound_prefix";
    public static final String OUTBOUND_CODE = "outbound_code";
    public static final String OUTBOUND_DESC = "outbound_desc";
    public static final String OUTBOUND_ID = "outbound_id";
    public static final String SCN = "scn";
    public static final String ORIGIN = "origin";
    public static final String TRANSPORT_ORDER = "transport_order";
    public static final String INVOICE_NUMBER = "invoice_number";
    public static final String INVOICE_DATE = "invoice_date";
    public static final String ETA_DATE = "eta_date";
    public static final String DEPARTURE_DATE = "departure_date";
    public static final String LOADING_DATE = "loading_date";
    public static final String FROM_SITE_CODE = "from_site_code";
    public static final String TO_TYPE = "to_type";
    public static final String TO_PARTNER_CODE = "to_partner_code";
    public static final String TO_PARTNER_ID = "to_partner_id";
    public static final String TO_PARTNER_DESC = "to_partner_desc";
    public static final String TO_SITE_CODE = "to_site_code";
    public static final String TO_SITE_ID = "to_site_id";
    public static final String TO_SITE_DESC = "to_site_desc";
    public static final String CARRIER_CODE = "carrier_code";
    public static final String CARRIER_ID = "carrier_id";
    public static final String CARRIER_DESC = "carrier_desc";
    public static final String TRUCK_NUMBER = "truck_number";
    public static final String DRIVER = "driver";
    public static final String COMMENTS = "comments";
    public static final String STATUS = "status";
    public static final String PERC_DONE = "perc_done";
    public static final String MODAL_CODE = "modal_code";
    public static final String MODAL_ID = "modal_id";
    public static final String MODAL_DESC = "modal_desc";
    public static final String ALLOW_NEW_ITEM = "allow_new_item";
    public static final String ZONE_CODE_PICKING = "zone_code_picking";
    public static final String ZONE_ID_PICKING = "zone_id_picking";
    public static final String ZONE_DESC_PICKING = "zone_desc_picking";
    public static final String LOCAL_CODE_PICKING = "local_code_picking";
    public static final String LOCAL_ID_PICKING = "local_id_picking";
    public static final String PICKING_PROCESS = "picking_process";
    public static final String DONE_AUTOMATIC = "done_automatic";
    public static final String UPDATE_REQUIRED = "update_required";
    public static final String SYNC_REQUIRED = "sync_required";
    public static final String TOKEN = "token";


    public IO_OutboundDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new IO_OutboundToContentValuesMapper();
        this.toIO_OutboundMapper = new CursorToIO_OutboundMapper();
    }

    @Override
    public DaoObjReturn addUpdate(IO_Outbound io_outbound) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        openDB();

        try{
            db.beginTransaction();
            //
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_outbound.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_PREFIX).append(" = '").append(String.valueOf(io_outbound.getOutbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_CODE).append(" = '").append(String.valueOf(io_outbound.getOutbound_code())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_outbound), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_outbound));
            }
            //Se operação de insert ou update executada com sucesso
            //Segue para inserção dos itens.
            IO_Outbound_ItemDao ioOutboundItemDao = new IO_Outbound_ItemDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //Chama insertUpdate de lista de item,passando db como param aguardando retorno.
            DaoObjReturn outboundItemRet = ioOutboundItemDao.addUpdate(io_outbound.getItems(),false,db);
            //Se erro durante insert, dispara exception abortando o processamento.
            if(outboundItemRet.hasError()){
                throw new Exception(outboundItemRet.getErrorMsg());
            }
            //
            db.setTransactionSuccessful();
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
            db.endTransaction();
            daoObjReturn.setAction(curAction);
            daoObjReturn.setActionReturn(addUpdateRet);
        }

        closeDB();

        return daoObjReturn;
    }

    public DaoObjReturn addUpdate(IO_Outbound io_outbound, SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_outbound.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_PREFIX).append(" = '").append(String.valueOf(io_outbound.getOutbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_CODE).append(" = '").append(String.valueOf(io_outbound.getOutbound_code())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_outbound), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_outbound));
            }
            //Se operação de insert ou update executada com sucesso
            //Segue para inserção dos itens.
            IO_Outbound_ItemDao outboundItemDao = new IO_Outbound_ItemDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //Chama insertUpdate de lista de item,passando db como param aguardando retorno.
            daoObjReturn = outboundItemDao.addUpdate(io_outbound.getItems(),false,db);
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
            daoObjReturn = moveDao.addUpdate(io_outbound.getMove(),false,db);
            if(daoObjReturn.hasError()){
                throw new Exception(daoObjReturn.getRawMessage());
            }
            MD_Product_SerialDao serialDao = new MD_Product_SerialDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //Tenta insert dos Seriais NÃO POSSUI RETURN....
            if(io_outbound.getSerial() != null && io_outbound.getSerial().size() > 0){
                serialDao.addUpdateTmpByIOProcess(io_outbound.getSerial(),db);
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

    @Override
    public DaoObjReturn addUpdate(List<IO_Outbound> io_outbounds, boolean status) {
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

            for (IO_Outbound io_outbound : io_outbounds) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_outbound.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(OUTBOUND_PREFIX).append(" = '").append(String.valueOf(io_outbound.getOutbound_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(OUTBOUND_CODE).append(" = '").append(String.valueOf(io_outbound.getOutbound_code())).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_outbound), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_outbound));
                }
                //Se operação de insert ou update executada com sucesso
                //Segue para inserção dos itens.
                IO_Outbound_ItemDao ioOutboundItemDao = new IO_Outbound_ItemDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );
                //Chama insertUpdate de lista de item,passando db como param aguardando retorno.
                DaoObjReturn outboundItemRet = ioOutboundItemDao.addUpdate(io_outbound.getItems(),false,db);
                //Se erro durante insert, dispara exception abortando o processamento.
                if(outboundItemRet.hasError()){
                    throw new Exception(outboundItemRet.getErrorMsg());
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

        closeDB();

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
    public IO_Outbound getByString(String sQuery) {
        IO_Outbound io_outbound = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_outbound = toIO_OutboundMapper.map(cursor);
            }
            //
            if(io_outbound != null){
                //Se operação de insert ou update executada com sucesso
                //Segue para inserção dos itens.
                IO_Outbound_ItemDao ioOutboundItemDao = new IO_Outbound_ItemDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );
                //
                io_outbound.setItems(
                        (ArrayList<IO_Outbound_Item>) ioOutboundItemDao.query(
                                new IO_Outbound_Item_Sql_001(
                                        io_outbound.getCustomer_code(),
                                        io_outbound.getOutbound_prefix(),
                                        io_outbound.getOutbound_code()
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

        return io_outbound;
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
    public List<IO_Outbound> query(String sQuery) {
        List<IO_Outbound> io_outbounds = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                IO_Outbound uAux = toIO_OutboundMapper.map(cursor);
                //
                if(uAux != null){
                    //Se operação de insert ou update executada com sucesso
                    //Segue para inserção dos itens.
                    IO_Outbound_ItemDao ioOutboundItemDao = new IO_Outbound_ItemDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );
                    //
                    uAux.setItems(
                            (ArrayList<IO_Outbound_Item>) ioOutboundItemDao.query(
                                    new IO_Outbound_Item_Sql_001(
                                            uAux.getCustomer_code(),
                                            uAux.getOutbound_prefix(),
                                            uAux.getOutbound_code()
                                    ).toSqlQuery()

                            )
                    );
                }
                //
                io_outbounds.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_outbounds;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> io_outbounds = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_outbounds.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_outbounds;
    }

    private class CursorToIO_OutboundMapper implements Mapper<Cursor, IO_Outbound> {
        @Override
        public IO_Outbound map(Cursor cursor) {
            IO_Outbound io_outbound = new IO_Outbound();
            //
            io_outbound.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            io_outbound.setOutbound_prefix(cursor.getInt(cursor.getColumnIndex(OUTBOUND_PREFIX)));
            io_outbound.setOutbound_code(cursor.getInt(cursor.getColumnIndex(OUTBOUND_CODE)));
            io_outbound.setOutbound_id(cursor.getString(cursor.getColumnIndex(OUTBOUND_ID)));
            if(cursor.isNull(cursor.getColumnIndex(OUTBOUND_DESC))){
                io_outbound.setOutbound_desc(null);
            }else{
                io_outbound.setOutbound_desc(cursor.getString(cursor.getColumnIndex(OUTBOUND_DESC)));
            }
            io_outbound.setScn(cursor.getInt(cursor.getColumnIndex(SCN)));
            io_outbound.setOrigin(cursor.getString(cursor.getColumnIndex(ORIGIN)));
            if(cursor.isNull(cursor.getColumnIndex(TRANSPORT_ORDER))){
                io_outbound.setTransport_order(null);
            }else{
                io_outbound.setTransport_order(cursor.getString(cursor.getColumnIndex(TRANSPORT_ORDER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(INVOICE_NUMBER))){
                io_outbound.setInvoice_number(null);
            }else{
                io_outbound.setInvoice_number(cursor.getString(cursor.getColumnIndex(INVOICE_NUMBER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(INVOICE_DATE))){
                io_outbound.setInvoice_date(null);
            }else{
                io_outbound.setInvoice_date(cursor.getString(cursor.getColumnIndex(INVOICE_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ETA_DATE))){
                io_outbound.setEta_date(null);
            }else{
                io_outbound.setEta_date(cursor.getString(cursor.getColumnIndex(ETA_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(DEPARTURE_DATE))){
                io_outbound.setDeparture_date(null);
            }else{
                io_outbound.setDeparture_date(cursor.getString(cursor.getColumnIndex(DEPARTURE_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(LOADING_DATE))){
                io_outbound.setLoading_date(null);
            }else{
                io_outbound.setLoading_date(cursor.getString(cursor.getColumnIndex(LOADING_DATE)));
            }
            io_outbound.setFrom_site_code(cursor.getInt(cursor.getColumnIndex(FROM_SITE_CODE)));
            io_outbound.setTo_type(cursor.getString(cursor.getColumnIndex(TO_TYPE)));
            if(cursor.isNull(cursor.getColumnIndex(TO_PARTNER_CODE))){
                io_outbound.setTo_partner_code(null);
            }else{
                io_outbound.setTo_partner_code(cursor.getInt(cursor.getColumnIndex(TO_PARTNER_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TO_PARTNER_ID))){
                io_outbound.setTo_partner_id(null);
            }else{
                io_outbound.setTo_partner_id(cursor.getString(cursor.getColumnIndex(TO_PARTNER_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TO_PARTNER_DESC))){
                io_outbound.setTo_partner_desc(null);
            }else{
                io_outbound.setTo_partner_desc(cursor.getString(cursor.getColumnIndex(TO_PARTNER_DESC)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TO_SITE_CODE))){
                io_outbound.setTo_site_code(null);
            }else{
                io_outbound.setTo_site_code(cursor.getInt(cursor.getColumnIndex(TO_SITE_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TO_SITE_ID))){
                io_outbound.setTo_site_id(null);
            }else{
                io_outbound.setTo_site_id(cursor.getString(cursor.getColumnIndex(TO_SITE_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TO_SITE_DESC))){
                io_outbound.setTo_site_desc(null);
            }else{
                io_outbound.setTo_site_desc(cursor.getString(cursor.getColumnIndex(TO_SITE_DESC)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CARRIER_CODE))){
                io_outbound.setCarrier_code(null);
            }else{
                io_outbound.setCarrier_code(cursor.getInt(cursor.getColumnIndex(CARRIER_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CARRIER_ID))){
                io_outbound.setCarrier_id(null);
            }else{
                io_outbound.setCarrier_id(cursor.getString(cursor.getColumnIndex(CARRIER_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CARRIER_DESC))){
                io_outbound.setCarrier_desc(null);
            }else{
                io_outbound.setCarrier_desc(cursor.getString(cursor.getColumnIndex(CARRIER_DESC)));
            }
            if(cursor.isNull(cursor.getColumnIndex(TRUCK_NUMBER))){
                io_outbound.setTruck_number(null);
            }else{
                io_outbound.setTruck_number(cursor.getString(cursor.getColumnIndex(TRUCK_NUMBER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(DRIVER))){
                io_outbound.setDriver(null);
            }else{
                io_outbound.setDriver(cursor.getString(cursor.getColumnIndex(DRIVER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(COMMENTS))){
                io_outbound.setComments(null);
            }else{
                io_outbound.setComments(cursor.getString(cursor.getColumnIndex(COMMENTS)));
            }
            io_outbound.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            if(cursor.isNull(cursor.getColumnIndex(PERC_DONE))){
                io_outbound.setPerc_done(null);
            }else {
                io_outbound.setPerc_done(cursor.getDouble(cursor.getColumnIndex(PERC_DONE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(MODAL_CODE))){
                io_outbound.setModal_code(null);
            }else{
                io_outbound.setModal_code(cursor.getInt(cursor.getColumnIndex(MODAL_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(MODAL_ID))){
                io_outbound.setModal_id(null);
            }else {
                io_outbound.setModal_id(cursor.getString(cursor.getColumnIndex(MODAL_ID)));
            }
            if(cursor.isNull(cursor.getColumnIndex(MODAL_DESC))){
                io_outbound.setModal_desc(null);
            }else {
                io_outbound.setModal_desc(cursor.getString(cursor.getColumnIndex(MODAL_DESC)));
            }
            io_outbound.setAllow_new_item(cursor.getInt(cursor.getColumnIndex(ALLOW_NEW_ITEM)));
            if(cursor.isNull(cursor.getColumnIndex(ZONE_CODE_PICKING))){
                io_outbound.setZone_code_picking(null);
            }else{
                io_outbound.setZone_code_picking(cursor.getInt(cursor.getColumnIndex(ZONE_CODE_PICKING)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ZONE_ID_PICKING))){
                io_outbound.setZone_id_picking(null);
            }else{
                io_outbound.setZone_id_picking(cursor.getString(cursor.getColumnIndex(ZONE_ID_PICKING)));
            }
            if(cursor.isNull(cursor.getColumnIndex(ZONE_DESC_PICKING))){
                io_outbound.setZone_desc_picking(null);
            }else{
                io_outbound.setZone_desc_picking(cursor.getString(cursor.getColumnIndex(ZONE_DESC_PICKING)));
            }
            if(cursor.isNull(cursor.getColumnIndex(LOCAL_CODE_PICKING))){
                io_outbound.setLocal_code_picking(null);
            }else{
                io_outbound.setLocal_code_picking(cursor.getInt(cursor.getColumnIndex(LOCAL_CODE_PICKING)));
            }
            if(cursor.isNull(cursor.getColumnIndex(LOCAL_ID_PICKING))){
                io_outbound.setLocal_id_picking(null);
            }else{
                io_outbound.setLocal_id_picking(cursor.getString(cursor.getColumnIndex(LOCAL_ID_PICKING)));
            }
            io_outbound.setPicking_process(cursor.getInt(cursor.getColumnIndex(PICKING_PROCESS)));
            io_outbound.setDone_automatic(cursor.getInt(cursor.getColumnIndex(DONE_AUTOMATIC)));
            if (cursor.isNull(cursor.getColumnIndex(UPDATE_REQUIRED))) {
                io_outbound.setUpdate_required(0);
            } else {
                io_outbound.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SYNC_REQUIRED))) {
                io_outbound.setSync_required(0);
            } else {
                io_outbound.setSync_required(cursor.getInt(cursor.getColumnIndex(SYNC_REQUIRED)));
            }
            io_outbound.setToken(cursor.getString(cursor.getColumnIndex(TOKEN)));
            //
            return io_outbound;
        }
    }

    private class IO_OutboundToContentValuesMapper implements Mapper<IO_Outbound, ContentValues> {
        @Override
        public ContentValues map(IO_Outbound io_outbound) {
            ContentValues contentValues = new ContentValues();
            //
            if(io_outbound.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,io_outbound.getCustomer_code());
            }
            if(io_outbound.getOutbound_prefix() > -1){
                contentValues.put(OUTBOUND_PREFIX,io_outbound.getOutbound_prefix());
            }
            if(io_outbound.getOutbound_code() > -1){
                contentValues.put(OUTBOUND_CODE,io_outbound.getOutbound_code());
            }
            if(io_outbound.getOutbound_id() != null){
                contentValues.put(OUTBOUND_ID ,io_outbound.getOutbound_id());
            }
            contentValues.put(OUTBOUND_DESC,io_outbound.getOutbound_desc());
            if(io_outbound.getScn() > -1){
                contentValues.put(SCN,io_outbound.getScn());
            }
            if(io_outbound.getOrigin() != null){
                contentValues.put(ORIGIN ,io_outbound.getOrigin());
            }
            contentValues.put(TRANSPORT_ORDER ,io_outbound.getTransport_order());
            contentValues.put(INVOICE_NUMBER ,io_outbound.getInvoice_number());
            contentValues.put(INVOICE_DATE ,io_outbound.getInvoice_date());
            contentValues.put(ETA_DATE ,io_outbound.getEta_date());
            contentValues.put(DEPARTURE_DATE ,io_outbound.getDeparture_date());
            contentValues.put(LOADING_DATE ,io_outbound.getLoading_date());
            if(io_outbound.getFrom_site_code() > -1){
                contentValues.put(FROM_SITE_CODE,io_outbound.getFrom_site_code());
            }
            if(io_outbound.getTo_type() != null){
                contentValues.put(TO_TYPE ,io_outbound.getTo_type());
            }
            contentValues.put(TO_PARTNER_CODE ,io_outbound.getTo_partner_code());
            contentValues.put(TO_PARTNER_ID ,io_outbound.getTo_partner_id());
            contentValues.put(TO_PARTNER_DESC ,io_outbound.getTo_partner_desc());
            contentValues.put(TO_SITE_CODE ,io_outbound.getTo_site_code());
            contentValues.put(TO_SITE_ID ,io_outbound.getTo_site_id());
            contentValues.put(TO_SITE_DESC ,io_outbound.getTo_site_desc());
            contentValues.put(CARRIER_CODE ,io_outbound.getCarrier_code());
            contentValues.put(CARRIER_ID ,io_outbound.getCarrier_id());
            contentValues.put(CARRIER_DESC ,io_outbound.getCarrier_desc());
            contentValues.put(TRUCK_NUMBER ,io_outbound.getTruck_number());
            contentValues.put(DRIVER ,io_outbound.getDriver());
            contentValues.put(COMMENTS ,io_outbound.getComments());
            if(io_outbound.getStatus() != null){
                contentValues.put(STATUS ,io_outbound.getStatus());
            }
            contentValues.put(PERC_DONE ,io_outbound.getPerc_done());
            contentValues.put(MODAL_CODE ,io_outbound.getModal_code());
            contentValues.put(MODAL_ID ,io_outbound.getModal_id());
            contentValues.put(MODAL_DESC ,io_outbound.getModal_desc());
            if(io_outbound.getAllow_new_item() > -1){
                contentValues.put(ALLOW_NEW_ITEM ,io_outbound.getAllow_new_item());
            }
            contentValues.put(ZONE_CODE_PICKING ,io_outbound.getZone_code_picking());
            contentValues.put(ZONE_ID_PICKING ,io_outbound.getZone_id_picking());
            contentValues.put(ZONE_DESC_PICKING,io_outbound.getZone_desc_picking());

            contentValues.put(LOCAL_CODE_PICKING ,io_outbound.getLocal_code_picking());
            contentValues.put(LOCAL_ID_PICKING ,io_outbound.getLocal_id_picking());

            if(io_outbound.getPicking_process () > -1){
                contentValues.put(PICKING_PROCESS ,io_outbound.getPicking_process());
            }
            if(io_outbound.getDone_automatic() > -1){
                contentValues.put(DONE_AUTOMATIC ,io_outbound.getDone_automatic());
            }
            if (io_outbound.getUpdate_required() > -1) {
                contentValues.put(UPDATE_REQUIRED, io_outbound.getUpdate_required());
            }
            /**
             * Atualizar somente via query update para evitar sobreposicao com o update da Outbound.
             * Atualiza com 0 quando Outbound Full e para um através do recebimento do GCM
             */
//            if (io_outbound.getSync_required() > -1) {
//                contentValues.put(SYNC_REQUIRED, io_outbound.getSync_required());
//            }

            if (io_outbound.getToken() != null) {
                contentValues.put(TOKEN, io_outbound.getToken());
            }
            //
            return contentValues;
        }
    }

    /**
     * BARRIONUEVO - 03/07/2019
     *
     * Metodo criado para executar a OUTBOUND FULL, agrupando tudo dentro da transaction
     *  as tarefas de , deletar itens, movimentação picking deletar cabeçalho e finalmente reinserção de
     *  cabeçalho e item
     * @param io_outbounds
     * @return
     */
    public DaoObjReturn processFull(ArrayList<IO_Outbound> io_outbounds){
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.DELETE;
        IO_Outbound_ItemDao itemDao = new IO_Outbound_ItemDao(
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
            for(IO_Outbound io_outbound :io_outbounds) {
                //Onde a magica acontece
                //Tenta o delete do items da outbound
                daoObjReturn = itemDao.remove(io_outbound, db);
                //verifica se erro ao remover itens
                if(daoObjReturn.hasError()){
                    throw new Exception(daoObjReturn.getRawMessage());
                }
                //verifica se erro ao remover moves
                daoObjReturn = moveDao.removeOutboundMoves(io_outbound, db);
                //
                if(daoObjReturn.hasError()){
                    throw new Exception(daoObjReturn.getRawMessage());
                }
                //Se sucesso ao deleta itens
                if (!daoObjReturn.hasError()) {
                    curAction = DaoObjReturn.DELETE;
                    //Where para update
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(io_outbound.getCustomer_code()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(OUTBOUND_PREFIX).append(" = '").append(io_outbound.getOutbound_prefix()).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(OUTBOUND_CODE).append(" = '").append(io_outbound.getOutbound_code()).append("'");
                    //Tenta update e armazena retorno
                    addUpdateRet = db.delete(TABLE, sbWhere.toString(), null);
                    //Se deletou e não rolou exception, executa insert
                    //Seta Pk no itens
                    io_outbound.setPK();
                    daoObjReturn = addUpdate(io_outbound, db);
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


}
