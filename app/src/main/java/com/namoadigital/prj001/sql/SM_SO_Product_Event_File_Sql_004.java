package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Product_Event_FileDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 07/11/2017
 *
 * Retorna todos os event_files não baixados do customer
 *
 */

public class SM_SO_Product_Event_File_Sql_004 implements Specification {

    private long customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(SM_SO_Product_Event_FileDao.columns);

    public SM_SO_Product_Event_File_Sql_004(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "  f.*\n" +
                        " FROM\n" +
                        SM_SO_Product_Event_FileDao.TABLE +"  f\n" +
                        " WHERE\n" +
                        "   f.customer_code = '"+customer_code+"'\n" +
                        "  and f.file_url <> '' \n" +
                        "  and f.file_url_local = ''\n" +
                        " ORDER BY\n" +
                        "   f.file_code \n")
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
