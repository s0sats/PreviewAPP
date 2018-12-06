package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
/**
 * Created by l.barrionuevo on 06/12/2018.
 * <p>
 * <p>
 * Query que seleciona serial vi PK
 * <p>
 *
 */
public class MD_Product_Serial_Sql_016 implements Specification {
    private long customer_code;
    private long product_code;
    private String serial_id;

    public MD_Product_Serial_Sql_016(long customer_code, long product_code, String serial_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_id = serial_code;
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
                        "    and ps.serial_code = '" + serial_id + "' ")
                .toString();
    }
}
