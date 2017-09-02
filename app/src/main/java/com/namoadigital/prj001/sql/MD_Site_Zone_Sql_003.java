package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 *
 * Seleciona todos dados de uma zona especifica
 */

public class MD_Site_Zone_Sql_003 implements Specification {
    private Long customer_code;
    private int site_code;
    private int zone_code;

    public MD_Site_Zone_Sql_003(Long customer_code, int site_code, int zone_code) {
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
                        "   * " +
                        " FROM " +
                        MD_Site_ZoneDao.TABLE +
                        " WHERE " +
                         MD_Site_ZoneDao.CUSTOMER_CODE +" = '"+ customer_code +"' "+
                        " and " + MD_Site_ZoneDao.SITE_CODE +" = '"+ site_code +"' "+
                        " and " + MD_Site_ZoneDao.ZONE_CODE +" = '"+ zone_code +"' ")
                .toString();
    }
}
