package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 23/02/17.
 *
 * LUCHE - 26/03/2020
 *
 * Modificado query para incluir o status cancelado, possivel após a implatação do novo agendamento.
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
                        " WHERE custom_form_status in ('"+ConstantBase.SYS_STATUS_SENT + "',\n" +
                        "                               '"+ ConstantBase.SYS_STATUS_DELETED + "', \n" +
                        "                               '"+ ConstantBase.SYS_STATUS_CANCELLED + "' \n" +
                        "                              )\n" +
                        " and Date(date_end) < Date('"+s_date+"');")
                .toString();
    }
}
