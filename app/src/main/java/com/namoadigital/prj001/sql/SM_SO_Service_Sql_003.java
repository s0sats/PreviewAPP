package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.SM_SO_ServiceDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 06/07/17.
 */

public class SM_SO_Service_Sql_003 implements Specification {

    private long customer_code;
    private int so_prefix;
    private int so_code;
//    private int price_list_code;
//    private int pack_code;
//    private int pack_seq;

    //public SM_SO_Service_Sql_003(long customer_code, int so_prefix, int so_code, int price_list_code, int pack_code, int pack_seq) {
    public SM_SO_Service_Sql_003(long customer_code, int so_prefix, int so_code) {
        this.customer_code = customer_code;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
//        this.price_list_code = price_list_code;
//        this.pack_code = pack_code;
//        this.pack_seq = pack_seq;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        " S.CUSTOMER_CODE,\n" +
                        " S.SO_PREFIX,\n" +
                        " S.SO_CODE,\n" +
                        " S.PRICE_LIST_CODE,\n" +
                        " P.PACK_CODE,\n" +
                        " S.PACK_SEQ,\n" +
                        " S.CATEGORY_PRICE_CODE,\n" +
                        " S.SERVICE_CODE,\n" +
                        " S.SERVICE_SEQ,\n" +
                        " P.PRICE_LIST_ID,\n" +
                        " P.PRICE_LIST_DESC,\n" +
                        " P.PACK_ID,\n" +
                        " P.PACK_DESC,\n" +
                        " S.SERVICE_DESC,\n" +
                        " S.STATUS,\n" +
                        " S.QTY,\n" +
                        " S.EXEC_SEQ_OPER\n" +
                        "\n" +
                        " from SM_SO_Packs as P inner join SM_SO_Services as S on P.customer_code = S.customer_code and \n" +
                        " P.so_prefix = S.so_prefix and \n" +
                        " P.so_code = S.so_code and \n" +
                        " P.price_list_code = S.price_list_code and \n" +
                        " P.pack_code =  S.pack_code and \n" +
                        " P.pack_seq = S.pack_seq\n" +
                        "\n" +
                        " WHERE\n" +
                        "    S.customer_code =              '" + customer_code + "'\n" +
                        "    AND S.so_prefix =              '" + so_prefix + "'\n" +
                        "    AND S.so_code =                '" + so_code + "' ")
                //"    AND S.price_list_code =        '" + price_list_code + "'\n" +
                //"    AND S.pack_code =              '" + pack_code + "'\n" +
                //"    AND S.pack_seq =               '" + pack_seq + "' ")
                .append(" ORDER BY \n")
                .append(" " + SM_SO_ServiceDao.EXEC_SEQ_OPER)
                .append(";")
                .append("customer_code#so_prefix#so_code#price_list_code#pack_code#pack_seq#category_price_code#service_code#service_seq#price_list_id#price_list_desc#pack_id#pack_desc#service_desc#status#qty#exec_seq_oper")
                .toString();
    }
}
