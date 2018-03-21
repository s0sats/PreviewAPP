package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 25/05/2017.
 * Retorna exec_tmp,task_tmp e task_perc da ultima task done em aberto.
 * Usada quando o Serviço é Start/Stop , a Ação é e ja existe um exec aberta.
 */

public class Sql_Act027_005 implements Specification {

    private String customer_code;
    private String so_prefix;
    private String so_code;
    private String price_list_code;
    private String pack_code;
    private String pack_seq;
    private String category_price_code;
    private String service_code;
    private String service_seq;

    public Sql_Act027_005(String customer_code, String so_prefix, String so_code, String price_list_code, String pack_code, String pack_seq, String category_price_code, String service_code, String service_seq) {
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

        StringBuilder sb =  new StringBuilder();
        return
                sb
                .append("  SELECT\n" +
                        "    t.exec_tmp,\n" +
                        "    t.task_tmp,\n" +
                        "    t.task_perc \n" +
                        "  FROM\n" +
                                SM_SO_Service_Exec_TaskDao.TABLE +" t, \n" +
                        "        (SELECT \n" +
                        "            /*t.*,\n" +
                        "            MAX(t.exec_tmp) exec_tmp,\n" +
                        "            MAX(t.task_tmp) task_tmp,*/\n" +
                        "            e.*,\n" +
                        "            MAX(t.task_seq_oper) task_seq_oper\n" +
                        "         FROM\n" +
                                      SM_SO_Service_ExecDao.TABLE +" e ,\n" +
                                      SM_SO_Service_Exec_TaskDao.TABLE +" t\n" +
                        "        WHERE \n" +
                        "           e.CUSTOMER_CODE = t.CUSTOMER_CODE\n" +
                        "           AND e.SO_PREFIX = t.SO_PREFIX\n" +
                        "           AND e.SO_CODE = t.SO_CODE\n" +
                        "           AND e.PRICE_LIST_CODE =  t.PRICE_LIST_CODE\n" +
                        "           AND e.PACK_CODE = t.PACK_CODE\n" +
                        "           AND e.PACK_SEQ = t.PACK_SEQ\n" +
                        "           AND e.CATEGORY_PRICE_CODE = t.CATEGORY_PRICE_CODE\n" +
                        "           AND e.SERVICE_CODE = t.SERVICE_CODE\n" +
                        "           AND e.SERVICE_SEQ = t.SERVICE_SEQ\n" +
                        "           AND e.exec_tmp = t.exec_tmp \n" +
                        "           \n" +
                        "          AND e.CUSTOMER_CODE = '"+customer_code+"'\n" +
                        "          AND e.SO_PREFIX = '"+so_prefix+"'\n" +
                        "          AND e.SO_CODE = '"+so_code+"'\n" +
                        "          AND e.PRICE_LIST_CODE = '"+price_list_code+"'\n" +
                        "          AND e.PACK_CODE = '"+pack_code+"'\n" +
                        "          AND e.PACK_SEQ = '"+pack_seq+"'\n" +
                        "          AND e.CATEGORY_PRICE_CODE = '"+ category_price_code +"'\n" +
                        "          AND e.SERVICE_CODE = '"+service_code+"'\n" +
                        "          AND e.SERVICE_SEQ = '"+service_seq+"' \n" +
                        "          AND t.status = '"+ Constant.SYS_STATUS_DONE+"'"+
                        "           \n" +
                        "        GROUP BY\n" +
                        "           e.CUSTOMER_CODE,\n" +
                        "           e.SO_PREFIX ,\n" +
                        "           e.SO_CODE ,\n" +
                        "           e.PRICE_LIST_CODE ,\n" +
                        "           e.PACK_CODE,\n" +
                        "           e.PACK_SEQ,\n" +
                        "           e.CATEGORY_PRICE_CODE,\n" +
                        "           e.SERVICE_CODE,\n" +
                        "           e.SERVICE_SEQ,\n" +
                        "           e.exec_tmp\n" +
                        "           ) e\n" +
                        "           \n" +
                        "   WHERE\n" +
                        "       e.CUSTOMER_CODE = t.CUSTOMER_CODE\n" +
                        "       AND e.SO_PREFIX = t.SO_PREFIX\n" +
                        "       AND e.SO_CODE = t.SO_CODE\n" +
                        "       AND e.PRICE_LIST_CODE =  t.PRICE_LIST_CODE\n" +
                        "       AND e.PACK_CODE = t.PACK_CODE\n" +
                        "       AND e.PACK_SEQ = t.PACK_SEQ\n" +
                        "       AND e.CATEGORY_PRICE_CODE = t.CATEGORY_PRICE_CODE\n" +
                        "       AND e.SERVICE_CODE = t.SERVICE_CODE\n" +
                        "       AND e.SERVICE_SEQ = t.SERVICE_SEQ\n" +
                        "       AND e.exec_tmp = t.exec_tmp \n" +
                        "       AND e.task_seq_oper = t.task_seq_oper")
                .append(";")
                .append(SM_SO_Service_Exec_TaskDao.EXEC_TMP+"#"+SM_SO_Service_Exec_TaskDao.TASK_TMP+"#"+SM_SO_Service_Exec_TaskDao.TASK_PERC)
                .toString();
    }
}
