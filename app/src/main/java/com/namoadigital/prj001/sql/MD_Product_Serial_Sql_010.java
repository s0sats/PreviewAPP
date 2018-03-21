package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 21/03/2018.
 * <p>
 * <p>
 * Seta flag sync_process de TODOS OS SERIAIS para 0
 * <p>
 */

public class MD_Product_Serial_Sql_010 implements Specification {

    private long customer_code;

    public MD_Product_Serial_Sql_010(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("UPDATE "+ MD_Product_SerialDao.TABLE+" SET\n" +
                        "    sync_process = 0" +
                        " WHERE\n" +
                        "    customer_code = '" + customer_code + "'\n")
                .toString();
    }
}
