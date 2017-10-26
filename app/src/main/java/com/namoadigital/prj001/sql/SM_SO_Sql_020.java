package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 26/10/2017.
 *
 * Seta update required pra o valor enviado
 *
 */

public class SM_SO_Sql_020 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int update_required;

    public SM_SO_Sql_020(long customer_code, int so_prefix, int so_code, int update_required) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.update_required = update_required;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+ SM_SODao.TABLE+" set\n" +
                        "   update_required = '"+update_required+"'\n"+
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and so_prefix = '"+so_prefix+"'\n" +
                        "  and so_code = '"+so_code+"'")
                .toString();
    }
}
