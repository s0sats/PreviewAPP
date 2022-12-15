package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class MD_Schedule_Exec_Sql_010 implements Specification {
    private long customer_code;
    private int schedule_prefix;
    private int schedule_code;
    private int schedule_exec;

    public MD_Schedule_Exec_Sql_010(long customer_code, int schedule_prefix, int schedule_code, int schedule_exec) {
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
                .append(" SELECT *\n" +
                        " FROM\n" +
                        "   "+ MD_Schedule_ExecDao.TABLE +"\n" +
                        " WHERE \n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and schedule_prefix = '"+schedule_prefix+"'\n" +
                        "   and schedule_code = '"+schedule_code+"'\n" +
                        "   and schedule_exec = '"+schedule_exec+"'\n" +
                        "   and status = '"+ ConstantBaseApp.SYS_STATUS_SCHEDULE +"'\n" +
                        "   and NOT EXISTS (SELECT 1 \n" +
                        "                      FROM ge_custom_forms_local l\n" +
                        "                      WHERE l.customer_code = "+MD_Schedule_ExecDao.TABLE+".customer_code\n" +
                        "                            and l.schedule_prefix = "+MD_Schedule_ExecDao.TABLE+".schedule_prefix\n" +
                        "                            and l.schedule_code = "+MD_Schedule_ExecDao.TABLE+".schedule_code\n" +
                        "                            and l.schedule_exec = "+MD_Schedule_ExecDao.TABLE+".schedule_exec )\n"
                )
                .toString();
    }
}
