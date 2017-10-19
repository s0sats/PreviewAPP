package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 06/02/2017.
 */

public class Sync_Checklist_Sql_002 implements Specification {
    private long customer_code;
    private long product_code;

    public Sync_Checklist_Sql_002(long customer_code,long product_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();
        sb.append(
                " SELECT " +
                        "   * " +
                        " FROM "
                        + Sync_ChecklistDao.TABLE +
                        " WHERE " +
                        Sync_ChecklistDao.CUSTOMER_CODE + " = '"+customer_code+"' " +
                        "     and "+ Sync_ChecklistDao.PRODUCT_CODE  +" = '"+product_code+"' " +
                        " ORDER BY " +
                        "   product_code;" +
                        "customer_code#product_code#last_update");

        return sb.toString();
    }
}
