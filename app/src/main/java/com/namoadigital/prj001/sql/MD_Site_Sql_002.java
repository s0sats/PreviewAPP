package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Site_Sql_002 implements Specification {
    private Long s_customer_code;
    private String s_external_translation;

    public MD_Site_Sql_002(Long s_customer_code, String s_external_translation) {
        this.s_customer_code = s_customer_code;
        this.s_external_translation = s_external_translation;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT " +
                        "   IFNULL(site_code,'') site_code, " +
                        "   IFNULL(site_id,'') site_id, " +
                        "   IFNULL(site_desc,'') site_desc " +
                        " FROM " +
                        " ("+
                        " SELECT " +
                        "   site_code, " +
                        "   site_id, " +
                        "   site_desc    " +
                        " FROM " +
                        MD_SiteDao.TABLE +
                        " WHERE " +
                        MD_SiteDao.CUSTOMER_CODE +" = '"+s_customer_code+"' "+
                        " UNION "+
                        " SELECT " +
                        "  null site_code, " +
                        "  null site_id, " +
                        "  '" + s_external_translation +"' site_desc" +
                        " ORDER BY " +
                        "      site_id,site_desc" +
                        " ) " +
                        " ORDER BY" +
                        " site_id IS NULL,site_id;")
                .append("site_code#site_id#site_desc")
                .toString();
    }
}
