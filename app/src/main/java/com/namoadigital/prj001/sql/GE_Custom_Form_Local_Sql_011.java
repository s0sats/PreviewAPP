package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 31/03/2017.
 */

/**
 *
 *  Seleciona todos os registros do customer na tabela
 *  GE_Custom_Form_Local
 *
 */

public class GE_Custom_Form_Local_Sql_011 implements Specification {

    private String customer_code;
    private boolean filter_status;
    private String filter = "";


    public GE_Custom_Form_Local_Sql_011(String customer_code, boolean filter_status) {
        this.customer_code = customer_code;
        this.filter_status = filter_status;

        if(filter_status){
            filter = " AND fl.custom_form_status = 'SCHEDULE' ";
        }
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "       fl.*\n" +
                        " FROM \n" +
                        GE_Custom_Form_LocalDao.TABLE +" fl \n" +
                        " WHERE \n" +
                        "      fl.customer_code = '"+customer_code+"'\n" +
                        "      AND fl.custom_form_data_serv is not null \n" +
                        filter +
                        " ORDER BY\n" +
                        "    fl.customer_code,\n" +
                        "    fl.custom_form_type,\n" +
                        "    fl.custom_form_code,\n" +
                        "    fl.custom_form_version,\n" +
                        "    fl.custom_form_data")
                .toString();
    }
}
