package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 01/03/2018.
 * <p>
 * Set nome do pdf no camp custom_form_url_local.
 *
 * OBS: A PK utilizada esta sendo a do form data, pois todos os form_ap de uma mesma
 * "resposta"(custom_form_data) comparatilham o mesmo pdf.
 */

public class GE_Custom_Form_Ap_Sql_008 implements Specification {
    
    private String customer_code;
    private String custom_form_type;
    private String custom_form_code;
    private String custom_form_version;
    private String custom_form_data;
    private String file_local_name;


    public GE_Custom_Form_Ap_Sql_008(String customer_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data, String file_local_name) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
        this.custom_form_data = custom_form_data;
        this.file_local_name = file_local_name;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+ GE_Custom_Form_ApDao.TABLE+" SET\n" +
                        "      custom_form_url_local = '"+file_local_name+"'\n" +
                        " WHERE\n" +
                        "       customer_code = '"+customer_code+"'\n" +
                        "       and custom_form_type = '"+custom_form_type+"'\n" +
                        "       and custom_form_code = '"+custom_form_code+"'\n" +
                        "       and custom_form_version = '"+custom_form_version+"'\n" +
                        "       and custom_form_data = '"+custom_form_data+"'\n")
                .append(";")
                .toString();
    }
}
