package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_FormDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by dluche on 24/08/2020.
 * Query q atualiza o nome do PDF baixado
 */

public class WS_Download_PDF_Sql_002 implements Specification {
    private String customer_code;
    private String ticket_prefix;
    private String ticket_code;
    private String ticket_seq;
    private String ticket_seq_tmp;
    private String step_code;
    private String pdf_url_local;

    public WS_Download_PDF_Sql_002(String customer_code, String ticket_prefix, String ticket_code, String ticket_seq, String ticket_seq_tmp, String step_code, String pdf_url_local) {
        this.customer_code = customer_code;
        this.ticket_prefix = ticket_prefix;
        this.ticket_code = ticket_code;
        this.ticket_seq = ticket_seq;
        this.ticket_seq_tmp = ticket_seq_tmp;
        this.step_code = step_code;
        this.pdf_url_local = pdf_url_local;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(" UPDATE "+TK_Ticket_FormDao.TABLE+" SET \n" +
                        "   pdf_url_local = '"+pdf_url_local+"'\n" +
                        " WHERE\n" +
                        "   customer_code = '"+customer_code+"' \n" +
                        "   and ticket_prefix ='"+ticket_prefix+"'\n" +
                        "   and ticket_code ='"+ticket_code+"'\n" +
                        "   and ticket_seq ='"+ticket_seq+"'\n" +
                        "   and ticket_seq_tmp='"+ticket_seq_tmp+"'\n" +
                        "   and step_code='"+step_code+"'\n")
                .append(";")
                .toString();
    }
}
