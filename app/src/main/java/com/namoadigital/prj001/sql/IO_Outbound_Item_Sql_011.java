package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 18/07/2019
 *
 * Query que seleciona item da outbound via produto e serial.
 * Usado no processo offline do verificar existentes.
 */
public class IO_Outbound_Item_Sql_011 implements Specification {

    private long customer_code;
    private int product_code;
    private int serial_code;

    public IO_Outbound_Item_Sql_011(long customer_code, int product_code, int serial_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_code = serial_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(" SELECT\n" +
                    "   o.*\n" +
                    " FROM\n" +
                        IO_Outbound_ItemDao.TABLE + " o \n" +
                    " WHERE\n" +
                    "   o.customer_code = '"+customer_code+"'\n" +
                    "   and o.product_code = '"+ product_code +"'\n" +
                    "   and o.serial_code = '"+ serial_code +"'\n" +
                    "   and o.status not in('"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"',\n" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"',\n" +
                    "                       '"+ ConstantBaseApp.SYS_STATUS_DONE +"'\n" +
                    "                       )\n " +
                    "; \n"
            )
            .toString();
    }
}
