package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 03/08/2017.
 * <p>
 * Atualiza origin_change para APP
 */

public class SO_Pack_Express_Local_Sql_008 implements Specification {

    private long customer_code;
    private String token;

    public SO_Pack_Express_Local_Sql_008(long customer_code, String token) {
        this.customer_code = customer_code;
        this.token = token;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + SO_Pack_Express_LocalDao.TABLE + " set\n" +
                        "   token = '" + token + "'\n" +
                        " WHERE\n" +
                        "  customer_code = '" + customer_code + "'\n" +
                        "  and express_tmp in (select express_tmp from so_pack_expresss_local where status = 'NEW')\n")

                .toString();
    }
}
