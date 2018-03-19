package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_PackDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;

/**
 * Created by d.luche on 25/05/2017.
 *
 * Query da Act026.
 * Seleciona todas S.O em que o usr pode ter alguma ação, incluindo
 * o status offine WAITING_SYNC.
 * "Filtro Minhas Pendencias"
 * A Query base é a Sql_Act026_001
 *
 */

public class Sql_Act005_004 implements Specification {
    public static final String PARTNER_RESTRICTION = "PARTNER_RESTRICTION";
    public static final String QTD_SERVICES = "QTD_SERVICES";
    public static final String QTD_MY_PENDING_SO = "QTD_MY_PENDING_SO";

    private long customer_code;
    private String site_code;
    private int zone_code;

    public Sql_Act005_004(long customer_code, String site_code, int zone_code) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.zone_code = zone_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb =  new StringBuilder();
        return
                sb
                .append(" SELECT \n" +
                        "  COUNT(1) "+QTD_MY_PENDING_SO+"\n" +
                        " FROM\n" +
                        "  (\n" +
                        "       SELECT\n" +
                        "         IFNULL(\n" +
                        "                (SELECT\n" +
                        "                  count(1)\n" +
                        "                FROM    \n" +
                        "                  (SELECT \n" +
                        "                       s.customer_code,\n" +
                        "                       s.so_prefix,\n" +
                        "                       s.so_code,\n" +
                        "                       s.site_code,\n" +
                        "                       s.zone_code,\n" +
                        "                       s.status, \n" +
                        "                       SUM(CASE WHEN e.status in ('"+Constant.SYS_STATUS_DONE+"','"+Constant.SYS_STATUS_NOT_EXECUTED+"') THEN 1 ELSE 0 END) QTY_DONE,\n" +
                        "                       (CASE WHEN \n" +
                        "                              S.exec_type = '"+ Constant.SO_SERVICE_TYPE_YES_NO+"' \n" +
                        "                                 THEN\n" +
                        "                                 (CASE WHEN s.partner_code IS NOT NULL\n" +
                        "                                       THEN (\n" +
                        "                                            SELECT\n" +
                        "                                                COUNT(1)  \n" +
                        "                                            FROM\n" +
                        "                                             "+ MD_PartnerDao.TABLE+" m\n" +
                        "                                            WHERE \n" +
                        "                                                m.customer_code = s.customer_code\n" +
                        "                                                and m.partner_code = s.partner_code\n" +
                        "                                            ) \n" +
                        "                                        ELSE \n" +
                        "                                        -1\n" +
                        "                                        END)\n" +
                        "                             ELSE (CASE WHEN IFNULL(\n" +
                        "                                                       (SELECT\n" +
                        "                                                          max(e2.partner_code) partner_code\n" +
                        "                                                        FROM\n" +
                        "                                                            "+SM_SO_Service_ExecDao.TABLE+" e2\n" +
                        "                                                        WHERE\n" +
                        "                                                          e2.customer_code =  e.customer_code\n" +
                        "                                                          and e2.so_prefix = e.so_prefix \n" +
                        "                                                          AND e2.so_code = e.so_code\n" +
                        "                                                          AND e2.price_list_code = e.price_list_code\n" +
                        "                                                          AND e2.pack_code = e.pack_code\n" +
                        "                                                          AND e2.pack_seq = e.pack_seq\n" +
                        "                                                          AND e2.category_price_code = e.category_price_code\n" +
                        "                                                          AND e2.service_code  = e.service_code\n" +
                        "                                                          AND e2.service_seq  = e.service_seq   \n" +
                        "                                                          AND e2.status NOT IN ('" + Constant.SYS_STATUS_CANCELLED + "','" + Constant.SYS_STATUS_INCONSISTENT + "')\n" +
                        "               )\n" +
                        "                                      \n" +
                        "                                                        , s.partner_code) IS NOT NULL \n" +
                        "                                           THEN\n" +
                        "                                           (SELECT\n" +
                        "                                                COUNT(1)  \n" +
                        "                                            FROM\n" +
                        "                                               "+ MD_PartnerDao.TABLE+" m\n" +
                        "                                            WHERE                        \n" +
                        "                                                m.customer_code = s.customer_code\n" +
                        "                                                and m.partner_code = IFNULL( (SELECT\n" +
                        "                                                          max(e2.partner_code) partner_code\n" +
                        "                                                        FROM\n" +
                        "                                                            "+SM_SO_Service_ExecDao.TABLE+" e2\n" +
                        "                                                        WHERE\n" +
                        "                                                          e2.customer_code =  e.customer_code\n" +
                        "                                                          and e2.so_prefix = e.so_prefix \n" +
                        "                                                          AND e2.so_code = e.so_code\n" +
                        "                                                          AND e2.price_list_code = e.price_list_code\n" +
                        "                                                          AND e2.pack_code = e.pack_code\n" +
                        "                                                          AND e2.pack_seq = e.pack_seq\n" +
                        "                                                          AND e2.category_price_code = e.category_price_code\n" +
                        "                                                          AND e2.service_code  = e.service_code\n" +
                        "                                                          AND e2.service_seq  = e.service_seq   \n" +
                        "                                                          AND e2.status NOT IN('" + Constant.SYS_STATUS_CANCELLED + "','" + Constant.SYS_STATUS_INCONSISTENT + "')\n" +
                        "                                                         ), s.partner_code )\n" +
                        "                                            ) \n" +
                        "                                           ELSE \n" +
                        "                                            -1\n" +
                        "                                           END)\n" +
                        "                                      END  )"+PARTNER_RESTRICTION+",\n" +
                        "                   (SELECT \n" +
                        "                      so.status \n" +
                        "                    FROM \n" +
                        "                      "+ SM_SODao.TABLE+" so \n" +
                        "                    WHERE \n" +
                        "                      so.customer_code = s3.customer_code\n" +
                        "                      AND so.so_prefix = s3.so_prefix\n" +
                        "                      AND so.so_code = s3.so_code\n" +
                        "                   ) SO_STATUS ,\n" +
                        "                   SUM(CASE WHEN \n" +
                        "                         e.status = '"+Constant.SYS_STATUS_PROCESS+"' \n" +
                        "                         and \n" +
                        "                         ( \n" +
                        "                           e.partner_code IS NULL OR \n" +
                        "                                (SELECT\n" +
                        "                                     COUNT(1)  \n" +
                        "                                 FROM\n" +
                        "                                     "+ MD_PartnerDao.TABLE+" m \n" +
                        "                                 WHERE                        \n" +
                        "                                     m.customer_code = e.customer_code\n" +
                        "                                     and m.partner_code = e.partner_code\n" +
                        "                               ) <> 0           )\n" +
                        "                    THEN \n" +
                        "                     1\n" +
                        "                    ELSE\n" +
                        "                     0\n" +
                        "                    END) ANY_PARTNER      \n" +
                        "                   FROM\n" +
                        "                     "+ SM_SO_PackDao.TABLE+" p,\n" +
                        "                     "+ SM_SO_ServiceDao.TABLE+" s\n" +
                        "                   LEFT JOIN  \n" +
                        "                    "+ SM_SO_Service_ExecDao.TABLE+" e on e.customer_code =  S.customer_code\n" +
                        "                                             AND e.so_prefix = S.so_prefix\n" +
                        "                                             AND e.so_code =  S.so_code \n" +
                        "                                             AND e.price_list_code = S.price_list_code\n" +
                        "                                             AND e.pack_code =  S.pack_code\n" +
                        "                                             AND e.pack_seq =  S.pack_seq\n" +
                        "                                             AND e.category_price_code = S.category_price_code  \n" +
                        "                                             AND e.service_code =  S.service_code          \n" +
                        "                                             AND e.service_seq = S.service_seq\n" +
                        "                                             AND S.customer_code = s3.customer_code\n" +
                        "                                             AND S.so_prefix = s3.so_prefix\n" +
                        "                                             AND S.so_code = s3.so_code\n" +
                        "                                             AND e.status NOT IN ('" + Constant.SYS_STATUS_CANCELLED + "','" + Constant.SYS_STATUS_INCONSISTENT + "')\n" +
                        "                   \n" +
                        "                   WHERE\n" +
                        "                   \n" +
                        "                     P.customer_code = S.customer_code  \n" +
                        "                     AND P.so_prefix = S.so_prefix  \n" +
                        "                     AND P.so_code = S.so_code  \n" +
                        "                     AND P.price_list_code = S.price_list_code  \n" +
                        "                     AND P.pack_code =  S.pack_code  \n" +
                        "                     AND P.pack_seq = S.pack_seq      \n" +
                        "                     \n" +
                        "                     AND S.customer_code = S3.customer_code\n" +
                        "                     AND S.so_prefix = S3.so_prefix\n" +
                        "                     AND S.so_code = S3.so_code\n" +
                        "                   \n" +
                        "                   GROUP BY\n" +
                        "                      S.customer_code,\n" +
                        "                      S.so_prefix, \n" +
                        "                      S.so_code,\n" +
                        "                      S.price_list_code,\n" +
                        "                      S.pack_code, \n" +
                        "                      S.pack_seq,  \n" +
                        "                      S.category_price_code,\n" +
                        "                      S.service_code,\n" +
                        "                      S.service_seq     \n" +
                        "                   \n" +
                        "                   ORDER BY \n" +
                        "                    exec_seq_oper) TTT \n" +
                        "                WHERE\n" +
                        "                    1 = 1\n" +
                        "                    AND TTT.status = '"+Constant.SYS_STATUS_PENDING+"'\n" +
                        "                    AND (TTT.PARTNER_RESTRICTION IN (-1,1) or TTT.ANY_PARTNER > 0)\n" +
                        "                    AND (TTT.ZONE_CODE is null or (TTT.SITE_CODE||'|'||TTT.ZONE_CODE = '"+site_code+"|"+zone_code+"'))\n" +
                        "               GROUP BY\n" +
                        "                 TTT.customer_code,\n" +
                        "                 TTT.so_prefix,\n" +
                        "                 TTT.so_code\n" +
                        "                    \n" +
                        "           ),\n" +
                        "            CASE WHEN S3.status = '"+Constant.SYS_STATUS_WAITING_SYNC+"'    \n" +
                        "                      THEN 1\n" +
                        "                 WHEN S3.status = '"+Constant.SYS_STATUS_WAITING_CLIENT+"' AND S3.client_type = '"+Constant.CLIENT_TYPE_USER+"' AND s3.approve_client = 1\n" +
                        "                      THEN 1\n" +
                        "                      ELSE 0\n" +
                        "            END           \n" +
                        "            ) "+QTD_SERVICES+" ,\n" +
                        "             s3.*          \n" +
                        "       FROM\n" +
                        "        "+SM_SODao.TABLE+" s3\n" +
                        "       WHERE\n" +
                        "         s3.customer_code = '"+customer_code+"'\n" +
                        "         and s3.status NOT IN ('" + Constant.SYS_STATUS_CANCELLED + "','" + Constant.SYS_STATUS_DONE + "')\n " +
                        "         and "+QTD_SERVICES+" > 0 \n " +
                        " ) F \n")

                .append(";")
                .append(QTD_MY_PENDING_SO)
                .toString();
    }
}
