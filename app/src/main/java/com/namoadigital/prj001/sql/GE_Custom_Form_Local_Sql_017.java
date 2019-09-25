package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 25/09/2019
 *
 * Query que seleciona os form agendados que possuem não possuem icone de produto baixado
 *
 */

public class GE_Custom_Form_Local_Sql_017 implements Specification {
    private long customer_code;

    public GE_Custom_Form_Local_Sql_017(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
            .append(" SELECT \n" +
                    "   l.customer_code,  \n" +
                    "   l.custom_form_type,\n" +
                    "   l.custom_form_code,\n" +
                    "   l.custom_form_version,\n" +
                    "   l.custom_form_data,\n" +
                    "   l.custom_form_data_serv,\n" +
                    "   l.custom_product_icon_name,\n" +
                    "   l.custom_product_icon_url\n" +
                    " FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE +" l\n" +
                    " WHERE\n" +
                    "  l.customer_code = '"+customer_code+"'\n" +
                    "  AND l.custom_product_icon_name != ''\n" +
                    "  AND l.custom_product_icon_url != ''\n" +
                    "  AND l.custom_product_icon_url_local == ''\n" +
                    "  AND l.custom_form_data_serv is not null\n")
            .toString();
    }
}
