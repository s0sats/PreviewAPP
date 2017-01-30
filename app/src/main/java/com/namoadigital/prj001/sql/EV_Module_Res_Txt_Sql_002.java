package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 30/01/2017.
 */

public class EV_Module_Res_Txt_Sql_002 implements Specification {

    private String module_code;
    private String resource_name;

    public EV_Module_Res_Txt_Sql_002(String module_code, String resource_name) {
        this.module_code = module_code;
        this.resource_name = resource_name;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb  = new StringBuilder();

        return sb
                .append(" SELECT " +
                        "      * " +
                        " FROM "
                        + EV_Module_ResDao.TABLE +
                        " WHERE " +
                        "   "+ EV_Module_ResDao.MODULE_CODE+" = '" + module_code + "' " +
                        "   AND "+ EV_Module_ResDao.RESOURCE_NAME+" = '" + resource_name + "' ")
                .toString();
    }
}
