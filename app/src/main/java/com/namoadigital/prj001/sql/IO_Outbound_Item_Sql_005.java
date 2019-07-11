package com.namoadigital.prj001.sql;


import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;

/**
 * BARRIONUEVO - 17/06/2019
 *
 * Query que retorna toda as Outbound pendente de envio
 *
 */
public class IO_Outbound_Item_Sql_005 implements Specification {
    private long customer_code;
    private int outbound_prefix;
    private int outbound_code;
    private int update_required;

    public IO_Outbound_Item_Sql_005(long customer_code, int outbound_prefix, int outbound_code, int update_required) {
        this.customer_code = customer_code;
        this.outbound_prefix = outbound_prefix;
        this.outbound_code = outbound_code;
        this.update_required = update_required;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+ IO_Outbound_ItemDao.TABLE + "  SET\n" +
                        "   update_required = '"+update_required+"'\n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"'\n" +
                        "   and outbound_prefix = '"+outbound_prefix+"'\n" +
                        "   and outbound_code = '"+outbound_code+"'\n" +
                        "; \n"
                )
                .toString();
    }
}
