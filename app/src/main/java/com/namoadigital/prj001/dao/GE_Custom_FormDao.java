package com.namoadigital.prj001.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.database.CursorToHMAuxMapper;
import com.namoadigital.prj001.database.Mapper;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 11/01/17.
 */

public class GE_Custom_FormDao extends BaseDao implements Dao<GE_Custom_Form> {
    private final Mapper<GE_Custom_Form, ContentValues> toContentValuesMapper;
    private final Mapper<Cursor, GE_Custom_Form> toGE_Custom_FormMapper;

    public static final String TABLE = "ge_custom_forms";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String CUSTOM_FORM_CODE = "custom_form_code";
    public static final String CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String REQUIRE_SIGNATURE = "require_signature";
    public static final String REQUIRE_LOCATION = "require_location";
    public static final String REQUIRE_SERIAL_DONE = "require_serial_done";
    public static final String AUTOMATIC_FILL = "automatic_fill";
    public static final String ALL_SITE = "all_site";
    public static final String ALL_OPERATION = "all_operation";
    public static final String ALL_PRODUCT = "all_product";
    public static final String TAG_OPERATIONAL_CODE = "tag_operational_code";
    public static final String IS_SO = "is_so";
    public static final String SO_EDIT_START_END = "so_edit_start_end";
    public static final String SO_ORDER_TYPE_CODE_DEFAULT = "so_order_type_code_default";
    public static final String SO_ALLOW_CHANGE_ORDER_TYPE = "so_allow_change_order_type";
    public static final String BLOCK_SPONTANEOUS = "block_spontaneous";
    //Não é campo da tabela, descrição vem da tradução
    public static final String CUSTOM_FORM_DESC = "custom_form_desc";

    public GE_Custom_FormDao(Context context, String DB_NAME, int DB_VERSION) {
        super(context, DB_NAME, DB_VERSION, Constant.DB_MODE_MULTI);
        //
        this.toContentValuesMapper = new GE_Custom_FormToContentValuesMapper();
        this.toGE_Custom_FormMapper = new CursorGE_Custom_FormMapper();
    }

    @Override
    public void addUpdate(GE_Custom_Form custom_form) {
        openDB();

        try {

            if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form)) == -1) {
                StringBuilder sbWhere = new StringBuilder();
                sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form.getCustomer_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form.getCustom_form_type())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form.getCustom_form_code())).append("'");
                sbWhere.append(" and ");
                sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form.getCustom_form_version())).append("'");

                db.update(TABLE, toContentValuesMapper.map(custom_form), sbWhere.toString(), null);
            }


        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void addUpdate(Iterable<GE_Custom_Form> custom_forms, boolean status) {
        openDB();

        try {

            db.beginTransaction();

            if (status) {
                db.delete(TABLE, null, null);
            }

            for (GE_Custom_Form custom_form : custom_forms) {
                if (db.insert(TABLE, null, toContentValuesMapper.map(custom_form)) == -1) {
                    StringBuilder sbWhere = new StringBuilder();
                    sbWhere.append(CUSTOMER_CODE).append(" = '").append(String.valueOf(custom_form.getCustomer_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_TYPE).append(" = '").append(String.valueOf(custom_form.getCustom_form_type())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_CODE).append(" = '").append(String.valueOf(custom_form.getCustom_form_code())).append("'");
                    sbWhere.append(" and ");
                    sbWhere.append(CUSTOM_FORM_VERSION).append(" = '").append(String.valueOf(custom_form.getCustom_form_version())).append("'");

                    db.update(TABLE, toContentValuesMapper.map(custom_form), sbWhere.toString(), null);
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
            db.endTransaction();
        }

        closeDB();
    }

    @Override
    public void addUpdate(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public void remove(String s_query) {
        openDB();

        try {

            db.execSQL(s_query);

        } catch (Exception e) {
        } finally {
        }

        closeDB();
    }

    @Override
    public GE_Custom_Form getByString(String s_query) {
        GE_Custom_Form custom_form = null;
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_form = toGE_Custom_FormMapper.map(cursor);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return custom_form;
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
    public List<GE_Custom_Form> query(String s_query) {
        List<GE_Custom_Form> custom_forms = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                GE_Custom_Form uAux = toGE_Custom_FormMapper.map(cursor);
                custom_forms.add(uAux);
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return custom_forms;
    }

    @Override
    public List<HMAux> query_HM(String s_query) {
        List<HMAux> custom_forms = new ArrayList<>();
        openDB();

        try {

            Cursor cursor = db.rawQuery(s_query, null);

            while (cursor.moveToNext()) {
                custom_forms.add(CursorToHMAuxMapper.mapN(cursor));
            }

            cursor.close();
        } catch (Exception e) {
            ToolBox_Inf.registerException(getClass().getName(),e);
        } finally {
        }

        closeDB();

        return custom_forms;
    }

    private class CursorGE_Custom_FormMapper implements Mapper<Cursor, GE_Custom_Form> {
        @Override
        public GE_Custom_Form map(Cursor cursor) {
            GE_Custom_Form custom_form = new GE_Custom_Form();

            custom_form.setCustomer_code(cursor.getLong(cursor.getColumnIndex(CUSTOMER_CODE)));
            custom_form.setCustom_form_type(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_TYPE)));
            custom_form.setCustom_form_code(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_CODE)));
            custom_form.setCustom_form_version(cursor.getInt(cursor.getColumnIndex(CUSTOM_FORM_VERSION)));
            custom_form.setRequire_signature(cursor.getInt(cursor.getColumnIndex(REQUIRE_SIGNATURE)));
            custom_form.setRequire_location(cursor.getInt(cursor.getColumnIndex(REQUIRE_LOCATION)));
            custom_form.setRequire_serial_done(cursor.getInt(cursor.getColumnIndex(REQUIRE_SERIAL_DONE)));
            custom_form.setAutomatic_fill(cursor.getString(cursor.getColumnIndex(AUTOMATIC_FILL)));
            custom_form.setAll_product(cursor.getInt(cursor.getColumnIndex(ALL_PRODUCT)));
            custom_form.setAll_site(cursor.getInt(cursor.getColumnIndex(ALL_SITE)));
            custom_form.setAll_operation(cursor.getInt(cursor.getColumnIndex(ALL_OPERATION)));
            //
            custom_form.setTag_operational_code(cursor.getInt(cursor.getColumnIndex(TAG_OPERATIONAL_CODE)));
            custom_form.setIs_so(cursor.getInt(cursor.getColumnIndex(IS_SO)));
            custom_form.setSo_edit_start_end(cursor.getInt(cursor.getColumnIndex(SO_EDIT_START_END)));
            custom_form.setSo_order_type_code_default(cursor.getInt(cursor.getColumnIndex(SO_ORDER_TYPE_CODE_DEFAULT)));
            custom_form.setSo_allow_change_order_type(cursor.getInt(cursor.getColumnIndex(SO_ALLOW_CHANGE_ORDER_TYPE)));
            custom_form.setBlock_spontaneous(cursor.getInt(cursor.getColumnIndex(BLOCK_SPONTANEOUS)));
            return custom_form;
        }
    }

    private class GE_Custom_FormToContentValuesMapper implements Mapper<GE_Custom_Form, ContentValues> {
        @Override
        public ContentValues map(GE_Custom_Form custom_form) {
            ContentValues contentValues = new ContentValues();

            if (custom_form.getCustomer_code() > -1) {
                contentValues.put(CUSTOMER_CODE, custom_form.getCustomer_code());
            }
            if (custom_form.getCustom_form_type() > -1) {
                contentValues.put(CUSTOM_FORM_TYPE, custom_form.getCustom_form_type());
            }
            if (custom_form.getCustom_form_code() > -1) {
                contentValues.put(CUSTOM_FORM_CODE, custom_form.getCustom_form_code());
            }
            if (custom_form.getCustom_form_version() > -1) {
                contentValues.put(CUSTOM_FORM_VERSION, custom_form.getCustom_form_version());
            }
            if (custom_form.getRequire_signature() > -1) {
                contentValues.put(REQUIRE_SIGNATURE, custom_form.getRequire_signature());
            }
            if (custom_form.getRequire_location() > -1) {
                contentValues.put(REQUIRE_LOCATION, custom_form.getRequire_location());
            }
            if (custom_form.getRequire_serial_done() > -1) {
                contentValues.put(REQUIRE_SERIAL_DONE, custom_form.getRequire_serial_done());
            }
            if (custom_form.getAutomatic_fill() != null) {
                contentValues.put(AUTOMATIC_FILL, custom_form.getAutomatic_fill());
            }
            if (custom_form.getAll_product() > -1) {
                contentValues.put(ALL_PRODUCT, custom_form.getAll_product());
            }
            if (custom_form.getAll_site() > -1) {
                contentValues.put(ALL_SITE, custom_form.getAll_site());
            }
            if (custom_form.getAll_operation() > -1) {
                contentValues.put(ALL_OPERATION, custom_form.getAll_operation());
            }
            //
            if (custom_form.getTag_operational_code() > -1) {
                contentValues.put(TAG_OPERATIONAL_CODE, custom_form.getTag_operational_code());
            }
            if (custom_form.getIs_so() > -1) {
                contentValues.put(IS_SO, custom_form.getIs_so());
            }
            if (custom_form.getSo_edit_start_end() > -1) {
                contentValues.put(SO_EDIT_START_END, custom_form.getSo_edit_start_end());
            }

            contentValues.put(SO_ORDER_TYPE_CODE_DEFAULT, custom_form.getSo_order_type_code_default());

            if (custom_form.getSo_allow_change_order_type() > -1) {
                contentValues.put(SO_ALLOW_CHANGE_ORDER_TYPE, custom_form.getSo_allow_change_order_type());
            }
            if (custom_form.getBlock_spontaneous() > -1) {
                contentValues.put(BLOCK_SPONTANEOUS, custom_form.getBlock_spontaneous());
            }
            return contentValues;
        }
    }

}
