package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Site_Sql_003 implements Specification {
    private Long s_customer_code;

    public MD_Site_Sql_003(Long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                        "   site_code "+ SearchableSpinner.ID +", " +
                        "   site_id , " +
                        "   site_desc "+ SearchableSpinner.DESCRIPTION +
                        " FROM " +
                        MD_SiteDao.TABLE +
                        " WHERE " +
                        MD_SiteDao.CUSTOMER_CODE +" = '"+s_customer_code+"' "+
                       " ORDER BY " +
                        "      site_id,site_desc;")
                .append(SearchableSpinner.ID + "#site_id#"+SearchableSpinner.DESCRIPTION )
                .toString();
    }
}
