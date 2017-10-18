package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_FileDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by dluche on 17/07/2017.
 *
 * Atualiza tabela com nome do arquivo local
 *
 */

public class SM_SO_File_Sql_004 implements Specification {
    private String customer_code;
    private String so_prefix;
    private String so_code;
    private String file_code;
    private String file_url_local;

    public SM_SO_File_Sql_004(String customer_code, String so_prefix, String so_code, String file_code, String file_url_local) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.file_code = file_code;
        this.file_url_local = file_url_local;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE " + SM_SO_FileDao.TABLE +"\n" +
                        "  SET file_url_local = '"+file_url_local+"'\n" +
                        " WHERE\n" +
                        "  customer_code = '"+customer_code+"'\n" +
                        "  AND so_prefix = '"+so_prefix+"'\n" +
                        "  AND so_code = '"+so_code+"' \n" +
                        "  AND file_code = '"+file_code+"'")
                .toString();
    }
}
