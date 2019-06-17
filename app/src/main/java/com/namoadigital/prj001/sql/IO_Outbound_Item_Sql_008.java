package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;


/**
 * BARRIONUEVO  - 09/04/2019
 *
 * Seleciona itens update_required de um inbound
 *
 */
public class IO_Outbound_Item_Sql_008 implements Specification {
    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;

    public IO_Outbound_Item_Sql_008(long customer_code, int outbound_prefix, int outbound_code) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "     it.*\n" +
                        " FROM\n" +
                        IO_Outbound_ItemDao.TABLE +" it \n" +
                        " WHERE\n" +
                        "     it.customer_code = '"+customer_code+"'\n" +
                        "     and it.outbound_prefix = '"+outbound_prefix+"' \n" +
                        "     and it.outbound_code ='"+outbound_code+"'\n" +
                        "     and it.update_required = 1\n")
                .toString();
    }
}
