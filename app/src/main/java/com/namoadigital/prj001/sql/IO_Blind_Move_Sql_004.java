package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Blind_Move_Sql_004 implements Specification {

    private long customer_code;
    private int blind_tmp;
    private long product_code;
    private String serial_id;

    public IO_Blind_Move_Sql_004(long customer_code, int blind_tmp, long product_code, String serial_id) {
        this.customer_code = customer_code;
        this.blind_tmp = blind_tmp;
        this.product_code = product_code;
        this.serial_id = serial_id;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
            .append(
                " SELECT\n" +
                        "   b.* " + "\n" +
                        " FROM\n" +
                        IO_Blind_MoveDao.TABLE + " b \n" +
                        " WHERE\n" +
                        "   b.customer_code = '" + customer_code + "'\n" +
                        "   and b.blind_tmp = '" + blind_tmp + "'\n" +
                        "   and b.product_code = '" + product_code + "'\n" +
                        "   and b.serial_id = '" + serial_id + "'\n"
            ).toString();
    }
}
