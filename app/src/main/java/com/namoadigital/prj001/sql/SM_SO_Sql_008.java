package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 21/06/2017.
 * <p>
 * Seleciona qtd de S.O's com update_required = 1
 */

public class SM_SO_Sql_008 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private String client_approval_date;
    private String client_approval_image_name;
    private String client_type_sig;

    public SM_SO_Sql_008(long customer_code, int so_prefix, int so_code, String client_approval_date, String client_approval_image_name, String client_type_sig) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.client_approval_date = client_approval_date;
        this.client_approval_image_name = client_approval_image_name;
        this.client_type_sig = client_type_sig;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + SM_SODao.TABLE + " set\n" +
                        "   client_approval_date = '" + client_approval_date + "',\n" +
                        "   client_approval_image_name = '" + client_approval_image_name + "',\n" +
                        "   client_type_sig = '" + client_type_sig + "',\n" +
                        "   update_required = 0\n" +
                        " WHERE\n" +
                        "  customer_code = '" + customer_code + "'\n" +
                        "  and so_prefix = '" + so_prefix + "'\n" +
                        "  and so_code = '" + so_code + "'")
                .toString();
    }
}
