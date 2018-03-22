package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 27/10/2017.
 */

public class MD_Product_Sql_006 implements Specification {
    private long customer_code;

    public MD_Product_Sql_006(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "   count(1) as count\n" +
                        " FROM\n" +
                        "      " + MD_ProductDao.TABLE + " p\n" +
                        " WHERE\n" +
                        "   p." + MD_ProductDao.CUSTOMER_CODE + " = '" + customer_code + " ''")
                .append(";")
                .append("count")
                .toString();
    }
}
