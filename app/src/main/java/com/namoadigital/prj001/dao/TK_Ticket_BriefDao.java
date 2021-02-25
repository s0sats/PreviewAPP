package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import androidx.annotation.Nullable;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.TK_Ticket_Brief;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class TK_Ticket_BriefDao extends BaseDao implements DaoWithReturn<TK_Ticket_Brief> {
    private final Mapper<TK_Ticket_Brief, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, TK_Ticket_Brief> toTK_Ticket_BriefMapper;

    public static final String TABLE = "tk_ticket_brief";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String TICKET_ID = "ticket_id";
    public static final String SCN = "scn";
    public static final String OPEN_SITE_CODE = "open_site_code";
    public static final String OPEN_SITE_DESC = "open_site_desc";
    public static final String OPEN_PRODUCT_DESC = "open_product_desc";
    public static final String OPEN_SERIAL_ID = "open_serial_id";
    public static final String CURRENT_STEP_ORDER = "current_step_order";
    public static final String TICKET_STATUS = "ticket_status";
    public static final String ORIGIN_DESC = "origin_desc";
    public static final String STEP_DESC = "step_desc";
    public static final String STEP_ORDER_SEQ = "step_order_seq";
    public static final String FORECAST_START = "forecast_start";
    public static final String FORECAST_END = "forecast_end";
    public static final String STEP_COUNT = "step_count";
    public static final String FCM = "fcm";
    public static final String LOCAL_TICKET = "local_ticket";
    public static final String CLIENT_CODE = "client_code";
    public static final String CLIENT_NAME = "client_name";
    public static final String CONTRACT_CODE = "contract_code";
    public static final String CONTRACT_DESC = "contract_desc";

    public TK_Ticket_BriefDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION,  Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new TK_Ticket_BriefDao.TK_Ticket_BriefToContentValuesMapper();
        this.toTK_Ticket_BriefMapper = new TK_Ticket_BriefDao.CursorToTK_Ticket_BriefMapper();
    }

    @Override
    public DaoObjReturn addUpdate(TK_Ticket_Brief tk_ticket_brief) {
        return addUpdate(tk_ticket_brief,null);
    }

    private DaoObjReturn addUpdate(TK_Ticket_Brief tk_ticket_brief, @Nullable SQLiteDatabase dbInstance) {
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
            StringBuilder sbWhere = getWherePkClause(tk_ticket_brief);
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_brief), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if(addUpdateRet == 0){
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_brief));
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
        }
        //
        if(dbInstance == null){
            closeDB();
        }

        return daoObjReturn;
    }

    private StringBuilder getWherePkClause(TK_Ticket_Brief tk_ticket_brief) {
        return
                new StringBuilder()
                        .append(CUSTOMER_CODE).append(" = '").append(tk_ticket_brief.getCustomer_code()).append("'")
                        .append(" and ")
                        .append(TICKET_PREFIX).append(" = '").append(tk_ticket_brief.getTicket_prefix()).append("'")
                        .append(" and ")
                        .append(TICKET_CODE).append(" = '").append(tk_ticket_brief.getTicket_code()).append("'");
    }


    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket_Brief> tk_ticket_briefs, boolean status) {
        return addUpdate(tk_ticket_briefs, status,null);

    }

    private DaoObjReturn addUpdate(List<TK_Ticket_Brief> tk_ticket_briefs, boolean status, SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        long addUpdateRet = 0;
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (TK_Ticket_Brief tk_ticket_brief : tk_ticket_briefs) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = getWherePkClause(tk_ticket_brief);
                /* LUCHE - 04/09/2020
                 * Antes de inserir, verifica a necessidade cancelar forms vinculados a esse obj.
                 */

                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(tk_ticket_brief), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if(addUpdateRet == 0){
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(tk_ticket_brief));
                }
            }

            //db.setTransactionSuccessful();
        } catch (SQLiteException e){
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
            //db.endTransaction();
        }
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

    @Override
    public TK_Ticket_Brief getByString(String sQuery) {
        return getByString(sQuery,null);
    }

    private TK_Ticket_Brief getByString(String sQuery,  @Nullable SQLiteDatabase dbInstance) {
        TK_Ticket_Brief tk_ticket_brief = null;

        if (dbInstance == null) {
            openDB();
        } else {
            this.db = dbInstance;
        }

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                tk_ticket_brief = toTK_Ticket_BriefMapper.map(cursor);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        if(dbInstance == null) {
            closeDB();
        }

        return tk_ticket_brief;
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
    public List<TK_Ticket_Brief> query(String sQuery) {
        List<TK_Ticket_Brief> tk_ticket_brief = new ArrayList<>();
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                TK_Ticket_Brief uAux = toTK_Ticket_BriefMapper.map(cursor);
                tk_ticket_brief.add(uAux);
            }
            //
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return tk_ticket_brief;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> tk_ticket_brief = new ArrayList<>();
        openDB();
        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                tk_ticket_brief.add(CursorToHMAuxMapper.mapN(cursor));
            }
            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }
        closeDB();
        return tk_ticket_brief;
    }

    private class TK_Ticket_BriefToContentValuesMapper implements Mapper<TK_Ticket_Brief, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket_Brief tk_ticket_brief) {
            ContentValues contentValues = new ContentValues();
            if (tk_ticket_brief.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, tk_ticket_brief.getCustomer_code());
            }
            if (tk_ticket_brief.getTicket_prefix() > -1) {
                contentValues.put(TICKET_PREFIX, tk_ticket_brief.getTicket_prefix());
            }
            if (tk_ticket_brief.getTicket_code() > -1) {
                contentValues.put(TICKET_CODE, tk_ticket_brief.getTicket_code());
            }
            if (tk_ticket_brief.getTicket_id() != null) {
                contentValues.put(TICKET_ID, tk_ticket_brief.getTicket_id());
            }
            if (tk_ticket_brief.getScn() > -1) {
                contentValues.put(SCN, tk_ticket_brief.getScn());
            }

            if (tk_ticket_brief.getOpen_site_code() > -1) {
                contentValues.put(OPEN_SITE_CODE, tk_ticket_brief.getOpen_site_code());
            }
            if (tk_ticket_brief.getOpen_site_desc() != null) {
                contentValues.put(OPEN_SITE_DESC, tk_ticket_brief.getOpen_site_desc());
            }
            if (tk_ticket_brief.getOpen_product_desc() != null) {
                contentValues.put(OPEN_PRODUCT_DESC, tk_ticket_brief.getOpen_product_desc());
            }

            if (tk_ticket_brief.getOpen_serial_id() != null) {
                contentValues.put(OPEN_SERIAL_ID, tk_ticket_brief.getOpen_serial_id());
            }

            contentValues.put(CURRENT_STEP_ORDER, tk_ticket_brief.getCurrent_step_order());

            if (tk_ticket_brief.getTicket_status() != null) {
                contentValues.put(TICKET_STATUS, tk_ticket_brief.getTicket_status());
            }

            if (tk_ticket_brief.getOrigin_desc() != null) {
                contentValues.put(ORIGIN_DESC, tk_ticket_brief.getOrigin_desc());
            }

            contentValues.put(STEP_DESC, tk_ticket_brief.getStep_desc());
            contentValues.put(STEP_ORDER_SEQ, tk_ticket_brief.getStep_order_seq());
            contentValues.put(FORECAST_START, tk_ticket_brief.getForecast_start());
            contentValues.put(FORECAST_END, tk_ticket_brief.getForecast_end());
            contentValues.put(STEP_COUNT, tk_ticket_brief.getStep_count());

            if (tk_ticket_brief.getFcm() > -1) {
                contentValues.put(FCM, tk_ticket_brief.getFcm());
            }

//            if (tk_ticket_brief.getLocal_ticket() > -1) {
//                contentValues.put(LOCAL_TICKET, tk_ticket_brief.getTicket_local());
//            }
            contentValues.put(CLIENT_CODE, tk_ticket_brief.getClient_code());
            contentValues.put(CLIENT_NAME, tk_ticket_brief.getClient_name());
            contentValues.put(CONTRACT_CODE, tk_ticket_brief.getContract_code());
            contentValues.put(CONTRACT_DESC, tk_ticket_brief.getContract_desc());

            return contentValues;
        }
    }

    public class CursorToTK_Ticket_BriefMapper implements Mapper<Cursor, TK_Ticket_Brief> {
        @Override
        public TK_Ticket_Brief map(Cursor cursor) {
            TK_Ticket_Brief tk_ticket_brief = new TK_Ticket_Brief();
            //
            tk_ticket_brief.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket_brief.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket_brief.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket_brief.setTicket_id(cursor.getString(cursor.getColumnIndex(TICKET_ID)));
            tk_ticket_brief.setScn(cursor.getInt(cursor.getColumnIndex(SCN)));
            tk_ticket_brief.setOpen_site_code(cursor.getInt(cursor.getColumnIndex(OPEN_SITE_CODE)));

            tk_ticket_brief.setOpen_site_desc(cursor.getString(cursor.getColumnIndex(OPEN_SITE_DESC)));
            tk_ticket_brief.setOpen_product_desc(cursor.getString(cursor.getColumnIndex(OPEN_PRODUCT_DESC)));
            tk_ticket_brief.setOpen_serial_id(cursor.getString(cursor.getColumnIndex(OPEN_SERIAL_ID)));


            if (cursor.isNull(cursor.getColumnIndex(CURRENT_STEP_ORDER))) {
                tk_ticket_brief.setCurrent_step_order(null);
            } else {
                tk_ticket_brief.setCurrent_step_order(cursor.getInt(cursor.getColumnIndex(CURRENT_STEP_ORDER)));
            }

            tk_ticket_brief.setTicket_status(cursor.getString(cursor.getColumnIndex(TICKET_STATUS)));
            tk_ticket_brief.setOrigin_desc(cursor.getString(cursor.getColumnIndex(ORIGIN_DESC)));

            if (cursor.isNull(cursor.getColumnIndex(STEP_DESC))) {
                tk_ticket_brief.setStep_desc(null);
            } else {
                tk_ticket_brief.setStep_desc(cursor.getString(cursor.getColumnIndex(STEP_DESC)));
            }
            if (cursor.isNull(cursor.getColumnIndex(STEP_ORDER_SEQ))) {
                tk_ticket_brief.setStep_order_seq(null);
            } else {
                tk_ticket_brief.setStep_order_seq(cursor.getString(cursor.getColumnIndex(STEP_ORDER_SEQ)));
            }
            if (cursor.isNull(cursor.getColumnIndex(FORECAST_START))) {
                tk_ticket_brief.setForecast_start(null);
            } else {
                tk_ticket_brief.setForecast_start(cursor.getString(cursor.getColumnIndex(FORECAST_START)));
            }
            if (cursor.isNull(cursor.getColumnIndex(FORECAST_END))) {
                tk_ticket_brief.setForecast_end(null);
            } else {
                tk_ticket_brief.setForecast_end(cursor.getString(cursor.getColumnIndex(FORECAST_END)));
            }
            if (cursor.isNull(cursor.getColumnIndex(STEP_COUNT))) {
                tk_ticket_brief.setStep_count(null);
            } else {
                tk_ticket_brief.setStep_count(cursor.getInt(cursor.getColumnIndex(STEP_COUNT)));
            }
            tk_ticket_brief.setFcm(cursor.getInt(cursor.getColumnIndex(FCM)));
            //tk_ticket_brief.setLocal_ticket(cursor.getInt(cursor.getColumnIndex(LOCAL_TICKET)));
            //
            if (cursor.isNull(cursor.getColumnIndex(CLIENT_CODE))) {
                tk_ticket_brief.setClient_code(null);
            } else {
                tk_ticket_brief.setClient_code(cursor.getInt(cursor.getColumnIndex(CLIENT_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLIENT_NAME))) {
                tk_ticket_brief.setClient_name(null);
            } else {
                tk_ticket_brief.setClient_name(cursor.getString(cursor.getColumnIndex(CLIENT_NAME)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CONTRACT_CODE))) {
                tk_ticket_brief.setContract_code(null);
            } else {
                tk_ticket_brief.setContract_code(cursor.getInt(cursor.getColumnIndex(CONTRACT_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CONTRACT_DESC))) {
                tk_ticket_brief.setContract_desc(null);
            } else {
                tk_ticket_brief.setContract_desc(cursor.getString(cursor.getColumnIndex(CONTRACT_DESC)));
            }
            return tk_ticket_brief;
        }
    }
}
