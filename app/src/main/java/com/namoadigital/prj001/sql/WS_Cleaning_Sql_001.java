package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * Created by neomatrix on 23/02/17.
 *
 * LUCHE - 26/03/2020
 * Modificado query para incluir o status cancelado, possivel após a implatação do novo agendamento.
 *
 * LUCHE - 26/03/2020
 * Modificado query para substituindo o status SYS_STATUS_SENT pelo status SYS_STATUS_DONE.
 * Compatiilização de status
 */

public class WS_Cleaning_Sql_001 implements Specification {

    private String s_date;

    public WS_Cleaning_Sql_001(String s_date) {
        this.s_date = s_date;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT * \n " +
                        " FROM "+ GE_Custom_Form_DataDao.TABLE +" \n" +
                        " WHERE custom_form_status in ('"+ConstantBase.SYS_STATUS_DONE + "',\n" +
                        "                               '"+ ConstantBase.SYS_STATUS_DELETED + "', \n" +
                        "                               '"+ ConstantBase.SYS_STATUS_CANCELLED + "', \n" +
                        "                               '"+ ConstantBase.SYS_STATUS_NOT_EXECUTED + "',\n" +
                        "                               '"+ ConstantBaseApp.SYS_STATUS_IGNORED + "' \n" +
                        "                              )\n" +
                        " and Date(date_end) < Date('"+s_date+"');")
                .toString();
    }
}
