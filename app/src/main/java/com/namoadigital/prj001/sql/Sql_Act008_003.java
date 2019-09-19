package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
/*
    19/09/2019 BARRIONUEVO
    Refatoracao da Sql_Act008_002, retorna um objeto GE_Custom_Form_LocalDao que será utilizado para
    a criação do md_product do agendamento caso user não tenho acesso ao produto.
*/
public class Sql_Act008_003 implements Specification {

    private String customer_code;
    private String custom_form_type;
    private String custom_form_code;
    private String custom_form_version;
    private String custom_form_data;


    public Sql_Act008_003(String customer_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data) {
        this.customer_code = customer_code;
        this.custom_form_type = custom_form_type;
        this.custom_form_code = custom_form_code;
        this.custom_form_version = custom_form_version;
        this.custom_form_data = custom_form_data;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n " +
                        "   l.*\n" +
                        //CAMPO CHUMBADOS POIS NÃO VEEM NO AGENDAMENTO
                        " FROM\n " +
                        GE_Custom_Form_LocalDao.TABLE + " l \n" +
                        " \n" +
                        " WHERE\n " +
                        "  l.customer_code = '" + customer_code + "' \n" +
                        "  and l.custom_form_type = '" + custom_form_type + "'\n " +
                        "  and l.custom_form_code = '" + custom_form_code + "'\n" +
                        "  and l.custom_form_version = '" + custom_form_version + "'\n" +
                        "  and l.custom_form_data = '" + custom_form_data + "';")
                .toString();
    }
}
