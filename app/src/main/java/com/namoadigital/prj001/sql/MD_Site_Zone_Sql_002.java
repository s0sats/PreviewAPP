package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 26/01/2017.
 */

public class MD_Site_Zone_Sql_002 implements Specification {
    private Long customer_code;
    private int site_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(MD_Site_ZoneDao.columns);

    public MD_Site_Zone_Sql_002(Long customer_code, int site_code) {
        this.customer_code = customer_code;
        this.site_code = site_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT \n" +
                        "   * \n" +
                        " FROM \n" +
                        MD_Site_ZoneDao.TABLE +"\n" +
                        " WHERE " +
                         MD_Site_ZoneDao.CUSTOMER_CODE +" = '"+ customer_code +"' "+
                        " and " + MD_Site_ZoneDao.SITE_CODE +" = '"+ site_code +"' "+
                        " ORDER BY " +
                        "      zone_id,zone_desc")
                .append(";" +HmAuxFields)
                .toString();
    }
}
