package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Site_Zone_Sql_SS implements Specification {
    private String customer_code;
    private String site_code;

    public MD_Site_Zone_Sql_SS(String customer_code, String site_code) {
        this.customer_code = customer_code;
        this.site_code = site_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                        "   zone_code "+ SearchableSpinner.ID +", " +
                        "   zone_id , " +
                        "   zone_desc "+ SearchableSpinner.DESCRIPTION +
                        " FROM " +
                        MD_Site_ZoneDao.TABLE +
                        " WHERE " +
                         MD_Site_ZoneDao.CUSTOMER_CODE +" = '"+ customer_code +"' "+
                        " and " + MD_Site_ZoneDao.SITE_CODE +" = '"+ site_code +"' "+
                       " ORDER BY " +
                        "      zone_id,zone_desc;")
                .append(SearchableSpinner.ID + "#zone_id#"+SearchableSpinner.DESCRIPTION )
                .toString();
    }
}
