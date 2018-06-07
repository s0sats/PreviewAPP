package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_SiteDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 05/06/2018.
 */

public class GE_Custom_Form_Site_Sql_Trucate implements Specification {
    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + GE_Custom_Form_SiteDao.TABLE);

        return sb.toString();

    }
}
