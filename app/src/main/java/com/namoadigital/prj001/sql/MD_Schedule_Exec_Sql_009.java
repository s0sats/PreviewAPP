package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;

public class MD_Schedule_Exec_Sql_009 implements Specification {
    private long customer_code;
    private int schedule_prefix;
    private int schedule_code;
    private int schedule_exec;

    public MD_Schedule_Exec_Sql_009(long customer_code, int schedule_prefix, int schedule_code, int schedule_exec) {
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
                .append(" DELETE\n" +
                        " FROM\n" +
                        "   "+ MD_Schedule_ExecDao.TABLE +"\n" +
                        " WHERE \n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and schedule_prefix = '"+schedule_prefix+"'\n" +
                        "   and schedule_code = '"+schedule_code+"'\n" +
                        "   and schedule_exec = '"+schedule_exec+"'\n")
                .toString();
    }
}
