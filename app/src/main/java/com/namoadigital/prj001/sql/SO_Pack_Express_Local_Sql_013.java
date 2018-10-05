package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 05/10/2018.
 *
 * Verifica se ja existe um pacote + serial pendente de envio
 *
 */

public class SO_Pack_Express_Local_Sql_013 implements Specification {

    public static final String ALREADY_NEW_EXPRESS_ORDER = "ALREADY_NEW_EXPRESS_ORDER";

    private long customer_code;
    private String site_code;
    private long operation_code;
    private long product_code;
    private String express_code;
    private String serial_id;

    public SO_Pack_Express_Local_Sql_013(long customer_code, String site_code, long operation_code, long product_code, String express_code, String serial_id) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.operation_code = operation_code;
        this.product_code = product_code;
        this.express_code = express_code;
        this.serial_id = serial_id;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      IFNULL(COUNT(S.express_code),0) "+ALREADY_NEW_EXPRESS_ORDER+"\n" +
                        //"      S.*\n" +
                        " FROM\n" +
                        SO_Pack_Express_LocalDao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =       '" + customer_code + "'\n" +
                        "    AND S.site_code =       '" + site_code + "'\n" +
                        "    AND S.operation_code =  '" + operation_code + "'\n" +
                        "    AND S.product_code =    '" + product_code + "'\n" +
                        "    AND S.express_code =    '" + express_code + "'\n" +
                        "    AND S.serial_id =    '" + serial_id + "'\n" +
                        "    AND S.status = 'NEW'\n")
                .append(";"+ALREADY_NEW_EXPRESS_ORDER)
                .toString();
    }
}
