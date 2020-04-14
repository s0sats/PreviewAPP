package com.namoadigital.prj001.sql;


import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 02/04/2020
 *
 * Criado query que verifica nas msg, se existe alguma foto que foi tirada, mas que ainda
 * não foi colocada na ch_file.
 *
 * Na teoria, não há necessidade de not exists com a tab ch_file, pois quando atualizada, msg possui
 * prefixo e code.
 *
 * Query criada especificamente para a NotificationHelper
 */

public class Sql_Chat_Notification_Helper_001 implements Specification {
    private String user_code;

    public Sql_Chat_Notification_Helper_001(String user_code) {
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "    m.*\n" +
                        " FROM\n" +
                        "   "+ CH_MessageDao.TABLE +" m\n" +
                        " WHERE\n" +
                        "   m.msg_prefix = 0\n" +
                        "   and m.msg_code = 0\n" +
                        "   and m.msg_type = '"+ ConstantBaseApp.CHAT_MESSAGE_TYPE_IMAGE +"'\n" +
                        "   and m.message_image_local is not null\n" +
                        "   and m.user_code = '"+user_code+"'\n")
                .append(";")
                .toString();
    }
}
