package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_ClassDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 21/05/2018.
 */

public class MD_Class_Sql_SS implements Specification {
    private String s_customer_code;

    public MD_Class_Sql_SS(String s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(
                        " SELECT " +
                        "   class_code "+ SearchableSpinner.ID +", \n" +
                        "   class_id ,\n " +
                        "   class_type ,\n " +
                        "   class_color , \n" +
                        "   class_available ,\n " +
                        "   class_id "+ SearchableSpinner.DESCRIPTION + "\n"+
                        " FROM " +
                        MD_ClassDao.TABLE +
                        " WHERE " +
                                MD_ClassDao.CUSTOMER_CODE +" = '"+s_customer_code+"' "+
                       " ORDER BY " +
                        "      class_id")
                .append(";")
//                .append(
//                        SearchableSpinner.ID + "#" +
//                        MD_ClassDao.CLASS_ID + "#" +
//                        MD_ClassDao.CLASS_TYPE + "#" +
//                        MD_ClassDao.CLASS_COLOR + "#" +
//                        MD_ClassDao.CLASS_AVAILABLE + "#" +
//                        SearchableSpinner.DESCRIPTION
//                )
                .toString();
    }
}
