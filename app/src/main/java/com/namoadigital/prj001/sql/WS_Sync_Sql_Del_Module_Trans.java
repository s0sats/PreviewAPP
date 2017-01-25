package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_TxtDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 25/01/2017.
 */

public class WS_Sync_Sql_Del_Module_Trans implements Specification {

    private String module_code;

    public WS_Sync_Sql_Del_Module_Trans(String module_code) {
        this.module_code = module_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();

          sb.append(  " DELETE FROM "
                        + EV_Module_ResDao.TABLE +
                        " WHERE "
                        + EV_Module_ResDao.MODULE_CODE +" = '"+module_code+"';")

            .append(  " DELETE FROM "
                        + EV_Module_Res_TxtDao.TABLE +
                        " WHERE "
                        + EV_Module_Res_TxtDao.MODULE_CODE +" = '"+module_code+"';")
            .append(  " DELETE FROM "
                        + EV_Module_Res_Txt_TransDao.TABLE +
                        " WHERE "
                        + EV_Module_Res_Txt_TransDao.MODULE_CODE +" = '"+module_code+"';");

        return sb.toString();
    }
}
