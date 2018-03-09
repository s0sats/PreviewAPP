package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act014.Act014_Main;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 *
 * Seleciona qtd de AP cancelados e done.
 */

public class Sql_Act014_003 implements Specification {

    public static final String SENT_QTY = "sent_qty";
    public static final String TYPE = "type";
    private long s_customer_code;
    private HMAux hmAux_Trans;

    public Sql_Act014_003(long s_customer_code, HMAux hmAux_Trans) {
        this.s_customer_code = s_customer_code;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "     count(1) "+ SENT_QTY+",\n " +
                        "    '"+hmAux_Trans.get(Act014_Main.LABEL_TRANS_FORM_AP)+"' " + TYPE + "\n " +
                        " FROM\n" +
                        GE_Custom_Form_ApDao.TABLE+" a\n " +
                        " WHERE\n" +
                        "    a."+GE_Custom_Form_ApDao.CUSTOMER_CODE+" = '"+s_customer_code+"' \n" +
                        "   AND a."+GE_Custom_Form_ApDao.AP_STATUS+"" +
                        "    in ('"+ Constant.SYS_STATUS_DONE +"','"+ Constant.SYS_STATUS_CANCELLED +"')\n" +
                        ";")
                .append(SENT_QTY+"#"+TYPE)
                .toString();
    }

}
