package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_CustomerDao;
import com.namoadigital.prj001.dao.EV_TranslateDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class EV_Customer_Id_SqlSpecification implements Specification {

    private String s_EV_Customer_Code;

    public EV_Customer_Id_SqlSpecification(String s_EV_Customer_Code) {
        this.s_EV_Customer_Code = s_EV_Customer_Code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(EV_CustomerDao.TABLE)
                .append(" where ")
                .append(EV_CustomerDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_EV_Customer_Code)
                .append("'")
                .append(";")
                .toString();
    }
}
