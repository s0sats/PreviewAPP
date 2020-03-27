package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * Created by dluche on 13/12/2019.
 *
 * Seleciona os ticket done a serem apagados
 */

public class WS_Cleaning_Sql_010 implements Specification {
    private long customer_code;
    private String s_date;

    public WS_Cleaning_Sql_010(long customer_code, String s_date) {
        this.customer_code = customer_code;
        this.s_date = s_date;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "     s.* \n" +
                        " FROM \n" +
                        "   "+ MD_Schedule_ExecDao.TABLE +" s \n" +
                        " WHERE \n" +
                        "   s.customer_code = '"+customer_code+"'\n"+
                        "   AND s.status in( '"+ ConstantBaseApp.SYS_STATUS_DONE + "'\n"+
                                             ",'" + ConstantBaseApp.SYS_STATUS_NOT_EXECUTED + "'\n"+
                                             ",'" + ConstantBaseApp.SYS_STATUS_CANCELLED + "'\n"+
                                             ",'" + ConstantBaseApp.SYS_STATUS_REJECTED +"'\n" +
                                             ",'" + ConstantBaseApp.SYS_STATUS_IGNORED +"'\n" +
                        "                   ) \n" +
                        "   AND Date(s.close_date) < Date('"+s_date+"')\n" )
                .append(";")
                .toString();
    }
}
