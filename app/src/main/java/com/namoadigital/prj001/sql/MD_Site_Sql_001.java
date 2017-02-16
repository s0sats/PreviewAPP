package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 23/01/17.
 */

public class MD_Site_Sql_001 implements Specification {

    private long s_customer_code;
    private String s_site_code;

    public MD_Site_Sql_001(long s_customer_code, String s_site_code) {
        this.s_customer_code = s_customer_code;
        this.s_site_code = s_site_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      s.customer_code,\n" +
                        "      s.site_code,\n" +
                        "      s.site_id,\n" +
                        "      s.site_id||' - '||s.site_desc site_desc\n" +
                        " FROM "  +
                        MD_SiteDao.TABLE + " s" +
                        " WHERE " +
                        MD_SiteDao.CUSTOMER_CODE +" = '"+s_customer_code+"' "+
                        "AND " +MD_SiteDao.SITE_CODE +" = '"+s_site_code+"' ")
                        .toString();
    }

}
