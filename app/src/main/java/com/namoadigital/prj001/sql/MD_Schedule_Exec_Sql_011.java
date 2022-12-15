package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

public class MD_Schedule_Exec_Sql_011 implements Specification {
    private long customer_code;
    private int schedule_prefix;
    private int schedule_code;

    public MD_Schedule_Exec_Sql_011(long customer_code, int schedule_prefix, int schedule_code) {
        this.customer_code = customer_code;
        this.schedule_prefix = schedule_prefix;
        this.schedule_code = schedule_code;
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
                        "   and schedule_code = '"+schedule_code+"'\n" )
                .toString();
    }
}
