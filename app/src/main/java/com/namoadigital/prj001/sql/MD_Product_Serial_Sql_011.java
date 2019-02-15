package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 21/03/2018.
 * <p>
 * <p>
 * Selecionata todos os seriais com flag sync_process = 0
 *
 * LUCHE - 15/02/2019
 *
 * Adicionado a condição serial_code != 0 no where para que os seriais criados offline e
 * ainda não transmitidos, não sejam excluidos.
 *
 * <p>
 */

public class MD_Product_Serial_Sql_011 implements Specification {

    private long customer_code;

    public MD_Product_Serial_Sql_011(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "    s.* \n" +
                        " FROM "+ MD_Product_SerialDao.TABLE+" s\n" +
                        " WHERE\n" +
                        "    s.customer_code = '" + customer_code + "'\n" +
                        "    and s.serial_code != 0\n" +
                        "    and sync_process = 0")
                .toString();
    }
}
