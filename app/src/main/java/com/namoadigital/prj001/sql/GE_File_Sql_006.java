package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by DANIEL.LUCHE on 20/07/2017.
 *
 * Atualiza novo path da uma imagem
 */

public class GE_File_Sql_006 implements Specification {

    private String file_code;
    private String file_path_new;

    public GE_File_Sql_006(String file_code, String file_path_new) {
        this.file_code = file_code;
        this.file_path_new = file_path_new;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" UPDATE ge_files SET \n " +
                        "   file_path_new = '"+file_path_new+"'"+
                        " WHERE \n" +
                        "   file_code = '"+file_code+"' " +
                        "   and file_status  = 'OPENED'" +
                        " ;\n")
                .toString();
    }
}
