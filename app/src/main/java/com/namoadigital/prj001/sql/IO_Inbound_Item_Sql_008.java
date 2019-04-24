package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 09/04/2019
 *
 * Seleciona itens update_required de um inbound
 *
 */
public class IO_Inbound_Item_Sql_008 implements Specification {
    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;

    public IO_Inbound_Item_Sql_008(long customer_code, int inbound_prefix, int inbound_code) {
        this.customer_code = customer_code;
        this.inbound_prefix = inbound_prefix;
        this.inbound_code = inbound_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "     it.*\n" +
                        " FROM\n" +
                          IO_Inbound_ItemDao.TABLE +" it \n" +
                        " WHERE\n" +
                        "     it.customer_code = '"+customer_code+"'\n" +
                        "     and it.inbound_prefix = '"+inbound_prefix+"' \n" +
                        "     and it.inbound_code ='"+inbound_code+"'\n" +
                        "     and it.update_required = 1\n")
                .toString();
    }
}
