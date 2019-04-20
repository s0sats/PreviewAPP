package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.database.Specification;

//Definir autoincremento para o campo blindtmp
public class IO_Blind_Move_Sql_002 implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(
                        " SELECT * FROM " + IO_Blind_MoveDao.TABLE +
                                " WHERE " + IO_Blind_MoveDao.BLIND_TMP + " = " +
                                " SELECT max(" + IO_Blind_MoveDao.BLIND_TMP + ") FROM " + IO_Blind_MoveDao.TABLE +
                                "order by " + IO_Blind_MoveDao.BLIND_TMP + " DESC " +
                                " LIMIT 1"
                )
                .toString();
    }
}
