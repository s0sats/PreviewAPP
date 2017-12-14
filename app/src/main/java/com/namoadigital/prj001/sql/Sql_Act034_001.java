package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 13/12/2017.
 * Query do drawer de customers.
 * Retorna lista de customer com qtd de msg não lidas
 */

public class Sql_Act034_001 implements Specification {

    public static final String MSG_QTY = "MSG_QTY";

    private String user_code;
    private long customer_code;

    public Sql_Act034_001(String user_code, long customer_code) {
        this.user_code = user_code;
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   t.customer_code "+CH_RoomDao.CUSTOMER_CODE+",\n" +
                        "   t.customer_code "+ EV_User_CustomerDao.CUSTOMER_NAME+",\n" +
                        "   count(t.msg_code) "+MSG_QTY+"\n," +
                        "   CASE WHEN t.customer_code = '"+customer_code+"'\n" +
                        "         THEN 0\n" +
                        "         ELSE 1\n" +
                        "    END ordernation \n " +
                        " FROM\n" +
                        "    (\n" +
                        "     SELECT\n" +
                        "         r.customer_code,\n" +
                        "         m.*\n" +
                        "      FROM    \n" +
                        "          "+CH_RoomDao.TABLE+" r\n" +
                        "      LEFT JOIN\n" +
                        "          "+ CH_MessageDao.TABLE+" m ON \n" +
                        "             r.room_code = m.room_code\n" +
                        "             and m.read = 0\n" +
                        "             and m.user_code <> '"+user_code+"'\n" +
                        "    ) t\n" +
                        " GROUP BY\n" +
                        "   t.customer_code\n" +
                        " ORDER BY   \n" +
                        "   ordernation,\n" +
                        "   t.customer_code\n")
                .append(";")
                .append(CH_RoomDao.CUSTOMER_CODE+"#"+EV_User_CustomerDao.CUSTOMER_NAME+"#"+MSG_QTY)
                .toString();

    }
}
