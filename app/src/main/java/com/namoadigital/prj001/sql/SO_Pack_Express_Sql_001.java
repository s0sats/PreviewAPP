package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 */

public class SO_Pack_Express_Sql_001 implements Specification {

    private long customer_code;
    private long site_code;
    private long operation_code;
    private String express_code;

    public SO_Pack_Express_Sql_001(long customer_code, long site_code, long operation_code, String express_code) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.operation_code = operation_code;
        this.express_code = express_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*\n" +
                        " FROM\n" +
                        SO_Pack_ExpressDao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =  '" + customer_code + "'\n" +
                        "    AND S.site_code =  '" + site_code + "'\n" +
                        "    AND S.operation_code =    '" + operation_code + "'\n" +
                        "    AND S.express_code =  '" + express_code + "' ")
                .toString();
    }
}
