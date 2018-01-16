package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 16/01/2018.
 * Seleciona as msg do remetente que tem all_delivered ou all_read 0
 *
 */

public class CH_Message_Sql_018 implements Specification {

    private long customer_code;
    private String user_code;
    private String translateMsgStr = "\"type\":\"TRANSLATE\"";

    public CH_Message_Sql_018(long customer_code, String user_code) {
        this.customer_code = customer_code;
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  m.msg_prefix,\n" +
                        "  m.msg_code,\n" +
                        "  m.msg_obj\n" +
                        " FROM\n" +
                        "   "+ CH_RoomDao.TABLE+" r\n" +
                        " LEFT JOIN  \n" +
                        "   "+ CH_MessageDao.TABLE+" m on m.room_code = r.room_code\n" +
                        " WHERE\n" +
                        "   r.customer_code = '"+customer_code+"'\n" +
                        "   and m.user_code = '"+user_code+"'\n" +
                        "   and (m.all_delivered = 0 OR m.all_read = 0)\n" +
                        " GROUP BY\n" +
                        "  m.msg_prefix,\n" +
                        "  m.msg_code\n" +
                        " HAVING \n" +
                        "   instr(m.msg_obj, '"+translateMsgStr+"') = 0\n" +
                        " ORDER BY\n" +
                        "   m.msg_pk ")
                .append(";")
                .append(CH_MessageDao.MSG_PREFIX+"#"+CH_MessageDao.MSG_CODE)
                .toString();
    }
}
