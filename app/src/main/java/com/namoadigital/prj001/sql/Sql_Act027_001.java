package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_PackDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 25/05/2017.
 *
 * Query da Act027. Seleciona todos os serviços da S.O e retorno qtd de finalizados por serviço, se serviço possui item em execução ,
 * Se existe apenas uma task aberta o tipo.
 *
 */

public class Sql_Act027_001 implements Specification {
    public static final String SET_FLAG = "SET_FLAG";
    public static final String QTY_DONE = "QTY_DONE";

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(SM_SO_ServiceDao.columns);

    public Sql_Act027_001(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb =  new StringBuilder();
        return
                sb
                .append("   SELECT \n" +
                        "       P.PRICE_LIST_ID,\n" +
                        "       P.PRICE_LIST_DESC,\n" +
                        "       P.PACK_ID,\n" +
                        "       P.PACK_DESC,\n" +
                        "       s.*,\n" +
                        "       MAX(CASE WHEN e.status = '"+ Constant.SYS_STATUS_PROCESS+"' THEN 1 ELSE 0 END) "+SET_FLAG+",\n" +
                        "       SUM(CASE WHEN e.status = '"+ Constant.SYS_STATUS_DONE+"' THEN 1 ELSE 0 END) "+QTY_DONE+"\n" +
                        "    \n" +
                        "   FROM\n" +
                        "     "+ SM_SO_PackDao.TABLE+" p,\n" +
                        "     "+ SM_SO_ServiceDao.TABLE+" s\n" +
                        "   LEFT JOIN  \n" +
                        "    "+ SM_SO_Service_ExecDao.TABLE+" e on e.customer_code =  S.customer_code\n" +
                        "                             AND e.so_prefix = S.so_prefix\n" +
                        "                             AND e.so_code =  S.so_code \n" +
                        "                             AND e.price_list_code = S.price_list_code\n" +
                        "                             AND e.pack_code =  S.pack_code\n" +
                        "                             AND e.pack_seq =  S.pack_seq\n" +
                        "                             AND e.category_price_code = S.category_price_code  \n" +
                        "                             AND e.service_code =  S.service_code          \n" +
                        "                             AND e.service_seq = S.service_seq\n" +
                        "                             AND S.customer_code = '"+customer_code+"'\n" +
                        "                             AND S.so_prefix = '"+so_prefix+"'\n" +
                        "                             AND S.so_code = '"+so_code+"'\n" +
                        "   \n" +
                        "   WHERE\n" +
                        "   \n" +
                        "     P.customer_code = S.customer_code  \n" +
                        "     AND P.so_prefix = S.so_prefix  \n" +
                        "     AND P.so_code = S.so_code  \n" +
                        "     AND P.price_list_code = S.price_list_code  \n" +
                        "     AND P.pack_code =  S.pack_code  \n" +
                        "     AND P.pack_seq = S.pack_seq      \n" +
                        "     \n" +
                        "     AND S.customer_code = '"+customer_code+"'\n" +
                        "     AND S.so_prefix = '"+so_prefix+"'\n" +
                        "     AND S.so_code = '"+so_code+"'\n" +
                        "   \n" +
                        "   GROUP BY\n" +
                        "      S.customer_code,\n" +
                        "      S.so_prefix, \n" +
                        "      S.so_code,\n" +
                        "      S.price_list_code,\n" +
                        "      S.pack_code, \n" +
                        "      S.pack_seq,  \n" +
                        "      S.category_price_code,\n" +
                        "      S.service_code,\n" +
                        "      S.service_seq     \n" +
                        "   \n" +
                        "   ORDER BY \n" +
                        "    exec_seq_oper;")
                //.append(SM_SO_PackDao.PRICE_LIST_ID+"#"+ SM_SO_PackDao.PRICE_LIST_DESC+"#"+SM_SO_PackDao.PACK_ID+"#"+SM_SO_PackDao.PACK_DESC+"#"+ HmAuxFields+"#"+SET_FLAG+"#"+QTY_DONE)
                .toString();
    }
}
