package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 23/03/2018.
 * Seleciona todas os seriais com um serial_id especifico
 * SEM LEVAR EM CONSIDERAÇÃO O PRODUTO.
 */

public class MD_Product_Serial_Sql_014 implements Specification {
    private long customer_code;
    private String serial_id;

    public MD_Product_Serial_Sql_014(long customer_code, String serial_id) {
        this.customer_code = customer_code;
        this.serial_id = serial_id;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "  s.* \n" +
                        " FROM "+ MD_Product_SerialDao.TABLE+" s \n" +
                        " WHERE\n" +
                        "    s.customer_code = '" + customer_code + "'\n" +
                        "    and s.serial_id = '"+serial_id+"'")
                .toString();
    }
}
