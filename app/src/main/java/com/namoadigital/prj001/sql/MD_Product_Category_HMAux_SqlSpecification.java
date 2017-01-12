package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_CategoryDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class MD_Product_Category_HMAux_SqlSpecification implements Specification {

    private String s_customer_code;
    private String s_category_code;

    public MD_Product_Category_HMAux_SqlSpecification(String s_customer_code, String s_category_code) {
        this.s_customer_code = s_customer_code;
        this.s_category_code = s_category_code;
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
                .append(MD_Product_CategoryDao.CATEGORY_CODE)
                .append(" ='")
                .append(s_category_code)
                .append("'")
                .append(" )")
                .append(" )")
                .append(";")
                .toString();
    }


}
