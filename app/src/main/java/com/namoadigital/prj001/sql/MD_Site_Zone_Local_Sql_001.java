package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Site_Zone_Local_Sql_001 implements Specification {
    private Long customer_code;
    private int site_code;
    private int zone_code;

    public MD_Site_Zone_Local_Sql_001(Long customer_code, int site_code, int zone_code) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.zone_code = zone_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                        " * " +
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
                .toString().replace("'%null%'","null").replace("'null'","null");
    }
}
