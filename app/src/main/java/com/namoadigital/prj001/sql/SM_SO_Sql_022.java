package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 26/10/2017.
 *
 * Seta valor do campo client_approval_image_url_local com o nome da imagem
 *
 */

public class SM_SO_Sql_022 implements Specification {

    private String customer_code;
    private String so_prefix;
    private String so_code;
    private String client_approval_image_name;

    public SM_SO_Sql_022(String customer_code, String so_prefix, String so_code, String client_approval_image_name) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.client_approval_image_name = client_approval_image_name;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE "+ SM_SODao.TABLE+" set\n" +
                        "   client_approval_image_url_local = '"+client_approval_image_name+"'\n"+
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  and so_prefix = '"+so_prefix+"'\n" +
                        "  and so_code = '"+so_code+"'")
                .toString();
    }
}
