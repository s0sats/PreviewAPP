package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

public class IO_Move_Order_Item_Sql_004 implements Specification {
    private long customer_code;
    private int s_pending;

    public IO_Move_Order_Item_Sql_004(long customer_code, int s_pending) {
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
                    "       AND "+ IO_MoveDao.TOKEN+" != ''";
        }else{
            tokenFilter =
                    "       AND "+IO_MoveDao.TOKEN+" = ''";
        }
        return sb
                .append(" SELECT\n" +
                        "   * " + "\n"+
                        " FROM\n" +
                        IO_MoveDao.TABLE + " \n" +
                        " WHERE\n" +
                        "   customer_code = '" + customer_code+"'\n" +
                        tokenFilter +
                        "   and status = '"+ Constant.SYS_STATUS_WAITING_SYNC +'\'' )
                .toString();
    }
}
