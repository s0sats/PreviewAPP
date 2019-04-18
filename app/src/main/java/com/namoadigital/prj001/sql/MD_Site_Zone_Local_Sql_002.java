package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.database.Specification;

public class MD_Site_Zone_Local_Sql_002 implements Specification {
    private Long customer_code;
    private String site_code;
    private int zone_code;
    private int local_code;

    public MD_Site_Zone_Local_Sql_002(Long customer_code, String site_code, int zone_code, int local_code) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.zone_code = zone_code;
        this.local_code = local_code;
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
                                MD_Site_Zone_LocalDao.CUSTOMER_CODE +" = "+ customer_code +" " +
                                " and " + MD_Site_Zone_LocalDao.SITE_CODE +" = "+ site_code +" "+
                                " and " + MD_Site_Zone_LocalDao.ZONE_CODE +" = "+ zone_code +" " +
                                " and " + MD_Site_Zone_LocalDao.LOCAL_CODE +" = "+ local_code ).toString();
    }
}
