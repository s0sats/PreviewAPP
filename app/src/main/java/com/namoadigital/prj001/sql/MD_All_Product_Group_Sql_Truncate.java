package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_All_Product_GroupDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 03/05/2018.
 */

public class MD_All_Product_Group_Sql_Truncate implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + MD_All_Product_GroupDao.TABLE);

        return sb.toString();
    }

}
