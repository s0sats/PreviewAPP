package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.util.Constant;

/**
 * Query que veifica qtd de ordem express salvas no banco local
 */

public class SO_Pack_Express_Local_Sql_011 implements Specification {

    public static final String SENT_QTY = "sent_qty";
    public static final String TYPE = "type";
    private long s_customer_code;
    private HMAux hmAux_Trans;


    public SO_Pack_Express_Local_Sql_011(long s_customer_code, HMAux hmAux_Trans) {
        this.s_customer_code = s_customer_code;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     count(1) " + SENT_QTY + ",\n " +
                        "    '" + hmAux_Trans.get(Act014_Main.LABEL_TRANS_SO_EXPRESS) + "' " + TYPE + "\n " +
                        " FROM\n" +
                        SO_Pack_Express_LocalDao.TABLE + " l\n " +
                        " WHERE\n" +
                        "   l." + SO_Pack_Express_LocalDao.CUSTOMER_CODE + " = '" + s_customer_code + "' \n"+
                        "   and (l.so_status  != '"+ Constant.SYS_STATUS_PROCESS+"'); \n"
                )
                .append(";")
                .toString();
    }
}
