package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 14/09/2017.
 * Seleciona o tmp de serial ja existente ou proximo tmp do produto.
 */

public class MD_Product_Serial_Sql_007 implements Specification {
    public static final String NEXT_TMP = "NEXT_TMP";

    private long customer_code;
    private long product_code;
    private long serial_code;

    public MD_Product_Serial_Sql_007(long customer_code, long product_code, long serial_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_code = serial_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT\n" +
                        " IFNULL(\n" +
                        "  (SELECT\n" +
                        "      CASE WHEN MAX(s.serial_tmp) = 0\n" +
                        "           THEN NULL\n" +
                        "           ELSE MAX(s.serial_tmp)\n" +
                        "           END serial_tmp \n" +
                        "    FROM\n" +
                            MD_Product_SerialDao.TABLE +"  s\n" +
                        "   WHERE\n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and s.product_code = '"+product_code+"'\n" +
                        "   and s.serial_code > 0 and s.serial_code = '"+serial_code+"'\n" +
                        "  ),\n" +
                        "  (\n" +
                        "    SELECT  \n" +
                        "   ifnull(max(serial_tmp),0) + 1 \n" +
                        "    FROM\n" +
                            MD_Product_SerialDao.TABLE +"  s\n" +
                        "    WHERE\n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and s.product_code = '"+product_code+"'\n" +
                        "  )\n" +
                        " )" + MD_Product_SerialDao.SERIAL_TMP)
                .toString();
    }
}
