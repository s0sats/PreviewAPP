package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 11/02/2020
 *
 * Query que seleciona os forms que não foram processados no sincronismo e que
 * podem ser deletados.
 *
 * Query usada no metodo de consiliação dos agendamentos.
 */

public class MD_Schedule_Exec_Sql_003 implements Specification {

    private long customer_code;

    public MD_Schedule_Exec_Sql_003(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        //
        return sb
                .append(" SELECT\n" +
                        "       s.*\n" +
                        " FROM \n" +
                            MD_Schedule_ExecDao.TABLE +" s \n" +
                        " WHERE \n" +
                        "      s.customer_code = '"+customer_code+"'\n" +
                        "      AND s.sync_process = 0\n" +
                        "      AND s.status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE +"'\n" +
                        " ORDER BY\n" +
                        "    s.customer_code,\n" +
                        "    s.schedule_prefix,\n" +
                        "    s.schedule_code,\n" +
                        "    s.schedule_exec")
                .toString();
    }
}
