package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_BrandDao;
import com.namoadigital.prj001.dao.MD_Brand_ColorDao;
import com.namoadigital.prj001.dao.MD_Brand_ModelDao;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_PackDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 25/05/2017.
 *
 * Query da Act026.
 * Seleciona todas S.O em que o usr pode ter alguma ação, incluindo
 * o status offine WAITING_SYNC.
 * "Filtro Minhas Pendencias"
 * A query "base" dessa é Sql_Act027_002, porem sem os campos que calculam
 * qual icone e qual ação existe naquela service.
 *
 * Created by d.luche on 25/05/2018.(Sim, exatamento 1 ano depois)
 * Comentado lefts join de marca, modelo e cor, pois agora as informações
 * vem da tabela de seria.
 *
 */

public class Sql_Act026_001 implements Specification {
    public static final String PARTNER_RESTRICTION = "PARTNER_RESTRICTION";
    public static final String QTD_SERVICES = "QTD_SERVICES";

    private long customer_code;
    private String site_code;
    private int zone_code;
    private String product_code;
    private String serial_id;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(SM_SODao.columns);
    private String serialFilter = "";
    private String only_avaliable_where = "";

    public Sql_Act026_001(long customer_code, String site_code, int zone_code, String product_code, String serial_id, boolean filter_only_avaliable) {
        this.customer_code = customer_code;
        this.site_code = site_code;
        this.zone_code = zone_code;
        this.product_code = product_code;
        this.serial_id = serial_id;

        if(product_code != null && serial_id != null){
            serialFilter += " AND s3.product_code = '"+product_code+"' \n"+
                            " AND s3.serial_id = '"+serial_id+"' \n";
        }
        //
        if(filter_only_avaliable){
            this.only_avaliable_where =
                    " and "+QTD_SERVICES+" > 0 \n ";
        }
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb =  new StringBuilder();
        return
                sb
                .append(" SELECT\n" +
                        "   IFNULL(\n" +
                        "          (SELECT\n" +
                        "            count(1)\n" +
                        "          FROM    \n" +
                        "            (SELECT \n" +
                        "                 s.customer_code,\n" +
                        "                 s.so_prefix,\n" +
                        "                 s.so_code,\n" +
                        "                 s.site_code,\n" +
                        "                 s.zone_code,\n" +
                        "                 s.status, \n" +
                        "                 SUM(CASE WHEN e.status in ('"+Constant.SYS_STATUS_DONE+"','"+Constant.SYS_STATUS_NOT_EXECUTED+"') THEN 1 ELSE 0 END) QTY_DONE,\n" +
                        "                 (CASE WHEN \n" +
                        "                        S.exec_type = '"+ Constant.SO_SERVICE_TYPE_YES_NO+"' \n" +
                        "                           THEN\n" +
                        "                           (CASE WHEN s.partner_code IS NOT NULL\n" +
                        "                                 THEN (\n" +
                        "                                      SELECT\n" +
                        "                                          COUNT(1)  \n" +
                        "                                      FROM\n" +
                        "                                       "+ MD_PartnerDao.TABLE+" m\n" +
                        "                                      WHERE \n" +
                        "                                          m.customer_code = s.customer_code\n" +
                        "                                          and m.partner_code = s.partner_code\n" +
                        "                                      ) \n" +
                        "                                  ELSE \n" +
                        "                                  -1\n" +
                        "                                  END)\n" +
                        "                       ELSE (CASE WHEN IFNULL(\n" +
                        "                                                 (SELECT\n" +
                        "                                                    max(e2.partner_code) partner_code\n" +
                        "                                                  FROM\n" +
                        "                                                      "+SM_SO_Service_ExecDao.TABLE+" e2\n" +
                        "                                                  WHERE\n" +
                        "                                                    e2.customer_code =  e.customer_code\n" +
                        "                                                    and e2.so_prefix = e.so_prefix \n" +
                        "                                                    AND e2.so_code = e.so_code\n" +
                        "                                                    AND e2.price_list_code = e.price_list_code\n" +
                        "                                                    AND e2.pack_code = e.pack_code\n" +
                        "                                                    AND e2.pack_seq = e.pack_seq\n" +
                        "                                                    AND e2.category_price_code = e.category_price_code\n" +
                        "                                                    AND e2.service_code  = e.service_code\n" +
                        "                                                    AND e2.service_seq  = e.service_seq   \n" +
                        "                                                    AND e2.status NOT IN ('" + Constant.SYS_STATUS_CANCELLED + "','" + Constant.SYS_STATUS_INCONSISTENT + "')\n" +
                        "         )\n" +
                        "                                \n" +
                        "                                                  , s.partner_code) IS NOT NULL \n" +
                        "                                     THEN\n" +
                        "                                     (SELECT\n" +
                        "                                          COUNT(1)  \n" +
                        "                                      FROM\n" +
                        "                                         "+ MD_PartnerDao.TABLE+" m\n" +
                        "                                      WHERE                        \n" +
                        "                                          m.customer_code = s.customer_code\n" +
                        "                                          and m.partner_code = IFNULL( (SELECT\n" +
                        "                                                    max(e2.partner_code) partner_code\n" +
                        "                                                  FROM\n" +
                        "                                                      "+SM_SO_Service_ExecDao.TABLE+" e2\n" +
                        "                                                  WHERE\n" +
                        "                                                    e2.customer_code =  e.customer_code\n" +
                        "                                                    and e2.so_prefix = e.so_prefix \n" +
                        "                                                    AND e2.so_code = e.so_code\n" +
                        "                                                    AND e2.price_list_code = e.price_list_code\n" +
                        "                                                    AND e2.pack_code = e.pack_code\n" +
                        "                                                    AND e2.pack_seq = e.pack_seq\n" +
                        "                                                    AND e2.category_price_code = e.category_price_code\n" +
                        "                                                    AND e2.service_code  = e.service_code\n" +
                        "                                                    AND e2.service_seq  = e.service_seq   \n" +
                        "                                                    AND e2.status NOT IN('" + Constant.SYS_STATUS_CANCELLED + "','" + Constant.SYS_STATUS_INCONSISTENT + "')\n" +
                        "                                                   ), s.partner_code )\n" +
                        "                                      ) \n" +
                        "                                     ELSE \n" +
                        "                                      -1\n" +
                        "                                     END)\n" +
                        "                                END  )"+PARTNER_RESTRICTION+",\n" +
                        "             (SELECT \n" +
                        "                so.status \n" +
                        "              FROM \n" +
                        "                "+ SM_SODao.TABLE+" so \n" +
                        "              WHERE \n" +
                        "                so.customer_code = s3.customer_code\n" +
                        "                AND so.so_prefix = s3.so_prefix\n" +
                        "                AND so.so_code = s3.so_code\n" +
                        "             ) SO_STATUS ,\n" +
                        "             SUM(CASE WHEN \n" +
                        "                   e.status = '"+Constant.SYS_STATUS_PROCESS+"' \n" +
                        "                   and \n" +
                        "                   ( \n" +
                        "                     e.partner_code IS NULL OR \n" +
                        "                          (SELECT\n" +
                        "                               COUNT(1)  \n" +
                        "                           FROM\n" +
                        "                               "+ MD_PartnerDao.TABLE+" m \n" +
                        "                           WHERE                        \n" +
                        "                               m.customer_code = e.customer_code\n" +
                        "                               and m.partner_code = e.partner_code\n" +
                        "                         ) <> 0           )\n" +
                        "              THEN \n" +
                        "               1\n" +
                        "              ELSE\n" +
                        "               0\n" +
                        "              END) ANY_PARTNER      \n" +
                        "             FROM\n" +
                        "               "+ SM_SO_PackDao.TABLE+" p,\n" +
                        "               "+ SM_SO_ServiceDao.TABLE+" s\n" +
                        "             LEFT JOIN  \n" +
                        "              "+ SM_SO_Service_ExecDao.TABLE+" e on e.customer_code =  S.customer_code\n" +
                        "                                       AND e.so_prefix = S.so_prefix\n" +
                        "                                       AND e.so_code =  S.so_code \n" +
                        "                                       AND e.price_list_code = S.price_list_code\n" +
                        "                                       AND e.pack_code =  S.pack_code\n" +
                        "                                       AND e.pack_seq =  S.pack_seq\n" +
                        "                                       AND e.category_price_code = S.category_price_code  \n" +
                        "                                       AND e.service_code =  S.service_code          \n" +
                        "                                       AND e.service_seq = S.service_seq\n" +
                        "                                       AND S.customer_code = s3.customer_code\n" +
                        "                                       AND S.so_prefix = s3.so_prefix\n" +
                        "                                       AND S.so_code = s3.so_code\n" +
                        "                                       AND e.status NOT IN ('" + Constant.SYS_STATUS_CANCELLED + "','" + Constant.SYS_STATUS_INCONSISTENT + "')\n" +
                        "             \n" +
                        "             WHERE\n" +
                        "             \n" +
                        "               P.customer_code = S.customer_code  \n" +
                        "               AND P.so_prefix = S.so_prefix  \n" +
                        "               AND P.so_code = S.so_code  \n" +
                        "               AND P.price_list_code = S.price_list_code  \n" +
                        "               AND P.pack_code =  S.pack_code  \n" +
                        "               AND P.pack_seq = S.pack_seq      \n" +
                        "               \n" +
                        "               AND S.customer_code = S3.customer_code\n" +
                        "               AND S.so_prefix = S3.so_prefix\n" +
                        "               AND S.so_code = S3.so_code\n" +
                        "             \n" +
                        "             GROUP BY\n" +
                        "                S.customer_code,\n" +
                        "                S.so_prefix, \n" +
                        "                S.so_code,\n" +
                        "                S.price_list_code,\n" +
                        "                S.pack_code, \n" +
                        "                S.pack_seq,  \n" +
                        "                S.category_price_code,\n" +
                        "                S.service_code,\n" +
                        "                S.service_seq     \n" +
                        "             \n" +
                        "             ORDER BY \n" +
                        "              exec_seq_oper) TTT \n" +
                        "          WHERE\n" +
                        "              1 = 1\n" +
                        "              AND TTT.status = '"+Constant.SYS_STATUS_PENDING+"'\n" +
                        "              AND (TTT.PARTNER_RESTRICTION IN (-1,1) or TTT.ANY_PARTNER > 0)\n" +
                        "              AND (TTT.ZONE_CODE is null or (TTT.SITE_CODE||'|'||TTT.ZONE_CODE = '"+site_code+"|"+zone_code+"'))\n" +
                        "         GROUP BY\n" +
                        "           TTT.customer_code,\n" +
                        "           TTT.so_prefix,\n" +
                        "           TTT.so_code\n" +
                        "              \n" +
                        "     ),\n" +
                        "      CASE WHEN S3.status = '"+Constant.SYS_STATUS_WAITING_SYNC+"'    \n" +
                        "                THEN 1\n" +
                        "           WHEN S3.status = '"+Constant.SYS_STATUS_WAITING_CLIENT+"' AND S3.client_type = '"+Constant.CLIENT_TYPE_USER+"' AND s3.approve_client = 1\n" +
                        "                THEN 1\n" +
                        "                ELSE 0\n" +
                        "      END           \n" +
                        "      ) "+QTD_SERVICES+" ,\n" +
                        "       s3.*,\n" +
                        "      '"+Constant.PARAM_KEY_TYPE_SO+"' " + Constant.PARAM_KEY_TYPE+" \n," +
                        "      ps."+ MD_BrandDao.BRAND_DESC+" ,\n" +
                        "      ps."+MD_Brand_ModelDao.MODEL_DESC+" ,\n" +
                        "      ps."+MD_Brand_ColorDao.COLOR_DESC+" \n" +
                        " FROM\n" +
                        "  "+SM_SODao.TABLE+" s3\n" +
                        "  LEFT JOIN\n" +
                        MD_Product_SerialDao.TABLE +" ps on ps.customer_code = s3.customer_code\n" +
                        "                             and ps.product_code = s3.product_code \n" +
                        "                             and ps.serial_code = s3.serial_code\n" +
                        /*" LEFT JOIN\n" +
                        MD_BrandDao.TABLE +" b on ps.customer_code = b.customer_code\n" +
                        "                    and ps.brand_code = b.brand_code\n" +
                        " LEFT JOIN\n" +
                        MD_Brand_ModelDao.TABLE +" m on ps.customer_code = m.customer_code\n" +
                        "                       and ps.brand_code = m.brand_code\n" +
                        "                       and ps.model_code = m.model_code\n" +
                        " LEFT JOIN\n" +
                        MD_Brand_ColorDao.TABLE +" c on ps.customer_code = c.customer_code\n" +
                        "                       and ps.brand_code = c.brand_code\n" +
                        "                       and ps.color_code = c.color_code\n" +*/
                        " WHERE\n" +
                        "   s3.customer_code = '"+customer_code+"'\n" +
                        "   and s3.status NOT IN ('" + Constant.SYS_STATUS_CANCELLED + "','" + Constant.SYS_STATUS_DONE + "') \n")
                .append(serialFilter)
                .append(only_avaliable_where)
                .append(";")
                .append(HmAuxFields+"#" +
                                QTD_SERVICES+"#"+
                                Constant.PARAM_KEY_TYPE+"#"+
                                MD_BrandDao.BRAND_DESC+"#"+
                                MD_Brand_ModelDao.MODEL_DESC+"#"+
                                MD_Brand_ColorDao.COLOR_DESC
                )
                .toString();
    }
}
