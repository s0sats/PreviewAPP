package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 27/03/2018.
 * <p>
 * Seleciona todos os ap cancelados ou finalizado
 *
 */

public class GE_Custom_Form_Ap_Sql_012 implements Specification {

    private long customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(GE_Custom_Form_ApDao.columns);

    public GE_Custom_Form_Ap_Sql_012(long customer_code) {
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
                        "   a.customer_code = '" + customer_code + "'\n" +
                        "   and a.ap_status in('" + Constant.SYS_STATUS_CANCELLED + "','" + Constant.SYS_STATUS_DONE + "')\n" +
                        " ORDER BY \n" +
                        "   CASE WHEN a.ap_status = '" + Constant.SYS_STATUS_EDIT + "' THEN 0\n" +
                        "        WHEN a.ap_status = '" + Constant.SYS_STATUS_PROCESS + "' THEN 1\n" +
                        "        WHEN a.ap_status = '" + Constant.SYS_STATUS_WAITING_ACTION + "' THEN 2\n" +
                        "        WHEN a.ap_status = '" + Constant.SYS_STATUS_DONE + "' THEN 3\n" +
                        "        ELSE 4\n" +
                        "   END\n"
                )
                .append(";")
                //.append(HmAuxFields)
                .toString();
    }
}
