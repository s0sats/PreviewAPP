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
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Product;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class TK_Ticket_ProductDao extends BaseDao implements DaoWithReturn<TK_Ticket_Product>, DaoWithReturnSharedDbInstance<TK_Ticket_Product> {
    private final Mapper<TK_Ticket_Product, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, TK_Ticket_Product> toTK_Ticket_ProductMapper;

    public static final String TABLE = "tk_ticket_product";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DESC = "product_desc";
    public static final String UN = "un";
    public static final String QTY = "qty";
    public static final String QTY_USED = "qty_used";
    public static final String PICKUP_STATUS = "pickup_status";
    public static final String QTY_RETURNED = "qty_returned";
    public static final String RETURN_STATUS = "return_status";
    public static final String UPDATE_REQUIRED = "update_required";

    public TK_Ticket_ProductDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        toContentValuesMapper = new TK_Ticket_ProductToContentValuesMapper();
        toTK_Ticket_ProductMapper = new CursorToTK_Ticket_ProductMapper();
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Product tk_ticket_product) {
        return addUpdate(tk_ticket_product, null);
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Product> tk_ticket_products, boolean status) {
        return addUpdate(tk_ticket_products,status,null);
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Product tk_ticket_product, SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_product.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_product.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_product.getTicket_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PRODUCT_CODE).append(" = '").append(tk_ticket_product.getProduct_code()).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_product), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0) {
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_product));
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
    public DaoObjReturn addUpdate(List<TK_Ticket_Product> tk_ticket_products, boolean status, SQLiteDatabase dbInstance) {
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
            for (TK_Ticket_Product tk_ticket_product : tk_ticket_products) {
                //
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_product.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_product.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_product.getTicket_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(tk_ticket_product.getProduct_code()).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_product), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0) {
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_product));
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

    @Override
    public DaoObjReturn remove(TK_Ticket_Product tk_ticket_product, @Nullable SQLiteDatabase dbInstance) {
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
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_product.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_product.getTicket_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_product.getTicket_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(PRODUCT_CODE).append(" = '").append(tk_ticket_product.getProduct_code()).append("'");
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

    public DaoObjReturn removeFull(TK_Ticket tk_ticket , SQLiteDatabase dbInstance ){
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long sqlRet = 0;
        long sqlRetTotal = 0;
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
            for (TK_Ticket_Product tk_ticket_product  : tk_ticket.getProduct()) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(tk_ticket_product.getCustomer_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_PREFIX).append(" = '").append(tk_ticket_product.getTicket_prefix()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TICKET_CODE).append(" = '").append(tk_ticket_product.getTicket_code()).append("'");
                sbWhere.append(" and ");
                sbWhere.append(PRODUCT_CODE).append(" = '").append(tk_ticket_product.getProduct_code()).append("'");
                daoObjReturn.setTable(TABLE);
                //
                sqlRet = db.delete(TABLE,sbWhere.toString(),null);
                if(sqlRet == 0){
                     daoObjReturn.setRawMessage(daoObjReturn.DELETE_ERROR_0_ROWS_AFFECTED);
                     throw new Exception(daoObjReturn.getErrorMsg());
                }
                sqlRetTotal += sqlRet;
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
            daoObjReturn.setActionReturn(sqlRetTotal);
        }
        //
        if(dbInstance == null){
            closeDB();
        }
        return daoObjReturn;
    }

    @Override
    public TK_Ticket_Product getByString(String sQuery) {
        TK_Ticket_Product tk_ticket_product = null;
        //
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                tk_ticket_product = toTK_Ticket_ProductMapper.map(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        //
        closeDB();
        //
        return tk_ticket_product;
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
    public List<TK_Ticket_Product> query(String sQuery) {
        List<TK_Ticket_Product> tk_ticket_products = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                TK_Ticket_Product uAux = toTK_Ticket_ProductMapper.map(cursor);
                tk_ticket_products.add(uAux);
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        closeDB();
        return tk_ticket_products;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> tk_ticket_products = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                tk_ticket_products.add(CursorToHMAuxMapper.mapN(cursor));
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        closeDB();
        return tk_ticket_products;
    }

    private class CursorToTK_Ticket_ProductMapper implements Mapper<Cursor, TK_Ticket_Product> {
        @Override
        public TK_Ticket_Product map(Cursor cursor) {
            TK_Ticket_Product tk_ticket_product = new TK_Ticket_Product();
            tk_ticket_product.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket_product.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket_product.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket_product.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
            tk_ticket_product.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            tk_ticket_product.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            tk_ticket_product.setUn(cursor.getString(cursor.getColumnIndex(UN)));
            if (cursor.isNull(cursor.getColumnIndex(QTY))) {
                tk_ticket_product.setQty(null);
            } else {
                tk_ticket_product.setQty(cursor.getDouble(cursor.getColumnIndex(QTY)));
            }
            if (cursor.isNull(cursor.getColumnIndex(QTY_USED))) {
                tk_ticket_product.setQty_used(null);
            } else {
                tk_ticket_product.setQty_used(cursor.getDouble(cursor.getColumnIndex(QTY_USED)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PICKUP_STATUS))) {
                tk_ticket_product.setPickup_status(null);
            } else {
                tk_ticket_product.setPickup_status(cursor.getString(cursor.getColumnIndex(PICKUP_STATUS)));
            }
            if (cursor.isNull(cursor.getColumnIndex(QTY_RETURNED))) {
                tk_ticket_product.setQty_returned(null);
            } else {
                tk_ticket_product.setQty_returned(cursor.getDouble(cursor.getColumnIndex(QTY_RETURNED)));
            }
            if (cursor.isNull(cursor.getColumnIndex(RETURN_STATUS))) {
                tk_ticket_product.setReturn_status(null);
            } else {
                tk_ticket_product.setReturn_status(cursor.getString(cursor.getColumnIndex(RETURN_STATUS)));
            }
            tk_ticket_product.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));
            return tk_ticket_product;
        }
    }

    private class TK_Ticket_ProductToContentValuesMapper implements Mapper<TK_Ticket_Product, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket_Product tk_ticket_product) {
            ContentValues contentValues = new ContentValues();
            if (tk_ticket_product.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, tk_ticket_product.getCustomer_code());
            }
            if (tk_ticket_product.getTicket_prefix() > -1) {
                contentValues.put(TICKET_PREFIX, tk_ticket_product.getTicket_prefix());
            }
            if (tk_ticket_product.getTicket_code() > -1) {
                contentValues.put(TICKET_CODE, tk_ticket_product.getTicket_code());
            }
            if (tk_ticket_product.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, tk_ticket_product.getProduct_code());
            }
            if (tk_ticket_product.getProduct_id() != null) {
                contentValues.put(PRODUCT_ID, tk_ticket_product.getProduct_id());
            }
            if (tk_ticket_product.getProduct_desc() != null) {
                contentValues.put(PRODUCT_DESC, tk_ticket_product.getProduct_desc());
            }
            if (tk_ticket_product.getUn() != null) {
                contentValues.put(UN, tk_ticket_product.getUn());
            }
            contentValues.put(QTY, tk_ticket_product.getQty());
            contentValues.put(QTY_USED, tk_ticket_product.getQty_used());
            contentValues.put(PICKUP_STATUS, tk_ticket_product.getPickup_status());
            contentValues.put(QTY_RETURNED, tk_ticket_product.getQty_returned());
            contentValues.put(RETURN_STATUS, tk_ticket_product.getReturn_status());
            if (tk_ticket_product.getUpdate_required() > -1) {
                contentValues.put(UPDATE_REQUIRED, tk_ticket_product.getUpdate_required());
            }
            return contentValues;
        }
    }

}
