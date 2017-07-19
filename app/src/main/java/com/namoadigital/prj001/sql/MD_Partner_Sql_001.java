package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 17/01/2017.
 */

public class MD_Partner_Sql_001 implements Specification {

    private long s_customer_code;

    public MD_Partner_Sql_001(long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        // customer_code, partner_code, partner_id as id, partner_desc as description

        return sb
                .append("SELECT " +
                        "   customer_code, " +
                        "   partner_code as id, " +
                        "   partner_id, " +
                        "   partner_desc as description " +
                        "FROM ")
                .append(MD_PartnerDao.TABLE)
                .append(" WHERE " +
                        MD_PartnerDao.CUSTOMER_CODE + " = '" + s_customer_code + "' " +
                        " ORDER BY " +
                        "      partner_desc;")
                .append("customer_code#partner_code#id#description")
                .toString();
    }
}
