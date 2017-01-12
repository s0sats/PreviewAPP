package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_Customer_TranslateDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 11/07/17.
 */

public class EV_Customer_Translate_SqlSpecification_001 implements Specification {

    private String s_customer_code;
    private String s_translate_code;


    public EV_Customer_Translate_SqlSpecification_001(String s_customer_code, String s_translete_code) {
        this.s_customer_code = s_customer_code;
        this.s_translate_code = s_translete_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(EV_Customer_TranslateDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(EV_Customer_TranslateDao.CUSTOMER_CODE)
                .append(" ='")
                .append(s_customer_code)
                .append("'")
                .append(" )")
                .append(" and ")
                .append(" (")
                .append(EV_Customer_TranslateDao.TRANSLATE_CODE)
                .append(" ='")
                .append(s_translate_code)
                .append("'")
                .append(" )")
                .append(" )")
                .append(";")
                .toString();
    }
}
