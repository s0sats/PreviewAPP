package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_UserDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 27/01/2017.
 */

public class EV_User_Sql_Truncate implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + EV_UserDao.TABLE);

        return sb.toString();
    }
}
