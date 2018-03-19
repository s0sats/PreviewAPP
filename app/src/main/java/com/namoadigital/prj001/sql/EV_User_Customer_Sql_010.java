package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.EV_User_CustomerDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 22/02/2018.
 *
 * Query que seleciona todos os customer com sessão ativa
 *
 */

public class EV_User_Customer_Sql_010 implements Specification {

    private String user_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(EV_User_CustomerDao.columns);

    public EV_User_Customer_Sql_010(String user_code) {
        this.user_code = user_code;
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
                        "   e.user_code = '"+user_code+"'\n" +
                        "   and e.session_app <> ''\n")
                .append(";" + HmAuxFields)
                .toString();
    }
}
