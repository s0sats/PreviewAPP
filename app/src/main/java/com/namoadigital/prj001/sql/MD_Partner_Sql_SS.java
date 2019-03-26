package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 17/01/2017.
 */

public class MD_Partner_Sql_SS implements Specification {

    private long s_customer_code;

    public MD_Partner_Sql_SS(long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        // customer_code, partner_code, partner_id as id, partner_desc as description

        return sb
                .append("SELECT " +
                        "   customer_code, " +
                        "   partner_code "+ SearchableSpinner.CODE +",\n " +
                        "   partner_id "+ SearchableSpinner.ID +",\n" +
                        "   partner_desc "+ SearchableSpinner.DESCRIPTION +" \n" +
                        "FROM ")
                .append(MD_PartnerDao.TABLE)
                .append(" WHERE " +
                        MD_PartnerDao.CUSTOMER_CODE + " = '" + s_customer_code + "' " +
                        " ORDER BY " +
                        "      partner_desc")
                .append(";")
                //.append("customer_code#partner_id#id#description")
                .toString();
    }
}
