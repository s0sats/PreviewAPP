package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 21/03/2018.
 * Verifica se o serial esta vinculado a alguma O.S
 */

public class MD_Product_Serial_Sql_012 implements Specification {
    public static final String EXISTS = "EXISTS";

    private long customer_code;
    private long product_code;
    private long serial_code;

    public MD_Product_Serial_Sql_012(long customer_code, long product_code, long serial_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_code = serial_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) "+EXISTS+"\n" +
                        " FROM\n" +
                        "   "+ SM_SODao.TABLE+" s \n" +
                        " WHERE\n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and s.product_code = '"+product_code+"'\n" +
                        "   and s.serial_code = '"+serial_code+"'\n" +
                        ";"+EXISTS)
                .toString();
    }
}
