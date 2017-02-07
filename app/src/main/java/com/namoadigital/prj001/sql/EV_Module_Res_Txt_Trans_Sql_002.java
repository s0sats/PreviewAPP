package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class EV_Module_Res_Txt_Trans_Sql_002 implements Specification {

    private String s_module_code;
    private String s_resource_code;
    private String s_translate_code;


    public EV_Module_Res_Txt_Trans_Sql_002(String s_module_code, String s_resource_code, String s_translate_code) {
        this.s_module_code = s_module_code;
        this.s_resource_code = s_resource_code;
        this.s_translate_code = s_translate_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append(  " SELECT * " +
                    " FROM  " +
                    EV_Module_Res_Txt_TransDao.TABLE +
                    " WHERE " +
                    EV_Module_Res_Txt_TransDao.MODULE_CODE + " = '"+s_module_code+"' " +
                    "  AND " + EV_Module_Res_Txt_TransDao.RESOURCE_CODE +" = '"+s_resource_code+"' " +
                    "  AND " + EV_Module_Res_Txt_TransDao.TRANSLATE_CODE +" = '"+s_translate_code+"' " +
                    " UNION " +
                    " SELECT * " +
                    " FROM  " +
                    EV_Module_Res_Txt_TransDao.TABLE  +
                    " WHERE " +
                    EV_Module_Res_Txt_TransDao.MODULE_CODE + " = 'SYS'" +
                    " ORDER BY " +
                    EV_Module_Res_Txt_TransDao.MODULE_CODE + " , " +
                    EV_Module_Res_Txt_TransDao.RESOURCE_CODE ).toString();

        /*return sb.append(" select * from ")
                .append(EV_Module_Res_Txt_TransDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(EV_Module_Res_Txt_TransDao.MODULE_CODE)
                .append(" ='")
                .append(s_module_code)
                .append("'")
                .append(" )")
                .append(" and")
                .append(" (")
                .append(EV_Module_Res_Txt_TransDao.RESOURCE_CODE)
                .append(" ='")
                .append(s_resource_code)
                .append("'")
                .append(" )")
                .append(" and")
                .append(" (")
                .append(EV_Module_Res_Txt_TransDao.TRANSLATE_CODE)
                .append(" ='")
                .append(s_translate_code)
                .append("'")
                .append(" )")
                .append(" )")
                .append(";")
                .toString();*/


    }
}
