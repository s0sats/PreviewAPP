package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 31/03/2017.
 */

public class GE_Custom_Form_Field_Local_Sql_005 implements Specification {

    private String customer_code;

    public GE_Custom_Form_Field_Local_Sql_005(String customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "       fl.*\n" +
                        " FROM \n" +
                        GE_Custom_Form_Field_LocalDao.TABLE +" fl \n" +
                        " WHERE \n" +
                        "      fl.customer_code = '"+customer_code+"'\n" +
                        " ORDER BY\n" +
                        "    fl.customer_code,\n" +
                        "    fl.custom_form_type,\n" +
                        "    fl.custom_form_code,\n" +
                        "    fl.custom_form_version,\n" +
                        "    fl.custom_form_data," +
                        "    fl.custom_form_seq "
                        )
                .toString();
    }
}
