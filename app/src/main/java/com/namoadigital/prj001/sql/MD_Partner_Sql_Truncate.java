package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 26/06/2017.
 */

public class MD_Partner_Sql_Truncate implements Specification {

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + MD_PartnerDao.TABLE);

        return sb.toString();
    }
}
