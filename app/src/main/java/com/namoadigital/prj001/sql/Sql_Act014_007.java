package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act014.Act014_Main;

public class Sql_Act014_007 implements Specification {
    public static final String SENT_QTY = "sent_qty";
    public static final String TYPE = "type";
    private long customer_code;
    private HMAux label_translation;

    public Sql_Act014_007(long customer_code, HMAux label_translation) {
        this.customer_code = customer_code;
        this.label_translation = label_translation;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append( "SELECT\n" +
                        "     count(1) "+SENT_QTY+",\n " +
                        "    '"+label_translation.get(Act014_Main.LABEL_TRANS_TK_TICKET)+"' "+TYPE+" \n " +
                        " FROM\n" +
                        TK_TicketDao.TABLE +" s\n" +
                        " WHERE \n" +
                        "   s.customer_code = '"+customer_code+"'\n" +
                        "   and s.ticket_status = '" + ConstantBase.SYS_STATUS_DONE +"';")
                .toString();
    }
}
