package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 21/06/2017.
 */

public class SO_Pack_Express_Local_Sql_012 implements Specification {

    private long customer_code;
    private String fields = ToolBox_Inf.getColumnsToHmAux(SO_Pack_Express_LocalDao.columns);

    public SO_Pack_Express_Local_Sql_012(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "      S.*\n," +
                        "      '" + Constant.PARAM_KEY_TYPE_SO_EXPRESS + "' " + Constant.PARAM_KEY_TYPE + ",\n" +
                        "   '' " + SM_SODao.CLIENT_TYPE + ",\n" +
                        "   '' " + SM_SODao.DEADLINE + "\n" +
                        " FROM\n" +
                        SO_Pack_Express_LocalDao.TABLE + " S\n" +
                        " WHERE\n" +
                        "    S.customer_code =  '" + customer_code + "'\n" +
                        "    AND s.status in ('" + Constant.SYS_STATUS_SENT + "');")
                .append(fields + "#" + Constant.PARAM_KEY_TYPE + "#" + SM_SODao.CLIENT_TYPE + "#" + SM_SODao.DEADLINE)
                .toString();
    }
}
