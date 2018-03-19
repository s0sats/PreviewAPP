package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 07/03/2018.
 * <p>
 * Deleta registro Ap do banco.
 */

public class GE_Custom_Form_Ap_Sql_010 implements Specification {

    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private long custom_form_data;
    private int ap_code;

    public GE_Custom_Form_Ap_Sql_010(long customer_code, int custom_form_type, int custom_form_code, int custom_form_version, long custom_form_data, int ap_code) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
        this.custom_form_data = custom_form_data;
        this.ap_code = ap_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE \n" +
                        " FROM \n "+ GE_Custom_Form_ApDao.TABLE+"\n" +
                        " WHERE\n" +
                        "       customer_code = '"+customer_code+"'\n" +
                        "       and custom_form_type = '"+custom_form_type+"'\n" +
                        "       and custom_form_code = '"+custom_form_code+"'\n" +
                        "       and custom_form_version = '"+custom_form_version+"'\n" +
                        "       and custom_form_data = '"+custom_form_data+"'\n" +
                        "       and ap_code = '"+ap_code+"'\n")
                .append(";")
                .toString();
    }
}
