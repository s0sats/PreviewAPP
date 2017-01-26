package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 25/01/2017.
 */

public class EV_Module_Res_Txt_Trans_Sql_001 implements Specification {

    private String module_code;

    public EV_Module_Res_Txt_Trans_Sql_001(String module_code) {
        this.module_code = module_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();

        return sb.append(  " DELETE FROM "
                + EV_Module_Res_Txt_TransDao.TABLE +
                " WHERE "
                + EV_Module_Res_Txt_TransDao.MODULE_CODE +" = '"+module_code+"';")
                .toString();
    }
}
