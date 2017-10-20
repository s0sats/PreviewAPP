package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 03/08/2017.
 * <p>
 * Atualiza origin_change para APP
 */

public class SM_SO_Sql_018 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int so_scn;


    public SM_SO_Sql_018(long customer_code, int so_prefix, int so_code, int so_scn) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.so_scn = so_scn;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        String sResp = "";

        if (so_scn != 0) {
            sResp = "  and so_scn < '" + so_scn + "'";
        }

        return sb
                .append(" UPDATE " + SM_SODao.TABLE + " set\n" +
                        "   sync_required = '" + "1" + "'\n" +
                        " WHERE\n" +
                        "  customer_code = '" + customer_code + "'\n" +
                        "  and so_prefix = '" + so_prefix + "'\n" +
                        "  and so_code = '" + so_code + "'\n")
                .append(sResp)
                .toString();
    }
}
