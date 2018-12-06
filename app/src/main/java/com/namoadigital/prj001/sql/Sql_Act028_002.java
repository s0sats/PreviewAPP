package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 25/05/2017.
 * <p>
 * Query que pega todos os dados de todas as execuções de um serviço e tb traz informações adicionas
 * para a celula de execução.
 */

public class Sql_Act028_002 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int price_list_code;
    private int pack_code;
    private int pack_seq;
    private int category_price_code;
    private int service_code;
    private int service_seq;

    public Sql_Act028_002(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq, int category_price_code, int service_code, int service_seq) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.price_list_code = price_list_code;
        this.pack_code = pack_code;
        this.pack_seq = pack_seq;
        this.category_price_code = category_price_code;
        this.service_code = service_code;
        this.service_seq = service_seq;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();
        return
                sb
                        .append(" SELECT\n" +
                                "  CASE WHEN T.SO_STATUS = 0 AND T.PACK_STATUS = 0 AND T.SERVICE_STATUS = 0 \n" +
                                "  THEN\n" +
                                "    1\n" +
                                "  ELSE\n" +
                                "    0\n" +
                                "  END full_status\n" +
                                "  \n" +
                                "FROM\n" +
                                "   (  \n" +
                                "\n" +
                                "      SELECT\n" +
                                "          SUM(\n" +
                                "          CASE WHEN s.status <> 'PROCESS' AND  s.status <> 'PENDING'     \n" +
                                "          THEN\n" +
                                "            1\n" +
                                "          ELSE\n" +
                                "            0\n" +
                                "          END\n" +
                                "          ) SO_STATUS,\n" +
                                "          SUM(\n" +
                                "          CASE WHEN p.status <> 'PROCESS' AND  p.status <> 'PENDING'     \n" +
                                "          THEN\n" +
                                "            1\n" +
                                "          ELSE\n" +
                                "            0\n" +
                                "          END\n" +
                                "          ) PACK_STATUS,\n" +
                                "          SUM(\n" +
                                "          CASE WHEN ss.status <> 'PROCESS' AND  ss.status <> 'PENDING'     \n" +
                                "          THEN\n" +
                                "            1\n" +
                                "          ELSE\n" +
                                "            0\n" +
                                "          END\n" +
                                "          ) SERVICE_STATUS\n" +
                                "        \n" +
                                "      FROM\n" +
                                "        sm_sos s,\n" +
                                "        sm_so_packs p,\n" +
                                "        (SELECT\n" +
                                "           * \n" +
                                "         FROM\n" +
                                "           sm_so_services s\n" +
                                "         WHERE\n" +
                                "           s.customer_code = '" + customer_code + "'\n" +
                                "           and s.so_prefix  = '" + so_prefix + "'\n" +
                                "           and s.so_code = '" + so_code + "'\n" +
                                "           and s.price_list_code = '" + price_list_code + "'\n" +
                                "           and s.pack_code = '" + pack_code + "'\n" +
                                "           and s.pack_seq = '" + pack_seq + "'\n" +
                                "           and s.category_price_code = '" + category_price_code + "'\n" +
                                "           and s.service_code = '" + service_code + "'\n" +
                                "           and s.service_seq = '" + service_seq + "'\n" +
                                "         )ss\n" +
                                "        \n" +
                                "      WHERE\n" +
                                "       s.customer_code = p.customer_code\n" +
                                "       and s.so_prefix  = p.so_prefix\n" +
                                "       and s.so_code = p.so_code\n" +
                                "       \n" +
                                "       and p.customer_code = ss.customer_code\n" +
                                "       and p.so_prefix = ss.so_prefix\n" +
                                "       and p.so_code = ss.so_code\n" +
                                "       and p.price_list_code = ss.price_list_code\n" +
                                "       and p.pack_code = ss.pack_code\n" +
                                "       and p.pack_seq = ss.pack_seq\n" +
                                "       \n" +
                                "       and s.customer_code  = '" + customer_code + "'\n" +
                                "       and s.so_prefix  = '" + so_prefix + "'\n" +
                                "       and s.so_code = '" + so_code + "'\n" +
                                "       \n" +
                                "      group by\n" +
                                "       s.customer_code,\n" +
                                "       s.so_prefix,\n" +
                                "       s.so_code\n" +
                                ") T")
                        .append(";")
                        //.append("full_status")
                        .toString();
    }
}
