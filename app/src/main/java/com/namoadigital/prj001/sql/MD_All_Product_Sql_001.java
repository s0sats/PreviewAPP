package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_All_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 06/02/2017.
 */

public class MD_All_Product_Sql_001 implements Specification {

    private long customer_code;
    private long product_code;

    public MD_All_Product_Sql_001(long customer_code, long product_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append(
                " SELECT \n" +
                "     *  \n" +
                "  FROM  \n" +
                MD_All_ProductDao.TABLE + " \n"+
                "  WHERE  \n" +
                MD_All_ProductDao.CUSTOMER_CODE + " = '"+ customer_code+"'  \n" +
                " AND " + MD_All_ProductDao.PRODUCT_CODE +  " = '"+product_code+"' \n"
        ).toString();

    }
}
