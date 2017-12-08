package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 23/02/17.
 */

public class Sql_Act035_001 implements Specification {

    private String room_code;

    public Sql_Act035_001(String room_code) {
        this.room_code = room_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*\n" +
                        " FROM\n" +
                        CH_MessageDao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.room_code =  '" + room_code + "'\n" +
                        "    order by case when msg_pk is null then 'ZZZZZZZZZZZZZZZZZZZZZZZZZZZ' else msg_pk end, msg_prefix, tmp")
                .append(";msg_prefix#msg_code#tmp#room_code#msg_date#msg_obj#message_image_local#msg_origin#delivered#delivered_date#read#read_date#msg_pk#user_code#user_nick")

                .toString();
    }
}
