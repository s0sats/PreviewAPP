package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.CH_MessageDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 06/12/2017.
 * <p>
 * Seleciona uma unica msg usando o tmp
 */

public class CH_Message_Sql_008 implements Specification {

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(CH_MessageDao.columns);

    public CH_Message_Sql_008() {
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT \n" +
                        "    m.* \n" +
                        " FROM \n" +
                        CH_MessageDao.TABLE + " m \n" +
                        " WHERE \n" +
                        "   m.status_update = '1' \n")
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
