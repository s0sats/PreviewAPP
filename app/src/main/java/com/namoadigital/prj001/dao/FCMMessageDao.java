package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.FCMMessage;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 12/04/17.
 */

public class FCMMessageDao extends BaseDao implements Dao<FCMMessage> {

    private final Mapper<FCMMessage, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, FCMMessage> toFCMMessageMapper;

    public static final String TABLE = "fcmmessages";
    public static final String FCMMESSAGE_CODE = "fcmmessage_code";
    public static final String CUSTOMER = "customer";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String MSG_SHORT = "msg_short";
    public static final String MSG_LONG = "msg_long";
    public static final String MODULE = "module";
    public static final String SENDER = "sender";
    public static final String SYNC = "sync";
    public static final String STATUS = "status";
    public static final String DATE_CREATE = "date_create";
    public static final String DATE_CREATE_MS = "date_create_ms";


    public FCMMessageDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_SINGLE);

        this.toContentValuesMapper = new FCMMessageToContentValuesMapper();
        this.toFCMMessageMapper = new CursorFCMMessageMapper();
    }

    @Override
    public void addUpdate(FCMMessage fcmMessage) {
        try {

            openDB();

            if (db.insert(TABLE, null, toContentValuesMapper.map(fcmMessage)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(FCMMESSAGE_CODE).append(" = '").append(fcmMessage.getFcmmessage_code()).append("'");

                db.update(TABLE, toContentValuesMapper.map(fcmMessage), sbWhere.toString(), null);
            }

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<FCMMessage> fcmMessages, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (FCMMessage fcmMessage : fcmMessages) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(fcmMessage)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(FCMMESSAGE_CODE).append(" = '").append(fcmMessage.getFcmmessage_code()).append("'");

                    db.update(TABLE, toContentValuesMapper.map(fcmMessage), sbWhere.toString(), null);
                }
            }

            //db.setTransactionSuccessful();
        } catch (Exception e) {
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
    public FCMMessage getByString(String sQuery) {
        FCMMessage fcmMessage = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                fcmMessage = toFCMMessageMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return fcmMessage;
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

        } finally {
        }

        closeDB();

        return hmAux;
    }

    @Override
    public List<FCMMessage> query(String sQuery) {
        List<FCMMessage> fcmMessages = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                FCMMessage uAux = toFCMMessageMapper.map(cursor);
                fcmMessages.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return fcmMessages;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> fcmMessages = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                fcmMessages.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {

        } finally {
        }

        closeDB();

        return fcmMessages;
    }

    private class CursorFCMMessageMapper implements Mapper<Cursor, FCMMessage> {
        @Override
        public FCMMessage map(Cursor cursor) {
            FCMMessage fcmMessage = new FCMMessage();

            fcmMessage.setFcmmessage_code(cursor.getInt(cursor.getColumnIndex(FCMMESSAGE_CODE)));
            fcmMessage.setCustomer(cursor.getString(cursor.getColumnIndex(CUSTOMER)));
            fcmMessage.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
            fcmMessage.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            fcmMessage.setMsg_short(cursor.getString(cursor.getColumnIndex(MSG_SHORT)));
            fcmMessage.setMsg_long(cursor.getString(cursor.getColumnIndex(MSG_LONG)));
            fcmMessage.setModule(cursor.getString(cursor.getColumnIndex(MODULE)));
            fcmMessage.setSender(cursor.getString(cursor.getColumnIndex(SENDER)));
            fcmMessage.setSync(cursor.getString(cursor.getColumnIndex(SYNC)));
            fcmMessage.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            fcmMessage.setDate_create(cursor.getString(cursor.getColumnIndex(DATE_CREATE)));
            fcmMessage.setDate_create_ms(cursor.getLong(cursor.getColumnIndex(DATE_CREATE_MS)));

            return fcmMessage;
        }

    }

    private class FCMMessageToContentValuesMapper implements Mapper<FCMMessage, ContentValues> {
        @Override
        public ContentValues map(FCMMessage fcmMessage) {
            ContentValues contentValues = new ContentValues();

            if (fcmMessage.getFcmmessage_code() != -1) {
                contentValues.put(FCMMESSAGE_CODE, fcmMessage.getFcmmessage_code());
            }
            if (fcmMessage.getCustomer() != null) {
                contentValues.put(CUSTOMER, fcmMessage.getCustomer());
            }
            if (fcmMessage.getType() != null) {
                contentValues.put(TYPE, fcmMessage.getType());
            }
            if (fcmMessage.getTitle() != null) {
                contentValues.put(TITLE, fcmMessage.getTitle());
            }
            if (fcmMessage.getMsg_short() != null) {
                contentValues.put(MSG_SHORT, fcmMessage.getMsg_short());
            }
            if (fcmMessage.getMsg_long() != null) {
                contentValues.put(MSG_LONG, fcmMessage.getMsg_long());
            }
            if (fcmMessage.getModule() != null) {
                contentValues.put(MODULE, fcmMessage.getModule());
            }
            if (fcmMessage.getSender() != null) {
                contentValues.put(SENDER, fcmMessage.getSender());
            }
            if (fcmMessage.getSync() != null) {
                contentValues.put(SYNC, fcmMessage.getSync());
            }
            if (fcmMessage.getStatus() != null) {
                contentValues.put(STATUS, fcmMessage.getStatus());
            }
            if (fcmMessage.getDate_create() != null) {
                contentValues.put(DATE_CREATE, fcmMessage.getDate_create());
            }
            if (fcmMessage.getDate_create_ms() != -1) {
                contentValues.put(DATE_CREATE_MS, fcmMessage.getDate_create_ms());
            }

            return contentValues;
        }
    }

}
