package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO_Product_Event;
import com.namoadigital.prj001.model.SM_SO_Product_Event_File;
import com.namoadigital.prj001.model.SM_SO_Product_Event_Sketch;
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

    public static final String[] columns = {
            CUSTOMER_CODE, SO_PREFIX, SO_CODE, SEQ, SEQ_TMP, PRODUCT_CODE, PRODUCT_ID,
            PRODUCT_DESC, UN, FLAG_APPLY, FLAG_INSPECTION, FLAG_REPAIR, QTY_APPLY,
            SKETCH_CODE, SKETCH_NAME, SKETCH_URL, SKETCH_URL_LOCAL, SKETCH_LINES,
            SKETCH_COLUMNS, SKETCH_COLOR, COMMENTS, STATUS
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
                eventFileDao.addUpdateTmp(sm_so_product_event.getFile(), false);
                //
                eventSketchDao.addUpdateTmp(sm_so_product_event.getSketch(), false);
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

//                    sm_so_product_event.setFile(
//                            eventFileDao.query(
//
//                            )
//                    );
//                    //
//                    sm_so_product_event.setSketch(
//                            eventSketchDao.query(
//
//                            )
//                    );


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

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                hmAux = toHMAuxMapper.map(cursor);
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

//                    uAux.setFile(
//                            eventFileDao.query(
//
//                            )
//
//                    );
//
//                    uAux.setSketch(
//                            eventSketchDao.query(
//
//                            )
//                    );

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
        return null;
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
            sm_so_product_event.setFlag_inspection(cursor.getInt(cursor.getColumnIndex(FLAG_APPLY)));
            sm_so_product_event.setFlag_repair(cursor.getInt(cursor.getColumnIndex(FLAG_REPAIR)));
            if (cursor.isNull(cursor.getColumnIndex(QTY_APPLY))) {
                sm_so_product_event.setQty_apply(null);
            } else {
                sm_so_product_event.setQty_apply(cursor.getInt(cursor.getColumnIndex(QTY_APPLY)));
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
            contentValues.put(SKETCH_CODE, sm_so_product_event.getSketch_code());
            contentValues.put(SKETCH_NAME, sm_so_product_event.getSketch_name());
            contentValues.put(SKETCH_URL, sm_so_product_event.getSketch_url());
            contentValues.put(SKETCH_LINES, sm_so_product_event.getSketch_lines());
            contentValues.put(SKETCH_COLUMNS, sm_so_product_event.getSketch_columns());
            contentValues.put(SKETCH_COLOR, sm_so_product_event.getSketch_color());
            contentValues.put(COMMENTS, sm_so_product_event.getComments());
            //
            if (sm_so_product_event.getStatus() != null) {
                contentValues.put(STATUS, sm_so_product_event.getStatus());

            }

            return contentValues;
        }
    }
}
