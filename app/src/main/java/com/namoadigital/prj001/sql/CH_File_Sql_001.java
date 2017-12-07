package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_FileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 11/01/17.
 */

public class CH_File_Sql_001 implements Specification {

    public CH_File_Sql_001() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" select * from ")
                .append(CH_FileDao.TABLE)
                .append(" where ")
                .append(" (")
                .append(" (")
                .append(CH_FileDao.FILE_STATUS)
                .append(" ='")
                .append("OPENED")
                .append("'")
                .append(" )")
                .append(" )")
                .append(" ORDER BY ")
                .append(" ")
                .append(" Date(")
                .append(CH_FileDao.FILE_DATE)
                .append(") ")
                .append(" ")
                .append(";")

                .toString();
    }
}
