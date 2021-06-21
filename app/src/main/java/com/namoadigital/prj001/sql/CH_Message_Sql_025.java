package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 27/09/2018.
 *
 * Query que retorna qtd de msg não lida pelo usr,
 * desconsideriando msg dele mesmo(quando web e app aberto e msg são enviadas pela web).
 * LUCHE - 21/06/2021
 *  Alterado query para receber lista de customers com sessão para usar no in e pegar qty de msg de todo sos customer
 *
 */

public class CH_Message_Sql_025 implements Specification {

    public static final String BADGE_MESSAGES_QTY = "messages_qty";

    private String user_code;
    private String customerWithSessonList;

    public CH_Message_Sql_025(String user_code, String customerWithSessonList) {
        this.user_code = user_code;
        this.customerWithSessonList = customerWithSessonList;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) " + BADGE_MESSAGES_QTY + " \n" +
                        " FROM\n" +
                        "   "+ CH_MessageDao.TABLE+" m,\n" +
                        "   "+ CH_RoomDao.TABLE +" r\n" +
                        " WHERE\n" +
                        "   m.room_code = r.room_code \n" +
                        "   AND r.customer_code in("+customerWithSessonList+") \n" +
                        "   AND m.read = '0' \n" +
                        "   AND (m.user_code <> '" + user_code + "'\n" +
                        "        or (m.user_code ='" + user_code+"' AND m.msg_type not in ('"+ Constant.CHAT_MESSAGE_TYPE_TEXT+"','"+Constant.CHAT_MESSAGE_TYPE_IMAGE+"'))\n" +
                        "   )\n"
                )
               // .append(";" + BADGE_MESSAGES_QTY)
                .toString();
    }
}
