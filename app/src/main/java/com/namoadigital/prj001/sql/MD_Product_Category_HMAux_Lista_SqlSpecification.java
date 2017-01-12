package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_CategoryDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class MD_Product_Category_HMAux_Lista_SqlSpecification implements Specification {

    private String s_customer_code;

    public MD_Product_Category_HMAux_Lista_SqlSpecification(String s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(MD_Product_CategoryDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(MD_Product_CategoryDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_customer_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(MD_Product_CategoryDao.ACTIVE)
                .append(" ='")
                .append(1)
                .append("'")
                .append(" )")
                .append(" )")
                .append(" order by ")
                .append(" customer_code, category_desc ")
                .append(";")
                .append("customer_code#category_code#category_code_father#struc_type#product_code#category_desc#active")
                .toString();
    }


}
