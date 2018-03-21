package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_Service_ExecDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 25/05/2017.
 *
 *  Retorna execuções validas de um serviço.
 */

public class Sql_Act027_003 implements Specification {

    private String customer_code;
    private String so_prefix;
    private String so_code;
    private String price_list_code;
    private String pack_code;
    private String pack_seq;
    private String category_price_code;
    private String service_code;
    private String service_seq;

    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(SM_SO_Service_ExecDao.columns);

    public Sql_Act027_003(String customer_code, String so_prefix, String so_code, String price_list_code, String pack_code, String pack_seq, String category_price_codE, String service_code, String service_seq) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.price_list_code = price_list_code;
        this.pack_code = pack_code;
        this.pack_seq = pack_seq;
        this.category_price_code = category_price_codE;
        this.service_code = service_code;
        this.service_seq = service_seq;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb =  new StringBuilder();
        return
                sb
                .append("   SELECT\n" +
                        "     e.*\n" +
                        "   FROM\n" +
                        SM_SO_Service_ExecDao.TABLE + "  e\n" +
                        "   WHERE \n" +
                        "       e.CUSTOMER_CODE = '"+customer_code+"'\n" +
                        "       AND e.SO_PREFIX = '"+so_prefix+"'\n" +
                        "       AND e.SO_CODE = '"+so_code+"'\n" +
                        "       AND e.PRICE_LIST_CODE = '"+price_list_code+"'\n" +
                        "       AND e.PACK_CODE = '"+pack_code+"'\n" +
                        "       AND e.PACK_SEQ = '"+pack_seq+"'\n" +
                        "       AND e.CATEGORY_PRICE_CODE = '"+ category_price_code +"'\n" +
                        "       AND e.SERVICE_CODE = '"+service_code+"'\n" +
                        "       AND e.SERVICE_SEQ = '"+service_seq+"' \n" +
                        "       AND e.status not in ('"+ Constant.SYS_STATUS_CANCELLED+"','"+ Constant.SYS_STATUS_INCONSISTENT+"')")
                /*.append(";")
                .append(HmAuxFields)*/
                .toString();
    }
}
