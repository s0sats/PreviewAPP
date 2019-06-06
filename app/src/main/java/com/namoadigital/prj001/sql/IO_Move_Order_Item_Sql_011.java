package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/*
    Seleção de move para processo offline
 */
public class IO_Move_Order_Item_Sql_011 implements Specification {
    private long customer_code;
    private int product_code;
    private int serial_code;

    public IO_Move_Order_Item_Sql_011(long customer_code, int product_code, int serial_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_code = serial_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "   i.*\n" +
                        " FROM\n" +
                        IO_MoveDao.TABLE + " i \n" +
                        " WHERE\n" +
                        "   i.customer_code = '"+customer_code+"'\n" +
                        "   and i.product_code = '"+ product_code +"'\n" +
                        "   and i.serial_code = '"+ serial_code +"'\n" +
//                        "   and i.status = '"+ ConstantBaseApp.SYS_STATUS_PENDING +"'\n" +
                        "; \n"
                )
                .toString();
    }
}
