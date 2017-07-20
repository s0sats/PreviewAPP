package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.model.SM_SO_File;
import com.namoadigital.prj001.model.SM_SO_Pack;
import com.namoadigital.prj001.sql.SM_SO_File_Sql_002;
import com.namoadigital.prj001.sql.SM_SO_Pack_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.luche on 20/06/2017.
 */

public class SM_SODao extends BaseDao implements Dao<SM_SO>, DaoSOFullDelete<SM_SO> {

    private final Mapper<SM_SO, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, SM_SO> toSM_SOMapper;

    public static final String TABLE = "sm_sos";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String SO_PREFIX = "so_prefix";
    public static final String SO_CODE = "so_code";
    public static final String SO_ID = "so_id";
    public static final String SO_SCN = "so_scn";
    public static final String SO_DESC = "so_desc";
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
    public static final String QUALITY_APPROVAL_USER_NICK = "quality_approval_user_nick";
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
    public static final String CLIENT_APPROVAL_IMAGE_NAME = "client_approval_image_name";
    public static final String CLIENT_APPROVAL_IMAGE_URL = "client_approval_image_url";
    public static final String CLIENT_APPROVAL_DATE = "client_approval_date";
    public static final String CLIENT_APPROVAL_USER = "client_approval_user";
    public static final String CLIENT_APPROVAL_USER_NICK = "client_approval_user_nick";
    public static final String ORIGIN_CHANGE = "origin_change";
    public static final String STARTED_FLAG = "started_flag";
    public static final String EDIT_ORIGIN = "edit_origin";
    public static final String EDIT_USER = "edit_user";
    public static final String EDIT_USER_NICK = "edit_user_nick";
    public static final String TOTAL_QTY_SERVICE = "total_qty_service";
    public static final String TOTAL_PRICE = "total_price";
    public static final String ADD_INF1 = "add_inf1";
    public static final String ADD_INF2 = "add_inf2";
    public static final String ADD_INF3 = "add_inf3";
    public static final String UPDATE_REQUIRED = "update_required";
    public static final String TOKEN = "token";

    public SM_SODao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);

        this.toContentValuesMapper = new SM_SOToContentValuesMapper();
        this.toSM_SOMapper = new CursorSM_SOMapper();
    }

    @Override
    public void addUpdate(SM_SO so) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(so)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(so.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(so.getSo_prefix())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(so.getSo_code())).append("'");

                db.update(TABLE, toContentValuesMapper.map(so), sbWhere.toString(), null);
            }

            SM_SO_FileDao sm_so_fileDao = new SM_SO_FileDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            SM_SO_PackDao sm_so_packDao = new SM_SO_PackDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            sm_so_fileDao.addUpdate(so.getSo_file(), false);
            sm_so_packDao.addUpdate(so.getPack(), false);

        } catch (Exception e) {
            String resultado = e.toString();
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<SM_SO> sos, boolean status) {
        openDB();

        try {

            //db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            SM_SO_FileDao sm_so_fileDao = new SM_SO_FileDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            SM_SO_PackDao sm_so_packDao = new SM_SO_PackDao(
                    context,
                    ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                    Constant.DB_VERSION_CUSTOM
            );

            for (SM_SO so : sos) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(so)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(so.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(so.getSo_prefix())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(so.getSo_code())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(so), sbWhere.toString(), null);
                }

                sm_so_fileDao.addUpdate(so.getSo_file(), false);
                sm_so_packDao.addUpdate(so.getPack(), false);

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
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();
    }

    @Override
    public void removeFull(SM_SO sm_so) {
        openDB();

        try {

            StringBuilder sbWhere = new StringBuilder();
            sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(sm_so.getCustomer_code())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_PREFIX).append(" = '").append(String.valueOf(sm_so.getSo_prefix())).append("'");
            sbWhere.append(" and ");
            sbWhere.append(SO_CODE).append(" = '").append(String.valueOf(sm_so.getSo_code())).append("'");

            db.beginTransaction();

            db.delete(TABLE, sbWhere.toString(), null);
            db.delete(SM_SO_FileDao.TABLE, sbWhere.toString(), null);
            db.delete(SM_SO_PackDao.TABLE, sbWhere.toString(), null);
            db.delete(SM_SO_ServiceDao.TABLE, sbWhere.toString(), null);
            db.delete(SM_SO_Service_ExecDao.TABLE, sbWhere.toString(), null);
            db.delete(SM_SO_Service_Exec_TaskDao.TABLE, sbWhere.toString(), null);
            db.delete(SM_SO_Service_Exec_Task_FileDao.TABLE, sbWhere.toString(), null);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
            db.endTransaction();
        }

        closeDB();

    }

    @Override
    public SM_SO getByString(String sQuery) {
        SM_SO so = null;

        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                so = toSM_SOMapper.map(cursor);
            }

            if (so != null) {
                SM_SO_FileDao sm_so_fileDao = new SM_SO_FileDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );

                so.setSo_file((ArrayList<SM_SO_File>) sm_so_fileDao.query(new SM_SO_File_Sql_002(
                        so.getCustomer_code(),
                        so.getSo_prefix(),
                        so.getSo_code()
                ).toSqlQuery()));

                SM_SO_PackDao sm_so_packDao = new SM_SO_PackDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                );

                so.setPack((ArrayList<SM_SO_Pack>) sm_so_packDao.query(new SM_SO_Pack_Sql_002(
                        so.getCustomer_code(),
                        so.getSo_prefix(),
                        so.getSo_code()
                ).toSqlQuery()));


            }


            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();


        return so;
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
    public List<SM_SO> query(String sQuery) {
        List<SM_SO> sos = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(sQuery, null);

            while (cursor.moveToNext()) {
                SM_SO uAux = toSM_SOMapper.map(cursor);

                if (uAux != null) {
                    SM_SO_FileDao sm_so_fileDao = new SM_SO_FileDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    uAux.setSo_file((ArrayList<SM_SO_File>) sm_so_fileDao.query(new SM_SO_File_Sql_002(
                            uAux.getCustomer_code(),
                            uAux.getSo_prefix(),
                            uAux.getSo_code()
                    ).toSqlQuery()));

                    SM_SO_PackDao sm_so_packDao = new SM_SO_PackDao(
                            context,
                            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                            Constant.DB_VERSION_CUSTOM
                    );

                    uAux.setPack((ArrayList<SM_SO_Pack>) sm_so_packDao.query(new SM_SO_Pack_Sql_002(
                            uAux.getCustomer_code(),
                            uAux.getSo_prefix(),
                            uAux.getSo_code()
                    ).toSqlQuery()));


                }

                sos.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sos;
    }

    @Override
    public List<HMAux> query_HM(String sQuery) {

        ArrayList<HMAux> sos = new ArrayList<>();
        openDB();

        String s_query_div[] = sQuery.split(";");

        Mapper<Cursor, HMAux> toHMAuxMapper = new CursorToHMAuxMapper(s_query_div[1]);

        try {

            Cursor cursor = db.rawQuery(s_query_div[0], null);

            while (cursor.moveToNext()) {
                sos.add(toHMAuxMapper.map(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(), e);
        } finally {
        }

        closeDB();

        return sos;
    }

    private class CursorSM_SOMapper implements Mapper<Cursor, SM_SO> {
        @Override
        public SM_SO map(Cursor cursor) {
            SM_SO so = new SM_SO();

            so.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            so.setSo_prefix(cursor.getInt(cursor.getColumnIndex(SO_PREFIX)));
            so.setSo_code(cursor.getInt(cursor.getColumnIndex(SO_CODE)));
            so.setSo_id(cursor.getString(cursor.getColumnIndex(SO_ID)));
            so.setSo_scn(cursor.getInt(cursor.getColumnIndex(SO_SCN)));
            so.setSo_desc(cursor.getString(cursor.getColumnIndex(SO_DESC)));
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

            if (cursor.isNull(cursor.getColumnIndex(CONTRACT_PO_ERP))) {
                so.setContract_po_erp(null);
            } else {
                so.setContract_po_erp(cursor.getString(cursor.getColumnIndex(CONTRACT_PO_ERP)));
            }

            if (cursor.isNull(cursor.getColumnIndex(CONTRACT_PO_CLIENT1))) {
                so.setContract_po_client1(null);
            } else {
                so.setContract_po_client1(cursor.getString(cursor.getColumnIndex(CONTRACT_PO_CLIENT1)));
            }

            if (cursor.isNull(cursor.getColumnIndex(CONTRACT_PO_CLIENT2))) {
                so.setContract_po_client2(null);
            } else {
                so.setContract_po_client2(cursor.getString(cursor.getColumnIndex(CONTRACT_PO_CLIENT2)));
            }

            so.setPriority_code(cursor.getInt(cursor.getColumnIndex(PRIORITY_CODE)));
            so.setPriority_desc(cursor.getString(cursor.getColumnIndex(PRIORITY_DESC)));
            so.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));

            if (cursor.isNull(cursor.getColumnIndex(QUALITY_APPROVAL_USER))) {
                so.setQuality_approval_user(null);
            } else {
                so.setQuality_approval_user(cursor.getInt(cursor.getColumnIndex(QUALITY_APPROVAL_USER)));
            }

            if (cursor.isNull(cursor.getColumnIndex(QUALITY_APPROVAL_USER_NICK))) {
                so.setQuality_approval_user_nick(null);
            } else {
                so.setQuality_approval_user_nick(cursor.getString(cursor.getColumnIndex(QUALITY_APPROVAL_USER_NICK)));
            }

            if (cursor.isNull(cursor.getColumnIndex(QUALITY_APPROVAL_DATE))) {
                so.setQuality_approval_date(null);
            } else {
                so.setQuality_approval_date(cursor.getString(cursor.getColumnIndex(QUALITY_APPROVAL_DATE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(COMMENTS))) {
                so.setComments(null);
            } else {
                so.setComments(cursor.getString(cursor.getColumnIndex(COMMENTS)));
            }

            if (cursor.isNull(cursor.getColumnIndex(SO_FATHER_PREFIX))) {
                so.setSo_father_prefix(null);
            } else {
                so.setSo_father_prefix(cursor.getInt(cursor.getColumnIndex(SO_FATHER_PREFIX)));
            }

            if (cursor.isNull(cursor.getColumnIndex(SO_FATHER_CODE))) {
                so.setSo_father_code(null);
            } else {
                so.setSo_father_code(cursor.getInt(cursor.getColumnIndex(SO_FATHER_CODE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(DEADLINE))) {
                so.setDeadline(null);
            } else {
                so.setDeadline(cursor.getString(cursor.getColumnIndex(DEADLINE)));
            }

            so.setOrigin(cursor.getString(cursor.getColumnIndex(ORIGIN)));
            so.setClient_type(cursor.getString(cursor.getColumnIndex(CLIENT_TYPE)));

            if (cursor.isNull(cursor.getColumnIndex(CLIENT_USER))) {
                so.setClient_user(null);
            } else {
                so.setClient_user(cursor.getInt(cursor.getColumnIndex(CLIENT_USER)));
            }

            if (cursor.isNull(cursor.getColumnIndex(CLIENT_CODE))) {
                so.setClient_code(null);
            } else {
                so.setClient_code(cursor.getInt(cursor.getColumnIndex(CLIENT_CODE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(CLIENT_ID))) {
                so.setClient_id(null);
            } else {
                so.setClient_id(cursor.getString(cursor.getColumnIndex(CLIENT_ID)));
            }

            so.setClient_name(cursor.getString(cursor.getColumnIndex(CLIENT_NAME)));

            if (cursor.isNull(cursor.getColumnIndex(CLIENT_EMAIL))) {
                so.setClient_email(null);
            } else {
                so.setClient_email(cursor.getString(cursor.getColumnIndex(CLIENT_EMAIL)));
            }

            if (cursor.isNull(cursor.getColumnIndex(CLIENT_PHONE))) {
                so.setClient_phone(null);
            } else {
                so.setClient_phone(cursor.getString(cursor.getColumnIndex(CLIENT_PHONE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(CLIENT_APPROVAL_IMAGE))) {
                so.setClient_approval_image(null);
            } else {
                so.setClient_approval_image(cursor.getInt(cursor.getColumnIndex(CLIENT_APPROVAL_IMAGE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(CLIENT_APPROVAL_IMAGE_NAME))) {
                so.setClient_approval_image_name(null);
            } else {
                so.setClient_approval_image_name(cursor.getString(cursor.getColumnIndex(CLIENT_APPROVAL_IMAGE_NAME)));
            }

            if (cursor.isNull(cursor.getColumnIndex(CLIENT_APPROVAL_IMAGE_URL))) {
                so.setClient_approval_image_url(null);
            } else {
                so.setClient_approval_image_url(cursor.getString(cursor.getColumnIndex(CLIENT_APPROVAL_IMAGE_URL)));
            }

            if (cursor.isNull(cursor.getColumnIndex(CLIENT_APPROVAL_DATE))) {
                so.setClient_approval_date(null);
            } else {
                so.setClient_approval_date(cursor.getString(cursor.getColumnIndex(CLIENT_APPROVAL_DATE)));
            }

            if (cursor.isNull(cursor.getColumnIndex(CLIENT_APPROVAL_USER))) {
                so.setClient_approval_user(null);
            } else {
                so.setClient_approval_user(cursor.getInt(cursor.getColumnIndex(CLIENT_APPROVAL_USER)));
            }
            if (cursor.isNull(cursor.getColumnIndex(CLIENT_APPROVAL_USER_NICK))) {
                so.setClient_approval_user_nick(null);
            } else {
                so.setClient_approval_user_nick(cursor.getString(cursor.getColumnIndex(CLIENT_APPROVAL_USER_NICK)));
            }

            so.setOrigin_change(cursor.getString(cursor.getColumnIndex(ORIGIN_CHANGE)));
            so.setStarted_flag(cursor.getInt(cursor.getColumnIndex(STARTED_FLAG)));

            if (cursor.isNull(cursor.getColumnIndex(EDIT_ORIGIN))) {
                so.setEdit_origin(null);
            } else {
                so.setEdit_origin(cursor.getString(cursor.getColumnIndex(EDIT_ORIGIN)));
            }

            if (cursor.isNull(cursor.getColumnIndex(EDIT_USER))) {
                so.setEdit_user(null);
            } else {
                so.setEdit_user(cursor.getInt(cursor.getColumnIndex(EDIT_USER)));
            }

            if (cursor.isNull(cursor.getColumnIndex(EDIT_USER_NICK))) {
                so.setEdit_user_nick(null);
            } else {
                so.setEdit_user_nick(cursor.getString(cursor.getColumnIndex(EDIT_USER_NICK)));
            }
            so.setTotal_qty_service(cursor.getInt(cursor.getColumnIndex(TOTAL_QTY_SERVICE)));
            so.setTotal_price(cursor.getDouble(cursor.getColumnIndex(TOTAL_PRICE)));

            if (cursor.isNull(cursor.getColumnIndex(ADD_INF1))) {
                so.setAdd_inf1(null);
            } else {
                so.setAdd_inf1(cursor.getString(cursor.getColumnIndex(ADD_INF1)));
            }

            if (cursor.isNull(cursor.getColumnIndex(ADD_INF2))) {
                so.setAdd_inf2(null);
            } else {
                so.setAdd_inf2(cursor.getString(cursor.getColumnIndex(ADD_INF2)));
            }

            if (cursor.isNull(cursor.getColumnIndex(ADD_INF3))) {
                so.setAdd_inf3(null);
            } else {
                so.setAdd_inf3(cursor.getString(cursor.getColumnIndex(ADD_INF3)));
            }

            if (cursor.isNull(cursor.getColumnIndex(UPDATE_REQUIRED))) {
                so.setUpdate_required(0);
            } else {
                so.setUpdate_required(cursor.getInt(cursor.getColumnIndex(UPDATE_REQUIRED)));
            }
            //
            so.setToken(cursor.getString(cursor.getColumnIndex(TOKEN)));


            return so;
        }
    }

    private class SM_SOToContentValuesMapper implements Mapper<SM_SO, ContentValues> {
        @Override
        public ContentValues map(SM_SO sm_so) {
            ContentValues contentValues = new ContentValues();

            if (sm_so.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, sm_so.getCustomer_code());
            }

            if (sm_so.getSo_prefix() > -1) {
                contentValues.put(SO_PREFIX, sm_so.getSo_prefix());
            }

            if (sm_so.getSo_code() > -1) {
                contentValues.put(SO_CODE, sm_so.getSo_code());
            }

            if (sm_so.getSo_id() != null) {
                contentValues.put(SO_ID, sm_so.getSo_id());
            }

            if (sm_so.getSo_scn() > -1) {
                contentValues.put(SO_SCN, sm_so.getSo_scn());
            }

            if (sm_so.getSo_desc() != null) {
                contentValues.put(SO_DESC, sm_so.getSo_desc());
            }

            if (sm_so.getProduct_code() > -1) {
                contentValues.put(PRODUCT_CODE, sm_so.getProduct_code());
            }

            if (sm_so.getProduct_id() != null) {
                contentValues.put(PRODUCT_ID, sm_so.getProduct_id());
            }

            if (sm_so.getProduct_desc() != null) {
                contentValues.put(PRODUCT_DESC, sm_so.getProduct_desc());
            }

            if (sm_so.getSerial_code() > -1) {
                contentValues.put(SERIAL_CODE, sm_so.getSerial_code());
            }

            if (sm_so.getSerial_id() != null) {
                contentValues.put(SERIAL_ID, sm_so.getSerial_id());
            }

            if (sm_so.getCategory_price_code() > -1) {
                contentValues.put(CATEGORY_PRICE_CODE, sm_so.getCategory_price_code());
            }

            if (sm_so.getCategory_price_id() != null) {
                contentValues.put(CATEGORY_PRICE_ID, sm_so.getCategory_price_id());
            }

            if (sm_so.getCategory_price_desc() != null) {
                contentValues.put(CATEGORY_PRICE_DESC, sm_so.getCategory_price_desc());
            }

            if (sm_so.getSegment_code() > -1) {
                contentValues.put(SEGMENT_CODE, sm_so.getSegment_code());
            }

            if (sm_so.getSegment_id() != null) {
                contentValues.put(SEGMENT_ID, sm_so.getSegment_id());
            }

            if (sm_so.getSegment_desc() != null) {
                contentValues.put(SEGMENT_DESC, sm_so.getSegment_desc());
            }

            if (sm_so.getSite_code() > -1) {
                contentValues.put(SITE_CODE, sm_so.getSite_code());
            }

            if (sm_so.getSite_id() != null) {
                contentValues.put(SITE_ID, sm_so.getSite_id());
            }

            if (sm_so.getSite_desc() != null) {
                contentValues.put(SITE_DESC, sm_so.getSite_desc());
            }

            if (sm_so.getOperation_code() > -1) {
                contentValues.put(OPERATION_CODE, sm_so.getOperation_code());
            }

            if (sm_so.getOperation_id() != null) {
                contentValues.put(OPERATION_ID, sm_so.getOperation_id());
            }

            if (sm_so.getOperation_desc() != null) {
                contentValues.put(OPERATION_DESC, sm_so.getOperation_desc());
            }

            if (sm_so.getContract_code() > -1) {
                contentValues.put(CONTRACT_CODE, sm_so.getContract_code());
            }

            if (sm_so.getContract_desc() != null) {
                contentValues.put(CONTRACT_DESC, sm_so.getContract_desc());
            }

            if (sm_so.getContract_po_erp() != null) {
                contentValues.put(CONTRACT_PO_ERP, sm_so.getContract_po_erp());
            }

            if (sm_so.getContract_po_client1() != null) {
                contentValues.put(CONTRACT_PO_CLIENT1, sm_so.getContract_po_client1());
            }

            if (sm_so.getContract_po_client2() != null) {
                contentValues.put(CONTRACT_PO_CLIENT2, sm_so.getContract_po_client2());
            }

            if (sm_so.getPriority_code() > -1) {
                contentValues.put(PRIORITY_CODE, sm_so.getPriority_code());
            }

            if (sm_so.getPriority_desc() != null) {
                contentValues.put(PRIORITY_DESC, sm_so.getPriority_desc());
            }

            if (sm_so.getStatus() != null) {
                contentValues.put(STATUS, sm_so.getStatus());
            }

            if (sm_so.getQuality_approval_user() != null) {
                contentValues.put(QUALITY_APPROVAL_USER, sm_so.getQuality_approval_user());
            }
            if (sm_so.getQuality_approval_user_nick() != null) {
                contentValues.put(QUALITY_APPROVAL_USER_NICK, sm_so.getQuality_approval_user_nick());
            }

            if (sm_so.getQuality_approval_date() != null) {
                contentValues.put(QUALITY_APPROVAL_DATE, sm_so.getQuality_approval_date());
            }

            if (sm_so.getComments() != null) {
                contentValues.put(COMMENTS, sm_so.getComments());
            }

            if (sm_so.getSo_father_prefix() != null) {
                contentValues.put(SO_FATHER_PREFIX, sm_so.getSo_father_prefix());
            }

            if (sm_so.getSo_father_code() != null) {
                contentValues.put(SO_FATHER_CODE, sm_so.getSo_father_code());
            }

            if (sm_so.getDeadline() != null) {
                contentValues.put(DEADLINE, sm_so.getDeadline());
            }

            if (sm_so.getOrigin() != null) {
                contentValues.put(ORIGIN, sm_so.getOrigin());
            }

            if (sm_so.getClient_type() != null) {
                contentValues.put(CLIENT_TYPE, sm_so.getClient_type());
            }

            if (sm_so.getClient_user() != null) {
                contentValues.put(CLIENT_USER, sm_so.getClient_user());
            }

            if (sm_so.getClient_code() != null) {
                contentValues.put(CLIENT_CODE, sm_so.getClient_code());
            }

            if (sm_so.getClient_id() != null) {
                contentValues.put(CLIENT_ID, sm_so.getClient_id());
            }

            if (sm_so.getClient_name() != null) {
                contentValues.put(CLIENT_NAME, sm_so.getClient_name());
            }

            if (sm_so.getClient_email() != null) {
                contentValues.put(CLIENT_EMAIL, sm_so.getClient_email());
            }

            if (sm_so.getClient_phone() != null) {
                contentValues.put(CLIENT_PHONE, sm_so.getClient_phone());
            }

            if (sm_so.getClient_approval_image() != null) {
                contentValues.put(CLIENT_APPROVAL_IMAGE, sm_so.getClient_approval_image());
            }

            if (sm_so.getClient_approval_image_name() != null) {
                contentValues.put(CLIENT_APPROVAL_IMAGE_NAME, sm_so.getClient_approval_image_name());
            }

            if (sm_so.getClient_approval_image_url() != null) {
                contentValues.put(CLIENT_APPROVAL_IMAGE_URL, sm_so.getClient_approval_image_url());
            }

            if (sm_so.getClient_approval_date() != null) {
                contentValues.put(CLIENT_APPROVAL_DATE, sm_so.getClient_approval_date());
            }

            if (sm_so.getClient_approval_user() != null) {
                contentValues.put(CLIENT_APPROVAL_USER, sm_so.getClient_approval_user());
            }

            if (sm_so.getClient_approval_user_nick() != null) {
                contentValues.put(CLIENT_APPROVAL_USER_NICK, sm_so.getClient_approval_user_nick());
            }

            if (sm_so.getOrigin_change() != null) {
                contentValues.put(ORIGIN_CHANGE, sm_so.getOrigin_change());
            }

            if (sm_so.getStarted_flag() > -1) {
                contentValues.put(STARTED_FLAG, sm_so.getStarted_flag());
            }

            if (sm_so.getEdit_origin() != null) {
                contentValues.put(EDIT_ORIGIN, sm_so.getEdit_origin());
            }

            if (sm_so.getEdit_user() != null) {
                contentValues.put(EDIT_USER, sm_so.getEdit_user());
            }

            if (sm_so.getEdit_user_nick() != null) {
                contentValues.put(EDIT_USER_NICK, sm_so.getEdit_user_nick());
            }

            if (sm_so.getTotal_qty_service() > -1) {
                contentValues.put(TOTAL_QTY_SERVICE, sm_so.getTotal_qty_service());
            }

            if (sm_so.getTotal_price() > -1) {
                contentValues.put(TOTAL_PRICE, sm_so.getTotal_price());
            }

            if (sm_so.getAdd_inf1() != null) {
                contentValues.put(ADD_INF1, sm_so.getAdd_inf1());
            }

            if (sm_so.getAdd_inf2() != null) {
                contentValues.put(ADD_INF2, sm_so.getAdd_inf2());
            }

            if (sm_so.getAdd_inf3() != null) {
                contentValues.put(ADD_INF3, sm_so.getAdd_inf3());
            }

            if (sm_so.getUpdate_required() > -1) {
                contentValues.put(UPDATE_REQUIRED, sm_so.getUpdate_required());
            }
            if (sm_so.getToken() != null) {
                contentValues.put(TOKEN, sm_so.getToken());
            }

            return contentValues;
        }
    }
}
