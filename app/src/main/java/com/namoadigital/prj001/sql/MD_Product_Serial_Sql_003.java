package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 *
 * Modificada em 21/09/2017 para remover novo serial que não foi transmitido
 *
 */

public class MD_Product_Serial_Sql_003 implements Specification {

    private long customer_code;
    private long product_code;
    private long serial_tmp;

    public MD_Product_Serial_Sql_003(long customer_code, long product_code, long serial_tmp) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_tmp = serial_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE FROM md_product_serials \n" +
                        " WHERE\n" +
                        "    customer_code = '" + customer_code + "'\n" +
                        "    and product_code =  '" + product_code + "'\n" +
                        "    and serial_code = '0' \n"+
                        "    and serial_tmp = '"+serial_tmp+"'")
                .toString();
    }
}
