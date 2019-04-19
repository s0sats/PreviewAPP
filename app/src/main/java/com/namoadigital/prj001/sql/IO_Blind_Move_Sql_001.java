package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
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
                "       AND b."+ IO_Blind_MoveDao.TOKEN+" != ''" +
                "       AND (b.status = '"+ Constant.SYS_STATUS_WAITING_SYNC +"'\n" +
                "            OR b."+ IO_Blind_MoveDao.FLAG_BLIND+" = '0' AND b."+IO_Blind_MoveDao.STATUS + " = '"+Constant.SYS_STATUS_ERROR+"'\n" +
                "           )\n";
        }else{
            //SE NÃO ESTA BUSCANDO POR blind COM TOKEN,
            //busca qql blind sem token e wating sync ou movimentação, flag_blind = 0 e com status de erro.
            tokenFilter =
                    "       AND b."+IO_Blind_MoveDao.TOKEN+" is null" +
                    "       AND (b.status = '"+ Constant.SYS_STATUS_WAITING_SYNC +"'\n" +
                                 " OR b."+ IO_Blind_MoveDao.FLAG_BLIND+" = '0' AND b."+IO_Blind_MoveDao.STATUS + " = '"+Constant.SYS_STATUS_ERROR+"'\n" +
                                ")\n";
        }

        return sb
            .append(" SELECT\n" +
                    "   b.* " + "\n"+
                    " FROM\n" +
                    IO_Blind_MoveDao.TABLE + " b \n" +
                    " WHERE\n" +
                    "   b.customer_code = '" + customer_code+"'\n" +
                    tokenFilter

            )
            .toString();
    }
}
