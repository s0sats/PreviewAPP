package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_Group_ProductDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 27/01/2017.
 */

public class MD_Product_Group_Product_Sql_Truncate implements Specification {
    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + MD_Product_Group_ProductDao.TABLE);

        return sb.toString();
    }
}
