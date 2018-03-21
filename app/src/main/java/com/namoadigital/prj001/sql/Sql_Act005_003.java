package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 18/04/2017.
 */

public class Sql_Act005_003 implements Specification {

    public static final String BADGE_SCHEDULED_QTY = "scheduled_qty";
    private String customer_code;

    public Sql_Act005_003(String customer_code) {
        this.customer_code = customer_code;
    }


    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) "+ BADGE_SCHEDULED_QTY +" \n" +
                        " FROM\n" +
                        "   ge_custom_forms_local l\n" +
                        " WHERE\n" +
                        "   l.customer_code = '"+customer_code+"' \n" +
                        "   and l.custom_form_data_serv is not null " +
                        "   and l.schedule_date_start_format_ms < (strftime('%s', 'now')  * 1000 ) " +
                        "   and l.custom_form_status = '"+ Constant.SYS_STATUS_SCHEDULE+"'\n")
                .append(";"+ BADGE_SCHEDULED_QTY)
                .toString();
    }
}
