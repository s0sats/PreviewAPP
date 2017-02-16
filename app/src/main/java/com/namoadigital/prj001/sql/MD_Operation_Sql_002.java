package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 16/02/2017.
 */

public class MD_Operation_Sql_002 implements Specification {

    private long s_customer_code;
    private long s_operation_code;

    public MD_Operation_Sql_002(long s_customer_code, long s_operation_code) {
        this.s_customer_code = s_customer_code;
        this.s_operation_code = s_operation_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      o.customer_code,\n" +
                        "      o.operation_code,\n" +
                        "      o.operation_id,\n" +
                        "      o.operation_id||' - '||o.operation_desc operation_desc,\n" +
                        "      o.alias_service_oper,\n" +
                        "      o.alias_service_com\n" +
                        " FROM " +
                        MD_OperationDao.TABLE + " o"+
                        " WHERE " +
                        MD_OperationDao.CUSTOMER_CODE +" = '"+s_customer_code+"' " +
                        " AND " + MD_OperationDao.OPERATION_CODE +" = '"+s_operation_code+"' ;")
                .toString();
    }
}
