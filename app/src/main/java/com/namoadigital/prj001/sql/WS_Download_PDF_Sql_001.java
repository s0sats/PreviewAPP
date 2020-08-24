package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.TK_Ticket_FormDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ConstantBaseApp;

/**
 * Created by dluche on 24/08/2020.
 *
 * Query q seleciona os ctrl tipo form que não possuem PDF baixado.
 *
 */

public class WS_Download_PDF_Sql_001 implements Specification {
    public static final String FILE_LOCAL_NAME = "FILE_NAME";

    private long customer_code;

    public WS_Download_PDF_Sql_001(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "    f.customer_code,\n" +
                        "    f.ticket_prefix,\n" +
                        "    f.ticket_code,\n" +
                        "    f.ticket_seq,\n" +
                        "    f.ticket_seq_tmp,\n" +
                        "    f.step_code,\n" +
                        "    f.pdf_url,\n" +
                        "    '"+ ConstantBaseApp.N_FORM_PDF_PREFIX +"'||f.customer_code||'_'||f.custom_form_type||'_'||f.custom_form_code||'_'||f.custom_form_version||'_'||f.custom_form_data\n "+FILE_LOCAL_NAME+" " +
                        " FROM\n" +
                        "   " +TK_Ticket_FormDao.TABLE +"  f\n" +
                        " WHERE\n" +
                        "   f.customer_code = '"+customer_code+"' \n" +
                        "   and f.pdf_url is not null \n" +
                        "   and f.pdf_url_local is null \n")
                .append(";")
                .toString();
    }
}
