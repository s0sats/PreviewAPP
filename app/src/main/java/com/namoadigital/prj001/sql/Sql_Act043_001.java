package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_PackDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act043.Act043_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * Created by neomatrix on 23/02/17.
 */

public class Sql_Act043_001 implements Specification {
    public static final String PACK_SERVICE_ID = "PACK_SERVICE_ID";
    public static final String PACK_SERVICE_DESC = "PACK_SERVICE_DESC";
    public static final String PACK_SERVICE_DESC_FULL = "PACK_SERVICE_DESC_FULL";
    public static final String IN_PROCESS = "IN_PROCESS";

    private long customer_code;
    private int so_prefix;
    private int so_code;

    public Sql_Act043_001(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append("   SELECT      \n" +
                        "         T.TYPE_PS "+ Act043_Main.TYPE_PS +",\n" +
                        "         T.CUSTOMER_CODE customer_code,\n" +
                        "         T.SO_PREFIX so_prefix,\n" +
                        "         T.SO_CODE so_code,\n" +
                        "         T.PRICE_LIST_CODE price_list_code,\n" +
                        "         T.PACK_CODE pack_code,\n" +
                        "         T.PACK_SEQ pack_seq,\n" +
                        "         T.CATEGORY_PRICE_CODE category_price_code,\n" +
                        "         T.SERVICE_CODE service_code,\n" +
                        "         T.SERVICE_SEQ service_seq,\n" +
                        "         T.PACK_SERVICE_DESC "+PACK_SERVICE_DESC+",\n" +
                        "         T.PACK_SERVICE_ID "+PACK_SERVICE_ID+",\n" +
                        "         T.PACK_SERVICE_DESC_FULL "+PACK_SERVICE_DESC_FULL+", \n" +
                        "         T.QTY qty,\n" +
                        "         printf(\"%.2f\",T.PRICE) price,\n" +
                        "         T.MANUAL_PRICE manual_price,\n" +
                        "         T.COMMENTS comments,\n" +
                        "         T.EXEC_SEQ_OPER exec_seq_oper,\n" +
                        "         T.exec_code exec_code,\n" +
                        "         (CASE WHEN  T.TYPE_PS = '"+ Act043_Main.TYPE_PS_PACK+"' THEN\n" +
                        "                   CASE\n" +
                        "                       WHEN T.QTD_SERVICES > 0 AND (T.STATUS / QTD_SERVICES) = 1 AND (T.STATUS % QTD_SERVICES) = 0 THEN '"+Constant.SYS_STATUS_DONE+"'\n" +
                        "                       WHEN T.QTD_SERVICES > 0 AND (T.STATUS / QTD_SERVICES) = 3 AND (T.STATUS % QTD_SERVICES) = 0 THEN '"+Constant.SYS_STATUS_CANCELLED+"'\n" +
                        "                       ELSE '"+ Constant.SYS_STATUS_PENDING+"'\n" +
                        "                    END \n" +
                        "                ELSE\n" +
                        "                  T.STATUS\n" +
                        "          END)  status,\n" +
                        "        (CASE WHEN T.TYPE_PS = '"+ Act043_Main.TYPE_PS_PACK+"' THEN\n" +
                        "                  IFNULL( (SELECT \n" +
                        "                       SUM(\n" +
                        "                             CASE WHEN  t2.status IS NOT NULL AND t2.status not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"','"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"')\n" +
                        "                                  THEN 1\n" +
                        "                                  ELSE 0\n" +
                        "                             END )\n" +
                        "                   FROM "+ SM_SO_Service_ExecDao.TABLE +" e\n" +
                        "                   LEFT JOIN\n" +
                        "                         " + SM_SO_Service_Exec_TaskDao.TABLE +" t2 ON \n" +
                        "                                            e.customer_code =  t2.customer_code\n" +
                        "                                            AND e.so_prefix = t2.so_prefix\n" +
                        "                                            AND e.so_code = t2.so_code\n" +
                        "                                            AND e.price_list_code = t2.price_list_code\n" +
                        "                                            AND e.pack_code = t2.pack_code\n" +
                        "                                            AND e.pack_seq = t2.pack_seq\n" +
                        "                                            AND e.category_price_code = t2.category_price_code\n" +
                        "                                            AND e.service_code = t2.service_code\n" +
                        "                                            AND e.service_seq = t2.service_seq  \n" +
                        "                                            AND e.exec_tmp = t2.exec_tmp  \n" +
                        "\n" +
                        "                   WHERE e.customer_code =  T.customer_code\n" +
                        "                         AND e.so_prefix = T.so_prefix\n" +
                        "                         AND e.so_code =  T.so_code \n" +
                        "                         AND e.price_list_code = T.price_list_code\n" +
                        "                         AND e.pack_code =  T.pack_code\n" +
                        "                         AND e.pack_seq =  T.pack_seq\n" +
                        "                         AND E.customer_code = '"+customer_code+"'\n" +
                        "                         AND E.so_prefix = '"+so_prefix+"'\n" +
                        "                         AND E.so_code = '"+so_code+"'),0)  \n" +
                        "               ELSE \n" +
                        "                    (SELECT \n" +
                        "                       COUNT(\n" +
                        "                             CASE WHEN  e.status IS NOT NULL AND e.status not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"','"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"')\n" +
                        "                                  THEN 1\n" +
                        "                                  ELSE 0\n" +
                        "                             END )\n" +
                        "                   FROM "+ SM_SO_Service_ExecDao.TABLE +" e\n" +
                        "                   WHERE e.customer_code =  T.customer_code\n" +
                        "                         AND e.so_prefix = T.so_prefix\n" +
                        "                         AND e.so_code =  T.so_code \n" +
                        "                         AND e.price_list_code = T.price_list_code\n" +
                        "                         AND e.pack_code =  T.pack_code\n" +
                        "                         AND e.pack_seq =  T.pack_seq\n" +
                        "                         AND e.category_price_code = t.category_price_code\n" +
                        "                         AND e.service_code = t.service_code\n" +
                        "                         and e.service_seq = t.service_seq\n" +
                        "                         AND E.customer_code = '"+customer_code+"'\n" +
                        "                         AND E.so_prefix = '"+so_prefix+"'\n" +
                        "                         AND E.so_code = '"+so_code+"') \n" +
                        "                  \n" +
                        "            END) "+IN_PROCESS+"\n " +
                        "   FROM (\n" +
                        "             SELECT '"+Act043_Main.TYPE_PS_PACK+"' "+Act043_Main.TYPE_PS+",\n" +
                        "                    p.CUSTOMER_CODE,\n" +
                        "                    p.so_prefix,\n" +
                        "                    p.so_code,\n" +
                        "                    p.PRICE_LIST_CODE,\n" +
                        "                    p.PACK_CODE,\n" +
                        "                    p.pack_seq,\n" +
                        "                    s.category_price_code,\n" +
                        "                    0 SERVICE_CODE,\n" +
                        "                    0 service_seq,\n" +
                        "                    MAX(P.PACK_ID) "+PACK_SERVICE_ID+",\n" +
                        "                    MAX(P.PACK_DESC) "+PACK_SERVICE_DESC+",\n" +
                        "                    MAX(P.PACK_ID || ' - ' || P.PACK_DESC) "+PACK_SERVICE_DESC_FULL+",\n" +
                        "                    1 QTY,\n" +
                        "                    printf(\"%.2f\",SUM(s.qty * s.PRICE)) PRICE,\n" +
                        "                    0 MANUAL_PRICE, \n" +
                        "                    '' COMMENTS, \n" +
                        "                    SUM(CASE WHEN S.STATUS = '"+Constant.SYS_STATUS_DONE+"' THEN 1\n" +
                        "                          WHEN S.STATUS = '"+Constant.SYS_STATUS_CANCELLED+"' THEN 3\n" +
                        "                          ELSE 0\n" +
                        "                         END) STATUS ,\n" +
                        "                    COUNT(1) QTD_SERVICES,\n" +
                        "                    MIN(S.EXEC_SEQ_OPER) EXEC_SEQ_OPER,\n" +
                        "                    0 exec_code \n" +
                        "             FROM\n" +
                                            SM_SO_PackDao.TABLE +"  p,\n" +
                                            SM_SO_ServiceDao.TABLE +" s\n" +
                        "             WHERE\n" +
                        "                p.customer_code =  s.customer_code\n" +
                        "                AND p.so_prefix = s.so_prefix\n" +
                        "                AND p.so_code = s.so_code\n" +
                        "                AND p.price_list_code = s.price_list_code\n" +
                        "                AND p.pack_code = s.pack_code\n" +
                        "                AND p.pack_seq = s.pack_seq\n" +
                        "                \n" +
                        "                AND p.customer_code = '"+customer_code+"'\n" +
                        "                AND p.so_prefix = '"+so_prefix+"'\n" +
                        "                AND p.so_code = '"+so_code+"'\n" +
                        "                AND P.SELECTION_TYPE = 'PACK'\n" +
                        "                AND s.STATUS not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"','"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"') \n" +
                        "             GROUP BY\n" +
                        "                   p.CUSTOMER_CODE,\n" +
                        "                   p.PRICE_LIST_CODE,\n" +
                        "                   p.PACK_CODE,\n" +
                        "                   p.PACK_SEQ  \n" +
                        "            --FIM PACK       \n" +
                        "            UNION\n" +
                        "            --INI service\n" +
                        "            SELECT '"+Act043_Main.TYPE_PS_SERVICE+"' "+Act043_Main.TYPE_PS+",\n" +
                        "                    s.CUSTOMER_CODE,\n" +
                        "                    p.so_prefix,\n" +
                        "                    p.so_code,\n" +
                        "                    s.PRICE_LIST_CODE,\n" +
                        "                    s.PACK_CODE,\n" +
                        "                    p.pack_seq,\n" +
                        "                    s.category_price_code,\n" +
                        "                    s.SERVICE_CODE,\n" +
                        "                    s.service_seq,\n" +
                        "                    s.SERVICE_ID "+PACK_SERVICE_ID+",\n" +
                        "                    s.SERVICE_DESC "+PACK_SERVICE_DESC+",\n" +
                        "                    s.SERVICE_ID || ' - ' || S.SERVICE_DESC  "+PACK_SERVICE_DESC_FULL+",\n" +
                        "                    s.qty QTY,\n" +
                        "                    printf(\"%.2f\",s.qty * s.PRICE) PRICE,\n" +
                        "                    s.MANUAL_PRICE, \n" +
                        "                    S.COMMENTS COMMENTS, \n" +
                        "                    S.STATUS STATUS, \n" +
                        "                    1 QTD_SERVICES, \n" +
                        "                    S.EXEC_SEQ_OPER EXEC_SEQ_OPER, \n" +
                        "                    0 exec_code \n" +
                        "             FROM\n" +
                                            SM_SO_PackDao.TABLE +"  p,\n" +
                                            SM_SO_ServiceDao.TABLE +" s\n" +
                        "             WHERE\n" +
                        "                p.customer_code =  s.customer_code\n" +
                        "                AND p.so_prefix = s.so_prefix\n" +
                        "                AND p.so_code = s.so_code\n" +
                        "                AND p.price_list_code = s.price_list_code\n" +
                        "                AND p.pack_code = s.pack_code\n" +
                        "                AND p.pack_seq = s.pack_seq\n" +
                        "                \n" +
                        "                AND p.customer_code = '"+customer_code+"'\n" +
                        "                AND p.so_prefix = '"+so_prefix+"'\n" +
                        "                AND p.so_code = '"+so_code+"'\n" +
                        "                AND P.SELECTION_TYPE = 'UNITARY' \n" +
                        "                AND s.STATUS not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"','"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"') \n" +
                        "                --FIM service                       \n" +
                        "   ) T\n" +
                        "   ORDER BY\n" +
                       /* "       T.TYPE_PS,\n" +
                        "       T.CUSTOMER_CODE,\n" +
                        "       T.PRICE_LIST_CODE,\n" +
                        "       T.PACK_CODE,\n" +
                        "       T.PACK_SEQ,\n" +
                        "       --T.category_price_code,\n" +
                        "       T.SERVICE_CODE,\n" +
                        "       T.SERVICE_SEQ  "*/
                       "        T.EXEC_SEQ_OPER"
                       )
                .append(";")
                .toString();
    }
}
