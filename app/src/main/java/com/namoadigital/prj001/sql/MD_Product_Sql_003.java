package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 20/02/2017.
 */

public class MD_Product_Sql_003 implements Specification {

    private long customer_code;
    private String product_code;
    private String product_id;

    public MD_Product_Sql_003(long customer_code, String product_code, String product_id) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.product_id = product_id;
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
                        " AND ( '"+product_code+"' is null OR " + MD_ProductDao.PRODUCT_CODE + " = '"+product_code+"' )" +
                        " AND ( '"+product_id+"' is null OR " + MD_ProductDao.PRODUCT_ID + " = '"+product_id+"' )")
                .toString().replace("'null'","null");
    }
}
