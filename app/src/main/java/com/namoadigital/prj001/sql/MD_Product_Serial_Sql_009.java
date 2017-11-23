package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 13/11/2017.
 * <p>
 * <p>
 * Query que seleciona todos do serial
 * Diferente da MD_Product_Serial_Sql_002, usa serial_code como filtro.
 * <p>
 * Retorna HMAux
 */

public class MD_Product_Serial_Sql_009 implements Specification {

    private long customer_code;
    private long product_code;
    private int serial_code;

    public MD_Product_Serial_Sql_009(long customer_code, long product_code, int serial_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_code = serial_code;
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
                        "    ps.customer_code = '" + customer_code + "'\n" +
                        "    and ps.product_code = '" + product_code + "'\n" +
                        "    and ps.serial_code = '" + serial_code + "' ")
                .toString();
    }
}
