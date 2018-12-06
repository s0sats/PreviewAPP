package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_UserDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by DANIEL.LUCHE on 23/02/2018.
 */

public class MD_User_Sql_001 implements Specification {

    private String customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(MD_UserDao.columns);

    public MD_User_Sql_001(String customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT \n" +
//                                "   *,  \n" +
                                MD_UserDao.USER_CODE + " " + SearchableSpinner.ID + "," +
                                MD_UserDao.USER_NICK + " " + SearchableSpinner.DESCRIPTION +
                                " FROM \n" +
                                MD_UserDao.TABLE + "\n" +
                                " WHERE " +
                                MD_UserDao.CUSTOMER_CODE + " = '" + customer_code + "' " +
                                " AND " + MD_UserDao.AP + " = '1' " +
                                " ORDER BY " +
                                "      user_nick")
                .append(";")
                //.append(HmAuxFields + "#" + SearchableSpinner.ID + "#" + SearchableSpinner.DESCRIPTION)
                .toString();
    }
}
