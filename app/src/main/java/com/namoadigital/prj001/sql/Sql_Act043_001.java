package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_PackDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by neomatrix on 23/02/17.
 */

public class Sql_Act043_001 implements Specification {
    public static final String TYPE_PS = "TYPE_PS";
    public static final String TYPE_PS_PACK = "P";
    public static final String TYPE_PS_SERVICE= "S";
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
                        "         T.TYPE_PS "+TYPE_PS+",\n" +
                        "         T.CUSTOMER_CODE customer_code,\n" +
                        "         T.PRICE_LIST_CODE price_list_code,\n" +
                        "         T.PACK_CODE pack_code,\n" +
                        "         T.PACK_SEQ pack_seq,\n" +
                        "         T.SERVICE_CODE service_code,\n" +
                        "         T.SERVICE_SEQ service_seq,\n" +
                        "         T.PACK_SERVICE_DESC "+PACK_SERVICE_DESC+",\n" +
                        "         T.PACK_SERVICE_DESC_FULL "+PACK_SERVICE_DESC_FULL+", \n" +
                        "         printf(\"%.2f\",T.PRICE) price,\n" +
                        "         T.MANUAL_PRICE manual_price\n," +
                        "        (CASE WHEN T.TYPE_PS = '"+TYPE_PS_PACK+"' THEN\n" +
                        "                  (SELECT \n" +
                        "                       COUNT(\n" +
                        "                             CASE WHEN  e.status IS NOT NULL AND e.status <> '"+ Constant.SYS_STATUS_CANCELLED+"'\n" +
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
                        "                         AND E.customer_code = '"+customer_code+"'\n" +
                        "                         AND E.so_prefix = '"+so_prefix+"'\n" +
                        "                         AND E.so_code = '"+so_code+"') \n" +
                        "               ELSE \n" +
                        "                    (SELECT \n" +
                        "                       COUNT(\n" +
                        "                             CASE WHEN  e.status IS NOT NULL AND e.status <> '"+ Constant.SYS_STATUS_CANCELLED+"'\n" +
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
                        "             SELECT '"+TYPE_PS_PACK+"' "+TYPE_PS+",\n" +
                        "                    p.CUSTOMER_CODE,\n" +
                        "                    p.so_prefix,\n" +
                        "                    p.so_code,\n" +
                        "                    p.PRICE_LIST_CODE,\n" +
                        "                    p.PACK_CODE,\n" +
                        "                    p.pack_seq,\n" +
                        "                    s.category_price_code,\n" +
                        "                    0 SERVICE_CODE,\n" +
                        "                    0 service_seq,\n" +
                        "                    MAX(P.PACK_DESC) "+PACK_SERVICE_DESC+",\n" +
                        "                    MAX(P.PACK_ID || ' - ' || P.PACK_DESC) "+PACK_SERVICE_DESC_FULL+",\n" +
                        "                    printf(\"%.2f\",SUM(s.qty * s.PRICE)) PRICE,\n" +
                        "                    0 MANUAL_PRICE\n" +
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
                        "             GROUP BY\n" +
                        "                   p.CUSTOMER_CODE,\n" +
                        "                   p.PRICE_LIST_CODE,\n" +
                        "                   p.PACK_CODE,\n" +
                        "                   p.PACK_SEQ  \n" +
                        "            --FIM PACK       \n" +
                        "            UNION\n" +
                        "            --INI service\n" +
                        "            SELECT '"+TYPE_PS_SERVICE+"' "+TYPE_PS+",\n" +
                        "                    s.CUSTOMER_CODE,\n" +
                        "                    p.so_prefix,\n" +
                        "                    p.so_code,\n" +
                        "                    s.PRICE_LIST_CODE,\n" +
                        "                    s.PACK_CODE,\n" +
                        "                    p.pack_seq,\n" +
                        "                    s.category_price_code,\n" +
                        "                    s.SERVICE_CODE,\n" +
                        "                    s.service_seq,\n" +
                        "                    s.SERVICE_DESC "+PACK_SERVICE_DESC+",\n" +
                        "                    s.SERVICE_ID || ' - ' || S.SERVICE_DESC  "+PACK_SERVICE_DESC_FULL+",\n" +
                        "                     printf(\"%.2f\",s.qty * s.PRICE) PRICE,\n" +
                        "                    s.MANUAL_PRICE\n" +
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
                        "                --FIM service                       \n" +
                        "   ) T\n" +
                        "   ORDER BY\n" +
                        "       T.TYPE_PS,\n" +
                        "       T.CUSTOMER_CODE,\n" +
                        "       T.PRICE_LIST_CODE,\n" +
                        "       T.PACK_CODE,\n" +
                        "       T.PACK_SEQ,\n" +
                        "       --T.category_price_code,\n" +
                        "       T.SERVICE_CODE,\n" +
                        "       T.SERVICE_SEQ  ")
                .append(";")
                .append(
                        TYPE_PS+"#"+
                        SM_SO_ServiceDao.CUSTOMER_CODE+"#"+
                        SM_SO_ServiceDao.PRICE_LIST_CODE+"#"+
                        SM_SO_ServiceDao.PACK_CODE+"#"+
                        SM_SO_ServiceDao.PACK_SEQ+"#"+
                        SM_SO_ServiceDao.SERVICE_CODE+"#"+
                        SM_SO_ServiceDao.SERVICE_SEQ+"#"+
                        PACK_SERVICE_DESC+"#"+
                        PACK_SERVICE_DESC_FULL+"#"+
                        SM_SO_ServiceDao.PRICE+"#"+
                        SM_SO_ServiceDao.MANUAL_PRICE +"#"+
                        IN_PROCESS
                )
                .toString();
    }
}
