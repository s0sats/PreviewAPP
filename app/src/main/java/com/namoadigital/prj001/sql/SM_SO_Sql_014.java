package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 21/06/2017.
 * <p>
 * Seta update required pra 1
 */

public class SM_SO_Sql_014 implements Specification {

    public SM_SO_Sql_014() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        StringBuilder sbStatus = new StringBuilder();

        return sb
                .append(" UPDATE " + SM_SODao.TABLE + "\n" +
                        "  set approval_required = '0',\n" +
                        "  status ='" + Constant.SO_STATUS_WAITING_CLIENT + "',\n" +
                        "  client_approval_user = NULL,\n" +

                        "  client_approval_date = NULL,\n" +
                        "  client_name = NULL,\n" +
                        "  client_approval_image_name = NULL,\n" +
                        "  client_approval_type_sig = NULL\n" +

                        " WHERE\n" +
                        "   approval_required = '2'")
                .toString().replace("\'null\'", "NULL");
    }
}
