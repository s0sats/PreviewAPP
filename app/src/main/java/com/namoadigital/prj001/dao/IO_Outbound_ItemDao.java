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
import com.namoadigital.prj001.model.IO_Outbound;
import com.namoadigital.prj001.model.IO_Outbound_Item;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class IO_Outbound_ItemDao extends BaseDao implements DaoWithReturn<IO_Outbound_Item>, DaoWithReturnSharedDbInstance<IO_Outbound_Item> {

    private final Mapper<Cursor, IO_Outbound_Item> toIO_Outbound_ItemMapper;
    private final Mapper<IO_Outbound_Item, ContentValues> toContentValuesMapper;

    public static final String TABLE = "io_outbound_item";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String OUTBOUND_PREFIX = "outbound_prefix";
    public static final String OUTBOUND_CODE = "outbound_code";
    public static final String OUTBOUND_ITEM = "outbound_item";
    public static final String PRODUCT_CODE = "product_code";
    public static final String SERIAL_CODE = "serial_code";
    public static final String CONF_DATE = "conf_date";
    public static final String STATUS = "status";
    public static final String INBOUND_PREFIX = "inbound_prefix";
    public static final String INBOUND_CODE = "inbound_code";
    public static final String INBOUND_ITEM = "inbound_item";
    public static final String COMMENTS = "comments";
    public static final String SAVE_DATE = "save_date";
    public static final String UPDATE_REQUIRED = "update_required";


    public IO_Outbound_ItemDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toIO_Outbound_ItemMapper = new CursorToIO_Outbound_ItemMapper();
        this.toContentValuesMapper = new IO_Outbound_ItemToContentValuesMapper();
    }

    @Override
    public DaoObjReturn addUpdate(IO_Outbound_Item io_outbound_item) {
        return addUpdate(io_outbound_item,null);
    }

    /**
     * Metoodo utilizando nova metodologia com objeto de retorno e compartilhamento de instance do db
     *
     * @param io_outbound_item
     * @param dbInstance
     * @return
     */
    @Override
    public DaoObjReturn addUpdate(IO_Outbound_Item io_outbound_item , SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_outbound_item.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_PREFIX).append(" = '").append(String.valueOf(io_outbound_item.getOutbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_CODE).append(" = '").append(String.valueOf(io_outbound_item.getOutbound_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_ITEM).append(" = '").append(String.valueOf(io_outbound_item.getOutbound_item())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_outbound_item), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_outbound_item));
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
    public DaoObjReturn addUpdate(List<IO_Outbound_Item> io_outbound_items , boolean status) {
        return addUpdate(io_outbound_items,status,null);
    }

    /**
     * Metoodo utilizando nova metodologia com objeto de retorno e compartilhamento de instance do db
     *
     * @param io_outbound_items
     * @param status
     * @param dbInstance
     * @return
     */
    @Override
    public DaoObjReturn addUpdate(List<IO_Outbound_Item> io_outbound_items , boolean status, SQLiteDatabase dbInstance) {
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

            for (IO_Outbound_Item io_outbound_item : io_outbound_items) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_outbound_item.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(OUTBOUND_PREFIX).append(" = '").append(String.valueOf(io_outbound_item.getOutbound_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(OUTBOUND_CODE).append(" = '").append(String.valueOf(io_outbound_item.getOutbound_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(OUTBOUND_ITEM).append(" = '").append(String.valueOf(io_outbound_item.getOutbound_item())).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_outbound_item), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_outbound_item));
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
     * Remove itens baseado no cabeçalho
     * @param io_outbound_item
     * @param dbInstance
     * @return
     */
    @Override
    public DaoObjReturn remove(IO_Outbound_Item io_outbound_item , @Nullable SQLiteDatabase dbInstance) {
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
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_outbound_item.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_PREFIX).append(" = '").append(String.valueOf(io_outbound_item.getOutbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_CODE).append(" = '").append(String.valueOf(io_outbound_item.getOutbound_code())).append("'");
//            sbWhere.append(" and ");
//            sbWhere.append(OUTBOUND_ITEM).append(" = '").append(String.valueOf(io_outbound_item.getOutbound_item())).append("'");
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
     * @param io_outbound
     * @param dbInstance
     * @return
     */
    public DaoObjReturn remove(IO_Outbound io_outbound, @Nullable SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_outbound.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_PREFIX).append(" = '").append(String.valueOf(io_outbound.getOutbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(OUTBOUND_CODE).append(" = '").append(String.valueOf(io_outbound.getOutbound_code())).append("'");
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
    public IO_Outbound_Item getByString(String sQuery) {
        IO_Outbound_Item io_outbound_item = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_outbound_item = toIO_Outbound_ItemMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_outbound_item;
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
    public List<IO_Outbound_Item> query(String sQuery) {
        List<IO_Outbound_Item> io_outbound_items = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                IO_Outbound_Item uAux = toIO_Outbound_ItemMapper.map(cursor);
                io_outbound_items.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return io_outbound_items;
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

    private class CursorToIO_Outbound_ItemMapper implements Mapper<Cursor, IO_Outbound_Item> {
        @Override
        public IO_Outbound_Item map(Cursor cursor) {
            IO_Outbound_Item io_outbound_item = new IO_Outbound_Item();
            //
            io_outbound_item.setCustomer_code(cursor.getInt(cursor.getColumnIndex(CUSTOMER_CODE)));
            io_outbound_item.setOutbound_prefix(cursor.getInt(cursor.getColumnIndex(OUTBOUND_PREFIX)));
            io_outbound_item.setOutbound_code(cursor.getInt(cursor.getColumnIndex(OUTBOUND_CODE)));
            io_outbound_item.setOutbound_item(cursor.getInt(cursor.getColumnIndex(OUTBOUND_ITEM)));
            io_outbound_item.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
            io_outbound_item.setSerial_code(cursor.getInt(cursor.getColumnIndex(SERIAL_CODE)));
            if (cursor.isNull(cursor.getColumnIndex(CONF_DATE))) {
                io_outbound_item.setConf_date(null);
            } else {
                io_outbound_item.setConf_date(cursor.getString(cursor.getColumnIndex(CONF_DATE)));
            }
            io_outbound_item.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            if (cursor.isNull(cursor.getColumnIndex(INBOUND_PREFIX))) {
                io_outbound_item.setInbound_prefix(null);
            } else {
                io_outbound_item.setInbound_prefix(cursor.getInt(cursor.getColumnIndex(INBOUND_PREFIX)));
            }
            if (cursor.isNull(cursor.getColumnIndex(INBOUND_CODE))) {
                io_outbound_item.setInbound_code(null);
            } else {
                io_outbound_item.setInbound_code(cursor.getInt(cursor.getColumnIndex(INBOUND_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(INBOUND_ITEM))) {
                io_outbound_item.setInbound_item(null);
            } else {
                io_outbound_item.setInbound_item(cursor.getInt(cursor.getColumnIndex(INBOUND_ITEM)));
            }
            if (cursor.isNull(cursor.getColumnIndex(COMMENTS))) {
                io_outbound_item.setComments(null);
            } else {
                io_outbound_item.setComments(cursor.getString(cursor.getColumnIndex(COMMENTS)));
            }
            if(cursor.isNull(cursor.getColumnIndex(SAVE_DATE))) {
                io_outbound_item.setSave_date(null);
            }else{
                io_outbound_item.setSave_date(cursor.getString(cursor.getColumnIndex(SAVE_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(UPDATE_REQUIRED))) {
                io_outbound_item.setUpdate_required(0);
            }else{
                io_outbound_item.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));
            }
            //
            return io_outbound_item;
        }
    }

    private class IO_Outbound_ItemToContentValuesMapper implements Mapper<IO_Outbound_Item, ContentValues> {
        @Override
        public ContentValues map(IO_Outbound_Item io_outbound_item) {
            ContentValues contentValues = new ContentValues();
            if (io_outbound_item.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, io_outbound_item.getCustomer_code());
            }
            if (io_outbound_item.getOutbound_prefix() > -1) {
                contentValues.put(OUTBOUND_PREFIX, io_outbound_item.getOutbound_prefix());
            }
            if (io_outbound_item.getOutbound_code() > -1) {
                contentValues.put(OUTBOUND_CODE, io_outbound_item.getOutbound_code());
            }
            if (io_outbound_item.getOutbound_item() > -1) {
                contentValues.put(OUTBOUND_ITEM, io_outbound_item.getOutbound_item());
            }
            if (io_outbound_item.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, io_outbound_item.getProduct_code());
            }
            if (io_outbound_item.getSerial_code() > -1) {
                contentValues.put(SERIAL_CODE, io_outbound_item.getSerial_code());
            }
            contentValues.put(CONF_DATE, io_outbound_item.getConf_date());
            if (io_outbound_item.getStatus() != null) {
                contentValues.put(STATUS, io_outbound_item.getStatus());
            }
            contentValues.put(INBOUND_PREFIX, io_outbound_item.getInbound_prefix());
            contentValues.put(INBOUND_CODE, io_outbound_item.getInbound_code());
            contentValues.put(INBOUND_ITEM, io_outbound_item.getInbound_item());
            contentValues.put(COMMENTS, io_outbound_item.getComments());
            contentValues.put(SAVE_DATE, io_outbound_item.getSave_date());
            contentValues.put(UPDATE_REQUIRED, io_outbound_item.getUpdate_required());




            return contentValues;
        }
    }
}
