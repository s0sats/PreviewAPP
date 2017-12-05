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

    public CH_Room_Sql_001(Long customer_code, String user_code) {
        this.customer_code = customer_code;
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return  sb
                /*.append(" SELECT\n" +
                        "   r.*\n" +
                        " FROM\n" +
                            CH_RoomDao.TABLE +" r\n" +
                        " WHERE\n" +
                        "   r.customer_code = '"+customer_code+"'\n" +
                        "   or r.customer_code is null\n" +
                        " ORDER BY\n" +
                        "   r.room_desc")*/
                .append(" SELECT\n" +
                        "   r.*,\n" +
                        "   t.*,\n" +
                        "   (SELECT\n" +
                        "      COUNT(1)\n" +
                        "    FROM\n" +
                        "    "+CH_MessageDao.TABLE +" m2\n" +
                        "    WHERE\n" +
                        "      m2.room_code = r.room_code \n" +
                        "      and m2.user_code <> '"+user_code+"'\n" +
                        "      and m2.read = 0\n" +
                        "    ) "+BADGE+" \n" +
                        " FROM\n" +
                        "      " + CH_RoomDao.TABLE +" r\n" +
                        " LEFT JOIN \n" +
                        "   (SELECT\n" +
                        "      m.room_code room_code_m,\n" +
                        "      m.msg_date,\n" +
                        "      m.msg_obj\n" +
                        "    FROM\n" +
                        "      " + CH_MessageDao.TABLE +" m\n" +
                        "    WHERE\n" +
                        "      m.msg_pk = (SELECT\n" +
                        "                    max(msg_pk)\n" +
                        "                  FROM\n" +
                        "      " +    CH_MessageDao.TABLE +" m1,\n" +
                        "      " +   CH_RoomDao.TABLE +" r1\n" +
                        "                  WHERE\n" +
                        "                    m1.room_code = r1.room_code\n" +
                        "                    and (r1.customer_code = '"+customer_code+"'\n" +
                        "                        or r1.customer_code is null)\n" +
                        "                  )\n" +
                        "    ) t  on  t.room_code_m = r.room_code\n" +
                        " WHERE\n" +
                        "  (r.customer_code = '"+customer_code+"'\n" +
                        "   or r.customer_code is null)\n" +
                        " ORDER BY\n" +
                        "   r.room_desc\n")
                .append(";")
                .append(HmAuxFields+"#"+CH_MessageDao.MSG_DATE+"#"+CH_MessageDao.MSG_OBJ+"#"+BADGE)
                .toString();
    }
}
