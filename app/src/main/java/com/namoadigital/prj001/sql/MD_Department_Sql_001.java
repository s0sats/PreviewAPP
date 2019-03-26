package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_DepartmentDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 23/02/2018.
 */

public class MD_Department_Sql_001 implements Specification {

    private String s_customer_code;

    public MD_Department_Sql_001(String s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                                "   department_code " + SearchableSpinner.CODE + ", " +
                                "   department_id " + SearchableSpinner.ID + ", " +
                                "   department_desc " + SearchableSpinner.DESCRIPTION +
                                " FROM " +
                                MD_DepartmentDao.TABLE +
                                " WHERE " +
                                MD_DepartmentDao.CUSTOMER_CODE + " = '" + s_customer_code + "' " +
                                " ORDER BY " +
                                "      department_id,department_desc")
                .append(";")
                //.append(SearchableSpinner.ID + "#department_id#" + SearchableSpinner.DESCRIPTION)
                .toString();
    }
}
