package com.namoadigital.prj001.sql;


import com.namoadigital.prj001.dao.EV_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class EV_Customer_HMAux_SqlSpecification implements Specification {

    private String s_customer_code;

    public EV_Customer_HMAux_SqlSpecification(String s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select customer_code, date_db_customer from ")
                .append(EV_CustomerDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(EV_CustomerDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_customer_code)
                .append("'")
                .append(" )")
                .append(" )")
                .append(";")
                .append("customer_code#date_db_customer")
                .toString();
    }
}
