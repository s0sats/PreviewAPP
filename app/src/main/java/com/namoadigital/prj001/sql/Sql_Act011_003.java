package com.namoadigital.prj001.sql;

import android.content.Context;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 26/04/2017.
 */

/**
 *
 *   Busca data serv e datas de inicio e fim do agendamento.
 */

public class Sql_Act011_003 implements Specification {

    private long s_customer_code;
    private String s_form_type;
    private String s_form_code;
    private String s_form_version;
    private String s_form_data;
    private String sqlite_date_format;


    public Sql_Act011_003(Context context,long s_customer_code, String s_form_type, String s_form_code, String s_form_version, String s_form_data) {
        this.s_customer_code = s_customer_code;
        this.s_form_type = s_form_type;
        this.s_form_code = s_form_code;
        this.s_form_version = s_form_version;
        this.s_form_data = s_form_data;
        this.sqlite_date_format = ToolBox_Inf.nlsDate2SqliteDate(context);
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "  l.custom_form_data_serv,\n"+
                        "  strftime('"+sqlite_date_format+" %H:%M',l.schedule_date_start_format,'localtime') schedule_date_start_format,\n"+
                        "  strftime('"+sqlite_date_format+" %H:%M',l.schedule_date_end_format,'localtime') schedule_date_end_format\n"+
                        " \n" +
                        "  FROM\n" +
                        GE_Custom_Form_LocalDao.TABLE+ " l\n" +
                        "  WHERE\n" +
                        "      l.customer_code = '"+s_customer_code+"'\n" +
                        "      AND l.custom_form_type = '"+s_form_type+"'\n" +
                        "      AND l.custom_form_code = '"+s_form_code+"'\n" +
                        "      AND l.custom_form_version = '"+s_form_version+"'\n" +
                        "      AND l.custom_form_data = '"+s_form_data+"'\n" +
                        "      AND l.custom_form_data_serv is not null \n" +
                        ";")
                .append("custom_form_data_serv#schedule_date_start_format#schedule_date_end_format")
                .toString();
    }
}
