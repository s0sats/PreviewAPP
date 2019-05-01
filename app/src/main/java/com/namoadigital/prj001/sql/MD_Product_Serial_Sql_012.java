package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.IO_Outbound_ItemDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.database.Specification;

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
                        "  ) T \n")
                //.append(EXISTS)
                .toString();

    }
}
