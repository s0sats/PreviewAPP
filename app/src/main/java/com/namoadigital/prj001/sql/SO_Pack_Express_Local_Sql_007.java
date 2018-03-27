package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 13/02/2017.
 */

public class SO_Pack_Express_Local_Sql_007 implements Specification {

    private long s_customer_code;
    private int s_pending;

    public SO_Pack_Express_Local_Sql_007(long s_customer_code, int s_pending) {
        this.s_customer_code = s_customer_code;
        this.s_pending = s_pending;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        String tokenFilter = "";
        //Baseado no parameto, define se busca novos token
        // ou token pendente
        if (s_pending == 1) {
            tokenFilter =
                    "       AND S." + SO_Pack_Express_LocalDao.TOKEN + " != ''";
        } else {
            tokenFilter =
                    "       AND S." + SO_Pack_Express_LocalDao.TOKEN + " = ''";
        }

        return sb
                .append(
                        " SELECT " +
                                "     S.*  " +
                                "  FROM " +
                                SO_Pack_Express_LocalDao.TABLE + " S " +
                                "  WHERE " +
                                "      S." + SO_Pack_Express_LocalDao.CUSTOMER_CODE + " = '" + s_customer_code + "'" +
                                "      AND S." + SO_Pack_Express_LocalDao.STATUS + " = 'NEW' " +
                                tokenFilter +
                                "  ORDER BY " +
                                "      S.customer_code,  " +
                                "      S.express_tmp ;"
                )
                .toString();
    }
}
