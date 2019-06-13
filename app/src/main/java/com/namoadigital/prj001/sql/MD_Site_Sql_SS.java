package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Site_Sql_SS implements Specification {
    private String s_customer_code;

    public MD_Site_Sql_SS(String s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT \n" +
                        "   site_code " + SearchableSpinner.CODE + ", \n" +
                        "   site_id "+ SearchableSpinner.ID + ", \n" +
                        "   site_desc " + SearchableSpinner.DESCRIPTION + ", \n" +
                        "   site_id , \n" +
                        "   io_control,\n " +
                        "   inbound_auto_create \n ," +
                        "   reason_code \n " +
                        " FROM \n" +
                        MD_SiteDao.TABLE +"\n"+
                        " WHERE \n" +
                        MD_SiteDao.CUSTOMER_CODE + " = '" + s_customer_code + "' \n" +
                        " ORDER BY \n" +
                        "      site_id,site_desc;")
//                .append(SearchableSpinner.ID + "#" +
//                        MD_SiteDao.SITE_ID + "#" +
//                        SearchableSpinner.DESCRIPTION + "#" +
//                        MD_SiteDao.IO_CONTROL + "#" +
//                        MD_SiteDao.INBOUND_AUTO_CREATE
//                )
                .toString();
    }
}
