package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 11/02/2020
 *
 * UPDATE que reseta sync_process para zero em nos agendamento em PENDING
 */

public class MD_Schedule_Exec_Sql_004 implements Specification {

    private long customer_code;

    public MD_Schedule_Exec_Sql_004(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        //
        return sb
                .append(" UPDATE "+MD_Schedule_ExecDao.TABLE+" SET\n" +
                        "      sync_process = 0\n" +
                        " WHERE \n" +
                        "      customer_code = '"+customer_code+"'\n" +
                        "      AND status = '"+ ConstantBaseApp.SYS_STATUS_PENDING +"'\n")
                .toString();
    }
}
