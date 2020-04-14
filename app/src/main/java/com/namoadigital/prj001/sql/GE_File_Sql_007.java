package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.database.Specification;

public class GE_File_Sql_007 implements Specification {

    private String file_code;

    public GE_File_Sql_007(String file_code) {
        this.file_code = file_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
            .append(" SELECT \n" +
                    "   g.* \n" +
                    " FROM  \n" +
                    "   " + GE_FileDao.TABLE +" g\n"+
                    " WHERE \n" +
                    "   g.file_code = '"+file_code+"'\n"
            )
            .toString();
    }
}
