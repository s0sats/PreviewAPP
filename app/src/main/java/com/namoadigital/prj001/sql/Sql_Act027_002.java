package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SM_SO_PackDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_Exec_TaskDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 25/05/2017.
 *
 * Query da Act027. Seleciona todos os serviços da S.O, calcula qtd de execs DONE, calcula se usr tem acesso ao parceiro do serviço ou exec,
 * calcula se existe uma exec em process para mostrar flag, por fim, calcula se icone de atalho deve ser exibido e , no caso do start_stop,
 * qual icone deve ser exibido.
 *
 */

public class Sql_Act027_002 implements Specification {
    public static final String YES_NO_ICON = "YES_NO_ICON";
    public static final String START_STOP_ICON = "START_STOP_ICON";
    public static final String QTY_DONE = "QTY_DONE";
    public static final String SET_FLAG = "SET_FLAG";
    public static final String PARTNER_RESTRICTION = "PARTNER_RESTRICTION";
    public static final String SO_STATUS = "SO_STATUS";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_NONE = "ACTION_NONE";

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private String user_code;
    private String site_code;
    private int zone_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(SM_SO_ServiceDao.columns);
    private String only_avaliable_where = "";

    public Sql_Act027_002(long customer_code, int so_prefix, int so_code, String user_code, String site_code, int zone_code, boolean filter_only_avaliable) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.user_code = user_code;
        this.site_code = site_code;
        this.zone_code = zone_code;
        //
        if(filter_only_avaliable){
            this.only_avaliable_where =
                    "     AND TTT.status = '"+Constant.SO_STATUS_PENDING+"'\n" +
                            "     AND (TTT.PARTNER_RESTRICTION IN (-1,1) or TTT.ANY_PARTNER > 0)\n" +
                            "     AND (TTT.ZONE_CODE is null or (TTT.SITE_CODE||'|'||TTT.ZONE_CODE = '"+site_code+"|"+zone_code+"'))" ;
        }
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb =  new StringBuilder();
        return
                sb
                .append(" SELECT\n" +
                        "   CASE WHEN TTT.SO_STATUS IN ('"+ Constant.SO_STATUS_PENDING+"','"+ Constant.SO_STATUS_PROCESS+"') AND TTT.status = '"+ Constant.SO_STATUS_PENDING+"' AND TTT.exec_type = '"+ Constant.SO_SERVICE_TYPE_YES_NO+"' AND TTT.QTY_DONE < TTT.qty AND TTT.PARTNER_RESTRICTION IN (-1,1)\n" +
                        "       THEN\n" +
                        "        1\n" +
                        "       ELSE\n" +
                        "        0\n" +
                        "       END  "+YES_NO_ICON+",\n" +
                        "   CASE WHEN TTT.SO_STATUS IN ('"+ Constant.SO_STATUS_PENDING+"','"+ Constant.SO_STATUS_PROCESS+"') AND TTT.status = '"+ Constant.SO_STATUS_PENDING+"' AND TTT.exec_type = '"+ Constant.SO_SERVICE_TYPE_START_STOP+"' AND TTT.qty = 1 AND TTT.PARTNER_RESTRICTION IN (-1,1) \n" +
                        "       THEN\n" +
                        "          CASE WHEN TTT.LAST_STATUS in ('"+Constant.SO_STATUS_DONE+"','NOT_EXIST') \n" +
                        "               THEN \n" +
                        "                 '"+ACTION_PLAY+"' \n" +
                        "               ELSE \n" +
                        "                 '"+ACTION_STOP+"' \n" +
                        "               END \n" +
                        "            \n" +
                        "       ELSE\n" +
                        "        '"+ACTION_NONE+"'\n" +
                        "       END  "+START_STOP_ICON+",\n" +
//                        "   TTT.PRICE_LIST_ID,\n" +
//                        "   TTT.PRICE_LIST_DESC,\n" +
//                        "   TTT.PACK_ID,\n" +
//                        "   TTT.PACK_DESC,\n" +
                        "   TTT.*\n" +
                        " FROM    \n" +
                        "   (SELECT \n" +
//                        "        P.PRICE_LIST_ID,\n" +
//                        "        P.PRICE_LIST_DESC,\n" +
                        "        P."+SM_SO_PackDao.PACK_ID+",\n" +
                        "        P."+SM_SO_PackDao.PACK_DESC+",\n" +
                        "        s.*,       \n" +
                        "        sum(CASE WHEN e.status = '"+Constant.SO_STATUS_PROCESS+"' THEN 1 ELSE 0 END) "+SET_FLAG+",\n" +
                        "        SUM(CASE WHEN e.status in ('"+Constant.SO_STATUS_DONE+"','"+Constant.SO_STATUS_NOT_EXECUTED+"') THEN 1 ELSE 0 END) "+QTY_DONE+",\n" +
//                        "        CASE WHEN IFNULL(e.partner_code,s.partner_code) IS NOT NULL \n" +
//                        "             THEN\n" +
//                        "             (SELECT\n" +
//                        "                  COUNT(1)  \n" +
//                        "              FROM\n" +
//                        "                  "+ MD_PartnerDao.TABLE+" m\n" +
//                        "              WHERE                        \n" +
//                        "                  m.customer_code = s.customer_code\n" +
//                        "                  and m.partner_code = IFNULL(e.partner_code,s.partner_code)\n" +
//                        "              ) \n" +
//                        "             ELSE \n" +
//                        "              -1\n" +
//                        "             END  "+PARTNER_RESTRICTION+",\n" +
                        "        CASE WHEN IFNULL(" +
                        "                         CASE WHEN e.status = '"+Constant.SO_STATUS_PROCESS+"' THEN\n" +
                        "                              e.partner_code\n" +
                        "                          ELSE\n" +
                        "                              null\n" +
                        "                          END ," +
                        "               s.partner_code) IS NOT NULL \n" +
                        "             THEN\n" +
                        "             (SELECT\n" +
                        "                  COUNT(1)  \n" +
                        "              FROM\n" +
                        "                  "+ MD_PartnerDao.TABLE+" m\n" +
                        "              WHERE                        \n" +
                        "                  m.customer_code = s.customer_code\n" +
                        "                  and m.partner_code = IFNULL(" +
                        "                                              CASE WHEN e.status = '"+Constant.SO_STATUS_PROCESS+"' THEN\n" +
                        "                                                  e.partner_code\n" +
                        "                                              ELSE\n" +
                        "                                                  null\n" +
                        "                                              END,\n" +
                        "                                               s.partner_code )\n" +
                        "              ) \n" +
                        "             ELSE \n" +
                        "              -1\n" +
                        "             END  "+PARTNER_RESTRICTION+",\n" +
                        "        CASE WHEN s.qty <> 1 THEN\n" +
                        "          0\n" +
                        "        ELSE\n" +
                        "        IFNULL(\n" +
                        "        (SELECT\n" +
                        "            tt.status\n" +
                        "         FROM\n" +
                        "           "+ SM_SO_Service_Exec_TaskDao.TABLE+" tt\n" +
                        "         WHERE\n" +
                        "              tt.customer_code =  e.customer_code\n" +
                        "              AND tt.so_prefix = e.so_prefix\n" +
                        "              AND tt.so_code =  e.so_code \n" +
                        "              AND tt.price_list_code = e.price_list_code\n" +
                        "              AND tt.pack_code =  e.pack_code\n" +
                        "              AND tt.pack_seq =  e.pack_seq\n" +
                        "              AND tt.category_price_code = e.category_price_code  \n" +
                        "              AND tt.service_code =  e.service_code          \n" +
                        "              AND tt.service_seq = e.service_seq                 \n" +
                        "              AND tt.customer_code = '"+customer_code+"'\n" +
                        "              AND tt.so_prefix = '"+so_prefix+"'\n" +
                        "              AND tt.so_code = '"+so_code+"'\n" +
                        "              AND tt.task_user = '"+user_code+"' \n" +
                        "              AND TT.task_seq_oper =(                                \n" +
                        "                               SELECT\n" +
                        "                                 MAX(t.task_seq_oper)                  \n" +
                        "                               FROM\n" +
                        "                                   "+ SM_SO_Service_Exec_TaskDao.TABLE+" t\n" +
                        "                               WHERE\n" +
                        "                                    t.customer_code =  e.customer_code\n" +
                        "                                    AND t.so_prefix = e.so_prefix\n" +
                        "                                    AND t.so_code =  e.so_code \n" +
                        "                                    AND t.price_list_code = e.price_list_code\n" +
                        "                                    AND t.pack_code =  e.pack_code\n" +
                        "                                    AND t.pack_seq =  e.pack_seq\n" +
                        "                                    AND t.category_price_code = e.category_price_code  \n" +
                        "                                    AND t.service_code =  e.service_code          \n" +
                        "                                    AND t.service_seq = e.service_seq\n" +
                        "                                    AND t.exec_tmp = e.exec_tmp                 \n" +
                        "                                    AND t.customer_code = '"+customer_code+"'\n" +
                        "                                    AND t.so_prefix = '"+so_prefix+"'\n" +
                        "                                    AND t.so_code = '"+so_code+"'\n" +
                        "                                    AND t.task_user = '"+user_code+"'\n" +
                        "                                    AND t.status NOT IN ('"+Constant.SO_STATUS_CANCELLED+"','"+Constant.SO_STATUS_INCONSISTENT+"')\n" +
                        "                                    AND e.status NOT IN ('"+Constant.SO_STATUS_CANCELLED+"','"+Constant.SO_STATUS_INCONSISTENT+"')\n" +
                        "                                    ) \n" +
                        " \n" +
                        "        ),'NOT_EXIST')\n" +
                        "         END LAST_STATUS,\n" +
                        "    (SELECT \n" +
                        "       so.status \n" +
                        "     FROM \n" +
                        "       "+ SM_SODao.TABLE +" so \n"+
                        "     WHERE \n" +
                        "       so.customer_code = '"+customer_code+"'\n" +
                        "       AND so.so_prefix = '"+so_prefix+"'\n" +
                        "       AND so.so_code = '"+so_code+"'\n" +
                        "    ) "+SO_STATUS+" ,\n" +
                        "    SUM(CASE WHEN \n" +
                        "          e.status = '"+Constant.SO_STATUS_PROCESS+"' \n" +
                        "          and \n" +
                        "          ( \n" +
                        "            e.partner_code IS NULL OR" +
                        "              (SELECT\n" +
                        "                      COUNT(1)  \n" +
                        "                  FROM\n" +
                        "                      "+ MD_PartnerDao.TABLE+" m\n" +
                        "                  WHERE                        \n" +
                        "                      m.customer_code = e.customer_code\n" +
                        "                      and m.partner_code = e.partner_code\n" +
                        "                ) <> 0" +
                        "           )" +
                        "\n" +
                        "     THEN \n" +
                        "      1\n" +
                        "     ELSE\n" +
                        "      0\n" +
                        "     END) ANY_PARTNER " +
                        "     \n" +
                        "    FROM\n" +
                        "      "+ SM_SO_PackDao.TABLE+" p,\n" +
                        "      "+ SM_SO_ServiceDao.TABLE+" s\n" +
                        "    LEFT JOIN  \n" +
                        "     sm_so_service_execs e on e.customer_code =  S.customer_code\n" +
                        "                              AND e.so_prefix = S.so_prefix\n" +
                        "                              AND e.so_code =  S.so_code \n" +
                        "                              AND e.price_list_code = S.price_list_code\n" +
                        "                              AND e.pack_code =  S.pack_code\n" +
                        "                              AND e.pack_seq =  S.pack_seq\n" +
                        "                              AND e.category_price_code = S.category_price_code  \n" +
                        "                              AND e.service_code =  S.service_code          \n" +
                        "                              AND e.service_seq = S.service_seq\n" +
                        "                              AND S.customer_code = '"+customer_code+"'\n" +
                        "                              AND S.so_prefix = '"+so_prefix+"'\n" +
                        "                              AND S.so_code = '"+so_code+"'\n" +
                        "    \n" +
                        "    WHERE\n" +
                        "    \n" +
                        "      P.customer_code = S.customer_code  \n" +
                        "      AND P.so_prefix = S.so_prefix  \n" +
                        "      AND P.so_code = S.so_code  \n" +
                        "      AND P.price_list_code = S.price_list_code  \n" +
                        "      AND P.pack_code =  S.pack_code  \n" +
                        "      AND P.pack_seq = S.pack_seq      \n" +
                        "      \n" +
                        "      AND S.customer_code = '"+customer_code+"'\n" +
                        "      AND S.so_prefix = '"+so_prefix+"'\n" +
                        "      AND S.so_code = '"+so_code+"'\n" +
                        "    \n" +
                        "    GROUP BY\n" +
                        "       S.customer_code,\n" +
                        "       S.so_prefix, \n" +
                        "       S.so_code,\n" +
                        "       S.price_list_code,\n" +
                        "       S.pack_code, \n" +
                        "       S.pack_seq,  \n" +
                        "       S.category_price_code,\n" +
                        "       S.service_code,\n" +
                        "       S.service_seq     \n" +
                        "    \n" +
                        "    ORDER BY \n" +
                        "     exec_seq_oper) TTT \n" +
                        " WHERE\n" +
                        "     1 = 1\n")
                .append(only_avaliable_where)
                .append(";")
                .append(SM_SO_PackDao.PRICE_LIST_ID+"#"+ SM_SO_PackDao.PRICE_LIST_DESC+"#"+SM_SO_PackDao.PACK_ID+"#"+SM_SO_PackDao.PACK_DESC+"#"+
                        HmAuxFields+"#"+YES_NO_ICON+"#"+START_STOP_ICON+"#"+SET_FLAG+"#"+QTY_DONE+"#"+PARTNER_RESTRICTION+"#"+SO_STATUS
                )
                .toString();
    }
}
