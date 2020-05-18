package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.SM_SO_Product_Event;
import com.namoadigital.prj001.model.SM_SO_Product_Event_File;
import com.namoadigital.prj001.model.SM_SO_Product_Event_Sketch;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_File_Sql_001;
import com.namoadigital.prj001.sql.SM_SO_Product_Event_Sketch_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 30/10/2017.
 */

public class SM_SO_Product_EventDao extends BaseDao implements Dao<SM_SO_Product_Event>, DaoTmp<SM_SO_Product_Event> {

    private final Mapper<SM_SO_Product_Event, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SM_SO_Product_Event> toSM_SO_Product_EventMapper;

    public static final String TABLE = "sm_so_product_events";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String SEQ = "seq";
    public static final String SEQ_TMP = "seq_tmp";
    public static final String PRODUCT_CODE = "product_code";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DESC = "product_desc";
    public static final String UN = "un";
    public static final String FLAG_APPLY = "flag_apply";
    public static final String FLAG_INSPECTION = "flag_inspection";
    public static final String FLAG_REPAIR = "flag_repair";
    public static final String QTY_APPLY = "qty_apply";
    public static final String SKETCH_CODE = "sketch_code";
    public static final String SKETCH_NAME = "sketch_name";
    public static final String SKETCH_URL = "sketch_url";
    public static final String SKETCH_URL_LOCAL = "sketch_url_local";
    public static final String SKETCH_LINES = "sketch_lines";
    public static final String SKETCH_COLUMNS = "sketch_columns";
    public static final String SKETCH_COLOR = "sketch_color";
    public static final String COMMENTS = "comments";
    public static final String STATUS = "status";
    public static final String CREATE_DATE = "create_date";
    public static final String CREATE_USER = "create_user";
    public static final String CREATE_USER_NICK = "create_user_nick";
    public static final String DONE_DATE = "done_date";
    public static final String DONE_USER = "done_user";
    public static final String DONE_USER_NICK = "done_user_nick";
    public static final String INTEGRATED = "integrated";

    public static final String[] columns = {
            CUSTOMER_CODE, SO_PREFIX, SO_CODE, SEQ, SEQ_TMP, PRODUCT_CODE, PRODUCT_ID,
            PRODUCT_DESC, UN, FLAG_APPLY, FLAG_INSPECTION, FLAG_REPAIR, QTY_APPLY,
            SKETCH_CODE, SKETCH_NAME, SKETCH_URL, SKETCH_URL_LOCAL, SKETCH_LINES,
            SKETCH_COLUMNS, SKETCH_COLOR, COMMENTS, STATUS, CREATE_DATE, CREATE_USER,
            CREATE_USER_NICK, DONE_DATE, DONE_USER, DONE_USER_NICK, INTEGRATED
    };


    public SM_SO_Product_EventDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SM_SO_Product_EventToContentValuesMapper();
        this.toSM_SO_Product_EventMapper = new CursorSM_SO_Product_EventMapper();
    }

    @Override
    public void addUpdateTmp(SM_SO_Product_Event sm_so_product_event) {
        openDB();

        try {
            if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SEQ_TMP).append(" = '").append(String.valueOf(sm_so_product_event.getSeq_tmp())).append("'");

                db.update(TABLE, toContentValuesMapper.map(sm_so_product_event), sbWhere.toString(), null);
            }
            //Popula ArrayList de file e sketch
            SM_SO_Product_Event_FileDao eventFileDao = new SM_SO_Product_Event_FileDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            eventFileDao.addUpdateTmp(sm_so_product_event.getFile(), false);
            //
            SM_SO_Product_Event_SketchDao eventSketchDao = new SM_SO_Product_Event_SketchDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            eventSketchDao.addUpdateTmp(sm_so_product_event.getSketch(), false);

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdateTmp(Iterable<SM_SO_Product_Event> sm_so_product_events, boolean status) {

        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            SM_SO_Product_Event_FileDao eventFileDao = new SM_SO_Product_Event_FileDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            SM_SO_Product_Event_SketchDao eventSketchDao = new SM_SO_Product_Event_SketchDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            for (SM_SO_Product_Event sm_so_product_event : sm_so_product_events) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event.getSo_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SEQ_TMP).append(" = '").append(String.valueOf(sm_so_product_event.getSeq_tmp())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(sm_so_product_event), sbWhere.toString(), null);
                }

                eventFileDao.addUpdateTmp(sm_so_product_event.getFile(), false);
                //
                eventSketchDao.addUpdateTmp(sm_so_product_event.getSketch(), false);
            }


        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            //db.endTransaction();
        }

        closeDB();

    }

    @Override
    public void addUpdate(SM_SO_Product_Event sm_so_product_event) {

        openDB();

        try {

            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event.getSo_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event.getSo_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SEQ).append(" = '").append(String.valueOf(sm_so_product_event.getSeq())).append("'");
            //
            if (db.update(TABLE, toContentValuesMapper.map(sm_so_product_event), sbWhere.toString(), null) == 0) {
                db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event));
            }
            //Popula ArrayList de file e sketch
            SM_SO_Product_Event_FileDao eventFileDao = new SM_SO_Product_Event_FileDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            eventFileDao.addUpdateTmp(sm_so_product_event.getFile(), false);
            //
            SM_SO_Product_Event_SketchDao eventSketchDao = new SM_SO_Product_Event_SketchDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            eventSketchDao.addUpdateTmp(sm_so_product_event.getSketch(), false);


        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SM_SO_Product_Event> sm_so_product_events, boolean status) {

        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }
            SM_SO_Product_Event_FileDao eventFileDao = new SM_SO_Product_Event_FileDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );
            //
            SM_SO_Product_Event_SketchDao eventSketchDao = new SM_SO_Product_Event_SketchDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            //Define contador inicial para o criação do temp;
            int seq_tmp = 100;
            //
            for (SM_SO_Product_Event sm_so_product_event : sm_so_product_events) {
                //Atualiza valor do exec_temp
                seq_tmp++;
                //Seta Tmp
                sm_so_product_event.setSeq_tmp(seq_tmp);
                //
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so_product_event.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so_product_event.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so_product_event.getSo_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SEQ).append(" = '").append(String.valueOf(sm_so_product_event.getSeq())).append("'");
                //
                if (db.update(TABLE, toContentValuesMapper.map(sm_so_product_event), sbWhere.toString(), null) == 0) {
                    db.insert(TABLE, null, toContentValuesMapper.map(sm_so_product_event));
                }
                //
                for (int i = 0; i < sm_so_product_event.getFile().size(); i++) {
                    SM_SO_Product_Event_File eventFile = sm_so_product_event.getFile().get(i);
                    eventFile.setPK(sm_so_product_event);
                }
                //
                for (int i = 0; i < sm_so_product_event.getSketch().size(); i++) {
                    SM_SO_Product_Event_Sketch eventSketch = sm_so_product_event.getSketch().get(i);
                    eventSketch.setPK(sm_so_product_event);
                }
                //
                eventFileDao.addUpdate(sm_so_product_event.getFile(), false);
                //
                eventSketchDao.addUpdate(sm_so_product_event.getSketch(), false);
            }

            //db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            //db.endTransaction();
        }

        closeDB();
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
     * LUCHE - 18/05/2020
     * <p></p>
     * Metodo que deleta produto evento e files usando os dados do obj passado.
     * @param sm_so_product_event Produto evento a ser deletado.
     * @return Obj Dao com informações da operação
     */
    public DaoObjReturn removeByTmp(SM_SO_Product_Event sm_so_product_event) {
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        long delRetCounter = 0;
        String curAction = DaoObjReturn.DELETE;

        openDB();
        try{
            db.beginTransaction();
            //
            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(sm_so_product_event.getCustomer_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_PREFIX).append(" = '").append(sm_so_product_event.getSo_prefix()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_CODE).append(" = '").append(sm_so_product_event.getSo_code()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SEQ).append(" = '").append(sm_so_product_event.getSeq()).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SEQ_TMP).append(" = '").append(sm_so_product_event.getSeq_tmp()).append("'");
            //Se produto evento possui file, deleta os registros.
            if(sm_so_product_event.getFile() != null && sm_so_product_event.getFile().size() > 0){
                daoObjReturn.setTable(SM_SO_Product_Event_FileDao.TABLE);
                delRetCounter = db.delete(SM_SO_Product_Event_FileDao.TABLE, sbWhere.toString(), null);
                //Se tinha file e qtd deleteda foi 0, lança exception indicando erro.
                if(delRetCounter <= 0){
                    daoObjReturn.setRawMessage(daoObjReturn.DELETE_ERROR_0_ROWS_AFFECTED);
                    throw new Exception(daoObjReturn.getErrorMsg());
                }
                //Reseta contador para validação do delete da propria table.
                delRetCounter = 0;
            }
            //
            daoObjReturn.setTable(TABLE);
            delRetCounter = db.delete(TABLE, sbWhere.toString(), null);
            if(delRetCounter <= 0){
                daoObjReturn.setRawMessage(daoObjReturn.DELETE_ERROR_0_ROWS_AFFECTED);
                throw new Exception(daoObjReturn.getErrorMsg());
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
            daoObjReturn.setActionReturn(delRetCounter);
        }
        //
        closeDB();
        //
        return daoObjReturn;
    }

    @Override
    public SM_SO_Product_Event getByString(String sQuery) {
        SM_SO_Product_Event sm_so_product_event = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_product_event = toSM_SO_Product_EventMapper.map(cursor);

                if (sm_so_product_event != null) {
                    SM_SO_Product_Event_FileDao eventFileDao = new SM_SO_Product_Event_FileDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );
                    //
                    SM_SO_Product_Event_SketchDao eventSketchDao = new SM_SO_Product_Event_SketchDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    sm_so_product_event.setFile(
                            (ArrayList<SM_SO_Product_Event_File>) eventFileDao.query(
                                new SM_SO_Product_Event_File_Sql_001(
                                        sm_so_product_event.getCustomer_code(),
                                        sm_so_product_event.getSo_prefix(),
                                        sm_so_product_event.getSo_code(),
                                        sm_so_product_event.getSeq_tmp()
                                ).toSqlQuery()
                            )
                    );
                    //
                    sm_so_product_event.setSketch(
                            (ArrayList<SM_SO_Product_Event_Sketch>) eventSketchDao.query(
                                    new SM_SO_Product_Event_Sketch_Sql_001(
                                            sm_so_product_event.getCustomer_code(),
                                            sm_so_product_event.getSo_prefix(),
                                            sm_so_product_event.getSo_code(),
                                            sm_so_product_event.getSeq_tmp()
                                    ).toSqlQuery()
                            )
                    );
                }

            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_product_event;
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
    public List<SM_SO_Product_Event> query(String sQuery) {
        List<SM_SO_Product_Event> sm_so_product_events = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                SM_SO_Product_Event uAux = toSM_SO_Product_EventMapper.map(cursor);
                //
                if (uAux != null) {
                    SM_SO_Product_Event_FileDao eventFileDao = new SM_SO_Product_Event_FileDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );
                    //
                    SM_SO_Product_Event_SketchDao eventSketchDao = new SM_SO_Product_Event_SketchDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );
                    uAux.setFile(
                            (ArrayList<SM_SO_Product_Event_File>) eventFileDao.query(
                                    new SM_SO_Product_Event_File_Sql_001(
                                            uAux.getCustomer_code(),
                                            uAux.getSo_prefix(),
                                            uAux.getSo_code(),
                                            uAux.getSeq_tmp()
                                    ).toSqlQuery()
                            )
                    );
                    //
                    uAux.setSketch(
                            (ArrayList<SM_SO_Product_Event_Sketch>) eventSketchDao.query(
                                    new SM_SO_Product_Event_Sketch_Sql_001(
                                            uAux.getCustomer_code(),
                                            uAux.getSo_prefix(),
                                            uAux.getSo_code(),
                                            uAux.getSeq_tmp()
                                    ).toSqlQuery()
                            )
                    );

                }
                //
                sm_so_product_events.add(uAux);

            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_product_events;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        ArrayList<HMAux> sm_so_product_events = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                sm_so_product_events.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sm_so_product_events;
    }

    private class CursorSM_SO_Product_EventMapper implements Mapper<Cursor, SM_SO_Product_Event> {
        @Override
        public SM_SO_Product_Event map(Cursor cursor) {
            SM_SO_Product_Event sm_so_product_event = new SM_SO_Product_Event();

            sm_so_product_event.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            sm_so_product_event.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            sm_so_product_event.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            sm_so_product_event.setSeq(cursor.getInt(cursor.getColumnIndex(SEQ)));
            sm_so_product_event.setSeq_tmp(cursor.getInt(cursor.getColumnIndex(SEQ_TMP)));
            sm_so_product_event.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
            sm_so_product_event.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            sm_so_product_event.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            sm_so_product_event.setUn(cursor.getString(cursor.getColumnIndex(UN)));
            sm_so_product_event.setFlag_apply(cursor.getInt(cursor.getColumnIndex(FLAG_APPLY)));
            sm_so_product_event.setFlag_inspection(cursor.getInt(cursor.getColumnIndex(FLAG_INSPECTION)));
            sm_so_product_event.setFlag_repair(cursor.getInt(cursor.getColumnIndex(FLAG_REPAIR)));
            if (cursor.isNull(cursor.getColumnIndex(QTY_APPLY))) {
                sm_so_product_event.setQty_apply(null);
            } else {
                sm_so_product_event.setQty_apply(cursor.getString(cursor.getColumnIndex(QTY_APPLY)).replace(".",","));
            }
            if (cursor.isNull(cursor.getColumnIndex(SKETCH_CODE))) {
                sm_so_product_event.setSketch_code(null);
            } else {
                sm_so_product_event.setSketch_code(cursor.getInt(cursor.getColumnIndex(SKETCH_CODE)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SKETCH_NAME))) {
                sm_so_product_event.setSketch_name(null);
            } else {
                sm_so_product_event.setSketch_name(cursor.getString(cursor.getColumnIndex(SKETCH_NAME)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SKETCH_URL))) {
                sm_so_product_event.setSketch_url(null);
            } else {
                sm_so_product_event.setSketch_url(cursor.getString(cursor.getColumnIndex(SKETCH_URL)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SKETCH_URL_LOCAL))) {
                sm_so_product_event.setSketch_url_local(null);
            } else {
                sm_so_product_event.setSketch_url_local(cursor.getString(cursor.getColumnIndex(SKETCH_URL_LOCAL)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SKETCH_COLUMNS))) {
                sm_so_product_event.setSketch_columns(null);
            } else {
                sm_so_product_event.setSketch_columns(cursor.getInt(cursor.getColumnIndex(SKETCH_COLUMNS)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SKETCH_LINES))) {
                sm_so_product_event.setSketch_lines(null);
            } else {
                sm_so_product_event.setSketch_lines(cursor.getInt(cursor.getColumnIndex(SKETCH_LINES)));
            }
            if (cursor.isNull(cursor.getColumnIndex(SKETCH_COLOR))) {
                sm_so_product_event.setSketch_color(null);
            } else {
                sm_so_product_event.setSketch_color(cursor.getString(cursor.getColumnIndex(SKETCH_COLOR)));
            }
            if (cursor.isNull(cursor.getColumnIndex(COMMENTS))) {
                sm_so_product_event.setComments(null);
            } else {
                sm_so_product_event.setComments(cursor.getString(cursor.getColumnIndex(COMMENTS)));
            }
            sm_so_product_event.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            sm_so_product_event.setCreate_date(cursor.getString(cursor.getColumnIndex(CREATE_DATE)));
            sm_so_product_event.setCreate_user(cursor.getInt(cursor.getColumnIndex(CREATE_USER)));
            sm_so_product_event.setCreate_user_nick(cursor.getString(cursor.getColumnIndex(CREATE_USER_NICK)));
            if(cursor.isNull(cursor.getColumnIndex(DONE_DATE))){
                sm_so_product_event.setDone_date(null);
            }else{
                sm_so_product_event.setDone_date(cursor.getString(cursor.getColumnIndex(DONE_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(DONE_USER))){
                sm_so_product_event.setDone_user(null);
            }else{
                sm_so_product_event.setDone_user(cursor.getInt(cursor.getColumnIndex(DONE_USER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(DONE_USER_NICK))){
                sm_so_product_event.setDone_user_nick(null);
            }else{
                sm_so_product_event.setDone_user_nick(cursor.getString(cursor.getColumnIndex(DONE_USER_NICK)));
            }
            sm_so_product_event.setIntegrated(cursor.getInt(cursor.getColumnIndex(INTEGRATED)));

            return sm_so_product_event;
        }
    }

    private class SM_SO_Product_EventToContentValuesMapper implements Mapper<SM_SO_Product_Event, ContentValues> {
        @Override
        public ContentValues map(SM_SO_Product_Event sm_so_product_event) {

            ContentValues contentValues = new ContentValues();

            if (sm_so_product_event.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, sm_so_product_event.getCustomer_code());
            }
            if (sm_so_product_event.getSo_prefix() > -1) {
                contentValues.put(SO_PREFIX, sm_so_product_event.getSo_prefix());
            }
            if (sm_so_product_event.getSo_code() > -1) {
                contentValues.put(SO_CODE, sm_so_product_event.getSo_code());
            }
            if (sm_so_product_event.getSeq() > -1) {
                contentValues.put(SEQ, sm_so_product_event.getSeq());
            }
            if (sm_so_product_event.getSeq_tmp() > -1) {
                contentValues.put(SEQ_TMP, sm_so_product_event.getSeq_tmp());
            }
            if (sm_so_product_event.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, sm_so_product_event.getProduct_code());
            }
            if (sm_so_product_event.getProduct_id() != null) {
                contentValues.put(PRODUCT_ID, sm_so_product_event.getProduct_id());
            }
            if (sm_so_product_event.getProduct_desc() != null) {
                contentValues.put(PRODUCT_DESC, sm_so_product_event.getProduct_desc());
            }
            if (sm_so_product_event.getUn() != null) {
                contentValues.put(UN, sm_so_product_event.getUn());
            }
            if (sm_so_product_event.getFlag_apply() > -1) {
                contentValues.put(FLAG_APPLY, sm_so_product_event.getFlag_apply());
            }
            if (sm_so_product_event.getFlag_inspection() > -1) {
                contentValues.put(FLAG_INSPECTION, sm_so_product_event.getFlag_inspection());
            }
            if (sm_so_product_event.getFlag_repair() > -1) {
                contentValues.put(FLAG_REPAIR, sm_so_product_event.getFlag_repair());
            }
            //campos que podem ser nulos
            contentValues.put(QTY_APPLY, sm_so_product_event.getQty_apply() != null ? sm_so_product_event.getQty_apply().replace(".",",") : sm_so_product_event.getQty_apply());
            contentValues.put(SKETCH_CODE, sm_so_product_event.getSketch_code());
            contentValues.put(SKETCH_NAME, sm_so_product_event.getSketch_name());
            contentValues.put(SKETCH_URL, sm_so_product_event.getSketch_url());
            contentValues.put(SKETCH_URL_LOCAL, sm_so_product_event.getSketch_url_local());
            contentValues.put(SKETCH_LINES, sm_so_product_event.getSketch_lines());
            contentValues.put(SKETCH_COLUMNS, sm_so_product_event.getSketch_columns());
            contentValues.put(SKETCH_COLOR, sm_so_product_event.getSketch_color());
            contentValues.put(COMMENTS, sm_so_product_event.getComments());
            //
            if (sm_so_product_event.getStatus() != null) {
                contentValues.put(STATUS, sm_so_product_event.getStatus());
            }
            if (sm_so_product_event.getCreate_date() != null) {
                contentValues.put(CREATE_DATE, sm_so_product_event.getCreate_date());
            }
            if (sm_so_product_event.getCreate_user() > -1) {
                contentValues.put(CREATE_USER, sm_so_product_event.getCreate_user());
            }
            if (sm_so_product_event.getCreate_user_nick() != null) {
                contentValues.put(CREATE_USER_NICK, sm_so_product_event.getCreate_user_nick());
            }
            contentValues.put(DONE_DATE,sm_so_product_event.getDone_date());
            contentValues.put(DONE_USER,sm_so_product_event.getDone_user());
            contentValues.put(DONE_USER_NICK,sm_so_product_event.getDone_user_nick());
            if(sm_so_product_event.getIntegrated() > -1){
                contentValues.put(INTEGRATED,sm_so_product_event.getIntegrated());
            }

            return contentValues;
        }
    }
}
