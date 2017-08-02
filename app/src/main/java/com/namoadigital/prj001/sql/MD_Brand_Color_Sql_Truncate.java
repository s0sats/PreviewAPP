package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_Brand_ColorDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Brand_Color_Sql_Truncate implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + MD_Brand_ColorDao.TABLE);

        return sb.toString();
    }
}
