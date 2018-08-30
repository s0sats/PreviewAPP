package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 08/02/17.
 */

public class GE_Custom_Form_Local_Sql_003 implements Specification {

    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;
    private String s_form_data;
    private String s_form_status;
    private String s_product_code;
    private String s_serial_id;

    private String s_filter;

    /**
     *Construtor para chamada do WS_SAVE
     */
    public GE_Custom_Form_Local_Sql_003(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code, String s_form_data) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;

        if (s_form_data.equals("0")) {
            this.s_form_status = Constant.SYS_STATUS_IN_PROCESSING;
            s_filter =
                    "     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS + " = '" + s_form_status + "' ";
        } else {
            this.s_form_data = s_form_data;
            s_filter = "     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA + " = '" + s_form_data + "' ";
        }
    }

    public GE_Custom_Form_Local_Sql_003(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code, String s_form_data ,String s_product_code,String s_serial_id) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
        this.s_product_code = s_product_code;
        this.s_serial_id = s_serial_id;

        if (s_form_data.equals("0")) {
            this.s_form_status = Constant.SYS_STATUS_IN_PROCESSING;
            s_filter =
                    "     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS + " = '" + s_form_status + "' " +
                    "     AND " + GE_Custom_Form_LocalDao.CUSTOM_PRODUCT_CODE + " = '" + s_product_code + "' " +
                    "     AND " + GE_Custom_Form_LocalDao.SERIAL_ID + " = '" + s_serial_id + "' " ;
        } else {
            this.s_form_data = s_form_data;
            s_filter = "     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA + " = '" + s_form_data + "' ";
        }
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append(
                " SELECT " +
                        " * " +
                        " FROM " +
                        GE_Custom_Form_LocalDao.TABLE +
                        " WHERE " +
                        GE_Custom_Form_LocalDao.CUSTOMER_CODE + "= '" + s_customer_code + "' " +
                        "     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE + " = '" + s_formtype_code + "' " +
                        "     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE + " = '" + s_form_code + "' " +
                        "     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION + " = '" + s_formversion_code + "' ")
                .append(s_filter)
                .append(";")
                .toString();
    }
}
