package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 */

public class SO_Pack_Express_Local_Sql_006 implements Specification {

    public static final String NEXT_TMP = "next_tmp";

    private long customer_code;
    private long site_code;
    private long operation_code;
    private long product_code;
    private String express_code;

    public SO_Pack_Express_Local_Sql_006(long customer_code, long site_code, long operation_code, long product_code, String express_code) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.operation_code = operation_code;
        this.product_code = product_code;
        this.express_code = express_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   IFNULL(max(express_tmp),0) + 1 " + NEXT_TMP + "\n" +
                        " FROM\n" +
                        SO_Pack_Express_LocalDao.TABLE + " \n" +
                        " WHERE\n" +
                        "    customer_code =  '" + customer_code + "'\n" +
                        "    AND site_code =  '" + site_code + "'\n" +
                        "    AND operation_code =    '" + operation_code + "'\n" +
                        "    AND product_code =  '" + product_code + "'\n" +
                        "    AND express_code =  '" + express_code + "' ")
                .append(";" + NEXT_TMP)
                .toString();
    }
}
