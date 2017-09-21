package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 21/06/2017.
 * <p>
 * Seta update required pra 1
 */

public class SM_SO_Sql_015 implements Specification {

    public static final String SENT_QTY = "sent_qty";
    public static final String TYPE = "type";
    private long s_customer_code;
    private HMAux hmAux_Trans;


    public SM_SO_Sql_015(long s_customer_code, HMAux hmAux_Trans) {
        this.s_customer_code = s_customer_code;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        StringBuilder sbStatus = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     count(1) " + SENT_QTY + ",\n " +
                        "    '" + hmAux_Trans.get(Act014_Main.LABEL_TRANS_OS) + "' " + TYPE + "\n " +
                        " FROM\n" +
                        SM_SODao.TABLE + " l\n " +
                        " WHERE\n" +
                        "   l." + SM_SODao.CUSTOMER_CODE + " = '" + s_customer_code + "' " +
                        "   AND ( l." + SM_SODao.STATUS + "" +
                        "    = '" + Constant.SO_STATUS_CANCELLED + "' " +
                        "   OR  l." + SM_SODao.STATUS + "" +
                        "    = '" + Constant.SO_STATUS_DONE + "' " +
                        "   OR  l." + SM_SODao.STATUS + "" )
//                        "    = '" + Constant.SO_STATUS_WAITING_QUALITY + "' ")

                .append(") ;")
                .append(SENT_QTY + "#" + TYPE)
                .toString();
    }
}
