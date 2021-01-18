package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Site_Sql_002 implements Specification {
    private Long s_customer_code;

    public MD_Site_Sql_002(Long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                        "   site_code, " +
                        "   site_id, " +
                        "   site_desc,    " +
                        MD_SiteDao.LICENSE_ENABLED + ", \n" +
                        MD_SiteDao.FREE_EXECUTIONS_MAX + ", \n" +
                        MD_SiteDao.FREE_EXECUTIONS_COUNT + ", \n" +
                        MD_SiteDao.APP_EXECUTIONS_COUNT + ", \n" +
                        MD_SiteDao.LICENSE_BLOCKED + " \n" +
                        " FROM " +
                        MD_SiteDao.TABLE +
                        " WHERE " +
                        MD_SiteDao.CUSTOMER_CODE +" = '"+s_customer_code+"' "+
                       " ORDER BY " +
                        "      site_id,site_desc;")
                //.append("site_code#site_id#site_desc")
                .toString();
    }
}
