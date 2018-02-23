package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 30/11/2017.
 * <p>
 * Selecion todos ap.
 */

public class GE_Custom_Form_Ap_Sql_003 implements Specification {

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(GE_Custom_Form_ApDao.columns);

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n " +
                        "   m.*\n  "  +
                        " FROM\n   " +
                        GE_Custom_Form_ApDao.TABLE + " m\n")
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
