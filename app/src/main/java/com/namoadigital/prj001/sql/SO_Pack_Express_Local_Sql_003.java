package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 21/06/2017.
 * <p>
 * Seleciona qtd de S.O's com update_required = 1
 */

public class SO_Pack_Express_Local_Sql_003 implements Specification {

    private long customer_code;
    private long site_code;
    private long operation_code;
    private long product_code;
    private String express_code;
    private long express_tmp;
    private String status;

    public SO_Pack_Express_Local_Sql_003(long customer_code, long site_code, long operation_code, long product_code, String express_code, long express_tmp, String status) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.operation_code = operation_code;
        this.product_code = product_code;
        this.express_code = express_code;
        this.express_tmp = express_tmp;
        this.status = status;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + SO_Pack_Express_LocalDao.TABLE + " set\n" +
                        "   status = '" + status + "'\n" +
                        " WHERE\n" +
                        "    customer_code =  '" + customer_code + "'\n" +
                        "    AND site_code =  '" + site_code + "'\n" +
                        "    AND operation_code =    '" + operation_code + "'\n" +
                        "    AND product_code =    '" + product_code + "'\n" +
                        "    AND express_code =  '" + express_code + "'\n" +
                        "    AND express_tmp =  '" + express_tmp + "' ")
                .toString();
    }
}
