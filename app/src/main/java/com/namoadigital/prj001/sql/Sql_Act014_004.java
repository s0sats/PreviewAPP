package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.database.Specification;

import static com.namoadigital.prj001.ui.act014.Act014_Main.LABEL_TRANS_IO_INBOUND;

public class Sql_Act014_004 implements Specification {

    public static final String SENT_QTY = "sent_qty";
    public static final String TYPE = "type";
    private long customer_code;
    private HMAux hmAux_Trans;


    public Sql_Act014_004(long customer_code, HMAux hmAux_Trans) {
        this.customer_code = customer_code;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "     count(1) "+ SENT_QTY+",\n " +
                        "    '"+hmAux_Trans.get(LABEL_TRANS_IO_INBOUND)+"' " + TYPE + "\n " +
                        " FROM\n" +
                        IO_InboundDao.TABLE + " t \n" +
                        " WHERE\n" +
                        "   t.customer_code = '"+ customer_code +"'\n" +
                        "   and t.status in (" +
                        "                       '"+ ConstantBase.SYS_STATUS_CANCELLED +"'," +
                        "                       '"+ ConstantBase.SYS_STATUS_DONE +
                        "' ); \n"
                )
                .toString();
    }
}
