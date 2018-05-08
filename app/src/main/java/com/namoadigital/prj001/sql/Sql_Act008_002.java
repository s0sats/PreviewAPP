package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 12/05/2017.
 * <p>
 * Query que retorna dados do produto, no modelo md_product, porem buscando da
 * tabela custom_form_local.
 * Usada no caso de um form agendado chamar a act008
 * 21/11/2017
 * Após a mudança do obj md_product, foi necessario adicionar os campos
 * un, sketch_code, sketch_url, sketch_url_local, sketch_lines, sketch_columns e sketch_olor
 * com valores chumbados para q o obj gerado pela query não desse NULL
 * 08/05/2018
 * Após nova mudança do obj md_product, foi necessario adicionar os campos
 * local_control , io_control , serial_rule , serial_min_length , serial_max_length
 * * com valores chumbados para q o obj gerado pela query não desse NULL
 */

public class Sql_Act008_002 implements Specification {

    private String customer_code;
    private String custom_form_type;
    private String custom_form_code;
    private String custom_form_version;
    private String custom_form_data;


    public Sql_Act008_002(String customer_code, String custom_form_type, String custom_form_code, String custom_form_version, String custom_form_data) {
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
                        "   l.customer_code,\n" +
                        "   l.custom_product_code product_code,\n" +
                        "   l.custom_product_id product_id,\n" +
                        "   l.custom_product_desc product_desc,\n" +
                        "   l.require_serial,\n" +
                        "   l.allow_new_serial_cl,\n" +
                        //"   '0' flag_offline,\n" +
                        "   'TST' un,\n" +
                        "   0 sketch_code,\n" +
                        "   '' sketch_url,\n" +
                        "   '' sketch_url_local,\n" +
                        "   0 sketch_lines,\n" +
                        "   0 sketch_columns,\n" +
                        "   '#FFFFFF' sketch_color\n," +
                        "   1 flag_offline,\n" +
                        "   0 local_control,\n" +
                        "   0 io_control,\n" +
                        "   null serial_rule,\n" +
                        "   null serial_min_length,\n" +
                        "   null serial_max_length\n" +
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
