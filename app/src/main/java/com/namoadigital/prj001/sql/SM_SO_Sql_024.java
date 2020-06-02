package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 02/06/2020
 * Criado query que atualiza room_code e scn da O.S
 * Usada ao criar room para a O.S
 */

public class SM_SO_Sql_024 implements Specification {
    private long customer_code;
    private int so_prefix;
    private int so_code;
    private Integer so_scn;
    private String room_code;

    public SM_SO_Sql_024(long customer_code, int so_prefix, int so_code, Integer so_scn, String room_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.so_scn = so_scn;
        this.room_code = room_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return
            sb
                .append(" UPDATE\n" +
                        SM_SODao.TABLE + "\n" +
                        " SET so_scn = '" + so_scn + "' ," +
                        "     room_code = '" + room_code + "' " +
                        " WHERE\n" +
                        "    customer_code = '" + customer_code + "'\n" +
                        "    AND so_prefix = '" + so_prefix + "'\n" +
                        "    AND so_code = '" + so_code + "'\n"
                )
               .toString()
            ;
    }
}
