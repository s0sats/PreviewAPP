package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class MD_Product_HMAux_SqlSpecification implements Specification {

    private String s_customer_code;
    private String s_product_code;

    public MD_Product_HMAux_SqlSpecification(String s_customer_code, String s_product_code) {
        this.s_customer_code = s_customer_code;
        this.s_product_code = s_product_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(MD_ProductDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(MD_ProductDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_customer_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(MD_ProductDao.PRODUCT_CODE)
                .append(" ='")
                .append(s_product_code)
                .append("'")
                .append(" )")
                .append(" )")
                .append(";")
                .toString();
    }


}
