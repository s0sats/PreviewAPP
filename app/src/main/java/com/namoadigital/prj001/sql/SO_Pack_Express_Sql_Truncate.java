package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 27/01/2017.
 */

public class SO_Pack_Express_Sql_Truncate implements Specification {
    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + SO_Pack_ExpressDao.TABLE);

        return sb.toString();
    }
}
