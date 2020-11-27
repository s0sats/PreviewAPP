package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * BARRIONUEVO 27-11-2020
 * QUery que verifica se o form ja esta sendo executado para o mesmo ticket, seq, step
 * Em termos praticos, permite criar apenas 1 form espontaneo do mesmo prod+serial+form.
 */
public class GE_Custom_Form_Local_Sql_019 implements Specification {


    private String s_customer_code;
    private String s_formtype_code;
    private String s_form_code;
    private String s_formversion_code;
    private String s_form_data;
    private String s_form_status;
    private String s_product_code;
    private String s_serial_id;
    private Integer mTicket_prefix;
    private Integer mTicket_code;
    private Integer mTicket_seq;
    private Integer mStep_code;

    private String s_filter;

    public GE_Custom_Form_Local_Sql_019(String s_customer_code, String s_formtype_code, String s_form_code, String s_formversion_code, String s_form_data, String s_product_code, String s_serial_id, Integer mTicket_prefix, Integer mTicket_code, Integer  mTicket_seq, Integer mStep_code) {
        this.s_customer_code = s_customer_code;
        this.s_formtype_code = s_formtype_code;
        this.s_form_code = s_form_code;
        this.s_formversion_code = s_formversion_code;
        this.s_product_code = s_product_code;
        this.s_serial_id = s_serial_id;
        this.mTicket_prefix = mTicket_prefix;
        this.mTicket_code = mTicket_code;
        this.mTicket_seq = mTicket_seq;
        this.mStep_code = mStep_code;

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
                        "\n     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE + " = '" + s_formtype_code + "' " +
                        "\n     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE + " = '" + s_form_code + "' " +
                        "\n     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION + " = '" + s_formversion_code + "' "+
                        "\n     AND " + GE_Custom_Form_LocalDao.TICKET_PREFIX + " = '" + mTicket_prefix + "' " +
                        "\n     AND " + GE_Custom_Form_LocalDao.TICKET_CODE + " = '" + mTicket_code + "' " +
                        "\n     AND " + GE_Custom_Form_LocalDao.TICKET_SEQ + " = '" + mTicket_seq + "' " +
                        "     AND " + GE_Custom_Form_LocalDao.STEP_CODE + " = '" + mStep_code + "' " )
                .append(s_filter)
                .append(";")
                .toString();
    }
}