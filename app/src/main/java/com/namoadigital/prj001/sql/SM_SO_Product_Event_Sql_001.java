package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 06/11/2017
 *
 * Seleciona todos eventos da S.O - Retorna Lista de obj SM_SO_Product_event
 *
 */

public class SM_SO_Product_Event_Sql_001 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;

    public SM_SO_Product_Event_Sql_001(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     e.*\n" +
                        " FROM\n" +
                        "    "+ SM_SO_Product_EventDao.TABLE+" e\n" +
                        " WHERE\n" +
                        "    e.customer_code = '"+customer_code+"'\n" +
                        "    and e.so_prefix = '"+so_prefix+"'\n" +
                        "    and e.so_code = '"+so_code+"'\n")
                .toString();
    }
}
