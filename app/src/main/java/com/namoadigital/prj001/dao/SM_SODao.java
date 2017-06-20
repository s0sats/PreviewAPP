package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.util.Constant;

import java.util.List;

/**
 * Created by d.luche on 20/06/2017.
 */

public class SM_SODao extends BaseDao implements Dao<SM_SO> {

    private final Mapper<SM_SO, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SM_SO> toSM_SOMapper;

    public static final String TABLE = "sm_sos";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX =  "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String SO_DESC = "so_desc";
    public static final String SO_SCN = "so_scn" ;
    public static final String PRODUCT_CODE = "product_code";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_DESC = "product_desc";
    public static final String SERIAL_CODE = "serial_code";
    public static final String SERIAL_ID = "serial_id";
    public static final String CATEGORY_PRICE_CODE = "category_price_code";
    public static final String CATEGORY_PRICE_ID = "category_price_id";
    public static final String CATEGORY_PRICE_DESC = "category_price_desc";
    public static final String SEGMENT_CODE = "segment_code";
    public static final String SEGMENT_ID = "segment_id";
    public static final String SEGMENT_DESC = "segment_desc";
    public static final String SITE_CODE = "site_code";
    public static final String SITE_ID = "site_id";
    public static final String SITE_DESC = "site_desc";
    public static final String OPERATION_CODE = "operation_code";
    public static final String OPERATION_ID = "operation_id";
    public static final String OPERATION_DESC = "operation_desc";
    public static final String CONTRACT_CODE = "contract_code";
    public static final String CONTRACT_DESC = "contract_desc";
    public static final String CONTRACT_PO_ERP = "contract_po_erp";
    public static final String CONTRACT_PO_CLIENT1 = "contract_po_client1";
    public static final String CONTRACT_PO_CLIENT2 = "contract_po_client2";
    public static final String PRIORITY_CODE = "priority_code";
    public static final String PRIORITY_DESC = "priority_desc";
    public static final String STATUS = "status";
    public static final String QUALITY_APPROVAL_USER = "quality_approval_user";
    public static final String QUALITY_APPROVAL_DATE = "quality_approval_date";
    public static final String COMMENTS = "comments";
    public static final String SO_FATHER_PREFIX = "so_father_prefix";
    public static final String SO_FATHER_CODE = "so_father_code";
    public static final String DEADLINE = "deadline";
    public static final String ORIGIN = "origin";
    public static final String CLIENT_TYPE = "client_type";
    public static final String CLIENT_USER = "client_user";
    public static final String CLIENT_CODE = "client_code";
    public static final String CLIENT_ID = "client_id";
    public static final String CLIENT_NAME = "client_name";
    public static final String CLIENT_EMAIL = "client_email";
    public static final String CLIENT_PHONE = "client_phone";
    public static final String CLIENT_APPROVAL_IMAGE = "client_approval_image";
    public static final String CLIENT_APPROVAL_DATE = "client_approval_date";
    public static final String CLIENT_APPROVAL_FLAG = "client_approval_flag";

    public SM_SODao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SM_SOToContentValuesMapper() ;
        this.toSM_SOMapper = new CursorSM_SOMapper();
    }


    @Override
    public void addUpdate(SM_SO item) {

    }

    @Override
    public void addUpdate(Iterable<SM_SO> items, boolean status) {

    }

    @Override
    public void addUpdate(String sQuery) {

    }

    @Override
    public void remove(String sQuery) {

    }

    @Override
    public SM_SO getByString(String sQuery) {
        return null;
    }

    @Override
    public HMAux getByStringHM(String sQuery) {
        return null;
    }

    @Override
    public List<SM_SO> query(String sQuery) {
        return null;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {
        return null;
    }

    private class CursorSM_SOMapper implements Mapper<Cursor,SM_SO> {
        @Override
        public SM_SO map(Cursor cursor) {
            SM_SO so =  new SM_SO();

            so.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            so.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            so.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            so.setSo_desc(cursor.getString(cursor.getColumnIndex(SO_DESC)));
            so.setSo_scn(cursor.getInt(cursor.getColumnIndex(SO_SCN)));
            so.setProduct_code(cursor.getInt(cursor.getColumnIndex(PRODUCT_CODE)));
            so.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
            so.setProduct_desc(cursor.getString(cursor.getColumnIndex(PRODUCT_DESC)));
            so.setSerial_code(cursor.getInt(cursor.getColumnIndex(SERIAL_CODE)));
            so.setSerial_id(cursor.getString(cursor.getColumnIndex(SERIAL_ID)));
            so.setCategory_price_code(cursor.getInt(cursor.getColumnIndex(CATEGORY_PRICE_CODE)));
            so.setCategory_price_id(cursor.getString(cursor.getColumnIndex(CATEGORY_PRICE_ID)));
            so.setCategory_price_desc(cursor.getString(cursor.getColumnIndex(CATEGORY_PRICE_DESC)));
            so.setSegment_code(cursor.getInt(cursor.getColumnIndex(SEGMENT_CODE)));
            so.setSegment_id(cursor.getString(cursor.getColumnIndex(SEGMENT_ID)));
            so.setSegment_desc(cursor.getString(cursor.getColumnIndex(SEGMENT_DESC)));
            so.setSite_code(cursor.getInt(cursor.getColumnIndex(SITE_CODE)));
            so.setSite_id(cursor.getString(cursor.getColumnIndex(SITE_ID)));
            so.setSite_desc(cursor.getString(cursor.getColumnIndex(SITE_DESC)));
            so.setOperation_code(cursor.getInt(cursor.getColumnIndex(OPERATION_CODE)));
            so.setOperation_id(cursor.getString(cursor.getColumnIndex(OPERATION_ID)));
            so.setOperation_desc(cursor.getString(cursor.getColumnIndex(OPERATION_DESC)));
            so.setContract_code(cursor.getInt(cursor.getColumnIndex(CONTRACT_CODE)));
            so.setContract_desc(cursor.getString(cursor.getColumnIndex(CONTRACT_DESC)));
            if(cursor.isNull(cursor.getColumnIndex(CONTRACT_PO_ERP))){
                so.setContract_po_erp("");
            }else{
                so.setContract_po_erp(cursor.getString(cursor.getColumnIndex(CONTRACT_PO_ERP)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(CONTRACT_PO_CLIENT1))){
                so.setContract_po_client1("");
            }else{
                so.setContract_po_client1(cursor.getString(cursor.getColumnIndex(CONTRACT_PO_CLIENT1)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(CONTRACT_PO_CLIENT2))){
                so.setContract_po_client2("");
            }else{
                so.setContract_po_client2(cursor.getString(cursor.getColumnIndex(CONTRACT_PO_CLIENT2)));
            }
            so.setPriority_code(cursor.getInt(cursor.getColumnIndex(PRIORITY_CODE)));
            so.setPriority_desc(cursor.getString(cursor.getColumnIndex(PRIORITY_DESC)));
            so.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            if(cursor.isNull(cursor.getColumnIndex(QUALITY_APPROVAL_USER))){
                so.setQuality_approval_user(null);
            }else{
                so.setQuality_approval_user(cursor.getInt(cursor.getColumnIndex(QUALITY_APPROVAL_USER)));
            }

            if(cursor.isNull(cursor.getColumnIndex(QUALITY_APPROVAL_DATE))){
                so.setQuality_approval_date("1900-01-01 00:00:00 -00:00");
            }else{
                so.setQuality_approval_date(cursor.getString(cursor.getColumnIndex(QUALITY_APPROVAL_DATE)));
            }

            if(cursor.isNull(cursor.getColumnIndex(COMMENTS))){
                so.setComments("");
            }else{
                so.setComments(cursor.getString(cursor.getColumnIndex(COMMENTS)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(SO_FATHER_PREFIX))){
                so.setSo_father_prefix(null);
            }else{
                so.setSo_father_prefix(cursor.getInt(cursor.getColumnIndex(SO_FATHER_PREFIX)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(SO_FATHER_CODE))){
                so.setSo_father_code(null);
            }else{
                so.setSo_father_code(cursor.getInt(cursor.getColumnIndex(SO_FATHER_CODE)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(DEADLINE))){
                so.setDeadline("1900-01-01 00:00:00 -00:00");
            }else{
                so.setDeadline(cursor.getString(cursor.getColumnIndex(DEADLINE)));
            }
            so.setOrigin(cursor.getString(cursor.getColumnIndex(ORIGIN)));
            so.setClient_type(cursor.getString(cursor.getColumnIndex(CLIENT_TYPE)));
            //
            if(cursor.isNull(cursor.getColumnIndex(CLIENT_USER))){
                so.setClient_user(null);
            }else{
                so.setClient_user(cursor.getInt(cursor.getColumnIndex(CLIENT_USER)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(CLIENT_CODE))){
                so.setClient_code(null);
            }else{
                so.setClient_code(cursor.getInt(cursor.getColumnIndex(CLIENT_CODE)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(CLIENT_ID))){
                so.setClient_id("");
            }else{
                so.setClient_id(cursor.getString(cursor.getColumnIndex(CLIENT_ID)));
            }
            so.setClient_name(cursor.getString(cursor.getColumnIndex(CLIENT_NAME)));
            //
            if(cursor.isNull(cursor.getColumnIndex(CLIENT_EMAIL))){
                so.setClient_email("");
            }else{
                so.setClient_email(cursor.getString(cursor.getColumnIndex(CLIENT_EMAIL)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(CLIENT_PHONE))){
                so.setClient_phone("");
            }else{
                so.setClient_phone(cursor.getString(cursor.getColumnIndex(CLIENT_PHONE)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(CLIENT_APPROVAL_IMAGE))){
                so.setClient_approval_image(null);
            }else{
                so.setClient_approval_image(cursor.getInt(cursor.getColumnIndex(CLIENT_APPROVAL_IMAGE)));
            }
            //
            if(cursor.isNull(cursor.getColumnIndex(CLIENT_APPROVAL_DATE))){
                so.setClient_approval_date("1900-01-01 00:00:00 -00:00");
            }else{
                so.setClient_approval_date(cursor.getString(cursor.getColumnIndex(CLIENT_APPROVAL_DATE)));
            }
            so.setClient_approval_flag(cursor.getInt(cursor.getColumnIndex(CLIENT_APPROVAL_FLAG)));

            return so;
        }
    }

    private class SM_SOToContentValuesMapper implements Mapper<SM_SO,ContentValues> {
        @Override
        public ContentValues map(SM_SO sm_so) {
            ContentValues contentValues = new ContentValues();

            if(sm_so.getCustomer_code() > -1){
                contentValues.put(CUSTOMER_CODE,sm_so.getCustomer_code());
            }
            if(sm_so.getSo_prefix() > -1){
                contentValues.put(SO_PREFIX,sm_so.getSo_prefix());
            }
            if(sm_so.getSo_code() > -1){
                contentValues.put(SO_CODE,sm_so.getSo_code());
            }
            if(sm_so.getSo_desc() != null){
                contentValues.put(SO_DESC,sm_so.getSo_desc());
            }
            if(sm_so.getSo_scn() > -1){
                contentValues.put(SO_SCN,sm_so.getSo_scn());
            }
            if(sm_so.getProduct_code() > -1){
                contentValues.put(PRODUCT_CODE,sm_so.getProduct_code());
            }
            if(sm_so.getProduct_id() != null){
                contentValues.put(PRODUCT_ID,sm_so.getProduct_id());
            }
            if(sm_so.getProduct_desc() != null){
                contentValues.put(PRODUCT_DESC,sm_so.getProduct_desc());
            }
            if(sm_so.getSerial_code() > -1){
                contentValues.put(SERIAL_CODE,sm_so.getSerial_code());
            }
            if(sm_so.getSerial_id() != null){
                contentValues.put(SERIAL_ID,sm_so.getSerial_id());
            }
            if(sm_so.getCategory_price_code() > -1){
                contentValues.put(CATEGORY_PRICE_CODE,sm_so.getCategory_price_code());
            }
            if(sm_so.getCategory_price_id() != null){
                contentValues.put(CATEGORY_PRICE_ID,sm_so.getCategory_price_id());
            }
            if(sm_so.getCategory_price_desc() != null){
                contentValues.put(CATEGORY_PRICE_DESC,sm_so.getCategory_price_desc());
            }
            if(sm_so.getSegment_code() > -1){
                contentValues.put(SEGMENT_CODE,sm_so.getSegment_code());
            }
            if(sm_so.getSegment_id() != null){
                contentValues.put(SEGMENT_ID,sm_so.getSegment_id());
            }
            if(sm_so.getSegment_desc() != null){
                contentValues.put(SEGMENT_DESC,sm_so.getSegment_desc());
            }
            if(sm_so.getSite_code() > -1){
                contentValues.put(SITE_CODE,sm_so.getSite_code());
            }
            if(sm_so.getSite_id() != null){
                contentValues.put(SITE_ID,sm_so.getSite_id());
            }
            if(sm_so.getSite_desc() != null){
                contentValues.put(SITE_DESC,sm_so.getSite_desc());
            }
            if(sm_so.getOperation_code() > -1){
                contentValues.put(OPERATION_CODE,sm_so.getOperation_code());
            }
            if(sm_so.getOperation_id() != null){
                contentValues.put(OPERATION_ID,sm_so.getOperation_id());
            }
            if(sm_so.getOperation_desc() != null){
                contentValues.put(OPERATION_DESC,sm_so.getOperation_desc());
            }
            if(sm_so.getContract_code() > -1){
                contentValues.put(CONTRACT_CODE,sm_so.getContract_code());
            }
            if(sm_so.getContract_desc() != null){
                contentValues.put(CONTRACT_DESC,sm_so.getContract_desc());
            }
            if(sm_so.getContract_po_erp() != null){
                contentValues.put(CONTRACT_PO_ERP,sm_so.getContract_po_erp());
            }
            if(sm_so.getContract_po_client1() != null){
                contentValues.put(CONTRACT_PO_CLIENT1,sm_so.getContract_po_client1());
            }
            if(sm_so.getContract_po_client2() != null){
                contentValues.put(CONTRACT_PO_CLIENT2,sm_so.getContract_po_client2());
            }
            if(sm_so.getPriority_code() > -1){
                contentValues.put(PRIORITY_CODE,sm_so.getPriority_code());
            }
            if(sm_so.getPriority_desc() != null){
                contentValues.put(PRIORITY_DESC,sm_so.getPriority_desc());
            }
            if(sm_so.getStatus() != null){
                contentValues.put(STATUS,sm_so.getStatus());
            }
            if(sm_so.getQuality_approval_user() > -1){
                contentValues.put(QUALITY_APPROVAL_USER,sm_so.getQuality_approval_user());
            }
            if(sm_so.getQuality_approval_date() != null){
                contentValues.put(QUALITY_APPROVAL_DATE,sm_so.getQuality_approval_date());
            }
            if(sm_so.getComments() != null){
                contentValues.put(COMMENTS,sm_so.getComments());
            }
            if(sm_so.getSo_father_prefix() > -1){
                contentValues.put(SO_FATHER_PREFIX,sm_so.getSo_father_prefix());
            }
            if(sm_so.getSo_father_code() > -1){
                contentValues.put(SO_FATHER_CODE,sm_so.getSo_father_code());
            }
            if(sm_so.getDeadline() != null){
                contentValues.put(DEADLINE,sm_so.getDeadline());
            }
            if(sm_so.getOrigin() != null){
                contentValues.put(ORIGIN,sm_so.getOrigin());
            }
            if(sm_so.getClient_type() != null){
                contentValues.put(CLIENT_TYPE,sm_so.getClient_type());
            }
            if(sm_so.getClient_user() > -1){
                contentValues.put(CLIENT_USER,sm_so.getClient_user());
            }
            if(sm_so.getClient_code() > -1){
                contentValues.put(CLIENT_CODE,sm_so.getClient_code());
            }
            if(sm_so.getClient_id() != null){
                contentValues.put(CLIENT_ID,sm_so.getClient_id());
            }
            if(sm_so.getClient_name() != null){
                contentValues.put(CLIENT_NAME,sm_so.getClient_name());
            }
            if(sm_so.getClient_email() != null){
                contentValues.put(CLIENT_EMAIL,sm_so.getClient_email());
            }
            if(sm_so.getClient_phone() != null){
                contentValues.put(CLIENT_PHONE,sm_so.getClient_phone());
            }
            if(sm_so.getClient_approval_image() > -1){
                contentValues.put(CLIENT_APPROVAL_IMAGE,sm_so.getClient_approval_image());
            }
            if(sm_so.getClient_approval_date() != null){
                contentValues.put(CLIENT_APPROVAL_DATE,sm_so.getClient_approval_date());
            }
            if(sm_so.getClient_approval_flag() > -1){
                contentValues.put(CLIENT_APPROVAL_FLAG,sm_so.getClient_approval_flag());
            }

            return contentValues;
        }
    }
}
