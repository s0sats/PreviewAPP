package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 14/09/2017.
 * Seleciona o tmp de serial ja existente ou proximo tmp do produto.
 */

public class MD_Product_Serial_Sql_006 implements Specification {
    public static final String NEXT_TMP = "NEXT_TMP";

    private long customer_code;
    private long product_code;
    private long serial_tmp;

    public MD_Product_Serial_Sql_006(long customer_code, long product_code, long serial_tmp) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_tmp = serial_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   s.serial_tmp \n" +
                        " FROM\n" +
                                MD_Product_SerialDao.TABLE +" s\n" +
                        " WHERE\n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and s.product_code = '"+product_code+"'\n" +
                        "   and s.serial_tmp = '"+serial_tmp+"'\n"
                )
                .toString();
    }
}
