package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_ClassDao;
import com.namoadigital.prj001.database.Specification;

public class MD_Class_Sql_001 implements Specification {
    private long customer_code;
    private int class_code;


    public MD_Class_Sql_001(long customer_code, int class_code) {
        this.customer_code = customer_code;
        this.class_code = class_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append("SELECT\n" +
                        "  c.*\n" +
                        "FROM\n" + MD_ClassDao.TABLE +" c " +
                        "WHERE\n" +
                        " c.customer_code = "+customer_code+"\n" +
                        " and c.class_code ="+class_code)
                .toString();


    }
}
