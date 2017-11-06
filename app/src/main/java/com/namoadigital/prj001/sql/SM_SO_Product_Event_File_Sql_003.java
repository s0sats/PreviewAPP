package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Product_Event_FileDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 31/10/2017.
 *
 * Retorna um file especific de um evento - Retorna HmAux
 *
 */

public class SM_SO_Product_Event_File_Sql_003 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int seq_tmp;
    private int file_tmp;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(SM_SO_Product_Event_FileDao.columns);


    public SM_SO_Product_Event_File_Sql_003(long customer_code, int so_prefix, int so_code, int seq_tmp, int file_tmp) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.seq_tmp = seq_tmp;
        this.file_tmp = file_tmp;
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
                        "   and f.so_prefix = '"+so_prefix+"'\n" +
                        "   and f.so_code = '"+so_code+"'\n" +
                        "   and f.seq_tmp = '"+seq_tmp+"'\n" +
                        "   and f.file_tmp = '"+file_tmp+"'\n" +
                        " ORDER BY\n" +
                        "   f.file_code \n")
                .append(";")
                .append(HmAuxFields)
                .toString();
    }
}
