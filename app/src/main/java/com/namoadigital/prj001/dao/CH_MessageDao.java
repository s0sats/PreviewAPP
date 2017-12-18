package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.CH_Message;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 01/12/2017.
 */

public class CH_MessageDao extends BaseDao implements Dao<CH_Message>, DaoTmp<CH_Message> {


    private final Mapper<CH_Message, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, CH_Message> toCH_MessageMapper;


    public static final String TABLE = "ch_messages";
    public static final String MSG_PREFIX = "msg_prefix";
    public static final String MSG_CODE = "msg_code";
    public static final String TMP = "tmp";
    public static final String ROOM_CODE = "room_code";
    public static final String MSG_DATE = "msg_date";
    public static final String MSG_OBJ = "msg_obj";
    public static final String MESSAGE_IMAGE_LOCAL = "message_image_local";
    public static final String MSG_ORIGIN = "msg_origin";
    public static final String DELIVERED = "delivered";
    public static final String DELIVERED_DATE = "delivered_date";
    public static final String READ = "read";
    public static final String READ_DATE = "read_date";
    public static final String MSG_PK = "msg_pk";
    public static final String USER_CODE = "user_code";
    public static final String USER_NICK = "user_nick";
    public static final String STATUS_UPDATE = "status_update";

    public static String[] columns = {
            MSG_PREFIX, MSG_CODE, TMP, ROOM_CODE, MSG_DATE, MSG_OBJ,
            MESSAGE_IMAGE_LOCAL, MSG_ORIGIN, DELIVERED, DELIVERED_DATE,
            READ, READ_DATE, MSG_PK, USER_CODE, USER_NICK, STATUS_UPDATE
    };

    public CH_MessageDao(Context context) {
        super(context, Constant.DB_FULL_CHAT, Constant.DB_VERSION_CHAT, Constant.DB_MODE_CHAT);
        //
        toContentValuesMapper = new CH_MessageToContentValuesMapper();
        toCH_MessageMapper = new CursorToCH_MessageMapper();
    }

    public CH_MessageDao(Context context, String mDB_NAME, int mDB_VERSION, String mMode) {
        super(context, mDB_NAME, mDB_VERSION, mMode);
        //
        toContentValuesMapper = new CH_MessageToContentValuesMapper();
        toCH_MessageMapper = new CursorToCH_MessageMapper();
    }

    @Override
    public void addUpdateTmp(CH_Message ch_message) {
        openDB();
        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(ch_message)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(MSG_PREFIX).append(" = '").append(String.valueOf(ch_message.getMsg_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(TMP).append(" = '").append(String.valueOf(ch_message.getTmp())).append("'");
                //
                db.update(TABLE, toContentValuesMapper.map(ch_message), sbWhere.toString(), null);
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdateTmp(Iterable<CH_Message> ch_messages, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (CH_Message ch_message : ch_messages) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(ch_message)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(MSG_PREFIX).append(" = '").append(String.valueOf(ch_message.getMsg_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(TMP).append(" = '").append(String.valueOf(ch_message.getTmp())).append("'");
                    db.update(TABLE, toContentValuesMapper.map(ch_message), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            db.endTransaction();
        }

        closeDB();
    }

    @Override
    public void addUpdate(CH_Message ch_message) {
        openDB();
        try {

            if(ch_message.getTmp() == 0) {
                //Resgata proximo tmp
                ch_message.setTmp(ToolBox_Inf.chatNextMSGCode(context));
            }

            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(MSG_PREFIX).append(" = '").append(String.valueOf(ch_message.getMsg_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(MSG_CODE).append(" = '").append(String.valueOf(ch_message.getMsg_code())).append("'");
            //
            if (db.update(TABLE, toContentValuesMapper.map(ch_message), sbWhere.toString(), null) == 0) {
                db.insert(TABLE, null, toContentValuesMapper.map(ch_message));
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<CH_Message> ch_messages, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (CH_Message ch_message : ch_messages) {

                if(ch_message.getTmp() == 0) {
                    //Resgata proximo tmp
                    ch_message.setTmp(ToolBox_Inf.chatNextMSGCode(context));
                }
                //
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(MSG_PREFIX).append(" = '").append(String.valueOf(ch_message.getMsg_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(MSG_CODE).append(" = '").append(String.valueOf(ch_message.getMsg_code())).append("'");

                //
                if (db.update(TABLE, toContentValuesMapper.map(ch_message), sbWhere.toString(), null) == 0) {
                    db.insert(TABLE, null, toContentValuesMapper.map(ch_message));
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            db.endTransaction();
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
    public CH_Message getByString(String sQuery) {
        CH_Message ch_message = new CH_Message();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                ch_message = toCH_MessageMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();


        return ch_message;
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

    private HMAux getByStringHMTmp(String sQuery) {
        HMAux hmAux = null;
        //openDB();

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

        //closeDB();

        return hmAux;
    }

    @Override
    public List<CH_Message> query(String sQuery) {
        List<CH_Message> ch_messages = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                CH_Message uAux = toCH_MessageMapper.map(cursor);
                ch_messages.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return ch_messages;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> ch_messages = new ArrayList<>();

        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                ch_messages.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return ch_messages;
    }

    private class CursorToCH_MessageMapper implements Mapper<Cursor, CH_Message> {
        @Override
        public CH_Message map(Cursor cursor) {
            CH_Message ch_message = new CH_Message();
            //
            ch_message.setMsg_prefix(cursor.getInt(cursor.getColumnIndex(MSG_PREFIX)));
            ch_message.setMsg_code(cursor.getInt(cursor.getColumnIndex(MSG_CODE)));
            ch_message.setTmp(cursor.getLong(cursor.getColumnIndex(TMP)));
            ch_message.setRoom_code(cursor.getString(cursor.getColumnIndex(ROOM_CODE)));
            ch_message.setMsg_date(cursor.getString(cursor.getColumnIndex(MSG_DATE)));
            ch_message.setMsg_obj(cursor.getString(cursor.getColumnIndex(MSG_OBJ)));
            ch_message.setMessage_image_local(cursor.getString(cursor.getColumnIndex(MESSAGE_IMAGE_LOCAL)));
            ch_message.setMsg_origin(cursor.getString(cursor.getColumnIndex(MSG_ORIGIN)));
            ch_message.setDelivered(cursor.getInt(cursor.getColumnIndex(DELIVERED)));

            if (!cursor.isNull(cursor.getColumnIndex(DELIVERED_DATE))) {
                ch_message.setDelivered_date(cursor.getString(cursor.getColumnIndex(DELIVERED_DATE)));
            } else {
                ch_message.setDelivered_date(null);
            }

            ch_message.setRead(cursor.getInt(cursor.getColumnIndex(READ)));

            if (!cursor.isNull(cursor.getColumnIndex(READ_DATE))) {
                ch_message.setRead_date(cursor.getString(cursor.getColumnIndex(READ_DATE)));
            } else {
                ch_message.setRead_date(null);
            }

            if (!cursor.isNull(cursor.getColumnIndex(MSG_PK))) {
                ch_message.setMsg_pk(cursor.getString(cursor.getColumnIndex(MSG_PK)));
            } else {
                ch_message.setMsg_pk(null);
            }

            ch_message.setUser_code(cursor.getInt(cursor.getColumnIndex(USER_CODE)));
            ch_message.setUser_nick(cursor.getString(cursor.getColumnIndex(USER_NICK)));
            ch_message.setStatus_update(cursor.getInt(cursor.getColumnIndex(STATUS_UPDATE)));

            return ch_message;
        }
    }

    private class CH_MessageToContentValuesMapper implements Mapper<CH_Message, ContentValues> {
        @Override
        public ContentValues map(CH_Message ch_message) {
            ContentValues contentValues = new ContentValues();
            //
            if (ch_message.getMsg_prefix() > -1) {
                contentValues.put(MSG_PREFIX, ch_message.getMsg_prefix());
            }
            if (ch_message.getMsg_code() > -1) {
                contentValues.put(MSG_CODE, ch_message.getMsg_code());
            }
            if (ch_message.getTmp() > -1) {
                contentValues.put(TMP, ch_message.getTmp());
            }
            if (ch_message.getRoom_code() != null) {
                contentValues.put(ROOM_CODE, ch_message.getRoom_code());
            }
            if (ch_message.getMsg_date() != null) {
                contentValues.put(MSG_DATE, ch_message.getMsg_date());
            }
            if (ch_message.getMsg_obj() != null) {
                contentValues.put(MSG_OBJ, ch_message.getMsg_obj());
            }
            if (ch_message.getMessage_image_local() != null) {
                contentValues.put(MESSAGE_IMAGE_LOCAL, ch_message.getMessage_image_local());
            }
            if (ch_message.getMsg_origin() != null) {
                contentValues.put(MSG_ORIGIN, ch_message.getMsg_origin());
            }
            if (ch_message.getDelivered() > -1) {
                contentValues.put(DELIVERED, ch_message.getDelivered());
            }

            contentValues.put(DELIVERED_DATE, ch_message.getDelivered_date());

            if (ch_message.getRead() > -1) {
                contentValues.put(READ, ch_message.getRead());
            }

            contentValues.put(READ_DATE, ch_message.getRead_date());

            //if (ch_message.getMsg_pk() != null) {
                contentValues.put(MSG_PK, ch_message.getMsg_pk());
            //}
            if (ch_message.getUser_code() > -1) {
                contentValues.put(USER_CODE, ch_message.getUser_code());
            }
            if (ch_message.getUser_nick() != null) {
                contentValues.put(USER_NICK, ch_message.getUser_nick());
            }
            if (ch_message.getStatus_update() > -1) {
                contentValues.put(STATUS_UPDATE, ch_message.getStatus_update());
            }

            return contentValues;
        }
    }


}
