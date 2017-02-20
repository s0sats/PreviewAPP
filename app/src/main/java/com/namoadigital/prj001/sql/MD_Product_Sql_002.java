package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 20/02/2017.
 */

public class MD_Product_Sql_002 implements Specification {

    private long customer_code;

    public MD_Product_Sql_002(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT " +
                        "     * " +
                        "  FROM " +
                        MD_ProductDao.TABLE +
                        "  WHERE " +
                        MD_ProductDao.CUSTOMER_CODE + " = '"+ customer_code+"' " +
                        //" AND " + MD_ProductDao.PRODUCT_CODE + " = '23' " +
                        "  ORDER BY " +
                        "     product_code")
                .toString();
    }
}
