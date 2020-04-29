package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.database.Specification;

public class MD_Operation_Sql_SS implements Specification {

    private long s_customer_code;

    public MD_Operation_Sql_SS(long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT " +
                        "   operation_code " + SearchableSpinner.CODE +", " +
                        "   operation_id " + SearchableSpinner.ID +", " +
                        "   operation_desc " + SearchableSpinner.DESCRIPTION +
                        "   FROM ")
                .append(MD_OperationDao.TABLE)
                .append(" WHERE " +
                        MD_OperationDao.CUSTOMER_CODE +" = '"+s_customer_code+"' " +
                        " ORDER BY " +
                        "      operation_id")
                .append(";")
                //.append("operation_code#operation_id#operation_desc#alias_service_oper#alias_service_com")
                .toString();
    }
}
