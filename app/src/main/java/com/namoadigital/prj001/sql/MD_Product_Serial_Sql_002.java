package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/06/2017.
 *
 *
 * Query que seleciona todos do serial
 *
 * Retorna HMAux
 *
 */

public class MD_Product_Serial_Sql_002 implements Specification {

    private long customer_code;
    private long product_code;
    private String serial_id;

    public MD_Product_Serial_Sql_002(long customer_code, long product_code, String serial_id) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_id = serial_id;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "    ps.*\n" +
                        " FROM\n" +
                        "     md_product_serials ps\n" +
                        " WHERE\n" +
                        "    ps.customer_code = '"+customer_code+"'\n" +
                        "    and ps.product_code = '"+product_code+"'\n" +
                        "    and ps.serial_id = '"+serial_id+"';")
                .toString();
    }
}
