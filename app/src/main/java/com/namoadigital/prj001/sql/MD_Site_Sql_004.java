package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;

public class MD_Site_Sql_004 implements Specification {
    private long s_customer_code;
    private boolean allowOrderByDesc;
    private StringBuilder orderBy = new StringBuilder("\n ORDER BY ");

    public MD_Site_Sql_004(long s_customer_code, Boolean allowOrderByDesc) {
        this.s_customer_code = s_customer_code;
        this.allowOrderByDesc = allowOrderByDesc;
    }

    public MD_Site_Sql_004(long s_customer_code) {
        this.s_customer_code = s_customer_code;
        this.allowOrderByDesc = false;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        if (allowOrderByDesc) {
            orderBy.append(MD_SiteDao.SITE_DESC);
        } else {
            orderBy.append(MD_SiteDao.SITE_CODE);
        }

        return sb
                .append(" SELECT\n" +
                        "s.* " +
                        "\n FROM " +
                        MD_SiteDao.TABLE + " s" +
                        "\n WHERE " +
                        MD_SiteDao.CUSTOMER_CODE + " = '" + s_customer_code + "' " +
                        orderBy
                )
                .toString();
    }
}
