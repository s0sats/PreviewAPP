package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 16/04/2018.
 *
 * Query que calcula valor total dos serviços de uma S.O
 */

public class Sql_Act043_002 implements Specification {
    public static final String TOTAL_PRICE = "TOTAL_PRICE";


    private long customer_code;
    private int so_prefix;
    private int so_code;

    public Sql_Act043_002(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append("   SELECT\n" +
                        "       printf(\"%.2f\",sum(s.qty * s.price)) "+ TOTAL_PRICE +"\n" +
                        "   FROM\n" +
                            SM_SO_ServiceDao.TABLE +" s\n" +
                        "   WHERE\n" +
                        "      s.customer_code = '"+customer_code+"'\n" +
                        "      and s.so_prefix = '"+so_prefix+"'\n" +
                        "      and s.so_code = '"+so_code+"'\n" +
                        "      and s.status <> '"+ Constant.SYS_STATUS_CANCELLED+"' ")
                .append(";")
//                .append(
//                        TOTAL_PRICE
//                )
                .toString();
    }
}
