package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 11/05/2017.
 */

public class GE_Custom_Form_Operation_Sql_Trucate implements Specification {
    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM " + GE_Custom_Form_OperationDao.TABLE);

        return sb.toString();

    }
}
