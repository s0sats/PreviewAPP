package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 *
 * Seleciona dados para exibição da lista de room.
 *
 * 27/11/18 - LUCHE
 * Modificado parametro no metodo de formação de data, strftime(), que indica para qual time zone
 * a data deve ser convertido.
 * Antes era usado o localtime, porem como ele apresentou problemas quando o device esta em horario de verão,
 * assim como a propria classe Calendar do Java, o parametro foi substituido pelo novo retorno do novo
 * metodo getDeviceGMT().
 */

public class Sql_Act034_004 implements Specification {

    public static final String BADGE = "BADGE";

    private Long customer_code;
    private String user_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_RoomDao.columns);
    private String room_type_filter = "";
    private String sqlite_db_format = "%Y-%m-%d %H:%M:%S";//formatação para comparação não exibição
    private String deviceGMT = ToolBox_Inf.getDeviceGMT(false);

    public Sql_Act034_004(Long customer_code, String user_code, boolean filter_workgroup, boolean filter_private, boolean filter_so, boolean filter_pa) {
        this.customer_code = customer_code;
        this.user_code = user_code;
        //
        if (filter_workgroup || filter_private || filter_so || filter_pa) {
            String inFilter = "";
            inFilter += filter_workgroup ? "'" + Constant.CHAT_ROOM_TYPE_WORKGROUP + "'," : "";
            inFilter += filter_private ? "'" + Constant.CHAT_ROOM_TYPE_PRIVATE_CUSTOMER + "'," : "";
            inFilter += filter_so ? "'" + Constant.CHAT_ROOM_TYPE_SO + "'," : "";
            inFilter += filter_pa ? "'" + Constant.CHAT_ROOM_TYPE_AP + "'," : "";
            //
            room_type_filter = "and r.room_type in ("
                    + inFilter.substring(0, inFilter.length() - 1) +
                    ")\n";

        }

    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   r.*,\n" +
                        "   m.msg_date,\n" +
                        "   m.msg_obj,\n" +
                        "   t.badge " + BADGE + "\n" +
                        " FROM\n" +
                        "   " + CH_RoomDao.TABLE + " r,\n" +
                        "   ( SELECT\n" +
                        "           r.room_code,\n" +
                        "           max(m.msg_pk)msg_pk,\n" +
                        "           max(m.tmp) tmp,\n" +
                        "           MAX(CASE WHEN m.msg_pk is null \n" +
                        "                THEN 1\n" +
                        "                ELSE 0\n" +
                        "           END) HAS_NULL,\n" +
                        "           SUM(CASE WHEN m.read = 0 AND \n" +
                        "                   (m.user_code <> '" + user_code + "' OR (m.user_code = '" + user_code + "' AND m.msg_type not in ('"+Constant.CHAT_MESSAGE_TYPE_TEXT+"','"+Constant.CHAT_MESSAGE_TYPE_IMAGE+"')))\n" +
                        "               THEN 1\n" +
                        "               ELSE 0\n" +
                        "           END) BADGE              \n" +
                        "       FROM                    \n" +
                        "            " + CH_RoomDao.TABLE + " r\n" +
                        "       LEFT JOIN \n" +
                        "            " + CH_MessageDao.TABLE + " m on m.room_code = r.room_code                       \n" +
                        "       WHERE           \n" +
                        "            (r.customer_code = '" + customer_code + "' or r.customer_code is null)  \n" +
                        "       GROUP BY\n" +
                        "            r.room_code) T\n" +
                        " LEFT JOIN\n" +
                        "      " + CH_MessageDao.TABLE + " m ON r.room_code = m.room_code\n" +
                        " WHERE\n" +
                        "    (\n" +
                        "     (t.HAS_NULL = 0 and t.msg_pk = m.msg_pk)\n" +
                        "      or\n" +
                        "     (t.HAS_NULL = 1 and t.tmp = m.tmp)\n" +
                        "      or\n" +
                        "     (t.HAS_NULL = 1 AND t.msg_pk is null and t.room_code= r.room_code) \n" +
                        "    \n" +
                        "    )\n" +
                        //"    and ('" + room_desc + "' is null or r.room_desc like '%" + room_desc + "%')\n" +
                        room_type_filter + //filtro de tipo
                        " ORDER BY  \n" +
                        "    strftime('" + sqlite_db_format + "',m.msg_date,'"+deviceGMT+"') desc,\n" +
                        "    r.room_desc\n")
                .append(";")
                //.append(HmAuxFields + "#" + CH_MessageDao.MSG_DATE + "#" + CH_MessageDao.MSG_OBJ + "#" + BADGE)
                .toString();




//        return sb
//                .append(" SELECT\n" +
//                        "   r.*,\n" +
//                        "   m.msg_date,\n" +
//                        "   m.msg_obj,\n" +
//                        "   t.badge " + BADGE + "\n" +
//                        " FROM\n" +
//                        "   " + CH_RoomDao.TABLE + " r,\n" +
//                        "   ( SELECT\n" +
//                        "           r.room_code,\n" +
//                        "           max(m.msg_pk)msg_pk,\n" +
//                        "           max(m.tmp) tmp,\n" +
//                        "           MAX(CASE WHEN m.msg_pk is null \n" +
//                        "                THEN 1\n" +
//                        "                ELSE 0\n" +
//                        "           END) HAS_NULL,\n" +
//                        "           SUM(CASE WHEN m.read = 0 AND m.user_code <> '" + user_code + "'\n" +
//                        "               THEN 1\n" +
//                        "               ELSE 0\n" +
//                        "           END) BADGE              \n" +
//                        "       FROM                    \n" +
//                        "            " + CH_RoomDao.TABLE + " r\n" +
//                        "       LEFT JOIN \n" +
//                        "            " + CH_MessageDao.TABLE + " m on m.room_code = r.room_code                       \n" +
//                        "       WHERE           \n" +
//                        "            (r.customer_code = '" + customer_code + "' or r.customer_code is null)  \n" +
//                        "       GROUP BY\n" +
//                        "            r.room_code) T\n" +
//                        " LEFT JOIN\n" +
//                        "      " + CH_MessageDao.TABLE + " m ON r.room_code = m.room_code\n" +
//                        " WHERE\n" +
//                        "    (\n" +
//                        "     (t.HAS_NULL = 0 and t.msg_pk = m.msg_pk)\n" +
//                        "      or\n" +
//                        "     (t.HAS_NULL = 1 and t.tmp = m.tmp)\n" +
//                        "      or\n" +
//                        "     (t.HAS_NULL = 1 AND t.msg_pk is null and t.room_code= r.room_code) \n" +
//                        "    \n" +
//                        "    )\n" +
//                        "    and ('" + room_desc + "' is null or r.room_desc like '%" + room_desc + "%')\n" +
//                        room_type_filter + //filtro de tipo
//                        " ORDER BY  \n" +
//                        "    strftime('" + sqlite_db_format + "',m.msg_date,'localtime') desc,\n" +
//                        "    r.room_desc\n")
//                .append(";")
//                .append(HmAuxFields + "#" + CH_MessageDao.MSG_DATE + "#" + CH_MessageDao.MSG_OBJ + "#" + BADGE)
//                .toString().replace("'%null%'", "null").replace("'null'", "null");

    }
}

