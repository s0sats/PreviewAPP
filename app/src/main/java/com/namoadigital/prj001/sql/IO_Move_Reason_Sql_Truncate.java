package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Move_ReasonDao;
import com.namoadigital.prj001.database.Specification;

public class IO_Move_Reason_Sql_Truncate implements Specification {
    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + IO_Move_ReasonDao.TABLE);

        return sb.toString();
    }
}
