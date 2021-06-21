package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.dao.CH_RoomDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 02/01/2018.
 * Query fragmento de room.
 * Retorna qtd de msg não lidas de outros customers q
 * LUCHE - 21/06/2021
 * Corrigido verificação de msg não lida, substituindo o usr_code <> do logado pela logica
 * Diferente do usr logado OU do usr logado, desde que NÃO seja imagem ou text
 */

public class Sql_Act034_003 implements Specification {

    public static final String OTHER_CUSTOMER_QTY_MSG = "OTHER_CUSTOMER_QTY_MSG";

    private long customer_code;
    private String user_code;
    private String customer_list_filter = "";

    public Sql_Act034_003(long customer_code, String user_code, String customer_list_filter) {
        this.customer_code = customer_code;
        this.user_code = user_code;
        this.customer_list_filter = customer_list_filter.trim().length() > 0 ? "   and r.customer_code in ("+ customer_list_filter+") \n" : "";
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
                        customer_list_filter +
                        "   AND m.read = 0 \n" +
                        "   AND (m.user_code <> '" + user_code + "'\n" +
                        "        or (m.user_code ='" + user_code+"' AND m.msg_type not in ('"+ Constant.CHAT_MESSAGE_TYPE_TEXT+"','"+Constant.CHAT_MESSAGE_TYPE_IMAGE+"'))\n" +
                        "   )\n"
                )
                .append(";")
                //.append(OTHER_CUSTOMER_QTY_MSG)
                .toString();

    }
}
