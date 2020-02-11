package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;

public class MD_Schedule_Exec_Sql_002 implements Specification {

    private long customer_code;

    public MD_Schedule_Exec_Sql_002(long customer_code) {
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
                        " ORDER BY\n" +
                        "    s.customer_code,\n" +
                        "    s.schedule_prefix,\n" +
                        "    s.schedule_code,\n" +
                        "    s.schedule_exec")
                .toString();
    }
}
