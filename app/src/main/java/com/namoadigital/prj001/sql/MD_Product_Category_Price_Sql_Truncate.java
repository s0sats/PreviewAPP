package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Product_Category_PriceDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Product_Category_Price_Sql_Truncate implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + MD_Product_Category_PriceDao.TABLE);

        return sb.toString();
    }
}
