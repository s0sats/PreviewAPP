package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_Task_FileDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 25/05/2017.
 *
 * Query que pega todos os dados de todas as execuções de um serviço e tb traz informações adicionas
 * para a celula de execução.
 */

public class Sql_Act028_001 implements Specification {
    public static final String TASK_PERC = "TASK_PERC";
    public static final String SUM_EXEC_TIME = "SUM_EXEC_TIME";
    public static final String QTY_COMMENT = "QTY_COMMENT";
    public static final String QTY_FILES = "QTY_FILES";
    public static final String MY_TASK = "MY_TASK";

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int price_list_code;
    private int pack_code;
    private int pack_seq;
    private int category_price_code;
    private int service_code;
    private int service_seq;
    private String user_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(SM_SO_Service_ExecDao.columns);

    public Sql_Act028_001(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq, int category_price_code, int service_code, int service_seq, String user_code) {
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
                .append(" SELECT *\n" +
                        " FROM "+ SM_SO_Service_ExecDao.TABLE+" E\n" +
                        " \n" +
                        " LEFT JOIN\n" +
                        "         (SELECT T.EXEC_CODE, T.EXEC_TMP,\n" +
                        "             MAX(IFNULL(X.TASK_PERC,0)) "+TASK_PERC+",\n" +
                        "             MAX(IFNULL(T.SUM_EXEC_TIME,0)) "+SUM_EXEC_TIME+",\n" +
                        "             MAX(IFNULL(T.QTY_COMMENT,0)) "+QTY_COMMENT+",\n" +
                        "             MAX(IFNULL(T.QTY_FILES,0)) "+QTY_FILES+"\n," +
                        "             MAX(IFNULL(T.MY_TASK,0)) "+MY_TASK+"\n" +
                        "      FROM (SELECT T.EXEC_CODE, T.EXEC_TMP,\n" +
                        "                   MAX((CASE WHEN T.STATUS IN ('DONE','NOT_EXECUTED') THEN T.TASK_SEQ_OPER ELSE NULL END)) MAX_TASK_SEQ_OPER,\n" +
                        "                   SUM((CASE WHEN T.STATUS IN ('DONE','NOT_EXECUTED') THEN T.EXEC_TIME ELSE 0 END)) SUM_EXEC_TIME,\n" +
                        "                   SUM((CASE WHEN T.COMMENTS IS NOT NULL THEN 1 ELSE 0 END)) QTY_COMMENT,\n" +
                        "                   SUM((SELECT COUNT(1)\n" +
                        "                        FROM "+ SM_SO_Service_Exec_Task_FileDao.TABLE+" F\n" +
                        "                        WHERE F.CUSTOMER_CODE = T.CUSTOMER_CODE\n" +
                        "                              AND F.SO_PREFIX = T.SO_PREFIX\n" +
                        "                              AND F.SO_CODE = T.SO_CODE\n" +
                        "                              AND F.PRICE_LIST_CODE = T.PRICE_LIST_CODE\n" +
                        "                              AND F.PACK_CODE = T.PACK_CODE\n" +
                        "                              AND F.PACK_SEQ = T.PACK_SEQ\n" +
                        "                              AND F.CATEGORY_PRICE_CODE = T.CATEGORY_PRICE_CODE\n" +
                        "                              AND F.SERVICE_CODE = T.SERVICE_CODE\n" +
                        "                              AND F.SERVICE_SEQ = T.SERVICE_SEQ\n" +
                        "                              AND F.EXEC_TMP = T.EXEC_TMP\n" +
                        "                              AND F.TASK_TMP = T.TASK_TMP)) QTY_FILES," +
                        "                       (SELECT COUNT(1)" +
                        "                        FROM "+ SM_SO_Service_Exec_TaskDao.TABLE+" TT\n" +
                        "                        WHERE TT.CUSTOMER_CODE = T.CUSTOMER_CODE\n" +
                        "                              AND TT.SO_PREFIX = T.SO_PREFIX\n" +
                        "                              AND TT.SO_CODE = T.SO_CODE\n" +
                        "                              AND TT.PRICE_LIST_CODE = T.PRICE_LIST_CODE\n" +
                        "                              AND TT.PACK_CODE = T.PACK_CODE\n" +
                        "                              AND TT.PACK_SEQ = T.PACK_SEQ\n" +
                        "                              AND TT.CATEGORY_PRICE_CODE = T.CATEGORY_PRICE_CODE\n" +
                        "                              AND TT.SERVICE_CODE = T.SERVICE_CODE\n" +
                        "                              AND TT.SERVICE_SEQ = T.SERVICE_SEQ\n" +
                        "                              AND TT.EXEC_TMP = T.EXEC_TMP\n" +
                        "                              AND TT.TASK_TMP = T.TASK_TMP\n" +
                        "                              AND TT.TASK_USER = '"+user_code+"') MY_TASK\n"+
                        "            FROM "+ SM_SO_Service_Exec_TaskDao.TABLE+" T\n" +
                        "            WHERE T.CUSTOMER_CODE = '"+customer_code+"'\n" +
                        "                  AND T.SO_PREFIX = '"+so_prefix+"'\n" +
                        "                  AND T.SO_CODE = '"+so_code+"'\n" +
                        "                  AND T.PRICE_LIST_CODE = '"+price_list_code+"'\n" +
                        "                  AND T.PACK_CODE = '"+pack_code+"'\n" +
                        "                  AND T.PACK_SEQ = '"+pack_seq+"'\n" +
                        "                  AND T.CATEGORY_PRICE_CODE = '"+category_price_code+"'\n" +
                        "                  AND T.SERVICE_CODE = '"+service_code+"'\n" +
                        "                  AND T.SERVICE_SEQ = '"+service_seq+"'\n" +
                        "                  AND T.STATUS NOT IN ('"+ Constant.SO_STATUS_CANCELLED+"','"+Constant.SO_STATUS_INCONSISTENT+"')\n" +
//                        "            GROUP BY T.EXEC_CODE) T\n" +
                        "            GROUP BY T.EXEC_TMP) T\n" +
                        "\n" +
                        "      LEFT JOIN\n" +
                        "           (SELECT *\n" +
                        "            FROM "+ SM_SO_Service_Exec_TaskDao.TABLE+" X\n" +
                        "            WHERE X.CUSTOMER_CODE = '"+customer_code+"'\n" +
                        "                  AND X.SO_PREFIX = '"+so_prefix+"'\n" +
                        "                  AND X.SO_CODE = '"+so_code+"'\n" +
                        "                  AND X.PRICE_LIST_CODE = '"+price_list_code+"'\n" +
                        "                  AND X.PACK_CODE = '"+pack_code+"'\n" +
                        "                  AND X.PACK_SEQ = '"+pack_seq+"'\n" +
                        "                  AND X.CATEGORY_PRICE_CODE = '"+category_price_code+"'\n" +
                        "                  AND X.SERVICE_CODE = '"+service_code+"'\n" +
                        "                  AND X.SERVICE_SEQ = '"+service_seq+"'\n" +
                        "                  AND X.STATUS NOT IN ('"+ Constant.SO_STATUS_CANCELLED+"','"+Constant.SO_STATUS_INCONSISTENT+"')\n"+
                        "                  ) X ON T.EXEC_TMP = X.EXEC_TMP AND  T.MAX_TASK_SEQ_OPER = X.TASK_SEQ_OPER            \n" +
//                        "      GROUP BY T.EXEC_CODE) T ON E.EXEC_CODE = T.EXEC_CODE            \n" +
                        "      GROUP BY T.EXEC_TMP) T ON E.EXEC_TMP = T.EXEC_TMP            \n" +
                        "      \n" +
                        " WHERE  E.CUSTOMER_CODE = '"+customer_code+"'\n" +
                        "        AND E.SO_PREFIX = '"+so_prefix+"'\n" +
                        "        AND E.SO_CODE = '"+so_code+"'\n" +
                        "        AND E.PRICE_LIST_CODE = '"+price_list_code+"'\n" +
                        "        AND E.PACK_CODE = '"+pack_code+"'\n" +
                        "        AND E.PACK_SEQ = '"+pack_seq+"'\n" +
                        "        AND E.CATEGORY_PRICE_CODE = '"+category_price_code+"'\n" +
                        "        AND E.SERVICE_CODE = '"+service_code+"'\n" +
                        "        AND E.SERVICE_SEQ = '"+service_seq+"'\n;")
                .append(HmAuxFields+"#"+TASK_PERC+"#"+SUM_EXEC_TIME+"#"+QTY_COMMENT+"#"+QTY_FILES+"#"+MY_TASK)
                .toString();
    }
}
