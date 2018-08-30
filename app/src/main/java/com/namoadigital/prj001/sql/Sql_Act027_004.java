package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 25/05/2017.
 *
 *  Retorna o exec_tmp e task_tmp da unica task em process do usr.
 */

public class Sql_Act027_004 implements Specification {

    private String customer_code;
    private String so_prefix;
    private String so_code;
    private String price_list_code;
    private String pack_code;
    private String pack_seq;
    private String category_price_code;
    private String service_code;
    private String service_seq;
    private String user_code;

    public Sql_Act027_004(String customer_code, String so_prefix, String so_code, String price_list_code, String pack_code, String pack_seq, String category_price_code, String service_code, String service_seq, String user_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.price_list_code = price_list_code;
        this.pack_code = pack_code;
        this.pack_seq = pack_seq;
        this.category_price_code = category_price_code;
        this.service_code = service_code;
        this.service_seq = service_seq;
        this.user_code = user_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb =  new StringBuilder();
        return
                sb
                .append(" SELECT\n" +
                        "   E.exec_tmp exec_tmp,\n" +
                        "   T.task_tmp task_tmp,\n" +
                        "   t.task_perc  task_perc\n" +
                        " FROM\n" +
                        "     sm_so_service_exec_tasks t," +
                        "     (SELECT \n" +
                        "         e.*\n"+
                        "   FROM\n" +
                             SM_SO_Service_ExecDao.TABLE +"  e \n " +
                        "      WHERE \n" +
                        "          e.CUSTOMER_CODE = '"+customer_code+"'\n" +
                        "          AND e.SO_PREFIX = '"+so_prefix+"'\n" +
                        "          AND e.SO_CODE = '"+so_code+"'\n" +
                        "          AND e.PRICE_LIST_CODE = '"+price_list_code+"'\n" +
                        "          AND e.PACK_CODE = '"+pack_code+"'\n" +
                        "          AND e.PACK_SEQ = '"+pack_seq+"'\n" +
                        "          AND e.CATEGORY_PRICE_CODE = '"+ category_price_code +"'\n" +
                        "          AND e.SERVICE_CODE = '"+service_code+"'\n" +
                        "          AND e.SERVICE_SEQ = '"+service_seq+"' \n" +
                        "          AND e.status = '"+ Constant.SYS_STATUS_PROCESS+"') e" +
                        "         WHERE\n" +
                        "               t.CUSTOMER_CODE = e.CUSTOMER_CODE\n" +
                        "               AND t.SO_PREFIX = e.SO_PREFIX\n" +
                        "               AND t.SO_CODE = e.SO_CODE\n" +
                        "               AND t.PRICE_LIST_CODE =  e.PRICE_LIST_CODE\n" +
                        "               AND t.PACK_CODE = e.PACK_CODE\n" +
                        "               AND t.PACK_SEQ = e.PACK_SEQ\n" +
                        "               AND t.CATEGORY_PRICE_CODE = e.CATEGORY_PRICE_CODE\n" +
                        "               AND t.SERVICE_CODE = e.SERVICE_CODE\n" +
                        "               AND t.SERVICE_SEQ = e.SERVICE_SEQ\n" +
                        "               AND t.exec_tmp = e.exec_tmp \n" +
                        "               AND t.status = '"+ Constant.SYS_STATUS_PROCESS+"' \n" +
                        "               AND t.task_user = '"+user_code+"'")
                /*.append("   SELECT\n" +
                        "       e.exec_tmp,  \n" +
                        "       ( SELECT\n" +
                        "           t.task_tmp\n" +
                        "         FROM\n" +
                        "           sm_so_service_exec_tasks t\n" +
                        "         WHERE\n" +
                        "               t.CUSTOMER_CODE = e.CUSTOMER_CODE\n" +
                        "               AND t.SO_PREFIX = e.SO_PREFIX\n" +
                        "               AND t.SO_CODE = e.SO_CODE\n" +
                        "               AND t.PRICE_LIST_CODE =  e.PRICE_LIST_CODE\n" +
                        "               AND t.PACK_CODE = e.PACK_CODE\n" +
                        "               AND t.PACK_SEQ = e.PACK_SEQ\n" +
                        "               AND t.CATEGORY_PRICE_CODE = e.CATEGORY_PRICE_CODE\n" +
                        "               AND t.SERVICE_CODE = e.SERVICE_CODE\n" +
                        "               AND t.SERVICE_SEQ = e.SERVICE_SEQ\n" +
                        "               AND t.exec_tmp = e.exec_tmp \n" +
                        "               AND t.status = '"+ Constant.SYS_STATUS_PROCESS+"' \n" +
                        "               AND t.task_user = '"+user_code+"' ) task_tmp," +
                        "       ( SELECT\n" +
                        "           t.task_perc\n" +
                        "         FROM\n" +
                        "           sm_so_service_exec_tasks t\n" +
                        "         WHERE\n" +
                        "               t.CUSTOMER_CODE = e.CUSTOMER_CODE\n" +
                        "               AND t.SO_PREFIX = e.SO_PREFIX\n" +
                        "               AND t.SO_CODE = e.SO_CODE\n" +
                        "               AND t.PRICE_LIST_CODE =  e.PRICE_LIST_CODE\n" +
                        "               AND t.PACK_CODE = e.PACK_CODE\n" +
                        "               AND t.PACK_SEQ = e.PACK_SEQ\n" +
                        "               AND t.CATEGORY_PRICE_CODE = e.CATEGORY_PRICE_CODE\n" +
                        "               AND t.SERVICE_CODE = e.SERVICE_CODE\n" +
                        "               AND t.SERVICE_SEQ = e.SERVICE_SEQ\n" +
                        "               AND t.exec_tmp = e.exec_tmp \n" +
                        "               AND t.status = '"+ Constant.SYS_STATUS_PROCESS+"' \n" +
                        "               AND '"+user_code+"' ) task_perc  \n" +
                        "   FROM\n" +
                            SM_SO_Service_ExecDao.TABLE +"  e \n " +
                        "      WHERE \n" +
                        "          e.CUSTOMER_CODE = '"+customer_code+"'\n" +
                        "          AND e.SO_PREFIX = '"+so_prefix+"'\n" +
                        "          AND e.SO_CODE = '"+so_code+"'\n" +
                        "          AND e.PRICE_LIST_CODE = '"+price_list_code+"'\n" +
                        "          AND e.PACK_CODE = '"+pack_code+"'\n" +
                        "          AND e.PACK_SEQ = '"+pack_seq+"'\n" +
                        "          AND e.CATEGORY_PRICE_CODE = '"+ category_price_code +"'\n" +
                        "          AND e.SERVICE_CODE = '"+service_code+"'\n" +
                        "          AND e.SERVICE_SEQ = '"+service_seq+"' \n" +
                        "          AND e.status = '"+ Constant.SYS_STATUS_PROCESS+"'")*/
                .append(";")
                //.append(SM_SO_Service_Exec_TaskDao.EXEC_TMP+"#"+SM_SO_Service_Exec_TaskDao.TASK_TMP+"#"+SM_SO_Service_Exec_TaskDao.TASK_PERC)
                .toString();
    }
}
