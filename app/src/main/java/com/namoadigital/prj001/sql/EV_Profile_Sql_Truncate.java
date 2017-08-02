package com.namoadigital.prj001.sql;


import com.namoadigital.prj001.dao.EV_ProfileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 26/05/2017.
 */

public class EV_Profile_Sql_Truncate implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + EV_ProfileDao.TABLE);

        return sb.toString();
    }
}
