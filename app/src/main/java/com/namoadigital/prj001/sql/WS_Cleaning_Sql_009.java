package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by dluche on 13/12/2019.
 *
 * Seleciona os ticket done a serem apagados
 */

public class WS_Cleaning_Sql_009 implements Specification {
    private long customer_code;
    private String s_date;

    public WS_Cleaning_Sql_009(long customer_code, String s_date) {
        this.customer_code = customer_code;
        this.s_date = s_date;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "     t.* \n" +
                        " FROM \n" +
                        "   "+ TK_TicketDao.TABLE +" t \n" +
                        " WHERE \n" +
                        "   t.customer_code = '"+customer_code+"'\n"+
                        "   AND t.ticket_status = '"+Constant.SYS_STATUS_DONE+"'\n" +
                        "   AND Date(t.close_date) < Date('"+s_date+"')\n" )
                .append(";")
                .toString();
    }
}
