package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * BARRIONUEVO - 02-12-2021
 * Verifica se há form em processo para o ctrl especificado.
 */
public class GE_Custom_Form_Local_Sql_020 implements Specification {


    private String s_customer_code;
    private Integer mTicket_prefix;
    private Integer mTicket_code;
    private Integer mTicket_seq;
    private Integer mTicket_seq_tmp;
    private Integer mStep_code;

    public GE_Custom_Form_Local_Sql_020(String s_customer_code, Integer mTicket_prefix, Integer mTicket_code, Integer  mTicket_seq,Integer  mTicket_seq_tmp, Integer mStep_code) {
        this.s_customer_code = s_customer_code;
        this.mTicket_prefix = mTicket_prefix;
        this.mTicket_code = mTicket_code;
        this.mTicket_seq = mTicket_seq;
        this.mTicket_seq_tmp = mTicket_seq_tmp;
        this.mStep_code = mStep_code;

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
                        "\n     AND " + GE_Custom_Form_LocalDao.TICKET_PREFIX + " = '" + mTicket_prefix + "' " +
                        "\n     AND " + GE_Custom_Form_LocalDao.TICKET_CODE + " = '" + mTicket_code + "' " +
                        "\n     AND " + GE_Custom_Form_LocalDao.TICKET_SEQ + " = '" + mTicket_seq + "' " +
                        "\n     AND " + GE_Custom_Form_LocalDao.TICKET_SEQ_TMP + " = '" + mTicket_seq_tmp + "' " +
                        "     AND " + GE_Custom_Form_LocalDao.STEP_CODE + " = '" + mStep_code + "' " +
                        "     AND " + GE_Custom_Form_LocalDao.CUSTOM_FORM_STATUS + " in(" +
                        "'"+ Constant.SYS_STATUS_IN_PROCESSING+"'," +
                                "'"+ Constant.SYS_STATUS_WAITING_SYNC+"')"
                )
                .append(";")
                .toString();
    }
}