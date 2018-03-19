package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;

/**
 * Created by neomatrix on 07/03/18.
 * Verifica se existem outros AP usando aquele PDF.
 */

public class WS_Cleaning_Sql_007 implements Specification {
    public static final String USING_PDF_QTY = "USING_PDF_QTY";

    private long customer_code;
    private String ap_pdf_name;

    public WS_Cleaning_Sql_007(long customer_code, String ap_pdf_name) {
        this.customer_code = customer_code;
        this.ap_pdf_name = ap_pdf_name;
    }

    @Override
    public String toSqlQuery() {

        StringBuilder sb = new StringBuilder();

        return sb
                .append("SELECT\n" +
                        "  count(1) "+USING_PDF_QTY+"\n" +
                        "FROM\n" +
                        GE_Custom_Form_ApDao.TABLE +" a\n" +
                        "WHERE\n" +
                        "  a.customer_code = '"+customer_code+"'\n" +
                        "  and a.custom_form_url_local = '"+ap_pdf_name+"'")
                .append(";"+USING_PDF_QTY)
                .toString();
    }
}
