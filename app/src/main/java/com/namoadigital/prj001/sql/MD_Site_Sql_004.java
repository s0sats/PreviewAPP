package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;

public class MD_Site_Sql_004 implements Specification {
    private long s_customer_code;

    public MD_Site_Sql_004(long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "s.* " +
                        "\n FROM "  +
                        MD_SiteDao.TABLE + " s" +
                        "\n WHERE " +
                        MD_SiteDao.CUSTOMER_CODE +" = '"+s_customer_code+"' " +
                        "\n ORDER BY " + MD_SiteDao.SITE_CODE
                        )
                .toString();
    }
}
