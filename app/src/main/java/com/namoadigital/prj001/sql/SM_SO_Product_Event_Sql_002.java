package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 06/11/2017
 *
 * Seleciona proximo seq_tmp do evento
 *
 */

public class SM_SO_Product_Event_Sql_002 implements Specification {

    public static final String NEXT_TMP = "next_tmp";

    private long customer_code;
    private int so_prefix;
    private int so_code;

    public SM_SO_Product_Event_Sql_002(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     IFNULL(MAX(e.seq_tmp),100) + 1 "+NEXT_TMP+"\n" +
                        " FROM\n" +
                        "    "+ SM_SO_Product_EventDao.TABLE+" e\n" +
                        " WHERE\n" +
                        "    e.customer_code = '"+customer_code+"'\n" +
                        "    and e.so_prefix = '"+so_prefix+"'\n" +
                        "    and e.so_code = '"+so_code+"'\n")
                .append(";")
                //.append(NEXT_TMP)
                .toString();
    }
}
