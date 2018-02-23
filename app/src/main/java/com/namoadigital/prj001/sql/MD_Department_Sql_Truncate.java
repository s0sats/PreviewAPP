package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_DepartmentDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 23/02/2018.
 */

public class MD_Department_Sql_Truncate implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + MD_DepartmentDao.TABLE);

        return sb.toString();
    }
}
