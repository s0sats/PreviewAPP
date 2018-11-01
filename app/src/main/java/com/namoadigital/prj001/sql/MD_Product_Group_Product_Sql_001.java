package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_Group_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 24/01/2017.
 */

public class MD_Product_Group_Product_Sql_001 implements Specification {

    private long s_customer_code;
    private long s_group_code;
    private long s_product_code;

    public MD_Product_Group_Product_Sql_001(long s_customer_code, long s_group_code, long s_product_code) {
        this.s_customer_code = s_customer_code;
        this.s_group_code = s_group_code;
        this.s_product_code = s_product_code;
    }


    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append(
                "SELECT " +
                "   * " +
                "FROM ")
                .append(MD_Product_Group_ProductDao.TABLE)
                .append(" WHERE " +
                        MD_Product_Group_ProductDao.CUSTOMER_CODE +" = '"+s_customer_code+"'"+
                        " AND " +
                        MD_Product_Group_ProductDao.GROUP_CODE +" = '"+s_group_code+"'"+
                        " AND " +
                        MD_Product_Group_ProductDao.PRODUCT_CODE +" = '"+s_product_code+"'"+
                        "ORDER BY " +
                        MD_Product_Group_ProductDao.GROUP_CODE + " , " +
                        MD_Product_Group_ProductDao.PRODUCT_CODE
                )
                .append(";")
                //.append("customer_code#group_code#product_code")
                .toString();
    }
}
