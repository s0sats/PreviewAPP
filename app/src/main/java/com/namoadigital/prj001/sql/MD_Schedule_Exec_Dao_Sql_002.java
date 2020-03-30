package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.database.Specification;

/**
 * LUCHE - 11/03/2020
 *
 * Seleciona os dados de master data de site, operação e produto para inserir na tabela de md_schedule
 *
 */

public class MD_Schedule_Exec_Dao_Sql_002 implements Specification {

    private long customer_code;
    private int site_code;
    private int operation_code;
    private int product_code;

    public MD_Schedule_Exec_Dao_Sql_002(long customer_code, int site_code, int operation_code, int product_code) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.operation_code = operation_code;
        this.product_code = product_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        //
        return sb
                .append(" SELECT\n" +
                        "    s.site_id,\n" +
                        "    s.site_desc,\n" +
                        "    o.operation_id,\n" +
                        "    o.operation_desc,\n" +
                        "    p.product_id,\n" +
                        "    p.product_desc\n" +
                        " FROM\n" +
                        "      ( SELECT\n" +
                        "            '"+customer_code+"' customer_code,\n" +
                        "            '"+site_code+"' site_code,\n" +
                        "            '"+operation_code+"' operation_code,\n" +
                        "            '"+product_code+"' product_code\n" +
                        "       ) sc,\n" +
                        "    "+ MD_SiteDao.TABLE +" s,\n" +
                        "    "+ MD_OperationDao.TABLE +" o,\n" +
                        "    "+ MD_ProductDao.TABLE +" p\n" +
                        " WHERE\n" +
                        "    sc.customer_code = s.customer_code\n" +
                        "    and sc.site_code = s.site_code\n" +
                        "    --\n" +
                        "    and sc.customer_code = o.customer_code\n" +
                        "    and sc.operation_code = o.operation_code\n" +
                        "    --\n" +
                        "    and sc.customer_code = p.customer_code\n" +
                        "    and sc.product_code = p.product_code\n")
                .toString();
    }
}
