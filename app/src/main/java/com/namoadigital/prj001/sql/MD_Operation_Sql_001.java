package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 17/01/2017.
 */

public class MD_Operation_Sql_001 implements Specification {

    private long s_customer_code;

    public MD_Operation_Sql_001(long s_customer_code) {
        this.s_customer_code = s_customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT " +
                        "   operation_code, " +
                        "   operation_id, " +
                        "   operation_desc, " +
                        "   alias_service_oper, " +
                        "   alias_service_com "+
                        "FROM ")
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
