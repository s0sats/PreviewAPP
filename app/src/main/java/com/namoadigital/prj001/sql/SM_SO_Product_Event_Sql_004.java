package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Product_EventDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 07/11/2017
 *
 * Seleciona todos croquis não baixados da tabela de eventos
 *
 */

public class SM_SO_Product_Event_Sql_004 implements Specification {

    private long customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(SM_SO_Product_EventDao.columns);

    public SM_SO_Product_Event_Sql_004(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     e.*\n" +
                        " FROM\n" +
                        "    "+ SM_SO_Product_EventDao.TABLE+" e\n" +
                        " WHERE\n" +
                        "    e.customer_code = '"+customer_code+"'\n" +
                        "    and e.sketch_url is not null\n" +
                        "    and e.sketch_url_local is null\n")
                .append(";")
                //.append(HmAuxFields)
                .toString();
    }
}
