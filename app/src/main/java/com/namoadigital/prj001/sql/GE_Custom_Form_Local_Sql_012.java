package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 06/04/2017.
 */

/**
 *  Apaga todos os forms local com status
 *  Schedule
 */

public class GE_Custom_Form_Local_Sql_012 implements Specification {

    private String customer_code;
    private String custom_form_data_serv_list;

    public GE_Custom_Form_Local_Sql_012(String customer_code, String custom_form_data_serv_list) {
        this.customer_code = customer_code;
        this.custom_form_data_serv_list = custom_form_data_serv_list;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE\n" +
                        " FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE +" \n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   AND custom_form_data_serv in ("+custom_form_data_serv_list+");")
                .toString();
    }
}
