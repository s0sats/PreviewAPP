package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 06/01/2019.
 *
 * Query retorna dados de um customer especifico, sem levar em consideração o user.
 * Query usada no processo verificação de dados pendentes de envio.
 *
 */

public class EV_User_Customer_Sql_011 implements Specification {

    private long customer_code;

    public EV_User_Customer_Sql_011(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "   e.* \n" +
                        " FROM\n" +
                        EV_User_CustomerDao.TABLE +" e\n" +
                        " WHERE\n" +
                        "   e.customer_code = '"+customer_code+"'\n")
                .append(";")
                //.append(HmAuxFields)
                .toString();
    }
}
