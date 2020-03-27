package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 24/03/2020
 * Criado query que selecion os agendamentos que serão atualizado pelo serviço de FCM
 */

public class Sql_Schedule_FCM_001 implements Specification {

    private String customer_code = "-1";
    private String schedule_prefix = "-1";
    private String schedule_code = "-1";
    private String schedule_exec = "-1";

    public Sql_Schedule_FCM_001(String customer_code, String schedule_prefix, String schedule_code, String schedule_exec) {
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
            .append(" SELECT \n" +
                    "   s.*\n" +
                    " FROM \n" +
                    "   " + MD_Schedule_ExecDao.TABLE + " s \n" +
                    " WHERE \n" +
                    "    s.customer_code = '"+customer_code+"'\n" +
                    "    AND s.schedule_prefix = '"+schedule_prefix+"'\n" +
                    "    AND s.schedule_code = '"+schedule_code+"'\n" +
                    "    AND ('"+schedule_exec+"' = '-1' OR s.schedule_exec = '"+schedule_exec+"')\n"
                ).toString();
    }
}
