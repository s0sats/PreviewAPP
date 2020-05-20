package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 15/05/2020
 *
 * Query que busca o form_local via dados do agendamentos
 * Usado no metodo de conciliação dos agendamentos no sincronismo.
 */

public class MD_Schedule_Exec_Sql_008 implements Specification {

    private long customer_code;
    private int schedule_prefix;
    private int schedule_code;
    private int schedule_exec;

    public MD_Schedule_Exec_Sql_008(long customer_code, int schedule_prefix, int schedule_code, int schedule_exec) {
        this.customer_code = customer_code;
        this.schedule_prefix = schedule_prefix;
        this.schedule_code = schedule_code;
        this.schedule_exec = schedule_exec;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        //
        return sb
            .append(" SELECT\n" +
                    "   l.*\n" +
                    " FROM\n" +
                    "   "+ GE_Custom_Form_LocalDao.TABLE +" l\n" +
                    " WHERE \n" +
                    "   l.customer_code = '"+customer_code+"'\n" +
                    "   and l.schedule_prefix = '"+schedule_prefix+"'\n" +
                    "   and l.schedule_code = '"+schedule_code+"'\n" +
                    "   and l.schedule_exec = '"+schedule_exec+"'\n")
            .toString();
    }
}
