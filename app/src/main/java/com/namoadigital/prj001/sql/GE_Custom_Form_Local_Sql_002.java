package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 7/13/16.
 */

public class GE_Custom_Form_Local_Sql_002 implements Specification {

    public GE_Custom_Form_Local_Sql_002() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append("select ifnull(max(CFL.CUSTOM_FORM_DATA), 0) + 1 id from GE_Custom_Forms_Local CFL")
                .append(";id")
                .toString();
    }
}
