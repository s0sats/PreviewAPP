package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.database.Specification;


/**
 * LUCHE 18/04/2019
 *
 * Query que atualiza status da blind move passada.
 *
 */

public class IO_Blind_Move_Sql_003 implements Specification {
    private long customer_code;
    private int blind_tmp;
    private String status;
    private String error_msg;

    public IO_Blind_Move_Sql_003(long customer_code, int blind_tmp, String status, String error_msg) {
        this.customer_code = customer_code;
        this.blind_tmp = blind_tmp;
        this.status = status;
        this.error_msg = error_msg;
        //
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        String erroMsgSet =  "";
            if(error_msg != null && error_msg.isEmpty()) {
                erroMsgSet = ",\n  error_msg = '" + error_msg + "'\n";
            }

        return sb
            .append(" UPDATE "+IO_Blind_MoveDao.TABLE+" SET\n" +
                    "   status = '"+ status +"' " +
                        erroMsgSet +
                    "\n WHERE\n" +
                    "   customer_code = '" + customer_code+"'\n" +
                    "   and blind_tmp = '"+blind_tmp+"'\n"
            )
            .toString();
    }
}
