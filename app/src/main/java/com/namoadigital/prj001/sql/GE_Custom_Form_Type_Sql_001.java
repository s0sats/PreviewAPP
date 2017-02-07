package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Module_ResDao;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 07/02/2017.
 */

public class GE_Custom_Form_Type_Sql_001 implements Specification {

    public static final String CUSTOM_FORM_TYPE_DESC_FULL = "custom_form_type_desc_full";

    private long s_customer_code;
    private long s_product_code;
    private String s_translate_code;

    public GE_Custom_Form_Type_Sql_001(long s_customer_code, long s_product_code, String s_translate_code) {
        this.s_customer_code = s_customer_code;
        this.s_product_code = s_product_code;
        this.s_translate_code = s_translate_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                        "    T.*, " +
                        "    ( " +
                        "     SELECT " +
                        "        ts.txt_value " +
                        "     FROM " +
                                EV_Module_ResDao.TABLE +" mr, " +
                                EV_Module_Res_Txt_TransDao.TABLE +" ts " +
                        "     WHERE " +
                        "          ts.module_code = mr.module_code " +
                        "          and ts.resource_code = mr.resource_code " +
                        "          and ts.txt_code = t.customer_code || '|' || t.custom_form_type     " +
                        "           " +
                        "          and ts.module_code = 'CUST_FORM'       " +
                        "          and ts.translate_code = '"+s_translate_code+"' " +
                        "    ) custom_form_type_desc, " +
                        "    ( " +
                        "     SELECT " +
                        "       t.custom_form_type || ' - ' || ts.txt_value " +
                        "     FROM " +
                        EV_Module_ResDao.TABLE +" mr, " +
                        EV_Module_Res_Txt_TransDao.TABLE +" ts " +
                        "     WHERE " +
                        "          ts.module_code = mr.module_code " +
                        "          and ts.resource_code = mr.resource_code " +
                        "          and ts.txt_code = t.customer_code || '|' || t.custom_form_type     " +
                        "           " +
                        "          and ts.module_code = 'CUST_FORM'       " +
                        "          and ts.translate_code = '"+s_translate_code+"' " +
                        "    ) custom_form_type_desc_full " +
                        "     " +
                        " FROM " +
                        "   ge_custom_form_types t, " +
                        "   ge_custom_form_products p " +
                        " WHERE " +
                        "   t.customer_code = p.customer_code " +
                        "   AND t.custom_form_type = p.custom_form_type " +
                        "   " +
                        "   AND p.customer_code = '" + s_customer_code + "' " +
                        "   AND p.product_code = '" + s_product_code + "' " +
                        " order by " +
                        "   t.custom_form_type;" +
                        "custom_form_type#custom_form_type_desc#custom_form_type_desc_full")
                .toString();

    }
}
