package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_GroupDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 24/01/2017.
 */

public class MD_Product_Group_Sql_001 implements Specification {
    private long s_customer_code;
    private long s_group_code;

    public MD_Product_Group_Sql_001(long s_customer_code, long s_group_code) {
        this.s_customer_code = s_customer_code;
        this.s_group_code = s_group_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb.append("SELECT " +
                        "   * " +
                        "FROM ")
                .append(MD_Product_GroupDao.TABLE)
                .append(" WHERE " +
                        MD_Product_GroupDao.CUSTOMER_CODE +" = '"+s_customer_code+"'"+
                        " AND " +
                        MD_Product_GroupDao.GROUP_CODE +" = '"+s_group_code+"'"+
                        "ORDER BY " +
                        MD_Product_GroupDao.GROUP_CODE)
                .append(";customer_code#group_code#recursive_code#recursive_code_father#group_id#group_desc")
                .toString();
    }
}
