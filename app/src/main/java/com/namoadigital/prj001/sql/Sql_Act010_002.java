package com.namoadigital.prj001.sql;

import static com.namoadigital.prj001.ui.act010.Act010_Main.CUSTOM_PK;
import static com.namoadigital.prj001.ui.act010.Act010_Main.IS_FORM;

import com.namoadigital.prj001.dao.TkTicketTypeDao;
import com.namoadigital.prj001.dao.TkTicketTypeOperationDao;
import com.namoadigital.prj001.dao.TkTicketTypeProductDao;
import com.namoadigital.prj001.dao.TkTicketTypeSiteDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.ui.act010.Act010_Main;

public class Sql_Act010_002 implements Specification {

    private long s_customer_code;
    private int s_tag_code;
    private String s_product_code;
    private long s_operation_code;
    private String s_site_code;
    private String s_serial_id;

    public Sql_Act010_002(long s_customer_code, int s_tag_code, String s_product_code, long s_operation_code, String s_site_code, String s_serial_id) {
        this.s_customer_code = s_customer_code;
        this.s_tag_code = s_tag_code;
        this.s_product_code = s_product_code;
        this.s_operation_code = s_operation_code;
        this.s_site_code = s_site_code;
        this.s_serial_id = s_serial_id.trim().length() != 0 ? s_serial_id.trim()  : "null";
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb =  new StringBuilder();

        return  sb.append(
                " SELECT\n" +
                        "      tp."+ TkTicketTypeDao.CUSTOMER_CODE+",\n" +
                        "      tp."+ TkTicketTypeDao.TAG_OPERATIONAL_CODE+",\n" +
                        "      tp."+ TkTicketTypeDao.TICKET_TYPE_CODE+",\n" +
                        "      tp."+ TkTicketTypeDao.TICKET_TYPE_ID+",\n" +
                        "      tp."+ TkTicketTypeDao.TICKET_TYPE_DESC+ " " + Act010_Main.CUSTOM_DESC+ ",\n" +
                        "      tp."+TkTicketTypeDao.TAG_OPERATIONAL_CODE +"||'.'||" +
                        "      tp."+ TkTicketTypeDao.TICKET_TYPE_ID+" " + CUSTOM_PK+",\n" +
                        "       0 " + IS_FORM+"\n" +
                        "    FROM\n" +
                        "       " +     TkTicketTypeDao.TABLE +" tp \n" +
                        " LEFT JOIN\n" +
                        "       "+ TkTicketTypeProductDao.TABLE +" p on p.customer_code = tp.customer_code \n" +
                        "                                           and p.ticket_type_code = tp.ticket_type_code \n"+
                        "                                           and p.product_code = '"+s_product_code+"'\n" +
                        " LEFT JOIN\n" +
                        "       "+ TkTicketTypeOperationDao.TABLE +" o on o.customer_code = tp.customer_code\n" +
                        "                               and o.ticket_type_code = tp.ticket_type_code\n" +
                        "                               and o.operation_code = '"+s_operation_code+"' \n "+
                        " LEFT JOIN\n" +
                        "    "+ TkTicketTypeSiteDao.TABLE +" s on s.customer_code = tp.customer_code\n" +
                        "                               and s.ticket_type_code = tp.ticket_type_code\n" +
                        "                               and s.site_code = '"+s_site_code+"' \n"+
                        "    WHERE\n" +
                        "      tp."+TkTicketTypeDao.CUSTOMER_CODE+" = '" + s_customer_code + "'\n" +
                        "      AND tp."+TkTicketTypeDao.TAG_OPERATIONAL_CODE+" = '" + s_tag_code +"'\n" +
                        "      AND (TP.all_product = 1 OR p.product_code = '"+s_product_code+"')\n" +
                        "      AND (TP.all_operation = 1 OR o.operation_code = '"+s_operation_code+"') \n" +
                        "      AND (TP.all_site = 1 OR s.site_code = '"+s_site_code+"')\n"+
                        "      AND ( '"+s_serial_id+"' IS NOT NULL)\n"+
                        "    \n" +
                        "    ORDER BY\n" +
                        "      upper(" + TkTicketTypeDao.TICKET_TYPE_DESC + ") \n;"
        )
                //GE_Custom_FormDao.CUSTOMER_CODE+"#"+GE_Custom_FormDao.CUSTOM_FORM_TYPE+"#"+GE_Custom_FormDao.CUSTOM_FORM_CODE+"#"+GE_Custom_FormDao.CUSTOM_FORM_VERSION+"#"+GE_Custom_FormDao.CUSTOM_FORM_DESC)
                .toString()
                .replace("'null'","null");
    }
}
