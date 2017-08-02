package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Site_Zone_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Site_Zone_Local_Sql_Truncate implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + MD_Site_Zone_LocalDao.TABLE);

        return sb.toString();
    }
}
