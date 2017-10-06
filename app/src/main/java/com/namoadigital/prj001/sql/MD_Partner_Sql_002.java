package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 17/01/2017.
 */

public class MD_Partner_Sql_002 implements Specification {

    private long s_customer_code;
    private int s_partner_code;


    public MD_Partner_Sql_002(long s_customer_code, int s_partner_code) {
        this.s_customer_code = s_customer_code;
        this.s_partner_code = s_partner_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "      * \n" +
                        " FROM \n " +
                        MD_PartnerDao.TABLE + " " +
                        " WHERE\n " +
                        "        customer_code =          '" + s_customer_code + "'\n" +
                        "     AND partner_code =           '" + s_partner_code + "' ")
                .toString();
    }
}
