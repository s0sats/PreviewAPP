package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 07/11/2017
 *
 * Atualiza url_local do croqui baixado
 *
 */

public class SM_SO_Product_Event_Sql_005 implements Specification {

    private long customer_code;
    private String so_prefix;
    private String so_code;
    private String seq;
    private String sketch_url_local;

    public SM_SO_Product_Event_Sql_005(long customer_code, String so_prefix, String so_code, String seq, String sketch_url_local) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.seq = seq;
        this.sketch_url_local = sketch_url_local;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " +SM_SO_Product_EventDao.TABLE+" SET \n" +
                        "  sketch_url_local = '"+sketch_url_local+"'" +
                        " WHERE\n" +
                        "    customer_code = '"+customer_code+"'\n" +
                        "    and so_prefix = '"+so_prefix+"'\n" +
                        "    and so_code = '"+so_code+"'\n" +
                        "    and seq = '"+seq+"'\n")
                .toString();
    }
}
