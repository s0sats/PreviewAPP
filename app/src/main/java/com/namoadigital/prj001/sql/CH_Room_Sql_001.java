package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 */

public class CH_Room_Sql_001 implements Specification {

    public static final String BADGE = "BADGE";

    private Long customer_code;
    private String user_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_RoomDao.columns);
    private String sqlite_db_format = "%Y-%m-%d %H:%M:%S";//formatação para comparação não exibição

    public CH_Room_Sql_001(Long customer_code, String user_code) {
        this.customer_code = customer_code;
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                .append(" SELECT\n" +
                        "   r.*,\n" +
                        "   m.msg_date,\n" +
                        "   m.msg_obj,\n" +
                        "   t.badge "+BADGE+"\n" +
                        " FROM\n" +
                        "   "+ CH_RoomDao.TABLE+" r,\n" +
                        "   ( SELECT\n" +
                        "           r.room_code,\n" +
                        "           max(m.msg_pk)msg_pk,\n" +
                        "           max(m.tmp) tmp,\n" +
                        "           MAX(CASE WHEN m.msg_pk is null \n" +
                        "                THEN 1\n" +
                        "                ELSE 0\n" +
                        "           END) HAS_NULL,\n" +
                        "           SUM(CASE WHEN m.read = 0 AND m.user_code <> '"+user_code+"'\n" +
                        "               THEN 1\n" +
                        "               ELSE 0\n" +
                        "           END) BADGE              \n" +
                        "       FROM                    \n" +
                        "            "+ CH_RoomDao.TABLE+" r\n" +
                        "       LEFT JOIN \n" +
                        "            "+ CH_MessageDao.TABLE+" m on m.room_code = r.room_code                       \n" +
                        "       WHERE           \n" +
                        "            (r.customer_code = '"+customer_code+"' or r.customer_code is null)  \n" +
                        "       GROUP BY\n" +
                        "            r.room_code) T\n" +
                        " LEFT JOIN\n" +
                        "      "+ CH_MessageDao.TABLE+" m ON r.room_code = m.room_code    " +
                        " WHERE\n" +
                        "    (\n" +
                        "     (t.HAS_NULL = 0 and t.msg_pk = m.msg_pk)\n" +
                        "      or\n" +
                        "     (t.HAS_NULL = 1 and t.tmp = m.tmp)\n" +
                        "      or\n" +
                        "     (t.HAS_NULL = 1 AND t.msg_pk is null and t.room_code= r.room_code) \n" +
                        "    \n" +
                        "    )" +
                        " ORDER BY  \n" +
                        "    strftime('"+sqlite_db_format+"',m.msg_date,'localtime') desc,\n" +
                        "    r.room_desc")
                .append(";")
                .append(HmAuxFields+"#"+CH_MessageDao.MSG_DATE+"#"+CH_MessageDao.MSG_OBJ+"#"+BADGE)
                .toString();

    }
}
