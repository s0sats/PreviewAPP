package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_TxtDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 13/02/2020
 *
 * Seleciona os dados de master data de site, operação e produto para inserir na tabela de md_schedule
 *
 */

public class MD_Schedule_Exec_Sql_005 implements Specification {

    private long customer_code;
    private int site_code;
    private int operation_code;
    private int product_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;

    public MD_Schedule_Exec_Sql_005(long customer_code, int site_code, int operation_code, int product_code, int custom_form_type, int custom_form_code, int custom_form_version) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.operation_code = operation_code;
        this.product_code = product_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        //
        return sb
                .append(" SELECT\n" +
                        "    s.site_id,\n" +
                        "    s.site_desc,\n" +
                        "    o.operation_id,\n" +
                        "    o.operation_desc,\n" +
                        "    p.product_id,\n" +
                        "    p.product_desc,\n" +
                        "    p.require_serial,\n" +
                        "    p.allow_new_serial_cl,\n" +
                        "    f.require_serial_done,\n" +
                        "    (SELECT txt_value\n" +
                        "     FROM  "+ EV_Module_ResDao.TABLE +" r,\n" +
                        "           "+ EV_Module_Res_Txt_TransDao.TABLE +" t\n" +
                        "     WHERE r.module_code = t.module_code\n" +
                        "           and r.resource_code = t.resource_code\n" +
                        "           and t.txt_code = sc.customer_code ||'|'|| sc.custom_form_type\n" +
                        "           and t.module_code = '"+ ConstantBaseApp.EV_MODULE_CUST_FORM +"'\n" +
                        "           and t.translate_code = 1 \n" +
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
                        "             and r.resource_name = sc.customer_code ||'|'|| sc.custom_form_type ||'|'|| sc.custom_form_code ||'|'|| sc.custom_form_version\n" +
                        "             and t.translate_code = 1\n" +
                        "             and t.txt_code = 'TITLE'             \n" +
                        "      ) "+GE_Custom_FormDao.CUSTOM_FORM_DESC +"\n" +
                        " FROM\n" +
                        "      ( SELECT\n" +
                        "            '"+customer_code+"' customer_code,\n" +
                        "            '"+site_code+"' site_code,\n" +
                        "            '"+operation_code+"' operation_code,\n" +
                        "            '"+product_code+"' product_code\n," +
                        "            '"+custom_form_type+"' custom_form_type,\n" +
                        "            '"+custom_form_code+"' custom_form_code,\n" +
                        "            '"+custom_form_version+"' custom_form_version" +
                        "       ) sc,\n" +
                        "    "+ MD_SiteDao.TABLE +" s,\n" +
                        "    "+ MD_OperationDao.TABLE +" o,\n" +
                        "    "+ MD_ProductDao.TABLE +" p,\n" +
                        "    "+ GE_Custom_FormDao.TABLE +" f\n" +
                        " WHERE\n" +
                        "    sc.customer_code = s.customer_code\n" +
                        "    and sc.site_code = s.site_code\n" +
                        "    --\n" +
                        "    and sc.customer_code = o.customer_code\n" +
                        "    and sc.operation_code = o.operation_code\n" +
                        "    --\n" +
                        "    and sc.customer_code = p.customer_code\n" +
                        "    and sc.product_code = p.product_code\n" +
                        "    --\n" +
                        "    and sc.customer_code = f.customer_code\n" +
                        "    and sc.custom_form_type = f.custom_form_type\n" +
                        "    and sc.custom_form_code = f.custom_form_code\n" +
                        "    and sc.custom_form_version = f.custom_form_version\n")
                .toString();
    }
}
