package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_InboundDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * LUCHE - 09/04/2019
 *
 * Verifica nas tabelas de io_inbound_item e io_move e gera cabeaçlho de envio da inbound
 * Retorna Array de HmAux
 *
 *
 */
public class IO_Inbound_Sql_009 implements Specification {
    private long customer_code;

    public IO_Inbound_Sql_009(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" SELECT\n" +
                        "  t.customer_code, \n" +
                        "  t.inbound_prefix, \n" +
                        "  t.inbound_code,\n" +
                        "  --t.inbound_item,\n" +
                        "  max(t.scn) " + IO_InboundDao.SCN + "\n" +
                        " FROM ( " +
                        "        SELECT\n" +
                        "             it.customer_code, \n" +
                        "             it.inbound_prefix, \n" +
                        "             it.inbound_code, \n" +
                        "             i.scn scn \n" +
                        "         FROM\n" +
                                 IO_InboundDao.TABLE + "  i,\n" +
                                 IO_Inbound_ItemDao.TABLE +" it \n" +
                        "         WHERE\n" +
                        "             i.customer_code = it.customer_code \n" +
                        "             and i.inbound_prefix =  it.inbound_prefix\n" +
                        "             and i.inbound_code =  it.inbound_code\n" +
                        "             \n" +
                        "             and it.customer_code = '"+customer_code+"'\n" +
                        "             and it.update_required = 1 \n" +
                        "        \n" +
                        "         UNION\n" +
                        "                     \n" +
                        "         SELECT\n" +
                        "             m.customer_code, \n" +
                        "             m.inbound_prefix, \n" +
                        "             m.inbound_code,\n" +
                        "             0 scn\n" +
                        "         FROM\n" +
                                   IO_MoveDao.TABLE +" m  \n" +
                        "         WHERE\n" +
                        "             m.customer_code = '"+customer_code+"'  \n" +
                        "             --define put_away\n" +
                        "             and m.move_type = '"+ ConstantBaseApp.IO_INBOUND +"' \n" +
                        "             and m.inbound_prefix is not null \n" +
                        "             and m.inbound_code is not null \n" +
                        "             and m.inbound_item is not null  \n" +
                        "             and m.update_required = 1 \n" +
                        "       ) t\n" +
                        "    \n" +
                        " GROUP BY\n" +
                        "  customer_code, \n" +
                        "  inbound_prefix, \n" +
                        "  inbound_code\n"
                )
                .toString();
    }
}
