package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 06/07/17.
 */

public class SM_SO_Sql_Status_001 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;

    public SM_SO_Sql_Status_001(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE\n" +
                        SM_SODao.TABLE + "\n" +
                        " SET status = '" + Constant.SO_STATUS_PROCESS  + "' " +
                        " WHERE\n" +
                        "        customer_code =          '" + customer_code + "'\n" +
                        "    AND so_prefix =              '" + so_prefix + "'\n" +
                        "    AND so_code =                '" + so_code + "'\n" +

                        "    AND status =            '" + Constant.SO_STATUS_PENDING + "' "
                )
                .toString();
    }
}
