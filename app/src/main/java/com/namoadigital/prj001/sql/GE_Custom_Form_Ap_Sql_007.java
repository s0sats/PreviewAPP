package com.namoadigital.prj001.sql;

import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.database.Specification;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 01/03/2018.
 * <p>
 * Seleciona todas os pdf pendentes de download do ap.
 *
 * OBS: O nome do arquivo esta utilizando a pk do form data, pois todos os form_ap de uma mesma
 * "resposta"(custom_form_data) comparatilham o mesmo pdf.
 */

public class GE_Custom_Form_Ap_Sql_007 implements Specification {

    public static final String FILE_LOCAL_NAME = "FILE_NAME";

    private long customer_code;
    private String HmAuxFields = ToolBox_Inf.getColumnsToHmAux(GE_Custom_Form_ApDao.columns);

    public GE_Custom_Form_Ap_Sql_007(long customer_code) {
        this.customer_code = customer_code;
    }

    @Override
    public String toSqlQuery() {
        StringBuilder sb = new StringBuilder();

        return sb
                .append(" SELECT\n" +
                        "       a.*,\n" +
                        "       'form_ap_' || a.customer_code ||'_'|| \n" +
                        "       a.custom_form_type ||'_'|| \n" +
                        "       a.custom_form_code ||'_'|| \n" +
                        "       a.custom_form_version ||'_'|| \n" +
                        "       a.custom_form_data "+FILE_LOCAL_NAME+"\n" +
                        " FROM\n" +
                            GE_Custom_Form_ApDao.TABLE +"  a\n" +
                        " WHERE\n" +
                        "     a.customer_code = '"+customer_code+"'\n" +
                        "     and a.custom_form_url is not null\n" +
                        "     and a.custom_form_url_local = '' \n" +
                        " GROUP BY\n" +
                        "       a.customer_code,\n" +
                        "       a.custom_form_type ,\n" +
                        "       a.custom_form_code , \n" +
                        "       a.custom_form_version ,\n" +
                        "       a.custom_form_data" +
                        " ORDER BY\n" +
                        "       a.customer_code,\n" +
                        "       a.custom_form_type,\n" +
                        "       a.custom_form_code,\n" +
                        "       a.custom_form_version,\n" +
                        "       a.custom_form_data\n")
                .append(";")
                .append(HmAuxFields+"#"+FILE_LOCAL_NAME)
                .toString();
    }
}
