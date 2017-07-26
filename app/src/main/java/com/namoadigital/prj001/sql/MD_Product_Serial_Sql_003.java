package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/06/2017.
 * <p>
 * <p>
 *  Apaga serial temporario que estava com code 0
 * <p>
 * Retorna HMAux
 */

public class MD_Product_Serial_Sql_003 implements Specification {

    private long customer_code;
    private long product_code;

    public MD_Product_Serial_Sql_003(long customer_code, long product_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE FROM md_product_serials \n" +
                        " WHERE\n" +
                        "    customer_code = '" + customer_code + "'\n" +
                        "    and product_code =  '" + product_code + "'\n" +
                        "    and serial_code = '0';")
                .toString();
    }
}
