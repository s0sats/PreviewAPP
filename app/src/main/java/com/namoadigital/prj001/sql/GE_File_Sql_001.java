package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 11/01/17.
 */

public class GE_File_Sql_001 implements Specification {

    public GE_File_Sql_001() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(GE_FileDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(GE_FileDao.FILE_STATUS)
                .append(" ='")
                .append("OPENED")
                .append("'")
                .append(" )")
                .append(" )")
                .append(" ORDER BY ")
                .append(" ")
                .append(" Date(")
                .append(GE_FileDao.FILE_DATE)
                .append(") ")
                .append(" ")
                .append(";")
                .toString();
    }
}
