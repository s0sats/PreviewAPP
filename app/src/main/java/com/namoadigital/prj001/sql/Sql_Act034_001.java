package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 13/12/2017.
 * Query do drawer de customers.
 * Retorna lista com qtd de msg não lidas para os customer com sessão
 * LUCHE - 21/06/2021
 * Corrigido verificação de msg não lida, substituindo o usr_code <> do logado pela logica
 * Diferente do usr logado OU do usr logado, desde que NÃO seja imagem ou text
 */

public class Sql_Act034_001 implements Specification {

    public static final String MSG_QTY = "MSG_QTY";

    private String user_code;
    private long customer_code;
    private String customer_list_filter;

    public Sql_Act034_001(String user_code, long customer_code, String customer_list_filter) {
        this.user_code = user_code;
        this.customer_code = customer_code;
        this.customer_list_filter = customer_list_filter;
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
                        "             AND (m.user_code <> '" + user_code + "'\n" +
                        "                  or (m.user_code ='" + user_code+"' AND m.msg_type not in ('"+ Constant.CHAT_MESSAGE_TYPE_TEXT+"','"+Constant.CHAT_MESSAGE_TYPE_IMAGE+"'))\n" +
                        "             )\n" +
                        "      WHERE \n" +
                        "        r.customer_code in ("+customer_list_filter+")\n" +
                        "    ) t\n" +
                        " GROUP BY\n" +
                        "   t.customer_code\n" +
                        " ORDER BY   \n" +
                        "   ordernation,\n" +
                        "   t.customer_code\n")
                .append(";")
                //.append(CH_RoomDao.CUSTOMER_CODE+"#"+EV_User_CustomerDao.CUSTOMER_NAME+"#"+MSG_QTY)
                .toString();

    }
}
