package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Site_Zone_Local_Sql_SS implements Specification {
    private String customer_code;
    private String site_code;
    private String zone_code;

    public MD_Site_Zone_Local_Sql_SS(String customer_code, String site_code, String zone_code) {
        this.customer_code = customer_code;
//        this.site_code = site_code.length() > 0 ? site_code : null;
//        this.zone_code = zone_code.length() > 0 ? zone_code : null;
        this.site_code = site_code;
        this.zone_code = zone_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                        "   local_code "+ SearchableSpinner.ID +", " +
                        "   local_id "+ SearchableSpinner.DESCRIPTION +", " +
                        "   site_code, " +
                        "   zone_code " +
                        " FROM " +
                        MD_Site_Zone_LocalDao.TABLE +
                        " WHERE " +
                        MD_Site_Zone_LocalDao.CUSTOMER_CODE +" = '"+ customer_code +"' " +
                        " and ('" + site_code  + "' IS NULL "+
                        " or " + MD_Site_Zone_LocalDao.SITE_CODE +" = '"+ site_code +"' "+
                        " and " + MD_Site_Zone_LocalDao.ZONE_CODE +" = '"+ zone_code +"'" +
                        ")  "+
                       " ORDER BY " +
                       "      local_id;")
                .append(SearchableSpinner.ID + "#"+SearchableSpinner.DESCRIPTION+"#site_code#zone_code")
                .toString().replace("'%null%'","null").replace("'null'","null");
    }
}
