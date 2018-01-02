package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 02/01/2018.
 * Query fragmento de room.
 * Retorna qtd de msg não lidas de outros customers q
 */

public class Sql_Act034_003 implements Specification {

    public static final String OTHER_CUSTOMER_QTY_MSG = "OTHER_CUSTOMER_QTY_MSG";

    private long customer_code;
    private String user_code;

    public Sql_Act034_003(long customer_code, String user_code) {
        this.customer_code = customer_code;
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   COUNT(1) "+OTHER_CUSTOMER_QTY_MSG+"\n" +
                        " FROM\n" +
                        "   "+ CH_RoomDao.TABLE+" r\n" +
                        " LEFT JOIN  \n" +
                        "   "+ CH_MessageDao.TABLE+" m ON m.room_code = r.room_code\n" +
                        " WHERE\n" +
                        "   r.customer_code <> '"+customer_code+"'\n" +
                        "   and m.user_code <> '"+user_code+"'\n" +
                        "   and m.read = 0 \n")
                .append(";")
                .append(OTHER_CUSTOMER_QTY_MSG)
                .toString();

    }
}
