package com.namoadigital.prj001.sql;

import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_FormDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * Created by d.luche on 21/03/2018.
 * Verifica se o serial esta vinculado a alguma O.S
 *
 *
 * Alterado por LUCHE - 01/05/2019
 *
 * Modificado query para avaliar se existe vinculo do serial com as seguintes tabela:
 *
 * SM_SO, IO_INBOUND_ITEM, IO_MOVE, IO_OUTBOUND_ITEM
 *
 * BARRIONUEVO - 09-11-2021
 * Adicionado os processos do ticket.
 */

public class MD_Product_Serial_Sql_012 implements Specification {
    public static final String EXISTS = "LINK_EXISTS";

    private long customer_code;
    private long product_code;
    private long serial_code;

    public MD_Product_Serial_Sql_012(long customer_code, long product_code, long serial_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_code = serial_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        //Antiga até 30/04/2019
//        return sb
//                .append(" SELECT\n" +
//                        "   count(1) "+EXISTS+"\n" +
//                        " FROM\n" +
//                        "   "+ SM_SODao.TABLE+" s \n" +
//                        " WHERE\n" +
//                        "   s.customer_code = '"+customer_code+"'\n" +
//                        "   and s.product_code = '"+product_code+"'\n" +
//                        "   and s.serial_code = '"+serial_code+"'\n" +
//                        ";")
//                //.append(EXISTS)
//                .toString();
    //NOVA
                return sb
                .append("  SELECT\n" +
                        "    SUM(t."+EXISTS+") "+EXISTS+"\n" +
                        "  FROM\n" +
                        "  (\n" +
                        "        SELECT\n" +
                        "          count(1) "+EXISTS+"\n" +
                        "        FROM\n" +
                        "          "+ SM_SODao.TABLE+" s \n" +
                        "        WHERE\n" +
                        "          s.customer_code = '"+customer_code+"'\n" +
                        "          and s.product_code = '"+product_code+"'\n" +
                        "          and s.serial_code = '"+serial_code+"'\n" +
                        "         \n" +
                        "        UNION\n" +
                        "        \n" +
                        "        SELECT\n" +
                        "          count(1) "+EXISTS+"\n" +
                        "        FROM\n" +
                        "          "+ IO_Inbound_ItemDao.TABLE+" i\n" +
                        "        WHERE\n" +
                        "          i.customer_code = '"+customer_code+"'\n" +
                        "          and i.product_code = '"+product_code+"'\n" +
                        "          and i.serial_code = '"+serial_code+"'\n" +
                        "          \n" +
                        "        UNION\n" +
                        "        \n" +
                        "        SELECT\n" +
                        "          count(1) "+EXISTS+"\n" +
                        "        FROM\n" +
                        "          "+ IO_MoveDao.TABLE+" m\n" +
                        "        WHERE\n" +
                        "          m.customer_code = '"+customer_code+"'\n" +
                        "          and m.product_code = '"+product_code+"'\n" +
                        "          and m.serial_code = '"+serial_code+"'\n" +
                        "          \n" +
                        "        UNION\n" +
                        "        \n" +
                        "        SELECT\n" +
                        "          count(1) "+EXISTS+"\n" +
                        "        FROM\n" +
                        "          "+ IO_Outbound_ItemDao.TABLE+" oi\n" +
                        "        WHERE\n" +
                        "          oi.customer_code = '"+customer_code+"'\n" +
                        "          and oi.product_code = '"+product_code+"'\n" +
                        "          and oi.serial_code = '"+serial_code+"'\n" +
                        "        UNION\n" +
                        "        \n" +
                        "        SELECT\n" +
                        "          count(1) "+EXISTS+"\n" +
                        "        FROM\n" +
                                    TK_TicketDao.TABLE +" t,\n" +
                                    TK_Ticket_StepDao.TABLE + " s,\n" +
                                    TK_Ticket_CtrlDao.TABLE + " c,\n" +
                                    TK_Ticket_FormDao.TABLE +"  f\n" +
                        "        WHERE\n" +
                        "           t.customer_code = " +customer_code + "\n" +
                        "           and t.customer_code = s.customer_code\n" +
                        "           and t.ticket_prefix = s.ticket_prefix\n" +
                        "           and t.ticket_code = s.ticket_code\n" +
                        "           and t.current_step_order = s.step_order\n" +

                        "           and s.ticket_prefix = c.ticket_prefix\n" +
                        "           and s.ticket_code = c.ticket_code\n" +
                        "           and s.customer_code = c.customer_code\n" +
                        "           and s.step_code = c.step_code\n" +

                        "           and c.customer_code = f.customer_code\n" +
                        "           and c.ticket_prefix = f.ticket_prefix\n" +
                        "           and c.ticket_code = f.ticket_code\n" +
                        "           and c.ticket_seq_tmp = f.ticket_seq_tmp\n" +
                        "           and c.product_code =  '"+product_code+"'\n" +
                        "           and c.serial_code =  '"+serial_code+"'\n" +
                        "           and t.ticket_status in ('"+ ConstantBase.SYS_STATUS_PENDING +"','"+ ConstantBase.SYS_STATUS_PROCESS+ "')\n" +
                        "           and s.step_status in ('"+ ConstantBase.SYS_STATUS_PENDING +"','"+ ConstantBase.SYS_STATUS_PROCESS+ "')\n" +
                        "           and c.ctrl_status in ('"+ ConstantBase.SYS_STATUS_PENDING +"','"+ ConstantBase.SYS_STATUS_PROCESS+ "')\n" +
                        "           and c.ctrl_type = '"+ ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM + "' \n" +
                        "  ) T \n")
                //.append(EXISTS)
                .toString();

    }
}
