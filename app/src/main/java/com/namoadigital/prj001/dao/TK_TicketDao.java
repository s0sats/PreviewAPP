package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.util.Constant;

import java.util.List;

public class TK_TicketDao extends BaseDao implements DaoWithReturn<TK_Ticket>{
    private final Mapper<TK_Ticket, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, TK_Ticket> toTK_TicketMapper;

    public static final String TABLE = "tk_ticket";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String TICKET_PREFIX = "ticket_prefix";
    public static final String TICKET_CODE = "ticket_code";
    public static final String SCN = "scn";
    public static final String TICKET_ID = "ticket_id";
    public static final String TYPE_CODE = "type_code";
    public static final String TYPE_ID = "type_id";
    public static final String TYPE_DESC = "type_desc";
    public static final String TYPE_PATH = "type_path";
    public static final String OPEN_COMMENTS = "open_comments";
    public static final String OPEN_PHOTO = "open_photo";
    public static final String OPEN_PHOTO_LOCAL = "open_photo_local";
    public static final String OPEN_NAME = "open_name";
    public static final String OPEN_EMAIL = "open_email";
    public static final String OPEN_PHONE = "open_phone";
    public static final String OPEN_DATE = "open_date";
    public static final String OPEN_USER = "open_user";
    public static final String OPEN_USER_NAME = "open_user_name";
    public static final String INTERNAL_COMMENTS = "internal_comments";
    public static final String CURRENT_SITE_CODE = "current_site_code";
    public static final String CURRENT_SITE_ID = "current_site_id";
    public static final String CURRENT_SITE_DESC = "current_site_desc";
    public static final String CURRENT_OPERATION_CODE = "current_operation_code";
    public static final String CURRENT_OPERATION_ID = "current_operation_id";
    public static final String CURRENT_OPERATION_DESC = "current_operation_desc";
    public static final String CURRENT_PRODUCT_CODE = "current_product_code";
    public static final String CURRENT_PRODUCT_ID = "current_product_id";
    public static final String CURRENT_PRODUCT_DESC = "current_product_desc";
    public static final String CURRENT_SERIAL_CODE = "current_serial_code";
    public static final String CURRENT_SERIAL_ID = "current_serial_id";
    public static final String FORECAST_DATE = "forecast_date";
    public static final String TICKET_STATUS = "ticket_status";
    public static final String CLOSE_DATE = "close_date";
    public static final String CLOSE_USER = "close_user";
    public static final String CLOSE_USER_NAME = "close_user_name";
    public static final String DURATION_MINUTES = "duration_minutes";
    public static final String BARCODE_CODE = "barcode_code";
    public static final String CHECKIN_DATE = "checkin_date";
    public static final String CHECKIN_USER = "checkin_user";
    public static final String CHECKIN_USER_NAME = "checkin_user_name";

    public TK_TicketDao(Context context, String mDB_NAME, int mDB_VERSION) {
        super(context, mDB_NAME, mDB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new TK_TicketToContentValuesMapper();
        this.toTK_TicketMapper = new CursorToTK_TicketMapper();
    }


    @Override
    public DaoObjReturn addUpdate(TK_Ticket tk_ticket) {
        return addUpdate(tk_ticket,null);
    }

    public DaoObjReturn addUpdate(TK_Ticket tk_ticket, SQLiteDatabase dbInstance) {
        return null;
    }

    @Override
    public DaoObjReturn addUpdate(List<TK_Ticket> tk_tickets, boolean status) {
        return addUpdate(tk_tickets,status,null);
    }
    public DaoObjReturn addUpdate(List<TK_Ticket> tk_tickets, boolean status, SQLiteDatabase dbInstance) {
        return null;
    }

    @Override
    public void addUpdate(String sQuery) {

    }

    @Override
    public void remove(String sQuery) {

    }

    @Override
    public TK_Ticket getByString(String sQuery) {
        return null;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        return null;
    }

    @Override
    public List<TK_Ticket> query(String sQuery) {
        return null;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        return null;
    }

    private class CursorToTK_TicketMapper implements Mapper<Cursor, TK_Ticket> {
        @Override
        public TK_Ticket map(Cursor cursor) {
            TK_Ticket tk_ticket = new TK_Ticket();
            //
            tk_ticket.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            tk_ticket.setTicket_prefix(cursor.getInt(cursor.getColumnIndex(TICKET_PREFIX)));
            tk_ticket.setTicket_code(cursor.getInt(cursor.getColumnIndex(TICKET_CODE)));
            tk_ticket.setScn(cursor.getInt(cursor.getColumnIndex(SCN)));
            tk_ticket.setTicket_id(cursor.getString(cursor.getColumnIndex(TICKET_ID)));
            tk_ticket.setType_code(cursor.getInt(cursor.getColumnIndex(TYPE_CODE)));
            tk_ticket.setType_id(cursor.getString(cursor.getColumnIndex(TYPE_ID)));
            tk_ticket.setType_desc(cursor.getString(cursor.getColumnIndex(TYPE_DESC)));
            if(cursor.isNull(cursor.getColumnIndex(TYPE_PATH))){
                tk_ticket.setType_path(null);
            }else{
                tk_ticket.setType_path(cursor.getString(cursor.getColumnIndex(TYPE_PATH)));
            }
            if(cursor.isNull(cursor.getColumnIndex(OPEN_COMMENTS))){
                tk_ticket.setOpen_comments(null);
            }else{
                tk_ticket.setOpen_comments(cursor.getString(cursor.getColumnIndex(OPEN_COMMENTS)));
            }
            if(cursor.isNull(cursor.getColumnIndex(OPEN_PHOTO))){
                tk_ticket.setOpen_photo(null);
            }else{
                tk_ticket.setOpen_photo(cursor.getString(cursor.getColumnIndex(OPEN_PHOTO)));
            }
            if(cursor.isNull(cursor.getColumnIndex(OPEN_PHOTO_LOCAL))){
                tk_ticket.setOpen_photo_local(null);
            }else{
                tk_ticket.setOpen_photo_local(cursor.getString(cursor.getColumnIndex(OPEN_PHOTO_LOCAL)));
            }
            if(cursor.isNull(cursor.getColumnIndex(OPEN_NAME))){
                tk_ticket.setOpen_name(null);
            }else{
                tk_ticket.setOpen_name(cursor.getString(cursor.getColumnIndex(OPEN_NAME)));
            }
            if(cursor.isNull(cursor.getColumnIndex(OPEN_EMAIL))){
                tk_ticket.setOpen_email(null);
            }else{
                tk_ticket.setOpen_email(cursor.getString(cursor.getColumnIndex(OPEN_EMAIL)));
            }
            if(cursor.isNull(cursor.getColumnIndex(OPEN_PHONE))){
                tk_ticket.setOpen_phone(null);
            }else{
                tk_ticket.setOpen_phone(cursor.getString(cursor.getColumnIndex(OPEN_PHONE)));
            }
            tk_ticket.setOpen_date(cursor.getString(cursor.getColumnIndex(OPEN_DATE)));
            tk_ticket.setOpen_user(cursor.getInt(cursor.getColumnIndex(OPEN_USER)));
            tk_ticket.setOpen_user_name(cursor.getString(cursor.getColumnIndex(OPEN_USER_NAME)));
            if(cursor.isNull(cursor.getColumnIndex(INTERNAL_COMMENTS))){
                tk_ticket.setInternal_comments(null);
            }else{
                tk_ticket.setInternal_comments(cursor.getString(cursor.getColumnIndex(INTERNAL_COMMENTS)));
            }
            tk_ticket.setCurrent_site_code(cursor.getInt(cursor.getColumnIndex(CURRENT_SITE_CODE)));
            tk_ticket.setCurrent_site_id(cursor.getString(cursor.getColumnIndex(CURRENT_SITE_ID)));
            tk_ticket.setCurrent_site_desc(cursor.getString(cursor.getColumnIndex(CURRENT_SITE_DESC)));
            tk_ticket.setCurrent_operation_code(cursor.getInt(cursor.getColumnIndex(CURRENT_OPERATION_CODE)));
            tk_ticket.setCurrent_operation_id(cursor.getString(cursor.getColumnIndex(CURRENT_OPERATION_ID)));
            tk_ticket.setCurrent_operation_desc(cursor.getString(cursor.getColumnIndex(CURRENT_OPERATION_DESC)));
            tk_ticket.setCurrent_product_code(cursor.getInt(cursor.getColumnIndex(CURRENT_PRODUCT_CODE)));
            tk_ticket.setCurrent_product_id(cursor.getString(cursor.getColumnIndex(CURRENT_PRODUCT_ID)));
            tk_ticket.setCurrent_product_desc(cursor.getString(cursor.getColumnIndex(CURRENT_PRODUCT_DESC)));
            tk_ticket.setCurrent_serial_code(cursor.getInt(cursor.getColumnIndex(CURRENT_SERIAL_CODE)));
            tk_ticket.setCurrent_serial_id(cursor.getString(cursor.getColumnIndex(CURRENT_SERIAL_ID)));
            if(cursor.isNull(cursor.getColumnIndex(FORECAST_DATE))){
                tk_ticket.setForecast_date(null);
            }else{
                tk_ticket.setForecast_date(cursor.getString(cursor.getColumnIndex(FORECAST_DATE)));
            }
            tk_ticket.setTicket_status(cursor.getString(cursor.getColumnIndex(TICKET_STATUS)));
            if(cursor.isNull(cursor.getColumnIndex(CLOSE_DATE))){
                tk_ticket.setClose_date(null);
            }else{
                tk_ticket.setClose_date(cursor.getString(cursor.getColumnIndex(CLOSE_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CLOSE_USER))){
                tk_ticket.setClose_user(null);
            }else{
                tk_ticket.setClose_user(cursor.getInt(cursor.getColumnIndex(CLOSE_USER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CLOSE_USER_NAME))){
                tk_ticket.setClose_user_name(null);
            }else{
                tk_ticket.setClose_user_name(cursor.getString(cursor.getColumnIndex(CLOSE_USER_NAME)));
            }
            if(cursor.isNull(cursor.getColumnIndex(DURATION_MINUTES))){
                tk_ticket.setDuration_minutes(null);
            }else{
                tk_ticket.setDuration_minutes(cursor.getInt(cursor.getColumnIndex(DURATION_MINUTES)));
            }
            if(cursor.isNull(cursor.getColumnIndex(BARCODE_CODE))){
                tk_ticket.setBarcode_code(null);
            }else{
                tk_ticket.setBarcode_code(cursor.getInt(cursor.getColumnIndex(BARCODE_CODE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CHECKIN_DATE))){
                tk_ticket.setCheckin_date(null);
            }else{
                tk_ticket.setCheckin_date(cursor.getString(cursor.getColumnIndex(CHECKIN_DATE)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CHECKIN_USER))){
                tk_ticket.setCheckin_user(null);
            }else{
                tk_ticket.setCheckin_user(cursor.getInt(cursor.getColumnIndex(CHECKIN_USER)));
            }
            if(cursor.isNull(cursor.getColumnIndex(CHECKIN_USER_NAME))){
                tk_ticket.setCheckin_user_name(null);
            }else{
                tk_ticket.setCheckin_user_name(cursor.getString(cursor.getColumnIndex(CHECKIN_USER_NAME)));
            }
            //
            return tk_ticket;
        }
    }

    private class TK_TicketToContentValuesMapper implements Mapper<TK_Ticket, ContentValues> {
        @Override
        public ContentValues map(TK_Ticket tk_ticket) {
            ContentValues contentValues = new ContentValues();
            //
            //@TODO Tratar os campos que não podem ser null.
            //@TODO ADD CAMPOS _LOCAL NO DATABASE_HELPER
            contentValues.put(CUSTOMER_CODE,tk_ticket.getCustomer_code());
            contentValues.put(TICKET_PREFIX,tk_ticket.getTicket_prefix());
            contentValues.put(TICKET_CODE,tk_ticket.getTicket_code());
            contentValues.put(SCN,tk_ticket.getScn());
            contentValues.put(TICKET_ID,tk_ticket.getTicket_id());
            contentValues.put(TYPE_CODE,tk_ticket.getType_code());
            contentValues.put(TYPE_ID,tk_ticket.getType_id());
            contentValues.put(TYPE_DESC,tk_ticket.getType_desc());
            contentValues.put(TYPE_PATH,tk_ticket.getType_path());
            contentValues.put(OPEN_COMMENTS,tk_ticket.getOpen_comments());
            contentValues.put(OPEN_PHOTO,tk_ticket.getOpen_photo());
            contentValues.put(OPEN_NAME,tk_ticket.getOpen_name());
            contentValues.put(OPEN_EMAIL,tk_ticket.getOpen_email());
            contentValues.put(OPEN_PHONE,tk_ticket.getOpen_phone());
            contentValues.put(OPEN_DATE,tk_ticket.getOpen_date());
            contentValues.put(OPEN_USER,tk_ticket.getOpen_user());
            contentValues.put(OPEN_USER_NAME,tk_ticket.getOpen_user_name());
            contentValues.put(INTERNAL_COMMENTS,tk_ticket.getInternal_comments());
            contentValues.put(CURRENT_SITE_CODE,tk_ticket.getCurrent_site_code());
            contentValues.put(CURRENT_SITE_ID,tk_ticket.getCurrent_site_id());
            contentValues.put(CURRENT_SITE_DESC,tk_ticket.getCurrent_site_desc());
            contentValues.put(CURRENT_OPERATION_CODE,tk_ticket.getCurrent_operation_code());
            contentValues.put(CURRENT_OPERATION_ID,tk_ticket.getCurrent_operation_id());
            contentValues.put(CURRENT_OPERATION_DESC,tk_ticket.getCurrent_operation_desc());
            contentValues.put(CURRENT_PRODUCT_CODE,tk_ticket.getCurrent_product_code());
            contentValues.put(CURRENT_PRODUCT_ID,tk_ticket.getCurrent_product_id());
            contentValues.put(CURRENT_PRODUCT_DESC,tk_ticket.getCurrent_product_desc());
            contentValues.put(CURRENT_SERIAL_CODE,tk_ticket.getCurrent_serial_code());
            contentValues.put(CURRENT_SERIAL_ID,tk_ticket.getCurrent_serial_id());
            contentValues.put(FORECAST_DATE,tk_ticket.getForecast_date());
            contentValues.put(TICKET_STATUS,tk_ticket.getTicket_status());
            contentValues.put(CLOSE_DATE,tk_ticket.getClose_date());
            contentValues.put(CLOSE_USER,tk_ticket.getClose_user());
            contentValues.put(CLOSE_USER_NAME,tk_ticket.getClose_user_name());
            contentValues.put(DURATION_MINUTES,tk_ticket.getDuration_minutes());
            contentValues.put(BARCODE_CODE,tk_ticket.getBarcode_code());
            contentValues.put(CHECKIN_DATE,tk_ticket.getCheckin_date());
            contentValues.put(CHECKIN_USER,tk_ticket.getCheckin_user());
            contentValues.put(CHECKIN_USER_NAME,tk_ticket.getCheckin_user_name());
            //
            return contentValues;
        }
    }
}
