package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 07/03/18.
 *
 * Seleciona todos os AP DONE E CANCELLED COM MAIS DE 30 DIAS.
 */

public class WS_Cleaning_Sql_006 implements Specification {

    private long customer_code;
    private String date;


    public WS_Cleaning_Sql_006(long customer_code, String date) {
        this.customer_code = customer_code;
        this.date = date;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append("   SELECT\n" +
                        "     a.*\n" +
                        "   FROM\n" +
                                GE_Custom_Form_ApDao.TABLE +" a\n" +
                        "   WHERE\n" +
                        "     a.customer_code = '"+customer_code+"'\n" +
                        "     and date(a.last_update) < date('"+date+"')\n" +
                        "     and a.ap_status in ('"+ Constant.SYS_STATUS_DONE+"','"+ Constant.SYS_STATUS_CANCELLED+"')\n" +
                        "  ORDER BY\n" +
                        "     a.custom_form_type,\n   " +
                        "     a.custom_form_code,\n   " +
                        "     a.custom_form_version,\n" +
                        "     a.custom_form_data,\n   " +
                        "     a.ap_code\n   " )
                .append(";")
                .toString();
    }
}
