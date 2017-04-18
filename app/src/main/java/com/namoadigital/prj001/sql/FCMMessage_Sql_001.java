package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.FCMMessageDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 06/04/2017.
 */

/**
 * Apaga resgistros de uma lista de data_serv enviada
 */
public class FCMMessage_Sql_001 implements Specification {

    public FCMMessage_Sql_001() {
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select *\n" +
                        " FROM\n" +
                        FCMMessageDao.TABLE + " \n" +
                        " ORDER BY date_create_ms desc")
                .append(";")
                .append("fcmmessage_code#customer#type#title#msg_short#module#sender#status#date_create_ms")
                .toString();
    }
}
