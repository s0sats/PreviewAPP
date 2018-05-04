package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 */

public class SO_Pack_Express_Sql_005 implements Specification {

    private long customer_code;
    private String express_code;

    public SO_Pack_Express_Sql_005(long customer_code, String express_code) {
        this.customer_code = customer_code;
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
                        "    AND S.express_code =  '" + express_code + "' ")
                .toString();
    }
}
