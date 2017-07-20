package com.namoadigital.prj001.sql;

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
                /*.append("select\n" +
                        " f.file_code,\n" +
                        " ifnull(f.file_path_new,f.file_path) as file_path,\n" +
                        " f.file_status,\n" +
                        " f.file_date \n" +
                        "from \n" +
                        " ge_files f\n" +
                        "where \n" +
                        " file_status ='OPENED' \n" +
                        "ORDER BY\n" +
                        " Date(file_date)  ;")*/
                .toString();
    }
}
