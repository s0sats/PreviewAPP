package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.CH_Room;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 29/11/2017.
 */

public class CH_RoomDao extends BaseDao implements Dao<CH_Room> {

    private final Mapper<CH_Room, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, CH_Room> toCH_RoomMapper;

    public static final String TABLE = "ch_rooms";
    public static final String ROOM_CODE = "room_code";
    public static final String ROOM_TYPE = "room_type";
    public static final String ROOM_DESC = "room_desc";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String ROOM_OBJ = "room_obj";
    public static final String ROOM_IMAGE = "room_image";
    public static final String ROOM_IMAGE_LOCAL = "room_image_local";
    public static final String FIRST_MSG_PREFIX = "first_msg_prefix";
    public static final String FIRST_MSG_CODE = "first_msg_code";

    public static String[] columns = {
            ROOM_CODE, ROOM_TYPE, ROOM_DESC, CUSTOMER_CODE, ROOM_OBJ, ROOM_IMAGE,
            ROOM_IMAGE_LOCAL, FIRST_MSG_PREFIX, FIRST_MSG_CODE

    };

    public CH_RoomDao(Context context) {
        super(context, Constant.DB_FULL_CHAT, Constant.DB_VERSION_CHAT, Constant.DB_MODE_CHAT);
        //
        this.toContentValuesMapper = new CH_RoomtoContentValuesMapper();
        this.toCH_RoomMapper = new CursortoCH_RoomMapper();

    }

    public CH_RoomDao(Context context, String mDB_NAME, int mDB_VERSION, String mMode) {
        super(context, mDB_NAME, mDB_VERSION, mMode);
        //
        //
        this.toContentValuesMapper = new CH_RoomtoContentValuesMapper();
        this.toCH_RoomMapper = new CursortoCH_RoomMapper();
    }

    @Override
    public void addUpdate(CH_Room ch_room) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(ch_room)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(ROOM_CODE).append(" = '").append(String.valueOf(ch_room.getRoom_code())).append("'");
                db.update(TABLE, toContentValuesMapper.map(ch_room), sbWhere.toString(), null);
            }

        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<CH_Room> ch_rooms, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (CH_Room ch_room : ch_rooms) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(ch_room)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(ROOM_CODE).append(" = '").append(String.valueOf(ch_room.getRoom_code())).append("'");
                    db.update(TABLE, toContentValuesMapper.map(ch_room), sbWhere.toString(), null);
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
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public CH_Room getByString(String sQuery) {
        CH_Room ch_room = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                ch_room = toCH_RoomMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return ch_room;
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
    public List<CH_Room> query(String sQuery) {
        List<CH_Room> ch_rooms = new ArrayList<>();

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                CH_Room uAux = toCH_RoomMapper.map(cursor);
                ch_rooms.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();


        return ch_rooms;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        List<HMAux> ch_rooms = new ArrayList<>();

        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                ch_rooms.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return ch_rooms;
    }

    private class CursortoCH_RoomMapper implements Mapper<Cursor, CH_Room> {
        @Override
        public CH_Room map(Cursor cursor) {
            CH_Room ch_room = new CH_Room();
            //
            ch_room.setRoom_code(cursor.getString(cursor.getColumnIndex(ROOM_CODE)));
            ch_room.setRoom_type(cursor.getString(cursor.getColumnIndex(ROOM_TYPE)));
            ch_room.setRoom_desc(cursor.getString(cursor.getColumnIndex(ROOM_DESC)));
            if(!cursor.isNull(cursor.getColumnIndex(CUSTOMER_CODE))){
                ch_room.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            }else{
                ch_room.setCustomer_code(null);
            }
            ch_room.setRoom_obj(cursor.getString(cursor.getColumnIndex(ROOM_OBJ)));
            if(!cursor.isNull(cursor.getColumnIndex(ROOM_IMAGE))){
                ch_room.setRoom_image(cursor.getString(cursor.getColumnIndex(ROOM_IMAGE)));
            }else{
                ch_room.setRoom_image(null);
            }
            if(!cursor.isNull(cursor.getColumnIndex(ROOM_IMAGE_LOCAL))){
                ch_room.setRoom_image_local(cursor.getString(cursor.getColumnIndex(ROOM_IMAGE_LOCAL)));
            }else{
                ch_room.setRoom_image_local(null);
            }
            ch_room.setFirst_msg_prefix(cursor.getInt(cursor.getColumnIndex(FIRST_MSG_PREFIX)));
            ch_room.setFirst_msg_code(cursor.getInt(cursor.getColumnIndex(FIRST_MSG_CODE)));
            return ch_room;
        }
    }

    private class CH_RoomtoContentValuesMapper implements Mapper<CH_Room, ContentValues> {
        @Override
        public ContentValues map(CH_Room ch_room) {
            ContentValues contentValues = new ContentValues();

            if (ch_room.getRoom_code() != null) {
                contentValues.put(ROOM_CODE, ch_room.getRoom_code());
            }

            if (ch_room.getRoom_type() != null) {
                contentValues.put(ROOM_TYPE, ch_room.getRoom_type());
            }

            if (ch_room.getRoom_desc() != null) {
                contentValues.put(ROOM_DESC, ch_room.getRoom_desc());
            }

            contentValues.put(CUSTOMER_CODE, ch_room.getCustomer_code());
            contentValues.put(ROOM_OBJ, ch_room.getRoom_obj());
            contentValues.put(ROOM_IMAGE, ch_room.getRoom_image());
            contentValues.put(ROOM_IMAGE_LOCAL, ch_room.getRoom_image_local());
            if (ch_room.getFirst_msg_prefix() > -1) {
                contentValues.put(FIRST_MSG_PREFIX, ch_room.getFirst_msg_prefix());
            }
            if (ch_room.getFirst_msg_code() > -1) {
                contentValues.put(FIRST_MSG_CODE, ch_room.getFirst_msg_code());
            }
            return contentValues;
        }
    }


}
