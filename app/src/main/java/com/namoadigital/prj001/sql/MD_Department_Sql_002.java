package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_DepartmentDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 23/02/2018.
 */

public class MD_Department_Sql_002 implements Specification {

    private String s_customer_code;
    private String s_department_code;

    public MD_Department_Sql_002(String s_customer_code, String s_department_code) {
        this.s_customer_code = s_customer_code;
        this.s_department_code = s_department_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                                "   * " +
                                " FROM " +
                                MD_DepartmentDao.TABLE +
                                " WHERE " +
                                MD_DepartmentDao.CUSTOMER_CODE + " = '" + s_customer_code + "' " +
                                " AND " +
                                MD_DepartmentDao.DEPARTMENT_CODE + " = '" + s_department_code + "' ")
                .toString();
    }

}
