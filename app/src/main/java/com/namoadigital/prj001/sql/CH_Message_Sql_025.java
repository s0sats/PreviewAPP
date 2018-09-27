package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 27/09/2018.
 *
 * Query que retorna qtd de msg não lida pelo usr,
 * desconsideriando msg dele mesmo(quando web e app aberto e msg são enviadas pela web).
 */

public class CH_Message_Sql_025 implements Specification {

    public static final String BADGE_MESSAGES_QTY = "messages_qty";

    private String user_code;

    public CH_Message_Sql_025(String user_code) {
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   count(1) " + BADGE_MESSAGES_QTY + " \n" +
                        " FROM\n" +
                        "   ch_messages m\n" +
                        " WHERE\n" +
                        "   m.read = '0' \n" +
                        "   AND (m.user_code <> '" + user_code + "'\n" +
                        "        or (m.user_code ='" + user_code+"' AND m.msg_type not in ('"+ Constant.CHAT_MESSAGE_TYPE_TEXT+"','"+Constant.CHAT_MESSAGE_TYPE_IMAGE+"'))\n" +
                        "   )\n"
                )
                .append(";" + BADGE_MESSAGES_QTY)
                .toString();
    }
}
