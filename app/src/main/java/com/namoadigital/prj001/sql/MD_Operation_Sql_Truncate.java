package com.namoadigital.prj001.sql;


import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 27/01/2017.
 */

public class MD_Operation_Sql_Truncate implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + MD_OperationDao.TABLE);

        return sb.toString();
    }
}
