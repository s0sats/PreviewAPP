package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * LUCHE - 18/04/2019
 *
 * Query que seleciona os items pendentes de envio, com ou sem token dependendo do parametro pending
 *
 */

public class IO_Blind_Move_Sql_001 implements Specification {
    private long customer_code;
    private int s_pending;

    public IO_Blind_Move_Sql_001(long customer_code, int s_pending) {
        this.customer_code = customer_code;
        this.s_pending = s_pending;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        String tokenFilter = "";
        //Baseado no parameto, define se busca novos token
        // ou token pendente
        if(s_pending == 1){
            tokenFilter =
                "       AND b."+ IO_MoveDao.TOKEN+" != ''";
        }else{
            tokenFilter =
                "       AND b."+IO_MoveDao.TOKEN+" = ''";
        }

        return sb
            .append(" SELECT\n" +
                    "   b.* " + "\n"+
                    " FROM\n" +
                    IO_Blind_MoveDao.TABLE + " b \n" +
                    " WHERE\n" +
                    "   b.customer_code = '" + customer_code+"'\n" +
                    tokenFilter +
                    "   and b.status = '"+ Constant.SYS_STATUS_WAITING_SYNC +"'\n"
            )
            .toString();
    }
}
