package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 30/11/2017.
 * <p>
 * Remove uma unica room baseada no Room code
 */

public class SO_Pack_Express_Local_Sql_002 implements Specification {

    private long customer_code;
    private long site_code;
    private long operation_code;
    private long product_code;
    private String express_code;
    private long express_tmp;

    public SO_Pack_Express_Local_Sql_002(long customer_code, long site_code, long operation_code, long product_code, String express_code, long express_tmp) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.operation_code = operation_code;
        this.product_code = product_code;
        this.express_code = express_code;
        this.express_tmp = express_tmp;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" DELETE FROM " + SO_Pack_Express_LocalDao.TABLE + " \n" +
                        " WHERE\n" +
                        "    customer_code =  '" + customer_code + "'\n" +
                        "    AND site_code =  '" + site_code + "'\n" +
                        "    AND operation_code =    '" + operation_code + "'\n" +
                        "    AND product_code =    '" + product_code + "'\n" +
                        "    AND S.express_code =  '" + express_code + "'\n" +
                        "    AND S.express_tmp =  '" + express_tmp + "' ")
                .toString();
    }
}
