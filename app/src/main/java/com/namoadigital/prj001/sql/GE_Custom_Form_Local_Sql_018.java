package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 25/09/2019
 *
 * Atualiza url local do icone produto baixado
 *
 */

public class GE_Custom_Form_Local_Sql_018 implements Specification {
    private long customer_code;
    private String custom_form_type;
    private String custom_form_code;
    private String custom_form_version;
    private String custom_form_data;
    private String custom_product_icon_name;

    public GE_Custom_Form_Local_Sql_018(long customer_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data, String custom_product_icon_name) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
        this.custom_form_data = custom_form_data;
        this.custom_product_icon_name = custom_product_icon_name;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" UPDATE "+ GE_Custom_Form_LocalDao.TABLE +" SET\n" +
                    "   custom_product_icon_url_local = '"+custom_product_icon_name+"'\n" +
                    " WHERE\n" +
                    "   "+GE_Custom_Form_LocalDao.CUSTOMER_CODE+" = '"+customer_code+"'\n" +
                    "   and "+GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE +" = '"+custom_form_type+"'" +
                    "   and "+GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE+" = '"+custom_form_code+"'" +
                    "   and "+GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION +" = '"+custom_form_version+"'" +
                    "   and "+GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA+" = '"+custom_form_data+"'"
            )
            .toString();
    }
}
