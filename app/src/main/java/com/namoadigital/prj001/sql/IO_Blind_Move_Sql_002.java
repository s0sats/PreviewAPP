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
                        " SELECT\n" +
                                "  ifnull(max(b.blind_tmp),0) + 1 next_tmp\n" +
                                " FROM\n" +
                                "  io_blind_move b \n" +
                                " WHERE\n" +
                                "   b.customer_code "
                )
                .toString();
    }
}
