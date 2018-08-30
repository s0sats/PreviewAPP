package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by d.luche on 25/05/2017.
 * Retorna exec_tmp,task_tmp e task_perc da ultima task done em aberto.
 * Usada quando o Serviço é Start/Stop , a Ação é e ja existe um exec aberta.
 */

public class Sql_Act027_006 implements Specification {
    public static final String NEXT_TASK_SEQ_OPER = "NEXT_TASK_SEQ_OPER";

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int price_list_code;
    private int pack_code;
    private int pack_seq;
    private int category_price_code;
    private int service_code;
    private int service_seq;

    public Sql_Act027_006(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq, int category_price_code, int service_code, int service_seq) {
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
                .append("   SELECT \n" +
                        "       IFNULL(MAX(t.task_seq_oper),0) + 1 "+NEXT_TASK_SEQ_OPER+" \n" +
                        "   FROM\n" +
                            SM_SO_Service_ExecDao.TABLE +" e \n" +
                        "   LEFT JOIN  \n" +
                            SM_SO_Service_Exec_TaskDao.TABLE +" t ON t.CUSTOMER_CODE = e.CUSTOMER_CODE\n" +
                        "                                   AND t.SO_PREFIX = e.SO_PREFIX\n" +
                        "                                   AND t.SO_CODE = e.SO_CODE\n" +
                        "                                   AND t.PRICE_LIST_CODE =  e.PRICE_LIST_CODE\n" +
                        "                                   AND t.PACK_CODE = e.PACK_CODE\n" +
                        "                                   AND t.PACK_SEQ = e.PACK_SEQ\n" +
                        "                                   AND t.CATEGORY_PRICE_CODE = e.CATEGORY_PRICE_CODE\n" +
                        "                                   AND t.SERVICE_CODE = e.SERVICE_CODE\n" +
                        "                                   AND t.SERVICE_SEQ = e.SERVICE_SEQ\n" +
                        "                                   AND t.exec_tmp = e.exec_tmp \n" +
                        "   WHERE  \n" +
                        "          e.CUSTOMER_CODE = '"+customer_code+"'\n" +
                        "          AND e.SO_PREFIX = '"+so_prefix+"'\n" +
                        "          AND e.SO_CODE = '"+so_code+"'\n" +
                        "          AND e.PRICE_LIST_CODE = '"+price_list_code+"'\n" +
                        "          AND e.PACK_CODE = '"+pack_code+"'\n" +
                        "          AND e.PACK_SEQ = '"+pack_seq+"'\n" +
                        "          AND e.CATEGORY_PRICE_CODE = '"+ category_price_code +"'\n" +
                        "          AND e.SERVICE_CODE = '"+service_code+"'\n" +
                        "          AND e.SERVICE_SEQ = '"+service_seq+"' \n")
                .append(";")
                //.append(NEXT_TASK_SEQ_OPER)
                .toString();
    }
}
