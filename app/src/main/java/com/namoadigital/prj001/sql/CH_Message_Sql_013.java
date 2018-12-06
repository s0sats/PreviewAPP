package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 18/12/2017.
 *
 * Seleciona maior msg code recebido
 *
 */

public class CH_Message_Sql_013 implements Specification {

    private String customer_filter = "";

    public CH_Message_Sql_013(String customer_filter) {
        this.customer_filter = "   and r.customer_code in ("+ customer_filter+") \n";
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        /*return sb
                .append(" SELECT\n" +
                        "   m.msg_prefix,\n" +
                        "   m.msg_code\n" +
                        " FROM\n" +
                        "   "+ CH_MessageDao.TABLE+" m\n" +
                        " WHERE\n" +
                        "   m.msg_pk = (  \n" +
                        "              SELECT\n" +
                        "                    MAX(t.msg_pk) msg_pk      \n" +
                        "               FROM\n" +
                        "                   "+ CH_MessageDao.TABLE+" t)\n")
                .append(";")
                .append(CH_MessageDao.MSG_PREFIX+"#"+CH_MessageDao.MSG_CODE)
                .toString();*/

        return  sb.append(  " SELECT\n" +
                            "   m.msg_prefix,\n" +
                            "   m.msg_code\n" +
                            " FROM\n" +
                            "   "+ CH_MessageDao.TABLE+" m\n" +
                            " WHERE\n" +
                            "   m.msg_pk = (  \n" +
                            "               SELECT\n" +
                            "                  MIN(tt.msg_pk)\n" +
                            "               FROM(\n" +
                            "                   SELECT\n" +
                            "                       IFNULL(MAX(t.msg_pk),0) msg_pk \n" +
                            "                   FROM\n" +
                            "                       "+ CH_RoomDao.TABLE+" r\n" +
                            "                   LEFT JOIN\n" +
                            "                       "+ CH_MessageDao.TABLE+" t on t.room_code = r.room_code \n" +
                            "                   WHERE \n " +
                            "                       1 = 1 \n" +
                                                customer_filter +
                            "                   GROUP BY\n" +
                            "                       r.customer_code\n" +
                            "                   ) tt       \n" +
                            "              )\n")
                .append(";")
                //.append(CH_MessageDao.MSG_PREFIX+"#"+CH_MessageDao.MSG_CODE)
                .toString();
    }
}
