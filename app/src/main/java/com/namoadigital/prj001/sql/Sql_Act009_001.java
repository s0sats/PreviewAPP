package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class Sql_Act009_001 implements Specification {

    private String s_customer_code;
    private String s_product_code;
    private String s_translate_code;


    public Sql_Act009_001(String s_customer_code, String s_product_code, String s_translate_code) {
        this.s_customer_code = s_customer_code;
        this.s_product_code = s_product_code;
        this.s_translate_code = s_translate_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select resource_code, txt_code, txt_value from ")
                .append(EV_Module_Res_Txt_TransDao.TABLE)
                .append(" where ")
                .append(" resource_code in ")
                .append(" (")
                .append(" select resource_code from ev_module_ress where resource_name = 'GE_CUSTOM_FORM_TYPE'")
                .append(" )")
                .append(" and")
                .append(" txt_code in ")
                .append(" (")
                .append(" select distinct '")
                .append(s_customer_code)
                .append("' || '|' || custom_form_type from ge_custom_form_products where customer_code = '")
                .append(s_customer_code)
                .append("' and product_code = '")
                .append(s_product_code)
                .append("' and active = '1'")
                .append(" )")
                .append(" and translate_code = '")
                .append(s_translate_code)
                .append("' and txt_code in (")
                //
                .append(" select distinct '")
                .append(s_customer_code)
                .append("' || '|' || custom_form_type from ge_custom_forms where custom_form_status = 'ACTIVE'")
                //
                .append(");")
                .append("resource_code#txt_code#txt_value")
                .toString();
    }
}
