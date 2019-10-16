package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_PackDao;
import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act043.Act043_Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * Created by d.luche on 15/10/2019
 *
 * Query que seleciona as execuções do serviços, retornando um registro similar ao da Sql_Act043_001.
 * Esse registro será usado na montagem da lista exibida na act043_preview
 */

public class Sql_Act043_003 implements Specification {
    public static final String PACK_SERVICE_ID = "PACK_SERVICE_ID";
    public static final String PACK_SERVICE_DESC = "PACK_SERVICE_DESC";
    public static final String PACK_SERVICE_DESC_FULL = "PACK_SERVICE_DESC_FULL";
    public static final String IN_PROCESS = "IN_PROCESS";

    private String customer_code;
    private String so_prefix;
    private String so_code;
    private String price_list_code;
    private String pack_code;
    private String pack_seq;
    private String category_price_code;
    private String service_code;
    private String service_seq;

    public Sql_Act043_003(String customer_code, String so_prefix, String so_code, String price_list_code, String pack_code, String pack_seq, String category_price_code, String service_code, String service_seq) {
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

        return sb
                .append(" SELECT      \n" +
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
                        "         (CASE WHEN  T.status IS NOT NULL\n" +
                        "                THEN T.status\n" +
                        "                ELSE '"+ Constant.SYS_STATUS_PENDING+"'\n" +
                        "           END) status,\n" +
                        "         (CASE WHEN  T.status IS NOT NULL AND T.status <> '"+ Constant.SYS_STATUS_CANCELLED+"'\n" +
                        "                THEN 1\n" +
                        "                ELSE 0\n" +
                        "           END ) "+IN_PROCESS+" \n" +
                        " FROM (\n" +
                        "            --INI service\n" +
                        "            SELECT '"+Act043_Main.TYPE_PS_SERVICE+"' "+ Act043_Main.TYPE_PS +",\n" +
                        "                    s.CUSTOMER_CODE,\n" +
                        "                    p.so_prefix,\n" +
                        "                    p.so_code,\n" +
                        "                    s.PRICE_LIST_CODE,\n" +
                        "                    s.PACK_CODE,\n" +
                        "                    p.pack_seq,\n" +
                        "                    s.category_price_code,\n" +
                        "                    s.SERVICE_CODE,\n" +
                        "                    s.service_seq,\n" +
                        "                    s.SERVICE_ID PACK_SERVICE_ID,\n" +
                        "                    s.SERVICE_DESC PACK_SERVICE_DESC,\n" +
                        "                    s.SERVICE_ID || ' - ' || S.SERVICE_DESC  PACK_SERVICE_DESC_FULL,\n" +
                        "                    s.qty QTY,\n" +
                        "                    printf(\"%.2f\",s.PRICE) PRICE,\n" +
                        "                    s.MANUAL_PRICE, \n" +
                        "                    S.COMMENTS COMMENTS, \n" +
                        "                    E.STATUS STATUS,                    \n" +
                        "                    S.EXEC_SEQ_OPER EXEC_SEQ_OPER,\n" +
                        "                    E.exec_code, \n" +
                        "                    E.exec_tmp \n" +
                        "             FROM\n" +
                        "           " + SM_SO_PackDao.TABLE +"  p,\n" +
                        "           " + SM_SO_ServiceDao.TABLE +" s\n" +
                        "             LEFT JOIN\n" +
                        "           " + SM_SO_Service_ExecDao.TABLE +" e ON \n" +
                        "                                      s.customer_code =  e.customer_code\n" +
                        "                                      AND s.so_prefix = e.so_prefix\n" +
                        "                                      AND s.so_code = e.so_code\n" +
                        "                                      AND s.price_list_code = e.price_list_code\n" +
                        "                                      AND s.pack_code = e.pack_code\n" +
                        "                                      AND s.pack_seq = e.pack_seq\n" +
                        "                                      AND s.category_price_code = e.category_price_code\n" +
                        "                                      AND s.service_code = e.service_code\n" +
                        "                                      AND s.service_seq = e.service_seq  \n" +
                        "                                      AND s.customer_code = '"+customer_code+"'\n" +
                        "                                      AND s.so_prefix = '"+so_prefix+"'\n" +
                        "                                      AND s.so_code = '"+so_code+"'\n" +
                        "                                      AND s.price_list_code = '"+price_list_code+"'\n" +
                        "                                      AND s.pack_code = '"+pack_code+"'\n" +
                        "                                      AND s.pack_seq = '"+pack_seq+"'\n" +
                        "                                      AND s.category_price_code = '"+category_price_code+"'\n" +
                        "                                      AND s.service_code = '"+service_code+"'\n" +
                        "                                      AND s.service_seq = '"+service_seq+"'\n" +
                        "                                      AND e.STATUS not in ('"+ ConstantBaseApp.SYS_STATUS_CANCELLED +"','"+ ConstantBaseApp.SYS_STATUS_INCONSISTENT +"') \n" +
                        "                                       \n" +
                        "             WHERE\n" +
                        "                p.customer_code =  s.customer_code\n" +
                        "                AND p.so_prefix = s.so_prefix\n" +
                        "                AND p.so_code = s.so_code\n" +
                        "                AND p.price_list_code = s.price_list_code\n" +
                        "                AND p.pack_code = s.pack_code\n" +
                        "                AND p.pack_seq = s.pack_seq\n" +
                        "                                              \n" +
                        "                AND p.customer_code = '"+customer_code+"'\n" +
                        "                AND p.so_prefix = '"+so_prefix+"'\n" +
                        "                AND p.so_code = '"+so_code+"'\n" +
                        "                AND s.price_list_code = '"+price_list_code+"'\n" +
                        "                AND s.pack_code = '"+pack_code+"'\n" +
                        "                AND s.pack_seq = '"+pack_seq+"'\n" +
                        "                AND s.category_price_code = '"+category_price_code+"'\n" +
                        "                AND s.service_code = '"+service_code+"'\n" +
                        "                AND s.service_seq = '"+service_seq+"'\n" +
                        "                --FIM service \n" +
                        "   ) T\n" +
                        "   ORDER BY\n" +
                        "        T.exec_tmp")
                .append(";")
                .toString();
    }
}
