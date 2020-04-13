package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_TxtDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 13/02/2020
 *
 * Seleciona os dados de master data de site, operação e produto para inserir na tabela de md_schedule
 *
 * LUCHE - 01/04/2020
 * Modificado query para receber como parametro o translate_code da preferencia.
 *
 * LUCHE - 13/04/2020
 * Modificado query removendo so parametro de site, operacao e produto , que agora terão os dados oriundos
 * de listas temporarias envias pelo server.
 */

public class MD_Schedule_Exec_Dao_Sql_001 implements Specification {

    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private String preference_translate_code;

    public MD_Schedule_Exec_Dao_Sql_001(long customer_code, int custom_form_type, int custom_form_code, int custom_form_version, String preference_translate_code) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
        this.preference_translate_code = preference_translate_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        //
        return sb
                .append(" SELECT\n" +
                        "    f.require_serial_done,\n" +
                        "    f.require_location,\n" +
                        "    (SELECT txt_value\n" +
                        "     FROM  "+ EV_Module_ResDao.TABLE +" r,\n" +
                        "           "+ EV_Module_Res_Txt_TransDao.TABLE +" t\n" +
                        "     WHERE r.module_code = t.module_code\n" +
                        "           and r.resource_code = t.resource_code\n" +
                        "           and t.txt_code = f.customer_code ||'|'|| f.custom_form_type\n" +
                        "           and t.module_code = '"+ ConstantBaseApp.EV_MODULE_CUST_FORM +"'\n" +
                        "           and t.translate_code = '"+preference_translate_code+"' \n" +
                        "     ) "+ GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC+" ,\n" +
                        "     ( SELECT txt_value\n" +
                        "       FROM "+EV_Module_ResDao.TABLE+" r,\n" +
                        "            "+ EV_Module_Res_TxtDao.TABLE+" rt,\n" +
                        "            "+ EV_Module_Res_Txt_TransDao.TABLE+" t \n" +
                        "       WHERE r.module_code = rt.module_code\n" +
                        "             and r.resource_code = rt.resource_code\n" +
                        "             --\n" +
                        "             and rt.module_code = t.module_code\n" +
                        "             and rt.resource_code = t.resource_code\n" +
                        "             and rt.txt_code = t.txt_code\n" +
                        "             --\n" +
                        "             and r.module_code = '"+ ConstantBaseApp.EV_MODULE_CUST_FORM +"'\n" +
                        "             and r.resource_name = f.customer_code ||'|'|| f.custom_form_type ||'|'|| f.custom_form_code ||'|'|| f.custom_form_version\n" +
                        "             and t.translate_code = '"+preference_translate_code+"'\n" +
                        "             and t.txt_code = 'TITLE'             \n" +
                        "      ) "+GE_Custom_FormDao.CUSTOM_FORM_DESC +"\n" +
                        " FROM\n" +
                        "    "+ GE_Custom_FormDao.TABLE +" f\n" +
                        " WHERE\n" +
                        "    f.customer_code = '"+customer_code+"'\n" +
                        "    and f.custom_form_type = '"+custom_form_type+"'\n" +
                        "    and f.custom_form_code = '"+custom_form_code+"'\n" +
                        "    and f.custom_form_version = '"+custom_form_version+"'\n"
                )
                .toString();
    }
}
