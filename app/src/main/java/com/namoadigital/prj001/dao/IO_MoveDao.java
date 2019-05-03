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
import com.namoadigital.prj001.model.IO_Inbound;
import com.namoadigital.prj001.model.IO_Move;
import com.namoadigital.prj001.model.IO_Move_Tracking;
import com.namoadigital.prj001.sql.IO_Move_Tracking_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class IO_MoveDao extends BaseDao implements DaoWithReturn<IO_Move> {

    private final Mapper<IO_Move, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, IO_Move> toIO_MoveMapper;

    public static final String TABLE = "io_move";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String MOVE_PREFIX = "move_prefix";
    public static final String MOVE_CODE = "move_code";
    public static final String PRODUCT_CODE = "product_code";
    public static final String SERIAL_CODE = "serial_code";
    public static final String SITE_CODE = "site_code";
    public static final String FROM_ZONE_CODE = "from_zone_code";
    public static final String FROM_LOCAL_CODE = "from_local_code";
    public static final String FROM_CLASS_CODE = "from_class_code";
    public static final String PLANNED_ZONE_CODE = "planned_zone_code";
    public static final String PLANNED_LOCAL_CODE = "planned_local_code";
    public static final String PLANNED_CLASS_CODE = "planned_class_code";
    public static final String TO_ZONE_CODE = "to_zone_code";
    public static final String TO_LOCAL_CODE = "to_local_code";
    public static final String TO_CLASS_CODE = "to_class_code";
    public static final String MOVE_TYPE = "move_type";
    public static final String REASON_CODE = "reason_code";
    public static final String INBOUND_PREFIX = "inbound_prefix";
    public static final String INBOUND_CODE = "inbound_code";
    public static final String INBOUND_ITEM = "inbound_item";
    public static final String OUTBOUND_PREFIX = "outbound_prefix";
    public static final String OUTBOUND_CODE = "outbound_code";
    public static final String OUTBOUND_ITEM = "outbound_item";
    public static final String DONE_DATE = "done_date";
    public static final String DONE_USER = "done_user";
    public static final String DONE_USER_NICK = "done_user_nick";
    public static final String STATUS = "status";
    public static final String UPDATE_REQUIRED = "update_required";
    public static final String TOKEN = "token";
    //COnstantes que não sao da tabela, mas utilizadas em suas queries
    public static final String PENDING_QTY = "PENDING_QTY";
    public static final String PLANNED_ZONE_ID = "planned_zone_id";
    public static final String PLANNED_LOCAL_ID = "planned_local_id";
    public static final String TO_ZONE_DESC = "to_zone_desc";
    public static final String TO_LOCAL_ID = "to_local_id";

    public IO_MoveDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new IO_MoveToContentValuesMapper();
        this.toIO_MoveMapper = new CursorIO_MoveMapper();
    }


    @Override
    public DaoObjReturn addUpdate(IO_Move io_move) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long addUpdateRet = 0;
        String curAction = DaoObjReturn.INSERT_OR_UPDATE;
        //
        openDB();

        try {
            curAction = DaoObjReturn.UPDATE;
            //Where para update
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_move.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(MOVE_PREFIX).append(" = '").append(String.valueOf(io_move.getMove_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(MOVE_CODE).append(" = '").append(String.valueOf(io_move.getMove_code())).append("'");
            //Tenta update e armazena retorno
            addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_move), sbWhere.toString(), null);
            //Se nenhuma linha afetada, tenta insert
            if (addUpdateRet == 0) {
                curAction = DaoObjReturn.INSERT;
                db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_move));
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

        closeDB();

        return daoObjReturn;
    }


    public DaoObjReturn addUpdate(List<IO_Move> io_moves, boolean status, SQLiteDatabase dbInstance) {
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

            for (IO_Move io_move : io_moves) {
                curAction = DaoObjReturn.UPDATE;
                //Where para update
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_move.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(MOVE_PREFIX).append(" = '").append(String.valueOf(io_move.getMove_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(MOVE_CODE).append(" = '").append(String.valueOf(io_move.getMove_code())).append("'");
                //Tenta update e armazena retorno
                addUpdateRet = db.update(TABLE, toContentValuesMapper.map(io_move), sbWhere.toString(), null);
                //Se nenhuma linha afetada, tenta insert
                if (addUpdateRet == 0) {
                    curAction = DaoObjReturn.INSERT;
                    db.insertOrThrow(TABLE, null, toContentValuesMapper.map(io_move));
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
            if (dbInstance == null) {
                db.endTransaction();
            }
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

    @Override
    public DaoObjReturn addUpdate(List<IO_Move> io_moves, boolean status) {
        return addUpdate(io_moves, status, null);
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
     * Remove todos itens baseado na PK da inbound
     *
     * @param io_inbound
     * @param dbInstance
     * @return
     */
    public DaoObjReturn removeInboundMoves(IO_Inbound io_inbound, @Nullable SQLiteDatabase dbInstance) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long sqlRet = 0;
        String curAction = DaoObjReturn.DELETE;
        //
        if (dbInstance == null) {
            openDB();
        } else {
            this.db = dbInstance;
        }

        try {
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(io_inbound.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_PREFIX).append(" = '").append(String.valueOf(io_inbound.getInbound_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(INBOUND_CODE).append(" = '").append(String.valueOf(io_inbound.getInbound_code())).append("'");
            //
            sqlRet = db.delete(TABLE, sbWhere.toString(), null);
        } catch (SQLiteException e) {
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
        if (dbInstance == null) {
            closeDB();
        }
        return daoObjReturn;
    }

    @Override
    public IO_Move getByString(String sQuery) {
        IO_Move io_move = null;
        openDB();

        try {
            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_move = toIO_MoveMapper.map(cursor);
                //
            }
            //
            if (io_move != null) {
                IO_Move_TrackingDao moveTrackingDao = new IO_Move_TrackingDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
                );
                //
                io_move.setTracking_list(
                    (ArrayList<IO_Move_Tracking>) moveTrackingDao.query(
                        new IO_Move_Tracking_Sql_001(
                            io_move.getCustomer_code(),
                            io_move.getMove_prefix(),
                            io_move.getMove_code()
                        ).toSqlQuery()
                    )
                );
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return io_move;
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
    public List<IO_Move> query(String sQuery) {
        List<IO_Move> io_moves = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                IO_Move uAux = toIO_MoveMapper.map(cursor);
                //
                if (uAux != null) {
                    IO_Move_TrackingDao moveTrackingDao = new IO_Move_TrackingDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    );
                    //
                    uAux.setTracking_list(
                        (ArrayList<IO_Move_Tracking>) moveTrackingDao.query(
                            new IO_Move_Tracking_Sql_001(
                                uAux.getCustomer_code(),
                                uAux.getMove_prefix(),
                                uAux.getMove_code()
                            ).toSqlQuery()
                        )
                    );
                }
                //
                io_moves.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return io_moves;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> io_moves = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                io_moves.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return io_moves;
    }

    private class CursorIO_MoveMapper implements Mapper<Cursor, IO_Move> {
        @Override
        public IO_Move map(Cursor cursor) {
            IO_Move io_move = new IO_Move();
            //
            io_move.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            io_move.setMove_prefix(cursor.getInt(cursor.getColumnIndex(MOVE_PREFIX)));
            io_move.setMove_code(cursor.getInt(cursor.getColumnIndex(MOVE_CODE)));
            io_move.setProduct_code(cursor.getLong(cursor.getColumnIndex(PRODUCT_CODE)));
            io_move.setSerial_code(cursor.getInt(cursor.getColumnIndex(SERIAL_CODE)));
            io_move.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            if (cursor.isNull(cursor.getColumnIndex(FROM_ZONE_CODE))) {
                io_move.setFrom_zone_code(null);
            } else {
                io_move.setFrom_zone_code(cursor.getInt(cursor.getColumnIndex(FROM_ZONE_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(FROM_LOCAL_CODE))) {
                io_move.setFrom_local_code(null);
            } else {
                io_move.setFrom_local_code(cursor.getInt(cursor.getColumnIndex(FROM_LOCAL_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(FROM_CLASS_CODE))) {
                io_move.setFrom_class_code(null);
            } else {
                io_move.setFrom_class_code(cursor.getInt(cursor.getColumnIndex(FROM_CLASS_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PLANNED_ZONE_CODE))) {
                io_move.setPlanned_zone_code(null);
            } else {
                io_move.setPlanned_zone_code(cursor.getInt(cursor.getColumnIndex(PLANNED_ZONE_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PLANNED_LOCAL_CODE))) {
                io_move.setPlanned_local_code(null);
            } else {
                io_move.setPlanned_local_code(cursor.getInt(cursor.getColumnIndex(PLANNED_LOCAL_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(PLANNED_CLASS_CODE))) {
                io_move.setPlanned_class_code(null);
            } else {
                io_move.setPlanned_class_code(cursor.getInt(cursor.getColumnIndex(PLANNED_CLASS_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(TO_ZONE_CODE))) {
                io_move.setTo_zone_code(null);
            } else {
                io_move.setTo_zone_code(cursor.getInt(cursor.getColumnIndex(TO_ZONE_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(TO_LOCAL_CODE))) {
                io_move.setTo_local_code(null);
            } else {
                io_move.setTo_local_code(cursor.getInt(cursor.getColumnIndex(TO_LOCAL_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(TO_CLASS_CODE))) {
                io_move.setTo_class_code(null);
            } else {
                io_move.setTo_class_code(cursor.getInt(cursor.getColumnIndex(TO_CLASS_CODE)));
            }
            io_move.setMove_type(cursor.getString(cursor.getColumnIndex(MOVE_TYPE)));
            if (cursor.isNull(cursor.getColumnIndex(REASON_CODE))) {
                io_move.setReason_code(null);
            } else {
                io_move.setReason_code(cursor.getInt(cursor.getColumnIndex(REASON_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(INBOUND_PREFIX))) {
                io_move.setInbound_prefix(null);
            } else {
                io_move.setInbound_prefix(cursor.getInt(cursor.getColumnIndex(INBOUND_PREFIX)));
            }
            if (cursor.isNull(cursor.getColumnIndex(INBOUND_CODE))) {
                io_move.setInbound_code(null);
            } else {
                io_move.setInbound_code(cursor.getInt(cursor.getColumnIndex(INBOUND_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(INBOUND_ITEM))) {
                io_move.setInbound_item(null);
            } else {
                io_move.setInbound_item(cursor.getInt(cursor.getColumnIndex(INBOUND_ITEM)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OUTBOUND_PREFIX))) {
                io_move.setOutbound_prefix(null);
            } else {
                io_move.setOutbound_prefix(cursor.getInt(cursor.getColumnIndex(OUTBOUND_PREFIX)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OUTBOUND_CODE))) {
                io_move.setOutbound_code(null);
            } else {
                io_move.setOutbound_code(cursor.getInt(cursor.getColumnIndex(OUTBOUND_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(OUTBOUND_ITEM))) {
                io_move.setOutbound_item(null);
            } else {
                io_move.setOutbound_item(cursor.getInt(cursor.getColumnIndex(OUTBOUND_ITEM)));
            }
            if (cursor.isNull(cursor.getColumnIndex(DONE_DATE))) {
                io_move.setDone_date(null);
            } else {
                io_move.setDone_date(cursor.getString(cursor.getColumnIndex(DONE_DATE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(DONE_USER))) {
                io_move.setDone_user(null);
            } else {
                io_move.setDone_user(cursor.getInt(cursor.getColumnIndex(DONE_USER)));
            }
            if (cursor.isNull(cursor.getColumnIndex(DONE_USER_NICK))) {
                io_move.setDone_user_nick(null);
            } else {
                io_move.setDone_user_nick(cursor.getString(cursor.getColumnIndex(DONE_USER_NICK)));
            }
            io_move.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            if (cursor.isNull(cursor.getColumnIndex(UPDATE_REQUIRED))) {
                io_move.setUpdate_required(0);
            } else {
                io_move.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));
            }
            //
            io_move.setToken(cursor.getString(cursor.getColumnIndex(TOKEN)));
            return io_move;
        }
    }

    private class IO_MoveToContentValuesMapper implements Mapper<IO_Move, ContentValues> {
        @Override
        public ContentValues map(IO_Move io_move) {
            ContentValues contentValues = new ContentValues();
            //
            if (io_move.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, io_move.getCustomer_code());
            }
            if (io_move.getMove_prefix() > -1) {
                contentValues.put(MOVE_PREFIX, io_move.getMove_prefix());
            }
            if (io_move.getMove_code() > -1) {
                contentValues.put(MOVE_CODE, io_move.getMove_code());
            }
            if (io_move.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, io_move.getProduct_code());
            }
            if (io_move.getSerial_code() > -1) {
                contentValues.put(SERIAL_CODE, io_move.getSerial_code());
            }
            if (io_move.getSite_code() > -1) {
                contentValues.put(SITE_CODE, io_move.getSite_code());
            }
            contentValues.put(FROM_ZONE_CODE, io_move.getFrom_zone_code());
            contentValues.put(FROM_LOCAL_CODE, io_move.getFrom_local_code());
            contentValues.put(FROM_CLASS_CODE, io_move.getFrom_class_code());
            contentValues.put(PLANNED_ZONE_CODE, io_move.getPlanned_zone_code());
            contentValues.put(PLANNED_LOCAL_CODE, io_move.getPlanned_local_code());
            contentValues.put(PLANNED_CLASS_CODE, io_move.getPlanned_class_code());
            contentValues.put(TO_ZONE_CODE, io_move.getTo_zone_code());
            contentValues.put(TO_LOCAL_CODE, io_move.getTo_local_code());
            contentValues.put(TO_CLASS_CODE, io_move.getTo_class_code());
            if (io_move.getMove_type() != null) {
                contentValues.put(MOVE_TYPE, io_move.getMove_type());
            }
            contentValues.put(REASON_CODE, io_move.getReason_code());
            contentValues.put(INBOUND_PREFIX, io_move.getInbound_prefix());
            contentValues.put(INBOUND_CODE, io_move.getInbound_code());
            contentValues.put(INBOUND_ITEM, io_move.getInbound_item());
            contentValues.put(OUTBOUND_PREFIX, io_move.getOutbound_prefix());
            contentValues.put(OUTBOUND_CODE, io_move.getOutbound_code());
            contentValues.put(OUTBOUND_ITEM, io_move.getOutbound_item());
            contentValues.put(DONE_DATE, io_move.getDone_date());
            contentValues.put(DONE_USER, io_move.getDone_user());
            contentValues.put(DONE_USER_NICK, io_move.getDone_user_nick());
            if (io_move.getStatus() != null) {
                contentValues.put(STATUS, io_move.getStatus());
            }
            if (io_move.getUpdate_required() > -1) {
                contentValues.put(UPDATE_REQUIRED, io_move.getUpdate_required());
            }
            if (io_move.getToken() != null) {
                contentValues.put(TOKEN, io_move.getToken());
            }
            //
            return contentValues;
        }
    }
}
