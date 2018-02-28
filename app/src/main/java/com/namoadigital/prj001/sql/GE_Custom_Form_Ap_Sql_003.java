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

    private long customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(GE_Custom_Form_ApDao.columns);

    public GE_Custom_Form_Ap_Sql_003(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n " +
                        "   a.*\n" +
                        " FROM\n   " +
                        GE_Custom_Form_ApDao.TABLE + " a\n" +
                        " WHERE \n" +
                        "   a.customer_code = '"+customer_code+"'\n" +
                        " ORDER BY " +
                        "   a.customer_code,\n" +
                        "   a.custom_form_type,\n" +
                        "   a.custom_form_code,\n" +
                        "   a.custom_form_version,\n" +
                        "   a.custom_form_data,\n" +
                        "   a.ap_code,\n"+
                        "   a.ap_when \n"
                )
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
